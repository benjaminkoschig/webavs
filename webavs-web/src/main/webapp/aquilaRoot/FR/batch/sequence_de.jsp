
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.aquila.db.batch.COSequenceViewBean"%>
<%idEcran = "GCO0034"; %>

<%
COSequenceViewBean viewBean = (COSequenceViewBean)session.getAttribute("viewBean");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {
  document.forms[0].elements('userAction').value="aquila.batch.sequence.ajouter"
}

function upd() {
	document.getElementById('idSequence').disabled=true;
	document.getElementById('libSequenceLibelle').disabled=true;
  document.forms[0].elements('userAction').value="aquila.batch.sequence.modifier"
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné ! Voulez-vous continuer ?")) {
        document.forms[0].elements('userAction').value="aquila.batch.sequence.supprimer";
        document.forms[0].submit();
    }
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add"){
        document.forms[0].elements('userAction').value="aquila.batch.sequence.ajouter";
    } else {
        document.forms[0].elements('userAction').value="aquila.batch.sequence.modifier";
    }
    return state;
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add"){
		document.forms[0].elements('userAction').value="back";
	} else{
		document.forms[0].elements('userAction').value="aquila.batch.sequence.afficher";
	}
}

function init(){}
top.document.title = "Détail d'une séquence contentieux " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'une séquence contentieux <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

		<TR>
			<TD>
				<table>
			<TR>
           		<TD nowrap >Num&eacute;ro</TD>
            	<TD width="10">&nbsp;</TD>
            	<TD  nowrap width="393">
            		<input  type="text" id="idSequence" name="idSequence" size="20" maxlength="15" value="<%=viewBean.getIdSequence()%>" tabindex="1">
            	</TD>
           </TR>
           <TR>
           		<TD nowrap >Libell&eacute;</TD>
            	<TD width="10" >&nbsp;</TD>
            	<TD nowrap width="393" >
            		<input type="text" id="libSequenceLibelle" name="libSequenceLibelle" size="20" maxlength="15" value="<%=viewBean.getLibSequenceLibelle()%>" tabindex="2">
            	</TD>
           </TR>
           <TR>
           		<TD nowrap >Code System</TD>
            	<TD width="10" >&nbsp;</TD>
            	<TD nowrap width="393" >
            		<input type="text" id="libSequence" name="libSequence" size="20" maxlength="15" value="<%=viewBean.getLibSequence()%>" tabindex="3">
            	</TD>
           </TR>
				</table>
			</TD>
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