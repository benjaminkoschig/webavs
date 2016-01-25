<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.pcaccordee.PCPlanCalculViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul"%>
<%@page import="ch.globaz.pegasus.businessimpl.utils.plancalcul.PCLignePlanCalculHandler"%>
<%@page import="ch.globaz.pegasus.businessimpl.utils.plancalcul.PCPlanCalculHandler"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%
	//ViewBean
	PCPlanCalculViewBean viewBean = (PCPlanCalculViewBean)request.getAttribute(FWServlet.VIEWBEAN);
	//Pal de calcul
	PCPlanCalculHandler planCalcul = viewBean.getPlanCalcul();
%>
<%@ include file="/theme/detail/header.jspf" %>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
	
	<link rel="stylesheet" type="text/css" media="screen" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/pcaccordees/detailPCAL.css"/>
	<link rel="stylesheet" type="text/css" media="print" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/pcaccordees/detailPCAL.css"/>
	<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/pcaccordees/detailPlanCalcul.js"></script>
</head>
	<!--  inclusion du plan de calcul -->
	<%@ include file="planCalculDetail.jspf" %>

</html>