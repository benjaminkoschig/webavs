<table border="0" cellspacing="4" class="extourne">
<tr>
	<td  height="65" width="250">
		<fieldset class="fieldSetVirement">
		<legend>B?n?ficiaire</legend>
		&nbsp;</BR>
		Nom : &nbsp;<textarea name="adressePaiementFournisseur" style="width:240px;height:85px" class="libelleDisabled" readonly="readonly"><%=showValue?viewBean.getAdressePaiementFournisseur():""%></textarea>&nbsp;&nbsp;&nbsp;</br>
		<%
			if ("add".equalsIgnoreCase(request.getParameter("_method")) && (request.getParameter("_valid") == null || request.getParameter("_valid").equals("fail"))) {
				tmpValidateAutoComplete = true;
			}
		%>
	   	<input type="hidden" name="idFournisseur" value="<%=viewBean.getIdFournisseur()%>" />
	   	<input type="text" name="idExterne" value="<%=viewBean.getIdExterneFournisseur()%>" style="width:240px;"/>&nbsp;&nbsp;&nbsp;</BR>
		&nbsp;<input type="hidden"  id="compte" name="compte"/>&nbsp;&nbsp;&nbsp;
		<script language="JavaScript">
		  	document.getElementById("idExterneFournisseur").style.width = '220px';
		</script>
		</fieldset>
	</td >
	<td height="65" width="250" colspan="2">

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
		<legend>Paiement</legend>
			&nbsp;</BR>
			Montant : &nbsp;<input type="text" id="montant" name="montant" value="<%=showValue?viewBean.getMontant():""%>" style="width: 180px;text-align:right;height:22px" />
			<%
				if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectCodeIsoMonnaieSelect)) {
					out.print(selectCodeIsoMonnaieSelect);
				}
			%>&nbsp;&nbsp;&nbsp;</br>
			 &nbsp;&nbsp;&nbsp;&nbsp;</br>
		</fieldset>
	</td>
	<td colspan="2">
		<fieldset class="fieldSetVirement" style="height:155px;">
			<legend>Soci?t? d?bitrice</legend>
			&nbsp;</BR>
			<textarea name="libelleSociete" style="width:320px;height:100px" class="libelleDisabled" readonly="readonly"><%=viewBean.getSociete().getAdresse()%></textarea>&nbsp;&nbsp;&nbsp;</br>
		</fieldset>
	</td>
</tr>
</table>