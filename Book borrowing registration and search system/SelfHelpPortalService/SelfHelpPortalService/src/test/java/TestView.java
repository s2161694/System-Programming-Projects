import model.FAQ;
import model.FAQSection;
import model.Inquiry;
import model.PageSearchResult;
import view.View;

import java.util.*;

public class TestView implements View {
    private Map<String, String> promptResponses = new HashMap<>();
    private List<String> messages = new ArrayList<>();

    public TestView() { }

    public TestView(Map<String, String> promptResponses) {
        this.promptResponses.putAll(promptResponses);
    }

    @Override
    public void displayInfo(String message) {
        messages.add(message);
    }

    @Override
    public void displaySuccess(String message) {

    }

    @Override
    public void displayWarning(String message) {

    }

    @Override
    public void displayError(String errorMessage) {

    }

    @Override
    public void displayException(String exceptionMessage) {

    }

    @Override
    public void displayDivider() {

    }

    @Override
    public void displayFAQ(FAQ faqList, boolean condition) {

    }

    @Override
    public void displayFAQSection(FAQSection faqListSection, boolean condition) {

    }

    @Override
    public void displayInquiry(Inquiry inquiry) {

    }

    @Override
    public void displaySearchResults(Collection<PageSearchResult> searchResult) {

    }

    @Override
    public String getInput(String prompt) {
        displayInfo(prompt); // Optionally capture the prompt as part of the messages
        return promptResponses.getOrDefault(prompt, "");
    }

    @Override
    public boolean getYesNoInput(String prompt) {
        String response = getInput(prompt);
        return "yes".equalsIgnoreCase(response) || "y".equalsIgnoreCase(response);
    }

    // Implement other methods as needed, potentially as no-ops or simple stubs

    public List<String> getMessages() {
        return messages;
    }

    public void setPromptResponse(String prompt, String response) {
        promptResponses.put(prompt, response);
    }
}