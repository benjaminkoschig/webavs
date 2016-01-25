<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%><%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%>
<%@page import="globaz.draco.db.declaration.DSDecompteImpotLtnViewBean"%>
<%@page import="globaz.naos.translation.CodeSystem"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery-ui.js"></script>

	<script type="text/javascript">
		$(document).ready(function() {
			$(".reImpressionLine").hide();
			
			$("#reImpression").click(function(){
				if($(this).attr("checked")){
					$(".specialLine").hide();
					$(".reImpressionLine").show();
				}else{
					$(".specialLine").show();
					$(".reImpressionLine").hide();
				}	
 			})
		});
	</script>
<%
	DSDecompteImpotLtnViewBean viewBean = (DSDecompteImpotLtnViewBean)session.getAttribute("viewBean");
	userActionValue = "draco.declaration.decompteImpotLtn.executer";
	String eMailAddress = !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getEmailAddress())?viewBean.getEmailAddress():"";
	idEcran = "CDS2004";
	String mail = !globaz.jade.client.util.JadeStringUtil.isBlank(eMailAddress)?eMailAddress : "";
	String simuChecked = !viewBean.isSimulation() ? "checked='checked'" : "";
	String pdfChecked = "pdf".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "";
	String xlsChecked = "xls".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="CDS2004_TITRE_ECRAN"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD><ct:FWLabel key="CDS2004_REIMPRESSION"/></TD>
		<TD><input type="checkbox" name="reImpression" id="reImpression" size="4" /></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key="CDS2004_ANNEE"/></TD>
		<TD><input type="text" name="annee" value="<%=viewBean.getAnnee()%>" size="4" /></TD>
	</TR>
	<TR class="specialLine">
		<TD><ct:FWLabel key="CDS2004_CANTON"/></TD>
		<TD><ct:FWCodeSelectTag name="cantonSelectionne" defaut="" codeType="PYCANTON" wantBlank="true" /> <ct:FWLabel key="CDS2004_INFO_DATE_DOC"/></TD>
	</TR>
	<TR class="specialLine">
		<TD><ct:FWLabel key="CDS2004_DATE_DOCUMENT"/></TD>
		<TD><ct:FWCalendarTag name="dateValeur" value="<%=viewBean.getDateValeur()%>" /></TD>
	</TR>
	<TR class="reImpressionLine">
		<TD><ct:FWLabel key="CDS2004_REIMP_DATE_DOC"/></TD>
		<TD><ct:FWCalendarTag name="dateImpression" value="<%=viewBean.getDateImpression()%>" /> <ct:FWLabel key="CDS2004_INFO_REIMP_DATE_DOC"/></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key="CDS2004_EMAIL"/></TD>
		<TD><input type="text" name="eMailAddress" value="<%=mail%>" size = "40"></TD>
	</TR>
	<TR class="specialLine">
		<TD><ct:FWLabel key="CDS2004_SIMULATION"/></TD>
		<TD><input name="simulation" size="20" type="checkbox" <%=simuChecked%> style="text-align : right;"></TD>
	</TR>
	<TR class="specialLine">
		<td><ct:FWLabel key="CDS2004_TYPE_IMPRESSION"/></td>
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