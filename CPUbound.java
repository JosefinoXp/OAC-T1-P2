import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.TimeUnit;

public class CPUBound {
    static long TorreDeHanoi(long n) {
        if (n == 0) return 0;
        return 1 + 2 * TorreDeHanoi(n - 1);
    }

    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        ThreadMXBean mx = ManagementFactory.getThreadMXBean();

        long ini = mx.getCurrentThreadCpuTime();
        long moves = TorreDeHanoi(N);
        long cpuNs = mx.getCurrentThreadCpuTime() - ini;

        System.out.println("Movimentos: " + moves);
        System.out.println("Duracao em milisegundos: " + TimeUnit.NANOSECONDS.toMillis(cpuNs));
        System.out.println("Duracao em segundos: " + TimeUnit.NANOSECONDS.toSeconds(cpuNs));
    }
    
}