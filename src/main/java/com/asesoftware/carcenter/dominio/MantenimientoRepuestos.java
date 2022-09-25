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
import java.util.List;

@Entity
@Getter
@Table(name = "repuestos_x_mantenimientos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MantenimientoRepuestos extends Entidad {

    @NotNull
    @NotBlank(message = "Las unidades son requeridas")
    @Column(name = "unidades", nullable = false)
    @Basic(optional = false)
    private Integer unidades;

    @NotNull
    @NotBlank(message = "El tiempo estimado es requerido")
    @Column(name = "tiempo_estimado", nullable = false)
    @Basic(optional = false)
    private Integer tiempoEstimado;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_mantenimiento",
            foreignKey = @ForeignKey(name = "rep_x_mtos_man_fk"))
    private Mantenimientos mantenimientos;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_repuesto",
            foreignKey = @ForeignKey(name = "rep_x_man_rep_fk"))
    private Repuestos repuestos;

    @Builder
    public MantenimientoRepuestos(Integer unidades, Integer tiempoEstimado, Mantenimientos mantenimientos, Repuestos repuestos) {
        this.unidades = unidades;
        this.tiempoEstimado = tiempoEstimado;
        this.mantenimientos = mantenimientos;
        this.repuestos = repuestos;
        validarAtributos();
    }

    @Repository
    public interface RepositoryMantenimientoRepuestos extends Repositorio<MantenimientoRepuestos> {
    }

    @Repository
    public interface RepositoryMantenimientoServicio extends Repositorio<MantenimientoServicios> {
    }

    public interface ServicioMantenimientoRepuestos {

        Either<Falla, Resultados.Id> crearMantenimientoRepuestos(Integer unidades, Integer tiempoEstimado, Mantenimientos mantenimientos, Repuestos repuestos);

        List<MantenimientoRepuestos> listarMantenimientoRepuestos();

        @Service
        class Impl extends ServicioDSL implements ServicioMantenimientoRepuestos {

           private final RepositoryMantenimientoRepuestos repositoryMantenimientoRepuestos;

            public Impl(RepositoryMantenimientoRepuestos repositoryMantenimientoRepuestos) {
                this.repositoryMantenimientoRepuestos = repositoryMantenimientoRepuestos;
            }


            @Override
            public Either<Falla, Resultados.Id> crearMantenimientoRepuestos(Integer unidades, Integer tiempoEstimado, Mantenimientos mantenimientos, Repuestos repuestos) {
                return nuevaInstanciaEntidad(contexto("Creando nuevo repuestos mantenimiento", repositoryMantenimientoRepuestos),
                        crearInstancia(() -> MantenimientoRepuestos.builder()
                                .unidades(unidades)
                                .tiempoEstimado(tiempoEstimado)
                                .mantenimientos(mantenimientos)
                                .repuestos(repuestos)
                                .build()));
            }
            @Override
            public List<MantenimientoRepuestos> listarMantenimientoRepuestos() {
                return repositoryMantenimientoRepuestos.findAll();
            }
        }

    }
}
