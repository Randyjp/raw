package raw;
import Util.FileUtils;
import Util.JsonUtils;
import  classifier.*;
import search.Search;

import java.util.ArrayList;
import Util.JsonUtils.*;
import search.Searchlc;

/**
 * Created by randyjp on 10/31/15.
 */
public class Raw {

    private ArrayList<String> queries;
    private ArrayList<String> categories;
    private JsonUtils js;
    private static String RESTAURANTS_FILE = "src/data/restaurants.txt";
    private static String CATEGORIES = "src/data/categories.txt";

    public static void main(String[] args){
        new Raw().run();
    }

    public  void run(){

        Train t = new Train();
        NaiveBayes classifier = new NaiveBayes(t.getTrainPredictors(),t.getTrainClasses(),2);
        Searchlc s = new Searchlc();
        //System.out.println(sl.search("food"));
         //sl.search("food");

        //Search s = new Search();
        js = new JsonUtils();
        try {
            queries = FileUtils.readFileAsListOfStrings(RESTAURANTS_FILE);
            categories = FileUtils.readFileAsListOfStrings(CATEGORIES);
        }
        catch (Exception e){
            e.printStackTrace();
        }
//        queries.add("Meatball Shop");
//        queries.add("Kreuther");
//        queries.add("fung");

        for(String query : queries) {
            String name  = query;
            ArrayList<String> documents = s.search(name);
            int count = 0;
            int size = 1;

            if (documents != null && documents.size() > 0) {
                size = documents.size();
                for (String ss : documents) {
                    count += classifier.classify(ss);
                }
            }

            js.addJsonObject(name, count / (double) size, count, categories.get(queries.indexOf(query)));

        }

        js.createJsonFile();
    }
}
