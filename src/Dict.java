import java.io.*;
import java.util.Set;
import java.util.TreeSet;

public class Dict {

    private Set<String> words = new TreeSet<>();

    public Dict(String path) {
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
            BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String ln;
            while((ln = in.readLine()) != null) {
                words.add(ln.toLowerCase());
            }

        } catch (Throwable e) {
            throw new RuntimeException("Can't load the dictionary", e);
        }
    }

    /**
     * Returns evaluation for a word candidate.
     *
     * @param word
     * @return 1 for a full word match, 0 - no match, value in-between for partial match
     */
    public double eval(String word) {
        if (words.contains(word.toLowerCase())) return word.length()*word.length();
        return 0;
    }

}
