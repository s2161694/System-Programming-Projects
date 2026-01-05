import model.Page;
import org.junit.Test;
import model.SharedContext;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AddPageSystemTests {

   private SharedContext sharedContext;

   public void setUp() {
       sharedContext = new SharedContext();
   }

   @Test
   public void testAddPage() {
       setUp();

       String title = ("Sample title");
       String content  = ("Sample content");
       Boolean isPrivate = true;
      
       Page newPage = new Page(title, content, isPrivate);
       sharedContext.addPage(newPage);
       Page addedPage = sharedContext.getPages().stream().reduce((first, second) -> second).orElse(null);
     
       assertFalse("Pages collection should not be empty after adding a page.", sharedContext.getPages().isEmpty() );
       assertTrue("Pages collection should contain the added page.", sharedContext.getPages().contains(newPage));


       assertEquals("The page title does not match the expected value.", title, addedPage.getTitle());
       assertEquals("The page content does not match the expected value.", content, addedPage.getContent());
       assertEquals("The page privacy setting does not match the expected value.", isPrivate, addedPage.getIsPrivate());
   }
  
}
