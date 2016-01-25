
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran="GCF4006";
    globaz.helios.db.classifications.CGDefinitionListeViewBean viewBean = (globaz.helios.db.classifications.CGDefinitionListeViewBean)session.getAttribute ("viewBean");
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
    document.forms[0].elements('userAction').value="helios.classifications.definitionListe.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="helios.classifications.definitionListe.ajouter";
    else
        document.forms[0].elements('userAction').value="helios.classifications.definitionListe.modifier";
    
    return state;

}
function cancel() {
  document.forms[0].elements('userAction').value="helios.classifications.definitionListe.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="helios.classifications.definitionListe.supprimer";
        document.forms[0].submit();
    }
}


function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail einer Definition der Liste<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>



						
					  <tr>
					    <td>Klassifikation</td>					    
					    <td>
						    <input  type="hidden" name="idDefinitionListe" value="<%=viewBean.getIdDefinitionListe()%>" >
							
				    		<%
				    		globaz.helios.db.comptes.CGExerciceComptableViewBean exerciceComptable = (globaz.helios.db.comptes.CGExerciceComptableViewBean )session.getAttribute(globaz.helios.db.interfaces.CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
				    		globaz.helios.db.classifications.CGClassification classification = viewBean.getClassification();
							String idClassification = "";
				    		if (classification!=null)
				    			idClassification = classification.getIdClassification();
							%>								    		
					    	  <ct:FWListSelectTag name="idClassification"
							 	  defaut="<%=idClassification%>"
							 	  data="<%=globaz.helios.translation.CGListes.getClassificationListe(session, exerciceComptable.getIdMandat(), null)%>"/>
					    </td>
					  </tr>

					  <tr>
					    <td>Bezeichnung FR</td>					    
					    <td>
							<input  type="text" name="libelleFr" class="libelleLong" value="<%=viewBean.getLibelleFr()%>">
					    </td>
					  </tr>

					  <tr>
					    <td>Bezeichnung DE</td>					    
					    <td>
							<input  type="text" name="libelleDe" class="libelleLong" value="<%=viewBean.getLibelleDe()%>">
					    </td>
					  </tr>

					  <tr>
					    <td>Bezeichnung IT</td>
					    <td>
							<input type="text" name="libelleIt" class="libelleLong" value="<%=viewBean.getLibelleIt()%>">
					    </td>
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