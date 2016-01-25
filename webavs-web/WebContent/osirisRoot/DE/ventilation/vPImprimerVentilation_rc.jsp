<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%
	idEcran = "GCA3023";
	rememberSearchCriterias = true;
	bButtonNew = false;
	
	String idCompteAnnexe = request.getParameter("idCompteAnnexe");
	
	globaz.osiris.db.ventilation.CAVPImprimerVentilationListViewBean manager = new globaz.osiris.db.ventilation.CAVPImprimerVentilationListViewBean();
	manager.setSession((globaz.globall.db.BSession)controller.getSession());
	manager.setForIdCompteAnnexe(request.getParameter("idCompteAnnexe"));
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
	var usrAction = "osiris.ventilation.vPImprimerVentilation.lister";
	bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Suche von Sektionen<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<tr>
					            <td class="label" width="70" valign="top" rowspan="3">Konto
					            <input type="hidden" name="forIdCompteAnnexe" value="<%=idCompteAnnexe%>"/>
					            </td>
					            <td class="control" valign="top" rowspan="3" colspan="2" width="200">
					              <TEXTAREA cols="40" rows="3" class="libelleLongDisabled" readonly><%=manager.getCompteAnnexeTitulaireEntete()%></TEXTAREA>
					            </td>
	            <td></td>
							</tr>
			<tr>
				<td class="label"></td>
				<td class="control" colspan="2"></td>
				<td></td>
			</tr>
			<tr>
				<td class="label"></td>
				<td class="control" colspan="2"></td>
				<td></td>
			</tr>
			<tr>
				<td class="label">Ab</td>
				<td class="control">
					<input type="text" name="likeIdExterne" value="">
				</td>
				<td class="label">
					Sektionen&nbsp;
				</td>
				<td class="control"><select name="forSelectionSections" class="libelleCourt">
	                <option value="1000">alle</option>
	                <option value="1">offenen</option>
	                <option value="2">saldiert</option>
	              </select>
	            </td>
			</tr>
							<input type="hidden" name="forIdCompteAnnexe" value="<%=request.getParameter("selectedId")%>">
							<input type="hidden" name="orderBy" value="DATEDEBUTPERIODE">
						<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>