<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="globaz.cygnus.vb.prestationsaccordees.RFPrestationsAccordeesListViewBean"%>
<%@page import="globaz.cygnus.vb.prestationsaccordees.RFPrestationsAccordeesViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%-- tpl:put name="zoneScripts" --%>
<%	

	//Author FHA, revision JJE 5.08.2011
	
	RFPrestationsAccordeesListViewBean viewBean = (RFPrestationsAccordeesListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	
	detailLink = "cygnus?userAction="+IRFActions.ACTION_PRESTATION_ACCORDEE+ ".afficher&selectedId=";
	
	menuName = "cygnus-menuprincipal";

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
   		<TH colspan="2"><ct:FWLabel key="JSP_RF_PRESTATION_ACCORDEE_DETAIL_REQUERANT"/></TH>
		<TH><ct:FWLabel key="JSP_RF_PRESTATION_ACCORDEE_GENRE"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_PRESTATION_ACCORDEE_PERIODE_DROIT"/></TH>		
		<TH><ct:FWLabel key="JSP_RF_PRESTATION_ACCORDEE_MONTANT"/></TH>
		<TH><ct:FWLabel key="JSP_RF_PRESTATION_ACCORDEE_ETAT"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_PRESTATION_ACCORDEE_DATE_ECHEANCE"/></TH>
		<TH><ct:FWLabel key="JSP_RF_PRESTATION_ACCORDEE_NUMERO"/></TH>				
		   		
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
		RFPrestationsAccordeesViewBean courant = (RFPrestationsAccordeesViewBean) viewBean.get(i);
		
		String urlForMenuPopUp = detailLink + courant.getId() 
		+ "&nss=" + courant.getNss() 
		+ "&nom=" + courant.getNom()
		+ "&prenom=" + courant.getPrenom() 
		+ "&dateNaissance="+courant.getDateNaissance()
		+ "&csSexe="+courant.getCsSexe()
		+ "&csNationalite="+courant.getCsNationalite() 
		+ "&idDecision="+ courant.getIdDecision()
		+ "&idTiers="+ courant.getIdTiers()
		+ "&idRFMAccordee="+ courant.getIdRFMAccordee()
		+ "&montantPrestation="+ courant.getMontantPrestation()
		+ "&dateEcheance="+ courant.getDateEcheance()
		+ "&idAdressePaiement="+ courant.getIdAdressePaiement();
		
		String detailUrl = "parent.location.href='" + urlForMenuPopUp + "'";		
		%>
	   	<TD class="mtd" nowrap onClick="<%=detailUrl%>">
	   		<ct:menuPopup menu="cygnus-optionsprestationacc" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=urlForMenuPopUp%>" >					
			</ct:menuPopup>
		</TD>				
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDetailAssure() %></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=viewBean.getSession().getCodeLibelle(courant.getCsGenrePrestationAccordee()) %></TD>    	
     	
     	<%-- période du droit --%>   
		<%
			String periode = courant.getDateDebutDroit();
			if (!JadeStringUtil.isBlankOrZero(courant.getDateFinDroit())) {
				periode += " - " + courant.getDateFinDroit();
			}
			else {
				periode += " -           ";
			}
		%>          	  	     	
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=periode %></TD>
     	
     	<TD align="right" class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getMontantPrestation() %></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=viewBean.getSession().getCodeLibelle(courant.getCsEtatRE()) %></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDateEcheance() %></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getIdRFMAccordee() %></TD>	
	
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>