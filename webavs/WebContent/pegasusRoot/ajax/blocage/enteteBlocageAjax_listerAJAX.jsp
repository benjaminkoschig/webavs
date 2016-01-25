<%@page import="java.math.BigDecimal"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="ch.globaz.pegasus.business.models.blocage.PcaBloque"%>
<%@page import="globaz.pegasus.vb.blocage.PCEnteteBlocageAjaxViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompenseSearch"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompense"%>
<%@page import="globaz.pegasus.vb.dettecomptatcompense.PCDetteComptatCompenseAjaxViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%
	PCEnteteBlocageAjaxViewBean  viewBean=(PCEnteteBlocageAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>

	<liste>
	<%
		for(PcaBloque pcaBloque: viewBean.getList()){
			BigDecimal bloque = new BigDecimal(pcaBloque.getMontantBloque());
			String montant = "0";
			if(pcaBloque.getMontantDebloque() !=null){
				montant =pcaBloque.getMontantDebloque();
			}
			BigDecimal debloque = new BigDecimal(montant);
			BigDecimal sold = bloque.subtract(debloque);
	%>
		<tr idEntity="<%=pcaBloque.getIdPca()%>">
			<td style="text-align:left"><%= pcaBloque.getNom()+" "+pcaBloque.getPrenom()+" / "+pcaBloque.getNss()%></td>
			<td style="text-align:center;"><%= ((pcaBloque.getIsRetenues())?"R":"") + ((pcaBloque.getIsPrestationBloquee())?"B":"") %></td>
			<td style="text-align:left"><%= BSessionUtil.getSessionFromThreadContext().getCodeLibelle(pcaBloque.getCsGenrPca())%></td>
			<td style="text-align:left"><%= BSessionUtil.getSessionFromThreadContext().getCodeLibelle(pcaBloque.getCsTypePca())%></td>
			<td style="text-align:left"><%= BSessionUtil.getSessionFromThreadContext().getCodeLibelle(pcaBloque.getCsEtatPca())%></td>
			<td style="text-align:left"><%= pcaBloque.getDateDebutPca()%></td>
			<td style="text-align:right" class="mtd"><%= new FWCurrency(pcaBloque.getMontantBloque()).toStringFormat()%></td>
			<td style="text-align:right" class="mtd"><%= new FWCurrency(sold.toString()).toStringFormat()%></td>
			<td style="text-align:right"><%= pcaBloque.getNoVersionDroit()%></td>
			<td style="text-align:right"><%= pcaBloque.getIdBlocage()%></td>
		</tr>
	<%}%>

	</liste>
