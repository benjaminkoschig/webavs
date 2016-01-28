// /*
// * Créé le 16 sept. 05
// *
// * Pour changer le modèle de ce fichier généré, allez à :
// * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
// */
// package globaz.hera.servlet;
//
// import globaz.framework.bean.FWViewBeanInterface;
// import globaz.framework.controller.FWAction;
// import globaz.framework.controller.FWController;
// import globaz.framework.controller.FWDispatcher;
// import globaz.framework.servlets.FWServlet;
// import globaz.globall.db.BSession;
// import globaz.hera.db.famille.SFConjointManager;
// import globaz.hera.db.famille.SFRelationConjoint;
// import globaz.hera.db.famille.SFRelationFamilialeRequerant;
// import globaz.hera.db.famille.SFRequerantDTO;
// import globaz.hera.helpers.famille.SFRequerantHelper;
// import globaz.hera.tools.SFSessionDataContainerHelper;
// import globaz.hera.vb.famille.SFRelationConjointViewBean;
// import globaz.jade.log.JadeLogger;
// import globaz.prestation.servlet.PRDefaultAction;
//
// import java.io.IOException;
//
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import javax.servlet.http.HttpSession;
//
// /**
// * @author mmu
// *
// * Pour changer le modèle de ce commentaire de type généré, allez à :
// * Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
// */
// public class SFRelationConjointAction extends PRDefaultAction {
//
// public SFRelationConjointAction(FWServlet servlet) {
// super(servlet);
// }
//
// /* (non-Javadoc)
// * @see globaz.framework.controller.FWDefaultServletAction#beforeModifier(javax.servlet.http.HttpSession,
// javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
// globaz.framework.bean.FWViewBeanInterface)
// */
// protected FWViewBeanInterface beforeModifier(
// HttpSession session,
// HttpServletRequest request,
// HttpServletResponse response,
// FWViewBeanInterface viewBean) {
// SFRelationConjointViewBean relation = new SFRelationConjointViewBean();
// try {
// relation.setIdRelationConjoint((String) request.getParameter("idRelationConjoint"));
// relation.setISession(((FWController) session.getAttribute("objController")).getSession());
// relation.retrieve(); // Retrieve pour mettre le pspy à jour
// globaz.globall.http.JSPUtils.setBeanProperties(request,relation);
// } catch (Exception e) {
// JadeLogger.warn(this, e);
// }
// //Renvoie un autre objet
// return relation;
// }
//
//
// protected String _getDestModifierEchec(HttpSession session, HttpServletRequest request, HttpServletResponse
// response,FWViewBeanInterface viewBean) {
// // _valid=fail : revient en mode edition
// return getRelativeURLwithoutClassPart(request,session)+"apercuRelationConjoint_de.jsp?_valid=fail";
// }
//
// protected String _getDestModifierSucces(HttpSession session, HttpServletRequest request, HttpServletResponse
// response,FWViewBeanInterface viewBean) {
// return "/hera?userAction=hera.famille.apercuRelationConjoint.chercher";
// }
//
// /* (non-Javadoc)
// * @see globaz.framework.controller.FWDefaultServletAction#beforeSupprimer(javax.servlet.http.HttpSession,
// javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
// globaz.framework.bean.FWViewBeanInterface)
// */
// protected FWViewBeanInterface beforeSupprimer(
// HttpSession session,
// HttpServletRequest request,
// HttpServletResponse response,
// FWViewBeanInterface viewBean) {
// SFRelationConjointViewBean relation = new SFRelationConjointViewBean();
// try {
// relation.setIdRelationConjoint((String) request.getParameter("idRelationConjoint"));
// relation.setISession(((FWController) session.getAttribute("objController")).getSession());
// relation.retrieve(); // Retrieve pour mettre le pspy à jour
// globaz.globall.http.JSPUtils.setBeanProperties(request,relation);
// } catch (Exception e) {
// JadeLogger.warn(this, e);
// }
// return relation;
// }
// /* (non-Javadoc)
// * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerEchec(javax.servlet.http.HttpSession,
// javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
// globaz.framework.bean.FWViewBeanInterface)
// */
// protected String _getDestSupprimerEchec(
// HttpSession session,
// HttpServletRequest request,
// HttpServletResponse response,
// FWViewBeanInterface viewBean) {
// // _valid=fail : revient en mode edition
// return getRelativeURLwithoutClassPart(request,session)+"apercuRelationConjoint_de.jsp?_valid=fail";
// }
//
// /* (non-Javadoc)
// * @see globaz.framework.controller.FWDefaultServletAction#_getDestSupprimerSucces(javax.servlet.http.HttpSession,
// javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
// globaz.framework.bean.FWViewBeanInterface)
// */
// protected String _getDestSupprimerSucces(
// HttpSession session,
// HttpServletRequest request,
// HttpServletResponse response,
// FWViewBeanInterface viewBean) {
// return "/hera?userAction=hera.famille.apercuRelationConjoint.chercher";
// }
//
//
// /* (non-Javadoc)
// * @see globaz.framework.controller.FWDefaultServletAction#beforeAjouter(javax.servlet.http.HttpSession,
// javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
// globaz.framework.bean.FWViewBeanInterface)
// */
// protected FWViewBeanInterface beforeAjouter(
// HttpSession session,
// HttpServletRequest request,
// HttpServletResponse response,
// FWViewBeanInterface viewBean) {
// SFRelationConjointViewBean relation = new SFRelationConjointViewBean();
// relation.setAlternateKey(SFRelationConjoint.ALT_KEY_CONJ_TYPE);
// try {
// globaz.globall.http.JSPUtils.setBeanProperties(request,relation);
// relation.setIdRelationConjoint("");
// } catch (Exception e) {
// JadeLogger.warn(this, e);
// }
// return relation;
// }
//
//
//
// /* (non-Javadoc)
// * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterEchec(javax.servlet.http.HttpSession,
// javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
// globaz.framework.bean.FWViewBeanInterface)
// */
// protected String _getDestAjouterEchec(
// HttpSession session,
// HttpServletRequest request,
// HttpServletResponse response,
// FWViewBeanInterface viewBean) {
// return getRelativeURLwithoutClassPart(request,session)+"apercuRelationConjoint_de.jsp?_valid=fail&_back=sl";
// }
//
// /* (non-Javadoc)
// * @see globaz.framework.controller.FWDefaultServletAction#_getDestAjouterSucces(javax.servlet.http.HttpSession,
// javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
// globaz.framework.bean.FWViewBeanInterface)
// */
// protected String _getDestAjouterSucces(
// HttpSession session,
// HttpServletRequest request,
// HttpServletResponse response,
// FWViewBeanInterface viewBean) {
// return "/hera?userAction=hera.famille.apercuRelationConjoint.chercher";
// }
//
// /*
// * Copie de la classe parent, sauf que l'on met en session le viewBean attendu (ApercuRelation) au lieu de (Relation)
// * (non-Javadoc)
// * @see globaz.framework.controller.FWDefaultServletAction#actionModifier(javax.servlet.http.HttpSession,
// javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
// globaz.framework.controller.FWDispatcher)
// */
// protected void actionModifier(HttpSession session, HttpServletRequest request, HttpServletResponse response,
// FWDispatcher mainDispatcher) throws javax.servlet.ServletException, java.io.IOException {
// String dest = "";
// try {
// String action = request.getParameter ("userAction");
// FWAction _action = FWAction.newInstance(action);
// FWViewBeanInterface receptVB = (FWViewBeanInterface) session.getAttribute ("viewBean");
// globaz.globall.http.JSPUtils.setBeanProperties(request,receptVB);
// FWViewBeanInterface viewBean = beforeModifier(session,request,response,receptVB);
// viewBean = mainDispatcher.dispatch(viewBean,_action);
// session.setAttribute ("viewBean", viewBean);
//
// if (viewBean.getMsgType().equals(FWViewBeanInterface.ERROR) == true) {
// session.setAttribute ("viewBean", receptVB); // On met le VB d'origine
// dest = _getDestModifierEchec(session,request,response,receptVB);
// } else {
// dest = _getDestModifierSucces(session,request,response,viewBean);
// }
//
// } catch (Exception e) {
// dest = ERROR_PAGE;
// }
// servlet.getServletContext().getRequestDispatcher (dest).forward (request, response);
// }
//
// /* (non-Javadoc)
// * @see globaz.framework.controller.FWDefaultServletAction#actionSupprimer(javax.servlet.http.HttpSession,
// javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
// globaz.framework.controller.FWDispatcher)
// */
// protected void actionSupprimer( HttpSession session, HttpServletRequest request, HttpServletResponse response,
// FWDispatcher mainDispatcher) throws ServletException, IOException {
// super.actionSupprimer(session, request, response, mainDispatcher);
// //On supprime la relation avec le conjoint si c'est la dernière relation
// String idConjoints = SFRequerantHelper.parseRequest(request, "idConjoints");
// String idMembreFamille = SFRequerantHelper.parseRequest(request, "idMembreFamille");
// SFConjointManager conjointManager = new SFConjointManager();
// conjointManager.setSession((BSession)mainDispatcher.getSession());
// conjointManager.setForIdConjoint(idConjoints);
// try {
// conjointManager.find();
// if(conjointManager.isEmpty()){
// //il n'y a plus de relation on peut alors supprimer la relation conjoint requerant
// SFRequerantDTO requerant =
// (SFRequerantDTO)SFSessionDataContainerHelper.getData(session,SFSessionDataContainerHelper.KEY_REQUERANT_DTO);
// SFRelationFamilialeRequerant relationRequerant = new SFRelationFamilialeRequerant();
// relationRequerant.setSession((BSession)mainDispatcher.getSession());
// relationRequerant.setIdMembreFamille(idMembreFamille);
// relationRequerant.setIdRequerant(requerant.getIdRequerant());
// relationRequerant.setAlternateKey(SFRelationFamilialeRequerant.ALTERNATE_KEY_MEMBRE_REQ);
// relationRequerant.retrieve();
// relationRequerant.delete();
// }
// } catch (Exception e) {
// JadeLogger.warn("Erreur pendant la suppresion de la relation entre le conjoint et le requerant",e);
// }
// }
//
// }
