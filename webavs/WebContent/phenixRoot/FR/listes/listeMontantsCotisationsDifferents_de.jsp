<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->

<%
	idEcran="CCP2008";
	globaz.phenix.db.listes.CPListeMontantsCotisationsDifferentsViewBean viewBean = (globaz.phenix.db.listes.CPListeMontantsCotisationsDifferentsViewBean) session.getAttribute ("viewBean");
	userActionValue ="phenix.listes.listeMontantsCotisationsDifferents.executer";
	selectedIdValue = "";
	subTableWidth = "75%";
%>

<SCRIPT language="JavaScript">
top.document.title = "Decision - Liste des décisions avec mise en compte ";
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">


<!--hide this script from non-javascript-enabled browsers

function toutOrNotTout(source) {
// Si la case "Tout le journal" est cochée les champs "De" et "à" sont désactivés

}

function init(){
//Initialisation

}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Différence entre les montants des cotisations personnelles avec l'affiliation <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD width="165">&nbsp;</TD>
            <TD width="513"></TD>
          </TR>
          <tr>
            <TD>Année de référence</TD>
            <TD>
             <INPUT type="text" name="année" class="libelleLongDisabled" maxlength="15" size="15"  value="<%=viewBean.getAnnee() %>" readonly>
			</TD>
			</tr>
          <tr>
            <TD>Trimestre</TD>
            <TD>
              <INPUT type="text" name="trimestre" class="libelleLongDisabled" value="<%=viewBean.getTrimestre()%>" readonly>
            </TD>
          </tr>
		<tr>
            <TD height="2" width="165">Adresse E-Mail</TD>
            <TD height="2" width="513">
              <input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" data-g-string="mandatory:true" value="<%=viewBean.getEMailAddress()%>">
              </TD>
          </tr>
	    <TR>
            <TD width="165">&nbsp;</TD>
            <TD width="513"></TD>
          </TR>
             <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>
<script>
// menu

//top.fr_menu.location.replace('appMenu.jsp?_optionMenu=-defaut-&changeTab=Menu');
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>