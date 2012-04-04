import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaplaceUnigramLanguageModel implements LanguageModel {

    protected Map<String, Integer> wordsMap;


    /**
     * Initialize your data structures in the constructor.
     */
    public LaplaceUnigramLanguageModel(HolbrookCorpus corpus) {
        wordsMap = new HashMap<String, Integer>();

        train(corpus);
    }

    /**
     * Takes a corpus and trains your language model.
     * Compute any counts or other corpus statistics in this function.
     */
    public void train(HolbrookCorpus corpus) {
        Integer value;
        for (Sentence sentence : corpus.getData()) { // iterate over sentences
            for (Datum datum : sentence) { // iterate over words
                String word = datum.getWord(); // get the actual word
                value = wordsMap.get(word);
                if (value == null) {
                    wordsMap.put(word, 1);
                } else {
                    wordsMap.put(word, value + 1);
                }

            }
        }
    }

    /**
     * Takes a list of strings as argument and returns the log-probability of the
     * sentence using your language model. Use whatever data you computed in train() here.
     */
    public double score(List<String> sentence) {
        double score = 0.0;
        for (String word : sentence) { // iterate over words in the sentence
            Integer count = wordsMap.get(word);
            if (count == null) count = 0;
            score += Math.log((count + 1.0) / (sentence.size() + wordsMap.size()));
        }
        return score;
    }
}
