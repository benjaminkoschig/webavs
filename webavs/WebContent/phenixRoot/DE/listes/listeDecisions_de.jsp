 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
	idEcran="CCP2003";
	//Récupération des beans
	globaz.phenix.db.listes.CPListeDecisionsViewBean viewBean = (globaz.phenix.db.listes.CPListeDecisionsViewBean) session.getAttribute ("viewBean");
	//Définition de l'action pour le bouton valider
	userActionValue = "phenix.listes.listeDecisions.executer";
	selectedIdValue = "";
	subTableWidth = "75%";
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
top.document.title = "Phenix - Ausdruck der Verfügungen"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function toutOrNotTout(source) {
// Si la case "Tout le journal" est cochée les champs "De" et "à" sont désactivés
	if (source.checked) {
		document.forms[0].fromAvs.value = "";
		document.forms[0].fromAvs.disabled = true;
		document.forms[0].toAvs.value = "";
		document.forms[0].toAvs.disabled = true;
	} else {
		document.forms[0].fromAvs.disabled = false;
		document.forms[0].toAvs.disabled = false;
	}
}

function init(){
//Initialisation
	document.forms[0].dateImpression.value="<%=globaz.globall.util.JACalendar.today().toString()%>";
	fieldFormat(document.forms[0].debutRelation,"CALENDAR");
}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Liste der Verfügungen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD width="165">&nbsp;</TD>
            <TD width="513"></TD>
          </TR>
          <tr>
            <TD width="165">Job</TD>
            <TD width="513"> 
              <INPUT type="text" name="idPassage" maxlength="15" size="15"  value="<%=viewBean.getIdPassage()%>">
              <%
			Object[] psgMethodsName = new Object[]{
				new String[]{"setIdPassage","getIdPassage"},
				new String[]{"setLibellePassage","getLibelle"}
			};
			Object[] psgParams= new Object[]{};
			String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/listes/listeDecisions_de.jsp";	
			%>
			<!--
			-->
			<ct:ifhasright element="musca.facturation.passage.chercher" crud="r">
            <ct:FWSelectorTag 
			name="passageSelector" 
			
			methods="<%=psgMethodsName%>"
			providerPrefix="FA"			
			providerApplication ="musca"			
			providerAction ="musca.facturation.passage.chercher"			
			providerActionParams ="<%=psgParams%>"
			redirectUrl="<%=redirectUrl%>"			
			/> 
			</ct:ifhasright>
			<input type="hidden" name="selectorName" value="">
			</TD>
		 </tr>
          <tr>
            <TD height="20" width="165"></TD>
            <TD nowrap width="513"> 
              <INPUT type="text" name="libellePassage" class="libelleLongDisabled" value="<%=viewBean.getLibellePassage()%>" readonly>
            </TD>
          </tr>
	    <TR>
            <TD width="165">&nbsp;</TD>
            <TD width="513"></TD>
          </TR>
	    <TR>
            <TD height="2" width="165">Art</TD>
            <TD nowrap width="202"><ct:FWCodeSelectTag name="typeDecision"
				defaut=""
				wantBlank="<%=true%>"
				codeType="CPTYPDECIS"
		       	/>
            </TD>
          </tr>
	    <TR>
            <TD width="165">&nbsp;</TD>
            <TD width="513"></TD>
          </TR>
    	    <tr>
            <TD width="150" height="20">Verfügung als</TD>
             <TD width="266">
            <%
				java.util.HashSet except = new java.util.HashSet();
				except.add(globaz.phenix.db.principale.CPDecision.CS_NON_SOUMIS);
				except.add(globaz.phenix.db.principale.CPDecision.CS_FICHIER_CENTRAL);
			%>
            <ct:FWCodeSelectTag name="genreAffilie"
					defaut="<%=((globaz.phenix.application.CPApplication) viewBean.getSession().getApplication()).getGenreDecisionDefaut()%>"
					wantBlank="<%=true%>"
			         codeType="CPGENDECIS"
			         except="<%=except%>"
		/>
            </TD>
            </tr>
	    <TR>
            <TD width="165">&nbsp;</TD>
            <TD width="513"></TD>
          </TR>
   	   <TR>
            <TD height="2" width="165">Jahr</TD>
            <TD height="2" width="513"><INPUT type="text" name="anneeDecision" maxlength="4" size="4"></TD>
          </tr>
	    <TR>
            <TD width="165">&nbsp;</TD>
            <TD width="513"></TD>
          </TR>
	   <TR>
            <TD height="2" width="165">E-Mail Adresse</TD>
            <TD height="2" width="513"> 
              <input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" data-g-string="mandatory:true" value="<%=viewBean.getEMailAddress()%>">
              *</TD>
          </tr>
	    <TR>
            <TD width="165">&nbsp;</TD>
            <TD width="513"></TD>
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