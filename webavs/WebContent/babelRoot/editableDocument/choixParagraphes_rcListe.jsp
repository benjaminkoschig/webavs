<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
	globaz.babel.api.doc.impl.CTScalableDocumentAbstractViewBeanDefaultImpl viewBean = (globaz.babel.api.doc.impl.CTScalableDocumentAbstractViewBeanDefaultImpl) session.getAttribute("viewBean");
	size = viewBean.getScalableDocumentProperties().getNiveau(viewBean.getSelectedNiveau()).getPositionSize();
	selectionStr="";
%>

<script language="JavaScript">
	function checkBoxChange(key){
		document.forms[0].elements('positionKey').value = key;
		document.forms[0].elements('userAction').value = "<%=globaz.babel.servlet.CTMainServletAction.ACTION_PARAGRAPHES%>.actionChangeSelection";
		document.forms[0].target = "fr_main";
		document.forms[0].submit();
	}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
		<TH>Sélection</TH> 
		<TH>Position</TH>
		<TH>Description</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
		globaz.babel.api.doc.ICTScalableDocumentPosition line = (globaz.babel.api.doc.ICTScalableDocumentPosition) viewBean.getScalableDocumentProperties().getNiveau(viewBean.getSelectedNiveau()).getPosition(i);
	%>

	<TD class="mtd" nowrap="nowrap"><INPUT type="checkbox" name="isSelected" value="on" <%=line.isSelected()?"CHECKED":""%> onclick="checkBoxChange(<%=line.getKey()%>);"></TD>
	<TD class="mtd" nowrap="nowrap"><%=line.getPosition()%></TD>
	<TD class="mtd" nowrap="nowrap"><%=line.getDescription()%></TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<FORM method="get" target="fr_main">
		<INPUT type="hidden" name="positionKey">
		<INPUT type="hidden" name="userAction">
	</FORM>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>