<%@page import="globaz.jade.i18n.JadeI18n"%>
<%@page import="globaz.al.vb.rafam.ALAnnonceRafamListViewBean"%>
<%@page import="ch.globaz.al.business.models.rafam.AnnonceRafamComplexModel"%>
<%@page import="ch.globaz.al.business.constantes.enumerations.RafamFamilyAllowanceType" %>
<%@page import="ch.globaz.al.business.constantes.enumerations.RafamReturnCode" %>
<%@page import="ch.globaz.al.business.constantes.enumerations.RafamTypeAnnonce" %>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
	<%
	ALAnnonceRafamListViewBean viewBean = (ALAnnonceRafamListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
    detailLink = "al?userAction=al.rafam.annonceRafam.afficher&selectedId=";
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
	<th><ct:FWLabel key="AL0030_LIST_ENTETE_NO_DOSSIER"/></th>
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
	AnnonceRafamComplexModel line =  (AnnonceRafamComplexModel)viewBean.getSearchModel().getSearchResults()[i];	
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
				<ct:menuPopup menu="rafam-recherchePopup" target="top.fr_main">
					<ct:menuParam key="selectedId" value="<%=line.getId()%>"/>
					<ct:menuParam key="idDossier" value="<%=line.getDossierModel().getIdDossier()%>"/>
					<ct:menuParam key="idDroit" value="<%=line.getAnnonceRafamModel().getIdDroit()%>"/>
					<ct:menuParam key="id" value="<%=line.getAnnonceRafamModel().getId() %>"/>
				</ct:menuPopup>
			</TD>
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1()%>&nbsp;<%=line.getDroitComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2()%></TD>
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getDroitComplexModel().getDroitModel().getIdDossier()%></TD>	
			<TD class="center" onClick="<%=actionDetail%>">
			<% if(!JadeStringUtil.isBlankOrZero(line.getAnnonceRafamModel().getDateMutation())) { %>
			<%=line.getAnnonceRafamModel().getDateMutation()%>
			<% } else if(!JadeStringUtil.isBlankOrZero(line.getAnnonceRafamModel().getDateCreation())) { %>
			<%=line.getAnnonceRafamModel().getDateCreation()%>
			<% } else { %>
			<%=line.getAnnonceRafamModel().getDateReception()%>
			<% } %>
			</TD>		
			<TD class="center" onClick="<%=actionDetail%>"><%=line.getId()%>/<%=line.getAnnonceRafamModel().getRecordNumber() %></TD>	
			<TD class="center" onClick="<%=actionDetail%>"><%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), RafamFamilyAllowanceType.getFamilyAllowanceType(line.getAnnonceRafamModel().getGenrePrestation()).getCodeLibelle())%></TD>	
			<TD class="center" onClick="<%=actionDetail%>">
				<%=line.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1()%>&nbsp;
				<%=line.getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2()%>
			</TD>	
			<TD class="center" onClick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getAnnonceRafamModel().getEtat())%></TD>
			<TD class="center" onClick="<%=actionDetail%>"><%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), RafamReturnCode.getRafamReturnCode(line.getAnnonceRafamModel().getCodeRetour()).getCodeLibelle())%></TD>	
			<TD class="center" onClick="<%=actionDetail%>"><%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), RafamTypeAnnonce.getRafamTypeAnnonce(line.getAnnonceRafamModel().getTypeAnnonce()).getCodeLibelle())%> (<%=line.getAnnonceRafamModel().getTypeAnnonce()%>)</TD>	
			<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>

	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	