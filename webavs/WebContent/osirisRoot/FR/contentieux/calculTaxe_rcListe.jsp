
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
  <%@ page import="globaz.globall.util.*" %>
  <%@ page import="globaz.osiris.db.contentieux.*" %>
<%
globaz.osiris.db.contentieux.CACalculTaxeManagerListViewBean viewBean =
  (globaz.osiris.db.contentieux.CACalculTaxeManagerListViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
globaz.osiris.db.contentieux.CACalculTaxe _calculTaxe = null ; 
size = viewBean.size();
detailLink ="osiris?userAction=osiris.contentieux.calculTaxe.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <TH colspan="2" nowrap>Num&eacute;ro</TH>
    <TH nowrap width="554">Libell&eacute;</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> 
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%	
    	_calculTaxe = (globaz.osiris.db.contentieux.CACalculTaxe) viewBean.getEntity(i); 
    	actionDetail = "parent.location.href='"+detailLink+_calculTaxe.getIdCalculTaxe()+"'";
    %>
<!--    <TD width="80"><a href="<%=request.getContextPath()%>/osiris?userAction=osiris.contentieux.calculTaxe.afficher&id=<%=_calculTaxe.getIdCalculTaxe()%>" target="fr_main"><img src="<%=request.getContextPath()%>/images/loupe.gif" border="0"></a></TD>-->
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_calculTaxe.getIdCalculTaxe())%>"/>
	</TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="371"><%=_calculTaxe.getIdCalculTaxe()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="554"><%=_calculTaxe.getDescription()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>