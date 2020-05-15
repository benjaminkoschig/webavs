package globaz.osiris.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import globaz.osiris.exceptions.CATechnicalException;

/**
 * Date de création : (31.01.2002 16:04:24)
 * 
 */
public interface APISection extends BIEntity {

    String[] CATEGORIE_SECTION = {APISection.ID_CATEGORIE_SECTION_DECOMPTE_TRANSFERT,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_JANVIER, APISection.ID_CATEGORIE_SECTION_DECOMPTE_FEVRIER,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_MARS, APISection.ID_CATEGORIE_SECTION_DECOMPTE_AVRIL,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_MAI, APISection.ID_CATEGORIE_SECTION_DECOMPTE_JUIN,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_JUILLET, APISection.ID_CATEGORIE_SECTION_DECOMPTE_AOUT,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_SEPTEMBRE, APISection.ID_CATEGORIE_SECTION_DECOMPTE_OCTOBRE,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_NOVEMBRE, APISection.ID_CATEGORIE_SECTION_DECOMPTE_DECEMBRE,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_FINAL, APISection.ID_CATEGORIE_SECTION_BOUCLEMENT_ACOMPTE,
            APISection.ID_CATEGORIE_SECTION_ALLOCATIONS_FAMILIALES, APISection.ID_CATEGORIE_SECTION_APG, APISection.ID_CATEGORIE_SECTION_PANDEMIE,
            APISection.ID_CATEGORIE_SECTION_CONTROLE_EMPLOYEUR,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_COMPLEMENTAIRE,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_COMPLEMENTAIRE_TSE,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_SALAIRES_DIFFERES,
            APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS, APISection.ID_CATEGORIE_SECTION_IJAI,
            APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS_ETUDIANT, APISection.ID_CATEGORIE_SECTION_DECOMPTE_PCG,
            APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS,
            APISection.ID_CATEGORIE_SECTION_IJAI,
            APISection.ID_CATEGORIE_SECTION_DECISION_COTPERS_ETUDIANT,
            APISection.ID_CATEGORIE_SECTION_DECOMPTE_PCG,
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
    String CATEGORIE_SECTION_AF = "15";
    String CATEGORIE_SECTION_APG = "16";
    String CATEGORIE_SECTION_AVANCE = "32";
    String CATEGORIE_SECTION_IJAI = "21";
    String CATEGORIE_SECTION_PCF_DECISION = "71";
    String CATEGORIE_SECTION_PCF_PP = "70";
    String CATEGORIE_SECTION_PCF_RESTITUTION = "26";
    String CATEGORIE_SECTION_PCF_RFM = "78";
    String CATEGORIE_SECTION_RENTES = "28";
    String CATEGORIE_SECTION_RESTITUTIONS = "26";
    String CATEGORIE_SECTION_RETOUR = "72";

    // Constante pour description des sections d'intérêts
    String DEC_INT_MORATOIRE_1_SEMESTRE = "256061";
    String DEC_INT_MORATOIRE_2_SEMESTRE = "256062";
    String DEC_INT_MORATOIRE_ANNUEL = "256040";
    String DEC_INT_MORATOIRE_AOUT = "256008";
    String DEC_INT_MORATOIRE_AVRIL = "256004";
    String DEC_INT_MORATOIRE_BOUCL_ACOMPTE = "256014";
    String DEC_INT_MORATOIRE_COMPLEMENTAIRE = "256018";
    String DEC_INT_MORATOIRE_CONT_EMP = "256017";
    String DEC_INT_MORATOIRE_COT_PERS = "256020";
    String DEC_INT_MORATOIRE_COT_PERS_ETU = "256022";
    String DEC_INT_MORATOIRE_DEC_FINAL = "256013";
    String DEC_INT_MORATOIRE_DECEMBRE = "256012";
    String DEC_INT_MORATOIRE_FEVRIER = "256002";
    String DEC_INT_MORATOIRE_FINAL_LTN = "256033";
    String DEC_INT_MORATOIRE_JANVIER = "256001";
    String DEC_INT_MORATOIRE_JUILLET = "256007";
    String DEC_INT_MORATOIRE_JUIN = "256006";
    String DEC_INT_MORATOIRE_MAI = "256005";
    String DEC_INT_MORATOIRE_MARS = "256003";
    String DEC_INT_MORATOIRE_NOVEMBRE = "256011";
    String DEC_INT_MORATOIRE_OCTOBRE = "256010";
    String DEC_INT_MORATOIRE_SEPTEMBRE = "256009";
    String DEC_INT_MORATOIRE_TO = "256030";

    String ID_CATEGORIE_SECTION_ALLOCATIONS_FAMILIALES = "227015";
    String ID_CATEGORIE_SECTION_APG = "227016";
    String ID_CATEGORIE_SECTION_PANDEMIE = "227027";
    String ID_CATEGORIE_SECTION_AVANCE = "227032";
    String ID_CATEGORIE_SECTION_BOUCLEMENT_ACOMPTE = "227014";
    String ID_CATEGORIE_SECTION_CONTROLE_EMPLOYEUR = "227017";

    // pour les rentes
    String ID_CATEGORIE_SECTION_DECISION = "227071"; // Utilisé
    String ID_CATEGORIE_SECTION_DECISION_1SEMESTRE = "227061";
    String ID_CATEGORIE_SECTION_DECISION_1TRIMESTRE = "227041";
    String ID_CATEGORIE_SECTION_DECISION_2SEMESTRE = "227062";
    String ID_CATEGORIE_SECTION_DECISION_2TRIMESTRE = "227042";
    String ID_CATEGORIE_SECTION_DECISION_3TRIMESTRE = "227043";
    String ID_CATEGORIE_SECTION_DECISION_4TRIMESTRE = "227044";
    String ID_CATEGORIE_SECTION_DECISION_ANNUELLE = "227040";
    String ID_CATEGORIE_SECTION_DECISION_ANNUELLE_1TRIMESTRE = "227045";
    String ID_CATEGORIE_SECTION_DECISION_ANNUELLE_3TRIMESTRE = "227047";
    String ID_CATEGORIE_SECTION_DECISION_CAP_CGAS = "227088";
    String ID_CATEGORIE_SECTION_DECISION_COTPERS = "227020";
    String ID_CATEGORIE_SECTION_DECISION_COTPERS_ETUDIANT = "227022";
    String ID_CATEGORIE_SECTION_DECISION_DE_TAXATION_OFFICE = "227030";
    String ID_CATEGORIE_SECTION_DECISION_PC = "227075";
    String ID_CATEGORIE_SECTION_DECISION_PCF_DECISION = "227071";
    String ID_CATEGORIE_SECTION_DECISION_PCF_PP = "227070";
    String ID_CATEGORIE_SECTION_DECISION_PCF_RESTITUTION = "227026";
    String ID_CATEGORIE_SECTION_DECISION_PCF_RFM = "227078";
    String ID_CATEGORIE_SECTION_DECISION_REGIME = "227077";
    String ID_CATEGORIE_SECTION_DECISION_RESTITUTION = "227095";
    String ID_CATEGORIE_SECTION_DECISION_RFM = "227076";
    // ajout fha : 227078 -> pour les remboursement de la DSAS à la caisse
    String ID_CATEGORIE_SECTION_DECISION_RFM_REMBOURSEMENT_CAISSE_DSAS = "227078";
    String ID_CATEGORIE_SECTION_DECOMPTE_AOUT = "227008";
    String ID_CATEGORIE_SECTION_DECOMPTE_AVRIL = "227004";
    String ID_CATEGORIE_SECTION_DECOMPTE_COMPLEMENTAIRE = "227018";
    String ID_CATEGORIE_SECTION_DECOMPTE_COMPLEMENTAIRE_TSE = "227019";
    String ID_CATEGORIE_SECTION_DECOMPTE_DECEMBRE = "227012";
    String ID_CATEGORIE_SECTION_DECOMPTE_FEVRIER = "227002";
    String ID_CATEGORIE_SECTION_DECOMPTE_FINAL = "227013";
    String ID_CATEGORIE_SECTION_DECOMPTE_JANVIER = "227001";
    String ID_CATEGORIE_SECTION_DECOMPTE_JUILLET = "227007";
    String ID_CATEGORIE_SECTION_DECOMPTE_JUIN = "227006";
    String ID_CATEGORIE_SECTION_DECOMPTE_MAI = "227005";
    String ID_CATEGORIE_SECTION_DECOMPTE_MARS = "227003";
    String ID_CATEGORIE_SECTION_DECOMPTE_NOVEMBRE = "227011";
    String ID_CATEGORIE_SECTION_DECOMPTE_OCTOBRE = "227010";
    String ID_CATEGORIE_SECTION_DECOMPTE_PCG = "227023";
    String ID_CATEGORIE_SECTION_DECOMPTE_SALAIRES_DIFFERES = "227036";
    String ID_CATEGORIE_SECTION_DECOMPTE_SEPTEMBRE = "227009";
    // Code système de catégorie de section
    String ID_CATEGORIE_SECTION_DECOMPTE_TRANSFERT = "227000";
    String ID_CATEGORIE_SECTION_DIVIDENDE = "227035";
    String ID_CATEGORIE_SECTION_FACTURATION_AMENDE = "227031";
    String ID_CATEGORIE_SECTION_FACTURE_ANNUELLE_30JUIN = "227046";
    String ID_CATEGORIE_SECTION_ICI = "227034";
    String ID_CATEGORIE_SECTION_IJAI = "227021";
    String ID_CATEGORIE_SECTION_INTERET_SUR_COTISATIONS_ARRIEREES_SUR_SECTION_SEPAREE = "227050";
    String ID_CATEGORIE_SECTION_IRRECOUVRABLE = "227024";
    String ID_CATEGORIE_SECTION_LTN = "227033";
    String ID_CATEGORIE_SECTION_LTN_COMPLEMENTAIRE = "227038";
    String ID_CATEGORIE_SECTION_NETENTREPRISE = "227029";
    // Pour les rentes
    String ID_CATEGORIE_SECTION_PAIEMENT_AVANCE = "227080";
    String ID_CATEGORIE_SECTION_PAIEMENT_PRINCIPAL = "227070"; // Utilisé
    
    
    /**
     * Catégorie pour le BMS
     */
    String ID_CATEGORIE_SECTION_PREST_CONV_ABSENCE_JUSTIFIEE = "227091";
    String ID_CATEGORIE_SECTION_PREST_CONV_CONGE_PAYE = "227092";
    String ID_CATEGORIE_SECTION_PREST_CONV_SERVICE_MILITAIRE = "227093";
    String ID_CATEGORIE_SECTION_DECOMPTE_SPECIAL = "227099";    // Pour les rentes
    String ID_CATEGORIE_SECTION_PRESTATIONS_BLOQUEES = "227074"; // Utilisé
    String ID_CATEGORIE_SECTION_RECOUVREMENT_IRRECOUVRABLE = "227025";
    String ID_CATEGORIE_SECTION_REPARATION_DOMMAGES = "227096";
    String ID_CATEGORIE_SECTION_RESTITUTIONS = "227026";
    String ID_CATEGORIE_SECTION_RETENU_SUR_RENTE = "227028";
    // Pour les rentes
    String ID_CATEGORIE_SECTION_RETOUR = "227072"; // Utilisé
    // Type de section
    String ID_TYPE_SECTION_AF = "15";
    String ID_TYPE_SECTION_ANCIENNE_FACTURE = "8";
    String ID_TYPE_SECTION_APG = "16";
    String ID_TYPE_SECTION_ARD = "96";
    String ID_TYPE_SECTION_AVANCE_RFM = "77";
    String ID_TYPE_SECTION_AVANCES = "7";
    String ID_TYPE_SECTION_BLOCAGE = "6";
    String ID_TYPE_SECTION_BULLETIN_NEUTRE = "81";
    String ID_TYPE_SECTION_DECISION_PC = "75";
    String ID_TYPE_SECTION_DECOMPTE_CAP_CGAS = "88";
    String ID_TYPE_SECTION_DECOMPTE_COTISATION = "1";
    String ID_TYPE_SECTION_ETUDIANTS = "2";
    String ID_TYPE_SECTION_FACTURATION_AUX_ORGANES_EXTERNES = "69";
    String ID_TYPE_SECTION_FCF = "61";
    String ID_TYPE_SECTION_IJAI = "21";
    String ID_TYPE_SECTION_PC = "79";
    String ID_TYPE_SECTION_PRESTATIONS_CONVENTIONNELLES = "29";
    String ID_TYPE_SECTION_RENTE_AVS_AI = "4";
    String ID_TYPE_SECTION_RESTITUTION = "26";
    String ID_TYPE_SECTION_RETOUR = "5";
    String ID_TYPE_SECTION_RFM = "76";
    String ID_TYPE_SECTION_ASSOCIATION = "10";
    String ID_TYPE_SECTION_CPP = "11";

    // Mode de compensation
    String MODE_BLOQUER_COMPENSATION = "244001";
    String MODE_COMP_COMPLEMENTAIRE = "244006";
    String MODE_COMP_CONT_EMPLOYEUR = "244005";
    String MODE_COMP_COT_PERS = "244004";
    String MODE_COMP_DEC_FINAL = "244003";
    String MODE_COMP_DEC_PERIODIQUE = "244007";
    String MODE_COMPENSATION_STANDARD = "0";
    String MODE_REPORT = "244002";

    // Convention : Les sections pour les intérêts moratoires sont identifiés
    // par le 9
    String SECTION_900_INTERET_MORATOIRE = "900";

    // Statut de suspension de procédure d'encaissement
    String STATUTBN_ANNULE = "257001";
    String STATUTBN_DECOMPTE_FINAL = "257003";
    String STATUTBN_REACTIVE = "257002";

    String getAmende();

    String getBase();

    String getCategorieSection();

    /**
     * 
     * @return String
     */
    APICompteAnnexe getCompteAnnexe();

    Boolean getContentieuxEstSuspendu();

    String getDateDebutPeriode();

    String getDateEcheance();

    String getDateFinPeriode();

    /**
     * 
     * @return String
     */
    String getDateReferenceContentieux(APIParametreEtape etape);

    String getDateSection();

    /**
     * Cette méthode retourne la date de fin de blocage du contentieux (Osiris) elle ne peut pas être utilisée avec le
     * contentieux Aquila
     * 
     * @return String date de fin de suspension du contentieux
     */
    String getDateSuspendu();

    String getDescription();

    /**
     * @return le domaine d'adresse où la facture a été envoyée
     */
    String getDomaine();

    String getFrais();

    String getIdCompteAnnexe();

    String getIdExterne();

    String getIdJournal();

    /**
     * Cette méthode retourne le code système de l'état du contentieux Aquila
     * 
     * @return String idLastEtatAquila
     */
    String getIdLastEtatAquila();

    /**
     * Cette méthode permet de récupérer le mode de compensation de la section Définit si une section peut être
     * compensée ou non
     * 
     * @return String idModeCompensation
     */
    String getIdModeCompensation();

    String getIdMotifContentieuxSuspendu();

    /**
     * Cette méthode retourne l'identifiant du passage dans lequel le report de la section est pris en compte
     * 
     * @return String idPassageComp
     */
    String getIdPassageComp();

    String getIdSection();

    String getIdTypeSection();

    String getInterets();

    /**
     * @return String
     */
    String getNumeroPoursuite();

    String getPmtCmp();

    String getSolde();

    /**
     * Cette méthode retourne le solde de la section formatté
     * 
     * @return String solde de la section formaté
     */
    String getSoldeFormate();

    String getTaxes();

    /**
     * @return le type d'adresse où la facture a été envoyée
     */
    String getTypeAdresse();

    /**
     * Cette méthode retourne le type de la section
     * 
     * @return String type de la section
     */
    APITypeSection getTypeSection();

    /**
     * Un motif de blocage (idModifBlocage) existe-t'il pour l'année (year) pour cette section ?
     * 
     * @param idModifBlocage
     * @param year
     * @return
     * @throws CATechnicalException
     */
    boolean hasMotifContentieuxForYear(String idModifBlocage, String year) throws CATechnicalException;

    /**
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
    boolean isSectionAuContentieux();

    /**
     * @param soldeOuvert
     *            (Boolean) : <br />
     *            <code>true</code> pour ne prendre que les sections qui ont un solde > 0. <br/>
     *            <code>false</code> prend toutes les sections indépendamment du solde.
     * @return <code>true</code> si la section est aux poursuites (RP et ss) et n'est pas radiée.
     */
    Boolean isSectionAuxPoursuitesNotRadiee(Boolean soldeOuvert);

    void retrieve() throws Exception;

    void retrieve(BITransaction transaction) throws Exception;

    void setAlternateKey(int altKey);

    void setIdExterne(String newIdExterne);

    /**
     * Set le mode de compensation de la section
     * 
     * @param idModeCompensation
     *            (Code Système)
     */
    void setIdModeCompensation(String idModeCompensation);

    /**
     * Cette méthode permet de setter l'identifiant du passage dans lequel le report de la section est pris en compte
     * 
     * @param String
     *            idPassageComp
     */
    void setIdPassageComp(String idPassageComp);

    void setIdSection(String newIdSection);

    void setIdTypeSection(String newIdTypeSection);

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
    void updateInfoCompensation(String idModeCompensation, String idPassageComp) throws Exception;
}
