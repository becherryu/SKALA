import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 플레이어 데이터를 파일에 저장하고 메모리에 로드합니다.
 */
public class PlayerRepository {
    private static final String PLAYER_FILE = "players.txt";
    private final Map<String, Player> playerMap = new LinkedHashMap<>();
    private final PlayerMapper mapper = new PlayerMapper();

    public void loadPlayerList() {

    }

    public void savePlayerList() {

    }

    public Player findPlayer(String id) {

    }

    public void addPlayer(Player player) {

    }
}