<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.al.vb.decision.ALDecisionListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>

<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALDecisionListViewBean viewBean = (ALDecisionListViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getId();
	btnUpdLabel = objSession.getLabel("MODIFIER");
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	//d�sactive le bouton new depuis cet �cran
	bButtonNew = false;
	bButtonDelete = false;
	
	boolean hasCreateRight = objSession.hasRight("al.decision.decisionList", FWSecureConstants.ADD);
	bButtonValidate = hasCreateRight;

	idEcran="AL0028";

	
	
%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<%@page import="globaz.al.vb.decision.ALDecisionListViewBean"%><script type="text/javascript">


function add() {	
}

function upd() { 
}

function validate() {
	   state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
    	 document.forms[0].elements('userAction').value="al.decision.decisionList.ajouter";
 

    else 
        document.forms[0].elements('userAction').value="al.decision.decisionList.modifier";
    return state; 
}

function cancel() {
 	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.decision.decisionList.afficher";
	} 
 	else {
		
        document.forms[0].elements('userAction').value="al.decision.decisionList.chercher";
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
				<ct:FWLabel key="AL0028_TITRE" />
			<%-- /tpl:insert --%>

<%@ include file="/theme/detail/bodyStart2.jspf" %>
<tr>
 		<td>
 			<div style="background-image:url('<%=request.getContextPath()%>/images/summary_bg.png');
 						background-repeat:repeat-x">
				<table width="100%" align="center" 
						style="border-collapse:collapse; font-size: 11px; border:2px solid #226194;">
						<tr>
							<td style="padding:5px;">
			                   	<h4>Description</h4><br>
			                   	<ul>
									<li>" Date p�riode de " correspond � la date � laquelle le dernier processus m�tier a �t� ex�cut�;
									<li>" Date p�riode � " correspond � la date jusqu'� laquelle le test doit �tre effectu�;
									<li>Les boutons radios " P�riodicit� " permettent de choisir quels dossiers doivent �tre trait�s (dossiers mensuels ou trimestriels) :
									<ul>
										<li>Le traitement <b>mensuel</b> retourne les dossiers comme tel : Pmt direct -> tous les dossier / Pmt indirect -> affili� avec p�riodicit� mensuelle;
										<li>Le traitement <b>trimestriel</b> retourne les dossiers comme tel : Pmt direct -> aucun / Pmt indirect -> affili� avec p�riodicit� trimestrielle.
									</ul>
								</ul>
								Les dossiers ressortis dans ce traitement cumulent les crit�res suivants :
								<ul>
								<li>Une journalisation a �t� cr��e durant la p�riode d�finie par l'utilisateur (d�cision mise en ged)</li>
								<li>La date de d�but de validit� du dossier est &lt;/= � la date " p�riode de " pour la periodicit� <b>mensuel</b>, et  &lt;/= � la date de fin du dernier <b>trimestriel</b> avant la date " p�riode de "</li>
								<li>Aucune prestation en SA n'existe pour une p�riode &lt;/= au mois de g�n�ration de la date " p�riode de "</li>
								</ul>
 
								 Attention, les dossiers radi�s ne sont pas g�r�s par ce traitement.
								<br>&nbsp;
							</td>
						</tr>
				</table>
 			</div>
 		</td>
 	</tr>
 	
 	<tr><td>&nbsp;<td></tr>
	<tr>
			<td><%-- tpl:insert attribute="zoneMain" --%>
				<table id="AL0028zone" class="zone">
				
				<tr>
	              	 		<td> <ct:FWLabel key="AL0028_EMAIL"/>
							</td>
							<td>
							<input tabindex="1"  type="text"  name=email  value="<%= viewBean.getEmail() != null ? viewBean.getEmail() : "" %>"/>
	                		</td>
	                		
						</tr>
					
						
					<tr>
						<td> <ct:FWLabel key="AL0028_DATE_DEBUT"/></td>
						<td>
	                		<input tabindex="4" class="clearable" type="text"
							name="datePeriodeDe" value="<%= viewBean.getDatePeriodeDe() != null ? viewBean.getDatePeriodeDe() : "" %>"
							data-g-calendar="mandatory:false" />	
	                	</td>
	                	
				</tr>
				<tr>
					<td> <ct:FWLabel key="AL0028_DATE_FIN"/></td>
					<td>
                		<input tabindex="4" class="clearable" type="text"
						name="datePeriodeA" value="<%= viewBean.getDatePeriodeA() != null ? viewBean.getDatePeriodeA() : "" %>"
						data-g-calendar="mandatory:false" />	
                	</td>	                	
				</tr>
				<tr>
					<td>
						<input type="radio" name="choixPeriodicite" value="men" checked> <ct:FWLabel key="AL0028_PERIODICITE_M"/> <br>
						<input type="radio" name="choixPeriodicite" value="tri"> <ct:FWLabel key="AL0028_PERIODICITE_T"/> <br>
					</td>
				</tr>
				</table>
					</td>
		</tr>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>	
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
	
				