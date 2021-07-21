package globaz.hercule.mappingXmlml;

/**
 * @author SCO
 * @since 2 déc. 2010
 */
public interface ICEListeColumns {

    // d'impression
    public static final String ANCIENNE_CATEGORIE = "ancienneCategorie";
    public static final String ANCIENNE_MASSE = "ancienneMasse";
    public static final String ANNEE_PREVUE = "anneePrevue";
    public static final String BRANCHE_ECONOMIQUE = "branche";
    public static final String CASE_POSTALE = "casePostale";
    public static final String CATEGORIE = "categorie";
    // arrière
    public static final String CI = "nbCI"; // le nombre de CI 1 ans en arrière
    // arrière
    public static final String CI_1 = "nbCI1"; // le nombre de CI 1 ans en
    // arrière
    public static final String CI_2 = "nbCI2"; // le nombre de CI 2 ans en
    // arrière
    public static final String CI_3 = "nbCI3"; // le nombre de CI 3 ans en
    // arrière
    public static final String CI_4 = "nbCI4"; // le nombre de CI 4 ans en
    // années en arrière
    public static final String CI_5 = "nbCI5"; // le nombre de CI 5 ans en
    public static final String CODE_NOGA = "noga";
    // reprise si
    // elle est
    // négative
    public static final String CORRECTION = "correction"; // Si il y a eu des
    public static final String CVS = "cvs";
    public static final String DATE_AFFILIATION = "dateAffiliation";
    public static final String DATE_DERNIER_CONTROLE = "dateDernierControle";
    public static final String DATE_DERNIER_RAPPORT = "dateRapport";
    public static final String DATE_EFFECTIVE = "dateEffective";
    // prochain
    // coontrôle, le
    // 01.01. de l'année
    // suivante si pas
    // renseignée
    public static final String DATE_IMPRESSION = "dateImpression"; // la date
    public static final String DATE_PARTICULARITE_SANS_PERSONNEL = "personnel";
    public static final String DATE_PREVUE = "datePrevue"; // la date prévue du
    public static final String DATE_RADIATION = "dateRadiation";
    public static final String DEBUT_PERIODE_AFFILIATION = "debutPeriodeAffiliation";
    // première
    // période
    // de
    // controle
    // ou de
    // couverture
    public static final String DERNIERE_PERIODE = "dernierePeriode"; // la
    public static final String ERREUR = "erreur";
    public static final String FIN_PERIODE_AFFILIATION = "finPeriodeAffiliation";
    public static final String HEADER_A_EFFECTUER = "headerAEffectuer";
    public static final String HEADER_ANCIENNE_CATEGORIE = "headerAncienneCategorie";
    public static final String HEADER_ANCIENNE_MASSE = "headerAncienneMasse";
    public static final String HEADER_ANNEE = "headerAnnee";
    public static final String HEADER_ANNEE_COMPTEUR = "headerAnneeCptr";
    public static final String HEADER_BLANK_1 = "headerBlank1";
    public static final String HEADER_BLANK_2 = "headerBlank2";

