<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_LOT_D"

	idEcran="PRE0031";

	globaz.corvus.vb.lots.RELotViewBean viewBean = (globaz.corvus.vb.lots.RELotViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdLot();
	bButtonUpdate = !viewBean.isInCsEtatLotValide() && viewBean.getSession().hasRight("corvus.lots.lot.modifier", FWSecureConstants.UPDATE);
	bButtonDelete = false;

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<!--si APG -->
<%@page import="globaz.corvus.api.lots.IRELot"%>
<%@page import="globaz.corvus.vb.prestations.REPrestationsJointDemandeRenteViewBean"%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionslot" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdLot()%>"/>
	<ct:menuSetAllParams key="idLot" value="<%=viewBean.getIdLot()%>"/>
	<ct:menuSetAllParams key="csTypeLot" value="<%=viewBean.getCsTypeLot()%>"/>
	<ct:menuSetAllParams key="csEtatLot" value="<%=viewBean.getCsEtatLot()%>"/>
	<ct:menuSetAllParams key="descriptionLot" value="<%=viewBean.getDescription()%>"/>
	<ct:menuSetAllParams key="provenance" value="<%=REPrestationsJointDemandeRenteViewBean.FROM_ECRAN_LOTS%>"/>		
	<%if (IRELot.CS_TYP_LOT_MENSUEL.equals(viewBean.getCsTypeLot()) || IRELot.CS_ETAT_LOT_VALIDE.equals(viewBean.getCsEtatLot())) {%>
		<ct:menuActivateNode nodeId="comptabiliser" active="no"/>
	<%} else{%>
		<ct:menuActivateNode nodeId="comptabiliser" active="yes"/>
	<%} %>
	<%if (IRELot.CS_TYP_LOT_MENSUEL.equals(viewBean.getCsTypeLot())){%>
		<ct:menuActivateNode nodeId="prestation" active="no"/>
		<ct:menuActivateNode nodeId="imprimerOrdresVersement" active="no"/>
	<%} else{%>
		<ct:menuActivateNode nodeId="prestation" active="yes"/>
		<ct:menuActivateNode nodeId="imprimerOrdresVersement" active="yes"/>
	<%} %>		
</ct:menuChange>

<SCRIPT language="javascript">

	function add(){
	    document.forms[0].elements('userAction').value="corvus.lots.lot.ajouter";
	}

	function upd(){
	}

	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="corvus.lots.lot.ajouter";
	    }else{
	        document.forms[0].elements('userAction').value="corvus.lots.lot.modifier";
	    }
	    return state;
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add"){
		  document.forms[0].elements('userAction').value="back";
		}else{
		  document.forms[0].elements('userAction').value="corvus.lots.lot.chercher";
		}
	}

	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="corvus.lots.lot.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LOT_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_LOT_D_NO"/></TD>
							<TD><INPUT type="text" class="disabled" name="idLot" readonly value="<%=viewBean.getIdLot()%>"></TD>
							<TD><ct:FWLabel key="JSP_LOT_D_ETAT"/></TD>
							<TD><INPUT name="csEtatLotLibelle" type="text" value="<%=viewBean.getCsEtatLotLibelle()%>" class="disabled" readonly></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_LOT_D_DATE_CREATION"/></TD>
							<TD><INPUT type="text" class="disabled" name="dateCreationLot" readonly value="<%=viewBean.getDateCreationLot()%>"></TD>
							<TD><ct:FWLabel key="JSP_LOT_D_DATE_COMPTABILISATION"/></TD>
							<TD><INPUT type="text" class="disabled" name="dateEnvoiLot" readonly  value="<%=viewBean.getDateEnvoiLot()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_LOT_D_DESCRIPTION"/></TD>
							<TD><INPUT type="text" name="description" value="<%=viewBean.getDescription()%>" class="libelleLong"> </TD>
							<TD><ct:FWLabel key="JSP_LOT_D_TYPE"/></TD>
							<TD><INPUT type="text" name="csTypeLotLibelle" value="<%=viewBean.getCsTypeLotLibelle()%>" class="disabled" readonly></TD>
						</TR>
						<%if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdJournalCA())) {%>
						<TR>
							<td colspan="4">
								<A href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.apercuJournal.afficher&selectedId=<%=viewBean.getIdJournalCA()%>" class="external_link">
									<ct:FWLabel key="JSP_LOT_D_JOURNAL_ASSOCIE_CA"/>
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