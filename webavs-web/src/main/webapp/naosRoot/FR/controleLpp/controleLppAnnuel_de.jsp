<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%>

<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.naos.db.controleLpp.AFControleLppAnnuelViewBean"%>

<%
	idEcran="CAF3020";
	AFControleLppAnnuelViewBean viewBean = (AFControleLppAnnuelViewBean) session.getAttribute("viewBean");                            
    userActionValue="naos.controleLpp.controleLppAnnuel.executer";
    
    String strSimulation = viewBean.isModeControleSimulation() ? "checked=\"checked\"" : "";
    String dateImpression = viewBean.getDateImpression() != null ? viewBean.getDateImpression() : "";
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT>
top.document.title = "Web@AVS - <ct:FWLabel key='AFFILIATION'/>";
function init(){}

function postInit() {
	var myDate = new Date();
	<% if(JadeStringUtil.isEmpty(viewBean.getAnneeDebut())) {%>
	$("#anneeDebut").val(myDate.getFullYear()-1);
	<%}%>
	<% if(JadeStringUtil.isEmpty(viewBean.getAnneeFin())) {%>
	$("#anneeFin").val(myDate.getFullYear()-1);
	<%}%>
}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key='TITRE_CONTROLE_LPP_ANNUEL'/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

<TR> 
   	<TD><ct:FWLabel key='ANNEES'/></TD>
   	<TD>
   		<INPUT name="anneeDebut" id="anneeDebut" maxlength="4" size="4" type="text" style="text-align : left;" value="<%= viewBean.getAnneeDebut() != null ? viewBean.getAnneeDebut() : "" %>" onkeypress="return filterCharForPositivInteger(window.event);" />
   		<ct:FWLabel key='A'/>
   		<INPUT name="anneeFin" id="anneeFin" maxlength="4" size="4" type="text" style="text-align : left;" value="<%= viewBean.getAnneeFin() != null ? viewBean.getAnneeFin() : "" %>" onkeypress="return filterCharForPositivInteger(window.event);" />
   	</TD>                   
 	<TD>&nbsp;</TD>
</TR> 
<TR>
    <TD width="23%" height="2"><ct:FWLabel key="TYPE_ADRESSE_DEFAUT"/></TD>
    <TD height="2"> 
    	<SELECT id="tri" name="typeAdresse" doClientValidation="">
			<OPTION value="domicile" <%="domicile".equals(viewBean.getTypeAdresse())?"selected":""%>><ct:FWLabel key="DOMICILE"/></OPTION>
			<OPTION value="courrier" <%="courrier".equals(viewBean.getTypeAdresse())?"selected":""%>><ct:FWLabel key="COURRIER"/></OPTION>
		</SELECT>
	</TD>
</TR>  			
<tr>
	<TD><ct:FWLabel key="DATE_IMPRESSION"/></TD>
	<TD><ct:FWCalendarTag name="dateImpression" value="<%=dateImpression%>" /></TD>
	<td>&nbsp;</td>
</tr>		
<tr>
	<TD><ct:FWLabel key="MODE_CONTROLE"/></TD>
	<TD><input type="checkbox" name="modeControle" <%=strSimulation%>></TD>
	<TD>&nbsp;</TD>
<TR> 
	<TD><ct:FWLabel key='EMAIL'/></TD>
	<TD><INPUT name="email" size="40" type="text" style="text-align : left;" value="<%= viewBean.getEmail() != null ? viewBean.getEmail() : "" %>"></TD>                        
 	<TD>&nbsp;</TD>
</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>