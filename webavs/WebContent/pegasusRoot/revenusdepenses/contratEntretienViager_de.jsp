<%-- tpl:insert page="/theme/detail.jtpl" --%>
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
<%@page
	import="globaz.pegasus.vb.revenusdepenses.PCContratEntretienViagerViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Vector"%>
<%@page
	import="ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViager"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%
	//Les labels de cette page commencent par le préfix "JSP_PC_PRET_TIERS_D"
	idEcran = "PPC0103";

	PCContratEntretienViagerViewBean viewBean = (PCContratEntretienViagerViewBean) session
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
	import="ch.globaz.pegasus.business.models.revenusdepenses.SimpleContratEntretienViager"%>	
<%@page
	import="ch.globaz.pegasus.business.models.revenusdepenses.ContratEntretienViager"%>

<%@page
	import="ch.globaz.pegasus.business.models.revenusdepenses.RevenusDepenses"%>
<%@page
	import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.ObjectOutput"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="org.apache.commons.codec.binary.Hex"%>
<%@page
	import="globaz.pegasus.vb.revenusdepenses.PCContratEntretienViagerAjaxViewBean"%>

<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.ContratEntretienViager"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>



<%@page import="globaz.pegasus.utils.PCCommonHandler"%><script language="JavaScript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var PAGE_ID_DROIT="<%=viewBean.getId()%>";
	var ACTION_AJAX_CONTRAT_ENTRETIEN_VIAGER="<%=IPCActions.ACTION_DROIT_CONTRAT_ENTRETIEN_VIAGER_AJAX%>";

	<%=viewBean.getLibelleInnerJavascript()%>

	function postInit(){
		<%=viewBean.getLibelleAssociationInnerJavascript()%>
	}

	
$(function(){	

	//au depart toutes décocher
	$(".libelleId").next('.multiSelectOptions').find('INPUT').attr("checked",false);
		
	//gestion de la liste déroulante de checkbox
	
		$('.libelleId').multiSelect({  
	        selectAll: false, 
	        noneSelected: "Pas de sélection",  
	        oneOrMoreSelected: "Sélection"
	 	},
		 function() {
			 	//Fonction appelée à chaque case cochée ou décochée
			 	
			 	//Récupération des éléments cochés
				var selected=new Array();
			 	$('.libelleId').next('.multiSelectOptions').find('INPUT:checkbox:checked').not('.optGroup, .selectAll').each(function() {
					selected.push($(this).attr('value'));
				});			 	
				
				var resumeLibelleElem = $("#resumeLibelleId");
				var listLibelle = $("[name=listLibelleInput]");
	
			
				
		});
	
});

</script>


<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/droit/ContratEntretienViager_MembrePart.js" /></script>	
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/droit/ContratEntretienViager_de.js" /></script>

