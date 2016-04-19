package search;

import Util.FileUtils;

import java.util.*;

import static java.util.Collections.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

/**
 * Created by randyjp on 9/20/15.
 */
public class InvertedIndex {
    private  static  String POSTINGS = "src/data/posting.txt";
    private  static  String TERMS = "src/data/terms.txt";
    private ArrayList<String> docs;
    private ArrayList<String> termsTepm;
    private ArrayList<String> termsSteammed;
    private ArrayList<ArrayList<Integer>> postingList;
    private ArrayList<String> stopWords;


    public InvertedIndex(ArrayList<String> documents, ArrayList<String> stop){
        docs = documents;
        stopWords = stop;
        termsTepm = new ArrayList<>();
        termsSteammed = new ArrayList<>();
        postingList = new ArrayList<>();

        int docId = 0;
        ArrayList<Integer> post;
        for(String s:docs){ //read every string which contains a file
            //String[] tokens = s.split("[.:,&%$#!/+*-] | ' '");
            String[] tokens = s.split("\\W");
            for (String token:tokens){
                if(stopWords.contains(token)) continue; //if it is a stop word, skip one loop cycle(could use hash).
                if(!termsTepm.contains(token)){
                    termsTepm.add(token); //adding to the normal terms
                    termsSteammed.add(steamer(token)); //adding to the stemmed terms, this will be my final terms list
                    post = new ArrayList<>(); //create a temporal ArrayList of string to hold the new token
                    post.add(new Integer(docId)); //add the single post to the list
                    postingList.add(post); //add to the posting list
                }
                else{ //for repeated term
                    int tokenIndex = termsTepm.indexOf(token); //This works because tokens are stored in order.
                    post = postingList.get(tokenIndex);
                    if(!post.contains(docId)){//this means that it's the first time the token happens in this document
                        post.add(docId);
                        postingList.set(tokenIndex,post);
                    }
                }
            }
            docId++;
        }

        writeToFile();
    }

    private String steamer(String word){
        Stemmer st = new Stemmer();
        st.add(word.toCharArray(), word.length());
        st.stem();
        return st.toString();
    }

    public ArrayList<Integer> search(String[] query){
        ArrayList<ArrayList<Integer>> ss = new ArrayList<>();
        Map<String,Integer> wordsWithSize = new Hashtable<>();

        for(String s:query){
            if(stopWords.contains(s)) continue; // skip the execution if it's a stop word
            int index = termsSteammed.indexOf(steamer(s));
            if(index < 0) return null;
            ArrayList<Integer> post = postingList.get(index);
            wordsWithSize.put(s,post.size());
            ss.add(post);
        }
        if(query.length == 1) return ss.get(0); // no merge need it of only one word
        //sorting the posting lists
        Collections.sort(ss, new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> a, List<Integer> b) {
                if (a.size() > b.size()) return 1;
                else if (a.size() == b.size()) return 0;
                else return -1;
            }
        });
        //sorting the words
        List<Map.Entry<String,Integer>> entries = new ArrayList<>(wordsWithSize.entrySet());
        Collections.sort(entries, (e1, e2) -> e1.getValue().compareTo(e2.getValue()));

        // Put entries back in an ordered map.
        Map<String,Integer> orderedMap = new LinkedHashMap<>();
        for (Map.Entry<String,Integer> entry : entries) {
            orderedMap.put(entry.getKey(), entry.getValue());
        }



        Set set = orderedMap.entrySet();
        Iterator j = set.iterator();
        while(j.hasNext()){
            Map.Entry me = (Map.Entry) j.next();
            System.out.println(me.getKey());
        }

        ArrayList<Integer> firstList = merge(ss.get(0),ss.get(1));
        for (int i = 2; i <ss.size();i++){
            firstList = merge(firstList,ss.get(i));
        }
        return firstList;
    }

    private ArrayList<Integer> merge(ArrayList<Integer> list1,ArrayList<Integer> list2){
        ArrayList<Integer> result = new ArrayList<>();
        int pointer1=0;
        int pointer2=0;
        while (pointer1<list1.size() && pointer2<list2.size()){
            if(list1.get(pointer1).intValue() == list2.get(pointer2).intValue()){
                result.add(list1.get(pointer1));
                pointer1++;
                pointer2++;
            }
            else if(list1.get(pointer1) < list2.get(pointer2)){
                pointer1++;
            }
            else{
                pointer2++;
            }
        }
        return result;
    }

    private void writeToFile(){
        try {
            FileUtils.writeFile(POSTINGS, this.postingList.toString());
            FileUtils.writeFile(TERMS, this.termsSteammed.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}

