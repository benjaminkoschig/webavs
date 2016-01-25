<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
idEcran="CCP4019";
bButtonFind = false;
bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CP-OnlyDetail"/>
<SCRIPT>
// menu 
top.document.title = "Cotisation - Décisions non comptabilisées"
usrAction = "phenix.principale.decisionNonComptabilisee.lister";
servlet = "phenix";
bFind=true;
</SCRIPT>
<%
/*							
globaz.phenix.db.principale.CPDecisionNonComptabiliseeViewBean viewBean = (globaz.phenix.db.principale.CPEnteteViewBean)session.getAttribute ("viewBean");							
*/
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Décisions non comptabilisées<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD nowrap width="*">Ce point de menu permet de visualiser les décisions qui ont été partiellement comptabilisées. Ce sont les décisions dont la mise à jour:</TD>
          </TR>
          <TR>  
            <TD nowrap width="*">&nbsp;&nbsp;&nbsp;&nbsp;du CI</TD>
          </TR>
          <TR>
            <TD nowrap width="*">&nbsp;&nbsp;&nbsp;&nbsp;ou des montants périodiques de l'affiliation</TD>
          </TR>
          <TR>
            <TD nowrap width="*">&nbsp;&nbsp;&nbsp;&nbsp;ou des communications fiscales</TD>
          </TR>
          <TR>
            <TD nowrap width="*"> a échoué.</TD>
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