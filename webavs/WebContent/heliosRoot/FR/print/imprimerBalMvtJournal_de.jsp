
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ page import="globaz.helios.db.comptes.*,globaz.helios.db.print.*,globaz.helios.db.interfaces.*,globaz.globall.db.*,globaz.helios.translation.*" %>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran="GCF2003";
	//Récupération du viewBean
	CGImprimerBalMvtJournalViewBean viewBean = (CGImprimerBalMvtJournalViewBean) session.getAttribute ("viewBean");
 
	//Récupération de l'exercice comptable
	CGExerciceComptable exerciceComptable = (CGExerciceComptable) session.getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

	//Label utilisé pour spécifier à l'utilisateur qu'aucune option n'est sélectionnée.
	String labelAucun = "Aucun";
	if (languePage.equalsIgnoreCase("de")) {
		labelAucun = "Kein";
	}

	userActionValue = "helios.print.imprimerBalMvtJournal.executer";

	String toutLexercice = "Tout l'exercice";
	if (languePage.equalsIgnoreCase("de")) {
		toutLexercice = "Ganze Rechnungsjahr";
	}
	
	String pdfChecked = "pdf".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "";
	String xlsChecked = "xls".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<ct:menuChange displayId="menu" menuId="CG-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CG-OnlyDetail"/>

<%
	globaz.framework.menu.FWMenuBlackBox bb = (globaz.framework.menu.FWMenuBlackBox) session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_USER_MENU);
	bb.setNodeOpen(false, "parameters", "CG-MenuPrincipal");
%>

<script>
function init() { } 
function onOk() {
	document.forms[0].submit();
} 
function onCancel() {
	document.forms[0].elements('userAction').value="back";
//	document.forms[0].submit();
}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%> 


	Imprimer la balance des mouvements par journal
      <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<tr>
			<td>Mandat</td>
			<td> <input name='libelle' class='libelleLongDisabled' readonly value='<%=exerciceComptable.getMandat().getLibelle()%>'> <input type="hidden" name="idExerciceComptable" value="<%=exerciceComptable.getIdExerciceComptable()%>"></td>
		</tr>
		<tr>
			<td>Exercice</td>
			<td> <input name='libelle' class='libelleLongDisabled' readonly value='<%=exerciceComptable.getFullDescription()%>'> 
			     <input name='idMandat' type="hidden" value='<%=exerciceComptable.getIdMandat()%>'></td>
		</tr>

		<tr>
			<td>Adresse E-Mail</td>
			<td> <input name='eMailAddress' class='libelleLong' value='<%=viewBean.getEMailAddress()==null?"":viewBean.getEMailAddress()%>'> * </td>
		</tr>
		<TR>
			<td><ct:FWLabel key="TYPE_IMPRESSION"/></td>
	  		<TD>
	   			<input type="radio" class="typeImp" id="radPdf" name="typeImpression" value="pdf" <%=pdfChecked%>/>PDF&nbsp;
	   			<input type="radio" class="typeImp" name="typeImpression" value="xls" <%=xlsChecked%>/>Excel
	   		</TD>
	    </TR> 
    			  
			  <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>
