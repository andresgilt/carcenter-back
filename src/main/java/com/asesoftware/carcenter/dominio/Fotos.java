package com.asesoftware.carcenter.dominio;

import com.asesoftware.carcenter.infraestructura.jpa.Repositorio;
import com.asesoftware.carcenter.infraestructura.jpa.entidad.Entidad;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Table(name = "fotos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Fotos extends Entidad {

    @NotNull
    @NotBlank(message = "La ruta es requerida")
    @Column(name = "ruta", nullable = false, length = 6)
    @Basic(optional = false)
    private String ruta;

    @ManyToOne(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_mantenimiento",
            foreignKey = @ForeignKey(name = "fotos_man_fk"))
    private Mantenimientos mantenimientos;

    @Builder
    public Fotos(String ruta, Mantenimientos mantenimientos) {
        this.ruta = ruta;
        this.mantenimientos = mantenimientos;
        validarAtributos();
    }

    @Repository
    public interface RepositoryFotos extends Repositorio<Fotos> {
    }
}
