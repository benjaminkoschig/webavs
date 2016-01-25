
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran="GCF4010";
    globaz.helios.db.comptes.CGLibelleStandardViewBean viewBean = (globaz.helios.db.comptes.CGLibelleStandardViewBean)session.getAttribute ("viewBean");

%>


<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="helios.comptes.libelleStandard.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="helios.comptes.libelleStandard.ajouter";
    else
        document.forms[0].elements('userAction').value="helios.comptes.libelleStandard.modifier";
    
    return state;

}
function cancel() {
	document.forms[0].elements('userAction').value="helios.comptes.libelleStandard.chercher";
}

function del() {
    if (window.confirm(" Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="helios.comptes.libelleStandard.supprimer";
        document.forms[0].submit();
    }
}


function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un libelle standard<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
<tr>
<TD>Pour le mandat</TD>						
<td></td>
<TD>
 <ct:FWListSelectTag name="idMandat"
	 defaut="<%=viewBean.getIdMandat()%>"
	 data="<%=globaz.helios.translation.CGListes.getMandatListe(session)%>"/>
</TD>
</tr>						

<tr>
 <td>Acronyme</td>
 <td></td>
 <td>  
<% if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) { %>
	<input name='idLibelleStandard' class='libelle' maxlength="5" size="8" value=''> 
<%} else {%>
 	<input name='idLibelleStandard' class='disabled' readonly size="8" value='<%=viewBean.getIdLibelleStandard()%>'> 
 <%}%>
 </td>
 
 
</tr>
<tr>
 <td>Libelle</td>
 <td>Fr </td>
 <td><input name='libelleFR' class='libelleLong' value='<%=viewBean.getLibelleFR()%>'></td>
</tr>
<tr>
 <td></td>
 <td>De</td>
 <td><input name='libelleDE' class='libelleLong' value='<%=viewBean.getLibelleDE()%>'></td>
</tr>
<tr>
 <td></td>
 <td>It</td>
 <td><input name='libelleIT' class='libelleLong' value='<%=viewBean.getLibelleIT()%>'></td>
</tr>

     
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT> 
<%  }  %>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>