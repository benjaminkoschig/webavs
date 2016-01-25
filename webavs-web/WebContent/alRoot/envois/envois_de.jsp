<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="ch.globaz.al.business.constantes.*"%>
<%@page import="globaz.al.vb.envois.ALEnvoisViewBean"%>
<%@page import="ch.globaz.al.business.models.envoi.EnvoiComplexModel"%>
<%@page import="ch.globaz.al.business.models.envoi.EnvoiComplexModelSearch"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/detail/header.jspf" %>

<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALEnvoisViewBean viewBean = (ALEnvoisViewBean) session.getAttribute("viewBean"); 

	String detailLink = "al?userAction=al.dossier.dossierMain.afficher&selectedId=";

	idEcran="AL0102";

	bButtonNew = false;
	bButtonValidate = false;
	bButtonCancel = false;

	boolean hasDeletedRight = objSession.hasRight(userActionDel, "REMOVE");
%>
<%-- /tpl:insert --%>

<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>


<%-- tpl:insert attribute="zoneScripts" --%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.helios.translation.CodeSystem"%>
<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<script type="text/javascript" src="<%=servletContext%>/alRoot/ajax_webaf.js"></script>
<script type="text/javascript" language="Javascript">


function add() {
}
function upd() {
	
}
function validate() {
}

function cancel() {
}

function del() {
}

function delJob(idJob){
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.envois.envois.supprimer";
        document.forms[0].elements('isJob').value="true";
        document.forms[0].elements('idToDelete').value=idJob;
        document.forms[0].submit();
    }
}

function delEnvoi(idEnvoi){
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.envois.envois.supprimer";
        document.forms[0].elements('isJob').value="false";
        document.forms[0].elements('idToDelete').value=idEnvoi;
        document.forms[0].submit();
    }
}

function updateJob(newStatus){
    document.forms[0].elements('userAction').value="al.envois.envois.modifier";
    document.forms[0].elements('newStatus').value=newStatus;
    document.forms[0].submit();
}

function init(){
}

function postInit(){
	// Activer les boutons
	$(':button').removeProp('disabled');
	// Activer la combobox de sélection du status si status = généré
	<%if(viewBean.getEnvoiComplexModel().getEnvoiJobSimpleModel().getJobStatus().equals(ALCSEnvoi.STATUS_ENVOI_GENERATED)){ %>
		// activer la modification
		$('#envoiComplexSearch\\.forJobStatus').removeProp('disabled');
		// supprimer la valeur en traitement de la combobox de sélection du status
		$('#envoiComplexSearch\\.forJobStatus option[value="<%=ALCSEnvoi.STATUS_ENVOI_IN_PROGRESS%>"]').remove();
	<%}%>
}

$(function() {
	$(':button').click(function(){
		var s_idButton = this.id;
		var s_ButtonName = s_idButton.split("_")[0];
		var s_idElement = s_idButton.split("_")[1];
		if(s_ButtonName==="buttonJob"){
			delJob(s_idElement);
		}else if(s_ButtonName==="buttonEnvoi"){
			delEnvoi(s_idElement);
		}
	});
	$('#envoiJobPropertiesTable :input').css({'width':'260px'});
	$('#envoiComplexSearch\\.forJobStatus').change(function(){
		var msgUpdate = 'Voulez-vous changer le status du job en cours ?';
	    if (window.confirm(msgUpdate)){
			updateJob($('#envoiComplexSearch\\.forJobStatus option:selected').val());
	    }else{
	    	location.reload();
	    }
	});
});

//------------------------------------------------------------
//Lancement de word si data est correctement renseigné 
//------------------------------------------------------------
function launchWord(_filePath){
	try{
		var s_filepath=""+_filePath;
		if(s_filepath.length<=0){
			alert("Error, file not found !");
		}else{
			var word=null;
			try {
		  		if(word==null){
		  			word = new ActiveXObject('Word.Application');
		  		}
			    word.application.visible="true";
		  	} catch(e){
			   	word = new ActiveXObject('Word.Application');
			    word.application.visible="true";
		  	}
		    var currentDocument = word.documents.open(s_filepath);
		}
	} catch (err){
		var s_errorMessage="\r\nError Description : "+err.description;
		alert(s_errorMessage);
	}
}


