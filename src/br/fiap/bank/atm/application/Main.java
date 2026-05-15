package br.fiap.bank.atm.application;

import br.fiap.bank.atm.infrastructure.ContaMemoriaRepository;
import br.fiap.bank.atm.presentation.TerminalBancarioController;

// começoo da entrada da aplicação.
public class Main {

    public static void main(String[] args) {
        ContaFactory contaFactory = ContaFactory.getInstance();
        ContaMemoriaRepository contaRepository = ContaMemoriaRepository.getInstance();

        CadastroContaService cadastroContaService = new CadastroContaService(contaFactory, contaRepository);

        TerminalBancarioController terminalBancarioController = new TerminalBancarioController(cadastroContaService);
        terminalBancarioController.iniciar();
    }
}
