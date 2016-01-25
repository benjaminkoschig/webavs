package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Cr�� le 10 f�vr. 06
 * 
 * @author dch
 * 
 *         Repr�sente un historique de rubriques (JAFPRUBR)
 */
public class ALHistoriqueRubriques extends BEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idHistoriqueRubriques = "";
    private String idDetailPrestation = "";
    private String categorieRubrique = "";
    private String chiffreStatistique = "";

    /**
     * Renvoie le nom de la table.
     * 
     * @return le nom de la table
     */
    @Override
    protected String _getTableName() {
        return "JAFPRUBR";
    }

    /**
     * Lit les valeurs des propri�t�s propres de l'entit� � partir de la base de donn�es.
     * 
     * @exception java.lang.Exception si la lecture des propri�t�s a �chou�e
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idHistoriqueRubriques = statement.dbReadNumeric("A7ID");
        idDetailPrestation = statement.dbReadNumeric("A7IDDT");
        categorieRubrique = statement.dbReadString("A7CAT");
        chiffreStatistique = statement.dbReadString("A7CHST");
    }

    /**
     * Valide le contenu de l'entit� (notamment les champs obligatoires).
     * 
     * @exception java.lang.Exception en cas d'erreur
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propri�t�s propres de l'entit� composant la cl� primaire.
     * 
     * @exception java.lang.Exception si la sauvegarde des propri�t�s a �chou�e
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("A7ID", _dbWriteNumeric(statement.getTransaction(), idHistoriqueRubriques, ""));
    }

    /**
     * Sauvegarde les valeurs des propri�t�s propres de l'entit� dans la base de donn�es.
     * 
     * @param statement l'instruction � utiliser
     * @exception java.lang.Exception si la sauvegarde des propri�t�s a �chou�e
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("A7ID", _dbWriteNumeric(statement.getTransaction(), idHistoriqueRubriques, ""));
        statement.writeField("A7IDDT", _dbWriteNumeric(statement.getTransaction(), idDetailPrestation, ""));
        statement.writeField("A7CAT", _dbWriteString(statement.getTransaction(), categorieRubrique, ""));
        statement.writeField("A7CHST", _dbWriteString(statement.getTransaction(), chiffreStatistique, ""));
    }

    /**
     * Renvoie si l'entit� contient un espion.
     */
    @Override
    public boolean hasSpy() {
        return false;
    }

    /**
     * @return
     */
    public String getCategorieRubrique() {
        return categorieRubrique;
    }

    /**
     * @return
     */
    public String getChiffreStatistique() {
        return chiffreStatistique;
    }

    /**
     * @return
     */
    public String getIdDetailPrestation() {
        return idDetailPrestation;
    }

    /**
     * @return
     */
    public String getIdHistoriqueRubriques() {
        return idHistoriqueRubriques;
    }

    /**
     * @param string
     */
    public void setCategorieRubrique(String string) {
        categorieRubrique = string;
    }

    /**
     * @param string
     */
    public void setChiffreStatistique(String string) {
        chiffreStatistique = string;
    }

    /**
     * @param string
     */
    public void setIdDetailPrestation(String string) {
        idDetailPrestation = string;
    }

    /**
     * @param string
     */
    public void setIdHistoriqueRubriques(String string) {
        idHistoriqueRubriques = string;
    }
}