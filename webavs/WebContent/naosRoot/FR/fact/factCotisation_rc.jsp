<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "CAF0052";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "naos.fact.factCotisation.lister";
	bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Liste de facturation d'une assurance
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%
							globaz.naos.db.fact.AFFactViewBean viewBean = (globaz.naos.db.fact.AFFactViewBean)session.getAttribute("viewBean");
							if (globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getAffiliationId()) ||
							    globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getCotisationId())) {
								bButtonNew = false;
							} else {
								actionNew += "&affiliationId=" + viewBean.getAffiliationId() + "&cotisationId=" + viewBean.getCotisationId();
							}
						%>
						<TR>
							<TD nowrap width="128"><p>Tiers:</p></TD>
							<TD nowrap colspan="2" width="298">
								<INPUT type="hidden" name="forCotisationId" value="<%=viewBean.getCotisationId()%>">
								<INPUT type="text" name="nom" size="60" maxlength="60" value="<%=viewBean.getTiers().getNom()%>" tabindex="-1" readOnly>
		              			<% 
					              	StringBuffer tmpLocaliteLong = new StringBuffer(viewBean.getTiers().getRue().trim());
					              	if (tmpLocaliteLong.length() != 0) {
					              		tmpLocaliteLong = tmpLocaliteLong.append(", ");
					              	}
					              	tmpLocaliteLong.append(viewBean.getTiers().getLocaliteLong());
								%>
								<INPUT type="text" name="localiteLong" size="60" maxlength="60" value="<%=tmpLocaliteLong.toString()%>" tabindex="-1" readOnly>
								<INPUT type="text" name="affilieNumero" size="60" maxlength="60" value="<%=viewBean.getCotisation().getAssurance().getAssuranceLibelle()%>"tabindex="-1" readOnly>
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