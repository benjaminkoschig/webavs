<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.hercule.db.groupement.CEGroupeViewBean"%>

<%
	idEcran ="CCE0007";
	CEGroupeViewBean viewBean = (CEGroupeViewBean)session.getAttribute ("viewBean");
	
	String typeAction = request.getParameter("_method");
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";
function add() {
	document.forms[0].elements('userAction').value="hercule.groupement.groupe.ajouter";
}

function upd() {}

function validate() {
	 state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="hercule.groupement.groupe.ajouter";
    else
        document.forms[0].elements('userAction').value="hercule.groupement.groupe.modifier";
    
    return state;
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
 		document.forms[0].elements('userAction').value="hercule.groupement.groupe.afficher";
}

function del() {
	if (window.confirm("<%=objSession.getLabel("CONFIRM_SUPPRESSION_OBJECT")%>")) {
		document.forms[0].elements('userAction').value="hercule.groupement.groupe.supprimer";
		document.forms[0].submit();
	}
}
function init() {
}

</SCRIPT> 

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="GROUPE_DETAIL"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

		<ct:inputHidden name="idGroupe"/>
		<ct:inputHidden name="id"/>
		<tr>
			<td width="250"><ct:FWLabel key="GROUPE_LIBELLE"/></td>
			<td width="250"><input type="text" size="30" maxlength="20" name="libelleGroupe" value="<%=viewBean.getLibelleGroupe()%>" /></td>
			<TD>&nbsp;</TD>
		</tr>
		<tr>
			<td width="250"><ct:FWLabel key="GROUPE_COUVERTURE"/></td>
			<td width="250"><input type ="text" name="anneeCouvertureMinimal" value="<%=viewBean.getAnneeCouvertureMinimal()%>" size ="4" maxlength="4" onkeypress="return filterCharForPositivInteger(window.event);"></td>
			<TD>&nbsp;</TD>
		</tr>
					
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<% if(!"add".equals(typeAction)) { %>
<ct:menuChange displayId="options" menuId="CE-OptionsGroupe" showTab="options" checkAdd="false">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdGroupe()%>"/>
	<ct:menuSetAllParams key="idGroupe" value="<%=viewBean.getIdGroupe()%>"/>
</ct:menuChange>
<% }%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>