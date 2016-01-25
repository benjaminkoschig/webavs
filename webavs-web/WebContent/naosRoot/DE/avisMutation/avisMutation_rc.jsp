<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CAF0030";
	globaz.naos.db.avisMutation.AFAvisMutationViewBean viewBean = (globaz.naos.db.avisMutation.AFAvisMutationViewBean)session.getAttribute("viewBean");
 	bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "naos.avisMutation.avisMutation.lister";
</SCRIPT>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFOptionsAffiliation" showTab="options">
		<ct:menuSetAllParams key="affiliationId" value="<%=viewBean.getAffiliationId()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getAffiliationId()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Liste der Mutationsmeldungen
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%
							if (globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getTiersId())) {
								bButtonNew = false;
							} else {
								actionNew += "&selectedId=" + viewBean.getTiersId();
							}
						%>
						<TR> 
						    <TD nowrap width="200"><A href="<%=request.getContextPath()%>\naos?userAction=naos.affiliation.affiliation.afficher">Mitglied:</A></TD>
							<TD nowrap colspan="3" width="400"> 
								<INPUT type="hidden" name="forIdTiers"  value="<%=viewBean.getTiersId()%>" >
						  		<INPUT type="text" name="nom" size="60" maxlength="60" value="<%=viewBean.getTiers().getNom()%>" tabindex="-1" readOnly class="disabled">
								<% 
								  	StringBuffer tmpLocaliteLong = new StringBuffer(viewBean.getTiers().getRue().trim());
								  	if (tmpLocaliteLong.length() != 0) {
								  		tmpLocaliteLong = tmpLocaliteLong.append(", ");
								  	}
									tmpLocaliteLong.append(viewBean.getTiers().getLocaliteLong());
								%>
								<input type="text" name="localiteLong" size="60" maxlength="60" value="<%=tmpLocaliteLong.toString()%>" tabindex="-1" readOnly class="disabled">
								<input type="text" name="canton" size="60" maxlength="60" value="<%=viewBean.getTiers().getCantonDomicile()%>" tabindex="-1" readOnly class="disabled">
								<input type="text" name="affilieNumero" size="60" maxlength="60" value="<%=viewBean.getAffiliation().getAffilieNumero()%>" readOnly tabindex="-1" class="disabled">
						    </TD>
						</TR>
						<TR>
				            <TD nowrap height="31" colspan="4"></TD>
						</TR>
						<TR>
					        <TD nowrap width="200">Mutationsmeldung ab dem</TD>
				            <TD nowrap colspan="3"><ct:FWCalendarTag name="fromDateAnnonce" value="" /></TD>  
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