/*
Purpose:          Wrapper object for user data to track information between pages
Collaboration:    Used in all UIPageControllers and UIFormControllers when more than one object is passed between stages
Author:           Jacob Wolfe
 */

public class InformationPackager {

    private UserAccount account;
    private FacultySearch search;
    private SearchApplicant applicant;
    private Comment comment;

    // getter and setter methods for attributes
    public UserAccount getAccount() {
        return account;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public void setAccount(UserAccount account) {
        this.account = account;
    }

    public FacultySearch getSearch() {
        return search;
    }

    public void setSearch(FacultySearch search) {
        this.search = search;
    }

    public SearchApplicant getApplicant() {
        return applicant;
    }

    public void setApplicant(SearchApplicant applicant) {
        this.applicant = applicant;
    }
}
