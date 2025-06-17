import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MemoryBound {
    
    // Classe para representar um nó da árvore binária
    static class TreeNode {
        int data;
        String additionalData; // Dados extras para consumir mais memória
        TreeNode left;
        TreeNode right;
        List<Integer> extraList; // Lista adicional para aumentar uso de memória
        
        public TreeNode(int data) {
            this.data = data;
            this.additionalData = "Node_" + data + "_" + generateRandomString(50);
            this.extraList = new ArrayList<>();
            // Adiciona dados extras para cada nó consumir mais memória
            for (int i = 0; i < 10; i++) {
                this.extraList.add(i * data);
            }
        }
        
        private String generateRandomString(int length) {
            StringBuilder sb = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < length; i++) {
                sb.append((char) (random.nextInt(26) + 'a'));
            }
            return sb.toString();
        }
    }
    
    // Classe da árvore binária de busca
    static class BinarySearchTree {
        TreeNode root;
        List<TreeNode> allNodes; // Lista para manter referência de todos os nós
        
        public BinarySearchTree() {
            this.allNodes = new ArrayList<>();
        }
        
        // Inserção de nó na árvore
        public void insert(int data) {
            root = insertRec(root, data);
        }
        
        private TreeNode insertRec(TreeNode root, int data) {
            if (root == null) {
                TreeNode newNode = new TreeNode(data);
                allNodes.add(newNode); // Mantém referência para operações de memória
                return newNode;
            }
            
            if (data < root.data) {
                root.left = insertRec(root.left, data);
            } else if (data > root.data) {
                root.right = insertRec(root.right, data);
            }
            
            return root;
        }
        
        // Busca intensiva que acessa muitos nós
        public boolean search(int data) {
            return searchRec(root, data);
        }
        
        private boolean searchRec(TreeNode root, int data) {
            if (root == null) return false;
            
            // Acesso adicional aos dados do nó para intensificar uso de memória
            String temp = root.additionalData;
            List<Integer> tempList = new ArrayList<>(root.extraList);
            
            if (data == root.data) return true;
            
            return data < root.data ? 
                searchRec(root.left, data) : searchRec(root.right, data);
        }
        
        // Percurso in-order que acessa todos os nós
        public void inOrderTraversal() {
            List<String> result = new ArrayList<>();
            inOrderRec(root, result);
        }
        
        private void inOrderRec(TreeNode root, List<String> result) {
            if (root != null) {
                inOrderRec(root.left, result);
                // Operações que consomem memória
                result.add(root.additionalData);
                result.addAll(root.extraList.stream().map(String::valueOf).toList());
                inOrderRec(root.right, result);
            }
        }
        
        // Operação que clona a árvore inteira (muito intensiva em memória)
        public BinarySearchTree cloneTree() {
            BinarySearchTree clonedTree = new BinarySearchTree();
            clonedTree.root = cloneRec(this.root, clonedTree);
            return clonedTree;
        }
        
        private TreeNode cloneRec(TreeNode original, BinarySearchTree clonedTree) {
            if (original == null) return null;
            
            TreeNode cloned = new TreeNode(original.data);
            clonedTree.allNodes.add(cloned);
            cloned.left = cloneRec(original.left, clonedTree);
            cloned.right = cloneRec(original.right, clonedTree);
            
            return cloned;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== Programa Memory-Bound com Árvore Binária ===");
        
        ThreadMXBean medidor = ManagementFactory.getThreadMXBean();

        long tempoInicio = medidor.getCurrentThreadCpuTime();
        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        // Criação da árvore binária
        BinarySearchTree bst = new BinarySearchTree();
        
        // Fase 1: Inserção massiva de dados (memory-intensive)
        System.out.println("Inserindo 10.000.000 nós na árvore...");
        Random random = new Random(12345); // Seed fixo para reprodutibilidade
        for (int i = 0; i < 10000000; i++) {
            bst.insert(random.nextInt(100000));
        }
        
        // Fase 2: Buscas intensivas
        System.out.println("Realizando 20.000.000 buscas...");
        for (int i = 0; i < 20000000; i++) {
            bst.search(random.nextInt(100000));
        }
        
        // Fase 3: Múltiplos percursos da árvore
        System.out.println("Realizando 1000 percursos completos da árvore...");
        for (int i = 0; i < 1000; i++) {
            bst.inOrderTraversal();
        }
        
        // Fase 4: Clonagem da árvore (operação muito intensiva em memória)
        System.out.println("Clonando a árvore 40 vezes...");
        List<BinarySearchTree> clones = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            clones.add(bst.cloneTree());
        }
        
        // Fase 5: Operações adicionais nos clones
        System.out.println("Realizando operações nos clones...");
        for (BinarySearchTree clone : clones) {
            for (int i = 0; i < 1000; i++) {
                clone.search(random.nextInt(100000));
            }
            clone.inOrderTraversal();
        }
        
        // Medição final

        long tempoFim = medidor.getCurrentThreadCpuTime();

        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        long executionTimeSeconds = tempoFim - tempoInicio;
        long memoryUsed = endMemory - startMemory;
        
        System.out.println("\n=== Resultados ===");
        System.out.println("Tempo de execução: " + TimeUnit.NANOSECONDS.toSeconds(executionTimeSeconds) + " segundos");
        System.out.println("Memória utilizada: " + (memoryUsed / (1024 * 1024)) + " MB");
        System.out.println("Número total de nós criados: " + bst.allNodes.size());
        System.out.println("Número de clones criados: " + clones.size());
        
        // Força garbage collection para liberar memória
        clones.clear();
        bst = null;
        System.gc();
        
        System.out.println("Programa concluído!");
    }
}
