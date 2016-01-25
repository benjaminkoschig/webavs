<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%--

INFO !!!!
les labels pour cette page sont prefixes avec 'LABEL_IJC_L'

--%>
<%
	globaz.ij.vb.prestations.IJIJCalculeeJointIndemniteListViewBean viewBean = (globaz.ij.vb.prestations.IJIJCalculeeJointIndemniteListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = servletContext + mainServletPath + "?userAction="  + globaz.ij.servlet.IIJActions.ACTION_IJ_CALCULEE_JOINT_GRANDE_PETITE + ".afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH>&nbsp;</TH>    
	<TH><ct:FWLabel key="JSP_DATE_DEBUT"/></TH>
	<TH><ct:FWLabel key="JSP_DATE_FIN"/></TH>
	<TH><ct:FWLabel key="JSP_IJC_L_REVENU_DETERMINANT"/></TH>
    <TH><ct:FWLabel key="JSP_IJC_L_MONTANT_INDEMNITE"/></TH>
    <TH><ct:FWLabel key="JSP_IJC_NO_REVISION"/></TH>
    <TH><ct:FWLabel key="JSP_IJC_L_TYPE_INDEMNITE"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	globaz.ij.vb.prestations.IJIJCalculeeJointIndemniteViewBean courant = (globaz.ij.vb.prestations.IJIJCalculeeJointIndemniteViewBean) viewBean.getEntity(i);
	actionDetail = targetLocation  + "='" + detailLink + courant.getIdIJCalculee() + "&csTypeIJ=" + viewBean.getCsTypeIJ() + "'";
	
	String detailMenu = detailLink + courant.getIdIJCalculee() + "&csTypeIJ=" + viewBean.getCsTypeIJ();
%>
    <TD class="mtd" width="">
    <ct:menuPopup menu="ij-optionsempty" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailMenu%>"/>	
    </TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=courant.getDateDebutDroit()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=courant.getDateFinDroit()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=courant.getRevenuDeterminant()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=courant.getMontantJournalierIndemnite()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="center"><%=courant.getNoRevision()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=courant.getLibelleTypeIndemnisation()%>&nbsp;</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>