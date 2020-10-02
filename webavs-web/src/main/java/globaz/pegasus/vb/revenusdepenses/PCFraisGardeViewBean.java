package globaz.pegasus.vb.revenusdepenses;

import ch.globaz.common.domaine.Echeance.EcheanceType;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.revenusdepenses.*;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PCFraisGardeViewBean extends PCAbstractRequerantDonneeFinanciereViewBean {


    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String getTypeEcheance() {
        return EcheanceType.FRAIS_GARDE.toString();
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
        RevenusDepensesSearch search = new RevenusDepensesSearch();
        search.setForNumeroVersion(getNoVersion());
        search.setForIdDroit(getId());
        search.setWhereKey("forVersionedFraisGarde");
        search = PegasusServiceLocator.getDroitService().searchRevenusDepenses(search);

        for (Iterator<JadeAbstractModel> it = Arrays.asList(search.getSearchResults()).iterator(); it.hasNext();) {
            RevenusDepenses donnee = (RevenusDepenses) it.next();
            if (donnee.getDonneeFinanciere() instanceof SimpleFraisGarde) {
                DroitMembreFamille f = donnee.getMembreFamilleEtendu().getDroitMembreFamille();

                List donneesMembre = (List) donnees.get(f.getId());
                if (donneesMembre == null) {
                    donneesMembre = new ArrayList<>();
                    donnees.put(f.getId(), donneesMembre);
                }
                donneesMembre.add(donnee);
            }
        }
    }
    public String getFraisGardeAjaxService(){
        return PCFraisGardeAjaxViewBean.class.getName();
    }

}
