package com.asesoftware.carcenter.dominio;

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

@Entity
@Getter
@Table(name = "servicios_x_mantenimientos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MantenimientoServicios extends Entidad {
    @NotNull
    @NotBlank(message = "El tiempo estimado es requerido")
    @Column(name = "tiempo_estimado", nullable = false)
    @Basic(optional = false)
    private Integer tiempoEstimado;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_mantenimiento",
            foreignKey = @ForeignKey(name = "ser_x_man_man_fk"))
    private Mantenimientos mantenimientos;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_servicio",
            foreignKey = @ForeignKey(name = "ser_x_man_ser_fk"))
    private Servicios servicios;

    @Builder
    public MantenimientoServicios(Integer tiempoEstimado, Mantenimientos mantenimientos, Servicios servicios) {
        this.tiempoEstimado = tiempoEstimado;
        this.mantenimientos = mantenimientos;
        this.servicios = servicios;
        validarAtributos();
    }

    @Repository
    public interface RepositoryMantenimientoServicio extends Repositorio<MantenimientoServicios> {
    }

    public interface ServicioMantenimientoServicio {

        Either<Falla, Resultados.Id> crearMantenimientoServicio(Integer tiempoEstimado, Mantenimientos mantenimientos, Servicios servicios);

        List<MantenimientoServicios> listarMantenimientoServicios();

        @Service
        class Impl extends ServicioDSL implements ServicioMantenimientoServicio {

            private final RepositoryMantenimientoServicio repositoryMantenimientoServicio;

            public Impl(RepositoryMantenimientoServicio repositoryMantenimientoServicio) {
                this.repositoryMantenimientoServicio = repositoryMantenimientoServicio;
            }


            @Override
            public Either<Falla, Resultados.Id> crearMantenimientoServicio(Integer tiempoEstimado, Mantenimientos mantenimientos, Servicios servicios) {
                return nuevaInstanciaEntidad(contexto("Creando nuevo servicio mantenimiento", repositoryMantenimientoServicio),
                        crearInstancia(() -> MantenimientoServicios.builder()
                                .tiempoEstimado(tiempoEstimado)
                                .mantenimientos(mantenimientos)
                                .servicios(servicios)
                                .build()));
            }
            @Override
            public List<MantenimientoServicios> listarMantenimientoServicios() {
                return repositoryMantenimientoServicio.findAll();
            }
        }

    }
}
