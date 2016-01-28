<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%

	globaz.libra.vb.dossiers.LIDossiersJointTiersListViewBean viewBean = (globaz.libra.vb.dossiers.LIDossiersJointTiersListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();

	detailLink = "libra?userAction="+globaz.libra.servlet.ILIActions.ACTION_DOSSIERS+ ".afficher&selectedId=";	
	
	menuName = "li-menuprincipal";
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    
	    	<TH>&nbsp;</TH>
    		<TH><ct:FWLabel key="ECRAN_DOS_LI_TIERS"/></TH>
    		<TH><ct:FWLabel key="ECRAN_DOS_LI_ETAT"/></TH>
    		<TH><ct:FWLabel key="ECRAN_DOS_LI_GEST"/></TH>
    		<TH><ct:FWLabel key="ECRAN_DOS_LI_GROU"/></TH>    		
    		<TH><ct:FWLabel key="ECRAN_DOS_LI_DOMAIN"/></TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			globaz.libra.vb.dossiers.LIDossiersJointTiersViewBean courant = (globaz.libra.vb.dossiers.LIDossiersJointTiersViewBean) viewBean.get(i);
			
			String detailUrl = "parent.location.href='" + detailLink + courant.getIdDossier()+"'";
			String goToApUrl = "parent.location.href='" 
									+ "libra?userAction="+globaz.libra.servlet.ILIActions.ACTION_DOSSIERS+ ".rediriger&selectedId=" 
									+ courant.getIdDossier()+"'";
	
		%>
		
		<TD class="mtd" width="60">
	     	<ct:menuPopup menu="libra-optionsdossiers" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + courant.getIdDossier()%>">
	     			<ct:menuParam key="selectedId" value="<%=courant.getIdDossier()%>"/>  
	     			<ct:menuParam key="idTiers" value="<%=courant.getIdTiers()%>"/>
		 	</ct:menuPopup>
		 	<% if (courant.getIsUrgent().booleanValue()){ %>
		 		<img src="<%=request.getContextPath()+"/images/up.gif"%>">
		 	<% } %>		 	
     	</TD>

		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDetailTiers()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getSession().getCodeLibelle(courant.getCsEtat())%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getVisaUtilisateur()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getLibelleGroupe()%></TD>
		<TD class="mtd" nowrap onClick="<%=goToApUrl%>" align="center">
			<a href="<%=request.getContextPath() +"/libra?userAction="+ globaz.libra.servlet.ILIActions.ACTION_DOSSIERS+".rediriger&selectedId="+ courant.getIdDossier()%>" class="external_link" target="_parent">
				<%= courant.getSession().getCodeLibelle(courant.getCsDomaine())%>
			</a>
		</TD>

		
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>