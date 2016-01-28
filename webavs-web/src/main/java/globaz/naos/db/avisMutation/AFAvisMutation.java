package globaz.naos.db.avisMutation;

import globaz.globall.parameters.FWParametersSystemCode;
import globaz.jade.client.util.JadeStringUtil;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (28.05.2002 09:11:43)
 * 
 * @author: St�phane Brand
 */
public class AFAvisMutation extends globaz.globall.db.BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private globaz.naos.db.affiliation.AFAffiliation _affiliation;
    private globaz.pyxis.db.tiers.TITiersViewBean _tiers;

    private java.lang.String affiliationId = new String();
    private java.lang.String avisMutationId = new String();
    private java.lang.String caisseEmetteur = new String();
    private java.lang.String caisseReception = new String();
    private FWParametersSystemCode csGenreAnnonce = null;
    private java.lang.String dateAnnonce = new String();
    private java.lang.String dateEnvoiAccuseReception = new String();
    private java.lang.String dateOpposition = new String();
    private java.lang.String dateReception = new String();
    private java.lang.String dateReceptionAccuseReception = new String();
    private java.lang.Boolean droitOption;
    private java.lang.String genreAnnonce = new String();
    private java.lang.String motifOpposition = new String();
    private java.lang.String tiersId = new String();

    /**
     * Commentaire relatif au constructeur AFAssurance.
     */
    public AFAvisMutation() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incr�mente de +1 le num�ro
        setAvisMutationId(this._incCounter(transaction, "0"));

    }

    /**
     * Renvoie le nom de la table
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "AFAVISP";
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la base de donn�es
     * 
     * @exception java.lang.Exception
     *                si la lecture des propri�t�s a �chou�e
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        avisMutationId = statement.dbReadNumeric("MQIAVI");
        tiersId = statement.dbReadNumeric("HTITIE");
        genreAnnonce = statement.dbReadNumeric("MQTAVI");
        dateAnnonce = statement.dbReadDateAMJ("MQDANN");
        dateReception = statement.dbReadDateAMJ("MQDREC");
        caisseReception = statement.dbReadNumeric("MQIREC");
        caisseEmetteur = statement.dbReadNumeric("MQIEME");
        dateEnvoiAccuseReception = statement.dbReadDateAMJ("MQDEAR");
        dateReceptionAccuseReception = statement.dbReadDateAMJ("MQDRAR");
        dateOpposition = statement.dbReadDateAMJ("MQDOPP");
        motifOpposition = statement.dbReadString("MQLOPP");
        // droitOption = statement.dbReadBoolean("MQBOPT");
    }

    /**
     * Valide le contenu de l'entit� (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws java.lang.Exception {
    }

    /**
     * Sauvegarde les valeurs des propri�t�s propres de l'entit� composant la cl� primaire
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propri�t�s a �chou�e
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("MQIAVI", this._dbWriteNumeric(statement.getTransaction(), getAvisMutationId(), ""));
    }

    /**
     * Sauvegarde les valeurs des propri�t�s propres de l'entit� dans la base de donn�es
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propri�t�s a �chou�e
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("MQIAVI",
                this._dbWriteNumeric(statement.getTransaction(), getAvisMutationId(), "AvisMutationId"));
        statement.writeField("HTITIE", this._dbWriteNumeric(statement.getTransaction(), getTiersId(), "TiersId"));
        statement.writeField("MQTAVI",
                this._dbWriteNumeric(statement.getTransaction(), getGenreAnnonce(), "GenreAnnonce"));
        statement.writeField("MQDANN",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateAnnonce(), "DateAnnonce"));
        statement.writeField("MQDREC",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateReception(), "DateReception"));
        statement.writeField("MQIREC",
                this._dbWriteNumeric(statement.getTransaction(), getCaisseReception(), "CaisseReception"));
        statement.writeField("MQIEME",
                this._dbWriteNumeric(statement.getTransaction(), getCaisseEmetteur(), "CaisseEmetteur"));
        statement.writeField("MQDEAR", this._dbWriteDateAMJ(statement.getTransaction(), getDateEnvoiAccuseReception(),
                "DateEnvoiAccuseReception"));
        statement.writeField("MQDRAR", this._dbWriteDateAMJ(statement.getTransaction(),
                getDateReceptionAccuseReception(), "DateReceptionAccuseReception"));
        statement.writeField("MQDOPP",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateOpposition(), "DateOpposition"));
        statement.writeField("MQLOPP",
                this._dbWriteString(statement.getTransaction(), getMotifOpposition(), "MotifOpposition"));
        // statement.writeField("MQBOPT",_dbWriteBoolean(statement.getTransaction(),
        // getDroitOption(),"DroitOption"));

    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 15:03:11)
     * 
     * @return globaz.osiris.db.comptes.CAJournal
     */
    public globaz.naos.db.affiliation.AFAffiliation getAffiliation() {

        // Si si pas d'identifiant, pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
            return null;
        }

        // Si log pas d�j� charg�
        if (_affiliation == null) {
            // Instancier un nouveau LOG
            _affiliation = new globaz.naos.db.affiliation.AFAffiliation();
            _affiliation.setIdTiers(getTiersId());
            _affiliation.setSession(getSession());

            // R�cup�rer le log en question
            _affiliation.setAffiliationId(getAffiliationId());

            try {
                _affiliation.retrieve();
                // if (_affiliation.hasErrors())
                // _affiliation = null;
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _affiliation = null;

            }
        }

        return _affiliation;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:19:38)
     * 
     * @return int
     */
    public java.lang.String getAffiliationId() {
        return affiliationId;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:10:39)
     * 
     * @return java.lang.String
     */
    public java.lang.String getAvisMutationId() {
        return avisMutationId;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:05:31)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCaisseEmetteur() {
        return caisseEmetteur;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:04:36)
     * 
     * @return java.lang.String
     */
    public java.lang.String getCaisseReception() {
        return caisseReception;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (03.10.2001 11:54:45)
     * 
     * @return globaz.bambou.db.AJCodeSysteme
     */
    public FWParametersSystemCode getCsGenreAnnonce() {
        // enregistrement d�j� charg� ?
        if (csGenreAnnonce == null) {
            // liste pas encore charg�e, on la charge
            csGenreAnnonce = new FWParametersSystemCode();
            csGenreAnnonce.getCode(getGenreAnnonce());
        }
        return csGenreAnnonce;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:02:58)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateAnnonce() {
        return dateAnnonce;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:06:20)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateEnvoiAccuseReception() {
        return dateEnvoiAccuseReception;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:07:50)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateOpposition() {
        return dateOpposition;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:03:48)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateReception() {
        return dateReception;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:07:07)
     * 
     * @return java.lang.String
     */
    public java.lang.String getDateReceptionAccuseReception() {
        return dateReceptionAccuseReception;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:09:03)
     * 
     * @return java.lang.String
     */
    public java.lang.Boolean getDroitOption() {
        return droitOption;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:02:26)
     * 
     * @return java.lang.String
     */
    public java.lang.String getGenreAnnonce() {
        return genreAnnonce;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:08:18)
     * 
     * @return java.lang.String
     */
    public java.lang.String getMotifOpposition() {
        return motifOpposition;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (13.02.2002 15:03:11)
     * 
     * @return globaz.osiris.db.comptes.CAJournal
     */
    public globaz.pyxis.db.tiers.TITiersViewBean getTiers() {

        // Si si pas d'identifiant, pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getTiersId())) {
            return null;
        }

        // Si log pas d�j� charg�
        if (_tiers == null) {
            // Instancier un nouveau LOG
            _tiers = new globaz.pyxis.db.tiers.TITiersViewBean();
            _tiers.setSession(getSession());

            // R�cup�rer le log en question
            _tiers.setIdTiers(getTiersId());
            try {
                _tiers.retrieve();
                if (_tiers.getSession().hasErrors()) {
                    _tiers = null;
                }
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _tiers = null;
            }
        }

        return _tiers;
    }

    /**
     * Ins�rer la d�scription de la m�thode ici
     */
    public java.lang.String getTiersId() {
        return tiersId;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */
    public void setAffiliationId(java.lang.String newAffiliationId) {
        affiliationId = newAffiliationId;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:10:39)
     * 
     * @param newAvisMutationId
     *            java.lang.String
     */
    public void setAvisMutationId(java.lang.String newAvisMutationId) {
        avisMutationId = newAvisMutationId;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:05:31)
     * 
     * @param newCaisseEmetteur
     *            java.lang.String
     */
    public void setCaisseEmetteur(java.lang.String newCaisseEmetteur) {
        caisseEmetteur = newCaisseEmetteur;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:04:36)
     * 
     * @param newCaisseReception
     *            java.lang.String
     */
    public void setCaisseReception(java.lang.String newCaisseReception) {
        caisseReception = newCaisseReception;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:02:58)
     * 
     * @param newDateAnnonce
     *            java.lang.String
     */
    public void setDateAnnonce(java.lang.String newDateAnnonce) {
        dateAnnonce = newDateAnnonce;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:06:20)
     * 
     * @param newDateEnvoiAccuseReception
     *            java.lang.String
     */
    public void setDateEnvoiAccuseReception(java.lang.String newDateEnvoiAccuseReception) {
        dateEnvoiAccuseReception = newDateEnvoiAccuseReception;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:07:50)
     * 
     * @param newDateOpposition
     *            java.lang.String
     */
    public void setDateOpposition(java.lang.String newDateOpposition) {
        dateOpposition = newDateOpposition;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:03:48)
     * 
     * @param newDateReception
     *            java.lang.String
     */
    public void setDateReception(java.lang.String newDateReception) {
        dateReception = newDateReception;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:07:07)
     * 
     * @param newDateReceptionAccuseReception
     *            java.lang.String
     */
    public void setDateReceptionAccuseReception(java.lang.String newDateReceptionAccuseReception) {
        dateReceptionAccuseReception = newDateReceptionAccuseReception;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:09:03)
     * 
     * @param newDroitOption
     *            java.lang.String
     */
    public void setDroitOption(java.lang.Boolean newDroitOption) {
        droitOption = newDroitOption;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:02:26)
     * 
     * @param newGenreAnnonce
     *            java.lang.String
     */
    public void setGenreAnnonce(java.lang.String newGenreAnnonce) {
        genreAnnonce = newGenreAnnonce;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (06.08.2003 09:08:18)
     * 
     * @param newMotifOpposition
     *            java.lang.String
     */
    public void setMotifOpposition(java.lang.String newMotifOpposition) {
        motifOpposition = newMotifOpposition;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (28.05.2002 09:19:38)
     * 
     * @param newAssuranceId
     *            int
     */
    public void setTiersId(java.lang.String newTiersId) {
        tiersId = newTiersId;
    }
}
