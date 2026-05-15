package br.fiap.bank.atm.model;

// Entidade que representa o correntista do banco.
public class Cliente extends BaseEntity {

    // Nome completo é obrigatório e imutável após o cadastro
    private final String nomeCompleto;

    // O construtor já valida o nome, garantindo que nenhum Cliente seja criado sem identificação

    public Cliente(String nomeCompleto) {
        super();
        validarNomeCompleto(nomeCompleto);
        this.nomeCompleto = nomeCompleto.trim();
    }

    private void validarNomeCompleto(String nomeCompleto) {
        if (nomeCompleto == null || nomeCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome completo é obrigatório.");
        }
    }

    // Extrai só o primeiro nome
    public String obterPrimeiroNome() {
        return nomeCompleto.split("\\s+")[0];
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Cliente)) return false;
        Cliente other = (Cliente) obj;
        return getId().equals(other.getId()) && nomeCompleto.equals(other.nomeCompleto);
    }
}
