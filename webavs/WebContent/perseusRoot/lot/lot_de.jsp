<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeLot"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatLot"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="globaz.perseus.vb.lot.PFLotViewBean"%>
<%@page import="ch.globaz.perseus.business.services.PerseusServiceLocator"%>

<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_PF_LOT_"
	idEcran="PPF0711";
	PFLotViewBean viewBean = (PFLotViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getLot().getSimpleLot().getIdLot();
//	bButtonUpdate = viewBean.getSimpleLot().getEtatCs()!= IRELot.CS_ETAT_LOT_VALIDE;
	bButtonDelete = false;
	
	
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>

<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.perseus.vb.lot.PFLotViewBean"%>

	<ct:menuChange displayId="menu" menuId="perseus-menuprincipal"/>
	<ct:menuChange displayId="options" menuId="perseus-optionsLot" showTab="options">

		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getLot().getSimpleLot().getIdLot()%>"/>
		<ct:menuSetAllParams key="idLot" value="<%=viewBean.getLot().getSimpleLot().getIdLot()%>"/>
		<ct:menuSetAllParams key="csEtatLot" value="<%=viewBean.getLot().getSimpleLot().getEtatCs()%>"/>
		<ct:menuSetAllParams key="csTypeLot" value="<%=viewBean.getLot().getSimpleLot().getTypeLot()%>"/>
		<ct:menuSetAllParams key="descriptionLot" value="<%=viewBean.getLot().getSimpleLot().getDescription()%>"/>
		<% if (CSEtatLot.LOT_VALIDE.getCodeSystem().equals(viewBean.getLot().getSimpleLot().getEtatCs())|| CSTypeLot.LOT_MENSUEL.getCodeSystem().equals(viewBean.getLot().getSimpleLot().getTypeLot())|| CSTypeLot.LOT_MENSUEL_RP.getCodeSystem().equals(viewBean.getLot().getSimpleLot().getTypeLot())||PerseusServiceLocator.getPmtMensuelService().isValidationDecisionAuthorise()) { %>
			<ct:menuActivateNode nodeId="COMPTABILISE" active="no"/>
		<% } else { %>
			<ct:menuActivateNode nodeId="COMPTABILISE" active="yes"/>
		<% } %>		
	</ct:menuChange>


	<SCRIPT language="javascript">
	
	function add(){
	    document.forms[0].elements('userAction').value="perseus.lot.lot.ajouter";
	}

	function upd(){
	}

	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="perseus.lot.lot.ajouter";
	    }else{
	        document.forms[0].elements('userAction').value="perseus.lot.lot.ajouter";
	    }
	    return state;
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add"){
		  document.forms[0].elements('userAction').value="back";
		}else{
		  document.forms[0].elements('userAction').value="perseus.lot.lot.chercher";
		}
	}

	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="perseus.lot.lot.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PF_LOT_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_PF_LOT_D_NO"/></TD>
							<TD><INPUT type="text" class="disabled" name="idLot" readonly value="<%=viewBean.getLot().getSimpleLot().getIdLot()%>"></TD>
							<TD><ct:FWLabel key="JSP_PF_LOT_D_ETAT"/></TD>
							<TD><INPUT name="etatCs" type="text" value="<%=objSession.getCodeLibelle(viewBean.getLot().getSimpleLot().getEtatCs())%>" class="disabled" readonly></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PF_LOT_D_DATE_CREATION"/></TD>
							<TD><INPUT type="text" class="disabled" name="dateCreation" readonly value="<%=viewBean.getLot().getSimpleLot().getDateCreation()%>"></TD>
							<TD><ct:FWLabel key="JSP_PF_LOT_D_DATE_COMPTABILISATION"/></TD>
							<TD><INPUT type="text" class="disabled" name="dateEnvoi" readonly  value="<%=viewBean.getLot().getSimpleLot().getDateEnvoi()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PF_LOT_D_DESCRIPTION"/></TD>
							<TD><INPUT type="text" name="description" value="<%=viewBean.getLot().getSimpleLot().getDescription()%>" class="libelleLong"> </TD>
							<TD><ct:FWLabel key="JSP_PF_LOT_D_TYPE"/></TD>
							<TD><INPUT type="text" name="typeLot" value="<%=objSession.getCodeLibelle(viewBean.getLot().getSimpleLot().getTypeLot())%>" class="disabled" readonly></TD>
						</TR>
						<TR height="10"><TD></TD></TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PF_LOT_D_PROPRIETAIRE"/></TD>
							<TD><INPUT type="text" name="proprietaireLot" value="<%=viewBean.getLot().getSimpleLot().getProprietaireLot()%>" class="libelleLong"> </TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PF_LOT_D_JOURNAL"/></TD>
							<TD><INPUT type="text" name="idJournal" value="<%=objSession.getCodeLibelle(viewBean.getLot().getSimpleLot().getIdJournal())%>" class="libelleLong"> </TD>
						</TR>
<%--						<%if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getLot().getSimpleLot().getIdJournalCA())) {%>
						<TR>
							<td colspan="4">
								<A href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.apercuJournal.afficher&selectedId=<%=viewBean.getLot().getSimpleLot().getIdJournal()%>" class="external_link">
									<ct:FWLabel key="JSP_PC_LOT_D_JOURNAL"/>
								</A>
							</td>
						</TR>			
						<%}%>
--%>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>