<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.naos.db.attestation.AFAttestationPersonnelleViewBean"%>

<%
	idEcran="CAF2016";
	AFAttestationPersonnelleViewBean viewBean = (AFAttestationPersonnelleViewBean) session.getAttribute("viewBean");                            
    userActionValue="naos.attestation.attestationPersonnelle.executer";
    int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);
    String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
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
	<% if(JadeStringUtil.isEmpty(viewBean.getAnnee())) {%>
	$("#annee").val(myDate.getFullYear()-1);
	<%}%>
}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key='TITRE_ATTESTATION_PERSONNELLE'/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	
<TR> 
   	<TD><ct:FWLabel key='ANNEE'/></TD>
   	<TD><INPUT name="annee" id="annee" maxlength="4" size="4" type="text" style="text-align : left;" value="<%= viewBean.getAnnee() != null ? viewBean.getAnnee() : "" %>" onkeypress="return filterCharForPositivInteger(window.event);" /></TD>                   
 	<TD>&nbsp;</TD>
</TR>   			
<tr>
	<td><ct:FWLabel key='AFFILIES'/></td>
	<td><ct:FWPopupList name="fromAffilie" 
				value='<%=viewBean.getFromAffilie() != null ? viewBean.getFromAffilie() : ""%>'
				className="libelle" 
				jspName="<%=jspLocation%>" 
				autoNbrDigit="<%=autoDigiAff%>" 
				size="15"
				minNbrDigit="3"/>
	&nbsp;&nbsp;<ct:FWLabel key='FROM_TO'/>&nbsp;&nbsp;
	     <ct:FWPopupList name="toAffilie" 
				value='<%=viewBean.getToAffilie() != null ? viewBean.getToAffilie() : ""%>'
				className="libelle" 
				jspName="<%=jspLocation%>" 
				autoNbrDigit="<%=autoDigiAff%>" 
				size="15"
				minNbrDigit="3"/>
	</td>
	<td></td>
</tr>	
<TR>
     <TD width="279" height="20">Date d'édition</TD>
	<TD><ct:FWCalendarTag name="dateEnvoiMasse" 
    	value='<%=globaz.globall.util.JACalendar.todayJJsMMsAAAA()%>'
	doClientValidation="CALENDAR"/></TD>
</TR>		
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