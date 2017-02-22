package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaok on 2017-02-21.
 */
public enum City {

    BYDGOSZCZ(467),
    KATOWICE(485),
    LUBLIN(497),
    LODZ(515),
    OLSZTYN(479),
    POZNAN(461),
    SZCZECIN(509),
    TROJMIASTO(473),
    WARSZAWA(503),
    WROCLAW(491);

    private final List<Integer> eventIds = new ArrayList<>();

    /* firstSingleEventId idicates id of the first result for the city and it is used by getting resutl from city trail site. Further results have is equal to next integer values.
    * i.e. if Bydgoszcz have first id 467 its mean that subsequent  results for Bydgoszcz have ids: 468, 469, 470, 471 and 472.
    * This method creates this ids lists for each city.
    * */
    City(int firstSingleEventId) {
        int next = 0;
        for (int i = firstSingleEventId; i < firstSingleEventId + GeneralResult.EVENT_COUNT; i ++) {
            eventIds.add(firstSingleEventId + next++);
        }
    }

    public Integer getEventId(Integer eventNumber) {
        return eventIds.get(eventNumber);
    }

    public String getName() {
        return name().toLowerCase();
    }
}
