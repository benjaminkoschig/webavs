
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<script language="JavaScript">
top.document.title = "Konfiguration der Dienste für die Rücksendung der MZR";
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
usrAction = "hermes.gestion.configurationService.lister";
bFind = true;
</SCRIPT>
<%
idEcran="GAZ4001";
actionNew  +=	(request.getParameter("colonneSelection")==null)?"":"&colonneSelection="+request.getParameter("colonneSelection");
rememberSearchCriterias = true;
%>
<ct:menuChange displayId="options" menuId="HE-ConfigurationService">
</ct:menuChange>
<ct:menuChange displayId="menu" menuId="HE-MenuPrincipal" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Suche der Dienste<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <tr>
            <td>Dienstname&nbsp;</td>
            <td>
              <input type="text" name="likeServiceName" value="">
            </td> 
          </tr>
          <tr>          
            <td>Eigener Hinweis&nbsp;</td>
            <td> 
          		<input type="text" name="likeReferenceInterne" value="">
            </td>
          </tr>
          <tr> 
          	<td>E-Mail Adresse&nbsp;</td>
    	   	<td><input type="text" name="likeEmailAdresse" value="" ></td>
    	   	<td>&nbsp;</td>
          </tr>
          <tr> 
            <td>&nbsp;</td>
            <td>
              &nbsp;<input type="hidden" name="from">
            </td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>