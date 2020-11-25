import java.util.ArrayList;
import java.util.Iterator;

// Typical Element:  <utente_i, { e_0, ..., e_n-1 }>

// AF: { [ <dati.get(i).getUser(), { dati.get(i).getUserData().get(0), ..., dati.get(i).getUserData.get(j)}> ] }
//      forall i, j: 0 <= i < dati.size() && 0 <= j < dati.get(i).getUserData().size()
//      dove:
//      dati.get(i).getUser() = <username, password> forall i: 0 <= i < dati.size()
//      dati.get(i).getUser().getUserData() = { datoE_0, ..., datoE_j }
//      forall i, j: 0 <= i < dati.size() && 0 <= j < dati.get(i).getUserData().size()

// INV: dati != null && forall i, j: 0 <= i, j < dati.size() con i != j =>
// => dati.get(i).getUser() != null
// && dati.get(j).getUser() != null
// && dati.get(i).getUser() != dati.get(j).getUser()
// && dati.get(i).getUserData() != null
// && forall k: 0 <= k < dati.get(i).getUserData.size() =>
// => dati.get(i).getUserData().get(k) != null

public class SingleListContainer<E> implements SecureDataContainer<E> {
    private ArrayList<Dato<E>> dati;

    //EFFECTS: inizializza dati: {}
    public SingleListContainer() {
        dati = new ArrayList<>();
    }

    private void check(String Id, String passw) {
        if (Id == null || passw == null) {
            throw new NullPointerException();
        }
    }

    /* controlla che l'utente con username = 'Owner' e con 'password' = passw sia registrato nel Container */
    // REQUIRES: Id, passw != null && coppia <Id, passw> = u appartenente a 'dati'
    // EFFECTS: return <Id, passw> = u  appartenente a 'dati'
    // THROWS: NullPointerException se Id == null or passw == null (unchecked)
    // UserNotInexception se non esiste <Id, passw> = u in 'dati' (checked)
    private User isIn(String Id, String passw) throws UserNotInException {
        check(Id, passw);
        for (Dato<E> usr : dati) {
            if (usr.getUser().getUsername().equals(Id) &&
                usr.getUser().getPassword().equals(passw)) {
               return usr.getUser();
            }
        }
        throw new UserNotInException(Id + " non registrato o password sbagliata");
    }

    /* ... e abbia 'data' */
    // REQUIRES .. data != null
    // EFFECTS: return la prima occorrenza di 'data' trovata nella lista di 'u'
    // THROW NullPointerException() se data == null (unchecked)
    private E isIn(String Id, String passw, E data) throws DataNotInException, UserNotInException {
        check(Id, passw);
        if (data == null) {
            throw new NullPointerException();
        }
        User u = isIn(Id, passw);
        Dato<E> d = new Dato<E>(u);
        //controlla che 'data' sia nella lista di 'u'
        if (!dati.get(dati.indexOf(d)).getUserData().contains(data)) {
            throw new DataNotInException(data.toString() + " non presente");
        }
        //return la prima occorrenza di 'data' trovata nella lista di 'u'
        return dati.get(dati.indexOf(d)).getUserData().get(dati.get(dati.indexOf(d)).getUserData().indexOf(data));
    }

    /* Crea l’identità di un nuovo utente della collezione e gli associa la listaE vuota */
    // REQUIRES: Id, passw != null && coppia <Id, passw> = u non appartenente a 'dati'
    // MODIFY: dati
    // EFFECTS: crea l'associazione <Id, passw, { }> in 'dati'
    // THROWS: NullPointerException se Id == null or passw == null (unchecked)
    // AlreadyInException se esiste già <Id, passw> in 'dati' (checked)
    @Override
    public void createUser(String Id, String passw) throws AlreadyInException {
        check(Id, passw);
        for (Dato<E> d : dati) {
            if (d.getUser().getUsername().equals(Id)) {
                throw new AlreadyInException("Utente già registrato");
            }
        }
        dati.add(new Dato<E>(new User(Id, passw)));
    }

    /* Restituisce il numero degli elementi di un utente presenti nella collezione */
    // REQUIRES: Owner, passw != null && coppia <Owner, passw> = u appartenente a 'dati'
    // EFFECTS: restituisce la lunghezza di 'list' tc: <u, {list} > presente in 'dati'
    // THROWS: NullPointerException se Owner == null or passw == null (unchecked)
    // UserNotInexception se non esiste <Owner, passw> = u in 'dati' (checked)
    @Override
    public int getSize(String Owner, String passw) throws UserNotInException {
        User u = isIn(Owner, passw);
        return dati.get(dati.indexOf(new Dato<E>(u))).getUserData().size();
    }

