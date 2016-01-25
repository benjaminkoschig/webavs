<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.aquila.db.access.poursuite.COContentieux"%>
<%@ page import="globaz.globall.db.BSession"%>
<%@ page import="globaz.aquila.util.CODateUtils"%>
<%
	COContentieux reception_cdpViewBean = (COContentieux) session.getAttribute("contentieuxViewBean");
%>
<TR>
	<TD class="label">Betreibung-Nr.</TD>
	<TD class="control"><INPUT type="text" name="numPoursuite" value="<%=reception_cdpViewBean.getNumPoursuite()%>"></TD>
	<TD class="label">Mit Einsprache</TD>
	<TD class="control" colspan="3"><input type="checkbox" name="opposition"></TD>
</TR>
