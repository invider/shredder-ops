import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ShredderOps {

    public static Strip scramble(Strip source) {
        int width = source.width() - 1;
        if (width < 2) return source;

        for (int i = 0; i < width*width*width; i++) {
            int target = (int)Math.floor(Math.random() * width);

            if (target == 0) source = source.swap();
            else source.scramble(target-1);
        }
        return source;
    }

    public static Strip shred(List<String> text) {
        Strip first = new Strip(text.size());

        for (int l = 0; l < text.size(); l++) {
            char[] ln = text.get(l).toCharArray();
            for (int i = 0; i < ln.length; i++) {
                first.append(l, i, ln[i]);
            }
        }

        return first;
    }

    public static Strip loadInput(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            List<String> inputLines = new ArrayList<String>();

            String ln;
            while ((ln = br.readLine()) != null) inputLines.add(ln);
            return shred(inputLines);

        } catch (Throwable t) {
            throw new RuntimeException("Can't loat input data");
        }
    }


    public static void main(String args[]) {
        try {
            // handle arguments
            boolean scramble = false;
            FileInputStream in = null;
            if (args.length > 0) {
                if (args[0].equals("scramble")) {
                    scramble = true;
                    if (args.length > 1) in = new FileInputStream(args[1]);
                } else in = new FileInputStream(args[0]);
            }

            // load stdin/file
            Strip source;
            if (in != null) source = loadInput(in);
            else source = loadInput(System.in);

            System.out.println(source);
            System.out.println("------------------------------------");

            if (scramble) {
                source = scramble(source);
                System.out.println(source);
            }

            Dict dict = new Dict("dict_en.txt");
            Descrambler descr = new Descrambler(dict);

            Strip out = descr.descramble(source);
            if (out == null) System.out.println("Unable to descramble!");
            else System.out.println("=== Descrambled ===\n" + out);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

