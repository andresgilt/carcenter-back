drop table fotos;
drop table repuestos_x_mantenimientos;
drop table repuestos;
drop table servicios_x_mantenimientos;
drop table servicios;
drop table detalle_facturas;
drop table mantenimientos;
drop table vehiculos;
drop table marcas;
drop table mecanicos;
drop table facturas;
drop table clientes;
drop table parametos;

create table mecanicos
(
    codigo           varchar2(32)  not null,
    tipo_documento   varchar2(2)   not null,
    documento        integer       not null,
    primer_nombre    varchar2(30)  not null,
    segundo_nombre   varchar2(30),
    primer_apellido  varchar2(30)  not null,
    segundo_apellido varchar2(30),
    celular          varchar2(10)  not null,
    direccion        varchar2(200) not null,
    email            varchar2(100) not null,
    estado           char(1)
);

create table clientes
(
    codigo           varchar2(32)  not null,
    tipo_documento   varchar2(2)   not null,
    documento        integer       not null,
    primer_nombre    varchar2(30)  not null,
    segundo_nombre   varchar2(30),
    primer_apellido  varchar2(30)  not null,
    segundo_apellido varchar2(30),
    celular          varchar2(10)  not null,
    direccion        varchar2(200) not null,
    email            varchar2(100) not null
);

create table marcas
(
    codigo       varchar2(32) not null,
    nombre_marca varchar2(30) not null
);

create table vehiculos
(
    codigo      varchar2(32) not null,
    placa       varchar2(6)  not null,
    color       varchar2(30) not null,
    cod_marca   varchar2(32) not null,
    cod_cliente varchar2(32) not null
);

create table fotos
(
    codigo            varchar2(32) not null,
    ruta              varchar2(200),
    cod_mantenimiento varchar2(32) not null
);

create table repuestos
(
    codigo              varchar2(32)  not null,
    nombre_repuesto     varchar2(100) not null,
    precio_unitario     number        not null,
    unidades_inventario integer       not null,
    proveedor           varchar2(300) not null
);

create table repuestos_x_mantenimientos
(
    codigo            varchar2(32) not null,
    unidades          integer      not null,
    tiempo_estimado   integer      not null,
    cod_mantenimiento varchar2(32) not null,
    cod_repuesto      varchar2(32) not null
);

create table servicios
(
    codigo          varchar2(32)  not null,
    nombre_servicio varchar2(100) not null,
    precio          number        not null
);

create table servicios_x_mantenimientos
(
    codigo            varchar2(32) not null,
    tiempo_estimado   integer,
    cod_servicio      varchar2(32) not null,
    cod_mantenimiento varchar2(32) not null
);


create table mantenimientos
(
    codigo       varchar2(32) not null,
    estado       integer,
    cod_vehiculo varchar2(32) not null,
    fecha        date         not null,
    cod_mecanico varchar2(32) not null,
    presupuesto  number
);

create table detalle_facturas
(
    codigo            varchar2(32) not null,
    cod_factura       varchar2(32) not null,
    precio_unidad     number       not null,
    numero_unidades   integer      not null,
    descuento         number       not null,
    precio_mano_obra  number       not null,
    cod_mantenimiento varchar2(32) not null
);

create table facturas
(
    codigo      varchar2(32) not null,
    cod_cliente varchar2(32) not null,
    total       number       not null,
    iva         number       not null

);

create table parametos
(
    codigo varchar2(32) not null,
    nombre varchar2(30) not null,
    valor  number       not null
);


alter table mecanicos
    add primary key (codigo);
alter table mecanicos
    add constraint mecanicos_uk unique (documento, tipo_documento);

alter table clientes
    add primary key (codigo);
alter table clientes
    add constraint clientes_uk unique (documento, tipo_documento);

alter table marcas
    add primary key (codigo);

alter table vehiculos
    add primary key (codigo);
alter table vehiculos
    add constraint vehiculos_uk unique (placa);
alter table vehiculos
    add constraint vehiculos_marcas_fk foreign key (cod_marca) references marcas (codigo);
alter table vehiculos
    add constraint vehiculos_clientes_fk foreign key (cod_cliente)
        references clientes (codigo);

alter table mantenimientos
    add primary key (codigo);
alter table mantenimientos
    add constraint man_vehicular_fk foreign key (cod_vehiculo) references vehiculos (codigo);
alter table mantenimientos
    add constraint mantenimiento_mecanicos_fk foreign key (cod_mecanico)
        references mecanicos (codigo);


alter table fotos
    add primary key (codigo);
alter table fotos
    add constraint fotos_man_fk foreign key (cod_mantenimiento) references mantenimientos (codigo);

alter table servicios
    add primary key (codigo);

alter table servicios_x_mantenimientos
    add primary key (codigo);
alter table servicios_x_mantenimientos
    add constraint ser_x_man_ser_fk
        foreign key (cod_servicio) references servicios (codigo);
alter table servicios_x_mantenimientos
    add constraint ser_x_man_man_fk
        foreign key (cod_mantenimiento) references mantenimientos (codigo);

alter table repuestos
    add primary key (codigo);

alter table repuestos_x_mantenimientos
    add primary key (codigo);
alter table repuestos_x_mantenimientos
    add constraint rep_x_man_rep_fk
        foreign key (cod_repuesto) references repuestos (codigo);
alter table repuestos_x_mantenimientos
    add constraint rep_x_mtos_man_fk
        foreign key (cod_mantenimiento) references mantenimientos (codigo);


alter table facturas
    add primary key (codigo);
alter table facturas
    add constraint factura_cliente_fk foreign key (cod_cliente)
        references clientes (codigo);


alter table detalle_facturas
    add primary key (codigo);
alter table detalle_facturas
    add constraint det_fac_fac_fk foreign key (cod_factura) references facturas (codigo);
alter table detalle_facturas
    add constraint det_factura_man_fk foreign key (cod_mantenimiento)
        references mantenimientos (codigo);



CREATE OR REPLACE NONEDITIONABLE PROCEDURE crear_mecanico(tipo_documento   IN CHAR,
                                                          documento        IN INTEGER,
                                                          primer_nombre    IN VARCHAR2,
                                                          segundo_nombre   IN VARCHAR2,
                                                          primer_apellido  IN VARCHAR2,
                                                          segundo_apellido IN VARCHAR2,
                                                          celular          IN VARCHAR2,
                                                          direccion        IN VARCHAR2,
                                                          email            IN VARCHAR2,
                                                          resultado        OUT VARCHAR2) AS
BEGIN
  BEGIN
    IF NOT (regexp_like(email, '.+[@].+[\.].+')) THEN
      resultado := 'Error: no se cumple con el formato de correo valido';
    ELSE
      resultado := sys_guid();
      BEGIN
        INSERT INTO mecanicos
        VALUES
          (resultado,
           tipo_documento,
           documento,
           primer_nombre,
           segundo_nombre,
           primer_apellido,
           segundo_apellido,
           celular,
           direccion,
           email,
           'A');
      EXCEPTION
        WHEN dup_val_on_index THEN
          resultado := 'Error: Llave primaria duplicada: Tipo Documento: ' ||
                       tipo_documento || ' documento: ' || documento;
        WHEN OTHERS THEN
          resultado := 'Error: Insertando Mecanico: Tipo Documento: ' ||
                       tipo_documento || ' documento: ' || documento;
      END;
    END IF;

  END;
END;

---Porcentaje de descuento
---Monto minimo descuento
---IVA
---VAlor minimo Servicio
---Valor Maximo Servicio
