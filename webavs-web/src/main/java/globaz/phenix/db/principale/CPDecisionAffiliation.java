package globaz.phenix.db.principale;

import globaz.jade.client.util.JadeStringUtil;
import globaz.phenix.interfaces.IDecision;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CPDecisionAffiliation extends globaz.globall.db.BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Boolean active = Boolean.FALSE;

    private java.lang.String ancienNumAvs = "";

    private java.lang.String anneeDecision = "";

    private java.lang.String anneePrise = "";

    private Boolean bloque = new Boolean(false);

    private java.lang.String collaborateur = "";

    private Boolean complementaire = new Boolean(false);
    // Constantes

    private java.lang.String dateInformation = "";

    private java.lang.String debutAffiliation = "";

    private java.lang.String debutDecision = "";

    private Boolean division2 = new Boolean(false);
    // Etat
    private java.lang.String etat = "";
    private Boolean facturation = new Boolean(true);
    private java.lang.String finAffiliation = "";
    private java.lang.String finDecision = "";
    private java.lang.String genreAffilie = "";
    private java.lang.String idAffiliation;
    private java.lang.String idCommune;
    private java.lang.String idCommunication = "";
    private java.lang.String idConjoint = "";
    // Decision
    private java.lang.String idDecision = "";
    private java.lang.String idIfdDefinitif = "";
    private java.lang.String idIfdProvisoire = "";
    private java.lang.String idPassage = "";
    private java.lang.String idServiceSociale = "";
    private java.lang.String idTiers = "";
    private Boolean impression = new Boolean(true);
    private java.lang.String interet = "";
    private Boolean lettreSignature = new Boolean(false);
    private String motifFin = "";
    private java.lang.String numAffilie = "";
    private Boolean opposition = new Boolean(false);
    private String prorata = "";
    private java.lang.String responsable = "";
    private java.lang.String specification = "";
    private java.lang.String taxation = "";
    private globaz.pyxis.db.tiers.TITiersViewBean tiers = null;
    private java.lang.String total = "";
    private String typeAffiliation = "";
    private java.lang.String typeDecision = "";

    // code systeme
    /**
     * Commentaire relatif au constructeur CPDecision
     */
    public CPDecisionAffiliation() {
        super();
    }

    /**
     * retourne la clause FROM de la requete SQL (la table)
     */
    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String table1 = "CPDECIP";
        String table2 = "AFAFFIP";
        return _getCollection() + table1 + " INNER JOIN " + _getCollection() + table2 + " ON (" + _getCollection()
                + table1 + ".MAIAFF=" + _getCollection() + table2 + ".MAIAFF)";
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
        active = statement.dbReadBoolean("IAACTI");
        // Tiers
        collaborateur = statement.dbReadString("IARESP");
        total = statement.dbReadNumeric("NOMBRE");
        // Personne avs
        numAffilie = statement.dbReadString("MALNAF");
        debutAffiliation = statement.dbReadDateAMJ("MADDEB");
        finAffiliation = statement.dbReadDateAMJ("MADFIN");
        typeAffiliation = statement.dbReadString("MATTAF");
        motifFin = statement.dbReadString("MATMOT");
        idCommune = statement.dbReadNumeric("IAICOM");
        idServiceSociale = statement.dbReadNumeric("IAISES");
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
    public java.lang.String getAncienNumAvs() {
        return ancienNumAvs;
    }

    public java.lang.String getAnneeDecision() {
        return anneeDecision;
    }

    public java.lang.String getAnneePrise() {
        return anneePrise;
    }

    /**
     * Returns the bloque.
     * 
     * @return Boolean
     */
    public Boolean getBloque() {
        return bloque;
    }

    public java.lang.String getCollaborateur() {
        return collaborateur;
    }

    public Boolean getComplementaire() {
        return complementaire;
    }

    public java.lang.String getDateInformation() {
        return dateInformation;
    }

    /**
     * Returns the debutAffiliation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getDebutAffiliation() {
        return debutAffiliation;
    }

    public java.lang.String getDebutDecision() {
        return debutDecision;
    }

    public IDecision getDerniereDecision() {
        try {
            CPDecisionManager decManager = new CPDecisionManager();
            decManager.setSession(getSession());
            decManager.setForIdTiers(getIdTiers());
            decManager.setForAnneeDecision(getAnneeDecision());
            decManager.orderByDateDecision();
            decManager.setInEtat(CPDecision.CS_FACTURATION + ", " + CPDecision.CS_REPRISE + ", "
                    + CPDecision.CS_PB_COMPTABILISATION);
            decManager.find();
            if (decManager.isEmpty()) {
                return null;
            } else {
                return (CPDecision) decManager.getEntity(0);
            }
        } catch (Exception e) {
            getSession().addError(e.getMessage());
            return null;
        }
    }

    /**
     * Returns the division2.
     * 
     * @return Boolean
     */
    public Boolean getDivision2() {
        return division2;
    }

    /**
     * Returns the etat.
     * 
     * @return java.lang.String
     */
    public java.lang.String getEtat() {
        return etat;
    }

    /**
     * Returns the facturation.
     * 
     * @return Boolean
     */
    public Boolean getFacturation() {
        return facturation;
    }

    /**
     * Returns the finAffiliation.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFinAffiliation() {
        return finAffiliation;
    }

    /**
     * Returns the finDecision.
     * 
     * @return java.lang.String
     */
    public java.lang.String getFinDecision() {
        return finDecision;
    }

    /**
     * Returns the genreAffilie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getGenreAffilie() {
        return genreAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:17:23)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIdAffiliation() {
        return idAffiliation;
    }

    public java.lang.String getIdCommune() {
        return idCommune;
    }

    public java.lang.String getIdCommunication() {
        return idCommunication;
    }

    public java.lang.String getIdConjoint() {
        return idConjoint;
    }

    /**
     * Getter
     */
    public java.lang.String getIdDecision() {
        return idDecision;
    }

    public java.lang.String getIdIfdDefinitif() {
        return idIfdDefinitif;
    }

    public java.lang.String getIdIfdProvisoire() {
        return idIfdProvisoire;
    }

    public java.lang.String getIdPassage() {
        return idPassage;
    }

    public java.lang.String getIdServiceSociale() {
        return idServiceSociale;
    }

    public java.lang.String getIdTiers() {
        return idTiers;
    }

    /**
     * Returns the impression.
     * 
     * @return Boolean
     */
    public Boolean getImpression() {
        return impression;
    }

    public java.lang.String getInteret() {
        return interet;
    }

    public Boolean getLettreSignature() {
        return lettreSignature;
    }

    public String getMotifFin() {
        return motifFin;
    }

    /**
     * Returns the numAffilie.
     * 
     * @return java.lang.String
     */
    public java.lang.String getNumAffilie() {
        return numAffilie;
    }

    /**
     * Returns the opposition.
     * 
     * @return Boolean
     */
    public Boolean getOpposition() {
        return opposition;
    }

    public String getProrata() {
        return prorata;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2003 12:01:46)
     * 
     * @return java.lang.String
     */
    public java.lang.String getResponsable() {
        return responsable;
    }

    /**
     * Returns the specification.
     * 
     * @return java.lang.String
     */
    public java.lang.String getSpecification() {
        return specification;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 08:54:47)
     * 
     * @return java.lang.String
     */
    public java.lang.String getTaxation() {
        return taxation;
    }

    /**
     * Returns the tiers.
     * 
     * @return globaz.pyxis.db.tiers.TITiersViewBean
     */
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

    public java.lang.String getTotal() {
        return total;
    }

    public String getTypeAffiliation() {
        return typeAffiliation;
    }

    public java.lang.String getTypeDecision() {
        return typeDecision;
    }

    public Boolean isBloque() {
        return bloque;
    }

    /**
     * Returns the complementaire.
     * 
     * @return Boolean
     */
    public Boolean isComplementaire() {
        return complementaire;
    }

    public Boolean isDivision2() {
        return division2;
    }

    public Boolean isFacturation() {
        return facturation;
    }

    public Boolean isImpression() {
        return impression;
    }

    /**
     * Returns the lettreSignature.
     * 
     * @return Boolean
     */
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
    public void setAncienNumAvs(java.lang.String ancienNumAvs) {
        this.ancienNumAvs = ancienNumAvs;
    }

    public void setAnneeDecision(java.lang.String newAnneeDecision) {
        anneeDecision = newAnneeDecision;
    }

    public void setAnneePrise(java.lang.String newAnneePrise) {
        anneePrise = newAnneePrise;
    }

    public void setBloque(Boolean newBloque) {
        bloque = newBloque;
    }

    public void setCollaborateur(java.lang.String string) {
        collaborateur = string;
    }

    /**
     * Sets the complementaire.
     * 
     * @param complementaire
     *            The complementaire to set
     */
    public void setComplementaire(Boolean complementaire) {
        this.complementaire = complementaire;
    }

    public void setDateInformation(java.lang.String newDateInformation) {
        dateInformation = newDateInformation;
    }

    /**
     * Sets the debutAffiliation.
     * 
     * @param debutAffiliation
     *            The debutAffiliation to set
     */
    public void setDebutAffiliation(java.lang.String debutActivite) {
        debutAffiliation = debutActivite;
    }

    public void setDebutDecision(java.lang.String newDebutDecision) {
        debutDecision = newDebutDecision;
    }

    public void setDivision2(Boolean newDivision2) {
        division2 = newDivision2;
    }

    /**
     * Sets the etat.
     * 
     * @param etat
     *            The etat to set
     */
    public void setEtat(java.lang.String etat) {
        this.etat = etat;
    }

    public void setFacturation(Boolean newFacturation) {
        facturation = newFacturation;
    }

    /**
     * Sets the finAffiliation.
     * 
     * @param finAffiliation
     *            The finAffiliation to set
     */
    public void setFinAffiliation(java.lang.String finActivite) {
        finAffiliation = finActivite;
    }

    public void setFinDecision(java.lang.String newFinDecision) {
        finDecision = newFinDecision;
    }

    public void setGenreAffilie(java.lang.String newGenreAffilie) {
        genreAffilie = newGenreAffilie;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (26.02.2003 16:17:23)
     * 
     * @param newIdAffiliation
     *            java.lang.String
     */
    public void setIdAffiliation(java.lang.String newIdAffiliation) {
        idAffiliation = newIdAffiliation;
    }

    public void setIdCommune(java.lang.String idCommune) {
        this.idCommune = idCommune;
    }

    public void setIdCommunication(java.lang.String newIdCommunication) {
        idCommunication = newIdCommunication;
    }

    public void setIdConjoint(java.lang.String newIdConjoint) {
        idConjoint = newIdConjoint;
    }

    /**
     * Setter
     */
    public void setIdDecision(java.lang.String newIdDecision) {
        idDecision = newIdDecision;
    }

    public void setIdIfdDefinitif(java.lang.String newIdIfdDefinitif) {
        idIfdDefinitif = newIdIfdDefinitif;
    }

    public void setIdIfdProvisoire(java.lang.String newIdIfdProvisoire) {
        idIfdProvisoire = newIdIfdProvisoire;
    }

    public void setIdPassage(java.lang.String newIdPassage) {
        idPassage = newIdPassage;
    }

    public void setIdServiceSociale(java.lang.String idServiceSociale) {
        this.idServiceSociale = idServiceSociale;
    }

    public void setIdTiers(java.lang.String newIdTiers) {
        idTiers = newIdTiers;
    }

    public void setImpression(Boolean newImpression) {
        impression = newImpression;
    }

    public void setInteret(java.lang.String newInteret) {
        interet = newInteret;
    }

    /**
     * Sets the lettreSignature.
     * 
     * @param lettreSignature
     *            The lettreSignature to set
     */
    public void setLettreSignature(Boolean lettreSignature) {
        this.lettreSignature = lettreSignature;
    }

    public void setMotifFin(String motifFin) {
        this.motifFin = motifFin;
    }

    /**
     * Sets the numAffilie.
     * 
     * @param numAffilie
     *            The numAffilie to set
     */
    public void setNumAffilie(java.lang.String numAffilieActuel) {
        numAffilie = numAffilieActuel;
    }

    public void setOpposition(Boolean newOpposition) {
        opposition = newOpposition;
    }

    public void setProrata(String newProrata) {
        prorata = newProrata;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (12.03.2003 12:01:46)
     * 
     * @param newResponsable
     *            java.lang.String
     */
    public void setResponsable(java.lang.String newResponsable) {
        responsable = newResponsable;
    }

    /**
     * Sets the specification.
     * 
     * @param specification
     *            The specification to set
     */
    public void setSpecification(java.lang.String specification) {
        this.specification = specification;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.02.2003 08:54:47)
     * 
     * @param newTaxation
     *            java.lang.String
     */
    public void setTaxation(java.lang.String newTaxation) {
        taxation = newTaxation;
    }

    /**
     * Sets the tiers.
     * 
     * @param tiers
     *            The tiers to set
     */
    public void setTiers(globaz.pyxis.db.tiers.TITiersViewBean tiers) {
        this.tiers = tiers;
    }

    public void setTotal(java.lang.String string) {
        total = string;
    }

    public void setTypeAffiliation(String typeAffiliation) {
        this.typeAffiliation = typeAffiliation;
    }

    public void setTypeDecision(java.lang.String newTypeDecision) {
        typeDecision = newTypeDecision;
    }

}