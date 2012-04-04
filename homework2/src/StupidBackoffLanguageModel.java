import sun.text.normalizer.IntTrie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StupidBackoffLanguageModel implements LanguageModel {

    protected Map<String, Integer> wordsMap;
    protected int k = 2;
    protected int wordsCount = 0;

    /**
     * Initialize your data structures in the constructor.
     */
    public StupidBackoffLanguageModel(HolbrookCorpus corpus) {
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
            for (int i = 0; i < sentence.size(); i++) { // iterate over words
                String word = "";
                for(int j = 0; j <= k && (i - j) >= 0; j++) {
                    String space = (j == 0) ? "" : " ";
                    word = sentence.get(i - j).getWord() + space + word; // get the actual word
                    value = wordsMap.get(word);

                    if (value == null) {
                        wordsMap.put(word, 1);
                        if (j == 0) wordsCount++;
                    } else {
                        wordsMap.put(word, value + 1);
                    }
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
        for (int i = k; i < sentence.size(); i++) { // iterate over words in the sentence
            score += Math.log(getScore(sentence, i - k, i));
/*
            if (wordsMap.containsKey(sentence.get(i - 2) + " " + sentence.get(i - 1) + " " + sentence.get(i))) {
                score += Math.log(wordsMap.get(sentence.get(i - 2) + " " + sentence.get(i - 1) + " " + sentence.get(i)));
//                System.out.println(sentence.get(i - 2) + " " + sentence.get(i - 1));
                score -= Math.log(wordsMap.get(sentence.get(i - 2) + " " + sentence.get(i - 1)));
            } else {
                if (wordsMap.containsKey(sentence.get(i - 1) + " " + sentence.get(i))) {
                    score += Math.log(0.4 * wordsMap.get(sentence.get(i - 1) + " " + sentence.get(i)));
                    score -= Math.log(wordsMap.get(sentence.get(i - 1)));
                } else {
                    if (wordsMap.containsKey(sentence.get(i))) {
                        score += Math.log(wordsMap.get(sentence.get(i)));
                        score -= Math.log(wordsCount);
                    } else {
                        score += 0;
                    }
                }
            }
*/
        }
        score += Math.log(getScore(sentence, 0, 0));
        score += Math.log(getScore(sentence, 0, 1));
        return score;
    }

    public double getScore(List<String> sentence, int l, int r) {
        if (l < r) {
            String string1 = "";
            String string2 = "";
            for(int i = l; i < r; i++) {
                String space = (i == l) ? "" : " ";
                string1 += space + sentence.get(i);
                string2 += space + sentence.get(i);
            }
            string2 += " " + sentence.get(r);
            if (wordsMap.containsKey(string2)) {
                return wordsMap.get(string2) / (1.0 * wordsMap.get(string1));
            } else {
                return 0.4 * getScore(sentence, l + 1, r);
            }

        } else {
            Integer value = wordsMap.get(sentence.get(l));
            value = (value == null) ? 0 : value;
            return (1.0 + value) / (sentence.size() + wordsCount);
        }

    }
}
