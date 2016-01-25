
<%@page import="globaz.pegasus.vb.dossier.PCDossierViewBean"%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/detail/header.jspf" %>

<%@page import="ch.globaz.pyxis.business.model.TiersSimpleModel"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneSimpleModel"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.prestation.interfaces.tiers.PRTiersHelper"%>

<%@page import="globaz.globall.vb.BJadePersistentObjectViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>

<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="globaz.pegasus.utils.PCRrequerantHandler"%>
<%@page import="globaz.pegasus.utils.PCUserHelper"%><script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/dossier/detail.css"/>
<%-- tpl:insert attribute="zoneInit" --%>
<%
PCDossierViewBean viewBean = (PCDossierViewBean) session.getAttribute("viewBean"); 

/* 
TiersSimpleModel tiers=viewBean.getDossier().getDemandePrestation().getPersonneEtendue().getTiers();
PersonneSimpleModel personne=viewBean.getDossier().getDemandePrestation().getPersonneEtendue().getPersonne();*/

PersonneEtendueComplexModel personne= viewBean.getDossier().getDemandePrestation().getPersonneEtendue();

String affichePersonnne ="";
String id=viewBean.getId();

boolean viewBeanIsNew="add".equals(request.getParameter("_method"));

if(!viewBeanIsNew){
	affichePersonnne=PCUserHelper.getDetailAssure(objSession,personne);
}

selectedIdValue=viewBean.getId();
//btnUpdLabel = objSession.getLabel("MODIFIER");
//btnDelLabel = objSession.getLabel("SUPPRIMER");
//btnValLabel = objSession.getLabel("VALIDER");
//btnCanLabel = objSession.getLabel("ANNULER");
//btnNewLabel = objSession.getLabel("NOUVEAU");

bButtonNew = JadeStringUtil.isEmpty(id);

bButtonDelete = !viewBean.isHasDemande();
idEcran="PPC0002";
autoShowErrorPopup = true;

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsdossiers">
	<ct:menuSetAllParams key="idDossier" value="<%=viewBean.getId()%>"/>
</ct:menuChange>

<script type="text/javascript">
//TODO check what to do with those empty functions
function add() {}
function upd() {}
var MAIN_URL="<%=formAction%>";

var ACTION_DOSSIER="<%=IPCActions.ACTION_DOSSIER%>";
var actionMethod;
var userAction;

$(function(){
	actionMethod=$('[name=_method]',document.forms[0]).val();
	userAction=$('[name=userAction]',document.forms[0])[0];

	// attribue une id à tous les champs ayant un nom mais pas encore d'id
	$('*',document.forms[0]).each(function(){
		if(this.name!=null && this.id==""){
			this.id=this.name;
		}
	});
});

function readOnly(flag) {
	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
	var enabledNames=['csNationaliteAffiche','csSexeAffiche','csCantonAffiche','partiallikeNSS'];
	$('input,select',document.forms[0]).each(function(){
		if (!this.readOnly &&
    	      $.inArray(this.name,enabledNames)==-1 &&
	       <%=!viewBeanIsNew%> &&
	       this.type != 'hidden') {
          
          this.disabled = flag;
		}
 	});
}

function cancel() {
	if (actionMethod == "add"){
		userAction.value=ACTION_DOSSIER+".chercher";
    }else{
    	userAction.value=ACTION_DOSSIER+".rechercher";
    }
}  

function validate() {
    state = true;
    if (actionMethod == "add"){
    	userAction.value=ACTION_DOSSIER+".ajouter";
    }else{
    	userAction.value=ACTION_DOSSIER+".modifier";
    }
    return state;
}    

function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
    	userAction.value=ACTION_DOSSIER+".supprimer";
        document.forms[0].submit();
    }
}

function init(){
	<%--	<%if(BJadePersistentObjectViewBean.ERROR.equals(viewBean.getMsgType())){%>
	errorObj.text="<%=viewBean.getMessage()%>";
	showErrors()
	errorObj.text="";
	<%}%>--%>
}

function postInit(){
	$("#csNationaliteAffiche").attr("disabled","true");
	
	<%if(!viewBeanIsNew){%>
		$("#partiallikeNSS").attr("disabled","true");
	<%}%>
}


