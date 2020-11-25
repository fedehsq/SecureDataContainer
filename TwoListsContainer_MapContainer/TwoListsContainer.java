
import java.util.ArrayList;
import java.util.Iterator;

// OVERVIEW: contenitore di oggetti di tipo E associati ad utenti registrati
// (Data Storage per la memorizzazione e condivisione di dati)

// Typical Element: { < utente_i, { datoE_1, ..., datoE_j} >  }
//		 t.c utente_i = < String username, String password >
//		 t.c datoE_j = < E dato, User owner, List <User> sharedWith >

// AF: { f(dati.get(i) = users.get(j) } forall i: 0 <= i < dati.size(), forall j: 0 <= j < users.size()
//      gli elementi del codominio 'users' possono non avere controimmagini
//     { users_i = < users.get(i).getUsername(), users.get(i).getPassword() > } forall i: 0 <= i < users.size()
//     { dati_i = < dati.get(i).getDato(), dati.get(i).getOwner(), dati.get(i).getSharedWith() > } forall i: 0 <= i < dati.size()

// INV: users!=null && forall i, j: 0 <= i, j < users.size()
// con i != j => users.get(i) != null && users.get(j) != null
// && users.get(i) != users.get(j)
// && dati != null && forall i: 0 <= i < dati.size() => dati.get(i) != null
// && dati.get(i).getOwner() è un elemento di 'users'
// && forall i, j: 0 <= i, j < dati.get(i).getSharedWith().size()
// => dati.get(i).getSharedWith().get(i) != dati.get(i).getSharedWith().get(j)
// per ogni dato, gli utenti che ne hanno accesso tramite sharedWith non possono essere ripetuti
// && dim = users.getSize() + dati.getSize()

public class TwoListsContainer <E> implements SecureDataContainer<E>{
    private ArrayList <User> users;
    private ArrayList <Dato<E>> dati;
    private int dim;

    //EFFECTS: Inizializza users: {} e dati: {}
    public TwoListsContainer() {
        users = new ArrayList<>();
        dati = new ArrayList<Dato<E>>();
	      dim=0;
    }

    //EFFECTS: return dim
    public int getDim() {
	    return dim;
    }

    /* controlla che l'utente con username = 'Owner' e con 'password' = passw
       sia registrato nel Container */
    // REQUIRES: Owner, passw != null && l'utente con id = 'Owner' presente in 'users'
    // EFFECTS: controlla che Owner sia presente in 'users'
    // THROWS: NullPointerException() se Owner == null or passw == null (unchecked)
    // UserNotInException se l'utente con id = 'Owner' non presente in 'users' (checked)
    // IllegalArgumentException se ad 'Owner' presente in 'users' non corrisponde 'passw'
    private void check(String Owner, String passw) throws UserNotInException {
        if (Owner==null || passw==null) {
            throw new NullPointerException();
        }
        int i = IndexOf(Owner);
        // se l'utente non c'è:
        if (i == -1) {
            throw new UserNotInException(Owner + " non registrato");
        }
        // o all'utente con username 'Id', non corrisponde la password passata:
        if (!(passw.equals(users.get(i).getPassword()))) {
            throw new IllegalArgumentException("Password errata");
        }
    }

    /* controlla che l'utente con username = 'Owner' e con 'password' = passw
       sia registrato nel Container e sia il proprietario di 'data' */
    // REQUIRES: Owner, passw, data != null &&
    // l'utente con id = 'Owner' presente in 'users'
    // EFFECTS: controlla che Owner sia presente in 'users'
    // && f(data) = Owner (Owner proprietario di data)
    // THROWS: NullPointerException() se Owner == null or passw == null or data == null (unchecked)
    // UserNotInException se Owner non presente in 'users' (checked)
    // IllegalArgumentException se ad 'Owner' presente in 'users'
    // non corrisponde 'passw' (unchecked)
    // DataNotInException se f(data) != Owner (checked)
    private void check(String Owner, String passw, E data) throws UserNotInException, DataNotInException {
        check(Owner, passw);
        if (data == null) {
            throw new NullPointerException();
        }
        int i = IndexOf(data, Owner);
        // se il dato 'data' non ha come proprietario 'Owner' solleva eccezione
        if (i == -1) {
            throw new DataNotInException(data.toString() + " non presente o accesso negato");
        }
    }

    // REQUIRES: s != null
    // EFFECTS: controlla che l'utente con username 's' appartiene al Container
    // restituisce l'indice se presente, -1 altrimenti
    // THROWS: NullPointerException se s == null (unchecked)
    private int IndexOf(String s) {
        if (s == null)
            throw new NullPointerException();
        //controlla che l'utente con username 's' sia in users
        for (int i = 0; i < users.size(); i++) {
            if (s.equals(users.get(i).getUsername())) {
               return i;
            }
        }
        return -1;
    }

