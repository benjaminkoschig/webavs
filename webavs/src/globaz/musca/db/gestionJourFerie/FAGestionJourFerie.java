package globaz.musca.db.gestionJourFerie;

import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.globall.db.BTransaction;
import java.util.ArrayList;

/**
 * @author MMO
 * @since 4 aout 2010
 */
public class FAGestionJourFerie extends BEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final int AK_DATE_JOUR = 1;
    public static final String F_DATE_JOUR = "DATEJOUR";

    public static final String F_ID_JOUR = "IDJOUR";
    public static final String F_LIBELLE = "LIBELLE";
    /**
     * Constantes
     */
    public static final String TABLE_NAME = "FAFERIE";

    private String dateJour = "";
    private ArrayList<String> domaineFerie = new ArrayList<String>();
    /**
     * Attributs
     */
    private String idJour = "";
    private String libelle = "";

    @Override
    protected void _afterAdd(BTransaction transaction) throws Exception {
        FAGestionJourFerieUtil.cleanNullDomainIdIn(domaineFerie);
        addJointureFerieDomaine();
    }

    @Override
    protected void _afterDelete(BTransaction transaction) throws Exception {
        removeJointureFerieDomaine();
    }

    @Override
    protected void _afterRetrieve(BTransaction transaction) throws Exception {
        FAGestionJourFerieDomaineManager mgrFerieDomaine = new FAGestionJourFerieDomaineManager();
        mgrFerieDomaine.setSession(getSession());
        mgrFerieDomaine.setForIdFerie(idJour);
        mgrFerieDomaine.find();

        for (int i = 1; i <= mgrFerieDomaine.size(); i++) {
            domaineFerie.add(((FAGestionJourFerieDomaine) mgrFerieDomaine.getEntity(i - 1)).getIdDomaine());
        }
    }

    @Override
    protected void _afterUpdate(BTransaction transaction) throws Exception {
        FAGestionJourFerieUtil.cleanNullDomainIdIn(domaineFerie);
        removeJointureFerieDomaine();
        addJointureFerieDomaine();
    }

    /**
     * Méthodes
     */

    @Override
    protected void _beforeAdd(BTransaction transaction) throws Exception {
        setIdJour(this._incCounter(transaction, idJour));
    }

    @Override
    protected String _getTableName() {
        return FAGestionJourFerie.TABLE_NAME;
    }

    @Override
    protected void _readProperties(BStatement statement) throws Exception {
        idJour = statement.dbReadNumeric(FAGestionJourFerie.F_ID_JOUR);
        dateJour = statement.dbReadDateAMJ(FAGestionJourFerie.F_DATE_JOUR);
        libelle = statement.dbReadString(FAGestionJourFerie.F_LIBELLE);
    }

    @Override
    protected void _validate(BStatement statement) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void _writeAlternateKey(BStatement statement, int alternateKey) throws Exception {
        if (FAGestionJourFerie.AK_DATE_JOUR == alternateKey) {
            statement.writeKey(FAGestionJourFerie.F_DATE_JOUR,
                    this._dbWriteDateAMJ(statement.getTransaction(), getDateJour(), "dateJour"));
        }
    }

    @Override
    protected void _writePrimaryKey(BStatement statement) throws Exception {
        statement.writeKey(FAGestionJourFerie.F_ID_JOUR,
                this._dbWriteNumeric(statement.getTransaction(), getIdJour(), "idJour"));
    }

    @Override
    protected void _writeProperties(BStatement statement) throws Exception {
        statement.writeField(FAGestionJourFerie.F_ID_JOUR,
                this._dbWriteNumeric(statement.getTransaction(), getIdJour(), "idJour"));
        statement.writeField(FAGestionJourFerie.F_DATE_JOUR,
                this._dbWriteDateAMJ(statement.getTransaction(), getDateJour(), "dateJour"));
        statement.writeField(FAGestionJourFerie.F_LIBELLE,
                this._dbWriteString(statement.getTransaction(), getLibelle(), "libelle"));
    }

    private void addJointureFerieDomaine() throws Exception {
        FAGestionJourFerieDomaine ferieDomaineEntity = null;

        for (int i = 1; i <= domaineFerie.size(); i++) {
            ferieDomaineEntity = new FAGestionJourFerieDomaine();
            ferieDomaineEntity.setSession(getSession());
            ferieDomaineEntity.setIdFerie(idJour);
            ferieDomaineEntity.setIdDomaine(domaineFerie.get(i - 1));
            ferieDomaineEntity.add();
        }
    }

    public String getDateJour() {
        return dateJour;
    }

    public ArrayList<String> getDomaineFerie() {
        return domaineFerie;
    }

    /**
     * Getter
     */
    public String getIdJour() {
        return idJour;
    }

    public String getLibelle() {
        return libelle;
    }

    private void removeJointureFerieDomaine() throws Exception {
        FAGestionJourFerieDomaineManager mgrFerieDomaine = new FAGestionJourFerieDomaineManager();
        mgrFerieDomaine.setSession(getSession());
        mgrFerieDomaine.setForIdFerie(idJour);
        mgrFerieDomaine.delete();
    }

    public void setDateJour(String newDateJour) {
        dateJour = newDateJour;
    }

    public void setDomaineFerie(ArrayList<String> newDomaineFerie) {
        domaineFerie = newDomaineFerie;
    }

    /**
     * Setter
     */
    public void setIdJour(String newIdJour) {
        idJour = newIdJour;
    }

    public void setLibelle(String newLibelle) {
        libelle = newLibelle;
    }

}
