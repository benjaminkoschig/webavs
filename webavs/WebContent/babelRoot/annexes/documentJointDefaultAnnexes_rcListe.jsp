<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%

globaz.babel.vb.annexes.CTDocumentJointDefaultAnnexesListViewBean viewBean = (globaz.babel.vb.annexes.CTDocumentJointDefaultAnnexesListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize ();
detailLink = baseLink + "afficher&selectedId=";

menuName = "CT-documents";
menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    <TH><ct:FWLabel key="JSP_CTA_L_NO"/></TH>
	    <TH><ct:FWLabel key="JSP_CTA_L_DESCRIPTION_ANNEXE"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			globaz.babel.vb.annexes.CTDocumentJointDefaultAnnexesViewBean courant = (globaz.babel.vb.annexes.CTDocumentJointDefaultAnnexesViewBean) viewBean.get(i);
			
			String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdDefaultAnnexe() + "'";
		%>

		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getIdDefaultAnnexe()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getCsAnnexeLibelle()%></TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>