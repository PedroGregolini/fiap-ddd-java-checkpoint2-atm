package br.fiap.bank.atm.presentation;

import br.fiap.bank.atm.application.AutorizacaoService;
import br.fiap.bank.atm.application.CadastroContaService;
import br.fiap.bank.atm.application.ContaService;
import br.fiap.bank.atm.model.Conta;
import br.fiap.bank.atm.model.ContaAcesso;
import br.fiap.bank.atm.model.Dinheiro;
import br.fiap.bank.atm.model.Movimentacao;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
//Fluxo
public class TerminalBancarioController {

    private static final DateTimeFormatter FORMATADOR_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final CadastroContaService cadastroContaService;
    private ContaService contaService;
    private AutorizacaoService autorizacaoService;
    private final Scanner scanner;

    public TerminalBancarioController(CadastroContaService cadastroContaService) {
        if (cadastroContaService == null) {
            throw new IllegalArgumentException("Serviço de cadastro é obrigatório.");
        }
        this.cadastroContaService = cadastroContaService;
        this.scanner = new Scanner(System.in);
    }

    // Fluxo principal: cadastra a conta, autentica e abre o menu
    public void iniciar() {
        try {
            Conta conta = cadastrarContaInicial();
            this.contaService = new ContaService(conta);
            this.autorizacaoService = new AutorizacaoService(conta);

            System.out.println("Bem-vindo(a), " + conta.getCliente().obterPrimeiroNome() + "!");

            if (autenticarUsuario()) {
                exibirMenuPrincipal();
            } else {
                System.out.println("Conta bloqueada por excesso de tentativas. Procure uma agência.");
            }
        } finally {
            // Garante que o scanner seja fechado mesmo se ocorrer alguma exceção
            scanner.close();
        }
    }

    // Coleta nome e senha para criar a conta no início da sessão
    private Conta cadastrarContaInicial() {
        String nomeCompleto = lerNomeCompleto();
        String senha = lerSenhaForte();
        return cadastroContaService.cadastrarContaCorrente(nomeCompleto, senha, Dinheiro.zero());
    }

    // Fica em loop até o usuário digitar um nome não vazio
    private String lerNomeCompleto() {
        String nomeCompleto = "";

        while (nomeCompleto.isEmpty()) {
            System.out.print("Digite seu nome completo: ");
            nomeCompleto = scanner.nextLine().trim();

            if (nomeCompleto.isEmpty()) {
                System.out.println("Nome inválido. Tente novamente.");
            }
        }

        return nomeCompleto;
    }

    // Valida a senha tentando criar um ContaAcesso temporário..
    private String lerSenhaForte() {
        String senha = "";
        boolean senhaCriada = false;

        System.out.println("A senha deve ter no mínimo 8 caracteres, 1 letra maiúscula, 1 número e 1 caractere especial.");

        while (!senhaCriada) {
            System.out.print("Cadastre uma senha: ");
            senha = scanner.nextLine();

            try {
                new ContaAcesso(senha);
                senhaCriada = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Senha inválida: " + e.getMessage());
            }
        }

        return senha;
    }

    // Tenta autenticar em loop. Para quando acerta a senha ou bloqueia por tentativas
    private boolean autenticarUsuario() {
        while (!autorizacaoService.isBloqueado()) {
            System.out.print("Digite sua senha: ");
            String senhaDigitada = scanner.nextLine();

            if (autorizacaoService.autorizar(senhaDigitada)) {
                return true;
            }

            if (!autorizacaoService.isBloqueado()) {
                System.out.println("Senha incorreta. Tente novamente.");
            }
        }

        return false;
    }

    // Loop principal do terminal — exibe o menu até o usuário escolher sair
    public void exibirMenuPrincipal() {
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n=== FIAP Bank ATM ===");
            System.out.println("[ 1 ] Consultar Saldo");
            System.out.println("[ 2 ] Fazer Depósito");
            System.out.println("[ 3 ] Fazer Saque");
            System.out.println("[ 4 ] Histórico de Movimentações");
            System.out.println("[ 5 ] Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1":
                    exibirSaldo();
                    break;
                case "2":
                    realizarDeposito();
                    break;
                case "3":
                    realizarSaque();
                    break;
                case "4":
                    exibirMovimentacoes();
                    break;
                case "5":
                    System.out.println("Sessão encerrada. Até logo!");
                    continuar = false;
                    break;
                default:
                    System.out.println("Opção inválida. Escolha entre 1 e 5.");
            }
        }
    }

    public void exibirSaldo() {
        System.out.println("Saldo atual: " + contaService.obterSaldo().formatado());
    }

    public void realizarDeposito() {
        System.out.print("Digite o valor do depósito: ");
        processarOperacao(scanner.nextLine(), true);
    }

    public void realizarSaque() {
        System.out.print("Digite o valor do saque: ");
        processarOperacao(scanner.nextLine(), false);
    }

    // Converte a entrada em Dinheiro e delega a operação ao serviço.
    private void processarOperacao(String entradaValor, boolean deposito) {
        try {
            Dinheiro valor = new Dinheiro(entradaValor);

            if (deposito) {
                contaService.realizarDeposito(valor);
                System.out.println("Depósito realizado. Saldo atual: " + contaService.obterSaldo().formatado());
            } else {
                contaService.realizarSaque(valor);
                System.out.println("Saque realizado. Saldo atual: " + contaService.obterSaldo().formatado());
            }
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Use números (ex: 100 ou 100,50).");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    // Exibe o extrato completo da conta com data, tipo e valor de cada movimentação
    public void exibirMovimentacoes() {
        List<Movimentacao> movimentacoes = contaService.obterMovimentacoes();

        if (movimentacoes.isEmpty()) {
            System.out.println("Nenhuma movimentação registrada.");
            return;
        }

        System.out.println("\n=== Histórico de Movimentações ===");

        for (Movimentacao mov : movimentacoes) {
            System.out.println(
                mov.getDataHora().format(FORMATADOR_DATA_HORA)
                + " | " + mov.getTipo()
                + " | " + mov.getValor().formatado()
            );
        }
    }
}
