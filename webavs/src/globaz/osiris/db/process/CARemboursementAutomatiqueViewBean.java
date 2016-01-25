package globaz.osiris.db.process;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.parameters.FWParametersSystemCodeManager;
import globaz.osiris.db.comptes.CACompteCourant;
import globaz.osiris.process.CAProcessRemboursementAutomatique;
import globaz.osiris.translation.CACodeSystem;

public class CARemboursementAutomatiqueViewBean extends CAProcessRemboursementAutomatique implements
        FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CARemboursementAutomatiqueViewBean() {
        super();
    }

    public FWParametersSystemCodeManager getCsNatureOrdres() {
        return CACodeSystem.getNatureVersementsManager(getSession());
    }

    public String getIdExterneCompteCourant() {
        try {
            CACompteCourant compteCourant = new CACompteCourant();
            compteCourant.setSession(getSession());

            compteCourant.setIdCompteCourant(getForIdCompteCourant());
            compteCourant.retrieve();

            if (compteCourant.isNew() || compteCourant.hasErrors()) {
                return "";
            } else {
                return compteCourant.getIdExterne();
            }
        } catch (Exception e) {
            // Do nothing.
            return "";
        }
    }

}
