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

    /**
     * Implementação do Passo 2: Define uma relação de subordinação (pai-filho).
     * @param nomeSuperior Nome da pessoa que será o superior
     * @param nomeSubordinado Nome da pessoa que será o subordinado
     */
    public void definirSubordinado(String nomeSuperior, String nomeSubordinado) {
        // Validação: Verifica se ambas as pessoas existem
        Pessoa superior = estrutura.get(nomeSuperior);
        Pessoa subordinado = estrutura.get(nomeSubordinado);

        if (superior == null) {
            System.out.println("ERRO: Pessoa '" + nomeSuperior + "' não encontrada na organização.");
            return;
        }

        if (subordinado == null) {
            System.out.println("ERRO: Pessoa '" + nomeSubordinado + "' não encontrada na organização.");
            return;
        }

        // Validação: Não permite que uma pessoa seja subordinada de si mesma
        if (nomeSuperior.equals(nomeSubordinado)) {
            System.out.println("ERRO: Uma pessoa não pode ser subordinada de si mesma.");
            return;
        }

        // Validação: Verifica se já existe relação de subordinação
        if (superior.getSubordinados().contains(subordinado)) {
            System.out.println("AVISO: '" + nomeSubordinado + "' já é subordinado de '" + nomeSuperior + "'.");
            return;
        }

        // Validação: Verifica se o subordinado já tem um superior (evita múltiplos superiores)
        if (subordinado.getSuperior() != null) {
            System.out.println("AVISO: '" + nomeSubordinado + "' já possui um superior ('" + 
                             subordinado.getSuperior().getNome() + "'). Removendo relação anterior.");
            // Remove da lista de subordinados do superior anterior
            subordinado.getSuperior().getSubordinados().remove(subordinado);
        }

        // Estabelece a relação pai-filho
        superior.getSubordinados().add(subordinado);
        subordinado.setSuperior(superior);

        System.out.println("\nSUCESSO! Relação de subordinação estabelecida:");
        System.out.println("-> " + nomeSuperior + " (superior)");
        System.out.println("   └─ " + nomeSubordinado + " (subordinado)");
    }

    /**
     * Função para gerenciar a interação via terminal para o Passo 2.
     */
    public void executarPasso2(Scanner scanner) {
        System.out.println("\n--- Passo 2: Definir Subordinado ---");

        // Validação: Verifica se existe pelo menos uma pessoa cadastrada
        if (estrutura.isEmpty()) {
            System.out.println("ERRO: Não há pessoas/cargos cadastrados na organização.");
            System.out.println("Por favor, adicione pelo menos uma pessoa/cargo antes de definir subordinação.");
            return;
        }

        // 1. Coleta o nome do superior
        System.out.print("Digite o NOME do SUPERIOR: ");
        String nomeSuperior = scanner.nextLine().trim();

        // 2. Coleta o nome do subordinado
        System.out.print("Digite o NOME do SUBORDINADO: ");
        String nomeSubordinado = scanner.nextLine().trim();

        // Chama a função central que realiza a definição
        definirSubordinado(nomeSuperior, nomeSubordinado);
    }
}