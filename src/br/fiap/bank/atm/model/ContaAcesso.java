package br.fiap.bank.atm.model;

// Value Object responsável pela segurança de acesso à conta.
public class ContaAcesso {

    // Após 3 tentativas erradas a conta é bloqueada
    public static final Integer MAXIMO_TENTATIVAS = 3;

    // Senha forte: mínimo 8 chars, 1 maiúscula, 1 número e 1 especial
    private static final String REGRA_SENHA_FORTE = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&*()\\-_+=?><]).{8,}$";

    private final String senha;
    private Integer tentativas;
    private Boolean bloqueado;

    public ContaAcesso(String senha) {
        validarSenhaForte(senha);
        this.senha = senha;
        this.tentativas = 0;
        this.bloqueado = Boolean.FALSE;
    }

    private void validarSenhaForte(String senha) {
        if (senha == null || !senha.matches(REGRA_SENHA_FORTE)) {
            throw new IllegalArgumentException(
                "A senha deve ter no mínimo 8 caracteres, ao menos 1 número, 1 letra maiúscula e 1 caractere especial."
            );
        }
    }

    // Verifica a senha digitada..
    public Boolean autenticar(String senhaDigitada) {
        if (bloqueado) {
            return Boolean.FALSE;
        }

        if (senha.equals(senhaDigitada)) {
            resetarTentativas();
            return Boolean.TRUE;
        }

        registrarTentativaInvalida();
        return Boolean.FALSE;
    }

    public Boolean validarSenha(String senhaDigitada) {
        return autenticar(senhaDigitada);
    }

    private void registrarTentativaInvalida() {
        tentativas = tentativas + 1;

        // Bloqueia automaticamente ao esgotar as tentativas
        if (tentativas >= MAXIMO_TENTATIVAS) {
            bloqueado = Boolean.TRUE;
        }
    }

    public Boolean isBloqueado() {
        return bloqueado;
    }

    public void resetarTentativas() {
        tentativas = 0;
    }

    public Integer getTentativas() {
        return tentativas;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ContaAcesso)) return false;
        ContaAcesso other = (ContaAcesso) obj;
        return senha.equals(other.senha);
    }
}
