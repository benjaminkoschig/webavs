<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CFA0009";%>
<%@ page import="globaz.musca.db.facturation.*" %>
<%
	//Récupération du bean
	FAPassageGenererViewBean viewBean = (FAPassageGenererViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "musca.facturation.passageFacturationGenerer.executer";

%>



<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
<!--
function init() {}

function onOk() {
	document.forms[0].submit();
}

function onCancel() {
	document.forms[0].elements('userAction').value="back";
	document.forms[0].submit();
}

// Si la checkbox "Automatique" est cochée, le menu déroulant est bloqué et inversément
function autoVSmodule() {
	if(document.forms[0].elements('auto').checked==true){
		document.forms[0].elements('module').disabled = true;
	}else{
		document.forms[0].elements('module').disabled = false;
	}
}
-->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Générer un journal de facturation <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<TR>
			<TD width="250">Passage n°</td>
			<TD> <INPUT name="idPassage" class="numeroCourtDisabled" value="<%=viewBean.getIdPassage()%>" readonly="readonly">
		</TR>
		
		<TR>
			<TD width="250">Adresse E-Mail</TD>
            <TD><input name='eMailAddress' class='libelleLong' value='<%=viewBean.getEMailAddress()%>'></TD>
		</TR>
		<!-- Ligne vide -->
		<TR><TD width="250"> &nbsp; </TD></TR>
		<TR>
			<TD width="250">Génération automatique*</TD>
			<TD> <INPUT name="auto" type="checkBox" onclick="autoVSmodule();" checked="checked">
		
				<%
				// Recherche de tous les modules de facturation liés au passage				
				FAModulePassageManager modulePassageManager = new FAModulePassageManager();
				modulePassageManager.setSession(viewBean.getSession());
				modulePassageManager.setForIdPassage(viewBean.getIdPassage());
				modulePassageManager.orderByNiveauAppel();
				modulePassageManager.find();

				FAModulePassage	module=null;
				%>
				Génère uniquement les modules qui ne sont pas à l'état "Généré"
			</TD>
		</TR>
		<!-- Ligne vide -->
		<TR><TD width="250"> &nbsp; </TD></TR>
		<TR>
			<TD valign="bottom"> ou à partir du module suivant** </TD>
			<TD valign="bottom"><SELECT name="module">
					<%
						/* On parcours le manager et on affiche tous les modules dont l'action
						 * est différente de VIDE, SUPPRIMER ou IMPRIMER (ces derniers
						 * ne sont plus utilisés mais subsistent dans la base de données
						 */
						for(int i=0;i < modulePassageManager.size();i++) {
							module = (FAModulePassage) modulePassageManager.getEntity(i);
							
							if( (!module.getIdAction().equals(FAModulePassage.CS_ACTION_VIDE) &&
								!module.getIdAction().equals(FAModulePassage.CS_ACTION_IMPRIMER) &&
								!module.getIdAction().equals(FAModulePassage.CS_ACTION_SUPPRIMER)) || i==0 ) {
							%>
							<OPTION value="<%= module.getIdModuleFacturation() %>"> <%= module.getLibelle() %> - <%= module.getLibelleAction() %> </OPTION>
							<%
							}
						}
					%>
				</SELECT>
			</TD>
		</TR>
		
		<!-- Lignes vide -->
		<TR><TD width="250"> &nbsp; </TD></TR>
		<TR><TD width="250"> &nbsp; </TD></TR>
		<TR><TD width="250"> &nbsp; </TD></TR>
		
		<TR>
			<TD>* à partir du premier module non généré</TD>
			<TD>** la génération est forcée sur ce module et les suivants</TD>
		</TR>
		
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
	<SCRIPT>
	
		//Conditions initiales
	
		/* Regarde si le premier module est sur l'état "GENERE", si c'est le cas,
		 * alors il est possible de désactiver le mode Automatique, sinon
		 * la case à cocher reste bloquée. Ceci implique que la toute première
		 * génération d'un passage DOIT ETRE faite de manière automatique.
		 * Par la suite, c'est au choix de l'utilisateur
		 */
		<%
			module = (FAModulePassage) modulePassageManager.getEntity(0);
			if(!module.getIdAction().equals(FAModulePassage.CS_ACTION_GENERE) ){ %>
				document.forms[0].elements('auto').disabled = true;
		<%	} %>
		
		//Bloque le menu déroulant puisque la cas "Automatique" est cochée
		autoVSmodule();
			
	</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>