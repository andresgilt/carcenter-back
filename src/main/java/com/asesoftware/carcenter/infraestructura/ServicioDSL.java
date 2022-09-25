package com.asesoftware.carcenter.infraestructura;

import com.asesoftware.carcenter.infraestructura.Resultados.*;
import com.asesoftware.carcenter.infraestructura.jpa.Repositorio;
import com.asesoftware.carcenter.infraestructura.jpa.entidad.Entidad;
import com.asesoftware.carcenter.infraestructura.jpa.entidad.ErrorValidacion;
import io.vavr.control.Either;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ServicioDSL {

    protected record Contexto<E extends Entidad>(String nombre, Repositorio<E> repositorio) {
        @Override
        public String toString() {
            return nombre;
        }
    }

    protected <E extends Entidad> Contexto<E> contexto(String nombre, Repositorio<E> repositorio) {
        return new Contexto<>(nombre, repositorio);
    }

    protected <E extends Entidad> Supplier<E> crearInstancia(Supplier<E> crear) {
        return crear;
    }

    protected <E extends Entidad> Either<Resultados.Falla, Resultados.Id> nuevaInstanciaEntidad(
            Contexto<E> contexto,
            Supplier<E> creadorInstancia
    ) {
        return nuevaInstanciaEntidad(contexto, creadorInstancia, contexto.repositorio::guardarYGrabar);
    }
    protected <E extends Entidad> Either<Falla, Id> nuevaInstanciaEntidad(
            Contexto<E> contexto,
            Supplier<E> creadorInstancia,
            Function<E, E> persistir
    ) {
        try {
            final E entidad;
            try {
                entidad = creadorInstancia.get();
            } catch (ErrorValidacion error) {
                return fallaValidacion(contexto.toString(), error);
            }
            final var entidadOk = persistir.apply(entidad);
            return Either.right(new Resultados.Id(entidadOk.getId()));
        } catch (Exception e) {
            return condicionError("%s: Error en la creación".formatted(contexto), e);
        }
    }

    protected <E extends Entidad, T> Function<String, Either<Falla, Void>> detectarDuplicado(
            Function<T, Optional<E>> leer, T t
    ) {
        return (contexto) -> leer.apply(t)
                .map(e -> this.<Void>fallaGeneral("%s. Clave duplicada: %s".formatted(contexto, t)))
                .orElseGet(() -> Either.right(null));
    }

    protected <E extends Entidad, T1, T2> Function<String, Either<Falla, Void>> detectarDuplicado(
            BiFunction<T1, T2, Optional<E>> leer, T1 t1, T2 t2
    ) {
        return (contexto) -> leer.apply(t1, t2)
                .map(e -> this.<Void>fallaGeneral("%s. Clave duplicada: %s/%s".formatted(contexto, t1, t2)))
                .orElseGet(() -> Either.right(null));
    }

    protected <E extends Entidad> Either<Falla, Id> nuevaInstanciaEntidad(
            Contexto<E> contexto,
            Function<String, Either<Falla, Void>> detectarDuplicado,
            Supplier<E> creadorInstancia
    ) {
        return nuevaInstanciaEntidad(contexto, detectarDuplicado, creadorInstancia,
                contexto.repositorio::guardarYGrabar);
    }

    protected <E extends Entidad> Either<Falla, Id> nuevaInstanciaEntidad(
            Contexto<E> contexto,
            Function<String, Either<Falla, Void>> detectarDuplicado,
            Supplier<E> creadorInstancia,
            Function<E, E> persistir
    ) {
        return detectarDuplicado.apply(contexto.toString())
                .flatMap(ignored -> nuevaInstanciaEntidad(contexto, creadorInstancia, persistir));
    }

    protected <T> Either<Falla, T> fallaValidacion(String contexto, ErrorValidacion error) {
        final var mensaje = "%s. Falla de validación: %s".formatted(contexto, error.getMessage());
        return Either.left(new FallaValidacion(mensaje, error));
    }

    protected <T> Either<Falla, T> condicionError(String contexto, Exception e) {
        final var mensaje = "%s: %s".formatted(contexto, e.getMessage());
        return Either.left(new CondicionError(mensaje, e));
    }

    protected <T> Either<Falla, T> fallaGeneral(String mensajeError) {
        return Either.left(new FallaGeneral(mensajeError));
    }
}
