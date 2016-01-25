 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 

<%@page import="globaz.phenix.process.communications.CPProcessAbandonnerEnMasseViewBean"%>
<%@page import="globaz.phenix.util.CPUtil"%>

<%
	idEcran="CCP1033";
	CPProcessAbandonnerEnMasseViewBean viewBean = (CPProcessAbandonnerEnMasseViewBean) session.getAttribute ("viewBean");
	selectedIdValue = "";
	userActionValue = "phenix.communications.journalRetour.executerAbandonnerEnMasse";
	subTableWidth = "75%";
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	int autoDigiAff = CPUtil.getAutoDigitAff(session);
%>

<SCRIPT language="JavaScript">
top.document.title = "Steuermeldungen - Massenabbruch"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Abbruch der Steuermeldungen zurück<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%> 

		<TR>
            <TD nowrap colspan="2" style="font-weight : bolder;">Dieses Programm ermöglicht, nach der gewählten Auswahl, die Steuermeldungen zurück zu abbrechen.</TD>
          </TR>
          <TR>
            <TD nowrap colspan="2" style="font-weight : bolder;">Er löscht auch die Verfügung, die nach der Steuermeldung generiert gewesen wäre.</TD>
          </TR>
          <TR>
            <TD nowrap colspan="2">&nbsp;</TD>
          </TR>
          <tr> 
            <TD width="50%" height="20">Ab Mitglied-Nr.</TD>
            <td>
            <ct:FWPopupList 
	           		name="fromNumAffilie" 
	           		value="" 
	           		className="libelle" 
	           		jspName="<%=jspLocation%>" 
	           		autoNbrDigit="<%=autoDigiAff%>" 
	           		size="20"
	           		minNbrDigit="3"
	       		/>
	       	</td>
          </tr>
          <tr> 
            <TD width="50%" height="20">Bis Mitglied-Nr.</TD>
            <TD>
            <ct:FWPopupList 
	           		name="tillNumAffilie" 
	           		value="" 
	           		className="libelle" 
	           		jspName="<%=jspLocation%>" 
	           		autoNbrDigit="<%=autoDigiAff%>" 
	           		size="20"
	           		minNbrDigit="3"
	       		/>
	       	</td>
          </tr>
         <tr>
            <TD width="150" height="20">Status</TD>
            <TD width="266">
             <%
				java.util.HashSet except1 = new java.util.HashSet();
				except1.add(globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE);
			%>
            <ct:FWCodeSelectTag name="forStatus"
					defaut=""
					wantBlank="<%=true%>"
			        codeType="CPETCOMRET"
		            except="<%=except1%>"
			/>
            </TD>
          </tr>
       <TR>
			<TD nowrap width="80">Plausibilitäten</TD>
			<TD width=50%>
				<% java.util.Vector tmp = globaz.phenix.db.communications.CPValidationCalculCommunication.getListPlausibilites(session,"",""); %>
				<ct:FWListSelectTag name="forIdPlausibilite" defaut="" data="<%=tmp%>"/>
			</TD>
	   </TR>    
		<TR>
		    <TD width="300px">E-Mail Adresse</TD>
		    <TD><input name='eMailAddress' id='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value='<%=viewBean.getEMailAddress() == null ? "" : viewBean.getEMailAddress()%>' tabindex="1"></TD>
		</TR>
	
<!------------------------------------------------------------------------------
	Permet de stocker la liste des id des journaux a annuler
------------------------------------------------------------------------------->	
<%	if(viewBean.getListIdJournalRetour() != null) { 
		for(int i = 0; i < viewBean.getListIdJournalRetour().length ; i++) {
%>
	<input name="listIdJournalRetour" type="hidden" value="<%=viewBean.getListIdJournalRetour()[i]%>"/><% 		
		}
	} 
%>  	
	
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>