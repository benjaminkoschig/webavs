<%@page import="ch.globaz.utils.CommonNSSFormater"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="globaz.amal.utils.AMSedexHelper"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTraitementsAnnonceSedex"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme.AMStatutAnnonceSedex"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex"%>
<%@page import="ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCO"%>
<%@page import="ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOAssure"%>
<%@page import="ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCODebiteur"%>
<%@page import="ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO4"%>
<%@page import="ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO4Search"%>

<%@ include file="/amalRoot/contribuable/contribuableHeader.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/amal.css" rel="stylesheet"/>

<script type="text/javascript">
<%

%>
$(document).ready(function() {
		$("#selectSedex").on("change", function() {
			var idDetailFamille = $(this).val();
			if (idDetailFamille == 'all') {
				$(".sedexLine").show();	
			} else {
				$(".sedexLine").hide();
				$(".sedexLine."+idDetailFamille).show();
			}
		});
		
		// ---------------------------------------------------------------------
		// Action pour afficher la boite de sélection des templates
		// ---------------------------------------------------------------------
		$('#btnPrintList').click(function(e) {
			$('#impressionListe').dialog('open');
		});
		
		$("#selectIdTiersCM").change(function(e) {
			var thisVal = $(this).val();
		
			if (!thisVal) {
				$('#idTiersCM').val('');
			}
		});

	setTimeout(function(){
		$('#impressionListe').dialog(
				{
					autoOpen:false,
					buttons:
						[	
							{
								text:"Imprimer",
								click: function() {
									imprimerListe();
								}
							},
				         	{
								text:"Fermer",
								click: function() {
									$(this).dialog("close");
								}
							}
						],
					modal:true,
					closeOnEscape:true,
					draggable : true,
					resizable : true,
					width : 1000,
					height: "auto",
					minHeight:400
					
				}
			);
	},300);

});


function toEmptyValue(val) {
	if (!val) {
		return null
	} else {
		return val;
	}
}

