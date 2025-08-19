package cl.kibernum.apirest.security.services;

import java.util.List;
import java.util.Optional;

import cl.kibernum.apirest.security.domain.UserAccount;
import cl.kibernum.apirest.security.dto.UserAccountDto;

public interface IUserAccountService {
    List<UserAccount> getAll();

    Optional<UserAccount> getById(Long id);

    UserAccount create(UserAccountDto dDto);

    UserAccount update(Long id, UserAccountDto dDto);

    void softDelete(Long id);

    Optional<UserAccount> findByEmailAndActiveTrue(String email);
}
