<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CFA2006";%>
<%@ page import="globaz.musca.db.facturation.*"%>
<%
	//Récupération des beans
	FAPassageListerAfactsAQuittancerViewBean viewBean = (FAPassageListerAfactsAQuittancerViewBean) session.getAttribute("viewBean");	 
	 
	userActionValue = "musca.facturation.passageFacturationListerAfactAQuittancer.executer";
%>
<SCRIPT language="JavaScript">
top.document.title = "Musca - Impression des afacts en suspens"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Impression des afacts en suspens<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<tr>
								<td>Numéro du passsage</td>
								<TD><INPUT name="idPassage" type="text" value="<%=viewBean.getIdPassage()%>" class="numeroCourtDisabled" readonly></TD>
							</tr>
					          <TR>
					            <TD>Adresse E-Mail</TD>
					            <TD><input name='eMailAddress' class='libelleLong' value="<%=viewBean.getEMailAddress()%>"></TD>
					          </TR>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>