<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PRE0091";
	
	globaz.corvus.vb.demandes.RERecapitulatifDemandeRenteViewBean viewBean = (globaz.corvus.vb.demandes.RERecapitulatifDemandeRenteViewBean)session.getAttribute("viewBean");

	selectedIdValue = viewBean.getIdDemandeRente();
	
	if (viewBean.getCsEtatDemande().equals(globaz.corvus.api.demandes.IREDemandeRente.CS_ETAT_DEMANDE_RENTE_PAYE)
		||
		viewBean.getCsEtatDemande().equals(globaz.corvus.api.demandes.IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE)
		||
		viewBean.getCsEtatDemande().equals(globaz.corvus.api.demandes.IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE)){
			bButtonUpdate = false;
			bButtonDelete = false;
	} else {
			bButtonDelete = true;
	}
	
	
	bButtonValidate = false;
	bButtonNew = false;

	bButtonCancel = true;

	
	String noDemandeRente = request.getParameter("noDemandeRente");
	String idTierRequerant = request.getParameter("idTierRequerant"); 
	
	// Les labels de cette page commence par la préfix "JSP_REC_D"
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.corvus.api.demandes.IREDemandeRente"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionsdemanderentes" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDemandeRente()%>"/>
	<ct:menuSetAllParams key="noDemandeRente" value="<%=noDemandeRente%>"/>
	<ct:menuSetAllParams key="idTierRequerant" value="<%=idTierRequerant%>"/>
	
	
	<%if (viewBean.getCsEtatDemande().equals(globaz.corvus.api.demandes.IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE)
			|| viewBean.getCsEtatDemande().equals(globaz.corvus.api.demandes.IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE)
			|| viewBean.getCsEtatDemande().equals(globaz.corvus.api.demandes.IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE)
			|| viewBean.getCsEtatDemande().equals(globaz.corvus.api.demandes.IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE)){%>
			<ct:menuActivateNode active="no" nodeId="saisieManuelle"/>
	<%} else {%>
			<ct:menuActivateNode active="yes" nodeId="saisieManuelle"/>
	<%}%>
	
	
	<%if (viewBean.getCsEtatDemande().equals(globaz.corvus.api.demandes.IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE)){%> 		
 		<ct:menuActivateNode active="no" nodeId="preparerDecision"/>
 		<ct:menuActivateNode active="no" nodeId="prepIntMoratoires"/>
	<%} else {%>
		<%if (!viewBean.isPreparationDecisionValide()){%>
			<ct:menuActivateNode active="no" nodeId="preparerDecision"/>
		<%}%>		
	<%}%>
	
</ct:menuChange>

<SCRIPT type="text/javascript"> 
function cancel(){
       document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE%>.chercher";
   	   document.forms[0].submit();
}

function upd(){
       document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".afficher" %>";
   	   document.forms[0].submit();
}

function del(){
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
       document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RECAPITULATIF_DEMANDE_RENTE + ".supprimer" %>";
   	   document.forms[0].submit();
    }   	   
}


function historiqueAValider() {

	if (window.confirm("<ct:FWLabel key='JSP_REC_D_MESSAGE_CONFIRMATION_HISTORIQUE'/>")){
       	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RECAPITULATIF_DEMANDE_RENTE%>.validerHistorique";
       	document.forms[0].submit();
   	}
}

function init(){
	<%if(FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>
	errorObj.text="<%=viewBean.getMessage()%>";
	showErrors()
	errorObj.text="";
	<%}%>
}

