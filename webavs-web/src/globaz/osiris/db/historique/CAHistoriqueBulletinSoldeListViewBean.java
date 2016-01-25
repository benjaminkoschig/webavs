package globaz.osiris.db.historique;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.comptes.CACompteAnnexeViewBean;
import globaz.osiris.db.comptes.CASectionViewBean;

public class CAHistoriqueBulletinSoldeListViewBean extends CAHistoriqueBulletinSoldeManager implements
        FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private CACompteAnnexeViewBean compteAnnexe;
    private CASectionViewBean section;

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CAHistoriqueBulletinSoldeViewBean();
    }

    public CACompteAnnexeViewBean getCompteAnnexe() {
        if (!JadeStringUtil.isIntegerEmpty(getForIdSection()) && (getSection() != null)) {
            compteAnnexe = new CACompteAnnexeViewBean();
            compteAnnexe.setSession(getSession());

            compteAnnexe.setIdCompteAnnexe(section.getIdCompteAnnexe());

            try {
                compteAnnexe.retrieve();

                if (compteAnnexe.hasErrors() || compteAnnexe.isNew()) {
                    compteAnnexe = null;
                }
            } catch (Exception e) {
                compteAnnexe = null;
            }
        }

        return compteAnnexe;
    }

    public CASectionViewBean getSection() {
        if (!JadeStringUtil.isIntegerEmpty(getForIdSection())) {
            section = new CASectionViewBean();
            section.setSession(getSession());

            section.setIdSection(getForIdSection());

            try {
                section.retrieve();

                if (section.hasErrors() || section.isNew()) {
                    section = null;
                }
            } catch (Exception e) {
                section = null;
            }
        }

        return section;
    }
}
