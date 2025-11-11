package sgeb.service;

import java.util.List;

import sgeb.exception.AdherentException;
import sgeb.model.Adherent;
import sgeb.model.Document;
import sgeb.model.Emprunt;

import sgeb.dao.AdherentDAO;
import sgeb.model.Adherent;


import java.util.ArrayList;

public class BibliothequeManager {
    //private List<Document> listeDocuments; // La liste de tous les documents disponibles dans la bibliotheque
    //private List<Adherent> listeAdherents; // Celle des adherents
    //private List<Emprunt> listeEmprunts; // Celle des emprunts en cours
    private final int NB_MAX_EMPRUNTS = 5; // Au max, un adherent peut emprunter 5 documents en meme temps

    private AdherentDAO listeAdherentsDAO = new AdherentDAO();


    public BibliothequeManager() {
        //this.listeDocuments = new ArrayList<>();
        //this.listeAdherents = new ArrayList<>();
        //this.listeEmprunts = new ArrayList<>();
    }

    // Ajouter un nouvel adhérent (homonymes et meme adresse autorisés)
    public void ajoutAdherent(Adherent adherent) throws AdherentException {

        for (Adherent a : listeAdherentsDAO.getlisteAdherents()) {
            if (a.getId_adherent() == adherent.getId_adherent()) {
                throw new AdherentException("Un adhérent avec cet ID existe déjà, il ne peut pas être ajouté !");
            }

            // Même nom, même prénom ? Homonymes autorisés

            // Même nom, même prénom, même adresse ? Pas autorisé (ça semble être la même personne)
            if (a.getNom().equalsIgnoreCase(adherent.getNom()) &&
                a.getPrenom().equalsIgnoreCase(adherent.getPrenom())) {
                throw new AdherentException("Un adhérent avec le même nom, prénom et adresse existe déjà.");
            }

            // Même nom, même prénom, même téléphone ? Pas autorisé (ça semble être la même personne aussi)
            if (a.getNom().equalsIgnoreCase(adherent.getNom()) &&
                a.getPrenom().equalsIgnoreCase(adherent.getPrenom()) &&
                a.getTelephone().equalsIgnoreCase(adherent.getTelephone())) {
                throw new AdherentException("Un adhérent avec le même nom, prénom et numéro de téléphone existe déjà.");
            }

            // Même email ? Pas autorisé
            if (a.getEmail().equalsIgnoreCase(adherent.getEmail())) {
                throw new AdherentException("Un adhérent avec cette adresse email existe déjà.");
            }

            // Même téléphone ? Pas autorisé
            if (a.getTelephone().equalsIgnoreCase(adherent.getTelephone())) {
                throw new AdherentException("Un adhérent avec ce numéro de téléphone existe déjà.");
            }

        }            

        // Tout semble bon, on peut ajouter l'adhérent
        listeAdherentsDAO.add(adherent);
    }


    // Rechercher un adhérent par son id
    public Adherent rechercherAdherentParId(int id_adherent) throws AdherentException {
        if(id_adherent <= 0){
            throw new AdherentException("L'ID de l'adhérent ne peut pas être négatif !");
        }
        for (Adherent a : listeAdherentsDAO.getlisteAdherents()) { // sinon, on parcourt la liste de tous les adhérents
            if (a.getId_adherent() == id_adherent) {
                return a;
            }
        }
        
        throw new AdherentException("Aucun adhérent trouvé avec cet ID.");
    }

    // Rechercher un adhérent par son nom (homonymes possibles donc retourne une liste)
    public List<Adherent> rechercherAdherentParNom(String nom) throws AdherentException {
        if (nom == null || nom.trim().isEmpty()){
            throw new AdherentException("Le nom de l'adhérent ne peut pas être vide.");
        }
        nom = nom.trim().toLowerCase(); // bien formater pour que la recherche soit correcte
        List<Adherent> resultats = new ArrayList<>();

        for (Adherent a : listeAdherentsDAO.getlisteAdherents()) {
            if (a.getNom().equalsIgnoreCase(nom)) {
                resultats.add(a);
            }
        }
        return resultats;
    }

    // Modifier les informations d'un adhérent

    // NOM
    public void modifierNomAdherent(Adherent adherent, String nouveauNom) throws AdherentException {
        if (nouveauNom == null || nouveauNom.trim().isEmpty()) {
            throw new AdherentException("Le nom ne peut pas être vide.");
        }
        nouveauNom = nouveauNom.trim().replaceAll("\\s+", " "); // bien formater le nom pour l'enregistrer
        adherent.setNom(nouveauNom); // la méthode setNom de la classe Adherent fait déjà les vérifications nécessaires
        
    }

    // PRENOM
    public void modifierPrenomAdherent(Adherent adherent, String nouveauPrenom) throws AdherentException {
        if (nouveauPrenom == null || nouveauPrenom.trim().isEmpty()) {
            throw new AdherentException("Le prénom ne peut pas être vide.");
        }
        nouveauPrenom = nouveauPrenom.trim().replaceAll("\\s+", " ");
        adherent.setPrenom(nouveauPrenom); 
    }

