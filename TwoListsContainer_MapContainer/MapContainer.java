import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

// OVERVIEW: contenitore di oggetti di tipo E associati ad utenti registrati
// (Data Storage per la memorizzazione e condivisione di dati)

// Typical Element: { < utente_i, { datoE_1, ..., datoE_j} >  }
//		 t.c utente_i = < String username, String password >
//		 t.c datoE_j = < E dato, User owner, List <User> sharedWith >

//     ogni utente (key), ha associata la propria lista di elementi
// AF: { f(user_key_i) = ArrayList<Dato<E>> list_ } forall i: 0 <= i < map.size()
//     t.c: user_key_i = <user_key_i.getUsername(), user_key_i.getPassword()>
//     && f(user_key_i) = list_ = <list_.getDato(), list_.getOwner()==user_key_i, list_.getSharedWith() } forall i: 0<=i<map.size()

// INV: map != null && forall i, j: 0 <= i, j < map.size()
// con i != j => user_key_i != null && users_key_j != null
// && user_key_i != user_key_j
// && forall i:  0 <= i <= map.size() => f(user_key_i) = ArrayList <Dato<E> list_
// && list_ != null && forall j: 0 <= j < list_.size() => list_.get(j) != null
// && forall z, k : 0 <= z, k < list_i.get(j).getSharedWith().size() =>
// => list_.get(j).getSharedWith().get(z) != list_.get(j).getSharedWith().get(k)
// (per ogni dato, gli utenti che ne hanno accesso tramite sharedWith non possono essere ripetuti)
// && dim = map.size() + ArrayList<Dato<E>> list_.size() di ogni chiave user
// (lunghezza di ogni lista di ogni utente + dimensione di map)

public class MapContainer <E> implements  SecureDataContainer<E> {
    // chiave è un utente, valore è una lista di Dato<E>
    private HashMap <User, ArrayList<Dato<E>>> map;
    private int dim;

    // EFFECTS: inizializza map
    public MapContainer() {
        map = new HashMap<>();
	   dim = 0;
    }

    // EFFECTS: return dim
    public int getDim() {
	    return dim;
    }

    /* controlla che l'utente con username = 'Owner' e con 'password' = passw
       sia registrato nel Container */
    // REQUIRES: Owner, passw != null && <Owner, passw> = u appartenente a 'map.keySet()'
    // EFFECTS: return <Owner, passw> = u
    // THROWS: NullPointerException se Id == null or passw == null (unchecked)
    // UserNotInexception se non esiste <Id, passw> = u in 'map.keySet()' (checked)
    private User isIn(String Owner, String passw) throws UserNotInException {
        if (Owner == null || passw == null) {
            throw new NullPointerException();
        }
        User u = new User(Owner, passw);
        //controllo che 'u' sia registrato
        if (!map.containsKey(u)) {
            throw new UserNotInException("'" + Owner + "' non registrato o password errata");
        }
        return u;
    }

    /* controlla che l'elemento 'data' sia accessibile da 'u' e
       restituisce l'oggetto 'Dato' contenente 'data' */
    // REQUIRES: data != null && in f(u) esiste 'data' or
    // forall other_user appartenenti a map.keySet() => in f(other_user) esiste 'data'
    // tc 'u' è presente in data.getSharedWith()
    // EFFECTS: return l'oggetto 'Dato' contenente 'data'
    // THROWS: NullPointerException se data == null (unchecked)
    // DataNotInException() se data.getOwner != 'u'
    // or 'u' non presente in data.getSharedWith() (checked)
    private Dato <E> isAccessibleBy(User u, E data) throws DataNotInException {
        if (data == null) {
            throw new NullPointerException();
        }
        //guarda per tutte le chiavi se u è proprietario, o se è in sharedwith
        for (User usr : map.keySet()) {
            ArrayList <Dato<E>> lst = map.get(usr);
            for (Dato <E> d : lst) {
                if (d.getDato().equals(data) && d.getOwner().equals(u)) {
                    return d;
                }
                for (User shareUser : d.getSharedWith()) {
                    if (d.getDato().equals(data) && shareUser.equals(u)) {
                        return d;
                    }
                }
            }
        }
        throw new DataNotInException(data.toString() + " non presente o accesso negato");
     }

