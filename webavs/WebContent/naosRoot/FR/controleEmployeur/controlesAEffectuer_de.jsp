<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CAF2005";%>
<%
	//Récupération des beans
	globaz.naos.db.controleEmployeur.AFControlesAEffectuerViewBean viewBean = (globaz.naos.db.controleEmployeur.AFControlesAEffectuerViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "naos.controleEmployeur.controlesAEffectuer.executer";

%>
<SCRIPT language="JavaScript">
top.document.title = "Naos - Impression de la liste des contrôles à effectuer"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

	function init() {
	}

	function postInit() {
		document.getElementById('afficheType').style.display = 'none';
		document.getElementById('afficheType').style.visibility = 'visible';
	}

	function afficheTypeAdresse() {
		if (document.getElementById('listeExcel').checked) {
			document.getElementById('afficheType').style.display = 'inline';
			document.getElementById('afficheType').style.visibility = 'visible';
		} else {
			document.getElementById('afficheType').style.display = 'none';
			document.getElementById('afficheType').style.visibility = "hidden";
		}
	}
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Imprimer la liste des contrôles à effectuer<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD width="26%">Masse salariale</TD>
							<TD>de CHF <INPUT type="text" name="masseSalDe" size="15" onchange="validateFloatNumber(this);" value="<%=viewBean.getMasseSalDe()%>" > à CHF
								   <INPUT type="text" name="masseSalA" size="15" onchange="validateFloatNumber(this);" value="<%=viewBean.getMasseSalA()%>" >
							</TD>
						</TR>
						<TR>
							<TD>
								
							</TD>
						</TR>
						<TR>
							<TD width="23%" height="2">Genre de contrôle</TD>
            				<TD height="2"> 
              					<ct:FWCodeSelectTag name="genreControle" wantBlank="true" defaut="<%=viewBean.getGenreControle()%>" codeType="VEGENRECON"/>
            				</TD>
						</TR>
						<TR><TD></TD><TD></TD></TR>
						<TR>
							<TD>Année de contrôle</TD>
							<TD>
								<INPUT type="text" name="annee" maxlength="4" size="4" value="<%=viewBean.getAnnee()%>">
							</TD>
						</TR>
						<TR>
            				<TD width="23%" height="2">Adresse E-Mail</TD>
            				<TD height="2"> 
              					<INPUT type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getSession().getUserEMail()%>">
            				</TD>
          				</TR>
          				<TR><TD></TD><TD></TD></TR>
          				<TR>
							<TD width="26%">Liste Excel</TD>
							<TD><INPUT type="checkbox" id="listeExcel" name="listeExcel" onclick="afficheTypeAdresse()" value="on"></TD>
						</TR>
						
						<TR id="afficheType">
            				<TD width="23%" height="2">Type Adresse</TD>
            				<TD height="2"> 
	            				<SELECT id="tri" name="typeAdresse" doClientValidation="">
	     								<OPTION value="domicile" <%="domicile".equals(viewBean.getTypeAdresse())?"selected":""%>>Domicile</OPTION>
	     								<OPTION value="courrier" <%="courrier".equals(viewBean.getTypeAdresse())?"selected":""%>>Courrier</OPTION>
	     						</SELECT>
	     					</TD>
          				</TR>
          				<TR>
							<TD width="26%">Liste Pdf</TD>
							<TD><INPUT type="checkbox" name="listePdf" value="on"></TD>
						</TR>					
						<TR>
							<TD width="26%">Avec contrôles attribués ?</TD>
							<TD><INPUT type="checkbox" name="isAvecReviseur" value="on"></TD>
						</TR>							
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>