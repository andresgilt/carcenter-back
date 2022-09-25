package com.asesoftware.carcenter.infraestructura.utils;

import java.util.UUID;

public class Identificadores {

    public static String siguienteIdentificador() {
        return UUID.randomUUID().toString().replace("-","");
    }
}
