package globaz.vulpecula.vb.congepaye;

import globaz.globall.db.BSpy;
import java.util.List;
import ch.globaz.common.vb.BJadeSearchObjectELViewBean;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.congepaye.Compteur;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;
import ch.globaz.vulpecula.domain.models.postetravail.PosteTravail;
import ch.globaz.vulpecula.domain.models.postetravail.Travailleur;

public class PTCompteursViewBean extends BJadeSearchObjectELViewBean {
    private String idPosteTravail;

    private List<Compteur> compteurs;
    private PosteTravail posteTravail;

    @Override
    public String getId() {
        return idPosteTravail;
    }

    @Override
    public void retrieve() throws Exception {
        posteTravail = VulpeculaRepositoryLocator.getPosteTravailRepository().findById(idPosteTravail);
        compteurs = VulpeculaRepositoryLocator.getCompteurRepository().findByIdPosteTravail(idPosteTravail);
    }

    @Override
    public void setId(String newId) {
        idPosteTravail = newId;
    }

    @Override
    public BSpy getSpy() {
        return null;
    }

    /**
     * Retourne la liste des compteurs.
     * 
     * @return Liste de compteurs
     */
    public List<Compteur> getCompteurs() {
        return compteurs;
    }

    /**
     * Retourne l'employeur lié au poste de travail.
     * 
     * @return Employeur du poste de travail
     */
    public Employeur getEmployeur() {
        return posteTravail.getEmployeur();
    }

    /**
     * Retourne le travailleur lié au poste de travail.
     * 
     * @return Travailleur du poste de travail
     */
    public Travailleur getTravailleur() {
        return posteTravail.getTravailleur();
    }

    /**
     * Retourne l'id du poste de travail
     * 
     * @return String id posteTravail
     */
    public String getIdPosteTravail() {
        return posteTravail.getId();
    }
}
