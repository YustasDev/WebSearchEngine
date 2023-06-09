import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;
import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnectionException;
import org.redisson.client.protocol.ScoredEntry;
import org.redisson.config.Config;

public class RedisStorage {

  // Объект для работы с Redis
  private RedissonClient redisson;

  // Объект для работы с ключами
  private RKeys rKeys;

  // Объект для работы с Sorted Set'ом
  private RScoredSortedSet<String> onlineUsers;

  private final static String KEY = "ONLINE_USERS";

  private double getTs() {
    return new Date().getTime() / 1000;
  }

  // Пример вывода всех ключей
  public void listKeys() {
    Iterable<String> keys = rKeys.getKeys();
    for (String key : keys) {
      System.out.println("KEY: " + key + ", type:" + rKeys.getType(key));
    }
  }

  void init() {
    Config config = new Config();
    config.useSingleServer().setAddress("redis://127.0.0.1:6379");
    try {
      redisson = Redisson.create(config);
    } catch (RedisConnectionException Exc) {
      System.out.println("Не удалось подключиться к Redis");
      System.out.println(Exc.getMessage());
    }
    rKeys = redisson.getKeys();
    onlineUsers = redisson.getScoredSortedSet(KEY);
    rKeys.delete(KEY);
  }

  void shutdown() {
    redisson.shutdown();
  }

  // Фиксирует посещение пользователем страницы
  void logPageVisit(int user_id) {
    //ZADD ONLINE_USERS
    onlineUsers.add(getTs(), "Пользователь № " + String.valueOf(user_id));
  }

  public Collection<String> getAllusers() {
    Collection<String> usersList = new ArrayList<String>();
    usersList = onlineUsers.readAll();
    return usersList;
  }

  public String getUser(int user_id) {
    Optional<String> userOptional = onlineUsers.stream()
        .filter(x -> x.contains(String.valueOf(user_id))).findFirst();
    String user = userOptional.orElse("");
    return user;
  }

  public Collection<String> getRankUsers() {
    Collection<String> usersRankList = new ArrayList<>();
    int userListSize = onlineUsers.size();
    usersRankList = onlineUsers.valueRange(0, userListSize);
    return usersRankList;
  }

  public void deleteUser(String user) {
    onlineUsers.remove(user);
  }

  public void clearListUsers() {
    onlineUsers.clear();
  }
}

