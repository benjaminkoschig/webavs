
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.aquila.db.batch.COSequenceListViewBean"%>
<%@page import="globaz.aquila.db.batch.COSequenceViewBean"%>
<%
  COSequenceListViewBean viewBean = (COSequenceListViewBean)session.getAttribute("listViewBean"); 
  COSequenceViewBean _sequence = null;
  size = viewBean.size();
  detailLink ="aquila?userAction=aquila.batch.sequence.afficher&selectedId=";

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
	<TH colspan="2" nowrap>Nummer</TH>
    <TH nowrap width="554">Bezeichnung</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
 <%-- tpl:put name="zoneCondition" --%> 
 
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%> 
<%
	
	_sequence = (COSequenceViewBean) viewBean.getEntity(i); 
	actionDetail = "parent.location.href='"+detailLink+_sequence.getIdSequence()+"'";
	
%>   
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_sequence.getIdSequence())%>"/>	    	
	</TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" ><%=_sequence.getIdSequence()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" ><%=_sequence.getLibSequenceLibelle()%></TD>
    
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>