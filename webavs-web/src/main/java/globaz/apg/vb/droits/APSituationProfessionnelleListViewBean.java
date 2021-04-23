/*
 * Créé le 23 mai 05
 */
package globaz.apg.vb.droits;

import globaz.apg.db.droits.APSituationProfessionnelleManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.prestation.api.PRTypeDemande;
import lombok.Setter;

/**
 * <H1>Description</H1>
 * 
 * @author dvh
 */
public class APSituationProfessionnelleListViewBean extends APSituationProfessionnelleManager implements
        FWListViewBeanInterface {

    @Setter
    private PRTypeDemande typeDemande;
    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new APSituationProfessionnelleViewBean();
    }

    public boolean displayJourIndemnise() {
        return this.typeDemande.isProcheAidant();
    }
}
