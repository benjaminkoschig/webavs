 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%idEcran="CAF2007";%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.naos.db.acompte.*"%>
<%
	//Récupération des beans
	AFBouclementAcompteImprimerViewBean viewBean = (AFBouclementAcompteImprimerViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "naos.acompte.bouclementAcompteImprimer.executer";
	
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);

%>
<SCRIPT language="JavaScript">
top.document.title = "Naos - Abschluss der Akonto drucken"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function init()
{}

function reloadPage() {
		fieldFormat(document.all('dateFin'),'CALENDAR');
		fieldFormat(document.all('dateEnvoi'),'CALENDAR');
		document.all('dateRetour').value = '';
		document.forms[0].elements('userAction').value = 'naos.acompte.bouclementAcompteImprimer.reload';
		document.forms[0].submit();
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Abschluss der Akonto drucken<%-- /tpl:put --%>
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
								value="<%=viewBean.getDateFin()%>" doClientValidation="' onchange='reloadPage()"/>
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
				 	<INPUT type="hidden" name="dateFin" value='<%=viewBean.getDateFin()%>'>
				 	<INPUT type="text" name="year" value="<%=viewBean.getYear()%>" size="4" maxlength="4"  onchange="reloadPage()">
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
				<TD>E-Mail Adresse:</TD>
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