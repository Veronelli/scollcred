package com.scollcred.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Dependencia.
 */
@Entity
@Table(name = "dependencia")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dependencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 4)
    @Column(name = "empleador", nullable = false)
    private String empleador;

    @OneToMany(mappedBy = "dependencia")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dependencia" }, allowSetters = true)
    private Set<Cliente> clientes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dependencia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmpleador() {
        return this.empleador;
    }

    public Dependencia empleador(String empleador) {
        this.setEmpleador(empleador);
        return this;
    }

    public void setEmpleador(String empleador) {
        this.empleador = empleador;
    }

    public Set<Cliente> getClientes() {
        return this.clientes;
    }

    public void setClientes(Set<Cliente> clientes) {
        if (this.clientes != null) {
            this.clientes.forEach(i -> i.setDependencia(null));
        }
        if (clientes != null) {
            clientes.forEach(i -> i.setDependencia(this));
        }
        this.clientes = clientes;
    }

    public Dependencia clientes(Set<Cliente> clientes) {
        this.setClientes(clientes);
        return this;
    }

    public Dependencia addCliente(Cliente cliente) {
        this.clientes.add(cliente);
        cliente.setDependencia(this);
        return this;
    }

    public Dependencia removeCliente(Cliente cliente) {
        this.clientes.remove(cliente);
        cliente.setDependencia(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dependencia)) {
            return false;
        }
        return id != null && id.equals(((Dependencia) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dependencia{" +
            "id=" + getId() +
            ", empleador='" + getEmpleador() + "'" +
            "}";
    }
}