function imprimerListe() {
	var idContribuable = $("#idContribuable").val();
	var annee = toEmptyValue($("#annee").val());
	var idTiersCM = toEmptyValue($("#idTiersCM").val());
	var typeDecompte = toEmptyValue($("#typeDecompte").val());
	var fromPeriode = toEmptyValue($("#fromPeriode").val());
	var toPeriode = toEmptyValue($("#toPeriode").val());
	
	var o_options= {
			serviceClassName: 'ch.globaz.amal.business.services.models.sedexCO.AnnoncesCOService',
			serviceMethodName:'printListAnnonces',
			parametres:idContribuable+","+annee+","+idTiersCM+","+typeDecompte+","+fromPeriode+","+toPeriode,
			callBack: callBackPrintList
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

function callBackPrintList() {
	$('#impressionListe').dialog('close');
}
</script>

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
	String period = "";
	String interets = "";
	String frais = "";
	String total = "";
	String noAvs = "";
	String nomPrenom = "";
	String subtypeLibelle = "";
	String dateAnnonce = "";
	String caisse = "";
	int assureCounter = 0;
	int annonceCounter = 0;
	ComplexAnnonceSedexCO4Search annoncesCO4Search = viewBean.getAnnonceSedexCO4();
	
	int nbItems = annoncesCO4Search.getSize();
	for(int iAnnonce = 0; iAnnonce<nbItems;iAnnonce++){
	    ComplexAnnonceSedexCO4 currentAnnonce = (ComplexAnnonceSedexCO4)annoncesCO4Search.getSearchResults()[iAnnonce];
	    csMess = currentAnnonce.getSimpleAnnonceSedexCO().getIdAnnonceSedexCO();
	    SimpleAnnonceSedexCODebiteur currentDebiteur = (SimpleAnnonceSedexCODebiteur)currentAnnonce.getSimpleAnnonceSedexCODebiteur();
	    SimpleAnnonceSedexCOAssure currentAssure = (SimpleAnnonceSedexCOAssure)currentAnnonce.getSimpleAnnonceSedexCOAssure();
	    SimpleAnnonceSedexCO annoceSedex = (SimpleAnnonceSedexCO)currentAnnonce.getSimpleAnnonceSedexCO();
	    
	    String assureNss = currentAssure.getNssAssure();
	    String debiteurNss = currentDebiteur.getNssDebiteur();
	    String messSubType = annoceSedex.getMessageSubType();
	    String currentIdDebiteur = currentDebiteur.getId();
		
		boolean isDebiteur = debiteurNss.equals(assureNss);
		annonceId = csMess;
		if(isDebiteur){
			subtypeLibelle = annoceSedex.getMessageSubTypeLibelle();
			title = "Détail annonce # " + csMess;
			if(annoceSedex.getMessageSubType().equals("402")){
				subtitle = subtypeLibelle + " " + annoceSedex.getStatementStartDate() + " - " + annoceSedex.getStatementEndDate();
			} else { 
			    subtitle = subtypeLibelle + " " + currentAssure.getPrimePeriodeDebut() + " - " + currentAssure.getPrimePeriodeFin();
			}
			interets = currentDebiteur.getInterets();
			frais = currentDebiteur.getFrais();
			total = currentDebiteur.getTotal();
			noAvs = nssFormater.format(debiteurNss);
			nomPrenom = currentDebiteur.getNomPrenomDebiteur() ;
			dateAnnonce = annoceSedex.getDateAnnonce();
			caisse = currentAnnonce.getCaisseMaladie().getTiers().getDesignation1();
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
		<%
		countElement++;
		}	%>
	<!-- Load assure information -->
	<script>
		var assure = new Personne('<%=avsAssure%>','<%=nomPrenomAssure%>');
		assureArray['<%=assureCounter%>'] = new DetailAssure(assure, detailAssure);
	</script>
		    
	    <%
	    String nextIdDebiteur = "";
	    if(iAnnonce + 1 < nbItems){
		    ComplexAnnonceSedexCO4 tmpAnnonce = (ComplexAnnonceSedexCO4)annoncesCO4Search.getSearchResults()[iAnnonce + 1];
		    nextIdDebiteur = tmpAnnonce.getSimpleAnnonceSedexCODebiteur().getId();
	    }
	    
	    if(!nextIdDebiteur.equals(currentIdDebiteur)){
    %>
        <script> 
	        var debiteur = new Personne('<%=noAvs%>','<%=nomPrenom%>');
	    	var annonce = new Annonce('<%=csMess%>', '<%=title%>', '<%=subtitle%>','<%=interets%>','<%=frais%>','<%=total%>', debiteur, assureArray);
	    	annoncesCO4['<%=annonceCounter%>'] = new AnnonceCO4(annonce,'<%=csMess%>', '<%=caisse%>', '<%=dateAnnonce%>');
	    	assureArray = new Array; // Reset object
    	</script>
   	<%
 			assureCounter = 0;
	    	annonceCounter++;
	  }
		assureCounter++;
	}// End for
%>
<!-- /////////////////////////////////////////////////////////////////////////////////////////////////////// -->
<!-- ////////////////////////		End: Popup section related   /////////////////////////////////////////// -->
<!-- /////////////////////////////////////////////////////////////////////////////////////////////////////// -->

<div id="impressionListe" title="Imprimer la liste pour le contribuable">
	<table>
		<tr>
			<td>Année</td>
			<td><input tabindex="1" type="text" id="annee" name="annee" size="4" maxlength="4"/></td>
		</tr>
		<tr>
			<td>Assureur</td>
			<td>
               		<input name="selectIdTiersCM" id="selectIdTiersCM" tabindex="2"
						class="jadeAutocompleteAjax" type="text"
						data-g-autocomplete="service:¦ch.globaz.amal.business.services.models.sedexCO.AnnoncesCOService¦,
 						 method:¦find¦,
 						 criterias:¦{
 						 	forCodeAdministrationLike:'Code',
 						 	forDesignation1Like:'Designation'
 						 }¦,
 						 constCriterias:¦forGenreAdministration=509008¦,
						 lineFormatter:¦<b>#{admin.codeAdministration}</b> - #{tiers.designation1} #{tiers.designation2}¦,
 						 modelReturnVariables:¦tiers.id,tiers.designation1,tiers.designation2,admin.codeAdministration¦,nbReturn:¦20¦,
 						 functionReturn:¦
 						 	function(element){
 						 		this.value=$(element).attr('admin.codeAdministration') + ' - ' + $(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
 						 		$('#idTiersCM').val($(element).attr('tiers.id'))
 						 	}¦
						 ,nbOfCharBeforeLaunch:¦3¦" />
						 <input type="hidden" name="idTiersCM" value="" id="idTiersCM"/>
           </td>
		</tr>
		<tr>
			<td>Type de décompte</td>
			<td>
				<select id="typeDecompte" tabindex="3">
					<option value="401">Trimestriel</option>
					<option value="402">Final</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>Période</td>
			<td><input type="text" id="fromPeriode" data-g-calendar="" tabindex="4"/> à <input type="text" id="toPeriode" data-g-calendar="" tabindex="5"/></td>
		</tr>
	</table>
</div>


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
		<tr>
			<td colspan="12">
				<input type="button" id="btnPrintList" value="Imprimer">
			</td>
		</tr>
		<%
			String rowStyle = "amalRowOdd";
			if(annoncesCO4Search != null){%>
			
			<tr style="background-color:#B3C4DB">
				<td colspan="12"/>
			</tr>
			<%
			int lineCounter = 0;
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
					period = currentAssure.getPrimePeriodeDebut() + " - " + currentAssure.getPrimePeriodeFin();
					dateAnnonce = currentAnnonce.getSimpleAnnonceSedexCO().getDateAnnonce();
					caisse = currentAnnonce.getCaisseMaladie().getTiers().getDesignation1();
					
					long messageId = 0;
					if(csMess != null){
				    	messageId = Long.parseLong(csMess);
					}
					
				    String statementYear = "";
				    if(statementDate.length() >= 10){
				        statementYear = statementDate.substring(6); 
				    }
				        
				    if(lineCounter%2==0){
						rowStyle = "amalRow";
					}else{
						rowStyle = "amalRowOdd";
					}
			%>
 
			<tr style="height:26px" class="<%=rowStyle%>" onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'amalRowHighligthed')" onMouseOut="jscss('swap', this, 'amalRowHighligthed', '<%=rowStyle%>')">
				<!-- Année -->
				<td><%=statementYear%></td>
				
				<!-- Date -->
				<td><%=dateAnnonce%></td>
				
				<!-- Assureur -->
				<td><%=caisse%></td>
				
				<!-- Message -->
				<td>
					<%
					%>
					<a style="color:blue" onclick="showdetailCO4(<%=lineCounter%>);" href="#<%=csMess%>">
						<%=subtypeLibelle%>
					</a>
				</td>
				
				<!-- Status -->
				<td><%
						lineCounter++;
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
				<td><%=period%></td>
				
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