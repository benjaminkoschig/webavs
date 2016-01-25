 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
 <%
	globaz.phenix.db.divers.CPPeriodeFiscaleListViewBean viewBean = (globaz.phenix.db.divers.CPPeriodeFiscaleListViewBean)request.getAttribute ("viewBean");
	size = viewBean.size ();
	session.setAttribute("listViewBean",viewBean);
    	detailLink ="phenix?userAction=phenix.divers.periodeFiscale.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
     <Th nowrap width="16">&nbsp;</Th>
    <Th nowrap width="25%">DBST-Nr.</Th>
      
    <Th width="25%">Erstes Einkommen</Th>
      
     <TH width="25%">Letztes Einkommen</TH>
     <TH width="25%">Verfügungsjahr</TH>

      <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
		actionDetail = "parent.location.href='"+detailLink+viewBean.getIdIfd(i)+"&colonneSelection="+viewBean.getColonneSelection()+"'";
		String tmp = detailLink+viewBean.getIdIfd(i);%>
		<TD class="mtd" width="">
			<ct:menuPopup menu="CP-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
		</TD>
		<TD class="mtd" width="25%" onclick="<%=actionDetail%>" align="right"><%=viewBean.getNumIfd(i)%></TD>
		<TD class="mtd" width="25%" onclick="<%=actionDetail%>" align="right"><%=viewBean.getAnneeRevenuDebut(i)%></TD>
		<TD class="mtd" width="25%" onclick="<%=actionDetail%>" align="right"><%=viewBean.getAnneeRevenuFin(i)%></TD>
		<TD class="mtd" width="25%" onclick="<%=actionDetail%>" align="right"><%=viewBean.getAnneeDecision(i)%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>