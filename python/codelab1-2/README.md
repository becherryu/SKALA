# [Codelab ②] 대용량 데이터 파이프라인의 메모리 프로파일링

## 요구사항

- 목표
  - 대규모 데이터 처리 시 하드웨어 자원을 효율적으로 사용
- 가이드
  - 1,000만 개의 정수 데이터를 처리할 때 List Comprehension과 Generator Expression의 메모리 점유율을 tracemalloc으로 측정
  - 결과를 통해 파이썬의 'Lazy Evaluation(지연 평가)' 원리를 설명
- 라이브러리
  - import ast
  - import tracemalloc

## AST(Abstract Syntax Tree)란?

프로그래밍 언어로 작성된 소스 코드의 추상적인 구문 구조를 `트리 형태`로 나타낸 것

- 코드 구조를 계층 적으로 표현하여 코드 분석, 변환, 최적화 등에 사용됨
- Python의 ast 모듈은 Python 코드의 구문을 분석·조작하는 데 사용되는 도구를 제공

### 주요 문법

- `ast.parse()` : Python 코드를 AST 트리로 변환 (분석 대상 파일을 AST로 변환함)
- `ast.ListComp` : List Comprehension을 나타내는 노드 (모든 결과를 즉시 리스트로 생성)
- `ast.GeneratorExp` : Generator Expression을 나타내는 노드 (값을 필요할 때 하나씩 생성 (Lazy Evaluation))

### 메모리 점유율 비교 결과

|        항목        | List Comprehension | Generator Expression |
| :----------------: | :----------------: | :------------------: |
|     평가 방식      |     즉시 평가      |   지연 평가(Lazy)    |
|     중간 결과      |    리스트 생성     |    생성하지 않음     |
|   메모리 사용량    |         큼         |      매우 작음       |
| 대용량 데이터 처리 |      비효율적      |        효율적        |

### Lazy Evaluation(지연 평가) 설명

Lazy Evaluation이란 값을 즉시 계산하지 않고, 실제로 필요할 때 계산하는 방식이다.

- Generator Expression은 값을 하나씩 생성하여 즉시 소비하므로 중간 결과를 메모리에 저장하지 않는다.
  이로 인해 대규모 데이터 처리 시 List Comprehension보다 훨씬 효율적인 메모리 사용이 가능하다.
