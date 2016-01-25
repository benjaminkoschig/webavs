<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_LOT_L"	

	globaz.corvus.vb.documents.RECopiesListViewBean viewBean = ( globaz.corvus.vb.documents.RECopiesListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "corvus?userAction=corvus.documents.copies.afficher&selectedId=";
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <%@page import="globaz.corvus.vb.documents.RECopiesViewBean"%>
<TH>&nbsp;</TH>
	<TH><ct:FWLabel key="JSP_LOT_L_DESCRIPTION"/></TH>
	<TH><ct:FWLabel key="JSP_COPIES_DATE_DEBUT"/></TH>
	<TH><ct:FWLabel key="JSP_COPIES_DATE_FIN"/></TH>
    <TH><ct:FWLabel key="JSP_LOT_L_NO_LOT"/></TH> 
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    	globaz.corvus.vb.documents.RECopiesViewBean line = (globaz.corvus.vb.documents.RECopiesViewBean) viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getId()+"'";
	%>
	
			<TD class="mtd" width=""></TD>
			<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getTiersCopiesAInfo(objSession)%>&nbsp;</TD>
			<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateDebutCopie()%>&nbsp;</TD>
			<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateFinCopie()%>&nbsp;</TD>
			<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getIdCopie()%>&nbsp;</TD>
						
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>