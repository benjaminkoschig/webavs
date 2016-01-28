
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.osiris.translation.CACodeSystem"%>
<%@ page language="java"  %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
 <%@ page import="globaz.globall.util.*" %>
  <%@ page import="globaz.osiris.db.interets.*" %>
  <%
  CARubriqueSoumiseInteretListViewBean viewBean = (CARubriqueSoumiseInteretListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
  size = viewBean.size();
  CARubriqueSoumiseInteret _rubriqueSoumiseInteret = null;
  detailLink ="osiris?userAction=osiris.interets.rubriqueSoumiseInteret.afficher&selectedId=";
  session.setAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT,viewBean);
  %>
 <%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH colspan="2" nowrap><ct:FWLabel key="GCA4034_RUBRIQUE"/></TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>

  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    _rubriqueSoumiseInteret = (CARubriqueSoumiseInteret) viewBean.getEntity(i);
		actionDetail = "parent.location.href='"+detailLink+_rubriqueSoumiseInteret.getIdRubrique()+"'";
    %>
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_rubriqueSoumiseInteret.getIdRubrique())%>"/>
	</TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="554"><%if (_rubriqueSoumiseInteret.getRubrique() != null) {%><%=_rubriqueSoumiseInteret.getRubrique().getIdExterne()%> - <%=_rubriqueSoumiseInteret.getRubrique().getDescription()%><%}%>
    
</TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>