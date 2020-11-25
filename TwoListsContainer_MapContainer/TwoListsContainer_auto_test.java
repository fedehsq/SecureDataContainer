import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;


public class TwoListsContainer_auto_test {
    public static void main(String[] args) throws AlreadyInException, UserNotInException, DataNotInException, FileNotFoundException{
        // crea lo stream ( formattato ) di input
        Scanner file_input =new Scanner ( new BufferedReader (new FileReader ("createUser.txt")));
        // crea container di stringhe
        TwoListsContainer<Canzone> c = new TwoListsContainer<>();
        ArrayList <Canzone> get= new ArrayList <>();
        ArrayList <Canzone> removed = new ArrayList <>();

        while (file_input.hasNext()) {
            String id=file_input.next();
            String psw=file_input.next();
            //inserzione utenti
            try {
                c.createUser(id, psw);
                System.out.println("Utente: " + "<" + id +", " + psw + ">" + " inserito");
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Inserzione di " + id + " non riuscita");
            }
        }
       //stampa utenti
	  System.out.println(System.lineSeparator());
   	  System.out.println("Stampo utenti...");
       System.out.println(c.usersToString());
	  System.out.println(System.lineSeparator());

        //inserzione elementi
	  System.out.println("Inserisco canzoni...");
       file_input =new Scanner ( new BufferedReader (new FileReader ("put.txt")));
       while (file_input.hasNext()) {
            String id=file_input.next();
            String psw=file_input.next();
            String str=file_input.next();
            Canzone song=new Canzone(str);
            try {
                c.put(id, psw, song);
                System.out.println("Utente: " + "<" + id +", " + psw + ">" + " ha inserito: " + song.toString());
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Inserzione di " + song.toString() + " non riuscita");
            }
       }
       //stampa canzoni con proprietario
       System.out.println(System.lineSeparator());
       System.out.println("Stampo canzoni con proprietario...");
       System.out.println(c.dataToString());
       System.out.println(System.lineSeparator());

       //copia elementi
       file_input =new Scanner ( new BufferedReader (new FileReader ("copy.txt")));
  	  System.out.println("Copio canzoni...");
       while (file_input.hasNext()) {
            String id=file_input.next();
            String psw=file_input.next();
            String str=file_input.next();
            Canzone song=new Canzone(str);
            try {
                c.copy(id, psw, song);
                System.out.println("Utente: " + "<" + id +", " + psw + ">" + " ha copiato: " + song.toString());
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Copia di " + song.toString() + " non riuscita");
            }
       }

       //stampa canzoni con proprietario
       System.out.println(System.lineSeparator());
       System.out.println("Stampo canzoni con proprietario...");
       System.out.println(c.dataToString());
       System.out.println(System.lineSeparator());

       //ottieni
	  System.out.println("Ottengo canzoni...");
       file_input =new Scanner ( new BufferedReader (new FileReader ("get.txt")));
       while (file_input.hasNext()) {
            String id=file_input.next();
            String psw=file_input.next();
            String str=file_input.next();
            Canzone song=new Canzone(str);
            try {
                Canzone s=c.get(id, psw, song);
                get.add(s);
                System.out.println("Utente: " + "<" + id +", " + psw + ">" + " ha ottenuto: " + song.toString());
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Get di " + song.toString() + " non riuscita");
            }
       }
       //stampa lista ottenuti
	  System.out.println(System.lineSeparator());
	  System.out.println("Stampo lista canzoni ottenute...");
       for(Canzone s:get)
          System.out.println(s.toString());

	  System.out.println(System.lineSeparator());
       //condividi elementi
	  System.out.println("Condivido canzoni...");
       file_input =new Scanner ( new BufferedReader (new FileReader ("share.txt")));
       while (file_input.hasNext()) {
            String id=file_input.next();
            String psw=file_input.next();
            String other=file_input.next();
            String str=file_input.next();
            Canzone song=new Canzone(str);
            try {
                c.share(id, psw, other, song);
                System.out.println("Utente: " + "<" + id +", " + psw + ">" + " ha condiviso: " + song.toString() + "con : " + other);
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Condivisione di " + song.toString() + " non riuscita");
            }
      }

      //stampa canzoni con proprietario e utenti che ne hanno accesso
	 System.out.println(System.lineSeparator());
 	 System.out.println("Stampo canzoni con proprietario e utenti che ne hanno accesso...");
	 System.out.println(c.dataToString());
      System.out.println(System.lineSeparator());

      //Rimuovi elementi
	 System.out.println("Elimino canzoni...");
      file_input =new Scanner ( new BufferedReader (new FileReader ("remove.txt")));
      while (file_input.hasNext()) {
            String id=file_input.next();
            String psw=file_input.next();
            String str=file_input.next();
            Canzone song=new Canzone(str);
            try {
                Canzone s=c.remove(id, psw, song);
                removed.add(s);
                System.out.println("Utente: " + "<" + id +", " + psw + ">" + " ha rimosso: " + song.toString());
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Rimozione di " + song.toString() + " non riuscita");
            }
     }
     System.out.println(System.lineSeparator());
     System.out.println("Stampo lista canzoni eliminate...");
     for(Canzone s:removed)
        System.out.println(s.toString());

	   //stampa canzoni con proprietario e utenti che ne hanno accesso
     System.out.println(System.lineSeparator());
     System.out.println("Stampo canzoni con proprietario e utenti che ne hanno accesso...");
     System.out.println(c.dataToString());
     System.out.println(System.lineSeparator());

     //crea iteratore su <utente1, ciao'>
     System.out.println("Creo iteratore su <utente1, ciao'>");
     Iterator <Canzone> it = c.getIterator("utente1", "ciao");
     System.out.println("Stampo iteratore:");
     while(it.hasNext())
         System.out.println(it.next());
     System.out.println("Dimensione container: " + c.getDim());
     }
}
