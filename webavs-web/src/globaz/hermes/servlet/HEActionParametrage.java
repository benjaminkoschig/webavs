package globaz.hermes.servlet;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWDefaultServletAction;
import globaz.framework.controller.FWDispatcher;
import globaz.framework.controller.FWViewBeanActionFactory;
import globaz.globall.db.BSession;
import globaz.globall.util.JADate;
import globaz.hermes.api.IHEAnnoncesViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceListViewBean;
import globaz.hermes.db.gestion.HEOutputAnnonceViewBean;
import globaz.hermes.db.parametrage.HEAttenteEnvoiCIViewBean;
import globaz.hermes.db.parametrage.HEAttenteEnvoiListViewBean;
import globaz.hermes.db.parametrage.HEAttenteEnvoiViewBean;
import globaz.hermes.db.parametrage.HEAttenteReceptionViewBean;
import globaz.hermes.db.parametrage.HEAttenteRetourCIViewBean;
import globaz.hermes.db.parametrage.HEAttenteRetourListViewBean;
import globaz.hermes.db.parametrage.HEAttenteRetourOptimizedListViewBean;
import globaz.hermes.db.parametrage.HEAttenteRetourOptimizedViewBean;
import globaz.hermes.db.parametrage.HEAttenteRetourViewBean;
import globaz.hermes.db.parametrage.HECodeapplicationViewBean;
import globaz.hermes.db.parametrage.HECriteremotif;
import globaz.hermes.db.parametrage.HECriteremotifListViewBean;
import globaz.hermes.db.parametrage.HECriteremotifViewBean;
import globaz.hermes.db.parametrage.HECriteresViewBean;
import globaz.hermes.db.parametrage.HELienannonceListViewBean;
import globaz.hermes.db.parametrage.HEMethodeListViewBean;
import globaz.hermes.db.parametrage.HEMethodeViewBean;
import globaz.hermes.db.parametrage.HEMotifcodeapplicationListViewBean;
import globaz.hermes.db.parametrage.HEMotifcodeapplicationViewBean;
import globaz.hermes.db.parametrage.HEMotifsListViewBean;
import globaz.hermes.db.parametrage.HEMotifsViewBean;
import globaz.hermes.db.parametrage.HEParametrageannonceListViewBean;
import globaz.hermes.db.parametrage.HEParametrageannonceViewBean;
import globaz.hermes.process.HEAnnulerAnnonceProcess;
import globaz.hermes.process.HELibererCIProcess;
import globaz.hermes.process.HESupprimerCIProcess;
import globaz.hermes.utils.HttpUtils;
import globaz.hermes.utils.StringUtils;
import globaz.jade.client.util.JadeStringUtil;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Insérez la description du type ici. Date de création : (25.10.2002 08:49:00)
 * 
 * @author: Administrator
 */
public class HEActionParametrage extends FWDefaultServletAction {
    /**
     * Commentaire relatif au constructeur HEActionParametrage.
     * 
     * @param servlet
     *            globaz.framework.servlets.FWServlet
     */
    public HEActionParametrage(globaz.framework.servlets.FWServlet servlet) {
        super(servlet);
    }

