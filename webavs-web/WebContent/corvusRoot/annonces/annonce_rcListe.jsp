<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ page import="globaz.corvus.servlet.IREActions"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.corvus.utils.RETiersForJspUtils"%>
<%@ page import="globaz.prestation.tools.PRDateFormater"%>
<%@ page import="globaz.corvus.vb.annonces.REAnnoncesRenteListeViewBean"%>
<%@ page import="globaz.corvus.vb.annonces.REAnnoncesRenteViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_ANN_L"

	REAnnoncesRenteListeViewBean viewBean = (REAnnoncesRenteListeViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
	
	detailLink = "corvus?userAction="+ IREActions.ACTION_ANNONCES+ ".afficher&selectedId=";
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
		<%-- tpl:put name="zoneHeaders" --%>
			<th>
				&nbsp;
			</th>
			<th>
				<ct:FWLabel key="JSP_ANN_L_DETAIL_AYANT_DROIT" />
			</th>
			<th>
				<ct:FWLabel key="JSP_ANN_L_PERIODE" />
			</th>
			<th>
				<ct:FWLabel key="JSP_ANN_L_CODE_APP" />
			</th>
			<th>
				<ct:FWLabel key="JSP_ANN_L_MOIS_RAPPORT" />
			</th>
			<th>
				<ct:FWLabel key="JSP_ANN_L_GENRE_PRESTATION" />
			</th>
			<th>
				<ct:FWLabel key="JSP_ANN_L_MONTANT" />
			</th>
			<th>
				<ct:FWLabel key="JSP_ANN_L_ETAT" />
			</th>
			<th>
				<ct:FWLabel key="JSP_ANN_L_CODE_TRAITEMENT" />
			</th>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
	<%-- tpl:put name="zoneCondition" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
			<%-- tpl:put name="zoneList" --%>
			<%
					REAnnoncesRenteViewBean line = (REAnnoncesRenteViewBean)viewBean.get(i);

					String annonce =	"&typeAnnonce=" + line.getCodeApplication()
										+ "&forMoisRapport=" + viewBean.getForMoisRapport()
										+ "&forCsEtat=" + viewBean.getForCsEtat()
										+ "&forCsCodeTraitement=" + viewBean.getForCsCodeTraitement()
										+ "&menuOptionToLoad=" + menuOptionToLoad;

					actionDetail = targetLocation  + "='" + detailLink + line.getIdAnnonce()+ annonce+ "'";
			%>
				<td class="mtd" width="" nowrap>
					<ct:menuPopup menu="corvus-optionsannonces" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getIdAnnonce()+ annonce%>">
					</ct:menuPopup>
				</td>
				<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
					<%=RETiersForJspUtils.getInstance(viewBean.getSession()).getDetailsTiers(line.getTiers(), true)%>
				</td>
				<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
					<%=line.getPeriodes()%>
				</td>
				<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="center">
					<%=line.getCodeApplication()%>
				</td>
				<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="center">
					<%=line.getMoisRapportFormat()%>
				</td>
				<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="center">
					<%=line.getCodePrestation()%>
				</td>
				<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="right">
					<%=line.getMontantFormate()%>
				</td>
				<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
					<%=line.getLibelleEtat()%>
				</td>
				<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
					<%=line.getLibelleCodeTraitement()%>
				</td>
			<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>