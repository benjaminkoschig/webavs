<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
	<%
		globaz.fx.code.client.bean.FXTypeCodeListViewBean viewBean = (globaz.fx.code.client.bean.FXTypeCodeListViewBean) request.getAttribute("viewBean");
	 	size = viewBean.getSize ();
    	detailLink ="fx?userAction=fx.code.typeCode.afficher&selectedId=";
    	menuName="typeCode"; 
    		
	%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <th >&nbsp;</th>
    	<th >Familie</th>
    	<th >Bezeichnung</th>
    	<th >Veränderbar</th>
    	
    	
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>

<%
	actionDetail = "parent.location.href='"+detailLink+viewBean.getIdCodes()[i]+"'";
	String menuDetailLink = request.getContextPath() + "/" + detailLink + viewBean.getIdCodes()[i];
%>

    <TD class="mtd" width="16" >
<%--    <ct:FWOptionSelectorTag name="<%=\"item\"+i%>" selectedId="<%=viewBean.getIdCodes()[i]%>"/><%=(i<9)?"<span style='background:#b0c0e0;border:solid 1 black ;margin-left:4px'>"+(i+1)+"</span>":""%>--%>
		<ct:menuPopup menu="optionsCodeType" detailLabel="Detail" detailLink="<%=menuDetailLink%>">
		 <ct:menuParam key="selectedId" value="<%=viewBean.getIdCodes()[i]%>"/>
		</ct:menuPopup>
    </TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getGroups()[i]%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getLibelles()[i]%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" >
		<%if (viewBean.getDroitMutations()[i].booleanValue()) {%>
			<img src="<%=request.getContextPath()%>/images/cadenas_ouvert.gif">
		<%}else {%>
			<img src="<%=request.getContextPath()%>/images/cadenas.gif">
		<%}%>
	</TD>
	
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>