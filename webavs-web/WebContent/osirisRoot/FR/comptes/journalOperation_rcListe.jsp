 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
  <%@ page import="globaz.globall.util.*" %>
  <%@ page import="java.util.Enumeration" %>
  <%@ page import="globaz.framework.util.*" %>
  <%@ page import="globaz.osiris.db.comptes.*"%>
  <%
CAOperationManagerListViewBean viewBean = (CAOperationManagerListViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
size = viewBean.size();
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <TH width="80" colspan="2">Date</TH>
    <TH nowrap>Compte annexe</TH>
    <TH width="226">Section</TH>
    <TH width="115">Montant</TH>
    <TH width="10" align="right">S</TH>
    <TH width="125" align="right">Type</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> 
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%	
		CAOperation operation = (globaz.osiris.db.comptes.CAOperation) viewBean.getEntity(i); 
		
    	String noAVS = "";
		if (operation.getCompteAnnexe() != null){
      		noAVS =operation.getCompteAnnexe().getIdExterneRole();
      	}      	      	      	
      	
      	if (operation.getIdTypeOperation().startsWith(globaz.osiris.api.APIOperation.CAAUXILIAIRE)) {
	      	detailLink ="osiris?userAction=osiris.comptes.journalOperationAuxiliaire.afficher&selectedId=";
      	} else if (operation.getIdTypeOperation().startsWith(globaz.osiris.api.APIOperation.CAPAIEMENTETRANGER)) {
	      	detailLink ="osiris?userAction=osiris.comptes.journalOperationPaiementEtranger.afficher&selectedId=";
      	} else if (operation.getIdTypeOperation().startsWith(globaz.osiris.api.APIOperation.CAECRITURE)) {
      		if (operation.getIdTypeOperation().equals(globaz.osiris.api.APIOperation.CAPAIEMENT)) {
	      		detailLink ="osiris?userAction=osiris.comptes.journalOperationPaiement.afficher&selectedId=";
      		} else if (operation.getIdTypeOperation().equals(globaz.osiris.api.APIOperation.CAPAIEMENTETRANGER)) {
	      		detailLink ="osiris?userAction=osiris.comptes.journalOperationPaiementEtranger.afficher&selectedId=";
      		} else if (operation.getIdTypeOperation().startsWith(globaz.osiris.api.APIOperation.CAVERSEMENT)) {
	      		detailLink ="osiris?userAction=osiris.comptes.journalOperationVersement.afficher&selectedId=";
      		} else if (operation.getIdTypeOperation().startsWith(globaz.osiris.api.APIOperation.CARECOUVREMENT)) {
	      		detailLink ="osiris?userAction=osiris.comptes.journalOperationRecouvrement.afficher&selectedId=";	      		
      		} else {
	      		detailLink ="osiris?userAction=osiris.comptes.journalOperationEcriture.afficher&selectedId=";
      		}
      	} else if (operation.getIdTypeOperation().startsWith(globaz.osiris.api.APIOperation.CAOPERATIONORDREVERSEMENT)) {
	      	detailLink ="osiris?userAction=osiris.comptes.journalOperationOrdreVersement.afficher&selectedId=";
      	} else if (operation.getIdTypeOperation().startsWith(globaz.osiris.api.APIOperation.CAOPERATIONORDRERECOUVREMENT)) {
	      	detailLink ="osiris?userAction=osiris.comptes.journalOperationOrdreRecouvrement.afficher&selectedId=";
      	} else {
      		detailLink = null;
      	}

    	actionDetail = "parent.location.href='"+detailLink+operation.getIdOperation()+"&noAVS="+noAVS+"'";
    %>
    
<%
	String styleAndTitle = "";
	if (operation.getEtat().equals(globaz.osiris.api.APIOperation.ETAT_PROVISOIRE)) {
		styleAndTitle = " style=\"color:#FF9933;\"";
	} else if (operation.getEtat().equals(globaz.osiris.api.APIOperation.ETAT_ERREUR)) {
		styleAndTitle = " style=\"color:#FF0000;\"";
	} else if ((operation.getEtat().equals(globaz.osiris.api.APIOperation.ETAT_OUVERT)) && ((operation.getJournal().getEtat().equals(CAJournal.PARTIEL)) || (operation.getJournal().getEtat().equals(CAJournal.ERREUR)))) {
		styleAndTitle = " style=\"color:#FF0000;\"";
	} 
	
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(styleAndTitle)) {
		styleAndTitle += " title=\"Etat '" + operation.getUcEtat().getLibelle() + "'\"";
	}
