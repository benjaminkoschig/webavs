/**
 *
 */
package globaz.osiris.db.comptecourant;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;

/**
 * @author SEL
 * 
 */
public class CAConcordanceCACG extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String compteCG = "";
    private String difference = "";
    private String idCompte = "";

    private String idJournal = "";

    private String idJournalExterne = "";
    private String libelleCG = "";
    private String numJournal = "";
    private String soldeCA = "";
    private String soldeCG = "";

    @Override
    protected String _getTableName() {
        return null;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        setCompteCG(statement.dbReadString("comptecg"));
        setIdCompte(statement.dbReadString("idcompte"));
        setIdJournal(statement.dbReadString("idjournal"));
        setIdJournalExterne(statement.dbReadString("idjournalexterne"));
        setNumJournal(statement.dbReadString("numero"));
        setSoldeCA(statement.dbReadString("soldeca"));
        setSoldeCG(statement.dbReadString("soldeCG"));
        setDifference(statement.dbReadString("diff"));
        setLibelleCG(statement.dbReadString("libelleCG"));
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // nope
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        // nope
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        // nope
    }

    /**
     * @return the compteCG
     */
    public String getCompteCG() {
        return compteCG;
    }

    /**
     * @return the difference
     */
    public String getDifference() {
        return difference;
    }

    /**
     * @return the idCompte
     */
    public String getIdCompte() {
        return idCompte;
    }

    /**
     * @return the idJournal
     */
    public String getIdJournal() {
        return idJournal;
    }

    /**
     * @return the idJournalExterne
     */
    public String getIdJournalExterne() {
        return idJournalExterne;
    }

    /**
     * @return the libelleCG
     */
    public String getLibelleCG() {
        return libelleCG;
    }

    /**
     * @return the numJournal
     */
    public String getNumJournal() {
        return numJournal;
    }

    /**
     * @return the soldeCA
     */
    public String getSoldeCA() {
        return soldeCA;
    }

    /**
     * @return the soldeCG
     */
    public String getSoldeCG() {
        return soldeCG;
    }

    /**
     * @param compteCG
     *            the compteCG to set
     */
    public void setCompteCG(String compteCG) {
        this.compteCG = compteCG;
    }

    /**
     * @param difference
     *            the difference to set
     */
    public void setDifference(String difference) {
        this.difference = difference;
    }

    /**
     * @param idCompte
     *            the idCompte to set
     */
    public void setIdCompte(String idCompte) {
        this.idCompte = idCompte;
    }

    /**
     * @param idJournal
     *            the idJournal to set
     */
    public void setIdJournal(String idJournal) {
        this.idJournal = idJournal;
    }

    /**
     * @param idJournalExterne
     *            the idJournalExterne to set
     */
    public void setIdJournalExterne(String idJournalExterne) {
        this.idJournalExterne = idJournalExterne;
    }

    /**
     * @param libelleCG
     *            the libelleCG to set
     */
    public void setLibelleCG(String libelleCG) {
        this.libelleCG = libelleCG;
    }

    /**
     * @param numJournal
     *            the numJournal to set
     */
    public void setNumJournal(String numJournal) {
        this.numJournal = numJournal;
    }

    /**
     * @param soldeCA
     *            the soldeCA to set
     */
    public void setSoldeCA(String soldeCA) {
        this.soldeCA = soldeCA;
    }

    /**
     * @param soldeCG
     *            the soldeCG to set
     */
    public void setSoldeCG(String soldeCG) {
        this.soldeCG = soldeCG;
    }
}
