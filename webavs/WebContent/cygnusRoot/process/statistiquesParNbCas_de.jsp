<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.cygnus.vb.process.RFStatistiquesParNbCasViewBean"%>
<%@page import="globaz.cygnus.vb.process.RFStatistiquesParMontantsSashViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.application.RFApplication"%>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_LER_D"

	idEcran="PRF0060";
	
	RFStatistiquesParNbCasViewBean viewBean = (RFStatistiquesParNbCasViewBean) session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAdr = null;
	userActionValue=IRFActions.ACTION_STATISTIQUES_PAR_NB_CAS + ".executer";
	
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
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_STATISTIQUES_PAR_NB_CAS"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

<tr>
	<td>
		<table>
			<tr>
				<td><label><ct:FWLabel key="JSP_GESTIONNAIRE_STATS_PAR_NB_CAS"/></label></td>
				<td width="50"></td>
				<td><ct:FWListSelectTag name="gestionnaire" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%=JadeStringUtil.isBlank(viewBean.getGestionnaire())?
			                        																																viewBean.getSession().getUserId():viewBean.getGestionnaire()%>" /></td>
			</tr>
			<tr>
				<td><label><ct:FWLabel key="JSP_ADR_MAIL_STATS_PAR_NB_CAS"/></label></td>
				<td width="50"></td>
				<td><input type="text" name="eMailAdr" id="eMailAdr" value="<%=eMailAdr%>" class="libelleLong" /></td>
			</tr>
		</table>
	</td>
</tr>
<tr>
	<td>
		<table>
			<tr height="10"><td></td></tr>
			<tr>
				<td><label><ct:FWLabel key="JSP_DATE_DEBUT_STATS_PAR_NB_CAS"/></label></td>
				<td width="52"></td>
				<td><input data-g-calendar=" " id="dateDebutStat" name="dateDebutStat" value="<%=viewBean.getDateDebutStat()%>"/></td>
				<td width="100"></td>
				<td><label><ct:FWLabel key="JSP_DATE_FIN_STATS_PAR_NB_CAS"/></label></td>
				<td width="50"></td>
				<td><input data-g-calendar=" " id="dateFinStat" name="dateFinStat" value="<%=viewBean.getDateFinStat()%>"/></td>
			</tr>
		</table>
	</td>
</tr>

						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>