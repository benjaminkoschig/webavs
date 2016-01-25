<%try{%>	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.musca.db.facturation.FAOrdreRegroupementListViewBean viewBean = (globaz.musca.db.facturation.FAOrdreRegroupementListViewBean)request.getAttribute ("viewBean");
	size = viewBean.size();
	detailLink = "musca?userAction=musca.facturation.ordreRegroupement.afficher&selectedId=";
	menuName = "ordreRegroupement-detail";
	session.setAttribute("viewBean",viewBean);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
      
<%@page import="globaz.musca.translation.CodeSystem"%><th width="20">&nbsp;</th>
      <th  width="5%">Ordre</th>
      <th  width="50%">Libell�</th>
      <th  width="15%">Num�ro de caisse</th>
      <th  width="*">Nature</th>
      <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
		actionDetail = targetLocation + "='" + detailLink + viewBean.getIdOrdreRegroupement(i) + "'";
		String detailAction = detailLink + viewBean.getIdOrdreRegroupement(i);
	%>
      <TD class="mtd" width="20" >
		<ct:menuPopup menu="FA-Detail" labelId="OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailAction%>" />
      </TD>
      <TD class="mtd" width="10%" align="center" onClick="<%=actionDetail%>"><%=viewBean.getOrdreRegroupement(i)%></TD>
      <TD class="mtd" width="40%" onClick="<%=actionDetail%>"><%=viewBean.getLibelleFR(i)%></TD>
      <TD class="mtd" width="5%" onClick="<%=actionDetail%>"><%=viewBean.getNumCaisse(i)%></TD>
      <TD class="mtd" width="*" onClick="<%=actionDetail%>"><%=CodeSystem.getLibelle(session, viewBean.getNature(i))%></TD>

      <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%><%}catch (Exception e){e.printStackTrace();}%>