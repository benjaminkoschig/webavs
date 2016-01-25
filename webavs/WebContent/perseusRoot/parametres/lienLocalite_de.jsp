<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="ch.globaz.pyxis.business.service.AdresseService"%>
<%@page import="globaz.perseus.utils.parametres.PFParametresHandler"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="ch.globaz.perseus.business.models.parametres.SimpleLoyer"%>
<%@page import="ch.globaz.perseus.business.models.parametres.SimpleLienLocalite"%>
<%@page import="ch.globaz.perseus.business.models.parametres.SimpleZone"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@ page import="globaz.perseus.vb.parametres.PFLienLocaliteViewBean" %>

<%-- tpl:insert attribute="zoneInit" --%>

<%//Les labels de cette page commence par le préfix "JSP_PF_PARAM_LIEN_LOCALITE"
idEcran="PPF0911";
PFLienLocaliteViewBean viewBean = (PFLienLocaliteViewBean) session.getAttribute ("viewBean");

if(objSession.hasRight("perseus", FWSecureConstants.ADD)){
	bButtonCancel = true;
	bButtonValidate = true;
}else
{
	bButtonCancel = false;
	bButtonValidate = false;
}

autoShowErrorPopup = true;
boolean viewBeanIsNew= true;

%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/perseusRoot/ajax/javascriptsAndCSS.jspf" %>
<%--<script type="text/javascript" src="<%=rootPath%>/scripts/jadeBaseFormulaire.js"/></script>--%>
<script>
	var url = "perseus.parametres.lienLocalite";
	
	function add() {}
	function upd() {}
	function init() {}
 	function postInit() {}
	
	//Fonction permettant la validation d'une modification ou d'un ajout
	function validate() {
		state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="perseus.parametres.lienLocalite.ajouter";
	    else if (document.forms[0].elements('_method').value == "upd")
	        document.forms[0].elements('userAction').value="perseus.parametres.lienLocalite.modifier";
	    
	    return state;
	  
	}
	
	function cancel() {
		if (document.forms[0].elements('_method').value == "upd")
			document.forms[0].elements('userAction').value="perseus.parametres.lienLocalite.chercher";
	}
	
	//Fonction permettant la suppresion d'un dossier
	function del() {
	    if (window.confirm("<%=objSession.getLabel("JSP_PF_PARAM_LIEN_LOCALITE_SUPPRESSION_CONFIRMATION")%>")){
	        document.forms[0].elements('userAction').value=url+".supprimer";
	        document.forms[0].submit();
	    }
	}
</script>
<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
		<table>
			<tr>
		  		<td>
		  			<label for="lienLocalite.simpleLienLocalite.idZone">
						<ct:FWLabel key="JSP_PF_PARAM_LIEN_LOCALITE_ZONE_DESIGN" />
					</label>
				</td>
				<td>								
					<ct:select 
						name="lienLocalite.simpleZone.idZone" 
						wantBlank="true" 
						notation="data-g-select='mandatory:true'"
		 				defaultValue="">
					    <%for( JadeAbstractModel model:PFParametresHandler.getListZone()){ 
					     	SimpleZone simpleZone = (SimpleZone)model;
					     %>
						<ct:option value="<%=simpleZone.getId()%>" label="<%=simpleZone.getDesignation() %>"/>
						<%} %>
					</ct:select>
				</td>
				<td></td>
				<td>			<!-- zoneLocalite.simpleLienZoneLocalite.idLocalite -->
					<label for="zoneLocalite.simpleLienZoneLocalite.idLocalite">
						<ct:FWLabel key="JSP_PF_PARAM_LIEN_LOCALITE_DESIGN" />
					</label>
				</td>
				<td>
					<input type="hidden" 
				       id="idLocalite" 
				       name = "lienLocalite.localiteSimpleModel.idLocalite"
				       value="<%=viewBean.getLienLocalite().getSimpleLienLocalite().getIdLocalite()%>" />
		 			<ct:widget id='localite' 
				           name='localite' 
				           notation='data-g-string="mandatory:true"'
				           defaultValue="<%=viewBean.getLienLocalite().getLocaliteSimpleModel().getLocalite()%>"
				           styleClass='libelleLong'>
						<ct:widgetService methodName="findLocalite" className="<%=AdresseService.class.getName()%>">										
							<ct:widgetCriteria  criteria="forNpaLike" label="JSP_PF_PARAM_LIEN_LOCALITE_LIBELLE_NPA"/>
							<ct:widgetCriteria criteria="forLocaliteUpperLike" label="JSP_PF_PARAM_LIEN_LOCALITE_LIBELLE_LOCALITE"/>
							<ct:widgetLineFormatter format="#{numPostal}, #{localite}"/>
							<ct:widgetJSReturnFunction>
								<script type="text/javascript">
									function(element){
										$(this).val($(element).attr('numPostal')+', '+$(element).attr('localite'));
										$('#idLocalite').val($(element).attr('idLocalite'));
									}
								</script>											
							</ct:widgetJSReturnFunction>
						</ct:widgetService>
					</ct:widget>
				</td>
			</tr>
			<tr>
			<tr>
				<td>			
					<label for="lienLocalite.simpleLienLocalite.dateDebut">
						<ct:FWLabel key="JSP_PF_PARAM_LIEN_LOCALITE_DATE_DEBUT"  />
					</label>
				</td>
				<td>					
					<input width="15" 
				 	   type="text"
					   name="setDateDebutConverter" 
					   id="lienLocalite.simpleLienLocalite.dateDebut" 
					   value="<%=viewBean.getDateDebutConverter()%>" 
					   data-g-calendar="mandatory:true, type:month"/>					   
				</td>
				<td></td>
				<td>
					<label for="lienLocalite.simpleLienLocalite.dateFin">
						<ct:FWLabel key="JSP_PF_PARAM_LIEN_LOCALITE_DATE_FIN" />
					</label>
				</td>
				<td>
					<input width="15" 
				 	   type="text"
					   name="setDateFinConverter" 
					   id="lienLocalite.simpleLienLocalite.dateFin" 
					   value="<%=viewBean.getDateFinConverter()%>" 
					   data-g-calendar="mandatory:false, type:month"/>
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
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>


