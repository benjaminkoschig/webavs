<%@page import="globaz.al.vb.traitement.ALRecapImpressionViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALRecapImpressionViewBean viewBean = (ALRecapImpressionViewBean) session.getAttribute("viewBean"); 
	selectedIdValue = viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	//désactive les boutons new depuis et delete cet écran
	bButtonNew = false;
	bButtonDelete = false;
	
	idEcran="AL0019";

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<%@page import="ch.globaz.al.business.constantes.ALConstPrestations"%>
<%@page import="ch.globaz.al.business.constantes.ALConstEcheances"%>
<%@page import="ch.globaz.al.business.constantes.ALConstRecap"%><script type="text/javascript">


function add() {
	

}
function upd() {
    document.forms[0].elements('userAction').value="al.traitement.recapImpression.modifier";
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.traitement.recapImpression.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.traitement.recapImpression.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.traitement.recapImpression.afficher";
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
				<ct:FWLabel key="AL0019_TITRE" />
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
		<tr>
			<td><%-- tpl:insert attribute="zoneMain" --%>
				<table id="AL0019dateZone" class="zone">
					<tr>
						<td class="subtitle" colspan="2"><ct:FWLabel key="AL0019_ENTETE_SELECTION"/>
					 	</td>
					</tr> 
					<tr>
	              	 	
	              	 	<td class="label"> <ct:FWLabel key="AL0019_SELECTION_DATE"/>
						</td>
						<td>              			
	                			<ct:FWCalendarTag name="date" tabindex="1"
									displayType ="month"
									value="<%=viewBean.getDate()%>" />
	                	</td>
	                	<td class="label"> <ct:FWLabel key="AL0019_IMPRESSION_DATE"/>
						</td>
						<td>              			 
	                			<ct:FWCalendarTag name="dateImpression" tabindex="2"
									displayType ="month"
									value="<%=viewBean.getDateImpression()%>" />	
	                	</td>
					</tr>
						 
				</table>
				
				<table id="AL0019typeZone" class="zone">
					<tr>
						<td class="subtitle" colspan="2"><ct:FWLabel key="AL0019_ENTETE_SELECTION_TYPE"/>
					 	</td>
					</tr> 
					<tr>
	              	 	
	              	 	<td><input type="radio" tabindex="3" name="typeRecap" value="<%=ALConstPrestations.TYPE_COT_PAR %>"/></td>
	              	 	<td><ct:FWLabel key="AL0019_SELECTION_PARITAIRE"/></td>
	              	</tr>
	              	<tr>
						<td><input type="radio" tabindex="4" name="typeRecap" value="<%=ALConstPrestations.TYPE_COT_PERS %>"/></td>
	              	 	<td><ct:FWLabel key="AL0019_SELECTION_PERSO"/></td>
	              	</tr>	
	              	<tr>
	              	 	<td><input type="radio" tabindex="5" name="typeRecap" value="<%=ALConstPrestations.TYPE_DIRECT %>"/></td>
	              	 	<td><ct:FWLabel key="AL0019_SELECTION_DIRECT"/></td>
	              	
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
