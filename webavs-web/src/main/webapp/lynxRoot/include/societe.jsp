<%String jspLocationSociete = servletContext + "/lynxRoot/autocomplete/societe_select.jsp";
	
	String classNameSociete = "libelle";
	globaz.lynx.db.societesdebitrice.LXSocieteDebitrice societeUnique = null;
	if (!globaz.lynx.utils.LXSocieteDebitriceUtil.hasSeveralSociete(objSession)) {
		classNameSociete = "libelleLongDisabled";
		
		societeUnique = globaz.lynx.utils.LXSocieteDebitriceUtil.getOnlyOneSociete(objSession);
	}
	String idSociete = "";
	String libelleSociete = "";
	String idExterneSociete = "";
	boolean validateOnChange = true;
	
	if (viewBean != null && viewBean.getSociete() != null && !viewBean.getSociete().isNew()) {
		idSociete = viewBean.getSociete().getIdSociete();
		libelleSociete = viewBean.getSociete().getNom();
		idExterneSociete = viewBean.getSociete().getNumRubrique();
		validateOnChange = false;
	} else if (societeUnique != null && !societeUnique.isNew()) {
		idSociete = societeUnique.getIdSociete();
		libelleSociete = societeUnique.getNom();
		idExterneSociete = societeUnique.getIdExterne();
		validateOnChange = false;
	}%>
<input type="hidden" name="idSociete" value="<%=idSociete%>"/>
<input type="hidden" name="forIdSociete" value="<%=idSociete%>"/>
<ct:FWPopupList name="idExterneSociete" onFailure="onSocieteFailure(window.event);" onChange="updateSociete(tag)"  validateOnChange="<%=validateOnChange%>" params="" value="<%=idExterneSociete%>" className="<%=classNameSociete%>" jspName="<%=jspLocationSociete%>" minNbrDigit="1" autoNbrDigit="3" forceSelection="true" tabindex="1"/>
&nbsp;
<INPUT type="text" name="libelleSociete" value="<%=libelleSociete%>" size="45" maxlength="40" readonly="readonly" class="libelleLongDisabled">
