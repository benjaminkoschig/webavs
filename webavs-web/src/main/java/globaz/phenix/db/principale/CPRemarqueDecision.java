package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

public class CPRemarqueDecision extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (IAIDEC) */
    private String emplacement = "";
    /** (INIREM) */
    private String idDecision = "";
    /** Fichier CPREDEP */
    private String idRemarqueDecision = "";
    /** (INTEMP) */
    private String texteRemarqueDecision = "";

    /** (INTEXT) */
    // code systeme

    /**
     * Commentaire relatif au constructeur CPRemarqueDecision
     */
    public CPRemarqueDecision() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incr�mente de +1 le num�ro
        setIdRemarqueDecision(_incCounter(transaction, idRemarqueDecision));

    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CPREDEP";
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propri�t�s �choue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRemarqueDecision = statement.dbReadNumeric("INIREM");
        idDecision = statement.dbReadNumeric("IAIDEC");
        emplacement = statement.dbReadNumeric("INTEMP");
        texteRemarqueDecision = statement.dbReadString("INTEXT");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'acc�s � la base
     */
    @Override
    protected void _validate(BStatement statement) {
        if (texteRemarqueDecision.length() > 255) {
            _addError(
                    statement.getTransaction(),
                    "La remarque d�passe la longueur maximale de 255 caract�res, elle est de "
                            + texteRemarqueDecision.length() + ". ");
        }
    }

    /**

 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("INIREM", _dbWriteNumeric(statement.getTransaction(), getIdRemarqueDecision(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField("INIREM",
                _dbWriteNumeric(statement.getTransaction(), getIdRemarqueDecision(), "idRemarqueDecision"));
        statement.writeField("IAIDEC", _dbWriteNumeric(statement.getTransaction(), getIdDecision(), "idDecision"));
        statement.writeField("INTEMP", _dbWriteNumeric(statement.getTransaction(), getEmplacement(), "emplacement"));
        statement.writeField("INTEXT",
                _dbWriteString(statement.getTransaction(), getTexteRemarqueDecision(), "texteRemarqueDecision"));
    }

    public String getEmplacement() {
        return emplacement;
    }

    public String getIdDecision() {
        return idDecision;
    }

    /**
     * Ins�rez la description de la m�thode ici.
     * 
     * @return String
     */
    public String getIdRemarqueDecision() {
        return idRemarqueDecision;
    }

    public String getTexteRemarqueDecision() {
        return texteRemarqueDecision;
    }

    public void setEmplacement(String newEmplacement) {
        emplacement = newEmplacement;
    }

    public void setIdDecision(String newIdDecision) {
        idDecision = newIdDecision;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setIdRemarqueDecision(String newIdRemarqueDecision) {
        idRemarqueDecision = newIdRemarqueDecision;
    }

    public void setTexteRemarqueDecision(String newTexteRemarqueDecision) {
        texteRemarqueDecision = newTexteRemarqueDecision;
    }
}