    /* Crea l’identità di un nuovo utente della collezione */
    // REQUIRES: Id, passw != null && <Owner, passw> = u non presente in 'map.keySet()'
    // MODIFY map, dim
    // EFFECTS: crea l'associazione < u, ArrayList<Dato<E> {} > in map
    // (f(u) = ArrayList<Dato<E>>) && dim++
    // THROWS: NullPointerException se Id == null or passw == null (unchecked)
    // AlreadyInexception se 'u' già presente in 'map.keySet()' (checked)
    @Override
    public void createUser(String Id, String passw) throws AlreadyInException {
        if (Id == null || passw == null) {
            throw new NullPointerException();
        }
        //controllo che l'utente non sia già registrato nel Container
        for (User u : map.keySet()) {
            if (u.getUsername().equals(Id)) {
                throw new AlreadyInException("Utente già registrato");
            }
        }
        User u = new User(Id, passw);
        //creo la lista dei dati con proprietario 'u'
        map.put(u, new ArrayList<>());
        dim++;
    }

    /* Restituisce il numero degli elementi di un utente presenti nella collezione */
    // REQUIRES: Owner, passw != null && <Owner, passw> = u appartenente a 'map.keySet()'
    // EFFECTS: return lunghezza lista degli elementi di 'u' (f(u).size())
    // THROWS: NullPointerException se Id == null or passw == null (unchecked)
    // UserNotInexception se non esiste <Id, passw> = u in 'map.keySet()' (checked)
    @Override
    public int getSize(String Owner, String passw) throws UserNotInException {
        //controllo che 'u' sia registrato
        User u = isIn(Owner, passw);
        return  map.get(u).size();
    }

    /* Inserisce il valore del dato nella collezione
       se vengono rispettati i controlli di identità */
    // REQUIRES: Owner, passw != null && <Owner, passw> = u appartenente a 'map.keySet()'
    // MODIFY: map, dim
    // EFFECTS: f(u).add(data), return l'esito dell'inserimento && dim++
    // THROWS: NullPointerException se Id == null or passw == null (unchecked)
    // UserNotInexception se non esiste <Id, passw> = u in 'map.keySet()' (checked)
    @Override
    public boolean put(String Owner, String passw, E data) throws UserNotInException {
        if (data == null)
            return false;
        //controllo che 'u' sia  registrato nel Container
        User u = isIn(Owner, passw);
        //aggunge alla lista di chiave 'u', 'data'
        dim++;
        return map.get(u).add(new Dato <E> (u, data));
    }

    /* Ottiene una copia del valore del dato nella collezione
       se vengono rispettati i controlli di identità */
    // REQUIRES: Owner, passw, data != null && <Owner, passw> = u appartenente a 'map.keySet()'
    // && in f(u) esiste 'data' or
    // forall other_user appartenenti a map.keySet(): in f(other_user) esiste 'data' tc
    // 'u' è presente in data.getSharedWith()
    // EFFECTS: return 'data'
    // THROWS: NullPointerException se Id == null or passw == null or data == null (unchecked)
    // UserNotInexception se non esiste <Id, passw> = u in 'map.keySet()' (checked)
    // DataNotInException se 'u' non ha accesso a 'data'
    @Override
    public E get(String Owner, String passw, E data) throws UserNotInException, DataNotInException{
        // controlla che l'utente con username = 'Owner' e con 'password' = passw
        // sia registrato nel Container
        User u = isIn(Owner, passw);
        // e che sia il proprietario di almeno un 'data'
        // oppure che abbia accesso ad almeno un 'data'
        Dato <E> d = isAccessibleBy(u, data);
        return d.getDato();
    }

