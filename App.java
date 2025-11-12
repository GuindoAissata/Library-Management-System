import Model.Adherent;
import Model.Emprunt;
import Model.Livre;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        Adherent personne1 = new Adherent("Guindo", "Aissata", "aissataguindo@dauphine.eu",null);
        Livre livre1 = new Livre(0, "Alice au pays des merveille", "Lewis Carrol", "fdfef",192);
        Emprunt emp1 = new Emprunt(personne1, livre1);
    }
}