    // REQUIRES: data!=null, owner!=null
    // EFFECTS: controlla che l'elemento 'data' con proprietario 'Owner'
    // occorra nel Container almeno una volta (f(data) = Owner)
    // restituisce il primo indice se presente, -1 altrimenti
    // THROWS: NullPointerException se data == null or Owner == null (unchecked)
    private int IndexOf(E data, String Owner) {
        if (data == null || Owner == null) {
            throw new NullPointerException();
        }
        // controlla se il dato 'data' è in 'dati' con proprietario 'Owner'
        for (int i = 0; i < dati.size(); i++) {
            if (data.equals(dati.get(i).getDato()) && dati.get(i).getOwner().getUsername().equals(Owner)) {
               return i;
            }
        }
        return -1;
    }

    //come sopra, ma la ricerca parte da un indice passato
    private int IndexOf(int start, E data, String Owner) {
        if (start > dati.size()) {
            throw new IllegalArgumentException();
        }
        if (data == null || Owner == null) {
            throw new NullPointerException();
        }
        // controlla se il dato 'data' è in 'dati' con proprietario 'Owner'
        for (int i = start; i < dati.size(); i++) {
            if (data.equals(dati.get(i).getDato()) && dati.get(i).getOwner().getUsername().equals(Owner)) {
               return i;
            }
        }
        return -1;
    }

    /* controlla che l'elemento 'data' sia accessibile da 'Owner',
       restituisce l'oggetto 'Dato' contenente 'data' */
    // REQUIRES: data != null
    // && in 'dati' esiste 'data' tc: f(data) == Owner or
    // in data.getSharedWith() esiste Owner
    // EFFECTS: return l'oggetto 'Dato' contenente 'data'
    // THROWS: NullPointerException se data == null (unchecked)
    // DataNotInException se f(data) != 'Owner'
    // or 'Owner' non presente in data.getSharedWith() (checked)
     private Dato <E> isAccessibleBy(String Owner, String passw, E data) throws DataNotInException {
        if (data == null) {
           throw new NullPointerException();
        }
        for (Dato<E> d : dati) {
            //controllo che 'data' sia nel Container e che 'Owner' sia il proprietario...
            if (d.getDato().equals(data) && d.getOwner().getUsername().equals(Owner)) {
                return d;
            }
            //... oppure controllo che 'Owner' sia nella lista degli utenti che possono accedere a 'data'
            ArrayList<User> SharedWith = d.getSharedWith();
            for (User u : SharedWith) {
                if (d.getDato().equals(data) && u.getUsername().equals(Owner)) {
                    return d;
                }
            }
        }
        throw new DataNotInException(data.toString() + " non presente o accesso negato");
    }

    /* Crea l’identità di un nuovo utente della collezione */
    // REQUIRES: Id, passw != null && coppia <Id, passw> = u non presente in 'users'
    // MODIFY users, dim
    // EFFECTS: crea e aggiunge l'utente <Id, passw> = u in 'users' && dim++
    // THROWS: NullPointerException se Id == null or passw == null (unchecked)
    // AlreadyInexception se <Id, passw> = u già presente in 'users' (checked)
    @Override
    public void createUser(String Id, String passw) throws AlreadyInException {
        if (Id == null || passw == null)
            throw new NullPointerException();
        //controllo che l'utente non sia già registrato nel Container
        if (IndexOf(Id) > -1) {
            throw new AlreadyInException("Utente già registrato");
        }
        users.add(new User(Id, passw));
	   dim++;
    }

    /* Restituisce il numero degli elementi di un utente presenti nella collezione */
    // REQUIRES: Owner, passw != null && <Owner, passw> = u appartenente a 'users'
    // EFFECTS: return #elem in 'dati' tc f(dati.get(i).getOwner()) == u
    // forall i: 0 <= i < dati.size()
    // THROWS: NullPointerException se Id == null or passw == null (unchecked)
    // UserNotInexception se non esiste <Id, passw> = u in 'users' (checked)
    @Override
    public int getSize(String Owner, String passw) throws UserNotInException {
        check(Owner, passw);
        int num = 0;
        // scorre tutta lista 'dati' e conta quanti dati sono di 'Owner'
        for (Dato<E> d : dati) {
            if (d.getOwner().getUsername().equals(Owner))
                num++;
        }
        return num;
    }

