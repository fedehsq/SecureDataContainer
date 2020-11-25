// OVERVIEW: (tipico elemento contenuto in SecureDataContainer)
// nuovo utente con relativo username e password

// AF: <username, password>
// INV: username!=null && password!=null

public class User {
    private String username, password;

    //REQUIRES: username!=null, password!=null
    //EFFECTS: crea un nuovo utente con 'username' e 'password'
    //THROWS: NullPointerException se username == null or password == null
    public User(String username, String password){
        if(username==null || password==null)
            throw new NullPointerException();
        this.username=username;
        this.password=password;
    }

    //EFFECTS: restituisce 'username' di this
    public String getUsername(){
        return username;
    }

    //EFFECTS: restituisce 'password' di this
    public String getPassword(){
        return password;
    }

    //EFFECTS: restituisce this
    public User getUser(){
        return this;
    }

    @Override
    //EFFECTS: trasforma in stringa this
    public String toString(){
        return "<" + username + ", " + password + ">";
    }

    //EFFECTS: verifica se this == other e return l'esito
    @Override
   public boolean equals(Object other){
        if(other == null || other.getClass() != getClass())
            return false;
        User u = (User) other;
        return this.getUsername().equals(u.getUsername()) &&
                this.getPassword().equals(u.getPassword());
    }


}
