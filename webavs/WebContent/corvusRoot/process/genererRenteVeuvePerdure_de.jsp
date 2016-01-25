<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.corvus.vb.process.REGenererRenteVeuvePerdureViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@page import="globaz.corvus.vb.process.REGenererTransfertDossierViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JACalendar"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// les labels de cette JSP commence par JSP_GRVP
	idEcran="PRE2013";

	REGenererRenteVeuvePerdureViewBean viewBean = (REGenererRenteVeuvePerdureViewBean)session.getAttribute("viewBean");

	userActionValue=globaz.corvus.servlet.IREActions.ACTION_GENERER_RENTE_VEUVE_PERDURE+ ".executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<script type="text/javascript" src="<%=servletContext%>/corvusRoot/script/process/genererRenteVeuvePerdure_de.js">
</script>

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" />
<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu" />

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
				<ct:FWLabel key="JSP_GRVP_TITRE" />
			<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
					<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
							<td>
								<label for="annexes">
									<ct:FWLabel key="JSP_GRVP_ANNEXE"/>
								</label>
							</td>
						</tr>
						<tr>
							<td>
								<label for="dateDocument">
									<ct:FWLabel key="JSP_GRVP_DATE_DOCUMENT" />
								</label>
							</td>
							<td>
								<input 	type="text" 
										id="dateDocument" 
										name="dateDocument" 
										data-g-calendar="mandatory:true" 
										value="<%=viewBean.getDateDocument()%>" />
							</td>
							<td rowspan="9">
								<div data-g-multistring="tagName:annexes" ></div>
							</td>
						</tr>
						<tr>
							<td>
								<label for="montantRenteVieillesse">
									<ct:FWLabel key="JSP_GRVP_MONTANT_RENTE_VIEILLESSE" />
								</label>
							</td>
							<td>
								<input	type="text"
										id="montantRenteVieillesse"
										name="montantRenteVieillesse"
										data-g-amount="mandatory:true"
										value="<%=viewBean.getMontantRenteVieillesse()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="adresseEmail">
									<ct:FWLabel key="JSP_GRVP_EMAIL" />
								</label>
							</td>
							<td>
								<input	type="text" 
										id="adresseEmail"
										name="adresseEmail"  
										data-g-string="mandatory:true" 
										value="<%=viewBean.getAdresseEmail()%>" />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<label for="annexeParDefaut">
									<ct:FWLabel key="JSP_GRVP_ANNEXE_DETAIL" />
								</label>
							</td>
							<td>
								<input	type="checkbox"
										id="annexeParDefaut"
										name="annexeParDefaut" 
										<%=viewBean.isAnnexeParDefaut()?"checked=\"checked\"":""%> />
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
<%	if (viewBean.showGedCheckbox()) { %>
							<td>
								<label for="isSendToGed">
									<ct:FWLabel key="JSP_GRVP_ENVOYER_GED" />
								</label>
							</td>
							<td>
								<input	type="checkbox" 
										id="sendToGed" 
										name="sendToGed"
										checked="checked" />
							</td>
							<td>
								&nbsp;
							</td>
<%	} else { %>
							<td colspan="3">
								&nbsp;
							</td>
<%	} %>
						</tr>
						<tr>
							<td colspan="3">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="3">
								&nbsp;
							</td>
						</tr>
					<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>