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

<%@page import="globaz.pegasus.vb.revenusdepenses.PCAllocationsFamilialesViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat"%>
<%@page import="globaz.framework.util.FWCurrency"%>

<%@page import="java.util.Arrays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="ch.globaz.pegasus.business.models.droit.DroitMembreFamille"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.SimpleAllocationsFamiliales"%>	
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamiliales"%>

<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.RevenusDepenses"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page	import="globaz.pegasus.vb.revenusdepenses.PCAllocationsFamilialesAjaxViewBean"%>

<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamiliales"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%
	//Les labels de cette page commencent par le pr?fix "JSP_PC_PRET_TIERS_D"
	idEcran = "PPC0103";

	PCAllocationsFamilialesViewBean viewBean = (PCAllocationsFamilialesViewBean) session
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


<%@page import="globaz.pegasus.utils.PCCommonHandler"%><script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId()%>";
	var ACTION_AJAX_ALLOCATIONS_FAMILIALES="<%=IPCActions.ACTION_DROIT_ALLOCATIONS_FAMILIALES_AJAX%>";
	
	$(function(){
		$('.cacher').hide();
	});
	
	var ID_TIERS_REQURANT ;
	$(function () {
		ID_TIERS_REQURANT = $("[idtiersmembrefamillerequerant]").attr("idtiersmembrefamillerequerant");
	});
	var getTitleForEchance = function ($element) { 
		var s_genreRent = $.trim($element.closest(".areaMembre").find(".areaTitre").text());
		var array = s_genreRent.split('/');
		var s_onglet = $(".onglets .selected").text();
		var libelle = "";
		if( array.length > 0) {libelle = s_onglet + " (" + array[1] + ")";}
		return libelle;
	}
