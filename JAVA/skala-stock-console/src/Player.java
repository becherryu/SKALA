/**
 * 플레이어의 기본 정보(ID, 자금)와 포트폴리오를 관리합니다.
 */
public class Player {
    private String id;
    private int money;
    private final Portfolio portfolio;

    public Player(String id, int initialMoney) {
        this.id = id;
        this.money = initialMoney;
        this.portfolio = new Portfolio();
    }

    // Getters
    // Setters
}