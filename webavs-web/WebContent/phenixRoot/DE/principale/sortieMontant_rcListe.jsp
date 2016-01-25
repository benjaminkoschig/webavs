
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%
    globaz.phenix.db.principale.CPSortieMontantListViewBean viewBean = (globaz.phenix.db.principale.CPSortieMontantListViewBean)request.getAttribute ("viewBean");
    request.getAttribute("viewBean");
    size = viewBean.size ();
    detailLink ="phenix?userAction=phenix.principale.sortieMontant.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <Th nowrap width="16">&nbsp;</Th>
    <TH>Versicherung</TH>
    <TH>Betrag</TH>

    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%
		actionDetail = targetLocation +"='" + detailLink + viewBean.getIdSortieMontant(i)+"&idSortie=" + viewBean.getForIdSortie()+"'";
		String style = "";
   		String tmp = detailLink+viewBean.getIdSortieMontant(i)+"&idSortie=" + viewBean.getForIdSortie();%>
    	<TD class="mtd" width="">
     		<ct:menuPopup menu="CP-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
		</TD>

		<%if(viewBean.getAssurance(i)!= null){%>
			<TD class="mtd" <%=style%> onClick="<%=actionDetail%>" width="50%"><%=viewBean.getAssurance(i)%></TD>
		<%}%>
		
    	<%if(viewBean.getMontant(i)!= null){%>
			<TD class="mtd" <%=style%> onclick="<%=actionDetail%>" width="50%" align="right"><%=viewBean.getMontant(i)%></TD>
		<%}%>
    	
    
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>