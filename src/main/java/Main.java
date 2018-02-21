import com.github.subh0m0y.matrix.Matrix;

public class Main {
    public static void main(String[] args) {
        Matrix original = Matrix.fromLinearArray(4, 4, 1, 2, 3, 4, 2, 3, 4, 1, 3, 4, 1, 2, 4, 1, 2, 3);
//        System.out.println(original);
        Matrix inverse = original.getInverse();
        System.out.println(inverse);
    }
}
