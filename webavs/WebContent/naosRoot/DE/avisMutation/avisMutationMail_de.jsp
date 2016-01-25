<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "CAF3001";
	globaz.naos.process.AFAvisMutationProcess viewBean= new globaz.naos.process.AFAvisMutationProcess();
	userActionValue = "naos.avisMutation.avisMutation.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>

function init() { 
} 

function onOk() {
	document.forms[0].submit();
} 

function onCancel() {
	document.forms[0].elements('userAction').value="back";
	document.forms[0].submit();
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Eine oder mehrere Mutationsmeldungen generieren
					<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>Abr.-Nr.</TD>
							<TD> 
								<INPUT name="affiliationId" class="numeroLong" value="" type="text">
							</TD>
						</TR>
						<TR>
							<TD height="32">E-Mail Adresse</TD>
							<TD height="32">
								<INPUT name='eMailAddress' class='libelleLong' value=''>
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>