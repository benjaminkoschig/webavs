ALTER TABLE SCHEMA.CACPTAP
ADD COLUMN EBILLID VARCHAR(20);

ALTER TABLE SCHEMA.CACPTAP
ADD COLUMN EBILLMAIL VARCHAR(50);

REORG TABLE SCHEMA.CACPTAP;

ALTER TABLE SCHEMA.CASECTP
ADD COLUMN EBILLTRANSACTIONID VARCHAR(50);

ALTER TABLE SCHEMA.CASECTP
ADD COLUMN EBILLETAT VARCHAR(1);

ALTER TABLE SCHEMA.CASECTP
ADD COLUMN EBILLERREUR VARCHAR(50);

REORG TABLE SCHEMA.CASECTP;

-- S200701_007 e-Bill: Proprietés générales/globales
    -- True/False activer la fonctionalité eBill
    INSERT INTO SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY)
    VALUES ('osiris.eBill.activer', 'false',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user,
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
    -- ID eBill de la caisse (valeur numerique de 15 caracteres, a valider lors de l'access)
    INSERT INTO SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY)
    VALUES ('osiris.eBill.numero.BillerID', '',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user,
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
    -- email(s) avec separateurs [";" "," ":"] pour les traitement de processus automatiques eBill
    INSERT INTO SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY)
    VALUES ('osiris.eBill.email.traitements', '',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user,
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

-- S200701_007 e-Bill: Proprietés SFTP dossiers INBOX et OUTBOX
    -- Dossier INBOX
    INSERT INTO SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY)
    VALUES ('osiris.eBill.dossier.Inbox', 'ebill/inbox',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user,
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

    -- Dossier OUTBOX
    INSERT INTO SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY)
    VALUES ('osiris.eBill.dossier.Outbox', 'ebill/outbox',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user,
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

    -- Key passphrase
    INSERT INTO SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY)
    VALUES ('osiris.eBill.key.passphrase', '',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user,
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

    -- Ftp Host
    INSERT INTO SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY)
    VALUES ('osiris.eBill.ftp.host', '',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user,
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

    -- Ftp Login
    INSERT INTO SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY)
    VALUES ('osiris.eBill.ftp.login', '',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user,
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

    -- Ftp Port
    INSERT INTO SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY)
    VALUES ('osiris.eBill.ftp.port', '',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user,
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

    -- Known hosts file
    INSERT INTO SCHEMA.JADEPROP (PROPNAME, PROPVAL, CSPY, PSPY)
    VALUES ('osiris.eBill.known.hosts', '',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user,
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);

-- S200701_007 e-Bill: Caisses eBill valides
    -- Clean up
    DELETE FROM SCHEMA.FWPARP where PPACDI like '%EBILLACNT%';
    -- FER-CIAM "106.1"
    INSERT INTO FWPARP (PPARAP,PPACDI,PPADDE,PPARIA,PPRADE,PPARVA,PSPY)
    VALUES ('OSIRIS','EBILLACNT',20210101,1,'Account eBill : FER-CIAM',
            'F3tLiNQ39EMiglURKUc/LQ==',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
    -- FER-CIFA "106.2"
    INSERT INTO FWPARP (PPARAP,PPACDI,PPADDE,PPARIA,PPRADE,PPARVA,PSPY)
    VALUES ('OSIRIS','EBILLACNT',20210101,2,'Account eBill : FER-CIFA',
            'Oc2Zg2bliCg+ZUUczZ0l3A==',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
    -- FER-CIGA "106.3"
    INSERT INTO FWPARP (PPARAP,PPACDI,PPADDE,PPARIA,PPRADE,PPARVA,PSPY)
    VALUES ('OSIRIS','EBILLACNT',20210101,3,'Account eBill : FER-CIGA',
            'FMb1OOHVlvg2FbZbJTNfdw==',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
    -- FER-CIAN "106.4"
    INSERT INTO FWPARP (PPARAP,PPACDI,PPADDE,PPARIA,PPRADE,PPARVA,PSPY)
    VALUES ('OSIRIS','EBILLACNT',20210101,4,'Account eBill : FER-CIAN',
            'b5+ROXLS6y65wb2BMZwROw==',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
    -- FER-CIAB "106.5"
    INSERT INTO FWPARP (PPARAP,PPACDI,PPADDE,PPARIA,PPRADE,PPARVA,PSPY)
    VALUES ('OSIRIS','EBILLACNT',20210101,5,'Account eBill : FER-CIAB',
            'wDWhwRTW5wRIdBZB4/gKgQ==',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
    -- FPV "110"
    INSERT INTO FWPARP (PPARAP,PPACDI,PPADDE,PPARIA,PPRADE,PPARVA,PSPY)
    VALUES ('OSIRIS','EBILLACNT',20210101,6,'Account eBill : FPV',
            '/5dTEpvWANekPcwBJxxK/Q==',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
    -- CVCI "109"
    INSERT INTO FWPARP (PPARAP,PPACDI,PPADDE,PPARIA,PPRADE,PPARVA,PSPY)
    VALUES ('OSIRIS','EBILLACNT',20210101,7,'Account eBill : CCVI',
            'ZI/ToYFd7nssy3jANdD6Kg==',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
    -- CICICAM "59"
    INSERT INTO FWPARP (PPARAP,PPACDI,PPADDE,PPARIA,PPRADE,PPARVA,PSPY)
    VALUES ('OSIRIS','EBILLACNT',20210101,8,'Account eBill : CICICAM',
            'SdoyhXoHwBxj0xnkY3h1Ew==',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
    -- NODE AVS "61"
    INSERT INTO FWPARP (PPARAP,PPACDI,PPADDE,PPARIA,PPRADE,PPARVA,PSPY)
    VALUES ('OSIRIS','EBILLACNT',20210101,9,'Account eBill : NODE AVS',
            'QMM8ff1ldJMrH1JIC1szBg==',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
    -- CCVD "22"
    INSERT INTO FWPARP (PPARAP,PPACDI,PPADDE,PPARIA,PPRADE,PPARVA,PSPY)
    VALUES ('OSIRIS','EBILLACNT',20210101,10,'Account eBill : CCVD',
            'NQhBwJnxsp5nhlOBIqrXtw==',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
    -- CCVS "23"
    INSERT INTO FWPARP (PPARAP,PPACDI,PPADDE,PPARIA,PPRADE,PPARVA,PSPY)
    VALUES ('OSIRIS','EBILLACNT',20210101,11,'Account eBill : CCVS',
            'HThu1+nro1IrbFfUzfp1rA==',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);
    -- Meroba-Genève "111"
    INSERT INTO FWPARP (PPARAP,PPACDI,PPADDE,PPARIA,PPRADE,PPARVA,PSPY)
    VALUES ('OSIRIS','EBILLACNT',20210101,12,'Account eBill : Meroba',
            'K8kT/2vfx0gBUxLJFMa+xA==',
            VARCHAR_FORMAT((current date), 'yyyymmdd') concat replace(char(current time), '.', '') concat user);


-- Création de la table des fichiers d'inscription
CREATE TABLE schema.CAEFI (
	ID_FICHIER NUMERIC(9,0) NOT NULL,
	NOM_FICHIER VARCHAR (40),
	STATUT_FICHIER NUMERIC(1, 0),
	DATE_LECTURE NUMERIC(8, 0),
	PSPY CHAR(24),
	PRIMARY KEY(ID_FICHIER));

-- Création de la table des inscriptions
CREATE TABLE schema.CAEI (
    ID_INSCRIPTION NUMERIC(9,0) NOT NULL,
    ID_FICHIER NUMERIC(9,0) NOT NULL,
	EBILL_ACCOUNT_ID VARCHAR(20) NOT NULL,
	NUMERO_AFFILIE VARCHAR (40),
	TYPE NUMERIC(1, 0),
	NOM VARCHAR (40),
	PRENOM VARCHAR (40),
	ENTREPRISE VARCHAR (40),
	CONTACT VARCHAR (40),
	ROLE_PARITAIRE VARCHAR (40),
	ROLE_PERSONNEL VARCHAR (40),
	ADRESSE_1 VARCHAR (40),
	ADRESSE_2 VARCHAR (40),
	STATUT NUMERIC(1, 0),
	NPA NUMERIC(9, 0),
	LOCALITE VARCHAR (40),
	TEXTE_ERREUR_INTERNE VARCHAR (255),
	EMAIL VARCHAR (40),
	NUM_TEL VARCHAR (40),
	NUM_ADHERENT_BVR VARCHAR (40),
	NUM_REF_BVR VARCHAR (40),
	PSPY CHAR(24),
	PRIMARY KEY(ID_INSCRIPTION));

-- Création de la table des fichiers de traitement
CREATE TABLE schema.CAEFT (
	ID_FICHIER NUMERIC(9,0) NOT NULL,
	NOM_FICHIER VARCHAR (40),
	STATUT_FICHIER NUMERIC(1, 0),
	DATE_LECTURE NUMERIC(8, 0),
	NB_ELEMENTS NUMERIC(8,0),
	NB_ELEMENTS_TRAITES NUMERIC(8,0),
	NB_ELEMENTS_EN_ERREURS NUMERIC(8,0),
	NB_ELEMENTS_REJETES NUMERIC(8,0),
	PSPY CHAR(24),
	PRIMARY KEY(ID_FICHIER));

-- Création de la table des traitements
CREATE TABLE schema.CAET (
    ID_TRAITEMENT NUMERIC(9,0) NOT NULL,
    ID_FICHIER NUMERIC(9,0) NOT NULL,
	EBILL_ACCOUNT_ID VARCHAR (20),
	NUMERO_AFFILIE VARCHAR (40),
	NOM VARCHAR (40),
	PRENOM VARCHAR (40),
	ENTREPRISE VARCHAR (40),
	STATUT NUMERIC(1, 0),
	ETAT NUMERIC(1, 0),
	CODE_ERREUR VARCHAR (40),
	TEXTE_ERREUR VARCHAR (255),
	TEXTE_ERREUR_INTERNE VARCHAR (255),
	NUM_ADHERENT_BVR VARCHAR (40),
	NUM_REF_BVR VARCHAR (40),
	TRANSACTION_ID VARCHAR (40),
	MONTANT_TOTAL VARCHAR (40),
	DATE_TRAITEMENT VARCHAR (40),
	PSPY CHAR(24),
	PRIMARY KEY(ID_TRAITEMENT));