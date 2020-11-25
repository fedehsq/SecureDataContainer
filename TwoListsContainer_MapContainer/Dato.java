import java.util.ArrayList;

// OVERVIEW: (tipico elemento contenuto in SecureDataContainer)
// è il dato di tipo E memorizzato nel SecureDataContainer col rispettivo proprietario
// ed eventualmente condiviso con altri utenti
// AF: < dato, owner, {user_1, user_i} >
// INV: dato != null && owner != null && sharedWith != null
// && forall i, j con i != j : 0 <= i, j < sharedWith.size =>
// sharedWith.get(i) != null && sharedWith.get(j) != null && sharedWith.get(i) != sharedWith.get(i)!=null

public class Dato <E> {
    private  E dato;
    private  User owner;
    private  ArrayList<User> sharedWith;

    /* crea l'elemento di tipo E col rispettivo proprietario e
       non condiviso inizialmente */
    // REQUIRES: owner, dato != null
    // EFFECTS: this.dato = dato && this.owner = owner && sharedWith = {}
    // THROWS: NullPointerException se owner==null or dato==null
    public Dato(User owner, E dato) {
        if(owner==null || dato==null) {
            throw new NullPointerException();
        }
        this.owner = owner;
        this.dato = dato;
        sharedWith = new ArrayList<>(); //inizialmente il dato non è condiviso con nessuno, accessibile solo al proprietario 'owner'
    }

    /* crea l'elemento di tipo E col rispettivo proprietario e
       condiviso inizialmente con una lista di utenti */
    // REQUIRES: owner, dato, sharedWith != null
    // EFFECTS: this.dato = dato && this.owner = owner && this.sharedWith = sharedWith
    // THROWS: NullPointerException se owner==null or dato==null
    public Dato(User owner, E dato, ArrayList<User> sharedWith) {
        if(owner==null || dato==null || sharedWith == null) {
            throw new NullPointerException();
        }
        this.owner = owner;
        this.dato = dato;
        this.sharedWith = sharedWith; //inizialmente il dato è già condiviso con qualche altro utente
    }

    // EFFECTS: restituisce l'elemento E
    public E getDato() {
        return dato;
    }

    // EFFECTS: restituisce il proprietario dell'elemento E
    public User getOwner() {
        return owner;
    }

    // EFFECTS: restituisce la lista degli utenti che hanno accesso all'elemento E con proprietario 'owner'
    public ArrayList<User> getSharedWith() {
        return sharedWith;
    }

    // EFFECTS: controlla se due dati sono uguali e return l'esito
    @Override
    public boolean equals(Object other){
        if(other == null || other.getClass() != getClass()) {
            return false;
        }
        Dato d = (Dato) other;
        return this.getDato().equals(d.getDato());
    }

    @Override
    // EFFECTS: trasformo in stringa 'Dato'
    public String toString() {
        String s = dato.toString() + " sharedWith: {";
        if(sharedWith.isEmpty()) {
            return s + " nobody }";
        }
        for(int i=0; i<sharedWith.size()-1; i++) {
            s = s + sharedWith.get(i).toString() + ", ";
        }
        return s + sharedWith.get(sharedWith.size()-1).toString() + " }";
    }
}
