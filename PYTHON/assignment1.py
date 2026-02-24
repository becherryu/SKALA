# --------------------------------------------------------------------------------
# [파이썬 1일차 실습 1 - 리스트 + 딕셔너리 기반 데이터 필터링기]
#
# 작성자(author): 장아현
# 작성일자(date): 2026.01.22
# 변경내역(description): 직원(Employee)들의 정보를 활용하여 4가지의 필터링 로직을 처리하여 출력
# --------------------------------------------------------------------------------
# --------------------------------------------------------------------------------
# 다음은 직원(Employee)들의 정보를 담은 리스트입니다.
# 이 데이터를 활용하여 아래 조건을 만족하는 필터링 로직을 작성하세요.
#
# 1) 부서가 "Engineering"이고 salary >= 80000인 직원들의 이름만 리스트로 출력하세요.
# 2) 30세 이상인 직원의 이름과 부서를 튜플 (name, department) 형태로 리스트로 출력하세요.
# 3) 급여 기준으로 직원 리스트를 salary 내림차순으로 정렬하고, 상위 3명의 이름과 급여를 출력하세요.
# 4) 모든 부서별 평균 급여를 출력하는 코드를 작성하세요.
# --------------------------------------------------------------------------------

employees = [
    {"name": "Alice", "department": "Engineering", "age": 30, "salary": 85000},
    {"name": "Bob", "department": "Marketing", "age": 25, "salary": 60000},
    {"name": "Charlie", "department": "Engineering", "age": 35, "salary": 95000},
    {"name": "David", "department": "HR", "age": 45, "salary": 70000},
    {"name": "Eve", "department": "Engineering", "age": 28, "salary": 78000},
]


# --------------------------------------------------------------------------------
# 1) 부서가 "Engineering"이고 salary >= 80000인 직원들의 이름을 출력하는 함수
def filterEngSalary(employees):
    result = []

    # employees의 항목을 돌면서 "Engineering"이고 salary >= 80000인 직원의 이름을 result 배열에 추가
    for employee in employees:
        if (employee["department"] == "Engineering") and (employee["salary"] >= 80000):
            result.append(employee["name"])

    return result


# 부서가 "Engineering"이고 salary >= 80000인 직원들의 이름을 출력
print("1) 부서가 'Engineering'이고 salary >= 80000인 직원들")
print(filterEngSalary(employees))


# --------------------------------------------------------------------------------
# 2) 기준 age 이상인 직원의 이름과 부서를 튜플 (name, department) 형태로 출력하는 함수
def OverAgeNameDep(employees, age):
    result = []

    # employees의 항목을 돌면서 기준 age이상의 직원의 (이름, 부서)을 result 배열에 추가
    for employee in employees:
        if employee["age"] >= age:
            result.append((employee["name"], employee["age"]))

    return result


# 30세 이상인 직원의 이름과 부서를 튜플형태로 리스트로 출력
print("\n2) 30세 이상인 직원의 이름과 부서")
print(OverAgeNameDep(employees, 30))


# --------------------------------------------------------------------------------
# 3) 급여 기준으로 직원 리스트를 salary 내림차순으로 정렬하고, 상위 3명의 이름과 급여를 출력하는 함수
def DescSalaryTop3(employees):

    # 급여를 기준으로 내림차순(큰 순서) 정렬
    for i in range(len(employees)):
        max_index = i
        for j in range(i + 1, len(employees)):
            if employees[j]["salary"] > employees[max_index]["salary"]:
                max_index = j
        employees[i], employees[max_index] = employees[max_index], employees[i]

    # 상위 3명의 이름과 급여 출력
    top3 = employees[:3]
    for employee in top3:
        print(f"이름: {employee['name']}, 급여: {employee['salary']}")


# 상위 3명의 이름과 급여를 출력
print("\n3) 급여 상위 3명")
DescSalaryTop3(employees)


# --------------------------------------------------------------------------------
# 4) 모든 부서별 평균 급여를 출력하는 함수
def avgSalary(employees):
    # 값을 저장할 딕셔너리
    dept_info = {}

    # for문을 돌면서 부서, 합계, 인원수 넣기
    for employee in employees:
        dept = employee["department"]
        sal = employee["salary"]

        total, cnt = dept_info.get(dept, (0, 0))
        dept_info[dept] = (total + sal, cnt + 1)

    # for문을 돌면서 부서별 평균 출력
    for dept, (total, cnt) in dept_info.items():
        print(f"부서: {dept}, 평균 급여: {total / cnt}")


# 모든 부서별 평균 급여 출력
print("\n4) 모든 부서별 평균 급여")
avgSalary(employees)
