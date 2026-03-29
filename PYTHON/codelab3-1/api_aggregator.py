# --------------------------------------------------------------------------------
# [파이썬 3일차 Codelab ① - asyncio 기반 고성능 API Aggregator 구축]
#
# 작성자(author): 장아현
# 작성일자(date): 2026.03.29
# 변경내역(description): 여러 마이크로서비스 API에서 데이터를 동시에 가져오는
#                      API Aggregator를 asyncio로 구현하고 동기 방식과 성능 비교
# --------------------------------------------------------------------------------
# --------------------------------------------------------------------------------
#   실습 가이드
#   - 여러 마이크로서비스(API)에서 데이터를 동시에 가져오는 시나리오를 asyncio.gather로 구현
#   - 동기 방식(Sequential) 대비 비동기 방식(Concurrent)의 응답 시간(Latency) 이득을 측정
# --------------------------------------------------------------------------------

import asyncio
import json
import time
import urllib.request


# -------------------------------------
# 마이크로서비스 API 목록 (JSONPlaceholder)
# -------------------------------------
# 각 엔드포인트를 별개의 마이크로서비스라고 가정하고 사용
SERVICES = {
    "user-service":    "https://jsonplaceholder.typicode.com/users/3",
    "post-service":    "https://jsonplaceholder.typicode.com/posts?userId=3",
    "album-service":   "https://jsonplaceholder.typicode.com/albums?userId=3",
    "todo-service":    "https://jsonplaceholder.typicode.com/todos?userId=3",
    "comment-service": "https://jsonplaceholder.typicode.com/comments?postId=3",
}


# -------------------------------------
# 동기 방식 (Sequential) - urllib
# -------------------------------------
def fetch_sync(service_name: str, url: str) -> dict:
    start = time.time()
    with urllib.request.urlopen(url) as response:
        data = json.loads(response.read().decode())
    latency = time.time() - start

    count = len(data) if isinstance(data, list) else 1

    return {
        "service": service_name,
        "status": response.status,
        "latency": round(latency, 3),
        "count": count,
    }


def run_sequential() -> tuple[list, float]:
    results = []
    start = time.time()

    for service, url in SERVICES.items():
        result = fetch_sync(service, url)
        results.append(result)
        print(f"  [{result['service']}] {result['count']}건 수신 ({result['latency']}s)")

    elapsed = time.time() - start
    return results, elapsed


# -------------------------------------
# 비동기 방식 (Concurrent)
# - asyncio.gather + run_in_executor
# -------------------------------------
# urllib은 동기라서 async def 안에서 그냥 쓰면 블로킹이 걸림
# run_in_executor로 스레드풀에 넘겨야 비동기처럼 동작함
async def fetch_async(service_name: str, url: str) -> dict:
    loop = asyncio.get_event_loop()
    # fetch_sync를 스레드풀에서 실행해 블로킹 없이 동작하게 함
    result = await loop.run_in_executor(None, fetch_sync, service_name, url)
    return result


async def run_concurrent() -> tuple[list, float]:
    start = time.time()

    # 모든 서비스를 동시에 호출하고 결과를 한꺼번에 받음
    results = await asyncio.gather(
        *[fetch_async(service, url) for service, url in SERVICES.items()]
    )

    elapsed = time.time() - start

    for result in results:
        print(f"  [{result['service']}] {result['count']}건 수신 ({result['latency']}s)")

    return list(results), elapsed


# -------------------------------------
# 메인
# -------------------------------------
if __name__ == "__main__":
    print("=" * 55)
    print(f"호출 대상 서비스: {len(SERVICES)}개 (JSONPlaceholder)")
    print("=" * 55)

    # --- 동기 방식 ---
    print("\n[1] 동기 방식 (Sequential) - urllib")
    _, t_seq = run_sequential()
    print(f"  총 소요 시간: {t_seq:.2f}초\n")

    # --- 비동기 방식 ---
    print("[2] 비동기 방식 (Concurrent) - asyncio.gather + run_in_executor")
    _, t_con = asyncio.run(run_concurrent())
    print(f"  총 소요 시간: {t_con:.2f}초\n")

    # --- 결과 비교 ---
    print("=" * 55)
    print(f"동기 방식 소요 시간  : {t_seq:.2f}초")
    print(f"비동기 방식 소요 시간: {t_con:.2f}초")
    print(f"응답 시간 단축       : {t_seq - t_con:.2f}초 ({t_seq / t_con:.1f}배 빠름)")
    print("=" * 55)
