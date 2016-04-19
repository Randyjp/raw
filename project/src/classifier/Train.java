package classifier;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.*;


/**
 * Created by randyjp on 10/31/15.
 */
public class Train {

    private  static  String DATA_SET = "src/data/train.csv";
    private ArrayList<Integer> classes;
    private ArrayList<String>  body;


    public Train(){
        //List<String> trainData = FileUtil.readFromFile
        classes = new ArrayList<>();
        body = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(DATA_SET));
            String [] nextLine;
            int i = 0;
            reader.readNext(); // not getting the header into my lists
            while ((nextLine = reader.readNext()) != null) {
                if(i == 100000) break;
                body.add(nextLine[3]);
                classes.add( new Integer(Integer.parseInt(nextLine[1])));
                i++;
            }
//            System.out.println(classes);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public int[] getTrainClasses(){
        int[] c = new int[classes.size()];

        for (int i=0; i < c.length; i++){
            c[i] = classes.get(i).intValue();
        }
        return c;
    }

    public String[] getTrainPredictors(){
        return body.toArray(new String[body.size()]);
    }
}
