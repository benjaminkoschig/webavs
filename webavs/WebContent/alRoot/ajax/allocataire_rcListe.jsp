<%@page import="ch.globaz.al.businessimpl.services.ALImplServiceLocator"%>
<%@page import="ch.globaz.al.business.models.allocataire.AgricoleModel"%>
<%@page import="ch.globaz.al.business.models.allocataire.AgricoleSearchModel"%>
<%@page import="globaz.al.vb.ajax.ALAllocataireListViewBean"%>
<%@page import="ch.globaz.al.business.constantes.ALCSPays"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/plain;charset=UTF-8" %>
<%-- page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=UTF-8" --%>
<%-- page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=UTF-8" --%>
<%-- taglib uri="/WEB-INF/taglib.tld" prefix="ct" --%>
	<%
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	ALAllocataireListViewBean viewBean = (ALAllocataireListViewBean) request.getAttribute("viewBean");
	
		/*	properties : 
				name : 		  nom de la propriété(champ formulaire page jsp) à mettre à jour dans le viewBean récupérant le résultat 
				persistence : indique si le champ formulaire à mettre à jour est géré ou non par la persistence (que indicatif, dans la partie overview allocataire de AL0004)
				value : 	  valeur à mettre à jour dans le champ
				
			properties spéciales : 
				name=suggestOptionSelect contient la valeur qui viendra s'ajouter des la liste des suggestions.
			
		*/
	%>
	
{"search":{
	"typeSearch" :	"<%=((Object)viewBean.getAllocataireComplexResult(0)).getClass()%>",
	"criteria" :	"TODO",
	"totalResults" : "<%=viewBean.getSize()%>",
	"results" : [
		<% for(int i=0;i<viewBean.getSize();i++){
		
			AgricoleSearchModel agricoleSearchModel = new AgricoleSearchModel();
			agricoleSearchModel.setForIdAllocataire(viewBean.getAllocataireComplexResult(i).getId());
			agricoleSearchModel = ALImplServiceLocator.getAgricoleModelService().search(agricoleSearchModel);
			AgricoleModel agricoleModel = new AgricoleModel();
			if (agricoleSearchModel.getSize()==1) {
				agricoleModel = (AgricoleModel) agricoleSearchModel.getSearchResults()[0];
			}
		%>
		{	"properties" : [
			{"name":"id",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getId()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.idTiers",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getTiers().getIdTiers()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.idTiers",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getPersonne().getIdTiers()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personneEtendue.idTiers",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getPersonneEtendue().getIdTiers()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personneEtendue.spy",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getPersonneEtendue().getSpy()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personneEtendue.new",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getPersonneEtendue().isNew()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.spy",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getPersonne().getSpy()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.new",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getPersonne().isNew()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.spy",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getTiers().getSpy()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.new",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getTiers().isNew()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.allocataireModel.idAllocataire",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getId()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.allocataireModel.spy",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getSpy()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.allocataireModel.new",
				"value":"<%=viewBean.getAllocataireComplexResult(i).isNew()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.allocataireModel.idTiersAllocataire",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getAllocataireModel().getIdTiersAllocataire()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personneEtendue.numAvsActuel",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.sexe",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getPersonne().getSexe() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.etatCivil",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getPersonne().getEtatCivil() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.dateNaissance",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getPersonne().getDateNaissance() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.designation1",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getTiers().getDesignation1() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.designation2",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getTiers().getDesignation2() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.idPays",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getTiers().getIdPays() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.allocataireModel.cantonResidence",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getAllocataireModel().getCantonResidence() %>"},
			{"name":"<%=request.getParameter("prefixModel")%>.allocataireModel.idPaysResidence",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getAllocataireModel().getIdPaysResidence() %>"},
			{"name":"dossierComplexModel.domaineMontagne",
				"value":"<%=agricoleModel.getDomaineMontagne() %>"},
			{"name":"dossierComplexModel.dossierModel.idAllocataire",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getAllocataireModel().getIdAllocataire() %>"},
			{"name":"allocataireDesignation","persistence":"no",
				"value":"<%=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getTiers().getDesignation1()%> <%=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getTiers().getDesignation2()%>"},
			{"name":"allocataireData","persistence":"no",
			<% String allocData = "";
				if(!JadeStringUtil.isBlankOrZero(viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getPersonne().getDateNaissance()))
					allocData+=viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getPersonne().getDateNaissance();
				if(!JadeStringUtil.isBlankOrZero(viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getPersonne().getSexe()))
					allocData+=" - "+objSession.getCode(viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getPersonne().getSexe());
				if(!JadeStringUtil.isBlankOrZero(viewBean.getAllocataireComplexResult(i).getPaysModel().getCodeIso()))
					allocData+=" - "+viewBean.getAllocataireComplexResult(i).getPaysModel().getCodeIso();
				if(!JadeStringUtil.isBlankOrZero(viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getPersonne().getEtatCivil()))
					allocData+=" - "+objSession.getCodeLibelle(viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getPersonne().getEtatCivil());
			%>
				"value":"<%=allocData%>"},
			{"name":"allocataireResidence","persistence":"no",
				"value":"<%=ALCSPays.PAYS_SUISSE.equals(viewBean.getAllocataireComplexResult(i).getAllocataireModel().getIdPaysResidence())?objSession.getCodeLibelle(viewBean.getAllocataireComplexResult(i).getAllocataireModel().getCantonResidence()):viewBean.getAllocataireComplexResult(i).getPaysResidenceModel().getLibelleFr()%>"},
			{"name":"statutWarningAlloc","persistence":"no",
				"value":"<%=Integer.parseInt(viewBean.getIsActifListResults().get(i).toString())>0?"<img src='images/dialog-warning.png' alt='"+objSession.getLabel("AL0006_ALLOC_WARNING_ACTIF")+"' width='16' height='16'/>":"" %>"},
			{"name":"suggestOptionSelect","persistence":"no",
				"value":"<%="<option value='"+viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel()+"'>"+viewBean.getAllocataireComplexResult(i).getPersonneEtendueComplexModel().getPersonneEtendue().getNumAvsActuel()+"</option>"%>"}
		]}<% if(i!=viewBean.getSize()-1){%>,<%}%> 
		<%} %>
	]
  }
}
