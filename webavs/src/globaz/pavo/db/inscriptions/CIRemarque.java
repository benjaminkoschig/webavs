package globaz.pavo.db.inscriptions;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import java.io.Serializable;

/**
 * Remarque. Date de création : (13.11.2002 09:44:42)
 * 
 * @author: ema
 */
public class CIRemarque extends BEntity implements Serializable {
    /** Fichier CIREMAP */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** (KBIECR) */
    private String ecritureId = new String();
    /** (KDID) */
    private String idDossierSplitting = new String();
    /** (KCID) */
    private String idJournal = new String();
    /** (KIIREM) */
    private String idRemarque = new String();
    /** (KILTEX) */
    private String texte = new String();

    // code systeme

    /**
     * Commentaire relatif au constructeur CIRemarque
     */
    public CIRemarque() {
        super();
    }

    @Override
    protected void _beforeAdd(BTransaction transaction) throws java.lang.Exception {

        // incrémente de +1 le numéro si pas d'erreur dans le bean
        if (!transaction.hasErrors()) {
            setIdRemarque(_incCounter(transaction, "0"));
        }
    }

    /**
     * Renvoie le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "CIREMAP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idRemarque = statement.dbReadNumeric("KIIREM");
        idJournal = statement.dbReadNumeric("KCID");
        ecritureId = statement.dbReadNumeric("KBIECR");
        idDossierSplitting = statement.dbReadNumeric("KDID");
        texte = statement.dbReadString("KILTEX");
    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */
    @Override
    protected void _validate(BStatement statement) {
    }

    /**

 */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("KIIREM", _dbWriteNumeric(statement.getTransaction(), getIdRemarque(), ""));
    }

    /**
     * write
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField("KIIREM", _dbWriteNumeric(statement.getTransaction(), getIdRemarque(), "idRemarque"));
        statement.writeField("KCID", _dbWriteNumeric(statement.getTransaction(), getIdJournal(), "idJournal"));
        statement.writeField("KBIECR", _dbWriteNumeric(statement.getTransaction(), getEcritureId(), "ecritureId"));
        statement.writeField("KDID",
                _dbWriteNumeric(statement.getTransaction(), getIdDossierSplitting(), "idDossierSplitting"));
        statement.writeField("KILTEX", _dbWriteString(statement.getTransaction(), getTexte(), "texte"));
    }

    public String getEcritureId() {
        return ecritureId;
    }

    public String getIdDossierSplitting() {
        return idDossierSplitting;
    }

    public String getIdJournal() {
        return idJournal;
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    public String getIdRemarque() {
        return idRemarque;
    }

    public String getTexte() {
        return texte;
    }

    public void setEcritureId(String newEcritureId) {
        ecritureId = newEcritureId;
    }

    public void setIdDossierSplitting(String newIdDossierSplitting) {
        idDossierSplitting = newIdDossierSplitting;
    }

    public void setIdJournal(String newIdJournal) {
        idJournal = newIdJournal;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    public void setIdRemarque(String newIdRemarque) {
        idRemarque = newIdRemarque;
    }

    public void setTexte(String newTexte) {
        texte = newTexte;
    }
}
