import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map; //place with imports
import java.util.Collections; //place with imports
import java.util.HashMap;



public class Driver{   
  
        static String mayRange = "May 01 2009-May 31 2009";
        static String juneRange = "Jun 01 2009-Jun 31 2009";
        static ArrayList<Sentence> sentenceList = new ArrayList<Sentence>();
        static ArrayList<Sentence> mayList = new ArrayList<Sentence>();
        static ArrayList<Sentence> juneList = new ArrayList<Sentence>();
  
        public static void main(String[] args) {
  
                BufferedReader objReader = null;
  
                try {
  
                        String strCurrentLine;
                        
                        //Insert your file map within the quotation marks
                        objReader = new BufferedReader(new FileReader("/Users/omarbarabandi/CS1111/twitterdatabase.csv"));

                        while ((strCurrentLine = objReader.readLine()) != null) {
  
                                Sentence sent = Sentence.convertLine(strCurrentLine);
                                if (sent.keep(mayRange) == true) {
                                        mayList.add(sent);
                                }
                                if (sent.keep(juneRange) == true) {
                                        juneList.add(sent);
                                }
                                sentenceList.add(sent);
                                System.out.println(strCurrentLine);
                        }

                } catch (IOException exception) {

                        exception.printStackTrace();

                }
   
                System.out.println(printTopWords(sentenceList));
   
   
                int mayScore = 0;
                int juneScore = 0;
   
                for (int i = 0; i < mayList.size(); i++) {
                        mayScore = mayScore + mayList.get(i).getSentiment();
                }  

                for (int i = 0; i < juneList.size(); i++) {
                        juneScore = juneScore + juneList.get(i).getSentiment();
                }
   
                System.out.println("MAYSCORE:" + mayScore + "------------------");
                System.out.println("JUNESCORE:" + juneScore + "--------------");
                int juneRatio = juneScore / juneList.size();
                int mayRatio = mayScore / mayList.size();
   
                if (juneRatio > mayRatio) {
                        System.out.println("June is more positive than May.");
                }
   
                if (mayRatio > juneRatio) {
                        System.out.println("May is more positive than June.");
                }
                if (mayRatio == juneRatio) {
                        System.out.println("May and June are equally positive.");
                }

                int score = 0;

                for (int i = 0; i < sentenceList.size(); i++) {
                        Sentence newSentence = sentenceList.get(i);
                        score = newSentence.getSentiment();
                        System.out.println(newSentence + ": " + score);
                }
        }

        public static ArrayList printTopWords(ArrayList<Sentence> sentenceList) {
   
                HashMap<String, Integer> map = new HashMap<String, Integer>();


                for (int i = 0; i < sentenceList.size(); i++) {
     
                        ArrayList<String> split = sentenceList.get(i).splitSentence();
                 
                        for (int j = 0; j < split.size(); j++) {
        
                                String word = split.get(j);
        
           
                                Integer integer = map.get(word);
                                if (word == null) {
                                        continue;
                                } else if (integer == null) {
                                        map.put(word, 1);
                                } else {
                                        map.put(word, integer + 1);
                                }
                        }

                }
                Map.Entry<String, Integer> maxEntry = null;
 
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
     
                        if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                                maxEntry = entry;
                        }
                }
                int maxValueLen = maxEntry.getValue().toString().length();
     
                ArrayList <String> results = new ArrayList<String>();
     
                for (Map.Entry set : map.entrySet()) {
        
                        String value = set.getValue().toString();
           
                        while (value.length() < maxValueLen)
              
                                value = " " + value;
                 
                        results.add(value + " of " + set.getKey());
                }
                Collections.sort(results);
                Collections.reverse(results);
                for (int i = 0; i < 100; i++)
                        System.out.println(results.get(i));
                return results;
        }
}