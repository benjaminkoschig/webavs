<%@page import="ch.globaz.utils.CommonNSSFormater"%>
<%@page	import="ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCODebiteur"%>
<%@page import="globaz.amal.utils.AMContribuableHistoriqueHelper"%>
<%@page	import="ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOAssure"%>
<%@page	import="ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO2"%>
<%@page	import="ch.globaz.amal.business.models.annoncesedexco.AnnonceSedexCOAssureContainer"%>
<%@page	import="ch.globaz.amal.business.models.annoncesedexco.AnnonceSedexCO2Detail"%>
<%@page	import="ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO2Search"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="globaz.amal.utils.AMSedexHelper"%>
<%@page	import="ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex"%>
<%@page	import="ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedexCO"%>
<%@page	import="ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTraitementsAnnonceSedex"%>
<%@page	import="ch.globaz.amal.business.constantes.IAMCodeSysteme.AMStatutAnnonceSedex"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page	import="ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedexSearch"%>
<%@page	import="ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedex"%>

<!-- Contains the contribuable actions -->
<%@ include file="/amalRoot/contribuable/contribuableHeader.jspf"%>

<link rel="stylesheet" type="text/css"
	href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/amal.css"
	rel="stylesheet" />

<script type="text/javascript"
	src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/annoncesedex/baseObjectsCO.js" /></script>
<script type="text/javascript"
	src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/annoncesedex/detailsCO2.js" /></script>

<div id="dlgAnnonceDetailCO2" title="Affichage detail SEDEX CO2">
	<table id="tblAnnonceContainerCO2"></table>
</div>

