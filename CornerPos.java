import java.util.Arrays;

public class CornerPos implements Cloneable {
    private byte[] val;

    public static byte[] SOLVED_VAL = {0, 1, 2, 3, 4, 5, 6, 7};

    @Override
    public CornerPos clone() {
        return new CornerPos(this.val.clone());
    }

    public CornerPos() {
        this.val = CornerPos.SOLVED_VAL.clone();
    }

    public CornerPos(CornerPos c){
        this.val = c.val.clone();
    }

    public CornerPos(byte[] val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "CornerPos{\n" +
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
