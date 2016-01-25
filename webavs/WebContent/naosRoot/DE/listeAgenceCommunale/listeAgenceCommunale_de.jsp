<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.naos.db.listeAgenceCommunale.AFListeAgenceCommunaleViewBean"%>
<%
idEcran = "CAF2015";
AFListeAgenceCommunaleViewBean viewBean = (AFListeAgenceCommunaleViewBean) session.getAttribute("viewBean");
userActionValue = "naos.listeAgenceCommunale.listeAgenceCommunale.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Liste aller Versicherten der entsprechenden AHV-Zweigstelle ausdruken<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<TR> 
  				          	<TD>E-Mail Adresse</TD>
 				          	<TD> 
								<INPUT name="email" size="40" type="text" value="<%=viewBean.getEmail()%>">                        
						  	<TD>&nbsp;</TD>
          				</TR>
          				<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
						<TR>
							<TD>Datum</TD>
							<TD>
								<ct:FWCalendarTag name="date" value="<%=viewBean.getDate()%>" doClientValidation="' onchange='reloadPage()"/>
							</TD>
						</TR>
          				<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
          				<TR>
            				<TD>Gemeindezweigstelle</TD>
							<td>
								<ct:FWListSelectTag data="<%=viewBean.returnAgenceCommunale()%>" name="idTiersAgence" defaut="<%=viewBean.getIdTiersAgence()%>" />
							</TD>
                        	<TD>&nbsp;</TD>
                    	</TR>  
                    	<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
                    	<TR> 
			            <TD>csv</TD>
			            <TD nowrap height="31" width="259"><input type="checkbox" name="wantCsv" "unchecked"></TD>
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