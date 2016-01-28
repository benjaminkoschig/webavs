<%@page import="ch.globaz.amal.business.models.revenu.RevenuHistorique"%>
<%@ include file="/amalRoot/contribuable/contribuableHeader.jspf" %>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/contribuable/contribuableRevenu.js"/></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/amal.css" rel="stylesheet"/>
<script type="text/javascript">
$(document).ready(function() {
	$(".subsideDetails").hide();
	$(".expandDetails").attr("src", "<%=request.getContextPath()%>/images/amal/view_detailed.png");
	// Define the toggle function for collapse-expand the icon
	// ----------------------------------------------------------------
	$(".expandDetails").toggle(function(){
			$(this).attr("src", "<%=request.getContextPath()%>/images/amal/view_tree.png");
		},
	  	function(){
			$(this).attr("src", "<%=request.getContextPath()%>/images/amal/view_detailed.png");
		}
	);
});

// Manage the onclick event for revenus
// ----------------------------------------------------------------
$('table img.expandDetails').click( function(){
		// get the id
		var currentId = $(this).attr("id");			
		// remove the term "expand"
		currentId = currentId.replace("expand","");
		// set new id
		var currentTableId = "subside"+currentId;
    	$('#'+currentTableId).toggle();
  	}
);

</script>

<div id="conteneurRevenus">
	<table width="100%" border="0">
		<col width="20px" align="center"></col>
		<col width="20px" align="center"></col>
		<col width="64px" align="center"></col>
		<col width="80px" align="right"></col>
		<col width="64px" align="center"></col>
		<col width="110px" align="center"></col>
		<col width="110px" align="center"></col>
		<col width="60px" align="center"></col>
		<col width="120px" align="right"></col>
		<col width="80px" align="center"></col>
		<col width="46px" align="center"></col>
		<tr>
			<th colspan="2"></th>
			<th>Année historique</th>
			<th>Revenu déterminant</th>
			<th>Année taxation</th>
			<th>Source taxation</th>
			<th>Type taxation</th>
			<th>Décision taxation</th>
			<th>Revenu imposable</th>
			<th>Etat civil</th>
			<th>Nbre enfants</th>
		</tr>
		<tr class="amalRowOdd" style="height:26px">
			<td>
			<% if (!contribReprise) {%> 
				<ct:ifhasright element="amal.revenuHistorique.revenuHistorique.nouveau" crud="c">
					<a href="<%=detailLinkRevenuHistoriqueNouveau%>">
					<img
						width="22px"
						height="22px"
						src="<%=request.getContextPath()%>/images/amal/tab_new.png" title="Nouveau" border="0">
					</a>
				</ct:ifhasright>
			<% } %>
			</td>
			<td colspan="11"></td>							
		</tr>
		<tr style="background-color:#B3C4DB"><td colspan="11"></td></tr>
	<%
	String rowStyle = "";
	int i=0;
	if (viewBean.getRevenusHistoriques().getSearchResults() != null) {
	for (Iterator it = Arrays.asList(viewBean.getRevenusHistoriques().getSearchResults()).iterator(); it.hasNext();) {
		RevenuHistorique revenu = (RevenuHistorique) it.next();
		String tabId = "1";
		String detailUrlRevenu = detailLinkRevenuHistorique + revenu.getId() + "&contribuableId="+revenu.getIdContribuable();
		String detailUrlRevenuSupprimer = detailLinkRevenuHistoriqueSupprimer + revenu.getIdRevenuHistorique();
		detailUrlRevenuSupprimer += "&contribuableId=" + revenu.getIdContribuable();
		boolean condition = (i % 2 == 0);
		if (condition) {
			rowStyle = "amalRow";
		} else {
			rowStyle = "amalRowOdd";
		}
		i++;
		String anneeHistorique = revenu.getAnneeHistorique();
		
		List<FamilleContribuableView> familleContribuableArray = viewBean.getListeFamilleContribuableViewAnnee().get(anneeHistorique);
		String tabImg = "tab.png";
		
		if ("42002601".equals(revenu.getTypeRevenu())) {
			tabImg = "tabSourcier.png";
		}
		%>
		<tr style="height:26px" class="<%=rowStyle%>"
		onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'amalRowHighligthed')"
		onMouseOut="jscss('swap', this, 'amalRowHighligthed', '<%=rowStyle%>')">
			<td>
			<% if (!contribReprise) {%> 
			<a href="<%=detailUrlRevenu%>">										
				<img width="22px"
					height="22px"
					src="<%=request.getContextPath()%>/images/amal/<%=tabImg%>"
					title="<%="Détails du revenu "+anneeHistorique%>" border="0">
			</a>
			<%}else{ %>
				<img width="22px"
					height="22px"
					src="<%=request.getContextPath()%>/images/amal/<%=tabImg%>"
					title="<%="Détails du revenu "+anneeHistorique%>" border="0">
			<%} %>
			</td>
			<td><%if(null!= familleContribuableArray && familleContribuableArray.size()>0){%>
				<img class="expandDetails" id="expand<%=i%>"
					src="<%=request.getContextPath()%>/images/icon-expand.gif" 
					title="<%="Aperçu des subsides "+anneeHistorique%>" 
					border="0"
					onMouseOver="this.style.cursor='hand';"
					onMouseOut="this.style.cursor='pointer';"
					width="22px"
					height="22px"
					>
				<% // fin if(null!= familleContribuableArray && familleContribuableArray.size()>0){
				}
				%>
			</td>
			<td><%=revenu.getAnneeHistorique()%></td>
			<td><%=revenu.getRevenuDeterminantCalculCurrency()%></td>
			<td><%=revenu.getAnneeTaxation()%></td>
			<td><%=objSession.getCodeLibelle(revenu.getTypeSource())%></td>
			<td><%=objSession.getCodeLibelle(revenu.getTypeTaxation())%></td>
			<td><%=revenu.getDateAvisTaxation()%></td>
			<td><%=revenu.getRevenuImposableCurrency()%></td>
			<td><%=objSession.getCodeLibelle(revenu.getEtatCivil())%></td>
			<td><%=revenu.getNbEnfantsPlusEnfantsSuspens()%></td>
		</tr>
		
		<tr style="background-color:#B3C4DB">
			<td></td>
			<td></td>
			<td class="tdSubsides" id="tdSubsides<%=i%>" colspan="8" >
				<!-- Ici viendra les lignes subsides des membres -->
			</td>
			<td></td>
		</tr>
			<%
			// Fin for année
			}
			}
			%>
			</td>
		</tr>
	</table>						
</div>