<LINK rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/jquery.multiSelect-1.2.2/jquery.multiSelect.css"/>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.multiSelect-1.2.2/jquery.multiSelect.js"></script>


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
					<th><ct:FWLabel key="JSP_PC_CONTRAT_ENTRETIEN_VIAGER_L_MONTANT_CONTRAT" /></th>
					<th><ct:FWLabel key="JSP_PC_CONTRAT_ENTRETIEN_VIAGER_L_LIBELLE" /></th>
					<th data-g-cellformatter="css:formatCellIcon" ><ct:FWLabel key="JSP_PC_CONTRAT_ENTRETIEN_VIAGER_L_DR" /></th>
					<th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_CONTRAT_ENTRETIEN_VIAGER_L_PERIODE" /></th>
				</tr>
			</thead>
			<tbody>
				<%
						FWCurrency montantContrat = new FWCurrency("0.00");
						SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager = new SimpleLibelleContratEntretienViager();
						//String currentId = "-1";
						String idGroup=null;
						int k = 0;
						for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){												
							
							RevenusDepenses donneeComplexe=(RevenusDepenses)itDonnee.next();				
							
							//ContratEntretienViager donneeALDComplexe=(ContratEntretienViager)itDonnee.next();//getContratEntretienViager();//
							SimpleContratEntretienViager donnee= (SimpleContratEntretienViager)donneeComplexe.getSimpleContratEntretienViager();//donneeComplexe.getDonneeFinanciere();// 
							SimpleDonneeFinanciereHeader dfHeader=donneeComplexe.getSimpleDonneeFinanciereHeader();
												
							if(!dfHeader.getIdEntityGroup().equals(idGroup)){
								idGroup=null;
							}						
							
							//formatage des données numériques
							montantContrat = new FWCurrency(donnee.getMontantContrat());
							
							//récupération des frais
							StringBuffer listeLibelle = new StringBuffer(); 
							for(int count = 0 ; count < ((String[])viewBean.getListeLibelle().get(k)).length;count++){
								listeLibelle.append(objSession.getCodeLibelle(((String[])viewBean.getListeLibelle().get(k))[count]));
								listeLibelle.append("<br/>");
							}							
	
				%>		
				<tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
					<td>&#160;</td>	
					<td style="text-align:right;"><%=montantContrat.toStringFormat() %></td>
					<td><%=listeLibelle.toString()%></td>
					<td align="center" >
						<% if(dfHeader.getIsDessaisissementRevenu().booleanValue()){%>
						<IMG src="<%=request.getContextPath()+"/images/ok.gif" %>"/>
						<%} else {
							%>&#160;<%
						}%>
					</td>											
					<td><%=dfHeader.getDateDebut()%> - <%=dfHeader.getDateFin()%></td>
				</tr>
				<%
							k++;
							idGroup=dfHeader.getIdEntityGroup();
					}
				%>
			</tbody>
		</table>
		<div class="areaDFDetail">
			<table>
				<tr>
					<td><ct:FWLabel key="JSP_PC_CONTRAT_ENTRETIEN_VIAGER_D_MONTANT_CONTRAT" /></td>
					<td>
						<input type="text" class="montantContrat" data-g-amount="mandatory:true, periodicity:Y"/>
					</td>
					<td><ct:FWLabel key="JSP_PC_CONTRAT_ENTRETIEN_VIAGER_D_LIBELLE" /></td>			
		            <td>
		                <SELECT  class="libelleId"  multiple="multiple" style="width: 250px;">
		                <!-- séléctionner les 5 codes systemes : remplir un vector à partir des codes systemes -->                   
		                <%Vector csLibelle = viewBean.getCsLibelle();
		                  for (int i=0;i<csLibelle.size();i++){%>
		                	<OPTION value="<%=((String[]) csLibelle.get(i))[0]%>"><%=((String[]) csLibelle.get(i))[1]%></OPTION>
		                <%}%>
		                </SELECT>
		            </td>						
				</tr>
				<tr>
					<td><ct:FWLabel key="JSP_PC_CONTRAT_ENTRETIEN_VIAGER_D_DR" /></td>
					<td><input type="checkbox" class="dessaisissementRevenu" /></td>
				</tr>		
				<tr>
					<td><ct:FWLabel key="JSP_PC_CONTRAT_ENTRETIEN_VIAGER_D_DATE_DEBUT" /></td>
					<td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month" /></td>
					<td><ct:FWLabel key="JSP_PC_CONTRAT_ENTRETIEN_VIAGER_D_DATE_FIN" /></td>
					<td><input type="text" name="dateFin" value="" data-g-calendar="type:month" /></td>
				</tr>
			</table>
			<ct:ifhasright element="<%=IPCActions.ACTION_DROIT_CONTRAT_ENTRETIEN_VIAGER_AJAX%>" crud="cud">
				<%@ include file="/pegasusRoot/droit/commonButtonDF.jspf" %>
			</ct:ifhasright>
		</div>
	</div>
	<%
		j++;
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