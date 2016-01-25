package globaz.corvus.servlet;

import globaz.corvus.api.demandes.IREDemandeRente;
import globaz.corvus.db.basescalcul.REBasesCalcul;
import globaz.corvus.db.demandes.REDemandeRente;
import globaz.corvus.db.rentesaccordees.REPrestationsAccordees;
import globaz.corvus.utils.enumere.genre.prestations.REGenresPrestations;
import globaz.corvus.vb.demandes.RENSSDTO;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.hera.api.ISFSituationFamiliale;
import globaz.hera.db.famille.SFApercuRequerant;
import globaz.hera.db.famille.SFMembreFamille;
import globaz.hera.external.ISFUrlEncode;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.servlet.PRDefaultAction;
import globaz.prestation.tools.PRSessionDataContainerHelper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RESituationFamilialeAction extends PRDefaultAction {

    public RESituationFamilialeAction(FWServlet servlet) {
        super(servlet);
        // TODO Auto-generated constructor stub
    }

    public void actionAfficherFamille(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher, FWViewBeanInterface viewBean) throws Exception {

        String _destination = "";
        String action = "";
        String idTiersBeneficiaire = request.getParameter("idTiersBeneficiaire");
        String idTiers = idTiersBeneficiaire;
        String idBaseCalcul = request.getParameter("idBaseCalcul");

        String idRA = request.getParameter("selectedId");
        REPrestationsAccordees pa = new REPrestationsAccordees();
        pa.setSession((BSession) mainDispatcher.getSession());
        pa.setIdPrestationAccordee(idRA);
        pa.retrieve();

        if (REGenresPrestations.GENRE_14.equals(pa.getCodePrestation())
                || REGenresPrestations.GENRE_15.equals(pa.getCodePrestation())
                || REGenresPrestations.GENRE_16.equals(pa.getCodePrestation())
                || REGenresPrestations.GENRE_24.equals(pa.getCodePrestation())
                || REGenresPrestations.GENRE_25.equals(pa.getCodePrestation())
                || REGenresPrestations.GENRE_26.equals(pa.getCodePrestation())
                || REGenresPrestations.GENRE_34.equals(pa.getCodePrestation())
                || REGenresPrestations.GENRE_35.equals(pa.getCodePrestation())
                || REGenresPrestations.GENRE_36.equals(pa.getCodePrestation())
                || REGenresPrestations.GENRE_45.equals(pa.getCodePrestation())
                || REGenresPrestations.GENRE_54.equals(pa.getCodePrestation())
                || REGenresPrestations.GENRE_55.equals(pa.getCodePrestation())
                || REGenresPrestations.GENRE_56.equals(pa.getCodePrestation())
                || REGenresPrestations.GENRE_74.equals(pa.getCodePrestation())
                || REGenresPrestations.GENRE_75.equals(pa.getCodePrestation())
                || REGenresPrestations.GENRE_76.equals(pa.getCodePrestation())) {

            REBasesCalcul bc = new REBasesCalcul();
            bc.setSession((BSession) mainDispatcher.getSession());
            bc.setIdBasesCalcul(idBaseCalcul);
            bc.retrieve();
            if (!bc.isNew()) {
                idTiers = bc.getIdTiersBaseCalcul();
            }
        }

        String idDemandeRente = request.getParameter("noDemandeRente");

        try {

            String csDomaine = ISFSituationFamiliale.CS_DOMAINE_RENTES;
            try {
                if (!JadeStringUtil.isBlankOrZero(idDemandeRente)) {
                    REDemandeRente dem = new REDemandeRente();
                    dem.setSession((BSession) mainDispatcher.getSession());
                    dem.setIdDemandeRente(idDemandeRente);
                    dem.retrieve();
                    if (IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL.equals(dem.getCsTypeCalcul())) {
                        csDomaine = ISFSituationFamiliale.CS_DOMAINE_CALCUL_PREVISIONNEL;
                    }

                }
            } catch (Exception e) {
                csDomaine = ISFSituationFamiliale.CS_DOMAINE_RENTES;
            }

            // Récupération du membre de famille
            SFMembreFamille mf = new SFMembreFamille();
            mf.setSession((BSession) mainDispatcher.getSession());
            mf.setIdTiers(idTiers);
            // String csDomaine = ISFSituationFamiliale.CS_DOMAINE_RENTES;
            mf.setCsDomaineApplication(csDomaine);
            mf.setAlternateKey(SFMembreFamille.ALTERNATE_KEY_IDTIERS);
            mf.retrieve();
            if (mf.isNew()) {
                if (!ISFSituationFamiliale.CS_DOMAINE_RENTES.equals(csDomaine)) {
                    csDomaine = ISFSituationFamiliale.CS_DOMAINE_RENTES;
                    mf.setCsDomaineApplication(csDomaine);
                    mf.retrieve();
                    if (mf.isNew()) {
                        csDomaine = ISFSituationFamiliale.CS_DOMAINE_STANDARD;
                        mf.setCsDomaineApplication(csDomaine);
                        mf.retrieve();
                    }
                } else {
                    csDomaine = ISFSituationFamiliale.CS_DOMAINE_STANDARD;
                    mf.setCsDomaineApplication(csDomaine);
                    mf.retrieve();
                }
            }

            if (mf.isNew()) {
                viewBean.setMessage("Membre de famille not found for idTiers= " + idTiers);
            }

            // On controle si le mf est un requérant ou non !!!!
            SFApercuRequerant req = new SFApercuRequerant();
            req.setSession((BSession) mainDispatcher.getSession());
            req.setIdMembreFamille(mf.getIdMembreFamille());
            req.setIdDomaineApplication(mf.getCsDomaineApplication());
            req.setAlternateKey(SFApercuRequerant.ALT_KEY_IDMEMBRE);
            req.retrieve();

            // String urlRetour =
            // ISFUrlEncode.encodeUrl("/corvus?userAction="+IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE
            // +
            // "." + FWAction.ACTION_CHERCHER);

            String urlRetour = ISFUrlEncode.encodeUrl("/corvus?userAction="
                    + IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE + "." + FWAction.ACTION_CHERCHER
                    + "&noDemandeRente=" + request.getParameter("noDemandeRente") + "&idTierRequerant" + idTiers);

            // Est requération, on affiche la famille en tant que requérant
            if (!req.isNew()) {
                action = "hera.famille.apercuRelationFamilialeRequerant.entrerApplication&csDomaine=" + csDomaine
                        + "&idTiers=" + mf.getIdTiers() + "&urlFrom=" + urlRetour;
            } else {
                action = "hera.famille.vueGlobale.afficherFamilleMembre&csDomaine=" + csDomaine + "&idMembreFamille="
                        + mf.getIdMembreFamille() + "&urlFrom=" + urlRetour;

            }

            _destination = "/hera?userAction=" + action;

            // On sauvegarde le numéro AVS du tiers en session
            RENSSDTO dtoNss = new RENSSDTO();

            PRTiersWrapper tiersWrapper = PRTiersHelper.getTiersParId(mainDispatcher.getSession(), idTiersBeneficiaire);
            if (tiersWrapper != null) {
                dtoNss.setNSS(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dtoNss);
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(_destination, request, response);

    }

    @Override
    protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String _destination = "";
        String action = "";
        String idTiers = request.getParameter("idTierRequerant");

        try {

            String urlRetour = ISFUrlEncode.encodeUrl("/corvus?userAction="
                    + IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE + "." + FWAction.ACTION_CHERCHER);

            String idDemandeRente = request.getParameter("noDemandeRente");
            String csDomaine = ISFSituationFamiliale.CS_DOMAINE_RENTES;
            try {
                if (!JadeStringUtil.isBlankOrZero(idDemandeRente)) {
                    REDemandeRente dem = new REDemandeRente();
                    dem.setSession((BSession) mainDispatcher.getSession());
                    dem.setIdDemandeRente(idDemandeRente);
                    dem.retrieve();
                    if (IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL.equals(dem.getCsTypeCalcul())) {
                        csDomaine = ISFSituationFamiliale.CS_DOMAINE_CALCUL_PREVISIONNEL;
                    }

                }
            } catch (Exception e) {
                csDomaine = ISFSituationFamiliale.CS_DOMAINE_RENTES;
            }

            action = "hera.famille.apercuRelationConjoint.entrerApplication&csDomaine=" + csDomaine + "&idTiers="
                    + idTiers + "&urlFrom=" + urlRetour;
            _destination = "/hera?userAction=" + action;

            // On sauvegarde le numéro AVS du tiers en session
            RENSSDTO dtoNss = new RENSSDTO();

            PRTiersWrapper tiersWrapper = PRTiersHelper.getTiersParId(mainDispatcher.getSession(), idTiers);
            if (tiersWrapper != null) {
                dtoNss.setNSS(tiersWrapper.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
                PRSessionDataContainerHelper.setData(session, PRSessionDataContainerHelper.KEY_NSS_DTO, dtoNss);
            }

        } catch (Exception e) {
            _destination = ERROR_PAGE;
        }

        /*
         * redirection vers la destination
         */
        goSendRedirect(_destination, request, response);

    }

}
