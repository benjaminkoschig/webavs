<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
  <%@ page import="globaz.framework.util.*" %>
  <%@ page import="globaz.osiris.db.contentieux.*" %>
  <%
CAParametreEtapeManagerListViewBean viewBean = (CAParametreEtapeManagerListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
globaz.osiris.db.comptes.CASectionViewBean element = (globaz.osiris.db.comptes.CASectionViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
globaz.osiris.api.APICompteAnnexe compteAnnexe = element.getCompteAnnexe();
size = viewBean.size();
detailLink ="osiris?userAction=osiris.contentieux.operationContentieux.afficher&selectedId=";
CAParametreEtapeViewBean _paramEtape = null ;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH colspan="2" width="210">Etappe</TH>
    <TH width="130" nowrap>Einleitung ab</TH>
    <TH width="120">Ausführungsdatum</TH>
    <TH width="120">Betrag</TH>
    <TH width="100">Gebühren</TH>
    <TH>Remarques</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%	_paramEtape = (globaz.osiris.db.contentieux.CAParametreEtapeViewBean) viewBean.getEntity(i);
			_paramEtape.setIdSection(element.getIdSection());
			String param = _paramEtape.getIdParametreEtape()+"&sectionId="+element.getIdSection()+"&parametreEtapeId="+_paramEtape.getIdParametreEtape();
			// ALD modif: éviter l'erreur de script dûe à l'apostrophe de fin
			if (_paramEtape.getEvenementContentieux(element) == null){
				param += "&_method=add";
			}

			actionDetail = "parent.location.href='"+detailLink+ param + "'";


		%>
<!--

    <TD width="10"><A href="<%=detailLink+param%>" target="fr_main"><IMG src="images/loupe.gif" border="0"></A></TD>-->

	<TD class="mtd" width="16" >
	<ct:menuPopup menu="CA-DetailOperationContentieux" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink + param)%>">
		<ct:menuParam key="idParametreEtape" value="<%=_paramEtape.getIdParametreEtape()%>"/>
		<ct:menuParam key="idSection" value="<%=element.getIdSection()%>"/>
<%		if (_paramEtape.getEvenementContentieux(element) == null){%>
				<ct:menuParam key="_method" value="add"/>
		<% } %>
    </ct:menuPopup>
	</TD>
	<% if (_paramEtape.getEvenementContentieux(element) != null) {
		FWCurrency cMontant = _paramEtape.getEvenementContentieux(element).getMontantToCurrency();
		FWCurrency cTaxe = _paramEtape.getEvenementContentieux(element).getTaxesToCurrency();
		String sDate = _paramEtape.getDateDeclenchement(element);

		globaz.osiris.db.comptes.CAOperationViewBean operCtc = new globaz.osiris.db.comptes.CAOperationViewBean();
		operCtc.setSession(objSession);
		operCtc.setIdOperation(_paramEtape.getEvenementContentieux(element).getIdOperation());
		operCtc.retrieve();

		boolean isActive = true;
		if (!operCtc.isNew()) {
			isActive = operCtc.getEstActive().booleanValue();
		}
		%>


		<TD class="mtd" nowrap onClick="<%=actionDetail%>" width="200" style="color : red;"><%=_paramEtape.getEtape().getDescription()%></TD>
	  	<TD class="mtd" nowrap onClick="<%=actionDetail%>" nowrap style="color : red;" align="center"><%if (_paramEtape.getEvenementContentieux(element).getEstIgnoree().booleanValue()){%>Ignorer<%} else if (sDate != null) {%><%=sDate%><% } %></TD>

	  	<%if (_paramEtape.getEvenementContentieux(element).getEstDeclenche().booleanValue() && isActive) {%>
		  	<TD class="mtd" nowrap onClick="<%=actionDetail%>" style="color : red;" align="center"><%=_paramEtape.getEvenementContentieux(element).getDateExecution()%></TD>
		  	<TD class="mtd" nowrap onClick="<%=actionDetail%>" style="color : red;" align="right"><% if (!cMontant.isZero()) {%><%=cMontant.toStringFormat()%><%}%></TD>
		  	<TD class="mtd" nowrap onClick="<%=actionDetail%>" style="color : red;" align="right"><% if (!cTaxe.isZero()) {%><%=cTaxe.toStringFormat()%><%}%></TD>
		<% }
		else {%>
      		<TD class="mtd" nowrap onClick="<%=actionDetail%>"></TD>
			<TD class="mtd" nowrap onClick="<%=actionDetail%>"></TD>
			<TD class="mtd" nowrap onClick="<%=actionDetail%>"></TD>
	<% }%>
		    <% if (_paramEtape.getEvenementContentieux(element).getRemarque() != null) {%>
				<TD class="mtd" nowrap onClick="<%=actionDetail%>" style="color : red;"><%=_paramEtape.getEvenementContentieux(element).getRemarque().getTexte()%></TD>
			<% } else {%>
				<TD class="mtd" nowrap onClick="<%=actionDetail%>" style="color : red;"></TD>
			<% }%>
	<% }
	 else {
			String sDate = _paramEtape.getDateDeclenchement(element);%>
			<TD class="mtd" onClick="<%=actionDetail%>" nowrap width="200"><%=_paramEtape.getEtape().getDescription()%></TD>
      		<TD class="mtd" nowrap onClick="<%=actionDetail%>" align="center"><%if (sDate != null) {%><%=sDate%><%}%></TD>
      		<TD class="mtd" nowrap onClick="<%=actionDetail%>"></TD>
			<TD class="mtd" nowrap onClick="<%=actionDetail%>"></TD>
			<TD class="mtd" nowrap onClick="<%=actionDetail%>"></TD>
			<TD class="mtd" nowrap onClick="<%=actionDetail%>"></TD>
	<% } %>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<ct:menuChange displayId="options" menuId="CA-DetailSectionGauche" showTab="options">
		<ct:menuSetAllParams key="id" value="<%=element.getIdSection()%>"/>
		<ct:menuSetAllParams key="idSection" value="<%=element.getIdSection()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=element.getIdSection()%>"/>
		<ct:menuSetAllParams key="noAffiliationId" value="<%=compteAnnexe.getIdExterneRole()%>"/>
		<ct:menuSetAllParams key="idCompteAnnexe" value="<%=compteAnnexe.getIdCompteAnnexe()%>"/>
		<ct:menuSetAllParams key="idPlanRecouvrement" value="<%=element.getIdPlanRecouvrement()%>"/>
		<ct:menuSetAllParams key="forIdSection" value="<%=element.getIdSection()%>"/>
		
		<% if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(element.getIdPlanRecouvrement())) {%>
		<ct:menuActivateNode active="no" nodeId="echeances_plan"/>
		<% } else { %>
		<ct:menuActivateNode active="yes" nodeId="echeances_plan"/>
		<% } %>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>