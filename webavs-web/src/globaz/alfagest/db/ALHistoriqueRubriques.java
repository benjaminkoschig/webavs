package globaz.alfagest.db;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * Créé le 10 févr. 06
 * 
 * @author dch
 * 
 *         Représente un historique de rubriques (JAFPRUBR)
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
     * Lit les valeurs des propriétés propres de l'entité à partir de la base de données.
     * 
     * @exception java.lang.Exception si la lecture des propriétés a échouée
     */
    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idHistoriqueRubriques = statement.dbReadNumeric("A7ID");
        idDetailPrestation = statement.dbReadNumeric("A7IDDT");
        categorieRubrique = statement.dbReadString("A7CAT");
        chiffreStatistique = statement.dbReadString("A7CHST");
    }

    /**
     * Valide le contenu de l'entité (notamment les champs obligatoires).
     * 
     * @exception java.lang.Exception en cas d'erreur
     */
    @Override
    protected void _validate(BStatement statement) throws Exception {
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant la clé primaire.
     * 
     * @exception java.lang.Exception si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("A7ID", _dbWriteNumeric(statement.getTransaction(), idHistoriqueRubriques, ""));
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité dans la base de données.
     * 
     * @param statement l'instruction à utiliser
     * @exception java.lang.Exception si la sauvegarde des propriétés a échouée
     */
    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField("A7ID", _dbWriteNumeric(statement.getTransaction(), idHistoriqueRubriques, ""));
        statement.writeField("A7IDDT", _dbWriteNumeric(statement.getTransaction(), idDetailPrestation, ""));
        statement.writeField("A7CAT", _dbWriteString(statement.getTransaction(), categorieRubrique, ""));
        statement.writeField("A7CHST", _dbWriteString(statement.getTransaction(), chiffreStatistique, ""));
    }

    /**
     * Renvoie si l'entité contient un espion.
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