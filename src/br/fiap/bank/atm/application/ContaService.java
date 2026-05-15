package br.fiap.bank.atm.application;

import br.fiap.bank.atm.model.Conta;
import br.fiap.bank.atm.model.Dinheiro;
import br.fiap.bank.atm.model.Movimentacao;

import java.util.List;

// Serviço que expõe as operações da conta para a camada de apresentação.
public class ContaService {

    private final Conta conta;

    public ContaService(Conta conta) {
        if (conta == null) {
            throw new IllegalArgumentException("Conta é obrigatória.");
        }
        this.conta = conta;
    }

    public void realizarDeposito(Dinheiro valor) {
        conta.realizarDeposito(valor);
    }

    public void realizarSaque(Dinheiro valor) {
        conta.realizarSaque(valor);
    }

    public Dinheiro obterSaldo() {
        return conta.getSaldo();
    }

    // Retorna a lista
    public List<Movimentacao> obterMovimentacoes() {
        return conta.getMovimentacoes();
    }

    public Conta getConta() {
        return conta;
    }
}
