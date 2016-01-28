
<%@page import="globaz.al.vb.dossier.ALRadiationViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALRadiationViewBean viewBean = (ALRadiationViewBean) session.getAttribute("viewBean"); 
	selectedIdValue = viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	//désactive les boutons new depuis et delete cet écran
	bButtonNew = false;
	bButtonDelete = false;
	
	idEcran="AL0020";

	

	

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>


<%@page import="ch.globaz.al.business.constantes.ALCSDossier"%><script type="text/javascript">
function add() {

}
function upd() {
    document.forms[0].elements('userAction').value="al.dossier.radiation.modifier";
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.dossier.radiation.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.dossier.radiation.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.dossier.dossierMain.afficher";
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
				<ct:FWLabel key="AL0020_TITRE" /><%=viewBean.getDossierModel().getId() %>
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
			<tr>
			<td><%-- tpl:insert attribute="zoneMain" --%>
				<table id="AL0020">
					<tr>
						<td class="label">
						
	              	 	<ct:FWLabel key="AL0020_DATE_RADIE"/>
	              	  
						</td>
						<td>

	                		<ct:FWCalendarTag name="finValidite" tabindex="1" value=""
									doClientValidation="CALENDAR"/>
							<ct:inputHidden name="dossierModel.finValidite" id="finValiditeValue"/>
							<script language="JavaScript">
								document.getElementsByName('finValidite')[0].onblur=function(){fieldFormat(document.getElementsByName('finValidite')[0],'CALENDAR');document.getElementById('finValiditeValue').value = this.value;};
								
								function theTmpReturnFunctionfinValidite(y,m,d) { 
									if (window.CalendarPopup_targetInput!=null) {
										var d = new Date(y,m-1,d,0,0,0);
										window.CalendarPopup_targetInput.value = formatDate(d,window.CalendarPopup_dateFormat);
										document.getElementById('finValiditeValue').value = document.getElementsByName('finValidite')[0].value;		
									}else {
										alert('Use setReturnFunction() to define which function will get the clicked results!'); 
									}	
								}
								cal_finValidite.setReturnFunction('theTmpReturnFunctionfinValidite');
							</script>	
	                	</td>
						
					</tr>
					<tr>
						<td class="label">
							<ct:FWLabel key="AL0020_MAJ_NB_JOURS"/>			
						</td>
						<td>
							<input type="checkbox"  name="updateNbJoursFin"/>
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
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>