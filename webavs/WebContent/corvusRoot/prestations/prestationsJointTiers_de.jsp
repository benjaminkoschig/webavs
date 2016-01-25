<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_PRE_D"

	idEcran="PRE0033";

	globaz.corvus.vb.prestations.REPrestationsJointTiersViewBean viewBean = (globaz.corvus.vb.prestations.REPrestationsJointTiersViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdPrestation();
	bButtonDelete = false;
	bButtonUpdate = false;
	bButtonCancel = true;
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionprestations" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPrestation()%>"/>
	<ct:menuSetAllParams key="montantPrestation" value="<%=viewBean.getMontantPrestation()%>"/>
	<ct:menuSetAllParams key="idTierRequerant" value="<%=viewBean.getIdTiersPrestataire()%>"/>
</ct:menuChange>

<SCRIPT language="javascript"> 
	
	function add(){
	}
	
	function upd(){
	}
	
	function validate() {
	}
	
	function cancel() {
		if (document.forms[0].elements('_method').value == "add"){
		  document.forms[0].elements('userAction').value="back";
		}else{
		  document.forms[0].elements('userAction').value="corvus.prestations.prestationsJointTiers.chercher";
		}
	}
	
	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="corvus.prestations.prestationsJointTiers.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PRE_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_PRE_D_PRESTATAIRE"/></TD>
							<TD colspan="5">
								<re:PRDisplayRequerantInfoTag 
								session="<%=(globaz.globall.db.BSession)controller.getSession()%>" 
								idTiers="<%=viewBean.getIdTiersPrestataire()%>"
								style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>"/>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PRE_D_NO"/></TD>
							<TD><INPUT name="no" type="text" class="disabled" readonly value="<%=viewBean.getIdPrestation()%>"></TD>
							<TD><ct:FWLabel key="JSP_PRE_D_NO_LOT"/></TD>
							<TD><INPUT name="noLot" type="text" value="<%=viewBean.getIdLot()%>" class="disabled" readonly></TD>
							<TD><ct:FWLabel key="JSP_PRE_D_ETAT"/></TD>
							<TD><INPUT name="csEtatLibelle" type="text" value="<%=viewBean.getCsEtatLibelle()%>" class="disabled" readonly></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PRE_D_MOIS_ANNEE"/></TD>
							<TD><INPUT name="moisAnnee" type="text" class="disabled"  readonly value="<%=viewBean.getMoisAnnee()%>"></TD>
							<TD><ct:FWLabel key="JSP_PRE_D_TYPE"/></TD>
							<TD><INPUT name="csTypeLibelle" type="text" value="<%=viewBean.getCsTypeLibelle()%>" class="disabled" readonly></TD>
							<TD><ct:FWLabel key="JSP_PRE_D_MONTANT"/></TD>
							<TD><INPUT name="montant" type="text" value="<%=viewBean.getMontantPrestationFormate()%>" class="montantDisabled" readonly></TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>