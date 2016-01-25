<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<script type="text/javascript"
		src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_BAC_D"
	
	idEcran="PRE0090";

	globaz.corvus.vb.recap.REVisuRecapMensuelleViewBean viewBean = (globaz.corvus.vb.recap.REVisuRecapMensuelleViewBean) session.getAttribute("viewBean");
	userActionValue=globaz.corvus.servlet.IREActions.ACTION_RECAP_VISU + ".charger";
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty">
</ct:menuChange>
<script language="JavaScript">
	function focus(){
		document.forms[0].elements("dateRapport").focus();
	}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_VSU_RECAP_MENSUELLE_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	 <tr>
	 	<TD><ct:FWLabel key="JSP_CHG_RECAP_MENSUELLE_DATE"/></TD>
		<td>&nbsp;</td>
		<TD>&nbsp;</TD>
		<TD>&nbsp;</TD>
		<td>
			<input	id="dateRapport"
					name="dateRapport"
					data-g-calendar="type:month"
					value="" />
			<script type="text/javascript">
				document.getElementById("dateRapport").onload=function(){focus();}
			</script>	
		</td>
	</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>