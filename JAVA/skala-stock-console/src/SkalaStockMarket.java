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
        stockService = new StockService(stockRepository);
        stockView = new StockView();
    }

    public void start() {
        stockRepository.loadStockList();
        playerRepository.loadPlayerList();

        initializePlayer();
        stockView.displayPlayerInfo(player);

        mainLoop();

        stockView.showMessage("프로그램을 종료합니다...Bye");
        stockView.close();
    }

    private void initializePlayer() {
        String playerId = stockView.promptForPlayerId();
        player = playerRepository.findPlayer(playerId);
        if (player == null) {
            int money = stockView.promptForInitialMoney();
            player = new Player(playerId, money);
            playerRepository.addPlayer(player);
        }
    }

    private void mainLoop() {
        boolean running = true;
        while (running) {
            int code = stockView.showMenuAndGetSelection();
            switch (code) {
                case 1:
                    stockView.displayPlayerInfo(player);
                    break;
                case 2:
                    buyStock();
                    break;
                case 3:
                    sellStock();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    stockView.showMessage("올바른 번호를 선택하세요.");
            }
        }
    }

    private void buyStock() {

    }

    private void sellStock() {

    }
}