<%@ page language="java" errorPage="/errorPage.jsp"  contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pyxis.db.divers.TIApplication"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIAvoirAdresse"%>
<%@page import="globaz.pyxis.db.tiers.TITiers"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.globall.util.JACalendarGregorian"%>
<%@page import="globaz.pyxis.adresse.formater.TIAdresseFormater"%>
<%@page import="globaz.pyxis.adresse.formater.TIAdressePaiementFormater"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<html>
<body>
	<h1>Test des adresses de paiement</h1>
	<form action="<%=request.getContextPath()%>/pyxisRoot/FR/test/pmt.jsp">
	
	<table>
	<tr>
		<td>idTiers</td>
		<td><input type="text" name="idTiers"></td>
	</tr>
	<tr>
		<td>idExterne</td>
		<td><input type="text" name="idExterne"></td>
	</tr>
	<tr>
		<td>Domaine</td>
		<td><ct:FWCodeSelectTag 
					name="domaine" 
					defaut="<%=globaz.pyxis.constantes.IConstantes.CS_APPLICATION_DEFAUT%>" 
					codeType="PYAPPLICAT"
					/>
		</td>
	</tr>
	
	</table>
	<br><br>
	<input type="submit">	
	<br>
	<br>	
	
	<% 
		if (!JadeStringUtil.isEmpty(request.getParameter("idTiers"))) {
	%>
	<hr>
	<b>Resultat</b><br><br>
	<% 
		String adr = "";
		String domaine = request.getParameter("domaine");
		String idExterne =request.getParameter("idExterne");
		String date =JACalendarGregorian.todayJJsMMsAAAA();
		String idTiers = request.getParameter("idTiers");
		
		TITiers tiers = new TITiers();
		BSession gs = (BSession)session.getAttribute(FWServlet.OBJ_SESSION);
		tiers.setSession(gs);
		tiers.setIdTiers(idTiers);
		adr = tiers.getAdressePaiementAsString(domaine,date,idExterne,new TIAdressePaiementFormater(),true);
	%>
	<table>
	<tr>
	<td>
	<table>
	
	<tr>
		<td>Domaine :</td>
		<td><%=gs.getCodeLibelle(domaine)%></td>
	</tr>
	<tr>
		<td>idTiers</td>
		<td><%=idTiers%></td>
	</tr>
	<tr>
		<td>idExterne</td>
		<td><%=idExterne%></td>
	</tr>
	</table>
	</td>
	<td>
	<table>
	
	<tr>
	<td style="border:solid 1px silver;padding : 4px 4px 4px 4px">
	<pre><%=adr%></pre>
	</td></tr></table>
	</td>
	</tr>
	</table>
	<%} %>	
	</form>
	
</body>
</html>