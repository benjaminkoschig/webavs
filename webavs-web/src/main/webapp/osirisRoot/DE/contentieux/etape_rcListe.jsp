
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
  <%@ page import="globaz.globall.util.*" %>
  <%@ page import="globaz.osiris.db.contentieux.*" %>
  <%
globaz.osiris.db.contentieux.CAEtapeManagerListViewBean viewBean  = 
  (globaz.osiris.db.contentieux.CAEtapeManagerListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
globaz.osiris.db.contentieux.CAEtape _etape = null ; 
size = viewBean.size();
detailLink ="osiris?userAction=osiris.contentieux.etape.afficher&selectedId=";
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
    	_etape = (globaz.osiris.db.contentieux.CAEtape) viewBean.getEntity(i); 
		actionDetail = "parent.location.href='"+detailLink+_etape.getIdEtape()+"'";
    %>
<!--    <TD width="80"><a href="<%=request.getContextPath()%>/osiris?userAction=osiris.contentieux.etape.afficher&id=<%=_etape.getIdEtape()%>" target="fr_main"><img src="<%=request.getContextPath()%>/images/loupe.gif" border="0"></a></TD>-->
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_etape.getIdEtape())%>"/>		
	</TD>    
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="371"><%=_etape.getIdEtape()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="554"><%=_etape.getDescription()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>