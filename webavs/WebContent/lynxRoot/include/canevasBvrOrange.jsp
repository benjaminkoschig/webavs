 <table border="0" cellspacing="4" class="canevasBvrOrange">

<tr>
	<td height="25" colspan="3">&nbsp;</td>
</tr>  		
<tr>
	<td  height="65" width="250"><textarea name="adressePaiementFournisseur" style="width:230px;height:65px" class="libelleBvrOrangeDisabled" readonly="readonly"><%=showValue?viewBean.getAdressePaiementFournisseur():""%></textarea></td>
	<td colspan="2">&nbsp;<input type="hidden" name="motif"/></td>
</tr>

<tr>
	<td height="20">	
   	<input type="hidden" name="idFournisseur" value="<%=viewBean.getIdFournisseur()%>"/>
   	<ct:FWPopupList name="idExterneFournisseur" onFailure="onFounisseurFailure(window.event);" onChange="updateFournisseur(tag)"  validateOnChange="<%=tmpValidateAutoComplete%>" params="<%=params%>" value="<%=tmpIdExterneFournisseur%>" className="libelle" jspName="<%=jspFournisseurLocation%>" minNbrDigit="1" autoNbrDigit="<%=autoCompleteStart%>" forceSelection="true"/>
	<script language="JavaScript">
		element = document.getElementById("idExterneFournisseur");
	  	element.onkeypress=function() {fillCell(this);}
		document.getElementById("idExterneFournisseur").style.width = '210px';
	</script>	
	</td>
	<td colspan="2" width="360">&nbsp;</td>
</tr>  
						
<tr>
	<td align="left">
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

	</td>
	<td width="242" colspan="2"><input type="text" onKeyPress="fillCell(this);" name="referenceBVR" id="referenceBVR" value="<%=showValue?viewBean.getReferenceBVR():""%>" style="width:338px;" /></td>
</tr>
						
<tr>
	<td height="31">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="ccpFournisseur" value="<%=showValue?viewBean.getCcpFournisseur():""%>" style="width: 120px;" class="libelleBvrOrangeDisabled" readonly="readonly"/></td>
	<td valign="2">&nbsp;</td>
</tr>
<tr>
	<td valign="top">&nbsp;
		<input type="text" onKeyPress="fillCell(this);" id="montant" name="montant" value="<%=showValue?viewBean.getMontantFormatted():""%>" style="width: 165px;text-align:right;height:22px" onChange="validateFloatNumber(this);updateMontant();updateSum();"/>
		<%
			if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectCodeIsoMonnaieSelect)) {
				out.print(selectCodeIsoMonnaieSelect);

			}
		%>
	<script language="JavaScript">
		element = document.getElementById("csCodeIsoMonnaie");
	  	element.onchange = function() {updateMontant();}
	</script>	
	</td>
	<td colspan="2">
		<textarea name="libelleSociete" style="width:330px;height:100px" class="libelleBvrOrangeDisabled" readonly="readonly"></textarea><br/>
		<% String idSociete = JadeStringUtil.isIntegerEmpty(viewBean.getIdSociete()) ? "" : viewBean.getIdSociete(); %>
		<ct:FWPopupList name="idExterneSociete" onFailure="onSocieteFailure(window.event);" onChange="updateSociete(tag)"  validateOnChange="true" params="<%=paramsSociete%>" value="<%=idSociete%>" className="libelle" jspName="<%=jspLocationSociete%>" minNbrDigit="1" autoNbrDigit="3" forceSelection="true" tabindex="1"/>
		<script language="JavaScript">
			document.getElementById("idExterneSociete").style.width = '328px';
		</script>
	</td>	
</tr>	
<tr>
	<td colspan ="3" height="60">
		&nbsp;<ct:FWPopupList name="lectureOptique" onFailure="onCodeBVRFailure(window.event);" onChange="updateBVR(tag);updateMontant();updateSum();"  validateOnChange="true" params="<%=paramsLectureOptique%>" value="" className="libelle" jspName="<%=jspFournisseurLigneCodage%>" minNbrDigit="1" forceSelection="true"/>
		<script language="JavaScript">
			document.getElementById("lectureOptique").style.width = '580px';
		</script>
	</td>
</tr>
</table>

