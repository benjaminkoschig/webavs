<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "CAF0054";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "naos.masse.masse.lister";
	bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Lohnsumme per Erfassungsperiode
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%
							globaz.naos.db.masse.AFMasseViewBean viewBean = (globaz.naos.db.masse.AFMasseViewBean)session.getAttribute("viewBean");
							actionNew += "&affiliationId="+viewBean.getAffiliationId()+ "&selectedId4="+"1"+ "&selectedId3="+"1";
						%>
						<TR>
							<TD nowrap width="128">Partner:</TD>
							<TD nowrap colspan="2" width="500">
							    <INPUT type="hidden" name="forAffiliationId"  value="<%=viewBean.getAffiliationId()%>">
							    <% if(viewBean.getTiers().idTiersExterneFormate().length()!=0) { %>
									<INPUT type="text" name="affilieNumero" size="28" maxlength="28" value="<%=viewBean.getAffiliation().getAffilieNumero()%>" readOnly tabindex="-1" class="disabled">
									<input type="text" name="idExterne" size="28" maxlength="28" value="<%=viewBean.getTiers().idTiersExterneFormate()%>" readOnly tabindex="-1" class="disabled">
								<% } else { %>
									<INPUT type="text" name="affilieNumero" size="60" maxlength="60" value="<%=viewBean.getAffiliation().getAffilieNumero()%>" readOnly tabindex="-1" class="disabled">
								<% } %>
								
							    <INPUT type="text" name="nom" size="60" maxlength="60" value="<%=viewBean.getTiers().getNom()%>" tabindex="-1" readOnly>
								<% 
						          	StringBuffer tmpLocaliteLong = new StringBuffer(viewBean.getTiers().getRue().trim());
						          	if (tmpLocaliteLong.length() != 0) {
						          		tmpLocaliteLong = tmpLocaliteLong.append(", ");
						          	}
						          	tmpLocaliteLong.append(viewBean.getTiers().getLocaliteLong());
								%>							    
							    <INPUT type="text" name="localiteLong" size="60" maxlength="60" value="<%=tmpLocaliteLong.toString()%>" tabindex="-1" readOnly>
							    <INPUT type="text" name="canton" size="60" maxlength="60" value="<%=viewBean.getTiers().getCantonDomicile()%>" tabindex="-1" readOnly>
							    <INPUT type="text" name="affilieNumero" size="60" maxlength="60" value="<%=viewBean.getAffiliation().getAffilieNumero()%>" tabindex="-1" readOnly>
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