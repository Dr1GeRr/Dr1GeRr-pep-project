package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private final AccountDAO accountDAO = new AccountDAO();

    /**
     * Registers a new account.
     * @param account The account to register.
     * @return The created account, or null if registration fails.
     */
    public Account registerAccount(Account account) {
        // Validate username
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            return null; // Username cannot be blank
        }

        // Validate password
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null; // Password must be at least 4 characters
        }

        // Check for duplicate username
        if (accountDAO.findByUsername(account.getUsername()) != null) {
            return null; // Username already exists
        }

        // Save the account to the database
        return accountDAO.save(account);
    }

    /**
     * Authenticates an account login.
     * @param account The account to authenticate.
     * @return The authenticated account, or null if authentication fails.
     */
    public Account loginAccount(Account account) {
        // Validate inputs
        if (account.getUsername() == null || account.getPassword() == null) {
            return null; // Invalid credentials
        }

        // Find the account in the database
        Account existingAccount = accountDAO.findByUsername(account.getUsername());
        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            return existingAccount; // Valid credentials
        }

        // Return null for invalid credentials
        return null;
    }
}
