package com.asesoftware.carcenter;

import com.asesoftware.carcenter.dominio.Clientes;
import com.asesoftware.carcenter.dominio.Marca;
import com.asesoftware.carcenter.dominio.TipoDocumento;
import com.asesoftware.carcenter.dominio.Vehiculos;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import javax.transaction.Transactional;

@Transactional
@SpringBootTest
public class EntidadesTest {

    @Test
    public void creaCliente(){
        final var cliente = Clientes.builder()
                .tipoDocumento(TipoDocumento.CEDULA_EXTRANJERIA)
                .documento(1005587987)
                .primerNombre("Juan")
                .primerApellido("Gonzales")
                .segundoApellido("Perez")
                .celular("3254567897")
                .direccion("Cra 32 # 15 -21")
                .email("correo@correo.com")
                .build();
        assertNotNull(cliente);
    }

    @Test
    public void fallaClientePorDocumento(){
        assertThrows(IllegalArgumentException.class,() -> Clientes.builder()
                .primerNombre("Juan")
                .segundoNombre("Andres")
                .primerApellido("Gonzales")
                .segundoApellido("Perez")
                .celular("3254567897")
                .direccion("Cra 32 # 15 -21")
                .email("correo@correo.com")
                .build());
    }

    @Test
    public void fallaClientePorCorreo(){
        assertThrows(IllegalArgumentException.class,() -> Clientes.builder()
                .tipoDocumento(TipoDocumento.CEDULA_EXTRANJERIA)
                .documento(1005587987)
                .primerNombre("Juan")
                .segundoNombre("Andres")
                .primerApellido("Gonzales")
                .segundoApellido("Perez")
                .celular("3254567897")
                .direccion("Cra 32 # 15 -21")
                .email("correo@correo")
                .build());
    }

    @Test
    public void creaVehiculo(){
        final var vehiculo = Vehiculos.builder().placa("AAA123")
                .color("azul")
                .marca(new Marca("A"))
                .cliente(new Clientes(TipoDocumento.CEDULA_EXTRANJERIA,10,"a","","b","","1","1","a@a.com"))
                .build();
        assertEquals("azul",vehiculo.getColor());
    }

}
