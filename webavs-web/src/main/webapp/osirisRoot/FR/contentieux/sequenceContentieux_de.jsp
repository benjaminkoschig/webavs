
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA4015"; %>
<%@ page import="globaz.osiris.db.contentieux.*" %>
<% 
globaz.osiris.db.contentieux.CASequenceContentieuxViewBean viewBean = (globaz.osiris.db.contentieux.CASequenceContentieuxViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
selectedIdValue = viewBean.getIdSequenceContentieux();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {  
    document.forms[0].elements('userAction').value="osiris.contentieux.sequenceContentieux.ajouter";
}
function upd() {
  document.forms[0].elements('userAction').value="osiris.contentieux.sequenceContentieux.modifier";
  document.forms[0].idSequenceContentieux.disabled = true;
}
  
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.contentieux.sequenceContentieux.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.contentieux.sequenceContentieux.modifier";
    
    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.contentieux.sequenceContentieux.afficher";
}
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer la séquence contentieux sélectionné! Voulez-vous continuer?")) {
        document.forms[0].elements('userAction').value="osiris.contentieux.sequenceContentieux.supprimer";
        document.forms[0].submit();
    }
}
function init(){
}

top.document.title = "Contentieux - détail d'une séquence contentieux- " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'une séquence contentieux<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD nowrap width="174"> 
              <p>Num&eacute;ro</p>
            </TD>
            <TD width="10">&nbsp;</TD>
            <TD nowrap width="393"> 
              <INPUT type="text" name="idSequenceContentieux" size="20" maxlength="15" value="<%=viewBean.getIdSequenceContentieux()%>">
            </TD>
            <TD width="118">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="174" height="43">Description</TD>
            <TD width="10" height="43">&nbsp;</TD>
            <TD nowrap width="393" height="43"> 
              <input type="text" name="descriptionFr" size="40" maxlength="40" value="<%=viewBean.getDescription("FR")%>">
              Fran&ccedil;ais 
              <input type="text" name="descriptionDe" size="40" maxlength="40" value="<%=viewBean.getDescription("DE")%>">
              Allemand 
              <input type="text" name="descriptionIt" size="40" maxlength="40" value="<%=viewBean.getDescription("IT")%>">
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
<ct:menuChange displayId="options" menuId="CA-SequenceContentieux" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdSequenceContentieux()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>