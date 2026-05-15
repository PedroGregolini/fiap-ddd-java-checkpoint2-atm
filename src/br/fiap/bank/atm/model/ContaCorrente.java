package br.fiap.bank.atm.model;

// Implementação de conta corrente.
public class ContaCorrente extends Conta {

    private static final Dinheiro TAXA_SAQUE = new Dinheiro("1.50");

    public ContaCorrente(Cliente cliente, ContaAcesso contaAcesso, Dinheiro saldoInicial) {
        super(cliente, contaAcesso, saldoInicial);
    }

    // Informa à classe pai que o custo real do saque é o valor + a taxa,
    @Override
    protected Dinheiro obterCustoTotalDoSaque(Dinheiro valor) {
        return valor.somar(TAXA_SAQUE);
    }

    // Chamado automaticamente pelo Template Method após o saque.
    @Override
    protected void aplicarRegraDeTaxa() {
        debitarTaxa(TAXA_SAQUE);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ContaCorrente)) return false;
        return super.equals(obj);
    }
}
