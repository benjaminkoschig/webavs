
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA4026"; %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.db.contentieux.*" %>
<%
CASecteurViewBean viewBean = (CASecteurViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {
  document.forms[0].elements('userAction').value="osiris.comptes.secteur.ajouter"
}

function upd() {
  document.forms[0].elements('userAction').value="osiris.comptes.secteur.modifier"
	document.forms[0].idTypeSection.disabled = true;
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer le type de section sélectionné! Voulez-vous continuer?")) {
        document.forms[0].elements('userAction').value="osiris.comptes.secteur.supprimer";
        document.forms[0].submit();
    }

}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.comptes.secteur.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.comptes.secteur.modifier";

    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else{
		document.forms[0].elements('userAction').value="osiris.comptes.secteur.afficher";
	}
}
function init(){}

top.document.title = "Détail d'un secteur - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un secteur<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD nowrap width="174">
              <input type="hidden" name="" value="<%=viewBean.getDescription()%>"/>
              <input type="hidden" name="" value="<%=viewBean.getIdTraduction()%>"/>
              <p>Num&eacute;ro</p>
            </TD>
            <TD width="10">&nbsp;</TD>
            <TD nowrap width="393">
              <input type="text" name="idSecteur" size="20" maxlength="15" value="<%=viewBean.getIdSecteur()%>" tabindex="1" >
            </TD>
            <TD width="118">&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap width="174" height="43">Description</TD>
            <TD width="10" height="43">&nbsp;</TD>
            <TD nowrap width="393" height="43">
              <input type="text" name="descriptionFr" size="40" maxlength="40" value="<%=viewBean.getDescription("FR")%>"  tabindex="2" >
              Fran&ccedil;ais
              <input type="text" name="descriptionDe" size="40" maxlength="40" value="<%=viewBean.getDescription("DE")%>"  tabindex="3" >
              Allemand
              <input type="text" name="descriptionIt" size="40" maxlength="40" value="<%=viewBean.getDescription("IT")%>"  tabindex="4" >
              Italien </TD>
            <TD width="118" height="43">&nbsp;</TD>
          </TR>

          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	} %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>