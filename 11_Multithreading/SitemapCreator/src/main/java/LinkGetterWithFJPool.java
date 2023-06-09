import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.RecursiveTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class LinkGetterWithFJPool extends RecursiveTask<List<String>> {

  private static final Logger LOGGER = LogManager.getLogger(LinkGetterWithFJPool.class);
  private static final Marker HISTORY_PARSING = MarkerManager.getMarker("HISTORY_PARSING");
  List<String> listURLtext = new ArrayList<>();
  Set<String> listURLSelect = new ConcurrentSkipListSet<>();
  static Set<String> visitedLinks = new ConcurrentSkipListSet<>();
  List<String> finalList = new ArrayList<>();
  String url;

  public LinkGetterWithFJPool(String url) {
    this.url = url;
  }

  @Override
  protected List<String> compute() {
    visitedLinks.add(url);
    Document docNeed = null;
    try {
      docNeed = Jsoup
          .connect(url)
          .userAgent("Mozilla/5.0")
          .timeout(10 * 1000)
          .get();
    } catch (IOException e) {
      e.printStackTrace();
      LOGGER.error("Parsing failed {} ", e);
      throw new DuringParseException("Ошибка при парсинге страницы  ", e, url);
    }

    try {
      Thread.sleep(150);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    for (Element docs : docNeed  // в коде страницы
        .select("a[href]"))  // осуществляем поиск элементов соответствующих требованию
    {
      String element = docs
          .attr("abs:href"); // можно и не вводить эту переменную, но с ней нагляднее
      if (element.contains("#")) {  // избавляемся от "#" в ссылках
        int indexOfNeedless = element.indexOf("#");
        element = element.substring(0, indexOfNeedless);
      }
      listURLtext.add(element); // прибавляем найденные элементы в список
    }

    /*
    Осуществляем фильтрацию полученных ссылок, в т.ч. исключаем ранее пройденные
     */
    for (String select : listURLtext) {
      if (select.startsWith(Main.URL_NEED) && !select.endsWith("pdf") && !visitedLinks
          .contains(select)) {
        listURLSelect.add(select);
        LOGGER.info(HISTORY_PARSING, " listURLSelect is {} ", listURLSelect);
      }
    }

    List<LinkGetterWithFJPool> taskList = new ArrayList<>();
    for (String url : listURLSelect) {
      LinkGetterWithFJPool task = new LinkGetterWithFJPool(url);
      task.fork();
      taskList.add(task);
    }

    for (LinkGetterWithFJPool task : taskList) {
      try {
        finalList.addAll(task.join());
      } catch (Exception e) {
        System.out.println(" Error into -> " + task.url);
      }
    }
    finalList.add(url);
    return finalList;
  }
}

