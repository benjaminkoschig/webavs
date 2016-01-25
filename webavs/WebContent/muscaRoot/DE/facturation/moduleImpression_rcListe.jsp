<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.musca.db.facturation.FAModuleImpressionListViewBean viewBean = (globaz.musca.db.facturation.FAModuleImpressionListViewBean)request.getAttribute ("viewBean");
	size = viewBean.size();
	detailLink = "musca?userAction=musca.facturation.moduleImpression.afficher&selectedId=";
	menuName = "moduleImpression-detail";

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
      <th width="20">&nbsp;</th>
      <th  width="8%">Job-Nr.</th>
      <TH  width="*">Beschreibung</TH>
      <th  width="20%">Abrechnungstyp</th>
      <th  width="20%">Eintreibungsart</th>
   
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	actionDetail = targetLocation + "='" + detailLink + viewBean.getIdModuleImpression(i) + "'";
	String detailAction = detailLink + viewBean.getIdModuleImpression(i);
%>
      <TD class="mtd" width="20" >
      <ct:menuPopup menu="FA-Detail" labelId="OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailAction%>" />
      </TD>
      <TD class="mtd" width="8%" onClick="<%=actionDetail%>"><%=viewBean.getIdModuleImpression(i)%></TD>
      <TD class="mtd" width="*" onClick="<%=actionDetail%>"><%=viewBean.getLibelleModule(i)%></TD>
      <TD class="mtd" width="20%" onClick="<%=actionDetail%>"><%=viewBean.getLibelleType(i)%></TD>
      <TD class="mtd" width="20%" onClick="<%=actionDetail%>"><%=viewBean.getLibelleMode(i)%></TD>


<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>