package ch.globaz.pegasus.business.models.monnaieetrangere;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author SCE
 * 
 *         Modèle simple pour la gestion des monnaies étrangères
 */
public class SimpleMonnaieEtrangere extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypeMonnaie = null; // Code Système
    private String dateDebut = null;
    private String dateFin = null;
    private String idMonnaieEtrangere = null;
    private String taux = null;

    /**
     * @return the nomMonnaie
     */
    public String getCsTypeMonnaie() {
        return csTypeMonnaie;
    }

    /**
     * @return the dateDebut
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateFin
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return the idMonnaieEtrangere
     */
    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return idMonnaieEtrangere;
    }

    /**
     * 
     * @return the idMonnaieEtrangere
     */
    public String getIdMonnaieEtrangere() {
        return idMonnaieEtrangere;
    }

    /**
     * @return the taux
     */
    public String getTaux() {
        return taux;
    }

    /**
     * @param nomMonnaie
     *            the nomMonnaie to set
     */
    public void setCsTypeMonnaie(String csTypeMonnaie) {
        this.csTypeMonnaie = csTypeMonnaie;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @param id
     *            the id to set
     */
    @Override
    public void setId(String id) {
        // TODO Auto-generated method stub
        idMonnaieEtrangere = id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setIdMonnaieEtrangere(String idMonnaieEtrangere) {
        // TODO Auto-generated method stub
        this.idMonnaieEtrangere = idMonnaieEtrangere;
    }

    /**
     * @param taux
     *            the taux to set
     */
    public void setTaux(String taux) {
        this.taux = taux;
    }

}
