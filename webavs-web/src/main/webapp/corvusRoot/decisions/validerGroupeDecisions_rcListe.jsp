<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ page import="globaz.corvus.utils.REPmtMensuel"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="globaz.corvus.vb.decisions.REDecisionJointDemandeRenteViewBean"%>
<%@ page import="globaz.globall.util.JADate"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.corvus.api.decisions.IREDecision"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="globaz.globall.util.JACalendar"%>
<%@ page import="globaz.globall.util.JACalendarGregorian"%>
<%@ page import="globaz.prestation.tools.PRDateFormater"%>
<%@ page import="java.util.Collections"%>
<%@ page import="globaz.corvus.vb.decisions.REDecisionJointDemandeRenteListViewBean"%>
<%@ page import="java.util.Comparator"%>
<%@ page import="globaz.corvus.vb.documents.RECopiesViewBean"%>
<%@ page import="globaz.corvus.vb.decisions.REValiderGroupeDecisionsListViewBean"%>
<%@ page import="globaz.corvus.db.demandes.REDemandeRenteJointDemande"%>
<%@ page import="globaz.corvus.vb.demandes.REDemandeRenteJointDemandeViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	REValiderGroupeDecisionsListViewBean viewBean = (REValiderGroupeDecisionsListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();

	globaz.corvus.tools.REDecisionJointDemandeRenteGroupByIterator gbIter = null;

%>
<script type="text/javascript">
	var buttonPrint = top.fr_main.document.getElementById("btnExecuter");

	function selectRappel(){
		var elems = document.getElementsByName("checkBox");
		for (var i = 0; i < elems.length; i++) {
			if (document.getElementById("selectionRappel").value === "ON" ) {
				elems(i).checked = false;
			} else {
				elems(i).checked = true;
			}
		}
		if (document.getElementById("selectionRappel").value === "ON" ) {
			document.getElementById("selectionRappel").value = "OFF";
			document.getElementById("oui").style.display = "none";
			document.getElementById("non").style.display = "block";
		} else {
			document.getElementById("selectionRappel").value = "ON";
			document.getElementById("oui").style.display = "block";
			document.getElementById("non").style.display = "none";
		}
		onButtonDisabled();
	}

	function onButtonDisabled(){
		var checkboxes = document.getElementsByName("checkBox");
		var findLine = false;
		for (var i = 0; i < checkboxes.length; i++) {
			if (checkboxes(i).checked && checkboxes(i).value !== 'ON' && checkboxes(i).value !== '') {
				buttonPrint.disabled = false;
				findLine = true;
			}
		}
		if (!findLine) {
			buttonPrint.disabled = true;
		}
	}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
		<%-- tpl:put name="zoneHeaders" --%>
			<th onClick="selectRappel();" style="cursor:hand; font-family: Wingdings; font-size: large;">
				<div id="oui" style="display: none">
					&#168;
				</div>
				<div id="non" style="display: block">
					&#254;
				</div>
				<input type="hidden" id="selectionRappel" value="OFF" />
			</th>
			<th><ct:FWLabel key="JSP_DECISION_REQUERANT"/></th>
			<th><ct:FWLabel key="JSP_DECISION_GENRE"/></th>
			<th><ct:FWLabel key="JSP_DECISION_PERIODE_DROIT"/></th>
			<th><ct:FWLabel key="JSP_DECISION_ETAT"/></th>
			<th><ct:FWLabel key="JSP_DECISION_PREP_PAR"/></th>
			<th><ct:FWLabel key="JSP_DECISION_DATE_PREP"/></th>
			<th><ct:FWLabel key="JSP_DECISION_VALIDE_PAR"/></th>
			<th><ct:FWLabel key="JSP_DECISION_DATE_VALID"/></th>
			<th><ct:FWLabel key="JSP_DECISION_TYPE"/></th>
			<th><ct:FWLabel key="JSP_DECISION_NO_DEMANDE"/></th>
			<th><ct:FWLabel key="JSP_LOT_DECISION_NO"/></th>
		<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
	<%-- tpl:put name="zoneCondition" --%>
