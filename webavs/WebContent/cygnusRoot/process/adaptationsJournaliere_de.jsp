<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.cygnus.vb.process.RFAdaptationsJournaliereViewBean"%>
<%@page import="globaz.cygnus.application.RFApplication"%>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_TR_PREPARER_DECISIONS"

	idEcran="ECRAN_TEST";
	
	RFAdaptationsJournaliereViewBean viewBean = (RFAdaptationsJournaliereViewBean) session.getAttribute("viewBean");
	
	userActionValue=IRFActions.ACTION_ADAPTATIONS_JOURNALIERE + ".executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">

</script>
<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>TEST Adaptations journalière TEST <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD width="200px"><ct:FWLabel key="JSP_RF_DEM_S_GESTIONNAIRE"/></TD>
							<TD colspan="5">
								<ct:FWListSelectTag name="idGestionnaire" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%=JadeStringUtil.isBlank(viewBean.getIdGestionnaire())?
			                        																																viewBean.getSession().getUserId():viewBean.getIdGestionnaire()%>"/>
							</TD>
						</TR>
								
						<TR><TD colspan="6">&nbsp;</TD></TR>						
						<TR>
							<TD><LABEL for="eMailAddress"><ct:FWLabel key="JSP_TR_PREPARER_DECISIONS_EMAIL"/></LABEL></TD>
							<TD><INPUT type="text" name="eMailAddress" value="<%=JadeStringUtil.isEmpty(viewBean.getEMailAddress())?
																				 viewBean.getSession().getUserEMail():viewBean.getEMailAddress()%>" class="libelleLong"></TD>
						</TR>						
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) 
{ %> 
<%	}%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>