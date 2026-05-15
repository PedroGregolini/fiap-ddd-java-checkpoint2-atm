package br.fiap.bank.atm.model;

import java.time.LocalDateTime;

// Value Object que representa um registro no extrato da conta.
public class Movimentacao {

    private final LocalDateTime dataHora;
    private final TipoMovimentacao tipo;
    private final Dinheiro valor;

    public Movimentacao(TipoMovimentacao tipo, Dinheiro valor) {
        validarTipo(tipo);
        validarValor(valor);
        this.dataHora = LocalDateTime.now();
        this.tipo = tipo;
        this.valor = valor;
    }

    private void validarTipo(TipoMovimentacao tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de movimentação é obrigatório.");
        }
    }

    private void validarValor(Dinheiro valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor da movimentação é obrigatório.");
        }
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public Dinheiro getValor() {
        return valor;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Movimentacao)) return false;
        Movimentacao other = (Movimentacao) obj;
        return dataHora.equals(other.dataHora)
                && tipo.equals(other.tipo)
                && valor.equals(other.valor);
    }
}
