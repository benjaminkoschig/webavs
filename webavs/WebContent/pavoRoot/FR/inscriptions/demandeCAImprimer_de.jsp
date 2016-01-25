<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "CCI2013";
%>
<%
CIDemandeCAImprimerViewBean viewBean = (CIDemandeCAImprimerViewBean) session.getAttribute("viewBean");
userActionValue = "pavo.inscriptions.demandeCAImprimer.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pavo.db.inscriptions.CIDemandeCAImprimerViewBean"%>
<ct:menuChange displayId="menu" menuId="CI-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CI-OnlyDetail"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

top.document.title = "Impression demande CA - " + top.location.href;

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Impression des demandes CA<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	  	<TR>
	  	<TR>
		  	<TR>
		  		<TD>N° du Journal</TD>
          		<TD><input  name="idJournal" class="numeroCourtDisabled" value="<%=request.getParameter("idJournal")%>"</TD>
          	</TR>
         <TR>
         <TR>
			<TD>Libellé du journal</TD>
			<TD align="left">
				<input type="text" name="libelle" class="libelleLongDisabled" value="<%=request.getParameter("libelle")%>"/>
			</TD>
		</TR>	 
		<TR>
			<TD>E-mail</TD>
			<TD align="left">
				<input type="text" name="eMailAddress" class="libelleLong" value="<%=viewBean.getEMailAddress()%>"/>
			</TD>
		</TR>	 
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> 
<SCRIPT>
document.forms[0].enctype = "multipart/form-data";
document.forms[0].method = "post";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>