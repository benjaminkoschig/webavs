 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.phenix.db.principale.CPDecision"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
	idEcran="CCP2014";
	//Récupération des beans
	globaz.phenix.db.listes.CPListeDecisionsPeriodeViewBean viewBean = (globaz.phenix.db.listes.CPListeDecisionsPeriodeViewBean) session.getAttribute ("viewBean");
	//Définition de l'action pour le bouton valider
	userActionValue = "phenix.listes.listeDecisionsPeriode.executer";
	selectedIdValue = "";
	subTableWidth = "75%";
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
%>
<%
String MSG_PROCESS_OK = "The process successfully started.";
if ("FR".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "La tâche a démarré avec succès.";
} else if ("DE".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "Prozess erfolgreich gestartet.";
}
%>

<SCRIPT language="JavaScript">
top.document.title = "Liste der verbuchten Verfügungen während einer bestimmten Periode."
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
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
			<%-- tpl:put name="zoneTitle" --%>Liste der verbuchten Verfügungen während einer bestimmten Periode.<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD width="165">&nbsp;</TD>
            <TD width=""></TD>
          </TR>
          <tr>
            <TD width="180">verbuchten Verfüngungen</TD>
            <TD width="400"> 
              <ct:FWCalendarTag name='fromDebutPeriode' value="" errorMessage="la date de début est incorrecte" doClientValidation="CALENDAR"/>
			 bis  
              <ct:FWCalendarTag name='toFinPeriode' value="" errorMessage="la date de fin est incorrecte" doClientValidation="CALENDAR"/>
			</TD>
			</tr>
			<TR>
            <TD width="165">&nbsp;</TD>
            <TD width=""></TD>
          </TR>
	    <TR>
	    
	    
	    <tr> 
            <TD width="300" height="20">Ab Abr-Nr. </TD>
             <td>
	           	<ct:FWPopupList 
	           		name="fromAffilieDebut" 
	           		value="<%=viewBean.getFromAffilieDebut()%>" 
	           		className="libelle" 
	           		jspName="<%=jspLocation%>" 
	           		autoNbrDigit="<%=autoDigiAff%>" 
	           		size="20"
	           		minNbrDigit="3"
	       		/>
	           	<SCRIPT>
	           		document.getElementById("fromAffilieDebut").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
	           	</SCRIPT>
	           	&nbsp; &nbsp;bis&nbsp; &nbsp; 
	           	<ct:FWPopupList 
	           		name="fromAffilieFin" 
	           		value="<%=viewBean.getFromAffilieFin()%>" 
	           		className="libelle" 
	           		jspName="<%=jspLocation%>" 
	           		autoNbrDigit="<%=autoDigiAff%>" 
	           		size="20"
	           		minNbrDigit="3"
	       		/>
	       			<SCRIPT>
	           		document.getElementById("fromAffiliefin").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
	           	</SCRIPT>
	          </td>
	        <TD></TD>
            <TD width="300"></TD>
            <td></td>
          </tr>
	   <TR>
            <TD width="165">&nbsp;</TD>
            <TD width=""></TD>
          </TR>
	    <TR>
            <TD height="2" width="165">Verfügungstyp</TD>
            <TD nowrap width="202">
            <%
				java.util.HashSet except1 = new java.util.HashSet();
				except1.add(globaz.phenix.db.principale.CPDecision.CS_RECTIFICATION);
				except1.add(globaz.phenix.db.principale.CPDecision.CS_CORRECTION);
			%>
            <ct:FWCodeSelectTag name="typeDecision"
				defaut="<%=globaz.phenix.db.principale.CPDecision.CS_DEFINITIVE %>"
				wantBlank="<%=true%>"
				codeType="CPTYPDECIS"
				except="<%=except1%>"
		       	/>
            </TD>
          </tr>
	    <TR>
            <TD width="165">&nbsp;</TD>
            <TD width=""></TD>
          </TR>
    	    <tr>
            <TD width="150" height="20">Mitgliedart</TD>
            <TD width="266">
            <%
				java.util.HashSet except = new java.util.HashSet();
				except.add(globaz.phenix.db.principale.CPDecision.CS_NON_SOUMIS);
				except.add(globaz.phenix.db.principale.CPDecision.CS_FICHIER_CENTRAL);
				except.add(globaz.phenix.db.principale.CPDecision.CS_RENTIER);
				except.add(globaz.phenix.db.principale.CPDecision.CS_AGRICULTEUR);
				except.add(globaz.phenix.db.principale.CPDecision.CS_TSE);
				except.add(globaz.phenix.db.principale.CPDecision.CS_ETUDIANT);
			%>
            <ct:FWCodeSelectTag name="genreAffilie"
					defaut=""
					wantBlank="<%=true%>"
			         codeType="CPGENDECIS"
			         except="<%=except%>"
			/>
            </TD>
            </tr>
	    <TR>
            <TD width="165">&nbsp;</TD>
            <TD width=""></TD>
          </TR>
   	   <TR>
            <TD height="2" width="165">Jahr</TD>
            <TD height="2" width="513"><INPUT type="text" name="anneeDecision" maxlength="4" size="4"></TD>
          </tr>
	    <TR>
            <TD width="165">&nbsp;</TD>
            <TD width=""></TD>
          </TR>
           <TR> 
            <TD height="20" width="150">Nur aktiv Verfügungen</TD>
            <TD nowrap height="31" width="259"><input type="checkbox" name="isActive" "unchecked"></TD>
          </TR>
	   <TR>
            <TD height="2" width="165">E-Mail Adresse</TD>
            <TD height="2" width="513"> 
              <input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" data-g-string="mandatory:true" value="<%=viewBean.getEMailAddress()%>">
              *</TD>
          </tr>
	    <TR>
            <TD width="165">&nbsp;</TD>
            <TD width=""></TD>
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