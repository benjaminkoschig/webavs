<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%

	globaz.libra.vb.journalisations.LIEcheancesJointDossiersListViewBean viewBean = (globaz.libra.vb.journalisations.LIEcheancesJointDossiersListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();

	detailLink = "libra?userAction="+globaz.libra.servlet.ILIActions.ACTION_ECHEANCES_DE + ".afficher&selectedId=";
	
	menuName = "li-menuprincipal";
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    
	    	<TH colspan="2">&nbsp;</TH>
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_DATE"/></TH> 
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_LIBELLE"/></TH> 
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_TYPE"/></TH> 
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_TIERS"/></TH> 
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_USER"/></TH> 
    		<TH><ct:FWLabel key="ECRAN_ECH_LI_DOMAINE"/></TH> 
    		
    		 		
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			globaz.libra.vb.journalisations.LIEcheancesJointDossiersViewBean courant = (globaz.libra.vb.journalisations.LIEcheancesJointDossiersViewBean) viewBean.get(i);
			
			String detailUrl = "parent.location.href='" + detailLink + courant.getIdJournalisation()+"'";
	
		%>
		
		<TD class="mtd" width="">
	     	<ct:menuPopup menu="libra-optionsecheances" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + courant.getIdJournalisation()%>">
	     			<ct:menuParam key="selectedId" value="<%=courant.getIdJournalisation()%>"/>  
		 	
		 	<%if (globaz.jade.client.util.JadeStringUtil.isBlankOrZero(courant.getIdReferenceDestination())){%>
					<ct:menuExcludeNode nodeId="rappel"/>
			<%}%>	
		 	
		 	</ct:menuPopup>
     	</TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>">
					<FONT face="<%="".equals(courant.getPoliceIcone())?"&nbsp;":courant.getPoliceIcone()%>"
					size="4"><%="".equals(courant.getIcone())?"&nbsp;":"&#"+courant.getIcone()%></FONT></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDateRappel()%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getLibelleAffichage()%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getSession().getCodeLibelle(courant.getValeurCodeSysteme())%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDetailTiers()%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getIdUtilisateur()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getSession().getCodeLibelle(courant.getCsDomaine())%></TD>

		
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>