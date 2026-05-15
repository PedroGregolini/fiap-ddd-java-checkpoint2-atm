package br.fiap.bank.atm.model;

import java.time.LocalDate;
import java.util.UUID;

// Classe base para todas as entidades do sistema.
public abstract class BaseEntity {

    // ID único gerado automaticamente ao criar qualquer entidade
    private final UUID id;

    // Data em que o objeto foi criado no sistema
    private final LocalDate dataCriacao;

    protected BaseEntity() {
        this.id = UUID.randomUUID();
        this.dataCriacao = LocalDate.now();
    }

    public UUID getId() {
        return id;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BaseEntity other = (BaseEntity) obj;
        return id.equals(other.id);
    }
}
