package globaz.apg.db.droits;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JAUtil;
import globaz.prestation.clone.factory.IPRCloneable;

/**
 * @author dvh
 */
public class APPeriodeAPG extends BEntity implements IPRCloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELDNAME_DATEDEBUT = "VCDDEB";
    public static final String FIELDNAME_DATEFIN = "VCDFIN";
    public static final String FIELDNAME_IDDROIT = "VCIDRO";
    public static final String FIELDNAME_IDPERIODE = "VCIPER";
    public static final String FIELDNAME_NBRJOURS = "VCNNBJ";
    public static final String FIELDNAME_TYPEPERIODE = "VCTTYP";
    public static final String TABLE_NAME = "APPERIP";

    private String dateDebutPeriode = "";
    private String dateFinPeriode = "";
    private String idDroit = "";
    private String idPeriode = "";
    private String nbrJours = "";
    private String typePeriode = "";

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdPeriode(this._incCounter(transaction, "0"));
    }

    @Override
    protected String _getTableName() {
        return APPeriodeAPG.TABLE_NAME;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idPeriode = statement.dbReadNumeric(APPeriodeAPG.FIELDNAME_IDPERIODE);
        dateDebutPeriode = statement.dbReadDateAMJ(APPeriodeAPG.FIELDNAME_DATEDEBUT);
        dateFinPeriode = statement.dbReadDateAMJ(APPeriodeAPG.FIELDNAME_DATEFIN);
        nbrJours = statement.dbReadNumeric(APPeriodeAPG.FIELDNAME_NBRJOURS);
        typePeriode = statement.dbReadNumeric(APPeriodeAPG.FIELDNAME_TYPEPERIODE);
        idDroit = statement.dbReadNumeric(APPeriodeAPG.FIELDNAME_IDDROIT);
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        BTransaction transaction = statement.getTransaction();
        BSession session = getSession();

        // on vérifie les champs obligatoires
        _propertyMandatory(transaction, getDateDebutPeriode(), session.getLabel("DATE_DEBUT_PERIODE_OBLIGATOIRE"));
        _propertyMandatory(transaction, getDateFinPeriode(), session.getLabel("DATE_FIN_PERIODE_OBLIGATOIRE"));

        // et la validité des dates
        _checkDate(transaction, getDateDebutPeriode(), session.getLabel("DATE_DEBUT_PERIODE_INCORRECTE"));
        _checkDate(transaction, getDateFinPeriode(), session.getLabel("DATE_FIN_PERIODE_INCORRECTE"));

        // la date de début doit être antérieure à la date de fin
        if (!JAUtil.isDateEmpty(getDateDebutPeriode()) && !JAUtil.isDateEmpty(getDateFinPeriode())
                && !BSessionUtil.compareDateFirstLowerOrEqual(session, getDateDebutPeriode(), getDateFinPeriode())) {
            _addError(transaction, session.getLabel("DATE_DEBUT_PERIODE_SUP_DATE_FIN_PERIODE"));
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey(APPeriodeAPG.FIELDNAME_IDPERIODE,
                this._dbWriteNumeric(statement.getTransaction(), idPeriode, "idPeriode"));
    }

    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField(APPeriodeAPG.FIELDNAME_IDPERIODE,
                this._dbWriteNumeric(statement.getTransaction(), idPeriode, "idPeriode"));
        statement.writeField(APPeriodeAPG.FIELDNAME_DATEDEBUT,
                this._dbWriteDateAMJ(statement.getTransaction(), dateDebutPeriode, "dateDebutPeriode"));
        statement.writeField(APPeriodeAPG.FIELDNAME_DATEFIN,
                this._dbWriteDateAMJ(statement.getTransaction(), dateFinPeriode, "dateFinPeriode"));
        statement.writeField(APPeriodeAPG.FIELDNAME_NBRJOURS,
                this._dbWriteNumeric(statement.getTransaction(), nbrJours, "nbrJours"));
        statement.writeField(APPeriodeAPG.FIELDNAME_TYPEPERIODE,
                this._dbWriteNumeric(statement.getTransaction(), typePeriode, "typePeriode"));
        statement.writeField(APPeriodeAPG.FIELDNAME_IDDROIT,
                this._dbWriteNumeric(statement.getTransaction(), idDroit, "idDroit"));
    }

    @Override
    public IPRCloneable duplicate(int actionType) {
        APPeriodeAPG clone = new APPeriodeAPG();
        clone.setDateDebutPeriode(getDateDebutPeriode());
        clone.setDateFinPeriode(getDateFinPeriode());
        clone.setNbrJours(getNbrJours());
        clone.setTypePeriode(getTypePeriode());

        // On ne veut pas de la validation pendant une duplication
        clone.wantCallValidate(false);
        return clone;
    }

    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    public String getIdDroit() {
        return idDroit;
    }

    public String getIdPeriode() {
        return idPeriode;
    }

    public String getNbrJours() {
        return nbrJours;
    }

    public String getTypePeriode() {
        return typePeriode;
    }

    @Override
    public String getUniquePrimaryKey() {
        return getIdPeriode();
    }

    public void setDateDebutPeriode(String string) {
        dateDebutPeriode = string;
    }

    public void setDateFinPeriode(String string) {
        dateFinPeriode = string;
    }

    public void setIdDroit(String string) {
        idDroit = string;
    }

    public void setIdPeriode(String string) {
        idPeriode = string;
    }

    public void setNbrJours(String string) {
        nbrJours = string;
    }

    public void setTypePeriode(String string) {
        typePeriode = string;
    }

    @Override
    public void setUniquePrimaryKey(String pk) {
        setIdPeriode(pk);
    }
}
