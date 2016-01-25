<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.aquila.db.access.batch.COEtapeInfoConfig"%>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.globall.util.JACalendar" %>
<%
// renseigné dans la jsp transition_de.jsp
Iterator infoIter = (Iterator) request.getAttribute("globaz.aquila.db.access.batch.ETAPE_INFO");
COEtapeInfoConfig infoConfig = null;

// recherche la prochaine instance à traiter automatiquement
while (infoIter.hasNext() && infoConfig == null) {
	infoConfig = (COEtapeInfoConfig) infoIter.next();
	
	if (!infoConfig.getAutomatique().booleanValue()) {
		infoConfig = null;
	}
}

if (infoConfig != null) {
	if (infoConfig.getRemplaceDateExecution().booleanValue()) {
		request.setAttribute("globaz.aquila.db.access.batch.DATE_EXEC_REMPLACEE", Boolean.TRUE);
	}
%>
<TD class="label"><%=infoConfig.getLibelle()%></TD>
<TD class="control" colspan="3">
<%
if (COEtapeInfoConfig.CS_BOOLEAN.equals(infoConfig.getCsType())) {
%><INPUT type="checkbox" value="true" name="<%=infoConfig.createNomChamp()%>"><%
} else if (COEtapeInfoConfig.CS_CHAINE.equals(infoConfig.getCsType())) {
%><INPUT type="text" name="<%=infoConfig.createNomChamp()%>"><%
} else if (COEtapeInfoConfig.CS_DATE.equals(infoConfig.getCsType())) {
%><ct:FWCalendarTag name="<%=infoConfig.createNomChamp()%>" value="<%=JACalendar.todayJJsMMsAAAA()%>"/><%	
} else if (COEtapeInfoConfig.CS_NOMBRE.equals(infoConfig.getCsType())) {
%><INPUT type="text" name="<%=infoConfig.createNomChamp()%>" style="text-align: right" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"><%	
} else if (COEtapeInfoConfig.CS_CODES.equals(infoConfig.getCsType())) {
%>
<ct:select name="<%=infoConfig.createNomChamp()%>">
	<ct:optionsCodesSystems csFamille="<%=infoConfig.getParam()%>"/>
</ct:select>
<%	
}
%>
</TD>
<%}%>