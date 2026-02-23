import java.util.stream.Collectors;

/**
 * Player 객체와 파일 데이터(문자열)를 상호 변환합니다.
 */
public class PlayerMapper {
    public Player fromLine(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("line은 비어 있을 수 없습니다.");
        }

        String[] topTokens = line.split(",", 3);
        if (topTokens.length < 2) {
            throw new IllegalArgumentException("line 형식이 올바르지 않습니다. expected: id,money[,stocks]");
        }

        String id = topTokens[0].trim();
        int money = parseInt(topTokens[1].trim(), "money");
        Player player = new Player(id, money);

        if (topTokens.length == 3 && !topTokens[2].isBlank()) {
            String[] stockTokens = topTokens[2].split("\\|");
            for (String stockToken : stockTokens) {
                if (stockToken.isBlank()) {
                    continue;
                }

                String[] fields = stockToken.split(":");
                if (fields.length != 3) {
                    throw new IllegalArgumentException(
                            "stock 형식이 올바르지 않습니다. expected: name:price:qty, value: " + stockToken);
                }

                String stockName = fields[0].trim();
                int price = parseInt(fields[1].trim(), "price");
                int quantity = parseInt(fields[2].trim(), "quantity");
                player.getPortfolio().addOrUpdateStock(new Stock(stockName, price, quantity));
            }
        }

        return player;
    }

    public String toLine(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("player는 null일 수 없습니다.");
        }

        String stocks = player.getPortfolio().getAllStocks().stream()
                .map(stock -> stock.getName() + ":" + stock.getPrice() + ":" + stock.getQuantity())
                .collect(Collectors.joining("|"));

        if (stocks.isEmpty()) {
            return player.getId() + "," + player.getMoney();
        }
        return player.getId() + "," + player.getMoney() + "," + stocks;
    }

    private static int parseInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " 값이 숫자가 아닙니다: " + value, e);
        }
    }
}
