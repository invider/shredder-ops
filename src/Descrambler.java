import java.util.Set;
import java.util.TreeSet;

public class Descrambler {

    private class Val implements Comparable<Val> {
        double weight;
        Strip source;
        Strip glue;

        Val(Strip source, Strip glue, double weight) {
            this.source = source;
            this.glue = glue;
            this.weight = weight;
        }

        @Override
        public int compareTo(Val o) {
            return (int)(this.weight - o.weight);
        }
    }

    private final Dict dict;

    private Strip topStrip;

    private double topScore = 0;

    private long N = 0;

    private long lastTime;

    public Descrambler(Dict dict) {
        this.dict = dict;
    }

    private double eval(String line) {
        double score = 0;
        String[] words = line.split("\\W+");

        int last = words.length - 1;
        boolean middleMissPenalty = false;
        if (last > 1) middleMissPenalty = true;

        int wrong = 0;
        int cur = 0;
        for (String w: words) {
            double v = this.dict.eval(w);
            score += this.dict.eval(w);

            if (v == 0) wrong++;
            if (middleMissPenalty && cur > 0 && cur < last && v == 0) {
                score -= 1000;
            }
            cur++;
        }

        if (words.length > 0 && wrong == 0)  score *= 2; // all words are OK!
        return score;
    }

    private double eval(Strip strip) {
        double score = 0;
        for (int i = 0; i < strip.height(); i++) {
            score += this.eval(strip.getLine(i));
        }
        return score;
    }

    private void glue(Strip source, Strip glued) {
        N++;

        double score = this.eval(glued);
        if (score < 0) return;

        if (System.currentTimeMillis() - this.lastTime > 1000) {
            System.out.println("" + glued + "\n===  Score: " + score + "  Steps: " + N + "  ===");
            this.lastTime = System.currentTimeMillis();
        }

        if (source == null) {
            // assembled!!! time to test the whole string
            if (score > this.topScore) {
                this.topStrip = glued;
                this.topScore = score;
                System.out.println("" + glued + "\n===  !!! Top Score: " + score + "  Steps: " + N + "  ===");
            }
            return;
        }
        //System.out.println("Gluing " + glued.getLine(0) + " => " + source.getLine(0));

        // take something from source and attach to glued
        Set<Val> solutions = new TreeSet<>();
        Strip cur = source;
        while (cur != null) {
            /*
            Strip nextSource = source.copy().skip(cur);
            Strip rightGlue = glued.copy().head(cur.copyOne());
            Strip leftGlue = glued.copy().tail(cur.copyOne());

            solutions.add(new Val(nextSource, rightGlue, eval(rightGlue)));
            solutions.add(new Val(nextSource, rightGlue, eval(leftGlue)));
            */
            glue(source.copy().skip(cur), glued.copy().head(cur.copyOne()));
            glue(source.copy().skip(cur), glued.copy().tail(cur.copyOne()));
            cur = cur.next;
        }

    }

    public synchronized Strip descramble(Strip source) {
        Strip res = null;
        Strip cur = source;

        // reset parsing state
        this.N = 0;
        this.topScore = 0;
        this.topStrip = null;
        this.lastTime = System.currentTimeMillis();

        glue(source.copy().skip(cur), cur.copyOne());

        System.out.println("Result score: " + this.topScore);
        System.out.println("Tries: " + N);

        return this.topStrip;
    }
}
