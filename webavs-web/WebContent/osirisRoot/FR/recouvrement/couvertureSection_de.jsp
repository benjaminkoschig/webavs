<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.globall.util.*,globaz.osiris.db.recouvrement.*"%> 
<%
idEcran="GCA60002";
	CACouvertureSectionViewBean viewBean = (CACouvertureSectionViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdCouvertureSection();
	
	

	bButtonUpdate = !viewBean.isNew() && (viewBean.isSectionEditable() || viewBean.isNumeroOrdreEditable()) && objSession.hasRight(userActionUpd, "UPDATE");
	bButtonDelete = !viewBean.isNew() && objSession.hasRight(userActionDel, "REMOVE");

	String compteAnnexeIdRoleLibelle = "";
	String compteAnnexeTiersNom = "";
	String sectionCategorieSectionLibelle = "";

	try {
		compteAnnexeIdRoleLibelle = viewBean.getSession().getCodeLibelle(viewBean.getCompteAnnexeIdRole());
		compteAnnexeTiersNom = viewBean.getSection().getCompteAnnexe().getTiers().getNom();
		sectionCategorieSectionLibelle = viewBean.getSession().getCodeLibelle(viewBean.getSectionCategorieSection());
	} catch (Exception e) {
	}

	tableHeight = 230;

	String idPlanRecouvrement = viewBean.getIdPlanRecouvrement();
	if (idPlanRecouvrement == null || idPlanRecouvrement.length() == 0) {
		idPlanRecouvrement = (String) session.getAttribute("idPlanRecouvrement");
		viewBean.setIdPlanRecouvrement(idPlanRecouvrement);
	}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="JavaScript">

	function add() {
		document.forms[0].elements('userAction').value="osiris.recouvrement.couvertureSection.ajouter"
	}

	function upd() {
	}

	function validate() {
		state = validateFields();
		if (document.forms[0].elements('_method').value == "add")
			document.forms[0].elements('userAction').value="osiris.recouvrement.couvertureSection.ajouter";
		else
			document.forms[0].elements('userAction').value="osiris.recouvrement.couvertureSection.modifier";
		return state;
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add")
			document.forms[0].elements('userAction').value="back";
		else
			document.forms[0].elements('userAction').value="osiris.recouvrement.couvertureSection.afficher";
	}

	function del() {
		if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
			document.forms[0].elements('userAction').value="osiris.recouvrement.couvertureSection.supprimer";
			document.forms[0].submit();
		}
	}

	function init(){
	}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail de la section couverte<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					<INPUT type="hidden" name="idPlanRecouvrement" value="<%=viewBean.getIdPlanRecouvrement() %>">
					<INPUT type="hidden" name="idCompteAnnexe" value="<%=viewBean.getCompteAnnexe().getIdCompteAnnexe() %>">

					<TR>
						<TD>
							<TABLE>
								<TR>
									<TD class="label">Compte annexe</TD>
									<TD class="control">
										<INPUT type="text" value="<%=viewBean.getCompteAnnexe().getIdExterneRole() +" - "+viewBean.getCompteAnnexe().getRole().getDescription()+" - "+viewBean.getCompteAnnexe().getDescription() %>" size="<%=(viewBean.getCompteAnnexe().getIdExterneRole() +" - "+viewBean.getCompteAnnexe().getRole().getDescription()+" - "+viewBean.getCompteAnnexe().getDescription()).length()+20 %>" class=disabled readonly>
									</TD>
								</TR>
								<TR>
									<TD class="label">Section</TD>
									<TD class="control">										
										<% if ( JadeStringUtil.equals(request.getParameter("_method"),"add",false) ) { %>
											<ct:FWListSelectTag data="<%=viewBean.getSectionsPossibles()%>"
													name="idSection" defaut="<%=viewBean.getIdSection()%>" /> <%} else { %>
											<INPUT type="text" value="<%=(viewBean.getSection()!= null)?viewBean.getSection().getIdExterne() + " - " + viewBean.getSection().getDescription():" "%>"
													size="<%=(viewBean.getCompteAnnexe().getIdExterneRole() +" - "+viewBean.getCompteAnnexe().getRole().getDescription()+" - "+viewBean.getCompteAnnexe().getDescription()).length()+20 %>"
											class=disabled readonly> <%} %>							
										
									</TD>
								</TR>			
								<TR>
									<TD class="label">N°Ordre</TD>
									<TD class="control">
										<input type="text" name="numeroOrdre" value="<%=viewBean.getNumeroOrdre()%>">
									</TD>
								</TR>													
								</table>
						</TD>
					</TR>
									
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>