<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CGE0004";
	GEEtudiantsAjoutViewBean viewBean = (GEEtudiantsAjoutViewBean)session.getAttribute ("viewBean");
%>
<%@page import="globaz.globall.util.JAUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="globaz.campus.vb.etudiants.GEEtudiantsAjoutViewBean"%>
<SCRIPT language="JavaScript">
</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">

function add() {
	document.forms[0].elements('userAction').value="campus.etudiants.etudiants.ajouter";
}

function upd() {
}

function validate() {
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="campus.etudiants.etudiants.ajouter";
    else
        document.forms[0].elements('userAction').value="campus.etudiants.etudiants.modifier";
	return (true);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="campus.etudiants.etudiants.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer le détail d'assurance sélectionné! Voulez-vous continuer?")) {
		document.forms[0].elements('userAction').value="campus.etudiants.etudiants.supprimer";
		document.forms[0].submit();
	}
}

function init() {}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Détail d'un étudiant
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD width="150">N° d'immatriculation</TD>
							<TD><input name="numImmatriculation" class="libelleCourt" value='<%=viewBean.getNumImmatriculation()!=null?viewBean.getNumImmatriculation():""%>'/></TD>					
							<%if (!JadeStringUtil.isBlankOrZero(viewBean.getIdTiersEtudiant())){ %>
							<TD>
							<A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=viewBean.getIdTiersEtudiant()%>" class="external_link">Tiers</A>
							</TD>
							<%}%>
						</TR>
						<TR>
						<TD>&nbsp;</TD>
						</TR>
						<TR>
							<TD width="150" valign="top">Etudiant</TD>
							<TD width="350">
							<textarea name='etudiant' readonly class='libelleLongDisabled'
							rows='6'><%=GEEtudiantsAjoutViewBean.getEtudiantDescription(viewBean.getIdTiersEtudiant(), session)%></textarea>
							<%
							Object[] etudiantMethodsName = new Object[]{
								new String[]{"setIdTiersEtudiant","getIdTiers"},
							};
							Object[]  etudiantParams = new Object[]{
								new String[]{"etudiantDescription","_pos"},
							};
							String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+ globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/etudiants/etudiants_de.jsp";
							%>
				            <ct:FWSelectorTag 
								name="etudiantSelector"
								methods="<%=etudiantMethodsName%>"
								providerApplication ="pyxis"
								providerPrefix="TI"
								providerAction ="pyxis.tiers.tiers.chercher"
								providerActionParams ="<%=etudiantParams%>"
								redirectUrl="<%=redirectUrl%>"
							/>
							<input type="hidden" name="selectorName" value="">
							<input name="idTiersEtudiant" type="hidden" value='<%=viewBean.getIdTiersEtudiant()!=null?viewBean.getIdTiersEtudiant():""%>'/>
							</TD>
							
							<TD width="150" valign="top">Ecole</TD>
							<TD width="350">
							<textarea name='ecole' readonly class='libelleLongDisabled'
							rows='6'><%=GEEtudiantsAjoutViewBean.getEcoleDescription(viewBean.getIdTiersEcole(), session)%></textarea>
							<%
							Object[] ecoleMethodsName = new Object[]{
								new String[]{"setIdTiersEcole","getIdTiersAdministration"},
							};
							Object[]  ecoleParams = new Object[]{
								new String[]{"ecoleDescription","_pos"},
							};
							%>
				            <ct:FWSelectorTag 
								name="ecoleSelector"
								methods="<%=ecoleMethodsName%>"
								providerApplication ="pyxis"
								providerPrefix="TI"
								providerAction ="pyxis.tiers.administration.chercher&selGenre='509036'&_pos=''"
								providerActionParams ="<%=ecoleParams%>"
								redirectUrl="<%=redirectUrl%>"
							/>
							<input type="hidden" name="selectorName" value="">
							<input name="idTiersEcole" type="hidden" value='<%=viewBean.getIdTiersEcole()!=null?viewBean.getIdTiersEcole():""%>'/>
							<INPUT type="hidden" name="selectedId" value="<%=viewBean.getIdEtudiant()%>">
							</TD>
						</TR>		
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<% if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> 
<SCRIPT>
</SCRIPT> 
<% } %> 
	<ct:menuChange displayId="menu" menuId="GEMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="GEMenuVide" showTab="options">
		<ct:menuSetAllParams key="idEtudiant" value="<%=viewBean.getIdEtudiant()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>