    /* Inserisce il valore del dato nella collezione
       se vengono rispettati i controlli di identità */
    // REQUIRES: Owner, passw != null && <Owner, passw> = u appartenente a 'users'
    // MODIFY: dati, dim
    // EFFECTS: dati.add(data) && f(data) = u, return l'esito dell'inserimento && dim++
    // THROWS: NullPointerException se Id == null or passw == null (unchecked)
    // UserNotInexception se non esiste <Id, passw> = u in 'users' (checked)
    @Override
    public boolean put(String Owner, String passw, E data) throws UserNotInException {
        // controlla che l'utente con username = 'Owner' e con 'password' = passw
        // sia registrato nel Container
        check(Owner, passw);
        if (data == null) {
            return false;
        }
        int i = IndexOf(Owner);
        dim++;
        return dati.add(new Dato<E> (users.get(i), data));
    }

    /* Ottiene una copia del valore del dato nella collezione
       se vengono rispettati i controlli di identità */
    // REQUIRES: data, Owner, passw != null && <Owner, passw> = u appartenente a 'users'
    // && in 'dati' esiste 'data' tc: f(data) == 'u' or
    // in data.getSharedWith() esiste 'u'
    // EFFECTS: return 'data'
    // THROWS: NullPointerException se Id == null or passw == null or data == null (unchecked)
    // UserNotInexception se non esiste <Id, passw> = u in 'users' (checked)
    // DataNotInException se f(data) != u or
    // in data.getSharedWith() non esiste 'u' (checked)
    @Override
    public E get(String Owner, String passw, E data) throws UserNotInException, DataNotInException {
        // controlla che l'utente con username = 'Owner' e con 'password' = passw
        // sia registrato nel Container e sia il proprietario di almeno un 'data'
        check(Owner, passw);
        // oppure che abbia accesso ad almeno un 'data'
        Dato<E> d = isAccessibleBy(Owner, passw, data);
        return d.getDato();
    }

    /* Rimuove il dato nella collezione
       se vengono rispettati i controlli di identità (solo il proprietario può rimuovere) */
    // REQUIRES: data, Owner, passw != null && <Owner, passw> = u appartenente a 'users'
    // && in 'dati' esiste 'data' tc: f(data) == 'u'
    // MODIFY: dati, dim
    // EFFECTS: elimina tutte le occorrenze di 'data' presenti in 'dati'
    // tc f(data) == 'u' && dim-- per ogni rimozione &&
    // return ultimo 'data' eliminato
    // THROWS: NullPointerException se Id == null or passw == null or data == null (unchecked)
    // UserNotInexception se non esiste <Id, passw> = u in 'users' (checked)
    // DataNotInException se f(data) != u (checked)
    @Override
    public E remove(String Owner, String passw, E data) throws UserNotInException, DataNotInException {
        // controlla che l'utente con username = 'Owner' e con 'password' = passw
        // sia registrato nel Container
        check(Owner, passw);
        // e sia il prorietario di 'data': solo il proprietario di 'data' può rimuoverlo
        int i = IndexOf(data, Owner);
	   if (i == -1)
	   	throw new IllegalArgumentException(data.toString() + " non presente o accesso negato");
        //salva 'data' contenuto nel Oggetto 'Dato' della lista prima di rimuoverlo
        E e = null;
        //rimuove tutte le occorrenze
        for (i = dati.size()-1; i>=0; i--) {
            if(dati.get(i).getDato().equals(data)
            && dati.get(i).getOwner().getUsername().equals(Owner)) {
                 e = dati.get(i).getDato();
                 dati.remove(i);
                 dim--;
            }
        }
        return e;
    }


    /* Crea una copia del dato nella collezione
       se vengono rispettati i controlli di identità  (solo il proprietario può copiare)*/
    // REQUIRES: data, Owner, passw != null && <Owner, passw> = u appartenente a 'users'
    // && in 'dati' esiste 'data' tc: f(data) == 'u'
    // EFFECTS: copia (aggiunge) in 'dati' la prima occcorrenza di 'data'
    // THROWS: NullPointerException se Id == null or passw == null or data == null (unchecked)
    // UserNotInexception se non esiste <Id, passw> = u in 'users' (checked)
    // DataNotInException se f(data) != u (checked)
    @Override
    public void copy(String Owner, String passw, E data) throws UserNotInException, DataNotInException {
        if (data == null) {
            throw new NullPointerException();
        }
        // controlla che l'utente con username = 'Owner' e con 'password' = passw
        // sia registrato nel Container e sia il proprietario di 'data'
        check(Owner, passw);
        int i = IndexOf(data, Owner);
	   if (i == -1) {
            throw new IllegalArgumentException(data.toString() + " non presente o accesso negato");
        }
        dati.add(new Dato <E> (dati.get(i).getOwner(), dati.get(i).getDato(), dati.get(i).getSharedWith()));
	   dim++;
    }

