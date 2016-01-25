<%@page import="globaz.jade.i18n.JadeI18n"%>
<%@page import="globaz.al.vb.rafam.ALAnnonceRafamEDListViewBean"%>
<%@page import="ch.globaz.al.business.models.rafam.AnnonceRafamModel"%>
<%@page import="ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType" %>
<%@page import="ch.globaz.al.business.constantes.enumerations.RafamReturnCode" %>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
	<%
	ALAnnonceRafamEDListViewBean viewBean = (ALAnnonceRafamEDListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
    detailLink = "al?userAction=al.rafam.annonceRafamED.afficher&selectedId=";
	%>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>

<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.globall.parameters.FWParametersSystemCodeManager" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>

	<th>&nbsp;</th>
	<th><ct:FWLabel key="AL0030_LIST_ENTETE_ENFANT"/></th>
	<th><ct:FWLabel key="AL0032_LIST_ENTETE_EMPLOYEUR"/></th>
	<th><ct:FWLabel key="AL0030_LIST_ENTETE_DATE_ANNONCE"/></th>
	<th><ct:FWLabel key="AL0030_LIST_ENTETE_NO_ANNONCE"/></th>
	<th><ct:FWLabel key="AL0030_LIST_ENTETE_GENRE_PREST"/></th>
	<th><ct:FWLabel key="AL0030_LIST_ENTETE_ALLOCATAIRE"/></th>
	<th><ct:FWLabel key="AL0030_LIST_ENTETE_ETAT"/></th>
	<th><ct:FWLabel key="AL0030_LIST_ENTETE_CODE_DIAG"/></th>
	<th><ct:FWLabel key="AL0030_LIST_ENTETE_TYPE"/></th>
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>  
	<%    
	AnnonceRafamModel line =  (AnnonceRafamModel)viewBean.getSearchModel().getSearchResults()[i];	
	condition = (i % 2 == 0);
	%>  
    
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
<%
			if (isSelection) { // mode sélection
				actionDetail = "document.body.innerHTML='';parent.location.href='" + globaz.fweb.taglib.FWChooserTag.getSelectLink(pageContext, i) + "'";
			} else { // détail "normal"
				actionDetail = targetLocation  + "='" + detailLink + line.getId()+"'";
			}
			%>
			
			<TD class="mtd">
				<ct:menuPopup menu="rafamED-recherchePopup" target="top.fr_main">
					<ct:menuParam key="selectedId" value="<%=line.getId()%>"/>
				</ct:menuPopup>
			</TD>
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getNomEnfant()%>&nbsp;<%=line.getPrenomEnfant()%></TD>
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getRecordNumber().substring(0,2)%></TD>	
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getDateCreation()%></TD>		
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getRecordNumber() %></TD>	
			<TD class="center" onClick="<%=actionDetail%>"><%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), RafamFamilyAllowanceType.getFamilyAllowanceType(line.getGenrePrestation()).getCodeLibelle())%></TD>	
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getNomAllocataire()%>&nbsp;<%=line.getPrenomAllocataire()%></TD>	
			<TD class="center" onClick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getEtat())%></TD>
			<TD class="center" onClick="<%=actionDetail%>"><%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), RafamReturnCode.getRafamReturnCode(line.getCodeRetour()).getCodeLibelle())%></TD>	
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getTypeAnnonce()%>/<%=objSession.getCodeLibelle(line.getEvDeclencheur())%></TD>	
			<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>

	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	