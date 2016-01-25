<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran = "CAF3005";
	globaz.naos.db.masse.AFConfirmationSalairesViewBean viewBean = (globaz.naos.db.masse.AFConfirmationSalairesViewBean)session.getAttribute("viewBean");
	userActionValue = "naos.masse.confirmationSalaires.executer";
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Bestätigung der Anpassung des Akontos<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
		 <%-- tpl:put name="zoneMain" --%>
		 <% if (viewBean.getAffiliationId() != null && viewBean.getAffiliationId().length() != 0) { %>
         	<naos:AFInfoAffiliation name="affiliationId" affiliation="<%=viewBean.getAffiliation()%>" titleWidth="100" />
		 <% }else{ %>
         <TR>
            <TD>Mitgliedern</TD>
            <TD>
            <TABLE border="0" cellspacing="0" cellpadding="0">
              <TBODY>
                <TR>
                  <TD nowrap width="547" valign="middle">
                  <TABLE width="400" cellpadding="0" cellspacing="0">
              		<TBODY>
                      <TR align="left">
                        <TD width="113">von:<BR>
                        <ct:FWPopupList name="fromIdExterneRole" 
							value="<%=viewBean.getFromIdExterneRole()%>"
							className="libelle" 
							jspName="<%=jspLocation%>" 
							autoNbrDigit="<%=autoDigiAff%>" 
							size="15"
							minNbrDigit="3"
							
						/></TD>
                        <TD width="186">bis:<BR>
                        <ct:FWPopupList name="tillIdExterneRole" 
							value="<%=viewBean.getTillIdExterneRole()%>"
							className="libelle" 
							jspName="<%=jspLocation%>" 
							autoNbrDigit="<%=autoDigiAff%>" 
							size="15"
							minNbrDigit="3"
							
						/></TD>
                      </TR>
                    </TBODY>
            	   </TABLE>
                  </TD>
                </TR>
              </TBODY>
            </TABLE>
            </TD>
          </TR>
		  <% } %>
			<TR>
				<TD>Senddatum:</TD>
				<TD><ct:FWCalendarTag name="dateEnvoi" value="<%=viewBean.getDateEnvoi()%>"/></TD>
			</TR>
			<TR>
				<TD>E-Mail:</TD>
				<TD><INPUT type="text" name="email" value="<%=viewBean.getEmail()%>"></TD>
			</TR>
			<% if (viewBean.getAffiliationId() != null && viewBean.getAffiliationId().length() != 0) { %>
			<TR>
				<TD>Erfassungsplan:</TD>
				<TD>
					<select name="planAffiliationId">
						<%=globaz.naos.util.AFUtil.getPlanAffiliation(viewBean.getAffiliation().getAffiliationId(), viewBean.getPlanAffiliationId(), session,true)%>
					</select>	
				</TD>
			</TR>
			<% } %>
			<tr ><TD>&nbsp;</TD></tr><tr ><TD>&nbsp;</TD></tr>
			<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%>
<% if (viewBean.getAffiliationId() != null && viewBean.getAffiliationId().length() != 0) { %>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFOptionsAffiliation" showTab="options">
		<ct:menuSetAllParams key="affiliationId" value="<%=viewBean.getAffiliationId()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getAffiliationId()%>"/>
	</ct:menuChange>
<% } else { %>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
<% } %>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>