# --------------------------------------------------------------------------------
# [파이썬 2일차 Codelab ① - 병렬 데이터 마스킹 및 텍스트 정규화(Normalization)]
#
# 작성자(author): 장아현
# 작성일자(date): 2026.03.28
# 변경내역(description): 100만건의 리뷰 csv에서 개인정보를 마스킹하고 금칙어를 표준 표현으로 순화
# --------------------------------------------------------------------------------
# --------------------------------------------------------------------------------
#   실습 가이드
#   - 수백만 건의 사용자 리뷰 데이터 내에 포함된 이메일 주소, 전화번호 등의 개인정보를 마스킹(****) 처리하고,
#     특정 금칙어를 표준 표현으로 순화하는 배치 프로세서를 구축
#   핵심 포인트
#   - 정규표현식(Regex)의 오버헤드
#       복잡한 정규표현식은 CPU 자원을 많이 소모
#       이를 멀티 프로세스로 분산했을 때의 성능 이득을 확인
#   - 데이터 청킹(Chunking)
#       100만 건의 데이터를 리스트 하나로 처리할 때와
#       코어 수만큼 나눠서 병렬 처리할 때의 처리량(Throughput) 차이를 분석
#   - IPC 비용
#       데이터가 너무 작으면 프로세스 간 통신(IPC) 비용이
#       실행 시간보다 커질 수 있음을 이해
# --------------------------------------------------------------------------------

from __future__ import annotations

import csv
import multiprocessing as mp
import re
import time
from dataclasses import dataclass
from typing import Dict, List


# ---------------------------
# 정규표현식 패턴 (Regex)
# ---------------------------
# 이메일: group(1)=로컬파트, group(2)=도메인
EMAIL_PATTERN = re.compile(r"([a-zA-Z0-9_.+-]+)(@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+)")

# 전화번호: group(1)=앞번호, group(2)=가운데, group(3)=끝번호
# \b 대신 숫자 경계 lookaround 사용 (한글이 \w로 인식돼 \b가 안 먹히는 문제 방지)
PHONE_PATTERN = re.compile(r"(?<!\d)(\d{2,3})-(\d{3,4})-(\d{4})(?!\d)")


# ---------------------------
# 금칙어 -> 표준 표현 매칭하기
# ---------------------------
BAD_WORD_MAP: Dict[str, str] = {
    "존나": "매우",
    "개판": "엉망",
    "쓰레기": "불량",
    "병신": "불량품",
    "최악": "불만족",
    "망했음": "실망함",
    "답없음": "개선필요",
    "개같음": "불쾌함",
    "별로임": "미흡함",
    "에바임": "부적절함",
    "헬임": "매우힘듦",
    "노답": "개선필요",
    "극혐": "매우불쾌",
}


# ---------------------------
# 데이터 구조 정의
# ---------------------------
@dataclass
class Review:
    name: str
    phone: str
    email: str
    review: str


# ---------------------------
# 처리 함수
# ---------------------------
def mask_pii(text: str) -> str:
    """이메일: @ 앞부분만 글자수만큼 * 처리 / 전화번호: 가운데·끝 구간만 * 처리"""
    # 이메일: abc@example.com -> ***@example.com (group(2)가 @ 포함이므로 따로 붙이지 않음)
    text = EMAIL_PATTERN.sub(
        lambda m: "*" * len(m.group(1)) + m.group(2), text
    )
    # 전화번호: 010-1234-5678 -> 010-****-****
    text = PHONE_PATTERN.sub(
        lambda m: m.group(1) + "-" + "*" * len(m.group(2)) + "-" + "*" * len(m.group(3)),
        text,
    )
    return text


def mask_name(name: str) -> str:
    """이름 가운데 글자 * 처리: 첫글자·끝글자 유지"""
    if len(name) <= 2:
        return name[0] + "*"
    return name[0] + "*" * (len(name) - 2) + name[-1]


def normalize_bad_words(text: str) -> str:
    """금칙어를 표준 표현으로 치환"""
    for bad, good in BAD_WORD_MAP.items():
        text = text.replace(bad, good)
    return text


def process_row(row: Review) -> Review:
    """단일 행 처리: 리뷰 마스킹 + 정규화, 개인정보 컬럼도 마스킹"""
    review = mask_pii(row.review)
    review = normalize_bad_words(review)

    return Review(
        name=mask_name(row.name),
        phone=mask_pii(row.phone),
        email=mask_pii(row.email),
        review=review,
    )


