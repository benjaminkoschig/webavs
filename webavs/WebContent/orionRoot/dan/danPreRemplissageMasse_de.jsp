<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.orion.vb.dan.EBDanPreRemplissageMasseViewBean"%>

<%
	idEcran="GEB3001";
	EBDanPreRemplissageMasseViewBean viewBean = (EBDanPreRemplissageMasseViewBean) session.getAttribute("viewBean");                            
    userActionValue = "orion.dan.danPreRemplissageMasse.executer";
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT>
top.document.title = "Web@AVS - <ct:FWLabel key='TITRE_PRE_REMPLISSAGE'/>";
function init(){}

function postInit() {

}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_PRE_REMPLISSAGE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
						
<TR> 
	<TD><ct:FWLabel key="EMAIL"/></TD>
	<TD><INPUT name="email" size="40" type="text" value="<%= viewBean.getEmail() != null ? viewBean.getEmail() : "" %>"></TD>                        
</TR>
<TR> 
	<TD><ct:FWLabel key="TYPE_DECLARATION"/></TD>
	<TD>
		<ct:FWCodeSelectTag 
			name="typeDeclaration" 
			defaut="<%= viewBean.getTypeDeclaration()%>"
			codeType="VEDECLARAT"
			wantBlank="true"/>
	</TD>
</TR>       				
<TR> 
	<TD><ct:FWLabel key="ANNEE"/></TD>
	<TD><INPUT name="annee" id="annee" size="4" maxlength="4" type="text" style="text-align : left;" value="<%= viewBean.getAnnee() != null ? viewBean.getAnnee() : "" %>" onkeypress="return filterCharForPositivInteger(window.event);"></TD>                        
</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>