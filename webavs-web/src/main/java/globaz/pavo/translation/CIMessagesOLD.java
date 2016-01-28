package globaz.pavo.translation;

/**
 * Messages de l'application PAVO. Date de cr�ation : (22.10.2002 09:12:46)
 * 
 * @author: Administrator
 */
public interface CIMessagesOLD {
    public static final String MSG_ANNONCE_21_EMAIL_MESSAGE = "Un ordre d'ouverture CI pour l''assur� {0} doit �tre compl�t� avec les diff�rentes p�riodes de splitting.";
    public static final String MSG_ANNONCE_21_EMAIL_SUJET = "Demande de compl�ment des p�riodes de splitting";
    public static final String MSG_ANNONCE_21_RCI_EMAIL_MESSAGE = "Un second RCI doit �tre envoy� pour l''assur� {0} afin de pouvoir ouvrir son CI.";
    public static final String MSG_ANNONCE_21_RCI_EMAIL_SUJET = "Second RCI";
    public static final String MSG_ANNONCE_22_EMAIL_MESSAGE = "Un ordre de cl�ture pour l''assur� {0} a �t� re�u alors qu''une cl�ture est d�j� existante.\n Contenu de l'ordre:\n{1}";
    public static final String MSG_ANNONCE_22_EMAIL_SUJET = "Cl�ture d�j� existante";
    public static final String MSG_ANNONCE_22_SPL_EMAIL_MESSAGE = "Un ordre de splitting pour l''assur� {0} a �t� re�u alors qu''un m�me ordre � d�j� �t� effectu�.\n Contenu de l''ordre:\n{1}";
    public static final String MSG_ANNONCE_22_SPL_EMAIL_SUJET = "Ordre de splitting d�j� existant";
    public static final String MSG_ANNONCE_22B_EMAIL_MESSAGE = "Un ordre de cl�ture pour l''assur� {0} a �t� re�u alors qu''aucun CI n''a �t� trouv�.\n Contenu de l''ordre:\n{1}";
    public static final String MSG_ANNONCE_22B_EMAIL_SUJET = "Num�ro d'assur� non trouv� pour cette cl�ture";
    public static final String MSG_ANNONCE_22B_SPL_EMAIL_MESSAGE = "Un ordre de splitting pour l''assur� {0} a �t� re�u alors qu''aucun CI n''a �t� trouv�.\n Contenu de l''ordre:\n{1}";
    public static final String MSG_ANNONCE_22B_SPL_EMAIL_SUJET = "Num�ro d'assur� non trouv� pour cet ordre de splitting";
    public static final String MSG_ANNONCE_22C_SPL_EMAIL_SUJET = "Demande d'ouverture du CI du conjoint.";
    public static final String MSG_ANNONCE_22R_EMAIL_MESSAGE = "Un ordre de rassemblement pour l''assur� {0} a �t� re�u alors qu''aucun CI n''a �t� trouv�.\n Contenu de l''ordre:\n{1}";
    public static final String MSG_ANNONCE_22R_EMAIL_SUJET = "Num�ro d'assur� non trouv� pour ce rassemblement";
    public static final String MSG_ANNONCE_23_EMAIL_MESSAGE = "Un CI de compl�ment pour l''assur� {0} ou {1} a �t� re�u alors qu''aucun CI n''a �t� trouv�.\nContenu du compl�ment:\n {2} ";
    public static final String MSG_ANNONCE_23_EMAIL_SUJET = "Num�ro d'assur� compl�t� et num�ro d'assur� � compl�ter non trouv� pour ce CI de compl�ment";
    public static final String MSG_ANNONCE_29_CL_EMAIL_MESSAGE = "Un ordre de r�vocation de RCI pour l''assur� {0} a �t� re�u alors qu''aucun CI ou aucune cl�ture n''a �t� trouv�.\n Contenu de l''ordre:\n{1}";
    public static final String MSG_ANNONCE_29_CL_EMAIL_SUJET = "Pas de cl�ture trouv�e pour ce num�ro d'assur�";
    public static final String MSG_ANNONCE_29_SP_EMAIL_MESSAGE = "Un ordre de r�vocation de splitting pour l''assur� {0} a �t� re�u alors qu''aucun CI ou ordre de splitting n''a �t� trouv�.\n Contenu de l''ordre:\n{1}";
    public static final String MSG_ANNONCE_29_SP_EMAIL_SUJET = "Pas d'ordre de splitting trouv� pour ce num�ro d'assur�";
    // CIANNONCESUSPENS
    // message CIAnnonceSuspens._beforeAdd(statement)
    public static final String MSG_ANNONCE_ADD_CA = "Le code application doit �tre sp�cifi�.";
    // message CIAnnonceSuspens._beforeDelete(statement)
    public static final String MSG_ANNONCE_DEL_USER = "Seul le responsable CI peut supprimer ce document.";
    public static final String MSG_ANNONCE_EMAIL_AGENCE = "Agence: ";
    public static final String MSG_ANNONCE_EMAIL_AVS = "Assur�: ";
    public static final String MSG_ANNONCE_EMAIL_CAISSE = "Caisse: ";
    public static final String MSG_ANNONCE_EMAIL_DATE = "Date de cl�ture: ";
    public static final String MSG_ANNONCE_EMAIL_DATE_SP = "Date de l'ordre de splitting: ";
    public static final String MSG_ANNONCE_EMAIL_ERREUR = "PAVO - erreur d'envoi d'email";
    public static final String MSG_ANNONCE_EMAIL_MOTIF = "Motif de l'annonce: ";
    public static final String MSG_ANNONCE_EMAIL_NOM = "Etat nominatif: ";
    public static final String MSG_ANNONCE_EMAIL_PARTENAIRE = "Partenaire: ";
    public static final String MSG_ANNONCE_EMAIL_REF = "R�f�rence interne: ";
    // message CIAnnonceSuspens.traitement21Ouverture();
    public static final String MSG_ANNONCE_EMAIL_SUJET = "Traitement des annonces.";
    // CIAPPLICATION
    // message CIApplication annonceARC()
    public static final String MSG_ANNONCE_INPUT_INVALID = "Une annonce ARC n'a pas pu �tre envoy�e.";
    public static final String MSG_ANNONCE_OUTPUT_INVALID = "Une annonce ARC n'a pas pu �tre lue.";
    // message CICompteIndividuel.annonceCIAssitionnel()
    public static final String MSG_CI_CIADD = "Selon le CI, des �critures devraient �tre cl�tur�es mais la cl�ture n'a pas �t� trouv�e. Mise � jour manuelle n�cessaire.";
    public static final String MSG_CI_EMAIL_MESSAGE = "Un CI provisoire et un au RA a �t� trouv� pour la m�me personne. No avs: ";
    public static final String MSG_CI_EMAIL_SUJET = "Erreur de CI";
    public static final String MSG_CI_ETAT_ACTIVES = "Ecritures actives";
    public static final String MSG_CI_ETAT_TOUS = "Toutes les cl�tures";
    public static final String MSG_CI_PROCESS_MESSAGE23 = "Le compl�ment de CI suivant n''a pas pu �tre effectu�: \n {0}";
    // PROCESS
    // message CICompteIndividuelProcess
    public static final String MSG_CI_PROCESS_SUJET23 = "Compl�ment de CI";
    public static final String MSG_CI_SUPPRESSION = "Impossible de supprimer un CI.";
    public static final String MSG_CI_VAL_AVS = "Le num�ro AVS n'est pas valide.";
    // CICOMPTEINDIVIDUEL
    // message CICompteIndividuel._validate(statement)
    public static final String MSG_CI_VAL_NAISSANCE = "La date de naissance n'est pas valide.";
    public static final String MSG_CI_VAL_NOM = "Le nom et pr�nom de l'assur� doit �tre rensign�.";
    public static final String MSG_CI_VAL_OUVERTURE = "L'ann�e d'ouverture n'est pas valide.";
    public static final String MSG_CI_VAL_PAYS = "Le pays d'origine n'est pas valide.";
    public static final String MSG_CI_VAL_REF = "La r�f�rence interne doit �tre renseign�e.";
    public static final String MSG_CI_VAL_REGISTRE = "Le registre du CI n'est pas renseign�.";
    public static final String MSG_CI_VAL_SEXE = "Le sexe de la personne n'est pas valide.";
    public static final String MSG_COMPTA_JOURNAL_AVS = "La plage des num�ros avs n'est pas correcte.";
    // message CIComptabiliserJournalProcess
    public static final String MSG_COMPTA_JOURNAL_EMAIL = "Adresse email obligatoire.";
    public static final String MSG_COMPTA_JOURNAL_EMAIL_INV = "Adresse email invalide.";
    public static final String MSG_COMPTA_JOURNAL_ID = "No de journal obligatoire.";
    // message CIDomicileSplitting._beforeAdd(transaction)
    public static final String MSG_DOMICILE_ADD_ETAT = "Impossible d'effectuer un ajout dans l'�tat actuel du dossier.";
    // message CIDomicileSplitting._beforeDelete(transaction)
    public static final String MSG_DOMICILE_DEL_ETAT = "Impossible de supprimer un domicile dans l'�tat actuel du dossier.";
    // message CIDomicileSplitting._beforeUpdate(transaction)
    public static final String MSG_DOMICILE_MOD_ETAT = "Impossible d'effectuer une modification dans l'�tat actuel du dossier.";
    public static final String MSG_DOMICILE_VAL_DATE = "La date de fin ne peut pas avoir lieu avant la date de d�but!";
    // CIDOMICILESPLITTING
    // message CIDomicileSplitting._validate(statement)
    public static final String MSG_DOMICILE_VAL_DEBUT = "La date de d�but est incorrecte.";
    public static final String MSG_DOMICILE_VAL_FIN = "La date de fin est incorrecte.";
    public static final String MSG_DOMICILE_VAL_LIBELLE = "Le libell� doit �tre sp�cifi�.";
    public static final String MSG_DOSSIER_ANNONCE = "Une annonce ARC n'a pas pu �tre envoy�e.";
    // message CIDossierSplitting.imprimerApercu()
    public static final String MSG_DOSSIER_APERCU = "Impossible d'imprimer l'aper�u dans l'�tat actuel du dossier.";
    // message CIDossierSplitting._beforeDelete(transaction)
    public static final String MSG_DOSSIER_DEL_ETAT = "Impossible de supprimer le dossier dans l'�tat actuel.";
    public static final String MSG_DOSSIER_DEL_USER = "Seul le responsable de ce dossier ou le reponsable CI peut supprimer ce document.";
    public static final String MSG_DOSSIER_EMAIL_ERREUR = "PAVO - erreur d'envoi d'email";
    public static final String MSG_DOSSIER_EMAIL_MESSAGE_CL = "Le dossier de splitting concernant l''assur� mentionn� ci-dessous a �t� cl�tur�.\n {0}";
    public static final String MSG_DOSSIER_EMAIL_MESSAGE_RE = "Le dossier de splitting concernant l''assur� mentionn� ci-dessous a �t� r�voqu�.\n {0}";
    public static final String MSG_DOSSIER_EMAIL_NO_AVS = "No AVS: ";
    public static final String MSG_DOSSIER_EMAIL_NO_AVSC = "No AVS du conjoint: ";
    public static final String MSG_DOSSIER_EMAIL_NOM = "Nom: ";
    public static final String MSG_DOSSIER_EMAIL_NOMC = "Nom du conjoint: ";
    // message CIDossierSplitting.updateDossier() (email)
    public static final String MSG_DOSSIER_EMAIL_SUJET_CL = "Cl�ture du dossier de splitting";
    public static final String MSG_DOSSIER_EMAIL_SUJET_RE = "R�vocation du dossier de splitting";
    public static final String MSG_DOSSIER_OUVRIR_IMPOSSIBLE = "Le dossier � d�j� �t� ouvert.";
    // CIDOSSIER
    // messages CIDossierSplitting.ouvrir()
    public static final String MSG_DOSSIER_OUVRIR_NOUVEAU = "Le dossier n'a pas encore �t� enregistr�.";
    // message CIDossierSplitting.revoquer()
    public static final String MSG_DOSSIER_REVOQUER = "Impossible de r�voquer le dossier son �tat actuel.";
    // message CIDossierSplitting.rouvrir()
    public static final String MSG_DOSSIER_ROUVRIR = "Impossible de rouvrir le dossier dans l'�tat actuel.";
    // message CIDossierSplitting.splitterMandats()
    public static final String MSG_DOSSIER_SPL_MANDATS = "Ce dossier contient des mandats en demande de r�vocation.";
    // message CIDossierSplitting.executerSplitting()
    public static final String MSG_DOSSIER_SPLITTING = "Impossible d'ex�cuter le splitting dans l'�tat actuel du dossier.";
    public static final String MSG_DOSSIER_UP_ASSURE = "Aucun mandat valide n'a �t� trouv� pour l'assur�.";
    public static final String MSG_DOSSIER_UP_CONJOINT = "Aucun mandat valide n'a �t� trouv� pour le conjoint.";
    // message CIDossierSplitting.updateDossier()
    public static final String MSG_DOSSIER_UPDATE = "Aucun mandat valide n'a �t� trouv� pour ex�cuter le splitting.";
    public static final String MSG_DOSSIER_VAL_CONJOINT = "Le champ Conjoint n'est pas valide.";
    public static final String MSG_DOSSIER_VAL_DATE = "Le divorce ne peut pas avoir eu lieu avant le mariage!";
    public static final String MSG_DOSSIER_VAL_DIVORCE = "La date de divorce est incorrecte.";
    public static final String MSG_DOSSIER_VAL_MARIAGE = "La date de mariage est incorrecte.";
    public static final String MSG_DOSSIER_VAL_OUVERTURE = "La date d'ouverture de dossier est incorrecte.";
    public static final String MSG_DOSSIER_VAL_RESP = "Le responsable du dossier n'est pas sp�cifi� ou n'est inconnu.";
    // message CIDossierSplitting._validate(statement)
    public static final String MSG_DOSSIER_VAL_TIERS = "Le champ Assur� n'est pas valide.";
    public static final String MSG_ECRITURE_ADD_CI_ACCESS = "Ajout �chou�. Erreur dans la tentative d'acc�s au compte individuel associ�.";
    public static final String MSG_ECRITURE_ADD_CI_NEW = "Ajout �chou�. Impossible de cr�er un nouveau CI. ";
    // message CIEcriture._beforeAdd()
    public static final String MSG_ECRITURE_ADD_ETAT = "Ajout/modification non autoris�. Le journal associ� n'est pas ouvert.";
    public static final String MSG_ECRITURE_ADD_REM = "Ajout �chou�. Impossible d'ajouter une nouvelle remarque.";
    public static final String MSG_ECRITURE_AFFILIE = "Impossible de retrouver l'affili� sp�cifi�.";
    public static final String MSG_ECRITURE_AGE = "Si le genre de l'�criture n'est pas splitting (genre 8), l'�ge minimum requis est 17 ans par rapport � l'ann�e de cotisation.";
    public static final String MSG_ECRITURE_ANNEE = "L'ann�e de cotisation doit �tre sup�rieure ou �gale � 1948";
    // message CIEcriture._validate()
    public static final String MSG_ECRITURE_AVS = "Le num�ro d'AVS est obligatoire.";
    public static final String MSG_ECRITURE_BRECO = "La branche �conomique est obligatoire.";
    public static final String MSG_ECRITURE_BRECO_NOT = "La branche �conomique doit �tre vide.";
    public static final String MSG_ECRITURE_CAICHO_NOT = "La caisse ch�mage n'a pas � �tre renseign�e.";
    public static final String MSG_ECRITURE_CAISCHO = "Le type d'inscription du journal est ASSURANCE CHOMAGE et �criture ant�c�dente � 1969. Branche �conomique obligatoire.";
    public static final String MSG_ECRITURE_CAISCHO_FORMAT = "Le num�ro de caisse ch�mage doit comporter au moins 3 chiffres.";
    public static final String MSG_ECRITURE_CI_CLOSED = "Le compte individuel n'est pas ouvert. Le type d'inscription doit �tre SUSPENS. ID du CI: ";
    public static final String MSG_ECRITURE_CLOT = "L'inscription a lieu dans une p�riode cl�tur�e. Le type de compte doit �tre CI ou PROVISOIRE.";
    public static final String MSG_ECRITURE_CODE_A = "Le code A ne peut �tre utilis� que pour une extourne.";
    public static final String MSG_ECRITURE_CODE_D = "L'�criture est de genre 4, le code irrecouvrable doit �tre D.";
    public static final String MSG_ECRITURE_CODE_NOT = "L'�criture est de genre 5 ou 8, le code irrecouvrable doit �tre blanc.";
    public static final String MSG_ECRITURE_CODE_VAL = "Mauvaise valeur du code irrecouvrable.";
    public static final String MSG_ECRITURE_CODSPE4 = "Inscription de genre 4: code sp�cial � 01 ou nul.";
    public static final String MSG_ECRITURE_CODSPE7 = "Inscription de genre 7 � partir de 1997: code sp�cial � 02 ou 03.";
    public static final String MSG_ECRITURE_CODSPE7_NOT = "Le code sp�cial doit �tre vide �tant donn� le genre d'�criture sp�cifi� (et aussi peut-�tre l'ann�e).";
    public static final String MSG_ECRITURE_COMPT_CTRL = "Inscription au CI de l'�criture impossible si le journal contient un total de contr�le.";
    // message CIEcriture.comptabiliser()
    public static final String MSG_ECRITURE_COMPT_TYPECOMPT = "Inscription au CI de l'�criture impossible. Le type de compte associ� � l'�criture n'est pas PROVISOIRE. ID de l'�criture: ";
    public static final String MSG_ECRITURE_COMPT_UPDATE = "Inscription au CI de l'�criture �chou�e. Echec de mise � jour. ID de l'�criture/ID du CI: ";
    public static final String MSG_ECRITURE_CONJ = "Un identifiant valable pour le partenaire est n�cessaire (genre 8).";
    public static final String MSG_ECRITURE_CONJ_IDEN = "Le partenaire ne peut pas �tre identique � l'assur� lui-m�me.";
    public static final String MSG_ECRITURE_DATEDEB = "Mauvaise valeur de date de d�but.";
    public static final String MSG_ECRITURE_DATEDEB_MANDAT = "Le mois de d�but est obligatoire.";
    public static final String MSG_ECRITURE_DATEDEB_NOT = "Le mois de d�but n'a pas � �tre sp�cifi�.";
    public static final String MSG_ECRITURE_DATEDEBFIN = "Sp�cifier une date de fin post�rieure � la date de d�but.";
    public static final String MSG_ECRITURE_DATEFIN = "Mauvaise valeur de date de fin.";
    public static final String MSG_ECRITURE_DATEFIN_MANDAT = "Le mois de fin est obligatoire.";
    public static final String MSG_ECRITURE_DATEFIN_NOT = "Le mois de fin n'a pas � �tre sp�cifi�.";
    public static final String MSG_ECRITURE_DEL_ETAT = "Suppression impossible. Le journal associ� n'est pas ouvert.";
    // message CIEcriture._beforeDelete()
    public static final String MSG_ECRITURE_DEL_TYPCOM = "Suppression impossible. Le type de compte est: ";
    public static final String MSG_ECRITURE_EMP = "Un identifiant valable pour l'employeur est n�cessaire.";
    public static final String MSG_ECRITURE_EMPCONJ_NOT = "L'employeur et le partenaire ne doivent pas �tre renseign�s.";
    public static final String MSG_ECRITURE_EXTOURNE = "En dehors de la p�riode 1969-1975, l'extourne ne peut prendre que les valeurs 0, 1, 8 ou 9.";
    public static final String MSG_ECRITURE_GENRE = "Mauvais genre d'�criture pour le type d'inscription (sp�cifi� au journal).";
    public static final String MSG_ECRITURE_GENRE1 = "Genre 1 non possible pour une personne � la retraite.";
    public static final String MSG_ECRITURE_GENRE7 = "La personne n'est pas � la retra�te lors de la p�riode de l'�criture ou celle-ci se trouve avant la cl�ture. Genre 7 impossible.";
    public static final String MSG_ECRITURE_GRE = "Le GRE doit �tre de longueur 2 (si le code particulier n'est sp�cifi�) ou 3.";
    public static final String MSG_ECRITURE_GRE6 = "La cotisation est de GENRE 6. Le type de compte ne peut �tre que GENRE 6 ou PROVISOIRE.";
    // message CIEcriture._beforeUpdate()
    public static final String MSG_ECRITURE_MODIFY_CI_UP = "Mise � jour �chou�e. Impossible de mettre � jour le CI correspondant.";
    public static final String MSG_ECRITURE_MODIFY_REM_UP = "Mise � jour �chou�e. Impossible de mettre � jour la remarque correspondante.";
    // message CIEcriture.getNoNomEmployeur()
    public static final String MSG_ECRITURE_NOM_AC = "99999999999 Assurance-ch�mage";
    public static final String MSG_ECRITURE_NOM_AI = "88888888888 Indemnit� journali�re AI";
    public static final String MSG_ECRITURE_NOM_APG = "77777777777 Allocation pour perte de gain";
    public static final String MSG_ECRITURE_NOM_BTA = "11111111111 T�ches d'assistance";
    public static final String MSG_ECRITURE_NOM_MIL = "66666666666 Assurance-militaire";
    public static final String MSG_ECRITURE_PARTBTA = "La part BTA doit �tre vide pour les genres autres que 0.";
    public static final String MSG_ECRITURE_PARTIC = "L'�criture est de GENRE 8, le chiffre cl� particulier ne peut prendre qu'une valeur de 0 � 5.";
    public static final String MSG_ECRITURE_PARTIC_NOT = "Seules valeurs vide ou nulle sont autoris�s pour le code particulier. L'�criture n'est pas du genre 8 (Splitting).";
    public static final String MSG_ECRITURE_PARTSPL = "L'identifiant de partenaire sp�cifi� ne correspond pas � un CI au registre des assur�s.";
    public static final String MSG_ECRITURE_RA = "L'�criture n'a pas de compte individuel associ� au registre des assur�s. L'�criture doit �tre en suspens.  ID du CI: ";
    public static final String MSG_ECRITURE_REVENU = "Le champs revenu doit �tre sup�rieur � 0.";
    public static final String MSG_ECRITURE_REVENU_NOT = "Le champs revenu n'a pas � �tre renseign�.";
    public static final String MSG_ECRITURE_TYPECOM1 = "Genre 0 � 6, 7 d�s 1997 ou 8, 9. Le type d'inscription doit �tre CI ou PROVISOIRE.";
    public static final String MSG_ECRITURE_TYPECOM2 = "Genre 7 avant 1997. Le type d'inscription doit �tre GENRE 7 ou PROVISOIRE.";
    // CIECRITURE
    // message CIEcriture.codeUtilisateurToCodeSysteme
    public static final String MSG_ECRITURE_USER_CODE = "Probl�me d'extraction d'un code syst�me � partir d'un utilisateur.";
    public static final String MSG_JOURNAL_ADDUP_AFF = "Le type de journal s�lectionn� requiert un num�ro d'affili�.";
    public static final String MSG_JOURNAL_ADDUP_AFF_NOTEXIST = "Aucun affili� ne correspond au num�ro sp�cifi�.";
    public static final String MSG_JOURNAL_ADDUP_ANCO = "Le type de journal s�lectionn� requiert une ann�e de cotisation.";
    public static final String MSG_JOURNAL_ADDUP_ANCO_TOLOW = "L'ann�e de cotisation sp�cifi� doit etre sup�rieure � 1948.";
    public static final String MSG_JOURNAL_ADDUP_CORREC = "Une correction sp�ciale de revenus n'est autoris�e que si un total de revenus pour contr�le est sp�cifi�.";
    public static final String MSG_JOURNAL_ADDUP_DREC = "Le type de journal s�lectionn� requiert une date de r�ception.";
    public static final String MSG_JOURNAL_ADDUP_MOTIF = "Une correction sp�ciale existe, un motif est n�cessaire.";
    public static final String MSG_JOURNAL_ADDUP_NOAFF = "Le type de journal s�lectionn� interdit tout num�ro d'affili�.";
    public static final String MSG_JOURNAL_ADDUP_NOMOTIF = "Aucune correction sp�ciale n'a �t� sp�cifi�e. Pas besoin de motif.";
    public static final String MSG_JOURNAL_ADDUP_TOTCTRL = "Le type de journal s�lectionn� requiert un montant de revenus pour contr�le.";
    public static final String MSG_JOURNAL_ADDUP_TYPCOM = "Le type de compte n'est pas sp�cifi�.";
    public static final String MSG_JOURNAL_ADDUP_TYPE = "Le type du journal n'a pas �t� sp�cifi�.";
    // message CIJournal.comptabiliser()
    public static final String MSG_JOURNAL_COMPT_ETAT = "Le journal est d�j� inscrit au CI. Nouvelle inscription impossible.";
    public static final String MSG_JOURNAL_COMPT_STILL = "Inscription au CI du journal impossible. Il existe des �critures en suspens ou provisoire. ID du journal: ";
    public static final String MSG_JOURNAL_COMPT_TOTAUX = "Le total des revenus inscrits n'est pas �gal au total des revenus control�s. ID du journal: ";
    public static final String MSG_JOURNAL_COMPT_UPDATE = "Inscription au CI du journal �chou�e. Echec de mise � jour.";
    public static final String MSG_JOURNAL_DATEREC_NOT = "Aucune date de r�ception n'a � �tre sp�cifi�e.";
    // message CIJournal.validateInputs()
    public static final String MSG_JOURNAL_DECLAR_SAL = "Tentative de cr�ation d'un second journal DECLARATION_SALAIRES pour l'ann�e sp�cifi�e alors qu'il en existe d�j� un.";
    public static final String MSG_JOURNAL_DELETE_ECR = "Suppression impossible. L'effacement d'une �criture associ�e au journal a �chou�e.";
    // message CIJournal._beforeDelete()
    public static final String MSG_JOURNAL_DELETE_ETAT = "Suppression impossible. Le journal � supprimer n'est pas ouvert.";
    public static final String MSG_JOURNAL_DELETE_TYPE = "Suppression impossible. Le type du journal est: ";
    public static final String MSG_JOURNAL_DELETE_TYPECOMPTE = "Suppression impossible. Le type de compte du journal est: ";
    // message CIJournal.updateInscription
    public static final String MSG_JOURNAL_INSCRIT = "Impossible de mettre � jour le total des revenus inscrits.";
    public static final String MSG_JOURNAL_LIBELLE_JOURN = "Inscriptions journali�res du ";
    // message CIJournal._afterAdd()
    public static final String MSG_JOURNAL_REM = "Ajout/Mise � jour impossible de la remarque associ�e.";
    // CIJOURNAL
    // message CIJournal.updateRemJournalRef
    public static final String MSG_JOURNAL_REM_UP_JOURNAL = "Impossible de mettre � jour la r�f�rence sur le journal pour la remarque.";
    // message CIJournal._afterRetrieve()
    public static final String MSG_JOURNAL_RETRI_ECR = "Impossible de lire la base des �critures associ�es au journal.";
    public static final String MSG_JOURNAL_RETRI_REM = "Impossible de r�cup�rer la remarque associ�e au journal.";
    // message CIJournal._beforeUpdate()
    public static final String MSG_JOURNAL_UPDATE = "Mise � jour impossible. Le journal � modifier n'est pas ouvert.";
    public static final String MSG_LIST_JOURNAL_DES = "D�s";
    public static final String MSG_LIST_JOURNAL_FILENAME = "JournalInscription";
    public static final String MSG_LIST_JOURNAL_JUSQUE = "jusqu'�";
    // message CIListJournal
    public static final String MSG_LIST_JOURNAL_TITLE = "Journal des inscription";
    public static final String MSG_LIST_JOURNAL_TOUS = "Tous";
    // message CIMandatSplitting._beforeAdd(transaction)
    public static final String MSG_MANDAT_ADD_ETAT = CIMessagesOLD.MSG_DOMICILE_ADD_ETAT;
    public static final String MSG_MANDAT_DATE_PERIODE = "Les dates indiqu�es ne sont pas dans la p�riode du mariage.";
    // message CIMandatSplitting._beforeDelete(transaction)
    public static final String MSG_MANDAT_DEL_ETAT = "Impossible de supprimer un mandat dans l'�tat actuel.";
    // message CIMandatSplitting._beforeUpdate(transaction)
    public static final String MSG_MANDAT_MOD_ETAT = "Impossible d'effectuer une modification dans l'�tat actuel du mandat.";
    public static final String MSG_MANDAT_REVOQUE_NOUVEAU = "Le mandat n'a pas �t� encore enregistr�!";
    // message CIMandatSplitting.revoquer()
    public static final String MSG_MANDAT_REVOQUER = "Impossible de r�voquer un mandat dans l'�tat actuel du mandat/dossier";
    public static final String MSG_MANDAT_SPLITTER_CI = "Aucun CI trouv� pour l'assur� ou son conjoint.";
    public static final String MSG_MANDAT_SPLITTER_RAM = "Le Ram de l'ann�e et le degr� d'invalidit� doivent �tre saisis.";
    // message CIMandatSplitting.splitter()
    public static final String MSG_MANDAT_SPLITTER_REV = "Aucun revenu trouv� pour le cas du partage des revenus cl�tur�s.";
    public static final String MSG_MANDAT_SPLITTER_TOT = "Le montant ne correspond pas au total des revenus et cotisations.";
    public static final String MSG_MANDAT_VAL_DATE = "L'ann�e de fin ne peut pas �tre plus petite que l'ann�e de d�but!";
    public static final String MSG_MANDAT_VAL_DEBUT = "L'ann�e de d�but est incorrecte.";
    public static final String MSG_MANDAT_VAL_FICHIER = "L'application ne trouve pas les rentes minimales n�cessaire pour le contr�le du Ram.";
    public static final String MSG_MANDAT_VAL_FIN = "L'ann�e de fin est incorrecte.";
    public static final String MSG_MANDAT_VAL_FIN_LI = "L'ann�e de fin ne peut pas �tre plus grande que 1996 pour ce genre de splitting.";
    public static final String MSG_MANDAT_VAL_GENRE = "Le genre de splitting '{0}' n''est pas possible pour l''�pouse.";
    // CIMANDATSPLITTING
    // message CIMandatSplitting._validate(statement)
    public static final String MSG_MANDAT_VAL_ID = "Probl�me d'acc�s aux donn�es.";
    public static final String MSG_MANDAT_VAL_INVALID = "Le degr� d'invalidit� doit �tre sp�cifi� pour ce genre de splitting.";
    public static final String MSG_MANDAT_VAL_MONTANT = "Le total des revenus ou Ram de l'ann�e de fin doit �tre sp�cifi� pour ce genre de splitting.";
    public static final String MSG_MANDAT_VAL_PRC = "Le degr� d'invalidit� doit �tre 50 ou 100.";
    public static final String MSG_MANDAT_VAL_RAM = "L'ann�e de fin doit �tre apr�s 1974 pour ce genre de splitting.";
    public static final String MSG_MANDAT_VAL_RENTES = "Le Ram de l''ann�e de fin doit �tre un multiple de la rente minimale de veuf ({0} en {1})";
    public static final String MSG_MENU_ANNONCE_DE = "Zentrale";
    public static final String MSG_MENU_ANNONCE_FR = "Centrale";
    public static final String MSG_MENU_COMPTE_CI_DE = "Verwaltung des IKs";
    public static final String MSG_MENU_COMPTE_CI_FR = "Gestion CI";
    public static final String MSG_MENU_COMPTE_DE = "IK";
    public static final String MSG_MENU_COMPTE_FR = "CI";
    public static final String MSG_MENU_INSCRIPTIONS_DE = "Buchungen";
    public static final String MSG_MENU_INSCRIPTIONS_DOSSIER_DE = "Verwaltung des Journals";
    public static final String MSG_MENU_INSCRIPTIONS_DOSSIER_FR = "Gestion journaux";
    public static final String MSG_MENU_INSCRIPTIONS_FR = "Inscriptions";
    // DE
    public static final String MSG_MENU_PRINCIPAL_DE = "Hauptmen�";
    // MENU
    // message Appmenu.initMenu
    public static final String MSG_MENU_PRINCIPAL_FR = "Principal";
    public static final String MSG_MENU_SPLITTING_DE = "Splitting";
    public static final String MSG_MENU_SPLITTING_DOSSIER_DE = "Splittingverwaltung";
    public static final String MSG_MENU_SPLITTING_DOSSIER_FR = "Gestion splitting";
    public static final String MSG_MENU_SPLITTING_FR = "Splitting";
    public static final String MSG_OPTIONS_CENTRALE_CI_DE = "Individuelles Konto";
    public static final String MSG_OPTIONS_CENTRALE_CI_FR = "Compte individuel";
    public static final String MSG_OPTIONS_CI_DE = "Auszug ausdrucken";
    public static final String MSG_OPTIONS_CI_FR = "Imprimer extrait";
    public static final String MSG_OPTIONS_COMPTE_ARC_DE = "Erstellung eines MZR";
    public static final String MSG_OPTIONS_COMPTE_ARC_FR = "Cr�er un ARC d'ouverture";
    public static final String MSG_OPTIONS_COMPTE_RASSEMBLEMENT_DE = "Zusammenruf und Erstellung";
    public static final String MSG_OPTIONS_COMPTE_RASSEMBLEMENT_FR = "Rassemblement et ouverture";
    public static final String MSG_OPTIONS_COMPTE_SPLITTING_DE = "Splittingperiode";
    public static final String MSG_OPTIONS_COMPTE_SPLITTING_FR = "P�riode de splitting";
    public static final String MSG_OPTIONS_DOSSIER_DOMICILE_AS_DE = "Auslandaufenthalte des Versicherten";
    public static final String MSG_OPTIONS_DOSSIER_DOMICILE_AS_FR = "Domicile de l'assur�";
    public static final String MSG_OPTIONS_DOSSIER_DOMICILE_CO_DE = "Auslandaufenthalte des Ehegatten";
    public static final String MSG_OPTIONS_DOSSIER_DOMICILE_CO_FR = "Domicile du conjoint";
    public static final String MSG_OPTIONS_DOSSIER_EXECUTER_DE = "Splitting ausf�hren";
    public static final String MSG_OPTIONS_DOSSIER_EXECUTER_FR = "Ex�cuter le splitting";
    public static final String MSG_OPTIONS_DOSSIER_IMPRIMER_DE = "Vorschau ausdrucken";
    public static final String MSG_OPTIONS_DOSSIER_IMPRIMER_FR = "Imprimer l'aper�u";
    public static final String MSG_OPTIONS_DOSSIER_MANDAT_AS_DE = "Auftr�ge des Versicherten";
    public static final String MSG_OPTIONS_DOSSIER_MANDAT_AS_FR = "Mandats de l'assur�";
    public static final String MSG_OPTIONS_DOSSIER_MANDAT_CO_DE = "Auftr�ge des Ehegatten";
    public static final String MSG_OPTIONS_DOSSIER_MANDAT_CO_FR = "Mandats du conjoint";
    public static final String MSG_OPTIONS_DOSSIER_OUVRIR_DE = "Dossier �ffnen";
    public static final String MSG_OPTIONS_DOSSIER_OUVRIR_FR = "Ouvir le dossier";
    public static final String MSG_OPTIONS_DOSSIER_RCI_AS_DE = "ZIK Vorschau des Versicherten";
    public static final String MSG_OPTIONS_DOSSIER_RCI_AS_FR = "Aper�u RCI de l'assur�";
    public static final String MSG_OPTIONS_DOSSIER_RCI_CO_DE = "ZIK Vorschau des Ehegatten";
    public static final String MSG_OPTIONS_DOSSIER_RCI_CO_FR = "Aper�u RCI du conjoint";
    public static final String MSG_OPTIONS_DOSSIER_REVOQUE_DE = "R�ckg�ngigmachen";
    public static final String MSG_OPTIONS_DOSSIER_REVOQUE_FR = "R�voquer";
    public static final String MSG_OPTIONS_DOSSIER_ROUVRIR_DE = "Dossier wieder �ffnen";
    public static final String MSG_OPTIONS_DOSSIER_ROUVRIR_FR = "Rouvrir le dossier";
    public static final String MSG_OPTIONS_JOURNAL_COMPTABILISER_DE = "IK-Buchen";
    public static final String MSG_OPTIONS_JOURNAL_COMPTABILISER_FR = "Inscrire au CI";
    public static final String MSG_OPTIONS_JOURNAL_IMPRIMER_DE = "Ausdrucken";
    public static final String MSG_OPTIONS_JOURNAL_IMPRIMER_FR = "Imprimer";
    public static final String MSG_OPTIONS_JOURNAL_INSCRIPTIONS_DE = "IK-Buchungen";
    public static final String MSG_OPTIONS_JOURNAL_INSCRIPTIONS_FR = "Inscriptions";
    public static final String MSG_OPTIONS_MANDAT_APERCU_RCI_DE = "ZIK Vorschau";
    public static final String MSG_OPTIONS_MANDAT_APERCU_RCI_FR = "Aper�u RCI";
    public static final String MSG_OPTIONS_MANDAT_REVENUS_DE = "Abgeschlossene Beitr�ge";
    public static final String MSG_OPTIONS_MANDAT_REVENUS_FR = "Revenus cl�tur�s";
    public static final String MSG_OPTIONS_MANDAT_REVOQUE_DE = "R�ckg�ngigmachen";
    public static final String MSG_OPTIONS_MANDAT_REVOQUE_FR = "R�voquer";
    // DE
    public static final String MSG_OPTIONS_PRINCIPAL_DE = "Optionen";
    // message Appmenu.initOptions
    public static final String MSG_OPTIONS_PRINCIPAL_FR = "Options";
    // message CIPeriodeSplitting._beforeUpdate(transaction)
    public static final String MSG_PESPLIT_MOD_ETAT = CIMessagesOLD.MSG_RASSEMB_MOD_ETAT;
    // CIPERIODESPLITTING
    // message CIPeriodeSplitting._validate(statement)
    public static final String MSG_PESPLIT_VAL_DEBUT = "L'ann�e de d�but n'est pas valide.";
    public static final String MSG_PESPLIT_VAL_DEBUTP = "L'ann�e de d�but doit �tre comprise entre 1948 et l'ann�e actuelle.";
    public static final String MSG_PESPLIT_VAL_FIN = "L'ann�e de fin n'est pas valide.";
    public static final String MSG_PESPLIT_VAL_FINP = "L'ann�e de fin doit �tre comprise entre l'ann�e de d�but et l'ann�e actuelle.";
    public static final String MSG_PESPLIT_VAL_REVOC = "La date de r�vocation n'est pas valide.";
    public static final String MSG_PESPLIT_VAL_REVOCG = "La date de r�vocation doit �tre apr�s 01.1997 et avant la date d'aujourdui.";
    // message CIRassemblementOuverture._beforeUpdate(transaction)
    public static final String MSG_RASSEMB_MOD_ETAT = "Impossible d'effectuer une modification sur ce document.";
    public static final String MSG_RASSEMB_VAL_ADMIN = "La caisse commettante n'est pas valide";
    public static final String MSG_RASSEMB_VAL_CLOTURE = "La date de cl�ture n'est pas valide.";
    // CIRASSEMBLEMENTOUVERTURE
    // message CIRassemblementOuverture._validate(statement)
    public static final String MSG_RASSEMB_VAL_MOTIF = "Le motif doit �tre renseign�";
    public static final String MSG_RASSEMB_VAL_ORDRE = "La date de l'ordre n'est pas valide.";
    public static final String MSG_RASSEMB_VAL_ORDREG = "La date de l'ordre ne peut pas �tre apr�s la date d'aujoutd'hui.";
    public static final String MSG_RASSEMB_VAL_ORDREP = "La date de l'ordre ne peut pas �tre avant le 01.01.1948.";
    public static final String MSG_RASSEMB_VAL_RCI = "La date de demande du second RCI n'est pas valide.";
    public static final String MSG_RASSEMB_VAL_REVOC = "La date de r�vocation n'est pas valide.";
    // message CIRevenuSplitting._beforeAdd(transaction)
    public static final String MSG_REVENU_ADD_ETAT = "Impossible d'effectuer un ajout dans l'�tat actuel du mandat.";
    // message CIRevenuSplitting._beforeDelete(transaction)
    public static final String MSG_REVENU_DEL_ETAT = "Impossible de supprimer un revenu dans l'�tat actuel du mandat.";
    // message CIRevenuSplitting._beforeUpdate(transaction)
    public static final String MSG_REVENU_MOD_ETAT = "Impossible d'effectuer une modification dans l'�tat actuel du mandat.";
    // CIREVENUSPLITTING
    // message CIRevenuSplitting._validate(statement)
    public static final String MSG_REVENU_VAL_ANNEE = "L'ann�e est incorrecte.";
    public static final String MSG_REVENU_VAL_COTI = "Impossible d'enter une cotisation apr�s 1969";
    public static final String MSG_REVENU_VAL_DATE = "L'ann�e doit �tre comprise entre les ann�es de d�but et de fin du mandat ";
    public static final String MSG_REVENU_VAL_EXIST = "L'ann�e sp�cifi�e a d�j� �t� entr�e utl�rieurement.";
    public static final String MSG_REVENU_VAL_REVENU = "La saisie des revenus est possible seulement si le genre de splitting est '{0}' ou '{1}'";
    public static final String MSG_REVENU_VAL_XOR = "Le montant de la cotisation OU le revenu doit �tre sp�cifi�.";
    public static final String MSG_TIERS_INVALID = "Le tiers n'a pas pu �tre lu.";

}
