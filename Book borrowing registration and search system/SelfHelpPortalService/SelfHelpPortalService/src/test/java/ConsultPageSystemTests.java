import controller.InquirerController;
import model.*;
import org.junit.Before;
import org.junit.Test;
import view.View;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

abstract class TestView2 implements View {
    private Queue<String> inputs = new LinkedList<>();
    private List<String> messages = new ArrayList<>();

    public void queueInput(String input) {
        inputs.add(input);
    }

    @Override
    public String getInput(String prompt) {
        displayInfo(prompt);
        return inputs.poll();
    }

    @Override
    public void displayInfo(String message) {
        messages.add(message);
    }

    public List<String> getMessages() {
        return messages;
    }

}

public class ConsultPageSystemTests {
    private InquirerController controller;
    private SharedContext context;
    private TestView2 view;

    @Before
    public void setUp() {
        context = new SharedContext();
        view = new TestView2() {
            @Override
            public boolean getYesNoInput(String prompt) {
                return false;
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
        };
        controller = new InquirerController(context, view, null, null);

        view.queueInput("search term");

        context.addPage(new Page("Public Page", "Content including search term", false));
        context.addPage(new Page("Private Page", "Content not for inquirer", true));
    }

    @Test
    public void SearchPagesForInquirer() {
        controller.searchPages();

        assertTrue("Prompt for search query should be displayed",
                view.getMessages().contains("Please enter your search query: "));

        boolean foundPublicResult = view.getMessages().stream()
                .anyMatch(message -> message.contains("Public Page"));
        assertTrue("Expected public search result was not displayed", foundPublicResult);

        boolean foundPrivateResult = view.getMessages().stream()
                .anyMatch(message -> message.contains("Private Page"));
        assertFalse("Private search result should not be displayed to inquirers", foundPrivateResult);
    }

    @Test
    public void EmptyPromptsInputAgain() {
        view.queueInput("");
        view.queueInput("valid search term");

        controller.searchPages();

        long promptCount = view.getMessages().stream()
                .filter(msg -> msg.contains("Please enter your search query:"))
                .count();
        assertTrue("User should be prompted again after empty input", promptCount > 1);
    }

    @Test
    public void NonMatchingSearchQuery() {
        view.queueInput("non-matching term");

        controller.searchPages();

        boolean noResultsDisplayed = view.getMessages().stream()
                .noneMatch(msg -> msg.contains("Page"));
        assertTrue("No search results should be displayed for non-matching query", noResultsDisplayed);
    }

    @Test
    public void DisplaysMultipleResults() {
        view.queueInput("common term");

        controller.searchPages();

        long matchCount = view.getMessages().stream()
                .filter(msg -> msg.contains("Page") && msg.contains("common term"))
                .count();
        assertTrue("Multiple search results should be displayed for a common query", matchCount >= 2);
    }




}