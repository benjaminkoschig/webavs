<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%--
INFO: les labels de cette page sont prefixes avec 'JSP_CTD_L'
--%>
<%

globaz.babel.vb.cat.CTDocumentListViewBean viewBean = (globaz.babel.vb.cat.CTDocumentListViewBean) request.getAttribute("viewBean");
size = viewBean.getSize ();
detailLink = baseLink + "afficher&selectedId=";

menuName = "CT-documents";
menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH></TH>
    <TH><ct:FWLabel key="JSP_CTD_L_NOM"/></TH>
    <TH><ct:FWLabel key="JSP_CTD_L_TYPE"/></TH>
    <TH><ct:FWLabel key="JSP_CTD_L_DESTINATAIRE"/></TH>
    <TH><ct:FWLabel key="JSP_CTD_L_DATE_DESACTIVATION"/></TH>
    <TH><ct:FWLabel key="JSP_CTD_L_DEFAUT"/></TH>
    <TH><ct:FWLabel key="JSP_CTD_L_ACTIF"/></TH>
    <TH><ct:FWLabel key="JSP_CTD_L_EDITABLE"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
globaz.babel.vb.cat.CTDocumentViewBean courant = (globaz.babel.vb.cat.CTDocumentViewBean) viewBean.get(i);
String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdDocument() + "&csGroupeDomaines=" + viewBean.getCsGroupeDomaines() + "&csGroupeTypesDocuments=" + viewBean.getCsGroupeTypesDocuments() + "'";
String csGroupeDomaine = "&csGroupeDomaines=";
String csGroupeTypesDocuments = "&csGroupeTypesDocuments=";
%>
	<TD class="mtd" width="">
    	<ct:menuPopup menu="CT-Options" label="<%=optionsPopupLabel%>" detailLabel="<%=menuDetailLabel%>" detailLink="<%=detailLink + courant.getIdDocument() + csGroupeDomaine + viewBean.getCsGroupeDomaines() + csGroupeTypesDocuments + viewBean.getCsGroupeTypesDocuments()%>" target="top.fr_main">
	 		<ct:menuParam key="selectedId" value="<%=courant.getIdDocument()+ \"&csGroupeDomaines=\" + viewBean.getCsGroupeDomaines() + \"&csGroupeTypesDocuments=\" + viewBean.getCsGroupeTypesDocuments()%>"/>  
	 
			<!--pas d'ajout d'annexe si aucun groupe de CS annexe existe -->
			<% if (globaz.jade.client.util.JadeStringUtil.isDecimalEmpty(courant.getIdGroupeAnnexe())) {%>
				<ct:menuExcludeNode nodeId="annexes"/>	
			<%}%>
		</ct:menuPopup>
    </TD>
    
	<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getNom()%></TD>
	<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getLibelleTypeDocument()%></TD>
	<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getLibelleDestinataire()%></TD>
	<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getDateDesactivation()%>&nbsp;</TD>
	<TD class="mtd" nowrap onclick="<%=detailUrl%>" style="text-align: center"><IMG src="<%=request.getContextPath()+courant.getImageDefaut()%>" alt=""></TD>
	<TD class="mtd" nowrap onclick="<%=detailUrl%>" style="text-align: center"><IMG src="<%=request.getContextPath()+courant.getImageActif()%>" alt=""></TD>
	<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getLibelleEditable()%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>