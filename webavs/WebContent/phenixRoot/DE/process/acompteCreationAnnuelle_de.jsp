
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.globall.api.GlobazSystem"%>
<%@page import="globaz.musca.application.FAApplication"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
		idEcran="CCP3001";
    	globaz.phenix.vb.acompte.CPAcompteCreationAnnuelleViewBean viewBean = (globaz.phenix.vb.acompte.CPAcompteCreationAnnuelleViewBean)session.getAttribute ("viewBean");
		selectedIdValue = "";
		userActionValue = "phenix.process.acompte.executerCreation";
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
top.document.title = "Anzahlung - Erstellung"
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
}

function postInit(){
	if (document.forms[0].elements('idPassage').value == "") {
		document.forms[0].elements('idPassage').focus();
	} else if (document.forms[0].elements('forAnneeReprise').value == "") {
		document.forms[0].elements('forAnneeReprise').focus();
	} else if (document.forms[0].elements('eMailAddress').value == "") {
		document.forms[0].elements('eMailAddress').focus();
	}
}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Erstellung der Anzahlungen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <tr>
            <TD width="50%" height="20">Job</TD>
            <TD width="50%">
              <INPUT type="text" name="idPassage" maxlength="15" size="15" data-g-string="mandatory:true" value="<%=viewBean.getIdPassage()%>">
              <%
			Object[] psgMethodsName = new Object[]{
				new String[]{"setIdPassage","getIdPassage"},
				new String[]{"setLibellePassage","getLibelle"}
			};
			Object[] psgParams= new Object[]{};
			String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/process/acompteCreationAnnuelle_de.jsp";
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
            <TD width="50%" height="20"></TD>
            <TD nowrap width="50%">
              <INPUT type="text" name="libellePassage" class="libelleLongDisabled" value="<%=viewBean.getLibellePassage()%>" readonly>
            </TD>
          </tr>
          <tr>
            <TD width="50%" height="20">Ab Mitglied-Nr.</TD>
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
            	<TD width="50%" height="20">Bis Mitglied-Nr.</TD>
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
             <TD width="50%" height="20">Jahr</TD>
            <TD width="50%">
              <INPUT type="text" name="forAnneeReprise" data-g-string="mandatory:true" maxlength="4" size="4">
            </TD>
          </tr>
          <tr>
          <TD width="150" height="20">Mitgliedsart</TD>
            <TD width="266">
             <%
				java.util.HashSet except = new java.util.HashSet();
				except.add(globaz.phenix.db.principale.CPDecision.CS_TSE);
				except.add(globaz.phenix.db.principale.CPDecision.CS_NON_SOUMIS);
				except.add(globaz.phenix.db.principale.CPDecision.CS_RENTIER);
				except.add(globaz.phenix.db.principale.CPDecision.CS_AGRICULTEUR);
				except.add(globaz.phenix.db.principale.CPDecision.CS_ETUDIANT);
				except.add(globaz.phenix.db.principale.CPDecision.CS_FICHIER_CENTRAL);
				Boolean	isSeprationIndNac = Boolean.FALSE;
				try {
					isSeprationIndNac = new Boolean(GlobazSystem
							.getApplication(FAApplication.DEFAULT_APPLICATION_MUSCA).getProperty(
									FAApplication.SEPARATION_IND_NA));
				} catch (Exception e) {
					isSeprationIndNac = Boolean.FALSE;
				}
				boolean wantB = true;
				if (isSeprationIndNac) {
					wantB = false;
				}
			%>
            <ct:FWCodeSelectTag name="forGenreAffilie"
					defaut=""
					wantBlank="<%=wantB%>"
			        codeType="CPGENDECIS"
			        except="<%=except%>"
		/>
            </TD>
          </tr>
          <tr>
          <TD nowrap width="150" height="20">Periodizität</TD>
		   <TD nowrap>
				<ct:FWCodeSelectTag 
		    		name="forPeriodicite" 
					defaut=""
					codeType="VEPERIODIC"
					wantBlank="true"/>
		   </TD>
		   </tr> 
          <TR>
            <TD height="50%" width="212">E-Mail Adresse</TD>
            <TD width="50%">
              <input name='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value='<%=viewBean.getEMailAddress()%>'>
            </TD>
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