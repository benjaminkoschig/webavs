 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%idEcran="CAF2008";%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.naos.db.acompte.*"%>
<%
	//Récupération des beans
	AFPrevisionAcompteImprimerViewBean viewBean = (AFPrevisionAcompteImprimerViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "naos.acompte.previsionAcompteImprimer.executer";

	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);
%>
<SCRIPT language="JavaScript">
top.document.title = "Naos - Impression des prévisions d'acompte"
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
function reloadPage() {
		fieldFormat(document.all('dateEnvoi'),'CALENDAR');
		document.all('dateRetour').value = '';
		document.forms[0].elements('userAction').value = 'naos.acompte.previsionAcompteImprimer.reload';
		document.forms[0].submit();
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Imprimer les prévisions d'acompte<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <% if (viewBean.getAffiliationId() != null && viewBean.getAffiliationId().length() != 0) { %>
			<naos:AFInfoAffiliation name="affiliationId" affiliation="<%=viewBean.getAffiliation()%>" titleWidth="150"/>
		  	<tr ><TD>&nbsp;</TD></tr>
		 <% }else{ %>
		  <%viewBean._setEcranForTraitementEnMasse(true);%>
          <TR>
            <TD>Affiliés</TD>
            <TD>
            <TABLE border="0" cellspacing="0" cellpadding="0">
              <TBODY>
                <TR>
                  <TD nowrap width="547" valign="middle">
                  <TABLE width="400" cellpadding="0" cellspacing="0">
              		<TBODY>
                      <TR align="left">
                        <TD width="113">de:<BR>
                        <ct:FWPopupList name="fromIdExterneRole" 
							value="<%=viewBean.getFromIdExterneRole()%>"
							className="libelle" 
							jspName="<%=jspLocation%>" 
							autoNbrDigit="<%=autoDigiAff%>" 
							size="15"
							minNbrDigit="3"
							
						/></TD>
                        <TD width="186">à:<BR>
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
				<TD>Période:</TD>
                <TD>
					<INPUT type="text" name="year" value="<%=viewBean.giveYear()%>" size="4" maxlength="4">
				</TD>
			</TR>
			<TR>
				<TD>Date d'envoi:</TD>
				<TD><ct:FWCalendarTag name="dateEnvoi" value="<%=viewBean.getDateEnvoi()%>" doClientValidation="' onchange='reloadPage()"/></TD>
			</TR>
			<TR>
				<TD>Date de retour:</TD>
				<TD><ct:FWCalendarTag name="dateRetour" value="<%=viewBean.getDateRetour()%>"/></TD>
			</TR>
			
			<TR>
				<TD>Masse supérieure à:</TD>
				<TD>
					<INPUT type="text" onchange="validateFloatNumber(this)" onkeypress="return filterCharForFloat(window.event)" id="masseSuperieur" name="masseSuperieur" size="30" maxlength="30" value="<%=viewBean.getMasseSuperieur()%>" class="montant">
				</TD>
			</TR>
			
			<TR>
				<TD>Décl. salaires:</TD>
				<TD>
					<ct:FWCodeSelectTag 
		            	name="declarationSalaire" 
						defaut="<%=viewBean.getDeclarationSalaire()%>"
						codeType="VEDECLARAT"
						wantBlank="true"
					/>
				</TD>
			</TR>
			
			<TR>
				<TD>E-mail:</TD>
				<TD><INPUT type="text" name="email" value="<%=viewBean.getEmail()%>"></TD>
			</TR>
			<% if (viewBean.getAffiliationId() != null && viewBean.getAffiliationId().length() != 0) { %>
			<TR>
				<TD>Plan d'affiliation:</TD>
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