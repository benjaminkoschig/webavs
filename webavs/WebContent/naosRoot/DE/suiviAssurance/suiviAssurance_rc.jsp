<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CAF0042";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "naos.suiviAssurance.suiviAssurance.lister";
	bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Liste der ablaufe einer Versicherung
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%
							globaz.naos.db.suiviAssurance.AFSuiviAssuranceViewBean viewBean = (globaz.naos.db.suiviAssurance.AFSuiviAssuranceViewBean)session.getAttribute("viewBean");
							if (globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getAssuranceId())) {
								bButtonNew = false;
							} else {
								actionNew += "&assuranceId="+ viewBean.getAssuranceId();
							}
						%>
						<TR>
	        				<TD nowrap width="128"></TD>
							<TD nowrap colspan="2" width="298">
              					<INPUT type="hidden" name="forAssuranceId" value="<%=viewBean.getAssuranceId()%>">
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