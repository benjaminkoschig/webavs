<!------------------------------------------------------------------------------
  Création de la ventilation
-------------------------------------------------------------------------------->
<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
	<TBODY>
		<tr>
			<td align="right" height="24">
				<%if (viewBean.isJournalEditable() && objSession.hasRight("lynx.facture.facture.afficher", globaz.framework.secure.FWSecureConstants.UPDATE)) {%>
				<a href="#" border="noborder" onclick="showNextRow();focusOnNextCompte();" id="plus"><img src="<%=request.getContextPath()%>/images/plus.jpg" border="0" title="Zeile hinzufügen"/></a>
				<%} else {%>
				&nbsp;
				<%}%>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" cellspacing="0" class="borderBlack">
					<tr>
						<th align="left">Konto</th>
						<th align="left">Kostenstelle</th>
						<th>Bezeichnung</th>
						<th>Soll</th>
						<th>Haben</th>
						<th>Betrag (&euro;,&#36;...)</th>
						<th>Kurs</th>
					</tr>
					<%
						for (int i = 0; i < viewBean.getMaxRows() ; i++) {
							String style = "row";
							if (i % 2 == 1) {
								style = "rowOdd";
							}
					%>
					<tr class="<%=style%>" id="ligne<%=i%>">
						<td style="vertical-align: middle; text-align: left;" class="mtdShortPadding">
						<%
					        String tmpIdext = "idext" + i;
					    	String tmpIdextScript = "updateCompte(tag, " + i + ");updateSum();";
					    	String tmpIdextValue = showValue?viewBean.getIdExt(i):"";
					    %>
					    <ct:FWPopupList name="<%=tmpIdext%>" onFailure="onCompteFailure(window.event);" onChange="<%=tmpIdextScript%>" validateOnChange="true" params="<%=params2%>" value="<%=tmpIdextValue%>" className="compte" jspName="<%=jspLocation%>" minNbrDigit="1" autoNbrDigit="<%=autoCompleteStart%>" forceSelection="true"/>
						<input type="hidden" name="idc<%=i%>" value="<%=viewBean.getIdCompte(i)%>"/>
						<input type="hidden" name="idv<%=i%>" value="<%=viewBean.getIdVentilation(i)%>"/>
					
						<script language="JavaScript">
							element = document.getElementById("<%=("idext" + i)%>");
						  	element.onkeypress=function() {fillCell(this);}
						</script>
					
						</td>
						<td style="vertical-align: middle; text-align: left;" class="mtdShortPadding">
						<%
					        String tmpIdcc = "idcc" + i;
					        String tmpIdccValue = showValue?viewBean.getIdCompteCharge(i):"0";
					        
					    %>
						<ct:FWListSelectTag name="<%=tmpIdcc%>" defaut="<%=tmpIdccValue%>" data="<%=centreChargeListe%>"/>
						<script language="JavaScript">
							document.getElementById("<%=tmpIdcc%>").style.width = '135px';
						</script>
						</td>
						<td style="vertical-align: middle; text-align: left;" class="mtdShortPadding">
						<%
					        String tmpLibelle = "l" + i;
					        String tmpLibelleValue = showValue?viewBean.getLibelle(i):"";
					    %>
						<ct:FWPopupList name="<%=tmpLibelle%>" size="26" maxlength="40" onFailure="onLibelleFailure(window.event);" onChange="fillCell(this);" params="<%=paramsLibelle%>" value="<%=tmpLibelleValue%>" validateOnChange="false" className="libelle" jspName="<%=jspLocationLibelle%>" minNbrDigit="2" forceSelection="false"/>
						
						<script language="JavaScript">
							element = document.getElementById("<%=("l" + i)%>");
						  	element.onkeypress=function() {fillCell(this);}
						</script>
						
						</td>
					
