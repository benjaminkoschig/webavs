<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="java.math.*" %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
targetLocation = "location.href";
CAOdresVersementListViewBean viewBean = (CAOdresVersementListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
detailLink ="osiris?userAction=osiris.comptes.journalOperationOrdreVersement.afficher&selectedId=";
String detailLinkRec ="osiris?userAction=osiris.comptes.journalOperationOrdreRecouvrement.afficher&selectedId=";
size = viewBean.size();

String idCompteAnnexe = request.getParameter("forIdCompteAnnexe");
%>

	<ct:menuChange displayId="options" menuId="CA-ApercuParSectionDossier" showTab="options">
		<ct:menuSetAllParams key="id" value="<%=idCompteAnnexe%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=idCompteAnnexe%>"/>
		<ct:menuSetAllParams key="idCompteAnnexe" value="<%=idCompteAnnexe%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<th colspan="2">Datum</th>
	<th>Endbegünstigter</th>
    <th>Zahlungsadresse</th>
    <th>Sektion</th>
    <th width="100">Betrag</th>
    <th>Gesperrt</th>
    <th>Status</th>
    <th>Typ</th>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
globaz.osiris.db.comptes.CAOperationOrdreVersement operation = (globaz.osiris.db.comptes.CAOperationOrdreVersement) viewBean.getEntity(i);
if (operation.getIdTypeOperation().equalsIgnoreCase(globaz.osiris.db.comptes.CAOperation.CAOPERATIONORDRERECOUVREMENT)){
	actionDetail = "parent.location.href='"+detailLinkRec+operation.getIdOperation()+"'";
} else {
	actionDetail = "parent.location.href='"+detailLink+operation.getIdOperation()+"'";	
}


%>

<td class="mtd" width="16">
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+operation.getIdOperation())%>"/>		
</td>
<td class="mtd" style="text-align:center" onClick="<%=actionDetail%>"><%=operation.getDate()%></td>

<%if(operation.getNomCache().length() > 0){%>
	<td class="mtd" onClick="<%=actionDetail%>"><%=operation.getNomCache()%></td>
<%}else{%>
	<td class="mtd" onClick="<%=actionDetail%>"><%=operation.getNomPrenom()%></td>
<%}%>

<%if(operation.getAdressePaiementFormatter().getAdressePaiement() != null){%>
	<td class="mtd" onClick="<%=actionDetail%>"><%=operation.getAdressePaiementFormatter().getShortAddress()%></td>
<%}else{%>
	<td class="mtd" onClick="<%=actionDetail%>"><%=operation.getAdresseCourrier()%></td>
<%}%>

<td class="mtd" onClick="<%=actionDetail%>"><%=operation.getSection().getFullDescription()%></td>
<td class="mtd" onClick="<%=actionDetail%>" align="right"><%=operation.getMontantToCurrency().toStringFormat()%></td>
     
<td class="mtd" onClick="<%=actionDetail%>" align="center">
<% if (operation.getEstBloque().booleanValue()) { 
	if (operation.getLog() != null) { 
		globaz.framework.util.FWMessage msg = operation.getLog().getHighestMessage();%>
	<IMG src="<%=request.getContextPath()%>/images/erreur.gif" title="<%=msg.getMessageText()%>" />
	<% } else { %>
	<IMG src="<%=request.getContextPath()%>/images/erreur.gif" />
	<% } %>	
<% } else { %>
&nbsp;
<% } %> 
</td>
   
<%
String image = "";
if (!operation.getEtat().equals(globaz.osiris.api.APIOperation.ETAT_OUVERT)) {
	if (operation.getEtat().equals(globaz.osiris.api.APIOperation.ETAT_COMPTABILISE)) {
		image = "<img src=\"" + request.getContextPath()+"/images/information.gif\" title=\"" + operation.getUcEtat().getLibelle() + "\">";
	} else if (operation.getEtat().equals(globaz.osiris.api.APIOperation.ETAT_VERSE)) {
		image = "<img src=\"" + request.getContextPath()+"/images/envoye.gif\" title=\"" + operation.getUcEtat().getLibelle() + "\">";
	} else if (operation.getEtat().equals(globaz.osiris.api.APIOperation.ETAT_PROVISOIRE)) {
		image = "<img src=\"" + request.getContextPath()+"/images/avertissement.gif\" title=\"" + operation.getUcEtat().getLibelle() + "\">";
	} else if (operation.getEtat().equals(globaz.osiris.api.APIOperation.ETAT_ERREUR) || operation.getEtat().equals(globaz.osiris.api.APIOperation.ETAT_ERREUR_VERSEMENT)) {
		image = "<img src=\"" + request.getContextPath()+"/images/erreur2.gif\" title=\"" + operation.getUcEtat().getLibelle() + "\">";
	}
}
%>
<td class="mtd" onClick="<%=actionDetail%>" align="center"><%=image%>&nbsp;</td>
<td class="mtd" onClick="<%=actionDetail%>" align="center"><%=operation.getIdTypeOperation()%>&nbsp;</td>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>