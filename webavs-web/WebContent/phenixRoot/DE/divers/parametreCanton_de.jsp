<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.phenix.db.principale.CPDecision"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.phenix.db.divers.CPParametreCanton"%>
<%@page import="globaz.phenix.db.divers.CPParametreCanton"%>
<%@page import="globaz.pyxis.constantes.IConstantes"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="CCP1039";
CPParametreCanton viewBean = (CPParametreCanton)session.getAttribute ("viewBean");
String input1="";
%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/CodeSystemPopup.js"></SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Beiträge - Parametrierung Kanton/Parameter - Detail"
function initFamille() {
	var varTypeParam = document.getElementById("typeParametre").value;
	if (varTypeParam ==<%=CPParametreCanton.CS_MODE_CALCUL_AF%>) {
			document.getElementById("codeParametre").value="";
			document.getElementById("codeParametre2").value="";
			document.getElementById("ligneModeEnvoiSedex").style.display = "none";
			document.getElementById("ligneModeReceptionSedex").style.display = "none";
			document.getElementById("ligneCalculAF").style.display = "block";	 
	 } else if (varTypeParam ==<%=CPParametreCanton.CS_MODE_ENVOI_SEDEX%>) {
			document.getElementById("codeParametre1").value="";
			document.getElementById("codeParametre2").value="";
			document.getElementById("ligneModeEnvoiSedex").style.display = "block";
			document.getElementById("ligneModeReceptionSedex").style.display = "none";
			document.getElementById("ligneCalculAF").style.display = "none";	   
	 } else {
			document.getElementById("codeParametre").value="";
			document.getElementById("codeParametre1").value="";
			document.getElementById("ligneModeEnvoiSedex").style.display = "none";
			document.getElementById("ligneModeReceptionSedex").style.display = "block";
			document.getElementById("ligneCalculAF").style.display = "none";	   
	}
}

function add() {
	document.forms[0].elements('userAction').value="phenix.divers.parametreCanton.ajouter"
}
function upd() {
}
function validate() {


	state = validateFields(); 

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="phenix.divers.parametreCanton.ajouter";
	else
		document.forms[0].elements('userAction').value="phenix.divers.parametreCanton.modifier";
	return (state);
}
function cancel() {
	document.forms[0].elements('userAction').value="phenix.divers.parametreCanton.chercher";
}
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="phenix.divers.parametreCanton.supprimer";
		document.forms[0].submit();
	}
}
function init(){}


