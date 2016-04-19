package search;

import Util.FileUtils;
import java.util.ArrayList;


/**
 * Created by randyjp on 10/31/15.
 */
public class Search {
    private ArrayList<String> docs;
    private ArrayList<String> stop;
    private InvertedIndex ii;
    //    private static String COMMENTS_FILE = "src/data/comments.txt";
    private static String COMMENTS_FILE = "src/data/commentsTest.txt";
    private static String STOP_FILE = "src/data/stopwords.txt";

    public Search() {
        try {
            docs = FileUtils.readFileAsListOfStrings(COMMENTS_FILE);
            stop = FileUtils.readFileAsListOfStrings(STOP_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ii = new InvertedIndex(docs, stop);

    }

    public ArrayList<Integer> searchWithQuery(String[] query) {
        ArrayList<Integer> s = ii.search(query);
        return s;
    }

    public ArrayList<String> getContainingDocuments(String[] query) {
        ArrayList<Integer> s = searchWithQuery(query);
        ArrayList<String> documents = new ArrayList<>();

        if (s == null) return documents;

        for (Integer i : s) {
            documents.add(docs.get(i.intValue()));
        }

        return documents;
    }
}
