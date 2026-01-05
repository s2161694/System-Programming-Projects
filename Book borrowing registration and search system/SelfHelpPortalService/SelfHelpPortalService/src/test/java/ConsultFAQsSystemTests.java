import model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ConsultFAQsSystemTests {
    private FAQSection faqRoot;

    @Before
    public void setUp() {
        // Set up the FAQ hierarchy for testing
        faqRoot = new FAQSection("FAQ");

        // Add top-level topics
        FAQSection topic1 = new FAQSection("Topic 1");

        faqRoot.addSubsection(topic1);

        FAQSection topic2 = new FAQSection("Topic 2");

        faqRoot.addSubsection(topic2);

        // Add sub-topics
        FAQSection subTopic1 = new FAQSection("Sub-Topic 1");
        subTopic1.addItem("Question 3", "Answer 3");
        topic1.addSubsection(subTopic1);

        FAQSection subTopic2 = new FAQSection("Sub-Topic 2");
        subTopic2.addItem("Question 4", "Answer 4");
        topic1.addSubsection(subTopic2);

        Collection<FAQSection> sections = faqRoot.getSubsections();
        for (FAQSection section : sections) {
            // Perform operations with 'section' here
        }
    }

    @Test
    public void testDisplayTopLevelTopics() {
        // Simulate inquirer consulting the FAQ
        String[] expectedTopics = {"Topic 1", "Topic 2"};
        String[] displayedTopics = faqRoot.getSubsections().stream()
                .map(FAQSection::getTopic)
                .toArray(String[]::new);

        assertArrayEquals(expectedTopics, displayedTopics);
    }

    @Test
    public void testNavigateToSubTopic() {
        List<FAQSection> sectionList = new ArrayList<>(faqRoot.getSubsections());
        FAQSection topic1 = sectionList.get(0); // Get Topic 1
        String[] expectedSubTopics = {"Sub-Topic 1", "Sub-Topic 2"};
        String[] displayedSubTopics = topic1.getSubsections().stream()
                .map(FAQSection::getTopic)
                .toArray(String[]::new);

        assertArrayEquals(expectedSubTopics, displayedSubTopics);
    }

    @Test
    public void testNavigateUpTheHierarchy() {

        List<FAQSection> sectionList = new ArrayList<>(faqRoot.getSubsections());
        FAQSection topic1 = sectionList.get(0); // Get Topic 1
        List<FAQSection> subsectionList = new ArrayList<>(topic1.getSubsections());
        FAQSection subtopic1 = subsectionList.get(0); // Get Topic 1
        // Simulate inquirer navigating down to a sub-topic


        // Simulate inquirer navigating up to the parent topic
        FAQSection parentTopic = subtopic1.getparent();
        assertEquals("Topic 1", parentTopic.getTopic());
    }

}
