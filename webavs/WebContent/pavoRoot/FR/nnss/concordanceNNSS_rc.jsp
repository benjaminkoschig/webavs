<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "CCI0035";
	
%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<script>
	usrAction="pavo.nnss.concordanceNNSS.lister";
	bFind = true;
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<tr>
								<td>
									No affilié	
								</td>
								<td>
									<input type="text" name ="likeNumeroAffilie">
								</td>
							</tr>
							
							<tr>	
								<td>
									No AVS
								</td>
								<td>
									<nss:nssPopup value=''name="likeNumeroAvs" 
									newnss="false"  avsMinNbrDigit="99" avsAutoNbrDigit="99"
									nssMinNbrDigit="99" nssAutoNbrDigit="9"/> 
								</td>
							</tr>
							<tr>
								<td>
									NNSS	
								</td>
								<td>
									<nss:nssPopup value=''name="likeNNSS" 
									  avsMinNbrDigit="99" avsAutoNbrDigit="99"
									  newnss="true"
									nssMinNbrDigit="99" nssAutoNbrDigit="9"/> 
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