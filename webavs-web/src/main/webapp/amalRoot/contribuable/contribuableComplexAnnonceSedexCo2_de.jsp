<%@page import="globaz.amal.utils.AMContribuableHistoriqueHelper"%>
<%@page import="ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOAssure"%>
<%@page import="ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO2"%>
<%@page import="ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO2Search"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="globaz.amal.utils.AMSedexHelper"%>
<%@page import="ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex"%>
<%@page import="ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedexCO"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTraitementsAnnonceSedex"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme.AMStatutAnnonceSedex"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedexSearch"%>
<%@page import="ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedex"%>

<!-- Contains the contribuable actions -->
<%@ include file="/amalRoot/contribuable/contribuableHeader.jspf" %>

<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/amal.css" rel="stylesheet"/>

<script type="text/javascript">

function Personne(_noAvs, _prenom, _nom){
	this.avs = _noAvs;
	this.prenom = _prenom;
	this.nom = _nom;
}

function DetailFinance(_description, _startDate, _endDate, _valeur){
	this.description = _description; // Prime or Participation
	this.startDate = _startDate;
	this.endDate = _endDate;
	this.valeur = _valeur;
}

function DetailAssure(_objAssure, _objPrime){
	this.assure = _objAssure; // Type Personne
	this.prime = _objPrime; // Type DetailFinance
}

function Annonce(_title, _subtitle, _interets, _frais, _total, _objDebiteur/*, _objAssure*/) {
	this.title = _title;
	this.subtitle = _subtitle;
	this.interets = _interets;
	this.frais = _frais;
	this.total = _total;
	this.debiteur = _objDebiteur; // Type personne seulement 1 element
	//this.assure = _objAssure; // Collection type DetailAssure
}

<%
	String title = "Détail annonce #";
	String subtitle = "Créance avec garantie de prise en charge";
	String interets = String.valueOf(3);
	String frais = String.valueOf(10);
	String total = String.valueOf(13);
	String noAvs = "756.4567.8912.33";
	String prenom = "Johnn Arnold";
	String nom = "Smidth Johannensons";
%>
var debiteur = new Personne('<%=noAvs%>','<%=prenom%>','<%=nom%>');
var annonceDetail = new Annonce('<%=title%>', '<%=subtitle%>','<%=interets%>','<%=frais%>','<%=total%>', debiteur);

</script>


<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/annoncesedex/detailsCO2.js"/></script>

<div id="dlgAnnonceDetail" title="Affichage detail SEDEX CO2">
	<table id="tblAnnonceContainer"></table>
</div>

