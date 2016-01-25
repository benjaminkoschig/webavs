 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@page import="globaz.lynx.db.extourne.LXExtourneProcessViewBean"%>
<%@ page import="globaz.globall.util.JACalendar" %>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran="GLX0024";
	LXExtourneProcessViewBean viewBean = (LXExtourneProcessViewBean) session.getAttribute ("viewBean");
	userActionValue = "lynx.extourne.extourne.executer";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession) controller.getSession();		
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 


<script>
function init() { } 

function postInit() {
	document.getElementById('eMailAddress').focus();
}
function onOk() {
	 document.forms[0].submit();
} 
function onCancel() { 
}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%> 
Gutschrift stornieren 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%> 

	<TR> 
		<%@ include file="/lynxRoot/FR/include/enteteJournalSociete.jsp" %>
	</TR>
	<TR> 
		<TD width="128">Lieferant</TD>
		<TD height="11" colspan="4">	
			<INPUT type="hidden" name="idFournisseur" value="<%= viewBean.getIdFournisseur() %>"/>
			<INPUT type="text" value="<%=globaz.lynx.utils.LXFournisseurUtil.getIdEtLibelleNomComplet(objSession, viewBean.getIdFournisseur())%>" style="width:7cm" class="libelleLongDisabled" readonly="readonly">
		</TD>
	</TR>
	<TR> 
		<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
	</TR>
		<TD width="128">Bezeichnung</TD>
		<TD>	
			<INPUT type="hidden" name="idOperation" value="<%= viewBean.getIdOperation() %>"/>
			<INPUT type="text" value="<%= viewBean.getLibelle() %>" style="width:7cm" class="libelleLongDisabled" readonly="readonly">
		</TD>
		<TD>&nbsp;</TD>
		<TD width="128">Betrag</TD>
		<TD>	
			<INPUT type="text" value="<%=globaz.globall.util.JANumberFormatter.fmt(viewBean.getMontant(), true, true, false, 2)%>" style="width:7cm" class="libelleLongDisabled" readonly="readonly">
		</TD>
	<TR> 
		<TD height="11" colspan="5"><hr size="3" width="100%"></TD>
	</TR>
	<TR> 
		<TD width="128">E-Mail Adresse</TD>
		<TD><input name='eMailAddress' id='eMailAddress' class='libelleLong' value='<%=viewBean.getEMailAddress()==null?"":viewBean.getEMailAddress()%>' tabindex="1"></TD>
		<TD>&nbsp;</TD>
		<TD width="128">Stornierungsdatum</TD>
		<TD>
		   	<ct:FWCalendarTag name="dateExtourne" doClientValidation="CALENDAR" value="<%=JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY)%>" tabindex="1"/>
		</TD>
	</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>
