<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.aquila.db.access.poursuite.COContentieux"%>
<%@ page import="globaz.globall.db.BSession"%>
<%@ page import="globaz.aquila.util.CODateUtils"%>
<%
	COContentieux contentieuxViewBean = (COContentieux) session.getAttribute("contentieuxViewBean");
%>
<TR>
<TD><INPUT type="button" name="" value="Passer en irrécouvrable" onclick="document.location.href='aquila?userAction=aquila.irrecouvrables.sections.passerIrrecouvrable&selectedId=<%=contentieuxViewBean.getIdCompteAnnexe()%>&idExterneRole=<%=contentieuxViewBean.getCompteAnnexe().getIdExterneRole()%>&description=<%=java.net.URLEncoder.encode(contentieuxViewBean.getCompteAnnexe().getDescription())%>';"></TD>
</TR>