<% condition = true; %>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	REDemandeRenteJointDemandeViewBean dem = (REDemandeRenteJointDemandeViewBean) viewBean.getEntity(i);

	REDecisionJointDemandeRenteListViewBean decisions = viewBean.getDecisionsForDemande(dem.getIdDemandeRente()); 

	if (viewBean.iterator() != null) {
		gbIter = new globaz.corvus.tools.REDecisionJointDemandeRenteGroupByIterator(decisions.iterator());
	}

	Comparator<String> myComparator = new Comparator<String>() {
		public int compare(String o1, String o2) {
			int o1Int = new Integer(o1).intValue();
			int o2Int = new Integer(o2).intValue();
			int res = 0;

			if (o1Int > o2Int) {
				res = 1;
			} else if (o1Int < o2Int) {
				res = -1;
			} else if (o1Int == o2Int) {
				res = 0;
			}
			return res;
		}
	};

	if (!decisions.isEmpty() && decisions.getNbDecisionInvalidables() == 0) {
%>
	<td class="mtd">
		<input title="-" type="checkbox" name="checkBox" value="<%=dem.getIdDemandeRente()%>_<%=dem.getIdDemandePrestation() %>" onclick="onButtonDisabled();" />
	</td>
	<td class="mtd" nowrap><%=dem.getDetailRequerantDecede()%></td>
	<td colspan="10" nowrap>&nbsp;</td>
<%
		for (Iterator iterator = decisions.iterator(); iterator.hasNext();) {

			REDecisionJointDemandeRenteViewBean line = (REDecisionJointDemandeRenteViewBean) iterator.next();

			if(gbIter != null && gbIter.hasNext()) {
				line = (REDecisionJointDemandeRenteViewBean) gbIter.next();
				REDecisionJointDemandeRenteViewBean current = line;
				String genrePrestation = "";

				boolean isContentInfini = false;

				JADate d1 = null;
				JADate d2 = null;

				if (!JadeStringUtil.isBlankOrZero(line.getDateDebutDroit())) {
					d1 = new JADate(line.getDateDebutDroit());
				}
				if (IREDecision.CS_TYPE_DECISION_RETRO.equals(line.getCsTypeDecision())) {
					d2 = new JADate(line.getDateFinRetro());
				} else if (!JadeStringUtil.isBlankOrZero(line.getDateFinDroit())) {
					d2 = new JADate(line.getDateFinDroit());
				}

				if (d1 == null) {
					d1 = new JADate("31.12.2999");
				}

				List<String> genres = new ArrayList<String>();
				genres.add(line.getGenrePrestation());

				JACalendar cal = new JACalendarGregorian();

				while (gbIter.isNextSameEntity()) {
					line = (REDecisionJointDemandeRenteViewBean) gbIter.next();

					JADate id1 = null;
					JADate id2 = null;

					if (!JadeStringUtil.isBlankOrZero(line.getDateDebutDroit())) {
						id1 = new JADate(line.getDateDebutDroit());
						if (cal.compare(d1, id1)==JACalendar.COMPARE_SECONDLOWER) {
							d1 = new JADate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(id1.toStrAMJ()));
						}
					}

					if (!IREDecision.CS_TYPE_DECISION_RETRO.equals(line.getCsTypeDecision())) {
						if (d2 != null) {
							if (!JadeStringUtil.isBlankOrZero(line.getDateFinDroit())) {
								id2 = new JADate(line.getDateFinDroit());
								if (cal.compare(d2, id2) == JACalendar.COMPARE_FIRSTLOWER) {
									d2 = new JADate(PRDateFormater.convertDate_AAAAMMJJ_to_JJxMMxAAAA(id2.toStrAMJ()));
								}
							} else {
								isContentInfini = true;
							}
						}
	
						if (JadeStringUtil.isIntegerEmpty(line.getDateFinDroit())) {
							isContentInfini = true;
						}
					}

					if (!(genres.contains(line.getGenrePrestation()))) {
						genres.add(line.getGenrePrestation());
					}

					if (current.getNumDateDebutDroit() < line.getNumDateDebutDroit()) {
						current = line;
					}
				}

				Collections.sort(genres, myComparator);

				for (int j = 0; genres.size() > j; j++) {
					if (j % 2 != 0) {
						genrePrestation += " - " + genres.get(j);
					} else {
						if (j == 0) {
							genrePrestation += genres.get(j);
						} else {
							genrePrestation += " <br/> " + genres.get(j);
						}
					}
				}

				String ddr = PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(d1.toStrAMJ());
				String dfr = "";
				if (d2 != null) {
					if (!isContentInfini) {
						dfr = PRDateFormater.convertDate_AAAAMMJJ_to_MMxAAAA(d2.toStrAMJ());
					}
				}
%>
		<tr>
			<td>&nbsp;</td>
			<td class="mtd" nowrap="nowrap"><%=current.getDetailRequerant()%>&nbsp;</td>
			<td class="mtd" nowrap="nowrap"><%=genrePrestation%></td>
			<td class="mtd" nowrap="nowrap"><%=ddr + " - " + dfr%>&nbsp;</td>
			<td class="mtd" nowrap="nowrap"><%=current.getCsEtatDecisionLibelle()%>&nbsp;</td>
			<td class="mtd" nowrap="nowrap"><%=current.getPreparePar()%>&nbsp;</td>
			<td class="mtd" nowrap="nowrap"><%=current.getDatePreparation()%>&nbsp;</td>
			<td class="mtd" nowrap="nowrap"><%=current.getValidePar()%>&nbsp;</td>
			<td class="mtd" nowrap="nowrap"><%=current.getDateValidation()%>&nbsp;</td>
			<td class="mtd" nowrap="nowrap"><%=current.getCsTypeDecisionLibelle()%>&nbsp;</td>
			<td class="mtd" nowrap="nowrap"><%=dem.getIdDemandeRente()%></td>
			<td class="mtd" nowrap="nowrap"><%=current.getIdDecision()%>&nbsp;</td>
		</tr>
<%
			}
		}
	}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<script type="text/javascript">
	buttonPrint.disabled=true;
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>