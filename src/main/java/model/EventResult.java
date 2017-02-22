package model;

/**
 * Created by jaok on 2017-02-20.
 */
public class EventResult {


    public static final String RESULT_TABLE_HTML_CLASS_NAME = "main-scores-table";

    public static final Integer LEGIT_COLUMN_COUNT = 9;
    public static final Integer COLUMN_NUMBER = 1;
    public static final Integer COLUMN_PLACE = 0;

    private Integer place;
    private Competitor competitor;

    public EventResult(Competitor competitor, Integer place) {
        this.place = place;
        this.competitor = competitor;
    }

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public Competitor getCompetitor() {
        return competitor;
    }

    public void setCompetitor(Competitor competitor) {
        this.competitor = competitor;
    }
}
