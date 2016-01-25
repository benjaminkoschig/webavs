<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp"
	import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ taglib uri="/WEB-INF/pegasus.tld" prefix="pe" %>
<%@ include file="/theme/detail_ajax/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%@page
	import="globaz.pegasus.vb.fortuneusuelle.PCCapitalLPPViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page
	import="ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%
	//Les labels de cette page commencent par le préfix "JSP_PC_CAPITAL_LPP"
	idEcran = "PPC0103";

	PCCapitalLPPViewBean viewBean = (PCCapitalLPPViewBean) session
			.getAttribute("viewBean");

	boolean viewBeanIsNew = "add".equals(request.getParameter("_method"));

	autoShowErrorPopup = true;

	bButtonUpdate = false;
	bButtonDelete = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf"%>
<%@ include file="/pegasusRoot/droit/commonDroit.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<%@page import="java.util.Arrays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page
	import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page
	import="ch.globaz.pegasus.business.models.fortuneusuelle.SimpleCapitalLPP"%>	
<%@page
	import="ch.globaz.pegasus.business.models.fortuneusuelle.CapitalLPP"%>

<%@page
	import="ch.globaz.pegasus.business.models.fortuneusuelle.FortuneUsuelle"%>
<%@page
	import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page
	import="globaz.pegasus.vb.fortuneusuelle.PCCapitalLPPAjaxViewBean"%>

<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.CapitalLPP"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>


<%@page import="globaz.pegasus.utils.PCCommonHandler"%><script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId()%>";
	var ACTION_AJAX_CAPITAL_LPP="<%=IPCActions.ACTION_DROIT_CAPITAL_LPP_AJAX%>";

	function validateFloorNumber(nombre){		
		if(nombre.value != ""){
			nombre.value = Number(nombre.value);
			nombre.value = Math.floor(nombre.value);
		}
	}	 	 	

	function validateFrais(nombre){
		if(nombre.value != ""){
			nombre.value = Number(nombre.value);
			nombre.value *= 20;
 			nombre.value = Math.round(nombre.value);
 			nombre.value /= 20;
		}
	}

	
$(function(){
	$('.typePropriete').change(function() {
		var value=($(this).attr("value"));		
		 
		if(value=="64009004") 
		{ 				
			$('.part').attr("readonly",true);
			$('.part').css("color","red");		
			$('.part').val("1/1");							 
		}	
		else{ 				
			$('.part').attr("readonly",false);
			$('.part').css("color","black");		 
		}	
				
	});	
});

</script>
	
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/droit/CapitalLPP_MembrePart.js" /></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/droit/CapitalLPP_de.js" /></script>


