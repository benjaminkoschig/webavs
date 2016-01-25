
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran="GCF4004";
    globaz.helios.db.classifications.CGClassificationViewBean viewBean = (globaz.helios.db.classifications.CGClassificationViewBean)session.getAttribute ("viewBean");
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
    document.forms[0].elements('userAction').value="helios.classifications.classification.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="helios.classifications.classification.ajouter";
    else
        document.forms[0].elements('userAction').value="helios.classifications.classification.modifier";
    
    return state;

}
function cancel() {
  document.forms[0].elements('userAction').value="helios.classifications.classification.afficher";
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="helios.classifications.classification.supprimer";
        document.forms[0].submit();
    }
}


function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'une classification<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

  <tr>
    <td>Mandat</td>
    <input type='hidden' name='isCreateDefaultDefinitionListe' value='on'>
    <td colspan="2"> <%    			
    		if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) {%>
	      <ct:FWListSelectTag name="idMandat"
	 	  defaut=""
	 	  data="<%=globaz.helios.translation.CGListes.getMandatListe(session)%>"/>
	 	  <script>
		 	  	element = document.getElementById("idMandat");
		 	  	element.onchange=refreshPage;
	 	  </script> 	  
      <%} else {%>
			<input  type="text" readonly class="libelleLongDisabled" value="<%=viewBean.getMandatLibelle()%>" >
      <%}%>
    </td>
  </tr>


<tr>
 <td>Numéro</td>
 <td> <input name='idClassification' class='libelleDisabled' readonly value='<%=viewBean.getIdClassification()%>'></td>
</tr>
<tr>
 <td>Libellé FR</td>
 <td><input name='libelleFr' class='libelleLong' value='<%=viewBean.getLibelleFr()%>'></td>
</tr>
<tr>
 <td>Libellé DE</td>
 <td><input name='libelleDe' class='libelleLong' value='<%=viewBean.getLibelleDe()%>'></td>
</tr>
<tr>
 <td>Libellé IT</td>
 <td><input name='libelleIt' class='libelleLong' value='<%=viewBean.getLibelleIt()%>'></td>
</tr>
<tr>
 <td>Type de classification</td>
<% if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equals("add"))) {%>
	<input type = 'hidden' name='idTypeClassification' value='<%=viewBean.getIdTypeClassification()%>'>
	<td> <input name='classification' class='libelleLongDisabled' readonly value='<%=globaz.helios.translation.CodeSystem.getLibelle(session, viewBean.getIdTypeClassification())%>'></td>
<%} else {%>
	<input type = 'hidden' name='idTypeClassification'value='<%=globaz.helios.db.classifications.CGClassification.CS_TYPE_MANUEL%>'>
 	<td> <input name='classification' class='libelleLongDisabled' readonly value='<%=globaz.helios.translation.CodeSystem.getLibelle(session, globaz.helios.db.classifications.CGClassification.CS_TYPE_MANUEL)%>'></td>

<%}%> 
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