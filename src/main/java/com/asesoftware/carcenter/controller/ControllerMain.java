package com.asesoftware.carcenter.controller;

import com.asesoftware.carcenter.dominio.*;
import com.asesoftware.carcenter.dominio.Clientes.ServicioClientes;
import com.asesoftware.carcenter.dominio.MantenimientoRepuestos.ServicioMantenimientoRepuestos;
import com.asesoftware.carcenter.dominio.MantenimientoServicios.ServicioMantenimientoServicio;
import com.asesoftware.carcenter.dominio.Mantenimientos.ServicioMantenimiento;
import com.asesoftware.carcenter.dominio.Marca.ServicioMarcas;
import com.asesoftware.carcenter.dominio.Mecanicos.Mecanico;
import com.asesoftware.carcenter.dominio.Mecanicos.ServicioMecanincos;
import com.asesoftware.carcenter.dominio.Repuestos.ServicioRepuestos;
import com.asesoftware.carcenter.dominio.Servicios.ServicioServicio;
import com.asesoftware.carcenter.dominio.Vehiculos.ServicioVehiculos;
import com.asesoftware.carcenter.dominio.Vehiculos.Vehiculo;
import com.asesoftware.carcenter.infraestructura.ServicioDSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins ="*", maxAge = 3600)
@RestController
@RequestMapping("/carcenter")
public class ControllerMain {

    private ServicioRepuestos servicioRepuestos;
    private ServicioMantenimientoRepuestos servicioMantenimientoRepuestos;
    private ServicioServicio servicioServicio;
    private ServicioMantenimientoServicio servicioMantenimientoServicio;
    private ServicioMantenimiento servicioMantenimiento;
    private ServicioClientes servicioClientes;
    private ServicioMarcas servicioMarcas;
    private ServicioMecanincos servicioMecanincos;
    private ServicioVehiculos servicioVehiculos;

    @Autowired
    public ControllerMain(ServicioRepuestos servicioRepuestos, ServicioMantenimientoRepuestos servicioMantenimientoRepuestos, ServicioServicio servicioServicio, ServicioMantenimientoServicio servicioMantenimientoServicio, ServicioMantenimiento servicioMantenimiento, ServicioClientes servicioClientes, ServicioMarcas servicioMarcas, ServicioMecanincos servicioMecanincos, ServicioVehiculos servicioVehiculos) {
        this.servicioRepuestos = servicioRepuestos;
        this.servicioMantenimientoRepuestos = servicioMantenimientoRepuestos;
        this.servicioServicio = servicioServicio;
        this.servicioMantenimientoServicio = servicioMantenimientoServicio;
        this.servicioMantenimiento = servicioMantenimiento;
        this.servicioClientes = servicioClientes;
        this.servicioMarcas = servicioMarcas;
        this.servicioMecanincos = servicioMecanincos;
        this.servicioVehiculos = servicioVehiculos;
    }

    @GetMapping("/marcas")
    public ResponseEntity<List<Marca>> listarMarcas(){
        var marcas = servicioMarcas.listarMarcas();
        return new ResponseEntity(marcas,HttpStatus.OK);
    }
    @GetMapping("/clientes")
    public ResponseEntity<List<Clientes>> listarClientes(){
        var clientes = servicioClientes.listarClientes();
        return new ResponseEntity(clientes,HttpStatus.OK);
    }
    @GetMapping("/vehiculos")
    public ResponseEntity<List<Vehiculos>> listarVehiculos(){
        var marcas = servicioVehiculos.listarVehiculos();
        return new ResponseEntity(marcas,HttpStatus.OK);
    }
    @GetMapping("/mantenimientos")
    public ResponseEntity<List<Mantenimientos>> listarMantenimientos(){
        var marcas = servicioMantenimiento.listarMantenimiento();
        return new ResponseEntity(marcas,HttpStatus.OK);
    }
    @GetMapping("/repuestos")
    public ResponseEntity<List<Repuestos>> listarRepuestos(){
        var marcas = servicioRepuestos.listarRepuestos();
        return new ResponseEntity(marcas,HttpStatus.OK);
    }
    @GetMapping("/servicios")
    public ResponseEntity<List<Servicios>> listarServicios(){
        var marcas = servicioServicio.listarServicios();
        return new ResponseEntity(marcas,HttpStatus.OK);
    }
    @GetMapping("/mantenimiento-servicios")
    public ResponseEntity<List<MantenimientoServicios>> listarMantenimientoServicio(){
        var marcas = servicioMantenimientoServicio.listarMantenimientoServicios();
        return new ResponseEntity(marcas,HttpStatus.OK);
    }
    @GetMapping("/mantenimiento-repuestos")
    public ResponseEntity<List<MantenimientoRepuestos>> listarMantenimientoRepuestos(){
        var marcas = servicioMantenimientoRepuestos.listarMantenimientoRepuestos();
        return new ResponseEntity(marcas,HttpStatus.OK);
    }

    @GetMapping("/mecanicos")
    public ResponseEntity<List<Mecanicos>> listarMecanicos(){
        var marcas = servicioMecanincos.listarMecanicos();
        return new ResponseEntity(marcas,HttpStatus.OK);
    }

    @PostMapping("/mecanicos")
    public ResponseEntity<?> crearMecanico (@RequestBody Mecanico mecanico){
        var created = servicioMecanincos.crearMecanico(mecanico.tipoDocumento().getCodigo(),mecanico.documento(),mecanico.primerNombre(),mecanico.segundoNombre(), mecanico.primerApellido(), mecanico.segundoApellido(), mecanico.celular(), mecanico.direccion(), mecanico.email());
        if (created.isRight()) {
            return new ResponseEntity(created.get(), HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity(created, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/marcas")
    public ResponseEntity<?> crearMarca (@RequestBody String marca){
        var created = servicioMarcas.crearMarca(marca);
        if (created.isRight()) {
            return new ResponseEntity(created.get(), HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity(created, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/clientes")
    public ResponseEntity<?> crearClientes (@RequestBody Mecanico cliente){
        var created = servicioClientes.crearCliente(cliente.tipoDocumento(), cliente.documento(), cliente.primerNombre(), cliente.segundoNombre(), cliente.primerApellido(), cliente.segundoApellido(), cliente.celular(), cliente.direccion(), cliente.email());
        if (created.isRight()) {
            return new ResponseEntity(created.get(), HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity(created, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/vehiculos")
    public ResponseEntity<?> crearVehiculos (@RequestBody Vehiculos vehiculo){
        var created = servicioVehiculos.crearVehiculo(vehiculo.getPlaca(),vehiculo.getColor(),vehiculo.getMarca(),vehiculo.getCliente());
        if (created.isRight()) {
            return new ResponseEntity(created.get(), HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity(created, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/servicios")
    public ResponseEntity<?> crearClientes (@RequestBody Servicios servicio){
        var created = servicioServicio.crearServicio(servicio.getNombre(),servicio.getPrecio());
        if (created.isRight()) {
            return new ResponseEntity(created.get(), HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity(created, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/repuestos")
    public ResponseEntity<?> crearClientes (@RequestBody Repuestos servicio){
        var created = servicioRepuestos.crearRepuestos(servicio.getNombre(),servicio.getPrecioUnitario(),servicio.getUnidadesInventario(),servicio.getProveedor());
        if (created.isRight()) {
            return new ResponseEntity(created.get(), HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity(created, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
