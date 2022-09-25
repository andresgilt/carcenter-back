package com.asesoftware.carcenter.infraestructura.jpa.entidad;

import com.asesoftware.carcenter.infraestructura.utils.Identificadores;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class EscuchaEntidad {

    @PrePersist
    public void antesDeGuardar(Entidad entidad){
        prePersist(entidad);
    };
    @PreUpdate
    public void despuesDeActualizar(Entidad entidad){
        preUpdate(entidad);
    };

    public static void prePersist(Entidad entidad){
        if (entidad.id == null){
            entidad.id = Identificadores.siguienteIdentificador();
            //entidad.fechaCreacion = LocalDateTime.now();
        }
    }

    public static void preUpdate(Entidad entidad){

        //entidad.fechaActualizacion = LocalDateTime.now();
    }

}
