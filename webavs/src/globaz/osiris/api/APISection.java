package globaz.osiris.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import globaz.osiris.exceptions.CATechnicalException;

/**
 * Date de création : (31.01.2002 16:04:24)
 * 
 */
public interface APISection extends BIEntity {

    public static String[] CATEGORIE_SECTION = { APISection.ID_CATEGORIE_SECTION_DECOMPTE_TRANSFERT,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_JANVIER, APISection.ID_CATEGORIE_SECTION_DECOMPTE_FEVRIER,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_MARS, APISection.ID_CATEGORIE_SECTION_DECOMPTE_AVRIL,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_MAI, APISection.ID_CATEGORIE_SECTION_DECOMPTE_JUIN,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_JUILLET, APISection.ID_CATEGORIE_SECTION_DECOMPTE_AOUT,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_SEPTEMBRE, APISection.ID_CATEGORIE_SECTION_DECOMPTE_OCTOBRE,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_NOVEMBRE, APISection.ID_CATEGORIE_SECTION_DECOMPTE_DECEMBRE,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_FINAL, APISection.ID_CATEGORIE_SECTION_BOUCLEMENT_ACOMPTE,
            APISection.ID_CATEGORIE_SECTION_ALLOCATIONS_FAMILIALES, APISection.ID_CATEGORIE_SECTION_APG,
            APISection.ID_CATEGORIE_SECTION_CONTROLE_EMPLOYEUR,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_COMPLEMENTAIRE,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_COMPLEMENTAIRE_TSE,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_SALAIRES_DIFFERES,
            APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS, APISection.ID_CATEGORIE_SECTION_IJAI,
            APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS_ETUDIANT, APISection.ID_CATEGORIE_SECTION_DECOMPTE_PCG,
            APISection.ID_CATEGORIE_SECTION_IRRECOUVRABLE,
            APISection.ID_CATEGORIE_SECTION_RECOUVREMENT_IRRECOUVRABLE,
            APISection.ID_CATEGORIE_SECTION_RESTITUTIONS,
            APISection.ID_CATEGORIE_SECTION_RETENU_SUR_RENTE,
            APISection.ID_CATEGORIE_SECTION_NETENTREPRISE,
            APISection.ID_CATEGORIE_SECTION_DECISION_DE_TAXATION_OFFICE,
            APISection.ID_CATEGORIE_SECTION_FACTURATION_AMENDE,
            APISection.ID_CATEGORIE_SECTION_AVANCE,
            APISection.ID_CATEGORIE_SECTION_LTN,
            APISection.ID_CATEGORIE_SECTION_LTN_COMPLEMENTAIRE,
            APISection.ID_CATEGORIE_SECTION_ICI,
            APISection.ID_CATEGORIE_SECTION_DIVIDENDE,
            APISection.ID_CATEGORIE_SECTION_DECISION_ANNUELLE,
            APISection.ID_CATEGORIE_SECTION_DECISION_1TRIMESTRE,
            APISection.ID_CATEGORIE_SECTION_DECISION_2TRIMESTRE,
            APISection.ID_CATEGORIE_SECTION_DECISION_3TRIMESTRE,
            APISection.ID_CATEGORIE_SECTION_DECISION_4TRIMESTRE,
            APISection.ID_CATEGORIE_SECTION_DECISION_ANNUELLE_1TRIMESTRE,
            APISection.ID_CATEGORIE_SECTION_FACTURE_ANNUELLE_30JUIN,
            APISection.ID_CATEGORIE_SECTION_DECISION_ANNUELLE_3TRIMESTRE,
            APISection.ID_CATEGORIE_SECTION_INTERET_SUR_COTISATIONS_ARRIEREES_SUR_SECTION_SEPAREE,
            APISection.ID_CATEGORIE_SECTION_DECISION_1SEMESTRE,
            APISection.ID_CATEGORIE_SECTION_DECISION_2SEMESTRE,
            APISection.ID_CATEGORIE_SECTION_PAIEMENT_PRINCIPAL, // Utilisé pour les rentes
            APISection.ID_CATEGORIE_SECTION_DECISION, // Utilisé pour les rentes
            APISection.ID_CATEGORIE_SECTION_RETOUR, // Utilisé pour les rentes
            APISection.ID_CATEGORIE_SECTION_PRESTATIONS_BLOQUEES, // Utilisé pour les
            APISection.ID_CATEGORIE_SECTION_DECISION_CAP_CGAS, APISection.ID_CATEGORIE_SECTION_PAIEMENT_AVANCE,
            APISection.ID_CATEGORIE_SECTION_DECISION_RESTITUTION, APISection.ID_CATEGORIE_SECTION_REPARATION_DOMMAGES,
            APISection.ID_CATEGORIE_SECTION_DECISION_PC, APISection.ID_CATEGORIE_SECTION_DECISION_RFM,
            APISection.ID_CATEGORIE_SECTION_DECISION_REGIME, APISection.ID_CATEGORIE_SECTION_DECOMPTE_SPECIAL,
            APISection.ID_CATEGORIE_SECTION_PREST_CONV_CONGE_PAYE,
            APISection.ID_CATEGORIE_SECTION_PREST_CONV_SERVICE_MILITAIRE,
            APISection.ID_CATEGORIE_SECTION_PREST_CONV_ABSENCE_JUSTIFIEE};

