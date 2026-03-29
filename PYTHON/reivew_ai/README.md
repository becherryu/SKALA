# 고객 리뷰 유사도 검색

리뷰 문장을 벡터로 변환하고, PostgreSQL pgvector로 비슷한 리뷰를 찾는 실습 프로젝트입니다.

## 사용 기술

- **Python** 3.12
- **SentenceTransformers** - `paraphrase-MiniLM-L6-v2` (384차원)
- **PostgreSQL** + **pgvector**
- **psycopg2**

## 파일 구조

```text
reivew_ai/
├── review_ai.ipynb   # 메인 노트북
├── .env              # DB 연결 정보 (git 제외)
├── pyproject.toml    # 의존성
└── uv.lock
```

## 구현 내용

### 리뷰 데이터

```python
reviews = [
    "배송이 빠르고 제품도 좋아요.",
    "품질이 기대 이상입니다!",
    "생각보다 배송이 오래 걸렸어요.",
    "배송은 느렸지만 포장은 안전했어요.",
    "아주 만족스러운 제품입니다."
]
```

### 유사도 검색 쿼리

`<=>` 는 pgvector의 코사인 거리 연산자로, 값이 작을수록 더 유사합니다.

```sql
SELECT review, embedding <=> %s::vector AS distance
FROM review_vectors
ORDER BY embedding <=> %s::vector
LIMIT 3;
```

### 실행 결과

```text
쿼리: '배송이 느렸어요'
=============================================
  순위 |  코사인 거리 | 리뷰
  ----+------------+--------------------
     1위 |     0.0783 | 배송이 빠르고 제품도 좋아요.
     2위 |     0.0990 | 배송은 느렸지만 포장은 안전했어요.
     3위 |     0.1253 | 생각보다 배송이 오래 걸렸어요.
```

1위가 "배송이 빠르고 제품도 좋아요."인 이유는 의미가 비슷해서가 아니라 "배송"이라는 단어가 겹쳐서입니다.
"생각보다 배송이 오래 걸렸어요."가 의미상으론 더 가깝지만 3위에 그쳤는데,
짧은 문장에서는 단어 중복이 의미보다 거리에 더 큰 영향을 주기 때문입니다.

## 실행 방법

### 1. 패키지 설치

```bash
uv sync
```

`.env` 설정:

```env
DB_HOST=localhost
DB_NAME=reviewdb
DB_USER=reviewuser
DB_PASSWORD=your_password
DB_PORT=5432
```

### 2. pgvector 설치 (최초 1회, macOS PostgreSQL@16 기준)

```bash
git clone --branch v0.8.0 https://github.com/pgvector/pgvector.git
cd pgvector
PG_CONFIG=/opt/homebrew/opt/postgresql@16/bin/pg_config make
PG_CONFIG=/opt/homebrew/opt/postgresql@16/bin/pg_config make install
```

### 3. 노트북 실행

`review_ai.ipynb` 셀을 순서대로 실행합니다.

| 단계  | 내용                         |
| ----- | ---------------------------- |
| 1단계 | 리뷰 5개 임베딩              |
| 2단계 | `review_vectors` 테이블 생성 |
| 3단계 | 벡터 저장                    |
| 4단계 | 유사 리뷰 검색               |
