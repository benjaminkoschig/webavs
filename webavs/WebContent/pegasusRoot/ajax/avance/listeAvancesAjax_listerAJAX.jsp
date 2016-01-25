<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompenseSearch"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.pegasus.business.models.avance.AvanceVo"%>;
<%@page import="globaz.pegasus.vb.avance.PCListeAvancesAjaxViewBean"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%
	PCListeAvancesAjaxViewBean  viewBean = (PCListeAvancesAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
	String idTiers = request.getParameter("idTiers");
	
	String image="";
	boolean montantModifie = false;
	String montantString = ""; 
	float total = 0;
	
	//SimpleDetteComptatCompenseSearch search = (SimpleDetteComptatCompenseSearch)viewBean.getSearchModel();
%>

	<liste>
	<%
		for(JadeAbstractModel model: viewBean.getSearchModel().getSearchResults()){
			AvanceVo avance = (AvanceVo)model;
		//montantModifie = false;
		//image = "";
		//montantString = "";
// 		if(!JadeStringUtil.isBlankOrZero(comptatCompense.getIdDetteComptatCompense())){
// 				montantString = comptatCompense.getMontant();
// 			if(!JadeStringUtil.isBlankOrZero(comptatCompense.getMontantModifie())) {
// 				montantModifie = true;
// 				montantString = comptatCompense.getMontantModifie();
// 			}	
// 			total= total + Float.valueOf(montantString);;
			
// 			image = "<span data-g-bubble='text:¦Compensée¦,wantMarker:false,position:right'>"+
// 						"<img alt='Compensée' src='/webavs/images/small_good.png' />"+
// 					"</span>";

// 		}
	%>
		<tr class="avanceEntity" idEntity="<%=avance.getId()%>">
			<TD style="text-align:left"><%= viewBean.getDetailAssure(avance)%></TD>
			<TD style="text-align:center"><%= viewBean.getDomaineAvanceAsLibelle(avance.getCsDomaineAvance())%></TD>
			<TD style="text-align:right"><%= avance.getMontant1erAcompte()%></TD>
			<TD style="text-align:right" class="mtd" ><%= avance.getMontantMensuel() %></TD>
			<TD style="text-align:left" class="mtd"><%= viewBean.getPeriode(avance) %></TD>
			<TD style="text-align:right" class="mtd"><%= viewBean.getEtatAcompteAsLibelle(avance.getCsEtatAcomptes()) %></TD>
			<TD style="text-align:right" class="mtd"><%= avance.getIdAvance() %></TD>
		</tr>
	<%}%>
		
		
	</liste>
	