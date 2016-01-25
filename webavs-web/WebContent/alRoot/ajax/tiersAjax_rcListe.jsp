<%@page import="globaz.al.vb.ajax.ALTiersAjaxListViewBean"%>

<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/plain;charset=UTF-8" %>
<%-- page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=UTF-8" --%>
<%-- page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=UTF-8" --%>
<%-- taglib uri="/WEB-INF/taglib.tld" prefix="ct" --%>
	<%
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	ALTiersAjaxListViewBean viewBean = (ALTiersAjaxListViewBean) request.getAttribute("viewBean");
	%>
	
{"search":{
	"typeSearch" :	"<%=((Object)viewBean.getPersonneEtendueResult(0)).getClass()%>",
	"criteria" :	"TODO2",
	"totalResults" : "<%=viewBean.getSize()%>",
	"results" : [
		<% for(int i=0;i<viewBean.getSize();i++){%>
		{	"properties" : [
			{"name":"id",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getId()%>"},		
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.idTiers",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getId()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.idTiers",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getId()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personneEtendue.idTiers",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getId()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personneEtendue.spy",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getPersonneEtendue().getSpy()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personneEtendue.new",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getPersonneEtendue().isNew()%>"},		
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.spy",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getPersonne().getSpy()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.new",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getPersonne().isNew()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.spy",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getTiers().getSpy()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.new",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getTiers().isNew()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personneEtendue.numAvsActuel",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getPersonneEtendue().getNumAvsActuel()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.sexe",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getPersonne().getSexe() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.titreTiers",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getTiers().getTitreTiers() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.etatCivil",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getPersonne().getEtatCivil() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.dateNaissance",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getPersonne().getDateNaissance() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.designation1",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getTiers().getDesignation1() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.designation2",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getTiers().getDesignation2() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.idPays",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getTiers().getIdPays() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.langue",
				"value":"<%=viewBean.getPersonneEtendueResult(i).getTiers().getLangue() %>"},
			{"name":"suggestOptionSelect","persistence":"no",
				"value":"<%="<option value='"+viewBean.getPersonneEtendueResult(i).getPersonneEtendue().getNumAvsActuel()+"'>"+viewBean.getPersonneEtendueResult(i).getPersonneEtendue().getNumAvsActuel()+"</option>"%>"}
		]}<% if(i!=viewBean.getSize()-1){%>,<%}%> 
		<%} %>
	]
		
  }
}
