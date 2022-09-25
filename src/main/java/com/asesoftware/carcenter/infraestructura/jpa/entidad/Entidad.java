package com.asesoftware.carcenter.infraestructura.jpa.entidad;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(EscuchaEntidad.class)
public abstract class Entidad extends Validable{

    @Id
    @Getter
    @Column(name = "codigo", nullable = false, length = 32, updatable = false)
    String id;



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entidad entidad)) {
            return false;
        }
        if (!(getClass().isAssignableFrom(o.getClass()) ||
                o.getClass().isAssignableFrom(getClass()))) {
            return false;
        }
        return id.equals(entidad.id);
    }
}
