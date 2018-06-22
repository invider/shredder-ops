import org.junit.Test;
import static org.junit.Assert.*;

public class DictTest {

    @Test
    public void testEnglish() {
        Dict dict = new Dict("dict_en.txt");

        assertTrue(dict.eval("word") > 1);
        assertTrue(dict.eval("nothing") > 1);
        assertTrue(dict.eval("everything") > 1);
        assertTrue(dict.eval("qwerpoiu") == 0);
    }

}
