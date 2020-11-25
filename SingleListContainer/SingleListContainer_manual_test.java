import java.util.Iterator;
import java.util.Scanner;

public class SingleListContainer_manual_test {
    private static Scanner sc = new Scanner(System.in);
    private static String S=System.lineSeparator();
    private static String id;
    private static String psw;
    private static String clear=S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S+S;
    private static String menu = "Operazioni disponibili: " + S +
	 "0: Visualizza menù operazioni" + S +
      "1: Aggiungi nuovo utente" + S + //void createUser
      "2: Restituisci il numero degli elementi di Owner" + S + //int get
      "3: Inserisci il dato con proprietario Owner nella collezione" + S + //E put
      "4: Restituisci una copia del dato nella collezione" + S + //E get
      "5: Elimina il dato dalla collezione" + S + //E remove
      "6: Copia il dato nella collezione" + S +  //void copy
      "7: Owner condivide il dato con l'utente Other"  + S +  //void share
      "8: Restituisci un iteratore che stampa tutti i dati dell’utente" + S +  //Iterator<E> getIterator
      "9: Stampa solo utenti" + S + //usersToString
      "10: Stampa solo dati" + S + //Only_E_toString
      "11: Stampa dati con proprietario e lista di utenti che hanno accesso" + S + //dataToString
      "12: Stampa Container" + S + //toString
      "13: Clear" + S +
	 "14: Restituisci dimensione Container" + S +
	 "15: Termina programma";

    static void InsertCredenzialiUtente(){
        System.out.print("Inserisci 'id': ");
        id = sc.nextLine();
        System.out.print("Inserisci 'password': ");
        psw = sc.nextLine();
    }

