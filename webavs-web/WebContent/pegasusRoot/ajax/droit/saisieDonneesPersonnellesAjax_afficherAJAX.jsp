<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.droit.PCSaisieDonneesPersonnellesAjaxViewBean"%>
<%
	PCSaisieDonneesPersonnellesAjaxViewBean viewBean = (PCSaisieDonneesPersonnellesAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
%>

<message>
	<contenu>
		<statusRefugie><%=viewBean.getDonneesPersonnelles().getSimpleDonneesPersonnelles().getCsStatusRefugieApatride()%></statusRefugie>
		<noOCC><%=viewBean.getDonneesPersonnelles().getSimpleDonneesPersonnelles().getNoOCC()%></noOCC>
		<noCaisseAVS><%=viewBean.getDonneesPersonnelles().getSimpleDonneesPersonnelles().getNoCaisseAvs()%></noCaisseAVS>
		<noOfficeAI><%=viewBean.getDonneesPersonnelles().getSimpleDonneesPersonnelles().getNoOfficeAi()%></noOfficeAI>
		<nomDernierDomicile><%=viewBean.getDonneesPersonnelles().getLocalite().getNumPostal()%>, <%=viewBean.getDonneesPersonnelles().getLocalite().getLocalite()%></nomDernierDomicile>
		<idDernierDomicile><%=viewBean.getDonneesPersonnelles().getSimpleDonneesPersonnelles().getIdDernierDomicileLegale()%></idDernierDomicile>
		<idTiersRepondant><%=viewBean.getDonneesPersonnelles().getSimpleDonneesPersonnelles().getIdTiersRepondant()%></idTiersRepondant>
		<repondant><%=viewBean.getDonneesPersonnelles().getSimpleDonneesPersonnelles().getIdDernierDomicileLegale()%></repondant>	
		<csLienRepondant><%=viewBean.getDonneesPersonnelles().getSimpleDonneesPersonnelles().getCsLienRepondant()%></csLienRepondant>
		<communeOrigineCodeOfs><%=viewBean.getDonneesPersonnelles().getSimpleDonneesPersonnelles().getCommuneOrigineCodeOfs()%></communeOrigineCodeOfs>	
		<communeOrigine><%=viewBean.getDonneesPersonnelles().getSimpleDonneesPersonnelles().getCommuneOrigine()%></communeOrigine>		
		<parentViewBean><ct:serializeObject objectName="viewBean.droit"/></parentViewBean>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
