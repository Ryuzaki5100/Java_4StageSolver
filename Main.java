public class Main {
    public static void main(String[] args) {
        Cube c = new Cube("yrogborbbgowwoorybgyowwgrgowbbyrrgrwyyrgyrgbywwyogbbwo");

        String sol1 = c.solveStage(0, (cube) -> Cube.maskStage1(cube));
        c = Cube.execute(c, sol1);

        String sol2 = c.solveStage(1, (cube) -> Cube.maskStage2(cube));
        c = Cube.execute(c, sol2);

        String sol3 = c.solveStage(2, (cube) -> Cube.maskStage3(cube));
        c = Cube.execute(c, sol3);

        String sol4 = c.solveStage(3, (cube) -> Cube.maskStage4(cube));
        c = Cube.execute(c, sol4);

        System.out.println(Cube.getAlgorithm(sol1 + sol2 + sol3 + sol4));
        System.out.println(Cube.getAlgorithm(sol1 + sol2 + sol3 + sol4).size());
    }
}