<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.aquila.db.access.poursuite.COContentieux"%>
<%@ page import="globaz.globall.db.BSession"%>
<%@ page import="globaz.aquila.util.CODateUtils"%>
<%
	COContentieux reception_cdpViewBean = (COContentieux) session.getAttribute("contentieuxViewBean");
%>
<TR>
	<TD class="label">N° poursuite</TD>
	<TD class="control"><INPUT type="text" name="numPoursuite" value="<%=reception_cdpViewBean.getNumPoursuite()%>"></TD>
	<TD class="label">Avec opposition</TD>
	<TD class="control" colspan="3"><input type="checkbox" name="opposition"></TD>
</TR>
