import sun.text.normalizer.IntTrie;

import java.util.*;

public class StupidBackoffLanguageModel implements LanguageModel {

    protected Map<MultiGram, Integer> wordsMap;
    protected int k = 2;
    protected int wordsCount = 0;

    /**
     * Initialize your data structures in the constructor.
     */
    public StupidBackoffLanguageModel(HolbrookCorpus corpus) {
        wordsMap = new HashMap<MultiGram, Integer>();

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
                MultiGram multiGram = new MultiGram();
                for(int j = 0; j <= k && (i - j) >= 0; j++) {
                    multiGram = multiGram.getClone();
                    multiGram.data.add(0, sentence.get(i - j).getWord());
                    value = wordsMap.get(multiGram);
                    if (value == null) {
                        wordsMap.put(multiGram, 1);
                        if (j == 0) wordsCount++;
                    } else {
                        wordsMap.put(multiGram, value + 1);
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
        MultiGram muligram = new MultiGram();
        muligram.data.add(0, "about");
        muligram.data.add(0, "was");
//        System.out.println(wordsMap.get(muligram));
        double score = 0.0;
        for (int i = k; i < sentence.size(); i++) { // iterate over words in the sentence
            score += Math.log(getScore(sentence, i - k, i));
        }
        score += Math.log(getScore(sentence, 0, 0));
        score += Math.log(getScore(sentence, 0, 1));
        return score;
    }

    public double getScore(List<String> sentence, int l, int r) {
        if (l < r) {
            MultiGram multiGram1 = new MultiGram();
            MultiGram multiGram2 = new MultiGram();


            for(int i = l; i < r; i++) {
                multiGram1.data.add(sentence.get(i));
                multiGram2.data.add(sentence.get(i));
            }
            multiGram2.data.add(sentence.get(r));
            if (wordsMap.containsKey(multiGram2) && wordsMap.containsKey(multiGram1)) {
                return wordsMap.get(multiGram2) / (1.0 * wordsMap.get(multiGram1));
            } else {
                return 0.4 * getScore(sentence, l + 1, r);
            }

        } else {
            Integer value = wordsMap.get(new MultiGram(sentence.get(l)));
            value = (value == null) ? 0 : value;
            return (1.0 + value) / (sentence.size() + wordsCount);
        }

    }
}

class MultiGram {
    public List<String> data;

    MultiGram() {
        this.data = new ArrayList<String>();
    }

    MultiGram(String string) {
        this();
        data.add(string);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultiGram multiGram = (MultiGram) o;

        if (!data.equals(multiGram.data)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    MultiGram getClone() {
        MultiGram clone = new MultiGram();
        for(String string : data) {
            clone.data.add(new String(string));
        }
        return clone;
    }
}
