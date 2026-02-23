/**
 * 주식 거래 관련 비즈니스 로직을 처리합니다.
 */
public class StockService {
    private final PlayerRepository playerRepository;
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this(null, stockRepository);
    }

    public StockService(PlayerRepository playerRepository, StockRepository stockRepository) {
        this.playerRepository = playerRepository;
        this.stockRepository = stockRepository;
    }

    public String buyStock(Player player, Stock stockToBuy, int quantity) {
        if (player == null || stockToBuy == null) {
            return "실패: 플레이어 또는 주식 정보가 올바르지 않습니다.";
        }
        if (quantity <= 0) {
            return "실패: 구매 수량은 1 이상이어야 합니다.";
        }

        Stock marketStock = stockRepository.findStock(stockToBuy.getName());
        if (marketStock == null) {
            return "실패: 해당 주식이 시장에 존재하지 않습니다.";
        }

        int unitPrice = marketStock.getPrice();
        final int totalPrice;
        try {
            totalPrice = Math.multiplyExact(unitPrice, quantity);
        } catch (ArithmeticException e) {
            return "실패: 거래 금액이 너무 큽니다.";
        }

        if (player.getMoney() < totalPrice) {
            return "실패: 보유 현금이 부족합니다.";
        }

        try {
            player.getPortfolio().addOrUpdateStock(new Stock(marketStock.getName(), unitPrice, quantity));
            player.withdraw(totalPrice);
            persistIfNeeded();
            return "성공: " + marketStock.getName() + " " + quantity + "주 매수 완료";
        } catch (IllegalArgumentException e) {
            return "실패: " + e.getMessage();
        }
    }

    public String sellStock(Player player, Stock stockToSell, int quantity) {
        if (player == null || stockToSell == null) {
            return "실패: 플레이어 또는 주식 정보가 올바르지 않습니다.";
        }
        if (quantity <= 0) {
            return "실패: 판매 수량은 1 이상이어야 합니다.";
        }

        Stock marketStock = stockRepository.findStock(stockToSell.getName());
        if (marketStock == null) {
            return "실패: 해당 주식이 시장에 존재하지 않습니다.";
        }

        Stock heldStock = player.getPortfolio().findStockByName(stockToSell.getName()).orElse(null);
        if (heldStock == null || heldStock.getQuantity() < quantity) {
            return "실패: 보유 수량이 부족합니다.";
        }

        int unitPrice = marketStock.getPrice();
        final int totalPrice;
        try {
            totalPrice = Math.multiplyExact(unitPrice, quantity);
        } catch (ArithmeticException e) {
            return "실패: 거래 금액이 너무 큽니다.";
        }

        try {
            Stock delta = new Stock(marketStock.getName(), unitPrice, 0).setQuantity(-quantity);
            player.getPortfolio().updateStock(delta);
            player.deposit(totalPrice);
            persistIfNeeded();
            return "성공: " + marketStock.getName() + " " + quantity + "주 매도 완료";
        } catch (IllegalArgumentException e) {
            return "실패: " + e.getMessage();
        }
    }

    private void persistIfNeeded() {
        if (playerRepository != null) {
            playerRepository.savePlayerList();
        }
    }
}
