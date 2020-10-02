<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*"
         contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator" %>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet" %>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCFraisGardeAjaxViewBean" %>
<%@page import="globaz.framework.util.FWCurrency" %>

<%
    PCFraisGardeAjaxViewBean viewBean = (PCFraisGardeAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
    globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
    globaz.globall.db.BSession objSession = (globaz.globall.db.BSession) controller.getSession();
%>


<%@page import="globaz.pegasus.vb.droit.PCAbstractRequerantDonneeFinanciereViewBean" %>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.FraisGarde" %>
<%@page import="ch.globaz.pegasus.business.models.droit.ModificateurDroitDonneeFinanciere" %>

<message>
    <liste>
        <%
            FWCurrency montant = new FWCurrency("0.00");

            String idGroup = null;

            for (Iterator itDonnee = viewBean.iterator(); itDonnee.hasNext(); ) {
                FraisGarde donnee = ((FraisGarde) itDonnee.next());

                montant = new FWCurrency(donnee.getSimpleFraisGarde().getMontant());

                if (!donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup().equals(idGroup)) {
                    idGroup = null;
                }
        %>

        <tr idEntity="<%=donnee.getId() %>" idGroup="<%=donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup() %>"
            header="<%=idGroup==null?"true":"false"%>">
            <td>&#160;</td>
            <td><%=donnee.getSimpleFraisGarde().getLibelle() %></td>
            <td style="text-align:right;"><%=montant.toStringFormat() %></td>
            <td><%=donnee.getSimpleDonneeFinanciereHeader().getDateDebut() %>
                - <%=donnee.getSimpleDonneeFinanciereHeader().getDateFin() %></td>
        </tr>
        <%
                idGroup = donnee.getSimpleDonneeFinanciereHeader().getIdEntityGroup();
            }%>
    </liste>
	<%@ include file="/pegasusRoot/ajax/commonListDonneeFinanciereWithWarning.jspf" %>
</message>