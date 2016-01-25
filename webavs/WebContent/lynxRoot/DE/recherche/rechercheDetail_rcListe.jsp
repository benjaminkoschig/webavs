<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
LXRechercheDetailListViewBean viewBean = (LXRechercheDetailListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

LXRechercheDetailViewBean operation = null;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>

<%@page import="globaz.lynx.db.recherche.LXRechercheDetailViewBean"%>
<%@page import="globaz.lynx.db.recherche.LXRechercheDetailListViewBean"%>
<%@page import="globaz.lynx.db.operation.LXOperation"%>
	
	<TH width="30">&nbsp;</TH>
	<TH width="100">Datum</TH>
	<TH width="100">Fälligkeit</TH>
	<TH>Bezeichnung</TH>
	<TH width="150">Lieferant Rechnung-Nr.</TH>
	<TH width="150">Typ</TH>
	<TH width="100">Betrag</TH>
	<TH width="15">Gesperrt</TH>
	
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

    <%
    operation = (LXRechercheDetailViewBean) viewBean.getEntity(i);
    
    if(operation.getCsTypeOperation().equals(LXOperation.CS_TYPE_ESCOMPTE)) {
    	detailLink ="lynx?userAction=lynx.escompte.escompte.afficher&idOperation="+operation.getIdOperation()+"&idJournal="+operation.getIdJournal();
    	
    }else if(operation.getCsTypeOperation().equals(LXOperation.CS_TYPE_NOTEDECREDIT_DEBASE)
    		|| operation.getCsTypeOperation().equals(LXOperation.CS_TYPE_NOTEDECREDIT_ENCAISSEE)){
    	
    	detailLink ="lynx?userAction=lynx.notedecredit.noteDeCredit.afficher&idOperation="+operation.getIdOperation();
    	
    }else if(operation.getCsTypeOperation().equals(LXOperation.CS_TYPE_NOTEDECREDIT_LIEE)) {    	
      	detailLink = "lynx?userAction=lynx.recherche.rechercheDetail.chercherDetail&forEtat="+ request.getParameter("forEtat") +"&forIdSection="+operation.getIdSectionLiee();
      	
    }else if(operation.getCsTypeOperation().equals(LXOperation.CS_TYPE_FACTURE_BVR_ORANGE) 
    		|| operation.getCsTypeOperation().equals(LXOperation.CS_TYPE_FACTURE_CAISSE)
    		|| operation.getCsTypeOperation().equals(LXOperation.CS_TYPE_FACTURE_BVR_ROUGE)
    		|| operation.getCsTypeOperation().equals(LXOperation.CS_TYPE_FACTURE_VIREMENT)
    		|| operation.getCsTypeOperation().equals(LXOperation.CS_TYPE_FACTURE_LSV)) {
		detailLink ="lynx?userAction=lynx.facture.facture.afficher&idOperation="+operation.getIdOperation();
		
    } else if(operation.getCsTypeOperation().equals(LXOperation.CS_TYPE_PAIEMENT_BVR_ORANGE) 
    	    		|| operation.getCsTypeOperation().equals(LXOperation.CS_TYPE_PAIEMENT_CAISSE)
    	    		|| operation.getCsTypeOperation().equals(LXOperation.CS_TYPE_PAIEMENT_BVR_ROUGE)
    	    		|| operation.getCsTypeOperation().equals(LXOperation.CS_TYPE_PAIEMENT_VIREMENT)
    	    		|| operation.getCsTypeOperation	().equals(LXOperation.CS_TYPE_PAIEMENT_LSV)) {
		detailLink ="lynx?userAction=lynx.paiement.paiement.afficher&idOperation="+operation.getIdOperation()+"&idJournal="+operation.getIdJournal();
		
    } else if(operation.getCsTypeOperation().equals(LXOperation.CS_TYPE_EXTOURNE)) {
    			detailLink ="lynx?userAction=lynx.extourne.extourne.afficher&idOperation="+operation.getIdOperation();
    			
    }  
    
    actionDetail = "parent.location.href='"+detailLink+"'";    

    String styleColor = "";
    if(LXOperation.CS_ETAT_OUVERT.equals(operation.getCsEtatOperation())) {
    	styleColor = "style=\"color:blue\"";
    } else if(LXOperation.CS_ETAT_TRAITEMENT.equals(operation.getCsEtatOperation())) {
    	styleColor = "style=\"color:#FF9933\"";
    }
    
    String bloque = "";
    if(operation.getEstBloque().booleanValue()) {
    	bloque = "<img title=\"Bloqué\" src=\"" + request.getContextPath()+"/images/cadenas.gif\"";
    }
    %>

    <td class="mtd" width="16">
    	<ct:menuPopup menu="LX-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=detailLink%>"/>
    </td>
    <td class="mtd" onClick="<%=actionDetail%>" align="center" <%= styleColor %>><%=operation.getDateOperation()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" align="center" <%= styleColor %>><%=operation.getDateEcheance()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" <%= styleColor %>><%=operation.getLibelle()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" align="center" <%= styleColor %>><%=operation.getReferenceExterne()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" <%= styleColor %>><%=operation.getUcType(null).getLibelle()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" align="right" <%= styleColor %>><%=operation.getMontant()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" align="center" <%= styleColor %>><%=bloque%>&nbsp;</td>
    
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>