package br.fiap.bank.atm.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Classe abstrata que representa uma conta bancária genérica.
public abstract class Conta extends BaseEntity {

    private final Cliente cliente;
    private final ContaAcesso contaAcesso;

    // Saldo é protegido: só pode ser alterado pelos métodos desta classe
    private Dinheiro saldo;

    private final LocalDate dataAbertura;
    private StatusConta status;

    // Histórico de todas as movimentações da conta
    private final List<Movimentacao> movimentacoes;

    protected Conta(Cliente cliente, ContaAcesso contaAcesso, Dinheiro saldoInicial) {
        super();
        validarCliente(cliente);
        validarContaAcesso(contaAcesso);
        validarSaldoInicial(saldoInicial);

        this.cliente = cliente;
        this.contaAcesso = contaAcesso;
        this.saldo = saldoInicial;
        this.dataAbertura = LocalDate.now();
        this.status = StatusConta.ATIVA;
        this.movimentacoes = new ArrayList<>();
    }

    // Ponto de entrada público para depósitos
    public void realizarDeposito(Dinheiro valor) {
        depositar(valor);
    }

    public void realizarSaque(Dinheiro valor) {
        validarContaAtiva();
        validarValorOperacao(valor);
        validarSaldoParaSaque(valor);
        sacar(valor);
        registrarMovimentacao(valor, TipoMovimentacao.SAQUE);
        aplicarRegraDeTaxa(); // cada tipo de conta define sua taxa aqui
    }

    protected abstract void aplicarRegraDeTaxa();

    // Permite que subclasses informem o custo total do saque (valor + taxa)
    protected Dinheiro obterCustoTotalDoSaque(Dinheiro valor) {
        return valor;
    }

    // Verifica se o saldo cobre o saque completo (incluindo possíveis taxas)
    protected void validarSaldoParaSaque(Dinheiro valor) {
        Dinheiro custoTotal = obterCustoTotalDoSaque(valor);

        if (saldo.menorQue(custoTotal)) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }
    }

    // Valida, debita e registra um depósito
    protected void depositar(Dinheiro valor) {
        validarContaAtiva();
        validarValorOperacao(valor);
        saldo = saldo.somar(valor);
        registrarMovimentacao(valor, TipoMovimentacao.DEPOSITO);
    }

    // Apenas debita o valor do sald
    protected void sacar(Dinheiro valor) {
        saldo = saldo.subtrair(valor);
    }

    // Debita uma taxa do saldo e registra como movimentação
    protected void debitarTaxa(Dinheiro taxa) {
        validarValorOperacao(taxa);
        saldo = saldo.subtrair(taxa);
        registrarMovimentacao(taxa, TipoMovimentacao.TAXA);
    }

    // Adiciona um rendimento ao saldo
    protected void creditarRendimento(Dinheiro rendimento) {
        validarValorOperacao(rendimento);
        saldo = saldo.somar(rendimento);
        registrarMovimentacao(rendimento, TipoMovimentacao.RENDIMENTO);
    }

    // Cria e adiciona uma nova entrada no histórico da conta
    protected void registrarMovimentacao(Dinheiro valor, TipoMovimentacao tipo) {
        movimentacoes.add(new Movimentacao(tipo, valor));
    }

    protected void validarValorOperacao(Dinheiro valor) {
        if (valor == null || !Boolean.TRUE.equals(valor.ehPositivo())) {
            throw new IllegalArgumentException("Valor inválido. Digite um valor maior que zero.");
        }
    }

    private void validarCliente(Cliente cliente) {
        if (cliente == null) throw new IllegalArgumentException("Cliente é obrigatório.");
    }

    private void validarContaAcesso(ContaAcesso contaAcesso) {
        if (contaAcesso == null) throw new IllegalArgumentException("Conta de acesso é obrigatória.");
    }

    private void validarSaldoInicial(Dinheiro saldoInicial) {
        if (saldoInicial == null || Boolean.TRUE.equals(saldoInicial.ehNegativo())) {
            throw new IllegalArgumentException("Saldo inicial não pode ser negativo.");
        }
    }

    private void validarContaAtiva() {
        if (!StatusConta.ATIVA.equals(status)) {
            throw new IllegalStateException("Conta não está ativa.");
        }
    }

    public Cliente getCliente() { return cliente; }

    public ContaAcesso getContaAcesso() { return contaAcesso; }

    public Dinheiro getSaldo() { return saldo; }

    public LocalDate getDataAbertura() { return dataAbertura; }

    public StatusConta getStatus() { return status; }


    public List<Movimentacao> getMovimentacoes() {
        return Collections.unmodifiableList(movimentacoes);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Conta)) return false;
        Conta other = (Conta) obj;
        return getId().equals(other.getId());
    }
}
