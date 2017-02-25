import model.City;
import model.Competitor;
import model.EventResult;
import model.GeneralResult;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jaok on 2017-02-22.
 */
public class Generator {
    static final String GENERAL_RESULTS_URL = "http://citytrail.pl/zawody/klasyfikacja_bg/miasto/%s";
    static final String SINGLE_EVENT_RESULT_URL = "http://citytrail.pl/zawody/wyniki/miasto/%s/id/%s";
    static final String FILE_RESULT_TEMPLATE = "results/%s.txt";
    static final String HTML_ROW_MARK = "tr";
    static final String HTML_COLUMN_MARK = "td";

    static final int LAST_EVENT_NUMBER = 4;

    public static void main (String[] args) throws IOException {
        List<GeneralResult> globalResults = new ArrayList<>();


        System.out.println("-----------|");

        for (City city : City.values()) {

            String generalResultsUrl = String.format(GENERAL_RESULTS_URL, city.getName());
            String eventResultUrl = String.format(SINGLE_EVENT_RESULT_URL, city.getName(), city.getEventId(LAST_EVENT_NUMBER));

            Map<Integer, GeneralResult> generalResults = getGeneralResults(generalResultsUrl);
            Collection<GeneralResult> updatedGeneralResults = updateGeneralResultsBySingleResult(generalResults, eventResultUrl);

            List<GeneralResult> r = new ArrayList<>(updatedGeneralResults);

            Collections.sort(r);
            printResults(r, city.getName());

            globalResults.addAll(r);

            System.out.print("-");
        }

        Collections.sort(globalResults);
        printResults(globalResults, "global");
        System.out.print("-|");
    }

    public static void printResults(List<GeneralResult> results, String fileName) {
        try {
            PrintWriter writer = new PrintWriter(String.format(FILE_RESULT_TEMPLATE, fileName) , "UTF-8");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String header = fileName.substring(0, 1).toUpperCase() + fileName.substring(1) + "\nwygenerowane: " + dateFormat.format(new Date());
            writer.println(header + "\n");
            Map<String, Integer> categoryRank = new HashMap<>();
            for (int i = 0; i < results.size(); i++) {
                GeneralResult result = results.get(i);
                String category = result.getCompetitor().getCategory();
                if (!categoryRank.containsKey(category)) {
                    categoryRank.put(category, 1);
                } else {
                    categoryRank.put(category, categoryRank.get(category) + 1);
                }
                writer.println((i + 1) + ". " + result + " w kategorii: " + categoryRank.get(category));
            }
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static Collection<GeneralResult> updateGeneralResultsBySingleResult(Map<Integer, GeneralResult> generalResults, String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Elements tables = doc.getElementsByClass(EventResult.RESULT_TABLE_HTML_CLASS_NAME);

        if (! tables.isEmpty()) {
            Element table = tables.get(0);
            Elements rawResults = table.select(HTML_ROW_MARK);

            for (Element rawResult : rawResults) {
                Elements rawRow = rawResult.select(HTML_COLUMN_MARK);

                if (rawRow.size() > EventResult.LEGIT_COLUMN_COUNT) {
                    try {
                        Integer number = getIntValueFrom(rawRow, EventResult.COLUMN_NUMBER);
                        Integer place = getIntValueFrom(rawRow, EventResult.COLUMN_PLACE);

                        GeneralResult generalResult = generalResults.get(number);
                        if (generalResult != null) {
                            generalResult.addPlace(place);
                        }
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return generalResults.values();
    }

    private static Integer getIntValueFrom(Elements rawRow, int i) {
        String[] v = rawRow.get(i).text().split(" ");
        return Integer.parseInt(v[0]);
    }

    private static Map<Integer, GeneralResult> getGeneralResults(String url) throws IOException {
        Map<Integer, GeneralResult> results = new HashMap<>();

        Document doc = Jsoup.connect(url).get();
        Elements tables = doc.getElementsByClass(GeneralResult.RESULT_TABLE_HTML_CLASS_NAME);


        for (Element table : tables) {
            Elements rawResults = table.select(HTML_ROW_MARK);

            for (Element rawResult : rawResults) {
                Elements rawCompetitors = rawResult.select(HTML_COLUMN_MARK);

                if (rawCompetitors.size() >= GeneralResult.LEGIT_COLUMN_COUNT) {
                    String firstName = rawCompetitors.get(GeneralResult.COLUMN_FIRST_NAME).text();
                    String lastName = rawCompetitors.get(GeneralResult.COLUMN_LAST_NAME).text();
                    String category = rawCompetitors.get(GeneralResult.COLUMN_CATEGORY).text();

                    Integer number = Integer.parseInt(rawCompetitors.get(GeneralResult.COLUMN_NUMBER).text());

                    List<Integer> places = new ArrayList<>();

                    for (Integer resultColumn : GeneralResult.COLUMNS_EVENT_RESULTS) {
                        addPlaceToListIfExist(places, rawCompetitors.get(resultColumn).text());
                    }

                    Competitor competitor = new Competitor(firstName, lastName, number, category);
                    GeneralResult result = new GeneralResult(competitor, places);

                    results.put(number, result);
                }
            }
        }

        return results;
    }

    private static void addPlaceToListIfExist(List<Integer> places, String text) {
        try {
            String[] placeField = text.split(" ");
            if (text.length() > 0 && placeField.length > 0) {
                String place = placeField[0];
                places.add(Integer.parseInt(place));
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
    }
}
