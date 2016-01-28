<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.vb.paiement.RFPrestationDetailViewBean"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_RF_PRE_D"

	idEcran="PRF0049";

	RFPrestationDetailViewBean viewBean = (RFPrestationDetailViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getId();
	bButtonDelete = false;
	bButtonUpdate = true;
	bButtonCancel = true;
	bButtonValidate = true;	
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsprestation" showTab="options" >
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPrestation()%>" />							
	<ct:menuSetAllParams key="idPrestation" value="<%=viewBean.getIdPrestation()%>" />
	<ct:menuSetAllParams key="idTierRequerant" value="<%=viewBean.getIdTiers()%>" />
	<ct:menuSetAllParams key="montantPrestation" value="<%=viewBean.getMontantTotal()%>" />
	<ct:menuSetAllParams key="idDecision" value="<%=viewBean.getIdDecision()%>" />
</ct:menuChange>

<SCRIPT language="javascript"> 
	
	function add(){
	}
	
	function upd(){
	}
	
	function validate() {
	    state = true;
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_PRESTATION%>.ajouter";
	    }else{
	    	document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_PRESTATION%>.modifier";
	    }
	    return state;		
	}
	
	function cancel() {
		if (document.forms[0].elements('_method').value == "add"){
		  document.forms[0].elements('userAction').value="back";
		}else{
		  document.forms[0].elements('userAction').value="cygnus.paiement.prestation.chercher";
		}
	}
	
	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="cygnus.paiement.prestation.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
	}
	
	function postInit(){
	    if (document.forms[0].elements('_method').value == "add"){		
			action('add');
	    }     
	}	
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_PRE_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
			<%-- tpl:put name="zoneMain" --%>
			<TR valign="top">
				<TD><ct:FWLabel key="JSP_RF_PRE_D_PRESTATAIRE"/></TD>
				<TD colspan="5">
					<%=viewBean.getDetailAssure()%>								
				</TD>
			</TR>
			<TR><TD colspan="4">&nbsp;</TD></TR>
			<TR>
				<TD><ct:FWLabel key="JSP_RF_PRE_D_REFERENCE_PAIEMENT"/></TD>
				<TD colspan="3">
					<textarea name="referencePaiement" cols="70" rows="3"><%=viewBean.getReferencePaiement()%></textarea>			
				</TD>
			</TR>
			<TR><TD colspan="4">&nbsp;</TD></TR>
			<TR>
				<TD><ct:FWLabel key="JSP_RF_PRE_D_NO"/></TD>
				<TD><INPUT name="no" type="text" class="disabled" readonly value="<%=viewBean.getIdPrestation()%>"></TD>
				<INPUT type="hidden" name="idTierRequerant" value="<%=viewBean.getIdTiers()%>" disabled="disabled" />
				<TD><ct:FWLabel key="JSP_RF_PRE_D_NO_LOT"/></TD>
				<TD><INPUT name="noLot" type="text" value="<%=viewBean.getIdLot()%>" class="disabled" readonly></TD>
				<TD><ct:FWLabel key="JSP_RF_PRE_D_ETAT"/></TD>
				<TD><INPUT name="csEtatLibelle" type="text" value="<%=objSession.getCodeLibelle(viewBean.getCsEtatPrestation())%>" class="disabled" readonly></TD>
			</TR>
			<TR>
				<TD><ct:FWLabel key="JSP_RF_PRE_D_MOIS_ANNEE"/></TD>
				<TD><INPUT name="moisAnnee" type="text" class="disabled"  readonly value="<%=viewBean.getDateMoisAnnee()%>"></TD>
				<TD><ct:FWLabel key="JSP_RF_PRE_D_MONTANT"/></TD>
				<TD colspan="3"><INPUT name="montant" type="text" value="<%=new FWCurrency(viewBean.getMontantTotal()).toStringFormat()%>" class="montantDisabled" readonly></TD>
			</TR>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<ct:menuChange displayId="options" menuId="cygnus-optionsprestation"/>
<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPrestation()%>" menuId="cygnus-optionsprestation"/>
<ct:menuSetAllParams key="idPrestation" value="<%=viewBean.getIdPrestation()%>" menuId="cygnus-optionsprestation"/>
<ct:menuSetAllParams key="idTierRequerant" value="<%=viewBean.getIdTiers()%>" menuId="cygnus-optionsprestation"/>
<ct:menuSetAllParams key="montantPrestation" value="<%=viewBean.getMontantTotal()%>" menuId="cygnus-optionsprestation"/>
<ct:menuSetAllParams key="idDecision" value="<%=viewBean.getIdDecision()%>" menuId="cygnus-optionsprestation"/>
<SCRIPT language="javascript">
reloadMenuFrame(top.fr_menu, MENU_TAB_OPTIONS);
</SCRIPT>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>