<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
<%=PCCommonHandler.getTitre(objSession,request)%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
<TR>
	<td colspan="4">
	<div class="conteneurDF">
						<div class="areaAssure">
							<%=viewBean.getRequerantDetail(objSession) %>
						</div>
	<hr />

						<%=PCDroitHandler.getOngletHtml(objSession,viewBean,IPCDroits.ONGLETS_FORTUNE_USUELLE,request,servletContext + mainServletPath)%>

	<div class="conteneurMembres">
							<% 
								for(Iterator itMembre=viewBean.getMembres().iterator();itMembre.hasNext();){
									MembreFamilleEtendu membreFamille=(MembreFamilleEtendu)itMembre.next();
							%>

	<div class="areaMembre" idMembre="<%=membreFamille.getId()%>">
	<div class="areaTitre">
		<%=PCDroitHandler.getFromattedTitreHTML(objSession,membreFamille)%>
	</div>
	<table class="areaDFDataTable">
		<thead>
			<tr>
				<th data-g-cellformatter="css:formatCellIcon" >&#160;</th>
				<th><ct:FWLabel key="JSP_PC_CAPITAL_LPP_L_PROPRIETE" /></th>
				<th><ct:FWLabel key="JSP_PC_CAPITAL_LPP_L_PART" /></th>
				<th><ct:FWLabel key="JSP_PC_CAPITAL_LPP_L_CAPITAL_LPP" /></th>
				<th><ct:FWLabel key="JSP_PC_CAPITAL_LPP_L_NUMERO_POLICE" /></th>
				<th><ct:FWLabel key="JSP_PC_CAPITAL_LPP_L_INSTITUTION" /></th>																		
				<th><ct:FWLabel key="JSP_PC_CAPITAL_LPP_L_SANS_INTERET" /></th>
				<th><ct:FWLabel key="JSP_PC_CAPITAL_LPP_L_INTERET" /></th>
				<th><ct:FWLabel key="JSP_PC_CAPITAL_LPP_L_FRAIS" /></th>
				<th data-g-cellformatter="css:formatCellIcon"  ><ct:FWLabel key="JSP_PC_CAPITAL_LPP_L_DF" /></th>
				<th data-g-cellformatter="css:formatCellIcon"  ><ct:FWLabel key="JSP_PC_CAPITAL_LPP_L_DR" /></th>
				<th data-g-periodformatter=" " data-g-deallaterperiod=" "> <ct:FWLabel	key="JSP_PC_CAPITAL_LPP_L_PERIODE" /></th>
			</tr>
		</thead>
		<tbody>
			<%								
					String currentId = "-1";
					String idGroup=null;
					for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){																	
						FortuneUsuelle donneeComplexe=(FortuneUsuelle)itDonnee.next();		
						CapitalLPP donneeALDComplexe =		donneeComplexe.getCapitalLPP();	  
						SimpleCapitalLPP donnee = (SimpleCapitalLPP)donneeALDComplexe.getSimpleCapitalLPP();
						SimpleDonneeFinanciereHeader dfHeader=donneeALDComplexe.getSimpleDonneeFinanciereHeader();																		
						String nomInstitution="";
						try{							
							nomInstitution=donneeALDComplexe.getCaisse().getTiers().getDesignation1();
						}catch(NullPointerException e){
							nomInstitution="";
						}				
						
						if(!dfHeader.getIdEntityGroup().equals(idGroup)){
							idGroup=null;
						}						
						
						if(!donneeALDComplexe.getSimpleCapitalLPP().getIdCapitalLPP().equals(currentId)){
							currentId = donneeALDComplexe.getSimpleCapitalLPP().getIdCapitalLPP();												

			%>		
							<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
								<td>&#160;</td>
								<td><%=objSession.getCode(donnee.getCsTypePropriete()) %></td>
								<td><%=donnee.getPartProprieteNumerateur() %> / <%=donnee.getPartProprieteDenominateur() %></td>
								<td style="text-align:right;"><%=new FWCurrency(donnee.getMontantCapitalLPP()).toStringFormat() %></td>
								<td><%=donnee.getNoPoliceNoCompte() %></td>
								<td><%=nomInstitution %></td>								
								<td><% if(donnee.getIsSansInteret().booleanValue()){%>
									<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
									<%} else {
										%>&nbsp;<%
									}%>
								</td>
								<td style="text-align:right;"><%=new FWCurrency(donnee.getMontantInteret()).toStringFormat() %></td>
								<td style="text-align:right;"><%=new FWCurrency(donnee.getMontantFrais()).toStringFormat() %></td>											
								<td><% if(dfHeader.getIsDessaisissementFortune().booleanValue()){%>
									<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
									<%} else {
										%>&nbsp;<%
									}%></td>
								<td><% if(dfHeader.getIsDessaisissementRevenu().booleanValue()){%>
									<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
									<%} else {
										%>&nbsp;<%
									}%></td>
								<td><%=dfHeader.getDateDebut() %> - <%=dfHeader.getDateFin() %></td>
							</tr>
			<%}
						idGroup=dfHeader.getIdEntityGroup();
				}
			%>
		</tbody>
	</table>
	<div class="areaDFDetail">
		<table>
			<tr>
				<td><ct:FWLabel key="JSP_PC_CAPITAL_LPP_L_PROPRIETE"/></td>
				<td><ct:select styleClass="typePropriete"  name="champTypeDePropriete" notation="data-g-select='mandatory:true'">
						<ct:optionsCodesSystems csFamille="PCTYPPROP">
							<ct:excludeCode code="64009003"/>
						</ct:optionsCodesSystems>
					</ct:select>
				 </td>
				<td><ct:FWLabel key="JSP_PC_CAPITAL_LPP_D_PART"/></td>
				<td><input type="text" class="part" value="1/1" data-g-string="mandatory:true"/></td>
				<td><ct:FWLabel key="JSP_PC_CAPITAL_LPP_D_CAPITAL_LPP"/></td>
				<td><input type="text" class="capitalLPP" data-g-amount="mandatory:true, periodicity:Y"></td>			
			</tr>
			<tr>
				<td><ct:FWLabel key="JSP_PC_CAPITAL_LPP_D_NUMERO_POLICE"/></td>
				<td><input type="text" class="numeroPolice"  data-g-string="mandatory:true" /></td>				
				<td><ct:FWLabel key="JSP_PC_CAPITAL_LPP_D_INSTITUTION"/></td>
				<td>
					<input type="hidden" class="institution" />	
					<ct:widget id='<%="institutionWidget"+membreFamille.getId()%>' 
					           name='<%="institutionWidget"+membreFamille.getId()%>' 
					           styleClass="libelleLong selecteurInstitution">
						<ct:widgetService methodName="find" className="<%=AdministrationService.class.getName()%>">										
							<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_CAPITAL_LPP_W_INSTITUTION_NOM"/>								
							<ct:widgetCriteria criteria="forCodeAdministrationLike" label="JSP_PC_BIEN_IMMOBILIER_SERVANT_HABITATION_PRINCIPALE_W_COMPAGNIE_ADMIN_CODE"/>																
							<ct:widgetLineFormatter format="#{tiers.designation2} #{tiers.designation1}, #{tiers.idTiers}  - (#{cs(admin.codeAdministration)} #{admin.genreAdministration})"/>
							<ct:widgetJSReturnFunction>
								<script type="text/javascript">
									function(element){
										$(this).parents('.areaMembre').find('.institution').val($(element).attr('tiers.id'));
										this.value=$(element).attr('tiers.designation1');
									}
								</script>										
							</ct:widgetJSReturnFunction>
						</ct:widgetService>
					</ct:widget>			
				</td>																		
			</tr>
			<tr>
				<td><ct:FWLabel key="JSP_PC_CAPITAL_LPP_D_DATE_LIBERATION"/></td>
				<td><input type="text" name="dateLiberation" value="" data-g-calendar=" "/></td>		
				<td><ct:FWLabel key="JSP_PC_CAPITAL_LPP_D_DESTINATION_LIBERATIOn"/></td>
				<td><input type="text" class="destinationLiberation"/></td>		
			</tr>
			<tr>		
				<td><ct:FWLabel key="JSP_PC_CAPITAL_LPP_D_INTERET"/></td>
				<td><input type="checkbox" class="interet"/></td>	
				<td><ct:FWLabel key="JSP_PC_CAPITAL_LPP_D_MONTANT_INTERETS"/></td>
				<td><input type="text" class="montantInteret" data-g-amount="periodicity:Y"/></td>	
				<td><ct:FWLabel key="JSP_PC_CAPITAL_LPP_D_MONTANT_FRAIS"/></td>
				<td><input type="text" class="montantFrais" data-g-amount="periodicity:Y"/></td>																														
			</tr>
			<tr>
				<td><ct:FWLabel key="JSP_PC_CAPITAL_LPP_D_DESSAISISSEMENT_FORTUNE"/></td>
				<td><input type="checkbox" class="dessaisissementFortune" /></td>
				<td><ct:FWLabel key="JSP_PC_CAPITAL_LPP_D_DESSAISISSEMENT_REVENU"/></td>
				<td><input type="checkbox" class="dessaisissementRevenu" /></td>
			</tr>
			<tr>
				<td><ct:FWLabel key="JSP_PC_CAPITAL_LPP_D_DATE_DEBUT"/></td>
				<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month"/></td>
				<td><ct:FWLabel key="JSP_PC_CAPITAL_LPP_D_DATE_FIN"/></td>
				<td><input type="text" name="dateFin" value="" data-g-calendar="type:month"/></td>
			</tr>
		</table>	
		<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_CAPITAL_LPP_AJAX%>" crud="cud">
			<%@ include file="/pegasusRoot/droit/commonButtonDF.jspf" %>
		</ct:ifhasright>
	</div>
	</div>
	<%
		}
	%>
	</div>
	</div>
	</TD>
</TR>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf"%>
<%-- /tpl:insert --%>