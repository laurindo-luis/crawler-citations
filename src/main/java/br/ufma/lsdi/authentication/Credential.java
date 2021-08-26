package br.ufma.lsdi.authentication;

public class Credential {
    private String user;
    private String password;

    public Credential(String user, String password) {
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("user: %s\npassword: %s", user, password);
    }
}
