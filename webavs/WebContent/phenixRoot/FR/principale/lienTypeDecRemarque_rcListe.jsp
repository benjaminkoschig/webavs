<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.pavo.db.splitting.*"%>
<%
	target = "parent.fr_detail";
	targetLocation = target+".location.href";
	detailLink ="phenix?userAction=phenix.principale.lienTypeDecRemarque.afficher&selectedId=";

    globaz.phenix.db.principale.CPLienTypeDecRemarqueListViewBean viewBean = (globaz.phenix.db.principale.CPLienTypeDecRemarqueListViewBean )request.getAttribute ("viewBean");
    size = viewBean.getSize();
    session.setAttribute("listViewBean",viewBean);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<Th width="16">&nbsp;</Th>
      <TH width="90%" align="left">Type de décision</TH>
      <TH width="10%">Code</TH>
      <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
	actionDetail = targetLocation+"='"+detailLink+viewBean.getIdLienTypeRemarque(i)+"'";
	String tmp = detailLink+viewBean.getIdLienTypeRemarque(i);%>
	<TD class="mtd" width="">
     	<ct:menuPopup menu="CP-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
	</TD>
    <TD class="mtd" onclick="<%=actionDetail%>" width="90%"><%=viewBean.getLibelleTypeDecision(i)%>&nbsp;</TD>
    <TD class="mtd" onclick="<%=actionDetail%>" width="10%"><%=viewBean.getCodeTypeDecision(i)%>&nbsp;</TD>
      <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>