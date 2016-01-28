<%@page import="globaz.aquila.db.access.batch.*"%>
<%@page import="java.util.Iterator"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
// renseigné dans la jsp historique_de.jsp
Iterator infoIter = (Iterator) request.getAttribute("globaz.aquila.db.poursuite.ETAPE_INFO");
COEtapeInfo info = null;

// recherche la prochaine instance à traiter automatiquement
while (infoIter.hasNext() && info == null) {
	info = (COEtapeInfo) infoIter.next();
	
	if (!info.getAutomatique().booleanValue()) {
		info = null;
	}
}

if (info != null) {
	if (info.getRemplaceDateExecution().booleanValue()) {
		request.setAttribute("globaz.aquila.db.poursuite.DATE_EXEC_REMPLACEE", Boolean.TRUE);
	}
%>
<TD class="label"><%=info.getLibelle()%></TD>
<TD class="control" colspan="3">
<% if (COEtapeInfoConfig.CS_BOOLEAN.equals(info.getCsType())) {%>
<INPUT type="checkbox" value="true" name="<%=info.createNomChamp()%>"<%="true".equals(info.getValeur())?" checked":""%> disabled>
<%} else if (COEtapeInfoConfig.CS_CODES.equals(info.getCsType())) {%>
<INPUT type="text" value="<%=info.getSession().getCodeLibelle(info.getValeur())%>" class="disabled" style="width: 100%" readonly>
<%} else  {%>
<INPUT type="text" value="<%=info.getValeur()%>" class="disabled" style="width: 100%" readonly>
<%}}%>
</TD>