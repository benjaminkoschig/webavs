<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.vb.typeDeSoins.RFParametrageTypeSoinsRecherchePeriodeListViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%-- tpl:put name="zoneScripts" --%>

<%
	RFParametrageTypeSoinsRecherchePeriodeListViewBean viewBean = (RFParametrageTypeSoinsRecherchePeriodeListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	
	detailLink = "cygnus?userAction="+IRFActions.ACTION_PARAMETRAGE_SOINS_RECHERCHE_PERIODE+ ".afficher&selectedId=";
	
	menuName = "cygnus-menuprincipal";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    
<%@page import="globaz.cygnus.vb.typeDeSoins.RFParametrageTypeSoinsRecherchePeriodeViewBean"%>
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

	<%--<TH><ct:FWLabel key="JSP_RF_SAISIE_SOIN_PERIODE_FORCER_PAYEMENT"/></TH>
	<TH><ct:FWLabel key="JSP_RF_SAISIE_SOIN_PERIODE_IMPUTER_GRANDE_QD"/></TH>--%>
	
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>

		<%-- tpl:put name="zoneList" --%>
		<%
			RFParametrageTypeSoinsRecherchePeriodeViewBean courant = (RFParametrageTypeSoinsRecherchePeriodeViewBean) viewBean.getEntity(i);
		
			String urlForMenuPopUp = detailLink + courant.getIdSoinPot() +
			   "&idSoinPot=" + courant.getIdSoinPot() +
			   "&idPotAssure=" + courant.getIdPotAssure() +
			   "&dateDebut=" + courant.getDateDebut() +
			   "&dateFin=" + courant.getDateFin() +
			   "&montantPlafondPourTous=" + courant.getMontantPlafondPourTous()+
			   "&montantPlafondPensionnairePourTous=" + courant.getMontantPlafondPensionnairePourTous()+		
			   "&montantPlafondPersonneSeule=" + courant.getMontantPlafondPersonneSeule()+
			   "&montantPlafondPensionnairePersonneSeule=" + courant.getMontantPlafondPensionnairePersonneSeule()+   
			   "&montantPlafondPensionnaireSepareHome=" + courant.getMontantPlafondPensionnaireSepareHome()+   
			   "&montantPlafondPensionnaireSepareDomicile=" + courant.getMontantPlafondPensionnaireSepareDomicile()+
			   "&montantPlafondSepareDomicile=" + courant.getMontantPlafondSepareDomicile()+	   
			   "&montantPlafondCoupleDomicile=" + courant.getMontantPlafondCoupleDomicile()+
			   "&montantPlafondCoupleEnfant=" + courant.getMontantPlafondCoupleEnfant()+	
			   "&montantPlafondEnfantsSepares=" + courant.getMontantPlafondEnfantsSepares()+
			   "&montantPlafondPensionnaireEnfantsSepares=" + courant.getMontantPlafondPensionnaireEnfantsSepares()+	
			   "&montantPlafondAdulteEnfants=" + courant.getMontantPlafondAdulteEnfants()+
			   "&montantPlafondEnfantsEnfants=" + courant.getMontantPlafondEnfantsEnfants()+
			   "&montantPlafondPensionnaireEnfantsEnfants=" + courant.getMontantPlafondPensionnaireEnfantsEnfants()+
			   "&idSousTypeSoin=" + courant.getIdSousTypeSoin() +		
			   "&codeSousTypeDeSoin=" + viewBean.getForCodeSousTypeDeSoin() +	
		       "&codeSousTypeDeSoinList=" + viewBean.getForCodeSousTypeDeSoin() +	
			   "&codeTypeDeSoin=" + viewBean.getForCodeTypeDeSoin() +
			   "&codeTypeDeSoinList=" + viewBean.getForCodeTypeDeSoin(); 
			
				String detailUrl = "parent.fr_detail.location.href='" + urlForMenuPopUp + "'";
		%>
		
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getIdSoinPot() %></TD>
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
     	<TD  style="text-align:right;" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantPlafondCoupleEnfant()).toStringFormat()%></TD>     	   	
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
     	
     	<%--
			String image1;
			if(courant.getForcerPayement())
				image1 = courant.getImageSuccess();
			else
				image1 = courant.getImageError();
     	
     	<TD  style="text-align:center;" class="mtd" nowrap onClick="<%=detailUrl%>"><IMG src='<%=request.getContextPath()+image1%>' alt="" name="imgCodeTypeDeSoin1"></TD>
     	
     	
			String image2;
			if(courant.getImputerGrandeQd())
				image2 = courant.getImageSuccess();
			else
				image2 = courant.getImageError();
     	
     	<TD  style="text-align:center;" class="mtd" nowrap onClick="<%=detailUrl%>"><IMG src='<%=request.getContextPath()+image2%>' alt="" name="imgCodeTypeDeSoin2"></TD>--%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>