<table border="0" cellspacing="4" class="virement">
<tr>
	<td  height="65" width="250">
		<fieldset class="fieldSetVirement">
		<legend><%= viewBean.getSession().getLabel("INCLUDE_BENEFICIAIRE") %></legend>
		&nbsp;</BR>
		<%= viewBean.getSession().getLabel("INCLUDE_NOM") %> : &nbsp;<textarea name="adressePaiementFournisseur" style="width:240px;height:85px" class="libelleVirementDisabled" readonly="readonly"><%=showValue?viewBean.getAdressePaiementFournisseur():""%></textarea>&nbsp;&nbsp;&nbsp;</br>
		<%	
			if ("add".equalsIgnoreCase(request.getParameter("_method")) && (request.getParameter("_valid") == null || request.getParameter("_valid").equals("fail"))) {
				tmpValidateAutoComplete = true;
			}
		%>		
	   	<input type="hidden" name="idFournisseur" value="<%=viewBean.getIdFournisseur()%>"/>
		<ct:FWPopupList name="idExterneFournisseur" onFailure="onFounisseurFailure(window.event);" onChange="updateFournisseur(tag)"  validateOnChange="<%=tmpValidateAutoComplete%>" params="<%=params%>" value="<%=tmpIdExterneFournisseur%>" className="libelle" jspName="<%=jspFournisseurLocation%>" minNbrDigit="1" autoNbrDigit="<%=autoCompleteStart%>" forceSelection="true"/>&nbsp;&nbsp;&nbsp;</BR>
		<script language="JavaScript">
			element = document.getElementById("idExterneFournisseur");
		  	element.onkeypress = function() {fillCell(this);}
		  	document.getElementById("idExterneFournisseur").style.width = '240px';
		</script></BR>
		
		<%if (!"add".equalsIgnoreCase(request.getParameter("_method"))) {%>
		<ct:FWSelectorTag name="selecteurAdresses" 
		methods="<%=viewBean.getMethodesSelectionAdressePaiement()%>"
		providerApplication="pyxis"
		providerPrefix="TI"
		providerAction="pyxis.adressepaiement.adressePaiement.chercher"
		target="fr_main"
		redirectUrl="<%=mainServletPath%>"/>
		
		&nbsp;
			<%if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdAdressePaiement())) {%>
				<%= viewBean.getSession().getLabel("INCLUDE_FORCER_ADRESSE") %>
			<%} else {%>
				<%= viewBean.getSession().getLabel("INCLUDE_ADRESSE_SELECTIONNEE") %>
			<%}%>
		<%}%>
		<input type="hidden" name="idAdressePaiement" value="<%=viewBean.getIdAdressePaiement()%>"/>	
		
		</fieldset>
	</td >
	<td height="65" width="250" colspan="2">
		<fieldset class="fieldSetVirement">
		<legend><%= viewBean.getSession().getLabel("INCLUDE_BANQUE_BENEFICIAIRE") %></legend>
		&nbsp;</BR>
		<%= viewBean.getSession().getLabel("INCLUDE_NOM") %> : &nbsp;<textarea name="adresseBanque" style="width:240px;height:85px" class="libelleVirementDisabled"><%=showValue?viewBean.getAdresseBanque():""%></textarea>&nbsp;&nbsp;&nbsp;</BR>
		<%= viewBean.getSession().getLabel("INCLUDE_NUM_CLEARING") %> : &nbsp;<input type="text" onKeyPress="fillCell(this);" id="clearingBanque" name="clearing" value="<%=showValue?viewBean.getClearingBanque():""%>" style="width: 240px;text-align:right;height:22px" class="libelleVirementDisabled"/>&nbsp;&nbsp;&nbsp;</BR>
		<%= viewBean.getSession().getLabel("INCLUDE_COMPTE") %> : &nbsp;<input type="text" id="compte" name="compte" value="<%=showValue?viewBean.getCompte():""%>" class="libelleVirementDisabled" style="width: 240px;text-align:right;height:22px" />&nbsp;&nbsp;&nbsp;
		</fieldset>
	</td>
</tr>							
<tr>
	<td colspan="2">&nbsp;
		<input type="hidden" name="ccpFournisseur" value="<%=showValue?viewBean.getCcpFournisseur():""%>"/>
		<input type="hidden" name="referenceBVR" />
		<input type="hidden" name="lectureOptique"/>
	</td>
</tr>
<tr>
	<td>
		<fieldset class="fieldSetVirement" style="height:155px;">
		<legend><%= viewBean.getSession().getLabel("INCLUDE_PAIEMENT") %></legend>
			&nbsp;</BR>
			<%= viewBean.getSession().getLabel("INCLUDE_MONTANT") %> : &nbsp;<input type="text" onKeyPress="fillCell(this);" id="montant" name="montant" value="<%=showValue?viewBean.getMontantFormatted():""%>" style="width: 183px;text-align:right;height:22px" onChange="validateFloatNumber(this);updateMontant();updateSum();"/>
			<%
				if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectCodeIsoMonnaieSelect)) {
					out.print(selectCodeIsoMonnaieSelect);
				}
			%>&nbsp;&nbsp;&nbsp;</br>
			<script language="JavaScript">
				element = document.getElementById("csCodeIsoMonnaie");
			  	element.onchange = function() {updateMontant();}
			</script>
			<%= viewBean.getSession().getLabel("INCLUDE_MOTIF") %> : &nbsp;<textarea name="motif" style="width:240px;height:65px" class="libelle" ><%=showValue?viewBean.getMotif():""%></textarea>&nbsp;&nbsp;&nbsp;</br>
		</fieldset>
	</td>
	<td colspan="2">
		<fieldset class="fieldSetVirement" style="height:155px;">
			<legend><%= viewBean.getSession().getLabel("INCLUDE_SOCIETE_DEBITRICE") %></legend>
			&nbsp;</BR>
			<textarea name="libelleSociete" style="width:320px;height:100px" class="libelleVirementDisabled" readonly="readonly"><%=viewBean.getSociete().getAdresse()%></textarea>&nbsp;&nbsp;&nbsp;</br>
		</fieldset>
	</td>	
</tr>	
</table>

