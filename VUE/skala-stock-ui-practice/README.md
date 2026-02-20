# SKALA STOCK UI

Vue 3 + Vite 기반의 **주식 거래 UI 연습 프로젝트**입니다.  
`json-server`를 활용하여 주식 목록 조회, 주식 거래, 플레이어 자산 관리, 주식 순위 기능을 구현했습니다.

프론트엔드 단에서 데이터 조합, 파생 값 계산, 상태 관리 등을 중심으로  
**실제 주식 서비스와 유사한 사용자 흐름(UI/UX)**을 구현하는 것을 목표로 합니다.

---

## 🔧 Tech Stack

- **Vue 3 (Composition API)**
- **Vite**
- **Vue Router**
- **Bootstrap 5 / Bootstrap Icons**
- **json-server** (Mock API)
- **SessionStorage** (로그인 상태 유지)

---

## ✨ 주요 기능

### 🔐 로그인 / 로그아웃

- 플레이어 ID 기반 로그인
- 아이디 존재 여부와 비밀번호 검증
- 로그인 상태를 SessionStorage에 저장
- 로그아웃 시 상태 초기화 및 시작 페이지로 이동

---

### 📈 주식 목록 관리

- 전체 주식 목록 조회
- 주식명 검색
- 페이지네이션 (숫자 버튼, 5페이지 단위 이동)
- 주식 추가 기능
  - 동일한 이름의 주식을 추가할 경우 **기존 주식이 중복 생성되지 않고 가격이 갱신**

---

### 💼 주식 거래 (구매 / 판매)

- 주식 구매 및 판매 기능
- 보유 수량 초과 판매 방지
- 예수금 기준 최대 구매 수량 계산
- 최대구매 / 최대판매 버튼 제공
- 거래 시 즉시 플레이어 자산 정보 갱신

---

### 💰 플레이어 자산 관리

- 로그인한 플레이어 정보 표시
  - 플레이어 ID
  - 현재 예수금
  - 보유 주식 평가 금액
- 주식 가격 변경 또는 거래 발생 시:
  - 보유 주식 자산 금액 자동 재계산
  - 현재 예수금과 총 자산 정보 즉시 반영
- 모든 자산 계산은 **프론트엔드 단에서 실시간으로 처리**

---

### 🏆 주식 순위

- 주식 순위 탭 제공
- 다음 기준의 TOP 10 주식 표시
  - 가장 많이 보유된 주식 (전체 플레이어 보유 수량 합산)
  - 현재 거래량이 많은 주식 (랜덤 데이터)
  - 검색량이 많은 주식 (랜덤 데이터)
  - 추천 주식 TOP 10 (랜덤 데이터)
- 갱신 버튼을 통해 순위 데이터 재생성

---

### 🔄 데이터 갱신

- 주식 목록, 플레이어 정보, 주식 순위 각각 갱신 가능
- 갱신 시 최신 데이터와 계산 결과를 즉시 반영

---

## ▶️ 실행 방법

### 1. 패키지 설치

```bash
npm install
```

### 2. MOCK API 실행

```bash
npx json-server --watch stockdb.json --port 3001
```

### 3. 개발 서버 실행

```bash
npm run dev
```

## 🧪 Mock API 구조

본 프로젝트는 json-server를 사용하여 다음과 같은 리소스를 관리합니다.

- players
  - 플레이어 ID
  - 예수금 정보
- stocks
  - 주식 ID
  - 주식명
  - 현재 가격
- playerStocks
  - 플레이어별 보유 주식 정보
  - 보유 수량

## 📁 폴더 구조

```

.skala-stock-ui-practice/
├─ public/
│  ├─ favicon.ico
│  └─ logo.png
├─ src/
│  ├─ components/            # 공용 컴포넌트
│  ├─ pages/
│  │  ├─ start/
│  │  │  └─ StartMain.vue    # 로그인 / 시작 페이지
│  │  └─ stock/
│  │     ├─ StockMain.vue    # 주식 메인 페이지
│  │     └─ components/
│  │        ├─ StockList.vue       # 주식 목록
│  │        ├─ PlayerStocks.vue    # 보유 주식 및 거래
│  │        └─ StockRankings.vue   # 주식 순위
│  ├─ scripts/
│  │  ├─ api-call.ts         # API 공통 호출 유틸
│  │  ├─ store-player.js     # 플레이어 상태 관리
│  │  ├─ store-popups.ts     # 알림/토스트 유틸
│  │  └─ store-callbacks.ts
│  ├─ App.vue
│  ├─ main.js
│  └─ router.js              # Vue Router 설정
├─ stockdb.json              # json-server mock 데이터
├─ vite.config.js
├─ package.json
└─ README.md

```
