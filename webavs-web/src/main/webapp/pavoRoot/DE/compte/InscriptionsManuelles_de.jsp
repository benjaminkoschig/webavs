
<!-- Sample JSP file -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.pavo.db.compte.CIInscriptionsManuellesViewBean viewBean = (globaz.pavo.db.compte.CIInscriptionsManuellesViewBean)session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdCorrection();
	userActionValue = "pavo.compte.InscriptionsManuelles.modifier";
	tableHeight = 150;
	String jspLocation2 = servletContext + mainServletPath + "Root/ti_select_par.jsp";
	
	int autoDigitAff = globaz.pavo.util.CIUtil.getAutoDigitAff(session);
	bButtonValidate = objSession.hasRight("pavo.compte.InscriptionsManuelles.afficher","ADD");
	bButtonCancel = objSession.hasRight("pavo.compte.InscriptionsManuelles.afficher","ADD");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
function add(){
	document.forms[0].elements('userAction').value ="pavo.compte.InscriptionsManuelles.ajouter";
}

function upd(){	

}

function validate(){
	
	state = validateFields();
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value ="pavo.compte.InscriptionsManuelles.ajouter";
	else
		document.forms[0].elements('userAction').value ="pavo.compte.InscriptionsManuelles.modifier";
	
	return state;
}


function cancel(){
	if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value ="pavo.compte.InscriptionsManuelles.afficher";

	
}

function del(){
	if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
		document.forms[0].elements('userAction').value ="pavo.compte.InscriptionsManuelles.supprimer";
		document.forms[0].submit();
	}
}

function init(){}	

function updateInfoAffilie(tag) {
	if(tag.select && tag.select.selectedIndex != -1){
 		document.getElementById('employeurNomPrenom').value = tag.select[tag.select.selectedIndex].nom;
 	}
}
function resetInfoAffilie() {
 	document.getElementById('employeurNomPrenom').value = '';
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail einer Manuelle Buchung<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
				<TD>Mitglied</TD>
				<TD width="5%">
				<ct:FWPopupList name="affilie" value="<%=viewBean.getAffilie()%>" jspName="<%=jspLocation2%>" autoNbrDigit="<%=autoDigitAff%>" minNbrDigit="3" size="16" onChange="updateInfoAffilie(tag);" onFailure="resetInfoAffilie()"/>
				</TD>
			
			
			<TD>
			<INPUT type="text" tabindex="-1" name="employeurNomPrenom" value="<%=viewBean.getEmployeurNomPrenom()%>" class ="disabled" readonly size="50">
			</TD>
			</TR>
			<TR>
				<TD>Beitragsjahr</TD>
				<TD colspan="2"><INPUT type="text" name="annee" value="<%=viewBean.getAnnee()%>" onkeypress="return filterCharForPositivInteger(window.event);" size="4"></TD>
			</TR>
			<TR>
				<TD>Bezeichnung</TD>
				<TD colspan="2"><INPUT type="text" name="libelle" value="<%=viewBean.getLibelle()%>" size="50"></TD>
			</TR>
			<TR>
				<TD>Betrag</TD>
				<TD colspan="2"><INPUT type="text" onchange="validateFloatNumber(this);" name="montant" value="<%=viewBean.getMontantFormate()%>" onkeypress="return filterCharForFloat(window.event);" size="15"></TD>
			</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>