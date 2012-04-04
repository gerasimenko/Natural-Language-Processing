import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class LaplaceBigramLanguageModel implements LanguageModel {

    public Map<Bigram, Integer> bigramMap;

    /**
     * Initialize your data structures in the constructor.
     */
    public LaplaceBigramLanguageModel(HolbrookCorpus corpus) {
        bigramMap = new HashMap<Bigram, Integer>();
        train(corpus);
    }

    /**
     * Takes a corpus and trains your language model.
     * Compute any counts or other corpus statistics in this function.
     */
    public void train(HolbrookCorpus corpus) {
        Integer value;
        Bigram bigram;
        for(Sentence sentence : corpus.getData()) {
            for(int i = 0; i < sentence.size() - 1; i++) {
                bigram = new Bigram(sentence.get(i).getWord(), sentence.get(i + 1).getWord());
                value = bigramMap.get(bigram);
                if (value == null) {
                    bigramMap.put(bigram, 1);
                } else {
                    bigramMap.put(bigram, value + 1);
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
        Bigram bigram;
        for(int i = 0; i < sentence.size() - 1; i++) {
            bigram = new Bigram(sentence.get(i), sentence.get(i + 1));
            Integer value = bigramMap.get(bigram);
            if (value == null) value = 0;
            score += Math.log((1.0 + value) / (sentence.size() -1 + bigramMap.size()));
        }
        return score;
    }
}

class Bigram {
    private String s1, s2;

    Bigram(String s1, String s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    public String getS1() {
        return s1;
    }

    public String getS2() {
        return s2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bigram bigram = (Bigram) o;

        if (!s1.equals(bigram.s1)) return false;
        if (!s2.equals(bigram.s2)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = s1.hashCode();
        result = 31 * result + s2.hashCode();
        return result;
    }
}
