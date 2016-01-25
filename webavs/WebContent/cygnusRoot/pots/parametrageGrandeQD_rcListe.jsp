<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.vb.pots.RFParametrageGrandeQDListViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%-- tpl:put name="zoneScripts" --%>

<%
	RFParametrageGrandeQDListViewBean viewBean = (RFParametrageGrandeQDListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	
	detailLink = "cygnus?userAction="+IRFActions.ACTION_PARAMETRAGE_GRANDE_QD+ ".afficher&selectedId=";
	
	menuName = "cygnus-menuprincipal";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    
<%@page import="globaz.cygnus.vb.pots.RFParametrageGrandeQDViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
	
	<TH><ct:FWLabel key="JSP_RF_SAISIE_SOIN_PERIODE_NUMERO"/></TH>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_SOIN_PERIODE_PERIODE"/></TH>
	
	<TH><ct:FWLabel key="JSP_RF_SAISIE_SOIN_PERIODE_MONTANT_TOUS"/></TH>
	
	<TH><ct:FWLabel key="JSP_RF_SAISIE_SOIN_PERIODE_MONTANT_PERSONNE_SEULE"/></TH>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_SOIN_PERIODE_MONTANT_COUPLES_DOMICILE"/></TH>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_SOIN_PERIODE_MONTANT_COUPLE_ENFANTS"/></TH>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_SOIN_PERIODE_MONTANT_SEPARE_DOMICILE"/></TH>	
	<TH><ct:FWLabel key="JSP_RF_SAISIE_SOIN_PERIODE_MONTANT_ENFANTS_SEPARES"/></TH>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_SOIN_PERIODE_MONTANT_ADULTE_ENFANTS"/></TH>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_SOIN_PERIODE_MONTANT_ENFANTS_ENFANTS"/></TH>
	
	<TH><ct:FWLabel key="JSP_RF_SAISIE_SOIN_PERIODE_MONTANT_PENSIONNAIRE_TOUS"/></TH>
	
	<TH><ct:FWLabel key="JSP_RF_SAISIE_SOIN_PERIODE_MONTANT_PENSIONNAIRE_PERSONNE_SEULE"/></TH>	
	<TH><ct:FWLabel key="JSP_RF_SAISIE_SOIN_PERIODE_MONTANT_PENSIONNAIRE_SEPARE_HOME"/></TH>	
	<TH><ct:FWLabel key="JSP_RF_SAISIE_SOIN_PERIODE_MONTANT_PENSIONNAIRE_SEPARE_DOMICILE"/></TH>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_SOIN_PERIODE_MONTANT_PENSIONNAIRE_ENFANTS_SEPARES"/></TH>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_SOIN_PERIODE_MONTANT_PENSIONNAIRE_ENFANTS_ENFANTS"/></TH>			
			
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>

		<%-- tpl:put name="zoneList" --%>
		<%
			RFParametrageGrandeQDViewBean courant = (RFParametrageGrandeQDViewBean) viewBean.getEntity(i);
				
			String urlForMenuPopUp = detailLink + courant.getIdPotPC() +
			   "&forIdPotPC=" + courant.getIdPotPC() +
			   "&forDateDebut=" + courant.getDateDebut() +
			   "&forDateFin=" + courant.getDateFin() +
			   "&montantPlafondCoupleDomicile=" + courant.getMontantPlafondCoupleDomicile()+
			   "&montantPlafondCoupleEnfants=" + courant.getMontantPlafondCoupleEnfants()+
			   "&montantPlafondPersonnesEnfants=" + courant.getMontantPlafondAdulteEnfants()+
			   "&montantPlafondEnfantsSepares=" + courant.getMontantPlafondEnfantsSepares()+
			   "&montantPlafondPersonneSeule=" + courant.getMontantPlafondPersonneSeule()+
			   "&montantPlafondSepareDomicile=" + courant.getMontantPlafondSepareDomicile()+
			   "&montantPlafondEnfantsEnfants=" + courant.getMontantPlafondEnfantsEnfants()+
			   "&montantPlafondPensionnaireSepareDomicile=" + courant.getMontantPlafondPensionnaireSepareDomicile() +
			   "&montantPlafondPensionnaireEnfantsSepares=" + courant.getMontantPlafondPensionnaireEnfantsSepares()+
			   "&montantPlafondPensionnairePersonneSeule=" + courant.getMontantPlafondPensionnairePersonneSeule()+
			   "&montantPlafondPensionnaireSepareHome=" + courant.getMontantPlafondPensionnaireSepareHome()+
			   "&montantPlafondPensionnaireEnfantsEnfants=" + courant.getMontantPlafondPensionnaireEnfantsEnfants() +
			   "&montantPlafondPensionnairePourTous=" + courant.getMontantPlafondPensionnairePourTous()+			   
			   "&montantPlafondPourTous=" + courant.getMontantPlafondPourTous();
			
			String detailUrl = "parent.fr_detail.location.href='" + urlForMenuPopUp + "'";
		%>
		
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><a href="#" title="<%=courant.getIdPotPC()%>"><%=courant.getIdPotPC() %></a></TD>
		<%
			String periode = courant.getDateDebut();
			if (!JadeStringUtil.isBlankOrZero(courant.getDateFin())) {
				periode += " - " + (JadeStringUtil.isEmpty(courant.getDateFin())?"":courant.getDateFin());
			}
			else {
				periode += " -           ";
			}						
		%>     	
     	<TD  style="text-align:left;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=periode%></TD>
     	
     	<TD  style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantPlafondPourTous()).toStringFormat() %></TD>
     	
     	<TD  style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantPlafondPersonneSeule()).toStringFormat()%></TD>
     	<TD  style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantPlafondCoupleDomicile()).toStringFormat()%></TD>
     	<TD  style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantPlafondCoupleEnfants()).toStringFormat()%></TD>	
     	<TD  style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantPlafondSepareDomicile()).toStringFormat()%></TD>
     	<TD  style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantPlafondEnfantsSepares()).toStringFormat()%></TD>
     	<TD  style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantPlafondAdulteEnfants()).toStringFormat()%></TD>
     	<TD  style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantPlafondEnfantsEnfants()).toStringFormat()%></TD>
     	
     	<TD  style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantPlafondPensionnairePourTous()).toStringFormat() %></TD>
     	<TD  style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantPlafondPensionnairePersonneSeule()).toStringFormat()%></TD>   	
     	<TD  style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantPlafondPensionnaireSepareHome()).toStringFormat()%></TD>
     	<TD  style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantPlafondPensionnaireSepareDomicile()).toStringFormat()%></TD>
     	<TD  style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantPlafondPensionnaireEnfantsSepares()).toStringFormat()%></TD>
     	<TD  style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantPlafondPensionnaireEnfantsEnfants()).toStringFormat()%></TD>  	
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>