
<%@page import="ch.globaz.pyxis.business.model.AdministrationComplexModel"%>
<%@page import="ch.globaz.pyxis.business.model.AdministrationSearchComplexModel"%>
<%@page import="ch.globaz.pyxis.business.service.TIBusinessServiceLocator"%>
<%@page import="ch.globaz.naos.business.service.AFBusinessServiceLocator"%><%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.al.vb.ajax.ALAdministrationListViewBean"%>

<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/plain;charset=UTF-8" %>
<%-- page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=UTF-8" --%>
<%-- page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=UTF-8" --%>
<%-- taglib uri="/WEB-INF/taglib.tld" prefix="ct" --%>
	<%
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();	
	ALAdministrationListViewBean viewBean = (ALAdministrationListViewBean) request.getAttribute("viewBean");
	
	%>
	
{"search":{
	"typeSearch" :	"<%=viewBean.getAdministrationComplexResult(0).getClass()%>",
	"criteria" :	"TODO",
	"totalResults" : "<%=viewBean.getSize()%>",
	"results" : [
	
	<% 
	if(viewBean.getSize()>0){
		
		for(int i=0;i<viewBean.getSize();i++){%>

			{	"properties" : [
				{"name":"id",
					"value":"<%=viewBean.getAdministrationComplexResult(i).getAdmin().getId()%>"},
				{"name":"<%=request.getParameter("prefixModel")%>.codeAdministration",
					"value":"<%=viewBean.getAdministrationComplexResult(i).getAdmin().getCodeAdministration()%>"},
				{"name":"caisseAlias","persistence":"no",
					"value":"TODO"},			
				{"name":"suggestOptionSelect","persistence":"no",
					"value":"<%="<option value='"+viewBean.getAdministrationComplexResult(i).getAdmin().getCodeAdministration()+"'>"+viewBean.getAdministrationComplexResult(i).getAdmin().getCodeAdministration()+"</option>"%>"}			
			]}<% if(i!=viewBean.getSize()-1){%>,<%}%> 
		<%	
		}
	}
	
	%>
	]
		
  }
}
