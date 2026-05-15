package br.fiap.bank.atm.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

// Value Object para representar valores
public class Dinheiro {

    private static final int ESCALA_MONETARIA = 2;
    private static final Locale LOCALE_BRASIL = Locale.forLanguageTag("pt-BR");

    private final BigDecimal valor;

    public Dinheiro(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor monetário é obrigatório.");
        }
        // Garante sempre 2 casas decimais com arredondamento padrão bancário
        this.valor = valor.setScale(ESCALA_MONETARIA, RoundingMode.HALF_UP);
    }

    // Aceita String para facilitar a entrada do usuário
    public Dinheiro(String valor) {
        this(new BigDecimal(normalizarEntrada(valor)));
    }

    // Troca vírgula por ponto para o BigDecimal aceitar corretamente
    private static String normalizarEntrada(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("Valor monetário é obrigatório.");
        }
        return valor.trim().replace(",", ".");
    }

    // Atalho para criar um Dinheiro zerado
    public static Dinheiro zero() {
        return new Dinheiro(BigDecimal.ZERO);
    }

    // Cada operação retorna um novo Dinheiro, mantendo a imutabilidade
    public Dinheiro somar(Dinheiro outro) {
        validarOutro(outro);
        return new Dinheiro(valor.add(outro.valor));
    }

    public Dinheiro subtrair(Dinheiro outro) {
        validarOutro(outro);
        return new Dinheiro(valor.subtract(outro.valor));
    }

    // Usado para calcular rendimentos
    public Dinheiro multiplicar(BigDecimal fator) {
        if (fator == null) throw new IllegalArgumentException("Fator é obrigatório.");
        return new Dinheiro(valor.multiply(fator));
    }

    public Boolean maiorQue(Dinheiro outro) {
        validarOutro(outro);
        return valor.compareTo(outro.valor) > 0;
    }

    public Boolean menorQue(Dinheiro outro) {
        validarOutro(outro);
        return valor.compareTo(outro.valor) < 0;
    }

    public Boolean maiorOuIgual(Dinheiro outro) {
        validarOutro(outro);
        return valor.compareTo(outro.valor) >= 0;
    }

    public Boolean menorOuIgual(Dinheiro outro) {
        validarOutro(outro);
        return valor.compareTo(outro.valor) <= 0;
    }

    public Boolean ehPositivo() {
        return valor.compareTo(BigDecimal.ZERO) > 0;
    }

    public Boolean ehZero() {
        return valor.compareTo(BigDecimal.ZERO) == 0;
    }

    public Boolean ehNegativo() {
        return valor.compareTo(BigDecimal.ZERO) < 0;
    }

    public BigDecimal getValor() {
        return valor;
    }

    // Formata o valor no padrão brasileiro: R$ 1.500,00
    public String formatado() {
        return NumberFormat.getCurrencyInstance(LOCALE_BRASIL).format(valor);
    }

    private void validarOutro(Dinheiro outro) {
        if (outro == null) throw new IllegalArgumentException("Valor monetário comparado é obrigatório.");
    }

    @Override
    public String toString() {
        return formatado();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Dinheiro)) return false;
        Dinheiro other = (Dinheiro) obj;
        return valor.compareTo(other.valor) == 0;
    }
}
