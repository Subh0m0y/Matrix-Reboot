import com.github.subh0m0y.matrix.Matrix;

public class Main {
    public static void main(String[] args) {
        Matrix matrix = Matrix.fromLinearArray(4, 2, 2, 2, 2, 2, 1, 1, 1, 1);
        System.out.println(matrix);
        Matrix[] matrices = matrix.splitAtRow(2);
        System.out.println(matrices[0]);
        System.out.println(matrices[1]);
    }
}
