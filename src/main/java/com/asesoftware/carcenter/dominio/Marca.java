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
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "marcas")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Marca extends Entidad {

    @NotNull
    @NotBlank(message = "Nombre de la Marca es requerido")
    @Column(name = "nombre_marca", nullable = false, length = 30)
    @Basic(optional = false)
    private String nombre;

    @Builder
    public Marca(String nombre){
        this.nombre = nombre;
        validarAtributos();
    }

    @Repository
    public interface RepositoryMarca extends Repositorio<Marca> {
        Optional<Clientes> findByNombre(String nombre);
    }


    public interface ServicioMarcas {

        Either<Falla, Id> crearMarca(String nombre);

        List<Marca> listarMarcas();

        @Service
        class Impl extends ServicioDSL implements ServicioMarcas {

            private final RepositoryMarca repositoryMarca;

            public Impl(RepositoryMarca repositoryMarca) {
                this.repositoryMarca = repositoryMarca;
            }


            @Override
            public Either<Falla, Resultados.Id> crearMarca(String nombre) {
                return nuevaInstanciaEntidad(contexto("Creando nueva marca con nombre: %s".formatted(nombre), repositoryMarca),
                        detectarDuplicado(repositoryMarca::findByNombre,nombre)
                        ,crearInstancia(() -> Marca.builder()
                                .nombre(nombre)
                                .build()));
            }
            @Override
            public List<Marca> listarMarcas() {
                return repositoryMarca.findAll();
            }
        }

    }
}
