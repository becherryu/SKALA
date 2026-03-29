# --------------------------------------------------------------------------------
# [파이썬 3일차 Codelab ② - 지수 백오프 기반 무차별 대입 공격 차단]
#
# 작성자(author): 장아현
# 작성일자(date): 2026.03.29
# 변경내역(description): bcrypt로 비밀번호를 검증하고, IP별 실패 횟수를 메모리에 기록해
#                       연속 실패 시 지수 백오프(Exponential Backoff)로 로그인을 차단
# --------------------------------------------------------------------------------
# --------------------------------------------------------------------------------
#   실습 가이드
#   - bcrypt를 사용하되, 메모리(Dictionary)에 IP별 실패 횟수를 기록
#   - 연속 실패 시 지수 백오프(Exponential Backoff)를 적용하여
#     무차별 대입 공격을 물리적으로 차단하는 알고리즘을 설계
# --------------------------------------------------------------------------------

import bcrypt
import time
from datetime import datetime


# ---------------------------
# 비밀번호 해싱
# ---------------------------
# 실제 서비스에서는 DB에 평문이 아닌 해시값을 저장
RAW_PASSWORD = "mySecurePassword123!"
HASHED_PASSWORD = bcrypt.hashpw(RAW_PASSWORD.encode(), bcrypt.gensalt())


# ---------------------------
# IP별 실패 기록 (메모리)
# ---------------------------
# { ip: { "attempts": int, "blocked_until": float } }
ip_tracker: dict = {}

MAX_ATTEMPTS = 5  # 이 횟수 초과 시 차단 시작


# ---------------------------
# 지수 백오프 대기 시간 계산
# ---------------------------
def get_backoff_seconds(attempts: int) -> float:
    # 실패 횟수에 따라 대기 시간이 2배씩 늘어남
    # 5회: 1초, 6회: 2초, 7회: 4초, 8회: 8초 ...
    return 2 ** (attempts - MAX_ATTEMPTS)


# ---------------------------
# 로그인 시도
# ---------------------------
def login(ip: str, password: str) -> bool:
    now = time.time()

    # IP 첫 접근이면 초기화
    if ip not in ip_tracker:
        ip_tracker[ip] = {"attempts": 0, "blocked_until": 0}

    record = ip_tracker[ip]

    # 차단 시간이 아직 안 지났으면 거부
    if now < record["blocked_until"]:
        remaining = record["blocked_until"] - now
        print(f"  [차단] {ip} - {remaining:.1f}초 후 재시도 가능 (대기 중 재시도는 무시됨)")
        return False

    # 비밀번호 검증 (bcrypt)
    is_correct = bcrypt.checkpw(password.encode(), HASHED_PASSWORD)

    if is_correct:
        # 로그인 성공 → 실패 기록 초기화
        ip_tracker[ip] = {"attempts": 0, "blocked_until": 0}
        print(f"  [성공] {ip} - 로그인 성공 ✓")
        return True
    else:
        # 로그인 실패 → 실패 횟수 증가 + 백오프 적용
        record["attempts"] += 1
        attempts = record["attempts"]

        if attempts >= MAX_ATTEMPTS:
            wait = get_backoff_seconds(attempts)
            record["blocked_until"] = now + wait
            print(f"  [실패] {ip} - {attempts}회 연속 실패 → {wait:.0f}초 차단")
        else:
            print(f"  [실패] {ip} - {attempts}회 실패 ({MAX_ATTEMPTS}회 초과 시 차단 시작)")

        return False


# ---------------------------
# 메인
# ---------------------------
if __name__ == "__main__":
    # --- 정상 로그인 시나리오 ---
    print("=" * 55)
    print("시나리오 1. 정상 로그인")
    print("=" * 55)
    login("192.168.0.1", "wrongPassword")   # 실패
    login("192.168.0.1", RAW_PASSWORD)      # 성공

    # --- 무차별 대입 공격 시나리오 ---
    print()
    print("=" * 55)
    print("시나리오 2. 무차별 대입 공격 (192.168.0.99)")
    print("=" * 55)

    attacker_ip = "192.168.0.99"

    for i in range(1, 11):
        print(f"\n  [시도 {i}회]")
        login(attacker_ip, f"guess{i}")

        # 차단 상태라면 차단이 풀린 직후 시도로 시뮬레이션
        # (실제 대기 없이 blocked_until만 초기화해 알고리즘 동작 확인)
        if attacker_ip in ip_tracker:
            record = ip_tracker[attacker_ip]
            if record["blocked_until"] > time.time():
                wait = record["blocked_until"] - time.time()
                print(f"  → 공격자가 {wait:.0f}초 대기 후 재시도한다고 가정")
                record["blocked_until"] = 0  # 시뮬레이션용: 대기 시간 건너뜀

    # --- 백오프 테이블 ---
    print()
    print("=" * 55)
    print("지수 백오프 대기 시간 테이블")
    print("=" * 55)
    print(f"  {'실패 횟수':>8} | {'대기 시간':>10}")
    print(f"  {'-'*8}-+-{'-'*10}")
    for n in range(MAX_ATTEMPTS, MAX_ATTEMPTS + 6):
        print(f"  {n:>8}회 | {get_backoff_seconds(n):>8.0f}초")

    # --- IP별 최종 기록 ---
    print()
    print("=" * 55)
    print("IP별 최종 실패 기록")
    print("=" * 55)
    for ip, record in ip_tracker.items():
        print(f"  {ip} | 누적 실패: {record['attempts']}회")
