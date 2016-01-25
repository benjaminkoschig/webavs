<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.al.vb.echeances.ALEcheancesViewBean"%>
<%@page import="ch.globaz.al.business.constantes.ALConstEcheances"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALEcheancesViewBean viewBean = (ALEcheancesViewBean) session.getAttribute("viewBean"); 
	selectedIdValue = viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	//désactive les boutons new depuis et delete cet écran
	bButtonNew = false;
	bButtonDelete = false;
	bButtonValidate = objSession.hasRight(userActionUpd, FWSecureConstants.UPDATE);
	
	idEcran="AL0018";

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<script type="text/javascript">

var typeListImpression = <%=ALConstEcheances.LISTE_IMPRESSION %>;
$(document).ready(function(){
	$('label[for=copieAllocPourDossierBeneficiaire],[name="copieAllocPourDossierBeneficiaire"]').hide();
	
	$('[name="typeAvis"]').change(function(event) {
	        if ($(this).is(":checked")) {
	        	if($(this).val()==typeListImpression){
	        		$('label[for=copieAllocPourDossierBeneficiaire],[name="copieAllocPourDossierBeneficiaire"]').show();
	        	}else{
	        		$('label[for=copieAllocPourDossierBeneficiaire],[name="copieAllocPourDossierBeneficiaire"]').hide();
	        	}
	        }
	 });
});

function add() {
	

}
function upd() {
    document.forms[0].elements('userAction').value="al.echeances.echeances.modifier";
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.echeances.echeances.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.echeances.echeances.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.echeances.echeances.afficher";
	} else {
        document.forms[0].elements('userAction').value="al.dossier.dossier.chercher";
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
				<ct:FWLabel key="AL0018_TITRE" />
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
		<tr>
			<td><%-- tpl:insert attribute="zoneMain" --%>
				<table id="AL0018dateZone" class="zone">
					<tr>
						 <td class="subtitle" colspan="2"><ct:FWLabel key="AL0018_ENTETE_SELECTION"/></td>
					</tr>  
					<tr>
	              	 		<td> <ct:FWLabel key="AL0018_SELECTION_DATE"/>
							</td>
							<td>
	                			<ct:FWCalendarTag name="echeanceAu" tabindex="1" value=""
									doClientValidation="CALENDAR"/>
								<ct:inputHidden name="date" id="dateValue"/>
							
								<script language="JavaScript">
									document.getElementsByName('echeanceAu')[0].onblur=function(){fieldFormat(document.getElementsByName('echeanceAu')[0],'CALENDAR');document.getElementById('dateValue').value = this.value;};
								
									function theTmpReturnFunctionecheanceAu(y,m,d) { 
										if (window.CalendarPopup_targetInput!=null) {
											var d = new Date(y,m-1,d,0,0,0);
											window.CalendarPopup_targetInput.value = formatDate(d,window.CalendarPopup_dateFormat);
											document.getElementById('dateValue').value = document.getElementsByName('echeanceAu')[0].value;		
										}else {
											alert('Use setReturnFunction() to define which function will get the clicked results!'); 
										}	
									}
									cal_echeanceAu.setReturnFunction('theTmpReturnFunctionecheanceAu');
								</script>	
	                		</td>

						</tr>
					<tr>
						 <td>  		               	
	                    	<input type="checkbox" checked="checked" name="adiExclu"/>
	                 		<ct:FWLabel key="AL0018_TRAITEMENT_DOSSIER_ADI"/>             		
	                 	</td> 
					</tr> 
				</table>
		
				<table id="AL0018traitementZone" class="zone">
					<tr>
					 	<td class="subtitle" colspan="2"><ct:FWLabel key="AL0018_ENTETE_TRAITEMENT"/></td>
					</tr>
					<tr>
						<td><input tabindex="2" type="radio" name="typeAvis" value="<%=ALConstEcheances.LISTE_PROVISOIRE %>"/></td>
	                	<td><ct:FWLabel key="AL0018_TRAITEMENT_LISTE"/></td>			
					</tr>
					<tr>
						<td><input tabindex="3" type="radio" name="typeAvis" value="<%=ALConstEcheances.LISTE_IMPRESSION %>"/></td>
	                	<td><ct:FWLabel key="AL0018_TRAITEMENT_IMPRESSION"/></td>
					</tr>
					<tr>
						<td></td>
	                	<td><input type="checkbox" checked="checked" name="copieAllocPourDossierBeneficiaire"/>
	                 		<label for="copieAllocPourDossierBeneficiaire"><ct:FWLabel key="AL0018_TRAITEMENT_DOSSIER_BENEF_COPIE_ALLOC"/></label>
	                 	</td>
					</tr>
				</table>
			</td>
		</tr>
	
			<%-- /tpl:insert --%>
	
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>	
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
