<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PIJ0019";
	globaz.ij.vb.lots.IJLotViewBean viewBean = (globaz.ij.vb.lots.IJLotViewBean) session.getAttribute("viewBean");
	boolean hasRight = viewBean.getSession().hasRight("ij.lots.lot", FWSecureConstants.UPDATE);
	selectedIdValue = viewBean.getIdLot();
	bButtonDelete = hasRight && !viewBean.isValide();
	bButtonUpdate = hasRight && !viewBean.isValide();
	
	//bButtonNew=true;
	//bButtonDelete=true;
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal"/>
<ct:menuChange displayId="options" menuId="ij-optionlot" showTab="options">
	<ct:menuSetAllParams key="forIdLot" value="<%=viewBean.getIdLot()%>"/>
    <ct:menuSetAllParams key="etatLot" value="<%=viewBean.getCsEtat()%>"/>
   	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdLot()%>"/>
    <ct:menuSetAllParams key="idLot" value="<%=viewBean.getIdLot()%>"/>
    
	<% if (viewBean.getCsEtat().equals(globaz.ij.api.lots.IIJLot.CS_VALIDE)) {%>
		<ct:menuActivateNode active="no" nodeId="generercompensations"/>
	<%} else {%>
		<ct:menuActivateNode active="yes" nodeId="generercompensations"/>
	<%}%>
    
</ct:menuChange>

<SCRIPT language="javascript"> 

	function add() {
	    document.forms[0].elements('userAction').value="ij.lots.lot.ajouter";
	}
	
	function upd() {
	}
	
	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add")
	        document.forms[0].elements('userAction').value="ij.lots.lot.ajouter";
	    else
	        document.forms[0].elements('userAction').value="ij.lots.lot.modifier";
	    
	    return state;
	
	}
	
	function cancel() {
	
	if (document.forms[0].elements('_method').value == "add")
	  document.forms[0].elements('userAction').value="back";
	 else
	  document.forms[0].elements('userAction').value="ij.lots.lot.chercher";
	  
	}
	
	function del() {
		if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="ij.lots.lot.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
	
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LOTS"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
						<TD><ct:FWLabel key="JSP_NUMERO"/></TD>
						<TD><INPUT type="text" class="disabled" name="noLot" readonly value="<%=viewBean.getNoLot()%>"></TD>
						<TD><ct:FWLabel key="JSP_ETAT"/></TD>
						<TD><INPUT type="text" value="<%=viewBean.getEtatLotLibelle()%>" class="disabled" readonly></TD>
						</TR>
						<TR>
						<TD><ct:FWLabel key="JSP_DATE_CREATION"/></TD>
						<TD><INPUT type="text" name="dateCreation" readonly class="disabled" value="<%=viewBean.getDateCreation()%>"></TD>
						<TD><ct:FWLabel key="JSP_DATE_ENVOI"/></TD>
						<TD><ct:FWCalendarTag name="dateComptable" value="<%=viewBean.getDateComptable()%>"/></TD>
						</TR>
						<TR>
						<TD><ct:FWLabel key="JSP_DESCRIPTION"/></TD>
						<TD><INPUT type="text" name="description" value="<%=viewBean.getDescription()%>" size="40" maxlength="40"> </TD>
						</TR>
						<%if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdJournalCA())) {%>
						<TR>
							<td>
								<ct:ifhasright element="osiris.comptes.apercuJournal.afficher" crud="r">
									<A href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.apercuJournal.afficher&selectedId=<%=viewBean.getIdJournalCA()%>" class="external_link">
										<ct:FWLabel key="JSP_JOURNAL_ASSOCIE_CA"/>
									</A>
								</ct:ifhasright>
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