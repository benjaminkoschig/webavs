<%@page import="globaz.al.vb.ajax.ALParametresMetierListViewBean"%>
<%@page import="globaz.fweb.taglib.FWCodeSelectTag"%>
<%@page import="globaz.jade.client.util.JadeCodesSystemsUtil"%>
<%@page import="globaz.al.vb.ajax.ALDossiersLiesListViewBean"%>
<%@page import="ch.globaz.al.business.constantes.ALCSPays"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Map.Entry"%>


<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/plain;charset=UTF-8" %>
<%-- page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=UTF-8" --%>
<%-- page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=UTF-8" --%>
<%-- taglib uri="/WEB-INF/taglib.tld" prefix="ct" --%>
	<%
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	ALParametresMetierListViewBean viewBean = (ALParametresMetierListViewBean) request.getAttribute("viewBean");
	%>
	
{"search":{
	"typeSearch" :	"<%=((Object)viewBean.getParameterModelResult(0)).getClass()%>",
	"totalResults" : "<%=viewBean.getSize()%>",
	"results" : [
		<% for(int i=0;i<viewBean.getSize();i++){%>
		{
		"id_param":"<%=viewBean.getParameterModelResult(i).getIdParam()%>",	
		"id_appli":"<%=viewBean.getParameterModelResult(i).getIdApplParametre()%>",
        "param_nom":"<%=viewBean.getParameterModelResult(i).getIdCleDiffere()%>",
		"param_description":"<%=viewBean.getParameterModelResult(i).getDesignationParametre()%>",
		"param_debut_validite":"<%=viewBean.getParameterModelResult(i).getDateDebutValidite()%>",
		"param_val_num":"<%=viewBean.getParameterModelResult(i).getValeurNumParametre()%>",
		"param_val_alpha":"<%=viewBean.getParameterModelResult(i).getValeurAlphaParametre()%>"
		}
	<% if(i!=viewBean.getSize()-1){%>,<%}%>
		<%}%>
		]
	}
}
	
	