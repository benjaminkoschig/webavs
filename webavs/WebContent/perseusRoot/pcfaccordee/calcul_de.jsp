<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.perseus.vb.pcfaccordee.PFCalculViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	idEcran="PPF1021";

	PFCalculViewBean viewBean = (PFCalculViewBean) session.getAttribute("viewBean");
	
	autoShowErrorPopup = true;
	
	bButtonNew      = false;
	bButtonUpdate   = false;
	bButtonCancel 	= false;	
	bButtonDelete   = false;
	if (JadeStringUtil.isEmpty(viewBean.getCheckCalcul())&& objSession.hasRight("perseus", FWSecureConstants.UPDATE)) {
		bButtonValidate = true;
	} else {
		bButtonValidate = false;
	}
	
	PersonneEtendueComplexModel personne = viewBean.getDemande().getDossier().getDemandePrestation().getPersonneEtendue();
	String affichePersonnne = PFUserHelper.getDetailAssure(objSession,personne);
%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="perseus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="perseus-optionsdemandes">
	<ct:menuSetAllParams key="idDemande" value="<%=viewBean.getDemande().getId()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getDemande().getSituationFamiliale().getId()%>"/>
</ct:menuChange>

<script type="text/javascript">

	function add() {}
	function upd() {}	
	function validate() {
		document.forms[0].elements('userAction').value='perseus.pcfaccordee.calcul.calculer'
		return true;
	}    
	function cancel() {}
	
	function init(){}
	function postInit(){
		$('.btnAjax input').attr('disabled',false);	
	}

</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_CALCUL_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
						
						<td>
							<table cellpadding="10">
								<tr>
									<td width="200" valign="top"><ct:FWLabel key="JSP_PF_CALCUL_REQUERANT"/></td>
									<td><%=affichePersonnne%></td>
								</tr>
								<tr>
									<td><ct:FWLabel key="JSP_PF_CALCUL_DATE_DEPOT"/></td>
									<td><%=viewBean.getDemande().getSimpleDemande().getDateDepot() %></td>
								</tr>
								<tr>
									<td><ct:FWLabel key="JSP_PF_CALCUL_PERIODE"/></td>
									<td><%=viewBean.getDemande().getSimpleDemande().getDateDebut() + " - " + viewBean.getDemande().getSimpleDemande().getDateFin() %></td>
								</tr>
								<tr>
									<td><ct:FWLabel key="JSP_PF_CALCUL_CALCUL_PRECEDANT"/></td>
									<% if (!viewBean.getPcfAccordee().isNew()) { %>
										<td><%=viewBean.getPcfAccordee().getSimplePCFAccordee().getDateCalcul() %></td>
									<% } else { %>
										<td><ct:FWLabel key="JSP_PF_CALCUL_AUCUN"/></td>
									<% } %>
								</tr>
								<tr>
									<td valign="top"><ct:FWLabel key="JSP_PF_CALCUL_CONTROLE"/></td>
									<% if (JadeStringUtil.isEmpty(viewBean.getCheckCalcul())) { %>
										<td><ct:FWLabel key="JSP_PF_CALCUL_OK"/></td>
									<% } else { %>
										<td>
											<b><ct:FWLabel key="JSP_PF_CALCUL_KO"/></b>
											<p><%=viewBean.getCheckCalcul() %></p>
										</td>
									<% } %>
								</tr>
								
							</table>
							
							<ct:inputHidden name="idDemande"/>
						
						</td>
						
						<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
