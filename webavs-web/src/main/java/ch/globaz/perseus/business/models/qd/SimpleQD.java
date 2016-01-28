package ch.globaz.perseus.business.models.qd;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Bean correspondant à une QD en base de données
 * 
 * @author JSI
 * 
 */
public class SimpleQD extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csType = null;
    private String idMembreFamille = null;
    private String idQD = null;
    private String idQDAnnuelle = null;
    private String idQDParente = null;
    private String montantLimite = null;
    private String montantUtilise = null;
    private Boolean ouvertureManuelle = null;

    /**
     * @return the csType
     */
    public String getCsType() {
        return csType;
    }

    @Override
    public String getId() {
        return idQD;
    }

    /**
     * @return the idMembreFamille
     */
    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    /**
     * @return the idQD
     */
    public String getIdQD() {
        return idQD;
    }

    /**
     * @return the idQDAnnuelle
     */
    public String getIdQDAnnuelle() {
        return idQDAnnuelle;
    }

    /**
     * @return the idQDParente
     */
    public String getIdQDParente() {
        return idQDParente;
    }

    /**
     * @return the montantLimite
     */
    public String getMontantLimite() {
        return montantLimite;
    }

    /**
     * @return the montantUtilise
     */
    public String getMontantUtilise() {
        return montantUtilise;
    }

    /**
     * @return the ouvertureManuelle
     */
    public Boolean getOuvertureManuelle() {
        return ouvertureManuelle;
    }

    public CSTypeQD getTypeQD() {
        return CSTypeQD.getType(csType);
    }

    /**
     * @param csType
     *            the csType to set
     */
    public void setCsType(String csType) {
        this.csType = csType;
    }

    @Override
    public void setId(String id) {
        idQD = id;
    }

    /**
     * @param idMembreFamille
     *            the idMembreFamille to set
     */
    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

    /**
     * @param idQD
     *            the idQD to set
     */
    public void setIdQD(String idQD) {
        this.idQD = idQD;
    }

    /**
     * @param idQDAnnuelle
     *            the idQDAnnuelle to set
     */
    public void setIdQDAnnuelle(String idQDAnnuelle) {
        this.idQDAnnuelle = idQDAnnuelle;
    }

    /**
     * @param idQDParente
     *            the idQDParente to set
     */
    public void setIdQDParente(String idQDParente) {
        this.idQDParente = idQDParente;
    }

    /**
     * @param montantLimite
     *            the montantLimite to set
     */
    public void setMontantLimite(String montantLimite) {
        this.montantLimite = montantLimite;
    }

    /**
     * @param montantUtilise
     *            the montantUtilise to set
     */
    public void setMontantUtilise(String montantUtilise) {
        this.montantUtilise = montantUtilise;
    }

    /**
     * @param ouvertureManuelle
     *            the ouvertureManuelle to set
     */
    public void setOuvertureManuelle(Boolean ouvertureManuelle) {
        this.ouvertureManuelle = ouvertureManuelle;
    }

}
