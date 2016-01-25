<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<style>
<!--
	.btnCtrl {
		display : none;
	}
-->
</style>
<%
	idEcran ="GTI00XX";
    globaz.pyxis.db.alternate.TIAllAdressesViewBean viewBean = (globaz.pyxis.db.alternate.TIAllAdressesViewBean)session.getAttribute ("viewBean");
    selectedIdValue ="";
   
    bButtonNew = false;
 bButtonUpdate = false;
 bButtonDelete = false;
 bButtonValidate = false;
 bButtonCancel = false;
    actionNew += "&idTiers="+viewBean.getIdTiers();
%>
<SCRIPT language="JavaScript">
</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers -->


top.document.title = "Tiers - Adresses"
function add() {
    document.forms[0].elements('userAction').value="pyxis.adressecourrier.avoirAdresse.ajouter"
   
	


}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pyxis.adressecourrier.avoirAdresse.ajouter";
    else
        document.forms[0].elements('userAction').value="pyxis.adressecourrier.avoirAdresse.modifier";
	


    return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add"){
 	
 	//document.forms[0].elements('userAction').value="back";
 	top.fr_appicons.icon_back.click();
 	
  }
 else
  document.forms[0].elements('userAction').value="pyxis.adressecourrier.avoirAdresse.afficher";
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="pyxis.adressecourrier.avoirAdresse.supprimer";
        document.forms[0].submit();
    }
}


function init(){
	try {btnUpd.style.display="block";} catch (e){}
	try {btnDel.style.display="block";} catch (e){}
	try {btnVal.style.display="block";} catch (e){}
	try {btnCan.style.display="block";} catch (e){}
}

function postInit(){
	
}


/*
*/
// stop hiding -->
</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Tiers - Adresses
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>

<tr>
<td width="100%">

<table id="innerTable" border ="1" width="100%" height="100%">
<tr>


<td  id="maintd" width="50%"  >
<iframe id="innerFrame" scrolling="YES"  style="border : solid 1px black; width:100%;" height="100%" src="<%=request.getContextPath()%>/pyxis?userAction=pyxis.alternate.allAdresses.adressesTiers">
	</iframe>	
</td>
<td id="adrframe" width="50%">
	<iframe id="innerFrame2" scrolling="YES"  style="border : solid 1px black; width:100%;" height="100%" src="<%=request.getContextPath()%>/pyxis?userAction=pyxis.alternate.allAdresses.adressesPaiementTiers">
	</iframe>
</td>
</tr>
</table>
 




<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%>
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT> 
<%  }  %>
	
<script>
	document.getElementById('innerTable').style.setExpression("height","document.body.clientHeight-150");
	document.getElementsByTagName('table')[0].style.setExpression("height","document.body.clientHeight-35");
</script>
	

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>