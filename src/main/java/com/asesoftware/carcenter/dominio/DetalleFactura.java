package com.asesoftware.carcenter.dominio;

import com.asesoftware.carcenter.dominio.Clientes.RepositoryClientes;
import com.asesoftware.carcenter.dominio.Clientes.ServicioClientes;
import com.asesoftware.carcenter.infraestructura.Resultados;
import com.asesoftware.carcenter.infraestructura.Resultados.Falla;
import com.asesoftware.carcenter.infraestructura.ServicioDSL;
import com.asesoftware.carcenter.infraestructura.jpa.Repositorio;
import com.asesoftware.carcenter.infraestructura.jpa.entidad.Entidad;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Table(name = "detalle_factura")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DetalleFactura extends Entidad {


    @NotNull
    @NotBlank(message = "La ruta es requerida")
    @Column(name = "ruta", nullable = false, length = 6)
    @Basic(optional = false)
    private BigDecimal precioUnidad;

    @NotNull
    @Column(name = "numero_unidades", nullable = false)
    @Basic(optional = false)
    private Integer numeroUnidades;

    @NotNull
    @Column(name = "descuento", nullable = false)
    @Basic(optional = false)
    private BigDecimal descuento;

    @NotNull
    @Column(name = "precio_mano_obra", nullable = false)
    @Basic(optional = false)
    private BigDecimal precioManoObra;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_mantenimiento",
            foreignKey = @ForeignKey(name = "det_factura_man_fk"))
    private Mantenimientos mantenimientos;

    @Builder
    public DetalleFactura(BigDecimal precioUnidad, Integer numeroUnidades, BigDecimal descuento, BigDecimal precioManoObra, Mantenimientos mantenimientos) {
        this.precioUnidad = precioUnidad;
        this.numeroUnidades = numeroUnidades;
        this.descuento = descuento;
        this.precioManoObra = precioManoObra;
        this.mantenimientos = mantenimientos;
    }

    @Repository
    public interface RepositoryDetalleFactura extends Repositorio<DetalleFactura> {

    }


    public interface ServicioDetalleFactura {

        Either<Falla, Resultados.Id> crearDetalle();



        @Service
        class Impl extends ServicioDSL implements ServicioDetalleFactura {


            @Override
            public Either<Falla, Resultados.Id> crearDetalle() {
                return null;
            }
        }

    }

}