%>    

	<TD class="mtd" width="16" <%=styleAndTitle%>>
	<% String tmp = (detailLink+operation.getIdOperation()+"&noAVS="+noAVS); %>
	<% if (detailLink != null) {%>
    <ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
    <% } %>
    </TD>
    
    <TD class="mtd" nowrap <%if (detailLink != null) {%>onClick="<%=actionDetail%>"<%}%> width="70" valign="top" <%=styleAndTitle%>><%=operation.getDate()%></TD>
    
    <TD class="mtd" nowrap <%if (detailLink != null) {%>onClick="<%=actionDetail%>"<%}%> <%=styleAndTitle%>> 
		<%if (operation.getCompteAnnexe() != null) {%>
			<%=operation.getCompteAnnexe().getCARole().getDescription()%>&nbsp;<%=operation.getCompteAnnexe().getIdExterneRole()%> 
      	<br/>
      		<%=operation.getCompteAnnexe().getDescription()%> 
      	<%}%>
    </TD>
    
    <TD class="mtd" nowrap <%if (detailLink != null) {%>onClick="<%=actionDetail%>"<%}%> valign="top" align="left" <%=styleAndTitle%>>
    	<%if (operation.getSection() != null) {%>
    		<%=operation.getSection().getFullDescription()%>
    	<%}%>    
    </td>
    <TD class="mtd" nowrap <%if (detailLink != null) {%>onClick="<%=actionDetail%>"<%}%> valign="top" align="right" <%=styleAndTitle%>>
    <%
    if (operation.getIdTypeOperation().startsWith(globaz.osiris.api.APIOperation.CAAUXILIAIRE)) {
    	out.print(JANumberFormatter.formatNoRound(((CAAuxiliaire) operation.getOperationFromType(null)).getMontantEcran()));
    } else if (operation.getIdTypeOperation().startsWith(globaz.osiris.api.APIOperation.CAPAIEMENTETRANGER)) {
    	out.print(JANumberFormatter.formatNoRound(((CAPaiementEtranger) operation.getOperationFromType(null)).getMontantEcran()));
    } else if (operation.getIdTypeOperation().startsWith(globaz.osiris.api.APIOperation.CAECRITURE)) {
    	out.print(JANumberFormatter.formatNoRound(((CAEcriture) operation.getOperationFromType(null)).getMontantEcran()));
    } else {
    	out.print(JANumberFormatter.formatNoRound(operation.getMontant()));
    }
    %>    
    </TD>
    <TD class="mtd" nowrap <%if (detailLink != null) {%>onClick="<%=actionDetail%>"<%}%> valign="top" align="right" <%=styleAndTitle%>>
    
    <%
    if (operation.getIdTypeOperation().startsWith(globaz.osiris.api.APIOperation.CAAUXILIAIRE)) {
    	out.print(((CAAuxiliaire) operation.getOperationFromType(null)).getCodeDebitCreditEcran());
    } else if (operation.getIdTypeOperation().startsWith(globaz.osiris.api.APIOperation.CAPAIEMENTETRANGER)) {
    	out.print(((CAPaiementEtranger) operation.getOperationFromType(null)).getCodeDebitCreditEcran());
    } else if (operation.getIdTypeOperation().startsWith(globaz.osiris.api.APIOperation.CAECRITURE)) {
    	out.print(((CAEcriture) operation.getOperationFromType(null)).getCodeDebitCreditEcran());
    } 
    %>
    </TD>
    <TD class="mtd" nowrap <%if (detailLink != null) {%>onClick="<%=actionDetail%>"<%}%> valign="top" align="right" <%=styleAndTitle%>><%=operation.getIdTypeOperation()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>