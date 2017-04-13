<%@page import="ch.globaz.utils.CommonNSSFormater"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="globaz.amal.utils.AMSedexHelper"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTraitementsAnnonceSedex"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme.AMStatutAnnonceSedex"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex"%>
<%@page import="ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOAssure"%>
<%@page import="ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCODebiteur"%>
<%@page import="ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO4"%>
<%@page import="ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO4Search"%>

<%@ include file="/amalRoot/contribuable/contribuableHeader.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/amal.css" rel="stylesheet"/>

<!-- /////////////////////////////////////////////////////////////////////////////////////////////////////// -->
<!-- ////////////////////////		Start: Popup section related /////////////////////////////////////////// -->
<!-- /////////////////////////////////////////////////////////////////////////////////////////////////////// -->
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/annoncesedex/baseObjectsCO.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/annoncesedex/detailsCO4.js"/></script>

<div id="dlgAnnonceDetailCO4" title="Affichage detail SEDEX CO4">
	<table id="tblAnnonceContainerCO4"></table>
</div>
<%	
	CommonNSSFormater nssFormater = new CommonNSSFormater();
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
	String subtypeLibelle = "";
	int assureCounter = 0;
	long latestAnnonce = 0;
	
	ComplexAnnonceSedexCO4Search annoncesCO4Search = viewBean.getAnnonceSedexCO4();

	for(int iAnnonce = 0; iAnnonce<annoncesCO4Search.getSize();iAnnonce++){
	    ComplexAnnonceSedexCO4 currentAnnonce = (ComplexAnnonceSedexCO4)annoncesCO4Search.getSearchResults()[iAnnonce];
	    String tmpMess = currentAnnonce.getSimpleAnnonceSedexCO().getIdAnnonceSedexCO();
	    
	    long messageId = 0;
		if(tmpMess != null){
	    	messageId = Long.parseLong(tmpMess);
		}
		
	    if (latestAnnonce == 0 || messageId > latestAnnonce){
	        latestAnnonce = messageId;
	    }
	}
	

	for(int iAnnonce = 0; iAnnonce<annoncesCO4Search.getSize();iAnnonce++){
	    ComplexAnnonceSedexCO4 currentAnnonce = (ComplexAnnonceSedexCO4)annoncesCO4Search.getSearchResults()[iAnnonce];
	    csMess = currentAnnonce.getSimpleAnnonceSedexCO().getIdAnnonceSedexCO();
	    
	    if(String.valueOf(latestAnnonce).equals(csMess)){
		    SimpleAnnonceSedexCODebiteur currentDebiteur = (SimpleAnnonceSedexCODebiteur)currentAnnonce.getSimpleAnnonceSedexCODebiteur();
		    SimpleAnnonceSedexCOAssure currentAssure = (SimpleAnnonceSedexCOAssure)currentAnnonce.getSimpleAnnonceSedexCOAssure();
		    String assureNss = currentAssure.getNssAssure();
		    String debiteurNss = currentDebiteur.getNssDebiteur();
		    String messSubType = currentAnnonce.getSimpleAnnonceSedexCO().getMessageSubType();
			subtypeLibelle = currentAnnonce.getSimpleAnnonceSedexCO().getMessageSubTypeLibelle();
			
			boolean isDebiteur = debiteurNss.equals(assureNss);
			annonceId = csMess;
			if(isDebiteur){
				title = "Détail annonce # " + csMess;
				subtitle = "TODO";
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
	var annonce = new Annonce('<%=csMess%>', '<%=title%>', '<%=subtitle%>','<%=interets%>','<%=frais%>','<%=total%>', debiteur, assureArray);
	var annonceCO4 = new AnnonceCO4(annonce, 'Assura', '30.10.2017');
	assureArray = new Array; // Reset object
</script>

<!-- /////////////////////////////////////////////////////////////////////////////////////////////////////// -->
<!-- ////////////////////////		End: Popup section related   /////////////////////////////////////////// -->
<!-- /////////////////////////////////////////////////////////////////////////////////////////////////////// -->

<div id="conteneurComplexAnnonceSedex" style="overflow: auto; height: 600px;">
	<table width="100%" border="0">
		<col align="left" style="font-weight: bold"></col>
		<col width="20px" align="center"></col>
		<col align="center"></col>
		<col align="left"></col>
		<col align="center"></col>
		<col align="center"></col>
		<col align="left"></col>
		<col align="left"></col>
		<col align="left"></col>
		<col align="left"></col>
		<col align="left"></col>
		<col align="left"></col>
		<tr>
			<th>Année</th>
			<th>Date</th>
			<th>Assureur</th>
			<th>Message</th>
			<th>Status</th>
			<th>Période</th>
			<th>Intérêts</th>
			<th>Frais</th>
			<th>Total créance</th>
			<th>Débiteur</th>
			<th>RP Rétro</th>
			<th>Annulation</th>
		</tr>
		<%
			String rowStyle = "amalRowOdd";
			if(annoncesCO4Search != null){%>
			
			<tr style="background-color:#B3C4DB">
				<td colspan="12"/>
			</tr>
			<%
			for(int iAnnonce = 0; iAnnonce<annoncesCO4Search.getSize();iAnnonce++){
			    ComplexAnnonceSedexCO4 currentAnnonce = (ComplexAnnonceSedexCO4)annoncesCO4Search.getSearchResults()[iAnnonce];
			    SimpleAnnonceSedexCODebiteur currentDebiteur = (SimpleAnnonceSedexCODebiteur)currentAnnonce.getSimpleAnnonceSedexCODebiteur();
			    SimpleAnnonceSedexCOAssure currentAssure = (SimpleAnnonceSedexCOAssure)currentAnnonce.getSimpleAnnonceSedexCOAssure();
			    String assureNss = currentAssure.getNssAssure();
			    String debiteurNss = currentDebiteur.getNssDebiteur();
			    
				if(debiteurNss.equals(assureNss)){
				    String statementDate = currentAnnonce.getSimpleAnnonceSedexCO().getStatementDate();
				    String messSubType = currentAnnonce.getSimpleAnnonceSedexCO().getMessageSubType();
					subtypeLibelle = currentAnnonce.getSimpleAnnonceSedexCO().getMessageSubTypeLibelle();
					csMess = currentAnnonce.getSimpleAnnonceSedexCO().getIdAnnonceSedexCO();

					interets = currentAnnonce.getSimpleAnnonceSedexCODebiteur().getInterets();
					frais = currentAnnonce.getSimpleAnnonceSedexCODebiteur().getFrais();
					total = currentAnnonce.getSimpleAnnonceSedexCODebiteur().getTotal();
					
					long messageId = 0;
					if(csMess != null){
				    	messageId = Long.parseLong(csMess);
					}
					
				    String statementYear = "";
				    if(statementDate.length() >= 10){
				        statementYear = statementDate.substring(6); 
				    }
				        
					if(iAnnonce%2==0){
						rowStyle = "amalRow";
					}else{
						rowStyle = "amalRowOdd";
					}
			%>
 
			<tr style="height:26px" class="<%=rowStyle%>" onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'amalRowHighligthed')" onMouseOut="jscss('swap', this, 'amalRowHighligthed', '<%=rowStyle%>')">
				<!-- Année -->
				<td><%=statementYear%></td>
				
				<!-- Date -->
				<td><%=currentAnnonce.getSimpleAnnonceSedexCO().getDateAnnonce()%></td>
				
				<!-- Assureur -->
				<td><%=currentAnnonce.getCaisseMaladie().getTiers().getDesignation1() %></td>
				
				<!-- Message -->
				<td>
					<%
					%>
					<a style="color:blue" onclick="showdetailCO4(<%=csMess%>);" href="#<%=csMess%>">
							<%=subtypeLibelle%>
					</a>
				</td>
				
				<!-- Status -->
				<td><%
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
				
				<!-- Période -->
				<td></td>
				
				<!-- Intérêts -->
				<td><%=interets%></td>
				
				<!-- Frais -->
				<td><%=frais%></td>
				
				<!-- Total créance -->
				<td><%=total%></td>
				
				<%
				/* Get elements from paiments*/
				%>
				<!-- Débiteur -->
				<td></td>
				
				<!-- RP Rétro -->
				<td></td>
				
				<!-- Annulation -->
				<td></td>
		</tr>
		<%		} // End if isDebiteur
			} // End for
		} // End if null object
		%>
		<tr style="background-color:#B3C4DB"><td colspan="12"></td></tr>
	</table>						
</div>