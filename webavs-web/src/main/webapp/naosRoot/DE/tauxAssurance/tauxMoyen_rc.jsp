<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CAF0066";
	globaz.naos.db.tauxAssurance.AFTauxMoyenViewBean viewBean = (globaz.naos.db.tauxAssurance.AFTauxMoyenViewBean)session.getAttribute ("viewBean");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "naos.tauxAssurance.tauxMoyen.lister";
	bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Durchschnittsbeitragsätze
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%
							if (globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getAffiliationId())) {
								bButtonNew = false;
							} else {
								actionNew += "&affiliationId=" + viewBean.getAffiliationId();
							}
						%>
						<naos:AFInfoAffiliation name="forIdAffiliation" affiliation="<%=viewBean.getAffiliation()%>"/>
						<TR>
		                    <TD nowrap width="128">Jahr:</TD>
                        	<TD nowrap>
                        		<INPUT type="text" name="fromAnnee" size="4" maxlength="4" tabindex="1">
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