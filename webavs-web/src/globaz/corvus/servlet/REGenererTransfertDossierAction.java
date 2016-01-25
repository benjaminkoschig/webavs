package globaz.corvus.servlet;

import globaz.babel.api.ICTDocument;
import globaz.corvus.api.codesystem.IRECatalogueTexte;
import globaz.corvus.api.topaz.IRENoDocumentInfoRom;
import globaz.corvus.vb.process.REGenererTransfertDossierViewBean;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.framework.servlets.FWServlet;
import globaz.globall.db.BSession;
import globaz.globall.http.JSPUtils;
import globaz.prestation.acor.PRACORConst;
import globaz.prestation.interfaces.babel.PRBabelHelper;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;
import globaz.prestation.interfaces.util.nss.PRUtil;
import globaz.prestation.utils.ged.PRGedUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author SCR
 */
public class REGenererTransfertDossierAction extends REDefaultProcessAction {

    private ICTDocument document;
    private ICTDocument documentHelper;

    public REGenererTransfertDossierAction(FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected void actionAfficher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {

        String destination = getRelativeURL(request, session) + "_de.jsp";

        try {
            FWAction action = FWAction.newInstance(request.getParameter("userAction"));
            FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(action, mainDispatcher.getPrefix());
            viewBean.setISession(mainDispatcher.getSession());

            String idTiers = request.getParameter("idTiers");
            String idDemandeRente = request.getParameter("idDemandeRente");
            String dateInfoComp = request.getParameter("dateInfoComp");
            String idInfoCompl = request.getParameter("idInfoCompl");

            // Chargement du catalogue de texte
            BSession bSession = (BSession) mainDispatcher.getSession();
            PRTiersWrapper tierswrap = PRTiersHelper.getTiersAdresseParId(bSession, idTiers);

            String codeIsoLangue = bSession.getCode(tierswrap.getProperty(PRTiersWrapper.PROPERTY_LANGUE));
            codeIsoLangue = PRUtil.getISOLangueTiers(codeIsoLangue);
            // creation du helper pour les catalogues de texte
            documentHelper = PRBabelHelper.getDocumentHelper(bSession);
            documentHelper.setCsDomaine(IRECatalogueTexte.CS_RENTES);
            documentHelper.setCsTypeDocument(IRECatalogueTexte.CS_TRANSFERT);
            documentHelper.setNom("openOffice");
            documentHelper.setDefault(Boolean.FALSE);
            documentHelper.setActif(Boolean.TRUE);
            documentHelper.setCodeIsoLangue(codeIsoLangue);

            ICTDocument[] documents = documentHelper.load();

            if ((documents == null) || (documents.length == 0)) {
                throw new Exception(bSession.getLabel("ERREUR_CHARGEMENT_CAT_TEXTE"));
            } else {
                document = documents[0];
            }

            REGenererTransfertDossierViewBean vb = (REGenererTransfertDossierViewBean) viewBean;

            vb.setIdDemandeRente(idDemandeRente);
            vb.setDateEnvoi(dateInfoComp);
            vb.setIdInfoCompl(idInfoCompl);
            vb.setIdTiers(idTiers);
            vb.setNss(tierswrap.getProperty(PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL));
            vb.setNomPrenomTiers(tierswrap.getProperty(PRTiersWrapper.PROPERTY_NOM) + " "
                    + tierswrap.getProperty(PRTiersWrapper.PROPERTY_PRENOM));

            if (null != codeIsoLangue) {
                if ("FR".equals(codeIsoLangue)) {
                    vb.setLangueAssure("Français");
                } else if ("DE".equals(codeIsoLangue)) {
                    vb.setLangueAssure("Allemand");
                } else if ("IT".equals(codeIsoLangue)) {
                    vb.setLangueAssure("Italien");
                }
            }

            if (PRACORConst.CS_HOMME.equals(tierswrap.getProperty(PRTiersWrapper.PROPERTY_SEXE))) {
                vb.setTexteRemarque(document.getTextes(2).getTexte(7).getDescription());
            } else {
                vb.setTexteRemarque(document.getTextes(2).getTexte(6).getDescription());
            }

            if (PRGedUtils.isDocumentInGed(IRENoDocumentInfoRom.TRANSFERT_A_CAISSE_COMPETENTE, bSession)
                    || PRGedUtils.isDocumentInGed(IRENoDocumentInfoRom.TRANSFERT_DOSSIER_EN_COURS_A_UNE_AUTRE_CAISSE,
                            bSession)
                    || PRGedUtils.isDocumentInGed(
                            IRENoDocumentInfoRom.TRANSFERT_A_CAISSE_COMPETENTE_POUR_CALCUL_PREVISIONNEL, bSession)) {
                vb.setDisplaySendToGed("1");
            } else {
                vb.setDisplaySendToGed("0");
            }

            JSPUtils.setBeanProperties(request, vb);
            session.setAttribute(FWServlet.VIEWBEAN, vb);

        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            destination = FWDefaultServletAction.ERROR_PAGE;
        }

        servlet.getServletContext().getRequestDispatcher(destination).forward(request, response);
    }
}
