<%@page import="globaz.aquila.util.COGedUtils"%>
<%@page import="globaz.aquila.application.COApplication"%>
<%@ page import="globaz.aquila.db.access.poursuite.*"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
	COContentieux contentieuxViewBean = (COContentieux) session.getAttribute("contentieuxViewBean");

	String idCompteAnnexe = "";
	String compteAnnexeTitulaireEntete = "";
	String compteAnnexeRoleDateDebutDateFin = "";
	String compteAnnexeSoldeFormate = "";
	String idSection = "";
	String sectionText = "";
	String sectionSoldeFormate = "";
	String sectionDate = "";
	String idContentieux = "";

	try {
		idCompteAnnexe = contentieuxViewBean.getIdCompteAnnexe();
		compteAnnexeTitulaireEntete = contentieuxViewBean.getCompteAnnexe().getTitulaireEntete();
		compteAnnexeRoleDateDebutDateFin = contentieuxViewBean.getCompteAnnexe().getRole().getDateDebutDateFin(contentieuxViewBean.getCompteAnnexe().getIdExterneRole());
		compteAnnexeSoldeFormate = contentieuxViewBean.getCompteAnnexe().getSoldeFormate();
		idSection = contentieuxViewBean.getIdSection();
		sectionText = contentieuxViewBean.getSection().getIdExterne() + " - " + contentieuxViewBean.getSection().getDescription();
		sectionSoldeFormate = contentieuxViewBean.getSection().getSoldeFormate();
		sectionDate = contentieuxViewBean.getSection().getDateSection();
		idContentieux = contentieuxViewBean.getIdContentieux();

	} catch (Exception e) {
	}
%>
<TR>
	<TD class="label">
		<input type="hidden" name="idCompteAnnexe" value="<%=contentieuxViewBean.getCompteAnnexe().getIdCompteAnnexe()%>">
		<input type="hidden" name="idSection" value="<%=contentieuxViewBean.getSection().getIdSection()%>">
		<A href="osiris?userAction=osiris.comptes.apercuComptes.afficher&selectedId=<%=idCompteAnnexe%>&idContentieuxSrc=<%=contentieuxViewBean.getIdContentieux()%>&libSequence=<%=contentieuxViewBean.getSequence().getLibSequence()%>" class="external_link">Compte</A>
	</TD>
	<TD class="control" colspan="3" rowspan="2"><TEXTAREA rows="4" class="disabled" readonly><%=compteAnnexeTitulaireEntete%></TEXTAREA></TD>
	<TD  class="label">Affiliation</TD>
	<ct:ifhasright element="naos.affiliation.affiliation.gedafficherdossier" crud="r">
		<%
		  String GEDText = "&nbsp;";
			if (globaz.jade.ged.client.JadeGedFacade.isInstalled()) {
				GEDText = "<a href=\"#\" onclick=\"window.open('" + request.getContextPath() +
					"/naos?userAction=naos.affiliation.affiliation.gedafficherdossier&amp;noAffiliationId=" + contentieuxViewBean.getCompteAnnexe().getIdExterneRole() +
					"&amp;serviceNameId=" + COGedUtils.getService(contentieuxViewBean.getSequence().getSession().getApplication()) +
					"&amp;gedFolderType=" + COGedUtils.getFolder(contentieuxViewBean.getSequence().getSession().getApplication()) +
					"&amp;idRole=" + contentieuxViewBean.getCompteAnnexe().getIdRole() +
					"&amp;idContentieux=" + contentieuxViewBean.getIdContentieux() +
					"&amp;idSection=" + contentieuxViewBean.getIdSection() +
					"','GED_CONSULT')\" >GED</a> (Dossier "+contentieuxViewBean.getIdContentieux()+")";
			}
			%>


		<TD nowrap class="control" colspan="3"><INPUT type="text" value="<%=compteAnnexeRoleDateDebutDateFin%>" class="libelleLongDisabled" readonly> 	<span style="padding-left: 2em"><%=GEDText%></span></TD>
	</ct:ifhasright>
	<TD>&nbsp;</TD>
</TR>
<TR>
	<TD>&nbsp;</TD>
	<TD class="label">Solde compte</TD>
	<TD class="control"><INPUT type="text" value="<%=compteAnnexeSoldeFormate%>" class="montantDisabled" readonly></TD>
	<TD>&nbsp;</TD>
	<TD>&nbsp;</TD>
</TR>
<TR>
	<TD class="label"><A href="osiris?userAction=osiris.comptes.apercuParSection.afficher&id=<%=idSection%>&idContentieuxSrc=<%=contentieuxViewBean.getIdContentieux()%>&libSequence=<%=contentieuxViewBean.getSequence().getLibSequence()%>" class="external_link">Section</A></TD>
	<TD class="control" colspan="3"><INPUT type="text" value="<%=sectionText%>" class="disabled" style="width: 100%" readonly></TD>
	<TD class="label">Solde section</TD>
	<TD class="control"><INPUT type="text" name="montantInitial" value="<%=sectionSoldeFormate%>" class="montantDisabled" readonly></TD>
	<TD class="label">Date section</TD>
	<TD class="control"><INPUT type="text" value="<%=sectionDate%>" class="dateDisabled" readonly></TD>
</TR>