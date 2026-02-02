# ---------------------------------------------------------------
# [파이썬 2일차 실습 1 - OOP 기반 AI 추천 주문 시스템 설계]
#
# 작성자(author): 장아현
# 작성일자(date): 2026.01.26
# 변경내역(description): 객체를 활용한 주문 이력을 기반으로 메뉴 추천 시스템
# ---------------------------------------------------------------
# ---------------------------------------------------------------
#  한 스타트업이 온라인 음료 주문 플랫폼을 개발 중이다.
#  사용자의 주문 이력을 기반으로 머신러닝 없이도 간단한 추천 시스템을 포함하려 한다.
#  이 시스템은 다음 기능을 포함해야 한다.
#
#  - 다양한 음료 메뉴 정의
#  (이름, 가격, 태그: 예: '커피', '차', '콜드', '뜨거운')
#  - 사용자의 주문 내역 저장 (사용자는 임의로 설정 후 진행)
#  - 최근 주문된 음료를 바탕으로 유사한 태그의 음료 추천
#  - 총 주문 금액 계산 (총합, 평균 등 포함)
# ---------------------------------------------------------------
from dataclasses import dataclass


# 음료 클래스
@dataclass
class Beverage:
    name: str
    price: int
    category: list


# 사용자 클래스
@dataclass
class User:
    name: str
    menuLog: list

    # 사용자가 주문을 하는 함수
    def orderBeverage(self, beverage: Beverage):
        self.menuLog.append(beverage)

    # 사용자의 메뉴 총합을 구하는 프로퍼티
    @property
    def totalMenuPrice(self):
        return sum(beverage.price for beverage in self.menuLog)

    # 사용자의 메뉴 평균을 구하는 프로퍼티
    @property
    def averageMenuPrice(self):
        return self.totalMenuPrice / len(self.menuLog) if self.menuLog else 0

    # 가장 최근 주문한 메뉴와 유사한 메뉴 추천하는 함수
    def recommendMenu(self, menu: list):
        # 주문 이력이 없으면 아메리카노 추천
        if not self.menuLog:
            return DEFAULT_MENU

        # 가장 최근한 주문 구하기
        recent = self.menuLog[-1]
        recent_category = set(recent.category)

        candidates = []

        # 메뉴 전체를 돌면서 각 category와 몇개 유사한지 구하기
        for drink in menu:
            if drink.name == recent.name:
                continue

            similarity = len(recent_category & set(drink.category))
            if similarity > 0:
                candidates.append((drink.name, similarity))

        # 유사한 것이 없으면 아메리카노 추천
        if not candidates:
            return DEFAULT_MENU

        # 최대로 비슷한 카테고리 개수 구하기
        max_similarity = max(cnt for _, cnt in candidates)

        return [name for name, cnt in candidates if cnt == max_similarity]


# 카페 메뉴
menu = [
    # coffee
    Beverage("아이스 아메리카노", 3000, ["커피", "차가운"]),
    Beverage("카페라떼", 3500, ["커피", "밀크", "뜨거운"]),
    Beverage("모카라떼", 3500, ["커피", "밀크", "차가운"]),
    Beverage("바닐라라떼", 3500, ["커피", "밀크", "뜨거운"]),
    # nonCoffee
    Beverage("녹차", 2800, ["차", "뜨거운"]),
    Beverage("허브티", 3000, ["차", "차가운"]),
    Beverage("캐모마일티", 3000, ["차", "뜨거운"]),
    Beverage("초코라떼", 3000, ["밀크", "차가운"]),
    Beverage("녹차라떼", 3000, ["밀크", "차가운"]),
]

DEFAULT_MENU = menu[0].name


# ---------------------------------------------------------------
# 사용자 생성하고 주문하기
# ---------------------------------------------------------------
user = User(name="장아현", menuLog=[])

# 주문 하기
user.orderBeverage(menu[0])
user.orderBeverage(menu[3])
user.orderBeverage(menu[8])

# 사용자 주문 이력 출력하기
print("~~~~ 사용자의 주문 이력 ~~~~")
for beverage in user.menuLog:
    print(f"- {beverage.name}")
print()

# 주문 총액과 평균 출력하기
print(f"총 주문 금액: {user.totalMenuPrice}")
print(f"평균 주문 금액: {user.averageMenuPrice:.2f}\n")

# 메뉴 추천받기
recommendation = user.recommendMenu(menu)
print("~~~~ 오늘은 이 메뉴 어때요? ~~~~")
print(f"추천 음료: {recommendation}")
