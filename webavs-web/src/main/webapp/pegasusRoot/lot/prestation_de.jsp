<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_PC_PRE_D"

	idEcran="PPC0093";

	PCPrestationViewBean viewBean = (PCPrestationViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getId();
	bButtonDelete = false;
	bButtonUpdate = false;
	bButtonCancel = true;
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.pegasus.vb.lot.PCPrestationViewBean"%>
<%@page import="globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsprestation" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getPrestation().getSimplePrestation().getIdPrestation()%>"/>
	<ct:menuSetAllParams key="montantPrestation" value="<%=viewBean.getPrestation().getSimplePrestation().getMontantTotal()%>"/>
	<ct:menuSetAllParams key="idTierRequerant" value="<%=viewBean.getPrestation().getTiersBeneficiaire().getTiers().getIdTiers()%>"/>
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
		  document.forms[0].elements('userAction').value="pegasus.lot.prestation.chercher";
		}
	}
	
	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="pegasus.lot.prestation.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_PRE_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_PC_PRE_D_PRESTATAIRE"/></TD>
							<TD colspan="5">
								<re:PRDisplayRequerantInfoTag 
								session="<%=(BSession)controller.getSession()%>" 
								idTiers="<%=viewBean.getPrestation().getTiersBeneficiaire().getTiers().getId()%>"
								style="<%=PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>"/>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PC_PRE_D_NO"/></TD>
							<TD><INPUT name="no" type="text" class="disabled" readonly value="<%=viewBean.getPrestation().getSimplePrestation().getIdPrestation()%>"></TD>
							<TD><ct:FWLabel key="JSP_PC_PRE_D_NO_LOT"/></TD>
							<TD><INPUT name="noLot" type="text" value="<%=viewBean.getPrestation().getSimplePrestation().getIdLot()%>" class="disabled" readonly></TD>
							<TD><ct:FWLabel key="JSP_PC_PRE_D_ETAT"/></TD>
							<TD><INPUT name="csEtatLibelle" type="text" value="<%=objSession.getCodeLibelle(viewBean.getPrestation().getSimplePrestation().getCsEtat())%>" class="disabled" readonly></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PC_PRE_D_MOIS_ANNEE"/></TD>
							<TD><INPUT name="moisAnnee" type="text" class="disabled"  readonly value="<%=viewBean.getPrestation().getSimplePrestation().getMoisAn()%>"></TD>
							<TD><ct:FWLabel key="JSP_PC_PRE_D_MONTANT"/></TD>
							<TD colspan="3"><INPUT name="montant" type="text" value="<%=new FWCurrency(viewBean.getPrestation().getSimplePrestation().getMontantTotal()).toStringFormat()%>" class="montantDisabled" readonly></TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>