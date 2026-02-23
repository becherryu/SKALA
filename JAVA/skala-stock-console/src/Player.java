/**
 * 플레이어의 기본 정보(ID, 자금)와 포트폴리오를 관리합니다.
 */
public class Player {
    private final String id;
    private int money;
    private final Portfolio portfolio;

    public Player(String id, int initialMoney) {
        validateId(id);
        validateMoney(initialMoney);
        this.id = id;
        this.money = initialMoney;
        this.portfolio = new Portfolio();
    }

    // Getters
    public String getId() {
        return id;
    }

    public int getMoney() {
        return money;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public Player setMoney(int money) {
        validateMoney(money);
        this.money = money;
        return this;
    }

    public Player deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("입금 금액은 1 이상이어야 합니다.");
        }
        money += amount;
        return this;
    }

    public Player withdraw(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("출금 금액은 1 이상이어야 합니다.");
        }
        if (money < amount) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }
        money -= amount;
        return this;
    }

    @Override
    public String toString() {
        return "플레이어 ID: " + id + ", 자금: " + money + ", 포트폴리오: " + portfolio;
    }

    private static void validateId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("플레이어 ID는 비어 있을 수 없습니다.");
        }
    }

    private static void validateMoney(int money) {
        if (money < 0) {
            throw new IllegalArgumentException("자금은 0 이상이어야 합니다.");
        }
    }
}
