 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.phenix.db.communications.CPJournalRetour"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
		idEcran="CCP1024";
    	globaz.phenix.db.communications.CPJournalRetourViewBean viewBean = (globaz.phenix.db.communications.CPJournalRetourViewBean)session.getAttribute ("viewBean");
		selectedIdValue = "";
		userActionValue = "phenix.communications.journalRetour.executerImprimer";
		subTableWidth = "75%";
		String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
		int autoDigiAff = globaz.phenix.util.CPUtil.getAutoDigitAff(session);
%>
<%
String MSG_PROCESS_OK = "The process successfully started.";
if ("FR".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "La t�che a d�marr� avec succ�s.";
} else if ("DE".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "Der Prozess wurde erfolgreich gestartet.";
}
%>
<SCRIPT language="JavaScript">
top.document.title = "Steuermeldungen - L�schung"
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
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function init(){
}
function postInit(){
	$("#wantDetail").hide();
	$("#libWantDetail").hide();
	$("#impression").change(function () {
		if($(this).val()=="LISTE_PDF_DETAIL"){
			$("#wantDetail").show();
			$("#libWantDetail").show();
		}else {
			$("#wantDetail").hide();
			$("#libWantDetail").hide();
		}
	});
}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ausdruck der Steuermeldungen zur�ck<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
		<TR>
          </TR>
          <TR>
            <TD nowrap colspan="2">&nbsp;</TD>
          </TR>
           <TR>
          <TD nowrap width="30%" >Liste</TD>
         	<% 
			//Sp�cifique CCVS - DDS : S141124_011
			if (viewBean.isCaisseCCCVS()) { 
			%>
            <TD nowrap width="70%" >
				<SELECT name="impression"  id="impression"  class="libelleLong" >
					<OPTION value='LISTE_EXCEL'>Excel Liste</OPTION>
					<OPTION selected="selected" value='LISTE_PDF' >G�ltigkeitsliste</OPTION>
					<OPTION value='LISTE_PDF_DETAIL'>Detail von Steuerverwaltung</OPTION>
				</SELECT>
			</TD>
			<% } else { %>			 
			<TD nowrap width="70%" >
				<SELECT name="impression"  id="impression"  class="libelleLong" >
					<OPTION selected="selected" value='LISTE_EXCEL'>Excel Liste</OPTION>
					<OPTION value='LISTE_PDF' >G�ltigkeitsliste</OPTION>
					<OPTION value='LISTE_PDF_DETAIL'>Detail von Steuerverwaltung</OPTION>
				</SELECT>
			</TD>
			<%}%>			
			
          </TR>
           <% if(viewBean.getTypeJournal().equalsIgnoreCase(CPJournalRetour.CS_TYPE_JOURNAL_SEDEX)) {%>
            <TR>
          <TD nowrap width="30%" name="libWantDetail" id="libWantDetail">mit Details</TD>
              <TD nowrap width="30%" ><input type="checkbox" name="wantDetail" id="wantDetail"/></TD>
          </TR>  
         <%} %>
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
			<TD nowrap width="80">Plausiblit�ten</TD>
			<TD width=50%>
			   <%
        		java.util.Vector tmp = globaz.phenix.db.communications.CPValidationCalculCommunication.getListPlausibilites(session,"", viewBean.getCanton());
				%>
				<ct:FWListSelectTag name="forIdPlausibilite"
						defaut=""
	            		data="<%=tmp%>"/>
			</TD>
		  </TR> 
          <TR>
          <TD nowrap width="30%" >Sortiert nach</TD>
            <TD nowrap width="70%" >
				<SELECT name="orderBy"    class="libelleLong" >
					<OPTION selected="selected" value='ORDER_BY_AFFILIE'>Mitglied-Nr.</OPTION>
					<OPTION value='ORDER_BY_CONTRIBUABLE'>Steuer-Nr.</OPTION>
					<OPTION value='ORDER_BY_AVS' >SVN</OPTION>
					<OPTION value='ORDER_BY_IFD'>Jahr</OPTION>
					<OPTION value='ORDER_BY_GTAXATION'>Besteuerungsgruppe</OPTION>
				</SELECT>
				<%--INPUT type="hidden" name="colonneSelection" value="<%=request.getParameter("colonneSelection")%>"--%>
			</TD>
          </TR>
	   <TR> 
            <TD height="20">E-Mail Adresse</TD>
            <TD><input name='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value='<%=viewBean.getSession().getUserEMail()%>'></TD>
          </TR>
		<% 
		if ("yes".equalsIgnoreCase(request.getParameter("processStarted"))) { 
		%>
		<TR class="title">
			<TD colspan="2" style="color:white; text-align:center">
			<SPAN style="color:palegreen">&gt;</SPAN> <%=MSG_PROCESS_OK%> <SPAN style="color:palegreen">&lt;</SPAN>
			</TD>
		</TR>
		<% 
		}
		%>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>
<script>
// menu 

//top.fr_menu.location.replace('appMenu.jsp?_optionMenu=-defaut-&changeTab=Menu');	
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>