<div id="conteneurComplexAnnonceSedex">
	<table width="100%" border="0">
		<col align="center" style="font-weight: bold"></col> 	<!-- Date -->
		<col align="center"></col>								<!-- Assureur -->
		<col align="left"></col>								<!-- Message -->
		<col align="center"></col>								<!-- Status -->
		<col align="left"></col>								<!-- Membre famille -->
		<col align="right"></col>								<!-- Intérêts -->
		<col align="right"></col>								<!-- Frais -->
		<col align="right"></col>								<!-- Total créance -->
		<tr>
			<th>Date</th>
			<th>Assureur</th>
			<th>Message</th>
			<th>Status</th>
			<th>Membre</th>
			<th>Intérêts</th>
			<th>Frais</th>
			<th>Total créance</th>
		</tr>
		<%
			HashMap<String, String> listIdsDetailFamille = viewBean.getIdDetailFamilleSedex();
			String rowStyle = "amalRowOdd";
			ComplexAnnonceSedexCO2Search annoncesCOSearch = viewBean.getAnnonceSedexCO2();
			if(annoncesCOSearch != null){%>
			
<!--
Stand by code if any filter is implement later

		<tr class="amalRowOdd" behaviour="search" style="height:26px; display:<%=annoncesCOSearch.getSize() > 0? "":"none"%>;" >
			<td colspan="10" align="left">
				<select id="selectSedex">
					<option value="all"></option>
				 <%for (String idDetailFamille : listIdsDetailFamille.keySet()) { %>
					<option value="<%=idDetailFamille%>"><%=listIdsDetailFamille.get(idDetailFamille) %></option>
				<%} %> 
				</select>
			</td>							
		</tr>
-->
		<tr style="background-color:#B3C4DB" >
			<td colspan="10"></td>
		</tr>
		<%
			for(int iAnnonce = 0; iAnnonce<annoncesCOSearch.getSize();iAnnonce++){
			    ComplexAnnonceSedexCO2 currentAnnonce = (ComplexAnnonceSedexCO2)annoncesCOSearch.getSearchResults()[iAnnonce];
			    SimpleAnnonceSedexCOAssure currentAssure = (SimpleAnnonceSedexCOAssure)currentAnnonce.getSimpleAnnonceSedexCOAssure();
			    String debiteurId = viewBean.getContribuable().getId();
			    String currentId = currentAssure.getIdContribuable();
			    boolean isDebiteur = debiteurId.equals(currentId);
				if(iAnnonce%2==0){
					rowStyle = "amalRow";
				}else{
					rowStyle = "amalRowOdd";
				}
		%>
 
		<tr style="height:26px" class="<%=rowStyle%>" onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'amalRowHighligthed')" onMouseOut="jscss('swap', this, 'amalRowHighligthed', '<%=rowStyle%>')">
			<!-- Date -->
			<td><%=currentAnnonce.getSimpleAnnonceSedexCO().getDateAnnonce()%></td>

			<!-- Assureur -->
			<td><%=currentAnnonce.getCaisseMaladie().getTiers().getDesignation1() %></td>

			<!-- Message subtype => Pas poursuivre || Créance garantie -->
			<!-- Seulement créance avec garantie auras de liens sur le détail -->
			<td>
			<%
			String messSubType = currentAnnonce.getSimpleAnnonceSedexCO().getMessageSubType();
			String creanceConst = AMMessagesSubTypesAnnonceSedexCO.CREANCE_AVEC_GARANTIE_DE_PRISE_EN_CHARGE.getValue();
			String subtypeLibelle = currentAnnonce.getSimpleAnnonceSedexCO().getMessageSubTypeLibelle();
			String csMess = currentAnnonce.getSimpleAnnonceSedexCO().getIdAnnonceSedexCO();
			long messageId = 0;
			if(csMess != null){
		    	messageId = Long.parseLong(csMess);
			}
			if (messSubType.equals(creanceConst)){ %>
				<a style="color:blue" onclick="showdetail(<%=messageId%>);" href="#">
					<%=subtypeLibelle%>
				</a>
				<%} else { %>
					<%=subtypeLibelle%>
			<%}%>
			</td>

			<!-- Message status In || Out -->
			<td>
				<%
					
					String status = currentAnnonce.getSimpleAnnonceSedexCO().getStatus();
					String imgName = AMStatutAnnonceSedex.getStatusImageName(status);
					String libelleImg = AMStatutAnnonceSedex.getStatusImageLabel(status);
					if (!JadeStringUtil.isEmpty(imgName)) { 
				%>
				<img width="20px" height="20px" title="<%=libelleImg%>" border="0"
						src="<%=request.getContextPath()%>/images/amal/<%=imgName%>">
				<% } else { %>
					&nbsp;
				<% } %>
			</td>

			<!-- Nom, Prénom Membre famille -->
			<%
				String nomMembre= "";
				if(isDebiteur){
					if(!contribReprise){
					    nomMembre = viewBean.getContribuable().getPersonneEtendue().getTiers().getDesignation1();
					    nomMembre += " " + viewBean.getContribuable().getPersonneEtendue().getTiers().getDesignation2();
					}else{
					    nomMembre = AMContribuableHistoriqueHelper.getContribuableInfos().getNom();
					    nomMembre += " " + AMContribuableHistoriqueHelper.getContribuableInfos().getPrenom();
					}
				}
				else{
				    nomMembre = currentAssure.getNomPrenomAssure();
				}%>
				
			<td><%=nomMembre%></td>
			<%if(isDebiteur){ %>
				<!--  Intérêts créance || vide  -->
				<td><%=currentAnnonce.getSimpleAnnonceSedexCODebiteur().getInterets()%></td>
	
				<!--  Frais créance || vide -->
				<td><%=currentAnnonce.getSimpleAnnonceSedexCODebiteur().getFrais()%></td>
	
				<!--  Total créance || vide  -->
				<td><%=currentAnnonce.getSimpleAnnonceSedexCODebiteur().getTotal()%></td>
			<%}else{%>
				<td/><td/><td/>
			<%}%>
		</tr>

		<%
			} //End for
		} // end if null object
		%>
		<tr style="background-color:#B3C4DB"><td colspan="8"></td></tr>
	</table>						
</div>