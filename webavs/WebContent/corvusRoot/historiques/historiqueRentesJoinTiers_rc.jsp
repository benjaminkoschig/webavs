<%-- tpl:insert page="/theme/find.jtpl" --%>
	<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
	<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
	<%@ include file="/theme/find/header.jspf" %>
	
	<%-- tpl:put name="zoneInit" --%>
		<%
			// Les labels de cette page commence par la préfix "JSP_RAC_R"
		
			//TODO: à changer!
			idEcran="PRE0046"; 
		
			//rememberSearchCriterias = true;
			String idTierRequerant = request.getParameter("idTierRequerant");
			
			globaz.corvus.vb.historiques.REHistoriqueRentesJoinTiersViewBean viewBean = (globaz.corvus.vb.historiques.REHistoriqueRentesJoinTiersViewBean)request.getAttribute("viewBean");
			actionNew =  servletContext + mainServletPath + "?userAction=" + globaz.corvus.servlet.IREActions.ACTION_HISTORIQUE_RENTES + ".afficher&_method=add&idTierRequerant="+idTierRequerant+"&idTiersIn="+viewBean.getIdTiersIn();
			userActionNew =  globaz.corvus.servlet.IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".afficher&idTierRequerant="+idTierRequerant+"&idTiersIn="+viewBean.getIdTiersIn();
		
			btnNewLabel = objSession.getLabel("JSP_HIST_NOUVEAU");
		%>
	<%-- /tpl:put --%>

	<%@ include file="/theme/find/javascripts.jspf" %>
	
	<%-- tpl:put name="zoneScripts" --%>
		<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
		<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>

		<script language="javascript">
			usrAction = "corvus.historiques.historiqueRentesJoinTiers.lister";
			bFind = true;
		</script>
	<%-- /tpl:put --%>

	<%@ include file="/theme/find/bodyStart.jspf" %>
	
	<%-- tpl:put name="zoneTitle" --%>
		<ct:FWLabel key="JSP_HIST_R_TITRE"/>
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/find/bodyStart2.jspf" %>
	
	<%-- tpl:put name="zoneMain" --%>
		<%if (globaz.framework.bean.FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {%>
			<tr>
				<td class="red"><strong><%=viewBean.getMessage()%></strong></td>
			</tr>
		<%} %>		
		<tr>
			<td>
				<table border="0" cellspacing="0" cellpadding="0" width="100%">
					<tr>
						<td>
							<input type="hidden" name="idTierRequerant" value="<%=idTierRequerant%>"/>
							<input type="hidden" name="idTiersIn" value="<%=viewBean.getIdTiersIn()%>"/>
							<td>
								<ct:FWLabel key="JSP_HIST_REQUERANT"/>
							</td>										
							<td>
								<%String s= viewBean.getRequerantInfo();%>
								<input type="text" class="libelleExtraLongDisabled" name="requerantInfo" value="<%=s%>"/>
							</td>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/find/bodyButtons.jspf" %>
	
	<%-- tpl:put name="zoneButtons" --%>
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/find/bodyEnd.jspf" %>
	
	<%-- tpl:put name="zoneVieuxBoutons" --%>
	<%-- /tpl:put --%>
	
	<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>