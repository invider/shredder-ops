public class StripTest {

    public void testSkip() {
        char[] v = {'a', 'b'};

        Strip head = new Strip(v, null);
        head.tail(new Strip(v, null));
        head.tail(new Strip(v, null));
        assert head.width() == 3;

        head = head.skip(head);
        assert head.width() == 2;
        head = head.skip(1);
        assert head.width() == 1;

        head = head.head(new Strip(v, null));
        assert head.width() == 2;
    }
}
