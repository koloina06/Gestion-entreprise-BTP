insert into admin(nomAdmin,email,mdp) values ('admin','admin@gmail.com','admin');

insert into unite(nomUnite,etat) values ('m2',0),('m3',0),('fft',0);


insert into maison(typeMaison,descriptions,duree,surface,etat) values ('villa basse','2 chambres,salle de vie,1 salle de bain',120,150,0);
insert into maison(typeMaison,descriptions,duree,surface,etat) values ('villa etages','4 chambres,salle de vie,3 salles de bain',180,300,0);
insert into maison(typeMaison,descriptions,duree,surface,etat) values ('appartements','6 chambres,salle de vie,4 salles de bain',300,800,0);

insert into finition(typeFinition,augmentation,etat) values ('Standard',0,0),('Gold',3,0),('Premium',5,0),('VIP',8,0);


INSERT INTO typeTravaux (code, nomTypeTravaux, idUnite, quantite, prixUnitaire)
VALUES 
    ('004', 'Pose de carrelage', 2, 80, 4000),
    ('005', 'Installation electrique', 3, 15, 5000),
    ('006', 'Peinture interieure', 1, 120, 8000),
    ('007', 'Installation de plomberie', 1, 25, 6000),
    ('008', 'Amenagement paysager', 2, 50, 7000),
    ('009', 'Construction de cloisons', 1, 60, 10000),
    ('010', 'Installation de climatisation', 3, 10, 15000),
    ('011', 'Pose de revetement de toit', 2, 30, 8000),
    ('012', 'Travaux de demolition', 1, 40, 7000),
    ('013', 'Finition de platrerie', 1, 70, 6000),
    ('014', 'Installation de portes', 3, 20, 4000),
    ('015', 'Travaux de terrassement', 1, 90, 5000),
    ('016', 'Revetement exterieur', 2, 40, 9000),
    ('017', 'Installation de systeme de securite', 3, 8, 20000),
    ('018', 'Pose de faux plafonds', 1, 50, 12000);

insert into maisonTravaux(idMaison,idTypeTravaux,etat) values(1,1,0),(1,2,0),(1,3,0),(1,4,0),(1,5,0);
insert into maisonTravaux(idMaison,idTypeTravaux,etat) values(2,6,0),(2,7,0),(2,8,0),(2,9,0),(2,10,0);
insert into maisonTravaux(idMaison,idTypeTravaux,etat) values(3,11,0),(3,12,0),(3,13,0),(3,14,0),(3,15,0);