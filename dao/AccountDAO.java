
package dao;

import model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDAO {
    Optional<Account> findByAccountNumber(String accountNumber);
    void save(Account account);
    boolean existsByAccountNumber(String accountNumber);
    List<Account> findAll();
}
