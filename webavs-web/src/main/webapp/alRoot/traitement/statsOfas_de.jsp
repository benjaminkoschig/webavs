<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.al.vb.traitement.ALStatsOfasViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "AL0061";
ALStatsOfasViewBean viewBean = (ALStatsOfasViewBean) session.getAttribute("viewBean");

//Définition de l'action pour le bouton valider
userActionValue = "al.traitement.statsOfas.executer";

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="AL0061_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
					<TR>
						<TR> 
  				          	<TD><ct:FWLabel key="AL0061_EMAIL"/></TD>
 				          	<TD> 
								<INPUT name="email" size="40" type="text" style="text-align : left;" value="<%=viewBean.getEmail()!=null?viewBean.getEmail():""%>">                        
						  	<TD>&nbsp;</TD>
          				</TR>
          				<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
						<TR> 
  				          	<TD><ct:FWLabel key="AL0061_ANNEE"/></TD>
 				          	<TD> 
								<INPUT name="forAnnee" size="15" type="text" style="text-align : left;" value="<%=viewBean.getForAnnee()!=null?viewBean.getForAnnee():""%>">                        
						  	<TD>&nbsp;</TD>
          				</TR>
          				<TR>
          					<TD>&nbsp;</TD>
                    	</TR>  
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>