<%@page import="ch.globaz.perseus.business.calcul.OutputData"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="globaz.perseus.utils.plancalcul.PFLignePlanCalculHandler"%>
<%@page import="globaz.perseus.utils.plancalcul.PFPlanCalculHandler"%>
<%@page import="globaz.perseus.vb.pcfaccordee.PFPlanCalculViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<%
	PFPlanCalculViewBean viewBean = (PFPlanCalculViewBean)session.getAttribute("viewBean");
	PFPlanCalculHandler planCalculHandler = viewBean.getPlanCalculHandler();
	idEcran="PPF1031";

	String monnaie = viewBean.getMonnaie();

%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>

		<link rel="stylesheet" type="text/css" media="screen" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/pcfaccordee/detailPCAL.css"/>
		<link rel="stylesheet" type="text/css" media="print" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/pcfaccordee/detailPCAL_print.css"/>
	
	</head>

	<body>
		<!-- Header ayant droit et periode -->
		<div id="adHeader">
		 	<span id="adInfos" class="labelInfos"><ct:FWLabel key="JSP_PF_PLANCALCUL_D_AYANTDROIT"/></span><span class="inforeq"><%= viewBean.getRequerantInfos() %></span><br />
			<span id="nssInfos" class="labelInfos"><ct:FWLabel key="JSP_PF_PLANCALCUL_D_NSS"/></span><span class="infonss"><%= viewBean.getNssInfos() %></span><br />
			<span id="localiteInfos" class="labelInfos"><ct:FWLabel key="JSP_PF_PLANCALCUL_D_LOCALITE"/></span><span class="infonss"><%= viewBean.getLocalite() %></span>
		</div><hr/>	
		<!-- conjoint -->
		<div id="conjointInfos">
			<span id="adInfos" class="labelInfos"><ct:FWLabel key="JSP_PF_PLANCALCUL_D_CONJOINT"/></span><span id="infoConjoint" class="inforeq"><%= viewBean.getConjointInfos() %></span><br />
			<hr/>
		</div>
		
		<!--  enfants compris -->
		<div id="enfantsInfos">
			<span class="labelInfos"><ct:FWLabel key="JSP_PF_PLANCALCUL_D_ENFANTCOMPRIS"/></span>
			<% int cpt = 0;
				for(String membres:viewBean.getEnfantsCompris()){
					if(cpt==0){
				%><span class="enfantpremier"><%= membres.toString() %></span><br/>
				<%
				cpt++;
				}else{
					%><span class="enfantsuite"><%= membres.toString() %></span><br/><% 
				}
			} %>
			<hr/>
		</div>
		
		<!-- Période de validité -->
		<div id="planCalculHeader">
			<span id="adInfos" class="labelInfos"><ct:FWLabel key="JSP_PF_PLANCALCUL_D_PERIODE"/></span><span id="dateVal" class="inforeq"><%=viewBean.getPeriode() %></span>
		</div>
		
		
		<div id="wrapper">
		
			<div id="zoneFortune" class="zone">
				<div class="titreBloc"><ct:FWLabel key="JSP_PF_PLANCALCUL_D_FORTUNE"/></div>
				<table>
					<%
					for(PFLignePlanCalculHandler ligne : planCalculHandler.getBlocFortune()){
					%>
						<tr>
							<td class="libelle <%=ligne.getCssClass()%>"><%= ligne.getLibelle() %></td>
							<td class="libelleMonnaie <%=ligne.getValCol1().getCssClass()%>"><%= (ligne.getValCol1().getValeur().equals(""))?"":monnaie %></td>
							<td class="montant <%=ligne.getValCol1().getCssClass()%>" data-g-amountformatter="blankAsZero:false"><%= ligne.getValCol1().getValeur() %></td>
							<td class="libelleMonnaie <%=ligne.getValCol2().getCssClass()%>"><%= (ligne.getValCol2().getValeur().equals(""))?"":monnaie %></td>
							<td class="montant <%=ligne.getValCol2().getCssClass()%>" data-g-amountformatter="blankAsZero:false"><%= ligne.getValCol2().getValeur() %></td>
							<td class="libelleMonnaie <%=ligne.getValCol3().getCssClass()%>"><%= (ligne.getValCol3().getValeur().equals(""))?"":monnaie %></td>
							<td class="montant <%=ligne.getValCol3().getCssClass()%>" data-g-amountformatter="blankAsZero:false"><%= ligne.getValCol3().getValeur() %></td>
						</tr>
					<%} %>
				</table>
			</div>
			
			<div id="zoneRevenus" class="zone">
				<div class="titreBloc"><ct:FWLabel key="JSP_PF_PLANCALCUL_D_REVENUS_DETERMINANTS"/></div>
				<table>
					<%
					for(PFLignePlanCalculHandler ligne : planCalculHandler.getBlocRevenus()){
						String libelle = ligne.getLibelle();
						if ("Revenu des enfants".equals(ligne.getLibelle())) {
							libelle = "<a href='#' id='revenuEnfantsLink'>" + libelle + "</a>";	
						}
					%>
						<tr>
							<td class="libelle <%=ligne.getCssClass()%>"><%= libelle %></td>
							<td class="libelleMonnaie <%=ligne.getValCol1().getCssClass()%>"><%= (ligne.getValCol1().getValeur().equals(""))?"":monnaie %></td>
							<td class="montant <%=ligne.getValCol1().getCssClass()%>" data-g-amountformatter="blankAsZero:false"><%= ligne.getValCol1().getValeur() %></td>
							<td class="libelleMonnaie <%=ligne.getValCol2().getCssClass()%>"><%= (ligne.getValCol2().getValeur().equals(""))?"":monnaie %></td>
							<td class="montant <%=ligne.getValCol2().getCssClass()%>" data-g-amountformatter="blankAsZero:false"><%= ligne.getValCol2().getValeur() %></td>
							<td class="libelleMonnaie <%=ligne.getValCol3().getCssClass()%>"><%= (ligne.getValCol3().getValeur().equals(""))?"":monnaie %></td>
							<td class="montant <%=ligne.getValCol3().getCssClass()%>" data-g-amountformatter="blankAsZero:false"><%= ligne.getValCol3().getValeur() %></td>
						</tr>
					<%} %>
				</table>
			</div>
			
			<div id="zoneDepensesReconnues" class="zone">
				<div class="titreBloc"><ct:FWLabel key="JSP_PF_PLANCALCUL_D_DEPENSES_RECONNUES"/></div>
				<table>
					<%
					for(PFLignePlanCalculHandler ligne : planCalculHandler.getBlocDepensesReconnues()){
						String libelle = ligne.getLibelle();
						if ("Frais d'obtention du revenu des enfants".equals(ligne.getLibelle())) {
							libelle = "<a href='#' id='fraisObtentionRevenuEnfantsLink'>" + libelle + "</a>";	
						}
					%>
						<tr>
							<td class="libelle <%=ligne.getCssClass()%>"><%= libelle %></td>
							<td class="libelleMonnaie <%=ligne.getValCol1().getCssClass()%>"><%= (ligne.getValCol1().getValeur().equals(""))?"":monnaie %></td>
							<td class="montant <%=ligne.getValCol1().getCssClass()%>" data-g-amountformatter="blankAsZero:false"><%= ligne.getValCol1().getValeur() %></td>
							<td class="libelleMonnaie <%=ligne.getValCol2().getCssClass()%>"><%= (ligne.getValCol2().getValeur().equals(""))?"":monnaie %></td>
							<td class="montant <%=ligne.getValCol2().getCssClass()%>" data-g-amountformatter="blankAsZero:false"><%= ligne.getValCol2().getValeur() %></td>
							<td class="libelleMonnaie <%=ligne.getValCol3().getCssClass()%>"><%= (ligne.getValCol3().getValeur().equals(""))?"":monnaie %></td>
							<td class="montant <%=ligne.getValCol3().getCssClass()%>" data-g-amountformatter="blankAsZero:false"><%= ligne.getValCol3().getValeur() %></td>
						</tr>
					<%} %>
				</table>
			</div>
			
			<div id="zonePrestation" class="zone">
				<div class="titreBloc"><ct:FWLabel key="JSP_PF_PLANCALCUL_D_CALCUL_PRESTATION"/></div>
				<table>
					<%
					for(PFLignePlanCalculHandler ligne : planCalculHandler.getBlocPrestation()){
					%>
						<tr>
							<td class="libelle <%=ligne.getCssClass()%>"><%= ligne.getLibelle() %></td>
							<td class="libelleMonnaie <%=ligne.getValCol1().getCssClass()%>"><%= (ligne.getValCol1().getValeur().equals(""))?"":monnaie %></td>
							<td class="montant <%=ligne.getValCol1().getCssClass()%>" data-g-amountformatter="blankAsZero:false"><%= ligne.getValCol1().getValeur() %></td>
							<td class="libelleMonnaie <%=ligne.getValCol2().getCssClass()%>"><%= (ligne.getValCol2().getValeur().equals(""))?"":monnaie %></td>
							<td class="montant <%=ligne.getValCol2().getCssClass()%>" data-g-amountformatter="blankAsZero:false"><%= ligne.getValCol2().getValeur() %></td>
							<td class="libelleMonnaie <%=ligne.getValCol3().getCssClass()%>"><%= (ligne.getValCol3().getValeur().equals(""))?"":monnaie %></td>
							<td class="montant <%=ligne.getValCol3().getCssClass()%>" data-g-amountformatter="blankAsZero:false"><%= ligne.getValCol3().getValeur() %></td>
						</tr>
					<%} %>
				</table>
			</div>
			
			<script>
				$(function() {
					$( "#revenuEnfantsDiv" ).dialog({
						autoOpen: false,
						width: 700,
						height: 500,
						modal: true
					});
					$( "#fraisObtentionRevenuEnfantsDiv" ).dialog({
						autoOpen: false,
						width: 700,
						height: 500,
						modal: true
					});
				});
				
				$("#revenuEnfantsLink").click(function() {
					$("#revenuEnfantsDiv").dialog( "open" );
					return false;
				});
				$("#fraisObtentionRevenuEnfantsLink").click(function() {
					$("#fraisObtentionRevenuEnfantsDiv").dialog( "open" );
					return false;
				});
			</script>
			
			<div id="revenuEnfantsDiv" title="Revenu des enfants">
				<%=planCalculHandler.getOutputCalcul().getDonneeString(OutputData.REVENUS_ENFANTS_DETAIL_XML) %>
			</div>
			
			<div id="fraisObtentionRevenuEnfantsDiv" title="Frais d'obtention du revenu des enfants">
				<%=planCalculHandler.getOutputCalcul().getDonneeString(OutputData.DEPENSES_FRAIS_OBTENTION_REVENU_ENFANTS_DETAIL_XML) %>
			</div>
		</div>
		
	</body>
</html>

