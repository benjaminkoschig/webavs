
<%@page import="globaz.webavs.common.CommonNSSFormater"%>
<%@page import="globaz.pyxis.api.ITIPersonne"%>
<%@page import="globaz.pyxis.api.ITITiers"%>
<%@page import="globaz.pavo.api.ICICompteIndividuel"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%><%@page import="globaz.al.vb.ajax.ALCiListViewBean"%>

<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/plain;charset=UTF-8" %>

<%-- taglib uri="/WEB-INF/taglib.tld" prefix="ct" --%>
	<%
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();	
	
	ALCiListViewBean viewBean = (ALCiListViewBean) request.getAttribute("viewBean");
	String ciNameSeparator = ",";
	
	CommonNSSFormater formatter = new CommonNSSFormater();
	%>
	
{"search":{
	"typeSearch" :	"((Object)viewBean.getCompteIndividuelResult(0)).getClass()",
	"criteria" :	"TODO",
	"totalResults" : "<%=viewBean.getSize()%>",
	"results" : [
	<% 
	for(int i=0;i<viewBean.getSize();i++){
		if(!JadeStringUtil.isEmpty(viewBean.getCompteIndividuelResult(i).getNomPrenom())){
		%>
				
		{	"properties" : [
			{"name":"id",
				"value":"<%=viewBean.getCompteIndividuelResult(i).getIdCompte()%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personneEtendue.numAvsActuel",
				"value":"<%= formatter.format(viewBean.getCompteIndividuelResult(i).getNumAvsActuel())%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.designation1",
				"value":"<%=viewBean.getCompteIndividuelResult(i).getNomPrenom().substring(0,viewBean.getCompteIndividuelResult(i).getNomPrenom().lastIndexOf(ciNameSeparator))%>"},
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.designation2",
					"value":"<%=viewBean.getCompteIndividuelResult(i).getNomPrenom().substring(viewBean.getCompteIndividuelResult(i).getNomPrenom().lastIndexOf(ciNameSeparator)+1,viewBean.getCompteIndividuelResult(i).getNomPrenom().length())%>"},
			{"name":"dateNaissance",
				"value":"<%=viewBean.getCompteIndividuelResult(i).getDateNaissance()%>"},				
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.dateNaissance",
				"value":"<%=viewBean.getCompteIndividuelResult(i).getDateNaissance()%>"},		
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.personne.sexe",
				"value":"<%=viewBean.getCompteIndividuelResult(i).getSexe().equals(ICICompteIndividuel.CS_HOMME)?ITIPersonne.CS_HOMME:ITIPersonne.CS_FEMME%>"},		
			{"name":"<%=request.getParameter("prefixModel")%>.personneEtendueComplexModel.tiers.idPays",
				"value":"<%=viewBean.getCompteIndividuelResult(i).getIdNationalite()%>"},		
			{"name":"compteIndividuel.spy",
				"value":"<%=viewBean.getCompteIndividuelResult(i).getSpy()%>"}			
					
			
			]
		
  		}
  	<%
	}
	}
	%>
]}
}