</script>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%>
			<span class="postItIcon" data-g-note="idExterne:<%=id%>, tableSource:PCDOSSI"></span>
			<ct:FWLabel key="JSP_PC_DOS_D_TITRE"/>
			<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:insert attribute="zoneMain" --%>
			<td>		
			<table width="90%">
				
				<TR>
					<TD class="standardLabel"><ct:FWLabel key="JSP_PC_DOS_D_ASSURE"/></TD>
					<TD>
						<%
							String params = "&provenance1=TIERS";
							String jspLocation = servletContext + "/pegasusRoot/numeroSecuriteSocialeSF_select.jsp";
						%>
						<%if(viewBeanIsNew){ %>
						<ct:FWListSelectTag  name="csNationaliteAffiche" data="<%=PRTiersHelper.getPays(objSession)%>" notation="style='display: none'"
							defaut="<%=JadeStringUtil.isIntegerEmpty(personne.getTiers().getIdPays())?TIPays.CS_SUISSE:personne.getTiers().getIdPays()%>"/>
						
						<ct:widget id='<%="widgetTiers"%>' name='<%="widgetTiers"%>' styleClass="widgetTiers">
							<ct:widgetService methodName="find" className="<%=PersonneEtendueService.class.getName()%>">
								<ct:widgetCriteria criteria="forNumeroAvsActuel" label="JSP_PC_DOS_W_NSS"/>									
								<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
								<ct:widgetJSReturnFunction>
									<script type="text/javascript">
										function(element){
											var idPays =null, pays=null,res=null;
											$('#idTiers').val($(element).attr('tiers.id'));
											$('#nss').val($(element).attr('personneEtendue.numAvsActuel'));;
											this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
											idPays = $(element).attr('tiers.idPays');
											pays = $('[name=csNationaliteAffiche]').val(idPays).find(':selected').text();
											$('#resultAutocompete').children().remove();
											res = '<div><br /><b>'+$(element).attr('personneEtendue.numAvsActuel')+'</b><br /> '+ 
													$(element).attr('tiers.designation1')+' '+
													$(element).attr('tiers.designation2')+' / '+
													$(element).attr('personne.dateNaissance')+' / '+
													$(element).attr('cs(personne.sexe)');
											res +=' / '+pays+'</div>'; 
											$('#resultAutocompete').append(res);
											$('#widgetTiers').hide();
										}
									</script>										
								</ct:widgetJSReturnFunction>
								</ct:widgetService> 
						</ct:widget>
	
						<!-- Workaround pour ne pas sumiter les forumlaire sur la séléction de l'autocomple -->
						<input type='text' name='test' style="display: none" />
						<%}%>
						 
						<input type="hidden" id="nss" name="dossier.demandePrestation.personneEtendue.personneEtendue.numAvsActuel" value="<%=JadeStringUtil.toNotNullString(personne.getPersonneEtendue().getNumAvsActuel())%>"/>
						<input type="hidden" id="idTiers" name="dossier.demandePrestation.personneEtendue.tiers.idTiers" value="<%=JadeStringUtil.toNotNullString(personne.getTiers().getId())%>"/>
						<input type="hidden" id="idDossier" name="idDossier" value="<%=viewBean.getId()%>"/>
						<span id="resultAutocompete"><%=affichePersonnne%></span>
					</TD>
				</TR>
				<!-- <tr><td></td><td id="resultAutocompete"><%=affichePersonnne%></td></tr> -->
				<TR><TD colspan="6">&nbsp;<HR class="separator"/></TD></TR>
				<TR>
					<TD class="standardLabel"><ct:FWLabel key="JSP_PC_DOS_D_GESTIONNAIRE"/></TD>
					<TD>
						<%if(viewBeanIsNew){ %>
						<ct:FWListSelectTag name="dossier.dossier.idGestionnaire" data="<%=PCGestionnaireHelper.getResponsableData(objSession)%>" 
							defaut="<%=objSession.getUserId()%>"/>
						<%}else {%>
						<ct:FWListSelectTag name="dossier.dossier.idGestionnaire" data="<%=PCGestionnaireHelper.getResponsableData(objSession)%>" 
							defaut="<%=viewBean.getDossier().getDossier().getIdGestionnaire()%>"/>
						<%}%>
					</TD>
				</TR>
			</TABLE>
			</td>
	                    				
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
