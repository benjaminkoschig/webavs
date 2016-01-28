<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.aquila.util.CODateUtils"%>
<%@ page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@ page import="globaz.globall.db.BSession"%>
<jsp:include flush="true" page="saisie_email.jsp"/>
<%
	FWViewBeanInterface localViewBean = (FWViewBeanInterface) session.getAttribute("viewBean");
%>
<TR>
	<TD class="label">Date de délai de paiement</TD>
	<TD class="control" colspan="3"><ct:FWCalendarTag name="dateDelaiPaiement" value="<%=CODateUtils.getDateDelaiPaiement(((BSession)localViewBean.getISession()))%>"/></TD>
</TR>
