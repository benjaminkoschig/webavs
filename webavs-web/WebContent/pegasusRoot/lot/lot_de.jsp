
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_PC_LOT_D"

	idEcran="PPC0091";

	PCLotViewBean viewBean = (PCLotViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getSimpleLot().getIdLot();
	bButtonUpdate = viewBean.getSimpleLot().getCsEtat()!= IRELot.CS_ETAT_LOT_VALIDE;
	bButtonDelete = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.corvus.api.lots.IRELot"%>
<%@page import="globaz.pegasus.vb.lot.PCLotViewBean"%>

	<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal"/>
	<ct:menuChange displayId="options" menuId="pegasus-optionslot" showTab="options">

		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getSimpleLot().getIdLot()%>"/>
		<ct:menuSetAllParams key="idLot" value="<%=viewBean.getSimpleLot().getIdLot()%>"/>
		<ct:menuSetAllParams key="csEtatLot" value="<%=viewBean.getSimpleLot().getCsEtat()%>"/>
		<ct:menuSetAllParams key="csTypeLot" value="<%=viewBean.getSimpleLot().getCsTypeLot()%>"/>
		<ct:menuSetAllParams key="descriptionLot" value="<%=viewBean.getSimpleLot().getDescription()%>"/>
		
		<%if (IRELot.CS_TYP_LOT_MENSUEL.equals(viewBean.getSimpleLot().getCsTypeLot())){%>
			<ct:menuActivateNode nodeId="prestation" active="no"/>
			<ct:menuActivateNode nodeId="comptabliser" active="no"/>
			<ct:menuActivateNode nodeId="listeOrdresVersement" active="no"/>
		<%} else{%>
		    <ct:menuActivateNode nodeId="prestation" active="yes"/>
			<ct:menuActivateNode nodeId="comptabliser" active="yes"/>
			<ct:menuActivateNode nodeId="listeOrdresVersement" active="yes"/>
		<%} %>		
	</ct:menuChange>


	<SCRIPT language="javascript">
	
	function add(){
	    document.forms[0].elements('userAction').value="pegasus.lot.lot.ajouter";
	}

	function upd(){
	}

	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="pegasus.lot.lot.ajouter";
	    }else{
	        document.forms[0].elements('userAction').value="pegasus.lot.lot.modifier";
	    }
	    return state;
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add"){
		  document.forms[0].elements('userAction').value="back";
		}else{
		  document.forms[0].elements('userAction').value="pegasus.lot.lot.chercher";
		}
	}

	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="pegasus.lot.lot.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_LOT_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_PC_LOT_D_NO"/></TD>
							<TD><INPUT type="text" class="disabled" name="idLot" readonly value="<%=viewBean.getSimpleLot().getIdLot()%>"></TD>
							<TD><ct:FWLabel key="JSP_PC_LOT_D_ETAT"/></TD>
							<TD><INPUT name="csEtatLotLibelle" type="text" value="<%=objSession.getCodeLibelle(viewBean.getSimpleLot().getCsEtat())%>" class="disabled" readonly></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PC_LOT_D_DATE_CREATION"/></TD>
							<TD><INPUT type="text" class="disabled" name="dateCreationLot" readonly value="<%=viewBean.getSimpleLot().getDateCreation()%>"></TD>
							<TD><ct:FWLabel key="JSP_PC_LOT_D_DATE_COMPTABILISATION"/></TD>
							<TD><INPUT type="text" class="disabled" name="dateEnvoiLot" readonly  value="<%=viewBean.getSimpleLot().getDateEnvoi()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PC_LOT_D_DESCRIPTION"/></TD>
							<TD><INPUT type="text" name="description" value="<%=viewBean.getSimpleLot().getDescription()%>" class="libelleLong"> </TD>
							<TD><ct:FWLabel key="JSP_PC_LOT_D_TYPE"/></TD>
							<TD><INPUT type="text" name="csTypeLotLibelle" value="<%=objSession.getCodeLibelle(viewBean.getSimpleLot().getCsTypeLot())%>" class="disabled" readonly></TD>
						</TR>
						<%if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getSimpleLot().getIdJournalCA())) {%>
						<TR>
							<td colspan="4">
								<A href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.apercuJournal.afficher&selectedId=<%=viewBean.getSimpleLot().getIdJournalCA()%>" class="external_link">
									<ct:FWLabel key="JSP_PC_LOT_D_JOURNAL_ASSOCIE_CA"/>
								</A>
							</td>
						</TR>
						<%}%>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>