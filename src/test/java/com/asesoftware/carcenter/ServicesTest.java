package com.asesoftware.carcenter;

import com.asesoftware.carcenter.dominio.TipoDocumento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.asesoftware.carcenter.dominio.Clientes.ServicioClientes;
import javax.transaction.Transactional;
import static org.junit.jupiter.api.Assertions.assertTrue;
@Transactional
@SpringBootTest
public class ServicesTest {

    private final ServicioClientes servicioClientes;

    @Autowired
    public ServicesTest(ServicioClientes servicioClientes) {
        this.servicioClientes = servicioClientes;
    }

    @Test
    public void creaCliente(){
        var cliente = servicioClientes.crearCliente(TipoDocumento.CEDULA_EXTRANJERIA,
                1005587987,
                "Juan",
                "Andres",
                "Gonzales",
                "Perez",
                "3254567897",
                "Cra 32 # 15 -21",
                "correo@correo.com");
        assertTrue(cliente.isRight());
        var cliente2 = servicioClientes.crearCliente(TipoDocumento.CEDULA_EXTRANJERIA,
                1005587987,
                "Juan",
                "Andres",
                "Gonzales",
                "Perez",
                "3254567897",
                "Cra 32 # 15 -21",
                "correo@correo.com");
        assertTrue(cliente2.isLeft());
    }
}
