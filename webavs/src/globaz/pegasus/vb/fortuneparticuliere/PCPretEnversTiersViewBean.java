/**
 * 
 */
package globaz.pegasus.vb.fortuneparticuliere;

import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Echeance.EcheanceType;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.fortuneparticuliere.FortuneParticuliere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.FortuneParticuliereSearch;
import ch.globaz.pegasus.business.models.fortuneparticuliere.SimplePretEnversTiers;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

/**
 * @author ECO
 */
public class PCPretEnversTiersViewBean extends PCAbstractRequerantDonneeFinanciereViewBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public String getTypeEcheance() {
        return EcheanceType.PRET_ENVERS_TIERS.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        super.retrieve();

        // cherche les données financières
        FortuneParticuliereSearch search = new FortuneParticuliereSearch();
        search.setForIdDroit(getId());
        search.setForNumeroVersion(getNoVersion());
        search.setWhereKey("forVersionnedPretEnversTiers");
        search = PegasusServiceLocator.getDroitService().searchFortuneParticuliere(search);

        for (JadeAbstractModel abstractDonnee : search.getSearchResults()) {
            FortuneParticuliere donnee = (FortuneParticuliere) abstractDonnee;
            if (donnee.getDonneeFinanciere() instanceof SimplePretEnversTiers) {
                DroitMembreFamille f = donnee.getMembreFamilleEtendu().getDroitMembreFamille();

                List donneesMembre = (List) donnees.get(f.getId());
                if (donneesMembre == null) {
                    donneesMembre = new ArrayList();
                    donnees.put(f.getId(), donneesMembre);
                }
                donneesMembre.add(donnee);
            }
        }
    }

}
