<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.naos.db.cotisation.AFCotisationListViewBean"%>
<%@page import="globaz.naos.translation.CodeSystem"%>
<%@page import="globaz.globall.util.JANumberFormatter"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "naos?userAction=naos.cotisation.cotisation.afficher&selectedId=";
	//detailLink = "naos?userAction=naos.cotisation.cotisation.afficherCreeException&cotisationId=";
	menuName = "Cotisation";
	AFCotisationListViewBean viewBean = (AFCotisationListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<%

		double montPersPeriode 	= 0;
		double montPersAnnuel = 0;
		String triGenre = "";
	%>
	<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<TH width="30">&nbsp;</TH>
	<TH nowrap width="200" align="left">Versicherung</TH>
	<TH width="70" align="center">Kasse</TH>
	<TH width="70" align="center">Beginn</TH>
	<TH width="70" align="center">Ende</TH>
	<TH width="100" align="left">Periodizität</TH>
	<TH width="150" align="center">Periodischer Betrag</TH>
	<TH width="150" align="center">Jährlicher Betrag</TH>
	<TH width="150" align="left" nowrap>Abgangsgrund</TH>
	<!--TH width="150" align="center">Maison m&egrave;re</TH-->
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<% String genreAssurance = viewBean.getAssurance(i).getAssuranceGenre();
	if(!triGenre.equals(genreAssurance)) { %>
		<TD class="title" colspan="9" ><b><%=CodeSystem.getLibelle(session,genreAssurance)%></b></TD>
	<% triGenre = genreAssurance;
	i--;
	} else { %>

	<TD class="mtd" width="16" >
	<% if (!JadeStringUtil.isIntegerEmpty(viewBean.getCotisationId(i))) { %>
	<%
	if (viewBean.getMotifFin(i).equals(CodeSystem.MOTIF_FIN_EXCEPTION)) {
		detailLink = "naos?userAction=naos.cotisation.cotisation.afficherModifierException&cotisationId=";
	}
	else{
		detailLink = "naos?userAction=naos.cotisation.cotisation.afficher&selectedId=";
	}
		
		actionDetail = targetLocation + "='" + detailLink + viewBean.getCotisationId(i) + "'";
	%>
	<ct:menuPopup menu="AFOptionsCotisation" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getCotisationId(i)%>">
		<ct:menuParam key="cotisationId" value="<%=viewBean.getCotisationId(i)%>"/>
	</ct:menuPopup>
	<% } else { %>
	&nbsp;
	<% } %>
	</TD>
	<%
		String stCaisseCode = "";
		if (viewBean.getCaisse(i).getAdministration() != null) {
			stCaisseCode = viewBean.getCaisse(i).getAdministration().getCodeAdministration();
		}
		String fontColor = "000000"; // Black
		if (viewBean.getMotifFin(i).equals(CodeSystem.MOTIF_FIN_EXCEPTION)) {
			fontColor = "FF0000"; // Red
		} else if (viewBean.getPlanAffiliation(i)!=null && viewBean.getPlanAffiliation(i).isInactif().booleanValue()) {
			fontColor = "999999"; // grey
		}
	%>
	<TD class="mtd" width="200" onClick="<%=actionDetail%>" align="left"><FONT color="<%=fontColor%>"><%=viewBean.getAssurance(i).getAssuranceLibelle()%></FONT></TD>
	<TD class="mtd" width="70" onClick="<%=actionDetail%>" align="right"><FONT color="<%=fontColor%>"><%=stCaisseCode%></FONT></TD>
	<TD class="mtd" width="70" onClick="<%=actionDetail%>" align="center"><FONT color="<%=fontColor%>"><%=viewBean.getDateDebut(i)%></FONT></TD>
	<TD class="mtd" width="70" onClick="<%=actionDetail%>" align="center"><FONT color="<%=fontColor%>">
	<% if (!JadeStringUtil.isIntegerEmpty(viewBean.getCotisationId(i)) || !JadeStringUtil.isEmpty(viewBean.getDateFinMin(i))) { %>
	<%=viewBean.getDateFin(i)%>
	<% } else { %>
	<%=viewBean.getDateFinMin(i)%>
	<% } %>
	</FONT></TD>
	<% 	String ajoutPeriode = "";
		if(CodeSystem.PERIODICITE_ANNUELLE.equals(viewBean.getPeriodicite(i)) && !JadeStringUtil.isDecimalEmpty(viewBean.getTraitementMoisAnnee(i))) {
			ajoutPeriode += "/"+CodeSystem.getLibelle(session,viewBean.getTraitementMoisAnnee(i));
		}
	%>
	<TD class="mtd" width="100" onClick="<%=actionDetail%>" align="left"><FONT color="<%=fontColor%>"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getPeriodicite(i))+ajoutPeriode%></FONT></TD>
	<TD class="mtd" width="150" style="align:right" onClick="<%=actionDetail%>" align="right" ><FONT color="<%=fontColor%>">
		<%=JANumberFormatter.fmt(JANumberFormatter.deQuote(viewBean.getMontantPeriodicite(i)),true,true,true,2)%>
		<%=JANumberFormatter.fmt(JANumberFormatter.deQuote(viewBean.getMassePeriodicite(i)),true,true,true,2)%></FONT></TD>
	<TD class="mtd" width="150" onClick="<%=actionDetail%>" align="right"><FONT color="<%=fontColor%>">
		<%=JANumberFormatter.fmt(JANumberFormatter.deQuote(viewBean.getMontantAnnuel(i)),true,true,true,2)%>

		<% if (viewBean.getMotifFin(i).equals(globaz.naos.translation.CodeSystem.MOTIF_FIN_EXCEPTION)) {%>
			<%=("0")%>
		<% 	} else { %>
			<%=	JANumberFormatter.fmt(globaz.globall.util.JANumberFormatter.deQuote(viewBean.getMasseAnnuelle(i)),true,true,true,2)%>
			<%}%>
			</FONT></TD>
	<TD class="mtd" width="150" onClick="<%=actionDetail%>" align="left" ><FONT color="<%=fontColor%>">
		<%=CodeSystem.getLibelle(session,viewBean.getMotifFin(i))%></FONT></TD>
	<!-- if (viewBean.getMaisonMere(i).booleanValue()) { -->
	<!--TD class="mtd" width="150" onClick="<=actionDetail>" align="center" ><IMG src="<%=request.getContextPath()%>/images/select2.gif" ></TD-->
	<!-- } else { -->
	<!--TD class="mtd" width="150" onClick="<=actionDetail>" align="center"><=""></TD-->
	<!-- } -->
	<% } // else de if(!triGenre.equals(genreAssurance)) %>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<% if(!JadeStringUtil.isBlankOrZero(viewBean.getTotalAnnuel())) {%>
	<TR bgcolor="black">
		<TD colspan="9"></TD>
	</TR>
	<TR class="<%=rowStyle%>"
		onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'rowHighligthed')" onMouseOut="jscss('swap', this, 'rowHighligthed', '<%=rowStyle%>')">
		<TD class="mtd" align="left" colspan="6"><b>Gesamtzahl der aktiven persönlichen Beiträge</b></TD>
		<!--TD class="mtd" align="left">&nbsp;</TD>
		<TD class="mtd" align="left">&nbsp;</TD>
		<TD class="mtd" align="left">&nbsp;</TD>
		<TD class="mtd" align="left">&nbsp;</TD>
		<TD class="mtd" align="left">&nbsp;</TD-->
		<TD class="mtd" align="right"><%=viewBean.getTotalPeriodique()%></TD>
		<TD class="mtd" align="right"><%=viewBean.getTotalAnnuel()%></TD>
		<TD class="mtd" align="left">&nbsp;</TD>
	</TR>
	<% } %>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>