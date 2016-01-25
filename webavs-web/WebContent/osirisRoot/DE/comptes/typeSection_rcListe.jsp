
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
 <%@ page import="globaz.globall.util.*" %>
  <%@ page import="globaz.osiris.db.comptes.*" %>
  <%
CATypeSectionManagerListViewBean viewBean = (CATypeSectionManagerListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
size = viewBean.size();
CATypeSection _typeSection = null;
detailLink ="osiris?userAction=osiris.comptes.typeSection.afficher&selectedId=";
session.setAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT,viewBean);
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
    	_typeSection = (globaz.osiris.db.comptes.CATypeSection) viewBean.getEntity(i); 
		actionDetail = "parent.location.href='"+detailLink+_typeSection.getIdTypeSection()+"'";
    %>
<!--    <TD width="80"><a href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.typeSection.afficher&id=<%=_typeSection.getIdTypeSection()%>" target="fr_main"><img src="<%=request.getContextPath()%>/images/loupe.gif" border="0"></a></TD>-->
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_typeSection.getIdTypeSection())%>"/>	    		
	</TD>    
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="371"><%=_typeSection.getIdTypeSection()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="554"><%=_typeSection.getDescription()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>