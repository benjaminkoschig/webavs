<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.jade.client.util.JadeNumericUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP0034";

	globaz.apg.vb.prestation.APPrestationJointLotTiersDroitViewBean viewBean = (globaz.apg.vb.prestation.APPrestationJointLotTiersDroitViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdPrestationApg();
	bButtonDelete = false;
	
	//bButtonNew=true;
	bButtonDelete=!viewBean.isDefinitif() && !viewBean.isAnnule() && viewBean.getSession().hasRight(IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT, FWSecureConstants.UPDATE);
	bButtonUpdate=!viewBean.isDefinitif() && !viewBean.isAnnule() && viewBean.getSession().hasRight(IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT, FWSecureConstants.UPDATE);
	
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

	<!--si APG -->
	<%if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_APG) {%>
   		<%@page import="globaz.apg.application.APApplication"%>
<%@page import="globaz.apg.servlet.IAPActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg"/>
	<!--sinon, maternité -->
	<%} else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE) {%>	   	
		<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat"/>
	<%}else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_PATERNITE) {%>
		<ct:menuChange displayId="menu" menuId="ap-menuprincipalapat"/>
	<%}%>

<%if (viewBean.isOkPourMiseEnLot()){%>
			<ct:menuChange displayId="options" menuId="ap-optionprestationlotok" showTab="options">
				<ct:menuSetAllParams key="idPrestationCourante" value="<%=viewBean.getIdPrestationApg()%>"/>
				<ct:menuSetAllParams key="genreService" value="<%=viewBean.getGenreService()%>"/>
				<ct:menuSetAllParams key="idDroit" value="<%=viewBean.getIdDroit()%>"/>
				<ct:menuSetAllParams key="forIdPrestation" value="<%=viewBean.getIdPrestationApg()%>"/>
				<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPrestationApg()%>"/>	
<%
			if (!JadeNumericUtil.isEmptyOrZero(viewBean.getIdAnnonce())) {
%>					<ct:menuSetAllParams key="idAnnonce" value="<%=viewBean.getIdAnnonce()%>" />
					<ct:menuActivateNode active="yes" nodeId="opAnnonce1"/>
<%
			} else {
%>					<ct:menuActivateNode active="no" nodeId="opAnnonce1"/>
<%
			}
%>
			</ct:menuChange>
	<% } else { %>
			<ct:menuChange displayId="options" menuId="ap-optionprestationlotpasok" showTab="options">
				<ct:menuSetAllParams key="idPrestationCourante" value="<%=viewBean.getIdPrestationApg()%>"/>
				<ct:menuSetAllParams key="genreService" value="<%=viewBean.getGenreService()%>"/>
				<ct:menuSetAllParams key="idDroit" value="<%=viewBean.getIdDroit()%>"/>
				<ct:menuSetAllParams key="forIdPrestation" value="<%=viewBean.getIdPrestationApg()%>"/>
<%
			if (!JadeNumericUtil.isEmptyOrZero(viewBean.getIdAnnonce())) {
%>					<ct:menuSetAllParams key="idAnnonce" value="<%=viewBean.getIdAnnonce()%>" />
					<ct:menuActivateNode active="yes" nodeId="opAnnonce2"/>
<%
			} else {
%>					<ct:menuActivateNode active="no" nodeId="opAnnonce2"/>
<%
			}
%>
			</ct:menuChange>
	<%}%>

