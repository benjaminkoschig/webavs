/*
 * Créé le 14 janvier 2010
 */
package globaz.cygnus.servlet;

import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.demandes.RFSaisieDemandeAbstractViewBean;
import globaz.cygnus.vb.demandes.RFSaisieDemandeViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 
 * @author jje
 */
public class RFSaisieDemandeAction extends RFDefaultAction {

    public RFSaisieDemandeAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected FWViewBeanInterface beforeNouveau(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {

        RFSaisieDemandeViewBean outputVb = new RFSaisieDemandeViewBean();

        boolean isRetro = Boolean.valueOf(request.getParameter("isretro"));

        try {
            globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);

            // Si l'on vient de la saisie des demandes par le boutton
            // "Valider + Saisie même assuré", on mémorise l'idTiers de l'assuré ainsi que l'état isRetro
            if (RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_MEME_ASSURE.equals(((RFSaisieDemandeViewBean) viewBean)
                    .getTypeValidation())) {

                setOutputViewBeanMemeAssure((RFSaisieDemandeAbstractViewBean) outputVb,
                        (RFSaisieDemandeAbstractViewBean) viewBean);
                outputVb.setIsRetro(isRetro);

                return outputVb;
            } else if (RFSaisieDemandeAbstractViewBean.TYPE_VALIDATION_NOUVELLE_SAISIE
                    .equals(((RFSaisieDemandeViewBean) viewBean).getTypeValidation())) {
                return outputVb;
            } else {
                return viewBean;
            }
        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return viewBean;
        }

    }

    /**
     * Méthode qui récupère les champs de l'assuré de la dernière demande encodée
     * 
     * @param FWViewBeanInterface
     *            , FWViewBeanInterface
     */
    private void setOutputViewBeanMemeAssure(RFSaisieDemandeAbstractViewBean outputViewBean,
            RFSaisieDemandeAbstractViewBean viewBean) {

        outputViewBean.setSession((BSession) viewBean.getISession());

        outputViewBean.setIdTiers(viewBean.getIdTiers());
        outputViewBean.setCsNationalite(viewBean.getCsNationalite());
        outputViewBean.setCsCanton(viewBean.getCsCanton());
        outputViewBean.setCsSexe(viewBean.getCsSexe());
        outputViewBean.setNss(viewBean.getNss());
        outputViewBean.setNom(viewBean.getNom());
        outputViewBean.setPrenom(viewBean.getPrenom());
        outputViewBean.setDateNaissance(viewBean.getDateNaissance());
        outputViewBean.setDateDeces(viewBean.getDateDeces());
        outputViewBean.setTypeValidation(viewBean.getTypeValidation());
        outputViewBean.setIdGestionnaire(viewBean.getIdGestionnaire());
        outputViewBean.setIsRetro(viewBean.getIsRetro());

    }

}