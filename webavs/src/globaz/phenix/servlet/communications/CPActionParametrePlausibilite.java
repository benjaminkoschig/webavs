/*
 * Créé le 16 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.phenix.servlet.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.servlets.FWServlet;
import globaz.phenix.db.communications.CPParametrePlausibiliteListViewBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author mmu
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class CPActionParametrePlausibilite extends FWDefaultServletAction {

    public CPActionParametrePlausibilite(FWServlet servlet) {
        super(servlet);

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    /*
     * protected FWViewBeanInterface beforeAfficher( HttpSession session, HttpServletRequest request,
     * HttpServletResponse response, FWViewBeanInterface viewBean) { CPParametrePlausibiliteViewBean pViewBean =
     * (CPParametrePlausibiliteViewBean) viewBean; pViewBean.setId(pViewBean.getIdParametre());
     * pViewBean.setId(pViewBean.getIdParametre()); return pViewBean; }
     */

    /*
     * Remet les parametres boolean à null s'ils ne sont pas dans la requete
     */
    @Override
    protected FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        CPParametrePlausibiliteListViewBean listViewBean = (CPParametrePlausibiliteListViewBean) viewBean;
        if (request.getParameter("actif") == null) {
            listViewBean.setForActif(null);
        }
        return viewBean;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#beforeAfficher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    /*
     * protected FWViewBeanInterface beforeAfficher( HttpSession session, HttpServletRequest request,
     * HttpServletResponse response, FWViewBeanInterface viewBean) { CPParametrePlausibiliteViewBean paramPlausiVB =
     * (CPParametrePlausibiliteViewBean) viewBean; String idPlausibilite = (String)
     * session.getAttribute("idPlausibilite"); if (!JadeStringUtil.isIntegerEmpty(idPlausibilite)) {
     * paramPlausiVB.setIdPlausibilite(idPlausibilite); session.removeAttribute("idPlausibilite"); } return
     * paramPlausiVB; }
     */

}