/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Parametrierung Kanton/Parameter<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
	      <TR>
            <TD nowrap width="160">Kanton</TD>
           <TD nowrap>
			<%
				java.util.HashSet except = new java.util.HashSet();
				except.add(IConstantes.CS_LOCALITE_ETRANGER);
			%>
			<ct:FWCodeSelectTag name="canton"
		        defaut="<%=viewBean.getCanton()%>"
				wantBlank="<%=false%>"
		        codeType="PYCANTON"
				libelle="codeLibelle"
				except="<%=except%>"
			/>
			</TD>
	      <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>
          <TR> 
            <TD nowrap width="100">&nbsp;</TD>
            <TD width="100" nowrap></TD>
	      <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>
           <TR>
            <TD nowrap width="160">Mitgliedsart</TD>
           <TD nowrap>
			<%
			java.util.HashSet exceptGenre = new java.util.HashSet();
			exceptGenre.add(CPDecision.CS_NON_SOUMIS);
			exceptGenre.add(CPDecision.CS_AGRICULTEUR);
			exceptGenre.add(CPDecision.CS_RENTIER);
			exceptGenre.add(CPDecision.CS_TSE);
			exceptGenre.add(CPDecision.CS_ETUDIANT);
			exceptGenre.add(CPDecision.CS_FICHIER_CENTRAL);
			%>
			<ct:FWCodeSelectTag name="genreAffilie"
		        defaut="<%=viewBean.getGenreAffilie()%>"
				wantBlank="<%=true%>"
		        codeType="CPGENDECIS"
				libelle="codeLibelle"
				except="<%=exceptGenre%>"
			/>
			</TD>
	      <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>
          <TR> 
            <TD nowrap width="100">&nbsp;</TD>
            <TD width="100" nowrap></TD>
	      <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>
          <TR>
            <TD nowrap width="160">Funktionalität</TD>
           <TD nowrap>
			<ct:FWCodeSelectTag 
				name="typeParametre" 
		        defaut="<%=viewBean.getTypeParametre()%>"
				wantBlank="<%=false%>"
		        codeType="CPPARMCANT"
				libelle="libelle"
			/>
				<script>
					document.getElementById("typeParametre").onchange = new Function("","return initFamille();");
					
				</script>
			</TD>
	      <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>
  	 	<TR> 
            <TD nowrap width="100">&nbsp;</TD>
            <TD width="100" nowrap></TD>
	      <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>
         <TR id="ligneModeEnvoiSedex">
            <TD nowrap width="160" >Modus</TD>
           <TD nowrap>
              <ct:FWCodeSelectTag name="codeParametre"
		        defaut="<%=viewBean.getCodeParametre()%>"
				wantBlank="<%=false%>"
		        codeType="CPMESEDEX"
		        libelle="libelle"		/>
			</TD>
	      <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>
          <TR id="ligneCalculAF">
            <TD nowrap width="160" >Modus</TD>
           <TD nowrap>
              <ct:FWCodeSelectTag name="codeParametre1"
		        defaut="<%=viewBean.getCodeParametre()%>"
				wantBlank="<%=false%>"
		        codeType="CPCALCULAF"
		        libelle="libelle"/>
			</TD>
	      <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>
           <TR id="ligneModeReceptionSedex">
            <TD nowrap width="160" >Modus</TD>
           <TD nowrap>
              <ct:FWCodeSelectTag name="codeParametre2"
		        defaut="<%=viewBean.getCodeParametre()%>"
				wantBlank="<%=false%>"
		        codeType="CPMRSEDEX"
		        libelle="libelle"/>
			</TD>
	      <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>
        
          <TR> 
            <TD nowrap width="100">&nbsp;</TD>
            <TD nowrap></TD>
             <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125"></TD>
          </TR>
         <TD nowrap width="110">Von</TD>
			            <TD nowrap width="171">
			            	<ct:FWCalendarTag name="dateDebut"
							value="<%=viewBean.getDateDebut()%>"
							errorMessage="das Datum ist ungültig."
							doClientValidation="CALENDAR"/>
		</TD>
	    <TR> 
            <TD nowrap width="100">&nbsp;</TD>
            <TD nowrap></TD>
             <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <td nowrap width="125">
		<INPUT type="hidden" name="_pos" value="">
		<INPUT type="hidden" name="_meth" value="">
		<INPUT type="hidden" name="_act" value="">
		<INPUT type="hidden" name="colonneSelection" value="<%=request.getParameter("colonneSelection")%>">
		</td>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} 
%>
<script>
// menu 

//top.fr_menu.location.replace('appMenu.jsp?_optionMenu=tiers-banque&id=<%=request.getParameter("selectedId")%>&changeTab=Options');
<% if (viewBean.getTypeParametre().equalsIgnoreCase(CPParametreCanton.CS_MODE_ENVOI_SEDEX)
|| JadeStringUtil.isEmpty(viewBean.getTypeParametre())) { %>
	document.getElementById("ligneModeEnvoiSedex").style.display = "block";
	document.getElementById("ligneModeReceptionSedex").style.display = "none";
	document.getElementById("ligneCalculAF").style.display = "none";
	document.getElementById("codeParametre1").value="";
	document.getElementById("codeParametre2").value="";
<%} else if (viewBean.getTypeParametre().equalsIgnoreCase(CPParametreCanton.CS_MODE_CALCUL_AF)) {%>
	document.getElementById("ligneModeEnvoiSedex").style.display = "none";
	document.getElementById("ligneModeReceptionSedex").style.display = "none";
	document.getElementById("ligneCalculAF").style.display = "block";
	document.getElementById("codeParametre").value="";
	document.getElementById("codeParametre2").value="";
<%} else {%>
	document.getElementById("ligneModeEnvoiSedex").style.display = "none";
	document.getElementById("ligneModeReceptionSedex").style.display = "block";
	document.getElementById("ligneCalculAF").style.display = "none";
	document.getElementById("codeParametre").value="";
	document.getElementById("codeParametre1").value="";
<%}%>
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>