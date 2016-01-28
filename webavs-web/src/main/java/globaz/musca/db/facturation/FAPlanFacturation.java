package globaz.musca.db.facturation;

import globaz.globall.db.BConstants;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;

public class FAPlanFacturation extends globaz.globall.db.BEntity implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public final static java.lang.String TABLE_FIELDS = "FAPLFAP.IDPLANFACTURATION, FAPLFAP.LIBELLEFR, FAPLFAP.LIBELLEDE, FAPLFAP.LIBELLEIT, FAPLFAP.IDTYPEFACTURATION, FAPLFAP.PLANDEFAUT, FAPLFAP.PSPY";
    private java.lang.String idPlanFacturation = new String();
    private java.lang.String idTypeFacturation = new String();
    private java.lang.String libelleDe = new String();
    private java.lang.String libelleFr = new String();
    private java.lang.String libelleIt = new String();
    private java.lang.Boolean planDefaut = new Boolean(false);

    /**
     * Commentaire relatif au constructeur FAPlanFacturation
     */
    public FAPlanFacturation() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdPlanFacturation(this._incCounter(transaction, idPlanFacturation));
        // setIdPlanFacturation(_incCounter(transaction, "0"));
    }

    /**
     * Renvoie la liste des champs
     * 
     * @return la liste des champs
     */
    @Override
    protected String _getFields(BStatement statement) {
        return FAPlanFacturation.TABLE_FIELDS;
    }

    /**
     * Renvoie la clause FROM
     * 
     * @return la clause FROM
     */
    @Override
    protected String _getFrom(BStatement statement) {
        return _getCollection() + "FAPLFAP  AS FAPLFAP ";
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "FAPLFAP";
    }

    /**
     * read
     */
    @Override
    protected void _readProperties(globaz.globall.db.BStatement statement) throws Exception {
        idPlanFacturation = statement.dbReadNumeric("IDPLANFACTURATION");
        libelleFr = statement.dbReadString("LIBELLEFR");
        libelleDe = statement.dbReadString("LIBELLEDE");
        libelleIt = statement.dbReadString("LIBELLEIT");
        idTypeFacturation = statement.dbReadNumeric("IDTYPEFACTURATION");
        planDefaut = statement.dbReadBoolean("PLANDEFAUT");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _validate(globaz.globall.db.BStatement statement) throws Exception {
        // Le libelle de la langue de l'application est obligatoire

        // En attendant les paramêtres pas défaut, on fait le test sur la langue
        // de l'utilisateur
        // !!!!!!!!!!!!!!!! A modifier
        String langue = getSession().getIdLangueISO();

        if ((langue.equals("FR")) && (JadeStringUtil.isBlank(getLibelleFr()))) {
            _addError(statement.getTransaction(), "Le libellé en français doit être renseigné. ");
        }
        if ((langue.equals("AL")) && (JadeStringUtil.isBlank(getLibelleDe()))) {
            _addError(statement.getTransaction(), "Le libellé en allemand doit être renseigné. ");
        }
        if ((langue.equals("IT")) && (JadeStringUtil.isBlank(getLibelleIt()))) {
            _addError(statement.getTransaction(), "Le libellé en italien doit être renseigné. ");
        }
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     */
    @Override
    protected void _writePrimaryKey(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeKey("IDPLANFACTURATION",
                this._dbWriteNumeric(statement.getTransaction(), getIdPlanFacturation(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(globaz.globall.db.BStatement statement) throws Exception {
        statement.writeField("IDPLANFACTURATION",
                this._dbWriteNumeric(statement.getTransaction(), getIdPlanFacturation(), "idPlanFacturation"));
        statement.writeField("LIBELLEFR", this._dbWriteString(statement.getTransaction(), getLibelleFr(), "libelleFr"));
        statement.writeField("LIBELLEDE", this._dbWriteString(statement.getTransaction(), getLibelleDe(), "libelleDe"));
        statement.writeField("LIBELLEIT", this._dbWriteString(statement.getTransaction(), getLibelleIt(), "libelleIt"));
        statement.writeField("IDTYPEFACTURATION",
                this._dbWriteNumeric(statement.getTransaction(), getIdTypeFacturation(), "idTypeFacturation"));
        statement.writeField("PLANDEFAUT", this._dbWriteBoolean(statement.getTransaction(), isPlanDefaut(),
                BConstants.DB_TYPE_BOOLEAN_CHAR, "planDefaut"));

    }

    /**
     * Getter
     */
    public java.lang.String getIdPlanFacturation() {
        return idPlanFacturation;
    }

    public java.lang.String getIdTypeFacturation() {
        return idTypeFacturation;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (28.05.2002 09:20:10)
     * 
     * @return int
     */
    public java.lang.String getLibelle() {
        String langue = getSession().getIdLangueISO();

        if (langue.equalsIgnoreCase("fr")) {

            return getLibelleFr();
        }
        if (langue.equalsIgnoreCase("de")) {

            return libelleDe;
        } else {

            return libelleIt;
        }
    }

    public java.lang.String getLibelleDe() {
        return libelleDe;
    }

    public java.lang.String getLibelleFr() {
        return libelleFr;
    }

    public java.lang.String getLibelleIt() {
        return libelleIt;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.06.2003 16:45:40)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLibelleType() {
        return getSession().getCodeLibelle(idTypeFacturation);
    }

    public java.lang.Boolean isPlanDefaut() {
        return planDefaut;
    }

    /**
     * Setter
     */
    public void setIdPlanFacturation(java.lang.String newIdPlanFacturation) {
        idPlanFacturation = newIdPlanFacturation;
    }

    public void setIdTypeFacturation(java.lang.String newIdTypeFacturation) {
        idTypeFacturation = newIdTypeFacturation;
    }

    public void setLibelleDe(java.lang.String newLibelleDe) {
        libelleDe = newLibelleDe;
    }

    public void setLibelleFr(java.lang.String newLibelleFr) {
        libelleFr = newLibelleFr;
    }

    public void setLibelleIt(java.lang.String newLibelleIt) {
        libelleIt = newLibelleIt;
    }

    public void setPlanDefaut(java.lang.Boolean newPlanDefaut) {
        planDefaut = newPlanDefaut;
    }
}
