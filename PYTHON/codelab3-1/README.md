# [Codelab 3-1] asyncio 기반 고성능 API Aggregator 구축

## 요구사항

- 가이드
  - 여러 마이크로서비스(API)에서 데이터를 동시에 가져오는 시나리오를 `asyncio.gather`로 구현
  - 동기 방식(Sequential) 대비 비동기 방식(Concurrent)의 응답 시간(Latency) 이득을 측정
- 라이브러리
  - `import asyncio`
  - `import urllib.request`
  - `import json`

## 핵심 개념

### 동기 방식 vs 비동기 방식

동기 방식은 API를 하나씩 순서대로 호출하기 때문에 전체 시간이 각 응답 시간의 합이 된다.
비동기 방식은 모든 API를 동시에 호출해서 가장 느린 API 하나의 응답 시간으로 줄어든다.

### asyncio.gather

`asyncio.gather`에 비동기 함수들을 넘기면 동시에 실행된다.

```python
results = await asyncio.gather(
    fetch_async("user-service", url1),
    fetch_async("post-service", url2),
    fetch_async("todo-service", url3),
)
```

### run_in_executor

`urllib`은 동기 라이브러리라 `async def` 안에서 그냥 쓰면 블로킹이 걸린다.
`run_in_executor`로 스레드풀에 넘기면 비동기처럼 동작시킬 수 있다.

```python
async def fetch_async(service_name, url):
    loop = asyncio.get_event_loop()
    result = await loop.run_in_executor(None, fetch_sync, service_name, url)
    return result
```

## 실행 결과

```
=======================================================
호출 대상 서비스: 5개 (JSONPlaceholder)
=======================================================

[1] 동기 방식 (Sequential) - urllib
  [user-service] 1건 수신 (0.579s)
  [post-service] 10건 수신 (0.7s)
  [album-service] 10건 수신 (0.702s)
  [todo-service] 20건 수신 (0.67s)
  [comment-service] 5건 수신 (0.678s)
  총 소요 시간: 3.33초

[2] 비동기 방식 (Concurrent) - asyncio.gather + run_in_executor
  [user-service] 1건 수신 (0.017s)
  [post-service] 10건 수신 (0.016s)
  [album-service] 10건 수신 (0.023s)
  [todo-service] 20건 수신 (0.017s)
  [comment-service] 5건 수신 (0.002s)
  총 소요 시간: 0.03초

=======================================================
동기 방식 소요 시간  : 3.33초
비동기 방식 소요 시간: 0.03초
응답 시간 단축       : 3.30초 (127.4배 빠름)
=======================================================
```

## 파일 구성

```
.
└── api_aggregator.py   # 동기/비동기 API Aggregator 구현 및 성능 비교
```
