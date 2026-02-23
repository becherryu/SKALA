/**
 * 주식 거래 관련 비즈니스 로직을 처리합니다.
 */
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public String buyStock(Player player, Stock stockToBuy, int quantity) {

    }

    public String sellStock(Player player, Stock stockToSell, int quantity) {

    }
}