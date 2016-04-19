package search;

import Util.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by randyjp on 11/17/15.
 */
public class Searchlc {

    private Analyzer analyzer;
    private Directory dir;
    private IndexWriterConfig config;
    private IndexWriter iwriter;
    //private Document doc;
    private ArrayList<String> docsFromFile;
    private static String COMMENTS_FILE = "src/data/comments.txt";
    //private static String COMMENTS_FILE = "src/data/commentsTest.txt";

    public Searchlc(){
        try {
            this.analyzer = new StandardAnalyzer();
            this.dir = new RAMDirectory();
//            this.dir = FSDirectory.open(new File("src/data/dir").toPath());
            this.config = new IndexWriterConfig(this.analyzer);
            this.iwriter = new IndexWriter(this.dir, this.config);
            //this.doc = new Document();
            this.docsFromFile = FileUtils.readFileAsListOfStrings(COMMENTS_FILE);

            for(String s: this.docsFromFile){
                Document doc = new Document();
                doc.add(new Field("content", s , TextField.TYPE_STORED));
                this.iwriter.addDocument(doc);
            }

            //this.iwriter.addDocument(this.doc);
            this.iwriter.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public ArrayList<String> search(String query){
        ArrayList<String> result = new ArrayList<>();

        try {
            DirectoryReader ireader = DirectoryReader.open(this.dir);
            IndexSearcher isearcher = new IndexSearcher(ireader);
            QueryParser parser = new QueryParser("content", this.analyzer);
            Query qr = parser.parse(QueryParser.escape(proximityQuery(query)));
            //Query qr = parser.parse(query);
            TopDocs tp = isearcher.search(qr,10000);
            //System.out.println(tp.totalHits);
            ScoreDoc[] hits = tp.scoreDocs;
            System.out.println(hits.length);
            for(int i = 0 ; i < hits.length; i++){
                Document hitDoc = isearcher.doc(hits[i].doc);
                result.add(hitDoc.get("content"));
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private String proximityQuery(String query){
        String result = "\"";
        String[] words = query.toLowerCase().split(" ");
        Boolean first  = true;
        if(words.length < 2) return query;

        for(String s: words){
            if(!StopAnalyzer.ENGLISH_STOP_WORDS_SET.contains(s)){
                if(first) {
                    result +=  s;
                    first = false;
                }
                else{
                    result +=  " " + s;
                }
            }
        }
        result +=  "\"~5";
        System.out.println(result);
        return  result;
    }
}
