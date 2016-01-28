/**
 * 
 */
package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.services.models.dossier.DossierBusinessService;

/**
 * *Modèle utilisé pour lister les dossiers à prendre en compte pour les attestations versées
 * 
 * {@link DossierBusinessService#getIdDossiersActifs(String, String)}
 * 
 * @author pta
 * 
 */
public class DossierAttestationVersementComplexModel extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * identifiant du dossier
     */
    private String idDossier = null;
    /**
     * num de l'affilié
     */
    private String numAffilie = null;
    /**
     * identifiant du tiers allocataire
     */
    private String tiersAlloc = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return idDossier;
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return the numAffilie
     */
    public String getNumAffilie() {
        return numAffilie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @return the tiersAlloc
     */
    public String getTiersAlloc() {
        return tiersAlloc;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idDossier = id;

    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param numAffilie
     *            the numAffilie to set
     */
    public void setNumAffilie(String numAffilie) {
        this.numAffilie = numAffilie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        // TODO Auto-generated method stub

    }

    /**
     * @param tiersAlloc
     *            the tiersAlloc to set
     */
    public void setTiersAlloc(String tiersAlloc) {
        this.tiersAlloc = tiersAlloc;
    }

}
