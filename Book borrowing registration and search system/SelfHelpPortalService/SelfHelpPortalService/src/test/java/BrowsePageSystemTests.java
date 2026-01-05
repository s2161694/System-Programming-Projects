import controller.AdminStaffController;
import model.*;
import view.View;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.*;
abstract class TestView1 implements View {
    // List to hold messages passed to displayInfo
    private List<String> messages = new ArrayList<>();

    @Override
    public void displayInfo(String message) {
        messages.add(message);
    }

    public List<String> getMessages() {
        return messages;
    }

    @Override
    public String getInput(String prompt) {
        return "e";
    }

    @Override
    public boolean getYesNoInput(String prompt) {
        return false;
    }
}
public class BrowsePageSystemTests {
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private SharedContext sharedContext;
    private AdminStaffController adminStaffController;
    private View controllerView;

    public void setUp(){
        sharedContext = new SharedContext();
        sharedContext.addPage(new Page("Page 1", "Content for Page 1", false));
        sharedContext.addPage(new Page("Page 2", "Content for Page 2", true));


        controllerView = new TestView1() {
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
        adminStaffController = new AdminStaffController(sharedContext, controllerView, null,null);
    }

    @Test
    public void testBrowsePages(){
        setUp();
        adminStaffController.viewAllPages();
        List<String> displayedMessages = ((TestView1) controllerView).getMessages();
        assertTrue("Output should contain the title 'Page 1'", displayedMessages.contains("1.Page 1\n"));
        assertTrue("Output should contain the title 'Page 2'", displayedMessages.contains("2.Page 2\n"));

        String expectedFirstPageTitle = "1.Page 1";
        String expectedSecondPageTitle = "2.Page 2";
        int expectedNumberOfPages = 2;

        assertTrue("The displayed messages should contain the title 'Page 1'",
                displayedMessages.stream().anyMatch(msg -> msg.contains(expectedFirstPageTitle)));
        assertTrue("The displayed messages should contain the title 'Page 2'",
                displayedMessages.stream().anyMatch(msg -> msg.contains(expectedSecondPageTitle)));

        assertEquals("The number of displayed pages should match the expected count",
                expectedNumberOfPages, displayedMessages.size());

        try {
            adminStaffController.viewAllPages();
            assertTrue("viewAllPages should complete without throwing an exception", true);
        } catch (Exception e) {
            fail("viewAllPages threw an exception: " + e.getMessage());
        }
    }
}