    // AJPPCOU -> pcouid
    public static String CATEGORIE_SECTION_AF = "15";
    public static String CATEGORIE_SECTION_APG = "16";
    public static String CATEGORIE_SECTION_AVANCE = "32";
    public static String CATEGORIE_SECTION_IJAI = "21";
    public static String CATEGORIE_SECTION_PCF_DECISION = "71";
    public static String CATEGORIE_SECTION_PCF_PP = "70";
    public static String CATEGORIE_SECTION_PCF_RESTITUTION = "26";
    public static String CATEGORIE_SECTION_PCF_RFM = "78";
    public static String CATEGORIE_SECTION_RENTES = "28";
    public static String CATEGORIE_SECTION_RESTITUTIONS = "26";
    public static String CATEGORIE_SECTION_RETOUR = "72";

    // Constante pour description des sections d'intérêts
    public final static String DEC_INT_MORATOIRE_1_SEMESTRE = "256061";
    public final static String DEC_INT_MORATOIRE_2_SEMESTRE = "256062";
    public final static String DEC_INT_MORATOIRE_ANNUEL = "256040";
    public final static String DEC_INT_MORATOIRE_AOUT = "256008";
    public final static String DEC_INT_MORATOIRE_AVRIL = "256004";
    public final static String DEC_INT_MORATOIRE_BOUCL_ACOMPTE = "256014";
    public final static String DEC_INT_MORATOIRE_COMPLEMENTAIRE = "256018";
    public final static String DEC_INT_MORATOIRE_CONT_EMP = "256017";
    public final static String DEC_INT_MORATOIRE_COT_PERS = "256020";
    public final static String DEC_INT_MORATOIRE_COT_PERS_ETU = "256022";
    public final static String DEC_INT_MORATOIRE_DEC_FINAL = "256013";
    public final static String DEC_INT_MORATOIRE_DECEMBRE = "256012";
    public final static String DEC_INT_MORATOIRE_FEVRIER = "256002";
    public final static String DEC_INT_MORATOIRE_FINAL_LTN = "256033";
    public final static String DEC_INT_MORATOIRE_JANVIER = "256001";
    public final static String DEC_INT_MORATOIRE_JUILLET = "256007";
    public final static String DEC_INT_MORATOIRE_JUIN = "256006";
    public final static String DEC_INT_MORATOIRE_MAI = "256005";
    public final static String DEC_INT_MORATOIRE_MARS = "256003";
    public final static String DEC_INT_MORATOIRE_NOVEMBRE = "256011";
    public final static String DEC_INT_MORATOIRE_OCTOBRE = "256010";
    public final static String DEC_INT_MORATOIRE_SEPTEMBRE = "256009";
    public final static String DEC_INT_MORATOIRE_TO = "256030";

    public final static String ID_CATEGORIE_SECTION_ALLOCATIONS_FAMILIALES = "227015";
    public final static String ID_CATEGORIE_SECTION_APG = "227016";
    public final static String ID_CATEGORIE_SECTION_AVANCE = "227032";
    public final static String ID_CATEGORIE_SECTION_BOUCLEMENT_ACOMPTE = "227014";
    public final static String ID_CATEGORIE_SECTION_CONTROLE_EMPLOYEUR = "227017";

