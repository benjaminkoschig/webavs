<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
	<%
		globaz.fx.code.client.bean.FXParamListViewBean  viewBean = (globaz.fx.code.client.bean.FXParamListViewBean) request.getAttribute("viewBean");
	 	size = viewBean.getSize ();
    	detailLink ="fx?userAction=fx.code.param.afficher&";
    	menuName="param"; 
    		
	%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	   
    	<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
		<th>Designation</th>
    	<th>Clé</th>
    	<th>Date de début de validité</th>
		<th>Valeur de début de plage</th>
		<th>Valeur de fin de plage</th>
		<th>Valeur (numerique)</th>
		<th>Valeur (alpha.)</th>
		<!-- 
		<th>Unite</th>
    	
    	<th>Lié au Code</th>
    	<th>Acteur</th>
    	-->
    	
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>

<%
	Map keyMap = new HashMap();
	
	actionDetail = "parent.location.href='" + detailLink
	+ "&module=" + viewBean.getModules()[i]
	+ "&idCle=" + viewBean.getIdCles()[i]
	+ "&idCodeSystem=" + viewBean.getIdCodeSystems()[i]
	+ "&plageValeurDebut=" + viewBean.getPlageValeurDebuts()[i]
	+ "&dateDebutValidite=" + viewBean.getDateDebutValidites()[i]
   	+ "&idActeur=" + viewBean.getIdActeurs()[i]
	+ "'";
	String menuDetailLink = ""; //request.getContextPath() + "/" + detailLink + viewBean.getIdCodes()[i];
%>

	<TD class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getDesignations()[i]%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getIdCles()[i]%>&nbsp;</TD>
	<TD align="center" class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getDateDebutValidites()[i]%>&nbsp;</TD>
	<TD align="right" class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getPlageValeurDebuts()[i]%>&nbsp;</TD>
	<TD align="right" class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getPlageValeurFins()[i]%>&nbsp;</TD>
	<TD align="right" class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getValeurNumeriques()[i]%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getValeurAlphas()[i]%>&nbsp;</TD>
	<!-- 
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getUnites()[i]%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><b><%=viewBean.getCodeSystemLabels()[i]+"</b><br>"+viewBean.getIdCodeSystems()[i]%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getIdActeurs()[i]%>&nbsp;</TD>
	
	 -->
	
	
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>