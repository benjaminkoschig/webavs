<%@page import="globaz.al.vb.ajax.ALEnfantListViewBean"%>

<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/plain;charset=UTF-8" %>
<%-- page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=UTF-8" --%>
<%-- page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=UTF-8" --%>
<%-- taglib uri="/WEB-INF/taglib.tld" prefix="ct" --%>
	<%
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();	
	ALEnfantListViewBean viewBean = (ALEnfantListViewBean) request.getAttribute("viewBean");
	%>
	
{"search":{
	"typeSearch" :	"<%=((Object)viewBean.getEnfantComplexResult(0)).getClass()%>",
	"criteria" :	"TODO",
	"totalResults" : "<%=viewBean.getSize()%>",
	"results" : [
		<% for(int i=0;i<viewBean.getSize();i++){%>
		{	"properties" : [
			{"name":"id",
				"value":"<%=viewBean.getEnfantComplexResult(i).getEnfantModel().getId()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.idTiers",
				"value":"<%=viewBean.getEnfantComplexResult(i).getPersonneEtendueComplexModel().getTiers().getIdTiers()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.idTiers",
				"value":"<%=viewBean.getEnfantComplexResult(i).getPersonneEtendueComplexModel().getPersonne().getIdTiers()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personneEtendue.idTiers",
				"value":"<%=viewBean.getEnfantComplexResult(i).getPersonneEtendueComplexModel().getPersonneEtendue().getIdTiers()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personneEtendue.spy",
				"value":"<%=viewBean.getEnfantComplexResult(i).getPersonneEtendueComplexModel().getPersonneEtendue().getSpy()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personneEtendue.new",
				"value":"<%=viewBean.getEnfantComplexResult(i).getPersonneEtendueComplexModel().getPersonneEtendue().isNew()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.spy",
				"value":"<%=viewBean.getEnfantComplexResult(i).getPersonneEtendueComplexModel().getPersonne().getSpy()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.new",
				"value":"<%=viewBean.getEnfantComplexResult(i).getPersonneEtendueComplexModel().getPersonne().isNew()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.spy",
				"value":"<%=viewBean.getEnfantComplexResult(i).getPersonneEtendueComplexModel().getTiers().getSpy()%>"},
						{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.new",
				"value":"<%=viewBean.getEnfantComplexResult(i).getPersonneEtendueComplexModel().getTiers().isNew()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personneEtendue.numAvsActuel",
				"value":"<%=viewBean.getEnfantComplexResult(i).getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.sexe",
				"value":"<%=viewBean.getEnfantComplexResult(i).getPersonneEtendueComplexModel().getPersonne().getSexe() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.etatCivil",
				"value":"<%=viewBean.getEnfantComplexResult(i).getPersonneEtendueComplexModel().getPersonne().getEtatCivil() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.dateNaissance",
				"value":"<%=viewBean.getEnfantComplexResult(i).getPersonneEtendueComplexModel().getPersonne().getDateNaissance() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.designation1",
				"value":"<%=viewBean.getEnfantComplexResult(i).getPersonneEtendueComplexModel().getTiers().getDesignation1() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.designation2",
				"value":"<%=viewBean.getEnfantComplexResult(i).getPersonneEtendueComplexModel().getTiers().getDesignation2() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.idPays",
				"value":"<%=viewBean.getEnfantComplexResult(i).getPersonneEtendueComplexModel().getTiers().getIdPays() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.enfantModel.idEnfant",
				"value":"<%=viewBean.getEnfantComplexResult(i).getId()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.enfantModel.idTiersEnfant",
				"value":"<%=viewBean.getEnfantComplexResult(i).getEnfantModel().getIdTiersEnfant()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.enfantModel.cantonResidence",
				"value":"<%=viewBean.getEnfantComplexResult(i).getEnfantModel().getCantonResidence() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.enfantModel.idPaysResidence",
				"value":"<%=viewBean.getEnfantComplexResult(i).getEnfantModel().getIdPaysResidence() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.enfantModel.capableExercer",
				"value":"<%=viewBean.getEnfantComplexResult(i).getEnfantModel().getCapableExercer() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.enfantModel.typeAllocationNaissance",
				"value":"<%=viewBean.getEnfantComplexResult(i).getEnfantModel().getTypeAllocationNaissance() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.enfantModel.allocationNaissanceVersee",
				"value":"<%=viewBean.getEnfantComplexResult(i).getEnfantModel().getAllocationNaissanceVersee() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.enfantModel.montantAllocationNaissanceFixe",
				"value":"<%=viewBean.getEnfantComplexResult(i).getEnfantModel().getMontantAllocationNaissanceFixe() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.enfantModel.spy",
				"value":"<%=viewBean.getEnfantComplexResult(i).getEnfantModel().getSpy() %>"},
				{"name":"<%=request.getParameter("prefixModel")%>.enfantModel.new",
				"value":"<%=viewBean.getEnfantComplexResult(i).getEnfantModel().isNew() %>"},
		    {"name":"statutWarningEnfant","persistence":"no",
				"value":"<%=Integer.parseInt(viewBean.getIsActifListResults().get(i).toString())>0?"<img src='images/dialog-warning.png' alt='"+objSession.getLabel("AL0003_ENFANT_WARNING_ACTIF")+"' width='16' height='16'/>":"" %>"}
		]}<% if(i!=viewBean.getSize()-1){%>,<%}%> 
		<%} 
		%>

	]
		
  }
}
