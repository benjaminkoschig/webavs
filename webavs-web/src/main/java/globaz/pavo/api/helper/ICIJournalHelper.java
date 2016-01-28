package globaz.pavo.api.helper;

import globaz.globall.api.BITransaction;
import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.pavo.api.ICIJournal;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class ICIJournalHelper extends GlobazHelper implements ICIJournal {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type ICIJournalHelper
     */
    public ICIJournalHelper() {
        super("globaz.pavo.db.inscriptions.CIJournal");
    }

    /**
     * Constructeur du type ICIJournalHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public ICIJournalHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type ICIJournalHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public ICIJournalHelper(String implementationClassName) {
        super(implementationClassName);
    }

    /**
     * Passe les inscriptions du journal au CI. Il est également possible d'inscrire au CI qu'une partie du journal en
     * spécifiant un sous-ensemble à l'aide des paramètres <tt>fromAvs</tt> et <tt>toAvs</tt>.
     * 
     * @param fromAvs
     *            limite inférieure des écritures à inscrire au CI.
     * @param fromAvs
     *            limite supérieure des écritures à inscrire au CI.
     * @param transaction
     *            la transasction à utiliser pour l'opération.
     * @exception si
     *                l'appel distant de la fonction n'a pas pu être effectué.
     */
    @Override
    public void comptabiliser(String fromAvs, String toAvs, BITransaction transaction) throws Exception {
        _execute("comptabiliser", new Object[] { fromAvs, toAvs, transaction });
    }

    @Override
    public String getAnneeCotisation() {
        return (String) _getValueObject().getProperty("anneeCotisation");
    }

    @Override
    public String getCorrectionSpeciale() {
        return (String) _getValueObject().getProperty("correctionSpeciale");
    }

    @Override
    public String getDate() {
        return (String) _getValueObject().getProperty("date");
    }

    @Override
    public String getIdEtat() {
        return (String) _getValueObject().getProperty("idEtat");
    }

    @Override
    public String getIdJournal() {
        return (String) _getValueObject().getProperty("idJournal");
    }

    @Override
    public String getIdTypeInscription() {
        return (String) _getValueObject().getProperty("idTypeInscription");
    }

    @Override
    public String getLibelle() {
        return (String) _getValueObject().getProperty("libelle");
    }

    @Override
    public String getMotifCorrection() {
        return (String) _getValueObject().getProperty("motifCorrection");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 10:09:42)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getRemTexte() {
        return (java.lang.String) _getValueObject().getProperty("remTexte");
    }

    @Override
    public String getTotalControle() {
        return (String) _getValueObject().getProperty("totalControle");
    }

    @Override
    public String getTotalInscrit() {
        return (String) _getValueObject().getProperty("totalInscrit");
    }

    @Override
    public void setAnneeCotisation(String newAnneeCotisation) {
        _getValueObject().setProperty("anneeCotisation", newAnneeCotisation);
    }

    @Override
    public void setCorrectionSpeciale(String newCorrectionSpeciale) {
        _getValueObject().setProperty("correctionSpeciale", newCorrectionSpeciale);
    }

    @Override
    public void setIdJournal(String newIdJournal) {
        _getValueObject().setProperty("idJournal", newIdJournal);
    }

    @Override
    public void setIdTypeInscription(String newIdTypeInscription) {
        _getValueObject().setProperty("idTypeInscription", newIdTypeInscription);
    }

    @Override
    public void setLibelle(String newLibelle) {
        _getValueObject().setProperty("libelle", newLibelle);
    }

    @Override
    public void setMotifCorrection(String newMotifCorrection) {
        _getValueObject().setProperty("motifCorrection", newMotifCorrection);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (13.11.2002 10:09:42)
     * 
     * @param newRemTexte
     *            java.lang.String
     */
    @Override
    public void setRemTexte(java.lang.String newRemTexte) {
        _getValueObject().setProperty("remTexte", newRemTexte);
    }

    @Override
    public void setTotalControle(String newTotalControle) {
        _getValueObject().setProperty("totalControle", newTotalControle);
    }
}
