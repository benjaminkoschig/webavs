<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
	 globaz.leo.db.parametrage.LEFormuleListViewBean viewBean =  (globaz.leo.db.parametrage.LEFormuleListViewBean)request.getAttribute("viewBean");
	 size = viewBean.getSize();
	 detailLink = "leo?userAction=leo.parametrage.formule.afficher&selectedId=";
	 menuName="admin-formule";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>

		<TH>&nbsp;</TH>
    	<TH>Libellé</TH>
    	<TH>Nom Classe</TH>
    	<TH>Manuel</TH>
    	<!-- <TH>Nom</TH> -->
    	
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

	<%
		globaz.leo.db.parametrage.LEFormuleViewBean line = (globaz.leo.db.parametrage.LEFormuleViewBean)viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdFormule()+"'";	
	%>
    <TD class="mtd" width="16">

		<ct:menuPopup menu="LE-formuleDetail" label="<%=optionsPopupLabel%>" target="top.fr_main">
			<ct:menuParam key="selectedId" value="<%=line.getIdFormule()%>"/>
		</ct:menuPopup>
	</TD>
	<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getDefinitionFormule().getCsDocument())?"&nbsp;":viewBean.getSession().getCodeLibelle(line.getDefinitionFormule().getCsDocument())%></TD>	
	<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getClasseName())?"&nbsp;":line.getClasseName()%></TD>		
	<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getCsManuAuto())?"&nbsp;":viewBean.getSession().getCodeLibelle(line.getCsManuAuto())%></TD>		

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>