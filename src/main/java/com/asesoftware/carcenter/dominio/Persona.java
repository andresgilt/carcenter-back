package com.asesoftware.carcenter.dominio;


import com.asesoftware.carcenter.infraestructura.jpa.entidad.Entidad;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@MappedSuperclass

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Persona extends Entidad {


    @NotNull
    @Column(name = "tipo_documento", nullable = false, length = 2)
    @Convert(converter = TipoDocumento.Convertidor.class)

    private TipoDocumento tipoDocumento;

    @NotNull
    @Basic(optional = false)
    @Column(name = "documento", nullable = false)
    private Integer documento;

    @NotNull
    @NotBlank(message = "Primer nombre es requerido")
    @Basic(optional = false)
    @Column(name = "primer_nombre", nullable = false, length = 30)
    private String primerNombre;

    @Basic(optional = true)
    @Column(name = "segundo_nombre", nullable = true, length = 30)
    private String segundoNombre;

    @NotNull
    @NotBlank(message = "Primer apellido es requerido")
    @Basic(optional = false)
    @Column(name = "primer_apellido", nullable = false, length = 30)
    private String primerApellido;

    @Basic(optional = true)
    @Column(name = "segundo_apellido", nullable = true, length = 30)
    private String segundoApellido;

    @NotNull
    @NotBlank(message = "Celular es requerido")
    @Basic(optional = false)
    @Column(name = "celular", nullable = false, length = 10)
    private String celular;

    @NotNull
    @NotBlank(message = "Direccion es requerido")
    @Basic(optional = false)
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @NotNull
    @Basic(optional = false)
    @Pattern(regexp = ".+[@].+[\\.].+", message = "Por favor escriba una dirección de correo válida")
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    public Persona(TipoDocumento tipoDocumento, Integer documento, String primerNombre, String segundoNombre, String primerApellido, String segundoApellido, String celular, String direccion, String email) {
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.celular = celular;
        this.direccion = direccion;
        this.email = email;
    }
}
