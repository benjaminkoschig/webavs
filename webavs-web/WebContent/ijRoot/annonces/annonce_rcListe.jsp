<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.ij.vb.annonces.IJAnnonceListViewBean viewBean = (globaz.ij.vb.annonces.IJAnnonceListViewBean) request.getAttribute("viewBean");
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");
	size = viewBean.size();
	detailLink = "ij?userAction=ij.annonces.annonce.afficher&selectedId=";
	globaz.prestation.tools.PRIterateurHierarchique iterH = viewBean.iterateurHierarchique();

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
    <TH>&nbsp;</TH>    
	<TH><ct:FWLabel key="JSP_ANNONCES_CODE"/></TH>
	<TH><ct:FWLabel key="JSP_ANNONCES_ANNONCE"/></TH>
	<TH><ct:FWLabel key="JSP_ANNONCES_GENRE_CARTE"/></TH>
	<TH><ct:FWLabel key="JSP_ANNONCES_CODE_ENREGISTREMENT"/></TH>
    <TH><ct:FWLabel key="JSP_ANNONCES_MOIS_ANNEE_COMPTABLE"/></TH>
    <TH><ct:FWLabel key="JSP_ANNONCES_NO_ASSURE"/></TH>
    <TH><ct:FWLabel key="JSP_ANNONCES_PERIODE_DU"/></TH>
    <TH><ct:FWLabel key="JSP_ANNONCES_PERIODE_AU"/></TH>
    <TH><ct:FWLabel key="JSP_ANNONCES_TOTAL_IJ"/></TH>
    <TH><ct:FWLabel key="JSP_ANNONCES_ETAT"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%   
		globaz.ij.vb.annonces.IJAnnonceViewBean line = null;
		try {
			line = (globaz.ij.vb.annonces.IJAnnonceViewBean) iterH.next();
		} catch (Exception e) {
			break;
		}	
		
		//on transmet dans la requête le type d'annonce pour savoir sur quel ecran aller ensuite
		actionDetail = targetLocation  + "='" + detailLink + line.getIdAnnonce()+"&codeApplication="+line.getCodeApplication()+"'";
		
		line.loadPeriodesAnnonces(null);
		
		globaz.framework.util.FWCurrency totalAllocation = new globaz.framework.util.FWCurrency();
		
		String ijPeriode1 = (line.getPeriodeAnnonce1().getCodeValeurTotalIJ().equals("1") ? "-" : "") + line.getPeriodeAnnonce1().getTotalIJ();
		String ijPeriode2 = (line.getPeriodeAnnonce2().getCodeValeurTotalIJ().equals("1") ? "-" : "") + line.getPeriodeAnnonce2().getTotalIJ();
		
		if("3".equals(line.getPetiteIJ())){
			ijPeriode1 = (line.getPeriodeAnnonce1().getCodeValeurTotalIJ().equals("1") ? "-" : "") + line.getPeriodeAnnonce1().getMontantAit();
			ijPeriode2 = (line.getPeriodeAnnonce2().getCodeValeurTotalIJ().equals("1") ? "-" : "") + line.getPeriodeAnnonce2().getMontantAit();
		}
		
		if("4".equals(line.getPetiteIJ())){
			ijPeriode1 = (line.getPeriodeAnnonce1().getCodeValeurTotalIJ().equals("1") ? "-" : "") + line.getPeriodeAnnonce1().getMontantAllocAssistance();
			ijPeriode2 = (line.getPeriodeAnnonce2().getCodeValeurTotalIJ().equals("1") ? "-" : "") + line.getPeriodeAnnonce2().getMontantAllocAssistance();
		}
		
		totalAllocation.add(ijPeriode1);
		totalAllocation.add(ijPeriode2);
		
		String detailMenu = detailLink + line.getIdAnnonce()+"&codeApplication="+line.getCodeApplication();
		
if (iterH.isPositionPlusPetite()) {
%> </TBODY><%
} else if (iterH.isPositionPlusGrande()) {
	%><TBODY id="groupe_<%=line.getIdParent()%>" style="display: none;"><%
} %>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<TD class="mtd">
<% for (int idPosition = 1; idPosition < iterH.getPosition(); ++idPosition) { %>
	&nbsp;&nbsp;
<% } %>
	
	<ct:menuPopup menu="ij-optionsempty" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailMenu%>"/>

<% if (iterH.isPere()) { %>
	<INPUT id="bouton_<%=line.getIdAnnonce()%>" type="button" value="+" onclick="afficherCacher(<%=line.getIdAnnonce()%>)">
<% } %>
</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getCodeApplication()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getIdAnnonce()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getCodeGenreCarte()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getCodeEnregistrement()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getMoisAnneeComptable()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=globaz.commons.nss.NSUtil.formatAVSUnknown(line.getNoAssure())%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getPeriodeAnnonce1().getPeriodeDe()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getPeriodeAnnonce1().getPeriodeA()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="right"><%=totalAllocation.toStringFormat()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getLibelleEtat()%>&nbsp;</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>