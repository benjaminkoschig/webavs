<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="java.util.Iterator"%>
<%@page import="ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat"%>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.home.PCHomeViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%
	globaz.pegasus.vb.home.PCPeriodeAjaxViewBean viewBean=(PCPeriodeAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>

<%@page import="ch.globaz.pegasus.business.models.home.PeriodeServiceEtat"%>

<liste>
	<%
	for(JadeAbstractModel model: viewBean.getSearchModel().getSearchResults()){
		PeriodeServiceEtat periodeServiceEtat = (PeriodeServiceEtat)model;
		SimplePeriodeServiceEtat _periode =periodeServiceEtat.getSimplePeriodeServiceEtat(); 
		%>
		<TR idEntity="<%=_periode.getId()%>">
			<TD align="left"><%=_periode.getDateDebut() + " - "+_periode.getDateFin()%></TD>
			<TD><%=objSession.getCodeLibelle(_periode.getCsServiceEtat())%></TD>
		</TR>
	<%}%>
</liste>
<ct:serializeObject objectName="viewBean.home" destination="xml"/>