    /* Inserisce il valore del dato nella collezione
       se vengono rispettati i controlli di identità */
    // REQUIRES: Owner, passw, data != null && coppia <Owner, passw> = u appartenente a 'dati'
    // MODIFY: dati
    // EFFECTS: aggiunge 'data' a 'list' di <u, {list} > in 'dati'
    // THROWS: NullPointerException se Owner == null or passw == null or data == null (unchecked)
    // UserNotInexception se non esiste <Owner, passw> = u in 'dati' (checked)
    @Override
    public boolean put(String Owner, String passw, E data) throws UserNotInException {
        User u = isIn(Owner, passw);
        if (data == null) {
            return false;
        }
        return dati.get(dati.indexOf(new Dato<E>(u))).getUserData().add(data);
    }

    /* Ottiene una copia del valore del dato nella collezione
       se vengono rispettati i controlli di identità */
    // REQUIRES: Owner, passw, data != null && coppia <Id, passw> = u appartenente a 'dati'
    // && 'data' appartenente a 'list' di <u, {list} >
    // EFFECTS: restituisce la prima occorrenza di 'data'
    // presente in 'list' di <u, {list}> appartenente a 'dati'
    // THROWS: NullPointerException se Owner == null or passw == null or data == null (unchecked)
    // UserNotInexception se non esiste <Owner, passw> = u in 'dati' (checked)
    // DataNotInException se 'data' non presente in 'list' di <u, {list}> (checked)
    @Override
    public E get(String Owner, String passw, E data) throws UserNotInException, DataNotInException {
        return isIn(Owner, passw, data);
    }

    /* Rimuove tutte le occorrenze del dato nella collezione
       se vengono rispettati i controlli di identità */
    // REQUIRES: Owner, passw, data != null && coppia <Owner, passw> = u appartenente a 'dati'
    // && 'data' appartenente a 'list' di <u, {list} >
    // MODIFY: dati
    // EFFECTS: elimina tutte le occorrenze di 'data'
    // presente in 'list' tc: <u, {list} > appartenente a 'dati'
    // THROWS: NullPointerException se Owner == null or passw == null or data == null (unchecked)
    // UserNotInexception se non esiste <Owner, passw> = u in 'dati' (checked)
    // DataNotInException se 'data' non presente in 'list' di <u, {list} > (checked)
    @Override
    public E remove(String Owner, String passw, E data) throws UserNotInException, DataNotInException {
        E e = isIn(Owner, passw, data);
        // lista dell'user '<Owner, passw>'
        ArrayList<E> lt = dati.get(dati.indexOf(new Dato<E>(isIn(Owner, passw)))).getUserData();
        lt.removeIf(elem -> elem.equals(e));
        return e;
    }

    /* Crea una copia del dato nella collezione
       se vengono rispettati i controlli di identità */
    // REQUIRES: Owner, passw, data != null && coppia <Owner, passw> = u appartenente a 'dati'
    // && 'data' appartenente a 'list' di <u, {list} >
    // MODIFY: dati
    // EFFECTS: copia (aggiunge) la prima occorrenza di 'data' presente in 'list',
    // di nuovo in 'list' di <u, {list} > appartenente a 'dati'
    // THROWS: NullPointerException se Owner == null or passw == null or data == null (unchecked)
    // UserNotInexception se non esiste <Owner, passw> = u in 'dati' (checked)
    // DataNotInException se 'data' non presente in 'list' di <u, {list} > (checked)
    @Override
    public void copy(String Owner, String passw, E data) throws UserNotInException, DataNotInException {
        //controlla che 'data' sia nella lista di '<Owner, passw>' e lo copia
        E e = isIn(Owner, passw, data);
        dati.get(dati.indexOf(new Dato<E>(isIn(Owner, passw)))).getUserData().add(e);
    }

