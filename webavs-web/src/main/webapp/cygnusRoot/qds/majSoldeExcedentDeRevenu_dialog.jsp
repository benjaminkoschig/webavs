<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ page import="globaz.globall.db.*, globaz.framework.util.*" %>
<%@page import="globaz.prestation.tools.PRSession"%>
<%
	String idQd 	  = request.getParameter("idQd");
	BSession bsession = (BSession)PRSession.getSession(session);
	String servletContext = request.getContextPath();
%>

<%@page import="java.util.Iterator"%>
<%@page import="globaz.cygnus.vb.qds.RFQdHistoriqueSoldeExcedentDeRevenuJointSelfListViewBean"%>
<%@page import="globaz.cygnus.vb.qds.RFQdHistoriqueSoldeExcedentDeRevenuJointSelfViewBean"%>
<html>
<LINK rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/master.css">

<script type="text/javascript">
function doCancel() {
	window.close();
}
function doValidate() {
	document.mainForm.status.value = "ok";
	document.mainForm.submit();
}
function showError(text) {
	document.getElementById("errorLabel").innerHTML = text;
}
</script>

<TITLE></TITLE>
<body style="margin-left:0px ; margin-bottom:0px">
<H1><%=bsession.getLabel("JSP_RF_S_SOEXR_H_DETAIL_SOLDE_EXCEDENT")%></H1>
<TABLE cellspacing="0" cellpadding="0" width="100%">
	<thead>
		<tr align="center">
			<th><%=bsession.getLabel("JSP_RF_S_SOEXR_H_TYPE_MODIF")%></th>
			<th><%=bsession.getLabel("JSP_RF_S_SOEXR_H_DATE_MODIF")%></th>
			<th><%=bsession.getLabel("JSP_RF_S_SOEXR_H_GESTIONNAIRE_MODIF")%></th>
			<th><%=bsession.getLabel("JSP_RF_S_SOEXR_H_REMARQUE")%></th>
			<th><%=bsession.getLabel("JSP_RF_S_SOEXR_H_DATE")%></th>
			<th><%=bsession.getLabel("JSP_RF_S_SOEXR_H_ANCIEN_CONCERNE")%></th>
			<th><%=bsession.getLabel("JSP_RF_S_SOEXR_H_CONCERNE")%></th>
			<th><%=bsession.getLabel("JSP_RF_S_SOEXR_H_ANCIEN_MONTANT")%></th>
			<th><%=bsession.getLabel("JSP_RF_S_SOEXR_H_MONTANT")%></th>
			<th><%=bsession.getLabel("JSP_RF_S_SOEXR_H_GESTIONNAIRE")%></th>
		</tr>
	</thead>
	<tbody>
<%
	RFQdHistoriqueSoldeExcedentDeRevenuJointSelfListViewBean dem = RFQdHistoriqueSoldeExcedentDeRevenuJointSelfListViewBean.loadHistorique(bsession, idQd);
		 for(Iterator it=dem.iterator();it.hasNext();){
	RFQdHistoriqueSoldeExcedentDeRevenuJointSelfViewBean element=(RFQdHistoriqueSoldeExcedentDeRevenuJointSelfViewBean)it.next();
%> 
		<tr align="center">
			<td><%=bsession.getCodeLibelle(element.getTypeModification())%></td>		
			<td><%=element.getDateModification() %></td>
			<td><%=element.getVisaGestionnaire() %></td>
			<td><%=element.getRemarque() %></td>
			<td><%=element.getAncienDate() %></td>
			<td><%=element.getAncienConcerne() %></td>
			<td><%=element.getConcerne() %></td>
			<td><%=element.getAncienMontant() %></td>
			<td><%=element.getMontantSoldeExcedent() %></td>
			<td><%=element.getAncienGestionnaire() %></td>
		</tr> 
			<%			
		}
%>
	</tbody>
</TABLE>
<p><button onclick="window.close();">Fermer</button></p>
</body>
</html>