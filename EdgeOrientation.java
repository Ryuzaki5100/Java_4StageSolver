import java.util.Arrays;

public class EdgeOrientation implements Cloneable {
    private byte[] val;

    public static byte[] SOLVED_VAL = {3, 3, 3, 3, 2, 2, -2, -2, -3, -3, -3, -3};

    @Override
    public EdgeOrientation clone() {
        return new EdgeOrientation(this.val.clone());
    }

    public EdgeOrientation() {
        this.val = EdgeOrientation.SOLVED_VAL.clone();
    }

    public EdgeOrientation(EdgeOrientation e) {
        this.val = e.val.clone();
    }

    public EdgeOrientation(byte[] val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "EdgeOrientation{\n" +
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
