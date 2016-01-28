<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<%@page import="ch.globaz.pyxis.business.model.TiersSimpleModel"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneSimpleModel"%>

<%@page import="globaz.perseus.vb.dossier.PFDossierViewBean"%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="globaz.prestation.interfaces.tiers.PRTiersHelper"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<%
PFDossierViewBean viewBean = (PFDossierViewBean) session.getAttribute("viewBean"); 
idEcran="PPF0511";
autoShowErrorPopup = true;

PersonneEtendueComplexModel personne = viewBean.getDossier().getDemandePrestation().getPersonneEtendue();

selectedIdValue = viewBean.getId();

String service = BSessionUtil.getSessionFromThreadContext().getApplication().getProperty(globaz.externe.IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED);

boolean viewBeanIsNew="add".equals(request.getParameter("_method"));

String affichePersonnne ="";
if(!viewBeanIsNew){
	affichePersonnne=PFUserHelper.getDetailAssure(objSession,personne);
} 

bButtonDelete = !viewBean.hasDemande();

bButtonNew = JadeStringUtil.isEmpty(viewBean.getId());

String dateDefault = JadeDateUtil.addMonths(JadeDateUtil.getGlobazFormattedDate(JadeDateUtil.addYears(Calendar.getInstance().getTime(), 1)), -1);

%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/perseusRoot/ajax/javascriptsAndCSS.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="perseus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="perseus-optionsdossiers">
	<ct:menuSetAllParams key="idDossier" value="<%=viewBean.getId()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getId()%>"/>
</ct:menuChange>

<script type="text/javascript">
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
		
		<%if(!viewBeanIsNew){%>
			$('#annoncesChangements').prop('disabled', true);
			globazNotation.utilsInput.changeColorDisabled($('#annoncesChangements'));
		<%}%>
		$("#lienGed").click(function() {
			window.open('<%=servletContext%><%=("/perseus")%>?userAction=perseus.dossier.dossier.actionAfficherDossierGed&amp;noAVSId=<%=viewBean.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel()%>&amp;idTiersExtraFolder=<%=viewBean.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers()%>&amp;serviceNameId=<%=service%>');
		});
	});

	//Fonction permettant d'ajouter un dossier
	function add() {
	}
	
	//Fonction permettant la modification d'un dossier
	function upd() {
		$('#annoncesChangements').prop('disabled', false);
		globazNotation.utilsInput.changeColorDisabled($('#annoncesChangements'));
	}

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
	
	//Fonction permettant la validation d'une modification ou d'un ajout
	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="perseus.dossier.dossier.ajouter";
	    else if (document.forms[0].elements('_method').value == "upd")
	        document.forms[0].elements('userAction').value="perseus.dossier.dossier.modifier";
	    
	    return state;
	}
	
	//Fonction permettant d'annuler une opération en cours
	function cancel() {
		document.forms[0].elements('userAction').value="perseus.dossier.dossier.chercher";
	}
	
	//Fonction permettant la suppresion d'un dossier
	function del() {
	    if (window.confirm("<%=objSession.getLabel("JSP_PF_DOS_SUPPRESSION_CONFIRMATION")%>")){
	        document.forms[0].elements('userAction').value="perseus.dossier.dossier.supprimer";
	        document.forms[0].submit();
	    }
	}
	
	function init() {
		
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
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_DOS_TITRE_DETAILS"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
	<tr><td>					
	<table width="90%">
		<TR>
			<TD><ct:FWLabel key="JSP_PF_DOS_D_ASSURE"/></TD>
			<TD>
				<%
					String params = "&provenance1=TIERS";
					String jspLocation = servletContext + "/perseusRoot/numeroSecuriteSocialeSF_select.jsp";
				%>
				<%if(viewBeanIsNew){ %>
				

				<ct:FWListSelectTag name="csNationaliteAffiche" data="<%=PRTiersHelper.getPays(objSession)%>" notation="style='display: none'"
					defaut="<%=JadeStringUtil.isIntegerEmpty(personne.getTiers().getIdPays())?TIPays.CS_SUISSE:personne.getTiers().getIdPays()%>"/>

				
				<ct:widget id='<%="widgetTiers"%>' name='<%="widgetTiers"%>' styleClass="widgetTiers" notation="data-g-string='mandatory:true'">
					<ct:widgetService methodName="find" className="<%=PersonneEtendueService.class.getName()%>">
						<ct:widgetCriteria criteria="forNumeroAvsActuel" label="JSP_PF_DOS_W_NSS"/>									
						<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
						<ct:widgetJSReturnFunction>
							<script type="text/javascript">
								function(element){
									var idPays=null, pays=null, res=null;
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
			</TD>
		</TR>
		<tr><td></td><td id="resultAutocompete"><%=affichePersonnne%></td></tr>
		<TR><TD colspan="6">&nbsp;<HR></TD></TR>
		<TR>
			<TD><ct:FWLabel key="JSP_PF_DOS_D_DATE_REVISION"/></TD>
			<TD>
				<input type="text" name="dossier.dossier.dateRevision" value="<%=(viewBeanIsNew)?dateDefault:JadeStringUtil.toNotNullString(viewBean.getDossier().getDossier().getDateRevision())%>" data-g-calendar="mandatory:true"/>
			</TD>
		</TR>
		<TR><TD colspan="6">&nbsp;</TD></TR>
		<TR>
			<TD><ct:FWLabel key="JSP_PF_DOS_D_GESTIONNAIRE"/></TD>
			<TD>
				<%if(viewBeanIsNew){ %>
				<ct:FWListSelectTag name="dossier.dossier.gestionnaire" data="<%=PFGestionnaireHelper.getResponsableData(objSession)%>" 
					notation="data-g-select='mandatory:true'" defaut="<%=objSession.getUserId()%>"/>
				<%}else {%>
				<ct:FWListSelectTag name="dossier.dossier.gestionnaire" data="<%=PFGestionnaireHelper.getResponsableData(objSession)%>" 
					notation="data-g-select='mandatory:true'" defaut="<%=viewBean.getDossier().getDossier().getGestionnaire()%>"/>
				<%}%>
				
			</TD>
		</TR>
		<TR><TD colspan="6">&nbsp;</TD></TR>
		<TR>
			<TD valign="top"><ct:FWLabel key="JSP_PF_DOS_D_ANNONCES_CHANGEMENTS"/></TD>
			<TD>
				<textarea rows="10" cols="80" name="dossier.dossier.annoncesChangements" id="annoncesChangements"><%=JadeStringUtil.toNotNullString(viewBean.getDossier().getDossier().getAnnoncesChangements()) %></textarea>
			</TD>
		</TR>
		<tr>
			<td>
			</td>
			<TD class="mtd">
				<a id="lienGed" href="#">GED</a>			
			</TD>
		</tr>
	</table>			
	</tr></td>				
						<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