<div id="conteneurComplexAnnonceSedex"
	style="overflow: auto; height: 600px;">
	<table width="100%" border="0">
		<!-- Année -->			<col align="center" style="font-weight: bold"></col>
		<!-- Date -->			<col align="center"></col>	
		<!-- Assureur -->		<col align="center"></col>
		<!-- Message -->		<col align="left"></col>
		<!-- Status -->			<col align="center"></col>
		<!-- Membre famille -->	<col align="left"></col>
		<!-- Intérêts -->		<col align="right"></col>
		<!-- Frais -->			<col align="right"></col>
		<!-- Total créance -->	<col align="right"></col>
		<tr>
			<th>Année</th>
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
		    String rowStyle = "amalRowOdd";
					List<AnnonceSedexCO2Detail> annoncesCO2List = viewBean.getAnnoncesSedexCO2();
		%>
		<!-- /////////////////////////////////////////////////////////////////////////////////////////////////////// -->
		<!-- ////////////////////////		Start: Load Popup data /////////////////////////////////////////// -->
		<!-- /////////////////////////////////////////////////////////////////////////////////////////////////////// -->
		<%
		    String csMess = "";
					String title = "";
					String subtitle = "";
					String interets = "";
					String frais = "";
					String total = "";
					String nssDebiteur = "";
					String nomPrenomDebiteur = "";
					int annonceIdx = 0;
		%>
			<script>
				var annonceDetail = new Array; 
			</script>
			<%
			    for (AnnonceSedexCO2Detail itemCO2Detail : annoncesCO2List) { 
					    	csMess = itemCO2Detail.getIdAnnonceCO();
							String subtypeLibelle = itemCO2Detail.getLibelleAnnonce();
							title = "Détail annonce # " + csMess;
							nssDebiteur = itemCO2Detail.getDebiteur().getFormattedNss();
							if (!contribReprise) {
							    nomPrenomDebiteur = itemCO2Detail.getDebiteur().getFormattedName();
							} else {
							    nomPrenomDebiteur = itemCO2Detail.getDebiteur().getFormattedNameReprise();
							}
							if(itemCO2Detail.getIsCreance()){
								subtitle = itemCO2Detail.getLibelleAnnonce();
								interets = itemCO2Detail.getInterets();
								frais = itemCO2Detail.getFrais();
								total = itemCO2Detail.getTotalCreance();
			%>
					<script>
						var detailAssure = new Array; 
					</script>
					<%
					    String description = "";
										String startDate = "";
										String endDate = "";
										String value = "";
										int assureIdx = 0;
										for(AnnonceSedexCOAssureContainer assureItem : itemCO2Detail.getAssureList()){
										    int detailIdx = 0;
											String nssAssure = assureItem.getFormattedNss();
											String nomAssure = assureItem.getFormattedName();
											if(!assureItem.getMontantPrime().isEmpty()){
										        description = "Prime";
												startDate = assureItem.getDaDebutPrime().getSwissValue();
												endDate = assureItem.getDaFinPrime().getSwissValue();
												value = assureItem.getMontantPrime();
					%>
							<script>
								detailAssure['<%=detailIdx%>'] = new DetailFinance('<%=description%>', '<%=startDate%>', '<%=endDate%>', '<%=value%>');
							</script>
							<%
							detailIdx++;
					    }
					    if(!assureItem.getMontantParticipation().isEmpty()){
					        description = "Participation";
							startDate = assureItem.getDaDebutParticipation().getSwissValue();
							endDate = assureItem.getDaFinParticipation().getSwissValue();
							value = assureItem.getMontantParticipation();
							%>
							<script>
								detailAssure['<%=detailIdx%>'] = new DetailFinance('<%=description%>', '<%=startDate%>', '<%=endDate%>', '<%=value%>');
							</script>
							<%
					    }
					    %>
						<!-- Load assure information -->
						<script>
							var assure = new Personne('<%=nssAssure%>','<%=nomAssure%>');
							assureArray['<%=assureIdx%>'] = new DetailAssure(assure, detailAssure);
						</script>
						<%
						assureIdx++;
					}
				} else {
				    subtitle = itemCO2Detail.getLibelleAnnonce();
					interets = "";
					frais = "";
					total = "";
				} // End if: isCreance
				%>
				<script>
					var debiteur = new Personne('<%=nssDebiteur%>','<%=nomPrenomDebiteur%>');
					annonceDetail[<%=annonceIdx%>] = new Annonce('<%=csMess%>', '<%=title%>', '<%=subtitle%>','<%=interets%>','<%=frais%>','<%=total%>', debiteur, assureArray);
					
					assureArray = new Array; // Reset object
				</script>
				<%
				annonceIdx++;
			} // End for over: annoncesCO2List
		%>
		<!-- /////////////////////////////////////////////////////////////////////////////////////////////////////// -->
		<!-- ////////////////////////		End: Load Popup data   /////////////////////////////////////////// -->
		<!-- /////////////////////////////////////////////////////////////////////////////////////////////////////// -->
		
		<tr style="background-color: #B3C4DB">
			<td colspan="9"></td>
		</tr>

		<%	
		Integer iAnnonce = 0;
		for (AnnonceSedexCO2Detail itemCO2Detail : annoncesCO2List) { 
	 		if (iAnnonce % 2 == 0) {
                   rowStyle = "amalRow";
               } else {
                   rowStyle = "amalRowOdd";
             	}
      	%>

		<tr style="height: 26px" class="<%=rowStyle%>"
			onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'amalRowHighligthed')"
			onMouseOut="jscss('swap', this, 'amalRowHighligthed', '<%=rowStyle%>')">
			
			<!-- Année -->
			<td><%=itemCO2Detail.getAnneeAnnonce()%></td>

			<!-- Date -->
			<td><%=itemCO2Detail.getDaAnnonce()%></td>
			<!-- Assureur -->
			<td><%=itemCO2Detail.getCaisseMaladie()%></td>

			<!-- Message -->
			<td>
				<a style="color: blue" onclick="showdetailCO2(<%=iAnnonce%>);" href="#<%=iAnnonce%>">
					<%=itemCO2Detail.getLibelleAnnonce()%>
				</a>
			</td>

			<!-- Status -->
			<td>
			<%
			    String status = itemCO2Detail.getStatus();
                String imgName = AMStatutAnnonceSedex.getStatusImageName(status);
                String libelleImg = AMStatutAnnonceSedex.getStatusImageLabel(status);
                if (!JadeStringUtil.isEmpty(imgName)) {
			%> 
				<img width="20px" height="20px" title="<%=libelleImg%>" border="0" src="<%=request.getContextPath()%>/images/amal/<%=imgName%>">
			<%
			    } else {
			%> &nbsp;
			<% 	} %>			
 			</td>

			<!-- Membre -->
			<td>
			<%if (!contribReprise) {%>
				<%=itemCO2Detail.getDebiteur().getFormattedName()%>
			<%} else {%>
				<%=itemCO2Detail.getDebiteur().getFormattedNameReprise()%>
			<%}%>
			</td>
			
			<!--  Intérêts créance -->
			<td><%=itemCO2Detail.getInterets()%></td>

			<!--  Frais créance || vide -->
			<td><%=itemCO2Detail.getFrais()%></td>

			<!--  Total créance || vide  -->
			<td><%=itemCO2Detail.getTotalCreance()%></td>
			<td />
			<td />
			<td />
		</tr>

		<% 
			iAnnonce++;
		} //End for	%>
		<tr style="background-color: #B3C4DB">
			<td colspan="8"></td>
		</tr>
	</table>
</div>