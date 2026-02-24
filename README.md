# SKALA AI 3기 학습 아카이브

SKALA AI 3기 과정 동안 수행한 학습 기록 및 실습 프로젝트 모음 저장소입니다.
각 디렉터리는 실습 코드와 과제 결과물을 포함하며, 상세 구현 내용은 하위 프로젝트의 README에서 확인할 수 있습니다.

## 학습 목표

- 문제 해결 중심으로 기초 문법과 실습 프로젝트 완성
- 작은 기능 단위로 구현 후 리팩터링 및 회고 기록
- 브랜치, PR, 코드 리뷰 반영 기반의 협업 습관화

## Tech Stack

### 🖥 Frontend / Language

<img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white"> <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> <img src="https://img.shields.io/badge/Vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white">

### 💻 Backend / Language

<img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white"> <img src="https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=python&logoColor=white">

### 🔧 Version Control

<img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white">

## 저장소 구조

- `HTML/`: HTML 기초 실습
- `CSS/`: CSS 레이아웃, 프로필/부트스트랩 실습
- `JS/`: JavaScript 기초 및 미니 프로젝트
- `JAVA/`: Java 문법, 객체지향, 콘솔 프로젝트
- `PYTHON/`: Python 기초 및 분석 실습
- `VUE/`: Vue 기반 UI 실습 및 미니 프로젝트

## 커밋 컨벤션

Conventional Commits 기반으로 작성합니다.

- 형식: `<type>(<scope>): <subject>`
- 본문(선택): 변경 이유, 대안, 영향 범위
- 꼬리말(선택): `Refs: #123`, `Closes: #123`, `BREAKING CHANGE: ...`

### 타입

- `feat`: 사용자 기능 추가
- `fix`: 버그 수정
- `refactor`: 동작 변경 없는 구조 개선
- `docs`: 문서 변경
- `test`: 테스트 추가/수정
- `chore`: 빌드, 패키지, 설정 변경
- `ci`: CI/CD 설정 변경
- `perf`: 성능 개선
- `style`: 포맷팅, 세미콜론 등 비기능 변경
- `revert`: 커밋 되돌리기

### 작성 예시

- `feat(stock): 매수/매도 주문 처리 추가`
- `fix(console-ui): 잘못된 입력 시 예외 메시지 개선`
- `refactor(portfolio): 보유 종목 계산 로직 분리`
- `docs(readme): 프로젝트 구조 및 실행 방법 정리`
- `chore(gitignore): 로그/빌드 산출물 제외 규칙 추가`
