package globaz.musca.api.helper;

import globaz.globall.api.BITransaction;
import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.musca.api.IFAEnteteFacture;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class IFAEnteteFactureHelper extends GlobazHelper implements IFAEnteteFacture {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type IFAEnteteFactureHelper
     */
    public IFAEnteteFactureHelper() {
        super("globaz.musca.db.facturation.FAEnteteFacture");
    }

    /**
     * Constructeur du type IFAEnteteFactureHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public IFAEnteteFactureHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type IFAEnteteFactureHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public IFAEnteteFactureHelper(String implementationClassName) {
        super(implementationClassName);
    }

    @Override
    public java.lang.String getDateReceptionDS() {
        return (java.lang.String) _getValueObject().getProperty("dateReceptionDS");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 10:50:06)
     * 
     * @return java.lang.String
     */
    @Override
    public String getDescriptionDecompte() {
        return (String) _getValueObject().getProperty("descriptionDecompte");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 11:24:17)
     * 
     * @return java.lang.String
     */
    @Override
    public String getDescriptionTiers() {
        return (String) _getValueObject().getProperty("descriptionTiers");
    }

    @Override
    public java.lang.String getEspion() {
        return (java.lang.String) _getValueObject().getProperty("espion");
    }

    @Override
    public java.lang.String getIdAdresse() {
        return (java.lang.String) _getValueObject().getProperty("idAdresse");
    }

    @Override
    public java.lang.String getIdAdressePaiement() {
        return (java.lang.String) _getValueObject().getProperty("idAdressePaiement");
    }

    /**
     * Getter
     */
    @Override
    public java.lang.String getIdEntete() {
        return (java.lang.String) _getValueObject().getProperty("idEntete");
    }

    @Override
    public java.lang.String getIdExterneFacture() {
        return (java.lang.String) _getValueObject().getProperty("idExterneFacture");
    }

    @Override
    public java.lang.String getIdExterneRole() {
        return (java.lang.String) _getValueObject().getProperty("idExterneRole");
    }

    @Override
    public java.lang.String getIdModeRecouvrement() {
        return (java.lang.String) _getValueObject().getProperty("idModeRecouvrement");
    }

    @Override
    public java.lang.String getIdPassage() {
        return (java.lang.String) _getValueObject().getProperty("idPassage");
    }

    @Override
    public java.lang.String getIdRemarque() {
        return (java.lang.String) _getValueObject().getProperty("idRemarque");
    }

    @Override
    public java.lang.String getIdRole() {
        return (java.lang.String) _getValueObject().getProperty("idRole");
    }

    @Override
    public java.lang.String getIdSoumisInteretsMoratoires() {
        return (java.lang.String) _getValueObject().getProperty("idSoumisInteretsMoratoires");
    }

    @Override
    public java.lang.String getIdSousType() {
        return (java.lang.String) _getValueObject().getProperty("idSousType");
    }

    @Override
    public java.lang.String getIdTiers() {
        return (java.lang.String) _getValueObject().getProperty("idTiers");
    }

    @Override
    public java.lang.String getIdTypeFacture() {
        return (java.lang.String) _getValueObject().getProperty("idTypeFacture");
    }

    @Override
    public java.lang.String getMotifInteretsMoratoires() {
        return (java.lang.String) _getValueObject().getProperty("motifInteretsMoratoires");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (01.03.2003 10:44:37)
     * 
     * @return java.lang.String
     */
    @Override
    public String getNomTiers() {
        return (String) _getValueObject().getProperty("nomTiers");
    }

    @Override
    public java.lang.String getNumCommune() {
        return (java.lang.String) _getValueObject().getProperty("numCommune");
    }

    @Override
    public java.lang.String getRemarque() {
        return (java.lang.String) _getValueObject().getProperty("remarque");
    }

    @Override
    public java.lang.String getRemarque(BITransaction transaction) {
        return (java.lang.String) _getValueObject().getProperty("remarque");
    }

    @Override
    public java.lang.String getTotalFacture() {
        return (java.lang.String) _getValueObject().getProperty("totalFacture");
    }

    @Override
    public void setDateReceptionDS(java.lang.String newDateReceptionDS) {
        _getValueObject().setProperty("dateReceptionDS", newDateReceptionDS);
    }

    @Override
    public void setEspion(java.lang.String newEspion) {
        _getValueObject().setProperty("espion", newEspion);
    }

    @Override
    public void setIdAdresse(java.lang.String newIdAdresse) {
        _getValueObject().setProperty("idAdresse", newIdAdresse);
    }

    @Override
    public void setIdAdressePaiement(java.lang.String newIdAdressePaiement) {
        _getValueObject().setProperty("idAdressePaiement", newIdAdressePaiement);
    }

    /**
     * Setter
     */
    @Override
    public void setIdEntete(java.lang.String newIdEntete) {
        _getValueObject().setProperty("idEntete", newIdEntete);
    }

    @Override
    public void setIdExterneFacture(java.lang.String newIdExterneFacture) {
        _getValueObject().setProperty("idExterneFacture", newIdExterneFacture);
    }

    @Override
    public void setIdExterneRole(java.lang.String newIdExterneRole) {
        _getValueObject().setProperty("idExterneRole", newIdExterneRole);
    }

    @Override
    public void setIdModeRecouvrement(java.lang.String newIdModeRecouvrement) {
        _getValueObject().setProperty("idModeRecouvrement", newIdModeRecouvrement);
    }

    @Override
    public void setIdPassage(java.lang.String newIdPassage) {
        _getValueObject().setProperty("idPassage", newIdPassage);
    }

    @Override
    public void setIdRemarque(java.lang.String newIdRemarque) {
        _getValueObject().setProperty("idRemarque", newIdRemarque);
    }

    @Override
    public void setIdRole(java.lang.String newIdRole) {
        _getValueObject().setProperty("idRole", newIdRole);
    }

    @Override
    public void setIdSoumisInteretsMoratoires(java.lang.String newIdSoumisInteretsMoratoires) {
        _getValueObject().setProperty("idSoumisInteretsMoratoires", newIdSoumisInteretsMoratoires);
    }

    @Override
    public void setIdSousType(java.lang.String newIdSousType) {
        _getValueObject().setProperty("idSousType", newIdSousType);
    }

    @Override
    public void setIdTiers(java.lang.String newIdTiers) {
        _getValueObject().setProperty("idTiers", newIdTiers);
    }

    @Override
    public void setIdTypeFacture(java.lang.String newIdTypeFacture) {
        _getValueObject().setProperty("idTypeFacture", newIdTypeFacture);
    }

    @Override
    public void setMotifInteretsMoratoires(java.lang.String newMotifInteretsMoratoires) {
        _getValueObject().setProperty("motifInteretsMoratoires", newMotifInteretsMoratoires);
    }

    @Override
    public void setNonImprimable(java.lang.Boolean newNonImprimable) {
        _getValueObject().setProperty("nonImprimable", newNonImprimable);
    }

    @Override
    public void setNumCommune(java.lang.String newNumCommune) {
        _getValueObject().setProperty("numCommune", newNumCommune);
    }

    @Override
    public void setRemarque(java.lang.String newRemarque) {
        _getValueObject().setProperty("remarque", newRemarque);
    }

    @Override
    public void setTotalFacture(java.lang.String newTotalFacture) {
        _getValueObject().setProperty("totalFacture", newTotalFacture);
    }
}
