<%@page import="ch.globaz.utils.CommonNSSFormater"%>
<%@page import="ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCODebiteur"%>
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

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/annoncesedex/baseObjectsCO.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/annoncesedex/detailsCO2.js"/></script>

<div id="dlgAnnonceDetailCO2" title="Affichage detail SEDEX CO2">
	<table id="tblAnnonceContainerCO2"></table>
</div>

<div id="conteneurComplexAnnonceSedex" style="overflow: auto; height: 600px;">
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

		<tr style="background-color:#B3C4DB" >
			<td colspan="10"></td>
		</tr>

<!-- /////////////////////////////////////////////////////////////////////////////////////////////////////// -->
<!-- ////////////////////////		Start: Popup section related /////////////////////////////////////////// -->
<!-- /////////////////////////////////////////////////////////////////////////////////////////////////////// -->
		<%
			CommonNSSFormater nssFormater = new CommonNSSFormater();
			long latestAnnonce = 0;
			for(int iAnnonce = 0; iAnnonce<annoncesCOSearch.getSize();iAnnonce++){
			    ComplexAnnonceSedexCO2 currentAnnonce = (ComplexAnnonceSedexCO2)annoncesCOSearch.getSearchResults()[iAnnonce];
			    String tmpMess = currentAnnonce.getSimpleAnnonceSedexCO().getIdAnnonceSedexCO();
			    
			    long messageId = 0;
				if(tmpMess != null){
			    	messageId = Long.parseLong(tmpMess);
				}
				
			    if (latestAnnonce == 0 || messageId > latestAnnonce){
			        latestAnnonce = messageId;
			    }
			}
			
			String tmpAnnonceSedex = "";
			String annonceId = "";
			String csMess = "";
			String title = "";
			String subtitle = "";
			String interets = "";
			String frais = "";
			String total = "";
			String noAvs = "";
			String nomPrenom = "";
			int assureCounter = 0;

			for(int iAnnonce = 0; iAnnonce<annoncesCOSearch.getSize();iAnnonce++){
			    ComplexAnnonceSedexCO2 currentAnnonce = (ComplexAnnonceSedexCO2)annoncesCOSearch.getSearchResults()[iAnnonce];
			    csMess = currentAnnonce.getSimpleAnnonceSedexCO().getIdAnnonceSedexCO();
			    
			    if(String.valueOf(latestAnnonce).equals(csMess)){
				    SimpleAnnonceSedexCODebiteur currentDebiteur = (SimpleAnnonceSedexCODebiteur)currentAnnonce.getSimpleAnnonceSedexCODebiteur();
				    SimpleAnnonceSedexCOAssure currentAssure = (SimpleAnnonceSedexCOAssure)currentAnnonce.getSimpleAnnonceSedexCOAssure();
				    String assureNss = currentAssure.getNssAssure();
				    String debiteurNss = currentDebiteur.getNssDebiteur();
				    
				    String messSubType = currentAnnonce.getSimpleAnnonceSedexCO().getMessageSubType();
					String creanceConst = AMMessagesSubTypesAnnonceSedexCO.CREANCE_AVEC_GARANTIE_DE_PRISE_EN_CHARGE.getValue();
					String subtypeLibelle = currentAnnonce.getSimpleAnnonceSedexCO().getMessageSubTypeLibelle();
					
					
					boolean isDebiteur = debiteurNss.equals(assureNss);
					boolean isCreance = messSubType.equals(creanceConst);
					annonceId = csMess;
					if(isDebiteur){
						title = "Détail annonce # " + csMess;
						subtitle = isCreance  ? "Créance avec garantie de prise en charge" :"";
						interets = currentAnnonce.getSimpleAnnonceSedexCODebiteur().getInterets();
						frais = currentAnnonce.getSimpleAnnonceSedexCODebiteur().getFrais();
						total = currentAnnonce.getSimpleAnnonceSedexCODebiteur().getTotal();
						noAvs = nssFormater.format(debiteurNss);
						nomPrenom = currentDebiteur.getNomPrenomDebiteur() ;
					}
					String avsAssure = nssFormater.format(currentAssure.getNssAssure());
					String nomPrenomAssure= currentAssure.getNomPrenomAssure();
					// Loop over Prime & Participation if any of them (Element)
					String description = "";
					String startDate = "";
					String endDate = "";
					String value = currentAssure.getPrimeMontant();
					int countElement = 0;
				%>
					<script>
						var detailAssure = new Array; 
					</script>
				<%
					double tmpValue = 0;
					if(value != null){
						tmpValue = Double.parseDouble(value);
					}
					if(tmpValue > 0){
					    description = "Prime";
					    startDate = currentAssure.getPrimePeriodeDebut();
					    endDate = currentAssure.getPrimePeriodeFin() ;
				%>
						<script>
							detailAssure['<%=countElement%>'] = new DetailFinance('<%=description%>', '<%=startDate%>', '<%=endDate%>', '<%=value%>');
						</script>
				<%
						countElement++;
					}
		
					value = currentAssure.getCostSharingMontant();
					if(value != null){
						tmpValue = Double.parseDouble(value);
					}
					if(tmpValue > 0){
					    description = "Participation";
					    startDate = currentAssure.getCostSharingPeriodeDebut();
					    endDate = currentAssure.getCostSharingPeriodeFin();
				%>
						<script>
							detailAssure['<%=countElement%>'] = new DetailFinance('<%=description%>', '<%=startDate%>', '<%=endDate%>', '<%=value%>');
						</script>
				<%}	%>
			<!-- Load assure information -->
			<script>
				var assure = new Personne('<%=avsAssure%>','<%=nomPrenomAssure%>');
				assureArray['<%=assureCounter%>'] = new DetailAssure(assure, detailAssure);
			</script>
			<%assureCounter++;
			}//End if latest annonce
		}// End for %>
 		<script>
			var debiteur = new Personne('<%=noAvs%>','<%=nomPrenom%>');
			var annonceDetail = new Annonce('<%=csMess%>', '<%=title%>', '<%=subtitle%>','<%=interets%>','<%=frais%>','<%=total%>', debiteur, assureArray);
			assureArray = new Array; // Reset object
		</script>
