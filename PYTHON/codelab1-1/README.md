# [Codelab ①] AST(추상 구문 트리)를 활용한 자동 보안 검사기

## 요구사항

- 가이드
  - ast.NodeVisitor를 상속받아 코드 내의 모든 함수 호출(Call) 노드를 탐색
  - 위험 함수(eval, exec, pickle.load)와 함께, 실무에서 금지하는 os.system 등을 감지
  - 단순 탐지를 넘어, 어떤 파일의 몇 번째 줄에서 위반이 발생했는지 리포트를 생성
- 라이브러리
  - import ast

## AST(Abstract Syntax Tree)란?

프로그래밍 언어로 작성된 소스 코드의 추상적인 구문 구조를 `트리 형태`로 나타낸 것

- 코드 구조를 계층 적으로 표현하여 코드 분석, 변환, 최적화 등에 사용됨
- Python의 ast 모듈은 Python 코드의 구문을 분석·조작하는 데 사용되는 도구를 제공

### 주요 문법

- `ast.parse()` : Python 코드를 AST 트리로 변환 (분석 대상 파일을 AST로 변환함)
- `ast.NodeVisitor` : AST 탐색을 위한 방문자(Visitor) 클래스 (노드를 직접 순회하지 않고, 노드 타입별 메서드를 통해 탐색)
- `ast.Name` : 단일 함수 이름을 나타내는 노드 (id 속성을 통해 함수 이름을 문자열로 추출)
- `ast.Attribute` : `모듈.함수`형태의 호출을 나타내는 노드 (재귀적으로 value를 탐색해 전체 함수 경로를 구성)
- `node.lineno` : 해당 AST 노드가 소스 코드에서 위치한 라인 번호를 나타냄(단순 탐지를 넘어 구체적인 위치 제공 가능)

### 위험 함수 목록 설명

- `eval()` : 문자열을 Python 코드로 해석해 실행 → 사용자 입력 실행 가능
- `exec()` : 여러 줄의 Python 코드 실행 → 임의 코드 실행 가능
- `pickle.load()` : 역직렬화 중 코드 실행 가능 → 신뢰되지 않은 파일 위험
- `os.system()` : OS 쉘 명령 실행 → 명령어 삽입 공격 위험

### 파일 구성

```
.
├── securityScanner.py      # AST 기반 보안 검사기
├── dangerFunc.py            # 위험 함수 사용 예제 파일
└── detected_danger.txt      # 보안 검사 결과 리포트
```
