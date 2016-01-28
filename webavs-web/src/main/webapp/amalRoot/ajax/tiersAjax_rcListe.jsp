<%@page import="globaz.amal.vb.ajax.AMTiersAjaxListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/plain;charset=UTF-8" %>
<%-- taglib uri="/WEB-INF/taglib.tld" prefix="ct" --%>
	<%
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	AMTiersAjaxListViewBean viewBean = (AMTiersAjaxListViewBean) request.getAttribute("viewBean");
	%>
	
{"search":{
	"typeSearch" :	"<%=((Object)viewBean.getPersonneEtendueResult(0)).getClass()%>",
	"criteria" :	"TODO",
	"totalResults" : "<%=viewBean.getSize()%>",
	"results" : [
		<% for(int i=0;i<viewBean.getSize();i++){%>
		{	"properties" : [
			{"name":"id",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getId()%>"},		
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendue.tiers.idTiers",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getId()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendue.personne.idTiers",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getId()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendue.personneEtendue.idTiers",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getId()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendue.personneEtendue.spy",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getPersonneEtendue().getSpy()%>"},	
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendue.personne.spy",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getPersonne().getSpy()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendue.tiers.spy",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getTiers().getSpy()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendue.personneEtendue.numAvsActuel",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getPersonneEtendue().getNumAvsActuel()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendue.personneEtendue.numContribuableActuel",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getPersonneEtendue().getNumContribuableActuel()%>"},				
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendue.personne.sexe",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getPersonne().getSexe() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendue.tiers.titreTiers",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getTiers().getTitreTiers() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendue.personne.etatCivil",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getPersonne().getEtatCivil() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendue.personne.dateNaissance",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getPersonne().getDateNaissance() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendue.tiers.designation1",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getTiers().getDesignation1() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendue.tiers.designation2",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getTiers().getDesignation2() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendue.tiers.idPays",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getTiers().getIdPays() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendue.tiers.langue",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getTiers().getLangue() %>"},
			{"name":"suggestOptionSelect","persistence":"no",
				"value":"<%="<option value='"+viewBean.getPersonneEtendueResult(i).getPersonneEtendue().getNumAvsActuel()+"'>"+viewBean.getPersonneEtendueResult(i).getPersonneEtendue().getNumAvsActuel()+"</option>"%>"}
		]}<% if(i!=viewBean.getSize()-1){%>,<%}%> 
		<%} %>
	]
		
  }
}