    /* Condivide il dato nella collezione con un altro utente
       se vengono rispettati i controlli di identità (solo il proprietario può condividere)*/
    // REQUIRES: data, Owner, Other, passw != null
    // && <Owner, passw> = u, <Other, _> = other appartenenti a 'users'
    // && in 'dati' esiste 'data' tc: f(data) == 'u'
    // EFFECTS: aggiunge la visibilità di tutte le occorrenze di 'data' a 'other' se non la ha già,
    // data.getSharedWith().add(other) ('other' viene aggiunto in sharedWith di tutti i 'data')
    // THROWS: NullPointerException se Id == null or passw == null or data == null or Other == null (unchecked)
    // UserNotInexception se non esiste <Id, passw> = u or
    // non esiste <Other, _> = other in 'users' (checked)
    // DataNotInException se f(data) != u (checked)
    @Override
    public void share(String Owner, String passw, String Other, E data) throws UserNotInException, DataNotInException {
        // controlla che l'utente con username = 'Owner'e con 'password' = passw
        // sia registrato nel Container
        // e possegga il dato 'data' solo il proprietario può condividere
        check(Owner, passw, data);
        int i = IndexOf(data, Owner);
        int j = IndexOf(Other);
        // se 'Other' non è registrato in Container non può accedere a nessun dato
        if (j == -1) {
            throw new UserNotInException(Other + " non registrato");
        }
        // controllo che l'utente 'Other' non abbia già accesso a 'data'
        // devo dargli accesso a tutte le occorrenze di 'data' con proprietario 'Owner'
        while (i > -1) {
            ArrayList <User> SharedWith = dati.get(i).getSharedWith();
            //se vuota aggiungo la visibilità dell'oggetto a 'Other'
            if (SharedWith.isEmpty()) {
                SharedWith.add(users.get(j));
            }
            else {
                boolean found = false;
                int z = 0;
                while (!found && z < SharedWith.size() ) {
                    if (SharedWith.get(z).getUsername().equals(Other)) {
                        found = true;
                    }
                    z++;
                }
                if (!found) {
                    //aggiungo la visibilità dell'oggetto a 'Other'
                    SharedWith.add(users.get(j));
                }
            }
            //la ricerca di un altro eventuale 'data' parte da un elemento dopo
            i = IndexOf(i+1, data, Owner);
        }
    }

    // REQUIRES: Owner, passw != null && <Owner, passw> = u appartenente a 'users'
    // EFFECTS: restituisce un iteratore (senza remove) che genera tutti i dati
    // dell’utente in ordine arbitrario se vengono rispettati i controlli di identità
    // i dati condivisi con u, ma dei quali non ne è prorpietario non vengono conteggiati
    // THROWS: NullPointerException se Id == null or passw == null or data == null (unchecked)
    // UserNotInexception se non esiste <Id, passw> = u in 'users' (checked)
    @Override
   public Iterator<E> getIterator(String Owner, String passw) throws UserNotInException {
        check(Owner, passw);
        return new datiIterator(Owner);
    }
	private class datiIterator implements Iterator<E> {
        int index;
        ArrayList<E> it;

        datiIterator(String Owner) {
            index = 0;
            it = new ArrayList<>();
            //tutti i dati con proprietario 'Owner' li inserisco in it
            for (int i=0; i<dati.size(); i++) {
                if (dati.get(i).getOwner().getUsername().equals(Owner)) {
                    it.add(dati.get(i).getDato());
                }
            }
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

    /* Restituisce la lista 'users' come stringa */
    public String usersToString(){
        int i;
        String s = "Utenti : { " + System.lineSeparator();
        if (users.isEmpty())
            return s + "empty"+ System.lineSeparator() +  "}";
        for (i = 0; i < users.size(); i++)
            s =  s + (i+1) + ": " + users.get(i).toString() +  System.lineSeparator();
        return s + " }";
    }

    /* Restituisce la lista 'dati' come stringa */
    public String dataToString() {
        int i;
        String s = "Dati : { " + System.lineSeparator();
        if (dati.isEmpty())
            return s + "empty" + System.lineSeparator() +  "}";
        for (i = 0; i < dati.size(); i++)
            s = s + (i+1) + ": " + dati.get(i).toString() + " di proprietà di: " + dati.get(i).getOwner() + System.lineSeparator();
        return s + " }";
    }

    /* Restituisce la componente 'dato <E>' della lista 'dati' come stringa */
    public String Only_E_ToString() {
        int i;
        String s = "E_obj: { " + System.lineSeparator();
        if (dati.isEmpty())
            return s + "empty"+ System.lineSeparator() +  "}";
        for (i = 0; i < dati.size(); i++)
            s = s + (i+1) + ": " + dati.get(i).getDato().toString() + System.lineSeparator();
        return s + " }";
    }

    /* Restituisce la classe 'MySecureDataContainer' come stringa */
    @Override
    public String toString() {
        return usersToString() + System.lineSeparator() + dataToString();
    }
}