    /* Rimuove il dato nella collezione
       se vengono rispettati i controlli di identità (solo il proprietario può rimuovere) */
    // REQUIRES: Owner, passw, data != null && <Owner, passw> = u appartenente a 'map.keySet()'
    // && in f(u) esiste data
    // EFFECTS: return l'ultima occorrenza di 'data' eliminati, dim-- per ogni eliminazione
    // THROWS: NullPointerException se Id == null or passw == null or data == null (unchecked)
    // UserNotInexception se non esiste <Id, passw> = u in 'map.keySet()' (checked)
    // DataNotInException se in f(u) non è presente 'data'
    @Override
    public E remove(String Owner, String passw, E data) throws UserNotInException, DataNotInException {
        // controlla che l'utente con username = 'Owner' e con 'password' = passw
        // sia registrato nel Container
        User u = isIn(Owner, passw);
        //controlla se è proprietario di almeno un 'data'
        //e nel caso lo rimuove
        E e = null;
        ArrayList<Dato<E>> lst = map.get(u);
        for (int i = lst.size()-1; i >= 0; i--) {
            if (lst.get(i).getDato().equals(data)) {
                e = lst.get(i).getDato();
                lst.remove(i);
                dim--;
            }
        }
        if (e == null) {
            throw new DataNotInException("dato non presente o non proprietario");
        }
        return e;
    }


    /* Crea una copia del dato nella collezione
       se vengono rispettati i controlli di identità (solo il proprietario può copiare)*/
    // REQUIRES: Owner, passw != null && <Owner, passw> = u appartenente a 'map.keySet()'
    // && in f(u) esiste 'data'
    // EFFECTS: copia la prima occorrenza di 'data' nella lista del proprietario
    // mantenendo la visibilità originale
    // THROWS: NullPointerException se Id == null or passw == null or data == null (unchecked)
    // UserNotInexception se non esiste <Id, passw> = u in 'map.keySet()' (checked)
    // DataNotInException se in f(u) non esiste 'data' (checked)
    @Override
    public void copy(String Owner, String passw, E data) throws UserNotInException, DataNotInException {
        if (data == null) {
            throw new NullPointerException();
        }
        // controlla che l'utente con username = 'Owner' e con 'password' = passw
        // sia registrato nel Container
        // e sia il proprietario di 'data'
        User u = isIn(Owner, passw);
        if (!map.get(u).contains(new Dato<E>(u, data))) {
             throw new DataNotInException(data.toString() +" non presente o accesso negato");
         }
         // scorro la sua lista finché non trovo la prima occorrenza, e la copio in fondo
         ArrayList<Dato<E>> list = map.get(u);
         for (int i = 0; i < list.size(); i++) {
             if (data.equals(list.get(i).getDato())) {
                 map.get(u).add(new Dato<E> (list.get(i).getOwner(), list.get(i).getDato(), list.get(i).getSharedWith()));
                 dim++;
                 return;
             }
         }
    }

