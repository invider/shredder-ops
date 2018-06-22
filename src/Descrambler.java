import java.util.Date;

public class Descrambler {

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
        String[] words = line.split(" ");

        boolean middleMissPenalty = false;
        if (words.length > 2) middleMissPenalty = true;

        for (String w: words) {
            double v = this.dict.eval(w);
            score += this.dict.eval(w);
        }
        return score;
    }

    private double eval(Strip strip) {
        double score = 0;
        for (int i = 0; i < strip.height(); i++) {
            score += this.eval(strip.getLine(i));
        }
        return score;
    }

    private Strip glue(Strip source, Strip glued) {
        N++;

        if (source == null) {
            // assembled!!! time to test the whole string
            double score = this.eval(glued);


            if (score > this.topScore) {
                this.topStrip = glued;
                this.topScore = score;
                System.out.println("" + glued + "\n===  @" + score + "  #" + N + "  ===");
            } else {
                if (System.currentTimeMillis() - this.lastTime > 1000) {
                    System.out.println("" + glued + "\n===  @" + score + "  #" + N + "  ===");
                    this.lastTime = System.currentTimeMillis();
                }
            }

            return null;
        }
        //System.out.println("Gluing " + glued.getLine(0) + " => " + source.getLine(0));

        // take something from source and attach to glued
        Strip res = null;
        Strip cur = source;
        while (cur != null) {
            glue(source.copy().skip(cur), glued.copy().head(cur.copyOne()));
            glue(source.copy().skip(cur), glued.copy().tail(cur.copyOne()));
            cur = cur.next;
        }

        return res;
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
