package bank.pkobp.entity;

public record UserCredentials(
        String login,
        String password) {

    public UserCredentials withLogin(String newLogin) {
        return new UserCredentials(newLogin, this.password());
    }

    public UserCredentials withPassword(String newPassword) {
        return new UserCredentials(this.login(), newPassword);
    }
}