    /* Condivide il dato nella collezione con un altro utente
       se vengono rispettati i controlli di identità (solo il proprietario può condividere)*/
    // REQUIRES: Owner, passw != null && <Owner, passw> = u, <other_user, _> = other appartenenti a 'map.keySet()'
    // && in f(u) esiste data
    // EFFECTS: aggiunge la visibilità di 'data' a 'other' se non la ha già
    // ('other' viene aggiunto in sharedWith di 'data')
    // THROWS: NullPointerException se Id == null or passw == null or data == null or Other == null (unchecked)
    // UserNotInexception se non esiste <Id, passw> = u in 'map.keySet()'
    // or non esiste <other_user, _> = other in 'map.keySet()' (checked)
    // DataNotInException se in f(u) non esiste 'data' (checked)
    @Override
    public void share(String Owner, String passw, String Other, E data) throws UserNotInException, DataNotInException {
        if (Other == null) {
            throw new NullPointerException();
        }
        // controlla che l'utente con username = 'Owner' e con 'password' = passw
        // sia registrato nel Container e sia proprietario di almeno un 'data'
        User u = isIn(Owner, passw);
        if (!map.get(u).contains(new Dato<E>(u, data))) {
            throw new DataNotInException(data.toString() +" non presente o accesso negato");
        }
        //controllo che che ci sia l'utente con username 'Other'
        User other_user = null;
        for (User usr: map.keySet()) {
            if (usr.getUsername().equals(Other)) {
                other_user = usr.getUser();
                break;
            }
        }
        // se 'other_user' non è registrato in Container non può accedere a nessun dato
        if(other_user == null) {
            throw new UserNotInException(Other + " non registrato");
        }
        // controllo che l'utente 'other_user' non abbia già accesso a 'data'
        // devo dargli accesso a tutte le occorrenze di 'data' con proprietario 'Owner'
        ArrayList<Dato<E>> temp = map.get(u);
        for (int i = 0 ; i < temp.size(); i++) {
             if (temp.get(i).getDato().equals(data) &&
            (!temp.get(i).getSharedWith().contains(other_user) || temp.get(i).getSharedWith().isEmpty())) {
                 temp.get(i).getSharedWith().add(other_user);
             }
         }
    }


    // REQUIRES: Owner, passw != null && <Owner, passw> = u appartenente a 'map.keySet()'
    // EFFECTS: restituisce un iteratore (senza remove) che genera tutti i dati
    // dell’utente in ordine arbitrario se vengono rispettati i controlli di identità
    // i dati condivisi con 'Owner', ma dei quali non è prorpietario non vengono conteggiati
    // THROWS: NullPointerException se Id == null or passw == null or data == null (unchecked)
    // UserNotInexception se non esiste <Id, passw> = u in 'map.keySet()' (checked)
    @Override
   public Iterator<E> getIterator(String Owner, String passw) throws UserNotInException {
        User u = isIn(Owner, passw);
        return new datiIterator(u);
    }

    private class datiIterator implements Iterator<E> {
        int index;
        ArrayList <Dato <E>> it;

        datiIterator(User Owner) {
            index = 0;
            it = map.get(Owner);
        }
        @Override
        public boolean hasNext() {
            return index < it.size();
        }
        @Override
        public E next() {
            return it.get(index++).getDato();
        }
        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove");
        }
    }

    /* Restituisce la lista 'users' come stringa */
    public String usersToString() {
        int i = 0;
        String s = "Utenti : { " + System.lineSeparator();
        if (map.isEmpty()) {
            return s + "empty"+ System.lineSeparator() +  "}";
        }
         for (User usr : map.keySet()) {
            s = s + (++i) + ": " + usr.toString() +  System.lineSeparator();
         }
        return s + " }";
    }

    /* Restituisce la lista 'dati' come stringa */
    public String dataToString() {
        int i = 0;
        if (map.isEmpty()) {
            return "{ empty }";
        }
        String s = "";
        for (User usr: map.keySet()) {
            s = s + (++i) + ": " + usr.getUsername() + " -> [ " ;
            for (Dato<E> d : map.get(usr)) {
                s = s + d.toString() + "; " + System.lineSeparator();
            }
            s = s + " ]" + System.lineSeparator();
        }
        return s;
    }

    /* Restituisce la componente 'dato <E>' della lista 'dati' come stringa */
    public String Only_E_ToString() {
        int i = 0;
        if (map.isEmpty()) {
            return "{ empty }";
        }
        String s = "";
        for (User usr : map.keySet()) {
            for(Dato <E> d : map.get(usr)) {
                s = s + (++i) + ": " + d.getDato().toString() + System.lineSeparator();
            }
        }
        return s + " }";
    }

    /* Restituisce la classe 'MySecureDataContainer' come stringa */
    @Override
    public String toString() {
        return usersToString() + System.lineSeparator() + dataToString();
    }
}
