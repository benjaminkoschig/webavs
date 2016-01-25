package globaz.phenix.db.principale;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.util.JANumberFormatter;

public class CPSortieMontant extends BEntity {
    /**
     * Fichier CPMOSOP
     */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** (ISTGCO) */
    private String assurance = "";

    private String debutSortie = "";

    private String finSortie = "";

    /** (IZISOR) */
    private String idSortie = "";
    /** (IKIMSO) */
    private String idSortieMontant = "";
    /** (IKMONT) */
    private String montant = "";

    public CPSortieMontant() {
        super();
    }

    /*
     * Traitement avant ajout
     */
    @Override
    protected void _beforeAdd(globaz.globall.db.BTransaction transaction) throws java.lang.Exception {
        // incrémente de +1 le numéro
        setIdSortieMontant(_incCounter(transaction, idSortieMontant));

    }

    /**
     * Renvoie le nom de la table
     */

    @Override
    protected String _getTableName() {
        return "CPMOSOP";
    }

    /**
     * Lit les valeurs des propriétés propres de l'entité à partir de la bdd
     * 
     * @exception Exception
     *                si la lecture des propriétés échoue
     */

    @Override
    protected void _readProperties(BStatement statement) throws Exception {

        idSortieMontant = statement.dbReadNumeric("IKIMSO");
        idSortie = statement.dbReadNumeric("IZISOR");
        montant = statement.dbReadNumeric("IKMONT");
        assurance = statement.dbReadString("MEICOT");
        debutSortie = statement.dbReadDateAMJ("IKDDEB");
        finSortie = statement.dbReadDateAMJ("IKDFIN");

    }

    /**
     * valide le contenu de l'entite (notamment les champs obligatoires)
     * 
     * @param statement
     *            L'objet d'accès à la base
     */

    @Override
    protected void _validate(BStatement statement) {
        // try {
        // if(!JadeStringUtil.isEmpty(getIdPassage())){
        // FAPassage passage = new FAPassage();
        // passage.setSession(getSession());
        // passage.setIdPassage(getIdPassage());
        // passage.retrieve();
        // if (passage == null || passage.isNew()) {
        // _addError(statement.getTransaction(),
        // "Le n° de passage est inexistant. ");
        // }
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        try {
            CPSortieMontantManager sortieMontantMng = new CPSortieMontantManager();
            sortieMontantMng.setSession(getSession());
            sortieMontantMng.setForIdSortie(getIdSortie());
            sortieMontantMng.setForAssurance(getAssurance());
            sortieMontantMng.setExceptIdMontantSortie(getIdSortieMontant());
            sortieMontantMng.find(statement.getTransaction());
            if (sortieMontantMng.size() >= 1) {
                _addError(statement.getTransaction(), getSession().getLabel("CP_MSG_0093"));
            }
        } catch (Exception e) {
            _addError(statement.getTransaction(), e.getMessage());
        }
    }

    /**
     * Sauvegarde les valeurs des propriétés propres de l'entité composant une clé alternée
     * 
     * @exception java.lang.Exception
     *                si la sauvegarde des propriétés a échouée
     * @param alternateKey
     *            int le numéro de la clé alternée à utiliser
     */
    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        // Traitement par défaut : pas de clé alternée
        // Recherche par sortie et genre de cotisation
        if (alternateKey == 1) {
            statement.writeKey(_getBaseTable() + "IZISOR",
                    _dbWriteNumeric(statement.getTransaction(), getIdSortie(), ""));
            statement.writeKey(_getBaseTable() + "MEICOT",
                    _dbWriteNumeric(statement.getTransaction(), getAssurance(), ""));
        }
    }

    /**
	
	 */

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey("IKIMSO", _dbWriteNumeric(statement.getTransaction(), getIdSortieMontant(), ""));
    }

    /**
     * write
     */

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {

        statement.writeField("IKIMSO",
                _dbWriteNumeric(statement.getTransaction(), getIdSortieMontant(), "idSortieMontant"));
        statement.writeField("IZISOR", _dbWriteNumeric(statement.getTransaction(), getIdSortie(), "idSortie"));
        statement.writeField("IKMONT", _dbWriteNumeric(statement.getTransaction(), getMontant(), "montant"));
        statement.writeField("MEICOT", _dbWriteNumeric(statement.getTransaction(), getAssurance(), "assurance"));
        statement.writeField("IKDDEB", _dbWriteDateAMJ(statement.getTransaction(), getDebutSortie(), "debutSortie"));
        statement.writeField("IKDFIN", _dbWriteDateAMJ(statement.getTransaction(), getFinSortie(), "finSortie"));
    }

    /**
     * @return
     */
    public String getAssurance() {
        return assurance;
    }

    /**
     * @return
     */
    public String getDebutSortie() {
        return debutSortie;
    }

    /**
     * @return
     */
    public String getFinSortie() {
        return finSortie;
    }

    public String getIdSortie() {
        return idSortie;
    }

    /**
     * @return
     */
    public String getIdSortieMontant() {
        return idSortieMontant;
    }

    /**
     * @return
     */
    public String getMontant() {
        try {
            return JANumberFormatter.format(montant);
        } catch (Exception e) {
            return montant;
        }
    }

    /**
     * @param string
     */
    public void setAssurance(String string) {
        assurance = string;
    }

    /**
     * @param string
     */
    public void setDebutSortie(String string) {
        debutSortie = string;
    }

    /**
     * @param string
     */
    public void setFinSortie(String string) {
        finSortie = string;
    }

    public void setIdSortie(String newIdSortie) {
        idSortie = newIdSortie;
    }

    /**
     * @param string
     */
    public void setIdSortieMontant(String string) {
        idSortieMontant = string;
    }

    /**
     * @param string
     */
    public void setMontant(String string) {
        montant = string;
    }

}