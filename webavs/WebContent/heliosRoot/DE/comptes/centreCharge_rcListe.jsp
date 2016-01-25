
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@ page import="globaz.helios.db.comptes.*, globaz.framework.util.*" %>
<%
    	CGCentreChargeListViewBean viewBean = (CGCentreChargeListViewBean)request.getAttribute ("viewBean");
    	size =viewBean.getSize();
    	detailLink ="helios?userAction=helios.comptes.centreCharge.afficher&selectedId=";
%>


<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

<Th width="16">&nbsp;</Th>

<Th width="">Nummer</Th>
<Th width="">Bezeichnung</Th>

<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	CGCentreCharge entity = (CGCentreCharge)viewBean.getEntity(i);
	actionDetail = "parent.location.href='"+detailLink+entity.getIdCentreCharge()+"&idMandat="+viewBean.getForIdMandat()+"'";
	String tmp = detailLink+entity.getIdCentreCharge()+"&idMandat="+viewBean.getForIdMandat();
%>
     <TD class="mtd" width="">
     <ct:menuPopup menu="CG-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=tmp%>"/>
     </TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getIdCentreCharge()%>&nbsp;</TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=entity.getLibelle()%>&nbsp;</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>