<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.pavo.db.splitting.*"%>
<%
	target = "parent.fr_detail";
	targetLocation = target+".location.href";
	detailLink ="phenix?userAction=phenix.principale.commentaireRemarqueType.afficher&selectedId=";

    globaz.phenix.db.principale.CPCommentaireRemarqueTypeListViewBean viewBean = (globaz.phenix.db.principale.CPCommentaireRemarqueTypeListViewBean)request.getAttribute ("viewBean");
    size = viewBean.getSize();
    session.setAttribute("listViewBean",viewBean);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<Th width="16">&nbsp;</Th>
      <TH width="90%" align="left">Mitteilung</TH>
      <TH width="10%">Code</TH>
      <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    	<%
		actionDetail = targetLocation+"='"+detailLink+viewBean.getIdCommentaireRemarque(i)+"'";
		String tmp = detailLink+viewBean.getIdCommentaireRemarque(i);%>
		<TD class="mtd" width="">
     		<ct:menuPopup menu="CP-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
		</TD>
      	<TD class="mtd" onclick="<%=actionDetail%>" width="90%"><%=viewBean.getLibelleCommentaire(i)%>&nbsp;</TD>
      	<TD class="mtd" onclick="<%=actionDetail%>" width="10%"><%=viewBean.getCodeCommentaire(i)%>&nbsp;</TD>
      <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>