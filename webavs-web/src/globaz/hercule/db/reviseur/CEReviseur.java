package globaz.hercule.db.reviseur;

import globaz.globall.db.BConstants;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.hercule.db.controleEmployeur.CEControleEmployeurManager;
import globaz.hercule.exception.HerculeException;
import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;

/**
 * Classe représentant un réviseur
 */
public class CEReviseur extends BEntity implements Serializable {

    private static final long serialVersionUID = 8755637531995554164L;
    public static int AK_VISAREVISEUR = 1;
    public static final String FIELD_IDREVISEUR = "MIIREV";

    public static final String FIELD_NOM_REVISEUR = "MILNOM";
    public static final String FIELD_REVISEURACTIF = "CEBRAV";
    public static final String FIELD_TYPE_REVISEUR = "MITTYR";
    public static final String FIELD_VISA = "MILVIS";
    public static final String TABLE_CEREVIP = "CEREVIP";
    public static final String TYPE_REV_EXT_RSA = "852004";
    public static final String TYPE_REV_EXT_SANS_SUVA = "852002";
    public static final String TYPE_REV_EXT_SUVA = "852003";
    public static final String TYPE_REV_INTERNE = "852001";

    private String idReviseur = new String();
    private String nomReviseur = new String();
    private Boolean reviseurActif = true;
    private String typeReviseur = new String();
    private String visa = new String();

    /**
     * Constructeur de CEReviseur.
     */
    public CEReviseur() {
        super();
    }

    @Override
    protected void _beforeAdd(final BTransaction transaction) throws Exception {
        setIdReviseur(this._incCounter(transaction, "0"));

        CEReviseurManager manager = new CEReviseurManager();
        manager.setSession(getSession());
        manager.setForVisa(getVisa());

        try {
            manager.find();
            if (manager.size() > 0) {
                _addError(transaction, getSession().getLabel("VISA_EXISTANT") + " : " + getVisa());
            }
        } catch (Exception e) {
            throw new HerculeException("Technical Exception, Unabled to add CEReviseur", e);
        }

        setReviseurActif(true);
    }

    @Override
    protected void _beforeDelete(final BTransaction transaction) throws Exception {
        CEControleEmployeurManager manager = new CEControleEmployeurManager();
        manager.setSession(getSession());
        manager.setLikeVisaReviseur(getVisa());

        try {
            manager.find();
            if (manager.size() > 0) {
                _addError(transaction, getSession().getLabel("REVISEUR_UTILISE") + " : " + getVisa());
            }
        } catch (Exception e) {
            throw new HerculeException("Technical Exception, Unabled to delere the reviseur", e);
        }
    }

    @Override
    protected String _getTableName() {
        return CEReviseur.TABLE_CEREVIP;
    }

    @Override
    protected void _readProperties(final BStatement statement) throws Exception {
        idReviseur = statement.dbReadNumeric(CEReviseur.FIELD_IDREVISEUR);
        visa = statement.dbReadString(CEReviseur.FIELD_VISA);
        nomReviseur = statement.dbReadString(CEReviseur.FIELD_NOM_REVISEUR);
        typeReviseur = statement.dbReadNumeric(CEReviseur.FIELD_TYPE_REVISEUR);
        reviseurActif = statement.dbReadBoolean(CEReviseur.FIELD_REVISEURACTIF);
    }

    @Override
    protected void _validate(final BStatement statement) throws Exception {
        if (JadeStringUtil.isEmpty(getVisa())) {
            _addError(statement.getTransaction(), getSession().getLabel("VAL_VISA_OBLIGATOIRE"));
        }
    }

    @Override
    protected void _writeAlternateKey(final BStatement statement, final int alternateKey) throws Exception {
        if (alternateKey == CEReviseur.AK_VISAREVISEUR) {
            statement.writeKey(CEReviseur.FIELD_VISA,
                    this._dbWriteString(statement.getTransaction(), getVisa(), CEReviseur.FIELD_VISA));
        }
    }

    @Override
    protected void _writePrimaryKey(final BStatement statement) throws Exception {
        statement.writeKey(CEReviseur.FIELD_IDREVISEUR,
                this._dbWriteNumeric(statement.getTransaction(), getIdReviseur(), ""));
    }

    @Override
    protected void _writeProperties(final BStatement statement) throws Exception {
        statement.writeField(CEReviseur.FIELD_IDREVISEUR,
                this._dbWriteNumeric(statement.getTransaction(), getIdReviseur(), "idReviseur"));
        statement.writeField(CEReviseur.FIELD_VISA, this._dbWriteString(statement.getTransaction(), getVisa(), "visa"));
        statement.writeField(CEReviseur.FIELD_NOM_REVISEUR,
                this._dbWriteString(statement.getTransaction(), getNomReviseur(), "nomReviseur"));
        statement.writeField(CEReviseur.FIELD_TYPE_REVISEUR,
                this._dbWriteNumeric(statement.getTransaction(), getTypeReviseur(), "typeReviseur"));
        statement.writeField(CEReviseur.FIELD_REVISEURACTIF, this._dbWriteBoolean(statement.getTransaction(),
                isReviseurActif(), BConstants.DB_TYPE_BOOLEAN_CHAR, "reviseurActif"));
    }

    public String getIdReviseur() {
        return idReviseur;
    }

    public String getNomReviseur() {
        return nomReviseur;
    }

    public Boolean getReviseurActif() {
        return reviseurActif;
    }

    public String getTypeReviseur() {
        return typeReviseur;
    }

    public String getVisa() {
        return visa;
    }

    public Boolean isReviseurActif() {
        return reviseurActif;
    }

    public void setIdReviseur(final String string) {
        idReviseur = string;
    }

    public void setNomReviseur(final String string) {
        nomReviseur = string;
    }

    public void setReviseurActif(final Boolean reviseurActif) {
        this.reviseurActif = reviseurActif;
    }

    public void setTypeReviseur(final String typeReviseur) {
        this.typeReviseur = typeReviseur;
    }

    public void setVisa(final String string) {
        visa = string;
    }
}
