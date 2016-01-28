<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.SimpleAutreRente"%>
<%@page import="globaz.pegasus.vb.renteijapi.PCAutreRenteAjaxViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%
  PCAutreRenteAjaxViewBean viewBean = (PCAutreRenteAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
  SimpleAutreRente entity = viewBean.getAutreRente().getSimpleAutreRente();
  globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
  globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>

<message>
	<contenu>
		<csGenre><%=entity.getCsGenre()%></csGenre>
		<autreGenre><%=JadeStringUtil.isBlankOrZero(entity.getAutreGenre())?"":entity.getAutreGenre()%></autreGenre>
		<csType><%=entity.getCsType()%></csType>
		<montant><%=entity.getMontant()%></montant>
		<monnaie><%=JadeStringUtil.isBlankOrZero(entity.getCsMonnaie())?"":entity.getCsMonnaie()%></monnaie>
		<pays><%=viewBean.getPays(objSession)%></pays>
		<idPays><%=entity.getIdPays()%></idPays>
		<fournisseurPrestation><%=entity.getFournisseurPrestation()%></fournisseurPrestation>
		<dateEcheance><%= JadeStringUtil.isBlankOrZero(entity.getDateEcheance())?"":entity.getDateEcheance()%></dateEcheance>
		<DR><%=viewBean.getAutreRente().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>