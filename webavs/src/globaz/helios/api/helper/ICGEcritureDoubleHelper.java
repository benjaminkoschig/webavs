package globaz.helios.api.helper;

import globaz.globall.api.BITransaction;
import globaz.globall.shared.GlobazHelper;
import globaz.globall.shared.GlobazValueObject;
import globaz.helios.api.ICGEcritureDouble;

/**
 * Classe helper d'une interface d'API
 * 
 * @author EFLCreateAPITool
 */
public class ICGEcritureDoubleHelper extends GlobazHelper implements ICGEcritureDouble {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur du type ICGEnteteEcritureViewBeanHelper
     */
    public ICGEcritureDoubleHelper() {
        super("globaz.helios.db.comptes.CGEcritureDoubleViewBean");
    }

    /**
     * Constructeur du type ICGEnteteEcritureViewBeanHelper
     * 
     * @param valueObject
     *            le Value Object contenant les données
     */
    public ICGEcritureDoubleHelper(GlobazValueObject valueObject) {
        super(valueObject);
    }

    /**
     * Constructeur du type ICGEnteteEcritureViewBeanHelper
     * 
     * @param implementationClassName
     *            le nom de la classe d'implémentation
     */
    public ICGEcritureDoubleHelper(String implementationClassName) {
        super(implementationClassName);
    }

    @Override
    public void add(BITransaction transaction) throws java.lang.Exception {
        this._invoke("addEcriture", new Object[] { transaction });
    }

    @Override
    public void delete(BITransaction transaction) throws java.lang.Exception {
        this._invoke("deleteEcriture", new Object[] { transaction });
    }

    /**
     * @see globaz.helios.api.ICGEcritureDouble#getCours()
     */
    @Override
    public String getCours() {
        return (String) _getValueObject().getProperty("cours");
    }

    @Override
    public String getDate() {
        return (String) _getValueObject().getProperty("date");
    }

    /**
     * Insérez la description de la méthode ici.
     * 
     * @return String
     */
    @Override
    public String getIdEcriture() {
        return (String) _getValueObject().getProperty("idEnteteEcriture");
    }

    @Override
    public String getIdExerciceComptable() {
        return (String) _getValueObject().getProperty("idExerciceComptable");
    }

    @Override
    public String getIdJournal() {
        return (String) _getValueObject().getProperty("idJournal");
    }

    @Override
    public String getIdMandat() {
        return (String) _getValueObject().getProperty("idMandat");
    }

    @Override
    public String getIdSecteurAvs() {
        return (String) _getValueObject().getProperty("idSecteurAvs");
    }

    @Override
    public String getLibelle() {
        return (String) _getValueObject().getProperty("libelle");
    }

    @Override
    public String getMontant() {
        return (String) _getValueObject().getProperty("montant");
    }

    /**
     * @see globaz.helios.api.ICGEcritureDouble#getMontantMonnaieEtrangere()
     */
    @Override
    public String getMontantMonnaieEtrangere() {
        return (String) _getValueObject().getProperty("montantMonnaie");
    }

    @Override
    public String getNumeroCentreChargeCredite() {
        return (java.lang.String) _getValueObject().getProperty("numeroCentreChargeCredite");
    }

    @Override
    public String getNumeroCentreChargeDebite() {
        return (java.lang.String) _getValueObject().getProperty("numeroCentreChargeDebite");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.02.2003 16:09:57)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getNumeroCompteCredite() {
        return (java.lang.String) _getValueObject().getProperty("idExterneCompteCredite");
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.02.2003 16:09:57)
     * 
     * @return java.lang.String
     */
    @Override
    public java.lang.String getNumeroCompteDebite() {
        return (java.lang.String) _getValueObject().getProperty("idExterneCompteDebite");
    }

    @Override
    public String getPiece() {
        return (String) _getValueObject().getProperty("piece");
    }

    @Override
    public String getRemarque() {
        return (String) _getValueObject().getProperty("remarque");
    }

    @Override
    public String getSoldeCompteCredite() {
        return (String) _getValueObject().getProperty("soldeCompteCredite");
    }

    @Override
    public String getSoldeCompteDebite() {
        return (String) _getValueObject().getProperty("soldeCompteDebite");
    }

    @Override
    public String getTotalAvoir() {
        return (String) _getValueObject().getProperty("totalAvoir");
    }

    @Override
    public String getTotalDoit() {
        return (String) _getValueObject().getProperty("totalDoit");
    }

    /**
     * @see globaz.helios.api.ICGEcritureDouble#setCours(String)
     */
    @Override
    public void setCours(String value) {
        _getValueObject().setProperty("cours", value);
    }

    @Override
    public void setDate(String newDate) {
        _getValueObject().setProperty("date", newDate);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (22.10.2002 13:52:58)
     * 
     * @param newC
     *            String
     */
    @Override
    public void setIdEcriture(String newIdEnteteEcriture) {
        _getValueObject().setProperty("idEnteteEcriture", newIdEnteteEcriture);
    }

    @Override
    public void setIdExerciceComptable(String value) {
        _getValueObject().setProperty("idExerciceComptable", value);
    }

    @Override
    public void setIdJournal(String newIdJournal) {
        _getValueObject().setProperty("idJournal", newIdJournal);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (20.11.2002 11:47:45)
     * 
     * @param newIdLivre
     *            java.lang.String
     */
    public void setIdLivre(java.lang.String newIdLivre) {
        _getValueObject().setProperty("idLivre", newIdLivre);
    }

    public void setIdMandat(String value) {
        _getValueObject().setProperty("idMandat", value);
    }

    @Override
    public void setIdSecteurAvs(String newIdSecteurAvs) {
        _getValueObject().setProperty("idSecteurAvs", newIdSecteurAvs);
    }

    @Override
    public void setLibelle(String newLibelle) {
        _getValueObject().setProperty("libelle", newLibelle);
    }

    @Override
    public void setMontant(String value) {
        _getValueObject().setProperty("montant", value);
    }

    /**
     * @see globaz.helios.api.ICGEcritureDouble#setMontantMonnaieEtrangere(String)
     */
    @Override
    public void setMontantMonnaieEtrangere(String value) {
        _getValueObject().setProperty("montantMonnaie", value);
    }

    @Override
    public void setNumeroCentreChargeCredite(String idCentreChargeCredite) {
        _getValueObject().setProperty("numeroCentreChargeCredite", idCentreChargeCredite);
    }

    @Override
    public void setNumeroCentreChargeDebite(String idCentreChargeDebite) {
        _getValueObject().setProperty("numeroCentreChargeDebite", idCentreChargeDebite);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.02.2003 16:10:31)
     * 
     * @param newIdExterneCompteCredite
     *            java.lang.String
     */
    @Override
    public void setNumeroCompteCredite(java.lang.String newIdExterneCompteCredite) {
        _getValueObject().setProperty("idExterneCompteCredite", newIdExterneCompteCredite);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.02.2003 16:09:57)
     * 
     * @param newIdExterneCompteDebite
     *            java.lang.String
     */
    @Override
    public void setNumeroCompteDebite(java.lang.String newIdExterneCompteDebite) {
        _getValueObject().setProperty("idExterneCompteDebite", newIdExterneCompteDebite);
    }

    @Override
    public void setPiece(String newPiece) {
        _getValueObject().setProperty("piece", newPiece);
    }

    @Override
    public void setRemarque(String value) {
        _getValueObject().setProperty("remarque", value);
    }

    @Override
    public void update(BITransaction transaction) throws java.lang.Exception {
        this._invoke("updateEcriture", new Object[] { transaction });
    }

}
