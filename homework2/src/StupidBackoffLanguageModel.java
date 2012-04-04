import sun.text.normalizer.IntTrie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                    multiGram.data.add(0, sentence.get(i - j).getWord());
                    value = wordsMap.get(multiGram);
                    if (value == null) {
                        wordsMap.put(multiGram, 1);
                        if (j == 0) wordsCount++;
                    } else {
                        wordsMap.put(multiGram, value + 1);
                    }
                    if (multiGram.data.size() == 2 && multiGram.data.get(0).equals("was") && multiGram.data.get(1).equals("about")) {
                        MultiGram muligram = new MultiGram();
                        muligram.data.add(0, "about");
                        muligram.data.add(0, "was");
//                        System.out.println(muligram.data);

//                        System.out.println(wordsMap.get(muligram));
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
            MultiGram multiGram1 = new MultiGram();
            MultiGram multiGram2 = new MultiGram();

            multiGram2.data.add(sentence.get(r));
            for(int i = r - 1; i >= l; i--) {
                multiGram1.data.add(0, sentence.get(i));
                multiGram2.data.add(0, sentence.get(i));
            }

//            for(int i = l; i < r; i++) {
//                multiGram1.data.add(sentence.get(i));
//                multiGram2.data.add(sentence.get(i));
//            }
//            multiGram2.data.add(sentence.get(r));
            if (wordsMap.containsKey(multiGram2) && wordsMap.containsKey(multiGram1)) {
//                System.out.print(multiGram2.data + "-" + wordsMap.get(multiGram2));
//                System.out.println(multiGram1.data + "-" + wordsMap.get(multiGram1));
                if (multiGram1.data.size() == 2 && multiGram1.data.get(0).equals("was") && multiGram1.data.get(1).equals("about")) {
                    int tt = multiGram1.hashCode();
                    tt = tt + 0;

                }

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
//        if (data.size() != multiGram.data.size()) return false;
//        for (int i = 0; i < data.size(); i++) {
//            if (!data.get(i).equals(multiGram.data.get(i))) return false;
//
//        }

        return true;
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }
}
