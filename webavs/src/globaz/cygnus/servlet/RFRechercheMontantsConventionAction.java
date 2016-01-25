/*
 * créé le 24 mars 2010
 */
package globaz.cygnus.servlet;

import globaz.cygnus.utils.RFUtils;
import globaz.cygnus.vb.conventions.RFAssureConvention;
import globaz.cygnus.vb.conventions.RFConventionViewBean;
import globaz.cygnus.vb.conventions.RFFournisseurType;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.http.JSPUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * author fha
 */
public class RFRechercheMontantsConventionAction extends RFDefaultAction {

    public RFRechercheMontantsConventionAction(FWServlet servlet) {
        super(servlet);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String _getDestAjouterEchec(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // TODO Auto-generated method stub
        ((RFConventionViewBean) viewBean).setIsUpdate(false);
        return super._getDestAjouterEchec(session, request, response, viewBean);
    }

    // @Override
    // protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
    // HttpServletResponse response, FWViewBeanInterface viewBean) {
    // String _destination = FWScenarios.getInstance().getDestination(
    // (String) session.getAttribute(FWScenarios.SCENARIO_ATTRIBUT),
    // new FWRequestActionAdapter().adapt(request), viewBean);
    // if (JadeStringUtil.isBlank(_destination)) {
    // _destination = this.getActionFullURL() + ".afficher" + "&_valid=new";
    // }
    //
    // return _destination;
    // }
    //
    // @Override
    // protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request,
    // HttpServletResponse response, FWViewBeanInterface viewBean) {
    // // TODO Auto-generated method stub
    // ((RFConventionViewBean) viewBean).setIsUpdate(true);
    // return super._getDestModifierEchec(session, request, response, viewBean);
    // }
    //
    // @Override
    // protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
    // HttpServletResponse response, FWViewBeanInterface viewBean) {
    // return this.getActionFullURL() + ".afficher" + "&_valid=new&_method=upd";
    // }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        FWViewBeanInterface outputViewBean = this.loadViewBean(session);

        /*
         * appelle du Dispatcher, puis met le bean en session
         */
        this.saveViewBean(outputViewBean, request);
        mainDispatcher.dispatch(outputViewBean, getAction());
        session.removeAttribute("viewBean");
        session.setAttribute("viewBean", outputViewBean);
        request.setAttribute(FWServlet.VIEWBEAN, outputViewBean);

        /*
         * si provenance=precedant ça veut dire qu'on à cliquer sur précédent dans l'écran saisieSoinFournisseur et donc
         * il faut vider le viewBean des éléments qui ne sont PAS en bdd
         */
        String provenance = request.getParameter("provenance");

        RFConventionViewBean conventionVB = (RFConventionViewBean) outputViewBean;

        if (provenance != null) {
            // on parcourir les tableaux et on supprime ceux dont l'indicateur
            // BDD est faux
            RFFournisseurType fournisseurTypeElem;
            for (int i = 0; i < conventionVB.getFournisseurTypeArray().size(); i++) {
                fournisseurTypeElem = (RFFournisseurType) conventionVB.getFournisseurTypeArray().get(i);
                if (!fournisseurTypeElem.getIsChargeDepuisDB()) {
                    conventionVB.getFournisseurTypeArray().remove(i);
                }
            }
            RFAssureConvention assureElem;
            for (int i = 0; i < conventionVB.getAssureArray().size(); i++) {
                assureElem = (RFAssureConvention) conventionVB.getAssureArray().get(i);
                if (!assureElem.getIsChargeDepuisDB()) {
                    conventionVB.getAssureArray().remove(i);
                }
            }
            // on vide les tableaux contenant les ids à supprimer
            conventionVB.getIdSuppressionFournisseurArray().clear();
            conventionVB.getIdSuppressionAssureArray().clear();

            supprimerInformationsAssureFournisseur(conventionVB);
        }
        /*
         * redirection vers la destination
         */
        String _destination = "";

        if (outputViewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
            _destination = getRelativeURL(request, session) + "_rc.jsp";// ERROR_PAGE;
        } else {
            _destination = getRelativeURL(request, session) + "_rc.jsp";
        }

        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);

    }

    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        try {

            viewBean = this.loadViewBean(session);

            // sauvegarder isAjout
            Boolean isAjout = ((RFConventionViewBean) viewBean).getIsAjout();

            JSPUtils.setBeanProperties(request, viewBean);

            // charger isAjout
            ((RFConventionViewBean) viewBean).setIsAjout(isAjout);

            return viewBean;

        } catch (Exception e) {
            RFUtils.setMsgExceptionErreurViewBean(viewBean, e.getMessage());
            return viewBean;
        }
    }

    /*
     * vide les champs de la page : information sur les fournisseurs et les assurés éventuels.
     */
    private void supprimerInformationsAssureFournisseur(RFConventionViewBean vb) {
        // on vide les informations du fournisseur (nom et adresse)
        vb.setIdFournisseur("");
        vb.setIdAdressePaiement("");
        vb.setDescFournisseur("");
        vb.setDescAdressePaiement("");

        // on vide les codes type et sous type de soin
        vb.setCodeSousTypeDeSoinList("");
        vb.setCodeTypeDeSoinList("");
        vb.setCodeTypeDeSoin("");
        vb.setCodeSousTypeDeSoin("");

        // on vide les infos de l'assuré (nss, nom, dates, montant)
        vb.setIdAssure("");
        vb.setDescAssure("");
        vb.setNssTiers("");
        vb.setForDateDebut("");
        vb.setForDateFin("");
        vb.setForMontantAssure("");
    }

}
