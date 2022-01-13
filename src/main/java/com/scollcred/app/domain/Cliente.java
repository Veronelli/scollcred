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
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 4)
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Size(min = 4)
    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Size(min = 10)
    @Column(name = "telefono")
    private String telefono;

    @Size(min = 8)
    @Column(name = "dni")
    private String dni;

    @OneToMany(mappedBy = "cliente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "mutual", "cliente" }, allowSetters = true)
    private Set<Creditos> creditos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "clientes" }, allowSetters = true)
    private Dependencia dependencia;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cliente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Cliente nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public Cliente apellido(String apellido) {
        this.setApellido(apellido);
        return this;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public Cliente telefono(String telefono) {
        this.setTelefono(telefono);
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDni() {
        return this.dni;
    }

    public Cliente dni(String dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Set<Creditos> getCreditos() {
        return this.creditos;
    }

    public void setCreditos(Set<Creditos> creditos) {
        if (this.creditos != null) {
            this.creditos.forEach(i -> i.setCliente(null));
        }
        if (creditos != null) {
            creditos.forEach(i -> i.setCliente(this));
        }
        this.creditos = creditos;
    }

    public Cliente creditos(Set<Creditos> creditos) {
        this.setCreditos(creditos);
        return this;
    }

    public Cliente addCreditos(Creditos creditos) {
        this.creditos.add(creditos);
        creditos.setCliente(this);
        return this;
    }

    public Cliente removeCreditos(Creditos creditos) {
        this.creditos.remove(creditos);
        creditos.setCliente(null);
        return this;
    }

    public Dependencia getDependencia() {
        return this.dependencia;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public Cliente dependencia(Dependencia dependencia) {
        this.setDependencia(dependencia);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        return id != null && id.equals(((Cliente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellido='" + getApellido() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", dni='" + getDni() + "'" +
            "}";
    }
}
