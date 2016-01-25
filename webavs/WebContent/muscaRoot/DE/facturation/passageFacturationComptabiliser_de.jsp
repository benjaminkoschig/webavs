<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CFA0011";%>
<%@ page import="globaz.musca.db.facturation.*" %>
<%
	//R�cup�ration du bean
	FAPassageGenererViewBean viewBean = (FAPassageGenererViewBean) session.getAttribute ("viewBean");

	//D�finition de l'action pour le bouton valider
	userActionValue = "musca.facturation.passageFacturationComptabiliser.executer";

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

// Si la checkbox "Automatique" est coch�e, le menu d�roulant est bloqu� et invers�ment
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
			<%-- tpl:put name="zoneTitle" --%>Journal verbuchen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<TR>
			<TD width="250">Job-Nr.</td>
			<TD> <INPUT name="idPassage" class="numeroCourtDisabled" value="<%=viewBean.getIdPassage()%>" type="text" readonly="readonly">
		</TR>
		
		<TR>
			<TD width="250">E-Mail Adresse</TD>
            <TD><input name='eMailAddress' class='libelleLong' value='<%=viewBean.getEMailAddress()%>'></TD>
		</TR>
		
		<!-- Ligne vide -->
		<TR><TD width="250"> &nbsp; </TD></TR>
		
		<TR>
			<TD width="250">Automatische Verbuchung* </TD>
			<TD><INPUT name="auto" type="checkBox" onclick="autoVSmodule();" checked="checked">
		
				<%
				// Recherche de tous les modules de facturation li�s au passage				
				FAModulePassageManager modulePassageManager = new FAModulePassageManager();
				modulePassageManager.setSession(viewBean.getSession());
				modulePassageManager.setForIdPassage(viewBean.getIdPassage());
				modulePassageManager.orderByNiveauAppel();
				modulePassageManager.find();

				FAModulePassage	module=null;
				%>
				Verbucht nur die Module, die noch nicht den Status "Verbucht" haben	
			</TD>
		</TR>
		<!-- Ligne vide -->
		<TR><TD width="250"> &nbsp; </TD></TR>
		<TR>
			<TD valign="bottom"> oder ab folgendem Modul** </TD>
			<TD valign="bottom"> 
				<SELECT name="module">
					<%
						/* On parcours le manager et on affiche tous les modules dont l'action
						 * est diff�rente de VIDE, SUPPRIMER ou IMPRIMER (ces derniers
						 * ne sont plus utilis�s mais subsistent dans la base de donn�es
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
			<TD>* Ab dem ersten nicht gebuchtes Modul</TD>
			<TD>** Die Verbuchung ist forciert auf diesem Modul und auf den weiteren</TD>
		</TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<SCRIPT>
	
		//Conditions initiales
	
		/* Regarde si le premier module est sur l'�tat "COMPTABILISE", si c'est le cas,
		 * alors il est possible de d�sactiver le mode Automatique, sinon
		 * la case � cocher reste bloqu�e. Ceci implique que la toute premi�re
		 * comptabilisation d'un passage DOIT ETRE faite de mani�re automatique.
		 * Par la suite, c'est au choix de l'utilisateur
		 */
		<%
			module = (FAModulePassage) modulePassageManager.getEntity(0);
			if(!module.getIdAction().equals(FAModulePassage.CS_ACTION_COMPTABILISE) ){ %>
				document.forms[0].elements('auto').disabled = true;
		<%	} %>
		
		//Bloque le menu d�roulant puisque la cas "Automatique" est coch�e
		autoVSmodule();
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>