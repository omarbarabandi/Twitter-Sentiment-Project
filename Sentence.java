import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.ejml.simple.SimpleMatrix;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sentence {

        private String text;
        private String author;
        private String timestamp;

        public int getSentiment() {
                Properties props = new Properties();
                props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
                StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
                Annotation annotation = pipeline.process(text);
                CoreMap sentence = annotation.get(CoreAnnotations.SentencesAnnotation.class).get(0);
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                return RNNCoreAnnotations.getPredictedClass(tree);
        }

        public static Sentence convertLine(String line) {

                String[] trialSentence = line.split(",");

                String rawText = "";

                if (trialSentence.length == 6) {

                        rawText = trialSentence[5];

                } else if (trialSentence.length > 6) {

                        rawText = trialSentence[5];
                        for (int i = 6; i <= trialSentence.length - 1; i++) {
                                rawText = rawText + trialSentence[i];
                        }
                }

                /*
                 * else if (trialSentence.length < 6) {
                 * return null;
                 * }
                 */

                String rawAuthor = trialSentence[4];

                String rawTimeStamp = trialSentence[2];

                String[] rawTimeStamp2 = rawTimeStamp.split(" ");

                String realTimeStamp = rawTimeStamp2[1] + " " + rawTimeStamp2[2] + " " + rawTimeStamp2[5];

                String newText = rawText.replaceAll("\\.", "");

                String newText1 = newText.replaceAll("\\,", "");

                String newText2 = newText1.replaceAll("\"", "");

                String newAuthor = rawAuthor.replaceAll("\"", "");

                String newTimeStamp = realTimeStamp.replaceAll("\"", "");

                Sentence newSentence = new Sentence(newText2, newAuthor, newTimeStamp);

                return newSentence;
        }

        public static Timestamp convertStringToTimestamp(String strDate) {
                try {
                        DateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
                        // you can change format of date
                        Date date = formatter.parse(strDate);
                        Timestamp tweetTimeStampDate = new Timestamp(date.getTime());

                        return tweetTimeStampDate;
                } catch (ParseException exception) {
                        System.out.println("Exception :" + exception);
                        return null;
                }
        }

        public boolean keep(String temporalRange) {
                String[] dateArray = temporalRange.split("-");

                Timestamp start = convertStringToTimestamp(dateArray[0]);
                Timestamp end = convertStringToTimestamp(dateArray[1]);
                Timestamp tweet = convertStringToTimestamp(this.timestamp);

                boolean after = tweet.after(start);
                boolean before = tweet.before(end);

                if (after == true && before == true) {
                        return true;
                } else {
                        return false;
                }
        }

        public Sentence(String text, String author, String timestamp) {

                this.text = text;
                this.author = author;
                this.timestamp = timestamp;
        }

        public String getText() {
                return text;
        }

        public void setText(String text) {
                this.text = text;
        }

        public String getAuthor() {
                return author;
        }

        public void setAuthor(String author) {
                this.author = author;
        }

        public String getTimestamp() {
                return timestamp;
        }

        public void setTimestamp(String timestamp) {
                this.timestamp = timestamp;
        }

        public String toString() {

                return "{author:" + author + ", " + "sentence:" + "\"" + text + "\"" + ", timestamp:" + "\"" + timestamp
                                + "\"" + "}";
        }

        public ArrayList<String> splitSentence() {

                ArrayList<String> myList = new ArrayList<String>();
                String[] stopwords = { "a", "about", "above", "after", "again", "against", "all", "am", "an", "and",
                                      "any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below",
                                      "between", "both", "but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't",
                                      "do", "does", "doesn't", "doing", "don't", "down", "during", "each", "few", "for",
                                      "from", "further", "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he",
                                      "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself",
                                      "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is",
                                      "isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my",
                                      "myself", "no", "nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought",
                                      "our", "ours ourselves", "out", "over", "own", "same", "shan't", "she", "she'd",
                                      "she'll", "she's", "should", "shouldn't", "so", "some", "such", "than", "that",
                                      "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's",
                                      "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through",
                                      "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd", "we'll",
                                      "we're", "we've", "were", "weren't", "what", "what's", "when", "when's", "where",
                                      "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "won't",
                                      "would", "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours",
                                      "yourself", "yourselves" }; // from https://www.ranks.nl/stopwords
                String[] newList = text.split("\\s ");

                // myList.replaceAll(String::toLowerCase);

                for (int i = 0; i < newList.length; i++) {

                        boolean isStopWords = false;

                        for (int j = 0; j < stopwords.length; j++) {

                                if (newList[i].toLowerCase().equals(stopwords[j])) {
                                        isStopWords = true;
                                }
                        }
                        if (isStopWords == false) {
                                myList.add(newList[i]);
                        }
                }
                myList.replaceAll(String::toLowerCase);
                return myList;
        }

}