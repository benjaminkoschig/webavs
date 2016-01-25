<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "naos?userAction=naos.particulariteAffiliation.particulariteAffiliation.afficher&selectedId=";
	menuName="particulariteAffiliation";
	globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationListViewBean viewBean = (globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<%
		
	%>
	<TH width="30">&nbsp;</TH> 
	<TH nowrap align="center">Eigenheit</TH>
	<TH align="center" width="80">Beginn</TH>
	<TH align="center" width="80">Ende</TH>
	<TH align="center">Numerischen Wert</TH>
	<TH align="center">Alphanumerischen Wert</TH>
      <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%    
	   actionDetail = targetLocation + "='" + detailLink + viewBean.getParticulariteId(i)+"'";
	%>
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="AFMenuVide" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getParticulariteId(i)%>"/>
	</TD>		
	<TD class="mtd" onClick="<%=actionDetail%>" align="left"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getParticulariteAffiliation(i))%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" align="center" width="80"><%=viewBean.getDateDebut(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" align="center" width="80"><%=viewBean.getDateFin(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" align="left"><%=!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getChampNumerique(i))?viewBean.getChampNumerique(i):""%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" align="left"><%=viewBean.getChampAlphanumerique(i)%></TD> 
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<TR bgcolor="black">
		<TD colspan="9"></TD>
	</TR>
	<TR>
		<TD class="mtd" align="left">&nbsp;</TD>
		<TD class="mtd" align="left">&nbsp;</TD>
		<TD class="mtd" align="left">&nbsp;</TD>
		<TD class="mtd" align="left">&nbsp;</TD>
		<TD class="mtd" align="right">&nbsp;</TD>
	</TR>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>