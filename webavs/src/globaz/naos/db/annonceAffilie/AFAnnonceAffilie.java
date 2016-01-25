package globaz.naos.db.annonceAffilie;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;
import globaz.naos.db.affiliation.AFAffiliation;
import globaz.naos.db.avisMutation.AFAvisMutation;
import globaz.naos.translation.CodeSystem;
import globaz.pyxis.db.tiers.TITiers;

/**
 * Insérez la description du type ici. Date de création : (28.05.2002 09:11:43)
 * 
 * @author: Stéphane Brand
 */
public class AFAnnonceAffilie extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AFAffiliation _affiliation = null;
    private TITiers _tiers = null;
    // Foreign Key
    private java.lang.String affiliationId = new String();
    // DB
    // Primary Key
    private java.lang.String annoncePreparationId = new String();
    private java.lang.String avisMutationId = new String();
    private java.lang.String champAncienneDonnee = new String();
    private java.lang.String champModifier = new String();
    private java.lang.String dateAnnonce = new String();
    // Fields
    private java.lang.String dateEnregistrement = new String();
    private java.lang.String heureEnregistrement = new String();
    // private FWParametersSystemCode csChampModifier = null;
    private java.lang.String heureEnregistrementFormat = new String();

    private java.lang.String observation = new String();

    private java.lang.Boolean traitement = new Boolean(true);
    private java.lang.String utilisateur = new String();

    /**
     * Constructeur AFAnnonceAffilie.
     */
    public AFAnnonceAffilie() {
        super();
    }

    /**
     * Effectue des traitements avant un ajout dans la BD.
     * 
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        // incrémente de +1 le numéro
        setAnnoncePreparationId(this._incCounter(transaction, "0"));
        // setDateAnnonce(JACalendar.todayJJsMMsAAAA());
    }

    /**
     * Création des Avis de Mutation plus utilisé?
     * 
     * @throws Exception
     */
    public void _creationAvisMutation() throws Exception {

        // TODO To be checked => creationAvisMutation()
        AFAnnonceAffilieListViewBean annonceList = new AFAnnonceAffilieListViewBean();
        AFAvisMutation avisMutation = new AFAvisMutation();

        annonceList.forTraitement("1");
        annonceList.setSession(getSession());
        annonceList.find();

        String temp = new String();
        try {
            for (int i = 0; i < annonceList.size(); i++) {

                AFAnnonceAffilie annonceAffilie = (AFAnnonceAffilie) annonceList.getEntity(i);

                if (annonceAffilie.getAffiliation().getIdTiers().equalsIgnoreCase(temp)) {
                    annonceAffilie.setTraitement(new Boolean("2"));
                    annonceAffilie.setSession(getSession());
                    annonceAffilie.update();
                } else {
                    // Gestion des avis de mutation pour l'impression
                    temp = annonceAffilie.getAffiliation().getIdTiers();
                    avisMutation.setTiersId(temp);

                    avisMutation.setCaisseEmetteur("26.1"); // TODO Hard-Coded
                    if (annonceAffilie.getChampModifier().equals(CodeSystem.CHAMPS_MOD_DATE_FIN)) {
                        avisMutation.setGenreAnnonce(CodeSystem.AVIS_MUTATION_FIN_AFFILIE);
                    }
                    if (annonceAffilie.getChampModifier().equals(CodeSystem.CHAMPS_MOD_CREATION_AFFILIE)
                            || annonceAffilie.getChampModifier().equals(CodeSystem.CHAMPS_MOD_CREATION_COTI)) {
                        avisMutation.setGenreAnnonce(CodeSystem.AVIS_MUTATION_CREATION);
                    }
                    if (!annonceAffilie.getChampModifier().equals(CodeSystem.CHAMPS_MOD_CREATION_AFFILIE)
                            && !annonceAffilie.getChampModifier().equals(CodeSystem.CHAMPS_MOD_DATE_FIN)) {
                        avisMutation.setGenreAnnonce(CodeSystem.AVIS_MUTATION_MODIFICATION);
                    }

                    avisMutation.setDateAnnonce(JACalendar.todayJJsMMsAAAA());
                    avisMutation.setSession(getSession());
                    avisMutation.add();

                    annonceAffilie.setTraitement(new Boolean("2"));
                    annonceAffilie.setSession(getSession());
                    annonceAffilie.setAvisMutationId(avisMutation.getAvisMutationId());
                    annonceAffilie.update();
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Retour le nom de la Table.
     * 
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return "AFAPREP";
    }

    /**
     * Lit dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        annoncePreparationId = statement.dbReadNumeric("MPIAPR");
        affiliationId = statement.dbReadNumeric("MAIAFF");
        // avisMutationId = statement.dbReadNumeric("XXX");
        dateEnregistrement = statement.dbReadDateAMJ("MPDENR");
        dateAnnonce = statement.dbReadDateAMJ("MPDANN");
        traitement = statement.dbReadBoolean("MPBTRA");
        heureEnregistrement = statement.dbReadNumeric("MPNENR");
        champModifier = statement.dbReadNumeric("MPTCHA");
        champAncienneDonnee = statement.dbReadString("MPLCHA");
        observation = statement.dbReadString("MPLOBS");
        utilisateur = statement.dbReadString("MPLUTI");
    }

    /**
     * Valide le contenu de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propriétés composant la clé primaire de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {

        statement.writeKey("MPIAPR", this._dbWriteNumeric(statement.getTransaction(), getAnnoncePreparationId(), ""));
    }

    /**
     * Sauvegarde dans la DB les valeurs des propriétés de l'entité.
     * 
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("MPIAPR",
                this._dbWriteNumeric(statement.getTransaction(), getAnnoncePreparationId(), "AnnoncePreparationId"));
        statement.writeField("MAIAFF",
                this._dbWriteNumeric(statement.getTransaction(), getAffiliationId(), "AffiliationId"));
        // statement.writeField("XXX",_dbWriteNumeric(statement.getTransaction(),
        // getAvisMutationId(),"AvisMutationId"));
        statement.writeField("MPDENR",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateEnregistrement(), "DateEnregistrement"));
        statement.writeField("MPDANN",
                this._dbWriteDateAMJ(statement.getTransaction(), getDateAnnonce(), "DateAnnonce"));
        statement.writeField("MPBTRA", this._dbWriteBoolean(statement.getTransaction(), isTraitement(), "traitement"));
        statement.writeField("MPNENR",
                this._dbWriteTime(statement.getTransaction(), getHeureEnregistrement(), "HeureEnregistrement"));
        statement.writeField("MPTCHA",
                this._dbWriteNumeric(statement.getTransaction(), getChampModifier(), "ChampModifier"));
        statement.writeField("MPLCHA",
                this._dbWriteString(statement.getTransaction(), getChampAncienneDonnee(), "ChampAncienneDonnee"));
        statement
                .writeField("MPLOBS", this._dbWriteString(statement.getTransaction(), getObservation(), "Observation"));
        statement
                .writeField("MPLUTI", this._dbWriteString(statement.getTransaction(), getUtilisateur(), "Utilisateur"));
    }

    // *******************************************************
    // Getter
    // *******************************************************

    /**
     * Rechercher l'Affiliation de l'Annonce en fonction de sont ID.
     * 
     * @return l'Affiliation
     */
    public AFAffiliation getAffiliation() {

        // Si pas d'identifiant => pas d'objet
        if (JadeStringUtil.isIntegerEmpty(getAffiliationId())) {
            return null;
        }

        if (_affiliation == null) {

            _affiliation = new AFAffiliation();
            _affiliation.setSession(getSession());
            _affiliation.setAffiliationId(getAffiliationId());
            try {
                _affiliation.retrieve();
                /*
                 * if (_affiliation.hasErrors()) _affiliation = null;
                 */
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _affiliation = null;
            }
        }
        return _affiliation;
    }

    public java.lang.String getAffiliationId() {
        return affiliationId;
    }

    public java.lang.String getAnnoncePreparationId() {
        return annoncePreparationId;
    }

    public java.lang.String getAvisMutationId() {
        return avisMutationId;
    }

    public java.lang.String getChampAncienneDonnee() {
        return champAncienneDonnee;
    }

    public java.lang.String getChampModifier() {
        return champModifier;
    }

    public java.lang.String getDateAnnonce() {
        return dateAnnonce;
    }

    public java.lang.String getDateEnregistrement() {
        return dateEnregistrement;
    }

    public java.lang.String getHeureEnregistrement() {
        return heureEnregistrement;
    }

    public java.lang.String getHeureEnregistrementFormat() {
        return heureEnregistrementFormat;
    }

    public java.lang.String getObservation() {
        return observation;
    }

    /**
     * Rechercher le tiers de l'affiliation en fonction de son ID.
     * 
     * @return le tiers
     */
    public TITiers getTiers() {

        // Si pas d'identifiant => pas d'objet
        if (_tiers == null) {
            if (_affiliation == null) {
                getAffiliation();
                if (_affiliation == null) {
                    return null;
                }
            }
            _tiers = new TITiers();
            _tiers.setSession(getSession());
            _tiers.setIdTiers(_affiliation.getIdTiers());
            try {
                _tiers.retrieve();
                /*
                 * if (_tiers.getSession().hasErrors()) _tiers = null;
                 */
            } catch (Exception e) {
                _addError(null, e.getMessage());
                _tiers = null;
            }
        }
        return _tiers;
    }

    public java.lang.String getUtilisateur() {
        return utilisateur;
    }

    public java.lang.Boolean isTraitement() {
        return traitement;
    }

    /*
     * public FWParametersSystemCode getCsChampModifier() { // enregistrement déjà chargé ? if (csChampModifier == null)
     * { // liste pas encore chargée, on la charge csChampModifier = new FWParametersSystemCode();
     * csChampModifier.getCode(getChampModifier()); } return csChampModifier; }
     */

    // *******************************************************
    // Setter
    // *******************************************************

    public void setAffiliationId(java.lang.String newAffiliationId) {
        affiliationId = newAffiliationId;
    }

    public void setAnnoncePreparationId(java.lang.String newAnnoncePreparationId) {
        annoncePreparationId = newAnnoncePreparationId;
    }

    public void setAvisMutationId(java.lang.String string) {
        avisMutationId = string;
    }

    public void setChampAncienneDonnee(java.lang.String newChampAncienneDonnee) {
        champAncienneDonnee = newChampAncienneDonnee;
    }

    public void setChampModifier(java.lang.String newChampModifier) {
        champModifier = newChampModifier;
    }

    public void setDateAnnonce(java.lang.String newDateAnnonce) {
        dateAnnonce = newDateAnnonce;
    }

    public void setDateEnregistrement(java.lang.String newDateEnregistrement) {
        dateEnregistrement = newDateEnregistrement;
    }

    public void setHeureEnregistrement(java.lang.String newHeureEnregistrement) {
        heureEnregistrement = newHeureEnregistrement;
    }

    public void setHeureEnregistrementFormat(java.lang.String newHeureEnregistrementFormat) {
        heureEnregistrementFormat = newHeureEnregistrementFormat;
    }

    public void setObservation(java.lang.String newObservation) {
        observation = newObservation;
    }

    public void setTraitement(java.lang.Boolean newTraitement) {
        traitement = newTraitement;
    }

    public void setUtilisateur(java.lang.String newUtilisateur) {
        utilisateur = newUtilisateur;
    }
}
