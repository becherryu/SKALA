# --------------------------------------------------------------------------------
# [파이썬 1일차 Codelab ② - 대용량 데이터 파이프라인의 메모리 프로파일링]
#
# 작성자(author): 장아현
# 작성일자(date): 2026.01.26
# 변경내역(description): AST 분석 + tracemalloc을 활용한 메모리 비교
# --------------------------------------------------------------------------------
# --------------------------------------------------------------------------------
# - 목표
#     - 대규모 데이터 처리 시 하드웨어 자원을 효율적으로 사용
# - 가이드
#     - 1,000만 개의 정수 데이터를 처리할 때 List Comprehension과
#       Generator Expression의 메모리 점유율을 tracemalloc으로 측정
#     - 결과를 통해 파이썬의 'Lazy Evaluation(지연 평가)' 원리를 설명
# - 라이브러리
#     - import ast
#     - import tracemalloc
# --------------------------------------------------------------------------------

import ast
import tracemalloc

# 1,000만 개의 정수 데이터
arr = list(range(10_000_000))

# 처리할 표현식 (AST 분석용)
list_comp_code = "sum([x + 10 for x in arr])"
gen_exp_code = "sum(x + 10 for x in arr)"


# -------------------------------------------------
# AST 분석 함수
# -------------------------------------------------
def analyzeAst(code: str):
    tree = ast.parse(code, mode="eval")
    call_node = tree.body
    arg = call_node.args[0]

    if isinstance(arg, ast.ListComp):
        return "List Comprehension (즉시 평가)"
    elif isinstance(arg, ast.GeneratorExp):
        return "Generator Expression (지연 평가, Lazy Evaluation)"
    else:
        return "Unknown"


# -------------------------------------------------
# 메모리 점유율 측정 (tracemalloc)
# -------------------------------------------------
# List Comprehension 메모리 점유율
tracemalloc.start()
sum([x + 10 for x in arr])
current, peak = tracemalloc.get_traced_memory()
print(analyzeAst(list_comp_code))
print(f"최대 메모리 사용량: {peak / 1024 / 1024:.2f} MB\n")
tracemalloc.stop()

# Generator Expression 메모리 점유율
tracemalloc.start()
sum(x + 10 for x in arr)
current, peak = tracemalloc.get_traced_memory()
print(analyzeAst(gen_exp_code))
print(f"최대 메모리 사용량: {peak / 1024 / 1024:.2f} MB\n")
tracemalloc.stop()


# ---------------------------------------------------------------------
# Lazy Evaluation(지연 평가)원리 설명
# Generator Expression은 값을 필요할 때 하나씩 생성하는 '지연 평가' 방식으로 동작해
# 중간 리스트를 생성하는 List Comprehension보다 메모리 사용량이 훨씬 적다.
# 따라서 대용량 데이터 처리에 적합하다. (메모리 효율 극대화)
# ---------------------------------------------------------------------
