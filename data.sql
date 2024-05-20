--create database evalbtp;

create table admin(
    idAdmin serial primary key,
    nomAdmin varchar(50),
    email varchar(50),
    mdp varchar(50)
);

create table unite(
    idUnite serial primary key,
    nomUnite varchar(20),
    etat int --0 valide -1 supprime
);

create table maison(
    idMaison serial primary key,
    typeMaison varchar(255),
    descriptions text,
    duree double precision,
    surface double precision,
    etat int --0 valide -1 supprime
);

create table finition(
    idFinition serial primary key,
    typeFinition varchar(255),
    augmentation double precision,
    etat int --0 valide -1 supprime
);

create table typeTravaux(
    idTypeTravaux serial primary key,
    code varchar(10),
    nomTypeTravaux varchar(255),
    idUnite int,
    quantite double precision,
    prixUnitaire double precision,
    foreign key(idUnite) references unite(idUnite)
);

create table maisonTravaux(
    idMaisonTravaux serial primary key,
    idMaison int,
    idTypeTravaux int,
    etat int,  --0 valide -1 supprime
    foreign key(idMaison) references maison(idMaison),
    foreign key(idTypeTravaux) references typeTravaux(idTypeTravaux)
);

create table devis(
    idDevis serial primary key,
    refDevis varchar(10),
    dateDevis date,
    idMaison int,
    idFinition int,
    dateDebut date,
    dateFin date,
    numeroClient varchar(10),
    augmentation double precision,
    prixTotalDevis double precision,
    lieu varchar(2553),
    foreign key(idMaison) references maison(idMaison),
    foreign key(idFinition) references finition(idFinition)
);

create table detailsDevis(
    idDetailsDevis serial primary key,
    idDevis int,
    code varchar(10),
    nomTypeTravaux varchar(255),
    idUnite int,
    quantite double precision,
    prixUnitaire double precision,
    prixTotal double precision,
    foreign key(idDevis) references devis(idDevis),
    foreign key(idUnite) references unite(idUnite)
);

create table paiement(
    idPaiement serial primary key,
    refPaiement varchar(255),
    idDevis int,
    datePaiement date,
    montant double precision,
    foreign key(idDevis) references devis(idDevis)
);

create table maisonTravauxTemp(
    id serial primary key,
    numligne int,
    typeMaison varchar(255),
    descriptions varchar(255),
    surface varchar(255),
    codeTravaux varchar(255),
    typeTravaux varchar(255),
    unite varchar(255),
    prixUnitaire varchar(255),
    quantite varchar(255),
    dureetravaux varchar(255)
);

create table devisTemp(
    id serial primary key,
    numligne int,
    client varchar(10),
    refDevis varchar(255),
    typeMaison varchar(255),
    finition varchar(255),
    tauxFinition varchar(50),
    dateDevis varchar(50),
    dateDebut varchar(50),
    lieu varchar(255)
);