    public static final String HEADER_BLANK_3 = "headerBlank3";
    public static final String HEADER_CATEGORIE = "headerCategorie";
    public static final String HEADER_COLONNE_MASSE_SALARIALE = "headerColonneMasseSalariale";
    public static final String HEADER_CONT_5_POURCENT = "headerControle5PourCent";
    public static final String HEADER_CONT_EXTRA = "headerControleExtraOrdinaire";
    public static final String HEADER_CONT_RESTANT = "headerControlesRestant";
    public static final String HEADER_CONT_NCC = "headerNombreNCC";
    public static final String HEADER_DATE_RADIATION = "headerDateRadiation";
    // header
    public static final String HEADER_DATE_VISA = "DATE_VISA";
    public static final String HEADER_FROM_DATE_IMPRESSION = "headerFromDateImpr";
    public static final String HEADER_GENRE = "headerGenre";
    public static final String HEADER_MASSE = "headerMasse";
    public static final String HEADER_MASSE_AF = "headerMasseAF";
    public static final String HEADER_MASSE_1 = "headerMasse1";
    public static final String HEADER_MASSE_2 = "headerMasse2";
    public static final String HEADER_MASSE_3 = "headerMasse3";
    public static final String HEADER_MASSE_4 = "headerMasse4";
    public static final String HEADER_MASSE_5 = "headerMasse5";
    public static final String HEADER_MASSE_SALARIALE = "headerMasseSalariale";
    public static final String HEADER_NB_CI_1 = "headerNbci1";
    public static final String HEADER_NB_CI_2 = "headerNbci2";
    public static final String HEADER_NB_CI_3 = "headerNbci3";
    public static final String HEADER_NB_CI_4 = "headerNbci4";
    public static final String HEADER_NB_CI_5 = "headerNbci5";
    public static final String HEADER_NB_MINI = "headerNombreMinimum";
    public static final String HEADER_NUM_AFFILIE = "headerNumAffilie";
    public static final String HEADER_NUM_INFOROM = "headerNumInforom";
    public static final String HEADER_REVISEUR = "headerReviseur";
    public static final String HEADER_TO_DATE_IMPRESSION = "headerToDateImpr";
    public static final String HEADER_VISA_REVISEUR = "headerVisaReviseur";
    public static final String[] listeNoms = { ICEListeColumns.NUM_AFFILIE, ICEListeColumns.NUM_IDE,
            ICEListeColumns.NOM, ICEListeColumns.NPA, ICEListeColumns.RUE, ICEListeColumns.LOCALITE,
            ICEListeColumns.CASE_POSTALE, ICEListeColumns.DEBUT_PERIODE_AFFILIATION,
            ICEListeColumns.FIN_PERIODE_AFFILIATION, ICEListeColumns.PERIODE_AFFILIATION, ICEListeColumns.NOM_GROUPE,
            ICEListeColumns.DATE_DERNIER_CONTROLE, ICEListeColumns.PERIODE_CONTROLEE,
            ICEListeColumns.SANS_PERSONNEL_DEPUIS, ICEListeColumns.ANNEE_PREVUE, ICEListeColumns.REVISEUR,
            ICEListeColumns.DATE_EFFECTIVE, ICEListeColumns.BRANCHE_ECONOMIQUE, ICEListeColumns.CODE_NOGA,
            ICEListeColumns.DATE_PARTICULARITE_SANS_PERSONNEL, ICEListeColumns.DATE_DERNIER_RAPPORT,
            ICEListeColumns.NUMERO_RAPPORT, ICEListeColumns.PREMIERE_PERIODE, ICEListeColumns.DERNIERE_PERIODE,
            ICEListeColumns.MASSE_AVS, ICEListeColumns.MASSE_AVS_NEG, ICEListeColumns.CORRECTION,
            ICEListeColumns.TEMPS_JOURS, ICEListeColumns.NOTE_DERNIERE_REVISION, ICEListeColumns.NOTE_QUALITE_RH,
            ICEListeColumns.NOTE_COLLABORATION, ICEListeColumns.NOTE_CRITERES_SPECIAUX, ICEListeColumns.NOTE_TOTAL,
            ICEListeColumns.MASSE_5, ICEListeColumns.MASSE_4, ICEListeColumns.MASSE_3, ICEListeColumns.MASSE_2,
            ICEListeColumns.MASSE_1, ICEListeColumns.MASSE, ICEListeColumns.MASSE_AF, ICEListeColumns.CI_5,
            ICEListeColumns.CI_4, ICEListeColumns.CI_3, ICEListeColumns.CI_2, ICEListeColumns.CI_1, ICEListeColumns.CI,
            ICEListeColumns.CVS, ICEListeColumns.TYPE_CONTROLE, ICEListeColumns.DATE_PREVUE,
            ICEListeColumns.DATE_IMPRESSION, ICEListeColumns.ANCIENNE_CATEGORIE, ICEListeColumns.ANCIENNE_MASSE,
            ICEListeColumns.CATEGORIE, ICEListeColumns.DATE_AFFILIATION, ICEListeColumns.DATE_RADIATION,
            ICEListeColumns.MASSE_SALARIALE, ICEListeColumns.MOTIF_RADIATION, ICEListeColumns.CAISSE_AVS };
    public static final String LOCALITE = "localite";
    // années en arrière
    public static final String MASSE = "masse"; // la masse salariale de 1
    // années en arrière
    public static final String MASSE_1 = "masse1"; // la masse salariale de 1
    // années en arrière
    public static final String MASSE_2 = "masse2"; // la masse salariale de 2
    // années en arrière
    public static final String MASSE_3 = "masse3"; // la masse salariale de 3
    // années en arrière
    public static final String MASSE_4 = "masse4"; // la masse salariale de 4
    public static final String MASSE_5 = "masse5"; // la masse salariale de 5
    // dernière
    // période
    // de
    // controle
    // ou de
    // couverture
    public static final String MASSE_AF = "masse_AF"; // la masse AF
    public static final String MASSE_AVS = "masseAVS"; // la masse AVS reprise
    public static final String MASSE_AVS_NEG = "masseAVSNeg"; // la masse AVS
    public static final String MASSE_SALARIALE = "masseSalariale";
    public static final String MOTIF_RADIATION = "motifRadiation";
    public static final String NOM = "nom";
    public static final String NOM_GROUPE = "nomGroupe";
    // qualité RH
    public static final String NOTE_COLLABORATION = "note3"; // la note de la
    // colaboration
    public static final String NOTE_CRITERES_SPECIAUX = "note4"; // la note de
    // précédent contrôle
    public static final String NOTE_DERNIERE_REVISION = "note1"; // la note de
    // la
    // dernière
    // révision
    public static final String NOTE_QUALITE_RH = "note2"; // la note de la
    // des
    // critères
    // spéciaux
    // de
    // l'entreprise
    public static final String NOTE_TOTAL = "totalnotes"; // le total des notes
    public static final String NPA = "npa";
    // detail
    public static final String NUM_AFFILIE = "numAffilie";
    public static final String NUM_IDE = "numeroIde";
    public static final String NUMERO_RAPPORT = "numRapport";
    public static final String PERIODE_AFFILIATION = "periodeAffiliation";
    public static final String PERIODE_CONTROLEE = "periodeControlee";
    public static final String PREMIERE_PERIODE = "premierePeriode"; // la
    public static final String REVISEUR = "reviseur";
    public static final String RUE = "rue";
    public static final String SANS_PERSONNEL_DEPUIS = "sansPersonnelDepuis";
    // corrections sur
    // le controle
    // d'employeur
    public static final String TEMPS_JOURS = "temps"; // le temps en jour du

