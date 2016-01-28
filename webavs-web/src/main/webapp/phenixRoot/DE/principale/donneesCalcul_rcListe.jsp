<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
    globaz.phenix.db.principale.CPDonneesCalculListViewBean viewBean = (globaz.phenix.db.principale.CPDonneesCalculListViewBean )request.getAttribute ("viewBean");
    size = viewBean.size ();
    detailLink ="phenix?userAction=phenix.principale.donneesCalcul.afficher&idDecision="+viewBean.getIdDecision()+"&selectedId=";
    menuName="";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    	<Th nowrap width="16">&nbsp;</Th>
    	<Th width="*">Detail </Th>
	<Th width="20%">Betrag</Th>
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
		<% 
		actionDetail = targetLocation+"='"+detailLink+viewBean.getIdDonneesCalcul(i)+"'";
		String tmp = detailLink+viewBean.getIdDonneesCalcul(i);
		%>
    	<TD class="mtd" width="">
     		<ct:menuPopup menu="CP-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
    	</TD>
      	<TD class="mtd" onClick="<%=actionDetail%>" width="*"><%=viewBean.getLibelleDonnee(i)%></TD>
      	<TD class="mtd" onclick="<%=actionDetail%>" width="20%" align="right"><%=viewBean.getMontantCalcul(i)%></TD>
      <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>