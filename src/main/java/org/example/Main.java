package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Organizacao org = new Organizacao();
        Scanner scanner = new Scanner(System.in);
        String opcao;

        do {
            System.out.println("\n==================================");
            System.out.println("Sistema de Estrutura Organizacional");
            System.out.println("==================================");
            System.out.println("1. Adicionar Pessoa/Cargo (Passo 1)");
            System.out.println("2. Definir Subordinado (Passo 2)");
            System.out.println("3. Remover Pessoa/Cargo (Passo 3)");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1":
                    // Executa a função do Passo 1
                    org.executarPasso1(scanner);
                    break;
                case "2":
                    // Executa a função do Passo 2
                    org.executarPasso2(scanner);
                    break;
                case "3":
                    org.executarPasso3(scanner); //  Passo 3
                    break;
                case "0":
                    System.out.println("Encerrando o sistema.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

            // Opcional: Mostrar o total de pessoas adicionadas
            System.out.println("\nPessoas na estrutura: " + org.getEstrutura().size());

        } while (!opcao.equals("0"));

        scanner.close();
    }
}