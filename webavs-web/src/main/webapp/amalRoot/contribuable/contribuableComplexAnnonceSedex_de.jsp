<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="globaz.amal.utils.AMSedexHelper"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTraitementsAnnonceSedex"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme.AMStatutAnnonceSedex"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex"%>
<%@page import="ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedexSearch"%>
<%@page import="ch.globaz.amal.business.models.annoncesedex.ComplexAnnonceSedex"%>
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
});
</script>

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
		<col align="right"></col>
		<col align="right"></col>
		<tr>
			<th>Ann?e</th>
			<th>Date</th>
			<th>Assureur</th>
			<th>Message</th>
			<th>Status</th>
			<th>Trait?</th>
			<th>Membre</th>
			<th>P?riode</th>
			<th>Contribution</th>
			<th>Prime</th>
		</tr>
		<%HashMap<String, String> listIdsDetailFamille = viewBean.getIdDetailFamilleSedex(); %>
		<tr class="amalRowOdd" style="height:26px; display:<%=listIdsDetailFamille.size() > 0? "":"none"%>;">
			<td colspan="10">
				<select id="selectSedex">
					<option value="all"></option>
				<%for (String idDetailFamille : listIdsDetailFamille.keySet()) { %>
					<option value="<%=idDetailFamille%>"><%=listIdsDetailFamille.get(idDetailFamille) %></option>
				<%} %>
				</select>
			</td>							
		</tr>
		<tr style="background-color:#B3C4DB" >
			<td colspan="10"></td>
		</tr>
		<%
			String rowStyle = "";
			ComplexAnnonceSedexSearch annoncesSearch = viewBean.getAnnonceSedex();
			for(int iAnnonce = 0; iAnnonce<annoncesSearch.getSize();iAnnonce++){
				ComplexAnnonceSedex currentAnnonce = (ComplexAnnonceSedex)annoncesSearch.getSearchResults()[iAnnonce];
				if(iAnnonce%2==0){
					rowStyle = "amalRow";
				}else{
					rowStyle = "amalRowOdd";
				}
		%>
				
		<tr class="sedexLine <%=currentAnnonce.getSimpleDetailFamille().getIdDetailFamille() %>" style="height:26px" class="<%=rowStyle%>" onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'amalRowHighligthed')" onMouseOut="jscss('swap', this, 'amalRowHighligthed', '<%=rowStyle%>')">
			<td><%=currentAnnonce.getSimpleDetailFamille().getAnneeHistorique()%></td>
			<td><%=currentAnnonce.getSimpleAnnonceSedex().getDateMessage()%></td>
			<td><%=currentAnnonce.getCaisseMaladie().getTiers().getDesignation1() %></td>
			<%
			String paddingLeft = "0px";
			if ("102".equals(currentAnnonce.getSimpleAnnonceSedex().getMessageSubType()) || "103".equals(currentAnnonce.getSimpleAnnonceSedex().getMessageSubType()) || "202".equals(currentAnnonce.getSimpleAnnonceSedex().getMessageSubType()) || "203".equals(currentAnnonce.getSimpleAnnonceSedex().getMessageSubType())) {
				paddingLeft = "20px";		 
			}%>
			<td style="padding-left:<%=paddingLeft%>">
				<a style="color:blue" href="<%=detailLinkDetailFamille+currentAnnonce.getSimpleDetailFamille().getIdDetailFamille()%>" title="No d?cision : <%=currentAnnonce.getSimpleAnnonceSedex().getNumeroDecision()%>">
					<%=AMMessagesSubTypesAnnonceSedex.getSubTypeCSLibelle(currentAnnonce.getSimpleAnnonceSedex().getMessageSubType())%>
				</a>
			</td>

			<td>
				<%
				String status = currentAnnonce.getSimpleAnnonceSedex().getStatus();
				String imgName = AMStatutAnnonceSedex.getStatusImageName(status);
				String libelleImg = AMStatutAnnonceSedex.getStatusImageLabel(status);
				%>
				<%
				if (!JadeStringUtil.isEmpty(imgName)) { %>
				<img
						width="20px"
						height="20px"
						src="<%=request.getContextPath()%>/images/amal/<%=imgName%>" title="<%=libelleImg%>" border="0">
				<% } else { %>
					&nbsp;
				<% } %>
				</TD>
				<%
				String traitement = currentAnnonce.getSimpleAnnonceSedex().getTraitement();
				if (AMTraitementsAnnonceSedex.TRAITE_AUTO.getValue().equals(traitement)) {
					%>
					<td title="Traitement automatique">
					<img
						width="16px"
						height="16px"
						src="<%=request.getContextPath()%>/images/amal/sedex_traite.png" border="0">
					<img
						width="16px"
						height="16px"
						src="<%=request.getContextPath()%>/images/amal/sedex_traitement_auto.png" border="0">
					</td>				
					<%
				} else if (AMTraitementsAnnonceSedex.TRAITE_MANU.getValue().equals(traitement)) {
					%>
					<td title="Traitement manuel">
					<img
						width="16px"
						height="16px"
						src="<%=request.getContextPath()%>/images/amal/sedex_traite.png" border="0">
					<img
						width="16px"
						height="16px"
						src="<%=request.getContextPath()%>/images/amal/sedex_traitement_manual.png" border="0">
					</td>
					<%
				}  else {
					%>
						<td title="A traiter">&nbsp;</td>
					<%
				}
				%>
			<td><%=currentAnnonce.getSimpleFamille().getNomPrenom()%></td>
			<td>
			<%=AMSedexHelper.getPeriodeInfo(currentAnnonce.getSimpleAnnonceSedex().getMessageSubType(), currentAnnonce.getSimpleAnnonceSedex().getMessageContent()) %>			
			</td>
			<td><%=AMSedexHelper.getMontant(currentAnnonce) %></td>
			<td><%=new FWCurrency(currentAnnonce.getSimpleDetailFamille().getMontantPrimeAssurance()) %> </td>
		</tr>
		<tr style="background-color:#B3C4DB"><td colspan="10"></td></tr>
		<%
			}
		%>
	</table>						
</div>