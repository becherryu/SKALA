import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 플레이어의 주식 포트폴리오를 관리합니다.
 */
public class Portfolio {
    private final Map<String, Stock> stocks = new LinkedHashMap<>();

    public void addOrUpdateStock(Stock stockToAdd) {

    }

    public void updateStock(Stock stockToUpdate) {

    }

    public Optional<Stock> findStockByName(String name) {
    }

    public Collection<Stock> getAllStocks() {
    }

    public List<Stock> getStocksAsList() {
    }
}