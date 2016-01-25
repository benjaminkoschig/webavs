<%@page import="globaz.al.vb.parametres.ALTauxMonnaieEtrangereViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%	
	ALTauxMonnaieEtrangereViewBean viewBean=(ALTauxMonnaieEtrangereViewBean) session.getAttribute("viewBean");
	selectedIdValue=viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	//désactive le bouton new depuis cet écran
	
	bButtonNew = false;
	bButtonCancel = true;
	bButtonValidate = true;
	
	
	idEcran="AL0041";
	userActionValue="al.parametres.tauxMonnaieEtrangere.modifier";
%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>


<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JAUtil"%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="globaz.jade.client.util.JadeNumericUtil"%>
<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>

<script type="text/javascript">

function add() {
    document.forms[0].elements('userAction').value="al.parametres.tauxMonnaieEtrangere.ajouter";
}
function upd() {
	//on affiche les liens retirer qui sont cachés si on est pas en mode modifier
	for (key in document.getElementsByTagName('a'))
  	{
		if(document.getElementsByTagName('a')[key].className=='removeLink')
			document.getElementsByTagName('a')[key].style.display='inline';
  	}
    document.forms[0].elements('userAction').value="al.parametres.tauxMonnaieEtrangere.modifier";
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.parametres.tauxMonnaieEtrangere.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.parametres.tauxMonnaieEtrangere.modifier";
    return state;
}
function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);

		document.forms[0].elements('userAction').value="al.parametres.tauxMonnaieEtrangere.afficher";
}

function del() {
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.parametres.tauxMonnnaieEtrangere.supprimer";
        document.forms[0].submit();
    }
}

function init(){
}


function postInit(){
}
</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>
				<%=(viewBean.getTauxMonnaieEtrangere().isNew())?objSession.getLabel("AL0041_TITRE_TAUX_NEW"):objSession.getLabel("AL0041_TITRE_TAUX")+ " " + viewBean.getTauxMonnaieEtrangere().getId()%>		
				
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
			<tr>
				<td>
						<%-- tpl:insert attribute="zoneMain" --%>
              		<table>
              			<tr>
              				<td class="label"><ct:FWLabel key="AL0041_TITRE_TYPE_MONNAIE"/></td>
              				<td>
              					<ct:select name="tauxMonnaieEtrangere.typeMonnaie" tabindex="2">	
													<ct:optionsCodesSystems csFamille="PYMONNAIE"/>	
															</ct:select>	
              				</td>    				
              				
              			</tr>
              			<tr>
              				<td class="label"><ct:FWLabel key="AL0041_TITRE_DEBUT_VALIDITE"/>
              				</td>
              				<td>
              					<ct:inputText name="tauxMonnaieEtrangere.debutTaux" disabled="disabled"/>
              				
              				</td>
              				<td class="label"><ct:FWLabel key="AL0041_TITRE_FIN_VALIDITE"/>
              				</td>
              				<td>
              					<ct:inputText name="tauxMonnaieEtrangere.finTaux" disabled="disabled"/> 
              				</td>
              			</tr>	
              			<tr>
              				<td class="label"><ct:FWLabel key="AL0041_TITRE_TAUX_MONNAIE"/>
              				</td>
              				<td>
              					<ct:inputText name="tauxMonnaieEtrangere.tauxMonnaie"  disabled="disabled"/> 
              				</td>
              			</tr>
              		</table>
				</td>
			</tr>


				<%-- tpl:insert attribute="zoneButtons" --%>
				<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>

<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
