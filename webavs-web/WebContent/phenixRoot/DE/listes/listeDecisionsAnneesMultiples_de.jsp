 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
		idEcran="CCP2010";
    	globaz.phenix.db.listes.CPListeDecisionsAnneesMultiplesViewBean viewBean = (globaz.phenix.db.listes.CPListeDecisionsAnneesMultiplesViewBean) session.getAttribute ("viewBean");
		selectedIdValue = "";
		userActionValue = "phenix.listes.listeDecisionsAnneesMultiples.executer";
		subTableWidth = "75%";
%>
<SCRIPT language="JavaScript">
top.document.title = "Verfügung - Ausdruck der Verfügungen in Doppel"
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
}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ausdruck
      der Liste den mehrfachen Verfügungen für ein Jahr<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <tr> 
            <TD width="50%" height="20">Job</TD>
            <TD width="50%"> 
              <INPUT type="text" name="idPassage" maxlength="15" size="15"  value="<%=viewBean.getIdPassage()%>">
              <%
			Object[] psgMethodsName = new Object[]{
				new String[]{"setIdPassage","getIdPassage"},
				new String[]{"setLibellePassage","getLibelle"}
			};
			Object[] psgParams= new Object[]{};
			String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/listes/listeDecisionsAnneesMultiples_de.jsp";	
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
          <TR> 
            <TD height="50%" width="212">E-Mail Adresse</TD>
            <TD width="50%"> 
              <input name='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value='<%=viewBean.getEMailAddress()%>'>
            </TD>
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