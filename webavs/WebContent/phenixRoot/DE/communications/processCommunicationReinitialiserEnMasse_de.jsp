 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 

<%@page import="globaz.phenix.db.communications.CPJournalRetourViewBean"%>
<%@page import="globaz.phenix.util.CPUtil"%>

<%
	idEcran="CCP1035";
	CPJournalRetourViewBean viewBean = (CPJournalRetourViewBean) session.getAttribute ("viewBean");
	selectedIdValue = "";
	userActionValue = "phenix.communications.journalRetour.executerReinitialiserEnMasse";
	subTableWidth = "75%";
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	int autoDigiAff = CPUtil.getAutoDigitAff(session);
%>

<SCRIPT language="JavaScript">
top.document.title = "Steuermeldungen - Massenzurücksetzung"
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
<%-- tpl:put name="zoneTitle" --%>Réinitialisation en masse<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%> 

		  <TR>
            <TD nowrap colspan="2" style="font-weight : bolder;">Ce programme permet de réinitialiser comme lors du chargement une communication fiscales en retour.</TD>
          </TR>
          <TR>
            <TD nowrap colspan="2" style="font-weight : bolder;">Il supprime aussi la décision qui aurait été générée selon la communication.</TD>
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
				java.util.HashSet except = new java.util.HashSet();
             	except.add(globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE);
			%>
            <ct:FWCodeSelectTag name="forStatus"
					defaut="<%=globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean.CS_RECEPTIONNE%>"
					wantBlank="<%=true%>"
			        codeType="CPETCOMRET"
		            except="<%=except%>"
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
		    <TD><input name='eMailAddress' id='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value='<%=viewBean.getSession().getUserEMail()%>' tabindex="1"></TD>
		</TR>
	
<!------------------------------------------------------------------------------
	Permet de stocker la liste des id des journaux a réinitialiser
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