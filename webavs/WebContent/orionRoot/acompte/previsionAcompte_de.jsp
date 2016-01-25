<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.orion.vb.acompte.EBPrevisionAcompteViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "GEB3002";
EBPrevisionAcompteViewBean viewBean = (EBPrevisionAcompteViewBean) session.getAttribute("viewBean");
userActionValue = "orion.acompte.previsionAcompte.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_PREVISION_ACOMPTE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<TD>&nbsp;</TD>
                    	</TR>
			         	<TR> 
  				          	<TD><ct:FWLabel key="EMAIL"/></TD>
 				          	<TD> 
								<INPUT name="email" size="40" type="text" value="<%=viewBean.getEmail()%>">                        
						    </TD>
          				</TR>
          				<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%>
	<ct:menuChange displayId="menu" menuId="EBMenuPrincipal"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>