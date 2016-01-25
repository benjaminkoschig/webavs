<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CAF2004";%>
<%
	//Récupération des beans
	globaz.naos.db.controleEmployeur.AFControlesAttribuesViewBean viewBean = (globaz.naos.db.controleEmployeur.AFControlesAttribuesViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "naos.controleEmployeur.controlesAttribues.executer";

	// Récupération des réviseurs à lister
	globaz.naos.db.controleEmployeur.AFReviseurManager reviseurs = viewBean.getReviseursList();	
	
%>
<SCRIPT language="JavaScript">
top.document.title = "Naos - Impression de la liste des contrôles attribués"
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

	function changeType() {
  		document.forms[0].elements("userAction").value = "naos.controleEmployeur.controlesAttribues.afficher";
		document.forms[0].submit();
	}

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Imprimer la liste des contrôles attribués<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
     						<TD>Réviseur</TD>
     						<TD>
  							   	<SELECT name="visaReviseur">
							   		<OPTION value="tous"> </OPTION>
							   		<%for (java.util.Iterator iter = reviseurs.iterator(); iter.hasNext();) {%>
										<%globaz.naos.db.controleEmployeur.AFReviseur reviseur = (globaz.naos.db.controleEmployeur.AFReviseur) iter.next();%>
								
									<OPTION value="<%=reviseur.getVisa()%>"><%=reviseur.getVisa()%></OPTION>
									<%}%>
								</SELECT>   							
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
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>