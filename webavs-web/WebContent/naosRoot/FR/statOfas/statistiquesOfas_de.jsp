<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "CAF3008";
AFStatistiquesOfasViewBean viewBean = (AFStatistiquesOfasViewBean) session.getAttribute("viewBean");
userActionValue = "naos.statOfas.statistiquesOfas.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Statistiques OFAS<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
<%@page import="globaz.naos.db.statOfas.AFStatistiquesOfasViewBean"%><TR>
						<TR> 
  				          	<TD>Adresse E-mail</TD>
 				          	<TD> 
								<INPUT name="email" size="40" type="text" style="text-align : left;" value="<%=viewBean.getEmail()!=null?viewBean.getEmail():""%>">                        
						  	<TD>&nbsp;</TD>
          				</TR>
          				<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
						<TR> 
  				          	<TD>Année</TD>
 				          	<TD> 
								<INPUT name="forAnnee" size="15" type="text" style="text-align : left;" value="<%=viewBean.getForAnnee()!=null?viewBean.getForAnnee():""%>">                        
						  	<TD>&nbsp;</TD>
          				</TR>
          				<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
          				<TR>
            				<TD>Type d'adresse</TD>
							<td>
								<ct:FWListSelectTag data="<%=viewBean.getVectorTypeAdresse()%>" name="idTypeAdresse" defaut=""  />
							</TD>
                        	<TD>&nbsp;</TD>
                    	</TR>   
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>