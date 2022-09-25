package com.asesoftware.carcenter.dominio;

import com.asesoftware.carcenter.infraestructura.Resultados.Falla;
import com.asesoftware.carcenter.infraestructura.Resultados.Id;
import com.asesoftware.carcenter.infraestructura.ServicioDSL;
import com.asesoftware.carcenter.infraestructura.jpa.Repositorio;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Table(name = "clientes", uniqueConstraints = {
        @UniqueConstraint(name = "clientes_uk", columnNames = {"tipo_documento", "documento"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Clientes extends Persona {

    @Builder
    public Clientes(TipoDocumento tipoDocumento, Integer documento, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String celular, String direccion, String email) {
        super(tipoDocumento, documento, primerNombre, segundoNombre, primerApellido, segundoApellido, celular, direccion, email);
        validarAtributos();
    }

    @Repository
    public interface RepositoryClientes extends Repositorio<Clientes> {
        Optional<Clientes> findByDocumento(Integer documento);

        Optional<Clientes> findByTipoDocumentoAndDocumento(TipoDocumento tipoDocumento, Integer documento);
    }


    public interface ServicioClientes {

        Either<Falla, Id> crearCliente(TipoDocumento tipoDocumento, Integer documento, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String celular, String direccion, String email);

        List<Clientes> listarClientes();

        @Service
        class Impl extends ServicioDSL implements ServicioClientes {

            private final RepositoryClientes repositoryClientes;

            public Impl(RepositoryClientes repositoryClientes) {
                this.repositoryClientes = repositoryClientes;
            }


            @Override
            public Either<Falla, Id> crearCliente(TipoDocumento tipoDocumento, Integer documento, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String celular, String direccion, String email) {
                return nuevaInstanciaEntidad(contexto("Creando nuevo Cliente con documento: %s".formatted(documento.toString()), repositoryClientes),
                        detectarDuplicado(repositoryClientes::findByTipoDocumentoAndDocumento,tipoDocumento,documento)
                        ,crearInstancia(() -> Clientes.builder()
                                .tipoDocumento(tipoDocumento)
                                .documento(documento)
                                .primerNombre(primerNombre)
                                .segundoNombre(segundoNombre)
                                .primerApellido(primerNombre)
                                .segundoApellido(segundoApellido)
                                .celular(celular)
                                .direccion(direccion)
                                .email(email)
                                .build()));
            }
            @Override
            public List<Clientes> listarClientes() {
                return repositoryClientes.findAll();
            }
        }

    }

}
