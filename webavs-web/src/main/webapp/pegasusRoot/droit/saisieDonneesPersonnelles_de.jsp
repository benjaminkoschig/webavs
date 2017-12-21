	<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%><%@page import="ch.globaz.pegasus.business.constantes.IPCLienRepondant"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.pegasus.business.constantes.EPCProperties"%>
<%@page import="ch.globaz.pegasus.businessimpl.utils.PCproperties"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		
<%@page import="java.util.Iterator"%>

<%@page import="globaz.pegasus.vb.droit.PCSaisieDonneesPersonnellesViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneesPersonnelles"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>

<%@page import="ch.globaz.pyxis.business.service.AdresseService"%>
<%@page import="ch.globaz.pegasus.business.models.droit.DonneesPersonnelles"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>

<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/droit/droit.css" />

<%
	//Les labels de cette page commence par le préfix "JSP_PC_SDP_D"
	idEcran="PPC0006";

	PCSaisieDonneesPersonnellesViewBean viewBean = (PCSaisieDonneesPersonnellesViewBean) session.getAttribute("viewBean");
	autoShowErrorPopup = true;
	bButtonDelete   = false;
	bButtonNew      = false;
	bButtonUpdate   = false;
	bButtonValidate = false;
	bButtonCancel 	= false;
%>

<ct:serializeObject variableName="droit" objectName="viewBean.droit" destination="javascript"/>


<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %> 

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/DonneesPersonnelles_MembrePart.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/saisieDonneesPersonnelles_de.js"/></script>


<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var ACTION_AJAX="<%=IPCActions.ACTION_DROIT_DONNEES_PERSONNELLES_AJAX%>";
	var MAIN_URL="<%=formAction%>";
	
$(function(){
	// configure AJAX
	$.ajaxSetup({
		url: MAIN_URL,
		error: function (req, textStatus, errorThrown) {alert("error: " + textStatus + " - " + errorThrown + "-" + req.status + " - ContentType: " + req.contentType);}
	});
});

</script>

<style>
	h2 {
		font-size: 1.1em;
		margin: 0px 0 10px 0;
		padding: 0;
		color: gray;
	}
</style>

<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsdroits">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getDroit().getId()%>"/>
	<ct:menuSetAllParams key="idDemandePc" value="<%=viewBean.getDroit().getSimpleDroit().getIdDemandePC()%>"/>
	<ct:menuSetAllParams key="idDroit" value="<%=viewBean.getDroit().getId()%>"/>
	<ct:menuSetAllParams key="noVersion" value="<%=viewBean.getDroit().getSimpleVersionDroit().getNoVersion()%>"/>
	<ct:menuSetAllParams key="idVersionDroit" value="<%=viewBean.getDroit().getSimpleVersionDroit().getIdVersionDroit()%>"/>
	<ct:menuSetAllParams key="idDossier" value="<%=viewBean.getIdDossier()%>"/>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_SDP_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
<TR>
<td>
<%-- HEADER PAGE --%>
<%-- BODY PAGE --%>
<div class="conteneurDF">

		<div id="areaAssure"">
		   <%=viewBean.getRequerantDetail(objSession) %>
		</div>

