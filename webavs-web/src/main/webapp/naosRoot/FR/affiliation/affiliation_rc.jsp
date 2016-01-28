<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "CAF0002";
%>
<%-- /tpl:put --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "naos.affiliation.affiliation.lister";
	bFind = true;
</SCRIPT>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					P&eacute;riode d'affiliation
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<%
							globaz.naos.db.affiliation.AFAffiliationViewBean viewBean = (globaz.naos.db.affiliation.AFAffiliationViewBean)session.getAttribute ("viewBean");
							actionNew += "&idTiers=" + viewBean.getIdTiers();
							if(!JadeStringUtil.isBlankOrZero(viewBean.getWarningMessageAnnonceIdeCreationNotAdded())){%>
							globazNotation.utils.consoleWarn("<%=viewBean.getWarningMessageAnnonceIdeCreationNotAdded()%>",'Avertissement',true);
							<%viewBean.setWarningMessageAnnonceIdeCreationNotAdded("");%>
						<%}%>
						<TR> 
						    <TD nowrap width="120" valign="top"><A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getIdTiers()%>">Tiers</A></TD>
							<TD nowrap colspan="2" width="298">
								<INPUT type="hidden" name="forIdTiers" value="<%=viewBean.getIdTiers()%>">
								<% if(viewBean.getTiers().idTiersExterneFormate().length()!=0) { %>
								<input type="text" name="idExterne" size="60" maxlength="60" value="<%=viewBean.getTiers().idTiersExterneFormate()%>" readOnly tabindex="-1" class="disabled">
								<% } %>
						  		<INPUT type="text" name="nom" size="60" maxlength="60" value="<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getTiers().getNom(),"\"","&quot;")%>" tabindex="-1" readOnly class="disabled">
								<% 
								  	StringBuffer tmpLocaliteLong = new StringBuffer(viewBean.getTiers().getRue().trim());
								  	if (tmpLocaliteLong.length() != 0) {
								  		tmpLocaliteLong = tmpLocaliteLong.append(", ");
								  	}
									tmpLocaliteLong.append(viewBean.getTiers().getLocaliteLong());
								%>
								<input type="text" name="localiteLong" size="60" maxlength="60" value="<%=tmpLocaliteLong.toString()%>" tabindex="-1" readOnly class="disabled">
								<input type="text" name="canton" size="60" maxlength="60" value="<%=viewBean.getTiers().getCantonDomicile()%>" tabindex="-1" readOnly class="disabled">
						    </TD>
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