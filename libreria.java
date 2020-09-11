import java.util.Random;
import java.math.BigInteger;
import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;

class libreria {

    static BigInteger[] test_prime_1 = new BigInteger[] {
        new BigInteger("10000").nextProbablePrime(),
        new BigInteger("1000000000").nextProbablePrime(),
        new BigInteger("100000000000000").nextProbablePrime(),
        new BigInteger("10000000000000000000").nextProbablePrime()
    };

    static BigInteger[] test_prime_2_3 = new BigInteger[] {
        
        /* n = 2^300 - 153. */
        TWO.pow(300).subtract(BigInteger.valueOf(153)),

        /* n = 2^400 - 593. */
        TWO.pow(400).subtract(BigInteger.valueOf(593)),

        /* The largest Carmichael number known. */
        new BigInteger("1590231231043178376951698401")
    };

    static BigInteger getRandom(BigInteger min, BigInteger max) {

        Random rand = new Random(System.nanoTime());
        int len = max.bitLength();
        BigInteger res = new BigInteger(len, rand);
        
        // return (res + min) % max
        return res.add(min).mod(max);
    }

    static BigInteger getPrime(BigInteger min, BigInteger max, int k) {

        min = min.divide(TWO); max = max.divide(TWO);

        // res = (getRandom(min, max) * 2) + 1
        BigInteger res = getRandom(min, max).multiply(TWO).add(ONE);

        while (!prime_3(res, k))
            res = res.add(TWO);

        return res;
    }

    static String convTime(long a, long b) {

        double c = (double)(b - a) / (double)1000000000;
        return (int)(c / 60) + " min " + (c % 60) + " sec";
    }

    static boolean prime_1(BigInteger n) {

        // n < 2
        if (n.compareTo(TWO) < 0)
            return false;

        // n % 2 == 0
        else if (n.mod(TWO).equals(ZERO))
            return false;

        BigInteger THREE = ONE.add(TWO), i;

        // i = 3; i <= sqrt(n); i += 2
        for (i = THREE; i.compareTo(n.sqrt()) <= 0; i = i.add(TWO))
            
            // n % i == 0
            if (n.mod(i).equals(ZERO))
                return false;
    
        return true;
    }

    static boolean prime_2(BigInteger n) {

        // n < 2
        if (n.compareTo(TWO) < 0)
            return false;

        // n % 2 == 0
        else if (n.mod(TWO).equals(ZERO))
            return false;

        BigInteger n_1 = n.subtract(ONE), a = getRandom(TWO, n_1);

        // gcd(a, n) != 1
        while (!a.gcd(n).equals(ONE))
            a = getRandom(TWO, n_1);

        if (a.modPow(n_1, n).equals(ONE))
            return true;
        else
            return false;
    }

    static boolean prime_3(BigInteger n, int k) {

        // n < 2
        if (n.compareTo(TWO) < 0)
            return false;

        // n % 2 == 0
        else if (n.mod(TWO).equals(ZERO))
            return false;

        BigInteger z = n.subtract(ONE), w = ZERO;

        while (z.mod(TWO).equals(ZERO)) {

            z = z.divide(TWO);
            w = w.add(ONE);
        }

        for (int i = 0; i < k; i++) {

            BigInteger a = getRandom(TWO, n.subtract(ONE));

            // gcd(a, n) != 1
            if (!a.gcd(n).equals(ONE))
                return false;

            BigInteger x = a.modPow(z, n), j = ONE;
            boolean stop = false; 

            // x == 1 || x == n - 1
            if (x.equals(ONE) || x.equals(n.subtract(ONE)))
                continue;

            // j <= w - 1 && !stop
            while (j.compareTo(w.subtract(ONE)) <= 0 && !stop) {

                // x = (x * x) % n
                x = x.multiply(x).mod(n);

                if (x.equals(n.subtract(ONE)))
                    stop = true;
                else
                    j = j.add(ONE);
            }

            if (!stop)
                return false;
        }
        
        return true;
    }
        
    public static void main(String[] args) {

        for (BigInteger i : test_prime_1) {

            System.out.println("Numero scelto: " + i);

            long a = System.nanoTime();
            System.out.print("Trial Division Test: Risultato: " + prime_1(i));
            long b = System.nanoTime();
            System.out.println(" Tempo: " + convTime(a, b));
            System.out.println("--------------------");
        }

        for (BigInteger i : test_prime_2_3) {

            System.out.println("Numero scelto: " + i);

            long a = System.nanoTime();
            System.out.print("Fermat Test: Risultato: " + prime_2(i));
            long b = System.nanoTime();
            System.out.println(" Tempo: " + convTime(a, b));
            System.out.println("----------------------------------------");
        }

        for (BigInteger i : test_prime_2_3) {

            System.out.println("Numero scelto: " + i);

            long a = System.nanoTime();
            System.out.print("Miller-Rabin Test: Risultato: " + prime_3(i, 30));
            long b = System.nanoTime();
            System.out.println(" Tempo: " + convTime(a, b));
            System.out.println("----------------------------------------");
        }
    }
}