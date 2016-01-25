<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="java.util.*"%>
<%@page import="globaz.naos.db.tauxAssurance.AFTauxAssuranceComparator"%>
<%
	detailLink = "naos?userAction=naos.tauxAssurance.tauxAssurance.afficher&selectedId=";
	globaz.naos.db.tauxAssurance.AFTauxAssuranceListViewBean viewBean = (globaz.naos.db.tauxAssurance.AFTauxAssuranceListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
	//pour trier par valeur
	if(viewBean.getForSelectionTri().equalsIgnoreCase("2")){
		Collections.sort((Vector)viewBean.getContainer(),new AFTauxAssuranceComparator());
	}

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<%
		
	%>
	
	<TH width="30">&nbsp;</TH>
	<TH align="left" width="80">Genre</TH>
	<TH width="80">D&eacute;but</TH>
	<!--TH width="120">Fin</TH-->
	<TH align="center" width="80">Sexe</TH>
	<TH align="right" width="80">Valeur</TH>
	<TH width="80">Rang</TH>
	<TH width="80">Tranche</TH>
	<TH width="80">Type</TH>
	<TH width="80">Catégorie</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%    
		actionDetail = targetLocation + "='" + detailLink + viewBean.getTauxAssuranceId(i)+"'";

		String categorie = null;
		globaz.naos.db.tauxAssurance.AFTauxAssurance taux = (globaz.naos.db.tauxAssurance.AFTauxAssurance) viewBean.getEntity(i);
		if (globaz.naos.translation.CodeSystem.TYPE_TAUX_CAISSE.equals(taux.getTypeId())) {
			categorie = taux.getSaisieCodeAdministration();
		} else {
			categorie = globaz.naos.translation.CodeSystem.getLibelle(session,taux.getCategorieId());
		}
	%>
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="AFMenuVide" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getTauxAssuranceId(i)%>"/>
	</TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="150" align="left"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getGenreValeur(i))%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" align="center" width="120"><%=viewBean.getDateDebut(i)%></TD>
	<!--TD class="mtd" onClick="<%=actionDetail%>" align="center" width="120"><=viewBean.getDateFin(i)></TD-->
	<TD class="mtd" onClick="<%=actionDetail%>" width="70" align="center"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getSexe(i))%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" align="right" width="120"><%=viewBean.getValeurTotal(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="70" align="center"><%=viewBean.getRang(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" align="right" width="120"><%=viewBean.getTranche(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getTypeId(i))%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>"><%=categorie%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>