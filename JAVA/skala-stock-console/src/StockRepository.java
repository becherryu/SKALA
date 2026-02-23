import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * 주식 데이터를 파일에 저장하고 메모리에 로드합니다.
 */
public class StockRepository {
    private static final String STOCK_FILE = "stocks.txt";
    private static final Path STOCK_FILE_PATH = resolveDataPath(STOCK_FILE);
    private final List<Stock> stockList = new ArrayList<>();
    private final StockMapper mapper = new StockMapper();

    public synchronized void loadStockList() {
        stockList.clear();
        if (Files.notExists(STOCK_FILE_PATH)) {
            initializeDefaultStocks();
            return;
        }

        try {
            List<String> lines = Files.readAllLines(STOCK_FILE_PATH, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line == null || line.isBlank()) {
                    continue;
                }
                try {
                    stockList.add(mapper.fromLine(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("[StockRepository] 잘못된 라인 스킵: " + line);
                }
            }
            if (stockList.isEmpty()) {
                initializeDefaultStocks();
            }
        } catch (IOException e) {
            initializeDefaultStocks();
        }
    }

    private void initializeDefaultStocks() {
        stockList.add(new Stock("TechCorp", 152, 0));
        stockList.add(new Stock("GreenEnergy", 88, 0));
        stockList.add(new Stock("HealthPlus", 210, 0));
        stockList.add(new Stock("BioGen", 75, 0));
    }

    public synchronized List<Stock> getAllStocks() {
        return new ArrayList<>(stockList);
    }

    public synchronized Stock findStock(int index) {
        if (index < 0 || index >= stockList.size()) {
            return null;
        }
        return stockList.get(index);
    }

    public synchronized Stock findStock(String name) {
        if (name == null || name.isBlank()) {
            return null;
        }
        for (Stock stock : stockList) {
            if (stock.getName().equals(name)) {
                return stock;
            }
        }
        return null;
    }

    private static Path resolveDataPath(String fileName) {
        Path dataInCwd = Path.of("data", fileName);
        if (Files.isDirectory(Path.of("data"))) {
            return dataInCwd;
        }

        Path dataInParent = Path.of("..", "data", fileName);
        if (Files.isDirectory(Path.of("..", "data"))) {
            return dataInParent.normalize();
        }

        return dataInCwd;
    }
}
