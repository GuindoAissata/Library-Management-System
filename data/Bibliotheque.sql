-- TABLE ADHERENT
CREATE TABLE IF NOT EXISTS Adherent (
    id_adherent INTEGER PRIMARY KEY AUTOINCREMENT,
    nom TEXT NOT NULL,
    prenom TEXT NOT NULL,
    email TEXT NOT NULL,
    UNIQUE(email)
);

-- TABLE DOCUMENT
CREATE TABLE IF NOT EXISTS Document (
    id_document INTEGER PRIMARY KEY AUTOINCREMENT,
    titre TEXT NOT NULL,
    editeur TEXT NOT NULL,
    anneePublication INTEGER NOT NULL,
    genre TEXT NOT NULL,
    disponible INTEGER DEFAULT 1 NOT NULL
);

-- TABLE EMPRUNT
CREATE TABLE IF NOT EXISTS Emprunt (
    id_emprunt INTEGER PRIMARY KEY AUTOINCREMENT,
    id_adherent INTEGER NOT NULL,
    id_document INTEGER NOT NULL,
    date_emprunt TEXT NOT NULL,
    date_retour_prevue TEXT NOT NULL,
    date_retour_reelle TEXT,
    FOREIGN KEY (id_adherent) REFERENCES Adherent(id_adherent) ON DELETE CASCADE,
    FOREIGN KEY (id_document) REFERENCES Document(id_document) ON DELETE CASCADE
);

-- INSERER DES ADHERENTS
INSERT OR IGNORE INTO Adherent(nom, prenom, email) VALUES
('GUINDO', 'Aissata','aissata@dauphine.eu'),
('PHICHITH', 'Nouannapha', 'nouannapha@dauphine.eu');

-- INSERER DES DOCUMENTS
INSERT OR IGNORE INTO Document (titre, editeur, anneePublication, genre, disponible) VALUES
('Le Petit Prince', 'Gallimard', 1943, 'Livre', 1),
('National Geographic n°482', 'National Geographic Society', 2020, 'Magazine', 1),
('1984', 'Secker & Warburg', 1949, 'Livre', 1);

-- INSERER DES EMPRUNTS
INSERT INTO Emprunt (id_adherent, id_document, date_emprunt, date_retour_prevue, date_retour_reelle) VALUES
(1, 1, '2025-10-01', '2025-11-01', NULL),
(2, 3, '2024-09-05', '2024-10-05', '2025-10-01');