</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>
			<%=objSession.getLabel("AL0102_TITRE_PRINCIPAL") %>
			<%=viewBean.getEnvoiComplexModel().getEnvoiJobSimpleModel().getId()%> - <%=viewBean.getEnvoiComplexModel().getEnvoiJobSimpleModel().getJobDescription() %>
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<tr><td>
	<INPUT type="hidden" name="isJob" value="false">
	<INPUT type="hidden" name="idToDelete" value="">
	<INPUT type="hidden" name="newStatus" value="">

	<%-- tpl:insert attribute="zoneMain" --%>
	<table id="envoiJobPropertiesTable" class="zone" style="width=100%" >
		<tr>
			<td></td>
			<td class="subtitle" colspan ="5"><ct:FWLabel key="AL0102_SOUS_TITRE_PROPRIETE"/></td>
			<td></td>
		</tr>
		<tr>
			<td colspan="7">&nbsp;</td>
		</tr>
		<tr>
			<td></td>
			<td><ct:FWLabel key="AL0102_JOB_ID"/></td>
			<td><ct:inputText name="envoiComplexModel.envoiJobSimpleModel.id"/></td>
			<td></td>
			<td><ct:FWLabel key="AL0102_JOB_USER"/></td>
			<td><ct:inputText name="envoiComplexModel.envoiJobSimpleModel.jobUser"/></td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td><ct:FWLabel key="AL0102_JOB_CREATION_DATE"/></td>
			<td><ct:inputText name="envoiComplexModel.envoiJobSimpleModel.jobDate"/></td>
			<td></td>
			<td><ct:FWLabel key="AL0102_JOB_DESCRIPTION"/></td>
			<td><ct:inputText name="envoiComplexModel.envoiJobSimpleModel.jobDescription"/></td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td><ct:FWLabel key="AL0102_JOB_TYPE"/></td>
			<td>
          	 	<ct:FWCodeSelectTag name="envoiComplexModel.envoiJobSimpleModel.jobType" 
            	defaut="<%=viewBean.getEnvoiComplexModel().getEnvoiJobSimpleModel().getJobType()%>"
				codeType="ALENVOITYP" wantBlank="false" />
			</td>	
			<td></td>
			<td><ct:FWLabel key="AL0102_JOB_STATUS"/></td>
			<td>
          	 	<ct:FWCodeSelectTag name="envoiComplexModel.envoiJobSimpleModel.jobStatus" 
            	defaut="<%=viewBean.getEnvoiComplexModel().getEnvoiJobSimpleModel().getJobStatus()%>"
				codeType="ALENVOISTS" wantBlank="false" />
			</td>	
			<td></td>
		</tr>
		<tr>
			<td colspan="7">&nbsp;</td>
		</tr>
	</table>
</td></tr>

<tr><td>&nbsp;</td></tr>											

