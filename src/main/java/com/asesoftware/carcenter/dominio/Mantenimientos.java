package com.asesoftware.carcenter.dominio;

import com.asesoftware.carcenter.infraestructura.Resultados;
import com.asesoftware.carcenter.infraestructura.Resultados.Falla;
import com.asesoftware.carcenter.infraestructura.ServicioDSL;
import com.asesoftware.carcenter.infraestructura.jpa.Repositorio;
import com.asesoftware.carcenter.infraestructura.jpa.entidad.Entidad;
import io.vavr.control.Either;
import lombok.*;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "mantenimientos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mantenimientos extends Entidad {

    @ToString.Include
    @NotNull
    @Basic(optional = false)
    @Column(name = "estado", nullable = false, length = 1)
    @Convert(converter = Estado.Convertidor.class)
    private Estado estado;

    @NotNull
    @NotBlank(message = "la fecha es requerida")
    @Column(name = "fecha", nullable = false)
    @Basic(optional = false)
    private LocalDateTime fecha;

    @Column(name = "presupuesto", nullable = true)
    @Basic(optional = true)
    private BigDecimal presupuesto;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_mecanico",
            foreignKey = @ForeignKey(name = "mantenimiento_mecanicos_fk"))
    private Mecanicos mecanicos;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_vehiculo",
            foreignKey = @ForeignKey(name = "man_vehicular_fk"))
    private Vehiculos vehiculos;
@Builder
    public Mantenimientos(Estado estado, LocalDateTime fecha, BigDecimal presupuesto, Mecanicos mecanicos, Vehiculos vehiculos) {
        this.estado = estado;
        this.fecha = fecha;
        this.presupuesto = presupuesto;
        this.mecanicos = mecanicos;
        this.vehiculos = vehiculos;
    validarAtributos();

    }

    @Repository
    public interface RepositoryMantenimiento extends Repositorio<Mantenimientos> {
    }


    public interface ServicioMantenimiento {

        Either<Falla, Resultados.Id> crearMantenimiento(BigDecimal presupuesto, Vehiculos vehiculos, Mecanicos mecanicos);

        List<Mantenimientos> listarMantenimiento();

        @Service
        class Impl extends ServicioDSL implements ServicioMantenimiento {

           private final RepositoryMantenimiento repositoryMantenimiento;

            public Impl(RepositoryMantenimiento repositoryMantenimiento) {
                this.repositoryMantenimiento = repositoryMantenimiento;
            }


            @Override
            public Either<Falla, Resultados.Id> crearMantenimiento(BigDecimal presupuesto, Vehiculos vehiculos, Mecanicos mecanicos){
                return nuevaInstanciaEntidad(contexto("Creando nuevo mantenimiento", repositoryMantenimiento),
                        crearInstancia(() -> Mantenimientos.builder()
                                .estado(Estado.ACTIVO)
                                .fecha(LocalDateTime.now())
                                .presupuesto(presupuesto)
                                .vehiculos(vehiculos)
                                .mecanicos(mecanicos)
                                .build()));
            }
            @Override
            public List<Mantenimientos> listarMantenimiento() {
                return repositoryMantenimiento.findAll();
            }
        }

    }
}
