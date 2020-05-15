package globaz.apg.db.droits;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.clone.factory.IPRCloneable;

public class APDroitPandemie extends APDroitLAPG implements IPRCloneable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME_DROIT_APG = "APDRAPP";
    public static final String FIELDNAME_DUPLICATA = "VBBDUP";
    public static final String FIELDNAME_IDDROIT_APG = "VBIDRO";
    public static final String FIELDNAME_IDSITUATIONFAM = "VBISIF";
    public static final String FIELDNAME_NBRJOURSSOLDES = "VBNJOS";
    public static final String FIELDNAME_NOCOMPTE = "VBNNOC";
    public static final String FIELDNAME_NOCONTROLEPERS = "VBNNCP";
    public static final String FIELDNAME_REVISION = "VBTREV";

    private Boolean duplicata = Boolean.FALSE;
    private String idSituationFam = "";
    private boolean isUnvalidateForReprise = false;
    private String nbrJourSoldes = "";
    private String noCompte = "";
    private String noControlePers = "";
    private String noRevision = "";
    private transient APSituationFamilialeAPG situationFamilliale = null;
    private boolean validerAgeAvs = true;
    private boolean validerPeriodes = true;

    public APDroitPandemie() {
    }

    /**
     * @see globaz.globall.db.BEntity#_afterDelete(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
    }

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        super._beforeAdd(transaction);
    }

    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + APDroitLAPG.TABLE_NAME_LAPG + " inner join " + _getCollection()
                + APDroitPandemie.TABLE_NAME_DROIT_APG + " on " + _getCollection() + APDroitLAPG.TABLE_NAME_LAPG + "."
                + APDroitLAPG.FIELDNAME_IDDROIT_LAPG + "=" + _getCollection() + APDroitPandemie.TABLE_NAME_DROIT_APG + "."
                + APDroitPandemie.FIELDNAME_IDDROIT_APG;
    }

    /**
     * DOCUMENT ME!
     *
     * @return la chaine "apdrapp"
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return APDroitPandemie.TABLE_NAME_DROIT_APG;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        super._readProperties(statement);
        setIdDroit(statement.dbReadNumeric(APDroitPandemie.FIELDNAME_IDDROIT_APG));
        idSituationFam = statement.dbReadNumeric(APDroitPandemie.FIELDNAME_IDSITUATIONFAM);
        nbrJourSoldes = statement.dbReadNumeric(APDroitPandemie.FIELDNAME_NBRJOURSSOLDES);
        noCompte = statement.dbReadNumeric(APDroitPandemie.FIELDNAME_NOCOMPTE);
        noControlePers = statement.dbReadNumeric(APDroitPandemie.FIELDNAME_NOCONTROLEPERS);
        duplicata = statement.dbReadBoolean(APDroitPandemie.FIELDNAME_DUPLICATA);
        noRevision = statement.dbReadNumeric(APDroitPandemie.FIELDNAME_REVISION);
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(APDroitAPG.FIELDNAME_IDDROIT_APG,
                this._dbWriteNumeric(statement.getTransaction(), getIdDroit(), "idDroit"));
    }

    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        if (_getAction() == BEntity.ACTION_COPY) {
            super._writeProperties(statement);
        } else {
            statement.writeField(APDroitAPG.FIELDNAME_IDDROIT_APG,
                    this._dbWriteNumeric(statement.getTransaction(), getIdDroit(), "idDroit"));
        }

        statement.writeField(APDroitAPG.FIELDNAME_IDSITUATIONFAM,
                this._dbWriteNumeric(statement.getTransaction(), idSituationFam, "idSituationFam"));
        statement.writeField(APDroitAPG.FIELDNAME_NBRJOURSSOLDES,
                this._dbWriteNumeric(statement.getTransaction(), nbrJourSoldes, "nbrJourSoldes"));
        statement.writeField(APDroitAPG.FIELDNAME_NOCOMPTE,
                this._dbWriteNumeric(statement.getTransaction(), noCompte, "noCompte"));
        statement.writeField(APDroitAPG.FIELDNAME_NOCONTROLEPERS,
                this._dbWriteNumeric(statement.getTransaction(), noControlePers, "noControlePers"));
        statement.writeField(APDroitAPG.FIELDNAME_DUPLICATA, this._dbWriteBoolean(statement.getTransaction(),
                duplicata, BConstants.DB_TYPE_BOOLEAN_CHAR, "duplicata"));
        statement.writeField(APDroitAPG.FIELDNAME_REVISION,
                this._dbWriteNumeric(statement.getTransaction(), noRevision, "noRevision"));
    }

    @Override
    public IPRCloneable duplicate(int actionType) {
        APDroitPandemie clone = new APDroitPandemie();
        clone.setNpa(getNpa());
        clone.setDateDepot(getDateDepot());
        clone.setDateReception(getDateReception());
        clone.setDuplicata(getDuplicata());
        clone.setGenreService(getGenreService());
        clone.setIdCaisse(getIdCaisse());
        clone.setIdDemande(getIdDemande());

        if (null != getSession().getUserId()) {
            clone.setIdGestionnaire(getSession().getUserId());
        } else {
            clone.setIdGestionnaire("");
        }

        if (IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_APG_PARENT == actionType) {
            clone.setIdDroitParent(null);
        } else {
            // un seul niveau hiérarchque... Si l'on clone un fils, le parent du
            // droit
            // cloné sera le même que le parent du fils.
            if (JadeStringUtil.isIntegerEmpty(getIdDroitParent())) {
                clone.setIdDroitParent(getIdDroit());
            } else {
                clone.setIdDroitParent(getIdDroitParent());
            }
        }

        clone.setNbrJourSoldes(getNbrJourSoldes());
        clone.setNoCompte(getNoCompte());
        clone.setNoControlePers(getNoControlePers());
        clone.setNoDroit(getNoDroit());
        clone.setPays(getPays());
        clone.setReference(getReference());
        clone.setDroitAcquis(getDroitAcquis());
        clone.setIsSoumisImpotSource(getIsSoumisImpotSource());
        clone.setTauxImpotSource(getTauxImpotSource());

        // S'il s'agit d'une simple copie, la date de début du droit et de fin
        // ne doit pas être copiée
        // HACK ce test ne doit pas se faire dans la méthode duplicate
        if (IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_APG_PARENT != actionType) {
            clone.setDateDebutDroit(getDateDebutDroit());
            clone.setDateFinDroit(getDateFinDroit());
        }

        // On ne veut pas de la validation pendant une duplication
        clone.wantCallValidate(false);
        // on ne veut pas faire de propagation GED
        clone.wantCallExternalServices(isCallExternalServices());

        if (IPRCloneable.ACTION_CREER_NOUVEAU_DROIT_PANDEMIE_FILS == actionType){
            clone.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_VALIDE);
        } else {
        // Etat par défaut : attente.
        clone.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);
        }

        return clone;
    }

    /**
     * getter pour l'attribut unique primary key
     *
     * @return la valeur courante de l'attribut unique primary key
     */
    @Override
    public String getUniquePrimaryKey() {
        return getIdDroit();
    }

    /**
     * setter pour l'attribut unique primary key
     *
     * @param pk
     *            une nouvelle valeur pour cet attribut
     */
    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdDroit(pk);
    }

    public Boolean getDuplicata() {
        return duplicata;
    }

    public void setDuplicata(Boolean duplicata) {
        this.duplicata = duplicata;
    }

    public String getIdSituationFam() {
        return idSituationFam;
    }

    public void setIdSituationFam(String idSituationFam) {
        this.idSituationFam = idSituationFam;
    }

    public boolean isUnvalidateForReprise() {
        return isUnvalidateForReprise;
    }

    public void setUnvalidateForReprise(boolean unvalidateForReprise) {
        isUnvalidateForReprise = unvalidateForReprise;
    }

    public String getNbrJourSoldes() {
        return nbrJourSoldes;
    }

    public void setNbrJourSoldes(String nbrJourSoldes) {
        this.nbrJourSoldes = nbrJourSoldes;
    }

    public String getNoCompte() {
        return noCompte;
    }

    public void setNoCompte(String noCompte) {
        this.noCompte = noCompte;
    }

    public String getNoControlePers() {
        return noControlePers;
    }

    public void setNoControlePers(String noControlePers) {
        this.noControlePers = noControlePers;
    }

    public String getNoRevision() {
        return noRevision;
    }

    public void setNoRevision(String noRevision) {
        this.noRevision = noRevision;
    }

    public APSituationFamilialeAPG getSituationFamilliale() {
        return situationFamilliale;
    }

    public void setSituationFamilliale(APSituationFamilialeAPG situationFamilliale) {
        this.situationFamilliale = situationFamilliale;
    }

    public boolean isValiderAgeAvs() {
        return validerAgeAvs;
    }

    public void setValiderAgeAvs(boolean validerAgeAvs) {
        this.validerAgeAvs = validerAgeAvs;
    }

    public boolean isValiderPeriodes() {
        return validerPeriodes;
    }

    public void setValiderPeriodes(boolean validerPeriodes) {
        this.validerPeriodes = validerPeriodes;
    }


}
