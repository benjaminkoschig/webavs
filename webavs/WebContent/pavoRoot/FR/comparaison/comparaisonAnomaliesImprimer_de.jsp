<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%	
	globaz.pavo.db.comparaison.CIComparaisonAnomaliesImprimerViewBean viewBean = (globaz.pavo.db.comparaison.CIComparaisonAnomaliesImprimerViewBean)session.getAttribute("viewBean");
	userActionValue = "pavo.comparaison.comparaisonAnomaliesImprimer.executer";
	idEcran = "CCI2009";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Imprimer la liste des anomalies<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>	
							<td>
								NSS
							</td>
							<td>
								<nss:nssPopup name="likeNumeroAvs" avsMinNbrDigit="99" nssMinNbrDigit="99"/>
							</td>
						</tr>
						<tr>
							<td>
								Type d'anomalie	
							</td>
							<td>
								<ct:FWCodeSelectTag name="forIdTypeAnomalie"
								codeType="CITYPANB" defaut="" wantBlank="true"/>
							</td>
						</tr>
						<tr>
							<td>
								E-mail
							</td>
							<td>
								<input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=!globaz.globall.util.JAUtil.isStringEmpty(viewBean.getEmailAddress())?viewBean.getEmailAddress():""%>">&nbsp;
							</td>
						</tr>
							
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>