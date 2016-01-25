 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ page import="globaz.naos.db.controleEmployeur.*"%>
<%idEcran="CFA2006";%>
<%
	//Récupération des beans
	AFImprimerLettreLibreViewBean viewBean = (AFImprimerLettreLibreViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "naos.controleEmployeur.imprimerlettrelibre.executer";

%>
<SCRIPT language="JavaScript">
top.document.title = "Naos - Impression de la lettre libre"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<SCRIPT language="JavaScript" src="/webavs/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/webavs/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/webavs/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/webavs/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/webavs/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="/webavs/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function init()
{
}

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Imprimer une lettre libre<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          	<TR>
            	<TD>Numéro</TD>
            	<TD><INPUT name="controleId" type="text" value="<%=viewBean.getControleId()%>" class="numeroCourtDisabled" readonly></TD>
         	</TR>
         	<TR>
            	<td width="23%" height="2">Adresse E-Mail</td>
            	<td height="2"> 
              		<input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getSession().getUserEMail()%>">*
            	</td>
          	</TR>
			<TR>
				<TD>
					Date d'envoi
				</TD>
				<TD>
					<ct:FWCalendarTag name="dateEnvoi" value="<%=viewBean.getDateEnvoi()%>" />
				</TD>
			</TR>
          	<TR>
            	<td width="23%" height="2">Texte libre : </td>
            	<td height="2"> 
              		<textarea name="textelibre" cols="85" rows="10" ></textarea>
            	</td>
          	</TR>
			<TR><TD>&nbsp;</TD></TR><TR><TD>&nbsp;</TD></TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>
<ct:menuChange displayId="menu" menuId="AFMenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="AFMenuVide"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>