package br.fiap.bank.atm.infrastructure;

import br.fiap.bank.atm.model.Conta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Repositório em memória para armazenar contas enquanto o sistema está rodando.
public class ContaMemoriaRepository {

    private static ContaMemoriaRepository instance;
    private final List<Conta> contas;

    private ContaMemoriaRepository() {
        this.contas = new ArrayList<>();
    }

    public static ContaMemoriaRepository getInstance() {
        if (instance == null) {
            instance = new ContaMemoriaRepository();
        }
        return instance;
    }

    public void salvar(Conta conta) {
        if (conta == null) {
            throw new IllegalArgumentException("Conta é obrigatória para armazenamento.");
        }
        contas.add(conta);
    }

    public List<Conta> listar() {
        return Collections.unmodifiableList(contas);
    }
}
