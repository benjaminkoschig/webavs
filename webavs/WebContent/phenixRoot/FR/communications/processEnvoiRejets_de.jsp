<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 

<%@ page import="globaz.phenix.db.communications.*" %>
<%@ page import="globaz.phenix.process.communications.CPProcessEnqueterEnMasseViewBean"%>

<%
	idEcran="CCP1036";
	CPRejetsListViewBean viewBean = (CPRejetsListViewBean) session.getAttribute("viewBean");
	userActionValue = "phenix.communications.rejets.envoiSedexExecuter";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession) controller.getSession();
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Ré-envoi des rejets (SEDEX)<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%> 

<TR>
 <%if (!processLaunched) {%>
	<TD colspan="2"><b><u><%=viewBean.getListIdRetour() == null ? "0" : ""+viewBean.getListIdRetour().length%></u> rejets vont être ré-envoyés via Sedex </b></TD>
<% } else { %>
	<TD colspan="2"><b>Les communications sont soumises pour l'envoi.</u></TD>
<% }%>
</TR>
<TR>
	<TD width="300px" colspan="2">&nbsp;</TD>
</TR>	
<TR>
	<TD height="2" width="165">Adresse E-Mail</TD>
	<TD height="2" width="513">
		<input name='eMailAddress' id='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value='<%=objSession.getUserEMail()%>' >
	</TD>
</TR>

<%	if(viewBean.getListIdRetour() != null) { 
		for(int i = 0; i < viewBean.getListIdRetour().length ; i++) {
%>
	<input name="listIdRetour" type="hidden" value="<%=viewBean.getListIdRetour()[i]%>"/><% 		
		}
	} 
%>  
<%-- /tpl:put --%>					
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>