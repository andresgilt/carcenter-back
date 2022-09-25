package com.asesoftware.carcenter.dominio;

import com.asesoftware.carcenter.infraestructura.jpa.entidad.Entidad;

import java.math.BigDecimal;

public class Factura extends Entidad {

    private BigDecimal total;
    private BigDecimal iva;
    private Clientes clientes;

}
