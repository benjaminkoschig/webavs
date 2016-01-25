<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->

<%
	idEcran="CCP3002";
	globaz.phenix.db.listes.CPListeAcompteCotisationAnnuelleDifferenteViewBean viewBean = (globaz.phenix.db.listes.CPListeAcompteCotisationAnnuelleDifferenteViewBean) session.getAttribute ("viewBean");
	userActionValue ="phenix.listes.listeAcompteCotisationAnnuelleDifferente.executer";
	selectedIdValue = "";
	subTableWidth = "75%";
%>

<SCRIPT language="JavaScript">
top.document.title = "Acompte - Liste des diff�rences avec le montant annuel factur�";
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
// Si la case "Tout le journal" est coch�e les champs "De" et "�" sont d�sactiv�s

}

function init(){
//Initialisation

}

function postInit(){
	if (document.forms[0].elements('idPassage').value == "") {
		document.forms[0].elements('idPassage').focus();
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
			<%-- tpl:put name="zoneTitle" --%>Diff�rence entre la cotisation annuelle de l'acompte et celui factur� <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD width="165">&nbsp;</TD>
            <TD width="513"></TD>
          </TR>
            <tr>
            <TD width="50%" height="20">Passage</TD>
            <TD width="50%">
              <INPUT type="text" name="idPassage" maxlength="15" size="15"  value="<%=viewBean.getIdPassage()%>">
              <%
			Object[] psgMethodsName = new Object[]{
				new String[]{"setIdPassage","getIdPassage"}
			};
			Object[] psgParams= new Object[]{};
			String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/listes/listeAcompteCotisationAnnuelleDifferente_de.jsp";
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
            <TD height="2" width="165">Adresse E-Mail</TD>
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