<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.cygnus.api.conventions.IRFConventions"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.api.IRFTypesBeneficiairePc"%>
<%@page import="globaz.cygnus.vb.conventions.RFRechercheMontantsConventionListViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%-- tpl:put name="zoneScripts" --%>

<%
	RFRechercheMontantsConventionListViewBean viewBean = (RFRechercheMontantsConventionListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	
	detailLink = "cygnus?userAction="+IRFActions.ACTION_RECHERCHE_MONTANTS_CONVENTION+ ".afficher&selectedId=";
	
	menuName = "cygnus-menuprincipal";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    
<%@page import="globaz.cygnus.vb.conventions.RFRechercheMontantsConventionViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_BENEFICIAIRE"/></TH>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_PERIODE"/></TH>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_MNT_PAR_DEFAUT"/></TH>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_MNT_SANS_API"/></TH>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_MNT_AVEC_API_GRAVE"/></TH>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_MNT_AVEC_API_MOYENNE"/></TH>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_MNT_AVEC_API_LEGERE"/></TH>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_GENRE_PC"/></TH>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_TYPE_PC"/></TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>

		<%-- tpl:put name="zoneList" --%>
		<%
			RFRechercheMontantsConventionViewBean courant = (RFRechercheMontantsConventionViewBean) viewBean.getEntity(i);
			
			String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdMontant() +
							   "&idMontant=" + courant.getIdMontant() +
							   "&dateDebut=" + courant.getDateDebut() +
							   "&dateFin=" + courant.getDateFin() +
							   "&csTypeBeneficiaire=" + courant.getCsTypeBeneficiaire() +
							   "&mntMaxDefaut=" + courant.getMntMaxDefaut() +
							   "&mntMaxAvecApiFaible=" + courant.getMntMaxAvecApiFaible() +
							   "&mntMaxAvecApiGrave=" + courant.getMntMaxAvecApiGrave() +
							   "&mntMaxAvecApiMoyen=" + courant.getMntMaxAvecApiMoyen() +
							   "&mntMaxSansApi=" + courant.getMntMaxSansApi()+
							   "&csPeriodicite=" + courant.getPeriodicite()+
							   "&csGenrePCAccordee=" + courant.getGenrePc()+
							   "&csTypePCAccordee=" + courant.getTypePc()+
			 				   "&plafonne=" + courant.isPlafonne()+"'";
				
		%>
		
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getCsTypeBeneficiaire().equals(IRFTypesBeneficiairePc.POUR_TOUS)?
     			viewBean.getSession().getLabel("JSP_RF_SAISIE_CONVENTION_MONTANT_POUR_TOUS_LISTE"):viewBean.getSession().getCodeLibelle(courant.getCsTypeBeneficiaire())%></TD>
		<%
			String periode = courant.getDateDebut();
			if (!JadeStringUtil.isBlankOrZero(courant.getDateFin())) {
				periode += " - " + courant.getDateFin();
			}
			else {
				periode += " -           ";
			}
		%>     	
     	<TD style="text-align:left;" class="mtd"  nowrap onClick="<%=detailUrl%>"><%=periode%></TD>
     	<TD style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMntMaxDefaut()).toStringFormat() %></TD>
     	<TD style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMntMaxSansApi()).toStringFormat() %></TD>
     	<TD style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMntMaxAvecApiGrave()).toStringFormat() %></TD>
     	<TD style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMntMaxAvecApiMoyen()).toStringFormat() %></TD>
     	<TD style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMntMaxAvecApiFaible()).toStringFormat() %></TD>
     	<TD style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getGenrePc().equals(IRFTypesBeneficiairePc.POUR_TOUS)?
     			viewBean.getSession().getLabel("JSP_RF_SAISIE_CONVENTION_MONTANT_POUR_TOUS_LISTE"):viewBean.getSession().getCodeLibelle(courant.getGenrePc())%></TD>
     	<TD style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getTypePc().equals(IRFTypesBeneficiairePc.POUR_TOUS)?
     			viewBean.getSession().getLabel("JSP_RF_SAISIE_CONVENTION_MONTANT_POUR_TOUS_LISTE"):viewBean.getSession().getCodeLibelle(courant.getTypePc())%></TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>