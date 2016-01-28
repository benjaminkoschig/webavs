<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.cygnus.vb.process.RFListeRecapitulativePaiementsViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.application.RFApplication"%>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_LER_D"

	idEcran="PRF0062";
	
	RFListeRecapitulativePaiementsViewBean viewBean = (RFListeRecapitulativePaiementsViewBean) session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAdr = null;
	userActionValue=IRFActions.ACTION_LISTE_RECAPITULATIVE_PAIEMENTS + ".executer";
	
	//Condition qui permet de garder la valeur saisie par l'utilisateur après la validation
	if (!JadeStringUtil.isEmpty(viewBean.geteMailAdr())){
		eMailAdr = viewBean.geteMailAdr();
	}else{
		eMailAdr = objSession.getUserEMail();
	}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">

</SCRIPT>
<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_RF_TITRE_LISTE_RECAPITULATIVE_PAIEMENT"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

<tr>
	<td>
		<table>
			<tr>
				<td><label><ct:FWLabel key="JSP_RF_MAIL_LISTE_RECAP"/></label></td>
				<td width="50"></td>
				<td><input type="text" name="eMailAdr" id="eMailAdr" value="<%=eMailAdr%>" class="libelleLong" /></td>
				<td width="100"></td>
				<td><label><ct:FWLabel key="JSP_RF_DATE_PERIODE_LISTE_RECAP"/></label></td>
				<td width="52"></td>
				<td><input data-g-calendar="type:month" id="datePeriode" name="datePeriode" value="<%=viewBean.getDatePeriode()%>"/></td>
			</tr>
		</table>
	</td>
</tr>

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>