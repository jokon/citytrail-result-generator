package model;

import java.util.Collections;
import java.util.List;

/**
 * Created by jaok on 2017-02-20.
 */
public class GeneralResult implements Comparable<GeneralResult> {

    public static final String RESULT_TABLE_HTML_CLASS_NAME = "main-scores-table";

    public static final Integer EVENT_COUNT = 6;
    public static final Integer QUALIFIED_EVENT_THRESHOLD = 4;


    public static final Integer LEGIT_COLUMN_COUNT = 15;
    public static final Integer COLUMN_FIRST_NAME = 2;
    public static final Integer COLUMN_LAST_NAME = 3;
    public static final Integer COLUMN_CATEGORY = 7;
    public static final Integer COLUMN_NUMBER = 1;
    public static final Integer COLUMN_EVENT_COUNT = 14;

    public static final Integer[] COLUMNS_EVENT_RESULTS = {8,9,10,11};

    private Competitor competitor;
    private List<Integer> places;

    public GeneralResult(Competitor competitor, List<Integer> places) {
        this.competitor = competitor;
        this.places = places;
    }

    public Integer getPointCount() {
        Collections.sort(places);
        int sum = places.subList(0, places.size() >= QUALIFIED_EVENT_THRESHOLD ? QUALIFIED_EVENT_THRESHOLD : places.size()).stream().mapToInt(Integer::intValue).sum();

        return sum;
    }

    public Integer getEventCount() {
        return places.size();
    }

    public Competitor getCompetitor() {
        return competitor;
    }

    public void setCompetitor(Competitor competitor) {
        this.competitor = competitor;
    }

    public List<Integer> getPlaces() {
        return places;
    }

    public void setPlaces(List<Integer> places) {
        this.places = places;
    }

    public void addPlace(Integer place) {
        places.add(place);
    }

    @Override
    public String toString() {
        return competitor + " [" + getEventCount() + " | " + getPointCount() + "]";
    }

    @Override
    public int compareTo(GeneralResult o) {
        Integer thisEventCount = getEventCount();
        Integer otherEventCount = o.getEventCount();

        Integer thisPointCount = getPointCount();
        Integer otherPointCount = o.getPointCount();

        if (thisEventCount < QUALIFIED_EVENT_THRESHOLD || otherEventCount < QUALIFIED_EVENT_THRESHOLD) {
            if (thisEventCount == otherEventCount) {
                return thisPointCount.compareTo(otherPointCount);
            } else {
                return otherEventCount.compareTo(thisEventCount);
            }
        } else {
            return thisPointCount.compareTo(otherPointCount);
        }
    }
}