def process_chunk(rows: List[Review]) -> List[Review]:
    """청크 단위 처리 (멀티프로세스 작업 단위)"""
    return [process_row(row) for row in rows]


# ---------------------------
# CSV 로드 / 저장
# ---------------------------
def load_csv(path: str) -> List[Review]:
    with open(path, encoding="utf-8") as f:
        reader = csv.DictReader(f)
        return [
            Review(
                name=row["이름"],
                phone=row["전화번호"],
                email=row["이메일"],
                review=row["리뷰"],
            )
            for row in reader
        ]


def save_csv(rows: List[Review], path: str) -> None:
    fieldnames = ["이름", "전화번호", "이메일", "리뷰"]
    with open(path, "w", newline="", encoding="utf-8") as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        for row in rows:
            writer.writerow({
                "이름": row.name,
                "전화번호": row.phone,
                "이메일": row.email,
                "리뷰": row.review,
            })


# ---------------------------
# 단일 프로세스 처리
# ---------------------------
def run_single(rows: List[Review]):
    start = time.time()
    result = process_chunk(rows)
    elapsed = time.time() - start
    return result, elapsed


# ---------------------------
# 멀티 프로세스 처리 (청킹)
# ---------------------------
def run_parallel(rows: List[Review]):
    num_cores = mp.cpu_count()

    # 코어 수만큼 청크로 나누기
    chunk_size = len(rows) // num_cores
    chunks = [rows[i : i + chunk_size] for i in range(0, len(rows), chunk_size)]

    start = time.time()
    with mp.Pool(num_cores) as pool:
        results = pool.map(process_chunk, chunks)
    elapsed = time.time() - start

    # 청크 결과 합치기
    flat = [row for chunk in results for row in chunk]
    return flat, elapsed


# ---------------------------
# 메인
# ---------------------------
if __name__ == "__main__":
    INPUT_FILE = "fake_reviews.csv"
    OUTPUT_SINGLE = "masked_single.csv"
    OUTPUT_PARALLEL = "masked_parallel.csv"

    print("CSV 로딩 중...")
    rows = load_csv(INPUT_FILE)
    print(f"{len(rows):,}건 로드 완료\n")

    # --- 단일 프로세스 ---
    print("[단일 프로세스] 처리 중...")
    result_single, t_single = run_single(rows)
    tput_single = len(rows) / t_single
    print(f"  처리 시간: {t_single:.2f}초")
    print(f"  처리량  : {tput_single:,.0f} rows/s")

    save_csv(result_single, OUTPUT_SINGLE)
    print(f"  저장 완료: {OUTPUT_SINGLE}\n")

    # --- 멀티 프로세스 ---
    num_cores = mp.cpu_count()
    print(f"[멀티 프로세스 - {num_cores}코어] 처리 중...")
    result_parallel, t_parallel = run_parallel(rows)
    tput_parallel = len(rows) / t_parallel
    print(f"  처리 시간: {t_parallel:.2f}초")
    print(f"  처리량  : {tput_parallel:,.0f} rows/s")

    save_csv(result_parallel, OUTPUT_PARALLEL)
    print(f"  저장 완료: {OUTPUT_PARALLEL}\n")

    # --- 결과 비교 ---
    print("=" * 40)
    print(f"단일 프로세스 처리량: {tput_single:>12,.0f} rows/s")
    print(f"멀티 프로세스 처리량: {tput_parallel:>12,.0f} rows/s")
    print(f"속도 향상(speedup)  : {t_single / t_parallel:.2f}x")
    print("=" * 40)

    # --- IPC 비용 관찰: 소량 데이터(1,000건) ---
    # 데이터가 너무 작으면 프로세스 생성 + 직렬화/역직렬화 비용이 처리 시간보다 커진다
    print("\n[IPC 비용 관찰 - 소량 데이터 1,000건]")
    small_rows = rows[:1000]
    _, t_small_single = run_single(small_rows)
    _, t_small_parallel = run_parallel(small_rows)
    print(f"  단일 프로세스: {t_small_single:.4f}초")
    print(f"  멀티 프로세스: {t_small_parallel:.4f}초")
    if t_small_parallel > t_small_single:
        print("  → IPC 비용으로 인해 멀티프로세스가 오히려 더 느리다")
    else:
        print("  → 소량에서도 멀티프로세스가 빠르다")

