package com.asesoftware.carcenter.dominio;

import com.asesoftware.carcenter.infraestructura.Resultados;
import com.asesoftware.carcenter.infraestructura.Resultados.Falla;
import com.asesoftware.carcenter.infraestructura.Resultados.FallaGeneral;
import com.asesoftware.carcenter.infraestructura.ServicioDSL;
import com.asesoftware.carcenter.infraestructura.jpa.Repositorio;
import io.vavr.control.Either;
import lombok.*;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Table(name = "mecanicos", uniqueConstraints = {
        @UniqueConstraint(name = "mecanicos_uk", columnNames = {"tipo_documento", "documento"})})
//@NamedStoredProcedureQuery(name = "Mecanico.crear", procedureName = "crear_mecanico", parameters = {
//        @StoredProcedureParameter(mode= ParameterMode.IN, name = "tipo_documento", type = TipoDocumento.class),
//        @StoredProcedureParameter(mode= ParameterMode.IN, name = "documento", type = Integer.class),
//        @StoredProcedureParameter(mode= ParameterMode.IN, name = "primer_nombre", type = String.class),
//        @StoredProcedureParameter(mode= ParameterMode.IN, name = "segundo_nombre", type = String.class),
//        @StoredProcedureParameter(mode= ParameterMode.IN, name = "primer_apellido", type = String.class),
//        @StoredProcedureParameter(mode= ParameterMode.IN, name = "segundo_apellido", type = String.class),
//        @StoredProcedureParameter(mode= ParameterMode.IN, name = "celular", type = String.class),
//        @StoredProcedureParameter(mode= ParameterMode.IN, name = "direccion", type = String.class),
//        @StoredProcedureParameter(mode= ParameterMode.IN, name = "email", type = String.class),
//        @StoredProcedureParameter(mode= ParameterMode.OUT, name = "resultado", type = String.class),
//} )
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mecanicos extends Persona {

    @ToString.Include
    @NotNull
    @Basic(optional = false)
    @Column(name = "estado", nullable = false, length = 1)
    @Convert(converter = Estado.Convertidor.class)
    private Estado estado;

    @Builder
    public Mecanicos(TipoDocumento tipoDocumento, Integer documento, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String celular, String direccion, String email, Estado estado) {
        super(tipoDocumento, documento, primerNombre, segundoNombre, primerApellido, segundoApellido, celular, direccion, email);
        this.estado = estado;
        validarAtributos();
    }

    @Repository
    public interface RepositoryMecanicos extends Repositorio<Mecanicos> {

        Optional<Mecanicos> findByDocumento(Integer documento);

        @Transactional
        @Procedure(name = "crear", procedureName = "crear_mecanico", outputParameterName = "resultado")
        String crearMecanico(@Param("tipo_documento") String tipoDocumento,
                             @Param("documento") Integer documento,
                             @Param("primer_nombre") String primerNombre,
                             @Param("segundo_nombre") String segundoNombre,
                             @Param("primer_apellido") String primerApellido,
                             @Param("segundo_apellido") String segundoApellido,
                             @Param("celular") String celular,
                             @Param("direccion") String direccion,
                             @Param("email") String email
        );
    }


    public interface ServicioMecanincos {

        Either<Falla, Resultados.Id> crearMecanico(String tipoDocumento, Integer documento, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String celular, String direccion, String email);

        List<Mecanicos> listarMecanicos();

        @Service
        class Impl extends ServicioDSL implements ServicioMecanincos {

           private final RepositoryMecanicos repositoryMecanicos;

            public Impl(RepositoryMecanicos repositoryMecanicos) {
                this.repositoryMecanicos = repositoryMecanicos;
            }


            @Override
            public Either<Falla, Resultados.Id> crearMecanico(String tipoDocumento, Integer documento, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String celular, String direccion, String email) {
                var mecanico = repositoryMecanicos.crearMecanico(tipoDocumento, documento,primerNombre,segundoNombre,primerApellido,segundoApellido,celular,direccion,email);
                if (mecanico.contains("Error")){
                    return Either.left(new FallaGeneral(mecanico));
                }else{
                    return Either.right(new Resultados.Id(repositoryMecanicos.findById(mecanico).get().getId()));
                }

            }
            @Override
            public List<Mecanicos> listarMecanicos() {
                return repositoryMecanicos.findAll();
            }
        }

    }

    public record Mecanico(
            TipoDocumento tipoDocumento, Integer documento, String primerNombre, String segundoNombre,
            String primerApellido, String segundoApellido, String celular, String direccion, String email, Estado estado
    ) {
    }
}

