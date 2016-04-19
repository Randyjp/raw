package classifier;

/**
 * Created by randyjp on 10/30/15.
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class NaiveBayes {
    String[] trainingDocs;
    int[] trainingLabels;
    int numClasses;
    int[] classCounts; //number of docs per class
    String[] classStrings; //concatenated string for a given class
    int[] classTokenCounts; //total number of tokens per class
    HashMap<String, Double>[] condProb;
    HashSet<String> vocabulary; //entire vocabuary

    public NaiveBayes(String[] docs, int[] labels, int numC) {
        System.out.println(labels.length + " "+labels[100]);
        trainingDocs = docs;
        trainingLabels = labels;
        numClasses = numC;
        classCounts = new int[numClasses];
        classStrings = new String[numClasses];
        classTokenCounts = new int[numClasses];
        condProb = new HashMap[numClasses];
        vocabulary = new HashSet<String>();
        for (int i = 0; i < numClasses; i++) {
            classStrings[i] = "";
            condProb[i] = new HashMap<String, Double>();
        }
        for (int i = 0; i < trainingLabels.length; i++) {
            classCounts[trainingLabels[i]]++;
            classStrings[trainingLabels[i]] += (trainingDocs[i] + " ");
        }
        for (int i = 0; i < numClasses; i++) {
            String[] tokens = classStrings[i].split(" ");
            classTokenCounts[i] = tokens.length;
            //collecting the counts
            for (String token : tokens) {
                vocabulary.add(token);
                if (condProb[i].containsKey(token)) {
                    double count = condProb[i].get(token);
                    condProb[i].put(token, count + 1);
                } else
                    condProb[i].put(token, 1.0);
            }
        }
        //computing the class conditional probability
        for (int i = 0; i < numClasses; i++) {
            Iterator<Map.Entry<String, Double>> iterator = condProb[i].entrySet().iterator();
            int vSize = vocabulary.size();
            while (iterator.hasNext()) {
                Map.Entry<String, Double> entry = iterator.next();
                String token = entry.getKey();
                Double count = entry.getValue();
                count = (count + 1) / (classTokenCounts[i] + vSize);
                condProb[i].put(token, count);
            }
            System.out.println(condProb[i]);
        }
    }

    public int classify(String doc) {
        int label = 0;
        int vSize = vocabulary.size();
        double[] score = new double[numClasses];
        for (int i = 0; i < score.length; i++) {
            score[i] = Math.log(classCounts[i] * 1.0 / trainingDocs.length);
        }
        String[] tokens = doc.split(" ");
        for (int i = 0; i < numClasses; i++) {
            for (String token : tokens) {
                if (condProb[i].containsKey(token))
                    score[i] += Math.log(condProb[i].get(token));
                else
                    score[i] += Math.log(1.0 / (classTokenCounts[i] + vSize));
            }
        }
        double maxScore = score[0];
        for (int i = 0; i < score.length; i++) {
            if (score[i] > maxScore)
                label = i;
        }
//        System.out.print(label);
        return label;
    }

}
