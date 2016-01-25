<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran = "CAF3004";
	globaz.naos.db.masse.AFAnnonceSalairesViewBean viewBean = (globaz.naos.db.masse.AFAnnonceSalairesViewBean)session.getAttribute("viewBean");
	userActionValue = "naos.masse.annonceSalaires.executer";
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
function reloadPage() {
		fieldFormat(document.all('dateEnvoi'),'CALENDAR');
		document.all('dateRetour').value = '';
		document.forms[0].elements('userAction').value = 'naos.masse.annonceSalaires.reload';
		document.forms[0].submit();
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ausdruck der leeren Auszüge<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>					
						<% if (viewBean.getAffiliationId() != null && viewBean.getAffiliationId().length() != 0) { %>
			         	<naos:AFInfoAffiliation name="affiliationId" affiliation="<%=viewBean.getAffiliation()%>" titleWidth="100" />
			         	<tr ><TD>&nbsp;</TD></tr>
			         	<TR>
							<TD>Periode:</TD>
							<TD>
				            <TABLE border="0" cellspacing="0" cellpadding="0">
				              <TBODY>
				                <TR>
				                  <TD nowrap width="547" valign="middle">
				                  <TABLE width="305" cellpadding="0" cellspacing="0">
				              		<TBODY>
				                      <TR align="left">
				                        <TD width="140">von:<BR>
				                        <ct:FWCalendarTag name="dateDebut" displayType ="month"  
											value="<%=viewBean.getDateDebut()%>" doClientValidation="CALENDAR"/>
				                        <TD width="186">bis:<BR>
				                        <ct:FWCalendarTag name="dateFin" displayType ="month" 
											value="<%=viewBean.getDateFin()%>" doClientValidation="CALENDAR"/>
				                      </TR>
				                    </TBODY>
				            	   </TABLE>
				                  </TD>
				                </TR>
				              </TBODY>
				            </TABLE>
				            </TD>
						</TR>
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
						<TR>
							<TD>Periode:</TD>
							<TD>
				                 <ct:FWCalendarTag name="dateFin" displayType ="month" 
									value="<%=viewBean.getDateFin()%>" doClientValidation="CALENDAR"/>
				            </TD>
						</TR>
						<% } %>
						<TR>
							<TD>Datum des Versands:</TD>
							<TD><ct:FWCalendarTag name="dateEnvoi" value="<%=viewBean.getDateEnvoi()%>" doClientValidation="' onchange='reloadPage()"/></TD>
						</TR>
						<TR>
							<TD>Datum der Rückkehr:</TD>
							<TD><ct:FWCalendarTag name="dateRetour" value="<%=viewBean.getDateRetour()%>"/></TD>
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
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
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