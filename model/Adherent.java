package sgeb.model;

import sgeb.exception.AdherentException;

public class Adherent {
    private static int cpt_adherent = 0; // compteur pour générer des ids uniques
    private int id_adherent; // car un adhérent est unique
    private String nom;
    private String prenom;
    private String email; // unique
    private String telephone; // unique
    private double montantPenalite;



    // Constructeur
    public Adherent(String nom, String prenom, String email, String telephone) throws AdherentException {
        cpt_adherent++;
        this.id_adherent = cpt_adherent; // s'incrémente à chaque fois qu'un nouvel adhérent est inscrit

        verificationAdherent(nom, prenom, email, telephone); // vérification des données fournies

        // bien formater les données ici comme ça plus plus tard on est sûr qu'elles sont correctes
        this.nom = nom.trim().replaceAll("\\s+", " "); // enlever les espaces en début/fin et les espaces multiples entre les mots
        this.prenom = prenom.trim().replaceAll("\\s+", " ");
        this.email = email.trim().replaceAll("\\s+", " "); // obligatoire comme ça on peut le contacter 
        this.telephone = telephone.trim(); // obligatoire comme ça on peut le contacter
        this.montantPenalite = 0.0; // Combien il lui reste à payer
    }

    // Getters et Setters

    // id_adherent (pas besoin de set car il ne sera pas changé par la suite)
    public int getId_adherent() {
        return id_adherent;
    }

    // nom
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) throws AdherentException {
        if (!verificationNom(nom)) {
            throw new AdherentException("Format du nom invalide (doit contenir uniquement des lettres)");
        }
        this.nom = nom.trim().replaceAll("\\s+", " ");
    }

    // prenom
    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) throws AdherentException {
        if (!verificationPrenom(prenom)) {
            throw new AdherentException("Format du prénom invalide (doit contenir uniquement des lettres)");
        }
        this.prenom = prenom.trim().replaceAll("\\s+", " ");
    }
    

    // email
    public String getEmail() {
        return email;
    }  

    public void setEmail(String email) throws AdherentException {
        if (!verificationEmail(email)) {
            throw new AdherentException("Format d'email invalide");
        }
        this.email = email.trim().replaceAll("\\s+", " ");
    }

    // telephone
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) throws AdherentException {
        if (!verificationTelephone(telephone)) {
            throw new AdherentException("Format de numéro de téléphone invalide");
        }
        this.telephone = telephone;
    }

    // montant_penalite
    public double getMontantPenalite() {
        return montantPenalite;
    }   

    public void setMontantPenalite(double montant_Penalite) throws AdherentException {
        if (montant_Penalite < 0) {
            throw new AdherentException("Le montant de la pénalité ne peut pas être négatif");
        }
        montantPenalite = montant_Penalite;
    }



    // Méthodes de validation
    private static boolean verificationNom(String nom) {
        // Uniquement des lettres (incluant les accents et tirets)
        return nom != null && !nom.trim().isEmpty() && nom.matches("^[a-zA-ZÀ-ÿ\\s.-]+$");
    }

    private static boolean verificationPrenom(String prenom) {
        // Uniquement des lettres (incluant les accents et tirets)
        return prenom != null && !prenom.trim().isEmpty() && prenom.matches("^[a-zA-ZÀ-ÿ\\s.-]+$");
    }
    
    private static boolean verificationEmail(String email) {
        // Email doit commencer par une lettre, suivi de lettres/chiffres
        String pattern_email = "^[a-zA-Z][a-zA-Z0-9]*@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}$";
        return email.matches(pattern_email);
    }

    private static boolean verificationTelephone(String numero) {
        // Exactement une suite de 10 chiffres qui commence par 0
        String pattern_telephone = "^0\\d{9}$";
        return numero != null && numero.matches(pattern_telephone);
    }

    


    // Redefinition de la methode equals pour comparer deux adherents en fonction de leurs données
    @Override
    public boolean equals(Object adh1) {
        if (this == adh1) {
            return true; // i.e ce sont les memes objets (reference)
        }
        if (adh1 == null || adh1.getClass() != this.getClass()) {
            return false;
        }

        Adherent adh2 = (Adherent) adh1;
        return (email != null && email.equalsIgnoreCase(adh2.getEmail())) ||
            (nom.equalsIgnoreCase(adh2.nom) && prenom.equalsIgnoreCase(adh2.prenom) && telephone.equals(adh2.telephone));
    }


    @Override
    public String toString() {
        return "Adherent [id_adherent=" + id_adherent + ", nom=" + nom + ", prenom=" + prenom
                + ", email=" + email + ", telephone=" + telephone + ", montant penalite=" + montantPenalite + "]";
    }


    // Méthode qui va vérifier si on peut créer un adhérent avec les données fournies
    public static void verificationAdherent(String nom, String prenom, String email, String telephone) throws AdherentException {
        if (!verificationNom(nom)) {
            throw new AdherentException("Format du nom invalide (doit contenir uniquement des lettres, des espaces, des tirets ou des points)");
        }
        if (!verificationPrenom(prenom)) {
            throw new AdherentException("Format du prénom invalide (doit contenir uniquement des lettres, des espaces, des tirets ou des points)");
        }
        if (!verificationEmail(email)) {
            throw new AdherentException("Format d'email invalide");
        }
        if (!verificationTelephone(telephone)) {
            throw new AdherentException("Format de numéro de téléphone invalide");
        }
    }






    
}
