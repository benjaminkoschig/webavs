 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ page import="globaz.lynx.db.ordregroupe.*" %>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran="GLX0027";
	LXOrdreGroupeExecutionViewBean viewBean = (LXOrdreGroupeExecutionViewBean) session.getAttribute ("viewBean");
	userActionValue = "lynx.ordregroupe.ordreGroupeExecution.executer";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession) controller.getSession();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 

<ct:menuChange displayId="options" menuId="LX-OrdreGroupe" showTab="options">
	<ct:menuSetAllParams key='selectedId' value='<%= request.getParameter("idOrdreGroupe") %>'/>
	<ct:menuSetAllParams key='idSociete' value='<%= request.getParameter("idSociete") %>'/>
	<ct:menuSetAllParams key='idOrdreGroupe' value='<%= request.getParameter("idOrdreGroupe") %>'/>
	<ct:menuSetAllParams key='idOrganeExecution' value='<%= request.getParameter("idOrganeExecution") %>'/>
</ct:menuChange>

<script>
function init() { } 
function postInit() {
	document.getElementById("eMailAddress").focus();
}
function onOk() {
	 document.forms[0].submit();
} 
function onCancel() { 
}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Ex�cuter l'ordre group�<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%> 
			
			<%@ include file="/lynxRoot/FR/include/enteteOrdreGroupe.jsp" %>	
			<TR> 
				<TD height="11" colspan="5"> 
					<hr size="3" width="100%">
				</TD>
			</TR>
			<TR> 
	        	<TD>Adresse E-Mail</TD>
	        	<TD><input name='eMailAddress' id='eMailAddress' class='libelleLong' value='<%=viewBean.getEMailAddress()==null?"":viewBean.getEMailAddress()%>' tabindex="1"></td>
	        </TR>
	        
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>