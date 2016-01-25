package globaz.lynx.db.codetva;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;

public class LXCodeTva extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final String FIELD_CSCODETVA = "CSCODETVA";
    public static final String FIELD_DATEDEBUT = "DATEDEBUT";
    public static final String FIELD_DATEFIN = "DATEFIN";
    public static final String FIELD_IDCODETVA = "IDCODETVA";
    public static final String FIELD_TAUX = "TAUX";

    public static final String TABLE_LXCTVAP = "LXCTVAP";

    private String csCodeTVA = "";
    private String dateDebut = "";
    private String dateFin = "";
    private String idCodeTVA = "";
    private String taux = "0.00";

    /**
     * @see globaz.globall.db.BEntity#_beforeAdd(globaz.globall.db.BTransaction)
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdCodeTVA(_incCounter(transaction, idCodeTVA));
    }

    /**
     * @see globaz.globall.db.BEntity#_getTableName()
     */
    @Override
    protected String _getTableName() {
        return TABLE_LXCTVAP;
    }

    /**
     * @see globaz.globall.db.BEntity#_readProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setIdCodeTVA(statement.dbReadNumeric(FIELD_IDCODETVA));
        setCsCodeTVA(statement.dbReadNumeric(FIELD_CSCODETVA));
        setTaux(statement.dbReadNumeric(FIELD_TAUX, 2));
        setDateDebut(statement.dbReadDateAMJ(FIELD_DATEDEBUT));
        setDateFin(statement.dbReadDateAMJ(FIELD_DATEFIN));
    }

    /**
     * @see globaz.globall.db.BEntity#_validate(globaz.globall.db.BStatement)
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {

        boolean checkDateDebut = false;
        boolean checkDateFin = false;

        // Vérification de la date de début
        if (_propertyMandatory(statement.getTransaction(), getDateDebut(),
                getSession().getLabel("VAL_DATE_DEBUT_RENSEIGNEE"))) {
            if (_checkDate(statement.getTransaction(), getDateDebut(), getSession().getLabel("VAL_DATE_DEBUT_INVALIDE"))) {
                checkDateDebut = true;
            }
        }

        // Vérification de la date de fin
        if (!JadeStringUtil.isBlankOrZero(getDateFin())) {
            if (_checkDate(statement.getTransaction(), getDateFin(), getSession().getLabel("VAL_DATE_FIN_INVALIDE"))) {
                checkDateFin = true;
            }
        }

        // si les deux date sont renseignées et sont dans un format correct on
        // vérifie que la date de début soit avant celle de fin
        if (checkDateDebut && checkDateFin) {

            try {
                if (BSessionUtil.compareDateFirstGreater(getSession(), getDateDebut(), getDateFin())) {
                    _addError(statement.getTransaction(), getSession().getLabel("VAL_DATE_DEBUT_PLUS_PETITE"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // vérification du taux
        if (JadeStringUtil.isBlank(getTaux())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_TAUX"));
        } else {
            float taux = new Float(getTaux()).floatValue();
            if (taux <= 0) {
                _addError(statement.getTransaction(), getSession().getLabel("VAL_TAUX_SUP_ZERO"));
            }
        }
    }

    /**
     * @see globaz.globall.db.BEntity#_writePrimaryKey(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FIELD_IDCODETVA, _dbWriteNumeric(statement.getTransaction(), getIdCodeTVA(), ""));
    }

    /**
     * @see globaz.globall.db.BEntity#_writeProperties(globaz.globall.db.BStatement)
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FIELD_IDCODETVA, _dbWriteNumeric(statement.getTransaction(), getIdCodeTVA(), "idCodeTVA"));
        statement.writeField(FIELD_CSCODETVA, _dbWriteNumeric(statement.getTransaction(), getCsCodeTVA(), "csCodeTVA"));
        statement.writeField(FIELD_TAUX, _dbWriteNumeric(statement.getTransaction(), getTaux(), "taux"));
        statement.writeField(FIELD_DATEDEBUT, _dbWriteDateAMJ(statement.getTransaction(), getDateDebut(), "dateDebut"));
        statement.writeField(FIELD_DATEFIN, _dbWriteDateAMJ(statement.getTransaction(), getDateFin(), "dateFin"));
    }

    // *******************************************************
    // Getter
    // *******************************************************

    public String getCsCodeTVA() {
        return csCodeTVA;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getIdCodeTVA() {
        return idCodeTVA;
    }

    public String getTaux() {
        return JANumberFormatter.deQuote(taux);
    }

    // *******************************************************
    // Setter
    // *******************************************************

    public void setCsCodeTVA(String csCodeTVA) {
        this.csCodeTVA = csCodeTVA;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setIdCodeTVA(String idCodeTVA) {
        this.idCodeTVA = idCodeTVA;
    }

    public void setTaux(String taux) {
        this.taux = taux;
    }
}
