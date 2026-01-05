import controller.Controller;
import external.AuthenticationService;
import external.EmailService;
import external.MockAuthenticationService;
import external.MockEmailService;
import model.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import view.TextUserInterface;
import view.View;
import controller.MenuController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.parser.ParseException;

class Main {

    public static void main(String[] args) throws IOException, URISyntaxException, NullPointerException, ParseException, org.apache.lucene.queryparser.classic.ParseException {
        View initialView = new TextUserInterface();
        SharedContext context = new SharedContext();
        AuthenticationService authenticationService = new MockAuthenticationService();
        EmailService emailService = new MockEmailService();
        MenuController mainController = new MenuController(context, initialView, authenticationService, emailService);
        FAQSection temp_FAQSection = new FAQSection("Topic 1");
        temp_FAQSection.addItem("Q1", "A1");
        FAQSection subsection = new FAQSection("Topic 1.1");
        subsection.addItem("Q1.1", "A1.1");
        temp_FAQSection.addSubsection(subsection);
        FAQSection temp_FAQSection2 = new FAQSection("Topic 2");
        FAQSection temp_FAQSection3 = new FAQSection("Topic 3");
        FAQSection temp_FAQSection4 = new FAQSection("Topic 4");
        temp_FAQSection3.addItem("Q3","A3");
        context.setFAQSection(temp_FAQSection);
        context.setFAQSection(temp_FAQSection2);
        subsection.addSubsection(temp_FAQSection3);


////        Collection<Page> pages = new ArrayList<>();
////        pages.add(new Page("title1", "This is test content", false));
////        pages.add(new Page("title2", "This is test content2", false));
////        pages.add(new Page("title3", "Hello My name is the great", false));
////        pages.add(new Page("title4", "This is test content4", false));
////        PageSearch Obj = new PageSearch(pages);
////
////        Collection<PageSearchResult> results = new ArrayList<>();
////
////        results = Obj.search("test");
////
////        for(PageSearchResult e: results)
////        {
////            System.out.println(e.getFormattedContent());
////        }
////
////        Collection<PageSearchResult> results2 = new ArrayList<>();
////        results2 = Obj.search("Hello");
////
////        for(PageSearchResult e: results2)
////        {
////            System.out.println(e.getFormattedContent());
////        }
//
//        HashMap<String, Collection <String>> subscribers = new HashMap<String, Collection<String>>();
//        Collection <String> emails = new ArrayList<>();
//        emails.add("arsh@gmail.com");
//        emails.add("keirs@gmail.com");
//        subscribers.put("Topic1", emails);
//
//        context.setSubs(subscribers);
//        boolean isALredayTHere = context.registerForFAQUpdates("keirs@gmail.com", "Topic1");
//
//        // now we'll print out the list of subscribers in the shared context.
//        Collection<String> topicSubscribers = subscribers.get("Topic1");
//        Iterator<String> iterateOverEmails = topicSubscribers.iterator();
//        while (iterateOverEmails.hasNext()) {
//            String email = iterateOverEmails.next();
//            System.out.println("Email: " + email);
//        }
//        System.out.println(isALredayTHere);
//        /////////////////////////
//
//
//
//        context.unregisterForFAQUpdates("jackichan@gmail.com", "Topic1");
//        // now we'll print out the list of subscribers in the shared context.
//        Collection<String> topicSubscribersAfterRemoval = subscribers.get("Topic1");
//        Iterator<String> iterateOverEmailsAfterRemoval = topicSubscribersAfterRemoval.iterator();
//        while (iterateOverEmailsAfterRemoval.hasNext()) {
//            String email = iterateOverEmails.next();
//            System.out.println("Email: " + email);
//        }
//
//
//
//        ////////////////////////////////
//        Collection<String> subies = context.usersSubscribedToFAQTopic("Topic1");
//        Iterator<String> iteratesubies = subies.iterator();
//        while (iteratesubies.hasNext()) {
//            String email = iterateOverEmails.next();
//            System.out.println("Person: " + email);
//        }

        




        Collection<Inquiry> queries = context.getUserInquiry();
        Inquiry query1 = new Inquiry("citation and references", "how to cite and reference a BBC article with the Vancouver style. thanks. ta xx", "keiraemmacrafts@gmail.com");
        context.setQuery(query1);
        Inquiry query2 = new Inquiry("chi-squared explanation", "what is chi-squared? is that the same as the test statistic?", "abrakadabra@gmail.com");
        Inquiry query3 = new Inquiry("loop before prinln", "I have a function with two pieces of code inside of it. one of them is a straight printlln and another is a loop that prints 3 times. the single println comes before the loop. but it's printing on the console before it. I don't want that. I know why it happens ( the loop takes more time to print). so ,I want the loop to do its job before the println happens, how can I do tha", "akmashbarbash@gmail.com");
        context.setQuery(query2);
        context.setQuery(query3);




        //display main menu
        mainController.mainMenu();


    }

}