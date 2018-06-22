import java.util.ArrayList;
import java.util.List;

public class Strip {

    public final int STRIP_WIDTH = 2;

    protected final char[] text;

    protected Strip next;

    public Strip(int height) {
        this.text = new char[height * STRIP_WIDTH];
    }

    public Strip(char[] text, Strip next) {
        this.text = text;
        this.next = next;
    }

    public void append(int line, int index, char c) {
        if (index >= STRIP_WIDTH) {
            if (this.next == null) this.next = new Strip(this.height());
            this.next.append(line, index - STRIP_WIDTH, c);
        } else {
            text[line*STRIP_WIDTH + index] = c;
        }
    }

    public Strip swap() {
        if (this.next == null) return this; // nothing to swap
        Strip first = this.next;

        this.next = first.next;
        first.next = this;

        return first;
    }

    public void scramble(int index) {
        if (this.next == null || this.next.next == null) return; // nothing to scramble

        if (index == 0) {
            Strip first = this.next;
            Strip second = first.next;

            // [0] -> [1] -> [2] -> ... => [0] -> [2] -> [1] -> ...
            this.next = second;
            first.next = second.next;
            second.next = first;

        } else this.next.scramble(index - 1);
    }

    public Strip skip(Strip s) {
        if (this.equal(s)) return this.next;

        Strip prev = this;
        Strip cur = this.next;
        while(cur != null) {
            if (cur.equal(s)) {
                prev.next = cur.next;
                return this;
            }
            prev = cur;
            cur = prev.next;
        }
        return this;
    }

    public Strip skip(int index) {
        if (index == 0) return this.next;
        Strip prev = this;
        Strip cur = this.next;
        while(cur != null && --index >= 0) {
            if (index == 0) {
                prev.next = cur.next;
            }
            prev = cur;
            cur = prev.next;
        }
        return this;
    }

    public Strip head(Strip head) {
        head.next = this;
        return head;
    }

    public Strip tail(Strip tail) {
        Strip cur = this;
        while (cur.next != null) cur = cur.next;
        cur.next = tail;

        return this;
    }

    public int width() {
        int i = 1;
        Strip s = next;

        while (s != null) {
            i++;
            s = s.next;
        }
        return i;
    }

    public int height() {
        return text.length / STRIP_WIDTH;
    }

    public String getLine(int num) {
        if (num >= this.height()) return "";
        char[] line = new char[this.width() * STRIP_WIDTH];

        int i = 0;
        Strip s = this;
        while (s != null) {
            int shift = num * STRIP_WIDTH;
            for (int j = 0; j < STRIP_WIDTH; j++) {
                line[i++] = s.text[shift] != 0? s.text[shift] : ' ';
                shift++;
            }
            s = s.next;
        }
        return new String(line);
    }

    public Strip copy() {
        if (this.next != null) return new Strip(this.text, this.next.copy());
        else return new Strip(this.text, null);
    }

    public Strip copyOne() {
        return new Strip(this.text, null);
    }

    public boolean equal(Object o) {
        if (!(o instanceof Strip)) return false;
        Strip c = (Strip)o;
        if (this.text == c.text) return true;
        else return false;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < this.height(); i++) buf.append(this.getLine(i)).append("\n");
        return buf.toString();
    }
}
