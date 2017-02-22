<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.apg.vb.process.APListeTaxationsDefinitivesViewBean"%>
<%@page import="globaz.prestation.api.IPRDemande"%>
<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PAP2005";
	userActionValue="apg.process.listeTaxationsDefinitives.executer";
	
	APListeTaxationsDefinitivesViewBean viewBean = (APListeTaxationsDefinitivesViewBean)(session.getAttribute("viewBean"));
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession) controller.getSession();
	
	String eMailAddress = objSession.getUserEMail();
	if(!JadeStringUtil.isEmpty(viewBean.geteMailAddress())){
	    eMailAddress = viewBean.geteMailAddress();
	}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/calendar/date.js"></script>
<%
	String keyTypePrestation = (String) PRSessionDataContainerHelper.getData(session,PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION);

	if(IPRDemande.CS_TYPE_APG.equalsIgnoreCase(keyTypePrestation)){%>
		<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>	
<%	} else {%>
		<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
<%	}%>

<ct:menuChange displayId="options" menuId="ap-optionsempty"/>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LISTE_TAXATIONS_DEFINITIVES_TITRE"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

	<tr>
		<td width="20%"><ct:FWLabel key="JSP_LISTE_TAXATIONS_DEFINITIVES_ANNEETAXATIONCP"/></td>
		<td width="20%"><input type="text" name="anneeTaxationCP" value="<%=viewBean.getAnneeTaxationCP()%>" size="4"/></td>
		<td width="15%">&nbsp;</td>
		<td width="20%"><ct:FWLabel key="JSP_LISTE_TAXATIONS_DEFINITIVES_ANNEEDROIT"/></td>
		<td width="25%"><input type="text" name="anneeDroit" value="<%=viewBean.getAnneeDroit()%>" size="4"/></td>
	</tr>
	
	<tr><td width="100%" colspan="5">&nbsp;</td></tr>
	
	<tr>
		<td width="20%"><ct:FWLabel key="JSP_LISTE_TAXATIONS_DEFINITIVES_PERIODEDECISIONSCP"/></td>
		<td width="20%">
			<input type="text" name="dateDebutDecisionsCP" value="<%=viewBean.getDateDebutDecisionsCP()%>" data-g-calendar="mandatory:false"> 
			&nbsp;&nbsp;
			<ct:FWLabel key="JSP_LISTE_TAXATIONS_DEFINITIVES_PERIODE_AU"/>
			&nbsp;&nbsp;
			<input type="text" name="dateFinDecisionsCP" value="<%=viewBean.getDateFinDecisionsCP()%>" data-g-calendar="mandatory:false">
		</td>
		<td width="15%">&nbsp;</td>
		<td width="20%"><ct:FWLabel key="JSP_LISTE_TAXATIONS_DEFINITIVES_PERIODEDECOMPTE"/></TD>
		<td width="25%">
			<input type="text" name="dateDebutDecompte" value="<%=viewBean.getDateDebutDecompte()%>" data-g-calendar="mandatory:false"> 
			&nbsp;&nbsp;
			<ct:FWLabel key="JSP_LISTE_TAXATIONS_DEFINITIVES_PERIODE_AU"/>
			&nbsp;&nbsp;
			<input type="text" name="dateFinDecompte" value="<%=viewBean.getDateFinDecompte()%>" data-g-calendar="mandatory:false">
		</td>
	</tr>
	
	<tr><td width="100%" colspan="5">&nbsp;</td></tr>
	
	<tr>
		<td width="55%" colspan="3">&nbsp;</td>
		<td width="20%"><ct:FWLabel key="JSP_LISTE_TAXATIONS_DEFINITIVES_INCLUREAFFILIERADIE"/></td>
		<td width="25%"><input type="checkbox" name="inclureAffilieRadie"/></td>
	</tr>
	
	<tr><td width="100%" colspan="5">&nbsp;</td></tr>
	<tr><td width="100%" colspan="5">&nbsp;</td></tr>
	
	<tr>
		<td width="100%" colspan="5">
			<table width="100%">
				<tr>
					<td width="35%"><ct:FWLabel key="JSP_LISTE_TAXATIONS_DEFINITIVES_NOAFFILIE"/></td>
					<td width="65%" colspan="3">
						<input type="text" id="startWithNoAffilie" name="startWithNoAffilie" value="<%=viewBean.getStartWithNoAffilie()%>" data-g-autocomplete="service:ch.globaz.naos.business.service.AffiliationService,
					            method:widgetFind,
					            wantInitThreadContext:true,
					            criterias:¦{
					                likeNumeroAffilie : '<ct:FWLabel key="JSP_LISTE_PRESTATION_VERSEE_WIDGET_NUMERO_AFFILIE"/>',
					                likeNomUpper : '<ct:FWLabel key="JSP_LISTE_PRESTATION_VERSEE_WIDGET_NOM"/>'
					            }¦,
					            modelReturnVariables:¦affiliation.affilieNumero¦,
					            lineFormatter:#{affiliation.affilieNumero} #{affiliation.raisonSocialeCourt},
					            functionReturn:¦
					                function(element){
					                    this.value= $(element).attr('affiliation.affilieNumero');}¦">
			     		&nbsp;&nbsp;
			     		<ct:FWLabel key="JSP_LISTE_TAXATIONS_DEFINITIVES_PERIODE_JUSQUA"/>
			     		&nbsp;&nbsp;
			     		<input type="text" id="endWithNoAffilie" name="endWithNoAffilie" value="<%=viewBean.getEndWithNoAffilie()%>" data-g-autocomplete="service:ch.globaz.naos.business.service.AffiliationService,
					            method:widgetFind,
					            wantInitThreadContext:true,
					            criterias:¦{
					                likeNumeroAffilie : '<ct:FWLabel key="JSP_LISTE_PRESTATION_VERSEE_WIDGET_NUMERO_AFFILIE"/>',
					                likeNomUpper : '<ct:FWLabel key="JSP_LISTE_PRESTATION_VERSEE_WIDGET_NOM"/>'
					            }¦,
					            modelReturnVariables:¦affiliation.affilieNumero¦,
					            lineFormatter:#{affiliation.affilieNumero} #{affiliation.raisonSocialeCourt},
					            functionReturn:¦
					                function(element){
					                    this.value= $(element).attr('affiliation.affilieNumero');}¦">
					</td>
				</tr>
				
				<tr>
					<td width="35%"><ct:FWLabel key="JSP_LISTE_TAXATIONS_DEFINITIVES_ADRESSEMAIL"/></td>
					<td width="65%" colspan="3"><input type="text" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>" size="60"/></td>
				</tr>
			</table>
		</td>
	</tr>
	
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>