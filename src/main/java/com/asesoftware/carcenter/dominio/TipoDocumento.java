package com.asesoftware.carcenter.dominio;

import com.asesoftware.carcenter.infraestructura.jpa.ConvertidorEnumeracion;

public enum TipoDocumento {

    CEDULA_CIUDADANIA("CC"),
    CEDULA_EXTRANJERIA("CE");

    private final String codigo;

    TipoDocumento(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    static class Convertidor extends ConvertidorEnumeracion<TipoDocumento> {

        public Convertidor() {
            super(values(), TipoDocumento::getCodigo);
        }
    }
}
