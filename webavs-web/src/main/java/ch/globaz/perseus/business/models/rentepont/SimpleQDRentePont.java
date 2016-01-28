/**
 * 
 */
package ch.globaz.perseus.business.models.rentepont;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author JSI
 * 
 */
public class SimpleQDRentePont extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String annee = null;
    private String excedantRevenu = null;
    private String excedantRevenuCompense = null;
    private String idDossier = null;
    private String idQDRentePont = null;
    private String montantLimite = null;
    private String montantUtilise = null;

    public String getAnnee() {
        return annee;
    }

    public String getExcedantRevenu() {
        return excedantRevenu;
    }

    public String getExcedantRevenuCompense() {
        return excedantRevenuCompense;
    }

    @Override
    public String getId() {
        return idQDRentePont;
    }

    public String getIdDossier() {
        return idDossier;
    }

    public String getIdQDRentePont() {
        return idQDRentePont;
    }

    public String getMontantLimite() {
        return montantLimite;
    }

    public String getMontantUtilise() {
        return montantUtilise;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public void setExcedantRevenu(String excedantRevenu) {
        this.excedantRevenu = excedantRevenu;
    }

    public void setExcedantRevenuCompense(String excedantRevenuCompense) {
        this.excedantRevenuCompense = excedantRevenuCompense;
    }

    @Override
    public void setId(String id) {
        idQDRentePont = id;
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdQDRentePont(String idQDRentePont) {
        this.idQDRentePont = idQDRentePont;
    }

    public void setMontantLimite(String montantLimite) {
        this.montantLimite = montantLimite;
    }

    public void setMontantUtilise(String montantUtilise) {
        this.montantUtilise = montantUtilise;
    }

}
