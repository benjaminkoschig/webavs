<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CFA0008";%>
<%
	//contrôle des droits
	bButtonNew = objSession.hasRight(userActionNew, "ADD");
	
	globaz.musca.db.facturation.FAOrdreAttribuerViewBean viewBean = (globaz.musca.db.facturation.FAOrdreAttribuerViewBean)session.getAttribute ("viewBean");
	//selectedIdValue = viewBean.getIdPassage();
  	userActionValue = "musca.facturation.ordreAttribuer.modifier";
%>
<%-- /tpl:put --%>
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
top.document.title = "Facturation - Attribution d'un ordre"
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="musca.facturation.ordreAttribuer.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="musca.facturation.ordreAttribuer.ajouter";
    else
        document.forms[0].elements('userAction').value="musca.facturation.ordreAttribuer.modifier";
    
    return state;

}
function cancel() {
 
 if (document.forms[0].elements('_method').value == "add")
    document.forms[0].elements('userAction').value="back";
 else
    document.forms[0].elements('userAction').value="musca.facturation.ordreAttribuer.afficher"
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="musca.facturation.ordreAttribuer.supprimer";
        document.forms[0].submit();
    }
}

function updateLibelle(tag){
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("descriptionRubrique").value = element.libelle;
	}
}
function updateLibelleOrdre(tag){
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.forms[0].elements('libelle').value = element.libelle;
		document.forms[0].elements('numCaisse').value = element.numCaisse;
		document.getElementById('idOrdreRegroupement').value = element.idOrdreRegroupement;
	}
}
function clearIdOrdreRegroupement(){
	document.forms[0].elements('libelle').value = "";
	document.forms[0].elements('numCaisse').value = "";
	document.getElementById('idOrdreRegroupement').value = "";
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Zuteilung eines Auftrags und eines Zusammenschlusses<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
		 
       	<TR>
        	<TD nowrap width="145">Rubrik</TD>
            <TD nowrap>
				<%String jspLocation = servletContext + "/muscaRoot/" + languePage + "/facturation/rubrique_select.jsp";%>
				<input type="hidden" name="idRubrique" value="<%=viewBean.getIdRubrique()%>">
				<ct:FWPopupList name="idExterneRubrique" 
								onFailure="document.mainForm.idRubrique.value='';" 
								onChange="if (tag.select) document.mainForm.idRubrique.value = tag.select[tag.select.selectedIndex].idRubrique;updateLibelle(tag);" 
								validateOnChange="true" 
								value="<%=viewBean.getIdExterneRubrique()%>" 
								className="libelle" jspName="<%=jspLocation%>" 
								minNbrDigit="1" 
								forceSelection="true"/>&nbsp;&nbsp;
				<INPUT name="descriptionRubrique" type="text" value="<%=viewBean.getLibelleRubrique()%>" class="libelleLongDisabled" readonly style="width : 12.15cm" tabindex="-1">
			</TD>
		</TR>
	   	<TR>
            <TD nowrap width="200"></TD>
            <TD nowrap width="547">&nbsp;</TD>
            <TD width="50"></TD>
       	</TR>
	  	<TR>
        	<TD nowrap width="145">Auftrag</TD>
            <TD nowrap>
				<%String jspLocationOrdre = servletContext + "/muscaRoot/ordre_select.jsp";%>
				<INPUT type="hidden" name="idOrdreRegroupement" value='<%=viewBean.getIdOrdreRegroupement()%>'>
				<ct:FWPopupList name="numOrdreRegroupement"
								onFailure="clearIdOrdreRegroupement();"
								onChange="updateLibelleOrdre(tag);"  
								validateOnChange="false" 
								value="<%=viewBean.getNumOrdreRegroupement()%>" 
								className="libelle" jspName="<%=jspLocationOrdre%>" 
								minNbrDigit="0"
								autoNbrDigit="1"/>
			</TD>
		</TR>
		<TR>
            <TD nowrap width="140">Gruppenbeschreibung</TD>
            <TD nowrap width="300">
            	<INPUT name="libelle" type="text" value="<%=viewBean.getLibelle()%>" class="libelleLongDisabled" style="width : 12.15cm" tabindex="-1"></TD>
   	    </TR>
	  	<TR>
            <TD nowrap width="200"></TD>
            <TD nowrap width="547">&nbsp;</TD>
            <TD width="50"></TD>
       	</TR>
	  	<TR>
			<TD nowrap width="140">Kassen-Nr.</TD>
			<TD nowrap width="300">
				<INPUT name="numCaisse" type="text" value="<%=viewBean.getNumCaisse()%>" class="libelleDisabled"></TD>
	  	</TR>
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
<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="FA-OptionVide"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>