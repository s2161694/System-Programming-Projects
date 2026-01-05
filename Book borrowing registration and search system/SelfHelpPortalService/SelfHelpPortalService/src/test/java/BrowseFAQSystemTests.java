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
abstract class TestView3 implements View {
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
public class BrowseFAQSystemTests {
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    SharedContext sharedContext = new SharedContext();
    FAQSection topic1 = new FAQSection("Topic 1");
    FAQSection topic2 = new FAQSection("Topic 2");

    private View controllerView;
    AdminStaffController adminStaffController=new AdminStaffController(sharedContext,controllerView,null,null);


    public void setUp(){




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
    public void testBrowseFAQ() {
        FAQSection topic1 = new FAQSection("Topic 1");
        FAQSection topic2 = new FAQSection("Topic 2");
        sharedContext.setFAQSection(topic1);
        sharedContext.setFAQSection(topic2);
        setUp();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
        adminStaffController.manageFAQ();
        System.setOut(originalOut);
        String printedOutput = outputStream.toString();

        assertFalse("Output should contain the title 'Topic 1'", printedOutput.contains("1.Topic 1\n"));
        assertFalse("Output should contain the title 'Topic 2'", printedOutput.contains("2.Topic 2\n"));
        assertFalse("Output should contain the option'", printedOutput.contains("Do you want to add a new question-answer pair here? "));

        assertTrue(sharedContext.getfaQList().headSection.getSubsections().contains(topic1));


        try {
            adminStaffController.viewAllPages();
            assertTrue("manage should complete without throwing an exception", true);
        } catch (Exception e) {
            fail("viewAllPages threw an exception: " + e.getMessage());
        }
    }
}
