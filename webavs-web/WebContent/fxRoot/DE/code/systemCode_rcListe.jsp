<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
	<%
		globaz.fx.code.client.bean.FXSystemCodeListViewBean viewBean = (globaz.fx.code.client.bean.FXSystemCodeListViewBean) request.getAttribute("viewBean");
	 	size = viewBean.getSize ();
    	detailLink ="fx?userAction=fx.code.systemCode.afficher&selectedId=";
    	menuName="systemCode"; 
    		
	%>
	
	

	

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <th >&nbsp;</th>
    	<th >Reihenfolge</th>
    	<th >Code</th>
    	<th >Bezeichnung</th>
    	<th >Werte</th>
    	
    	<th >Aktiv</th>
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
		<ct:menuPopup menu="optionsCode" detailLabel="Detail" detailLink="<%=menuDetailLink%>">
		 <ct:menuParam key="selectedId" value="<%=viewBean.getIdCodes()[i]%>"/>
		</ct:menuPopup>
    </TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getOrders()[i]%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getIdCodes()[i]%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getLibelles()[i]%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><pre style="margin:0 0 0 0;font-family:verdana;"><%=viewBean.getTraductions()[i]%></pre></TD>
	
	
	<TD class="mtd" onClick="<%=actionDetail%>" >
		<%if (viewBean.getActifs()[i].booleanValue()) {%>
			<img src="<%=request.getContextPath()%>/images/ok.gif">
		<%}else {%>
			<img src="<%=request.getContextPath()%>/images/erreur.gif">
		<%}%>
	</TD>
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


	