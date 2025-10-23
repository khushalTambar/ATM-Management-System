
package dao;

import model.Account;

import java.util.*;

public class InMemoryAccountDAO implements AccountDAO {
    private final Map<String, Account> store = new HashMap<>();

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return Optional.ofNullable(store.get(accountNumber));
    }

    @Override
    public void save(Account account) {
        store.put(account.getAccountNumber(), account);
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return store.containsKey(accountNumber);
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(store.values());
    }
}
