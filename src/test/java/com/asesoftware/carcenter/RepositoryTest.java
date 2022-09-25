package com.asesoftware.carcenter;

import com.asesoftware.carcenter.dominio.Clientes;
import com.asesoftware.carcenter.dominio.Clientes.RepositoryClientes;
import com.asesoftware.carcenter.dominio.Mecanicos;
import com.asesoftware.carcenter.dominio.Marca;
import com.asesoftware.carcenter.dominio.Mecanicos.RepositoryMecanicos;
import com.asesoftware.carcenter.dominio.TipoDocumento;
import com.asesoftware.carcenter.dominio.Vehiculos;
import com.asesoftware.carcenter.dominio.Vehiculos.RepositoryVehiculos;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTest extends PruebaJpa{

    private final Clientes.RepositoryClientes repositoryClientes;

    private final Vehiculos.RepositoryVehiculos repositoryVehiculos;

    private final Mecanicos.RepositoryMecanicos repositoryMecanicos;

    @Autowired
    public RepositoryTest(RepositoryClientes repositoryClientes, RepositoryVehiculos repositoryVehiculos, RepositoryMecanicos repositoryMecanicos) {
        this.repositoryClientes = repositoryClientes;
        this.repositoryVehiculos = repositoryVehiculos;
        this.repositoryMecanicos = repositoryMecanicos;
    }

    @Test
    public void creaCliente(){
        var cliente = repositoryClientes.saveAndFlush(
                Clientes.builder()
                        .tipoDocumento(TipoDocumento.CEDULA_EXTRANJERIA)
                        .documento(1005587987)
                        .primerNombre("Juan")
                        .primerApellido("Gonzales")
                        .segundoApellido("Perez")
                        .celular("3254567897")
                        .direccion("Cra 32 # 15 -21")
                        .email("correo@correo.com")
                        .build()
        );
        assertNotNull(cliente);
        var clienteCreado = repositoryClientes.findByDocumento(1005587987);
        assertTrue(clienteCreado.isPresent());
        assertEquals("Juan",clienteCreado.get().getPrimerNombre());
    }

    @Test
    public void creaVehiculo(){
        var marca = Marca.builder().nombre("Ford").build();
        var cliente =  Clientes.builder()
                .tipoDocumento(TipoDocumento.CEDULA_EXTRANJERIA)
                .documento(1005587987)
                .primerNombre("Juan")
                .primerApellido("Gonzales")
                .segundoApellido("Perez")
                .celular("3254567897")
                .direccion("Cra 32 # 15 -21")
                .email("correo@correo.com")
                .build();
        var vehiculo = repositoryVehiculos.saveAndFlush(
                Vehiculos.builder().placa("AAA123")
                .color("azul")
                .marca(marca)
                .cliente(cliente)
                .build()
        );
        assertNotNull(vehiculo);
        var vehiculoCreado = repositoryVehiculos.findByPlaca("AAA123");
        assertTrue(vehiculoCreado.isPresent());
        assertEquals("Ford",vehiculoCreado.get().getMarca().getNombre());
    }


    @Test
    @Disabled
    public void creaMecanico(){
        var mecanico = repositoryMecanicos.crearMecanico(
          TipoDocumento.CEDULA_CIUDADANIA.getCodigo(),
          102,
          "1",
          "1",
          "1",
          "1",
          "1",
          "1",
          "1"
        );
        System.out.println(mecanico);
    }


}
