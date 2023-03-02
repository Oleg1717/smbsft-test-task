package globalsqa.com.utils;

public class MathUtils {

    public static int calcFibonacciNumber(int n) {
        if (n == 0 || n == 1) {
            return n;
        }
        int n0 = 0, n1 = 1;
        int tmpNumber;
        for (int i = 2; i <= n; i++) {
            tmpNumber = n0 + n1;
            n0 = n1;
            n1 = tmpNumber;
        }
        return n1;
    }
}
