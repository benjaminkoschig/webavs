<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompenseSearch"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompense"%>
<%@page import="globaz.pegasus.vb.dettecomptatcompense.PCDetteComptatCompenseAjaxViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%
	PCDetteComptatCompenseAjaxViewBean  viewBean=(PCDetteComptatCompenseAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
	String image="";
	boolean montantModifie = false;
	String montantString = ""; 
	float total = 0;
	SimpleDetteComptatCompenseSearch search = (SimpleDetteComptatCompenseSearch)viewBean.getSearchModel();
%>

	<liste>
	<%
		for(JadeAbstractModel model: viewBean.getSearchModel().getSearchResults()){
		SimpleDetteComptatCompense comptatCompense = (SimpleDetteComptatCompense)model;
		montantModifie = false;
		image = "";
		montantString = "";
		if(!JadeStringUtil.isBlankOrZero(comptatCompense.getIdDetteComptatCompense())){
				montantString = comptatCompense.getMontant();
			if(!JadeStringUtil.isBlankOrZero(comptatCompense.getMontantModifie())) {
				montantModifie = true;
				montantString = comptatCompense.getMontantModifie();
			}	
			total= total + Float.valueOf(montantString);;
			
			image = "<span data-g-bubble='text:¦Compensée¦,wantMarker:false,position:right'>"+
						"<img alt='Compensée' src='/webavs/images/small_good.png' />"+
					"</span>";

		}
	%>
		<tr idEntity="<%=comptatCompense.getIdSectionDetteEnCompta()%>_<%=comptatCompense.getIdVersionDroit()%>_<%=search.getForIdDroit()%>">
			<TD><%= image%></TD>
			<TD style="text-align:left"><%= viewBean.getDescriptionSection(comptatCompense.getIdSectionDetteEnCompta())%></TD>
			<TD style="text-align:right" class="mtd" ><%= new FWCurrency(comptatCompense.getMontant()).toStringFormat()%></TD>
			<TD style="text-align:right" class="mtd"><%= new FWCurrency(montantString).toStringFormat()%></TD>
		</tr>
	<%}%>
		<tr>
			<TD style="text-align:left" class="bold" colspan="4"></TD>
		</tr>
		<tr>
			<TD style="text-align:left" class="bold" colspan="3"><ct:FWLabel key="JSP_PC_DETTE_COMPENSE_L_TOTAL"/></TD>
			<TD style="text-align:right" class="mtd bold" ><%=new FWCurrency(String.valueOf(total)).toStringFormat()%></TD>
		</tr>
	</liste>
	<infoJson>
		<%= viewBean.getJsonInfoDecompt() %>
	</infoJson>