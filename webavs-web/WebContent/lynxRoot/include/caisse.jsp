<table border="0" cellspacing="4" class="caisse">
<tr>
	<td  height="65" width="250">
		<fieldset class="fieldSetVirement">
		<legend><%= viewBean.getSession().getLabel("INCLUDE_BENEFICIAIRE") %></legend>
		&nbsp;</BR>
		<%= viewBean.getSession().getLabel("INCLUDE_NOM") %> : &nbsp;<textarea name="adressePaiementFournisseur" style="width:240px;height:85px" class="libelleCaisseDisabled" readonly="readonly"><%=showValue?viewBean.getAdressePaiementFournisseur():""%></textarea>&nbsp;&nbsp;&nbsp;</br>
		<%	
			if ("add".equalsIgnoreCase(request.getParameter("_method")) && (request.getParameter("_valid") == null || request.getParameter("_valid").equals("fail"))) {
				tmpValidateAutoComplete = true;
			}
		%>		
	   	<input type="hidden" name="idFournisseur" value="<%=viewBean.getIdFournisseur()%>"/>
		<ct:FWPopupList name="idExterneFournisseur" onFailure="onFounisseurFailure(window.event);" onChange="updateFournisseur(tag)"  validateOnChange="<%=tmpValidateAutoComplete%>" params="<%=params%>" value="<%=tmpIdExterneFournisseur%>" className="libelle" jspName="<%=jspFournisseurLocation%>" minNbrDigit="1" autoNbrDigit="<%=autoCompleteStart%>" forceSelection="true"/>&nbsp;&nbsp;&nbsp;</BR>
		&nbsp;<input type="hidden" id="compte" name="compte">&nbsp;&nbsp;&nbsp;
		<script language="JavaScript">
			element = document.getElementById("idExterneFournisseur");
		  	element.onkeypress = function() {fillCell(this);}
		  	document.getElementById("idExterneFournisseur").style.width = '240px';
		</script>	
		</fieldset>
	</td >
	<td height="65" width="250" colspan="2">
		&nbsp;
		<input type="hidden" id="clearingBanque" name="clearing"/>
		<input type="hidden" id="adresseBanque" name="adresseBanque"/>
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
			<textarea name="libelleSociete" style="width:320px;height:100px" class="libelleCaisseDisabled" readonly="readonly"><%=viewBean.getSociete().getAdresse()%></textarea>&nbsp;&nbsp;&nbsp;</br>
		</fieldset>
	</td>	
</tr>	
</table>

