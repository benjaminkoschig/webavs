<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="ch.globaz.al.business.constantes.*"%>
<%@page import="globaz.al.vb.prestation.ALGenerationAffilieViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/detail/header.jspf" %>


<%@page import="ch.globaz.al.business.models.prestation.DetailPrestationComplexModel"%>

<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALGenerationAffilieViewBean viewBean = (ALGenerationAffilieViewBean) session.getAttribute("viewBean"); 
	selectedIdValue=viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("GENERER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	
	bButtonValidate = objSession.hasRight(userActionUpd, FWSecureConstants.ADD);
	
	idEcran="AL0012";

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.helios.translation.CodeSystem"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/ajax_webaf.js"></script>
<script type="text/javascript" language="Javascript">

function add() {
    
}
function upd() {
   
}
function validate() {
    state = validateFields();

    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.prestation.generationAffilie.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.prestation.generationAffilie.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	
	if(methodElement.value == ADD) {
		document.forms[0].elements('selectedId').value='';
		document.forms[0].elements('userAction').value="al.dossier.dossier.chercher";
	} else {
		document.forms[0].elements('selectedId').value='';
		document.forms[0].elements('userAction').value="al.dossier.dossier.chercher";
	}
}

function del() {
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.prestation.generationAffilie.supprimer";
        document.forms[0].submit();
    }
}

function init(){
	initDebugManager();
}

function postInit(){
}

//définir cette méthode si traitement après remplissage ajax du formulaire
function callbackFillInputAjax(){
}


</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>		
			<ct:FWLabel key="AL0012_TITRE"/> 
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<tr><td>
		
	<%-- tpl:insert attribute="zoneMain" --%>
    	<table id="AL0012periodeZone" class="zone">
			
			<tr>
				<td><ct:FWLabel key="AL0012_PERIODE_GENERATION"/></td>
				<td>			
					<ct:FWCalendarTag name="periodeAGenerer"
					displayType ="month"
					value="<%=viewBean.getPeriodeAGenerer()%>" />	
				</td>
			</tr>
			
			<tr>
				<td><ct:FWLabel key="AL0012_NUM_AFFILIE"/></td>
				<td>			
					<ct:inputText name="numAffilie" styleClass="medium"/>
				</td>
			</tr>
			
			
    	</table>
    	
	<%-- /tpl:insert --%>			
</td></tr>											
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>


<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
