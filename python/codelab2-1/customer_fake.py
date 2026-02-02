# --------------------------------------------------------------------------------
# [파이썬 2일차 Codelab ① - 병렬 데이터 마스킹 및 텍스트 정규화(Normalization)]
#
# 작성자(author): 장아현
# 작성일자(date): 2026.01.26
# 변경내역(description): 100만 건의 가짜 사용자 데이터(이름, 전화번호, 이메일, 리뷰)를 만들어내는 파일
# ---------------------------------------------------------------------------------

from faker import Faker  # 가짜 데이터를 생성하기 위한 라이브러리
import random
import csv

fake = Faker("ko_KR")

TOTAL_ROWS = 1_000_000
OUTPUT_FILE = "fake_reviews.csv"

# 금칙어 목록 (정규화 대상)
BAD_WORDS = [
    "존나",
    "개판",
    "쓰레기",
    "병신",
    "최악",
    "망했음",
    "답없음",
    "개같음",
    "별로임",
    "에바임",
    "헬임",
    "노답",
    "극혐",
]


# 리뷰 템플릿 (개인정보 + 금칙어 혼합)
REVIEW_TEMPLATES = [
    # 부정적 후기 + 금칙어
    "배송은 빠른데 제품 상태가 {bad}임",
    "솔직히 말해서 이건 좀 {bad}",
    "기대했는데 결과가 {bad}",
    "다시는 안 살 듯, 너무 {bad}",
    "가격 대비 완전 {bad}",
    # 개인정보 포함
    "문제 있으면 {phone}으로 연락 주세요",
    "이메일은 {email} 입니다",
    "상담은 {email} 또는 {phone}으로 부탁드립니다",
    "문의는 {email}로 남겨주세요",
    "연락처 {phone}으로 연락 바랍니다",
    # 혼합형
    "제품이 {bad}라서 {phone}로 항의함",
    "이거 {bad}임, 메일은 {email}",
    "진짜 {bad}라서 고객센터({phone})에 문의함",
    "품질이 {bad}, 이메일 {email}로 문의함",
    # 중립 / 긍정
    "전체적으로 무난한 편입니다",
    "배송과 포장은 만족스럽습니다",
    "가격 대비 괜찮은 제품입니다",
]


# 개인정보와 금칙어가 섞인 리뷰 생성하기
def generate_review():
    template = random.choice(REVIEW_TEMPLATES)

    return template.format(
        bad=random.choice(BAD_WORDS), phone=fake.phone_number(), email=fake.email()
    )


with open(OUTPUT_FILE, "w", newline="", encoding="utf-8") as f:
    writer = csv.writer(f)
    writer.writerow(["이름", "전화번호", "이메일", "리뷰"])

    for _ in range(TOTAL_ROWS):
        writer.writerow(
            [
                fake.name(),
                fake.phone_number(),
                fake.email(),
                generate_review(),
            ]
        )

print(f"{TOTAL_ROWS:,}건의 가짜 데이터 생성 완료\n 파일 이름: {OUTPUT_FILE}")