<tr><td>
	<table id="envoiJobItemsTable" class="zone" style="width=100%; border-collapse: collapse">
		<tr style="height:50px">
			<th align="left"><ct:FWLabel key="AL0102_LIST_JOB_ID"/><%=viewBean.getEnvoiComplexModel().getEnvoiJobSimpleModel().getId()%></th>
			<th align="left"><%=viewBean.getEnvoiComplexModel().getEnvoiJobSimpleModel().getJobDescription()%></th>
			<th align="right" class="longSelect"><ct:FWLabel key="AL0102_LIST_STATUS"/>&nbsp;&nbsp;&nbsp;&nbsp; 
			<ct:FWCodeSelectTag 
				name="envoiComplexSearch.forJobStatus" codeType="ALENVOISTS" 
				defaut="<%=viewBean.getEnvoiComplexModel().getEnvoiJobSimpleModel().getJobStatus()%>"
				wantBlank="false" />
			</th>
			<%if(viewBean.getEnvoiComplexModel().getEnvoiJobSimpleModel().getJobStatus().equals(ALCSEnvoi.STATUS_ENVOI_GENERATED) && hasDeletedRight){ %>
			<th align="center"><input id="buttonJob_<%=viewBean.getEnvoiComplexModel().getEnvoiJobSimpleModel().getId()%>" type="button" value="Supprimer" style="width:110px;height:24px"></th>
			<%}else{ %>
			<%} %>
		</tr>
		<%
		String rowStyle = "";
		for(int iElement = 0; iElement<viewBean.getEnvoiComplexSearch().getSize();iElement++){
			EnvoiComplexModel currentModel = (EnvoiComplexModel)viewBean.getEnvoiComplexSearch().getSearchResults()[iElement];
			boolean condition = (iElement % 2 == 0);
			if (condition) {
				rowStyle = "row";
			} else {
				rowStyle = "rowOdd";
			}
		%>
		
		<tr class="<%=rowStyle %>">
			<td class="mtd" align="left"><ct:FWLabel key="AL0102_LIST_DOCUMENT"/><%=currentModel.getEnvoiItemSimpleModel().getId() %>
			<%if(currentModel.getEnvoiItemSimpleModel().getEnvoiError().length()>0){%>
						<img title="<%=currentModel.getEnvoiItemSimpleModel().getEnvoiError()%>"
							src="<%=request.getContextPath()%>/images/moins_lock.png" 
							width="14px"
							height="14px"
						>
	
			<%}else{%>
	
			<%}%>
			</td>
			<td class="mtd">
				<%if(ALCSEnvoi.STATUS_ENVOI_GENERATED.equals(currentModel.getEnvoiItemSimpleModel().getEnvoiStatus())){ %>
					<a href="javascript:launchWord('<%=viewBean.getFilePathDocument(currentModel.getEnvoiItemSimpleModel().getEnvoiFileName())%>')">
						<%=viewBean.getDocumentLibelle(currentModel.getEnvoiItemSimpleModel().getIdFormule())%>
					</a>
				<%}else{ %>
					<%=(viewBean.getDocumentLibelle(currentModel.getEnvoiItemSimpleModel().getIdFormule()).equals("")?currentModel.getEnvoiItemSimpleModel().getEnvoiFileName():viewBean.getDocumentLibelle(currentModel.getEnvoiItemSimpleModel().getIdFormule()))%>
				<%} %>
			</td>
			<td class="mtd"><ct:FWLabel key="AL0102_LIST_DOSSIER"/>&nbsp; 
			<a href="<%=detailLink+currentModel.getEnvoiItemSimpleModel().getIdExternalLink() %>">
			<%=currentModel.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1()%>
			&nbsp;<%=currentModel.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2()%>
			</a>
				<img
					src="<%=request.getContextPath()%>/images/external.png" 
				>
			</td>
			<%if(currentModel.getEnvoiJobSimpleModel().getJobStatus().equals(ALCSEnvoi.STATUS_ENVOI_GENERATED) && hasDeletedRight){ %>
			<td class="mtd" align="center"><input id="buttonEnvoi_<%=currentModel.getEnvoiItemSimpleModel().getId() %>" type="button" value="Supprimer" style="font-size: 10px;"></td>
			<%}else{ %>
			<%} %>
		</tr>
		<%
		}
		%>
	</table>
</td></tr>											

<tr><td>
	<%-- /tpl:insert --%>			
</td></tr>											
<%@ include file="/theme/detail/bodyButtons.jspf" %>
	<%-- tpl:insert attribute="zoneButtons" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
	<%-- tpl:insert attribute="zoneEndPage" --%>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>
<ct:menuChange displayId="options" menuId="empty-detail"/>				
	<%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
