import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class CPUBound {
    public static boolean EhPrimo(long numeroAtual) {
        if (numeroAtual <= 1) return false;
        for (long i = 2; i <= Math.sqrt(numeroAtual); i++) {
            if (numeroAtual % i == 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {

        System.out.println("ATENÇÃO: NÃO ACEITAMOS NÚMEROS A PARTIR DE UM SEXTILIAO");
        System.out.println("Digite o maior valor que vai querer verificar: ");
        
        ThreadMXBean medidor = ManagementFactory.getThreadMXBean();
        Scanner entrada = new Scanner(System.in);
        
        long maiorNumero = entrada.nextLong();

        entrada.close();

        System.out.println("Contando...");

        //tempo em nanosegundos, conta depois do IO (Pois estamos so contando o CPU-bound)
        long inicio = medidor.getCurrentThreadCpuTime();
        
        long contagemPrimos = 0;
        for (long i = 2; i < maiorNumero; i++) {
            if (EhPrimo(i)) {
                contagemPrimos++;
            }
        }
        
        long tempoCPU = medidor.getCurrentThreadCpuTime() - inicio;
        
        System.out.println("Quantidade de Numeros Primos achados: " + contagemPrimos);
        System.out.println("Quantidade de Nanosegundos da CPU: " + tempoCPU);
        System.out.println("Quantidade de Milisegundos da CPU: " + TimeUnit.NANOSECONDS.toMillis(tempoCPU));
        System.out.println("Quantidade de Segundos da CPU: " + TimeUnit.NANOSECONDS.toSeconds(tempoCPU));
    }
}