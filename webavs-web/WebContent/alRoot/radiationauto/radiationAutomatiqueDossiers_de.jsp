<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.al.vb.radiationauto.ALRadiationAutomatiqueDossiersViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
ALRadiationAutomatiqueDossiersViewBean viewBean = (ALRadiationAutomatiqueDossiersViewBean) session.getAttribute("viewBean"); 

btnUpdLabel = objSession.getLabel("MODIFIER");
btnDelLabel = objSession.getLabel("SUPPRIMER");
btnValLabel = objSession.getLabel("VALIDER");
btnCanLabel = objSession.getLabel("ANNULER");
btnNewLabel = objSession.getLabel("NOUVEAU");

bButtonNew = false;
bButtonDelete = false;
bButtonCancel = false;
bButtonValidate = objSession.hasRight(userActionUpd, FWSecureConstants.UPDATE);

idEcran="AL0029";
%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<script type="text/javascript">
function add() {
}
function upd() {
    document.forms[0].elements('userAction').value="al.radiationauto.al.radiationauto.radiationAutomatiqueDossiers.modifier";
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.radiationauto.radiationAutomatiqueDossiers.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.radiationauto.radiationAutomatiqueDossiers.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.radiationauto.radiationAutomatiqueDossiers.afficher";
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
<style type="text/css">

div#AL0029zone {
	padding:5px;
}

div#AL0029zone div {
	height:30px;
	vertical-align:middle;
}

div#AL0029zone div div {
	width:220px;
	text-align: right;
	display: inline;
}

div#AL0029zone label {
	width:200px;
}

</style>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>
				<ct:FWLabel key="AL0029_TITRE" />
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
		<tr>
			<td><%-- tpl:insert attribute="zoneMain" --%>
				<div id="AL0029zone">
					<div style="background-image:url('<%=request.getContextPath()%>/images/summary_bg.png'); 
					background-repeat:repeat-x; 
					border:2px solid #226194; 
					font-size: 11px; padding:5px; margin-bottom:20px;">
					
						<h4>Description</h4>

						<p>Les dossiers radiés par ce traitement sont les suivants :</p>

						<p>Tous les dossiers actifs dans lesquels la dernière prestation versée remonte à plus de 12 mois à 
						compter du mois en cours. <br/><br/>
						<em>Exemple si mois en cours = 01.2014<br/>
						Radiation du dossier si dernière prestation versée &lt;/= 01.2013</em>
						</p>

						<p>Tous les dossiers actifs dans lesquels le dernier droit actif est un droit de type formation ou de type enfant 
						en incapacité et que son échéance calculée est échue de plus d’un mois à compter du mois en cours. Les autres droits 
						du dossier n’ont pas eu de prestations versées dans le courant des 12 derniers mois.<br/><br/> 
						<em>Exemple si mois en cours = 01.2014<br/>
						Dernier droit actif = Droit formation/enfant en incapacité avec échéance calculée &lt; 01.2014</em></p>	
		 			</div>
				
					<div>
						<label for="email"><ct:FWLabel key="AL0029_EMAIL"/></label>
						<div>
							<input tabindex="1" type="text" id="email" name="email" value="<%=viewBean.getEmail() %>" class="long"/>
						</div>
					</div>
					<div>
						<label for="printDecisions"><ct:FWLabel key="AL0029_IMPRESSION_DECISION"/></label>
						<div><input type="checkbox" id="printDecisions" name="printDecisions" tabindex="3" /></div>
					</div>
					<div>
						<label for="GED"><ct:FWLabel key="AL0029_DECISION_GED"/></label>
						<div><input type="checkbox" checked="checked" id="GED" name="GED" tabindex="4" /></div>
					</div>
				</div>
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

				