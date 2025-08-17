package cl.kibernum.apirest.services;

import java.util.List;
import java.util.Optional;

public interface ICrudService<T, D> {
    List<T> getAll();

    Optional<T> getById(int id);

    T create(D dDto);

    T update(int id, D dDto);

    void softDelete(int id);
}