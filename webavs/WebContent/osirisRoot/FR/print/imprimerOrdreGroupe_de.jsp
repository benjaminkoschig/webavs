 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 

<%@page import="globaz.osiris.db.print.CAImprimerOrdreGroupeViewBean"%>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%@page import="globaz.osiris.application.CAApplication"%>

<%
	idEcran = "GCA2001";
	CAImprimerOrdreGroupeViewBean viewBean = (CAImprimerOrdreGroupeViewBean) session.getAttribute(CADefaultServletAction.VB_ELEMENT);
	userActionValue = CAApplication.DEFAULT_OSIRIS_NAME + ".print.imprimerOrdreGroupe.executer"; 
	String pdfChecked = "pdf".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "";
	String xlsChecked = "xls".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "";
	String libLong = viewBean.getOrdreGroupe() != null ? viewBean.getOrdreGroupe().getOrdreGroupeLong() : "";
%>
<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 

<SCRIPT language="JavaScript">
top.document.title = "Web@Avs - <ct:FWLabel key='GCA2001_TITRE_ECRAN'/> - " + top.location.href;
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="GCA2001_TITRE_ECRAN"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%> 
 
    <TR> 
    	<TD nowrap width="144"><ct:FWLabel key="EMAIL"/></TD>
    	<TD nowrap width="560"><INPUT type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong"></TD>
    </TR>
    <TR> 
      	<TD nowrap width="144"><ct:FWLabel key="GCA2001_MOTIF_OG"/></TD>
      	<TD nowrap width="560">
      		<INPUT type="text" name="_libelleLong" value="<%=libLong%>" maxlength="80" class="libelleLongDisabled">
      		<input type="hidden" name="idOrdreGroupe" value="<%=viewBean.getIdOrdreGroupe()%>">
      	</TD>
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