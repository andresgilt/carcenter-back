package com.asesoftware.carcenter.infraestructura.jpa.entidad;

import com.asesoftware.carcenter.infraestructura.validacion.Validador;
import com.asesoftware.carcenter.infraestructura.validacion.ValorInvalido;

import java.util.List;
import java.util.function.Consumer;

public abstract class Validable {

    protected void validarAtributos(){
        Validador.validarAtributos(this);
    }

    protected void validarInstancia(Consumer<List<ValorInvalido>> validacion){
        validarAtributos();
        Validador.validarAtributos(this,validacion);
    }
}