<SCRIPT language="javascript"> 


	function add() {
	    //document.forms[0].elements('userAction').value="apg.lots.lot.ajouter";
	}
	
	function upd() {
	}
	
	function validate() {
	    state = true;
	    //On n'ajoute rien ici, on est forcément sur une prestation existante
	        document.forms[0].elements('userAction').value="apg.prestation.prestationJointLotTiersDroit.modifier";
	    return state;
	
	}
	
	function cancel() {
	
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="apg.prestation.prestationJointLotTiersDroit.chercher";
	}
	
	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="apg.prestation.prestationJointLotTiersDroit.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
	
	}
	
	function ajouterDansLot(){
		document.forms[0].elements('userAction').value="apg.prestation.prestationJointLotTiersDroit.actionAjouterDansLot";
		document.forms[0].submit();	
	}
	
	function limiteur()
   // limite la saisie de la remarque à 255 caractères //D0107 1000char
    { 
       	maximum = 1000;
    	if (document.forms[0].elements('remarque').value.length > maximum)
      		document.forms[0].elements('remarque').value = document.forms[0].elements('remarque').value.substring(0, maximum);
    }
    
    function recalculer() {
    	document.forms[0].elements('montantBrut').value = toFloat(document.forms[0].elements('nombreJoursSoldes').value) * toFloat(document.forms[0].elements('montantJournalier').value);
    	validateFloatNumber(document.forms[0].elements('montantBrut'));
    }
    
    function toFloat(input){
		var output = input.replace("'", ""); 
		return output;
	}
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<span class="postItIcon"><ct:FWNote sourceId="<%=viewBean.getIdPrestationApg()%>" tableSource="<%=APApplication.KEY_POSTIT_PRESTATIONS_APG_MAT%>"/></span>
			<ct:FWLabel key="JSP_PRESTATION"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD>
						<table border="0" cellspacing="4" cellpadding="0">
						<TR>
							<TD colspan="2"><ct:FWLabel key="JSP_NO_PRESTATION"/>&nbsp;<INPUT type="text" class="numeroCourt disabled" readonly value="<%=viewBean.getIdPrestationApg()%>"></TD>
							<TD colspan="2"><ct:FWLabel key="JSP_NO_REVISION"/>&nbsp;<INPUT type="text" class="disabled" readonly value="<%=viewBean.getLibelleNoRevision()%>"></TD>
							<TD colspan="4">&nbsp;</TD>
						</TR>						
						<tr><td colspan="8">&nbsp;</td></tr>
						<TR>
							<TD><B><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></B></TD>
							<TD colspan="7"><INPUT type="text" value="<%=viewBean.getDetailRequerantDetail()%>" size="100" class="disabled" readonly></TD>

						</TR>
						<TR>
						<TD colspan="8">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="2"><ct:FWLabel key="JSP_CONTENU_ANNONCE"/></TD>							
							<td></td>
							<TD><INPUT type="text" name="libelleContenuAnnonce" readonly value="<%=viewBean.getLibelleContenuAnnonce()%>" class="disabled"></TD>							
							<TD><ct:FWLabel key="JSP_GENRE_PRESTATION"/></TD>							
							<TD colspan="3"><INPUT type="text" name="genrePrestation" readonly value="<%=viewBean.getLibelleGenrePrestation()%>" class="disabled"></TD>

						</TR>						
						<TR>
							<TD colspan="2"><ct:FWLabel key="JSP_PERIODE"/></TD>
							<TD><ct:FWLabel key="JSP_DU"/></td><td><INPUT type="text" name="dateDebut" value="<%=viewBean.getDateDebut()%>" <%=viewBean.isRestitution()?"class=\"date disabled\" readonly":" class=\"date\""%>></TD>
							<TD><ct:FWLabel key="JSP_AU"/></td><td><INPUT type="text" name="dateFin" value="<%=viewBean.getDateFin()%>" <%=viewBean.isRestitution()?"class=\"date disabled\" readonly":" class=\"date\""%>></TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="2"><ct:FWLabel key="JSP_NB_JOURS_SOLDES"/></TD>
							<td></td>
							<TD colspan="5"><INPUT type="text" name="nombreJoursSoldes" value="<%=viewBean.getNombreJoursSoldes()%>" onchange="validateIntegerNumber(this);recalculer();" onkeypress="return filterCharForInteger(window.event);" <%=viewBean.isRestitution()?"class=\"numeroCourt disabled\" readonly":" class=\"numeroCourt\""%>></TD>
						</TR>
						<TR>
							<TD colspan="2"><ct:FWLabel key="JSP_TAUX_JOURNALIER"/></TD>
							<td></td>
							<TD>
								<INPUT type="text" name="montantJournalier" value="<%=globaz.globall.util.JANumberFormatter.fmt(viewBean.getMontantJournalier(),true,true,false,2)%>" onchange="validateFloatNumber(this);recalculer();" onkeypress="return filterCharForFloat(window.event);" <%=viewBean.isRestitution()?"class=\"montant disabled\" readonly":" class=\"montant\""%>>
							</TD>
							<TD><ct:FWLabel key="JSP_TAUX_JOURNALIER_BASE"/></TD>
							<TD>
								<INPUT type="text" class="montant disabled" name ="basicDailyAmount" readonly value="<%=viewBean.getBasicDailyAmount()%>">
							</TD>
						
						</TR>
						
						<TR>
							<TD colspan="2"><ct:FWLabel key="JSP_MONTANT_BRUT"/></TD>
							<td>&nbsp;</td>
							<TD>
								<input type="text" class="montant disabled" name="montantBrut" value="<%=globaz.globall.util.JANumberFormatter.fmt(viewBean.getMontantBrut(),true,true,false,2)%>" readonly/>
							</td>
							<td>
								<ct:FWLabel key="JSP_DONT_MONTANT_ALLOC_EXPLOIT"/>
							</td>
							<td>
								<INPUT type="text" class="montant disabled" name ="dummy" readonly value="<%=viewBean.getMontantTotalAllocExploitation()%>">
								<INPUT type="hidden" name ="montantAllocationExploitation" value="<%=viewBean.getMontantAllocationExploitation()%>">
							</td>
						</TR>

						<TR>
							<TD colspan="2"><ct:FWLabel key="JSP_ALLOCATION_FRAIS_GARDE"/></TD>
							<td></td>
							<TD colspan="3"> <INPUT type="text" name="allocationFraisGarde" class="montant disabled" readonly value="<%=viewBean.getFraisGarde()%>"></TD>
							<TD colspan="2"></TD><TD colspan="2"></TD>										
						</TR>

						<TR>
							<TD colspan="2"><ct:FWLabel key="JSP_ETAT"/></TD>
							<td></td>
							<TD><ct:FWCodeSelectTag codeType="<%=globaz.apg.api.prestation.IAPPrestation.CS_GROUPE_ETAT_PRESTATION_APG%>" defaut="<%=viewBean.getEtat()%>" name="etat" except="<%=viewBean.getExceptEtat()%>"/> </TD>
							<td><ct:FWLabel key="JSP_PMT_DU"/></td>
							<td><INPUT type="text" name="date du paiement" class="date" readonly value="<%=viewBean.getDatePaiement()%>"></td>
							<td></td>
							<td></td>
						</TR>
						<TR>
							<TD colspan="2"><LABEL for="remarque"><ct:FWLabel key="JSP_REMARQUE"/></LABEL></TD>
							<td></td>
							<TD colspan="5"><textarea name="remarque" cols="100" rows="10" onkeyup="limiteur();" <%=viewBean.isRestitution()?"class=\"disabled\" readonly":" class=\"\""%>><%=viewBean.getRemarque()%></textarea></TD>
								<!--<INPUT type="texte" value="<%//=viewBean.getRemarque()%>" size="100" onkeydown="limiteur();" name="remarque" id="remarque">-->
						</TR>
						<TR>
							<TD colspan="3"></TD>
							<TD colspan="6"><LABEL for="commentaireRemarque"><ct:FWLabel key="JSP_REMARQUE_PRESTATION"/></LABEL></TD>
						</TR>
						<TR>
							<TD colspan="8">
							<%if (viewBean.isOkPourMiseEnLot()&&
								  viewBean.getSession().hasRight(IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT, FWSecureConstants.UPDATE)){ %>
								<INPUT type="button" onclick="ajouterDansLot()" value="<ct:FWLabel key="JSP_AJOUTER_DANS_LOT"/>">
							<%}%>
							</TD>
						</TR>
						</table>
						</TD></TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