    // EMAIL
    public void modifierEmailAdherent(Adherent adherent, String nouvelEmail) throws AdherentException {
        if (nouvelEmail == null || nouvelEmail.trim().isEmpty()) {
            throw new AdherentException("L'email ne peut pas être vide.");
        }
        nouvelEmail = nouvelEmail.trim().replaceAll("\\s+", " ");
        for (Adherent a : listeAdherentsDAO.getlisteAdherents()) { // vérifier que l'email n'est pas déjà utilisé
            if (a != adherent) { // ne pas comparer avec lui-même
                if (a.getEmail().equalsIgnoreCase(nouvelEmail)) {
                    throw new AdherentException("Un adhérent avec cet email existe déjà.");
                }
            }
        }
        // Si pas de doublons, on peut rajouter, à condition que l'email soit valide (setEmail le vérifie)
        adherent.setEmail(nouvelEmail); 
    }
   
    // TELEPHONE
    public void modifierTelephoneAdherent(Adherent adherent, String nouveauTelephone) throws AdherentException {
        if (nouveauTelephone == null || nouveauTelephone.trim().isEmpty()) {
            throw new AdherentException("Le numéro de téléphone ne peut pas être vide.");
        }for (Adherent a : listeAdherentsDAO.getlisteAdherents()) { // vérifier que le téléphone n'est pas déjà utilisé
            if (a != adherent) { // ne pas comparer avec lui-même
                if (a.getTelephone().equalsIgnoreCase(nouveauTelephone)) {
                    throw new AdherentException("Un adhérent avec ce numéro de téléphone existe déjà.");
                }
            }
        }
        nouveauTelephone = nouveauTelephone.trim();
        adherent.setTelephone(nouveauTelephone); 
    }



    // VERIFIER SI UN ADHERENT A DES PENALITES
    public boolean adherentAvecPenalites(Adherent adherent) {
        if (adherent.getMontantPenalite() > 0) {
            return true;
        }
        return false;
    } 







    /*
    // Ajouter un document à la bibliothèque
    public void ajoutDocument(Document document) {
        if (document == null){
            throw new IllegalArgumentException("Le document ne peut pas être null.");
        }
        for (Document doc : listeDocuments) {
            if (doc.equals(document)) { // Document déjà présent 
                throw new IllegalArgumentException("Ce document existe déjà dans la bibliothèque.");
            }
        }
        listeDocuments.add(document);
        System.out.println("Document ajouté avec succès : " + document.getTitre());
    }


    // Rechercher des documents par titre
    public List<Document> rechercherDocumentParTitre(String titre){
        if (titre == null || titre.trim().isEmpty()){
            throw new IllegalArgumentException("Le titre ne peut pas être null ou vide.");
        }
        String[] titre_tokens = titre.toLowerCase().trim().split("\\s+"); // pour éviter les problèmes d'espaces ou de casse et transformation en token

        List<Document> resultats = new ArrayList<>(); // la méthode va renvoyer une liste de documents
        for (Document document : listeDocuments){
            String titreDoc = document.getTitre().toLowerCase().trim();
            boolean tousTokentrouves = true;
            for (String mot : titre_tokens){ 
                if (!(titreDoc.contains(mot))){ // si un des tokens n'est pas trouvé dans le titre du document
                    tousTokentrouves = false; 
                    break; // pas besoin de continuer à chercher dans ce titre
                }
            }

            if (tousTokentrouves){
                resultats.add(document); // on ajoute le document aux résultats
            }
        }
        return resultats;
    }

    // Rechercher des documents par auteur
    public List<Document> rechercherDocumentParAuteur(String auteur){
        if (auteur == null || auteur.isEmpty()){
            throw new IllegalArgumentException("L'auteur ne peut pas être null ou vide.");
        }
        List<Document> resultats = new ArrayList<>(); 
        for (Document document : listeDocuments) {
            if (document instanceof sgeb.model.Livre) {
                sgeb.model.Livre livre = (sgeb.model.Livre) document;
                if (livre.getAuteur().equalsIgnoreCase(auteur)) {
                    resultats.add(document);
                }
            }
        }
        return resultats;
    }


    // Supprimer un document
    public void supprimerDocument(Document document) {
        listeDocuments.remove(document);
    }

    // Supprimer un adhérent (et ses emprunts éventuels)
    public void supprimerAdherent(Adherent adherent) {
        // supprimer les emprunts liés
        listeEmprunts.removeIf(e -> e.getAdherent().equals(adherent));
        listeAdherents.remove(adherent);
    }

    // Ajouter un emprunt si possible
    public void ajoutEmprunt(Emprunt emprunt) {
        Document doc = emprunt.getDocument();
        Adherent adh = emprunt.getAdherent();

        // Vérifier disponibilité du document
        if (!doc.isDisponible()) {
            System.out.println("Document non disponible pour emprunt.");
            return;
        }

        // Vérifier le nombre d'emprunts en cours pour cet adhérent
        long count = listeEmprunts.stream().filter(e -> e.getAdherent().equals(adh)).count();
        if (count >= NB_MAX_EMPRUNTS) {
            System.out.println("L'adhérent a atteint le nombre maximal d'emprunts.");
            return;
        }

        listeEmprunts.add(emprunt);
        doc.setDisponible(false);
    }

    // Retourner un emprunt
    public void retournerEmprunt(Emprunt emprunt) {
        Document doc = emprunt.getDocument();
        doc.setDisponible(true);
        listeEmprunts.remove(emprunt);
    }
        */
    
}
