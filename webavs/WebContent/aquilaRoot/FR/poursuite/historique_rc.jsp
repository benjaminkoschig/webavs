<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.aquila.db.access.poursuite.*"%>
<%
	COContentieux contentieuxViewBean = (COContentieux) session.getAttribute("contentieuxViewBean");

	String idContentieux = "";
	String idEtape = "";
	String idSequence = "";
	String idSection = request.getParameter("idSection");
	String idEtapePrecedent = "";

	try {
		idContentieux = contentieuxViewBean.getIdContentieux();
		idEtape = contentieuxViewBean.getEtape().getIdEtape();
		idSequence = contentieuxViewBean.getSequence().getIdSequence();
		idEtapePrecedent = contentieuxViewBean.getIdEtapePrecedenteNonSpecifique();
	} catch (Exception e) {
	}

	idEcran = "GCO0003";
	IFrameHeight = "240";
	rememberSearchCriterias = true;
	bButtonNew = false;

	String etapeSuivante = "aquila?userAction=aquila.poursuite.etapeSuivante.lister&orderByLibEtapeCSOrder=true&forIdEtape=" + idEtape + "&forIdSequence=" + idSequence + "&orIdEtapePrecedent=" + idEtapePrecedent;
	int etapeSuivanteHeight = 22 * 4;
%>
<%@ taglib uri="/WEB-INF/aquila.tld" prefix="co"%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<jsp:include flush="true" page="../menuChange.jsp"/>
<SCRIPT language="JavaScript">
	usrAction = "aquila.poursuite.historique.lister";
	bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
		<% if (contentieuxViewBean != null && !contentieuxViewBean.getIdContentieux().equals("-1")) {%>
			<span class="postItIcon">
			<ct:FWNote sourceId="<%=contentieuxViewBean.getIdContentieux()%>" tableSource="<%=contentieuxViewBean.getTableName()%>"/>
			</span>
		<% } %>
			Suivi des étapes<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<jsp:include flush="true" page="../headerContentieux.jsp"/>
						<TR>
							<TD class="control" colspan="8">
								<INPUT type="hidden" name="forIdContentieux" value="<%=idContentieux.equals("")?"-1":idContentieux%>">
								<INPUT type="hidden" name="notForLibEtape" value="<%=globaz.aquila.api.ICOEtape.CS_AUCUNE%>">

								<IFRAME src="<%=etapeSuivante%>" scrolling="auto"  style="border: solid 1px black; width: 100%;" height="auto" width="auto"></IFRAME>
							</TD>
						</TR>
			          <TR><TD colspan="8"><br/><HR></TD></TR>
			          <TR><TD colspan="8" class="title">Historique des étapes</TD></TR>
						<TR>
							<TD class="label">Etape</TD>
							<TD class="control" colspan="3">
								<co:COListSelect name="forIdEtape" manager="globaz.aquila.db.batch.COEtapeListViewBean" valueProperty="getIdEtape" labelProperty="getLibEtapeLibelle" wantBlank="true">
									<co:COListParam name="forIdSequence" value="<%=idSequence%>"/>
									<co:COListParam name="orderByLibEtapeCSOrder" value="true"/>
								</co:COListSelect>
							</TD>
							<TD class="label">Date déclench.</TD>
							<TD class="control"><ct:FWCalendarTag name="forDateDeclenchement" doClientValidation="CALENDAR" value=""/></TD>
							<TD class="label">Date exécution</TD>
							<TD class="control" nowrap="true"><ct:FWCalendarTag name="forDateExecution" doClientValidation="CALENDAR" value=""/></TD>
						</TR>
						<tr>
							<td class="label" colspan="4">
								<input id="annule" type="checkbox" value="true" name="afficheEtapesAnnulees">
								<label for="annule">Afficher les étapes annulées</label>
							</td>
							<td class="label" colspan="2"> Trié par
				                <SELECT name="forTriHistorique" class="libelleCourt">
				                  <option selected value="1">id historique</option>
				                  <option value="2">date d'exécution</option>
				                </SELECT>
				            </td>
							<td class="label" colspan="2">
								<input id="triDecroissant" type="checkbox" value="true" name="triDecroissant">
								<label for="triDecroissant">Tri décroissant</label>
							</td>
						</tr>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>