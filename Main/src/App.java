import Model.Adherent;
import Model.BibliothequeManager;
import Model.Emprunt;
import Model.Livre;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Adherent personne1 = new Adherent("Guindo", "Aissata", "aissataguindo@dauphine.eu","060606");
        Livre livre1 = new Livre(0, "Alice au pays des merveille", "Lewis Carrol", "fdfef",192);
        Emprunt emp1 = new Emprunt(personne1, livre1);
        personne1.AddEmpruntAdherent(emp1);
        personne1.AddEmpruntAdherent(emp1);
        personne1.setNb_Emprunt_Encours();
        BibliothequeManager b = new BibliothequeManager();
        b.AddAdherent(personne1);
        System.out.println( b.toString() + " N: B" +personne1.getNb_Emprunt_Encours());
        personne1.setNom("Oumou");
        System.out.println( b.toString() + " N: B" +personne1.getNb_Emprunt_Encours());

    }
}
