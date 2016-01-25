<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/xml" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.fortuneparticuliere.PCPretEnversTiersAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.SimplePretEnversTiers"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.dessaisissement.PCDessaisissementFortuneAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.dessaisissement.DessaisissementFortune"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.pegasus.business.models.dessaisissement.SimpleDessaisissementFortune"%>
<%@page import="ch.globaz.hera.business.models.famille.MembreFamille"%>
<%
PCDessaisissementFortuneAjaxViewBean viewBean = (PCDessaisissementFortuneAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleDessaisissementFortune entity=viewBean.getDessaisissementFortune().getSimpleDessaisissementFortune();
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
Boolean facteurUtilise = false;

%>

<%@page import="globaz.pegasus.utils.PCDessaisissementHandler"%>
<message>
	<contenu>
		<csMotif><%=JadeStringUtil.isBlankOrZero(entity.getCsMotifDessaisissement())?"":entity.getCsMotifDessaisissement()%></csMotif>
		<motifAutre><%=entity.getAutreMotifDessaisissement() %></motifAutre>
		<montantBrutDessaisi><%=new FWCurrency(entity.getMontantBrut()).toStringFormat()%></montantBrutDessaisi>
		<montantDeductions><%=new FWCurrency(entity.getDeductionMontantDessaisi()).toStringFormat()%></montantDeductions>
		<charges><%=new FWCurrency(entity.getCharges()).toStringFormat()%></charges>
		<rendementFortune><%=new FWCurrency(entity.getRendementFortune()).toStringFormat()%></rendementFortune>
		<contrePrestation><%=entity.getIsContrePrestation() %></contrePrestation>
		<% if(viewBean.getCalculContrePresation()!=null && PCDessaisissementHandler.hasCalculToDo(viewBean.getDessaisissementFortune())){%>
			<calculContrePrestation>
				<montantNetDuBien><%=new FWCurrency(viewBean.getCalculContrePresation().getMontantNetDuBien().toString()).toStringFormat()%></montantNetDuBien>
				<rendementNet><%=new FWCurrency(viewBean.getCalculContrePresation().getRendementNet().toString()).toStringFormat()%></rendementNet>
				<% for (MembreFamille key : viewBean.getCalculContrePresation().getFacteurCapitalisation().keySet()) {%>
					<%if(key.equals(viewBean.getCalculContrePresation().getKeyFacteurCapitalisationPlusFavorable())){
						facteurUtilise = true;
					}else{
						facteurUtilise = false;
					}%>
					<<%="facteur_"+objSession.getCode(key.getCsSexe())%> utilise="<%=facteurUtilise.toString()%>"><%=viewBean.getCalculContrePresation().getFacteurCapitalisation().get(key)%></<%="facteur_"+objSession.getCode(key.getCsSexe())%> >
				<%}%>
				<typeValeur><%=viewBean.getCalculContrePresation().getTypeValeur()%></typeValeur>
				<rendementNetAvecFateur><%=new FWCurrency(viewBean.getCalculContrePresation().getRendementNetAvecFacteur().toString()).toStringFormat()%></rendementNetAvecFateur>
				<resultatCalcul><%=new FWCurrency(viewBean.getCalculContrePresation().getDessaisissement().toString()).toStringFormat()%></resultatCalcul>
				<totalArrondi><%=new FWCurrency(viewBean.getCalculContrePresation().getTotalArrondi().toString()).toStringFormat()%></totalArrondi>
			</calculContrePrestation>
		<%}%>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>

	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
