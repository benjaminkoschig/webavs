
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA3022"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.process.interetmanuel.*" %>
<%@ page import="globaz.osiris.process.interetmanuel.visualcomponent.*" %>
<%
globaz.osiris.db.process.CAInteretMoratoireManuelViewBean viewBean = (globaz.osiris.db.process.CAInteretMoratoireManuelViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
userActionValue = "osiris.process.interetMoratoireManuel.executer";
selectedIdValue=viewBean.getIdSection();

globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

okButtonLabel = "Créer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function SetTimer() {
		document.forms[0].lancement.value = "Lancé...";
		vSubmit();
}
function fLancement() {
	document.forms[0].lancer.value = "lancer";
	SetTimer();

}
function vSubmit() {
	document.forms[0].submit();
}

top.document.title = "Calculer intérêt moratoire - Etape 2/3 - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Calculer intérêt moratoire - Etape 2/3<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

<%
	CACompteAnnexe compte = (CACompteAnnexe) viewBean.getCompteAnnexeInformation();
	CASection section = (CASection) viewBean.getSectionInformation();
	if (compte != null && section != null) {
%>

		<tr>
		<td valign="top"><a href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.apercuComptes.afficher&id=<%=compte.getIdCompteAnnexe()%>">Compte</a></td>
		<td colspan="3"><TEXTAREA cols="40" rows="4" class="libelleLongDisabled" readonly><%=compte.getTitulaireEnteteForCompteAnnexeParSection()%></TEXTAREA></td>
		</tr>
		<tr>
		<td><a href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.apercuParSection.afficher&id=<%=section.getIdSection()%>">Section</a>&nbsp;</td>
		<td colspan="3"><input type="text" name="section" class="libelleSpecialLongDisabled" readonly value="<%=section.getFullDescription()%>"/></td>
		</tr>
		<tr><td colspan="4">&nbsp;</td></tr>
<%
	} else {
		showProcessButton = false;
	}
%>

	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>

	<tr>
		<td width="40">E-mail</td>
		<td colspan="3">
        <input type="text" name="eMailAddress" class="libelleLongDisabled" value="<%=viewBean.getEMailAddress()%>"/>
        </td>
	</tr>

	<tr>
	    <td>Numéro de facture (optionnel)</td>
	    <td align="left" colspan="3">
	    <input type="text" name="numeroFactureGroupe" class="numeroDisabled" value="<%=viewBean.getNumeroFactureGroupe()%>"/>
	    &nbsp;
		</td>
	</tr>

	<tr>
	    <td>Date de fin</td>
	    <td align="left" colspan="3">
	    <input type="text" name="dateFin" class="numeroDisabled" value="<%=viewBean.getDateFin()%>"/>
	    &nbsp;<a href="<%=request.getContextPath()%>/osiris?userAction=osiris.process.interetMoratoireManuel.afficher&idSection=<%=section.getIdSection()%>&eMailAddress=<%=viewBean.getEMailAddress()%>&dateFin=<%=viewBean.getDateFin()%>&numeroFactureGroupe=<%=viewBean.getNumeroFactureGroupe()%>" class="back_link">Modifier</a>&nbsp;
		</td>
	</tr>

	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>

	<%

	globaz.framework.util.FWCurrency totalInteret = new globaz.framework.util.FWCurrency();

	for (java.util.Iterator it = viewBean.getVisualComponents().iterator(); it.hasNext(); ) {
        CAInteretManuelVisualComponent element = (CAInteretManuelVisualComponent) it.next();
    %>
    	<tr>
			<td colspan="4" class="gText"><%=element.getPlanLibelle(objSession)%></td>
		</tr>

    	<tr>
    	<td colspan="4" width="100%">

    		<table width="100%" cellspacing="0" class="borderBlack">
				<tr>
					<th>Du</th>
					<th>Au</th>
					<th>Nbr. de jours</th>
					<th>Montant soumis</th>
					<th>Taux</th>
					<th>Intérêt calculé</th>
				</tr>

				<%
					for (int j=0; j<element.getDetailInteretMoratoire().size(); j++) {

						String style = "row";
						if (j % 2 == 1) {
							style = "rowOdd";
						}
				%>
						<tr class="<%=style%>">
						<td style="vertical-align: middle; text-align: center;" class="mtd"><%=element.getDetailInteretMoratoire(j).getDateDebut()%></td>
						<td style="vertical-align: middle; text-align: center;" class="mtd"><%=element.getDetailInteretMoratoire(j).getDateFin()%></td>
						<td style="vertical-align: middle; text-align: center;" class="mtd"><%=element.getDetailInteretMoratoire(j).getNbJours()%></td>
						<td style="vertical-align: middle; text-align: right;"  class="mtd"><%=JANumberFormatter.format(element.getDetailInteretMoratoire(j).getMontantSoumis())%></td>
						<td style="vertical-align: middle; text-align: right;"  class="mtd"><%=element.getDetailInteretMoratoire(j).getTaux()%></td>
						<td style="vertical-align: middle; text-align: right;"  class="mtd" width="100"><%=JANumberFormatter.format(element.getDetailInteretMoratoire(j).getInteretCalcule())%></td>
						</tr>
				<%
						totalInteret.add(element.getDetailInteretMoratoire(j).getInteretCalcule());
					}
				%>

    		</table>
    	</td>
    	</tr>
    	<tr><td colspan="4">&nbsp;</td></tr>
    <%
    }
%>

	<tr>
	<td colspan="4" width="100%">
		<table width="100%" cellspacing="0">
			<tr>
				<td align="right" class="mtd"><b>Total :</b></td>
				<td style="vertical-align: middle; text-align: right;" class="mtd" width="100"><b><%=JANumberFormatter.format(totalInteret.toString())%></b></td>
			</tr>
		</table>
	</td>
	</tr>

          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>