    // pour les rentes
    public final static String ID_CATEGORIE_SECTION_DECISION = "227071"; // Utilisé
    public final static String ID_CATEGORIE_SECTION_DECISION_1SEMESTRE = "227061";
    public final static String ID_CATEGORIE_SECTION_DECISION_1TRIMESTRE = "227041";
    public final static String ID_CATEGORIE_SECTION_DECISION_2SEMESTRE = "227062";
    public final static String ID_CATEGORIE_SECTION_DECISION_2TRIMESTRE = "227042";
    public final static String ID_CATEGORIE_SECTION_DECISION_3TRIMESTRE = "227043";
    public final static String ID_CATEGORIE_SECTION_DECISION_4TRIMESTRE = "227044";
    public final static String ID_CATEGORIE_SECTION_DECISION_ANNUELLE = "227040";
    public final static String ID_CATEGORIE_SECTION_DECISION_ANNUELLE_1TRIMESTRE = "227045";
    public final static String ID_CATEGORIE_SECTION_DECISION_ANNUELLE_3TRIMESTRE = "227047";
    public final static String ID_CATEGORIE_SECTION_DECISION_CAP_CGAS = "227088";
    public final static String ID_CATEGORIE_SECTION_DECISION_COTPERS = "227020";
    public final static String ID_CATEGORIE_SECTION_DECISION_COTPERS_ETUDIANT = "227022";
    public final static String ID_CATEGORIE_SECTION_DECISION_DE_TAXATION_OFFICE = "227030";
    public final static String ID_CATEGORIE_SECTION_DECISION_PC = "227075";
    public final static String ID_CATEGORIE_SECTION_DECISION_PCF_DECISION = "227071";
    public final static String ID_CATEGORIE_SECTION_DECISION_PCF_PP = "227070";
    public final static String ID_CATEGORIE_SECTION_DECISION_PCF_RESTITUTION = "227026";
    public final static String ID_CATEGORIE_SECTION_DECISION_PCF_RFM = "227078";
    public final static String ID_CATEGORIE_SECTION_DECISION_REGIME = "227077";
    public final static String ID_CATEGORIE_SECTION_DECISION_RESTITUTION = "227095";
    public final static String ID_CATEGORIE_SECTION_DECISION_RFM = "227076";
    // ajout fha : 227078 -> pour les remboursement de la DSAS à la caisse
    public final static String ID_CATEGORIE_SECTION_DECISION_RFM_REMBOURSEMENT_CAISSE_DSAS = "227078";
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_AOUT = "227008";
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_AVRIL = "227004";
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_COMPLEMENTAIRE = "227018";
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_COMPLEMENTAIRE_TSE = "227019";
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_DECEMBRE = "227012";
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_FEVRIER = "227002";
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_FINAL = "227013";
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_JANVIER = "227001";
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_JUILLET = "227007";
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_JUIN = "227006";
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_MAI = "227005";
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_MARS = "227003";
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_NOVEMBRE = "227011";
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_OCTOBRE = "227010";
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_PCG = "227023";
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_SALAIRES_DIFFERES = "227036";
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_SEPTEMBRE = "227009";
    // Code système de catégorie de section
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_TRANSFERT = "227000";
    public final static String ID_CATEGORIE_SECTION_DIVIDENDE = "227035";
    public final static String ID_CATEGORIE_SECTION_FACTURATION_AMENDE = "227031";
    public final static String ID_CATEGORIE_SECTION_FACTURE_ANNUELLE_30JUIN = "227046";
    public final static String ID_CATEGORIE_SECTION_ICI = "227034";
    public final static String ID_CATEGORIE_SECTION_IJAI = "227021";
    public final static String ID_CATEGORIE_SECTION_INTERET_SUR_COTISATIONS_ARRIEREES_SUR_SECTION_SEPAREE = "227050";
    public final static String ID_CATEGORIE_SECTION_IRRECOUVRABLE = "227024";
    public final static String ID_CATEGORIE_SECTION_LTN = "227033";
    public final static String ID_CATEGORIE_SECTION_LTN_COMPLEMENTAIRE = "227038";
    public final static String ID_CATEGORIE_SECTION_NETENTREPRISE = "227029";
    // Pour les rentes
    public final static String ID_CATEGORIE_SECTION_PAIEMENT_AVANCE = "227080";
    public final static String ID_CATEGORIE_SECTION_PAIEMENT_PRINCIPAL = "227070"; // Utilisé
    
    
    /**
     * Catégorie pour le BMS
     */
    public final static String ID_CATEGORIE_SECTION_PREST_CONV_ABSENCE_JUSTIFIEE = "227091";
    public final static String ID_CATEGORIE_SECTION_PREST_CONV_CONGE_PAYE = "227092";
    public final static String ID_CATEGORIE_SECTION_PREST_CONV_SERVICE_MILITAIRE = "227093";
    public final static String ID_CATEGORIE_SECTION_DECOMPTE_SPECIAL = "227099";    // Pour les rentes
    public final static String ID_CATEGORIE_SECTION_PRESTATIONS_BLOQUEES = "227074"; // Utilisé
    public final static String ID_CATEGORIE_SECTION_RECOUVREMENT_IRRECOUVRABLE = "227025";
    public final static String ID_CATEGORIE_SECTION_REPARATION_DOMMAGES = "227096";
    public final static String ID_CATEGORIE_SECTION_RESTITUTIONS = "227026";
    public final static String ID_CATEGORIE_SECTION_RETENU_SUR_RENTE = "227028";
    // Pour les rentes
    public final static String ID_CATEGORIE_SECTION_RETOUR = "227072"; // Utilisé
    // Type de section
    public static String ID_TYPE_SECTION_AF = "15";
    public static String ID_TYPE_SECTION_ANCIENNE_FACTURE = "8";
    public static String ID_TYPE_SECTION_APG = "16";
    public static String ID_TYPE_SECTION_ARD = "96";
    public static String ID_TYPE_SECTION_AVANCE_RFM = "77";
    public static String ID_TYPE_SECTION_AVANCES = "7";
    public static String ID_TYPE_SECTION_BLOCAGE = "6";
    public static String ID_TYPE_SECTION_BULLETIN_NEUTRE = "81";
    public static String ID_TYPE_SECTION_DECISION_PC = "75";
    public static String ID_TYPE_SECTION_DECOMPTE_CAP_CGAS = "88";
    public static String ID_TYPE_SECTION_DECOMPTE_COTISATION = "1";
    public static String ID_TYPE_SECTION_ETUDIANTS = "2";
    public static String ID_TYPE_SECTION_FACTURATION_AUX_ORGANES_EXTERNES = "69";
    public static String ID_TYPE_SECTION_FCF = "61";
    public static String ID_TYPE_SECTION_IJAI = "21";
    public static String ID_TYPE_SECTION_PC = "79";
    public static String ID_TYPE_SECTION_PRESTATIONS_CONVENTIONNELLES = "29";
    public static String ID_TYPE_SECTION_RENTE_AVS_AI = "4";
    public static String ID_TYPE_SECTION_RESTITUTION = "26";
    public static String ID_TYPE_SECTION_RETOUR = "5";
    public static String ID_TYPE_SECTION_RFM = "76";

