package globaz.osiris.db.comptes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.api.BISession;
import globaz.globall.db.GlobazServer;
import globaz.jade.client.util.JadeStringUtil;
import globaz.osiris.api.APICompteAnnexe;
import globaz.osiris.application.CAApplication;
import globaz.pyxis.api.osiris.TITiersAdministrationOSI;

/**
 * @author user
 */
public class CAEcritureViewBean extends CAEcriture implements FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Return le libellé de la caisse professionnelle.
     * 
     * @return Le libellé. Si id vide => return "".
     */
    public String getCaisseProfessionnelleLibelle() {
        if (!JadeStringUtil.isIntegerEmpty(getIdCaisseProfessionnelle())) {
            try {
                BISession pyxisSession = ((CAApplication) GlobazServer.getCurrentSystem().getApplication(
                        CAApplication.DEFAULT_APPLICATION_OSIRIS)).getSessionPyxis(getSession(), true);
                return TITiersAdministrationOSI.getAdministrationLibelle(pyxisSession, getIdCaisseProfessionnelle());
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * @return l'id caisse professionnelle de l'écriture s'il est renseigné. <br>
     *         Sinon celui de la section.
     */
    @Override
    public String getIdCaisseProfessionnelle() {
        String retour = super.getIdCaisseProfessionnelle();
        if ((getCompte() == null) || !getCompte().isUseCaissesProf()) {
            return "";
        }
        if (JadeStringUtil.isBlankOrZero(retour)) {
            if (JadeStringUtil.isBlankOrZero(getIdSection())) {
                try {
                    CACompteAnnexe compteAnnexe = new CACompteAnnexe();
                    compteAnnexe.setSession(getSession());
                    compteAnnexe.setAlternateKey(APICompteAnnexe.AK_IDEXTERNE);
                    compteAnnexe.setIdRole(getIdRoleEcran());
                    compteAnnexe.setIdExterneRole(getIdExterneRoleEcran());
                    compteAnnexe.retrieve();
                    if (compteAnnexe.isNew()) {
                        return "";
                    }

                    CASectionManager manager = new CASectionManager();
                    manager.setSession(getSession());
                    manager.setForIdCompteAnnexe(compteAnnexe.getIdCompteAnnexe());
                    manager.setForIdExterne(getIdExterneSectionEcran());
                    manager.find();

                    CASection section = (CASection) manager.get(0);
                    setIdSection(section.getId());

                } catch (Exception e) {
                    return "";
                }
            }
            retour = getSection().getIdCaisseProfessionnelle();
        }
        return retour;
    }
}
