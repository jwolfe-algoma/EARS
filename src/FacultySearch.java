import javafx.beans.property.*;

import java.time.LocalDate;

/*
Purpose:          Wrapper object for faculty search data to transfer data between database and UI
Collaboration:    Used in all controllers that deal with faculty searches
Author:           Jacob Wolfe
 */

public class FacultySearch {

    StringProperty position = new SimpleStringProperty();
    ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<LocalDate>();
    ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<LocalDate>();
    StringProperty description = new SimpleStringProperty();
    StringProperty requirements = new SimpleStringProperty();
    IntegerProperty searchID = new SimpleIntegerProperty();

    // default constructor
    public FacultySearch() {

    }

    /*
    Function:       Creates a new FacultySearch
    Parameters:     String position: Title of the search position
                    LocalDate startDate: start of the search
                    LocalDate endDate: end of the search
                    String description: description of the search position
                    String requirements: requirements for potential applicants
                    int searchID: ID for the search
     */
    public FacultySearch(String position, LocalDate startDate, LocalDate endDate, String description, String requirements, int searchID) {
        this.position.set(position);
        this.startDate.set(startDate);
        this.endDate.set(endDate);
        this.description.set(description);
        this.requirements.set(requirements);
        this.searchID.set(searchID);
    }

    // getter and setter methods for search properties
    public int getSearchID() {
        return searchID.get();
    }

    public IntegerProperty getSearchIDProperty() {
        return searchID;
    }

    public void setSearchID(int searchID) {
        this.searchID.set(searchID);
    }

    public String getPosition() {
        return position.get();
    }

    public StringProperty getPositionProperty() {
        return position;
    }

    public void setPosition(String position) {
        this.position.set(position);
    }

    public LocalDate getStartDate() {
        return startDate.get();
    }

    public ObjectProperty<LocalDate> getStartDateProperty() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate.set(startDate);
    }

    public LocalDate getEndDate() {
        return endDate.get();
    }

    public ObjectProperty<LocalDate> getEndDateProperty() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate.set(endDate);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty getDescriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getRequirements() {
        return requirements.get();
    }

    public StringProperty getRequirementsProperty() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements.set(requirements);
    }
}
