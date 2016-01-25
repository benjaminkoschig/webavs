<%@page import="globaz.al.vb.adi.ALImprimerDecompteViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALImprimerDecompteViewBean viewBean = (ALImprimerDecompteViewBean) session.getAttribute("viewBean"); 
	selectedIdValue = viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	
	//désactive les boutons new depuis et delete cet écran
	bButtonNew = false;
	bButtonDelete = false;
	bButtonValidate =false;
	bButtonUpdate = false;
	idEcran="AL0026";
%>
	<%-- /tpl:insert --%>
	<%-- tpl:insert attribute="zoneBusiness" --%>
	<%-- /tpl:insert --%>
	<%@ include file="/theme/detail/javascripts.jspf" %>
	<%-- tpl:insert attribute="zoneScripts" --%>
	

<%@page import="globaz.jade.client.util.JadeNumericUtil"%>

<%@page import="ch.globaz.al.business.constantes.ALConstAdiDecomptes"%><script type="text/javascript">

function add() {	
}

function upd() { 
}

function validate() {

	
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.adi.decompteAdiImpression.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.adi.decompteAdiImpression.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.adi.decompteAdiImpression.afficher";
	} else {
		
        document.forms[0].elements('userAction').value="al.adi.decompteAdiImpression.chercher";
	}
}

function del() {	
}

function init(){
}

function postInit(){
}


function updateGedValue(inGed){
	 document.forms[0].elements('printGed').value=inGed;
	
}

</script>
	
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>
				<ct:FWLabel key="AL0026_TITRE" />
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<tr>
<td>
	<ct:inputHidden name="printGed"/>
	<table id="AL0026typeZone" class="zone">
	<tr>
		<td class="subtitle" colspan="2"><ct:FWLabel key="AL0026_ENTETE_SELECTION_TYPE"/></td>
	</tr> 
	<tr>
	 	<td><input type="radio" tabindex="3" name="typeDecompte" value="<%=ALConstAdiDecomptes.ADI_DECOMPTE_ALL %>" checked="checked"/></td>
	 	<td><ct:FWLabel key="AL0026_IMPR_DECOMPTE_ADI_TOUS"/></td>
	</tr>
	<tr>
		<td><input type="radio" tabindex="4" name="typeDecompte" value="<%=ALConstAdiDecomptes.ADI_DECOMPTE_GLOBAL %>"/></td>
	 	<td><ct:FWLabel key="AL0026_IMPR_DECOMPTE_ADI_GLOBAL"/></td>
	</tr>	
	<tr>
	 	<td><input type="radio" tabindex="5" name="typeDecompte" value="<%=ALConstAdiDecomptes.ADI_DECOMPTE_DETAILLE %>"/></td>
	 	<td><ct:FWLabel key="AL0026_IMPR_DECOMPTE_ADI_DETAIL"/></td>
	 </tr>
	 
	</table>
</td>
</tr>



			<%-- /tpl:insert --%>
	
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<input class="btnCtrl" id="btnPrint" type="button" value="Aperçu" onclick="updateGedValue('false');if(validate()) action(COMMIT);">
				<input class="btnCtrl" id="btnSend" type="button" value="Ins. GED" onclick="updateGedValue('true');if(validate()) action(COMMIT);">
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>	
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>

	
	
