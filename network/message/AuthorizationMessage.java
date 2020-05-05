package network.message;

import network.User;

public class AuthorizationMessage extends Message {
    public final User user;
    public final boolean registration;
    public boolean changingAccount;
    public User lastAccount;

    public AuthorizationMessage(String login, String password, boolean registration) {
        super("authorization");
        user = new User(login, password);
        this.registration = registration;
    }

    public AuthorizationMessage withLastAccount(User lastAccount) {
        this.lastAccount = lastAccount;
        changingAccount = true;
        return this;
    }
}
