<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
	idEcran="CCP4015";
	actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT>
// menu 
top.document.title = "Beitr�ge - Steuerperiode"
usrAction = "phenix.divers.periodeFiscale.lister";
servlet = "phenix";
bFind = false;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Steuerperiode<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          
	  <TR> 
            <TD nowrap width="100"></TD>
            <TD nowrap colspan="2"></TD>
          </TR>
	  <TR>
            <TD nowrap width="130">DBST-Nr.</TD>
		<%
		if (request.getParameter("numIfdDefinitif")!=null){
     		%>
		<TD nowrap width="87">
		<INPUT type="text" name="fromNumIfd" value='<%=request.getParameter("numIfdDefinitif")%>' size="4" maxlength="4">
	       </TD>
		<% } else { %>
		<TD nowrap width="87">
		<INPUT type="text" name="fromNumIfd" size="4" maxlength="4">
	       </TD>
		<%}%>
		    <TD nowrap width="150"></TD>
            <TD nowrap width="120">Verf�gungsjahr</TD>
           <%
		if (request.getParameter("anneeDecision")!=null){
     		%>
		<TD nowrap width="87">
              <INPUT type="text" name="forAnneeDecisionDebut" value='<%=request.getParameter("anneeDecision")%>'size="4">
		</TD> 
		<% } else { %>
		<TD nowrap width="87">
              <INPUT type="text" name="forAnneeDecisionDebut" size="4" maxlength="4">
	    </TD>
		<%}%>


          </TR>
         
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>