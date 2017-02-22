package model;

/**
 * Created by jaok on 2017-02-20.
 */
public class Competitor {

    private String firstName;
    private String lastName;
    private String category;
    private Integer number;


    public Competitor(String firstName, String lastName, Integer number, String category) {
        this.firstName = firstName;
        this .lastName = lastName;
        this.number = number;
        this.category = category;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s %s - %s", getNumber(), getFirstName(), getLastName(), getCategory());
    }
}
