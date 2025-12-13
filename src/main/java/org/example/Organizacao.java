package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Organizacao {
    // Map principal para armazenar todos os nós. Chave: Nome (String), Valor: Objeto Pessoa
    private Map<String, Pessoa> estrutura;

    public Organizacao() {
        this.estrutura = new HashMap<>();
    }

    /**
     * Implementação do Passo 1: Adiciona uma nova pessoa à estrutura.
     */
    public void adicionarPessoa(String nome, String cargo, String departamento, Double salario) {
        // Validação: Verifica se a pessoa já existe
        if (estrutura.containsKey(nome)) {
            System.out.println("ERRO: Pessoa '" + nome + "' já existe na organização. Use um nome ou ID único.");
            return;
        }

        // 1. Cria o objeto Pessoa
        Pessoa novaPessoa = new Pessoa(nome, cargo, departamento, salario);

        // 2. Adiciona o objeto ao Map principal
        estrutura.put(nome, novaPessoa);

        System.out.println("\nSUCESSO! Pessoa adicionada:");
        System.out.println("-> " + novaPessoa.toString());
    }

    /**
     * Função para gerenciar a interação via terminal para o Passo 1.
     */
    public void executarPasso1(Scanner scanner) {
        System.out.println("\n--- Passo 1: Adicionar Pessoa/Cargo ---");

        // 1. Coleta o Nome
        System.out.print("Digite o NOME da pessoa/cargo (ID ÚNICO): ");
        String nome = scanner.nextLine().trim();

        // 2. Coleta o Cargo
        System.out.print("Digite o CARGO: ");
        String cargo = scanner.nextLine().trim();

        // 3. Coleta o Departamento
        System.out.print("Digite o DEPARTAMENTO: ");
        String departamento = scanner.nextLine().trim();

        // 4. Coleta o Salário (opcional)
        Double salario = null;
        System.out.print("Digite o SALÁRIO (opcional, deixe vazio para pular): ");
        String salarioStr = scanner.nextLine().trim();

        if (!salarioStr.isEmpty()) {
            try {
                // Tenta converter a string para Double
                salario = Double.parseDouble(salarioStr.replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("AVISO: Salário inválido. O salário não será registrado.");
            }
        }

        // Chama a função central que realiza a adição
        adicionarPessoa(nome, cargo, departamento, salario);
    }

    // --- FUNÇÕES DE UTILDADE (Para debug e futuros passos) ---
    public Map<String, Pessoa> getEstrutura() {
        return estrutura;
    }

    // Esta função será crucial para o Passo 2
    public Pessoa getPessoa(String nome) {
        return estrutura.get(nome);
    }
}