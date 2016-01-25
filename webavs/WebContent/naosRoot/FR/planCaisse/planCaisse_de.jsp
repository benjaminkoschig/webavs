<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregistrement si il n'existe pas -->
<%
	idEcran = "CAF0013";
	globaz.naos.db.planCaisse.AFPlanCaisseViewBean viewBean = (globaz.naos.db.planCaisse.AFPlanCaisseViewBean) session
			.getAttribute("viewBean");
	String jspLocation = servletContext + mainServletPath
			+ "Root/planCaisseTiers_select.jsp";
	String method = request.getParameter("_method");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">

function add() {
	document.forms[0].elements('userAction').value="naos.planCaisse.planCaisse.ajouter"
}

function upd() {
}

function validate() {
	var exit = true;
	
	document.forms[0].elements('userAction').value="naos.planCaisse.planCaisse.modifier";
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.planCaisse.planCaisse.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.planCaisse.planCaisse.modifier";
	return (exit);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="naos.planCaisse.planCaisse.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer un plan d'assurance! Voulez-vous continuer?")) {
		document.forms[0].elements('userAction').value="naos.planCaisse.planCaisse.supprimer";
		document.forms[0].submit();
	}
}

function init() {
}

function updateIdTiers(tag){
	if(tag.select && tag.select.selectedIndex != -1){
		document.getElementById('idTiers').value = tag.select[tag.select.selectedIndex].idTiers;
	}
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
Plan - Caisses Professionelles - D&eacute;tail
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
<TR>
	<TD nowrap height="31" width="100">Plan</TD>
	<TD nowrap></TD>
	<TD nowrap>
	<INPUT type="hidden" name="selectedId" value="<%=viewBean.getPlanCaisseId()%>"> 
	<INPUT name="libelleFrancais" type="text" size="40" maxlength="40" value="<%=viewBean.getLibelleFrancais()%>">&nbsp;Fran&ccedil;ais<BR>
	<INPUT name="libelleAllemand" type="text" size="40" maxlength="40" value="<%=viewBean.getLibelleAllemand()%>">&nbsp; Allemand<BR>
	<INPUT name="libelleItalien" type="text" size="40" maxlength="40" value="<%=viewBean.getLibelleItalien()%>">&nbsp; Italien</TD>
</TR>

<TR>
	<%
		if (!(method != null && method.equals("add"))) {
	%>
	<TD nowrap height="31">N&deg; de Caisse</TD>
	<TD nowrap></TD>
	<TD nowrap><INPUT name="numeroCaisse" size="20" maxlength="20"
		type="text"
		value="<%=viewBean.getAdministration().getCodeAdministration()%>"
		tabindex="-1" class="Disabled" readonly></TD>
	<%
		} else {
	%>
	<TD nowrap height="31" colspan="3"></TD>
	<%
		}
	%>
</TR>
<TR>
	<TD nowrap height="11" colspan="3">
	<hr size="3" width="100%">
	</TD>
</TR>
<%
	if (!(method != null && method.equals("add"))) {
%>
<TR>
	<TD nowrap><A
		href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getIdTiers()%>">Tiers</A></TD>
	<TD nowrap></TD>
	<TD nowrap><INPUT type="text" name="nom" size="60" maxlength="60"
		value="<%=viewBean.getAdministration().getNom()%>" tabindex="-1"
		class="Disabled" readOnly><BR>
	<%
		StringBuffer tmpLocaliteLong = new StringBuffer(viewBean
					.getAdministration().getRue().trim());
			if (tmpLocaliteLong.length() != 0) {
				tmpLocaliteLong = tmpLocaliteLong.append(", ");
			}
			tmpLocaliteLong.append(viewBean.getAdministration()
					.getLocaliteLong());
	%> <INPUT type="text" name="localiteLong" size="60"
		maxlength="60" value="<%=tmpLocaliteLong.toString()%>" tabindex="-1"
		class="Disabled" readOnly><BR>
	<INPUT type="text" name="canton" size="60" maxlength="60"
		value="<%=viewBean.getAdministration().getCantonDomicile()%>"
		tabindex="-1" class="Disabled" readOnly></TD>
</TR>
<%
	} else {
%>
<TR>
	<TD colspan="2">S&eacute;lectionner un tiers</TD>
	<TD nowrap><INPUT type="hidden" name="idTiers"
		value='<%=viewBean.getIdTiers()%>'> <%
 	String tmpValue;
 		if (!globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean
 				.getIdTiers())) {
 			tmpValue = viewBean.getAdministration()
 					.getCodeAdministration()
 					+ " - " + viewBean.getAdministration().getNom();
 		} else {
 			tmpValue = "";
 		}
 %> <ct:FWPopupList name="idTiersList" value="<%=tmpValue%>"
		className="libelle" jspName="<%=jspLocation%>" size="70"
		onChange="updateIdTiers(tag)" minNbrDigit="0" /> <IMG
		src="<%=servletContext%>/images/down.gif"
		alt="presser sur la touche 'flèche bas' pour effectuer une recherche"
		title="presser sur la touche 'flèche bas' pour effectuer une recherche"
		onclick="if (document.forms[0].elements('idTiersList').value != '') idTiersListPopupTag.validate();">
	</TD>
</TR>
<%
	}
%>
<TR>
	<TD nowrap height="31">Plan destiné aux affiliations</TD>
	<TD nowrap></TD>
	<TD nowrap>
	<%
		java.util.HashSet except = new java.util.HashSet();
		except
				.add(globaz.naos.translation.CodeSystem.TYPE_AFFILI_INDEP_EMPLOY);
	%> <ct:FWCodeSelectTag name="typeAffiliation"
		defaut="<%=viewBean.getTypeAffiliation()%>" except="<%=except%>"
		wantBlank="true" codeType="VETYPEAFFI" /></TD>
</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<%
	if (request.getParameter("_back") != null
			&& request.getParameter("_back").equals("sl")) {
%>
<SCRIPT>
</SCRIPT>
<%
	}
%>
<ct:menuChange displayId="menu" menuId="AFMenuPrincipal" />
<ct:menuChange displayId="options" menuId="AFOptionsPlanCaisse"
	showTab="options">
	<ct:menuSetAllParams key="planCaisseId"
		value="<%=viewBean.getPlanCaisseId()%>" />
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>