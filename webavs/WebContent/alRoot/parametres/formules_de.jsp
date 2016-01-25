<%@page import="ch.globaz.al.business.constantes.ALCSDossier"%>
<%@page import="globaz.al.vb.parametres.ALFormulesViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%	
	ALFormulesViewBean viewBean=(ALFormulesViewBean) session.getAttribute("viewBean");
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	

	//désactive le bouton new depuis cet écran
	bButtonNew = false;
	bButtonCancel = true;
	bButtonValidate = true;

	// Importer fichier xml
	String currentFormAction= request.getContextPath()+ mainServletPath+"Root/parametres/formules_upload.jsp?selectedId="+viewBean.getId();

	idEcran="AL0106";
	userActionValue="al.parametres.formules.modifier";
	
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
    document.forms[0].elements('userAction').value="al.parametres.formules.ajouter";
}

function validate() {
	// Pour prise en compte d'un paramètre obligatoire ....
	$('#envoiTemplate\\.formuleList\\.formule\\.csLangue').removeProp('disabled');
    if (document.forms[0].elements('_method').value == "add"){
        document.forms[0].elements('userAction').value="al.parametres.formules.ajouter";
    } else { 
        document.forms[0].elements('userAction').value="al.parametres.formules.modifier";
    }
    return true;
}

function cancel() {
	document.forms[0].elements('userAction').value="al.parametres.formules.chercher";
}

function del() {
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.parametres.formules.supprimer";
        document.forms[0].submit();
    }
}

function upd() {
	// keep not modifiable field not modifiable
	$('select[name="envoiTemplate.formuleList.definitionformule.csDocument"]').prop('disabled','disabled');
	$('select[name="envoiTemplate.formuleList.formule.csLangue"]').prop('disabled','disabled');
}

function upload(){

	$('form[name=mainForm]').attr('action','<%=currentFormAction%>');
    document.forms[0].encoding = "multipart/form-data";
	document.forms[0].method = "post";
	document.getElementById("fileName").disabled=false;
	document.getElementById("fileName").readOnly=false;
	action(COMMIT);

}

function download(){
	// valeur du chemin dans filePathInput input text
    document.forms[0].elements('action').value="download";
    document.forms[0].elements('userAction').value="al.parametres.formules.exporter";
    document.forms[0].submit();
}

function browse(){
	
}

function init(){
}


function postInit(){
	$('#filePathDownloadButton').removeProp('disabled');
	$('#envoiTemplate\\.formuleList\\.formule\\.csLangue').prop('disabled','disabled');
	$('#envoiTemplate\\.envoiTemplateSimpleModel\\.codeEtatDossier option[value="<%=ALCSDossier.ETAT_RADIE%>"]').remove();
	$('#envoiTemplate\\.envoiTemplateSimpleModel\\.codeEtatDossier option[value="<%=ALCSDossier.ETAT_ACTIF%>"]').remove();
	$('#envoiTemplate\\.envoiTemplateSimpleModel\\.codeEtatDossier option[value="<%=ALCSDossier.ETAT_REFUSE%>"]').remove();
}

$(function() {
	$('#filePathUploadButton').click(function(){
		upload();
	});
	$('#filePathBrowseButton').click(function(){
		browse();
	});
	$('#filePathDownloadButton').click(function(){
		download();
	});
	
});

</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>
				<%=(viewBeanIsNew?objSession.getLabel("AL0106_TITRE_PRINCIPAL_CREATION"):objSession.getLabel("AL0106_TITRE_PRINCIPAL_MODIFICATION"))%>
				<%=(viewBeanIsNew?"":" "+viewBean.getEnvoiTemplate().getEnvoiTemplateSimpleModel().getIdFormule())%>		
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:insert attribute="zoneMain" --%>
			<tr>
				<INPUT type="hidden" name="envoiTemplate.formuleList.definitionformule.csManuAuto" value="6300001">
				<td>
					<div id="envoiTemplateDiv" style="float:left;width=100%">
						<table id="envoiTemplateTable" class="zone" style="width=100%">
							<tr><td colspan="8">&nbsp;</td></tr>
							<tr>
								<td></td>
								<td class="subtitle"><ct:FWLabel key="AL0106_DETAIL"/></td>
								<td></td>
								<td><ct:FWLabel key="AL0106_DOCUMENT"/></td>
								<td colspan="2"><ct:FWCodeSelectTag 
									name="envoiTemplate.formuleList.definitionformule.csDocument" codeType="ALENVOIDOC"
									defaut="<%=(!viewBeanIsNew?viewBean.getEnvoiTemplate().getFormuleList().getDefinitionformule().getCsDocument():new String())%>" wantBlank="false" />
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td><ct:FWLabel key="AL0106_LANGUAGE"/></td>
								<td><ct:FWCodeSelectTag 
									name="envoiTemplate.formuleList.formule.csLangue" codeType="PYLANGUE"
									defaut="<%=(!viewBeanIsNew?viewBean.getEnvoiTemplate().getFormuleList().getFormule().getCsLangue():\"503001\")%>" wantBlank="false" />
								</td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td><ct:FWLabel key="AL0106_DESCRIPTION"/></td>
								<td><input disabled="disabled" name="envoiTemplate.formuleList.formule.libelleDocument" type="text" size="60" data-g-string="sizeMax:40,autoFit:true"
									value="<%=(!viewBeanIsNew?viewBean.getEnvoiTemplate().getFormuleList().getFormule().getLibelleDocument():"")%>" />
								</td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td></td>
								<td></td>
								<td></td>
								<td><ct:FWLabel key="AL0106_ETAT_DOSSIER_AF"/></td>
								<td><ct:FWCodeSelectTag
									name="envoiTemplate.envoiTemplateSimpleModel.codeEtatDossier" codeType="ALDOSETAT"
									defaut="<%=(!viewBeanIsNew?viewBean.getEnvoiTemplate().getEnvoiTemplateSimpleModel().getCodeEtatDossier():new String())%>" wantBlank="true" />
								</td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
							<%if(viewBeanIsNew==false){%>
							<tr><td colspan="8">&nbsp;</td></tr>
							<tr>
								<td></td>
								<td class="subtitle"><ct:FWLabel key="AL0106_FICHIER_WORD"/></td>
								<td></td>
								<td><ct:FWLabel key="AL0106_NOUVEAU_MODELE"/></td>
								<td>
								<ct:inputHidden name="action"/>					
								<ct:inputHidden name="fileInput"/>					
								<input type="file" size="40" id="fileName" name="fileName">
								</td>
								<td align="right">
								<input type="button" id="filePathUploadButton" value="<%=objSession.getLabel("AL0106_BOUTON_UPLOAD")%>" title="Transmission du fichier au serveur">
								<input type="button" id="filePathDownloadButton" value="<%=objSession.getLabel("AL0106_BOUTON_TEST") %>" title="Test du fichier présent sur le serveur">
								</td>
								<td></td>
								<td></td>
							</tr>
							<%}%>
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

<%-- tpl:insert attribute="zoneEndPage" --%>
<%if(viewBeanIsNew==false){%>
<ct:menuChange displayId="options" menuId="formule-detail" showTab="options" checkAdd="no" >		
	<ct:menuSetAllParams checkAdd="no" key="searchModel.forIdFormule" value="<%=viewBean.getEnvoiTemplate().getFormuleList().getFormule().getId()%>"/>
</ct:menuChange>
<%} %>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
