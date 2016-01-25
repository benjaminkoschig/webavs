<%@page import="globaz.al.vb.envois.ALParametresViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%	
	ALParametresViewBean viewBean=(ALParametresViewBean) session.getAttribute("viewBean");
	
	//désactive le bouton new depuis cet écran
	bButtonNew = false;
	bButtonDelete = true;
	bButtonCancel = true;
	bButtonUpdate = true;
	bButtonValidate = true;
	
	
	idEcran="AL0104";
	userActionValue="al.envois.parametres.modifier";
	
	boolean viewBeanIsNew = "add".equals(request.getParameter("_method"));

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
<script type="text/javascript">

function add() {
    document.forms[0].elements('userAction').value="al.envois.parametres.ajouter";
}

function validate() {
    if (document.forms[0].elements('_method').value == "add"){
        document.forms[0].elements('userAction').value="al.envois.parametres.ajouter";
    } else { 
        document.forms[0].elements('userAction').value="al.envois.parametres.modifier";
    }
    return true;
}

function cancel() {
	document.forms[0].elements('userAction').value="al.envois.parametres.chercher";
}

function del() {
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.envois.parametres.supprimer";
        document.forms[0].submit();
    }
}

function upd() {
	// keep not modifiable field not modifiable
}

function init(){
}


function postInit(){
}

$(function() {
});

</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>
				<%=(viewBeanIsNew?objSession.getLabel("AL0104_TITRE_PRINCIPAL_CREATION"):objSession.getLabel("AL0104_TITRE_PRINCIPAL_MODIFICATION"))%>		
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:insert attribute="zoneMain" --%>
			<tr>
				<td>
					<div id="parametreDiv" style="float:left;width=100%">
						<table id="parametreTable" class="zone" style="width=100%">
							<tr><td colspan="8">&nbsp;</td></tr>
							<tr>
								<td></td>
								<td class="subtitle"><ct:FWLabel key="AL0104_SOUS_TITRE_DETAIL"/></td>
								<td></td>
								<td><ct:FWLabel key="AL0104_PARAMETRE_CATEGORIE"/></td>
								<td>
								<ct:FWCodeSelectTag 
									name="envoiParametres.csTypeParametre" codeType="ALENVOIPAR" 
									defaut="<%=viewBean.getEnvoiParametres().getCsTypeParametre()%>"
									wantBlank="false" />
								</td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td><ct:FWLabel key="AL0104_PARAMETRE_VALEUR"/></td>
								<td>
									<input tabindex="2" class="clearable" type="text" name="envoiParametres.valeurParametre"
									type="text" size="80" data-g-string="sizeMax:255,autoFit:false" 
									value="<%=(!viewBeanIsNew?viewBean.getEnvoiParametres().getValeurParametre():"")%>">
								</td>
								<td></td>
								<td></td>
							</tr>
							<tr><td colspan="8">&nbsp;</td></tr>
						</table>
					</div>
				</td>
			</tr>
<%-- /tpl:put --%>

<%-- tpl:insert attribute="zoneButtons" --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>

<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
