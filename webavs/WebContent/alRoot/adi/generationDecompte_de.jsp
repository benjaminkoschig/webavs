<%@page import="globaz.al.vb.adi.ALGenerationDecompteViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JANumberFormatter" %>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>


<%@page import="ch.globaz.al.business.constantes.ALCSTiers"%>
<%@page import="ch.globaz.al.business.constantes.ALCSProcessus"%>
<%@page import="ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseModel"%>
<%@page import="ch.globaz.al.business.models.processus.ProcessusPeriodiqueModel"%>
<%@page import="ch.globaz.al.business.constantes.ALCSPrestation"%><script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALGenerationDecompteViewBean viewBean = (ALGenerationDecompteViewBean) session.getAttribute("viewBean"); 
	selectedIdValue = viewBean.getId();
	
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("GENERER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");

	
	if(ALCSPrestation.ETAT_CO.equals(viewBean.getDecompteAdiModel().getEtatDecompte())){
		bButtonUpdate = false;
	}
	
	idEcran="AL0027";

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/dossier/generation.js"/></script>
<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript">
function add() {

}
function upd() {
    document.forms[0].elements('userAction').value="al.adi.decompteAdi.modifier";

}
function validate() {
 
	state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.adi.decompteAdi.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.adi.decompteAdi.modifier";
    return state;
}

function cancel() {

	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.dossier.dossierAdi.afficher";
		document.forms[0].elements('selectedId').value='<%=JavascriptEncoder.getInstance().encode(viewBean.getDecompteAdiModel().getIdDossier()) %>';
	} else {
		document.forms[0].elements('userAction').value="al.adi.decompteAdi.afficher";
        
	}
}
function del() {	
}

function init(){
	
}

function postInit(){
	
}



</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>
				<%=(viewBean.getDecompteAdiModel().isNew())?objSession.getLabel("AL0024_TITRE_NEW"):objSession.getLabel("AL0024_TITRE")+viewBean.getDecompteAdiModel().getId()%>		
				(<ct:FWLabel key="AL0004_TITRE"/><%=viewBean.getDecompteAdiModel().getIdDossier() %>)
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
			<tr>
			<td><%-- tpl:insert attribute="zoneMain" --%>
		
            <table id="AL0015optionsZone" class="zone">
				<tr>
					<td class="label subtitle" colspan="2"><ct:FWLabel key="AL0015_GENERATION_OPTIONS"/></td>
				</tr>
               	<tr>
               		<td>
               		<table style="width:70%;">
						<tr>
							<td><ct:FWLabel key="AL0015_PERIODE_TRAITEMENT"/></td>
							<td>
							<ct:inputText tabindex="1" name="periodeTraitement" disabled="true" styleClass="date"/>
							</td>		
						</tr>		
						<tr>
							<td><ct:FWLabel key="AL0015_NUM_FACTURE"/></td>
							<td>						
							<ct:inputText name="noFacture" id="noFactureValue" tabindex="2"/>
							</td>		
						</tr>
                		<tr>
                	 		<td><ct:FWLabel key="AL0024_GENERATION_OPTION_PROCESSUS"/></td>
	            			<td>
	            			<select size="5" name="numProcessus" id="numProcessus">
		            		<%        	
		            		for(int i=0;i<viewBean.getProcessusSelectableList().size();i++){
		            			ProcessusPeriodiqueModel currentProcessus = (ProcessusPeriodiqueModel)viewBean.getProcessusSelectableList().get(i);
		            		%>
		            			<option <%=viewBean.getNumProcessus().equals(currentProcessus.getIdProcessusPeriodique())?"selected='selected' ":""%> <%=currentProcessus.getIsPartiel()?"":"class='main'"%> value='<%=currentProcessus.getIsPartiel()?currentProcessus.getId():"0"%>'><%=viewBean.getDescriptionProcessusSelectable(i)%></option>
		            		<%
		            			}
		            		%>
		            			<option <%=viewBean.getNumProcessus().equals("0")?"selected='selected' ":""%> value='0'><ct:FWLabel key="AL0017_RECAP_DETACHER_PROCESSUS"/></option>
							</select>
	            			</td>
               	 		</tr>
                
                	</table>
               		</td>
               		<td>
               		<table style="width:50%;">
						<tr>
							<td><ct:FWLabel key="AL0015_NUM_FACTURE_DISPO"/></td>
							<td>
							<%if(viewBean.getSearchRecapsExistantesAffilie().getSize()>0){ %>					
								<select size="5" name="noFactureSelect" id="noFactureSelection">
			                    <%for (int i=0;i<viewBean.getSearchRecapsExistantesAffilie().getSize();i++){ 
			                   		RecapitulatifEntrepriseModel currentRecap = (RecapitulatifEntrepriseModel)viewBean.getSearchRecapsExistantesAffilie().getSearchResults()[i];
			                    	%>
			                    	<option al_process="noFactureSelection-<%=i%>" value="<%=currentRecap.getNumeroFacture()%>"><%=currentRecap.getNumeroFacture()%></option>
			                    
			                    <%} %>
			                   
			            		<select>
	            				<%for (int i=0;i<viewBean.getSearchRecapsExistantesAffilie().getSize();i++){ 
	                   			RecapitulatifEntrepriseModel currentRecap = (RecapitulatifEntrepriseModel)viewBean.getSearchRecapsExistantesAffilie().getSearchResults()[i];
	                    		%>
	                    		<input type="hidden" value="<%=currentRecap.getIdProcessusPeriodique()%>" id="noFactureSelection-<%=i%>"/>
	                    	   <%} %>
						   <%} %>
						
							</td>
						</tr>
					</table>
               		</td>
               		</tr>
               	</table>
				
			<%-- /tpl:insert --%>
			</td>
				
			</tr>								
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>
<ct:menuChange displayId="options" menuId="decompte-detail" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="id" checkAdd="no" value="<%=viewBean.getDecompteAdiModel().getId()%>"  />
	<ct:menuSetAllParams key="selectedId" checkAdd="no" value="<%=viewBean.getDecompteAdiModel().getId()%>"  />	
	<ct:menuSetAllParams key="idDossier" checkAdd="no" value="<%=viewBean.getDecompteAdiModel().getIdDossier()%>"  />		
</ct:menuChange>	
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>