import java.util.Iterator;

// OVERVIEW: contenitore di oggetti modificabile di tipo E associati ad utenti registrati
// (Data Storage per la memorizzazione e condivisione di dati)

// Typical Element:  <Id, passw, { e_0, ..., e_n-1 }> dove e_i != null forall i: 0 <= i < n
// Ogni utente ha la sua lista di elementi

public interface SecureDataContainer <E>{

    /* crea l’identità di un nuovo utente della collezione */
    // REQUIRES: Id != null && passw != null,
    // e non esiste una tripla <Id, passw, { e_0, ..., e_n-1 }> in this
    // MODIFY: this
    // EFFECTS: crea e aggiunge la tripla <Id, passw,  {} }> in this
    // THROWS: NullPointerException se id == null or passw == null, (unchecked)
    // UserAlreadyInException se esiste <Id, passw, { e_0, ..., e_n-1 }> in 'this' (checked)
    public void createUser(String Id, String passw) throws AlreadyInException, NullPointerException;


    /* restituisce il numero degli elementi di Owner presenti nella collezione */
    // REQUIRES: Owner != null, passw != null,
    // e esiste una tripla <Owner, passw, { e_0, ..., e_n-1 }> in this
    // EFFECTS: restituisce #{ e_0, ..., e_n-1 } di <Owner, passw, { e_0, ..., e_n-1 }>
    // THROWS: NullPointerException se Owner == null or passw == null (unchecked)
    // UserNotInexception se non esiste <Owner, passw, { e_0, ..., e_n-1 }> in 'this' (checked)
    public int getSize(String Owner, String passw) throws UserNotInException, NullPointerException;


    /* Inserisce il valore del dato nella collezione
       se vengono rispettati i controlli di identità */
    // REQUIRES: Owner != null, passw != null, data!=null
    // e esiste una tripla <Owner, passw, { e_0, ..., e_n-1 }> in this
    // MODIFY: this
    // EFFECTS: inserisce 'data' in { e_0, ..., e_n-1 } di <Owner, passw, { e_0, ..., e_n-1 }>
    // restituendo l'esito dell'operazione
    // THROWS: NullPointerException se Owner == null or passw == null or data == null (unchecked)
    // UserNotInexception se non esiste <Owner, passw, { e_0, ..., e_n-1 }> in 'this' (checked)
    public boolean put(String Owner, String passw, E data) throws UserNotInException, NullPointerException;


    /* Ottiene una copia del valore del dato nella collezione
       se vengono rispettati i controlli di identità */
    // REQUIRES: Owner != null, passw != null, data!=null
    // e esiste una tripla <Owner, passw, { e_0, ..., e_n-1 }> in this
    // e 'data' presente in { e_0, ..., e_n-1 } di <Owner, passw, { e_0, ..., e_n-1 }>
    // EFFECTS: restituisce la prima occorrenza di 'data'
    // presente in { e_0, ..., e_n-1 } di <Owner, passw, { e_0, ..., e_n-1 }>
    // THROWS: NullPointerException se Owner == null or passw == null or data == null (unchecked)
    // UserNotInexception se non esiste <Owner, passw, { e_0, ..., e_n-1 }> in 'this' (checked)
    // DataNotInException se 'data' non presente in
    // { e_0, ..., e_n-1 } di <Owner, passw, { e_0, ..., e_n-1 }> (checked)
    public E get(String Owner, String passw, E data) throws UserNotInException, DataNotInException, NullPointerException;