    @Override
    protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        if (request.getParameter("modeSaisie") == null ? false : request.getParameter("modeSaisie").equals("true")) {
            String action = request.getParameter("userAction");
            FWAction _action = FWAction.newInstance(action);
            HEAttenteEnvoiViewBean vb = (HEAttenteEnvoiViewBean) viewBean;
            return "/" + _action.getApplicationPart()
                    + "?userAction=hermes.gestion.inputAnnonce.afficher&_method=add&_valid=error&update=ok&numeroAVS="
                    + vb.getNouveauNumAVS() + "&idDernierAjout=" + vb.getIdAnnonce();
        } else {
            return super._getDestModifierSucces(session, request, response, viewBean);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces (javax.servlet.http.HttpSession,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * globaz.framework.bean.FWViewBeanInterface)
     */
    @Override
    protected String _getDestSupprimerSucces(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String action = request.getParameter("userAction");
        FWAction _action = FWAction.newInstance(action);
        if (request.getParameter("modeSaisie") == null ? false : request.getParameter("modeSaisie").equals("true")) {
            HEAttenteEnvoiViewBean vb = (HEAttenteEnvoiViewBean) viewBean;
            return "/" + _action.getApplicationPart()
                    + "?userAction=hermes.gestion.inputAnnonce.afficher&_method=add&_valid=error&delete=ok&numeroAVS="
                    + vb.getNouveauNumAVS();
        } else {
            return "/" + _action.getApplicationPart() + "?userAction=hermes.parametrage.attenteEnvoi.chercher";
        }
    }

    @Override
    protected void actionChercher(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
        FWAction _monAction = FWAction.newInstance(request.getParameter("userAction"));
        String _myDestination = getRelativeURL(request, session) + "_rc.jsp";
        String actionSuite = getActionSuite(request);
        String referenceUnique = (!JadeStringUtil.isEmpty(request.getParameter("referenceUnique"))) ? request
                .getParameter("referenceUnique") : null;
        String refUnique = (!JadeStringUtil.isEmpty(request.getParameter("refUnique"))) ? request
                .getParameter("refUnique") : null;
        if (actionSuite.equals("criteremotif")) {
            try {
                FWViewBeanInterface viewBean = FWViewBeanActionFactory.newInstance(_monAction,
                        mainDispatcher.getPrefix());
                globaz.globall.http.JSPUtils.setBeanProperties(request, viewBean);
                // Comme y'a ni actionChercher par défaut et pas de before
                // chercher, on set et on retrieve ici
                HECriteremotif vBean = (HECriteremotif) viewBean;
                vBean.setIdcriteremotif(request.getParameter("selectedId"));
                vBean.setSession((BSession) mainDispatcher.getSession());
                vBean.retrieve();
                request.setAttribute("viewBean", vBean);
                // _myDestination = getRelativeURL() + "_rc.jsp";
                _myDestination = getRelativeURL(request, session) + "_rc.jsp";
            } catch (Exception e) {
                e.printStackTrace();
                _myDestination = ERROR_PAGE;
            }
        } else if (actionSuite.equals("lienannonce")) {
            try {
                // Comme y'a ni actionChercher par défaut et pas de before
                // chercher, on set et on retrieve ici
                HELienannonceListViewBean vBean = new HELienannonceListViewBean();
                vBean.setSession((BSession) mainDispatcher.getSession());
                vBean.setForIdParametrageAnnonce(request.getParameter("selectedId"));
                vBean.find();
                request.setAttribute("viewBean", vBean);
                // _myDestination = getRelativeURL() + "_rc.jsp";
                _myDestination = getRelativeURL(request, session) + "_rc.jsp";
            } catch (Exception e) {
                e.printStackTrace();
                _myDestination = ERROR_PAGE;
            }
        } else if (actionSuite.equals("attenteEnvoi")) {
            try {
                if (referenceUnique != null) {
                    HEAttenteEnvoiListViewBean vBean = new HEAttenteEnvoiListViewBean();
                    vBean.setSession((BSession) mainDispatcher.getSession());
                    vBean.setForReferenceUnique(request.getParameter("referenceUnique"));
                    vBean.setIsArchivage(StringUtils.isStringEmpty(request.getParameter("isArchivage")) ? "false"
                            : request.getParameter("isArchivage"));
                    vBean.find();
                    session.setAttribute("attenteEnvoi-rcBean", vBean.getFirstEntity());
                } else {
                    session.removeAttribute("attenteEnvoi-rcBean");
                }
                if (request.getParameter("idLot") != null) {
                    _myDestination += "?idLot=" + request.getParameter("idLot");
                }
            } catch (Exception e) {
                e.printStackTrace();
                _myDestination = ERROR_PAGE;
            }
        } else if (actionSuite.equals("attenteReception")) {
            try {
                if (referenceUnique != null) {
                    /*
                     * HEAttenteReceptionListViewBean vBean = new HEAttenteReceptionListViewBean();
                     * vBean.setSession((BSession) mainDispatcher.getSession()); vBean
                     * .setForReferenceUnique(request.getParameter("referenceUnique" ));
                     * vBean.setForIdAnnonce(request.getParameter("idAnnonce")); vBean.find();
                     */
                    HEOutputAnnonceViewBean vBean = new HEOutputAnnonceViewBean();
                    vBean.setSession((BSession) mainDispatcher.getSession());
                    vBean.setArchivage(!StringUtils.isStringEmpty(request.getParameter("isArchivage")) ? Boolean
                            .valueOf(request.getParameter("isArchivage")).booleanValue() : false);
                    vBean.setIdAnnonce(request.getParameter("idAnnonce"));
                    // vBean.wantCallMethodAfter(false);
                    vBean.retrieve();
                    session.setAttribute("attenteReception-rcBean", vBean);
                } else {
                    session.removeAttribute("attenteReception-rcBean");
                }
            } catch (Exception e) {
                e.printStackTrace();
                _myDestination = ERROR_PAGE;
            }
        } else if (actionSuite.equals("attenteRetourOptimized")) {
            try {
                if (referenceUnique != null) {
                    HEAttenteRetourOptimizedListViewBean vBean = new HEAttenteRetourOptimizedListViewBean();
                    vBean.setSession((BSession) mainDispatcher.getSession());
                    vBean.setForReferenceUnique(request.getParameter("referenceUnique"));
                    vBean.setForIdAnnonce(request.getParameter("referenceUnique"));
                    vBean.setIsArchivage(!StringUtils.isStringEmpty(request.getParameter("isArchivage")) ? Boolean
                            .valueOf(request.getParameter("isArchivage")).booleanValue() : false);
                    vBean.find();
                    if (vBean.getFirstEntity() != null) {
                        session.setAttribute("attenteRetourOptimized-rcBean", vBean.getFirstEntity());
                    } else {
                        session.removeAttribute("attenteRetourOptimized-rcBean");
                    }
                } else {
                    session.removeAttribute("attenteRetourOptimized-rcBean");
                }
            } catch (Exception e) {
                e.printStackTrace();
                _myDestination = ERROR_PAGE;
            }
        } else if (actionSuite.equals("attenteEnvoiCI") || actionSuite.equals("attenteRetourCI")) {
            try {
                if (refUnique != null) {
                    HEOutputAnnonceViewBean vBean = new HEOutputAnnonceViewBean();
                    vBean.setSession((BSession) mainDispatcher.getSession());
                    vBean.setIdAnnonce(request.getParameter("selectedId"));
                    vBean.setArchivage(!StringUtils.isStringEmpty(request.getParameter("isArchivage")) ? Boolean
                            .valueOf(request.getParameter("isArchivage")).booleanValue() : false);
                    vBean.retrieve();
                    session.setAttribute("attenteEnvoiCI-rcBean", vBean);
                } else {
                    session.removeAttribute("attenteEnvoiCI-rcBean");
                }
                if (request.getParameter("idLot") != null) {
                    _myDestination += "?idLot=" + request.getParameter("idLot");
                    if (request.getParameter("isArchivage") != null) {
                        _myDestination += "&isArchivage=" + request.getParameter("isArchivage");
                    }
                } else {
                    if (!StringUtils.isStringEmpty(request.getParameter("isArchivage"))) {
                        _myDestination += "?isArchivage=" + request.getParameter("isArchivage");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                _myDestination = ERROR_PAGE;
            }
        }
        servlet.getServletContext().getRequestDispatcher(_myDestination).forward(request, response);
    }

    /**
     * @see globaz.framework.controller.FWDefaultServletAction#actionExecuter(HttpSession, HttpServletRequest,
     *      HttpServletResponse, FWDispatcher)
     */
    @Override
    protected void actionExecuter(HttpSession session, HttpServletRequest request, HttpServletResponse response,
            FWDispatcher mainDispatcher) throws ServletException, IOException {
        String _destination = "";
        FWAction _action = FWAction.newInstance(request.getParameter("userAction"));
        if (_action.getClassPart().equals("libererCI")) {
            try {
                HEOutputAnnonceViewBean rcBean = (HEOutputAnnonceViewBean) session
                        .getAttribute("attenteEnvoiCI-rcBean");
                // ouvrir une nouvelle transaction pour effectuer le travail de
                // libération du CI
                HELibererCIProcess libererCi = new HELibererCIProcess();
                libererCi.setSession(rcBean.getSession());
                libererCi.setRefUnique(rcBean.getRefUnique());
                libererCi.setMotif(rcBean.getMotif());
                libererCi.setStatut(rcBean.getStatut());
                libererCi.setNumeroAVS(rcBean.getNumeroAVS());
                libererCi.setNumeroCaisse(rcBean.getNumeroCaisse());
                libererCi = (HELibererCIProcess) mainDispatcher.dispatch(libererCi,
                        FWAction.newInstance(request.getParameter("userAction")));
                if (libererCi.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                    rcBean.setMessage(libererCi.getMessage());
                    rcBean.setMsgType(FWViewBeanInterface.ERROR);
                    session.setAttribute("viewBean", rcBean);
                    _destination = ERROR_PAGE;
                } else {
                    _destination = getActionBack();// getRelativeURLwithoutClassPart(request,
                    // session) +
                    // "attenteReception_rc.jsp";//"hermes?userAction=showProcess";
                }
            } catch (Exception e) {
                e.printStackTrace();
                _destination = ERROR_PAGE;
            }
        } else if (_action.getClassPart().equals("supprimerCI")) {
            try {
                HEOutputAnnonceViewBean rcBean = (globaz.hermes.db.gestion.HEOutputAnnonceViewBean) session
                        .getAttribute("attenteEnvoiCI-rcBean");
                HESupprimerCIProcess supprimerCi = new HESupprimerCIProcess();

                if (rcBean != null) {
                    supprimerCi.setSession(rcBean.getSession());
                    supprimerCi.setReferenceUnique(rcBean.getRefUnique());
                    supprimerCi.setReferenceUnique38(rcBean.getRefUnique38());
                    supprimerCi.setMotif(rcBean.getMotif());
                    supprimerCi.setNumeroAvs(rcBean.getNumeroAVS());
                    supprimerCi.setNumeroCaisse(rcBean.getNumeroCaisse());
                    supprimerCi.setNbreInscriptionCI(rcBean.getField(IHEAnnoncesViewBean.NOMBRE_INSCRIPTIONS_CI));
                    supprimerCi.setStatut(rcBean.getStatut());
                    supprimerCi = (HESupprimerCIProcess) mainDispatcher.dispatch(supprimerCi,
                            FWAction.newInstance(request.getParameter("userAction")));
                }
                if (rcBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                    rcBean.setMessage(supprimerCi.getMessage());
                    rcBean.setMsgType(FWViewBeanInterface.ERROR);
                    session.setAttribute("viewBean", rcBean);
                    _destination = ERROR_PAGE;
                } else {
                    _destination = getRelativeURLwithoutClassPart(request, session) + "attenteEnvoi_rc.jsp";// _getDestAjouterSucces(session,
                    // request,
                    // response,
                    // rcBean);//"hermes?userAction=showProcess";
                }
            } catch (Exception e) {
                e.printStackTrace();
                _destination = ERROR_PAGE;
            }
        } else if (_action.getClassPart().equals("annulerAnnonce")) {
            try {
                HEAttenteEnvoiViewBean rcBean = (HEAttenteEnvoiViewBean) session.getAttribute("viewBean");
                HEAnnulerAnnonceProcess annulerArc = new HEAnnulerAnnonceProcess();
                annulerArc.setSession(rcBean.getSession());
                annulerArc.setReferenceUnique(rcBean.getRefUnique());
                annulerArc.setMotif(rcBean.getMotifArc());
                annulerArc = (HEAnnulerAnnonceProcess) mainDispatcher.dispatch(annulerArc,
                        FWAction.newInstance(request.getParameter("userAction")));
                if (annulerArc.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
                    rcBean.setMessage(annulerArc.getMessage());
                    rcBean.setMsgType(FWViewBeanInterface.ERROR);
                    session.setAttribute("viewBean", rcBean);
                    _destination = ERROR_PAGE;
                } else {
                    _destination = getRelativeURLwithoutClassPart(request, session) + "attenteEnvoi_rc.jsp";// "hermes?userAction=showProcess";
                }
            } catch (Exception e) {
                e.printStackTrace();
                _destination = ERROR_PAGE;
            }
        }
        servlet.getServletContext().getRequestDispatcher(_destination).forward(request, response);
    }

    // ALD : surcharge cette méthode car dans Hermes, il y a 2 paramètres pour
    // la référence interne
    @Override
    protected String addParametersFrom(HttpServletRequest request, String url) {
        String result = url;
        Enumeration enum1 = request.getParameterNames();
        char separator = result.indexOf('?') == -1 ? '?' : '&';
        while (enum1.hasMoreElements()) {
            String paramName = (String) enum1.nextElement();
            boolean add = !"userAction".equals(paramName);
            if (add) {
                String[] p = request.getParameterValues(paramName);
                for (int i = 0; i < p.length; i++) {
                    result += separator + paramName + "=" + URLEncoder.encode(p[i]);
                    separator = '&';
                }
            }
        }
        return result;
    }

    // Attention : le dispatcher ne doit pas être appelé dans cette méthode. La
    // gestion des droits se fait sur l'action "afficher" par la suite
    @Override
    protected FWViewBeanInterface beforeAfficher(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String actionSuite = getActionSuite(request);
        if (actionSuite.equals("codeapplication")) {
            HECodeapplicationViewBean vBean = (HECodeapplicationViewBean) viewBean;
            vBean.setIdCode(request.getParameter("selectedId"));
            return vBean;
        } else if (actionSuite.equals("criteremotif")) {
            HECriteremotifViewBean vBean = (HECriteremotifViewBean) viewBean;
            vBean.setIdcriteremotif(request.getParameter("selectedId"));
            return vBean;
        } else if (actionSuite.equals("parametrageannonce")) {
            HEParametrageannonceViewBean vBean = (HEParametrageannonceViewBean) viewBean;
            vBean.setIdParametrageAnnonce(request.getParameter("selectedId"));
            return vBean;
        } else if (actionSuite.equals("attenteRetour")) {
            HEAttenteRetourViewBean vBean = (HEAttenteRetourViewBean) viewBean;
            vBean.setIdAttenteRetour(request.getParameter("selectedId"));
            return vBean;
        } else if (actionSuite.equals("attenteRetourOptimized")) {
            HEAttenteRetourOptimizedViewBean vBean = (HEAttenteRetourOptimizedViewBean) viewBean;
            vBean.setIdAttenteRetour(request.getParameter("selectedId"));
            vBean.setIsArchivage(!JadeStringUtil.isEmpty(request.getParameter("isArchivage")) ? Boolean.valueOf(
                    request.getParameter("isArchivage")).booleanValue() : false);
            return vBean;
        } else if (actionSuite.equals("attenteEnvoi")) {
            HEAttenteEnvoiViewBean vBean = (HEAttenteEnvoiViewBean) viewBean;
            // setRefUnique pour lister l'ensemble des infos
            vBean.setRefUnique(request.getParameter("referenceUnique"));
            vBean.setCritere(request.getParameter("critere"));
            vBean.setMotif(request.getParameter("motif"));
            vBean.setIsArchivage(!JadeStringUtil.isEmpty(request.getParameter("isArchivage")) ? Boolean.valueOf(
                    request.getParameter("isArchivage")).booleanValue() : false);
            return vBean;
        } else if (actionSuite.equals("attenteReception")) {
            HEAttenteReceptionViewBean vBean = (HEAttenteReceptionViewBean) viewBean;
            // setRefUnique pour lister l'ensemble des infos
            vBean.setRefUnique(request.getParameter("referenceUnique"));
            vBean.setIsArchivage(!JadeStringUtil.isEmpty(request.getParameter("isArchivage")) ? Boolean.valueOf(
                    request.getParameter("isArchivage")).booleanValue() : false);
            return vBean;
        } else if (actionSuite.equals("attenteEnvoiCI")) {
            HEAttenteEnvoiCIViewBean vBean = (HEAttenteEnvoiCIViewBean) viewBean;
            vBean.setIdAnnonce(request.getParameter("selectedId"));
            vBean.setArchivage(!JadeStringUtil.isEmpty(request.getParameter("isArchivage")) ? Boolean.valueOf(
                    request.getParameter("isArchivage")).booleanValue() : false);
            return vBean;
        } else if (actionSuite.equals("attenteRetourCI")) {
            HEAttenteRetourCIViewBean vBean = (HEAttenteRetourCIViewBean) viewBean;
            vBean.setIdAnnonce(request.getParameter("selectedId"));
            vBean.setArchivage(!JadeStringUtil.isEmpty(request.getParameter("isArchivage")) ? Boolean.valueOf(
                    request.getParameter("isArchivage")).booleanValue() : false);
            return vBean;
        }
        return viewBean;
    }

    // Attention : le dispatcher ne doit pas être appelé dans cette méthode. La
    // gestion des droits se fait sur l'action "ajouter" par la suite
    @Override
    protected FWViewBeanInterface beforeAjouter(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        // codeapplication
        String actionSuite = getActionSuite(request);
        if (actionSuite.equals("codeapplication")) {
            HECodeapplicationViewBean vBean = (HECodeapplicationViewBean) viewBean;
            // vBean.setCodeUtilisateur(request.getParameter("idappli"));
            // vBean.setLibelleCodeApplication(request.getParameter("libelle"));
            return vBean;
        } else if (actionSuite.equals("motifcodeapplication")) {
            HEMotifcodeapplicationViewBean vBean = (HEMotifcodeapplicationViewBean) viewBean;
            vBean.setIdCritereMotif("1"); // A changer plus tard
            vBean.setIdCodeApplication(request.getParameter("selectedId"));
            vBean.setIdMotif(request.getParameter("idmotif"));
            return vBean;
        } else if (actionSuite.equals("criteres")) {
            HECriteresViewBean vBean = (HECriteresViewBean) viewBean;
            vBean.setLibelle(request.getParameter("libelle"));
            return vBean;
        } else if (actionSuite.equals("parametrageannonce")) {
            HEParametrageannonceViewBean vBean = (HEParametrageannonceViewBean) viewBean;
            vBean.setIdCSCodeApplication(request.getParameter("idCodeApplication"));
            vBean.setCodeEnregistrementDebut(request.getParameter("codeEnregistrementDebut"));
            vBean.setCodeEnregistrementFin(request.getParameter("codeEnregistrementFin"));
            return vBean;
        }
        return viewBean;
    }

    // Attention : le dispatcher ne doit pas être appelé dans cette méthode. La
    // gestion des droits se fait sur l'action "lister" par la suite
    @Override
    public final FWViewBeanInterface beforeLister(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String actionSuite = getActionSuite(request);
        // if (actionSuite.equals("codeapplication")) {
        // HECodeapplicationListViewBean vBean = (HECodeapplicationListViewBean)
        // viewBean;
        // vBean.setFromCodeUti(request.getParameter("fromCodeUti"));
        // return vBean;
        // } else
        if (actionSuite.equals("motifs")) {
            HEMotifsListViewBean mBean = (HEMotifsListViewBean) viewBean;
            // L'ordre
            // A partir de
            String from = request.getParameter("from");
            String orderBy = request.getParameter("order");
            if (orderBy.equals("codeutilisateur")) {
                // mBean.setOrder("PCOSID");
                // mBean.setFromCodeUtilisateur(from);
                // mBean.wantCallMethodBeforeFind(true);
            } else {
                // mBean.setOrder("PCOSLI");
                // mBean.setFromCodeUtilisateurLibelle(from);
            }
            return mBean;
        } else if (actionSuite.equals("criteremotif")) {
            HECriteremotifListViewBean cmBean = (HECriteremotifListViewBean) viewBean;
            cmBean.setForIdcriteremotif(request.getParameter("selectedId"));
            return cmBean;
        } else if (actionSuite.equals("motifcodeapplication")) {
            HEMotifcodeapplicationListViewBean vBean = (HEMotifcodeapplicationListViewBean) viewBean;
            vBean.setForIdCodeApplication(request.getParameter("selectedId"));
            return vBean;
        } else if (actionSuite.equals("parametrageannonce")) {
            HEParametrageannonceListViewBean vBean = (HEParametrageannonceListViewBean) viewBean;
            // fromIdCodeApplication contient un code utilisateur, pas un code
            // système, genre 11 pour 110001
            // récupération du CS pour un CU
            vBean.setFromIdCSCodeApplication(request.getParameter("fromIdCodeapplication"));
            if (vBean.getFromIdCSCodeApplication().length() != 0) {
                vBean.wantCallMethodBeforeFind(true);
            }
            return vBean;
        } else if (actionSuite.equals("methode")) {
            HEMethodeListViewBean vBean = (HEMethodeListViewBean) viewBean;
            vBean.setFromLibelle(request.getParameter("fromLibelle"));
            return vBean;
        } else if (actionSuite.equals("lienannonce")) {
            HELienannonceListViewBean vBean = (HELienannonceListViewBean) viewBean;
            vBean.setForIdParametrageAnnonce(request.getParameter("selectedId"));
            return vBean;
        } else if (actionSuite.equals("attenteRetour")) {
            HEAttenteRetourListViewBean vBean = (HEAttenteRetourListViewBean) viewBean;
            vBean.setForIdAnnonceRetour("0");
            // vBean.setForIdAnnonce(request.getParameter("idAnnonce"));
            return vBean;
        } else if (actionSuite.equals("attenteRetourOptimized")) {
            // || actionSuite.equals("attenteReception") ||
            // actionSuite.equals("attenteEnvoi")) {
            HEAttenteRetourOptimizedListViewBean vBean = (HEAttenteRetourOptimizedListViewBean) viewBean;
            String numAVS = request.getParameter("numeroavs");
            numAVS = globaz.hermes.utils.StringUtils.removeDots(numAVS);
            vBean.setLikeNumeroAvs(numAVS);
            vBean.setForMotif(request.getParameter("motif"));
            vBean.setForStatut(request.getParameter("statut"));
            vBean.setForUserId(request.getParameter("userid"));
            vBean.setForNom(request.getParameter("nom"));
            vBean.setForIdAnnonce(request.getParameter("idAnnonce"));
            vBean.setIsArchivage(!StringUtils.isStringEmpty(request.getParameter("isArchivage")) ? Boolean.valueOf(
                    request.getParameter("isArchivage")).booleanValue() : false);
            try {
                vBean.setForDate(new JADate(request.getParameter("date")).toAMJ().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // pour appel depuis PAVO
            String refUnique = request.getParameter("referenceUnique");
            if (!refUnique.equals("null") && refUnique.trim().length() != 0) {
                vBean.setForReferenceUnique(refUnique);
            }
            String typeRetour = request.getParameter("typeRetour");
            if (!typeRetour.equals("null") && typeRetour.trim().length() != 0) {
                vBean.setForTypeRetour(typeRetour);
            }
            return vBean;
        } else if (actionSuite.equals("attenteEnvoiCI") || actionSuite.equals("attenteRetourCI")) {
            HEOutputAnnonceListViewBean vBean = (HEOutputAnnonceListViewBean) viewBean;
            // vBean.setForRefUnique(request.getParameter("refUnique"));
            vBean.setIsArchivage(StringUtils.isStringEmpty(request.getParameter("isArchivage")) ? false : Boolean
                    .valueOf(request.getParameter("isArchivage")).booleanValue());
            vBean.setForNumeroAVS(StringUtils.removeDots(request.getParameter("numavs")));
            vBean.setForNumCaisse(request.getParameter("caisse"));
            vBean.setForCodeApplicationLike("3");
            vBean.wantCallMethodAfterFind(false);
            vBean.wantCallMethodBefore(false);
            vBean.wantCallMethodBeforeFind(false);
            return vBean;
        }
        return viewBean;
    }

    // Attention : le dispatcher ne doit pas être appelé dans cette méthode. La
    // gestion des droits se fait sur l'action "modifier" par la suite
    @Override
    protected FWViewBeanInterface beforeModifier(javax.servlet.http.HttpSession session,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
            FWViewBeanInterface viewBean) {
        String actionSuite = getActionSuite(request);
        if (actionSuite.equals("codeapplication")) {
            HECodeapplicationViewBean vBean = (HECodeapplicationViewBean) viewBean;
            return vBean;
        } else if (actionSuite.equals("motifs")) {
            HEMotifsViewBean vBean = (HEMotifsViewBean) viewBean;
            return vBean;
        } else if (actionSuite.equals("motifcodeapplication")) {
            HEMotifcodeapplicationViewBean vBean = (HEMotifcodeapplicationViewBean) viewBean;
            vBean.setIdMotifCodeApplication(request.getParameter("motifId"));
            vBean.setIdMotif(request.getParameter("idmotif"));
            return vBean;
        } else if (actionSuite.equals("criteremotif")) {
            HECriteremotifViewBean vBean = (HECriteremotifViewBean) viewBean;
            vBean.setIdcriteremotif(request.getParameter("modifyId"));
            vBean.setIdcritere(request.getParameter("idcritere"));
            vBean.setFatherId(request.getParameter("fatherId"));
            return vBean;
        } else if (actionSuite.equals("parametrageannonce")) {
            HEParametrageannonceViewBean vBean = (HEParametrageannonceViewBean) viewBean;
            vBean.setCodeEnregistrementDebut(request.getParameter("codeEnregistrementDebut"));
            vBean.setCodeEnregistrementFin(request.getParameter("codeEnregistrementFin"));
            // ? quid des codes application ???
            return vBean;
        } else if (actionSuite.equals("methode")) {
            HEMethodeViewBean vBean = (HEMethodeViewBean) viewBean;
            vBean.setNewCode(request.getParameter("code"));
            vBean.setNewLibelle(request.getParameter("libelle"));
            return vBean;
        } else if (actionSuite.equals("attenteRetour")) {
            HEAttenteRetourViewBean vBean = (HEAttenteRetourViewBean) viewBean;
            vBean.replaceValues(HttpUtils.getParamsAsMap(request));
            return vBean;
        } else if (actionSuite.equals("attenteRetourOptimized")) {
            HEAttenteRetourOptimizedViewBean vBean = (HEAttenteRetourOptimizedViewBean) viewBean;
            // ////// TOSTR ?
            vBean.putFieldsToUpdate(HttpUtils.getParamsAsMap(request));
            return vBean;
        } else if (actionSuite.equals("attenteEnvoi")) {
            HEAttenteEnvoiViewBean vBean = (HEAttenteEnvoiViewBean) viewBean;
            vBean.putFieldsToUpdate(HttpUtils.getParamsAsMap(request));
            vBean.setNumeroAffilie(request.getParameter("numeroAffilie"));
            vBean.setNumeroSuccursale(request.getParameter("numeroSuccursale"));
            vBean.setNumeroEmploye(request.getParameter("numeroEmploye"));
            vBean.setDateEngagement(request.getParameter("dateEngagement"));
            return vBean;
        } else if (actionSuite.equals("attenteEnvoiCI")) {
            HEOutputAnnonceViewBean vBean = (HEOutputAnnonceViewBean) viewBean;
            for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
                String name = (String) e.nextElement();
                if (name.startsWith("TOSTR")) {
                    vBean.put(name.substring(5, name.length()), request.getParameter(name));
                } else if (name.startsWith("118")) {
                    vBean.put(name, request.getParameter(name));
                }
            }
            return vBean;
        }
        return viewBean;
    }

    // Attention : le dispatcher ne doit pas être appelé dans cette méthode. La
    // gestion des droits se fait sur l'action "supprimer" par la suite
    @Override
    protected FWViewBeanInterface beforeSupprimer(HttpSession session, HttpServletRequest request,
            HttpServletResponse response, FWViewBeanInterface viewBean) {
        String actionSuite = getActionSuite(request);
        if (actionSuite.equals("motifcodeapplication")) {
            // piège, selectedId ne contient pas la clef primaire
            HEMotifcodeapplicationViewBean vBean = (HEMotifcodeapplicationViewBean) viewBean;
            vBean.setIdMotifCodeApplication(request.getParameter("motifId"));
        }
        return viewBean;
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (25.10.2002 15:27:52)
     * 
     * @return java.lang.String
     * @param param
     *            javax.servlet.http.HttpServletRequest
     */
    public String getActionSuite(HttpServletRequest request) {
        return split(request.getParameter("userAction"), 2);
    }

    /* Découpe et retourne une chaîne de caractère compris entre un élément */
    public String split(String str, int pos) {
        Vector tmp = new Vector();
        try {
            StringTokenizer st = new StringTokenizer(str, ".");
            while (st.hasMoreTokens()) {
                tmp.add(st.nextToken());
            }
            return (String) tmp.elementAt(pos);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
