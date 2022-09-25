package com.asesoftware.carcenter.dominio;

import com.asesoftware.carcenter.infraestructura.Resultados;
import com.asesoftware.carcenter.infraestructura.Resultados.Falla;
import com.asesoftware.carcenter.infraestructura.Resultados.Id;
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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Table(name = "repuestos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Repuestos extends Entidad {

    @NotNull
    @NotBlank(message = "El nombre es requerido")
    @Column(name = "nombre_repuesto", nullable = false, length = 100)
    @Basic(optional = false)
    private String nombre;

    @NotNull
    @Column(name = "precio_unitario", nullable = false)
    @Basic(optional = false)
    private BigDecimal precioUnitario;

    @NotNull
    @Column(name = "unidades_inventario", nullable = false)
    @Basic(optional = false)
    private Integer unidadesInventario;

    @NotNull
    @NotBlank(message = "El proveedor es requerido")
    @Column(name = "proveedor", nullable = false)
    @Basic(optional = false)
    private String proveedor;

    @Builder
    public Repuestos(String nombre, BigDecimal precioUnitario, Integer unidadesInventario, String proveedor) {
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.unidadesInventario = unidadesInventario;
        this.proveedor = proveedor;
        validarAtributos();
    }

    @Repository
    public interface RepositoryRepuestos extends Repositorio<Repuestos> {

        Optional<Repuestos> findByNombre(String nombre);
    }
    public interface ServicioRepuestos {

        Either<Falla, Id> crearRepuestos(String nombre, BigDecimal precioUnitario, Integer unidadesInventario, String proveedor);

        List<Repuestos> listarRepuestos();

        @Service
        class Impl extends ServicioDSL implements ServicioRepuestos {

            private final RepositoryRepuestos repositoryRepuestos;

            public Impl(RepositoryRepuestos repositoryRepuestos) {
                this.repositoryRepuestos = repositoryRepuestos;
            }


            @Override
            public Either<Falla, Resultados.Id> crearRepuestos(String nombre, BigDecimal precioUnitario, Integer unidadesInventario, String proveedor) {
                return nuevaInstanciaEntidad(contexto("Creando nuevo repuesto con nombre: %s".formatted(nombre), repositoryRepuestos),
                        detectarDuplicado(repositoryRepuestos::findByNombre,nombre)
                        ,crearInstancia(() -> Repuestos.builder()
                                .nombre(nombre)
                                .precioUnitario(precioUnitario)
                                .unidadesInventario(unidadesInventario)
                                .proveedor(proveedor)
                                .build()));
            }
            @Override
            public List<Repuestos> listarRepuestos() {
                return repositoryRepuestos.findAll();
            }
        }

    }
}
