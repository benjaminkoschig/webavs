<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
	<%
		globaz.fx.user.client.bean.FXUserListViewBean viewBean = (globaz.fx.user.client.bean.FXUserListViewBean) request.getAttribute("viewBean");
	 	size = viewBean.getSize ();
    	detailLink ="fx?userAction=fx.user.user.afficher&selectedId=";
    	menuName="user"; 
    		
	%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <th >&nbsp;</th>
    	<th >Visa</th>
    	<th >Nom</th>
    	<th >Prénom</th>
    	<th >Email</th>
    	
    	
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>

<%
	actionDetail = "parent.location.href='"+detailLink+viewBean.getIds()[i]+"'";
	String menuDetailLink = request.getContextPath() + "/" + detailLink + viewBean.getIds()[i];
%>

    <TD class="mtd" width="16" >
<%--    <ct:FWOptionSelectorTag name="<%=\"item\"+i%>" selectedId="<%=viewBean.getIds()[i]%>"/><%=(i<9)?"<span style='background:#b0c0e0;border:solid 1 black ;margin-left:4px'>"+(i+1)+"</span>":""%>--%>
		<ct:menuPopup menu="optionsUser" detailLabel="Detail" detailLink="<%=menuDetailLink%>">
		 <ct:menuParam key="selectedId" value="<%=viewBean.getIds()[i]%>"/>
		 <ct:menuParam key="forIdUser" value="<%=viewBean.getIds()[i]%>"/>
		</ct:menuPopup>
    </TD>
 	<TD class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getVisas()[i]%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getLastNames()[i]%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getFirstNames()[i]%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getEmails()[i]%>&nbsp;</TD>
	
	
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>