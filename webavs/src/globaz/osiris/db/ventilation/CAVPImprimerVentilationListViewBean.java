package globaz.osiris.db.ventilation;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.globall.db.BStatement;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexe;
import globaz.osiris.db.comptes.CASectionManagerListViewBean;

/**
 * @author dostes, 3 janv. 05
 */
public class CAVPImprimerVentilationListViewBean extends CASectionManagerListViewBean implements
        FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected String _getOrder(BStatement statement) {
        return getOrderBy();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAVPImprimerVentilationViewBean();
    }

    public String getCompteAnnexeTitulaireEntete() {
        if (JadeStringUtil.isIntegerEmpty(getForIdCompteAnnexe())) {
            return "";
        }

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(getSession());
        compteAnnexe.setIdCompteAnnexe(getForIdCompteAnnexe());

        try {
            compteAnnexe.retrieve();

            if (compteAnnexe.isNew() || compteAnnexe.hasErrors()) {
                return "";
            }

            return compteAnnexe.getTitulaireEntete();
        } catch (Exception e) {
            return "";
        }
    }
}