    public static final String TYPE_CONTROLE = "typeControle";
    public static final String CAISSE_AVS = "caisseAVS";
    public static final String CODE_SUVA = "codeSUVA";
    public static final String LIBELLE_SUVA = "libelleSUVA";

    // Header pour internationalisation
    public static final String HEADER_COLONNE_LABEL_NUM_AFFILIE = "headerColonneLabelNumAffilie";
    public static final String HEADER_COLONNE_LABEL_NOM = "headerColonneLabelNom";
    public static final String HEADER_COLONNE_LABEL_RUE = "headerColonneLabelRue";
    public static final String HEADER_COLONNE_LABEL_CASE_POSTALE = "headerColonneLabelCasePostale";
    public static final String HEADER_COLONNE_LABEL_NPA = "headerColonneLabelNpa";
    public static final String HEADER_COLONNE_LABEL_LOCALITE = "headerColonneLabelLocalite";
    public static final String HEADER_COLONNE_LABEL_GROUPE = "headerColonneLabelGroupe";
    public static final String HEADER_COLONNE_LABEL_PERIODE_AFFILIATION = "headerColonneLabelPeriodeAffiliation";
    public static final String HEADER_COLONNE_LABEL_CAISSE_AVS = "headerColonneLabelCaisseAvs";
    public static final String HEADER_COLONNE_LABEL_CATEGORIE = "headerColonneLabelCategorie";
    public static final String HEADER_COLONNE_LABEL_MASSE_AVS_1 = "headerColonneLabelMasseAvs1";
    public static final String HEADER_COLONNE_LABEL_MASSE_AVS_2 = "headerColonneLabelMasseAvs2";
    public static final String HEADER_COLONNE_LABEL_MASSE_AVS_3 = "headerColonneLabelMasseAvs3";
    public static final String HEADER_COLONNE_LABEL_MASSE_AVS_4 = "headerColonneLabelMasseAvs4";
    public static final String HEADER_COLONNE_LABEL_MASSE_AVS_5 = "headerColonneLabelMasseAvs5";
    public static final String HEADER_COLONNE_LABEL_CI_1 = "headerColonneLabelCI1";
    public static final String HEADER_COLONNE_LABEL_CI_2 = "headerColonneLabelCI2";
    public static final String HEADER_COLONNE_LABEL_CI_3 = "headerColonneLabelCI3";
    public static final String HEADER_COLONNE_LABEL_CI_4 = "headerColonneLabelCI4";
    public static final String HEADER_COLONNE_LABEL_CI_5 = "headerColonneLabelCI5";
    public static final String HEADER_COLONNE_LABEL_MASSE_AF = "headerColonneLabelMasseAf";
    public static final String HEADER_COLONNE_LABEL_CODE_SUVA = "headerColonneLabelCodeSuva";
    public static final String HEADER_COLONNE_LABEL_LIBELLE_SUVA = "headerColonneLabelLibelleSuva";
    public static final String HEADER_LABEL_NOM_LISTE = "headerLabelNomListe";
    public static final String HEADER_LABEL_NUM_AFFILIES = "headerLabelNumAffilies";
    public static final String HEADER_LABEL_ANNEE = "headerLabelAnnee";
    public static final String HEADER_LABEL_NB_MINI_CONTROLER = "headerLabelNbMiniControler";
    public static final String HEADER_LABEL_CONTROLE_EXTRA = "headerLabelControleExtra";
    public static final String HEADER_LABEL_CONTROLE_5 = "headerLabelControle5";
    public static final String HEADER_LABEL_CONTROLE_RESTANT = "headerLabelControleRestant";
    public static final String HEADER_COLONNE_LABEL_TYPE_CONTROLE = "headerColonneTypeControle";
    public static final String HEADER_COLONNE_LABEL_DATE_EFFECTIVE = "headerColonneDateEffective";
    public static final String HEADER_COLONNE_LABEL_DATE_PREVU = "headerColonneDatePrevue";

}
