/**
 * Stock 객체와 파일 데이터(문자열)를 상호 변환합니다.
 */
public class StockMapper {
    public Stock fromLine(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("line은 비어 있을 수 없습니다.");
        }

        String[] tokens = line.split(",", 2);
        if (tokens.length != 2) {
            throw new IllegalArgumentException("line 형식이 올바르지 않습니다. expected: 주식명,가격");
        }

        String name = tokens[0].trim();
        int price = parsePrice(tokens[1].trim());
        return new Stock(name, price, 0);
    }

    public String toLine(Stock stock) {
        if (stock == null) {
            throw new IllegalArgumentException("stock은 null일 수 없습니다.");
        }
        if (stock.getName().contains(",")) {
            throw new IllegalArgumentException("stock 이름에는 ',' 문자를 사용할 수 없습니다.");
        }
        return stock.getName() + "," + stock.getPrice();
    }

    private static int parsePrice(String rawPrice) {
        try {
            return Integer.parseInt(rawPrice);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("price 값이 숫자가 아닙니다: " + rawPrice, e);
        }
    }
}
