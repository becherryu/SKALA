# SKALA Stock Console

콘솔 기반 주식 거래 미니 프로젝트입니다.

- 플레이어 생성/조회
- 시장 주식 조회
- 매수/매도 처리
- 파일 기반 데이터 영속화

## 폴더 역할

- `./src`
    - 애플리케이션 소스 코드
    - 도메인(`Player`, `Stock`, `Portfolio`), 저장소(`PlayerRepository`, `StockRepository`), 서비스(`StockService`), UI(`StockView`), 실행 진입점(`App`, `SkalaStockMarket`) 포함

- `./data`
    - 영속 데이터 파일 저장 위치
    - `players.txt`: 플레이어/포트폴리오 상태
    - `stocks.txt`: 시장 주식 목록(주식명,가격)

## 현재 구현 상태(핵심)

- `SkalaStockMarket`에서 전체 생명주기 제어
    - 데이터 로드 → 플레이어 초기화 → 메인 루프 → 종료 시 저장
- `StockService`에 매수/매도 비즈니스 로직 구현
    - 자금/보유수량 검증
    - 성공/실패 메시지 반환
    - 판매 시 수량 차감 후 금액 반영 순서 보장
- `Portfolio`에 주식 추가/갱신/조회 로직 구현
    - 종목별 맵 관리
    - 수량 0 이하 시 자동 제거
    - 방어적 복사로 내부 상태 보호
- `PlayerMapper`, `StockMapper` 구현
    - 문자열 ↔ 객체 변환
- `PlayerRepository`, `StockRepository` 구현
    - 파일 로드/저장
    - 잘못된 라인 스킵 처리
- 입력 안정성 개선
    - `StockView`에서 `nextLine + parseInt` 방식으로 변경
    - 잘못된 숫자 입력 시 무한루프 방지

## 데이터 포맷

- `data/stocks.txt`
    - 한 줄당: `주식명,가격`
    - 예: `TechCorp,152`

- `data/players.txt`
    - 한 줄당: `id,money,stockName1:price1:qty1|stockName2:price2:qty2`
    - 주식이 없으면 `id,money`

## 실행 방법

```bash
cd /Users/ahyeon/Desktop/SKALA/JAVA/skala-stock-console
javac -d bin src/*.java
java -cp bin App
```

## 개발 메모

- 데이터 파일 경로는 `data/`를 우선 사용하며, 실행 위치가 `src`여도 `../data`를 자동 인식하도록 처리되어 있습니다.
- 종료 시 플레이어 데이터 저장을 보장합니다.
