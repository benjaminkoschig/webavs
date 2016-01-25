
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@ page import="globaz.helios.db.consolidation.*, globaz.framework.util.*" %>
<%
CGSuccursaleListViewBean viewBean = (CGSuccursaleListViewBean) session.getAttribute ("listViewBean");
size =viewBean.getSize();
detailLink ="helios?userAction=helios.consolidation.succursale.afficher&selectedId=";
%>


<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

<TH width="10">&nbsp;</TH>
<TH width="20%">Num&eacute;ro de succursale</TH>
<TH>Nom</TH>

<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	CGSuccursaleViewBean entity = (CGSuccursaleViewBean) viewBean.getEntity(i);
	actionDetail = "parent.location.href='"+detailLink+entity.getIdSuccursale()+"'";
	String tmp = detailLink+entity.getIdSuccursale();
%>
     <TD class="mtd" width="10">
     <ct:menuPopup menu="CG-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
     </TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getNumeroSuccursale()%>&nbsp;</TD>     
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getNom()%>&nbsp;</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>