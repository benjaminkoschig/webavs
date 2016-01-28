package globaz.draco.db.declaration;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import globaz.globall.parameters.FWParametersSystemCode;

public class DSContentieuxViewBean extends BEntity {
    /** Fichier DSCONTP */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (TDTCON) */
    private String csContentieux = new String();
    // code systeme
    private FWParametersSystemCode csCsContentieux = null;
    /** (TDDATE) */
    private String date = new String();
    /** (TDICON) */
    private String idContentieux = new String();
    /** (TAIDDE) */
    private String idDeclaration = new String();

    /** (TDLREM) */
    private String remarque = new String();

    /**
     * Commentaire relatif au constructeur DSContentieux
     */
    public DSContentieuxViewBean() {
        super();
    }

    /**
     * Permet d'effectuer des op�rations avant de lancer l'ajout
     */
    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {
        // incr�mente de +1 le num�ro
        setIdContentieux(_incCounter(transaction, getIdContentieux()));
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "DSCONTP";
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propri�t�s �choue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idContentieux = statement.dbReadNumeric("TDICON");
        idDeclaration = statement.dbReadNumeric("TAIDDE");
        csContentieux = statement.dbReadNumeric("TDTCON");
        date = statement.dbReadDateAMJ("TDDATE");
        remarque = statement.dbReadString("TDLREM");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**
	
	 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("TDICON", _dbWriteNumeric(statement.getTransaction(), getIdContentieux(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement
                .writeField("TDICON", _dbWriteNumeric(statement.getTransaction(), getIdContentieux(), "idContentieux"));
        statement
                .writeField("TAIDDE", _dbWriteNumeric(statement.getTransaction(), getIdDeclaration(), "idDeclaration"));
        statement
                .writeField("TDTCON", _dbWriteNumeric(statement.getTransaction(), getCsContentieux(), "csContentieux"));
        statement.writeField("TDDATE", _dbWriteDateAMJ(statement.getTransaction(), getDate(), "date"));
        statement.writeField("TDLREM", _dbWriteString(statement.getTransaction(), getRemarque(), "remarque"));
    }

    public String getCsContentieux() {
        return csContentieux;
    }

    public FWParametersSystemCode getCsCsContentieux() {

        if (csCsContentieux == null) {
            // liste pas encore chargee, on la charge
            csCsContentieux = new FWParametersSystemCode();
            csCsContentieux.getCode(getCsContentieux());
        }
        return csCsContentieux;
    }

    public String getDate() {
        return date;
    }

    /**
     * Ins�rez la description de la m�thode ici.
     * 
     * @return String
     */
    public String getIdContentieux() {
        return idContentieux;
    }

    public String getIdDeclaration() {
        return idDeclaration;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setCsContentieux(String newCsContentieux) {
        csContentieux = newCsContentieux;
    }

    public void setDate(String newDate) {
        date = newDate;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.10.2002 13:52:58)
     * 
     * @param newD
     *            String
     */
    public void setIdContentieux(String newIdContentieux) {
        idContentieux = newIdContentieux;
    }

    public void setIdDeclaration(String newIdDeclaration) {
        idDeclaration = newIdDeclaration;
    }

    public void setRemarque(String newRemarque) {
        remarque = newRemarque;
    }

}
