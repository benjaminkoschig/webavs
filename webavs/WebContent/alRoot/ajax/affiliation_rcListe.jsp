<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="globaz.al.vb.ajax.ALAffiliationListViewBean"%>

<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/plain;charset=UTF-8" %>
<%-- page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=UTF-8" --%>
<%-- page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=UTF-8" --%>
<%-- taglib uri="/WEB-INF/taglib.tld" prefix="ct" --%>
	<%
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();	
	ALAffiliationListViewBean viewBean = (ALAffiliationListViewBean) request.getAttribute("viewBean");
	%>
	
{"search":{
	"typeSearch" :	"Affiliation",
	"criteria" :	"TODO",
	"totalResults" : "<%=viewBean.getSize()%>",
	"results" : [
	
	<% 
	if(viewBean.getSize()>0){
		//Partie affichage résultats pour la suggestion de n°
		if(!JadeStringUtil.isEmpty(viewBean.getLikeNumeroAffilie())){
			for(int i=0;i<viewBean.getSize();i++){%>
			{	"properties" : [
				{"name":"suggestOptionSelect","persistence":"no",
					"value":"<%="<option value='"+viewBean.getSuggestNumerosAffilie()[i]+"'>"+viewBean.getSuggestNumerosAffilie()[i]+"</option>"%>"}			
				
				]}<% if(i!=viewBean.getSize()-1){%>,<%}%> 
		<%	}
		}else if(!JadeStringUtil.isEmpty(viewBean.getForNumeroAffilie())){%>
		<% %>
			{	"properties" : [
				{"name":"<%=request.getParameter("prefixModel")%>.numero",
					"value":"<%=viewBean.getInfoAffiliation().getNumeroAffilie()%>"},
				{"name":"<%=request.getParameter("prefixModel")%>.designation",
					"value":"<%=JadeStringUtil.isEmpty(viewBean.getInfoAffiliation().getDesignation())?"":StringEscapeUtils.escapeJavaScript(viewBean.getInfoAffiliation().getDesignation())%>"},
				{"name":"<%=request.getParameter("prefixModel")%>.canton",
					"value":"<%=objSession.getCodeLibelle(viewBean.getInfoAffiliation().getCanton())%>"},
				{"name":"<%=request.getParameter("prefixModel")%>.periode",
					"value":"<%=JadeStringUtil.isEmpty(viewBean.getInfoAffiliation().getDateDebutAffiliation())?"":viewBean.getInfoAffiliation().getDateDebutAffiliation()+" - "+viewBean.getInfoAffiliation().getDateFinCotisation()%>"},
				{"name":"<%=request.getParameter("prefixModel")%>.couvert",
					"value":"<%=viewBean.getInfoAffiliation().getCouvert().booleanValue()?objSession.getLabel("AL0004_AFFILIE_ETAT_ACTIF"):objSession.getLabel("AL0004_AFFILIE_ETAT_INACTIF") %>"},
				{"name":"<%=request.getParameter("prefixModel")%>.periodiciteAffiliation",
					"value":"<%=objSession.getCodeLibelle(viewBean.getInfoAffiliation().getPeriodicitieAffiliation())%>"},
				{"name":"typeAffilie",
					"value":"<%=viewBean.isMaisonMere()?objSession.getLabel("AL0004_AFFILIE_TYPE_MERE"):(viewBean.isSuccursale()?objSession.getLabel("AL0004_AFFILIE_TYPE_SUCCU"):objSession.getLabel("AL0004_AFFILIE_TYPE_NORMAL")) %>"},
				{"name":"<%=request.getParameter("prefixModel")%>.codeCaisseAF",
					"value":"<%=JadeStringUtil.isEmpty(viewBean.getInfoAffiliation().getCodeCaisseProf())?"":viewBean.getInfoAffiliation().getCodeCaisseProf()%>"},
				{"name":"<%=request.getParameter("prefixModel")%>.caisseAlias",
					"value":"TODO"}
			]}
		<%
		}
	}
	%>
	]
		
  }
}
