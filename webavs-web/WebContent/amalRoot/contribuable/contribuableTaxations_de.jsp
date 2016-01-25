<%@page import="ch.globaz.amal.business.models.revenu.Revenu"%>
<%@ include file="/amalRoot/contribuable/contribuableHeader.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/amal.css" rel="stylesheet"/>
<div id="conteneurTaxations">
	<table width="100%" border="0">
		<col width="20px" align="center"></col>
		<col width="20px" align="center"></col>
		<col width="110px" align="center"></col>
		<col width="110px" align="center"></col>
		<col width="110px" align="center"></col>
		<col width="110px" align="center"></col>
		<col width="130px" align="right"></col>
		<col width="130px" align="right"></col>
		<col width="80px" align="center"></col>
		<col width="46px" align="center"></col>
		<tr>
			<th colspan="2"></th>
			<th>Année taxation</th>
			<th>Source taxation</th>
			<th>Type taxation</th>
			<th>Décision taxation</th>
			<th>Revenu imposable</th>
			<th>Fortune imposable</th>
			<th>Etat civil</th>
			<th>Nbre enfants</th>
		</tr>
		<tr class="amalRowOdd" style="height:26px">
			<td>
			<% if (!contribReprise) {%>
				<ct:ifhasright element="amal.revenu.revenu.nouveau" crud="c">
				<a href="<%=detailLinkRevenuNouveau%>">
					<img
						width="22px"
						height="22px"
						src="<%=request.getContextPath()%>/images/amal/tab_new.png" title="Nouveau" border="0">
					</a>
				</ct:ifhasright> 
			<% } %>
			</td>
			<td colspan="9"></td>							
		</tr>
		<tr style="background-color:#B3C4DB"><td colspan="12"></td></tr>
		<%
		String rowStyle = "";
		int i=0;
		if (viewBean.getRevenus().getSearchResults() != null) {
		for (Iterator it = Arrays.asList(viewBean.getRevenus().getSearchResults()).iterator(); it.hasNext();) {
			Revenu revenu = (Revenu) it.next();
			
			String tabId = "2";
			String detailUrlRevenu = detailLinkRevenu + revenu.getId() + "&contribuableId="+revenu.getIdContribuable();
			String detailUrlRevenuSupprimer = detailLinkRevenuSupprimer + revenu.getIdRevenu();
			detailUrlRevenuSupprimer += "&contribuableId=" + revenu.getIdContribuable();
			boolean condition = (i % 2 == 0);
			if (condition) {
				rowStyle = "amalRow";
			} else {
				rowStyle = "amalRowOdd";
			}
			i++;
			String anneeTaxation = revenu.getAnneeTaxation();
			
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
						title="<%="Détails du revenu "+anneeTaxation%>" border="0">
				</a>
				<%}else{ %>
					<img width="22px"
						height="22px"
						src="<%=request.getContextPath()%>/images/amal/<%=tabImg%>"
						title="<%="Détails du revenu "+anneeTaxation%>" border="0">
				<%} %>
				</td>
				<td>
				<% 
				// Check type source et idRevenuHistorique
				boolean taxationCanBeDeleted = false;
				if(("42002901".equals(revenu.getTypeSource()) || "42002903".equals(revenu.getTypeSource()))&& JadeStringUtil.isBlankOrZero(revenu.getIdRevenuHistorique())){
					taxationCanBeDeleted = true;
				}												
				String iconRemove = request.getContextPath()+"/images/amal/";
				String titleRemove = null;
				if(taxationCanBeDeleted){
					iconRemove+="tab_remove.png";
					titleRemove="Supprimer la taxation "+anneeTaxation;
				}else{
					iconRemove+="tab_remove_disabled.png";
					titleRemove="La taxation "+anneeTaxation+" ne peut être supprimée (utilisée ou AUTO FISC)";
				}
				if (!contribReprise) {
				%>
					<ct:ifhasright element="amal.contribuable" crud="d">
						<img
							width="22px"
							height="22px"
							onMouseOut="this.style.cursor='pointer';"
							<%if(taxationCanBeDeleted){%>
							onMouseOver="this.style.cursor='hand';" 
							onClick="delRevenu('<%=detailUrlRevenuSupprimer%>')"
							<%}%>
							
							src="<%=iconRemove%>"
							title="<%=titleRemove%>" border="0">
					</ct:ifhasright> 
				<% } %>
				</td>
				<td><%=revenu.getAnneeTaxation()%></td>
				<td><%=objSession.getCodeLibelle(revenu.getTypeSource())%></td>
				<td><%=objSession.getCodeLibelle(revenu.getTypeTaxation())%></td>
				<td><%=revenu.getDateAvisTaxation()%></td>
				<td><%=revenu.getRevenuImposableCurrency()%></td>
				<td><%=revenu.getFortuneImposableCurrency()%></td>
				<td><%=objSession.getCodeLibelle(revenu.getEtatCivil())%></td>
				<td><%=revenu.getNbEnfantsPlusEnfantsSuspens()%></td>											
			</tr>
			<tr style="background-color:#B3C4DB"><td colspan="12"></td></tr>
		<%
			} // fin for
		} // fin if
		%>
	</table>						
</div>