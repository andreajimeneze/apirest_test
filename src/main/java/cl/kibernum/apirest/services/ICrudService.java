package cl.kibernum.apirest.services;

import java.util.List;
import java.util.Optional;

public interface ICrudService<T, D> {
    List<T> getAll();

    Optional<T> getById(Long id);

    T create(D dDto);

    T update(Long id, D dDto);

    void softDelete(Long id);
}