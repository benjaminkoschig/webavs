package globaz.apg.db.droits;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.clone.factory.IPRCloneable;
import globaz.prestation.interfaces.tiers.PRTiersHelper;

/**
 * BEntity représentant un droit APG
 * 
 * @author vre
 */
public class APDroitAPG extends APDroitLAPG implements IPRCloneable {

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

    public APDroitAPG() {
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
                + APDroitAPG.TABLE_NAME_DROIT_APG + " on " + _getCollection() + APDroitLAPG.TABLE_NAME_LAPG + "."
                + APDroitLAPG.FIELDNAME_IDDROIT_LAPG + "=" + _getCollection() + APDroitAPG.TABLE_NAME_DROIT_APG + "."
                + APDroitAPG.FIELDNAME_IDDROIT_APG;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return la chaine "apdrapp"
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return APDroitAPG.TABLE_NAME_DROIT_APG;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        super._readProperties(statement);
        setIdDroit(statement.dbReadNumeric(APDroitAPG.FIELDNAME_IDDROIT_APG));
        idSituationFam = statement.dbReadNumeric(APDroitAPG.FIELDNAME_IDSITUATIONFAM);
        nbrJourSoldes = statement.dbReadNumeric(APDroitAPG.FIELDNAME_NBRJOURSSOLDES);
        noCompte = statement.dbReadNumeric(APDroitAPG.FIELDNAME_NOCOMPTE);
        noControlePers = statement.dbReadNumeric(APDroitAPG.FIELDNAME_NOCONTROLEPERS);
        duplicata = statement.dbReadBoolean(APDroitAPG.FIELDNAME_DUPLICATA);
        noRevision = statement.dbReadNumeric(APDroitAPG.FIELDNAME_REVISION);
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
        APDroitAPG clone = new APDroitAPG();
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

        // Etat par défaut : attente.
        clone.setEtat(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE);

        return clone;
    }

    /**
     * getter pour l'attribut Duplicata
     * 
     * @return la valeur courante de l'attribut Duplicata
     */
    public Boolean getDuplicata() {
        return duplicata;
    }

    /**
     * getter pour l'attribut Id Situation Fam
     * 
     * @return la valeur courante de l'attribut Id Situation Fam
     */
    public String getIdSituationFam() {
        return idSituationFam;
    }

    /**
     * getter pour l'attribut Nbr Jour Soldes
     * 
     * @return la valeur courante de l'attribut Nbr Jour Soldes
     */
    public String getNbrJourSoldes() {
        return nbrJourSoldes;
    }

    /**
     * getter pour l'attribut No Compte
     * 
     * @return la valeur courante de l'attribut No Compte
     */
    public String getNoCompte() {
        // pour éviter d'avoir "0" si l'enregistrement ds la base est vide
        if (JadeStringUtil.isIntegerEmpty(noCompte)) {
            return "";
        }

        return noCompte;
    }

    /**
     * getter pour l'attribut No Controle Pers
     * 
     * @return la valeur courante de l'attribut No Controle Pers
     */
    public String getNoControlePers() {
        // pour éviter d'avoir "0" si l'enregistrement ds la base est vide
        if (JadeStringUtil.isIntegerEmpty(noControlePers)) {
            return "";
        }

        return noControlePers;
    }

    /**
     * getter pour l'attribut no revision
     * 
     * @return la valeur courante de l'attribut no revision
     */
    public String getNoRevision() {
        return noRevision;
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
     * Réactive le pspy pour la table des droits APG.
     * 
     * @return true
     * @see globaz.globall.db.BEntity#hasSpy()
     */
    @Override
    public boolean hasSpy() {
        return true;
    }

    /**
     * Vrais si l'assure est en age AVS au debut du droit
     * 
     * @return
     */
    public boolean isAgeAvs() {

        try {
            if (!JadeStringUtil.isEmpty(getDateDebutDroit())
                    && !JadeStringUtil.isIntegerEmpty(loadDemande().getIdTiers())) {
                return PRTiersHelper.isRentier(getSession(), loadDemande().getIdTiers(), getDateDebutDroit());
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isUnvalidateForReprise() {
        return isUnvalidateForReprise;
    }

    public boolean isValiderAgeAvs() {
        return validerAgeAvs;
    }

    /**
     * getter pour l'attribut valider NBPeriodes
     * 
     * @return la valeur courante de l'attribut valider NBPeriodes
     */
    public boolean isValiderPeriodes() {
        return validerPeriodes;
    }

    /**
     * Charge la situation familliale liée à ce droit. La situation familliale est automatiquement rechargée si (et
     * seulement si) la valeur Id Situation Fam de ce bean est modifiée.
     * 
     * @return la situation familliale liée à ce droit (jamais nul).
     * @throws Exception
     *             si la situation familliale ne peut être chargée.
     */
    public APSituationFamilialeAPG loadSituationFamilliale() throws Exception {
        if (situationFamilliale == null) {
            situationFamilliale = new APSituationFamilialeAPG();
        }

        // si l'identifiant de la situation familiale a changé, charger
        if (!idSituationFam.equals(situationFamilliale.getIdSitFamAPG())) {
            situationFamilliale.setIdSitFamAPG(idSituationFam);
            situationFamilliale.setSession(getSession());
            situationFamilliale.retrieve();
        }

        return situationFamilliale;
    }

    /**
     * setter pour l'attribut Duplicata
     * 
     * @param duplicata
     *            une nouvelle valeur pour cet attribut
     */
    public void setDuplicata(Boolean duplicata) {
        this.duplicata = duplicata;
    }

    /**
     * setter pour l'attribut Id Situation Fam
     * 
     * @param idSituationFam
     *            une nouvelle valeur pour cet attribut
     */
    public void setIdSituationFam(String idSituationFam) {
        this.idSituationFam = idSituationFam;
    }

    /**
     * setter pour l'attribut Nbr Jour Soldes
     * 
     * @param nbrJourSoldes
     *            une nouvelle valeur pour cet attribut
     */
    public void setNbrJourSoldes(String nbrJourSoldes) {
        this.nbrJourSoldes = nbrJourSoldes;
    }

    /**
     * setter pour l'attribut No Compte
     * 
     * @param noCompte
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoCompte(String noCompte) {
        this.noCompte = noCompte;
    }

    /**
     * setter pour l'attribut No Controle Pers
     * 
     * @param noControlePers
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoControlePers(String noControlePers) {
        this.noControlePers = noControlePers;
    }

    /**
     * setter pour l'attribut no revision
     * 
     * @param string
     *            une nouvelle valeur pour cet attribut
     */
    public void setNoRevision(String string) {
        noRevision = string;
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

    public void setUnvalidateForReprise(boolean isUnvalidateForReprise) {
        this.isUnvalidateForReprise = isUnvalidateForReprise;
    }

    public void setValiderAgeAvs(boolean validerAgeAvs) {
        this.validerAgeAvs = validerAgeAvs;
    }

    /**
     * setter pour l'attribut valider NBPeriodes
     * 
     * @param b
     *            une nouvelle valeur pour cet attribut
     */
    public void setValiderPeriodes(boolean b) {
        validerPeriodes = b;
    }

    @Override
    public void setDateFinDroit(String dateFinDroit) {
        super.setDateFinDroit(dateFinDroit);
    }
}
