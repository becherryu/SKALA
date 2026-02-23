import java.util.ArrayList;
import java.util.List;

/**
 * 주식 데이터를 파일에 저장하고 메모리에 로드합니다.
 */
public class StockRepository {
    private static final String STOCK_FILE = "stocks.txt";
    private final List<Stock> stockList = new ArrayList<>();
    private final StockMapper mapper = new StockMapper();

    public void loadStockList() {

    }

    private void initializeDefaultStocks() {
        stockList.add(new Stock("TechCorp", 152, 0));
        stockList.add(new Stock("GreenEnergy", 88, 0));
        stockList.add(new Stock("HealthPlus", 210, 0));
        stockList.add(new Stock("BioGen", 75, 0));
    }

    public List<Stock> getAllStocks() {
        return new ArrayList<>(stockList);
    }

    public Stock findStock(int index) {

    }

    public Stock findStock(String name) {

    }
}