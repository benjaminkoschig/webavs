<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "naos?userAction=naos.couverture.couverture.afficher&selectedId=";
	menuName="Couverture";
	globaz.naos.db.couverture.AFCouvertureListViewBean viewBean = (globaz.naos.db.couverture.AFCouvertureListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<%
		
	%>
	<TH width="30">&nbsp;</TH>
	<TH width="350">Versicherung</TH>
	<TH width="100" align="center">Beginndatum</TH>
	<TH width="100" align="center">Enddatum</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
	    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%    
		actionDetail = targetLocation + "='" + detailLink + viewBean.getCouvertureId(i) + "'";
	%>
	<TD class="mtd" width="30" >

	<ct:menuPopup menu="AFOptionsCouverture" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getCouvertureId(i)%>">
		<ct:menuParam key="couvertureId" value="<%=viewBean.getCouvertureId(i)%>"/>
	</ct:menuPopup>
	</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="350"><%=viewBean.getAssurance(i).getAssuranceLibelle()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="100" align="center"><%=viewBean.getDateDebut(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="100" align="center"><%=viewBean.getDateFin(i)%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>