<!-- /////////////////////////////////////////////////////////////////////////////////////////////////////// -->
<!-- ////////////////////////		End: Popup section related   /////////////////////////////////////////// -->
<!-- /////////////////////////////////////////////////////////////////////////////////////////////////////// -->

		<%		
			for(int iAnnonce = 0; iAnnonce<annoncesCOSearch.getSize();iAnnonce++){
			    ComplexAnnonceSedexCO2 currentAnnonce = (ComplexAnnonceSedexCO2)annoncesCOSearch.getSearchResults()[iAnnonce];
			    csMess = currentAnnonce.getSimpleAnnonceSedexCO().getIdAnnonceSedexCO();
			    
			    if(String.valueOf(latestAnnonce).equals(csMess)){
				    SimpleAnnonceSedexCODebiteur currentDebiteur = (SimpleAnnonceSedexCODebiteur)currentAnnonce.getSimpleAnnonceSedexCODebiteur();
				    SimpleAnnonceSedexCOAssure currentAssure = (SimpleAnnonceSedexCOAssure)currentAnnonce.getSimpleAnnonceSedexCOAssure();
				    String assureNss = currentAssure.getNssAssure();
				    String debiteurNss = currentDebiteur.getNssDebiteur();
				    
				    String messSubType = currentAnnonce.getSimpleAnnonceSedexCO().getMessageSubType();
					String creanceConst = AMMessagesSubTypesAnnonceSedexCO.CREANCE_AVEC_GARANTIE_DE_PRISE_EN_CHARGE.getValue();
					String subtypeLibelle = currentAnnonce.getSimpleAnnonceSedexCO().getMessageSubTypeLibelle();
					interets = currentAnnonce.getSimpleAnnonceSedexCODebiteur().getInterets();
					frais = currentAnnonce.getSimpleAnnonceSedexCODebiteur().getFrais();
					total = currentAnnonce.getSimpleAnnonceSedexCODebiteur().getTotal();
						
					boolean isDebiteur = debiteurNss.equals(assureNss);
					boolean isCreance = messSubType.equals(creanceConst);
					
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
			
			if (isCreance){ %>
				<a style="color:blue" onclick="showdetailCO2(<%=csMess%>);" href="#<%=csMess%>">
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
				<td><%=interets%></td>
	
				<!--  Frais créance || vide -->
				<td><%=frais%></td>
	
				<!--  Total créance || vide  -->
				<td><%=total%></td>
			<%}else{%>
				<td/><td/><td/>
			<%}%>
		</tr>

		<%
			    } //End if latestAnnonce
			} //End for
		} // end if null object
		%>
		<tr style="background-color:#B3C4DB"><td colspan="8"></td></tr>
	</table>						
</div>