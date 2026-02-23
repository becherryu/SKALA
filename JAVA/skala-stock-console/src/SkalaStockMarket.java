import java.util.List;

/**
 * 프로그램의 시작점. 각 컴포넌트를 생성하고 전체 흐름을 제어합니다.
 */
public class SkalaStockMarket {
    private final PlayerRepository playerRepository;
    private final StockRepository stockRepository;
    private final StockService stockService;
    private final StockView stockView;
    private Player player;

    public SkalaStockMarket() {
        playerRepository = new PlayerRepository();
        stockRepository = new StockRepository();
        stockService = new StockService(playerRepository, stockRepository);
        stockView = new StockView();
    }

    public void start() {
        try {
            stockRepository.loadStockList();
            playerRepository.loadPlayerList();

            initializePlayer();
            stockView.displayPlayerInfo(player);

            mainLoop();
        } finally {
            playerRepository.savePlayerList();
            stockView.showMessage("프로그램을 종료합니다...Bye");
            stockView.close();
        }
    }

    private void initializePlayer() {
        while (true) {
            try {
                String playerId = stockView.promptForPlayerId();
                player = playerRepository.findPlayer(playerId);
                if (player == null) {
                    int money = stockView.promptForInitialMoney();
                    player = new Player(playerId, money);
                    playerRepository.addPlayer(player);
                    playerRepository.savePlayerList();
                }
                return;
            } catch (RuntimeException e) {
                stockView.showMessage("입력 오류: " + e.getMessage());
            }
        }
    }

    private void mainLoop() {
        boolean running = true;
        while (running) {
            try {
                int code = stockView.showMenuAndGetSelection();
                switch (code) {
                    case 1 -> stockView.displayPlayerInfo(player);
                    case 2 -> buyStock();
                    case 3 -> sellStock();
                    case 0 -> running = false;
                    default -> stockView.showMessage("올바른 번호를 선택하세요.");
                }
            } catch (RuntimeException e) {
                stockView.showMessage("입력 오류: 숫자를 정확히 입력하세요.");
            }
        }
    }

    private void buyStock() {
        List<Stock> stocks = stockRepository.getAllStocks();
        if (stocks.isEmpty()) {
            stockView.showMessage("거래 가능한 주식이 없습니다.");
            return;
        }

        stockView.displayStockList(stocks);
        int index = stockView.getStockIndexFromUser();
        Stock selectedStock = stockRepository.findStock(index);
        if (selectedStock == null) {
            stockView.showMessage("실패: 올바른 주식 번호를 선택하세요.");
            return;
        }

        int quantity = stockView.getQuantityFromUser();
        String result = stockService.buyStock(player, selectedStock, quantity);
        stockView.showMessage(result);
    }

    private void sellStock() {
        List<Stock> marketStocks = stockRepository.getAllStocks();
        if (marketStocks.isEmpty()) {
            stockView.showMessage("거래 가능한 주식이 없습니다.");
            return;
        }

        stockView.displayStockList(marketStocks);
        int index = stockView.getStockIndexFromUser();
        Stock selectedStock = stockRepository.findStock(index);
        if (selectedStock == null) {
            stockView.showMessage("실패: 올바른 주식 번호를 선택하세요.");
            return;
        }

        int quantity = stockView.getQuantityFromUser();
        String result = stockService.sellStock(player, selectedStock, quantity);
        stockView.showMessage(result);
    }
}
