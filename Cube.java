import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class Cube implements Cloneable {

    public static Map<Character, EdgePos> nextEdgePos = Map.of(
            'R', new EdgePos(new byte[]{0, 5, 2, 3, 4, 9, 1, 7, 8, 6, 10, 11}),
            'U', new EdgePos(new byte[]{1, 2, 3, 0, 4, 5, 6, 7, 8, 9, 10, 11}),
            'F', new EdgePos(new byte[]{0, 1, 6, 3, 4, 5, 10, 2, 8, 9, 7, 11}),
            'B', new EdgePos(new byte[]{4, 1, 2, 3, 8, 0, 6, 7, 5, 9, 10, 11}),
            'L', new EdgePos(new byte[]{0, 1, 2, 7, 3, 5, 6, 11, 8, 9, 10, 4}),
            'D', new EdgePos(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 11, 8, 9, 10})
    );

    public static Map<Character, CornerPos> nextCornerPos = Map.of(
            'R', new CornerPos(new byte[]{0, 5, 1, 3, 4, 6, 2, 7}),
            'U', new CornerPos(new byte[]{1, 2, 3, 0, 4, 5, 6, 7}),
            'F', new CornerPos(new byte[]{0, 1, 6, 2, 4, 5, 7, 3}),
            'B', new CornerPos(new byte[]{4, 0, 2, 3, 5, 1, 6, 7}),
            'L', new CornerPos(new byte[]{3, 1, 2, 7, 0, 5, 6, 4}),
            'D', new CornerPos(new byte[]{0, 1, 2, 3, 7, 4, 5, 6})
    );

    public static Map<Character, List<Map<Byte, Byte>>> nextEdgeOrientation = Map.of(
            'R', List.of(
                    Map.of((byte) 3, (byte) 3, (byte) 2, (byte) 2),
                    Map.of((byte) 3, (byte) 2, (byte) 1, (byte) 1),
                    Map.of((byte) 3, (byte) 3, (byte) -2, (byte) -2),
                    Map.of((byte) 3, (byte) 3, (byte) -1, (byte) -1),
                    Map.of((byte) 2, (byte) 2, (byte) -1, (byte) -1),
                    Map.of((byte) 2, (byte) -3, (byte) 1, (byte) 1),
                    Map.of((byte) -2, (byte) 3, (byte) 1, (byte) 1),
                    Map.of((byte) -2, (byte) -2, (byte) -1, (byte) -1),
                    Map.of((byte) 2, (byte) 2, (byte) -3, (byte) -3),
                    Map.of((byte) 1, (byte) 1, (byte) -3, (byte) -2),
                    Map.of((byte) -3, (byte) -3, (byte) -2, (byte) -2),
                    Map.of((byte) -3, (byte) -3, (byte) -1, (byte) -1)
            ),
            'U', List.of(
                    Map.of((byte) 3, (byte) 3, (byte) 2, (byte) 1),
                    Map.of((byte) 3, (byte) 3, (byte) 1, (byte) -2),
                    Map.of((byte) 3, (byte) 3, (byte) -2, (byte) -1),
                    Map.of((byte) 3, (byte) 3, (byte) -1, (byte) 2),
                    Map.of((byte) 2, (byte) 2, (byte) -1, (byte) -1),
                    Map.of((byte) 2, (byte) 2, (byte) 1, (byte) 1),
                    Map.of((byte) -2, (byte) -2, (byte) 1, (byte) 1),
                    Map.of((byte) -2, (byte) -2, (byte) -1, (byte) -1),
                    Map.of((byte) 2, (byte) 2, (byte) -3, (byte) -3),
                    Map.of((byte) 1, (byte) 1, (byte) -3, (byte) -3),
                    Map.of((byte) -3, (byte) -3, (byte) -2, (byte) -2),
                    Map.of((byte) -3, (byte) -3, (byte) -1, (byte) -1)
            ),
            'F', List.of(
                    Map.of((byte) 3, (byte) 3, (byte) 2, (byte) 2),
                    Map.of((byte) 3, (byte) 3, (byte) 1, (byte) 1),
                    Map.of((byte) 3, (byte) 1, (byte) -2, (byte) -2),
                    Map.of((byte) 3, (byte) 3, (byte) -1, (byte) -1),
                    Map.of((byte) 2, (byte) 2, (byte) -1, (byte) -1),
                    Map.of((byte) 2, (byte) 2, (byte) 1, (byte) 1),
                    Map.of((byte) -2, (byte) -2, (byte) 1, (byte) -3),
                    Map.of((byte) -2, (byte) -2, (byte) -1, (byte) 3),
                    Map.of((byte) 2, (byte) 2, (byte) -3, (byte) -3),
                    Map.of((byte) 1, (byte) 1, (byte) -3, (byte) -3),
                    Map.of((byte) -3, (byte) -1, (byte) -2, (byte) -2),
                    Map.of((byte) -3, (byte) -3, (byte) -1, (byte) -1)
            ),
            'B', List.of(
                    Map.of((byte) 3, (byte) -1, (byte) 2, (byte) 2),
                    Map.of((byte) 3, (byte) 3, (byte) 1, (byte) 1),
                    Map.of((byte) 3, (byte) 3, (byte) -2, (byte) -2),
                    Map.of((byte) 3, (byte) 3, (byte) -1, (byte) -1),
                    Map.of((byte) 2, (byte) 2, (byte) -1, (byte) -3),
                    Map.of((byte) 2, (byte) 2, (byte) 1, (byte) 3),
                    Map.of((byte) -2, (byte) -2, (byte) 1, (byte) 1),
                    Map.of((byte) -2, (byte) -2, (byte) -1, (byte) -1),
                    Map.of((byte) 2, (byte) 2, (byte) -3, (byte) 1),
                    Map.of((byte) 1, (byte) 1, (byte) -3, (byte) -3),
                    Map.of((byte) -3, (byte) -3, (byte) -2, (byte) -2),
                    Map.of((byte) -3, (byte) -3, (byte) -1, (byte) -1)
            ),
            'L', List.of(
                    Map.of((byte) 3, (byte) 3, (byte) 2, (byte) 2),
                    Map.of((byte) 3, (byte) 3, (byte) 1, (byte) 1),
                    Map.of((byte) 3, (byte) 3, (byte) -2, (byte) -2),
                    Map.of((byte) 3, (byte) -2, (byte) -1, (byte) -1),
                    Map.of((byte) 2, (byte) 3, (byte) -1, (byte) -1),
                    Map.of((byte) 2, (byte) 2, (byte) 1, (byte) 1),
                    Map.of((byte) -2, (byte) -2, (byte) 1, (byte) 1),
                    Map.of((byte) -2, (byte) -3, (byte) -1, (byte) -1),
                    Map.of((byte) 2, (byte) 2, (byte) -3, (byte) -3),
                    Map.of((byte) 1, (byte) 1, (byte) -3, (byte) -3),
                    Map.of((byte) -3, (byte) -3, (byte) -2, (byte) -2),
                    Map.of((byte) -3, (byte) 2, (byte) -1, (byte) -1)
            ),
            'D', List.of(
                    Map.of((byte) 3, (byte) 3, (byte) 2, (byte) 2),
                    Map.of((byte) 3, (byte) 3, (byte) 1, (byte) 1),
                    Map.of((byte) 3, (byte) 3, (byte) -2, (byte) -2),
                    Map.of((byte) 3, (byte) 3, (byte) -1, (byte) -1),
                    Map.of((byte) 2, (byte) 2, (byte) -1, (byte) -1),
                    Map.of((byte) 2, (byte) 2, (byte) 1, (byte) 1),
                    Map.of((byte) -2, (byte) -2, (byte) 1, (byte) 1),
                    Map.of((byte) -2, (byte) -2, (byte) -1, (byte) -1),
                    Map.of((byte) 2, (byte) -1, (byte) -3, (byte) -3),
                    Map.of((byte) 1, (byte) 2, (byte) -3, (byte) -3),
                    Map.of((byte) -3, (byte) -3, (byte) -2, (byte) 1),
                    Map.of((byte) -3, (byte) -3, (byte) -1, (byte) -2)
            )
    );

    public static Map<Character, List<Map<Byte, Byte>>> nextCornerOrientation = Map.of(
            'R', List.of(
                    Map.of((byte) -1, (byte) -1, (byte) 2, (byte) 2, (byte) 3, (byte) 3),
                    Map.of((byte) 1, (byte) 1, (byte) 2, (byte) -3, (byte) 3, (byte) 2),
                    Map.of((byte) 1, (byte) 1, (byte) -2, (byte) 3, (byte) 3, (byte) 2),
                    Map.of((byte) -1, (byte) -1, (byte) -2, (byte) -2, (byte) 3, (byte) 3),
                    Map.of((byte) -1, (byte) -1, (byte) 2, (byte) 2, (byte) -3, (byte) -3),
                    Map.of((byte) 1, (byte) 1, (byte) 2, (byte) -3, (byte) -3, (byte) -2),
                    Map.of((byte) 1, (byte) 1, (byte) -2, (byte) 3, (byte) -3, (byte) -2),
                    Map.of((byte) -1, (byte) -1, (byte) -2, (byte) -2, (byte) -3, (byte) -3)
            ),
            'U', List.of(
                    Map.of((byte) -1, (byte) 2, (byte) 2, (byte) 1, (byte) 3, (byte) 3),
                    Map.of((byte) 1, (byte) -2, (byte) 2, (byte) 1, (byte) 3, (byte) 3),
                    Map.of((byte) 1, (byte) -2, (byte) -2, (byte) -1, (byte) 3, (byte) 3),
                    Map.of((byte) -1, (byte) 2, (byte) -2, (byte) -1, (byte) 3, (byte) 3),
                    Map.of((byte) -1, (byte) -1, (byte) 2, (byte) 2, (byte) -3, (byte) -3),
                    Map.of((byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) -3, (byte) -3),
                    Map.of((byte) 1, (byte) 1, (byte) -2, (byte) -2, (byte) -3, (byte) -3),
                    Map.of((byte) -1, (byte) -1, (byte) -2, (byte) -2, (byte) -3, (byte) -3)
            ),
            'F', List.of(
                    Map.of((byte) -1, (byte) -1, (byte) 2, (byte) 2, (byte) 3, (byte) 3),
                    Map.of((byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) 3, (byte) 3),
                    Map.of((byte) 1, (byte) -3, (byte) -2, (byte) -2, (byte) 3, (byte) 1),
                    Map.of((byte) -1, (byte) 3, (byte) -2, (byte) -2, (byte) 3, (byte) 1),
                    Map.of((byte) -1, (byte) -1, (byte) 2, (byte) 2, (byte) -3, (byte) -3),
                    Map.of((byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) -3, (byte) -3),
                    Map.of((byte) 1, (byte) -3, (byte) -2, (byte) -2, (byte) -3, (byte) -1),
                    Map.of((byte) -1, (byte) 3, (byte) -2, (byte) -2, (byte) -3, (byte) -1)
            ),
            'B', List.of(
                    Map.of((byte) -1, (byte) -3, (byte) 2, (byte) 2, (byte) 3, (byte) -1),
                    Map.of((byte) 1, (byte) 3, (byte) 2, (byte) 2, (byte) 3, (byte) -1),
                    Map.of((byte) 1, (byte) 1, (byte) -2, (byte) -2, (byte) 3, (byte) 3),
                    Map.of((byte) -1, (byte) -1, (byte) -2, (byte) -2, (byte) 3, (byte) 3),
                    Map.of((byte) -1, (byte) -3, (byte) 2, (byte) 2, (byte) -3, (byte) 1),
                    Map.of((byte) 1, (byte) 3, (byte) 2, (byte) 2, (byte) -3, (byte) 1),
                    Map.of((byte) 1, (byte) 1, (byte) -2, (byte) -2, (byte) -3, (byte) -3),
                    Map.of((byte) -1, (byte) -1, (byte) -2, (byte) -2, (byte) -3, (byte) -3)
            ),
            'L', List.of(
                    Map.of((byte) -1, (byte) -1, (byte) 2, (byte) 3, (byte) 3, (byte) -2),
                    Map.of((byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) 3, (byte) 3),
                    Map.of((byte) 1, (byte) 1, (byte) -2, (byte) -2, (byte) 3, (byte) 3),
                    Map.of((byte) -1, (byte) -1, (byte) -2, (byte) -3, (byte) 3, (byte) -2),
                    Map.of((byte) -1, (byte) -1, (byte) 2, (byte) 3, (byte) -3, (byte) 2),
                    Map.of((byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) -3, (byte) -3),
                    Map.of((byte) 1, (byte) 1, (byte) -2, (byte) -2, (byte) -3, (byte) -3),
                    Map.of((byte) -1, (byte) -1, (byte) -2, (byte) -3, (byte) -3, (byte) 2)
            ),
            'D', List.of(
                    Map.of((byte) -1, (byte) -1, (byte) 2, (byte) 2, (byte) 3, (byte) 3),
                    Map.of((byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) 3, (byte) 3),
                    Map.of((byte) 1, (byte) 1, (byte) -2, (byte) -2, (byte) 3, (byte) 3),
                    Map.of((byte) -1, (byte) -1, (byte) -2, (byte) -2, (byte) 3, (byte) 3),
                    Map.of((byte) -1, (byte) -2, (byte) 2, (byte) -1, (byte) -3, (byte) -3),
                    Map.of((byte) 1, (byte) 2, (byte) 2, (byte) -1, (byte) -3, (byte) -3),
                    Map.of((byte) 1, (byte) 2, (byte) -2, (byte) 1, (byte) -3, (byte) -3),
                    Map.of((byte) -1, (byte) -2, (byte) -2, (byte) 1, (byte) -3, (byte) -3)
            )
    );

    public static byte[][] edgeList = {
            {1, 37},
            {5, 28},
            {7, 19},
            {3, 10},
            {12, 41},
            {32, 39},
            {23, 30},
            {14, 21},
            {43, 52},
            {34, 50},
            {25, 46},
            {16, 48}
    };

    public static byte[][] cornerList = {
            {0, 9, 38},
            {2, 29, 36},
            {8, 20, 27},
            {6, 11, 18},
            {15, 44, 51},
            {35, 42, 53},
            {26, 33, 47},
            {17, 24, 45}
    };

    public static Map<Character, Byte> binEncoding = Map.of(
            'U', (byte) 0b100000,
            'L', (byte) 0b010000,
            'F', (byte) 0b001000,
            'R', (byte) 0b000100,
            'B', (byte) 0b000010,
            'D', (byte) 0b000001
    );

    public static Map<Character, Byte> priority = Map.of(
            'U', (byte) 2,
            'L', (byte) 0,
            'F', (byte) 1,
            'R', (byte) 0,
            'B', (byte) 1,
            'D', (byte) 2
    );

    public static Map<Byte, Byte> edgeNumberForPos;
    public static Map<Byte, Byte> cornerNumberForPos;

    static {
        Map<Byte, Byte> edgeMap = new HashMap<>();
        edgeMap.put((byte) 0b100010, (byte) 0);
        edgeMap.put((byte) 0b100100, (byte) 1);
        edgeMap.put((byte) 0b101000, (byte) 2);
        edgeMap.put((byte) 0b110000, (byte) 3);
        edgeMap.put((byte) 0b010010, (byte) 4);
        edgeMap.put((byte) 0b000110, (byte) 5);
        edgeMap.put((byte) 0b001100, (byte) 6);
        edgeMap.put((byte) 0b011000, (byte) 7);
        edgeMap.put((byte) 0b000011, (byte) 8);
        edgeMap.put((byte) 0b000101, (byte) 9);
        edgeMap.put((byte) 0b001001, (byte) 10);
        edgeMap.put((byte) 0b010001, (byte) 11);
        edgeNumberForPos = Collections.unmodifiableMap(edgeMap);

        Map<Byte, Byte> cornerMap = new HashMap<>();
        cornerMap.put((byte) 0b110010, (byte) 0);
        cornerMap.put((byte) 0b100110, (byte) 1);
        cornerMap.put((byte) 0b101100, (byte) 2);
        cornerMap.put((byte) 0b111000, (byte) 3);
        cornerMap.put((byte) 0b010011, (byte) 4);
        cornerMap.put((byte) 0b000111, (byte) 5);
        cornerMap.put((byte) 0b001101, (byte) 6);
        cornerMap.put((byte) 0b011001, (byte) 7);
        cornerNumberForPos = Collections.unmodifiableMap(cornerMap);
    }

    public static int[][] edgePossiblePlacesStage3 = {
            {1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1},
            {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
            {1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1}};

    public static int[][] cornerPossiblePlacesStage3 = {
            {1, 0, 1, 0, 0, 1, 0, 1},
            {0, 1, 0, 1, 1, 0, 1, 0},
            {1, 0, 1, 0, 0, 1, 0, 1},
            {0, 1, 0, 1, 1, 0, 1, 0},
            {0, 1, 0, 1, 1, 0, 1, 0},
            {1, 0, 1, 0, 0, 1, 0, 1},
            {0, 1, 0, 1, 1, 0, 1, 0},
            {1, 0, 1, 0, 0, 1, 0, 1}};

    public static String[][] stagedMoveRestrictions = {
            {"U", "UU", "UUU", "R", "RR", "RRR", "F", "FF", "FFF", "B", "BB", "BBB", "L", "LL", "LLL", "D", "DD", "DDD"},
            {"U", "UU", "UUU", "R", "RR", "RRR", "FF", "BB", "L", "LL", "LLL", "D", "DD", "DDD"},
            {"U", "UU", "UUU", "RR", "FF", "BB", "LL", "D", "DD", "DDD"},
            {"UU", "RR", "FF", "BB", "LL", "DD"}
    };

    private Edge edge;
    private Corner corner;

    public Cube() {
        this.edge = new Edge();
        this.corner = new Corner();
    }

    public Cube(Cube c) {
        this.setEdge(new Edge(c.getEdge()));
        this.setCorner(new Corner(c.getCorner()));
    }

    public Cube(Edge edge, Corner corner) {
        this.edge = edge;
        this.corner = corner;
    }

    public Cube(String colorInput) {
        Cube c = new Cube();

        EdgePos edgePos = c.getEdge().getEdgePos();
        EdgeOrientation edgeOrientation = c.getEdge().getEdgeOrientation();

        CornerPos cornerPos = c.getCorner().getCornerPos();
        CornerOrientation cornerOrientation = c.getCorner().getCornerOrientation();

        byte[] basicPositionsInfo = {4, 13, 22, 31, 40, 49}, basicOrientationInfo = {3, -1, -2, 1, 2, -3};
        String basicPositions = "ULFRBD", givenPositions = "";

        HashMap<Character, Character> colorToSide = new HashMap<>();

        for (int i = 0; i < basicOrientationInfo.length; i++)
            givenPositions += colorInput.charAt(basicPositionsInfo[i]);

        for (int i = 0; i < 6; i++)
            colorToSide.put(givenPositions.charAt(i), basicPositions.charAt(i));

        byte tempCounter = 0;

        for (int i = 0; i < edgeList.length; i++) {
            char side1 = colorToSide.get(colorInput.charAt(edgeList[i][0]));
            char side2 = colorToSide.get(colorInput.charAt(edgeList[i][1]));

            byte binaryNum = (byte) (binEncoding.get(side1) ^ binEncoding.get(side2));

            edgePos.setVal(edgeNumberForPos.get(binaryNum), tempCounter++);

            byte priorityNumber = (byte) Math.max(priority.get(side1), priority.get(side2));
            byte referenceNumber = priorityNumber == priority.get(side1) ? edgeList[i][0] : edgeList[i][1];

            edgeOrientation.setVal(edgeNumberForPos.get(binaryNum), basicOrientationInfo[referenceNumber / 9]);
        }

        tempCounter = 0;

        for (int i = 0; i < cornerList.length; i++) {
            char side1 = colorToSide.get(colorInput.charAt(cornerList[i][0]));
            char side2 = colorToSide.get(colorInput.charAt(cornerList[i][1]));
            char side3 = colorToSide.get(colorInput.charAt(cornerList[i][2]));

            byte binaryNum = (byte) (binEncoding.get(side1) ^ binEncoding.get(side2) ^ binEncoding.get(side3));

            cornerPos.setVal(cornerNumberForPos.get(binaryNum), tempCounter++);

            byte priorityNumber = (byte) Math.max(priority.get(side1), Math.max(priority.get(side2), priority.get(side3)));
            byte referenceNumber = priorityNumber == priority.get(side1) ? cornerList[i][0] : (priorityNumber == priority.get(side2) ? cornerList[i][1] : cornerList[i][2]);

            cornerOrientation.setVal(cornerNumberForPos.get((binaryNum)), basicOrientationInfo[referenceNumber / 9]);
        }

        this.setEdge(new Edge(edgePos, edgeOrientation));
        this.setCorner(new Corner(cornerPos, cornerOrientation));
    }

    @Override
    public Cube clone() {
        return new Cube(this.getEdge().clone(), this.getCorner().clone());
    }

    public static Cube execute(Cube c, String s) {
        Cube temp = c.clone();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            EdgePos edgePos = temp.getEdge().getEdgePos().clone();
            EdgeOrientation edgeOrientation = temp.getEdge().getEdgeOrientation().clone();


            for (int j = 0; j < 12; j++) {
                edgeOrientation.setVal(j, nextEdgeOrientation.get(ch).get(edgePos.getVal()[j]).get(edgeOrientation.getVal()[j]));
                edgePos.setVal(j, nextEdgePos.get(ch).getVal()[edgePos.getVal()[j]]);
            }

            temp.setEdge(new Edge(edgePos, edgeOrientation));

            CornerPos cornerPos = temp.getCorner().getCornerPos().clone();
            CornerOrientation cornerOrientation = temp.getCorner().getCornerOrientation().clone();

            for (int j = 0; j < 8; j++) {
                cornerOrientation.setVal(j, nextCornerOrientation.get(ch).get(cornerPos.getVal()[j]).get(cornerOrientation.getVal()[j]));
                cornerPos.setVal(j, nextCornerPos.get(ch).getVal()[cornerPos.getVal()[j]]);
            }

            temp.setCorner(new Corner(cornerPos, cornerOrientation));
        }
        return temp;
    }

    public static String reverseAlgorithm(String s) {
        String result = "";
        for (int i = 0; i < s.length(); i++)
            for (int j = 0; j < 3; j++)
                result += s.charAt(i);
        return new StringBuilder(result).reverse().toString();
    }

    public static ArrayList<String> getAlgorithm(String moves) {
        class Temp {
            char ch;
            byte b;

            public Temp(char ch, byte b) {
                this.ch = ch;
                this.b = b;
            }
        }

        Stack<Temp> s = new Stack<>();

        ArrayList<String> v = new ArrayList<>(Arrays.asList("", "", "2", "\'"));
        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < moves.length(); i++) {
            if (s.isEmpty() || s.peek().ch != moves.charAt(i))
                s.push(new Temp(moves.charAt(i), (byte) 1));
            else {
                Temp x = s.pop();
                if (x.b != (byte) 3)
                    s.push(new Temp(x.ch, (byte) (x.b + 1)));
            }
        }

        while (!s.isEmpty()) {
            Temp x = s.pop();
            if (x.b != 0)
                result.add(0, x.ch + v.get(x.b));
        }

        return result;
    }

    public static Integer maskStage1(Cube c) {
        ArrayList<Integer> mask = new ArrayList<>();

        for (int i = 0; i < 12; i++)
            mask.add(0);

        EdgePos edgePos = c.getEdge().getEdgePos();
        EdgeOrientation edgeOrientation = c.getEdge().getEdgeOrientation();

        for (int i = 0; i < 12; i++) {
            int edgePiecePosition = edgePos.getVal()[i];
            int edgePieceOrientation = edgeOrientation.getVal()[i];

            if (edgePiecePosition < 4 || edgePiecePosition > 7) {
                if (Math.abs(edgePieceOrientation) == 3)
                    mask.set(edgePiecePosition, 1);
            } else {
                if (Math.abs(edgePieceOrientation) == 2)
                    mask.set(edgePiecePosition, 1);
            }
        }

        return mask.hashCode();
    }

    public static Integer maskStage2(Cube c) {
        ArrayList<Integer> mask = new ArrayList<>();
        ArrayList<Integer> cornerMask = new ArrayList<>();
        ArrayList<Integer> edgeMask = new ArrayList<>();

        for (int i = 0; i < 8; i++)
            cornerMask.add(0);

        for (int i = 0; i < 12; i++)
            edgeMask.add(0);

        for (int i = 0; i < 8; i++) {
            int cornerPiecePosition = c.getCorner().getCornerPos().getVal()[i];
            int cornerPieceOrientation = c.getCorner().getCornerOrientation().getVal()[i];

            cornerMask.set(cornerPiecePosition, cornerPieceOrientation);
        }

        for (int i = 0; i < 12; i++) {
            int edgePiecePosition = c.getEdge().getEdgePos().getVal()[i];

            boolean positionGroup = edgePiecePosition < 4 || edgePiecePosition > 7;
            boolean pieceGroup = i < 4 || i > 7;

            edgeMask.set(edgePiecePosition, positionGroup != pieceGroup ? 1 : 0);
        }

        for (Integer i : cornerMask)
            mask.add(i);

        for (Integer i : edgeMask)
            mask.add(i);

        return mask.hashCode();
    }

    public static Integer maskStage3(Cube c) {
        ArrayList<Integer> mask = new ArrayList<>();
        ArrayList<Integer> parityEdges = new ArrayList<>();
        ArrayList<Integer> parityCorners = new ArrayList<>();
        ArrayList<Integer> parityCornerPos = new ArrayList<>();

        for (int i = 0; i < 12; i++)
            parityEdges.add(0);

        for (int i = 0; i < 8; i++) {
            parityCorners.add(0);
            parityCornerPos.add(0);
        }

        for (int i = 0; i < 12; i++)
            parityEdges.set(c.getEdge().getEdgePos().getVal()[i], Cube.edgePossiblePlacesStage3[c.getEdge().getEdgePos().getVal()[i]][i]);

        for (int i = 0; i < 8; i++) {
            parityCorners.set(c.getCorner().getCornerPos().getVal()[i], Cube.cornerPossiblePlacesStage3[c.getCorner().getCornerPos().getVal()[i]][i]);
            parityCornerPos.set(c.getCorner().getCornerPos().getVal()[i], i / 4);
        }

        for (int i = 0; i < 4; i++)
            mask.add(parityEdges.get(i));

        for (int i = 8; i < 12; i++)
            mask.add(parityEdges.get(i));

        for (Integer i : parityCorners)
            mask.add(i);

        for (Integer i : parityCornerPos)
            mask.add(i);

        for (byte i : c.getCorner().getCornerPos().getVal())
            mask.add((int) i);

        return mask.hashCode();
    }

    public static Integer maskStage4(Cube c) {
        ArrayList<Integer> mask = new ArrayList<>();

        for (byte i : c.getEdge().getEdgePos().getVal())
            mask.add((int) i);

        for (byte i : c.getCorner().getCornerPos().getVal())
            mask.add((int) i);

        return mask.hashCode();
    }

    public String solveStage(int stageNumber, StageMasker s) {
        Cube source = new Cube(this);
        Cube destination = new Cube();

        String[] operations = Cube.stagedMoveRestrictions[stageNumber];

        class Temp {
            Cube c;
            String s;

            public Temp(Cube c, String s) {
                this.c = c.clone();
                this.s = new String(s);
            }
        }

        Queue<Temp> forward = new LinkedBlockingQueue<>();
        Queue<Temp> backward = new LinkedBlockingQueue<>();

        HashMap<Integer, String> forwardSolution = new HashMap<>();
        HashMap<Integer, String> backwardSolution = new HashMap<>();

        forwardSolution.put(s.mask(source), "");
        backwardSolution.put(s.mask(destination), "");

        forward.add(new Temp(source, ""));
        backward.add(new Temp(destination, ""));

        while (true) {
            Temp f = forward.poll();

            for (int i = 0; i < operations.length; i++) {
                Cube temp = Cube.execute(f.c, operations[i]);

                Integer x = s.mask(temp);

                if (backwardSolution.containsKey(x))
                    return f.s + operations[i] + Cube.reverseAlgorithm(backwardSolution.get(x));


                if (!forwardSolution.containsKey(x)) {
                    forwardSolution.put(x, f.s + operations[i]);
                    forward.add(new Temp(temp, f.s + operations[i]));
                }
            }

            Temp b = backward.poll();

            for (int i = 0; i < operations.length; i++) {
                Cube temp = Cube.execute(b.c, operations[i]);

                Integer x = s.mask(temp);

                if (forwardSolution.containsKey(x))
                    return forwardSolution.get(x) + Cube.reverseAlgorithm(b.s + operations[i]);

                if (!backwardSolution.containsKey(x)) {
                    backwardSolution.put(x, b.s + operations[i]);
                    backward.add(new Temp(temp, b.s + operations[i]));
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Cube{\n" +
                "edge=" + edge.toString() +
                ",\ncorner=" + corner.toString() +
                "\n}";
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public Corner getCorner() {
        return corner;
    }

    public void setCorner(Corner corner) {
        this.corner = corner;
    }
}
