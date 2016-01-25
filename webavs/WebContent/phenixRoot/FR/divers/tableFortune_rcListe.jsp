 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
 <%
	globaz.phenix.db.divers.CPTableFortuneListViewBean viewBean = (globaz.phenix.db.divers.CPTableFortuneListViewBean)request.getAttribute ("viewBean");
	size = viewBean.size ();
	session.setAttribute("listViewBean",viewBean);
    	detailLink ="phenix?userAction=phenix.divers.tableFortune.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
     <Th nowrap width="16">&nbsp;</Th>
  
    <Th nowrap width="10%">Année</Th>
      
    <Th width="25%">Canton</Th>
      
     <TH width="20%">Taux agricole</TH>
      <TH width="25%">Taux non agricole</TH>
	 <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
	actionDetail = "parent.location.href='"+detailLink+viewBean.getIdTableFortune(i)+"'";
	String tmp = detailLink+viewBean.getIdTableFortune(i);%>
     <TD class="mtd" width="">
     	<ct:menuPopup menu="CP-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
     </TD>
      <TD class="mtd" width="20%" onclick="<%=actionDetail%>" align="center"><%=viewBean.getAnneeFortune(i)%></TD>
      <TD class="mtd" width="40%" onclick="<%=actionDetail%>" align="left"><%=viewBean.getCanton(i)%></TD>
      <TD class="mtd" width="40%" onclick="<%=actionDetail%>" align="right"><%=viewBean.getTauxAgricole(i)%></TD>
      <TD class="mtd" width="40%" onclick="<%=actionDetail%>" align="right"><%=viewBean.getTauxNonAgricole(i)%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>