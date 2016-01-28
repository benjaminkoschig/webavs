<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.framework.menu.FWMenuBlackBox"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.aquila.db.access.poursuite.COContentieux"%>

<%@page import="globaz.aquila.api.ICOSequence"%>
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal"/>
<%
COContentieux viewBean = (COContentieux) session.getAttribute("contentieuxViewBean");
String idSection = request.getParameter("idSection");

// customiser les entrées pour la compta
FWMenuBlackBox menu = (FWMenuBlackBox) session.getAttribute(FWServlet.OBJ_USER_MENU);

// concernant aquila
menu.setAllParameters("selectedId", viewBean.getIdContentieux(), "CO-OptionsDossierMenu");
menu.setAllParameters("libSequence", viewBean.getLibSequence(), "CO-OptionsDossierMenu");
menu.setAllParameters("idContentieuxSrc", viewBean.getIdContentieux(), "CO-OptionsDossierMenu");

menu.setAllParameters("noAffiliationId", viewBean.getCompteAnnexe().getIdExterneRole(), "CO-OptionsDossierMenu");
menu.setAllParameters("idRole", viewBean.getCompteAnnexe().getIdRole(), "CO-OptionsDossierMenu");
menu.setAllParameters("idContentieux", viewBean.getIdContentieux(), "CO-OptionsDossierMenu");

if (idSection!=null) {
	menu.setAllParameters("idSection", idSection, "CO-OptionsDossierMenu");
} else {
	menu.setAllParameters("idSection", " ", "CO-OptionsDossierMenu");
}

// concernant Osiris
menu.setActionParameter("selectedId", viewBean.getIdCompteAnnexe(), "AQUILA_COMPTE", "CO-OptionsDossierMenu");
menu.setActionParameter("id", viewBean.getIdCompteAnnexe(), "AQUILA_OPTIONS_EXTRAIT_DE_COMPTE", "CO-OptionsDossierMenu");
menu.setActionParameter("forIdSection", viewBean.getIdSection(), "AQUILA_OPTIONS_EXTRAIT_DE_COMPTE", "CO-OptionsDossierMenu");
menu.setActionParameter("id", viewBean.getIdSection(), "AQUILA_SECTION", "CO-OptionsDossierMenu");

// le lien vers le tiers ARD
menu.setActionParameter("idTiers", viewBean.getCompteAnnexe().getTiers().getIdTiers(), "AQUILA_TIERS", "CO-OptionsDossierMenu");
menu.setNodeActive(true, "AQUILA_TIERS", "CO-OptionsDossierMenu");
%>
<ct:menuChange displayId="options" menuId="CO-OptionsDossierMenu" showTab="options">
<% if (viewBean.getLibSequence().equals(ICOSequence.CS_SEQUENCE_ARD)) { %>
	<ct:menuActivateNode active="yes" nodeId="AQUILA_OPTIONS_ARD"/>
	<% } else { %>
	<ct:menuActivateNode active="no" nodeId="AQUILA_OPTIONS_ARD"/>
	<% } %>
</ct:menuChange>
