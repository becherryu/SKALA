import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 플레이어의 주식 포트폴리오를 관리합니다.
 */
public class Portfolio {

    private final Map<String, Stock> stocks = new LinkedHashMap<>();

    // 주식 추가 또는 업데이트
    public synchronized Portfolio addOrUpdateStock(Stock stock) {
        validateStock(stock);

        Stock existing = stocks.get(stock.getName());

        if (existing == null) {
            if (stock.getQuantity() <= 0) {
                throw new IllegalArgumentException("신규 주식 수량은 1 이상이어야 합니다.");
            }
            stocks.put(stock.getName(), copy(stock));
        } else {
            updateQuantityAndPrice(existing, stock);
        }

        return this;
    }

    // 기존 주식 정보 업데이트
    public synchronized Portfolio updateStock(Stock stock) {
        validateStock(stock);

        Stock existing = stocks.get(stock.getName());
        if (existing == null) {
            throw new IllegalArgumentException("해당 주식이 포트폴리오에 존재하지 않습니다.");
        }

        updateQuantityAndPrice(existing, stock);
        return this;
    }

    // 내부 수량 및 가격 업데이트 로직
    private void updateQuantityAndPrice(Stock existing, Stock incoming) {
        int newQuantity = existing.getQuantity() + incoming.getQuantity();

        existing.setQuantity(newQuantity);
        existing.setPrice(incoming.getPrice());

        // 수량이 0 이하이면 제거
        if (newQuantity <= 0) {
            stocks.remove(existing.getName());
        }
    }

    // 이름으로 주식 조회
    public synchronized Optional<Stock> findStockByName(String name) {
        validateStockName(name);
        return Optional.ofNullable(stocks.get(name))
                .map(Portfolio::copy);
    }

    // 포트폴리오 내 전체 주식 조회
    public synchronized List<Stock> getAllStocks() {
        return getStockListInternal();
    }

    // 메뉴 선택용 list 반환
    public synchronized List<Stock> getStocksAsList() {
        return getStockListInternal();
    }

    // 내부 반환 공통 로직
    private List<Stock> getStockListInternal() {
        return stocks.values()
                .stream()
                .map(Portfolio::copy)
                .toList();
    }

    // 내부 객체 복사 메서드
    private static Stock copy(Stock stock) {
        return new Stock(
                stock.getName(),
                stock.getPrice(),
                stock.getQuantity());
    }

    /**
     * 유효성 검사
     */
    private static void validateStock(Stock stock) {
        if (stock == null) {
            throw new IllegalArgumentException("Stock 객체는 null일 수 없습니다.");
        }
    }

    private static void validateStockName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("조회할 Stock 이름은 비어 있을 수 없습니다.");
        }
    }
}
