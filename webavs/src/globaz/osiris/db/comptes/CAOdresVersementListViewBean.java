package globaz.osiris.db.comptes;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.db.ordres.CAOrdresCompteAnnexeManager;
import globaz.osiris.external.IntRole;

public class CAOdresVersementListViewBean extends CAOrdresCompteAnnexeManager implements FWListViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

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

    public Boolean isCompteAnnexeRoleRentier() {
        if (JadeStringUtil.isIntegerEmpty(getForIdCompteAnnexe())) {
            return new Boolean(false);
        }

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(getSession());

        compteAnnexe.setIdCompteAnnexe(getForIdCompteAnnexe());

        try {
            compteAnnexe.retrieve();

            if (compteAnnexe.isNew() || compteAnnexe.hasErrors()) {
                return new Boolean(false);
            }

            return IntRole.ROLE_RENTIER.equals(compteAnnexe.getRole().getIdRole());
        } catch (Exception e) {
            return new Boolean(false);
        }
    }

    public Boolean isIJRoleAPG() {
        if (JadeStringUtil.isIntegerEmpty(getForIdCompteAnnexe())) {
            return new Boolean(false);
        }

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(getSession());

        compteAnnexe.setIdCompteAnnexe(getForIdCompteAnnexe());

        try {
            compteAnnexe.retrieve();

            if (compteAnnexe.isNew() || compteAnnexe.hasErrors()) {
                return new Boolean(false);
            }

            return IntRole.ROLE_APG.equals(compteAnnexe.getRole().getIdRole());
        } catch (Exception e) {
            return new Boolean(false);
        }
    }

    public Boolean isIJRoleRentier() {
        if (JadeStringUtil.isIntegerEmpty(getForIdCompteAnnexe())) {
            return new Boolean(false);
        }

        CACompteAnnexe compteAnnexe = new CACompteAnnexe();
        compteAnnexe.setSession(getSession());

        compteAnnexe.setIdCompteAnnexe(getForIdCompteAnnexe());

        try {
            compteAnnexe.retrieve();

            if (compteAnnexe.isNew() || compteAnnexe.hasErrors()) {
                return new Boolean(false);
            }

            return IntRole.ROLE_IJAI.equals(compteAnnexe.getRole().getIdRole());
        } catch (Exception e) {
            return new Boolean(false);
        }
    }
}
