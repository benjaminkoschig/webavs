<table border="0" cellspacing="4" class="bvrRouge">

<tr>
	<td height="26" colspan="3">&nbsp;</td>
</tr>  		
<tr>
	<td  height="65" width="250"><textarea name="adressePaiementFournisseur" style="width:230px;height:65px" class="libelleBvrRougeDisabled" readonly="readonly"><%=showValue?viewBean.getAdressePaiementFournisseur():""%></textarea></td>
	<td colspan="2">&nbsp;<textarea name="motif" style="width:210px;height:65px" class="libelle" ><%=showValue?viewBean.getMotif():""%></textarea></td>
</tr>

<tr>
	<td height="20">
	<%
		if ("add".equalsIgnoreCase(request.getParameter("_method")) && (request.getParameter("_valid") == null || request.getParameter("_valid").equals("fail"))) {
			tmpValidateAutoComplete = true;
		}
	%>
	
   	<input type="hidden" name="idFournisseur" value="<%=viewBean.getIdFournisseur()%>"/>
	<ct:FWPopupList name="idExterneFournisseur" onFailure="onFounisseurFailure(window.event);" onChange="updateFournisseur(tag)"  validateOnChange="<%=tmpValidateAutoComplete%>" params="<%=params%>" value="<%=tmpIdExterneFournisseur%>" className="libelle" jspName="<%=jspFournisseurLocation%>" minNbrDigit="1" autoNbrDigit="<%=autoCompleteStart%>" forceSelection="true"/>
	<script language="JavaScript">
		element = document.getElementById("idExterneFournisseur");
	  	element.onkeypress = function() {fillCell(this);}
		document.getElementById("idExterneFournisseur").style.width = '230px';
	</script>	
	</td>
	<td colspan="2" width="360">&nbsp;</td>
</tr>  
						
<tr>
	<td>
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
	<td width="242" colspan="2" height="28">&nbsp;<input type="hidden" name="referenceBVR" /></td>
</tr>
						
<tr>
	<td height="31">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="ccpFournisseur" value="<%=showValue?viewBean.getCcpFournisseur():""%>" style="width: 120px;" class="libelleBvrRougeDisabled" readonly="readonly"/></td>
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
		<br/><br/><textarea name="nomBanque" style="width:230px;height:65px" class="libelleBvrRougeDisabled" readonly="readonly"><%=showValue?viewBean.getNomBanque():""%></textarea>
	<script language="JavaScript">
		element = document.getElementById("csCodeIsoMonnaie");
	  	element.onchange = function() {updateMontant();}
	</script>	
	</td>
	<td colspan="2"><textarea name="libelleSociete" style="width:330px;height:100px" class="libelleBvrRougeDisabled" readonly="readonly"><%=viewBean.getSociete().getAdresse()%></textarea></td>	
</tr>	
<tr>
	<td colspan ="3" height="110" align="right">
		<ct:FWPopupList name="lectureOptique" onFailure="onCodeBVRFailure(window.event);" onChange="updateBVR(tag);updateMontant();updateSum();"  validateOnChange="true" params="<%=paramsLectureOptique%>" value="" className="libelle" jspName="<%=jspFournisseurLigneCodageBVRRouge%>" minNbrDigit="1" forceSelection="true" autoNbrDigit="10"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	</td>
</tr>
</table>

