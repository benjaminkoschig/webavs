package globaz.hermes.db.access;

import globaz.globall.db.BEntity;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;
import globaz.globall.util.JACalendar;
import globaz.jade.client.util.JadeStringUtil;

public class HEInfos extends BEntity {

    private static final long serialVersionUID = 6345978464727129215L;
    public final static int ALTERNATE_KEY_IDARC_TYPEINFO = 1;
    public final static String CS_ADRESSE_ASSURE = "112501";
    public final static String CS_CATEGORIE = "112507";
    public final static String CS_DATE_ENGAGEMENT = "112505";
    public final static String CS_LANGUE_CORRESPONDANCE = "112503";
    public final static String CS_NUMERO_AFFILIE = "112502";

    public final static String CS_NUMERO_EMPLOYE = "112509";

    public final static String CS_NUMERO_SUCCURSALE = "112508";

    public final static String CS_REMARQUE_RCI = "112504";

    public final static String CS_TITRE_ASSURE = "112506";
    public final static String CS_FORMULE_POLITESSE = "112510";

    // code systeme
    private FWParametersSystemCode csTypeInfo = null;

    /** (RNIANN) */
    private String idArc = new String();

    /** Fichier WEBAVS_D.HEINCOP */
    /** (IDINCO) */
    private String idInfoComp = new String();

    /** (LIBINCO) */
    private String libInfo = new String();

    /** (TYPEINCO) */
    private String typeInfo = new String();

    /**
     * Commentaire relatif au constructeur HEInfos
     */
    public HEInfos() {
        super();
    }

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdInfoComp(_incCounter(transaction, "0"));
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "HEINCOP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idInfoComp = statement.dbReadNumeric("IDINCO");
        idArc = statement.dbReadNumeric("RNIANN");
        typeInfo = statement.dbReadNumeric("TYPEINCO");
        libInfo = statement.dbReadString("LIBINCO");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
        if (CS_DATE_ENGAGEMENT.equals(getTypeInfo()) && !JadeStringUtil.isEmpty(getLibInfo())) {
            try {
                String date = getLibInfo();
                if (date.trim().length() == 4) {
                    date = "00.00." + date.trim();
                }
                BSessionUtil.checkDateGregorian(getSession(), date);
                setLibInfo(JACalendar.format(date));
            } catch (Exception e) {
                _addError(statement.getTransaction(), getSession().getLabel("HERMES_10035"));
            }
        }
    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (alternateKey == ALTERNATE_KEY_IDARC_TYPEINFO) {
            statement.writeKey("RNIANN", _dbWriteNumeric(statement.getTransaction(), getIdArc()));
            statement.writeKey("TYPEINCO", _dbWriteNumeric(statement.getTransaction(), getTypeInfo()));
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("IDINCO",
                _dbWriteNumeric(statement.getTransaction(), getIdInfoComp(), "Clef primaire IDINCO"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("IDINCO", _dbWriteNumeric(statement.getTransaction(), getIdInfoComp(), "idInfoComp"));
        statement.writeField("RNIANN", _dbWriteNumeric(statement.getTransaction(), getIdArc(), "idArc"));
        statement.writeField("TYPEINCO", _dbWriteNumeric(statement.getTransaction(), getTypeInfo(), "typeInfo"));
        statement.writeField("LIBINCO", _dbWriteString(statement.getTransaction(), getLibInfo(), "libInfo"));
    }

    public FWParametersSystemCode getCsTypeInfo() {
        if (csTypeInfo == null) {
            // liste pas encore chargee, on la charge
            csTypeInfo = new FWParametersSystemCode();
            csTypeInfo.getCode(getTypeInfo());
        }
        return csTypeInfo;
    }

    public String getIdArc() {
        return idArc;
    }

    public String getIdInfoComp() {
        return idInfoComp;
    }

    public String getLibInfo() {
        return libInfo;
    }

    public String getTypeInfo() {
        return typeInfo;
    }

    public void setIdArc(String newIdArc) {
        idArc = newIdArc;
    }

    public void setIdInfoComp(String newIdInfoComp) {
        idInfoComp = newIdInfoComp;
    }

    public void setLibInfo(String newLibInfo) {
        libInfo = newLibInfo;
    }

    public void setTypeInfo(String newTypeInfo) {
        typeInfo = newTypeInfo;
    }

    public String toMyString() {
        return getIdInfoComp() + " - " + getLibInfo();
    }

}
