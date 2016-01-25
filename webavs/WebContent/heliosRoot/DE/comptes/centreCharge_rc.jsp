<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.helios.db.interfaces.*,globaz.helios.db.comptes.*" %>
<%
idEcran="GCF4009";
CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean )session.getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
usrAction = "helios.comptes.centreCharge.lister";
bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Kostenstellenübersicht<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

		<tr>
	            <TD colspan="6">&nbsp;Mandant&nbsp;<input name='libelle' class="libelleLongDisabled" readonly value="<%=exerciceComptable.getMandat().getLibelle()%>">&nbsp;Rechnungsjahr&nbsp;<input name='fullDescription' readonly class="libelleLongDisabled" value="<%=exerciceComptable.getFullDescription()%>"></TD>
		</tr>
		<tr><td colspan="6"><hr></td></tr>
            <tr>

	            <TD width="">&nbsp;Ab&nbsp;</TD>
	            <TD width=""><INPUT name="fromNumero" class="libelle" value="" size="20"></TD>

				<td>
					<input type="hidden" name="forIdMandat" value="<%=exerciceComptable.getIdMandat()%>">
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