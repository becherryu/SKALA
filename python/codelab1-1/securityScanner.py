# --------------------------------------------------------------------------------
# [파이썬 1일차 Codelab① - AST(추상 구문 트리)를 활용한 자동 보안 검사기]
#
# 작성자(author): 장아현
# 작성일자(date): 2026.01.26
# 변경내역(description): 위험 함수 목록에 속한 함수를 사용하면 검사를 통해 탐지하고 report를 작성
# --------------------------------------------------------------------------------
# --------------------------------------------------------------------------------
#  가이드
#     - ast.NodeVisitor를 상속받아 코드 내의 모든 함수 호출(Call) 노드를 탐색
#     - 위험 함수(eval, exec, pickle.load)와 함께, 실무에서 금지하는 os.system 등을 감지
#     - 단순 탐지를 넘어, 어떤 파일의 몇 번째 줄에서 위반이 발생했는지 리포트를 생성
#  라이브러리
#     - import ast
# --------------------------------------------------------------------------------
import ast

# 위험 함수 목록
DANGER_FUNCTIONS = {"eval", "exec", "os.system", "pickle.load"}


# 함수 호출 노드에서 함수 이름을 문자열로 추출하는 함수
def getFunction(node):
    if isinstance(node, ast.Name):
        return node.id

    if isinstance(node, ast.Attribute):
        return f"{getFunction(node.value)}.{node.attr}"

    return ""


# AST 기반 보안 검사 Visitor
class SecurityVisitor(ast.NodeVisitor):
    def __init__(self, filename):
        self.filename = filename
        self.issues = []

    # 함수 호출(Call) 노드를 방문할 때마다 실행
    def visit_Call(self, node):
        func_name = getFunction(node.func)

        if func_name in DANGER_FUNCTIONS:
            self.issues.append(
                {
                    "file": self.filename,
                    "line": node.lineno,
                    "function": func_name,
                }
            )

        # 하위 노드 계속 탐색
        self.generic_visit(node)


# AST를 순회하면서 위험 함수 호출을 탐지하는 함수
def scanCode(code, filename):
    tree = ast.parse(code)

    visitor = SecurityVisitor(filename)
    visitor.visit(tree)

    return visitor.issues


# 파일을 스캔하는 함수
def scanFile(filename):
    with open(filename, "r", encoding="utf-8") as f:
        code = f.read()

    return scanCode(code, filename)


# 위험 탐지 리포트를 생성하는 함수
def dangerReport(issues, report_file="detected_danger.txt"):
    with open(report_file, "w", encoding="utf-8") as f:
        if not issues:
            f.write("보안 위반 사항이 발견되지 않았습니다.\n")
            return

        f.write((f"- 파일: {issues[0]['file']}\n\n"))
        for issue in issues:
            f.write(f"[라인: {issue['line']}] | 위험 함수: {issue['function']}\n")


# -----------------------------
# 실행하기
# -----------------------------
if __name__ == "__main__":
    target = "dangerFunc.py"
    result = scanFile(target)

    dangerReport(result)

    if not result:
        print("보안 위반 사항이 발견되지 않았습니다.")
    else:
        print("보안 위반 사항이 탐지되었습니다.")
        print(f"- 파일: {target}\n")
        for r in result:
            print(f"[라인: {r['line']}] | 위험 함수: {r['function']}")
        print("\ndetected_danger.txt 파일이 생성되었습니다.")
