<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

		<%@ page import="globaz.hera.tools.*"%>
		<%@ page import="globaz.hera.db.famille.*"%>
		<%@ page import="globaz.hera.vb.famille.*"%>
		<%
idEcran = "GSF0005";
BISession bSession = ((globaz.framework.controller.FWController) session.getAttribute("objController")).getSession();
globaz.hera.vb.famille.SFPeriodeViewBean viewBean = new globaz.hera.vb.famille.SFPeriodeViewBean();
viewBean.setISession(bSession);
if (session.getAttribute("viewBean")!=null) {
	if (session.getAttribute("viewBean") instanceof SFPeriodeViewBean){
	 viewBean =(globaz.hera.vb.famille.SFPeriodeViewBean)session.getAttribute("viewBean");
	}
}



String idMembreFamille = request.getParameter("idMembreFamille");
idEcran="GSF0009";

String warningMsg = "";
if (FWViewBeanInterface.WARNING.equals(viewBean.getMsgType())) {
	warningMsg = viewBean.getMessage();
	viewBean.setMessage("");
	viewBean.setMsgType(FWViewBeanInterface.OK);
}


String NomPrenom = "";
String NumAvs = "";
String dateNaissance = "";
String sexe = "";
String pays = "";
String csDomaine = "";
boolean hasError = false;




	//On regarde si on a un idMembreFamille, sinon on prend le requérant
	if (!globaz.globall.util.JAUtil.isStringEmpty(idMembreFamille)) {
		viewBean.retrieveMembre(((globaz.framework.controller.FWController) session.getAttribute("objController")).getSession(), idMembreFamille);
		NomPrenom = viewBean.getNomPrenom();
		NumAvs	  = viewBean.getNumAvs();
		pays = viewBean.getLibellePays();
		dateNaissance = viewBean.getDateNaissance();
		sexe = viewBean.getLibelleSexe();
		csDomaine = viewBean.getCsDomaine();
	
	}else{
		SFRequerantDTO requerant = (SFRequerantDTO)SFSessionDataContainerHelper.getData(session,SFSessionDataContainerHelper.KEY_REQUERANT_DTO);
		if(requerant!=null){
		idMembreFamille = requerant.getIdMembreFamille();
		NomPrenom = requerant.getNom()+" "+requerant.getPrenom();
		NumAvs = requerant.getNss();
		dateNaissance = requerant.getDateNaissance();
		sexe = requerant.getLibelleSexe();
		pays = requerant.getLibellePays();
		csDomaine = requerant.getIdDomaineApplication();
		}else{
		//erreur pas de requerant ni de membre sélectionné
		hasError = true;
		%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.api.BISession"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%><SCRIPT>alert("<%=((globaz.globall.db.BSession)session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_SESSION)).getLabel("JSP_PERIODE_ALERT_NO_REQUERANT")%>");</SCRIPT>
		<%
		}
	}
actionNew += "&idMembreFamille=" + idMembreFamille;
%>
<%-- /tpl:put --%>

<%@ include file="/theme/find/javascripts.jspf" %>

<%-- tpl:put name="zoneScripts" --%>
		<ct:menuChange displayId="menu" menuId="sf-menuprincipal"
			showTab="menu" checkAdd="no" />
		<ct:menuChange displayId="options" menuId="sf-optionsempty" />

		<SCRIPT>


		var warningObj = new Object();
		<%if (!JadeStringUtil.isBlankOrZero(warningMsg)) {%>
			warningObj.text = "<%=warningMsg%>";
			showWarnings();
			
		<%}%>

		function showWarnings() {
			if (warningObj.text != "") {
				showModalDialog('<%=servletContext%>/warningModalDlg.jsp',warningObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');				
			}
		}


		
		
bFind = false;
<%if(!hasError){%>
bFind = true;
usrAction = "hera.famille.periode.lister";
detailLink= servlet+"?userAction=hera.famille.periode.afficher&_method=add&idMembreFamille="+<%=idMembreFamille%>;
<%}%>
</SCRIPT>
<%-- /tpl:put --%> 


<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
				<ct:FWLabel key="JSP_PERIODE_TITLE" />
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
													<%if(session.getAttribute(globaz.hera.tools.SFSessionDataContainerHelper.KEY_VALEUR_RETOUR)!=null){
						%>
							
							<tr>
								<td><a
									href="<%=servletContext + mainServletPath + "?userAction=" + globaz.hera.servlet.ISFActions.ACTION_RELATION_FAMILIALE_REQUERANT + ".quitterApplication"%>"><ct:FWLabel
									key="JSP_URL_FROM" /></a></td>
							</tr>
							<%
						}%>
							<input type="hidden" name="forIdMembreFamille" value="<%=idMembreFamille%>">						
														
							<TR>							
								<td colspan="2"><ct:FWLabel key="JSP_SF_DOMAINE"/></td>
								<td><input type="text" name="dummy" value="<%=viewBean.getSession().getCodeLibelle(csDomaine)%>" disabled/></td>									
							</TR>
							<TR>
								<TD><ct:FWLabel key="JSP_PERIODE_DE" /></TD>
								<TD><input type="text"
									value="<%=NomPrenom + " - " +globaz.commons.nss.NSUtil.formatAVSUnknown(NumAvs) + " - " + dateNaissance + " - " + sexe + " - " + pays %>"
									disabled="disabled" size=100 class="readonly"></TD>
							</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>				
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>
