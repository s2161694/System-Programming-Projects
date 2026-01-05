package view;
import model.*;
import java.util.Collection;

public interface View {
    public String getInput(String prompt);
    public boolean getYesNoInput(String prompt);
    public void displayInfo(String content);
    public void displaySuccess(String message);
    public void displayWarning(String message);
    public void displayError(String errorMessage);
    public void displayException(String exceptionMessage);
    public void displayDivider();
    public void displayFAQ(FAQ faqList, boolean condition);
    public void displayFAQSection(FAQSection faqListSection, boolean condition);
    public void displayInquiry(Inquiry inquiry);
    public void displaySearchResults(Collection<PageSearchResult> searchResult);
}