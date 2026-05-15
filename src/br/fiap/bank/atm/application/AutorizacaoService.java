package br.fiap.bank.atm.application;

import br.fiap.bank.atm.model.Conta;

// Serviço de autenticação da sessão no terminal.
public class AutorizacaoService {

    private final Conta conta;

    public AutorizacaoService(Conta conta) {
        if (conta == null) {
            throw new IllegalArgumentException("Conta é obrigatória.");
        }
        this.conta = conta;
    }

    // Tenta autenticar com a senha
    public Boolean autorizar(String senha) {
        return conta.getContaAcesso().autenticar(senha);
    }

    // Indica se a conta foi bloqueada por excesso de tentativas erradas
    public Boolean isBloqueado() {
        return conta.getContaAcesso().isBloqueado();
    }
}
