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
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "vehiculos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vehiculos extends Entidad {

    @NotNull
    @NotBlank(message = "Placa es requerido")
    @Pattern(regexp = "\\w{3}\\d{3}$",
            message = "Placa inválida ; debe constar de 3 letras y 3 dígitos XXX000")
    @Column(name = "placa", nullable = false, length = 6)
    @Basic(optional = false)
    private String placa;

    @NotNull
    @NotBlank(message = "Color es requerido")
    @Column(name = "color", nullable = false, length = 30)
    @Basic(optional = false)
    private String color;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_marca",
            foreignKey = @ForeignKey(name = "vehiculos_marcas_fk"))
    private Marca marca;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_cliente")
    private Clientes cliente;

    @Builder
    public Vehiculos(String placa, String color, Marca marca, Clientes cliente) {
        this.placa = placa;
        this.color = color;
        this.marca = marca;
        this.cliente = cliente;
        validarAtributos();
    }

    @Repository
    public interface RepositoryVehiculos extends Repositorio<Vehiculos> {
        Optional<Vehiculos> findByPlaca(String placa);
    }


    public interface ServicioVehiculos {

        Either<Falla, Resultados.Id> crearVehiculo(String placa, String color, Marca marca, Clientes cliente);

        List<Vehiculos> listarVehiculos();

        @Service
        class Impl extends ServicioDSL implements ServicioVehiculos {

            private final RepositoryVehiculos repositoryVehiculos;

            public Impl(RepositoryVehiculos repositoryVehiculos) {
                this.repositoryVehiculos = repositoryVehiculos;
            }


            @Override
            public Either<Falla, Resultados.Id> crearVehiculo(String placa, String color, Marca marca, Clientes cliente) {
                return nuevaInstanciaEntidad(contexto("Creando nuevo Vehiculo con placa: %s".formatted(placa), repositoryVehiculos),
                        detectarDuplicado(repositoryVehiculos::findByPlaca,placa)
                        ,crearInstancia(() -> Vehiculos.builder()
                                .placa(placa)
                                .color(color)
                                .marca(marca)
                                .cliente(cliente)
                                .build()));
            }
            @Override
            public List<Vehiculos> listarVehiculos() {
                return repositoryVehiculos.findAll();
            }
        }

    }

    public record Vehiculo(
            String placa, String color, String marca, String cliente
    ) {
    }
}
