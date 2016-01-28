<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/find/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GCO0015";
	rememberSearchCriterias = true;
	globaz.aquila.db.plaintes.CORecherchePlaintes viewBean = (globaz.aquila.db.plaintes.CORecherchePlaintes) session.getAttribute("viewBean");
	String idCompteAuxiliaire = request.getParameter("selectedId");
	actionNew += "&idCompteAuxiliaire=" + viewBean.getIdCompteAuxiliaire();
	//java.util.Map societes = globaz.aquila.util.COAdministrateurUtil.getSocietes(viewBean.getIdCompteAuxiliaire(), viewBean.getISession());
	java.util.Map societes = viewBean.getSocietes();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu" />
<ct:menuChange displayId="options" menuId="CO-OptionsDefaut"
	showTab="menu" />

<SCRIPT language="javaScript">
bFind = true;

var usrAction = "aquila.plaintes.plaintePenale.lister";

function societeChangee(select){
document.location.href='<%=servletContext + mainServletPath + "?userAction=aquila.plaintes.plaintePenale.chercher&selectedId="%>'+select.options[select.selectedIndex].value;
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
Suche der Strafanzeigen
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
<TR>
	<TD>Verwalter-Nr. :</td>
	<td><INPUT type="text" name="numeroAdministrateur"
		class="disabled" readonly
		value="<%=viewBean.getNumeroAdministrateur()%>"></TD>
	</td>
	<TD>&nbsp;Name : <INPUT type="text"
		value="<%=viewBean.getNomAdministrateur()%>" class="libelleSectionLongDisabled" readonly></TD>
	<TD><INPUT type="hidden" name="forIdCompteAuxiliaire"
		value="<%=idCompteAuxiliaire%>"></TD>
</TR>
<TR>
	<TD >Gesellschaft :</td>
	<td COLSPAN="3"><SELECT name="societe" onchange="societeChangee(this);">
		<%
				if (societes != null) {
				java.util.Iterator iterator = societes.keySet().iterator();

				while (iterator.hasNext()) {
					String idCA = (String) iterator.next();
					String nomSociete = (String) societes.get(idCA);
		%>
		<OPTION value="<%=idCA%>"
			<%=idCA.equals(viewBean.getIdCompteAuxiliaire())?"selected":""%>><%=nomSociete%></OPTION>
		<%
			}
			}
		%>
	</SELECT></td>
</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf"%>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf"%>
<%-- /tpl:insert --%>