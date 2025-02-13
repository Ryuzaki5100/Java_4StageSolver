import java.util.Arrays;

public class EdgePos implements Cloneable {
    private byte[] val;

    public static byte[] SOLVED_VAL = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};

    @Override
    public EdgePos clone() {
        return new EdgePos(this.val.clone());
    }

    public EdgePos() {
        this.val = EdgePos.SOLVED_VAL.clone();
    }

    public EdgePos(EdgePos e) {
        this.val = e.val.clone();
    }

    public EdgePos(byte[] val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "EdgePos{\n" +
                "val=" + Arrays.toString(val) +
                "\n}";
    }

    public byte[] getVal() {
        return val;
    }

    public void setVal(int i, byte val) {
        this.val[i] = val;
    }
}