    // Mode de compensation
    public final static String MODE_BLOQUER_COMPENSATION = "244001";
    public final static String MODE_COMP_COMPLEMENTAIRE = "244006";
    public final static String MODE_COMP_CONT_EMPLOYEUR = "244005";
    public final static String MODE_COMP_COT_PERS = "244004";
    public final static String MODE_COMP_DEC_FINAL = "244003";
    public final static String MODE_COMP_DEC_PERIODIQUE = "244007";
    public static final String MODE_COMPENSATION_STANDARD = "0";
    public final static String MODE_REPORT = "244002";

    // Convention : Les sections pour les intérêts moratoires sont identifiés
    // par le 9
    public final static String SECTION_900_INTERET_MORATOIRE = "900";

    // Statut de suspension de procédure d'encaissement
    public final static String STATUTBN_ANNULE = "257001";
    public final static String STATUTBN_DECOMPTE_FINAL = "257003";
    public final static String STATUTBN_REACTIVE = "257002";

    public String getAmende();

    public String getBase();

    public String getCategorieSection();

    /**
     * 
     * @return String
     */
    public APICompteAnnexe getCompteAnnexe();

    public Boolean getContentieuxEstSuspendu();

    public String getDateDebutPeriode();

    public String getDateEcheance();

    public String getDateFinPeriode();

    /**
     * 
     * @return String
     */
    public String getDateReferenceContentieux(APIParametreEtape etape);

    public String getDateSection();

    /**
     * Cette méthode retourne la date de fin de blocage du contentieux (Osiris) elle ne peut pas être utilisée avec le
     * contentieux Aquila
     * 
     * @return String date de fin de suspension du contentieux
     */
    public String getDateSuspendu();

