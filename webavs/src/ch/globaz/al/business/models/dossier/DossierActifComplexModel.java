package ch.globaz.al.business.models.dossier;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.al.business.services.models.dossier.DossierBusinessService;

/**
 * Modèle utilisé pour lister les dossiers actifs d'un allocataire
 * {@link DossierBusinessService#getIdDossiersActifs(String, String)}
 * 
 * @author jts
 * 
 */
public class DossierActifComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * identifiant du dossier
     */
    private String idDossier = null;

    @Override
    public String getId() {
        return idDossier;
    }

    public String getIdDossier() {
        return idDossier;
    }

    @Override
    public String getSpy() {
        return null;
    }

    @Override
    public void setId(String id) {
        // DO NOTHING
    }

    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    @Override
    public void setSpy(String spy) {
        // DO NOTHING
    }
}