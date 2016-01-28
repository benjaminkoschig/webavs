<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
	<%	
		CITransfertAffilieViewBean viewBean = (CITransfertAffilieViewBean)session.getAttribute("viewBean");
		userActionValue = "pavo.compte.transfertAffilie.executer";
		String jspLocation = servletContext + mainServletPath + "Root/ti_select_par.jsp";
		int autoDigitAff = CIUtil.getAutoDigitAff(session);
		idEcran ="CCI3006";
	%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
	function updateInfoAffilieSrc(tag){
		if(tag.select && tag.select.selectedIndex != -1){
			document.getElementById('infoAffilieSrc').value = tag.select[tag.select.selectedIndex].nom;
		}
	}
	function resetInfoAffilieSrc(){
		document.getElementById('infoAffilieSrc').value = "";		
	}
	function updateInfoAffilieDst(tag){
		if(tag.select && tag.select.selectedIndex != -1){
			document.getElementById('infoAffilieDst').value = tag.select[tag.select.selectedIndex].nom;
		}
	}
	function resetInfoAffilieDst(){
		document.getElementById('infoAffilieDst').value = "";		
	}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_CCI3006_TITRE_JSP"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							
<%@page import="globaz.pavo.db.compte.CITransfertAffilieViewBean"%>
	
	
	
<%@page import="globaz.pavo.util.CIUtil"%><tr>
		<td>
		<ct:FWLabel key="JSP_CCI3006_AFFILIE_SOURCE"/>
		</td>
		<td>
		<ct:FWPopupList name="employeurSrc" value="<%=viewBean.getEmployeurSrc()%>" onFailure="resetInfoAffilieSrc();" onChange="updateInfoAffilieSrc(tag);" jspName="<%=jspLocation%>" autoNbrDigit="<%=autoDigitAff%>" size="15" minNbrDigit="3"/>
		&nbsp;
		<input type="text" class="disabled" readonly name="infoAffilieSrc" size="50" maxlength="50" value="<%=viewBean.getInfoAffilieSrc()%>" tabIndex="-1">
		</td>
	</tr>
	<tr>
		<td>
		<ct:FWLabel key="JSP_CCI3006_AFFILIE_DESTINATION"/>
		</td>
		<td>
		<ct:FWPopupList name="employeurDst" value="<%=viewBean.getEmployeurDst()%>" onFailure="resetInfoAffilieDst();" onChange="updateInfoAffilieDst(tag);" jspName="<%=jspLocation%>" autoNbrDigit="<%=autoDigitAff%>" size="15" minNbrDigit="3"/>
		&nbsp;
		<input type="text" class="disabled" readonly name="infoAffilieDst" size="50" maxlength="50" value="<%=viewBean.getInfoAffilieDst()%>" tabIndex="-1">
		</td>
	</tr>
	<tr>
		<td>
			<ct:FWLabel key="JSP_CCI3006_DATE_FUSION"/>
		</td>
		<td>
			<ct:FWCalendarTag name="dateFusion" value = "<%=viewBean.getDateFusion()%>"/>
		</td>
	</tr>
     <tr>
	<td><ct:FWLabel key="JSP_CCI3006_IMPRIMER_ATTESTATION"/></td>
	<td><input type="checkbox" id="imprimerAttestations" name="imprimerAttestations" checked="checked"></td>
	</tr>
	<tr>
		<td><ct:FWLabel key="JSP_CCI3006_EMAIL"/></td>
		<td><input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getEmailAddress()%>">&nbsp;</td>
	</tr>

						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>