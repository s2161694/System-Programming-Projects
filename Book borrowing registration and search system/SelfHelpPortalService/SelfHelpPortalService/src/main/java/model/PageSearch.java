package model;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.IOUtils;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class PageSearch {
   private Analyzer StandardAnalyzer;
   private Directory index;

   public PageSearch(Collection<Page> Pages) throws IOException
   {
       Path path_files = Files.createTempDirectory("mainIndex");
       index = FSDirectory.open(path_files);
       StandardAnalyzer = new StandardAnalyzer();
       IndexWriter iWriter = new IndexWriter(index, new IndexWriterConfig(StandardAnalyzer));
       for(Page page:Pages)
       {
           addDoc(iWriter, page.getTitle(), page.getContent()+String.format(
                   "isPrivate: %b", page.getIsPrivate()));
       }
       iWriter.close();
   }

   private void addDoc(IndexWriter Writer,String title, String content) throws IOException
   {
        Document doc = new Document();
        doc.add(new Field("title", title, TextField.TYPE_STORED));
        doc.add(new Field("content", content, TextField.TYPE_STORED));
        Writer.addDocument(doc);
   }

   public Collection<PageSearchResult> search(String searchQuery) throws IOException, ParseException
   {
        Collection<PageSearchResult> result = new ArrayList<>();
        DirectoryReader iReader = DirectoryReader.open(index);
        IndexSearcher iSearcher = new IndexSearcher(iReader);
        QueryParser parser = new QueryParser("content", StandardAnalyzer);
        Query query = parser.parse(searchQuery);
        ScoreDoc[] hits = iSearcher.search(query, 10).scoreDocs;
        StoredFields storedFields = iSearcher.storedFields();
        for(int indexSearch = 0; indexSearch < hits.length; indexSearch++)
        {
            Document hitDoc = storedFields.document(hits[indexSearch].doc);
            PageSearchResult temp_result = new PageSearchResult("Name: " + hitDoc.get("title") + "\n"
                    + hitDoc.get("content"));
            result.add(temp_result);
        }
        return result;
   }

}

