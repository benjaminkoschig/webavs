package globaz.pavo.translation;

/**
 * Messages de l'application PAVO. Date de création : (22.10.2002 09:12:46)
 * 
 * @author: Administrator
 */
public interface CIMessagesOLD {
    public static final String MSG_ANNONCE_21_EMAIL_MESSAGE = "Un ordre d'ouverture CI pour l''assuré {0} doit être complété avec les différentes périodes de splitting.";
    public static final String MSG_ANNONCE_21_EMAIL_SUJET = "Demande de complément des périodes de splitting";
    public static final String MSG_ANNONCE_21_RCI_EMAIL_MESSAGE = "Un second RCI doit être envoyé pour l''assuré {0} afin de pouvoir ouvrir son CI.";
    public static final String MSG_ANNONCE_21_RCI_EMAIL_SUJET = "Second RCI";
    public static final String MSG_ANNONCE_22_EMAIL_MESSAGE = "Un ordre de clôture pour l''assuré {0} a été reçu alors qu''une clôture est déjà existante.\n Contenu de l'ordre:\n{1}";
    public static final String MSG_ANNONCE_22_EMAIL_SUJET = "Clôture déjà existante";
    public static final String MSG_ANNONCE_22_SPL_EMAIL_MESSAGE = "Un ordre de splitting pour l''assuré {0} a été reçu alors qu''un même ordre à déjà été effectué.\n Contenu de l''ordre:\n{1}";
    public static final String MSG_ANNONCE_22_SPL_EMAIL_SUJET = "Ordre de splitting déjà existant";
    public static final String MSG_ANNONCE_22B_EMAIL_MESSAGE = "Un ordre de clôture pour l''assuré {0} a été reçu alors qu''aucun CI n''a été trouvé.\n Contenu de l''ordre:\n{1}";
    public static final String MSG_ANNONCE_22B_EMAIL_SUJET = "Numéro d'assuré non trouvé pour cette clôture";
    public static final String MSG_ANNONCE_22B_SPL_EMAIL_MESSAGE = "Un ordre de splitting pour l''assuré {0} a été reçu alors qu''aucun CI n''a été trouvé.\n Contenu de l''ordre:\n{1}";
    public static final String MSG_ANNONCE_22B_SPL_EMAIL_SUJET = "Numéro d'assuré non trouvé pour cet ordre de splitting";
    public static final String MSG_ANNONCE_22C_SPL_EMAIL_SUJET = "Demande d'ouverture du CI du conjoint.";
    public static final String MSG_ANNONCE_22R_EMAIL_MESSAGE = "Un ordre de rassemblement pour l''assuré {0} a été reçu alors qu''aucun CI n''a été trouvé.\n Contenu de l''ordre:\n{1}";
    public static final String MSG_ANNONCE_22R_EMAIL_SUJET = "Numéro d'assuré non trouvé pour ce rassemblement";
    public static final String MSG_ANNONCE_23_EMAIL_MESSAGE = "Un CI de complément pour l''assuré {0} ou {1} a été reçu alors qu''aucun CI n''a été trouvé.\nContenu du complément:\n {2} ";
    public static final String MSG_ANNONCE_23_EMAIL_SUJET = "Numéro d'assuré complété et numéro d'assuré à compléter non trouvé pour ce CI de complément";
    public static final String MSG_ANNONCE_29_CL_EMAIL_MESSAGE = "Un ordre de révocation de RCI pour l''assuré {0} a été reçu alors qu''aucun CI ou aucune clôture n''a été trouvé.\n Contenu de l''ordre:\n{1}";
    public static final String MSG_ANNONCE_29_CL_EMAIL_SUJET = "Pas de clôture trouvée pour ce numéro d'assuré";
    public static final String MSG_ANNONCE_29_SP_EMAIL_MESSAGE = "Un ordre de révocation de splitting pour l''assuré {0} a été reçu alors qu''aucun CI ou ordre de splitting n''a été trouvé.\n Contenu de l''ordre:\n{1}";
    public static final String MSG_ANNONCE_29_SP_EMAIL_SUJET = "Pas d'ordre de splitting trouvé pour ce numéro d'assuré";
    // CIANNONCESUSPENS
    // message CIAnnonceSuspens._beforeAdd(statement)
    public static final String MSG_ANNONCE_ADD_CA = "Le code application doit être spécifié.";
    // message CIAnnonceSuspens._beforeDelete(statement)
    public static final String MSG_ANNONCE_DEL_USER = "Seul le responsable CI peut supprimer ce document.";
    public static final String MSG_ANNONCE_EMAIL_AGENCE = "Agence: ";
    public static final String MSG_ANNONCE_EMAIL_AVS = "Assuré: ";
    public static final String MSG_ANNONCE_EMAIL_CAISSE = "Caisse: ";
    public static final String MSG_ANNONCE_EMAIL_DATE = "Date de clôture: ";
    public static final String MSG_ANNONCE_EMAIL_DATE_SP = "Date de l'ordre de splitting: ";
    public static final String MSG_ANNONCE_EMAIL_ERREUR = "PAVO - erreur d'envoi d'email";
    public static final String MSG_ANNONCE_EMAIL_MOTIF = "Motif de l'annonce: ";
    public static final String MSG_ANNONCE_EMAIL_NOM = "Etat nominatif: ";
    public static final String MSG_ANNONCE_EMAIL_PARTENAIRE = "Partenaire: ";
    public static final String MSG_ANNONCE_EMAIL_REF = "Référence interne: ";
    // message CIAnnonceSuspens.traitement21Ouverture();
    public static final String MSG_ANNONCE_EMAIL_SUJET = "Traitement des annonces.";
    // CIAPPLICATION
    // message CIApplication annonceARC()
    public static final String MSG_ANNONCE_INPUT_INVALID = "Une annonce ARC n'a pas pu être envoyée.";
    public static final String MSG_ANNONCE_OUTPUT_INVALID = "Une annonce ARC n'a pas pu être lue.";
    // message CICompteIndividuel.annonceCIAssitionnel()
    public static final String MSG_CI_CIADD = "Selon le CI, des écritures devraient être clôturées mais la clôture n'a pas été trouvée. Mise à jour manuelle nécessaire.";
    public static final String MSG_CI_EMAIL_MESSAGE = "Un CI provisoire et un au RA a été trouvé pour la même personne. No avs: ";
    public static final String MSG_CI_EMAIL_SUJET = "Erreur de CI";
    public static final String MSG_CI_ETAT_ACTIVES = "Ecritures actives";
    public static final String MSG_CI_ETAT_TOUS = "Toutes les clôtures";
    public static final String MSG_CI_PROCESS_MESSAGE23 = "Le complément de CI suivant n''a pas pu être effectué: \n {0}";
    // PROCESS
    // message CICompteIndividuelProcess
    public static final String MSG_CI_PROCESS_SUJET23 = "Complément de CI";
    public static final String MSG_CI_SUPPRESSION = "Impossible de supprimer un CI.";
    public static final String MSG_CI_VAL_AVS = "Le numéro AVS n'est pas valide.";
    // CICOMPTEINDIVIDUEL
    // message CICompteIndividuel._validate(statement)
    public static final String MSG_CI_VAL_NAISSANCE = "La date de naissance n'est pas valide.";
    public static final String MSG_CI_VAL_NOM = "Le nom et prénom de l'assuré doit être rensigné.";
    public static final String MSG_CI_VAL_OUVERTURE = "L'année d'ouverture n'est pas valide.";
    public static final String MSG_CI_VAL_PAYS = "Le pays d'origine n'est pas valide.";
    public static final String MSG_CI_VAL_REF = "La référence interne doit être renseignée.";
    public static final String MSG_CI_VAL_REGISTRE = "Le registre du CI n'est pas renseigné.";
    public static final String MSG_CI_VAL_SEXE = "Le sexe de la personne n'est pas valide.";
    public static final String MSG_COMPTA_JOURNAL_AVS = "La plage des numéros avs n'est pas correcte.";
    // message CIComptabiliserJournalProcess
    public static final String MSG_COMPTA_JOURNAL_EMAIL = "Adresse email obligatoire.";
    public static final String MSG_COMPTA_JOURNAL_EMAIL_INV = "Adresse email invalide.";
    public static final String MSG_COMPTA_JOURNAL_ID = "No de journal obligatoire.";
    // message CIDomicileSplitting._beforeAdd(transaction)
    public static final String MSG_DOMICILE_ADD_ETAT = "Impossible d'effectuer un ajout dans l'état actuel du dossier.";
    // message CIDomicileSplitting._beforeDelete(transaction)
    public static final String MSG_DOMICILE_DEL_ETAT = "Impossible de supprimer un domicile dans l'état actuel du dossier.";
    // message CIDomicileSplitting._beforeUpdate(transaction)
    public static final String MSG_DOMICILE_MOD_ETAT = "Impossible d'effectuer une modification dans l'état actuel du dossier.";
    public static final String MSG_DOMICILE_VAL_DATE = "La date de fin ne peut pas avoir lieu avant la date de début!";
    // CIDOMICILESPLITTING
    // message CIDomicileSplitting._validate(statement)
    public static final String MSG_DOMICILE_VAL_DEBUT = "La date de début est incorrecte.";
    public static final String MSG_DOMICILE_VAL_FIN = "La date de fin est incorrecte.";
    public static final String MSG_DOMICILE_VAL_LIBELLE = "Le libellé doit être spécifié.";
    public static final String MSG_DOSSIER_ANNONCE = "Une annonce ARC n'a pas pu être envoyée.";
    // message CIDossierSplitting.imprimerApercu()
    public static final String MSG_DOSSIER_APERCU = "Impossible d'imprimer l'aperçu dans l'état actuel du dossier.";
    // message CIDossierSplitting._beforeDelete(transaction)
    public static final String MSG_DOSSIER_DEL_ETAT = "Impossible de supprimer le dossier dans l'état actuel.";
    public static final String MSG_DOSSIER_DEL_USER = "Seul le responsable de ce dossier ou le reponsable CI peut supprimer ce document.";
    public static final String MSG_DOSSIER_EMAIL_ERREUR = "PAVO - erreur d'envoi d'email";
    public static final String MSG_DOSSIER_EMAIL_MESSAGE_CL = "Le dossier de splitting concernant l''assuré mentionné ci-dessous a été clôturé.\n {0}";
    public static final String MSG_DOSSIER_EMAIL_MESSAGE_RE = "Le dossier de splitting concernant l''assuré mentionné ci-dessous a été révoqué.\n {0}";
    public static final String MSG_DOSSIER_EMAIL_NO_AVS = "No AVS: ";
    public static final String MSG_DOSSIER_EMAIL_NO_AVSC = "No AVS du conjoint: ";
    public static final String MSG_DOSSIER_EMAIL_NOM = "Nom: ";
    public static final String MSG_DOSSIER_EMAIL_NOMC = "Nom du conjoint: ";
    // message CIDossierSplitting.updateDossier() (email)
    public static final String MSG_DOSSIER_EMAIL_SUJET_CL = "Clôture du dossier de splitting";
    public static final String MSG_DOSSIER_EMAIL_SUJET_RE = "Révocation du dossier de splitting";
    public static final String MSG_DOSSIER_OUVRIR_IMPOSSIBLE = "Le dossier à déjà été ouvert.";
    // CIDOSSIER
    // messages CIDossierSplitting.ouvrir()
    public static final String MSG_DOSSIER_OUVRIR_NOUVEAU = "Le dossier n'a pas encore été enregistré.";
    // message CIDossierSplitting.revoquer()
    public static final String MSG_DOSSIER_REVOQUER = "Impossible de révoquer le dossier son état actuel.";
    // message CIDossierSplitting.rouvrir()
    public static final String MSG_DOSSIER_ROUVRIR = "Impossible de rouvrir le dossier dans l'état actuel.";
    // message CIDossierSplitting.splitterMandats()
    public static final String MSG_DOSSIER_SPL_MANDATS = "Ce dossier contient des mandats en demande de révocation.";
    // message CIDossierSplitting.executerSplitting()
    public static final String MSG_DOSSIER_SPLITTING = "Impossible d'exécuter le splitting dans l'état actuel du dossier.";
    public static final String MSG_DOSSIER_UP_ASSURE = "Aucun mandat valide n'a été trouvé pour l'assuré.";
    public static final String MSG_DOSSIER_UP_CONJOINT = "Aucun mandat valide n'a été trouvé pour le conjoint.";
    // message CIDossierSplitting.updateDossier()
    public static final String MSG_DOSSIER_UPDATE = "Aucun mandat valide n'a été trouvé pour exécuter le splitting.";
    public static final String MSG_DOSSIER_VAL_CONJOINT = "Le champ Conjoint n'est pas valide.";
    public static final String MSG_DOSSIER_VAL_DATE = "Le divorce ne peut pas avoir eu lieu avant le mariage!";
    public static final String MSG_DOSSIER_VAL_DIVORCE = "La date de divorce est incorrecte.";
    public static final String MSG_DOSSIER_VAL_MARIAGE = "La date de mariage est incorrecte.";
    public static final String MSG_DOSSIER_VAL_OUVERTURE = "La date d'ouverture de dossier est incorrecte.";
    public static final String MSG_DOSSIER_VAL_RESP = "Le responsable du dossier n'est pas spécifié ou n'est inconnu.";
    // message CIDossierSplitting._validate(statement)
    public static final String MSG_DOSSIER_VAL_TIERS = "Le champ Assuré n'est pas valide.";
    public static final String MSG_ECRITURE_ADD_CI_ACCESS = "Ajout échoué. Erreur dans la tentative d'accès au compte individuel associé.";
    public static final String MSG_ECRITURE_ADD_CI_NEW = "Ajout échoué. Impossible de créer un nouveau CI. ";
    // message CIEcriture._beforeAdd()
    public static final String MSG_ECRITURE_ADD_ETAT = "Ajout/modification non autorisé. Le journal associé n'est pas ouvert.";
    public static final String MSG_ECRITURE_ADD_REM = "Ajout échoué. Impossible d'ajouter une nouvelle remarque.";
    public static final String MSG_ECRITURE_AFFILIE = "Impossible de retrouver l'affilié spécifié.";
    public static final String MSG_ECRITURE_AGE = "Si le genre de l'écriture n'est pas splitting (genre 8), l'âge minimum requis est 17 ans par rapport à l'année de cotisation.";
    public static final String MSG_ECRITURE_ANNEE = "L'année de cotisation doit être supérieure ou égale à 1948";
    // message CIEcriture._validate()
    public static final String MSG_ECRITURE_AVS = "Le numéro d'AVS est obligatoire.";
    public static final String MSG_ECRITURE_BRECO = "La branche économique est obligatoire.";
    public static final String MSG_ECRITURE_BRECO_NOT = "La branche économique doit être vide.";
    public static final String MSG_ECRITURE_CAICHO_NOT = "La caisse chômage n'a pas à être renseignée.";
    public static final String MSG_ECRITURE_CAISCHO = "Le type d'inscription du journal est ASSURANCE CHOMAGE et écriture antécédente à 1969. Branche économique obligatoire.";
    public static final String MSG_ECRITURE_CAISCHO_FORMAT = "Le numéro de caisse chômage doit comporter au moins 3 chiffres.";
    public static final String MSG_ECRITURE_CI_CLOSED = "Le compte individuel n'est pas ouvert. Le type d'inscription doit être SUSPENS. ID du CI: ";
    public static final String MSG_ECRITURE_CLOT = "L'inscription a lieu dans une période clôturée. Le type de compte doit être CI ou PROVISOIRE.";
    public static final String MSG_ECRITURE_CODE_A = "Le code A ne peut être utilisé que pour une extourne.";
    public static final String MSG_ECRITURE_CODE_D = "L'écriture est de genre 4, le code irrecouvrable doit être D.";
    public static final String MSG_ECRITURE_CODE_NOT = "L'écriture est de genre 5 ou 8, le code irrecouvrable doit être blanc.";
    public static final String MSG_ECRITURE_CODE_VAL = "Mauvaise valeur du code irrecouvrable.";
    public static final String MSG_ECRITURE_CODSPE4 = "Inscription de genre 4: code spécial à 01 ou nul.";
    public static final String MSG_ECRITURE_CODSPE7 = "Inscription de genre 7 à partir de 1997: code spécial à 02 ou 03.";
    public static final String MSG_ECRITURE_CODSPE7_NOT = "Le code spécial doit être vide étant donné le genre d'écriture spécifié (et aussi peut-être l'année).";
    public static final String MSG_ECRITURE_COMPT_CTRL = "Inscription au CI de l'écriture impossible si le journal contient un total de contrôle.";
    // message CIEcriture.comptabiliser()
    public static final String MSG_ECRITURE_COMPT_TYPECOMPT = "Inscription au CI de l'écriture impossible. Le type de compte associé à l'écriture n'est pas PROVISOIRE. ID de l'écriture: ";
    public static final String MSG_ECRITURE_COMPT_UPDATE = "Inscription au CI de l'écriture échouée. Echec de mise à jour. ID de l'écriture/ID du CI: ";
    public static final String MSG_ECRITURE_CONJ = "Un identifiant valable pour le partenaire est nécessaire (genre 8).";
    public static final String MSG_ECRITURE_CONJ_IDEN = "Le partenaire ne peut pas être identique à l'assuré lui-même.";
    public static final String MSG_ECRITURE_DATEDEB = "Mauvaise valeur de date de début.";
    public static final String MSG_ECRITURE_DATEDEB_MANDAT = "Le mois de début est obligatoire.";
    public static final String MSG_ECRITURE_DATEDEB_NOT = "Le mois de début n'a pas à être spécifié.";
    public static final String MSG_ECRITURE_DATEDEBFIN = "Spécifier une date de fin postérieure à la date de début.";
    public static final String MSG_ECRITURE_DATEFIN = "Mauvaise valeur de date de fin.";
    public static final String MSG_ECRITURE_DATEFIN_MANDAT = "Le mois de fin est obligatoire.";
    public static final String MSG_ECRITURE_DATEFIN_NOT = "Le mois de fin n'a pas à être spécifié.";
    public static final String MSG_ECRITURE_DEL_ETAT = "Suppression impossible. Le journal associé n'est pas ouvert.";
    // message CIEcriture._beforeDelete()
    public static final String MSG_ECRITURE_DEL_TYPCOM = "Suppression impossible. Le type de compte est: ";
    public static final String MSG_ECRITURE_EMP = "Un identifiant valable pour l'employeur est nécessaire.";
    public static final String MSG_ECRITURE_EMPCONJ_NOT = "L'employeur et le partenaire ne doivent pas être renseignés.";
    public static final String MSG_ECRITURE_EXTOURNE = "En dehors de la période 1969-1975, l'extourne ne peut prendre que les valeurs 0, 1, 8 ou 9.";
    public static final String MSG_ECRITURE_GENRE = "Mauvais genre d'écriture pour le type d'inscription (spécifié au journal).";
    public static final String MSG_ECRITURE_GENRE1 = "Genre 1 non possible pour une personne à la retraite.";
    public static final String MSG_ECRITURE_GENRE7 = "La personne n'est pas à la retraîte lors de la période de l'écriture ou celle-ci se trouve avant la clôture. Genre 7 impossible.";
    public static final String MSG_ECRITURE_GRE = "Le GRE doit être de longueur 2 (si le code particulier n'est spécifié) ou 3.";
    public static final String MSG_ECRITURE_GRE6 = "La cotisation est de GENRE 6. Le type de compte ne peut être que GENRE 6 ou PROVISOIRE.";
    // message CIEcriture._beforeUpdate()
    public static final String MSG_ECRITURE_MODIFY_CI_UP = "Mise à jour échouée. Impossible de mettre à jour le CI correspondant.";
    public static final String MSG_ECRITURE_MODIFY_REM_UP = "Mise à jour échouée. Impossible de mettre à jour la remarque correspondante.";
    // message CIEcriture.getNoNomEmployeur()
    public static final String MSG_ECRITURE_NOM_AC = "99999999999 Assurance-chômage";
    public static final String MSG_ECRITURE_NOM_AI = "88888888888 Indemnité journalière AI";
    public static final String MSG_ECRITURE_NOM_APG = "77777777777 Allocation pour perte de gain";
    public static final String MSG_ECRITURE_NOM_BTA = "11111111111 Tâches d'assistance";
    public static final String MSG_ECRITURE_NOM_MIL = "66666666666 Assurance-militaire";
    public static final String MSG_ECRITURE_PARTBTA = "La part BTA doit être vide pour les genres autres que 0.";
    public static final String MSG_ECRITURE_PARTIC = "L'écriture est de GENRE 8, le chiffre clé particulier ne peut prendre qu'une valeur de 0 à 5.";
    public static final String MSG_ECRITURE_PARTIC_NOT = "Seules valeurs vide ou nulle sont autorisés pour le code particulier. L'écriture n'est pas du genre 8 (Splitting).";
    public static final String MSG_ECRITURE_PARTSPL = "L'identifiant de partenaire spécifié ne correspond pas à un CI au registre des assurés.";
    public static final String MSG_ECRITURE_RA = "L'écriture n'a pas de compte individuel associé au registre des assurés. L'écriture doit être en suspens.  ID du CI: ";
    public static final String MSG_ECRITURE_REVENU = "Le champs revenu doit être supérieur à 0.";
    public static final String MSG_ECRITURE_REVENU_NOT = "Le champs revenu n'a pas à être renseigné.";
    public static final String MSG_ECRITURE_TYPECOM1 = "Genre 0 à 6, 7 dès 1997 ou 8, 9. Le type d'inscription doit être CI ou PROVISOIRE.";
    public static final String MSG_ECRITURE_TYPECOM2 = "Genre 7 avant 1997. Le type d'inscription doit être GENRE 7 ou PROVISOIRE.";
    // CIECRITURE
    // message CIEcriture.codeUtilisateurToCodeSysteme
    public static final String MSG_ECRITURE_USER_CODE = "Problème d'extraction d'un code système à partir d'un utilisateur.";
    public static final String MSG_JOURNAL_ADDUP_AFF = "Le type de journal sélectionné requiert un numéro d'affilié.";
    public static final String MSG_JOURNAL_ADDUP_AFF_NOTEXIST = "Aucun affilié ne correspond au numéro spécifié.";
    public static final String MSG_JOURNAL_ADDUP_ANCO = "Le type de journal sélectionné requiert une année de cotisation.";
    public static final String MSG_JOURNAL_ADDUP_ANCO_TOLOW = "L'année de cotisation spécifié doit etre supérieure à 1948.";
    public static final String MSG_JOURNAL_ADDUP_CORREC = "Une correction spéciale de revenus n'est autorisée que si un total de revenus pour contrôle est spécifié.";
    public static final String MSG_JOURNAL_ADDUP_DREC = "Le type de journal sélectionné requiert une date de réception.";
    public static final String MSG_JOURNAL_ADDUP_MOTIF = "Une correction spéciale existe, un motif est nécessaire.";
    public static final String MSG_JOURNAL_ADDUP_NOAFF = "Le type de journal sélectionné interdit tout numéro d'affilié.";
    public static final String MSG_JOURNAL_ADDUP_NOMOTIF = "Aucune correction spéciale n'a été spécifiée. Pas besoin de motif.";
    public static final String MSG_JOURNAL_ADDUP_TOTCTRL = "Le type de journal sélectionné requiert un montant de revenus pour contrôle.";
    public static final String MSG_JOURNAL_ADDUP_TYPCOM = "Le type de compte n'est pas spécifié.";
    public static final String MSG_JOURNAL_ADDUP_TYPE = "Le type du journal n'a pas été spécifié.";
    // message CIJournal.comptabiliser()
    public static final String MSG_JOURNAL_COMPT_ETAT = "Le journal est déjà inscrit au CI. Nouvelle inscription impossible.";
    public static final String MSG_JOURNAL_COMPT_STILL = "Inscription au CI du journal impossible. Il existe des écritures en suspens ou provisoire. ID du journal: ";
    public static final String MSG_JOURNAL_COMPT_TOTAUX = "Le total des revenus inscrits n'est pas égal au total des revenus controlés. ID du journal: ";
    public static final String MSG_JOURNAL_COMPT_UPDATE = "Inscription au CI du journal échouée. Echec de mise à jour.";
    public static final String MSG_JOURNAL_DATEREC_NOT = "Aucune date de réception n'a à être spécifiée.";
    // message CIJournal.validateInputs()
    public static final String MSG_JOURNAL_DECLAR_SAL = "Tentative de création d'un second journal DECLARATION_SALAIRES pour l'année spécifiée alors qu'il en existe déjà un.";
    public static final String MSG_JOURNAL_DELETE_ECR = "Suppression impossible. L'effacement d'une écriture associée au journal a échouée.";
    // message CIJournal._beforeDelete()
    public static final String MSG_JOURNAL_DELETE_ETAT = "Suppression impossible. Le journal à supprimer n'est pas ouvert.";
    public static final String MSG_JOURNAL_DELETE_TYPE = "Suppression impossible. Le type du journal est: ";
    public static final String MSG_JOURNAL_DELETE_TYPECOMPTE = "Suppression impossible. Le type de compte du journal est: ";
    // message CIJournal.updateInscription
    public static final String MSG_JOURNAL_INSCRIT = "Impossible de mettre à jour le total des revenus inscrits.";
    public static final String MSG_JOURNAL_LIBELLE_JOURN = "Inscriptions journalières du ";
    // message CIJournal._afterAdd()
    public static final String MSG_JOURNAL_REM = "Ajout/Mise à jour impossible de la remarque associée.";
    // CIJOURNAL
    // message CIJournal.updateRemJournalRef
    public static final String MSG_JOURNAL_REM_UP_JOURNAL = "Impossible de mettre à jour la référence sur le journal pour la remarque.";
    // message CIJournal._afterRetrieve()
    public static final String MSG_JOURNAL_RETRI_ECR = "Impossible de lire la base des écritures associées au journal.";
    public static final String MSG_JOURNAL_RETRI_REM = "Impossible de récupérer la remarque associée au journal.";
    // message CIJournal._beforeUpdate()
    public static final String MSG_JOURNAL_UPDATE = "Mise à jour impossible. Le journal à modifier n'est pas ouvert.";
    public static final String MSG_LIST_JOURNAL_DES = "Dès";
    public static final String MSG_LIST_JOURNAL_FILENAME = "JournalInscription";
    public static final String MSG_LIST_JOURNAL_JUSQUE = "jusqu'à";
    // message CIListJournal
    public static final String MSG_LIST_JOURNAL_TITLE = "Journal des inscription";
    public static final String MSG_LIST_JOURNAL_TOUS = "Tous";
    // message CIMandatSplitting._beforeAdd(transaction)
    public static final String MSG_MANDAT_ADD_ETAT = CIMessagesOLD.MSG_DOMICILE_ADD_ETAT;
    public static final String MSG_MANDAT_DATE_PERIODE = "Les dates indiquées ne sont pas dans la période du mariage.";
    // message CIMandatSplitting._beforeDelete(transaction)
    public static final String MSG_MANDAT_DEL_ETAT = "Impossible de supprimer un mandat dans l'état actuel.";
    // message CIMandatSplitting._beforeUpdate(transaction)
    public static final String MSG_MANDAT_MOD_ETAT = "Impossible d'effectuer une modification dans l'état actuel du mandat.";
    public static final String MSG_MANDAT_REVOQUE_NOUVEAU = "Le mandat n'a pas été encore enregistré!";
    // message CIMandatSplitting.revoquer()
    public static final String MSG_MANDAT_REVOQUER = "Impossible de révoquer un mandat dans l'état actuel du mandat/dossier";
    public static final String MSG_MANDAT_SPLITTER_CI = "Aucun CI trouvé pour l'assuré ou son conjoint.";
    public static final String MSG_MANDAT_SPLITTER_RAM = "Le Ram de l'année et le degré d'invalidité doivent être saisis.";
    // message CIMandatSplitting.splitter()
    public static final String MSG_MANDAT_SPLITTER_REV = "Aucun revenu trouvé pour le cas du partage des revenus clôturés.";
    public static final String MSG_MANDAT_SPLITTER_TOT = "Le montant ne correspond pas au total des revenus et cotisations.";
    public static final String MSG_MANDAT_VAL_DATE = "L'année de fin ne peut pas être plus petite que l'année de début!";
    public static final String MSG_MANDAT_VAL_DEBUT = "L'année de début est incorrecte.";
    public static final String MSG_MANDAT_VAL_FICHIER = "L'application ne trouve pas les rentes minimales nécessaire pour le contrôle du Ram.";
    public static final String MSG_MANDAT_VAL_FIN = "L'année de fin est incorrecte.";
    public static final String MSG_MANDAT_VAL_FIN_LI = "L'année de fin ne peut pas être plus grande que 1996 pour ce genre de splitting.";
    public static final String MSG_MANDAT_VAL_GENRE = "Le genre de splitting '{0}' n''est pas possible pour l''épouse.";
    // CIMANDATSPLITTING
    // message CIMandatSplitting._validate(statement)
    public static final String MSG_MANDAT_VAL_ID = "Problème d'accès aux données.";
    public static final String MSG_MANDAT_VAL_INVALID = "Le degré d'invalidité doit être spécifié pour ce genre de splitting.";
    public static final String MSG_MANDAT_VAL_MONTANT = "Le total des revenus ou Ram de l'année de fin doit être spécifié pour ce genre de splitting.";
    public static final String MSG_MANDAT_VAL_PRC = "Le degré d'invalidité doit être 50 ou 100.";
    public static final String MSG_MANDAT_VAL_RAM = "L'année de fin doit être après 1974 pour ce genre de splitting.";
    public static final String MSG_MANDAT_VAL_RENTES = "Le Ram de l''année de fin doit être un multiple de la rente minimale de veuf ({0} en {1})";
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
    public static final String MSG_MENU_PRINCIPAL_DE = "Hauptmenü";
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
    public static final String MSG_OPTIONS_COMPTE_ARC_FR = "Créer un ARC d'ouverture";
    public static final String MSG_OPTIONS_COMPTE_RASSEMBLEMENT_DE = "Zusammenruf und Erstellung";
    public static final String MSG_OPTIONS_COMPTE_RASSEMBLEMENT_FR = "Rassemblement et ouverture";
    public static final String MSG_OPTIONS_COMPTE_SPLITTING_DE = "Splittingperiode";
    public static final String MSG_OPTIONS_COMPTE_SPLITTING_FR = "Période de splitting";
    public static final String MSG_OPTIONS_DOSSIER_DOMICILE_AS_DE = "Auslandaufenthalte des Versicherten";
    public static final String MSG_OPTIONS_DOSSIER_DOMICILE_AS_FR = "Domicile de l'assuré";
    public static final String MSG_OPTIONS_DOSSIER_DOMICILE_CO_DE = "Auslandaufenthalte des Ehegatten";
    public static final String MSG_OPTIONS_DOSSIER_DOMICILE_CO_FR = "Domicile du conjoint";
    public static final String MSG_OPTIONS_DOSSIER_EXECUTER_DE = "Splitting ausführen";
    public static final String MSG_OPTIONS_DOSSIER_EXECUTER_FR = "Exécuter le splitting";
    public static final String MSG_OPTIONS_DOSSIER_IMPRIMER_DE = "Vorschau ausdrucken";
    public static final String MSG_OPTIONS_DOSSIER_IMPRIMER_FR = "Imprimer l'aperçu";
    public static final String MSG_OPTIONS_DOSSIER_MANDAT_AS_DE = "Aufträge des Versicherten";
    public static final String MSG_OPTIONS_DOSSIER_MANDAT_AS_FR = "Mandats de l'assuré";
    public static final String MSG_OPTIONS_DOSSIER_MANDAT_CO_DE = "Aufträge des Ehegatten";
    public static final String MSG_OPTIONS_DOSSIER_MANDAT_CO_FR = "Mandats du conjoint";
    public static final String MSG_OPTIONS_DOSSIER_OUVRIR_DE = "Dossier öffnen";
    public static final String MSG_OPTIONS_DOSSIER_OUVRIR_FR = "Ouvir le dossier";
    public static final String MSG_OPTIONS_DOSSIER_RCI_AS_DE = "ZIK Vorschau des Versicherten";
    public static final String MSG_OPTIONS_DOSSIER_RCI_AS_FR = "Aperçu RCI de l'assuré";
    public static final String MSG_OPTIONS_DOSSIER_RCI_CO_DE = "ZIK Vorschau des Ehegatten";
    public static final String MSG_OPTIONS_DOSSIER_RCI_CO_FR = "Aperçu RCI du conjoint";
    public static final String MSG_OPTIONS_DOSSIER_REVOQUE_DE = "Rückgängigmachen";
    public static final String MSG_OPTIONS_DOSSIER_REVOQUE_FR = "Révoquer";
    public static final String MSG_OPTIONS_DOSSIER_ROUVRIR_DE = "Dossier wieder öffnen";
    public static final String MSG_OPTIONS_DOSSIER_ROUVRIR_FR = "Rouvrir le dossier";
    public static final String MSG_OPTIONS_JOURNAL_COMPTABILISER_DE = "IK-Buchen";
    public static final String MSG_OPTIONS_JOURNAL_COMPTABILISER_FR = "Inscrire au CI";
    public static final String MSG_OPTIONS_JOURNAL_IMPRIMER_DE = "Ausdrucken";
    public static final String MSG_OPTIONS_JOURNAL_IMPRIMER_FR = "Imprimer";
    public static final String MSG_OPTIONS_JOURNAL_INSCRIPTIONS_DE = "IK-Buchungen";
    public static final String MSG_OPTIONS_JOURNAL_INSCRIPTIONS_FR = "Inscriptions";
    public static final String MSG_OPTIONS_MANDAT_APERCU_RCI_DE = "ZIK Vorschau";
    public static final String MSG_OPTIONS_MANDAT_APERCU_RCI_FR = "Aperçu RCI";
    public static final String MSG_OPTIONS_MANDAT_REVENUS_DE = "Abgeschlossene Beiträge";
    public static final String MSG_OPTIONS_MANDAT_REVENUS_FR = "Revenus clôturés";
    public static final String MSG_OPTIONS_MANDAT_REVOQUE_DE = "Rückgängigmachen";
    public static final String MSG_OPTIONS_MANDAT_REVOQUE_FR = "Révoquer";
    // DE
    public static final String MSG_OPTIONS_PRINCIPAL_DE = "Optionen";
    // message Appmenu.initOptions
    public static final String MSG_OPTIONS_PRINCIPAL_FR = "Options";
    // message CIPeriodeSplitting._beforeUpdate(transaction)
    public static final String MSG_PESPLIT_MOD_ETAT = CIMessagesOLD.MSG_RASSEMB_MOD_ETAT;
    // CIPERIODESPLITTING
    // message CIPeriodeSplitting._validate(statement)
    public static final String MSG_PESPLIT_VAL_DEBUT = "L'année de début n'est pas valide.";
    public static final String MSG_PESPLIT_VAL_DEBUTP = "L'année de début doit être comprise entre 1948 et l'année actuelle.";
    public static final String MSG_PESPLIT_VAL_FIN = "L'année de fin n'est pas valide.";
    public static final String MSG_PESPLIT_VAL_FINP = "L'année de fin doit être comprise entre l'année de début et l'année actuelle.";
    public static final String MSG_PESPLIT_VAL_REVOC = "La date de révocation n'est pas valide.";
    public static final String MSG_PESPLIT_VAL_REVOCG = "La date de révocation doit être après 01.1997 et avant la date d'aujourdui.";
    // message CIRassemblementOuverture._beforeUpdate(transaction)
    public static final String MSG_RASSEMB_MOD_ETAT = "Impossible d'effectuer une modification sur ce document.";
    public static final String MSG_RASSEMB_VAL_ADMIN = "La caisse commettante n'est pas valide";
    public static final String MSG_RASSEMB_VAL_CLOTURE = "La date de clôture n'est pas valide.";
    // CIRASSEMBLEMENTOUVERTURE
    // message CIRassemblementOuverture._validate(statement)
    public static final String MSG_RASSEMB_VAL_MOTIF = "Le motif doit être renseigné";
    public static final String MSG_RASSEMB_VAL_ORDRE = "La date de l'ordre n'est pas valide.";
    public static final String MSG_RASSEMB_VAL_ORDREG = "La date de l'ordre ne peut pas être après la date d'aujoutd'hui.";
    public static final String MSG_RASSEMB_VAL_ORDREP = "La date de l'ordre ne peut pas être avant le 01.01.1948.";
    public static final String MSG_RASSEMB_VAL_RCI = "La date de demande du second RCI n'est pas valide.";
    public static final String MSG_RASSEMB_VAL_REVOC = "La date de révocation n'est pas valide.";
    // message CIRevenuSplitting._beforeAdd(transaction)
    public static final String MSG_REVENU_ADD_ETAT = "Impossible d'effectuer un ajout dans l'état actuel du mandat.";
    // message CIRevenuSplitting._beforeDelete(transaction)
    public static final String MSG_REVENU_DEL_ETAT = "Impossible de supprimer un revenu dans l'état actuel du mandat.";
    // message CIRevenuSplitting._beforeUpdate(transaction)
    public static final String MSG_REVENU_MOD_ETAT = "Impossible d'effectuer une modification dans l'état actuel du mandat.";
    // CIREVENUSPLITTING
    // message CIRevenuSplitting._validate(statement)
    public static final String MSG_REVENU_VAL_ANNEE = "L'année est incorrecte.";
    public static final String MSG_REVENU_VAL_COTI = "Impossible d'enter une cotisation après 1969";
    public static final String MSG_REVENU_VAL_DATE = "L'année doit être comprise entre les années de début et de fin du mandat ";
    public static final String MSG_REVENU_VAL_EXIST = "L'année spécifiée a déjà été entrée utlérieurement.";
    public static final String MSG_REVENU_VAL_REVENU = "La saisie des revenus est possible seulement si le genre de splitting est '{0}' ou '{1}'";
    public static final String MSG_REVENU_VAL_XOR = "Le montant de la cotisation OU le revenu doit être spécifié.";
    public static final String MSG_TIERS_INVALID = "Le tiers n'a pas pu être lu.";

}
