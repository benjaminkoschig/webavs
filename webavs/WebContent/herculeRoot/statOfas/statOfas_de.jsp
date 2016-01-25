<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.hercule.db.statOfas.CEStatOfasViewBean"%>

<%
	idEcran="CCE2011";
	CEStatOfasViewBean viewBean = (CEStatOfasViewBean)session.getAttribute("viewBean");                            
    userActionValue="hercule.statOfas.statOfas.executer";
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT>
top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";
function init(){}

function postInit() {
	var myDate = new Date();
	<% if(JadeStringUtil.isEmpty(viewBean.getAnnee())) {%>
	$("#annee").val(myDate.getFullYear()-1);
	<%}%>
}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_IMPRESSION_STAT_OFAS"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
						
<TR> 
	<TD><ct:FWLabel key="EMAIL"/></TD>
	<TD><INPUT name="email" size="40" type="text" style="text-align : left;" value="<%= viewBean.getEmail() != null ? viewBean.getEmail() : "" %>"></TD>                        
 	<TD>&nbsp;</TD>
</TR>
<TR> 
   	<TD><ct:FWLabel key="ANNEE"/></TD>
   	<TD><INPUT name="annee" id="annee" maxlength="4" size="4" type="text" style="text-align : left;" value="<%= viewBean.getAnnee() != null ? viewBean.getAnnee() : "" %>" onkeypress="return filterCharForPositivInteger(window.event);" /></TD>                   
 	<TD>&nbsp;</TD>
</TR>       				

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>