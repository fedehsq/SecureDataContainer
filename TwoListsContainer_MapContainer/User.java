// OVERVIEW: (tipico elemento contenuto in SecureDataContainer)
// nuovo utente con relativo username e password

// AF: <username, password>
// INV: username!=null && password!=null

public class User {
    private String username, password;

    //REQUIRES: username!=null, password!=null
    //EFFECTS: crea un nuovo utente con 'username' e 'password'
    //THROWS: NullPointerException se username==null or password==null
    public User(String username, String password) {
        if(username==null || password==null) {
            throw new NullPointerException();
        }
        this.username=username;
        this.password=password;
    }

    //EFFECTS: restituisce 'username' dell'utente
    public String getUsername() {
        return username;
    }

    //EFFECTS: restituisce 'password' dell'utente
    public String getPassword() {
        return password;
    }

    //EFFECTS: restituisce 'objects' dell'utente
    public User getUser() {
        return this;
    }

    @Override
    //EFFECTS: trasformo in stringa User
    public String toString() {
        return "<" + username + ", " + password + ">";
    }

    //REQUIRES: other!=null
    //EFFECTS: verifica se this == other e return l'esito
    @Override
    public boolean equals(Object other) {
        if(other == null || other.getClass() != getClass()) {
           return false;
        }
        User u = (User) other;
        return this.getUsername().equals(u.getUsername()) &&
                this.getPassword().equals(u.getPassword());
    }
    // necessario per funzioni su tabella hash
    @Override
    public int hashCode() {
        return 1;
    }
}
