package br.fiap.bank.atm.application;

import br.fiap.bank.atm.infrastructure.ContaMemoriaRepository;
import br.fiap.bank.atm.model.Cliente;
import br.fiap.bank.atm.model.Conta;
import br.fiap.bank.atm.model.ContaAcesso;
import br.fiap.bank.atm.model.Dinheiro;

// Serviço responsável pelo cadastro de novas contas
public class CadastroContaService {

    private final ContaFactory contaFactory;
    private final ContaMemoriaRepository contaRepository;

    public CadastroContaService(ContaFactory contaFactory, ContaMemoriaRepository contaRepository) {
        if (contaFactory == null || contaRepository == null) {
            throw new IllegalArgumentException("Dependências de cadastro são obrigatórias.");
        }
        this.contaFactory = contaFactory;
        this.contaRepository = contaRepository;
    }

    // Cria o client
    public Conta cadastrarContaCorrente(String nomeCompleto, String senha, Dinheiro saldoInicial) {
        Cliente cliente = new Cliente(nomeCompleto);
        ContaAcesso contaAcesso = new ContaAcesso(senha);
        Conta conta = contaFactory.criarContaCorrente(cliente, contaAcesso, saldoInicial);
        contaRepository.salvar(conta);
        return conta;
    }
}
