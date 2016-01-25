<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
		idEcran="CCP1020";
    	globaz.phenix.db.communications.CPMiseAjourComFiscViewBean viewBean = (globaz.phenix.db.communications.CPMiseAjourComFiscViewBean)session.getAttribute("viewBean");
		userActionValue = "phenix.communications.miseAjourComFisc.executer";
		String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
%>
<SCRIPT language="JavaScript">
top.document.title = "Aktualisierung der Steuermeldungen"
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
<%
	int autoDigiAff = globaz.phenix.util.CPUtil.getAutoDigitAff(session);
%>
function init(){
}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Generierung der Anfragen von Steuermeldungen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
         <TR>
            <TD nowrap colspan="2" style="font-weight : bolder;">Dieses Programm aktualisiert die Steuermeldungen auf Grund der aktiven Verfügungen (die nicht grau sind).</TD>
         </TR>
         <tr>
            <TD>Jahr</TD>
            <td><input name="anneeDecision"  class="libelleShort" size="4" maxlength="4" value='<%=(globaz.globall.util.JACalendar.today().getYear())-1%>'></td>
         </tr>
         <tr>
            <TD>Simulation</TD>
            <TD> 
              <INPUT type="checkbox" checked="checked" name="simulation">
            </TD>
          </tr>
          <tr> 
            <TD width="150" height="20">Ab Mitglied-Nr.</TD>
             <td>
	           	<ct:FWPopupList 
	           		name="fromAffilieDebut" 
	           		value="" 
	           		className="libelle" 
	           		jspName="<%=jspLocation%>" 
	           		autoNbrDigit="<%=autoDigiAff%>" 
	           		size="20"
	           		minNbrDigit="3"
	       		/>
	           	<SCRIPT>
	           		document.getElementById("fromAffilieDebut").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
	           	</SCRIPT>
	          </td>
          	</tr>
          	<tr> 
            	<TD width="150" height="20">Bis Mitglied-Nr.</TD>
	            <td>
	            <ct:FWPopupList 
		           		name="fromAffilieFin" 
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
          <TD width="150" height="20">Mitgliedsart</TD>
            <TD width="266">
              <%
				java.util.HashSet except = new java.util.HashSet();
				except.add(globaz.phenix.db.principale.CPDecision.CS_TSE);
				except.add(globaz.phenix.db.principale.CPDecision.CS_NON_SOUMIS);
				except.add(globaz.phenix.db.principale.CPDecision.CS_ETUDIANT);
				except.add(globaz.phenix.db.principale.CPDecision.CS_FICHIER_CENTRAL);
			%>
		    <ct:FWCodeSelectTag name="forGenreAffilie"
					defaut=""
					wantBlank="<%=true%>"
			        codeType="CPGENDECIS"
		            except="<%=except%>"
			/>
            </TD>
          </tr>
	  	 <tr>
            <TD height="2" width="165">E-Mail Adresse</TD>
            <TD height="2" width="513"> 
              <input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" data-g-string="mandatory:true" value="<%=viewBean.getEMailAddress()%>">
              </TD>
          </tr>
          <TR>
            <TD width="165">&nbsp;</TD>
            <TD width="513"></TD>
          </TR>
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