    /* Condivide il dato nella collezione con un altro utente
       se vengono rispettati i controlli di identità */
    // REQUIRES: Owner, passw, other, data != null && coppia <Owner, passw> = u appartenente a 'dati'
    // && 'data' appartenente a 'list' di <u, {list} >
    // && <Other, _> = o appartenente a 'dati'
    // MODIFY: dati
    // EFFECTS: copia (aggiunge) tutte le occorrenze di 'data' presente in 'list',
    // in 'list_o' di <o, {list_o} > appartenente a 'dati'
    // THROWS: NullPointerException se Owner == null or passw == null or data == null or Other == null (unchecked)
    // UserNotInexception se non esiste <Owner, passw> = u in 'dati' (checked)
    // DataNotInException se 'data' non presente in 'list' di <u, {list} > (checked)
    @Override
    public void share(String Owner, String passw, String Other, E data) throws UserNotInException, DataNotInException {
        if (Other == null) {
            throw new NullPointerException();
        }
        E e = isIn(Owner, passw, data);
        User other = null;
        // controlla se 'other' è registrato
        for (Dato<E> usr : dati) {
            if (usr.getUser().getUsername().equals(Other)) {
                other = usr.getUser();
                break;
            }
        }
        if (other == null) {
            throw new UserNotInException("Other non registrato");
        }
        // condivide con 'other' tutte le occorrenze di 'data' con proprietario 'Owner'
        int i = dati.indexOf(new Dato<E>(isIn(Owner, passw)));
        int j = dati.indexOf(new Dato<E>(other));
        for (int z = 0; z < dati.get(i).getUserData().size(); z++) {
            E e_ = dati.get(i).getUserData().get(z);
            if (e_.equals(e)) {
                dati.get(j).getUserData().add(e_);
            }
        }
    }

    /* restituisce un iteratore (senza remove)
       che genera tutti i dati dell’utente in ordine arbitrario */
    // REQUIRES: Owner, passw != null && coppia <Owner, passw> = u appartenente a 'dati'
    // EFFECTS: restituisce un iteratore che genera tutti i dati
    // <e_1, .., e_n> presenti in 'list' di <u, {list} > appartenente a 'dati'
    // THROWS: NullPointerException se Owner == null or passw == null (unchecked)
    // UserNotInexception se non esiste <Owner, passw> = u in 'dati' (checked)
    @Override
    public Iterator<E> getIterator(String Owner, String passw) throws UserNotInException {
        User u = isIn(Owner, passw);
        return new datiIterator(u);
    }

    private class datiIterator implements Iterator<E> {
        int index;
        ArrayList<E> it;

        public datiIterator(User u) {
            index = 0;
            // lista di dati di 'u'
            it = dati.get(dati.indexOf(new Dato <E> (u))).getUserData();
        }

        @Override
        public boolean hasNext() {
            return index < it.size();
        }

        @Override
        public E next() {
            return it.get(index++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }

    // EFFECTS: restituisce la dimensione del container
    // (intesa come numero di utenti più lunghezza di ogni lista)
    public int getDim() {
        int d = dati.size();
        for(int i = 0; i < dati.size(); i++) {
            d = d + dati.get(i).getUserData().size();
        }
        return d;
    }

    //EFFECTS: Restituisce la lista 'users' come stringa
    public String usersToString() {
        int i;
        String s = "Utenti : { " + System.lineSeparator();
        if(dati.isEmpty()) {
            return s + "empty" + System.lineSeparator() +  "}";
        }
        for(i = 0; i < dati.size(); i++) {
            s = s + (i+1) + ": " + dati.get(i).getUser().toString() + System.lineSeparator();
        }
        return s + " }";
    }

    //EFFECTS: Restituisce la lista 'dati' come stringa
    public String dataToString() {
        int i;
        String s = "Dati : { " + System.lineSeparator();
        if(dati.isEmpty()) {
            return s + "empty" + System.lineSeparator() + "}";
        }
        for(i = 0; i < dati.size(); i++) {
            s = s + (i+1) + ": " + dati.get(i).toString() + System.lineSeparator();
        }
        return s + " }";
    }

    //EFFECTS: Restituisce la componente 'dato <E>' della lista 'dati' come stringa
    public String Only_E_ToString() {
        int i;
        String s = "E_obj: { " + System.lineSeparator();
        if(dati.isEmpty()) {
            return s + "empty"+ System.lineSeparator() +  "}";
        }
        for(i = 0; i < dati.size(); i++) {
            s = s + (i+1) + ": " + dati.get(i).getUserData().toString() + System.lineSeparator();
        }
        return s + " }";
    }

    // EFFECTS: Restituisce la classe 'MySecureDataContainer' come stringa
    @Override
    public String toString() {
        return usersToString() + System.lineSeparator() + dataToString();
    }
}
