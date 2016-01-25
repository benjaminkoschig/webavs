package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import ch.globaz.al.business.services.models.dossier.DossierBusinessService;

/**
 * Modèle utilisé pour lister les dossiers actifs d'un allocataire
 * {@link DossierBusinessService#getIdDossiersActifs(String, String)}
 * 
 * @author jts
 * 
 */
public class DossierActifComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Recherche selon un état de dossier
     */
    private String forEtatDossier = null;
    /**
     * Recherche sur le NSS de l'allocataire
     */
    private String forNssAllocataire = null;
    /**
     * Recherche par le numéro d'affilié
     */
    private String forNumAffilie = null;

    public String getForEtatDossier() {
        return forEtatDossier;
    }

    public String getForNssAllocataire() {
        return forNssAllocataire;
    }

    public String getForNumAffilie() {
        return forNumAffilie;
    }

    public void setForEtatDossier(String forEtatsDossier) {
        forEtatDossier = forEtatsDossier;
    }

    public void setForNssAllocataire(String forNssAllocataire) {
        this.forNssAllocataire = forNssAllocataire;
    }

    public void setForNumAffilie(String forNumAffilie) {
        this.forNumAffilie = forNumAffilie;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<DossierActifComplexModel> whichModelClass() {
        return DossierActifComplexModel.class;
    }
}
