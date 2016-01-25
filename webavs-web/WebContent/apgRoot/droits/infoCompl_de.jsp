<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PAP0040";
	
	globaz.apg.vb.droits.APInfoComplViewBean viewBean = (globaz.apg.vb.droits.APInfoComplViewBean)(session.getAttribute("viewBean"));
	
	bButtonCancel = true;
	bButtonValidate = true;
	bButtonDelete =  true;
	bButtonUpdate = true;
	bButtonNew = false;		
%>


<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<!--si APG -->
<%if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_APG) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<!--sinon, maternité -->
<%} else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%}%>

<SCRIPT language="javaScript">

function add() {}

function upd() {
	document.forms[0].elements('typeInfoCompl').disabled = true;
}

function validate() {
	state = true;
	    
    if (document.forms[0].elements('_method').value == "add"){
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_INFO_COMPL%>.ajouter";
    }
    else{
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_INFO_COMPL%>.modifier";
    }
    
    return state;
}

function cancel() {
   document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_DROIT_LAPG%>.chercher";
}

function init(){
	<%if (viewBean.isBaseIndAfterEnd()){%>
		if (window.confirm("<ct:FWLabel key='JSP_BASES_IND_ULTERIEURS_TROUVEES'/>")){
			document.forms[0].elements('userAction').value = "<%=globaz.apg.servlet.IAPActions.ACTION_INFO_COMPL%>.transfererPrononceFinal";
			document.forms[0].submit();  
		}
	<%}%>
}

function typeInfoComplChange(){
	document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_INFO_COMPL%>.afficher";
	document.forms[0].submit();
}

function del() {
	if(window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_INFO_COMPL%>.supprimer";
        document.forms[0].submit();
	}
}

function impDecRef(){
	document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_GENERER_DECISION_REFUS%>.afficher";
	document.forms[0].submit();
}

 </SCRIPT>
 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="CREER_INFO_COMPL_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><b><ct:FWLabel key="JSP_DETAIL_ASSURE"/></b></TD>
							<TD colspan="3"><INPUT type="text" class="disabled" readonly value="<%=viewBean.getDetailRequerantDetail()%>" size="100"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_NO_DROIT"/></TD>
							<TD><INPUT type="text" name="idPrononce" class="disabled" readonly value="<%=viewBean.getIdPrononce()%>"></TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="4">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="4"><HR></TD>
						</TR>
						<TR>
							<TD colspan="4">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_TYPE_INFORMATION_COMPLEMENTAIRE"/></TD>
							<TD><ct:select name="typeInfoCompl" defaultValue="<%=viewBean.getTypeInfoCompl()%>" onchange="typeInfoComplChange();">
									<ct:optionsCodesSystems csFamille="<%=globaz.apg.api.droits.IAPDroitLAPG.CS_GROUPE_TYPE_INFO_COMPL%>">
										<%if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_APG) {%>
											<ct:excludeCode code="<%=globaz.apg.api.droits.IAPDroitLAPG.CS_REFUS_DOSSIER%>"/>
										<%}%>
									</ct:optionsCodesSystems>
								</ct:select>
							</TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="4">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="4"><HR></TD>
						</TR>
						<TR>
							<TD colspan="4">&nbsp;</TD>
						</TR>
						<%if(globaz.apg.api.droits.IAPDroitLAPG.CS_TRANSFER_DOSSIER.equalsIgnoreCase(viewBean.getTypeInfoCompl())){%>
						<TBODY id="transfererDroit">
							<TR>
								<TD><ct:FWLabel key="JSP_DATE_TRANSFER"/></TD>
								<TD><ct:FWCalendarTag name="dateInfoCompl" value="<%=viewBean.getDateInfoCompl()%>"/></TD>
								<TD colspan="2">&nbsp;</TD>
							</TR>
							<TR>
								<TD><ct:FWLabel key="JSP_CAISSE"/></TD>
								<TD>
									<INPUT type="text" name="nomCaisse" class="disabled" readonly value="<%=viewBean.getNomCaisse()%>">
									<INPUT type="hidden" name="idTiersCaisse" value="<%=viewBean.getIdTiersCaisse()%>">
									<ct:FWSelectorTag
										name="selecteurCaisse"
										
										methods="<%=viewBean.getMethodesSelecteurCaisse()%>"
										providerApplication="pyxis"
										providerPrefix="TI"
										providerAction="pyxis.tiers.administration.chercher"
										target="fr_main"
										redirectUrl="<%=mainServletPath%>"/>
								</TD>
								<TD colspan="2">&nbsp;</TD>
							</TR>
							<TR>
								<TD><ct:FWLabel key="JSP_MOTIF_TRANSFER"/></TD>
								<TD><ct:select name="motif" defaultValue="<%=viewBean.getMotif()%>">
									<ct:optionsCodesSystems csFamille="<%=globaz.apg.api.droits.IAPDroitLAPG.CS_GROUPE_MOTIFS_TRANSFER%>"/>
								</ct:select></TD>
								<TD colspan="2">&nbsp;</TD>
							</TR>
						</TBODY>
						<%} else if(globaz.apg.api.droits.IAPDroitLAPG.CS_REFUS_DOSSIER.equalsIgnoreCase(viewBean.getTypeInfoCompl())){%>
						<TBODY id="refuserDroit">
							<TR>
								<TD><ct:FWLabel key="JSP_DATE_REFUS"/></TD>
								<TD><ct:FWCalendarTag name="dateInfoCompl" value="<%=viewBean.getDateInfoCompl()%>"/></TD>
								<TD colspan="2">&nbsp;</TD>
							</TR>
							<TR>
								<TD><ct:FWLabel key="JSP_MOTIF_REFUS"/></TD>
								<TD><ct:select name="motif" defaultValue="<%=viewBean.getMotif()%>">
									<ct:optionsCodesSystems csFamille="<%=globaz.apg.api.droits.IAPDroitLAPG.CS_GROUPE_MOTIFS_REFUS%>"/>
								</ct:select></TD>
								<TD colspan="2">&nbsp;</TD>
							</TR>
						</TBODY>
						<%}%>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%if (globaz.apg.api.droits.IAPDroitLAPG.CS_REFUS_DOSSIER.equals(viewBean.getTypeInfoCompl())){%>
	<input class="btnCtrl" id="btnImpDecRef" type="button" value="<ct:FWLabel key="JSP_BUTTON_IMPRIMER_DEC_REFUS"/>" onclick="impDecRef()">
<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>