package br.fiap.bank.atm.application;

import br.fiap.bank.atm.model.Cliente;
import br.fiap.bank.atm.model.Conta;
import br.fiap.bank.atm.model.ContaAcesso;
import br.fiap.bank.atm.model.ContaCorrente;
import br.fiap.bank.atm.model.ContaPoupanca;
import br.fiap.bank.atm.model.Dinheiro;

// Factory responsável por criar os diferentes tipos de conta.
public class ContaFactory {

    private static ContaFactory instance;

    private ContaFactory() {}

    // Retorna sempre a mesma instância
    public static ContaFactory getInstance() {
        if (instance == null) {
            instance = new ContaFactory();
        }
        return instance;
    }

    // Cria uma conta corrente — cobra taxa de R$ 1,50 por saque
    public Conta criarContaCorrente(Cliente cliente, ContaAcesso contaAcesso, Dinheiro saldoInicial) {
        return new ContaCorrente(cliente, contaAcesso, saldoInicial);
    }

    // Cria uma conta poupança — sem taxa de saque, com rendimento mensal de 0,5%
    public Conta criarContaPoupanca(Cliente cliente, ContaAcesso contaAcesso, Dinheiro saldoInicial) {
        return new ContaPoupanca(cliente, contaAcesso, saldoInicial);
    }
}
