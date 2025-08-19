package cl.kibernum.apirest.security.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cl.kibernum.apirest.security.domain.UserAccount;
import cl.kibernum.apirest.exception.ResourceNotFoundException;
import cl.kibernum.apirest.security.repository.*;
import cl.kibernum.apirest.security.dto.UserAccountDto;

@Service
public class UserAccountServiceImpl implements IUserAccountService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public List<UserAccount> getAll() {
        return userAccountRepository.findAll();
    }

    @Override
    public Optional<UserAccount> getById(Long id) {
        return userAccountRepository.findById(id);
    }

    @Override
    public UserAccount create(UserAccountDto dDto) {
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public UserAccount update(Long id, UserAccountDto dDto) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void softDelete(Long id) {
       UserAccount searchingUserAccount = userAccountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("UserAcount no encontrado"));

        searchingUserAccount.desactivateUser();
        userAccountRepository.save(searchingUserAccount);
    }

    @Override
    public Optional<UserAccount> findByEmailAndActiveTrue(String email) {
        return userAccountRepository.findByEmailAndActiveTrue(email);
    }    
}