    /* Rimuove il dato nella collezione
       se vengono rispettati i controlli di identità */
    // REQUIRES: Owner != null, passw != null, data!=null e
    // e esiste una tripla <Owner, passw, { e_0, ..., e_n-1 }> in this
    // e 'data' presente in { e_0, ..., e_n-1 } di <Owner, passw, { e_0, ..., e_n-1 }>
    // EFFECTS: rimuove tutte le occorrenze di 'data' presente in  { e_0, ..., e_n-1 } di
    // <Owner, passw, { e_0, ..., e_n-1 }> e ne restituisce una
    // THROWS: NullPointerException se Owner == null or passw == null or data == null (unchecked)
    // UserNotInexception se non esiste <Owner, passw, { e_0, ..., e_n-1 }> in 'this' (checked)
    // DataNotInException se 'data' non presente in
    // { e_0, ..., e_n-1 } di <Owner, passw, { e_0, ..., e_n-1 }> (checked)
    public E remove(String Owner, String passw, E data) throws UserNotInException, DataNotInException, NullPointerException;


    /* Crea una copia del dato nella collezione
       se vengono rispettati i controlli di identità */
    // REQUIRES: Owner != null, passw != null, data!=null e
    // e esiste una tripla <Owner, passw, { e_0, ..., e_n-1 }> in this
    // e 'data' presente in { e_0, ..., e_n-1 } di <Owner, passw, { e_0, ..., e_n-1 }>
    // EFFECTS: copia (riaggiunge) la prima occorrenza di 'data' presente in { e_0, ..., e_n-1 }
    // nuovamente in { e_0, ..., e_n-1 } di <Owner, passw, { e_0, ..., e_n-1 }>
    // THROWS: NullPointerException se Id == null or passw == null or data == null (unchecked)
    // UserNotInexception se non esiste <Owner, passw, { e_0, ..., e_n-1 }> in 'this' (checked)
    // DataNotInException se 'data' non presente in { e_0, ..., e_n-1 }
    // di <Owner, passw, { e_0, ..., e_n-1 }> (checked)
    public void copy(String Owner, String passw, E data) throws UserNotInException, DataNotInException, NullPointerException;


    /* Condivide il dato nella collezione con un altro utente
       se vengono rispettati i controlli di identità */
    // REQUIRES: Owner != null, passw != null, data!=null e
    // e esiste una tripla <Owner, passw, { e_0, ..., e_n-1 }> in this
    // e 'data' presente in { e_0, ..., e_n-1 } di <Owner, passw, { e_0, ..., e_n-1 }>
    // e esiste una tripla <Other, _, { e_o_0, ..., e_o_n-1 }> in this
    // MODIFY: this
    // EFFECTS: copia (aggiunge) la prima occorrenza di 'data' presente in { e_0, ..., e_n-1 },
    // in { e_o_0, ..., e_o_n-1 } di <Other, _, { e_o_0, ..., e_o_n-1 }> appartenente a this
    // THROWS: NullPointerException se Id == null or passw == null or data == null or Other == null (unchecked)
    // UserNotInexception se non esiste <Other, _, { e_o_0, ..., e_o_n-1 }>
    // or <Owner, passw, { e_0, ..., e_n-1 }> in this (checked)
    // DataNotInException se 'data' non presente in { e_0, ..., e_n-1 }
    // di <Owner, passw, { e_0, ..., e_n-1 }> (checked)
    public void share(String Owner, String passw, String Other, E data) throws UserNotInException, DataNotInException, NullPointerException;


    /* Condivide il dato nella collezione con un altro utente
       se vengono rispettati i controlli di identità  */
    // REQUIRES: Owner != null, passw != null, data!=null e
    // e esiste una tripla <Owner, passw, { e_0, ..., e_n-1 }> in this
    // EFFECTS: restituisce un iteratore che genera tutti i dati <e_1, .., e_n>
    // presenti in { e_0, ..., e_n-1 } di <Owner, passw, { e_0, ..., e_n-1 } > appartenente a this
    // THROWS: NullPointerException se Owner == null or passw == null (unchecked)
    // UserNotInexception se non esiste <Owner, passw, { e_0, ..., e_n-1 }> in this (checked)
    public Iterator<E> getIterator(String Owner, String passw) throws UserNotInException, NullPointerException;
}