<br style="clear:both; "/>
<div class="conteneurMembres">
	<% 
	int counter=0;
			for(Iterator itMembre=viewBean.getListeMembres().iterator();itMembre.hasNext();){
				DroitMembreFamille membreFamille=(DroitMembreFamille)itMembre.next();
				
				DonneesPersonnelles donnee=viewBean.getDonnees(membreFamille.getId());
				SimpleDonneesPersonnelles simpleDonnee=donnee.getSimpleDonneesPersonnelles();
		%>

		<div class="areaMembre" idMembre="<%=membreFamille.getId() %>" idDonnePersonnel='<%=simpleDonnee.getId()%>'>
			<div class="areaTitre">
				<%=PCDroitHandler.getFromattedTitreHTML(objSession,membreFamille, simpleDonnee.getNoCaisseAvs())%>
			</div>
		
			<TABLE class="areaDetail">
				<TR>
					<TD><ct:FWLabel key="JSP_PC_SDP_D_STATUT_REFUGIE_APATRIDE"/></TD>
					<TD>
						<ct:select wantBlank="true" name="csStatusRefugieApatride" defaultValue="<%=simpleDonnee.getCsStatusRefugieApatride()%>" styleClass="csStatusRefugieApatride">
							<ct:optionsCodesSystems csFamille="<%=IPCDroits.CS_STATUT_REFUGIE_APATRIDE%>"/>
						</ct:select>
					</TD>
					<TD><ct:FWLabel key="JSP_PC_SDP_D_NO_OCC"/></TD>
					<TD>
						<INPUT type="text" class="noOCC" value="<%=simpleDonnee.getNoOCC()%>">
					</TD>
				</TR>
				
				<tr>
					<td><ct:FWLabel key="JSP_PC_SDP_D_NO_CAISSE_AVS"/></td>
					<td><INPUT type="text" class="noCaisseAVS" value="<%=simpleDonnee.getNoCaisseAvs()%>" data-g-string="sizeMax:7"></td>
					<td><ct:FWLabel key="JSP_PC_SDP_D_NO_OFFICE_AI"/></td>
					<td><INPUT type="text" class="noOfficeAI" value="<%=simpleDonnee.getNoOfficeAi()%>" data-g-integer="sizeMax:3"></td>
				</tr>
				
				<TR>
					<TD><ct:FWLabel key="JSP_PC_SDP_D_DERNIER_DOMICILE_LEGAL"/></TD>
					<TD>
						<ct:widget name='<%="tiersWidget"+String.valueOf(counter)%>' id='<%="tiersWidget"+String.valueOf(counter) %>' notation='data-g-string="mandatory:true"' styleClass="libelleLong nomDernierDomicile" defaultValue='<%=donnee.getLocalite().getNumPostal()+", "+donnee.getLocalite().getLocalite() %>'>
							<ct:widgetService methodName="findLocalite" className="<%=AdresseService.class.getName()%>">
								<ct:widgetCriteria criteria="forNpaLike" label="JSP_PC_PARAM_HOMES_W_TIERS_NPA"/>
								<ct:widgetCriteria criteria="forLocaliteUpperLike" label="JSP_PC_PARAM_HOMES_W_TIERS_LOCALITE"/>
								<ct:widgetLineFormatter format="#{numPostal}, #{localite}"/>
								<ct:widgetJSReturnFunction>
									<script type="text/javascript">
										function(element){
											$(this).val($(element).attr('numPostal')+', '+$(element).attr('localite'));
											var zone=$(this).parents('.areaMembre')[0].zone;
											zone.idLocalite=$(element).attr('idLocalite');
										}
									</script>										
								</ct:widgetJSReturnFunction>
							</ct:widgetService>
						</ct:widget>						
					</TD>
					<% if(simpleDonnee.getIsEnfant()){ %>
						<td><ct:FWLabel key="JSP_PC_SDP_D_ISENFANT"/></td>
						<td><input type="checkbox" checked /></td>
					<%} %>
				</TR>
				<tr>
					<td><ct:FWLabel key="JSP_PC_SDP_D_REPRESENTANT_LEGAL"/></td>
					<td><input type="checkbox" class="representant" <% if (simpleDonnee.getIsRepresentantLegal()) { %> checked<% } %> /></td>
				</tr>
				<%if (EPCProperties.GESTION_ANNONCES_LAPRAMS.getBooleanValue()) { %>
					<tr><td colspan="6"><hr></td></tr>
					<tr><td colspan="6"><h2 calss="h2"> <ct:FWLabel key="JSP_PC_SDP_D_COMPLEMENT_LAPRAMS"/> </h2></td></tr>
				<tr>
						<td><ct:FWLabel key="JSP_PC_SDP_D_REPONDANT"/></td>
						<td>	
							<input type="hidden"  class="idTiersRepondant" />
							<ct:widget id='repondant<%= String.valueOf(counter)%>' name='repondant<%=String.valueOf(counter)%>' 
								           defaultValue="<%=viewBean.getInfoRepondant(donnee)%>"
								           styleClass="repondant">
								<ct:widgetService methodName="findByAlias" className="<%=PersonneEtendueService.class.getName()%>">
									<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_CREANCIER_W_NOM"/>								
									<ct:widgetCriteria criteria="forDesignation2Like" label="JSP_PC_CREANCIER_W_PRENOM"/>
									<ct:widgetCriteria criteria="forNumeroAvsActuel" label="JSP_PC_CREANCIER_W_AVS"/>									
									<ct:widgetCriteria criteria="forDateNaissance" label="JSP_PC_CREANCIER_W_NAISS"/>			
									<ct:widgetCriteria criteria="forAlias" label="JSP_PC_CREANCIER_W_ALIAS"/>																			
									<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
									<ct:widgetJSReturnFunction>
										<script type="text/javascript">
											function(element){
												$(this).parents('.areaMembre').find('.idTiersRepondant').val($(element).attr('tiers.id')).change();
												this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
											}
										</script>										
									</ct:widgetJSReturnFunction>
								</ct:widgetService>
							</ct:widget>
						</td>
						<td><ct:FWLabel key="JSP_PC_SDP_D_LIEN_REPONDANT"/></td>
						<td>
							<ct:select wantBlank="true" name="csLienRepondant" defaultValue="<%=donnee.getSimpleDonneesPersonnelles().getCsLienRepondant()%>" styleClass="csLienRepondant">
								<ct:optionsCodesSystems csFamille="<%=IPCLienRepondant.CS_TYPE_LIEN%>"/>
							</ct:select>
						</td>
					</tr>	
					
				<tr>
					<td><ct:FWLabel key="JSP_PC_SDP_D_PERMIS"/></td>
					<td colspan="6">
						<ct:select wantBlank="true" 
						           name="csPermis" 
						           defaultValue="<%=donnee.getSimpleDonneesPersonnelles().getCsPermis()%>" 
						           styleClass="csPermis">
							<ct:optionsCodesSystems csFamille="PCPERMIS"/>
						</ct:select>
					</td>
				</tr>	
					<tr>
						<td><ct:FWLabel key="JSP_PC_SDP_D_COMMUNE_ORIGINE_ORIGINE"/></td>
						<td>
							<input type="text" class="communeOrigine" value="<%=simpleDonnee.getCommuneOrigine()%>">
						</td>
						<td><ct:FWLabel key="JSP_PC_SDP_D_CODE_OFS_COMMUNE_ORIGINE"/></td>
						<td>
							<input type="text" data-g-integer="sizeMax:4,autoFit:true"  class="communeOrigineCodeOfs" value="<%=simpleDonnee.getCommuneOrigineCodeOfs()%>">
						</td>
				</tr>		
				<%} %>				
				<tr>
						<td class="btnAjax" colspan="4">
						<input class="btnAjaxValidate" type="button" value="<%=btnValLabel%>">
						<input class="btnAjaxCancel" type="button" value="<%=objSession.getLabel("JSP_PC_SGL_D_ANNULER")%>">
						<input class="btnAjaxUpdate" type="button" value="<%=btnUpdLabel%>">
					</td>
				</tr>
			</TABLE>
		</div>
		
		<% counter++;
		} %>
	</div>
	</div>
	</td>
</TR>	
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
<%-- /tpl:insert --%>