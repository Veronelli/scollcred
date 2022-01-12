package com.scollcred.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Creditos.
 */
@Entity
@Table(name = "creditos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Creditos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "emision_cuotas", nullable = false)
    private Integer emisionCuotas;

    @NotNull
    @Column(name = "monto", nullable = false)
    private Integer monto;

    @NotNull
    @Column(name = "pago_cuota", nullable = false)
    private Integer pagoCuota;

    @NotNull
    @Column(name = "cantidad_cuotas", nullable = false)
    private Integer cantidadCuotas;

    @NotNull
    @Column(name = "tomado", nullable = false)
    private LocalDate tomado;

    @NotNull
    @Column(name = "inicio_pago", nullable = false)
    private LocalDate inicioPago;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "dependencia" }, allowSetters = true)
    private Cliente cliente;

    @ManyToOne(optional = false)
    @NotNull
    private Mutual mutual;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Creditos id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEmisionCuotas() {
        return this.emisionCuotas;
    }

    public Creditos emisionCuotas(Integer emisionCuotas) {
        this.setEmisionCuotas(emisionCuotas);
        return this;
    }

    public void setEmisionCuotas(Integer emisionCuotas) {
        this.emisionCuotas = emisionCuotas;
    }

    public Integer getMonto() {
        return this.monto;
    }

    public Creditos monto(Integer monto) {
        this.setMonto(monto);
        return this;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
    }

    public Integer getPagoCuota() {
        return this.pagoCuota;
    }

    public Creditos pagoCuota(Integer pagoCuota) {
        this.setPagoCuota(pagoCuota);
        return this;
    }

    public void setPagoCuota(Integer pagoCuota) {
        this.pagoCuota = pagoCuota;
    }

    public Integer getCantidadCuotas() {
        return this.cantidadCuotas;
    }

    public Creditos cantidadCuotas(Integer cantidadCuotas) {
        this.setCantidadCuotas(cantidadCuotas);
        return this;
    }

    public void setCantidadCuotas(Integer cantidadCuotas) {
        this.cantidadCuotas = cantidadCuotas;
    }

    public LocalDate getTomado() {
        return this.tomado;
    }

    public Creditos tomado(LocalDate tomado) {
        this.setTomado(tomado);
        return this;
    }

    public void setTomado(LocalDate tomado) {
        this.tomado = tomado;
    }

    public LocalDate getInicioPago() {
        return this.inicioPago;
    }

    public Creditos inicioPago(LocalDate inicioPago) {
        this.setInicioPago(inicioPago);
        return this;
    }

    public void setInicioPago(LocalDate inicioPago) {
        this.inicioPago = inicioPago;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Creditos cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public Mutual getMutual() {
        return this.mutual;
    }

    public void setMutual(Mutual mutual) {
        this.mutual = mutual;
    }

    public Creditos mutual(Mutual mutual) {
        this.setMutual(mutual);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Creditos)) {
            return false;
        }
        return id != null && id.equals(((Creditos) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Creditos{" +
            "id=" + getId() +
            ", emisionCuotas=" + getEmisionCuotas() +
            ", monto=" + getMonto() +
            ", pagoCuota=" + getPagoCuota() +
            ", cantidadCuotas=" + getCantidadCuotas() +
            ", tomado='" + getTomado() + "'" +
            ", inicioPago='" + getInicioPago() + "'" +
            "}";
    }
}
