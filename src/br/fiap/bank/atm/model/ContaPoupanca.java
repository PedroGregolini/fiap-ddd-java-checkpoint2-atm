package br.fiap.bank.atm.model;

import java.math.BigDecimal;

// Implementação de conta poupança.
public class ContaPoupanca extends Conta {

    private static final BigDecimal TAXA_RENDIMENTO_MENSAL = new BigDecimal("0.005");

    public ContaPoupanca(Cliente cliente, ContaAcesso contaAcesso, Dinheiro saldoInicial) {
        super(cliente, contaAcesso, saldoInicial);
    }

    // Poupança não cobra taxa de saque
    @Override
    protected void aplicarRegraDeTaxa() {
        // Conta poupança não cobra taxa de saque.
    }

    // Aplica o rendimento mensal sobre o saldo atual e registra no histórico
    public void renderJuros() {
        Dinheiro rendimento = getSaldo().multiplicar(TAXA_RENDIMENTO_MENSAL);

        if (rendimento.ehPositivo()) {
            creditarRendimento(rendimento);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ContaPoupanca)) return false;
        return super.equals(obj);
    }
}
