    package org.example;

    import java.util.*;

    public class Organizacao {
        // Map principal para armazenar todos os nós. Chave: Nome (String), Valor:
        // Objeto Pessoa
        private Map<String, Pessoa> estrutura;

        public Organizacao() {
            this.estrutura = new HashMap<>();
        }

        /**
         * Passo 1: Adiciona uma nova pessoa à estrutura.
         * author Arthur Mendes Couto
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
         * Passo 2: Define uma relação de subordinação (pai-filho).
         * 
         * @param nomeSuperior    Nome da pessoa que será o superior
         * @param nomeSubordinado Nome da pessoa que será o subordinado
         * @author Cláudio Augusto
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

            // Validação: Verifica se o subordinado já tem um superior (evita múltiplos
            // superiores)
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

        /**
         * Passo 3: Remove uma pessoa/cargo e todos os seus subordinados recursivamente.
         * @author Sérgio Emílio
         */
        public void removerPessoa(String nome) {
            Pessoa pessoa = estrutura.get(nome);

            if (pessoa == null) {
                System.out.println("ERRO: Pessoa '" + nome + "' não encontrada.");
                return;
            }

            // Se tiver superior, remove da lista de subordinados dele
            if (pessoa.getSuperior() != null) {
                pessoa.getSuperior().getSubordinados().remove(pessoa);
            }

            // Remove todos os subordinados recursivamente
            removerSubordinadosRecursivo(pessoa);

            // Remove a própria pessoa do Map
            estrutura.remove(nome);

            System.out.println("\nSUCESSO! Pessoa '" + nome +
                    "' e todos os seus subordinados foram removidos.");
        }

        private void removerSubordinadosRecursivo(Pessoa pessoa) {
            for (Pessoa subordinado : pessoa.getSubordinados()) {
                // Remove filhos do subordinado primeiro (recursão)
                removerSubordinadosRecursivo(subordinado);

                // Remove o subordinado do Map
                estrutura.remove(subordinado.getNome());
            }

            // Limpa a lista de subordinados da pessoa
            pessoa.getSubordinados().clear();
        }

        /**
         * Função para gerenciar a interação via terminal para o Passo 3.
         */
        public void executarPasso3(Scanner scanner) {
            System.out.println("\n--- Passo 3: Remover Pessoa/Cargo ---");

            if (estrutura.isEmpty()) {
                System.out.println("ERRO: Não há pessoas cadastradas.");
                return;
            }

            System.out.print("Digite o NOME da pessoa/cargo a ser removida: ");
            String nome = scanner.nextLine().trim();

            removerPessoa(nome);
        }

        public void executarPasso4(Scanner scanner) {
            System.out.println("\n--- Passo 4: Exibir organograma ---");

            if (estrutura.isEmpty()) {
                System.out.println("Ainda não há pessoas/cargos cadastrados no sistema");
                return;
            }
            //Verifica se há mais de uma pessoa com a opção superior == null
            List<Pessoa> pessoasSemSuperior = new ArrayList<>();
            for (Pessoa p : estrutura.values()) {
                if (p.getSuperior() == null)
                    pessoasSemSuperior.add(p);
            }
            if (pessoasSemSuperior.size() == 1){
                printOrganograma();
            }
            //Em nossa lógica, o usuário raiz é o que possui o parâmetro "Superior" como null
            //Caso exista mais de 1 será dada ao usuário a opção de efetuar a correção.
            if (pessoasSemSuperior.size() > 1) {
                String userInput;
                System.out.println("ERRO: Há mais de uma pessoa sem superior, oraganograma não pode ser listado");
                boolean invalidInput = true;
                while (invalidInput) {
                    System.out.println("1. Ver passos para a correção");
                    System.out.println("0. Voltar para tela inicial");
                    userInput = scanner.nextLine().trim();
                    //Passos para a correção da árvore
                    if (userInput.equals("1")) {
                        corrigirArvore(scanner, pessoasSemSuperior);
                        invalidInput = false;
                    }
                    else if (userInput.equals("0")) {
                        invalidInput = false;
                    } else {
                        System.out.println("Valor inválido, tente novamente!");
                    }
                }
            }

        }

        /**
         *
         * @param scanner utilizado para digitação de valores pelo usuário
         * @param listaDePessoas lista de pessoas com o parâmetro "superior" igual a "null"
         */
        public void corrigirArvore(Scanner scanner, List<Pessoa> listaDePessoas) {
            //Definir o Diretor/Raiz
            System.out.println("Para começar, me diga qual desses usuários é o diretor/presidente: ");

            for (Pessoa p : listaDePessoas)
                System.out.println(p.getNome());

            // Chama o método auxiliar. Se retornar null, o usuário digitou 0.
            String nomeRoot = buscarNomeValido(scanner, "Nome do diretor (ou 0 para voltar): ");

            if (nomeRoot == null) return; // Sai do método e volta pro menu

            // Remove da lista de forma segura (Java 8+)
            listaDePessoas.removeIf(p -> p.getNome().equalsIgnoreCase(nomeRoot));

            System.out.println("Certo, agora vamos corrigir os demais.");

            //Definir os superiores dos demais
            for (Pessoa pessoa : listaDePessoas) {
                String msg = "Quem é o superior de " + pessoa.getNome() + "? (ou 0 para voltar): ";
                String nomeSuperior = buscarNomeValido(scanner, msg);

                // Se digitou 0, encerra o método inteiro
                if (nomeSuperior == null) return;

                // Lógica de negócio (atualizar a estrutura)
                Pessoa superior = estrutura.get(nomeSuperior);
                Pessoa subordinado = estrutura.get(pessoa.getNome());

                superior.getSubordinados().add(subordinado);
                subordinado.setSuperior(superior);
            }

            System.out.println("Correção da árvore finalizada!");
        }

        /**
         * Método auxiliar que trata o loop de validação e a opção de sair.
         * Retorna o nome válido (já com trim) ou null se o usuário digitou 0.
         */
        private String buscarNomeValido(Scanner scanner, String mensagem) {
            while (true) {
                System.out.print(mensagem);
                String input = scanner.nextLine().trim();

                if (input.equals("0")) {
                    return null;
                }
                if (estrutura.containsKey(input)) {
                    return input;
                }

                System.out.println("Erro: Pessoa não encontrada no sistema.");
                System.out.println("Tente novamente.");
            }
        }

        private void printOrganograma() {
            Pessoa root = null;
            for (Pessoa p : estrutura.values()) {
                if (p.getSuperior() == null) {
                    root = p;
                    break;
                }
            }
            if (root != null) {
                System.out.println("\n--- Organograma Atual ---");
                imprimirRecursivo(root, 0);
            } else {
                System.out.println("Não foi possível encontrar o diretor raiz para exibir.");
            }
        }
        private void imprimirRecursivo(Pessoa pessoa, int nivel) {
            String indentacao = "";
            for (int i = 0; i < nivel; i++)
                indentacao += "    |";
            System.out.println(indentacao + "--> " + "[" + pessoa.getCargo() +"] " + pessoa.getNome() + " " + "(" + pessoa.getDepartamento() + ")");
            if (pessoa.getSubordinados() != null) {
                for (Pessoa subordinado : pessoa.getSubordinados()) {
                    imprimirRecursivo(subordinado, nivel + 1);
                }
            }
        }

        /**
         * Passo 4: Lista as "linhas de reporte" de cada funcionário - percurso raiz → folha.
         * Para cada pessoa na organização, mostra o caminho completo desde a raiz até ela.
         * @author Felipe Oliveira de Abreu 
         */
        public void listarLinhasDeReporte() {
            if (estrutura.isEmpty()) {
                System.out.println("ERRO: Não há pessoas cadastradas na organização.");
                return;
            }

            // Encontra a raiz (pessoa sem superior)
            Pessoa root = null;
            for (Pessoa p : estrutura.values()) {
                if (p.getSuperior() == null) {
                    root = p;
                    break;
                }
            }

            if (root == null) {
                System.out.println("ERRO: Não foi possível encontrar a raiz da organização.");
                return;
            }

            System.out.println("\n--- Passo 6: Linhas de Reporte (Raiz → Folha) ---");
            System.out.println("Mostrando o caminho hierárquico completo para cada funcionário:\n");

            // Lista auxiliar para armazenar o caminho atual
            List<String> caminhoAtual = new ArrayList<>();
            
            // Chama a função recursiva para percorrer toda a árvore
            listarLinhasRecursivo(root, caminhoAtual);
        }

        /**
         * Função recursiva auxiliar para listar as linhas de reporte.
         * 
         * @param pessoa        A pessoa atual sendo processada
         * @param caminhoAtual  Lista com os nomes do caminho da raiz até o pai da pessoa atual
         * @author Felipe Oliveira
         */
        private void listarLinhasRecursivo(Pessoa pessoa, List<String> caminhoAtual) {
            // Adiciona a pessoa atual ao caminho
            caminhoAtual.add(pessoa.getNome() + " (" + pessoa.getCargo() + ")");

            // Monta a string de caminho hierárquico
            String linhadeReporte = String.join(" > ", caminhoAtual);
            
            // Imprime a linha de reporte
            System.out.println(linhadeReporte);

            // Processa todos os subordinados
            for (Pessoa subordinado : pessoa.getSubordinados()) {
                listarLinhasRecursivo(subordinado, caminhoAtual);
            }

            // Remove a pessoa atual do caminho ao voltar da recursão (backtracking)
            caminhoAtual.remove(caminhoAtual.size() - 1);
        }

        /**
         * Passo 5: Retorna o caminho hierárquico da raiz até a pessoa buscada.
         * @param nome Nome da pessoa
         * @return Caminho hierárquico formatado
         * @author Filipe Alves
         */
        public void executarPasso5(Scanner scanner){
            System.out.println("\n--- Passo 5: Buscar Caminho Hierárquico ---");

            if (estrutura.isEmpty()) {
                System.out.println("ERRO: Não há pessoas cadastradas na organização.");
                return;
            }

            System.out.print("Digite o NOME da pessoa para buscar o caminho hierárquico: ");
            String nome = scanner.nextLine().trim();

            String resultado = buscarCaminhoHierarquico(nome);
            System.out.println("Caminho Hierárquico: " + resultado);
        }
        
        public String buscarCaminhoHierarquico (String nome) {
            Pessoa pessoa = estrutura.get(nome);

            if(pessoa == null){
                return "Pessoa não encontrada na organização";
            }

            List<String> caminho = new ArrayList<>();
            Pessoa atual = pessoa;
            while(atual != null) {
                caminho.add(atual.getCargo());
                atual = atual.getSuperior();
            }

            Collections.reverse(caminho);
            return String.join(" > ", caminho);
        }

        /**
         * Função para gerenciar a interação via terminal para o Passo 6.
         * @author Yuri Kevin
         */
        public void executarPasso6(Scanner scanner) {
            System.out.println("\n--- Passo 6: Listar Linhas de Reporte ---");

            if (estrutura.isEmpty()) {
                System.out.println("ERRO: Não há pessoas cadastradas na organização.");
                return;
            }

            // Verifica se há mais de uma pessoa com superior == null
            List<Pessoa> pessoasSemSuperior = new ArrayList<>();
            for (Pessoa p : estrutura.values()) {
                if (p.getSuperior() == null)
                    pessoasSemSuperior.add(p);
            }

            if (pessoasSemSuperior.size() == 1) {
                listarLinhasDeReporte();
            }

            // Caso exista mais de 1, será dada ao usuário a opção de efetuar a correção
            if (pessoasSemSuperior.size() > 1) {
                String userInput;
                System.out.println("ERRO: Há mais de uma pessoa sem superior, linhas de reporte não podem ser listadas");
                boolean invalidInput = true;
                while (invalidInput) {
                    System.out.println("1. Ver passos para a correção");
                    System.out.println("0. Voltar para tela inicial");
                    userInput = scanner.nextLine().trim();
                    // Passos para a correção da árvore
                    if (userInput.equals("1")) {
                        corrigirArvore(scanner, pessoasSemSuperior);
                        invalidInput = false;
                        // Após correção, lista as linhas de reporte
                        listarLinhasDeReporte();
                    } else if (userInput.equals("0")) {
                        invalidInput = false;
                    } else {
                        System.out.println("Valor inválido, tente novamente!");
                    }
                }
            }
        }
    }