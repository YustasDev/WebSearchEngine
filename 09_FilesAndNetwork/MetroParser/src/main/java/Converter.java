
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Converter {

  private static String baseFile = "metro.json";

  // используем Jackson Framework
  public static void toJSON(List list) throws Exception {   // преобразуем из списка объектов в JSON
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(new File(baseFile), list);
    System.out.println("json created!");
  }

  public static List<MetroLine> jsonReader(File jsonFile)
      throws Exception {  // читаем файл и переводим в список
    ObjectMapper mapper = new ObjectMapper();
    List<MetroLine> metroLineList = new ArrayList<>();
    try {
      metroLineList = Arrays.asList(mapper.readValue(jsonFile, MetroLine[].class));
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(13);         //  что-то пошло не так
    }
    return metroLineList;
  }

  public static void countigStations(List<MetroLine> list) {
    for (MetroLine line : list) {
      System.out.println(
          "На линии метро '" + line.getName() + "':" + " количество станций составляет - " + line
              .getStations().size() + "\n");
    }
  }
}