</script>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/droit/AllocationsFamiliales_MembrePart.js" /></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/droit/AllocationsFamiliales_de.js" /></script>

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

	<%=PCDroitHandler.getOngletHtml(objSession,viewBean,IPCDroits.ONGLETS_REVENUS_DEPENSES,request,servletContext + mainServletPath)%>

	<div class="conteneurMembres">
							<% 
								int j = 0;
								for(Iterator itMembre=viewBean.getMembres().iterator();itMembre.hasNext();){
									MembreFamilleEtendu membreFamille=(MembreFamilleEtendu)itMembre.next();
							%>

	<div class="areaMembre" idMembre="<%=membreFamille.getId()%>">
	<div class="areaTitre">
		<%=PCDroitHandler.getFromattedTitreHTML(objSession,membreFamille)%>
	</div>	 
	<table class="areaDFDataTable" width="100%">
		<thead>
			<tr>
				<th data-g-cellformatter="css:formatCellIcon">&#160;</th>
				<th><ct:FWLabel	key="JSP_PC_ALLOCATIONS_FAMILIALES_L_MONTANT_MENSUEL" /></th>
				<th><ct:FWLabel key="JSP_PC_ALLOCATIONS_FAMILIALES_L_CAISSE_AF" /></th>
				<th><ct:FWLabel key="JSP_PC_ALLOCATIONS_FAMILIALES_L_DATE_ECHEANCE" /></th>				
				<th data-g-cellformatter="css:formatCellIcon"><ct:FWLabel key="JSP_PC_ALLOCATIONS_FAMILIALES_L_DR" /></th>
				<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_ALLOCATIONS_FAMILIALES_L_PERIODE" /></th>
			</tr>
		</thead>
		<tbody>
			<%
					FWCurrency montantMensuel = new FWCurrency("0.00");
			
					String currentId = "-1";
					String idGroup=null;
					for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){																	
						
						RevenusDepenses donneeALDComplexe=(RevenusDepenses)itDonnee.next();						 
						AllocationsFamiliales donnee = (AllocationsFamiliales)donneeALDComplexe.getAllocationsFamiliales();
						SimpleDonneeFinanciereHeader dfHeader=donneeALDComplexe.getSimpleDonneeFinanciereHeader();																		
						String nomCaisse="";
						if(donneeALDComplexe.getAllocationsFamiliales().getCaisse().getTiers().getDesignation1()!=null){						
							nomCaisse=donneeALDComplexe.getAllocationsFamiliales().getCaisse().getTiers().getDesignation1()+" "+donneeALDComplexe.getAllocationsFamiliales().getCaisse().getTiers().getDesignation2();
						}		
						
						if(!dfHeader.getIdEntityGroup().equals(idGroup)){
							idGroup=null;
						}						
						
						if(!donneeALDComplexe.getAllocationsFamiliales().getSimpleAllocationsFamiliales().getIdAllocationsFamiliales().equals(currentId)){
							currentId = donneeALDComplexe.getAllocationsFamiliales().getSimpleAllocationsFamiliales().getIdAllocationsFamiliales();							
							montantMensuel = new FWCurrency(donnee.getSimpleAllocationsFamiliales().getMontantMensuel());							

			%>		
			<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
				<td>&#160;</td>	
				<td  style="text-align:right;"><%=montantMensuel.toStringFormat() %></td>
				<td><%=nomCaisse %></td>
				<td><%=donnee.getSimpleAllocationsFamiliales().getDateEcheance() %></td>
				<td align="center" ><% if(dfHeader.getIsDessaisissementRevenu().booleanValue()){%>
					<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
					<%} else {
						%>&#160;<%
					}%>
				</td>											
				<td><%=dfHeader.getDateDebut()%> - <%=dfHeader.getDateFin()%></td>
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
			<td><ct:FWLabel key="JSP_PC_ALLOCATIONS_FAMILIALES_D_MONTANT_MENSUEL" /></td>
			<td>
				<input type="text" class="montantMensuel" name="montantMensuel" data-g-amount="mandatory:true, periodicity:M"/>
			</td>
			<td><ct:FWLabel key="JSP_PC_ALLOCATIONS_FAMILIALES_D_CAISSE_AF" /></td>
			<td>
				<input type="hidden" class="caisse" />	
				<ct:widget id='<%="caisseWidget"+membreFamille.getId()%>' notation="data-g-string='mandatory:true'" name='<%="caisseWidget"+membreFamille.getId()%>' styleClass="libelleLong selecteurCaisse">
					<ct:widgetService methodName="find" className="<%=AdministrationService.class.getName()%>">										
						<ct:widgetCriteria criteria="forCodeAdministrationLike" label="JSP_PC_ASSURANCE_RENTE_VIAGERE_W_TIERS_CS_ADMIN"/>								
						<ct:widgetCriteria criteria="forGenreAdministration" label="JSP_PC_ASSURANCE_RENTE_VIAGERE_W_TIERS_TYPE_ADMIN"/>								
						<ct:widgetLineFormatter format="#{tiers.designation2} #{tiers.designation1}, #{tiers.idTiers}  - (#{cs(admin.codeAdministration)} #{admin.genreAdministration})"/>
						<ct:widgetJSReturnFunction>
							<script type="text/javascript">
								function(element){
									$(this).parents('.areaMembre').find('.caisse').val($(element).attr('tiers.id'));
									this.value=$(element).attr('tiers.designation1');
								}
							</script>										
						</ct:widgetJSReturnFunction>
					</ct:widgetService>
				</ct:widget>			
			</td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PC_ALLOCATIONS_FAMILIALES_D_DATE_ECHEANCE" /></td>
			<!--<td><input type="text" name="dateEcheance" value="" data-g-calendar=" "/></td>  -->
			<td><input class="dateEcheance" name="dateEcheance" value="" 
					   data-g-echeance="idTiers: <%=membreFamille.getDroitMembreFamille().getMembreFamille().getPersonneEtendue().getTiers().getIdTiers()%>,
									    idExterne: <%=viewBean.getDroit().getSimpleDroit().getIdDemandePC()%>,
									    csDomaine: <%=viewBean.getEcheanceDomainePegasus()%>,	   
									    type: <%=viewBean.getTypeEcheance()%>,
									    position: right,
									   	libelle:   "/>
			</td>		
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PC_ALLOCATIONS_FAMILIALES_D_DR" /></td>
			<td><input type="checkbox" class="dessaisissementRevenu" /></td>
		</tr>		
		<tr>
			<td><ct:FWLabel key="JSP_PC_ALLOCATIONS_FAMILIALES_D_DATE_DEBUT" /></td>
			<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month" /></td>
			<td><ct:FWLabel key="JSP_PC_ALLOCATIONS_FAMILIALES_D_DATE_FIN" /></td>
			<td><input type="text" name="dateFin" value="" data-g-calendar="type:month" /></td>
		</tr>
	</table>
	<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_ALLOCATIONS_FAMILIALES_AJAX%>" crud="cud">
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