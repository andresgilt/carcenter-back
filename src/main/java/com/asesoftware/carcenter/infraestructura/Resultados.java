package com.asesoftware.carcenter.infraestructura;

import com.asesoftware.carcenter.infraestructura.jpa.entidad.ErrorValidacion;
import com.asesoftware.carcenter.infraestructura.validacion.ValorInvalido;

import java.util.List;

public interface Resultados {

    interface Identificado {
        String id();
    }

    record Id(String id) implements Identificado {
    }

    interface Falla {
        String mensaje();
    }

    record FallaGeneral(String mensaje) implements Falla {
    }
    record FallaValidacion(String contexto, List<ValorInvalido> erroresValidacion, String mensaje) implements Falla {
        public FallaValidacion(String contexto, ErrorValidacion errorValidacion) {
            this(contexto,
                    errorValidacion.valoresInvalidos(),
                    "%s: %s".formatted(contexto, errorValidacion.getMessage()));
        }
    }

    record CondicionError(String contexto, Throwable error) implements Falla {

        @Override
        public String mensaje() {
            return "Error %s: %s".formatted(
                    contexto(),
                    error().getMessage() == null ? error().toString() : error().getMessage()
            );
        }
    }
}