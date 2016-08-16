<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" isELIgnored="false"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="globaz.pegasus.vb.lot.PCComptabiliserViewBean"%>
<%@page import="java.util.Vector"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/theme/process/header.jspf" %>

<c:set var="rootPath" value="${pageContext.request.contextPath}${requestScope.mainServletPath}Root"/>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_LAN_D"

	idEcran="PPC0096";
    
  	PCComptabiliserViewBean viewBean = (PCComptabiliserViewBean)session.getAttribute("viewBean"); 
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	userActionValue = IPCActions.ACTION_LOT_COMPTABILISER + ".executer";
	
%>
<%@ include file="/theme/process/javascripts.jspf" %>
<script type="text/javascript">

function selectChangeISO20022(s) {
	changeAffichageISO20022(s[s.selectedIndex].id);
}

function changeAffichageISO20022(cs) {
	if(cs==258003){ // 258003 traitement ISO20022
		$('.classIso').css('display', 'block'); $('.classNonIso').css('display', 'none');
	} else {
		$('.classNonIso').css('display', 'block'); $('.classIso').css('display', 'none');
	} 
}
function init(){}

$(document).ready(function() {
	//ne prend pas l'init! pourquoi? 
	selectChangeISO20022(document.getElementById("idOrganeExecution"));
});
</script>





<c:if test="${empty viewBean.idLot}">
    <c:set target="${viewBean}" property="idLot" value="${param['selectedId']}"/>
</c:if>

<c:choose>
	<c:when test="${empty param['descriptionLot']}">
		<c:set scope="page" var="descriptionLot" value="${viewBean.descriptionLot}"/>
	</c:when>
	
	<c:when test="${not empty param['descriptionLot']}">
		<c:set scope="page" var="descriptionLot" value="${param['descriptionLot']}"/>
	</c:when>
</c:choose>




<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="${rootPath}/css/lot/comptabiliser.css"/>
<%-- tpl:put name="zoneScripts" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_VALID_LOT_D_TITRE"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR class="lotTitle">
							<TD><LABEL for="descriptionLot" class="lot"><ct:FWLabel key="JSP_VALID_LOT_DESCR"/></LABEL></TD>
							<TD>	
								<span id="descLot" class="lot">${viewBean.idLot}</span>		
								<span  class="lot italic"><c:out value="${descriptionLot}"/></span>			
							</TD>															
						</TR>						

						<TR>
							<TD><LABEL for="eMailAddress"><ct:FWLabel key="JSP_VALID_LOT_D_EMAIL"/></LABEL></TD>
							<TD><INPUT type="text" name="eMailAddress" value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.geteMailAddress())?controller.getSession().getUserEMail():viewBean.geteMailAddress()%>" class="libelleLong"></TD>
						</TR>						
						<TR>
							<TD><LABEL for="dateValeurComptable"><ct:FWLabel key="JSP_VALID_LOT_DVC"/></LABEL></TD>
							<TD><input data-g-calendar="mandatory:true" name="dateValeurComptable" value="${viewBean.dateValeurComptable}"/></TD>
						</TR>	
						
						<TR><TD colspan="2"><br/></TD></TR>
						
						<TR>
							<TD><LABEL for="dateEcheancePaiement"><ct:FWLabel key="JSP_EPM_DATE_ECHEANCE"/></LABEL></TD>
							<TD><input data-g-calendar="mandatory:true" name="dateEcheancePaiement" value="${viewBean.dateEcheancePaiement}"/></TD>													
						</TR>		

						<tr>
							<td>
								<label for="idOrganeExecution">
									<ct:FWLabel key="JSP_EPM_ORGANE_EXECUTION" />
								</label>
							</td>
							<td>
<%-- 								<ct:FWListSelectTag	name="idOrganeExecution"  --%>
<%-- 													data="<%=viewBean.getOrganesExecution()%>"  --%>
<%-- 													defaut="<%=viewBean.getIdOrganeExecution()%>" /> --%>
								<% 	Vector<String[]> _CsOrganeExecution = viewBean.getOrganesExecution();%> 
									<select id="idOrganeExecution" name="idOrganeExecution" onchange="selectChangeISO20022(this)">
								    <%for (int i=0; i < _CsOrganeExecution.size(); i++) {
										String[] _organeExecution = _CsOrganeExecution.get(i);
										if (_organeExecution[0].equalsIgnoreCase(viewBean.getIdOrganeExecution())) { %>
	               							<option selected value="<%=_organeExecution[0]%>" id="<%=_organeExecution[2]%>"><%=_organeExecution[1]%></option>
	                
	             						<%} else { %>			
									  		<option value="<%=_organeExecution[0]%>" id="<%=_organeExecution[2]%>"><%=_organeExecution[1]%></option>
										<%} 
									}%>
							</td>
						</tr>


						<TR class="classNonIso">
							<TD><LABEL for="numeroOG"><ct:FWLabel key="JSP_EPM_NUMERO_OG"/></LABEL></TD>
							<TD>
								<INPUT 	data-g-integer="sizeMax:2,mandatory:true"  type="text" name="numeroOG" value="" class="libelleShort" size="2" />
							</TD>													
						</TR>	
						<tr class="classIso">
							<TD><ct:FWLabel key="JSP_VALID_LOT_ISO_GEST"/>&nbsp;</TD>
							<TD><input type="text" name="isoGestionnaire" id="isoGestionnaire" value="<%=viewBean.getIsoGestionnaire()%>" /></TD>
						</tr>
						<tr class="classIso">
							<TD><ct:FWLabel key="JSP_VALID_LOT_ISO_PRIO"/>&nbsp;</TD>
							<td>
								<select id="isoHighPriority" name="isoHighPriority">
					                <OPTION selected value="0">normale</OPTION>
					                <OPTION value="1">haute</OPTION>
				              	</select>
							</td>
						</tr>
						<tr class="classIso">
							<TD><ct:FWLabel key="JSP_VALID_LOT_ISO_TYPE_AVIS"/>&nbsp;</TD>
							<TD><ct:FWCodeSelectTag name="isoCsTypeAvis" defaut="<%=viewBean.getIsoCsTypeAvis()%>" codeType="OSIOGTYA" /></TD>
						</tr>	
						<INPUT type="hidden" name="idLot" value="${viewBean.idLot}"/>
						<INPUT type="hidden" name="descriptionLot" value="${param['descriptionLot']}"/>

						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>