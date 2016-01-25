<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.al.vb.radiationaffilie.ALRadiationAffilieViewBean"%>
<%@page import="ch.globaz.naos.business.service.AffiliationService"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
ALRadiationAffilieViewBean viewBean = (ALRadiationAffilieViewBean) session.getAttribute("viewBean"); 

btnUpdLabel = objSession.getLabel("MODIFIER");
btnDelLabel = objSession.getLabel("SUPPRIMER");
btnValLabel = objSession.getLabel("VALIDER");
btnCanLabel = objSession.getLabel("ANNULER");
btnNewLabel = objSession.getLabel("NOUVEAU");

bButtonNew = false;
bButtonDelete = false;
bButtonValidate = objSession.hasRight(userActionUpd, FWSecureConstants.UPDATE);

idEcran="AL0033";
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
    document.forms[0].elements('userAction').value="al.radiationaffilie.radiationAffilie.modifier";
}
function validate() {
    state = validateFields();
    //if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.radiationaffilie.radiationAffilie.ajouter";
    //else 
    //   document.forms[0].elements('userAction').value="al.radiationaffilie.radiationAffilie.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.radiationaffilie.radiationAffilie.afficher";
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

fieldset div label {
    display: inline-block;
    width: 200px;
    margin-right: 20px;
    vertical-align: middle;
}

fieldset {
	border:0;
	margin:5px 0;
}

fieldset div {
	margin:5px 0;
}
</style>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>
				<ct:FWLabel key="AL0033_TITRE" />
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
		<tr><td><%-- tpl:insert attribute="zoneMain" --%>
		<div id="AL0029zone" class="zone">
		
		<fieldset id="fs_email">
			<div>
				<label for="email"><ct:FWLabel key="AL0033_EMAIL"/></label>
				<input tabindex="1"  type="text" id="email" name="email" value="<%=viewBean.getEmail() %>"/>
			</div>
		</fieldset>
		
		<hr />
		
		<fieldset id="fs_radiation"  style="width:450px;display: inline-block">
			<div>
				<label for="affilieOrigine"><ct:FWLabel key="AL0033_AFFILIE_ORIGINE"/></label>
				<input class="jadeAutocompleteAjax medium" data-g-autocomplete="service:ch.globaz.naos.business.service.AffiliationService,
                                method:widgetFind,
                                criterias:¦{
                                    likeNumeroAffilie: '<ct:FWLabel key="AL0033_NUM_AFFILIE"/>'
                                }¦,
                                modelReturnVariables:¦affiliation.affilieNumero,affiliation.raisonSocialeCourt¦,
                                lineFormatter:#{affiliation.affilieNumero} #{affiliation.raisonSocialeCourt},
                                functionReturn:¦
                                    function(element){
                                    	this.value = $(element).attr('affiliation.affilieNumero')
                                        }¦" type="text" name="affilieOrigine" id="affilieOrigine" tabindex="2"
                                        value="<%=viewBean.getAffilieOrigine() %>">
			</div>
			<div>
				<label for="dateRadiation"><ct:FWLabel key="AL0033_DATE_RADIATION"/></label>
				<input tabindex="3" class="clearable" type="text" name="dateRadiation" id="dateRadiation" data-g-calendar="mandatory:false" value="<%=viewBean.getDateRadiation() %>"/>
			</div>
		</fieldset>
		
		<div style="width:100px;display:inline;padding-bottom:20px">>>></div>
		
		<fieldset id="fs_copie" style="width:450px;display: inline-block">
			<div>
				<label for="affilieDestinataire"><ct:FWLabel key="AL0033_AFFILIE_DESTINATAIRE"/></label>
				<input class="jadeAutocompleteAjax medium" data-g-autocomplete="mandatory:false,
                                service:ch.globaz.naos.business.service.AffiliationService,
                                method:widgetFind,
                                criterias:¦{
                                    likeNumeroAffilie: '<ct:FWLabel key="AL0033_NUM_AFFILIE"/>'
                                }¦,
                                modelReturnVariables:¦affiliation.affilieNumero,affiliation.raisonSocialeCourt¦,
                                lineFormatter:#{affiliation.affilieNumero} #{affiliation.raisonSocialeCourt},
                                functionReturn:¦
                                    function(element){
                                    	this.value = $(element).attr('affiliation.affilieNumero')
                                        }¦" type="text" name="affilieDestinataire" id="affilieDestinataire" tabindex="4"
                                        value="<%=viewBean.getAffilieDestinataire() %>">
			</div>
			<div>
				<label for="dateDebutActivite"><ct:FWLabel key="AL0033_DATE_DEBUT_ACTIVITE"/></label>
				<input tabindex="5" class="clearable" type="text" name="dateDebutActivite" id="dateDebutActivite" data-g-calendar="mandatory:false" value="<%=viewBean.getDateDebutActivite() %>"/>
			</div>
		</fieldset>
		
		<hr />
		
		<fieldset id="fs_decision">
		<div>
			<label for="printDecisions"><ct:FWLabel key="AL0033_EDITION_CERTIFICAT_RADIATION"/></label>
			<input name="printDecisions" id="printDecisions" type="checkbox" tabindex="6" <%if(viewBean.getPrintDecisions()) { %>checked="checked"<%} %>/>
			<script type="text/javascript">
			$('input[name=printDecisions]').click(function() {
			      $('#printDecisions-ref').toggle($(this).is(':checked'));     
			 });
			</script>
		</div>
		<div id="printDecisions-ref" <%if(!viewBean.getPrintDecisions()) { %>style="display:none;"<%} %>>
			<div>
				<label for="reference"><ct:FWLabel key="AL0033_REFERENCE"/></label>
				<input tabindex="7" type="text" id="reference" name="reference" class="medium" value="<%=viewBean.getReference() %>"/>
			</div>
			<div>
				<label for="dateImpression"><ct:FWLabel key="AL0033_DATE_IMPRESSION"/></label>
				<input tabindex="8" class="clearable" type="text" name="dateImpression" id="dateImpression" data-g-calendar="mandatory:false" value="<%=viewBean.getDateImpression() %>"/>
			</div>
		</div>
		</fieldset>
		
		</div>		
		</td></tr>
	
			<%-- /tpl:insert --%>
	
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>	
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>

				