<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CAF0032";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "naos.avisMutation.avisMutation.lister";
	bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Liste des avis de mutation
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%
							globaz.naos.db.avisMutation.AFAvisMutationViewBean viewBean = (globaz.naos.db.avisMutation.AFAvisMutationViewBean)session.getAttribute("viewBean");
							if ( globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getTiersId())) {
								bButtonNew = false;
							} else {
								actionNew += "&selectedId=" + viewBean.getTiersId(); //+ "&affiliation="+viewBean.getAffiliation().getAffiliationId()+ "&selectedId4="+"1"+ "&selectedId3="+"1";
							}
						%>
						<TR>
					        <!--TD nowrap width="160">Avis de mutation depuis le</TD-->
				            <TD nowrap colspan="2" width="298"> 
						  		<input type="hidden" name="forIdTiers" size="35" maxlength="40" value="<%=viewBean.getTiersId()%>" tabindex="-1">
					            <!--input type="hidden" name="forAffiliationId" size="60" maxlength="60" value="<=viewBean.getAffiliationId()>" tabindex="-1"-->
				            </TD> 
				            <TD nowrap height="31" colspan="2"> 
				            	<!--<ct:FWCalendarTag name="dateReception" value="<=viewBean.getFromDateAnnonce()>" /> -->
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