<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CFA0016";%>
<%
	//contrôle des droits
	bButtonNew = objSession.hasRight(userActionNew, "ADD");
	
    globaz.musca.db.facturation.FAOrdreRegroupementViewBean viewBean = (globaz.musca.db.facturation.FAOrdreRegroupementViewBean)session.getAttribute ("viewBean");
	userActionValue = "musca.facturation.ordreRegroupement.modifier";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/CodeSystemPopup.js"></SCRIPT>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">

top.document.title = "Fakturierung - Ordre d'impression et regroupement"
<!--hide this script from non-javascript-enabled browsers

var ajouter = false;

function add() {
    document.forms[0].elements('userAction').value="musca.facturation.ordreRegroupement.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="musca.facturation.ordreRegroupement.ajouter";
    else
        document.forms[0].elements('userAction').value="musca.facturation.ordreRegroupement.modifier";
    
    return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
    document.forms[0].elements('userAction').value="back";
 else
    document.forms[0].elements('userAction').value="musca.facturation.ordreRegroupement.afficher"
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="musca.facturation.ordreRegroupement.supprimer";
        document.forms[0].submit();
    }
}

function updateLibelle(tag){
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.forms[0].elements('libelle').value = element.libelle;
		document.getElementById('idOrdreRegroupement').value = element.idOrdreRegroupement;
	}
}
function clearIdOrdreRegroup(){
	document.forms[0].elements('libelle').value = "";
	document.getElementById('idOrdreRegroupement').value = "";
}
function init(){}

/*
*/
// stop hiding -->

</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail eines Auftrags und einer Ausdruckzusammenfassung<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
   	   <TR>
            <TD nowrap width="140">Kassen-Nr.</TD>
            <TD nowrap width="300">
            	<INPUT name="numCaisse" type="text" value="<%=viewBean.getNumCaisse()%>" class="numeroCourt" style="width : 2cm" tabindex="-1"></TD>
            <TD width="50"></TD>
   	   </TR> 
	   <TR>
        	<TD nowrap width="145">Auftrag</TD>
            <%--TD nowrap>
				<%String jspLocation = servletContext + "/muscaRoot/ordre_select.jsp";%>
				<ct:FWPopupList name="ordreRegroupement"
								onFailure="clearIdOrdreRegroup();"
								onChange="updateLibelle(tag);"  
								validateOnChange="false" 
								value="<%=viewBean.getOrdreRegroupement()%>" 
								className="libelle" jspName="<%=jspLocation%>" 
								minNbrDigit="0"
								autoNbrDigit="1"/>
				<INPUT type="hidden" name="idOrdreRegroupement " value='<%=viewBean.getIdOrdreRegroupement()%>'>
			</TD--%>
			<TD nowrap width="300">
            	<INPUT name="ordreRegroupement" type="text" value="<%=viewBean.getOrdreRegroupement()%>" class="numeroCourt" style="width : 2cm" tabindex="-1"></TD>
            <TD width="50"></TD>
		</TR>
		<TR>
            <TD nowrap width="200"></TD>
            <TD nowrap width="547">&nbsp;</TD>
            <TD width="50"></TD>
       	</TR>
		<TR>
            <TD nowrap width="140">Gruppenbeschreibung</TD>
        </TR>
        <TR>
            <TD nowrap width="140">FR</TD>
            <TD nowrap width="300">
            	<INPUT name="libelleFR" type="text" value="<%=viewBean.getLibelleFR()%>" class="libelleLong" style="width : 12.15cm" tabindex="-1"></TD>
            <TD width="50"></TD>
   	    </TR>
   	    <TR>
            <TD nowrap width="140">DE</TD>
            <TD nowrap width="300">
            	<INPUT name="libelleDE" type="text" value="<%=viewBean.getLibelleDE()%>" class="libelleLong" style="width : 12.15cm" tabindex="-1"></TD>
            <TD width="50"></TD>
   	    </TR>
   	    <TR>
            <TD nowrap width="140">IT</TD>
            <TD nowrap width="300">
            	<INPUT name="libelleIT" type="text" value="<%=viewBean.getLibelleIT()%>" class="libelleLong" style="width : 12.15cm" tabindex="-1"></TD>
            <TD width="50"></TD>
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
<script>	

//Pour agrandir le taglib concerné et l'aligner
document.getElementById("idTypeFacturation").style.width=120;
</script>
<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="FA-OptionVide"/>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>