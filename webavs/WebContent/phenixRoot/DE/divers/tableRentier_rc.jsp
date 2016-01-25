<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran="CCP4013";%> 
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT>
// menu 
top.document.title = "Beiträge - Rentner Tabelle"
usrAction = "phenix.divers.tableRentier.lister";
servlet = "phenix";
bFind = false;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Rentner Tabelle <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          
	  <TR> 
            <TD nowrap width="100"></TD>
            <TD nowrap colspan="2"></TD>
          </TR>
	  <TR>
            <TD nowrap width="130">Jahr</TD>
            <TD nowrap colspan="2"><INPUT type="text" name="fromAnneeRentier" size="5" maxlength="4"></TD>
            <TD nowrap colspan="2" width="150"></TD>
            <TD nowrap width="120">Einkommen</TD>
            <TD nowrap colspan="2" width="87"><INPUT type="text" name="fromRevenuRentier" class="montant"></TD>
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