<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "CCI0028";
	
%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
	<ct:menuChange displayId="options" menuId="anomalie-detail" showTab="options">
	</ct:menuChange>
<%bButtonNew = false;%>
<script>
	usrAction="pavo.comparaison.anomalieCI.lister";
	bFind = true;
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Anomalies<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<tr>	
								<td>
									NSS
								</td>
								<td>
									<nss:nssPopup value='' name="likeNumeroAvs" 
									cssclass="visible"  avsMinNbrDigit="99" avsAutoNbrDigit="99"
									nssMinNbrDigit="99" nssAutoNbrDigit="9"/> 
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
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>