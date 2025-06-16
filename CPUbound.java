import java.util.*;
import java.util.concurrent.TimeUnit;

public class CPUbound {
    static void TorreDeHanoi(int N, char torre_vindo, char torre_indo, char torre_aux) {
        if (N == 0)
            return;

        TorreDeHanoi(N - 1, torre_vindo, torre_aux, torre_indo);

        System.out.println("Disco movido " + N + " vindo do disco " + torre_vindo + " para disco " + torre_indo);

        TorreDeHanoi(N - 1, torre_aux, torre_indo, torre_vindo);
    }
    
    public static void main(String[] args) {
        long TempoInicial, TempoFinal;

        System.out.println("Digite o numero de interacoes: ");

        Scanner entrada = new Scanner(System.in);

        int N = entrada.nextInt();

        TempoInicial = System.nanoTime();

        TorreDeHanoi(N, 'A', 'C', 'B');

        TempoFinal = (System.nanoTime() - TempoInicial);

        System.out.println("Duracao de tempo em milisegundos: (" + TimeUnit.NANOSECONDS.toMillis(TempoFinal)  + ")");
        System.out.println("Duracao de tempo em segundos: (" + TimeUnit.NANOSECONDS.toSeconds(TempoFinal) + ")");

        entrada.close();
    }
    
}