public class Edge implements Cloneable {
    private EdgePos edgePos;
    private EdgeOrientation edgeOrientation;

    @Override
    public Edge clone() {
        return new Edge(this.getEdgePos().clone(), this.getEdgeOrientation().clone());
    }

    public Edge() {
        this.edgePos = new EdgePos();
        this.edgeOrientation = new EdgeOrientation();
    }

    public Edge(Edge e) {
        this.setEdgePos(new EdgePos(e.getEdgePos()));
        this.setEdgeOrientation(new EdgeOrientation(e.getEdgeOrientation()));
    }

    public Edge(EdgePos edgePos, EdgeOrientation edgeOrientation) {
        this.edgePos = edgePos;
        this.edgeOrientation = edgeOrientation;
    }

    @Override
    public String toString() {
        return "Edge{\n" +
                "edgePos=" + edgePos.toString() +
                ",\nedgeOrientation=" + edgeOrientation.toString() +
                "\n}";
    }

    public EdgePos getEdgePos() {
        return edgePos;
    }

    public EdgeOrientation getEdgeOrientation() {
        return edgeOrientation;
    }

    public void setEdgeOrientation(EdgeOrientation edgeOrientation) {
        this.edgeOrientation = edgeOrientation;
    }

    public void setEdgePos(EdgePos edgePos) {
        this.edgePos = edgePos;
    }
}
