package com.asesoftware.carcenter.dominio;

import com.asesoftware.carcenter.infraestructura.jpa.ConvertidorEnumeracion;

public enum Estado {
    ACTIVO("A"),
    INACTIVO("I"),
    OCUPADO("O");
    private final String codigo;

    Estado(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    static class Convertidor extends ConvertidorEnumeracion<Estado> {

        public Convertidor() {
            super(values(), Estado::getCodigo);
        }
    }
}
