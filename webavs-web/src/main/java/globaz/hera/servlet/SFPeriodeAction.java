/*
 * Cr�� le 16 sept. 05
 * 
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.hera.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.vb.famille.SFPeriodeViewBean;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author jpa
 * 
 *         Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class SFPeriodeAction extends SFDefaultAction {

    /**
     * @param servlet
     */
    public SFPeriodeAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (FWViewBeanInterface.WARNING.equals(viewBean.getMsgType())) {
            session.setAttribute("viewBean", viewBean);
        }
        return getActionFullURL() + ".chercher";

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#actionChercher(javax .servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.controller.FWDispatcher)
     */
    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String idMembreFamille = request.getParameter("idMembreFamille");
        if (ISFSituationFamiliale.ID_MEMBRE_FAMILLE_CONJOINT_INCONNU.equals(idMembreFamille)) {
            // renvoyer vers l'�cran _rc avec message d'erreur

            FWAction action = FWAction.newInstance("hera.famille.apercuRelationFamilialeRequerant.chercher");
            BSession bSession = (BSession) mainDispatcher.getSession();
            String _destination = "/hera?userAction=hera.famille.apercuRelationFamilialeRequerant.chercher&message="
                    + bSession.getLabel("JSP_MEMBRE_FAMILLE_CONJOINT_INCONNU_NO_PERIODE");

            servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
        } else {
            super.actionChercher(session, request, response, mainDispatcher);
        }
    }


    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        SFPeriodeViewBean vb = (SFPeriodeViewBean) viewBean;
        validate(vb);
        return vb;
    }


    @Override
    protected FWViewBeanInterface beforeModifier(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        SFPeriodeViewBean vb = (SFPeriodeViewBean) viewBean;
        validate(vb);
        return vb;
    }

    /**
     * Validation/Mise � blanc de certains champs du viewbean. En fait on ne valide rien ici.. 
     * On met uniquement des champs � blanc en fonction du type de p�riode.
     * La validation est r�alis�e dans {@link SFPeriode}._validate();
     * 
     * @param vb
     */
    private void validate(SFPeriodeViewBean vb) {
        String typePeriode = vb.getType();
        // Si le type de p�riode N'EST PAS garde_BTE ou enfant_recueillit les 2 champs suivants doivent �tre vides
        if (!ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE.equals(typePeriode)
                && !ISFSituationFamiliale.CS_TYPE_PERIODE_ENFANT.equals(typePeriode)) {
            vb.setIdDetenteurBTE("");
            vb.setCsTypeDeDetenteur("");
        }

        if (!ISFSituationFamiliale.CS_TYPE_PERIODE_ASSURANCE_ETRANGERE.equals(typePeriode)) {
            vb.setPays("");
        }
    }
}
