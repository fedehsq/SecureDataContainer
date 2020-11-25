import java.util.ArrayList;

// OVERVIEW: (tipico elemento contenuto in SecureDataContainer)
// utente memorizzato nel SecureDataContainer con la rispettiva lista di datiE,
// indetificato univocamente dall'indice della sua posizizione nel Container
// ogni utente ha la sua lista di dati (eventualmente vuota)

// AF: { < user, { userData.get(i) } > } t.c user = <String username, String password>
// INV: user!=null && userData!=null
// && forall i: 0 <= i < userData.size() => userData.get(i)!=null

public class Dato <E> {
    private  User user;
    private  ArrayList<E> userData;

    // REQUIRES: user != null
    // EFFECTS: crea la coppia <user, userData>
    // THROWS: NullPointerException se user == null or dato==null
    public Dato(User user){
        if(user==null)
            throw new NullPointerException();
        this.user=user;
        userData = new ArrayList<>(); //inizialmente lista vuota
    }

    // EFFECTS: restituisce this.user
    public User getUser(){
        return user;
    }

    // EFFECTS: restituisce this.userData
    public ArrayList <E> getUserData(){
        return userData;
    }

    // EFFECTS: verifica se this.user == other.user e return l'esito
    @Override
    public boolean equals(Object other){
        if(other == null || other.getClass() != getClass())
            return false;
        Dato<?> d = (Dato<?>) other;
        return this.getUser().equals(d.getUser());
    }

    @Override
    public int hashCode() {
        return 17;
    }

    @Override
    // EFFECTS: trasformo in stringa 'Dato'
    public String toString() {
        int i;
        String s = user.getUsername() + " -> [ ";
        for(i=0 ; i<userData.size()-1; i++)
            s = s + userData.get(i).toString() + "; ";
        if(userData.isEmpty())
           return s + " ]";
        return s + userData.get(i).toString() + " ]";
    }
}
