package globaz.musca.api.helper;

import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.musca.api.IFAPassage;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class IFAPassageHelper extends GlobazHelper implements IFAPassage {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type IFAPassageHelper
     */
    public IFAPassageHelper() {
        super("globaz.musca.db.facturation.FAPassage");
    }

    /**
     * Constructeur du type IFAPassageHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IFAPassageHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type IFAPassageHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IFAPassageHelper(String implementationClassName) {
        super(implementationClassName);
    }

    @Override
    public java.lang.String getDateComptabilisation() {
        return (java.lang.String) _getValueObject().getProperty("dateComptabilisation");
    }

    @Override
    public java.lang.String getDateCreation() {
        return (java.lang.String) _getValueObject().getProperty("dateCreation");
    }

    @Override
    public java.lang.String getDateEcheance() {
        return (java.lang.String) _getValueObject().getProperty("dateEcheance");
    }

    @Override
    public java.lang.String getDateFacturation() {
        return (java.lang.String) _getValueObject().getProperty("dateFacturation");
    }

    @Override
    public java.lang.String getDatePeriode() {
        return (java.lang.String) _getValueObject().getProperty("datePeriode");
    }

    @Override
    public java.lang.String getIdDernierPassage() {
        return (java.lang.String) _getValueObject().getProperty("idDernierPassage");
    }

    @Override
    public java.lang.String getIdJournal() {
        return (java.lang.String) _getValueObject().getProperty("idJournal");
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdPassage() {
        return (java.lang.String) _getValueObject().getProperty("idPassage");
    }

    @Override
    public java.lang.String getIdPlanFacturation() {
        return (java.lang.String) _getValueObject().getProperty("idPlanFacturation");
    }

    @Override
    public java.lang.String getIdRemarque() {
        return (java.lang.String) _getValueObject().getProperty("idRemarque");
    }

    @Override
    public java.lang.String getIdTypeFacturation() {
        return (java.lang.String) _getValueObject().getProperty("idTypeFacturation");
    }

    @Override
    public Boolean getIsAuto() {
        return (java.lang.Boolean) _getValueObject().getProperty("isAuto");
    }

    @Override
    public java.lang.String getLibelle() {
        return (java.lang.String) _getValueObject().getProperty("libelle");
    }

    @Override
    public java.lang.String getRemarque() {
        return (java.lang.String) _getValueObject().getProperty("remarque");
    }

    @Override
    public java.lang.String getStatus() {
        return (java.lang.String) _getValueObject().getProperty("status");
    }

    @Override
    public void setDateComptabilisation(java.lang.String newDateComptabilisation) {
        _getValueObject().setProperty("dateComptabilisation", newDateComptabilisation);
    }

    @Override
    public void setDateCreation(java.lang.String newDateCreation) {
        _getValueObject().setProperty("dateCreation", newDateCreation);
    }

    @Override
    public void setDateEcheance(java.lang.String newDateEcheance) {
        _getValueObject().setProperty("dateEcheance", newDateEcheance);
    }

    @Override
    public void setDateFacturation(java.lang.String newDateFacturation) {
        _getValueObject().setProperty("dateFacturation", newDateFacturation);
    }

    @Override
    public void setDatePeriode(java.lang.String newDatePeriode) {
        _getValueObject().setProperty("datePeriode", newDatePeriode);
    }

    @Override
    public void setEstVerrouille(java.lang.Boolean newEstVerrouille) {
        _getValueObject().setProperty("estVerrouille", newEstVerrouille);
    }

    @Override
    public void setIdJournal(java.lang.String newIdJournal) {
        _getValueObject().setProperty("idJournal", newIdJournal);
    }

    /**
     * Setter
     */
    @Override
    public void setIdPassage(java.lang.String newIdPassage) {
        _getValueObject().setProperty("idPassage", newIdPassage);
    }

    @Override
    public void setIdPlanFacturation(java.lang.String newIdPlanFacturation) {
        _getValueObject().setProperty("idPlanFacturation", newIdPlanFacturation);
    }

    @Override
    public void setIdRemarque(java.lang.String newIdRemarque) {
        _getValueObject().setProperty("idRemarque", newIdRemarque);
    }

    @Override
    public void setIdTypeFacturation(java.lang.String newIdTypeFacturation) {
        _getValueObject().setProperty("idTypeFacturation", newIdTypeFacturation);
    }

    @Override
    public void setIsAuto(Boolean newIsAuto) {
        _getValueObject().setProperty("isAuto", newIsAuto);

    }

    @Override
    public void setLibelle(java.lang.String newLibelle) {
        _getValueObject().setProperty("libelle", newLibelle);
    }

    @Override
    public void setRemarque(java.lang.String newRemarque) {
        _getValueObject().setProperty("remarque", newRemarque);
    }

    @Override
    public void setStatus(java.lang.String newStatus) {
        _getValueObject().setProperty("status", newStatus);
    }
}
