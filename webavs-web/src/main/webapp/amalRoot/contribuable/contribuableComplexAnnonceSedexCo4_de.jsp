<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="globaz.amal.utils.AMSedexHelper"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme.AMTraitementsAnnonceSedex"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme.AMStatutAnnonceSedex"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedex"%>
<%@page import="ch.globaz.amal.business.models.annoncesedexco.SimpleAnnonceSedexCOAssure"%>
<%@page import="ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO4"%>
<%@page import="ch.globaz.amal.business.models.annoncesedexco.ComplexAnnonceSedexCO4Search"%>

<%@ include file="/amalRoot/contribuable/contribuableHeader.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/amal.css" rel="stylesheet"/>
<%-- <script type="text/javascript">
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
</script> --%>

<div id="conteneurComplexAnnonceSedex">
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
			ComplexAnnonceSedexCO4Search annoncesCO4Search = viewBean.getAnnonceSedexCO4();
			if(annoncesCO4Search != null){%>
			
			<tr style="background-color:#B3C4DB">
				<td colspan="12"/>
			</tr>
			<%
			for(int iAnnonce = 0; iAnnonce<annoncesCO4Search.getSize();iAnnonce++){
			    ComplexAnnonceSedexCO4 currentAnnonce = (ComplexAnnonceSedexCO4)annoncesCO4Search.getSearchResults()[iAnnonce];
			    SimpleAnnonceSedexCOAssure currentAssure = (SimpleAnnonceSedexCOAssure)currentAnnonce.getSimpleAnnonceSedexCOAssure();
			    String statementDate = currentAnnonce.getSimpleAnnonceSedexCO().getStatementDate();
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
				String messSubType = currentAnnonce.getSimpleAnnonceSedexCO().getMessageSubType();
				String subtypeLibelle = currentAnnonce.getSimpleAnnonceSedexCO().getMessageSubTypeLibelle();
				String csMess = currentAnnonce.getSimpleAnnonceSedexCO().getIdAnnonceSedexCO();
				long messageId = 0;
				if(csMess != null){
			    	messageId = Long.parseLong(csMess);
				}%>
				<a style="color:blue" onclick="showdetail(<%=messageId%>);" href="#">
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
			<td><%=currentAnnonce.getSimpleAnnonceSedexCODebiteur().getInterets()%></td>
			
			<!-- Frais -->
			<td><%=currentAnnonce.getSimpleAnnonceSedexCODebiteur().getFrais()%></td>
			
			<!-- Total créance -->
			<td><%=currentAnnonce.getSimpleAnnonceSedexCODebiteur().getTotal()%></td>
			
			<!-- Débiteur -->
			<td></td>
			
			<!-- RP Rétro -->
			<td></td>
			
			<!-- Annulation -->
			<td></td>
			
			<td></td>

			<!-- Assureur -->
			<td></td>

		<%
			} //End for
		} // end if null object
		%>
		<tr style="background-color:#B3C4DB"><td colspan="12"></td></tr>
	</table>						
</div>