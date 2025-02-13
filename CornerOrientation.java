import java.util.Arrays;

public class CornerOrientation implements Cloneable {
    private byte[] val;

    public static byte[] SOLVED_VAL = {3, 3, 3, 3, -3, -3, -3, -3};

    @Override
    public CornerOrientation clone() {
        return new CornerOrientation(this.val.clone());
    }

    public CornerOrientation() {
        this.val = CornerOrientation.SOLVED_VAL.clone();
    }

    public CornerOrientation(CornerOrientation c) {
        this.val = c.val.clone();
    }

    public CornerOrientation(byte[] val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "CornerOrientation{\n" +
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
