<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.hercule.db.traitement.CETraitementViewBean"%>
<%@page import="globaz.hercule.db.traitement.CERattrapageNumViewBean"%>

<%
	idEcran="CCE3005";
	CERattrapageNumViewBean viewBean = (CERattrapageNumViewBean) session.getAttribute("viewBean");                            
    userActionValue="hercule.traitement.rattrapageNum.executer";
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT>
top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";
function init(){}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="MENU_RATTRAPAGE_NO_CONTROLE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
						
<TR> 
	<TD><ct:FWLabel key="EMAIL"/></TD>
	<TD><INPUT name="email" size="40" type="text" style="text-align : left;" value="<%= viewBean.getEmail() != null ? viewBean.getEmail() : "" %>"></TD>                        
 	<TD>&nbsp;</TD>
</TR>
<TR> 
   	<TD><ct:FWLabel key="FROM_ANNEE"/></TD>
   	<TD><INPUT name="fromAnnee" maxlength="4" size="4" type="text" style="text-align : left;" value="<%=viewBean.getFromAnnee() != null ? viewBean.getFromAnnee() : "" %>" onkeypress="return filterCharForPositivInteger(window.event);" /></TD>                   
 	<TD>&nbsp;</TD>
</TR>
<TR>
 	<TD><ct:FWLabel key="UNTIL_ANNEE"/></TD>
   	<TD><INPUT name="untilAnnee" maxlength="4" size="4" type="text" style="text-align : left;" value="<%=viewBean.getUntilAnnee() != null ? viewBean.getUntilAnnee() : "" %>" onkeypress="return filterCharForPositivInteger(window.event);" /></TD>                   
 	<TD>&nbsp;</TD>
</TR>       				

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>