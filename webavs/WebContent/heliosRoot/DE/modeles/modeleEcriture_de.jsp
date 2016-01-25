<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.helios.db.modeles.*, globaz.helios.db.interfaces.*, globaz.globall.db.*, globaz.helios.translation.*" %>
<%
	idEcran = "GCF4016";

	CGModeleEcritureViewBean viewBean = (CGModeleEcritureViewBean)session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getIdModeleEcriture();	
	userActionValue = "helios.modeles.modeleEcriture.modifier";
%>

<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="helios.comptes.exerciceComptable.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="helios.modeles.modeleEcriture.ajouter";
    else
        document.forms[0].elements('userAction').value="helios.modeles.modeleEcriture.modifier";
    
    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="helios.modeles.modeleEcriture.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="helios.modeles.modeleEcriture.supprimer";
        document.forms[0].submit();
    }
}

function refreshPage() {
	document.forms[0].elements('_method').value == "add"
	document.forms[0].elements('userAction').value="helios.modeles.modeleEcriture.afficher";
	document.forms[0].submit();	
}


function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail des Buchungsvorlage<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>			

		<tr>
            <td>Mandant</td>
	    	<td>
	    	<%
	    		if (!viewBean.hasEnteteModele()) {
	    	%>
	    		<%=globaz.helios.parser.CGSelectBlockParser.getMandatSelectBlock(objSession, viewBean.getIdMandat(), true, null)%></td>
	    	<%
	    		} else {
	    	%>
	 	  		<input type="hidden" name="idMandat" value="<%=viewBean.getIdMandat()%>"/>
						<%
			 	  			if (viewBean.getMandat() != null) {
			 	  		%>
				 	  		<input type="text" name=="idMandatLibelle" value="<%=viewBean.getMandat().getLibelle()%>" class="libelleLongDisabled"/>
				 	  	<%
				 	  		} else {
				 	  	%>
				 	  		<input type="text" name=="idMandatLibelle" value="" class="libelleLongDisabled"/>
				 	  	<%
				 	  		}
				 	  	%>
	    	<%
	    		}
	    	%>
		</tr>
						
	  <tr>
	    <td>Nummer</td>
	    <td><input type="text" name="idModeleEcriture" class="libelleDisabled" readonly value="<%=viewBean.getIdModeleEcriture()==null?"":viewBean.getIdModeleEcriture()%>"></td>
	  </tr>
	
	  <tr>
	    <td>Bezeichnung FR</td>
	    <td>
			<input type="text" name="libelleFr" class="libelleLong" value="<%=viewBean.getLibelleFr()%>">
	    </td>
	  </tr>
	
	  <tr>
	    <td>Bezeichnung DE</td>
	    <td>
			<input type="text" name="libelleDe" class="libelleLong" value="<%=viewBean.getLibelleDe()%>">
	    </td>
	  </tr>
	  <tr>
	    <td>Bezeichnung IT</td>
	    <td>
			<input type="text" name="libelleIt" class="libelleLong" value="<%=viewBean.getLibelleIt()%>">
	    </td>
	  </tr>
	
	  <tr>
	    <td>Beleg</td>
	    <td>
			<input type="text" name="piece" class="libelleLong" value="<%=viewBean.getPiece()%>">
	    </td>
	  </tr>

     
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="CG-modele" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdModeleEcriture()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>