import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 플레이어 데이터를 파일에 저장하고 메모리에 로드합니다.
 */
public class PlayerRepository {
    private static final String PLAYER_FILE = "players.txt";
    private static final Path PLAYER_FILE_PATH = Path.of(PLAYER_FILE);
    private final Map<String, Player> playerMap = new LinkedHashMap<>();
    private final PlayerMapper mapper = new PlayerMapper();

    public synchronized void loadPlayerList() {
        playerMap.clear();
        if (Files.notExists(PLAYER_FILE_PATH)) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(PLAYER_FILE_PATH, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line == null || line.isBlank()) {
                    continue;
                }

                try {
                    Player player = mapper.fromLine(line);
                    playerMap.put(player.getId(), player);
                } catch (IllegalArgumentException e) {
                    System.err.println("[PlayerRepository] 잘못된 라인 스킵: " + line);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("players 파일 로드 실패: " + PLAYER_FILE_PATH, e);
        }
    }

    public synchronized void savePlayerList() {
        List<String> lines = playerMap.values().stream()
                .map(mapper::toLine)
                .toList();
        try {
            Files.write(PLAYER_FILE_PATH, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("players 파일 저장 실패: " + PLAYER_FILE_PATH, e);
        }
    }

    public synchronized Player findPlayer(String id) {
        validatePlayerId(id);
        return playerMap.get(id);
    }

    public synchronized void addPlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("player는 null일 수 없습니다.");
        }
        playerMap.put(player.getId(), player);
    }

    private static void validatePlayerId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("플레이어 ID는 비어 있을 수 없습니다.");
        }
    }
}
