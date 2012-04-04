import java.util.*;

/**
 * A uniform language model. This simply counts the vocabulary size V of the training
 * corpus and assigns p(w) = 1/V for any word.
 */
public class UniformLanguageModel implements LanguageModel {

    protected Set<String> words; // set of words that occur in training
    protected Map<String, Integer> wordsMap;

    /**
     * Initialize your data structures in the constructor.
     */
    public UniformLanguageModel() {
        words = new HashSet<String>();
        wordsMap = new HashMap<String, Integer>();
    }

    public UniformLanguageModel(HolbrookCorpus corpus) {
        words = new HashSet<String>();
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
                words.add(word);
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
        // NOTE: a simpler method would be just score = sentence.size() * - Math.log(words.size()).
        // we show the 'for' loop for insructive purposes.
        return score;
    }
}
