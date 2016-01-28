<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_IMD_D"

	idEcran="PRE2003";


	globaz.corvus.vb.process.REImprimerDecisionViewBean viewBean = (globaz.corvus.vb.process.REImprimerDecisionViewBean)session.getAttribute("viewBean");

	userActionValue=globaz.corvus.servlet.IREActions.ACTION_IMPRIMER_DECISION+".executer";

	selectedIdValue = viewBean.getIdDemandeRente();
	String noDemandeRente = viewBean.getIdDemandeRente();
	String idTierRequerant = viewBean.getIdTiersRequerant();
	String idTierBenef = request.getParameter("idTierBeneficiaire");

	String eMailAddress=viewBean.getEMailAddress();
	String requerantDescription = viewBean.getRequerantInfo(idTierBenef);
	
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	BSession objSession = (BSession)controller.getSession();

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%if(JadeStringUtil.isNull(menuOptionToLoad) || JadeStringUtil.isEmpty(menuOptionToLoad)){%>
	
<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<%@page import="globaz.globall.db.BSession"%><ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
	<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu">
	</ct:menuChange>
<%}else if("decision".equals(menuOptionToLoad)){%>
	<ct:menuChange displayId="options" menuId="corvus-optionsdecisions" showTab="options">
		<ct:menuSetAllParams key="selectedId" checkAdd="no" value="<%=viewBean.getIdDecision()%>"/>
		<ct:menuSetAllParams key="noDemandeRente" checkAdd="no" value="<%=viewBean.getIdDemandeRente()%>"/>
		<ct:menuSetAllParams key="idTierRequerant" checkAdd="no" value="<%=viewBean.getIdTiersRequerant()%>"/>
		<ct:menuSetAllParams key="provenance" checkAdd="no" value="<%=globaz.corvus.vb.prestations.REPrestationsJointDemandeRenteViewBean.FROM_ECRAN_DECISIONS%>"/>
		<ct:menuSetAllParams key="idPrestation" checkAdd="no" value="<%=viewBean.getIdPrestation()%>"/>
		<ct:menuSetAllParams key="montantPrestation" checkAdd="no" value="<%=viewBean.getMontantPrestation()%>"/>
		<%if (globaz.corvus.api.decisions.IREDecision.CS_ETAT_ATTENTE.equals(viewBean.getCsEtatDecision())) {%>
		 		<ct:menuActivateNode active="no" nodeId="imprimerDecision"/>
		<%} else { %>
				<ct:menuActivateNode active="yes" nodeId="imprimerDecision"/>
	    <%}%>
	    
	    <%if (REPmtMensuel.isValidationDecisionAuthorise(objSession)) {%>
		 		<ct:menuActivateNode active="yes" nodeId="validerdecision"/>
		<%} else { %>
				<ct:menuActivateNode active="no" nodeId="validerdecision"/>
	    <%}%>	    
	</ct:menuChange>
<%}%>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_IMD_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
<%if (globaz.framework.bean.FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {%>
	<TR><TD colspan="4" style="color:red;">HACK-FWK.error : <%=viewBean.getMessage()%></TD></TR>
<%}%>

		<input type = "hidden" name = "idTiersRequerant" value = "<%= viewBean.getIdTiersRequerant() %>">
		<input type = "hidden" name = "idDemandeRente" value = "<%= viewBean.getIdDemandeRente() %>">
		<input type="hidden" name="menuOptionToLoad" value="<%=menuOptionToLoad%>">
		<TR>
			<TD><LABEL for="requerantDescription"><b><ct:FWLabel key="JSP_IMD_D_REQUERANT"/></b></TD>
			<TD colspan="3"><INPUT type="text" name="requerantDescription" value="<%=requerantDescription%>" style="width:550px;" class="RElibelleExtraLongDisabled" disabled="true" READONLY></TD>
		</TR>
		<TR>
			<TD><LABEL for="idDecision"><ct:FWLabel key="JSP_IMD_D_NO_DECISION"/></TD>
			<TD colspan="3"><INPUT type="text" name="idDecision" value="<%=viewBean.getIdDecision()%>" disabled="true" size="7" READONLY></TD>
		</TR>

		<TR>
			<TD><LABEL for="eMailAddress"><ct:FWLabel key="JSP_IMD_D_EMAIL"/></TD>
			<TD><INPUT type="text" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>" style="width:250px;"></TD>
		</TR>
		<TR>
			<TD><LABEL for="dateDocument"><ct:FWLabel key="JSP_IMD_D_DATE_DOCUMENT"/></LABEL></TD>
			<TD>
				<% if (viewBean.getCsEtatDecision().equals(globaz.corvus.api.decisions.IREDecision.CS_ETAT_VALIDE)){ %>
					<input type="text" name="dateDocument" value="<%=viewBean.getDateDocument()%>" class="disabled" size="10" readonly/>
				<% } else { %>
					<input	id="dateDocument"
							name="dateDocument"
							data-g-calendar="type:default"
							value="<%=viewBean.getDateDocument()%>" />
				<% } %>
			</TD>
		</TR>
		<% if (viewBean.getCsEtatDecision().equals(globaz.corvus.api.decisions.IREDecision.CS_ETAT_VALIDE)){ %>
			<% if ("1".equals(viewBean.getDisplaySendToGed())) { %> 
				<TR>
					<TD><ct:FWLabel key="JSP_ENVOYER_DANS_GED"/></TD>
					<TD>
						<INPUT type="checkbox" name="isSendToGed" value="on">
						<INPUT type="hidden" name="displaySendToGed" value="1">
					</TD>
				</TR>
				<TR>
					<TD>&nbsp;</TD>
				</TR>
			<% } else {%>	
				<INPUT type="hidden" name="isSendToGed" value="">						
				<INPUT type="hidden" name="displaySendToGed" value="0">
			<% } %>
		<% } %>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>