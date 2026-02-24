# -----------------------------------------------------------------
# [파이썬 2일차 실습 3 - multiprocessing으로 대용량 데이터 처리]
#
# 작성자(author): 장아현
# 작성일자(date): 2026.01.26
# 변경내역(description): 난수 생성 후 각 숫자가 소수인지 판별하는 함수를 작성하고
#                      단일프로세스 방식과 병렬 처리 방식의 시간을 비교한다.
# -----------------------------------------------------------------
# -----------------------------------------------------------------
# [11. 병렬처리]
#   1,000만 개의 난수를 생성한 후, 각 숫자가 소수인지 판별하는 작업을
#   단일 프로세스와 멀티 프로세스로 나누어 처리 시간을 비교
# 요구사항
# -	random을 사용하여 1,000만 개의 1~10,000,000 사이 정수 리스트를 생성하세요.
# -	숫자가 소수인지 판별하는 함수를 구현하세요.
# -	아래 두 가지 방식으로 소수의 개수를 세고 처리 시간을 비교하세요
# 		(a) 단일 프로세스 방식
# 		(b) multiprocessing.Pool 사용 (병렬 처리)
# -	처리 시간과 소수 개수를 출력하세요.
# -----------------------------------------------------------------

import random
import time
from multiprocessing import Pool, cpu_count


# 숫자가 소수인지 판별하는 함수 - 1과 자기 자신이 약수인 수
def isPrime(num):
    if num < 2:
        return False

    for i in range(2, int(num**0.5) + 1):
        if num % i == 0:
            return False
    return True


# random을 활용해 1,000만 개의 정수 리스트 생성
arr = [random.randint(1, 10_000_000) for _ in range(10_000_000)]

if __name__ == "__main__":
    # (a) 단일 프로세스 방식
    single_start = time.time()
    single_cnt = sum(1 for n in arr if isPrime(n))
    single_end = time.time()

    print("단일 프로세스 방식일 때")
    print(f"소수 개수 : {single_cnt} 개")
    print(f"처리 시간 : {single_end - single_start:.4f} seconds\n")

    # (b) multiprocessing.Pool 사용 (병렬 처리)
    multi_start = time.time()

    # cpu의 코어 개수만큼(8개) 프로세스 생성
    with Pool(cpu_count()) as pool:
        result = pool.map(isPrime, arr)

    multi_cnt = sum(result)
    multi_end = time.time()

    print("multiprocessing.Pool 사용 (병렬 처리)일 때")
    print(f"소수 개수 : {multi_cnt} 개")
    print(f"처리 시간 : {multi_end - multi_start:.4f} seconds")
