-- ============================================================
--  SCRIPT DE RESET — Nettoyage complet et réinitialisation
-- ============================================================

USE gestion_stock;

-- Désactiver les contraintes de clé étrangère
SET FOREIGN_KEY_CHECKS = 0;

-- Vider toutes les tables
TRUNCATE TABLE stock;
TRUNCATE TABLE produit;
TRUNCATE TABLE categorie;
TRUNCATE TABLE utilisateur;

-- Réactiver les contraintes
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
--  Insertion des données de test
-- ============================================================

-- Utilisateurs avec mots de passe hashés (BCrypt de "Admin@12345", "Manager@1234", "Employe@1234")
-- Vous pouvez générer des hash BCrypt avec : https://www.browserling.com/tools/bcrypt
-- Pour cet exemple, utilisez des vrais hash ou laissez InitialDataLoader créer les comptes

-- Catégories
INSERT INTO categorie (nom, description) VALUES
('Informatique',     'Ordinateurs, composants et périphériques informatiques'),
('Bureautique',      'Fournitures et équipements de bureau'),
('Électronique',     'Appareils et accessoires électroniques'),
('Mobilier',         'Tables, chaises et rangements de bureau'),
('Consommables',     'Papier, encre, cartouches et articles jetables');

-- Produits
INSERT INTO produit (nom, reference, prix_unitaire, categorie_id) VALUES
('Ordinateur Portable Dell XPS',    'INFO-001', 12500.00, 1),
('Écran 27 pouces Samsung',         'INFO-002',  3200.00, 1),
('Clavier Mécanique Logitech',      'INFO-003',   650.00, 1),
('Souris Sans Fil Microsoft',       'INFO-004',   280.00, 1),
('Disque Dur Externe 1TB',          'INFO-005',   750.00, 1),
('Bureau en L 160cm',               'BURO-001',  2400.00, 2),
('Chaise Ergonomique',              'BURO-002',  1800.00, 2),
('Lampe de Bureau LED',             'BURO-003',   320.00, 2),
('Agenda 2025',                     'BURO-004',    85.00, 2),
('Imprimante HP LaserJet',          'ELEC-001',  4500.00, 3),
('Scanner A4 Canon',                'ELEC-002',  2200.00, 3),
('Téléphone IP Cisco',              'ELEC-003',  1350.00, 3),
('Armoire Métallique 4 Tiroirs',    'MOBI-001',  3600.00, 4),
('Étagère Murale 5 Niveaux',        'MOBI-002',   980.00, 4),
('Ramette Papier A4 (500 feuilles)','CONS-001',    45.00, 5),
('Cartouche Encre Noire HP',        'CONS-002',   220.00, 5),
('Stylos Bille (boîte 50)',         'CONS-003',    95.00, 5),
('Ruban Adhésif (lot 10)',          'CONS-004',    60.00, 5);

-- Stock
INSERT INTO stock (produit_id, quantite, seuil_minimum) VALUES
(1,  15,  3),   -- Dell XPS
(2,  22,  5),   -- Écran Samsung
(3,   4,  5),   -- Clavier
(4,  30, 10),   -- Souris
(5,   2,  5),   -- Disque dur
(6,   8,  2),   -- Bureau en L
(7,   6,  2),   -- Chaise ergo
(8,  12,  4),   -- Lampe LED
(9,   0,  5),   -- Agenda
(10,  5,  2),   -- Imprimante HP
(11,  3,  2),   -- Scanner Canon
(12,  7,  3),   -- Téléphone IP
(13,  4,  2),   -- Armoire
(14, 10,  3),   -- Étagère
(15, 200, 50),  -- Ramette papier
(16,  18, 20),  -- Cartouche HP
(17,  45, 10),  -- Stylos
(18,  30, 10);  -- Ruban adhésif

-- ============================================================
--  NOTES IMPORTANTES
-- ============================================================
-- Les comptes utilisateurs seront créés automatiquement par
-- InitialDataLoader au démarrage de l'application avec les
-- identifiants de test suivants :
--   - admin@test.com / Admin@12345 (ADMIN)
--   - manager@test.com / Manager@1234 (MANAGER)
--   - employe@test.com / Employe@1234 (EMPLOYE)
--
-- Si vous voulez créer les comptes manuellement, utilisez BCrypt
-- pour hasher les mots de passe avant insertion.
-- ============================================================

