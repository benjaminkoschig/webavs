<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
		globaz.fx.user.client.bean.FXGroupListViewBean viewBean = (globaz.fx.user.client.bean.FXGroupListViewBean) request.getAttribute("viewBean");
	 	size = viewBean.getSize ();
    	detailLink ="fx?userAction=fx.user.group.afficher&selectedId=";	
	%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
      <th >&nbsp;</th>
    	<th >Name</th>
    	<th >Beschreibung</th>
    
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>

<%
	actionDetail = "parent.location.href='"+detailLink+java.net.URLEncoder.encode(viewBean.getIds()[i],"iso-8859-1")+"'";
	String menuDetailLink = request.getContextPath() + "/" + detailLink + java.net.URLEncoder.encode(viewBean.getIds()[i],"iso-8859-1");
%>

    <TD class="mtd" width="16" >
<%--    <ct:FWOptionSelectorTag name="<%=\"item\"+i%>" selectedId="<%=viewBean.getIds()[i]%>"/><%=(i<9)?"<span style='background:#b0c0e0;border:solid 1 black ;margin-left:4px'>"+(i+1)+"</span>":""%>--%>
		<ct:menuPopup menu="optionsGroup" detailLabel="Detail" detailLink="<%=menuDetailLink%>">
		 <ct:menuParam key="selectedId" value='<%=java.net.URLEncoder.encode(viewBean.getIds()[i],"iso-8859-1")%>'/>
		 <ct:menuParam key="forIdGroup" value='<%=java.net.URLEncoder.encode(viewBean.getIds()[i],"iso-8859-1")%>'/>
		</ct:menuPopup>
    </TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getIds()[i]%>&nbsp;</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" ><%=viewBean.getDescriptions()[i]%>&nbsp;</TD>
	
	
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>