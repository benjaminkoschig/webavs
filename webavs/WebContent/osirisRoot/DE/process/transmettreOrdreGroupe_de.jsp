
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.ordres.*" %>
<%
	idEcran = "GCA3011";
	globaz.osiris.db.process.CATransmettreOrdreGroupeViewBean viewBean = (globaz.osiris.db.process.CATransmettreOrdreGroupeViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
	userActionValue = "osiris.process.transmettreOrdreGroupe.executer";
	selectedIdValue = viewBean.getIdOrdreGroupe();
	String impJournalChecked = !viewBean.getImprimerJournal() ? "checked='checked'" : "";
	String pdfChecked = "pdf".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "";
	String xlsChecked = "xls".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "";
	String libLong = viewBean.getOrdreGroupe() != null ? viewBean.getOrdreGroupe().getOrdreGroupeLong() : "";
%>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">
function validate() {
	document.getElementById("btnOk").disabled = true;
	return true;
}

top.document.title = "Process - <ct:FWLabel key='GCA3011_TITRE_ECRAN'/> - " + top.location.href;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="GCA3011_TITRE_ECRAN"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

    <TR>
      	<TD><ct:FWLabel key="GCA3011_DESC_OG"/></TD>
      	<TD>
        	<INPUT type="hidden" name="comptabiliserOrdre" value="on">
        	<INPUT type="hidden" name="genererFichierEchange" value="on">
        	<input type="text" name="_libelleLong" value="<%=libLong%>" maxlength="80" class="libelleLongDisabled" readonly="readonly" tabindex="-1">
      	</TD>
    </TR>
    <TR>
     	<TD><ct:FWLabel key="EMAIL"/></TD>
      	<TD><input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong" tabindex="1"></TD>
    </TR>
    <TR>
      	<TD><ct:FWLabel key="GCA3011_IMP_JOURNAL"/></TD>
      	<TD><input type="checkbox" name="imprimerJournal" value="on" <%=impJournalChecked%> tabindex="2"></TD>
    </TR>
	<TR>
		<td><ct:FWLabel key="TYPE_IMPRESSION"/></td>
  		<TD>
   			<input type="radio" name="typeImpression" value="pdf" <%=pdfChecked%>/>PDF&nbsp;
   			<input type="radio" name="typeImpression" value="xls" <%=xlsChecked%>/>Excel
   		</TD>
    </TR> 
              
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>