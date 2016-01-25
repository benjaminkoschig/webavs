 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
  <%
	globaz.phenix.db.divers.CPTableNonActifListViewBean viewBean = (globaz.phenix.db.divers.CPTableNonActifListViewBean)request.getAttribute ("viewBean");
	size = viewBean.size ();
	session.setAttribute("listViewBean",viewBean);
    	detailLink ="phenix?userAction=phenix.divers.tableNonActif.afficher&selectedId=";
  %>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <Th nowrap width="16">&nbsp;</Th>
    <Th nowrap width="10%">Année</Th>
    <Th width="25%">Revenu</Th>
    <TH width="20%">Cotisation annuelle</TH>
    <TH width="25%">Cotisation mensuelle</TH>
    <TH width="20%">Revenu CI</TH>
      <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
	actionDetail = "parent.location.href='"+detailLink+viewBean.getIdTableNonActif(i)+"'";
	String tmp = detailLink+viewBean.getIdTableNonActif(i);%>
    <TD class="mtd" width="">
     	<ct:menuPopup menu="CP-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
    </TD>
     <TD class="mtd" width="10%" onclick="<%=actionDetail%>" align="right"><%=viewBean.getAnneeVigueur(i)%></TD>
     <TD class="mtd" width="25%" onclick="<%=actionDetail%>" align="right"><%=viewBean.getRevenu(i)%></TD>
     <TD class="mtd" width="20%" onclick="<%=actionDetail%>" align="right"><%=viewBean.getCotisationAnnuelle(i)%></TD>
     <TD class="mtd" width="25%" onclick="<%=actionDetail%>" align="right"><%=viewBean.getCotisationMensuelle(i)%></TD>
     <TD class="mtd" width="20%" onclick="<%=actionDetail%>" align="right"><%=viewBean.getRevenuCi(i)%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>