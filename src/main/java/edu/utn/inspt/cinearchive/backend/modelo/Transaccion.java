package edu.utn.inspt.cinearchive.backend.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Transaccion implements Serializable {

    public enum Estado {
        COMPLETADA,
        PENDIENTE,
        FALLIDA,
        REEMBOLSADA
    }

    private Long id;

    @NotNull
    private Long usuarioId;

    @NotNull
    private Long alquilerId;

    @Min(0)
    private BigDecimal monto;

    @NotNull
    private String metodoPago;

    private LocalDateTime fechaTransaccion;

    private Estado estado;

    private String referenciaExterna;

    public Transaccion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getAlquilerId() {
        return alquilerId;
    }

    public void setAlquilerId(Long alquilerId) {
        this.alquilerId = alquilerId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public LocalDateTime getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(LocalDateTime fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getReferenciaExterna() {
        return referenciaExterna;
    }

    public void setReferenciaExterna(String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaccion)) return false;
        Transaccion that = (Transaccion) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Transaccion{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", monto=" + monto +
                '}';
    }
}
