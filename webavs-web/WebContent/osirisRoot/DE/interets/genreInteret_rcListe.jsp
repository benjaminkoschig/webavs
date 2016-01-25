
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.osiris.translation.CACodeSystem"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
 <%@ page import="globaz.globall.util.*" %>
  <%@ page import="globaz.osiris.db.interets.*" %>
  <%
  CAGenreInteretListViewBean viewBean = (CAGenreInteretListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
  size = viewBean.size();
  CAGenreInteret _genreInteret = null;
  detailLink ="osiris?userAction=osiris.interets.genreInteret.afficher&selectedId=";
  session.setAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT,viewBean);
  %>
 <%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH colspan="2" nowrap><ct:FWLabel key="GCA4030_LIBELLE"/></TH>
    <TH nowrap width="554"><ct:FWLabel key="GCA4030_RUBRIQUE"/></TH>
    <TH nowrap><ct:FWLabel key="GCA4030_TYPE_D_INTERET"/></TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>

  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    _genreInteret = (CAGenreInteret) viewBean.getEntity(i);
		actionDetail = "parent.location.href='"+detailLink+_genreInteret.getIdGenreInteret()+"'";
    %>
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_genreInteret.getIdGenreInteret())%>"/>
	</TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="371"><%=_genreInteret.getLibelleFR()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="554"><%if (_genreInteret.getRubrique() != null) {%><%=_genreInteret.getRubrique().getIdExterne()%> - <%=_genreInteret.getRubrique().getDescription()%><%}%> </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" ><%=CACodeSystem.getLibelle(session,_genreInteret.getIdTypeInteret())%></TD>

    
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>