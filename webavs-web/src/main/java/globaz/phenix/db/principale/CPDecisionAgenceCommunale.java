package globaz.phenix.db.principale;

import globaz.pyxis.db.alternate.TIPAvsAdrLienAdmin;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CPDecisionAgenceCommunale extends TIPAvsAdrLienAdmin {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String anneeDecision = "";
    private String anneePrise = "";
    private Boolean complementaire = new Boolean(false);
    private Boolean cotiMinimumPayeEnSalarie = Boolean.FALSE;
    private String dateFacturation = "";
    private String dateInformation = "";
    private String debutDecision = "";
    private String etat = "";
    private String finDecision = "";
    private Boolean firstActivite = new Boolean(false);
    private String genreAffilie = "";
    private String idAffiliation = "";
    private String idCommune = "";
    private String idCommunication = "";
    private String idConjoint = "";
    private String idDecision = "";
    private String idIfdDefinitif = "";
    private String idIfdProvisoire = "";
    private String idPassage = "";
    private Boolean lettreSignature = new Boolean(false);
    private String numeroAffilie = "";
    private Boolean opposition = new Boolean(false);
    private String periodicite = "";
    private Boolean premiereAssurance = new Boolean(false);
    private String prorata = "";
    private String responsable = "";
    private String specification = "";
    private String typeDecision = "";

    @Override
    protected String _getFields(globaz.globall.db.BStatement statement) {

        String fields = super._getFields(statement);
        fields += "," + _getCollection() + " CPDECIP.IAIDEC IAIDEC," + _getCollection() + "CPDECIP.MAIAFF MAIAFF,"
                + _getCollection() + "AFAFFIP.MALNAF MALNAF," + _getCollection() + "AFAFFIP.MATPER MATPER,"
                + _getCollection() + "CPDECIP.ICIIFD ICIIFD," + _getCollection() + "CPDECIP.IBIDCF IBIDCF,"
                + _getCollection() + "CPDECIP.ICIIFP ICIIFP," + _getCollection() + "CPDECIP.EBIPAS EBIPAS,"
                + _getCollection() + "CPDECIP.IATTDE IATTDE," + _getCollection() + "CPDECIP.HTICJT HTICJT,"
                + _getCollection() + "CPDECIP.IATGAF IATGAF," + _getCollection() + "CPDECIP.IADINF IADINF,"
                + _getCollection() + "CPDECIP.IAANNE IAANNE," + _getCollection() + "CPDECIP.IADDEB IADDEB,"
                + _getCollection() + "CPDECIP.IADFIN IADFIN," + _getCollection() + "CPDECIP.IARESP IARESP,"
                + _getCollection() + "CPDECIP.IATSPE  IATSPE," + _getCollection() + "CPDECIP.IABSIG IABSIG,"
                + _getCollection() + "CPDECIP.IAACTI IAACTI," + _getCollection() + "CPDECIP.IABDAC IABDAC,"
                + _getCollection() + "CPDECIP.IABCOM IABCOM," + _getCollection() + "CPDECIP.IAOPPO IAOPPO,"
                + _getCollection() + "CPDECIP.IAAPRI IAAPRI, " + _getCollection() + "CPDECIP.IATETA IATETA, "
                + _getCollection() + "CPDECIP.IADFAC IADFAC, " + _getCollection() + "CPDECIP.IABCMP IABCMP, "
                + _getCollection() + "CPDECIP.IAICOM IAICOM, " + _getCollection() + "CPDECIP.IABPRO IABPRO ";

        return fields;

    }

    @Override
    protected String _getFrom(globaz.globall.db.BStatement statement) {
        String decision = _getCollection() + "CPDECIP";
        String affiliation = _getCollection() + "AFAFFIP";
        String from = super._getFrom(statement);
        from += " INNER JOIN " + decision + " ON (" + decision + ".HTITIE=tiers.HTITIE)";
        from += " INNER JOIN " + affiliation + " ON (" + affiliation + ".HTITIE=tiers.HTITIE)";
        return from;
    }

    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        super._readProperties(statement);
        idDecision = statement.dbReadNumeric("IAIDEC");
        idIfdDefinitif = statement.dbReadNumeric("ICIIFD");
        idAffiliation = statement.dbReadNumeric("MAIAFF");
        idCommunication = statement.dbReadNumeric("IBIDCF");
        numeroAffilie = statement.dbReadString("MALNAF");
        periodicite = statement.dbReadString("MATPER");
        idIfdProvisoire = statement.dbReadNumeric("ICIIFP");
        idPassage = statement.dbReadNumeric("EBIPAS");
        typeDecision = statement.dbReadNumeric("IATTDE");
        idConjoint = statement.dbReadNumeric("HTICJT");
        genreAffilie = statement.dbReadNumeric("IATGAF");
        dateInformation = statement.dbReadDateAMJ("IADINF");
        anneeDecision = statement.dbReadNumeric("IAANNE");
        debutDecision = statement.dbReadDateAMJ("IADDEB");
        finDecision = statement.dbReadDateAMJ("IADFIN");
        responsable = statement.dbReadString("IARESP");
        specification = statement.dbReadNumeric("IATSPE");
        lettreSignature = statement.dbReadBoolean("IABSIG");
        firstActivite = statement.dbReadBoolean("IABDAC");
        premiereAssurance = statement.dbReadBoolean("IABPAS");
        complementaire = statement.dbReadBoolean("IABCOM");
        opposition = statement.dbReadBoolean("IAOPPO");
        anneePrise = statement.dbReadString("IAAPRI");
        etat = statement.dbReadNumeric("IATETA");
        prorata = statement.dbReadString("IABPRO");
        dateFacturation = statement.dbReadDateAMJ("IADFAC");
        cotiMinimumPayeEnSalarie = statement.dbReadBoolean("IABCMP");
        idCommune = statement.dbReadNumeric("IAICOM");
    }

    /**
     * @return
     */
    public String getAnneeDecision() {
        return anneeDecision;
    }

    /**
     * @return
     */
    public String getAnneePrise() {
        return anneePrise;
    }

    /**
     * @return
     */
    public Boolean getComplementaire() {
        return complementaire;
    }

    public Boolean getCotiMinimumPayeEnSalarie() {
        return cotiMinimumPayeEnSalarie;
    }

    public String getDateFacturation() {
        return dateFacturation;
    }

    /**
     * @return
     */
    public String getDateInformation() {
        return dateInformation;
    }

    /**
     * @return
     */
    public String getDebutDecision() {
        return debutDecision;
    }

    public CPDecision getDerniereDecision() {
        CPDecision decision = new CPDecision();
        decision.setSession(getSession());
        decision.setAnneeDecision(getAnneeDecision());
        decision.setIdTiers(getIdTiers());
        decision.setIdAffiliation(getIdAffiliation());
        decision.setDebutDecision(getDebutDecision());
        decision.setFinDecision(getFinDecision());
        return decision.loadDerniereDecision(1);
    }

    public String getEtat() {
        return etat;
    }

    /**
     * @return
     */
    public String getFinDecision() {
        return finDecision;
    }

    /**
     * @return
     */
    public Boolean getFirstActivite() {
        return firstActivite;
    }

    /**
     * @return
     */
    public String getGenreAffilie() {
        return genreAffilie;
    }

    /**
     * @return
     */
    public String getIdAffiliation() {
        return idAffiliation;
    }

    public String getIdCommune() {
        return idCommune;
    }

    /**
     * Return id communication en retour qui a permis de calculer la décision
     * 
     * @return
     */
    public String getIdCommunication() {
        return idCommunication;
    }

    /**
     * @return
     */
    public String getIdConjoint() {
        return idConjoint;
    }

    /**
     * @return
     */
    public String getIdDecision() {
        return idDecision;
    }

    /**
     * @return
     */
    public String getIdIfdDefinitif() {
        return idIfdDefinitif;
    }

    /**
     * @return
     */
    public String getIdIfdProvisoire() {
        return idIfdProvisoire;
    }

    /**
     * @return
     */
    public String getIdPassage() {
        return idPassage;
    }

    /**
     * @return
     */
    public Boolean getLettreSignature() {
        return lettreSignature;
    }

    @Override
    public String getNom() {
        return getDesignation1_tiers() + " " + getDesignation2_tiers();
    }

    /**
     * @return
     */
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    /**
     * @return
     */
    public Boolean getOpposition() {
        return opposition;
    }

    /**
     * @return
     */
    public String getPeriodicite() {
        return periodicite;
    }

    /**
     * @return
     */
    public Boolean getPremiereAssurance() {
        return premiereAssurance;
    }

    public String getProrata() {
        return prorata;
    }

    /**
     * @return
     */
    public String getResponsable() {
        return responsable;
    }

    /**
     * @return
     */
    public String getSpecification() {
        return specification;
    }

    /**
     * @return
     */
    public String getTypeDecision() {
        return typeDecision;
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

}
