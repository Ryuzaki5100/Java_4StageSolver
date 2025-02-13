public class Corner implements Cloneable {
    private CornerPos cornerPos;
    private CornerOrientation cornerOrientation;

    @Override
    public Corner clone() {
        return new Corner(this.cornerPos.clone(), this.getCornerOrientation().clone());
    }

    public Corner() {
        this.cornerPos = new CornerPos();
        this.cornerOrientation = new CornerOrientation();
    }

    public Corner(Corner c) {
        this.setCornerPos(new CornerPos(c.getCornerPos()));
        this.setCornerOrientation(new CornerOrientation(c.getCornerOrientation()));

    }

    public Corner(CornerPos cornerPos, CornerOrientation cornerOrientation) {
        this.cornerPos = cornerPos;
        this.cornerOrientation = cornerOrientation;
    }

    @Override
    public String toString() {
        return "Corner{\n" +
                "cornerPos=" + cornerPos +
                ",\ncornerOrientation=" + cornerOrientation +
                "\n}";
    }

    public CornerPos getCornerPos() {
        return cornerPos;
    }

    public CornerOrientation getCornerOrientation() {
        return cornerOrientation;
    }

    public void setCornerOrientation(CornerOrientation cornerOrientation) {
        this.cornerOrientation = cornerOrientation;
    }

    public void setCornerPos(CornerPos cornerPos) {
        this.cornerPos = cornerPos;
    }
}
