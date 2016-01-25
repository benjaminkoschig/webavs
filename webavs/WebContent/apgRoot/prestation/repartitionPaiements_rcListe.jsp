<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="globaz.globall.db.BSession"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.framework.controller.FWController"%>
<%@ page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%

globaz.apg.vb.prestation.APRepartitionPaiementsListViewBean viewBean = (globaz.apg.vb.prestation.APRepartitionPaiementsListViewBean) request.getAttribute("viewBean");
globaz.prestation.tools.PRIterateurHierarchique iterH = viewBean.iterateurHierarchique();

size = viewBean.getSize ();
detailLink = baseLink + "afficher&miseAJourLot=true&idDroit="+viewBean.getIdDroit()+"&genreService="+viewBean.getGenreService()+"&selectedId=";

menuName=globaz.apg.menu.IAppMenu.MENU_OPTION_REPARTITION;
menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");
BSession bSession = viewBean.getSession();

FWController controllerbis = (FWController) session.getAttribute("objController");
boolean hasOsirisReadAccess = controllerbis.getSession().hasRight("osiris.comptes.ordresVersement", FWSecureConstants.READ);

// pour l'affichage du montant restant
float sommeVentilations = 0;
float previousMontantNet = 0;

%>
<SCRIPT language="JavaScript">
	function afficherCacher(id) {
		if (document.all("groupe_" + id).style.display == "none") {
			document.all("groupe_" + id).style.display = "block";
			document.all("bouton_" + id).value = "-";
		} else {
			document.all("groupe_" + id).style.display = "none";
			document.all("bouton_" + id).value = "+";
		}
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH></TH>
    <th>&nbsp;</th>
    <TH><ct:FWLabel key="JSP_BENEFICIAIRES"/></TH>
    <TH><ct:FWLabel key="JSP_MONTANT_BRUT"/></TH>
    <TH><ct:FWLabel key="JSP_COTISATIONS"/></TH>
    <TH><ct:FWLabel key="JSP_MONTANT_NET"/></TH>
    <TH><ct:FWLabel key="JSP_MONTANT_VENTILE"/></TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
<%
	globaz.apg.vb.prestation.APRepartitionPaiementsViewBean courant = null;

	try {
		courant = (globaz.apg.vb.prestation.APRepartitionPaiementsViewBean) iterH.next();
	} catch (Exception e) {
		break;
	}
	String detailUrl = "parent.fr_detail.location.href='" + detailLink + courant.getIdRepartitionBeneficiairePaiement() + "'";

	if (iterH.isPositionPlusPetite()) {%>
		<%if (sommeVentilations>0){ %>
			<TR>
				<TD colspan="5" align="right" style="font-style: italic; background-color: #dddddd;"><ct:FWLabel key="JSP_MONTANT_RESTANT"/></TD>
				<TD class="mtd" nowrap align="right" style="font-style: italic; background-color: #dddddd;"><%=new globaz.framework.util.FWCurrency(previousMontantNet-sommeVentilations).toStringFormat()%></TD>
			</TR>
		<%}%>
		</TBODY><%
	} else if (iterH.isPositionPlusGrande()) {
		%><TBODY id="groupe_<%=courant.getIdParent()%>" style="display: none;"><%
	}
%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<TD class="mtd" width=""<% if (courant.isVentilation()) { %> onclick="<%=detailUrl%>"<% } %>>
			<% for (int idPosition = 1; idPosition < iterH.getPosition(); ++idPosition) { %>
				--
			<% } %>
			<%if (!courant.isVentilation()) {
				sommeVentilations = 0;
				previousMontantNet = new globaz.framework.util.FWCurrency(courant.getMontantNet()).floatValue();%>
			<ct:menuPopup menu="ap-optionrepartition">
				<ct:menuParam key="selectedId" value="<%=courant.getIdRepartitionBeneficiairePaiement()%>"/>
			</ct:menuPopup>
			<%}else {
				sommeVentilations = sommeVentilations + (new globaz.framework.util.FWCurrency(courant.getMontantVentile()).floatValue());
			}%>

			<% if (iterH.isPere()) {%>
			<INPUT type="button" id="bouton_<%=courant.getIdRepartitionBeneficiairePaiement()%>" value="+" onclick="afficherCacher(<%=courant.getIdRepartitionBeneficiairePaiement()%>)">
			<% } %>
		</TD>
		
			<% if (!JadeStringUtil.isBlankOrZero(courant.getIdCompteAnnexe(bSession, viewBean.getNoAVS())) && hasOsirisReadAccess) {
			%>				<td  class="mtd" nowrap>
			<%
					String urlLienOrdreVersement = request.getContextPath() + "/osiris?"
												+ "userAction=osiris.comptes.odresVersement.chercher" 
												+ "&selectedId=" + courant.getIdCompteAnnexe() 
												+ "&id=" + courant.getIdCompteAnnexe() 
												+ "&idCompteAnnexe=" + courant.getIdCompteAnnexe();
			%>
								<a href="<%=urlLienOrdreVersement%>" class="external_link" target="_parent">
									<ct:FWLabel key="JSP_RAC_L_OV_LINK" />
								</a>
							</td>
			<%
				} else {
			%>				<td class="mtd" nowrap>
								&nbsp;
							</td>
			<%
				}
			%>
		
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=courant.getNomPlusTypeAffiliation()%>&nbsp;</TD>
		<TD class="mtd" nowrap align="right" onclick="<%=detailUrl%>"><%=courant.getMontantBrut()%>&nbsp;</TD>
		<TD class="mtd" nowrap align="right" onclick="<%=detailUrl%>"><%=courant.getMontantCotisations()%>&nbsp;</TD>
		<TD class="mtd" nowrap align="right" onclick="<%=detailUrl%>"><%=courant.getMontantNet()%>&nbsp;</TD>
		<TD class="mtd" nowrap align="right" onclick="<%=detailUrl%>"><%=courant.getMontantVentile()%>&nbsp;</TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
		<%if(!iterH.hasNext() && sommeVentilations>0){%>
			<TR>
				<TD colspan="5" align="right" style="font-style: italic; background-color: #dddddd;"><ct:FWLabel key="JSP_MONTANT_RESTANT"/></TD>
				<TD class="mtd" nowrap align="right" style="font-style: italic; background-color: #dddddd;"><%=new globaz.framework.util.FWCurrency(previousMontantNet-sommeVentilations).toStringFormat()%></TD>
			</TR>
		<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>