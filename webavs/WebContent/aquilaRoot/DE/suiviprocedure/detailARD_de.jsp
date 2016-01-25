
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCO0029"; %>
<%@ page import="globaz.aquila.db.suiviprocedure.*" %>
<%@ page import="globaz.globall.util.*" %>
<%
CODetailARDViewBean viewBean = (CODetailARDViewBean) session.getAttribute("viewBean");
selectedIdValue = viewBean.getIdDetailARD();
userActionValue = "aquila.suiviprocedure.detailARD.modifier";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="javascript">

function add() {
    document.forms[0].elements('userAction').value="aquila.suiviprocedure.detailARD.ajouter"
}

function upd() {
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add") {
        document.forms[0].elements('userAction').value="aquila.suiviprocedure.detailARD.ajouter";
    } else {
	    document.forms[0].elements('userAction').value="aquila.suiviprocedure.detailARD.modifier";
    }

    return state;

}

function cancel() {
	if (document.forms[0].elements('_method').value == "add") {
		document.forms[0].elements('userAction').value="back";
	} else {
		document.forms[0].elements('userAction').value="aquila.suiviprocedure.detailARD.afficher";
	}
}

function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="aquila.suiviprocedure.detailARD.supprimer";
        document.forms[0].submit();
    }
}

function init() {}

top.document.title = "Detail Verfolgung des Verfahrens - Detail VGS - " + top.location.href;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<%globaz.aquila.db.access.poursuite.COContentieux contentieuxViewBean = (globaz.aquila.db.access.poursuite.COContentieux) session.getAttribute("contentieuxViewBean");%>
			<span class="postItIcon">
			<ct:FWNote sourceId="<%=contentieuxViewBean.getIdContentieux()%>" tableSource="<%=contentieuxViewBean.getTableName()%>"/>
			</span>
			Verfolgung des Verfahrens - Detail VGS<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
			<%-- tpl:put name="zoneMain" --%>
			<jsp:include flush="true" page="../headerContentieux.jsp"/>

						<TR>
							<TD colspan="8"><HR></TD>
							<input type="hidden" name="idDetailARD" value="<%=viewBean.getIdDetailARD()%>"/>
							<%
            					if (JadeStringUtil.isIntegerEmpty(viewBean.getIdContentieux())) {
            				%>
	            				<input type="hidden" name="idContentieux" value="<%=request.getParameter("idContentieux")%>"/>
					        <%
					        	} else {
	        				%>
	        					<input type="hidden" name="idContentieux" value="<%=viewBean.getIdContentieux()%>"/>
	        				<%
	        					}
	        				%>
						</TR>
						<TR>
							<TD>VGS Datum</TD>
							<TD><ct:FWCalendarTag name="dateARD" value="<%=viewBean.getDateARD()%>"/></TD>
							<TD>Ursprünglicher Betrag</TD>
							<TD><INPUT type="text" name="montantARD" value="<%=JANumberFormatter.formatNoRound(viewBean.getMontantARD(), 2)%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
						</TR>
						<TR>
							<TD>Oppositionsdatum</TD>
							<TD><ct:FWCalendarTag name="dateOpposition" value="<%=viewBean.getDateOpposition()%>"/></TD>
							<TD>Antragsdatum der Aufhebung der Pfändung</TD>
							<TD><ct:FWCalendarTag name="dateRequete" value="<%=viewBean.getDateRequete()%>"/></TD>
							<TD>Verfügungscode</TD>
							<TD><input type="checkbox" name="codeDecision" <%=(viewBean.getCodeDecision().booleanValue())? "checked" : "unchecked"%> ></TD>
						</TR>
						<TR>
							<TD>Einspruchsdatum KVG</TD>
							<TD><ct:FWCalendarTag name="dateRecours" value="<%=viewBean.getDateRecours()%>"/></TD>
							<TD>Urteilsdatum KVG</TD>
							<TD><ct:FWCalendarTag name="dateJugement" value="<%=viewBean.getDateJugement()%>"/></TD>
							<TD>Urteilscode KVG</TD>
							<TD><input type="checkbox" name="codeJugement" <%=(viewBean.getCodeJugement().booleanValue())? "checked" : "unchecked"%> ></TD>
						</TR>
						<TR>
							<TD>Einspruchsdatum EVG</TD>
							<TD><ct:FWCalendarTag name="dateRecoursTFA" value="<%=viewBean.getDateRecoursTFA()%>"/></TD>
							<TD>Entscheiddatum EVG</TD>
							<TD><ct:FWCalendarTag name="dateArretTFA" value="<%=viewBean.getDateArretTFA()%>"/></TD>
							<TD>Entscheidscode EVG</TD>
							<TD><input type="checkbox" name="codeArretTFA" <%=(viewBean.getCodeArretTFA().booleanValue())? "checked" : "unchecked"%> ></TD>
						</TR>
						<TR>
							<TD> Annullierungsdatum </TD>
							<TD><ct:FWCalendarTag name="dateAnnulation" value="<%=viewBean.getDateAnnulation()%>"/></TD>
						</TR>

          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>