    public static void main(String[] args) throws AlreadyInException, UserNotInException, DataNotInException {
        String title;
        System.out.println("Per questo test il Container conterrà oggetti di tipo 'Foto'");
        //creazione Container vuoto di foto
        SingleListContainer<Foto> c = new SingleListContainer<>();
        //Stampa menu operazioni
        System.out.println(S + menu);
        //finché non si decide di terminare, richiede operazioni
        for (;;) {
            System.out.print(S + "Digitare uno dei precedenti numeri: ");
            //decisione operazione
            int operation = -1;
		  //finché non si inserisce un numero, continua a chiederlo
		  boolean ok=false;
		  while (!ok) {
		       try {
		            operation=sc.nextInt();
		            ok=true;
	  	       } catch (Exception e) {
		            System.out.print("Inserire un intero: ");
		            sc.nextLine();
                 }
		 }
	      sc.nextLine();//necessario perché il prossimo nextLine prenderebbe blank spaces
           //controlla quale operazione si è scelta
           switch (operation) {
                case 0: {
                     //stampa del menu
                     System.out.print(menu);
                     break;
                }
                case 1: {
                    //inserzione di un nuovo utente
                    System.out.println("1: Aggiungi nuovo utente");
                    //inserisce i dati dell'utente
                    InsertCredenzialiUtente();
                    try {
                        c.createUser(id, psw);
                        System.out.println("Inserzione utente riuscita");
                    } catch (Exception e) {
                        System.out.println(e);
                        System.out.println("Inserzione utente non riuscita");
                    }
                    break;
                }
                case 2: {
			     //se Container è vuoto richiede operazione
			     if(c.getDim()==0) {
			         System.out.println("Container vuoto");
			         break;
			 }
                    //restituzione del numero di elementi di un utente
                    System.out.println("2: Restituisci il numero degli elementi di Owner");
                    //inserisce i dati dell'utente
                    InsertCredenzialiUtente();
                    try{
                         int r=c.getSize(id, psw);
                         System.out.print("L'utente: " + "' " + id +  " ' ha" + " " + r + " elementi");
                    } catch (Exception e) {
                         System.out.println(e);
                         System.out.println("Operazione non riuscita");
                    }
                    break;
               }
               case 3: {
			    //se Container è vuoto richiede operazione
                   if(c.getDim()==0) {
                       System.out.println("Container vuoto");
                       break;
                   }
                  //Inserzione di un dato di tipo E con proprietario Owner nella collezione
                  System.out.println("3: Inserisci il dato con proprietario Owner nella collezione");
                  //inserisce i dati dell'utente
                  InsertCredenzialiUtente();
	             System.out.print("Titolo: ");
                  title=sc.nextLine();
                  try {
                      boolean put=c.put(id, psw, new Foto(title));
                      System.out.println("Inserzione riuscita");
                  } catch (Exception e) {
                      System.out.println(e);
                      System.out.println("Inserzione non riuscita");
                  }
                  break;
               }
               case 4: {
			     //se Container è vuoto richiede operazione
                    if(c.getDim()==0) {
  			          System.out.println("Container vuoto");
  					break;
  		          }
                    //Restituisce una copia del dato nella collezione
                    System.out.println("4: Restituisci una copia del dato nella collezione");
                    //inserisce i dati dell'utente
                    InsertCredenzialiUtente();
                    System.out.print("Titolo: ");
                    title=sc.nextLine();
                    try {
                        Foto r=c.get(id, psw, new Foto(title));
                        System.out.println("Dato restituito "+ r.toString());
                    } catch (Exception e) {
                        System.out.println(e);
                        System.out.println("Restituzione dato non riuscita");
                    }
                    break;
                }
                case 5: {
			     //se Container è vuoto richiede operazione
                    if(c.getDim()==0) {
                        System.out.println("Container vuoto");
                        break;
                    }
                    //eliminazione di tutte le occorrenze di un elemento
                    System.out.println("5: Elimina il dato dalla collezione");
                    //inserisce i dati dell'utente
                    InsertCredenzialiUtente();
                    System.out.print("Titolo: ");
                    title=sc.nextLine();
                    try {
                        Foto r = c.remove(id, psw, new Foto(title));
                        System.out.println("Dato eliminato: " + r.toString());
                    } catch (Exception e) {
                        System.out.println(e);
                        System.out.println("Eliminazione non riuscita");
                    }
                    break;
                }
                case 6: {
				//se Container è vuoto richiede operazione
				if(c.getDim()==0){
				    System.out.println("Container vuoto");
				    break;
		          }
                    //copia di un dato
                    System.out.println("6: Copia il dato nella collezione");
                    //inserisce i dati dell'utente
                    InsertCredenzialiUtente();
                    System.out.print("Titolo: ");
                    title=sc.nextLine();
                    try {
                        c.copy(id, psw, new Foto(title));
                        System.out.println("Copia riuscita");
                    } catch (Exception e) {
                        System.out.println(e);
                        System.out.println("Copia non riuscita");
                    }
                    break;
                }
                case 7: {
				//se Container è vuoto richiede operazione
				if(c.getDim()==0){
				    System.out.println("Container vuoto");
				    break;
				}
                    //condivisione di un elemento
                    System.out.println("7: Owner condivide il dato con l'utente Other");
                     //inserisce i dati dell'utente
                    InsertCredenzialiUtente();
                    //inserici utente destinatario
                    System.out.print("Utente destinatario: ");
                    String other=sc.nextLine();
                    System.out.print("Titolo: ");
                    title=sc.nextLine();
                    try {
                        c.share(id, psw, other, new Foto(title));
                        System.out.println("Condivisione riuscita");
                    } catch (Exception e) {
                        System.out.println(e);
                        System.out.println("Condivisione non riuscita");
                    }
                    break;
                }
                case 8: {
                    //se Container è vuoto richiede operazione
				if(c.getDim()==0){
				    System.out.println("Container vuoto");
				    break;
				 }
                    // Restituisce un iteratore che genera tutti i dati dell’utente"
                    System.out.println("8: Restituisci un iteratore che stampa tutti i dati dell’utente");
                    //inserisce i dati dell'utente
                    InsertCredenzialiUtente();
                    try {
                        Iterator <Foto> it = c.getIterator(id,  psw);
                        System.out.println("Stampo elementi di: " + id);
                        while(it.hasNext())
                            System.out.println(it.next());
                    } catch (Exception e) {
                        System.out.println(e);
                        System.out.println("Qualcosa è andato storto..");
                    }
                    break;

                }
                case 9: {
                    //stampa solo utenti
                    System.out.println("Stampa users");
                    System.out.println(c.usersToString());
                    break;
                }
                case 10: {
                    //stampa solo oggetti E
                    System.out.println("Stampa generici");
                    System.out.println(c.Only_E_ToString());
                    break;
                }
                case 11: {
                    //stampa solo dati con propr.
                    System.out.println("Stampa dati");
                    System.out.println(c.dataToString());
                    break;
                }
                case 12: {
                    //stampa tutti gli elementi del Container
                    System.out.println("Stampa Container");
                    System.out.println(c.toString());
                    break;
                }
			          case 13: {
                    //pulisce interfaccia
                    System.out.println(clear);
                    break;
                }
			          case 14: {
                    //restituisce dimensione
				            int dim=c.getDim();
                    System.out.println(dim);
                    break;
			          }
                default: {
                    System.out.println("Termino programma");
                    return;
                }
            }
        }
    }
}
