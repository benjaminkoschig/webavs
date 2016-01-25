<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.musca.db.facturation.FAOrdreAttribuerListViewBean viewBean = (globaz.musca.db.facturation.FAOrdreAttribuerListViewBean)request.getAttribute ("viewBean");
	size = viewBean.size();
	detailLink = "musca?userAction=musca.facturation.ordreAttribuer.afficher&selectedId=";
	menuName = "ordreAttribuer-detail";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
      <th width="20">&nbsp;</th>
      <th  width="10%">Auftrag</th>
      <th  width="40%">Beschreibung</th>
	  <th width ="30%">Rubrik</th>                
      <th  width="*">Kassen-Nr.</th>
      
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	actionDetail = targetLocation + "='" + detailLink + viewBean.getIdOrdreRegroupement(i) + "&selectedId2=" + viewBean.getIdRubrique(i) + "&selectedId3=" + viewBean.getIdOrdreAttribuer(i) + "'";
	String detailAction = detailLink + viewBean.getIdOrdreRegroupement(i)+ "&selectedId2=" + viewBean.getIdRubrique(i) + "&selectedId3=" + viewBean.getIdOrdreAttribuer(i);
%>
    <TD class="mtd" width="20" >
		<ct:menuPopup menu="FA-Detail" labelId="OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailAction%>" />
    </TD>      
	<TD class="mtd" width="10%" onClick="<%=actionDetail%>"><%=viewBean.getNumOrdreRegroupement(i)%></TD>
    <TD class="mtd" width="40%" onClick="<%=actionDetail%>"><center><%=viewBean.getLibelle(i)%></center></TD>
 	<TD class="mtd" width="30%" align="right" onClick="<%=actionDetail%>"><%=viewBean.getIdExterneRubrique(i)%></TD>
 	<TD class="mtd" width="*" align="right" onClick="<%=actionDetail%>"><%=viewBean.getNumCaisse(i)%></TD>
     


<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>