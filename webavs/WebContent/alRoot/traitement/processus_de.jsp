<%@page import="ch.globaz.musca.business.models.PassageModuleComplexModel"%>
<%@page import="globaz.al.vb.traitement.ALProcessusViewBean"%>
<%@page import="ch.globaz.musca.business.models.PassageModuleComplexModel"%>
<%@page import="ch.globaz.musca.business.models.PassageModuleComplexSearchModel"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
    ALProcessusViewBean viewBean = (ALProcessusViewBean) session.getAttribute("viewBean"); 
	selectedIdValue = viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	//désactive le bouton new depuis cet écran
	bButtonNew = false;
	bButtonDelete = false;
	
	idEcran="AL0035";
	userActionValue = "al.traitement.processus.modifier";

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<%@page import="ch.globaz.al.business.constantes.ALCSDossier"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JAUtil"%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="globaz.jade.client.util.JadeNumericUtil"%>
<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<script type="text/javascript">
function add() {
    document.forms[0].elements('userAction').value="al.traitement.processus.ajouter";
}
function upd() {
	
    document.forms[0].elements('userAction').value="al.traitement.processus.modifier";
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.traitement.processus.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.traitement.processus.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.traitement.processusGestion.afficher";
	} else {
        document.forms[0].elements('userAction').value="al.traitement.processus.afficher";
	}
}
function del() {
	var msgDelete = '<%= JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.traitement.processus.supprimer";
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
			<%=objSession.getLabel("AL0051_TITRE")+objSession.getCodeLibelle(viewBean.getTemplateTraitementComplexModel().getConfigProcessusModel().getBusinessProcessus()) +" - "+viewBean.getTemplateTraitementComplexModel().getPeriodeAFModel().getDatePeriode()%>
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
			<tr><td>
			<%-- tpl:insert attribute="zoneMain" --%>
			
			
			<table id="AL0051Processus" class="zone">
             	<tr>
                	<td class="label subtitle" colspan="4">	
						<ct:FWLabel key="AL0051_TITRE_PROCESSUS"/>		
                    </td>
                </tr>
	            <tr>
	                  <td class="label"><ct:FWLabel key="AL0051_PROCESSUS_NUMERO"/></td>
	                  <td>
	                  <ct:inputText name="templateTraitementComplexModel.processusPeriodiqueModel.id" styleClass="readOnly" readonly="readonly"/>
	                  
	                  </td>
	                  <td class="label"><ct:FWLabel key="AL0051_PROCESSUS_NOM"/></td>
	                  <td>
	                  	<input value='<%=objSession.getCodeLibelle(viewBean.getTemplateTraitementComplexModel().getConfigProcessusModel().getBusinessProcessus()) %>' 
	                		class="readOnly extra" readonly="readonly" type="text" name="nomprocessus" disabled="disabled"/>
	                  	
	                  </td>  
	              </tr>
	              <tr>
	               	  <td class="label"><ct:FWLabel key="AL0051_PROCESSUS_PERIODE"/></td>
	                  <td><ct:inputText name="templateTraitementComplexModel.periodeAFModel.datePeriode" styleClass="readOnly" readonly="readonly"/></td>
	                  <td class="label"></td>
	                  <td></td>
	               </tr>
	           </table>
	           <table id="AL0051Passage" class="zone">
	           <tr>
                	<td class="label subtitle" colspan="2">	
						<ct:FWLabel key="AL0051_TITRE_PASSAGE"/>		
                    </td>
                </tr>
	           	   <tr>
	                  <td class="label"><ct:FWLabel key="AL0051_PROCESSUS_PASSAGE_LIE"/></td>
	                  <td>
	                  <input value='<%=JadeStringUtil.isBlankOrZero(viewBean.getPassageModuleComplexModel().getIdPassage())?objSession.getLabel("AL0051_PROCESSUS_PASSAGE_AUCUN"):viewBean.getPassageModuleComplexModel().getIdPassage()+" - "+viewBean.getPassageModuleComplexModel().getLibellePassage()%>' 
	                		class="readOnly extra" readonly="readonly" type="text" name="descriptionPassage" disabled="disabled"/>   		
	                  </td>
	              </tr>
	              <tr>
	              <td class="label"><ct:FWLabel key="AL0051_PROCESSUS_PASSAGE_CHOIX"/></td>
	              <td>
	              
	      
	              	<select size="5" name="templateTraitementComplexModel.processusPeriodiqueModel.idPassageFactu">
	            		<option value="0"><ct:FWLabel key="AL0051_PROCESSUS_PASSAGE_AUCUN"/></option>
	            		<%
	            			
	            			for(int i=0;i<viewBean.getSearchPassageModuleComplexModel().getSize();i++){
	            			PassageModuleComplexModel currentPassage = (PassageModuleComplexModel)viewBean.getSearchPassageModuleComplexModel().getSearchResults()[i];
	            		%>
	            			<option value="<%=currentPassage.getId()%>"><%=currentPassage.getId()+" - "+currentPassage.getDateFacturation()+" - "+currentPassage.getLibellePassage()+" - "+currentPassage.getDatePeriode()%></option>
	            		<%
	            			}
	            		%>
					</select> 
					</td>

	              </tr>
          		</table>
    		
			<%-- /tpl:insert --%>
			</td></tr>								
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>	

<ct:menuChange displayId="options" menuId="processus-detail" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="recapSearchModel.forNumProcessusLie" checkAdd="no" value="<%=viewBean.getId()%>"  />
</ct:menuChange>

<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
