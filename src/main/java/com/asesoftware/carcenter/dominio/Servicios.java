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
@Table(name = "Servicios")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Servicios extends Entidad {

    @NotNull
    @NotBlank(message = "El nombre es requerido")
    @Column(name = "nombre_servicio", nullable = false, length = 100)
    @Basic(optional = false)
    private String nombre;

    @NotNull
    @Column(name = "precio", nullable = false)
    @Basic(optional = false)
    private BigDecimal precio;

    @Builder
    public Servicios(String nombre, BigDecimal precio) {
        this.nombre = nombre;
        this.precio = precio;
        validarAtributos();
    }

    @Repository
    public interface RepositoryServicios extends Repositorio<Servicios> {
        Optional<Servicios> findByNombre(String nombre);
    }

    public interface ServicioServicio {

        Either<Falla, Id> crearServicio(String nombre, BigDecimal precio);

        List<Servicios> listarServicios();

        @Service
        class Impl extends ServicioDSL implements ServicioServicio {

            private final RepositoryServicios repositoryServicios;

            public Impl(RepositoryServicios repositoryServicios) {
                this.repositoryServicios = repositoryServicios;
            }


            @Override
            public Either<Falla, Resultados.Id> crearServicio(String nombre, BigDecimal precio) {
                return nuevaInstanciaEntidad(contexto("Creando nuevo servicio con nombre: %s".formatted(nombre), repositoryServicios),
                        detectarDuplicado(repositoryServicios::findByNombre,nombre)
                        ,crearInstancia(() -> Servicios.builder()
                                .nombre(nombre)
                                .precio(precio)
                                .build()));
            }
            @Override
            public List<Servicios> listarServicios() {
                return repositoryServicios.findAll();
            }
        }

    }
}
