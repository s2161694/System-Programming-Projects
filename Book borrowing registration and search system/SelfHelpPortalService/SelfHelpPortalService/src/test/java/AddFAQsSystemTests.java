import model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;


public class AddFAQsSystemTests {

    private SharedContext sharedContext;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;


    @Before
    public void setUp() {
        sharedContext = new SharedContext();
        System.setOut(new PrintStream(outContent));
        FAQSection validSection = new FAQSection("Sample Topic");
    }
    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }


    @Test
    public void AddFAQsSectionTest(){

        //  Create a valid FAQSection with a non-empty topic
        FAQSection validSection = new FAQSection("Sample Topic");

        // Call setFAQSection with the valid FAQSection
        sharedContext.setFAQSection(validSection);

        // Assert: Verify that the FAQSection was added successfully to the shared context
        assertTrue(sharedContext.getfaQList().headSection.getSubsections().contains(validSection));
    }



    @Test
    public void AddNullInputFAQSection() {
        //  Create an FAQSection with a null topic
        FAQSection section = new FAQSection(null);

        // Call setFAQSection with the section having a null topic
        sharedContext.setFAQSection(section);

        // Assert: Verify that a warning message is logged
        String expectedOutput = "WARNING: FAQ section topic cannot be null or empty.\n";
        String actualOutput = outContent.toString().replaceAll("\\R", "\n");
        assertEquals(expectedOutput, actualOutput);
    }
    @Test
    public void AddFAQsItemTest(){
        //  Create a valid FAQSection with a non-empty topic
        FAQSection validSection = new FAQSection("Sample Topic");
        sharedContext.setFAQSection(validSection);
        validSection.addItem("Question","Answer");

        assertEquals(1, validSection.getFaqItems().size());

    }


    @Test
    public void AddExistedFAQsTest(){
        //add new section with Same topics
        FAQSection section1 = new FAQSection("topic1");
        FAQSection section2 = new FAQSection("topic1");

        sharedContext.setFAQSection(section1);
        sharedContext.setFAQSection(section2);
        assertEquals(1,sharedContext.getfaQList().headSection.getSubsections().size());

    }
    @Test
    public void AddExistedFAQsWithQAItemTest(){
        // Simulate user input for the existing section title
        String existingSectionTitle = "Existing Section";

        // Simulate user input for the FAQ item (question-answer pair)
        String question = "What is this?";
        String answer = "This is a test.";

        // Call the addFAQItem method with the existing section title and FAQ it
        FAQSection section1 = new FAQSection(existingSectionTitle);
        FAQSection section2 = new FAQSection(existingSectionTitle);
        section2.addItem(question,answer);
        sharedContext.setFAQSection( section1);
        sharedContext.setFAQSection( section2);


        // Verify that the existing section is found and the FAQ item is added to it
        assertEquals(1, sharedContext.getfaQList().headSection.getSubsections().size());

        FAQSection existingSection = sharedContext.getfaQList().headSection.getSubsections().iterator().next();

        // Verify that the existing section has the correct title
        assertEquals(existingSectionTitle, existingSection.getTopic());

        // Verify that the existing section contains the added FAQ item
        assertEquals(1, existingSection.getFaqItems().size());

        // Get the FAQ item from the existing section and verify its content
        FAQItem faqItem = existingSection.getFaqItems().iterator().next();
        assertEquals(question, faqItem.getQuestion());
        assertEquals(answer, faqItem.getAnswer());
    }

}
