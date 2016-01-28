package globaz.phenix.db.principale;

import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.interfaces.ICDecisionTiers;
import globaz.phenix.interfaces.IDecision;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CPDecisionAffiliationTiers extends globaz.globall.db.BEntity implements ICDecisionTiers {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Boolean active = new Boolean(false);

    private java.lang.String ancienNumAvs = "";

    private java.lang.String anneeDecision = "";

    private java.lang.String anneePrise = "";

    private Boolean bloque = new Boolean(false);

    private java.lang.String collaborateur = "";

    private Boolean complementaire = new Boolean(false);
    // Constantes

    private java.lang.String dateEtat = "";

    private java.lang.String dateInformation = "";

    private java.lang.String debutActivite = "";

    private java.lang.String debutDecision = "";
    private java.lang.String designation1 = "";
    private java.lang.String designation2 = "";
    private java.lang.String designation3 = "";
    private java.lang.String designation4 = "";
    private Boolean division2 = new Boolean(false);
    // Etat
    private java.lang.String etat = "";
    private Boolean facturation = new Boolean(true);
    private java.lang.String finActivite = "";
    private java.lang.String finDecision = "";
    private java.lang.String genreAffilie = "";
    private java.lang.String genreDecision = "";
    private java.lang.String heureEtat = "";
    private java.lang.String idAffiliation;
    private java.lang.String idCommune = "";
    private java.lang.String idCommunication = "";
    private java.lang.String idConjoint = "";
    // Decision
    private java.lang.String idDecision = "";
    private java.lang.String idIfdDefinitif = "";
    private java.lang.String idIfdProvisoire = "";
    private java.lang.String idPassage = "";
    // tiers
    private java.lang.String idPays = "";
    private java.lang.String idTiers = "";
    private Boolean impression = new Boolean(true);
    private java.lang.String interet = "";
    private java.lang.String langue = "";
    private Boolean lettreSignature = new Boolean(false);
    private java.lang.String numAffilie = "";
    // Personne AVS
    private java.lang.String numAvsActuel = "";
    private java.lang.String numContribuableActuel = "";
    private Boolean opposition = new Boolean(false);
    private String prorata = "";
    private java.lang.String responsable = "";
    private java.lang.String specification = "";
    private java.lang.String taxation = "";
    private globaz.pyxis.db.tiers.TITiersViewBean tiers = null;
    private java.lang.String titreTiers = "";
    private java.lang.String total = "";
    private java.lang.String typeDecision = "";
    private java.lang.String typeTiers = "";
    private java.lang.String userEtat = "";

    // code systeme
    /**
     * Commentaire relatif au constructeur CPDecision
     */
    public CPDecisionAffiliationTiers() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String table1 = "CPDECIP";
        String table2 = "TIPAVSP";
        String table3 = "TITIERP";
        String table4 = "AFAFFIP";
        return _getCollection() + table1 + " INNER JOIN " + _getCollection() + table2 + " ON (" + _getCollection()
                + table1 + ".HTITIE=" + _getCollection() + table2 + ".HTITIE)" + " INNER JOIN " + _getCollection()
                + table3 + " ON (" + _getCollection() + table1 + ".HTITIE=" + _getCollection() + table3 + ".HTITIE)"
                + " INNER JOIN " + _getCollection() + table4 + " ON (" + _getCollection() + table4 + ".MAIAFF="
                + _getCollection() + table1 + ".MAIAFF)";
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPDECIP";
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (06.05.2003 11:37:07)
     * 
     * @return globaz.pyxis.db.tiers.TITiersViewBean
     */
    @Override
    public globaz.pyxis.db.tiers.TITiersViewBean _getTiers() {
        // enregistrement déjà chargé ?
        if (tiers == null) {
            // liste pas encore chargée, on la charge
            tiers = new globaz.pyxis.db.tiers.TITiersViewBean();
            tiers.setSession(getSession());
        }
        if (!JadeStringUtil.isIntegerEmpty(getIdTiers())) {
            try {
                tiers.setIdTiers(getIdTiers());
                tiers.retrieve();
            } catch (Exception e) {
                getSession().addError(e.getMessage());
            }
        }
        return tiers;
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        // Decision
        idDecision = statement.dbReadNumeric("IAIDEC");
        idTiers = statement.dbReadNumeric("HTITIE");
        idAffiliation = statement.dbReadNumeric("MAIAFF");
        idIfdDefinitif = statement.dbReadNumeric("ICIIFD");
        idIfdProvisoire = statement.dbReadNumeric("ICIIFP");
        idCommunication = statement.dbReadNumeric("IBIDCF");
        idCommune = statement.dbReadNumeric("IAICOM");
        idPassage = statement.dbReadNumeric("EBIPAS");
        typeDecision = statement.dbReadNumeric("IATTDE");
        idConjoint = statement.dbReadNumeric("HTICJT");
        genreAffilie = statement.dbReadNumeric("IATGAF");
        dateInformation = statement.dbReadDateAMJ("IADINF");
        anneeDecision = statement.dbReadNumeric("IAANNE");
        debutDecision = statement.dbReadDateAMJ("IADDEB");
        finDecision = statement.dbReadDateAMJ("IADFIN");
        bloque = statement.dbReadBoolean("IACBLO");
        interet = statement.dbReadNumeric("IACINT");
        impression = statement.dbReadBoolean("IABIMP");
        facturation = statement.dbReadBoolean("IAFACT");
        division2 = statement.dbReadBoolean("IAASSU");
        anneePrise = statement.dbReadString("IAAPRI");
        taxation = statement.dbReadString("IATAXA");
        responsable = statement.dbReadString("IARESP");
        opposition = statement.dbReadBoolean("IAOPPO");
        prorata = statement.dbReadString("IABPRO");
        specification = statement.dbReadNumeric("IATSPE");
        complementaire = statement.dbReadBoolean("IABCOM");
        lettreSignature = statement.dbReadBoolean("IABSIG");
        etat = statement.dbReadNumeric("IATETA");
        genreDecision = statement.dbReadNumeric("IATGAF");
        active = statement.dbReadBoolean("IAACTI");
        // Tiers
        idPays = statement.dbReadNumeric("HNIPAY");
        typeTiers = statement.dbReadNumeric("HTTTIE");
        titreTiers = statement.dbReadNumeric("HTTTTI");
        designation1 = statement.dbReadString("HTLDE1");
        designation2 = statement.dbReadString("HTLDE2");
        designation3 = statement.dbReadString("HTLDE3");
        designation4 = statement.dbReadString("HTLDE4");
        langue = statement.dbReadNumeric("HTTLAN");
        collaborateur = statement.dbReadString("IARESP");
        total = statement.dbReadNumeric("NOMBRE");
        // Personne avs
        numAvsActuel = statement.dbReadString("HXNAVS");
        numAffilie = statement.dbReadString("MALNAF");
        numContribuableActuel = statement.dbReadString("HXNCON");
        ancienNumAvs = statement.dbReadString("HXAAVS");
        // genreAffilie = statement.dbReadNumeric("HXTGAF");
        debutActivite = statement.dbReadDateAMJ("HXDDAC");
        finActivite = statement.dbReadDateAMJ("HXDFAC");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
    }

    public Boolean getActive() {
        return active;
    }

    /**
     * Returns the ancienNumAvs.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getAncienNumAvs() {
        return ancienNumAvs;
    }

    @Override
    public java.lang.String getAnneeDecision() {
        return anneeDecision;
    }

    @Override
    public java.lang.String getAnneePrise() {
        return anneePrise;
    }

    /**
     * Returns the bloque.
     * 
     * @return Boolean
     */
    @Override
    public Boolean getBloque() {
        return bloque;
    }

    @Override
    public java.lang.String getCollaborateur() {
        return collaborateur;
    }

    /**
     * Returns the dateEtat.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDateEtat() {
        return dateEtat;
    }

    @Override
    public java.lang.String getDateInformation() {
        return dateInformation;
    }

    /**
     * Returns the debutActivite.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDebutActivite() {
        return debutActivite;
    }

    @Override
    public java.lang.String getDebutDecision() {
        return debutDecision;
    }

    @Override
    public IDecision getDerniereDecision() {
        try {
            CPDecisionManager decManager = new CPDecisionManager();
            decManager.setSession(getSession());
            decManager.setForIdTiers(getIdTiers());
            decManager.setForAnneeDecision(getAnneeDecision());
            decManager.orderByDateDecision();
            decManager.find();
            for (int i = 0; i < decManager.size(); i++) {
                CPDecision myDecision = (CPDecision) decManager.getEntity(i);
                if (!myDecision.getTypeDecision().equalsIgnoreCase(CPDecision.CS_IMPUTATION)) {
                    // Aller rechercher le dernier état
                    if (CPDecision.CS_FACTURATION.equalsIgnoreCase(myDecision.getDernierEtat())
                            || CPDecision.CS_REPRISE.equalsIgnoreCase(myDecision.getDernierEtat())
                            || CPDecision.CS_PB_COMPTABILISATION.equalsIgnoreCase(myDecision.getDernierEtat())) {
                        return (CPDecision) decManager.getEntity(i);
                    }
                }
            }
            return null;
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return null;
        }
    }

    /**
     * Returns the designation1.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDesignation1() {
        return designation1;
    }

    /**
     * Returns the designation2.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDesignation2() {
        return designation2;
    }

    /**
     * Returns the designation3.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDesignation3() {
        return designation3;
    }

    /**
     * Returns the designation4.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getDesignation4() {
        return designation4;
    }

    /**
     * Returns the division2.
     * 
     * @return Boolean
     */
    @Override
    public Boolean getDivision2() {
        return division2;
    }

    /**
     * Returns the etat.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getEtat() {
        return etat;
    }

    /**
     * Returns the facturation.
     * 
     * @return Boolean
     */
    @Override
    public Boolean getFacturation() {
        return facturation;
    }

    /**
     * Returns the finActivite.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getFinActivite() {
        return finActivite;
    }

    /**
     * Returns the finDecision.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getFinDecision() {
        return finDecision;
    }

    /**
     * Returns the genreAffilie.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getGenreAffilie() {
        return genreAffilie;
    }

    public java.lang.String getGenreDecision() {
        return genreDecision;
    }

    /**
     * Returns the heureEtat.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getHeureEtat() {
        return heureEtat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:17:23)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdAffiliation() {
        return idAffiliation;
    }

    public java.lang.String getIdCommune() {
        return idCommune;
    }

    @Override
    public java.lang.String getIdCommunication() {
        return idCommunication;
    }

    @Override
    public java.lang.String getIdConjoint() {
        return idConjoint;
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdDecision() {
        return idDecision;
    }

    @Override
    public java.lang.String getIdIfdDefinitif() {
        return idIfdDefinitif;
    }

    @Override
    public java.lang.String getIdIfdProvisoire() {
        return idIfdProvisoire;
    }

    @Override
    public java.lang.String getIdPassage() {
        return idPassage;
    }

    /**
     * Returns the idPays.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getIdPays() {
        return idPays;
    }

    @Override
    public java.lang.String getIdTiers() {
        return idTiers;
    }

    /**
     * Returns the impression.
     * 
     * @return Boolean
     */
    @Override
    public Boolean getImpression() {
        return impression;
    }

    @Override
    public java.lang.String getInteret() {
        return interet;
    }

    /**
     * Returns the langue.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getLangue() {
        return langue;
    }

    /**
     * Returns the numAffilie.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getNumAffilie() {
        return numAffilie;
    }

    /**
     * Returns the numAvsActuel.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getNumAvsActuel() {
        return numAvsActuel;
    }

    /**
     * Returns the numContribuableActuel.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getNumContribuableActuel() {
        return numContribuableActuel;
    }

    /**
     * Returns the opposition.
     * 
     * @return Boolean
     */
    @Override
    public Boolean getOpposition() {
        return opposition;
    }

    @Override
    public String getProrata() {
        return prorata;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2003 12:01:46)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getResponsable() {
        return responsable;
    }

    /**
     * Returns the specification.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getSpecification() {
        return specification;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 08:54:47)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getTaxation() {
        return taxation;
    }

    /**
     * Returns the tiers.
     * 
     * @return globaz.pyxis.db.tiers.TITiersViewBean
     */
    @Override
    public globaz.pyxis.db.tiers.TITiersViewBean getTiers() {
        try {
            // enregistrement déjà chargé ?
            if (tiers == null) {
                tiers = new globaz.pyxis.db.tiers.TITiersViewBean();
                tiers.setSession(getSession());
                if (!JadeStringUtil.isIntegerEmpty(getIdTiers())) {
                    tiers.setIdTiers(getIdTiers());
                    tiers.retrieve();
                }
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
        }
        return tiers;
    }

    /**
     * Returns the titreTiers.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getTitreTiers() {
        return titreTiers;
    }

    @Override
    public java.lang.String getTotal() {
        return total;
    }

    @Override
    public java.lang.String getTypeDecision() {
        return typeDecision;
    }

    /**
     * Returns the typeTiers.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getTypeTiers() {
        return typeTiers;
    }

    /**
     * Returns the userEtat.
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getUserEtat() {
        return userEtat;
    }

    @Override
    public Boolean isBloque() {
        return bloque;
    }

    /**
     * Returns the complementaire.
     * 
     * @return Boolean
     */
    @Override
    public Boolean isComplementaire() {
        return complementaire;
    }

    @Override
    public Boolean isDivision2() {
        return division2;
    }

    @Override
    public Boolean isFacturation() {
        return facturation;
    }

    @Override
    public Boolean isImpression() {
        return impression;
    }

    /**
     * Returns the lettreSignature.
     * 
     * @return Boolean
     */
    @Override
    public Boolean isLettreSignature() {
        return lettreSignature;
    }

    /**
     * 07.09.2007: Cette méthode retourne si la décision est de genre non actif comme provisoire au point de vue métier
     * 
     * @param myDecision
     * @return
     */
    public boolean isNonActif() {
        if (CPDecision.CS_NON_ACTIF.equalsIgnoreCase(getGenreAffilie())
                || CPDecision.CS_ETUDIANT.equalsIgnoreCase(getGenreAffilie())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean isOpposition() {
        return opposition;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Sets the ancienNumAvs.
     * 
     * @param ancienNumAvs
     *            The ancienNumAvs to set
     */
    @Override
    public void setAncienNumAvs(java.lang.String ancienNumAvs) {
        this.ancienNumAvs = ancienNumAvs;
    }

    @Override
    public void setAnneeDecision(java.lang.String newAnneeDecision) {
        anneeDecision = newAnneeDecision;
    }

    @Override
    public void setAnneePrise(java.lang.String newAnneePrise) {
        anneePrise = newAnneePrise;
    }

    @Override
    public void setBloque(Boolean newBloque) {
        bloque = newBloque;
    }

    @Override
    public void setCollaborateur(java.lang.String string) {
        collaborateur = string;
    }

    /**
     * Sets the complementaire.
     * 
     * @param complementaire
     *            The complementaire to set
     */
    @Override
    public void setComplementaire(Boolean complementaire) {
        this.complementaire = complementaire;
    }

    /**
     * Sets the dateEtat.
     * 
     * @param dateEtat
     *            The dateEtat to set
     */
    @Override
    public void setDateEtat(java.lang.String dateEtat) {
        this.dateEtat = dateEtat;
    }

    @Override
    public void setDateInformation(java.lang.String newDateInformation) {
        dateInformation = newDateInformation;
    }

    /**
     * Sets the debutActivite.
     * 
     * @param debutActivite
     *            The debutActivite to set
     */
    @Override
    public void setDebutActivite(java.lang.String debutActivite) {
        this.debutActivite = debutActivite;
    }

    @Override
    public void setDebutDecision(java.lang.String newDebutDecision) {
        debutDecision = newDebutDecision;
    }

    /**
     * Sets the designation1.
     * 
     * @param designation1
     *            The designation1 to set
     */
    @Override
    public void setDesignation1(java.lang.String designation1) {
        this.designation1 = designation1;
    }

    /**
     * Sets the designation2.
     * 
     * @param designation2
     *            The designation2 to set
     */
    @Override
    public void setDesignation2(java.lang.String designation2) {
        this.designation2 = designation2;
    }

    /**
     * Sets the designation3.
     * 
     * @param designation3
     *            The designation3 to set
     */
    @Override
    public void setDesignation3(java.lang.String designation3) {
        this.designation3 = designation3;
    }

    /**
     * Sets the designation4.
     * 
     * @param designation4
     *            The designation4 to set
     */
    @Override
    public void setDesignation4(java.lang.String designation4) {
        this.designation4 = designation4;
    }

    @Override
    public void setDivision2(Boolean newDivision2) {
        division2 = newDivision2;
    }

    /**
     * Sets the etat.
     * 
     * @param etat
     *            The etat to set
     */
    @Override
    public void setEtat(java.lang.String etat) {
        this.etat = etat;
    }

    @Override
    public void setFacturation(Boolean newFacturation) {
        facturation = newFacturation;
    }

    /**
     * Sets the finActivite.
     * 
     * @param finActivite
     *            The finActivite to set
     */
    @Override
    public void setFinActivite(java.lang.String finActivite) {
        this.finActivite = finActivite;
    }

    @Override
    public void setFinDecision(java.lang.String newFinDecision) {
        finDecision = newFinDecision;
    }

    @Override
    public void setGenreAffilie(java.lang.String newGenreAffilie) {
        genreAffilie = newGenreAffilie;
    }

    public void setGenreDecision(java.lang.String genreDecision) {
        this.genreDecision = genreDecision;
    }

    /**
     * Sets the heureEtat.
     * 
     * @param heureEtat
     *            The heureEtat to set
     */
    @Override
    public void setHeureEtat(java.lang.String heureEtat) {
        this.heureEtat = heureEtat;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:17:23)
     * 
     * @param newIdAffiliation
     *            java.lang.String
     */
    @Override
    public void setIdAffiliation(java.lang.String newIdAffiliation) {
        idAffiliation = newIdAffiliation;
    }

    public void setIdCommune(java.lang.String idCommune) {
        this.idCommune = idCommune;
    }

    @Override
    public void setIdCommunication(java.lang.String newIdCommunication) {
        idCommunication = newIdCommunication;
    }

    @Override
    public void setIdConjoint(java.lang.String newIdConjoint) {
        idConjoint = newIdConjoint;
    }

    /**
     * Setter
     */
    @Override
    public void setIdDecision(java.lang.String newIdDecision) {
        idDecision = newIdDecision;
    }

    @Override
    public void setIdIfdDefinitif(java.lang.String newIdIfdDefinitif) {
        idIfdDefinitif = newIdIfdDefinitif;
    }

    @Override
    public void setIdIfdProvisoire(java.lang.String newIdIfdProvisoire) {
        idIfdProvisoire = newIdIfdProvisoire;
    }

    @Override
    public void setIdPassage(java.lang.String newIdPassage) {
        idPassage = newIdPassage;
    }

    /**
     * Sets the idPays.
     * 
     * @param idPays
     *            The idPays to set
     */
    @Override
    public void setIdPays(java.lang.String idPays) {
        this.idPays = idPays;
    }

    @Override
    public void setIdTiers(java.lang.String newIdTiers) {
        idTiers = newIdTiers;
    }

    @Override
    public void setImpression(Boolean newImpression) {
        impression = newImpression;
    }

    @Override
    public void setInteret(java.lang.String newInteret) {
        interet = newInteret;
    }

    /**
     * Sets the langue.
     * 
     * @param langue
     *            The langue to set
     */
    @Override
    public void setLangue(java.lang.String langue) {
        this.langue = langue;
    }

    /**
     * Sets the lettreSignature.
     * 
     * @param lettreSignature
     *            The lettreSignature to set
     */
    @Override
    public void setLettreSignature(Boolean lettreSignature) {
        this.lettreSignature = lettreSignature;
    }

    /**
     * Sets the numAffilie.
     * 
     * @param numAffilie
     *            The numAffilie to set
     */
    @Override
    public void setNumAffilie(java.lang.String numAffilieActuel) {
        numAffilie = numAffilieActuel;
    }

    /**
     * Sets the numAvsActuel.
     * 
     * @param numAvsActuel
     *            The numAvsActuel to set
     */
    @Override
    public void setNumAvsActuel(java.lang.String numAvsActuel) {
        this.numAvsActuel = numAvsActuel;
    }

    /**
     * Sets the numContribuableActuel.
     * 
     * @param numContribuableActuel
     *            The numContribuableActuel to set
     */
    @Override
    public void setNumContribuableActuel(java.lang.String numContribuableActuel) {
        this.numContribuableActuel = numContribuableActuel;
    }

    @Override
    public void setOpposition(Boolean newOpposition) {
        opposition = newOpposition;
    }

    @Override
    public void setProrata(String newProrata) {
        prorata = newProrata;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2003 12:01:46)
     * 
     * @param newResponsable
     *            java.lang.String
     */
    @Override
    public void setResponsable(java.lang.String newResponsable) {
        responsable = newResponsable;
    }

    /**
     * Sets the specification.
     * 
     * @param specification
     *            The specification to set
     */
    @Override
    public void setSpecification(java.lang.String specification) {
        this.specification = specification;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 08:54:47)
     * 
     * @param newTaxation
     *            java.lang.String
     */
    @Override
    public void setTaxation(java.lang.String newTaxation) {
        taxation = newTaxation;
    }

    /**
     * Sets the tiers.
     * 
     * @param tiers
     *            The tiers to set
     */
    @Override
    public void setTiers(globaz.pyxis.db.tiers.TITiersViewBean tiers) {
        this.tiers = tiers;
    }

    /**
     * Sets the titreTiers.
     * 
     * @param titreTiers
     *            The titreTiers to set
     */
    @Override
    public void setTitreTiers(java.lang.String titreTiers) {
        this.titreTiers = titreTiers;
    }

    @Override
    public void setTotal(java.lang.String string) {
        total = string;
    }

    @Override
    public void setTypeDecision(java.lang.String newTypeDecision) {
        typeDecision = newTypeDecision;
    }

    /**
     * Sets the typeTiers.
     * 
     * @param typeTiers
     *            The typeTiers to set
     */
    @Override
    public void setTypeTiers(java.lang.String typeTiers) {
        this.typeTiers = typeTiers;
    }

    /**
     * Sets the userEtat.
     * 
     * @param userEtat
     *            The userEtat to set
     */
    @Override
    public void setUserEtat(java.lang.String userEtat) {
        this.userEtat = userEtat;
    }
}