    public String getDescription();

    /**
     * @return le domaine d'adresse où la facture a été envoyée
     */
    public String getDomaine();

    public String getFrais();

    public String getIdCompteAnnexe();

    public String getIdExterne();

    public String getIdJournal();

    /**
     * Cette méthode retourne le code système de l'état du contentieux Aquila
     * 
     * @return String idLastEtatAquila
     */
    public String getIdLastEtatAquila();

    /**
     * Cette méthode permet de récupérer le mode de compensation de la section Définit si une section peut être
     * compensée ou non
     * 
     * @return String idModeCompensation
     */
    public String getIdModeCompensation();

    public String getIdMotifContentieuxSuspendu();

    /**
     * Cette méthode retourne l'identifiant du passage dans lequel le report de la section est pris en compte
     * 
     * @return String idPassageComp
     */
    public String getIdPassageComp();

    public String getIdSection();

    public String getIdTypeSection();

    public String getInterets();

    /**
     * 
     * @return String
     */
    public String getNumeroPoursuite();

    public String getPmtCmp();

    public String getSolde();

    /**
     * Cette méthode retourne le solde de la section formatté
     * 
     * @return String solde de la section formaté
     */
    public String getSoldeFormate();

    public String getTaxes();

    /**
     * @return le type d'adresse où la facture a été envoyée
     */
    public String getTypeAdresse();

    /**
     * Cette méthode retourne le type de la section
     * 
     * @return String type de la section
     */
    public APITypeSection getTypeSection();

    /**
     * Un motif de blocage (idModifBlocage) existe-t'il pour l'année (year) pour cette section ?
     * 
     * @param idModifBlocage
     * @param year
     * @return
     * @throws CATechnicalException
     */
    public boolean hasMotifContentieuxForYear(String idModifBlocage, String year) throws CATechnicalException;

    /**
     * 
     * @return boolean
     */
    boolean hasOperations();

    /**
     * Cette méthode retourne true si la section est au contentieux.<br/>
     * Si le dossier contentieux est créé mais que l'étape est "Aucune" (par exemple lors d'un report de délai de
     * paiement), la section n'est pas considérée comme étant au contentieux.<br/>
     * Les étapes rappel et sommation sont considérées comme faisant partie du contentieux. <br/>
     * Si la poursuite est radiée, la section est considérée comme étant au contentieux.
     * 
     * @return <code>true</code> si la section est au contentieux.
     */
    public boolean isSectionAuContentieux();

    /**
     * @param soldeOuvert
     *            (Boolean) : <br />
     *            <code>true</code> pour ne prendre que les sections qui ont un solde > 0. <br/>
     *            <code>false</code> prend toutes les sections indépendamment du solde.
     * @return <code>true</code> si la section est aux poursuites (RP et ss) et n'est pas radiée.
     */
    public Boolean isSectionAuxPoursuitesNotRadiee(Boolean soldeOuvert);

    void retrieve() throws Exception;

    void retrieve(BITransaction transaction) throws Exception;

    void setAlternateKey(int altKey);

    public void setIdExterne(String newIdExterne);

    /**
     * Set le mode de compensation de la section
     * 
     * @param idModeCompensation
     *            (Code Système)
     */
    public void setIdModeCompensation(String idModeCompensation);

    /**
     * Cette méthode permet de setter l'identifiant du passage dans lequel le report de la section est pris en compte
     * 
     * @param String
     *            idPassageComp
     */
    public void setIdPassageComp(String idPassageComp);

    void setIdSection(String newIdSection);

    public void setIdTypeSection(String newIdTypeSection);

    /**
     * Met à jour la caisse professionnelle de la section
     * 
     * @param idCaisseProfessionnelle
     * @throws Exception
     */
    void updateCaisseProf(String idCaisseProfessionnelle) throws Exception;

    /**
     * Cette méthode permet de mettre à jour les zones suivantes : <br>
     * <li>idModeCompensation</li> <li>idPassageComp</li> Un retrieve est effectué avant de faire la mise à jour des
     * deux zones ci-dessus <br>
     * 
     * @param idModeCompensation
     * @param idPassageComp
     * @throws Exception
     */
    public void updateInfoCompensation(String idModeCompensation, String idPassageComp) throws Exception;
}