</SCRIPT>
<%
	String requerantDescription = viewBean.getNoAVS() + " - "
								  +viewBean.getNom() + " " +viewBean.getPrenom() 
								  + " (" +viewBean.getDateNaissance() + " / " + viewBean.getSexe() + ")";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_REC_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><LABEL for="requerantDescription"><b><ct:FWLabel key="JSP_REC_D_REQUERANT"/></b></LABEL></TD>
							<TD colspan="3"><INPUT type="text" name="requerantDescription" value="<%=requerantDescription%>" class="RElibelleExtraLongDisabled" disabled="true" READONLY></TD>
						</TR>
						<TR>
							<TD><LABEL for="idDemande"><ct:FWLabel key="JSP_REC_D_NO_INTERNE"/></TD>
							<TD colspan="3"><INPUT type="text" name="idDemandeRente" value="<%=viewBean.getIdDemandeRente()%>" disabled="true" size="7" READONLY></TD>
						</TR>
						<TR>
							<TD colspan="4">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="4"><HR></TD>
						</TR>
						<TR>
							<TD><LABEL for="dateDemande"><ct:FWLabel key="JSP_REC_D_DATE_DEMANDE"/></LABEL></TD>
							<TD><INPUT type="text" name="dateDemande" value="<%=viewBean.getDateReception()%>" disabled="true" READONLY></TD>
							<TD><LABEL for="dateDepot"><ct:FWLabel key="JSP_REC_D_DATE_DEPOT"/></LABEL></TD>
							<TD><INPUT type="text" name="dateDepot" value="<%=viewBean.getDateDepot()%>" disabled="true" READONLY></TD>
						</TR>
						<TR>
							<TD><LABEL for="dateTraitement"><ct:FWLabel key="JSP_REC_D_DATE_TRAITEMENT"/></LABEL></TD>
							<TD><INPUT type="text" name="dateDemande" value="<%=viewBean.getDateTraitement()%>" disabled="true" READONLY></TD>
							<TD><LABEL for="etatDemande"><ct:FWLabel key="JSP_REC_D_ETAT"/></LABEL></TD>
							<TD>
								<ct:select name="etatDemande" defaultValue="<%=viewBean.getCsEtatDemande()%>" disabled="true">
									<ct:optionsCodesSystems csFamille="REETATDEM">
									</ct:optionsCodesSystems>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD><LABEL for="typeDemande"><ct:FWLabel key="JSP_REC_D_TYPE_DEMANDE"/></LABEL></TD>
							<TD>
								<ct:select name="typeDemande" defaultValue="<%=viewBean.getCsTypeDemande()%>" disabled="true">
									<ct:optionsCodesSystems csFamille="RETYPEDEM">
									</ct:optionsCodesSystems>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD colspan="4"><HR></TD>
						</TR>
						<TR>
							<TD><LABEL for="rentesAccordees"><b><ct:FWLabel key="JSP_REC_D_RENTES_ACCORDEES"/></b></LABEL></TD>
						</TR>
						<TR>
							<TD colspan="4">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="4">
								<TABLE border="1">
									<TR align="center">
										<TD><b><ct:FWLabel key="JSP_REC_D_NSS"/></b></TD>
										<TD><b><ct:FWLabel key="JSP_REC_D_NOM"/></b></TD>
										<TD><b><ct:FWLabel key="JSP_REC_D_GENRE"/></b></TD>
										<TD><b><ct:FWLabel key="JSP_REC_D_MONTANT"/></b></TD>
										<TD><b><ct:FWLabel key="JSP_REC_D_DATE_DEBUT"/></b></TD>
										<TD><b><ct:FWLabel key="JSP_REC_D_DATE_FIN"/></b></TD>
										<TD><b><ct:FWLabel key="JSP_REC_D_ETAT_1"/></b></TD>
										<TD><b><ct:FWLabel key="JSP_REC_D_RELATION_REQUERANT"/></b></TD>
									</TR>
									<TR>
										<TD colspan="8">Fonction à faire pour aller rechercher les rentes accordées par rapport à ce requérant</TD>
									</TR>
									<TR>
										<TD>&nbsp;</TD>
										<TD>&nbsp;</TD>
										<TD>&nbsp;</TD>
										<TD>&nbsp;</TD>
										<TD>&nbsp;</TD>
										<TD>&nbsp;</TD>
										<TD>&nbsp;</TD>
										<TD>&nbsp;</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
						<%if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE.equals(viewBean.getCsEtatDemande())) {%>

						<TR>
							<TD colspan="4" align="center"><br/>
									<a href="#" onclick="historiqueAValider();">
										<ct:FWLabel key="JSP_REC_D_HISTORIQUE_A_VALIDER"/>
									</a>
							</TD>							
						</TR>

						<%} %> 
						
						
						<TR>
							<TD colspan="4"><HR></TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<input class="btnCtrl" id="btnCan" type="button" value="<%=btnCanLabel%>" onclick="cancel()">
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>