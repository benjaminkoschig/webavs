/**
 * 
 */
package globaz.vulpecula.vb.postetravaildetail;

import ch.globaz.common.vb.DomainPersistentObjectViewBean;
import ch.globaz.vulpecula.business.services.VulpeculaRepositoryLocator;
import ch.globaz.vulpecula.domain.models.postetravail.Employeur;

/**
 * @author Arnaud Geiser (AGE) | Créé le 17 déc. 2013
 * 
 */
public class PTEmployeurdetailViewBean extends DomainPersistentObjectViewBean<Employeur> {
    private Employeur employeur = null;

    public PTEmployeurdetailViewBean() {
        employeur = new Employeur();
    }

    @Override
    public void add() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update() throws Exception {
        // Il n'est pas possible de créer un nouvelle employeur depuis cet écran. Cependant, il se petu que la table
        // PT_EMLPOYEURS ne soient pas renseignés, de ce fait, un create peut être nécessaire.
        if (employeur.getSpy().isEmpty()) {
            VulpeculaRepositoryLocator.getEmployeurRepository().create(employeur);
        } else {
            VulpeculaRepositoryLocator.getEmployeurRepository().update(employeur);
        }
    }

    @Override
    public void delete() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void retrieve() throws Exception {
        employeur = VulpeculaRepositoryLocator.getEmployeurRepository().findByIdAffilie(employeur.getId());
    }

    public Employeur getEmployeur() {
        return employeur;
    }

    public void setBvr(final boolean bvr) {
        employeur.setBvr(bvr);
    }

    @Override
    public Employeur getEntity() {
        return employeur;
    }
}
