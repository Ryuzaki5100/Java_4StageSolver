package com.cube.demo.rbxcb.rbxcb_3x3;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Cube implements Cloneable {

    private static final Map<Character, EdgePos> nextEdgePos = Map.of(
            'R', new EdgePos(new byte[]{0, 5, 2, 3, 4, 9, 1, 7, 8, 6, 10, 11}),
            'U', new EdgePos(new byte[]{1, 2, 3, 0, 4, 5, 6, 7, 8, 9, 10, 11}),
            'F', new EdgePos(new byte[]{0, 1, 6, 3, 4, 5, 10, 2, 8, 9, 7, 11}),
            'B', new EdgePos(new byte[]{4, 1, 2, 3, 8, 0, 6, 7, 5, 9, 10, 11}),
            'L', new EdgePos(new byte[]{0, 1, 2, 7, 3, 5, 6, 11, 8, 9, 10, 4}),
            'D', new EdgePos(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 11, 8, 9, 10})
    );

    private static final Map<Character, CornerPos> nextCornerPos = Map.of(
            'R', new CornerPos(new byte[]{0, 5, 1, 3, 4, 6, 2, 7}),
            'U', new CornerPos(new byte[]{1, 2, 3, 0, 4, 5, 6, 7}),
            'F', new CornerPos(new byte[]{0, 1, 6, 2, 4, 5, 7, 3}),
            'B', new CornerPos(new byte[]{4, 0, 2, 3, 5, 1, 6, 7}),
            'L', new CornerPos(new byte[]{3, 1, 2, 7, 0, 5, 6, 4}),
            'D', new CornerPos(new byte[]{0, 1, 2, 3, 7, 4, 5, 6})
    );

    private static final Map<Character, List<Map<Byte, Byte>>> nextEdgeOrientation = Map.of(
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

    private static final Map<Character, List<Map<Byte, Byte>>> nextCornerOrientation = Map.of(
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

    private static final byte[][] edgeList = {
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

    private static final byte[][] cornerList = {
            {0, 9, 38},
            {2, 29, 36},
            {8, 20, 27},
            {6, 11, 18},
            {15, 44, 51},
            {35, 42, 53},
            {26, 33, 47},
            {17, 24, 45}
    };

    private static final Map<Character, Byte> binEncoding = Map.of(
            'U', (byte) 0b100000,
            'L', (byte) 0b010000,
            'F', (byte) 0b001000,
            'R', (byte) 0b000100,
            'B', (byte) 0b000010,
            'D', (byte) 0b000001
    );

    private static final Map<Character, Byte> priority = Map.of(
            'U', (byte) 2,
            'L', (byte) 0,
            'F', (byte) 1,
            'R', (byte) 0,
            'B', (byte) 1,
            'D', (byte) 2
    );

    private static final Map<Byte, Byte> edgeNumberForPos;
    private static final Map<Byte, Byte> cornerNumberForPos;

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

    private static final int[][] edgePossiblePlacesStage3 = {
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

    private static final int[][] cornerPossiblePlacesStage3 = {
            {1, 0, 1, 0, 0, 1, 0, 1},
            {0, 1, 0, 1, 1, 0, 1, 0},
            {1, 0, 1, 0, 0, 1, 0, 1},
            {0, 1, 0, 1, 1, 0, 1, 0},
            {0, 1, 0, 1, 1, 0, 1, 0},
            {1, 0, 1, 0, 0, 1, 0, 1},
            {0, 1, 0, 1, 1, 0, 1, 0},
            {1, 0, 1, 0, 0, 1, 0, 1}};

    private static final String[][] stagedMoveRestrictions_4Stage = {
            {"U", "UU", "UUU", "R", "RR", "RRR", "F", "FF", "FFF", "B", "BB", "BBB", "L", "LL", "LLL", "D", "DD", "DDD"},
            {"U", "UU", "UUU", "R", "RR", "RRR", "FF", "BB", "L", "LL", "LLL", "D", "DD", "DDD"},
            {"U", "UU", "UUU", "RR", "FF", "BB", "LL", "D", "DD", "DDD"},
            {"UU", "RR", "FF", "BB", "LL", "DD"}
    };

    private static final String[][] stagedMoveRestrictions_3Stage = {
            {"U", "UU", "UUU", "R", "RR", "RRR", "F", "FF", "FFF", "B", "BB", "BBB", "L", "LL", "LLL", "D", "DD", "DDD"},
            {"U", "UU", "UUU", "RR", "FF", "BB", "LL", "D", "DD", "DDD"},
            {"UU", "RR", "FF", "BB", "LL", "DD"}
    };

    private static final String[][] stagedMoveRestrictions_2Stage = {
            {"R", "RR", "RRR", "U", "UU", "UUU", "F", "FF", "FFF", "L", "LL", "LLL", "B", "BB", "BBB", "D", "DD", "DDD"},
            {"U", "UU", "UUU", "D", "DD", "DDD", "RR", "FF", "LL", "BB"}
    };

    private static final String[][][] stagedMoveRestrictions = {
            null,
            null,
            Cube.stagedMoveRestrictions_2Stage,
            Cube.stagedMoveRestrictions_3Stage,
            Cube.stagedMoveRestrictions_4Stage
    };

    private static final StageMasker[] maskStages_4Stage = {
            Cube::maskStage1_4Stage,
            Cube::maskStage2_4Stage,
            Cube::maskStage3_4Stage,
            Cube::maskStage4_4Stage
    };

    private static final StageMasker[] maskStages_3Stage = {
            Cube::maskStage1_3Stage,
            Cube::maskStage2_3Stage,
            Cube::maskStage3_3Stage
    };

    private static final StageMasker[] maskStages_2Stage = {
            Cube::maskStage1_2Stage,
            Cube::maskStage2_2Stage
    };

    private static final StageMasker[][] maskStages = {
            null,
            null,
            Cube.maskStages_2Stage,
            Cube.maskStages_3Stage,
            Cube.maskStages_4Stage
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
        String basicPositions = "ULFRBD";

        StringBuilder givenPositions = new StringBuilder();

        HashMap<Character, Character> colorToSide = new HashMap<>();

        for (int i = 0; i < basicOrientationInfo.length; i++)
            givenPositions.append(colorInput.charAt(basicPositionsInfo[i]));

        for (int i = 0; i < 6; i++)
            colorToSide.put(givenPositions.charAt(i), basicPositions.charAt(i));

        byte tempCounter = 0;

        for (byte[] bytes : edgeList) {
            char side1 = colorToSide.get(colorInput.charAt(bytes[0]));
            char side2 = colorToSide.get(colorInput.charAt(bytes[1]));

            byte binaryNum = (byte) (binEncoding.get(side1) ^ binEncoding.get(side2));

            edgePos.setVal(edgeNumberForPos.get(binaryNum), tempCounter++);

            byte priorityNumber = (byte) Math.max(priority.get(side1), priority.get(side2));
            byte referenceNumber = priorityNumber == priority.get(side1) ? bytes[0] : bytes[1];

            edgeOrientation.setVal(edgeNumberForPos.get(binaryNum), basicOrientationInfo[referenceNumber / 9]);
        }

        tempCounter = 0;

        for (byte[] bytes : cornerList) {
            char side1 = colorToSide.get(colorInput.charAt(bytes[0]));
            char side2 = colorToSide.get(colorInput.charAt(bytes[1]));
            char side3 = colorToSide.get(colorInput.charAt(bytes[2]));

            byte binaryNum = (byte) (binEncoding.get(side1) ^ binEncoding.get(side2) ^ binEncoding.get(side3));

            cornerPos.setVal(cornerNumberForPos.get(binaryNum), tempCounter++);

            byte priorityNumber = (byte) Math.max(priority.get(side1), Math.max(priority.get(side2), priority.get(side3)));
            byte referenceNumber = priorityNumber == priority.get(side1) ? bytes[0] : (priorityNumber == priority.get(side2) ? bytes[1] : bytes[2]);

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

        String[] moves = s.split(" ");

        if (moves.length > 1) {
            StringBuilder sBuilder = new StringBuilder();

            for (String string : moves) {
                if (string.length() == 1)
                    sBuilder.append(string.charAt(0));
                else if (string.charAt(1) == '2')
                    sBuilder.append(String.valueOf(string.charAt(0)).repeat(2));
                else
                    sBuilder.append(String.valueOf(string.charAt(0)).repeat(3));
            }

            s = sBuilder.toString();
        }

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
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < s.length(); i++)
            result.append(String.valueOf(s.charAt(i)).repeat(3));

        return new StringBuilder(result.toString()).reverse().toString();
    }

    public static ArrayList<String> getAlgorithm(String moves) {
        class Temp {
            final char ch;
            final byte b;

            public Temp(char ch, byte b) {
                this.ch = ch;
                this.b = b;
            }
        }

        Stack<Temp> s = new Stack<>();

        ArrayList<String> v = new ArrayList<>(Arrays.asList("", "", "2", "'"));
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

    private static ArrayList<Integer> maskStage1_4Stage(Cube c) {
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

        return mask;
    }

    private static ArrayList<Integer> maskStage2_4Stage(Cube c) {
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

        mask.addAll(cornerMask);

        mask.addAll(edgeMask);

        return mask;
    }

    private static ArrayList<Integer> maskStage3_4Stage(Cube c) {
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

        mask.addAll(parityCorners);

        mask.addAll(parityCornerPos);

        for (byte i : c.getCorner().getCornerPos().getVal())
            mask.add((int) i);

        return mask;
    }

    private static ArrayList<Integer> maskStage4_4Stage(Cube c) {
        ArrayList<Integer> mask = new ArrayList<>();

        for (byte i : c.getEdge().getEdgePos().getVal())
            mask.add((int) i);

        for (byte i : c.getCorner().getCornerPos().getVal())
            mask.add((int) i);

        return mask;
    }

    private static ArrayList<Integer> maskStage1_3Stage(Cube c) {
        ArrayList<ArrayList<Integer>> edgeMask = new ArrayList<>();
        ArrayList<Integer> cornerMask = new ArrayList<>();
        ArrayList<Integer> mask = new ArrayList<>();

        EdgePos edgePos = c.getEdge().getEdgePos();
        EdgeOrientation edgeOrientation = c.getEdge().getEdgeOrientation();

        CornerPos cornerPos = c.getCorner().getCornerPos();
        CornerOrientation cornerOrientation = c.getCorner().getCornerOrientation();

        for (int i = 0; i < 12; i++)
            edgeMask.add(new ArrayList<>(Arrays.asList(0, 0)));

        for (int i = 0; i < 8; i++)
            cornerMask.add(0);

        for (int i = 0; i < 4; i++) {
            edgeMask.get(edgePos.getVal()[i]).set(0, 0);
            edgeMask.get(edgePos.getVal()[i]).set(1, (int) edgeOrientation.getVal()[i]);
        }

        for (int i = 4; i < 8; i++) {
            edgeMask.get(edgePos.getVal()[i]).set(0, 1);
            edgeMask.get(edgePos.getVal()[i]).set(1, (int) edgeOrientation.getVal()[i]);
        }

        for (int i = 8; i < 12; i++) {
            edgeMask.get(edgePos.getVal()[i]).set(0, 0);
            edgeMask.get(edgePos.getVal()[i]).set(1, (int) edgeOrientation.getVal()[i]);
        }

        for (int i = 0; i < 8; i++)
            cornerMask.set(cornerPos.getVal()[i], (int) cornerOrientation.getVal()[i]);

        for (ArrayList<Integer> i : edgeMask) {
            mask.add(i.get(0));
            mask.add(i.get(1));
        }

        mask.addAll(cornerMask);

        return mask;
    }

    private static ArrayList<Integer> maskStage2_3Stage(Cube c) {
        return Cube.maskStage3_4Stage(c);
    }

    private static ArrayList<Integer> maskStage3_3Stage(Cube c) {
        return Cube.maskStage4_4Stage(c);
    }

    private static ArrayList<Integer> maskStage1_2Stage(Cube c) {
        return Cube.maskStage1_3Stage(c);
    }

    private static ArrayList<Integer> maskStage2_2Stage(Cube c) {
        return Cube.maskStage4_4Stage(c);
    }

//        public String solveStage(String[] stagedMoveRestrictions, StageMasker sm) {
//        Cube source = new Cube(this);
//        Cube destination = new Cube();
//
//        class Temp {
//            final Cube c;
//            final String s;
//
//            public Temp(Cube c, String s) {
//                this.c = c.clone();
//                this.s = new String(s);
//            }
//        }
//
//        Queue<Temp> forward = new LinkedBlockingQueue<>();
//        Queue<Temp> backward = new LinkedBlockingQueue<>();
//
//        HashMap<ArrayList<Integer>, String> forwardSolution = new HashMap<>();
//        HashMap<ArrayList<Integer>, String> backwardSolution = new HashMap<>();
//
//        forwardSolution.put(sm.mask(source), "");
//        backwardSolution.put(sm.mask(destination), "");
//
//        forward.add(new Temp(source, ""));
//        backward.add(new Temp(destination, ""));
//
//        while (true) {
//            Temp f = forward.poll();
//
//            for (String stagedMoveRestriction : stagedMoveRestrictions) {
//                Cube temp = Cube.execute(f.c, stagedMoveRestriction);
//
//                ArrayList<Integer> x = sm.mask(temp);
//
//                if (backwardSolution.containsKey(x))
//                    return f.s + stagedMoveRestriction + Cube.reverseAlgorithm(backwardSolution.get(x));
//
//
//                if (!forwardSolution.containsKey(x)) {
//                    forwardSolution.put(x, f.s + stagedMoveRestriction);
//                    forward.add(new Temp(temp, f.s + stagedMoveRestriction));
//                }
//            }
//
//            Temp b = backward.poll();
//
//            for (String stagedMoveRestriction : stagedMoveRestrictions) {
//                Cube temp = Cube.execute(b.c, stagedMoveRestriction);
//
//                ArrayList<Integer> x = sm.mask(temp);
//
//                if (forwardSolution.containsKey(x))
//                    return forwardSolution.get(x) + Cube.reverseAlgorithm(b.s + stagedMoveRestriction);
//
//                if (!backwardSolution.containsKey(x)) {
//                    backwardSolution.put(x, b.s + stagedMoveRestriction);
//                    backward.add(new Temp(temp, b.s + stagedMoveRestriction));
//                }
//            }
//        }
//    }


//    public String solveStage(String[] stagedMoveRestrictions, StageMasker sm) {
//        Cube source = new Cube(this);
//        Cube destination = new Cube();
//
//        class Temp {
//            final Cube c;
//            final String s;
//
//            public Temp(Cube c, String s) {
//                this.c = c.clone();
//                this.s = new String(s);
//            }
//        }
//
//        Queue<Temp> forward = new LinkedBlockingQueue<>();
//        Queue<Temp> backward = new LinkedBlockingQueue<>();
//
//        Map<ArrayList<Integer>, String> forwardSolution = Collections.synchronizedMap(new HashMap<>());
//        Map<ArrayList<Integer>, String> backwardSolution = Collections.synchronizedMap(new HashMap<>());
//
//        forwardSolution.put(sm.mask(source), "");
//        backwardSolution.put(sm.mask(destination), "");
//
//        forward.add(new Temp(source, ""));
//        backward.add(new Temp(destination, ""));
//
//        // Create a shared flag to signal when a solution is found
//        AtomicReference<String> solution = new AtomicReference<>(null);
//        AtomicBoolean found = new AtomicBoolean(false);
//
//        // Forward search thread
//        Thread forwardThread = new Thread(() -> {
//            while (!found.get()) {
//                Temp f = forward.poll();
//                if (f == null) continue;
//
//                for (String stagedMoveRestriction : stagedMoveRestrictions) {
//                    Cube temp = Cube.execute(f.c, stagedMoveRestriction);
//                    ArrayList<Integer> x = sm.mask(temp);
//
//                    // Check for intersection
//                    if (backwardSolution.containsKey(x)) {
//                        solution.set(f.s + stagedMoveRestriction + Cube.reverseAlgorithm(backwardSolution.get(x)));
//                        found.set(true);
//                        return;
//                    }
//
//                    // Add new state if not already visited
//                    if (!forwardSolution.containsKey(x)) {
//                        forwardSolution.put(x, f.s + stagedMoveRestriction);
//                        forward.add(new Temp(temp, f.s + stagedMoveRestriction));
//                    }
//                }
//            }
//        });
//
//        // Backward search thread
//        Thread backwardThread = new Thread(() -> {
//            while (!found.get()) {
//                Temp b = backward.poll();
//                if (b == null) continue;
//
//                for (String stagedMoveRestriction : stagedMoveRestrictions) {
//                    Cube temp = Cube.execute(b.c, stagedMoveRestriction);
//                    ArrayList<Integer> x = sm.mask(temp);
//
//                    // Check for intersection
//                    if (forwardSolution.containsKey(x)) {
//                        solution.set(forwardSolution.get(x) + Cube.reverseAlgorithm(b.s + stagedMoveRestriction));
//                        found.set(true);
//                        return;
//                    }
//
//                    // Add new state if not already visited
//                    if (!backwardSolution.containsKey(x)) {
//                        backwardSolution.put(x, b.s + stagedMoveRestriction);
//                        backward.add(new Temp(temp, b.s + stagedMoveRestriction));
//                    }
//                }
//            }
//        });
//
//        // Start both threads
//        forwardThread.start();
//        backwardThread.start();
//
//        // Wait for both threads to complete
//        try {
//            forwardThread.join();
//            backwardThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // Return the solution found
//        return solution.get();
//    }

    public String solveStage(String[] stagedMoveRestrictions, StageMasker sm) {
        Cube source = new Cube(this);
        Cube destination = new Cube();

        if (sm.mask(source).equals(sm.mask(destination)))
            return "";

        class Temp {
            final Cube c;
            final String s;

            public Temp(Cube c, String s) {
                this.c = c.clone();
                this.s = new String(s);
            }
        }

        Queue<Temp> forward = new LinkedBlockingQueue<>();
        Queue<Temp> backward = new LinkedBlockingQueue<>();

        ConcurrentHashMap<ArrayList<Integer>, String> forwardSolution = new ConcurrentHashMap<>();
        ConcurrentHashMap<ArrayList<Integer>, String> backwardSolution = new ConcurrentHashMap<>();

        forwardSolution.put(sm.mask(source), "");
        backwardSolution.put(sm.mask(destination), "");

        forward.add(new Temp(source, ""));
        backward.add(new Temp(destination, ""));

        // Shared variable to store the result
        AtomicReference<String> result = new AtomicReference<>(null);
        CountDownLatch latch = new CountDownLatch(1);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Forward search task
        Runnable forwardSearch = () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Temp f = forward.poll();
                    if (f == null) continue;

                    for (String move : stagedMoveRestrictions) {
                        Cube temp = Cube.execute(f.c, move);
                        ArrayList<Integer> maskedState = sm.mask(temp);

                        if (backwardSolution.containsKey(maskedState)) {
                            result.set(f.s + move + Cube.reverseAlgorithm(backwardSolution.get(maskedState)));
                            latch.countDown(); // Signal that a solution was found
                            return;
                        }

                        if (!forwardSolution.containsKey(maskedState)) {
                            forwardSolution.put(maskedState, f.s + move);
                            forward.add(new Temp(temp, f.s + move));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        // Backward search task
        Runnable backwardSearch = () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Temp b = backward.poll();
                    if (b == null) continue;

                    for (String move : stagedMoveRestrictions) {
                        Cube temp = Cube.execute(b.c, move);
                        ArrayList<Integer> maskedState = sm.mask(temp);

                        if (forwardSolution.containsKey(maskedState)) {
                            result.set(forwardSolution.get(maskedState) + Cube.reverseAlgorithm(b.s + move));
                            latch.countDown(); // Signal that a solution was found
                            return;
                        }

                        if (!backwardSolution.containsKey(maskedState)) {
                            backwardSolution.put(maskedState, b.s + move);
                            backward.add(new Temp(temp, b.s + move));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        // Submit both tasks to the executor
        executor.submit(forwardSearch);
        executor.submit(backwardSearch);

        try {
            latch.await(); // Wait for one thread to find a solution
            executor.shutdownNow(); // Stop all threads once a solution is found
            return result.get(); // Return the found solution
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown(); // Ensure the executor is properly shut down
        }

        return null; // In case no solution is found
    }


    public static ArrayList<String> solveCube(Cube c, int stages) {
        if (stages > 4 || stages < 2)
            return null;

        StringBuilder solution = new StringBuilder();

        for (int i = 0; i < stages; i++) {
            String sol = c.solveStage(Cube.stagedMoveRestrictions[stages][i], Cube.maskStages[stages][i]);
            System.out.println(Cube.getAlgorithm(sol));
            c = Cube.execute(c, sol);
            solution.append(sol);
        }

        return Cube.getAlgorithm(solution.toString());
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

// U2 L2 F R D R F R F' L' B U2 F R2 L2 F' U2 B L2 B
