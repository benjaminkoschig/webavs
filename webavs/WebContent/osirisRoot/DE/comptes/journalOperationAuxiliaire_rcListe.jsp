
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
CAAuxiliaireManagerListViewBean viewBean = (CAAuxiliaireManagerListViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
size = viewBean.size();
detailLink ="osiris?userAction=osiris.comptes.journalOperationAuxiliaire.afficher&selectedId=";

CAAuxiliaire operationAuxiliaire = null;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH colspan="2" width="80">Valutadatum</TH>
    <TH nowrap>Abrechnungskonto</TH>
    <TH width="226">Sektion</TH>
    <TH width="115">Betrag</TH>
    <TH width="10" align="right">S</TH>
    <TH width="125" align="right">Typ</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
		CAAuxiliaire temp = (globaz.osiris.db.comptes.CAAuxiliaire) viewBean.getEntity(i);

		if (temp instanceof CAAuxiliairePaiement) {
			operationAuxiliaire = (CAAuxiliairePaiement) temp;
		} else {
			operationAuxiliaire = (CAAuxiliaire) temp;
		}

    	String noAVS = "";
		if (operationAuxiliaire.getCompteAnnexe() != null){
      		noAVS =operationAuxiliaire.getCompteAnnexe().getIdExterneRole();
      	}
    	actionDetail = "parent.location.href='"+detailLink+operationAuxiliaire.getIdOperation()+"&noAVS="+noAVS+"'";
    %>

<%
	String styleAndTitle = "";
	if (operationAuxiliaire.getEtat().equals(globaz.osiris.api.APIOperation.ETAT_PROVISOIRE)) {
		styleAndTitle = " style=\"color:#FF9933;\"";
	} else if (operationAuxiliaire.getEtat().equals(globaz.osiris.api.APIOperation.ETAT_ERREUR)) {
		styleAndTitle = " style=\"color:#FF0000;\"";
	} else if ((operationAuxiliaire.getEtat().equals(globaz.osiris.api.APIOperation.ETAT_OUVERT)) && ((operationAuxiliaire.getJournal().getEtat().equals(CAJournal.PARTIEL)) || (operationAuxiliaire.getJournal().getEtat().equals(CAJournal.ERREUR)))) {
		styleAndTitle = " style=\"color:#FF0000;\"";
	}

	if (!globaz.jade.client.util.JadeStringUtil.isBlank(styleAndTitle)) {
		styleAndTitle += " title=\"Status '" + operationAuxiliaire.getUcEtat().getLibelle() + "'\"";
	}
%>

	<TD class="mtd" width="16" <%=styleAndTitle%>>
	<% String tmp = (detailLink+operationAuxiliaire.getIdOperation()+"&noAVS="+noAVS); %>
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
	</TD>

    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="70" valign="top" <%=styleAndTitle%>><%=operationAuxiliaire.getDate()%></TD>

    <TD class="mtd" nowrap onClick="<%=actionDetail%>" <%=styleAndTitle%>>
		<%if (operationAuxiliaire.getCompteAnnexe() != null) {%>
			<%=operationAuxiliaire.getCompteAnnexe().getCARole().getDescription()%>&nbsp;<%=operationAuxiliaire.getCompteAnnexe().getIdExterneRole()%>
      	<br/>
      		<%=operationAuxiliaire.getCompteAnnexe().getDescription()%>
      	<%}%>
    </TD>

    <TD class="mtd" nowrap onClick="<%=actionDetail%>" valign="top" align="left" <%=styleAndTitle%>><%=operationAuxiliaire.getSection().getFullDescription()%></td>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" valign="top" align="right" <%=styleAndTitle%>><%=JANumberFormatter.formatNoRound(operationAuxiliaire.getMontantEcran())%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" valign="top" align="right" <%=styleAndTitle%>><%=operationAuxiliaire.getCodeDebitCreditEcran()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" valign="top" align="right" <%=styleAndTitle%>><%=operationAuxiliaire.getIdTypeOperation()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>