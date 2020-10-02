package ch.globaz.pegasus.web.servlet;

import ch.globaz.pegasus.business.constantes.EPCForfaitType;
import ch.globaz.pegasus.business.models.parametre.SimpleLienZoneLocalite;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.servlets.FWServlet;
import globaz.pegasus.vb.parametre.PCConversionRenteViewBean;
import globaz.pegasus.vb.parametre.PCZoneForfaitsViewBean;
import globaz.pegasus.vb.parametre.PCZoneLocaliteViewBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PCParametreServletAction extends PCAbstractServletAction {

    public PCParametreServletAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestAjouterSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        Boolean useAnneAge = Boolean.valueOf(request.getParameter("valueAnneAge"));
        if ((viewBean instanceof PCConversionRenteViewBean) && useAnneAge) {

            String annee = ((PCConversionRenteViewBean) viewBean).getAnnee();
            String age = ((PCConversionRenteViewBean) viewBean).getConversionRente().getSimpleConversionRente()
                    .getAge();
            return getActionFullURL() + ".chercher&anneeValeur=" + annee + "&ageValeur=" + age + "&useAnneAge="
                    + useAnneAge.toString();

        } else if (viewBean instanceof PCZoneLocaliteViewBean) {
            SimpleLienZoneLocalite lienZone = ((PCZoneLocaliteViewBean) viewBean).getZoneLocalite()
                    .getSimpleLienZoneLocalite();
            Boolean useValiderContinuer = Boolean.valueOf(request.getParameter("useValiderContinuer"));

            return getActionFullURL() + ".chercher&zoneLocalite.simpleLienZoneLocalite.anneeDebut="
                    + lienZone.getDateDebut() + "&zoneLocalite.simpleLienZoneLocalite.idZoneForfait="
                    + lienZone.getIdZoneForfait() + "&useValiderContinuer=" + useValiderContinuer;
        } else {
            return super._getDestAjouterSucces(session, request, response, viewBean);
        }
    }
}
