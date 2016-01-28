<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0033"; %>
<%@ page import="globaz.osiris.db.interets.*" %>
<%@ page import="globaz.globall.util.JAUtil" %>
<%@ page import="globaz.osiris.translation.CACodeSystem" %>
<%
CAInteretMoratoireViewBean viewBean = (CAInteretMoratoireViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);         

String decisionFacturee = "ko";

bButtonNew = ((globaz.globall.db.BSession)session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_SESSION)).hasRight(request.getParameter("userAction"), globaz.framework.secure.FWSecureConstants.ADD);

//Désctivation du bouton nouveau si la décision est facturée
if(viewBean.getStatus().equals(viewBean.STATUS_COMPTABILISE) || viewBean.getStatus().equals(viewBean.STATUS_BLOQUE)) {
	bButtonNew = false;
	decisionFacturee = "ok";
}


actionNew = actionNew + "&idInteretMoratoire=" + viewBean.getIdInteretMoratoire() + "&decisionFacturee=" + decisionFacturee + "&domaine=" + viewBean.getDomaine();
bButtonFind = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	String tmpDomaine = "";
	if (viewBean.isDomaineCA()) { 
		tmpDomaine = "CA";
	} else {
		tmpDomaine = "FA";
	}
%>

<ct:menuChange displayId="options" menuId="CA-DecisionInteretsMoratoires" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdInteretMoratoire()%>"/>
	<ct:menuSetAllParams key="domaine" value="<%=tmpDomaine%>"/>	
</ct:menuChange>

<script language="JavaScript">
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.interets.detailInteretMoratoire.lister";
detailLink = servlet+"?userAction=<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.interets.detailInteretMoratoire.afficher&_method=add&idInteretMoratoire=<%=viewBean.getIdInteretMoratoire()%>&decisionFacturee=<%=decisionFacturee%>&domaine=<%=viewBean.getDomaine()%>";
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Detail einer Verzugszinsenverfügungen<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
						<td colspan="4">
						</td>
						<tr>
							<td rowspan="3" style="vertical-align:top; padding:0 5px">
								<%= (viewBean.isDomaineCA()?"<a href="+request.getContextPath()+"/osiris?userAction=osiris.comptes.apercuComptes.afficher&id="+viewBean.getIdCompteAnnexe()+">Konto</a>":"Debitor") %>
							</td>
							<td rowspan="3" style="vertical-align:top; padding:0 5px">
								<input type="hidden" name="forIdInteretMoratoire" value="<%=viewBean.getIdInteretMoratoire()%>">
								<input type="hidden" name="forDomaine" value="<%=viewBean.getDomaine()%>">
								<textarea style="width: 300px; height: 100px;" readonly class="disabled"><%=viewBean.isDomaineCA()?viewBean.getSection().getCompteAnnexe().getTitulaireEntete():CACodeSystem.getLibelle(session,viewBean.getEnteteFacture().getIdRole())+ " " +viewBean.getIdExterneRoleEcran()+"\r\n"+viewBean.getEnteteFacture().getNomTiers()%></textarea>
							</td>
							<td style="padding:0 5px">
								<a href="<%=request.getContextPath()+(viewBean.isDomaineCA()?"/osiris?userAction=osiris.comptes.apercuParSection.afficher&id="+viewBean.getIdSection():"/musca?userAction=musca.facturation.enteteFacture.afficher&selectedId="+viewBean.getIdSectionFacture())%>" > <%=viewBean.isDomaineCA()?"Sektion":"Abrechnung"%></a>
							</td>
							
							<td style="padding:0 5px"><input type="text" name="section" value=" <%=viewBean.getIdExterneSectionEcran() + " " + (viewBean.isDomaineCA()?viewBean.getSection().getDescription():viewBean.getEnteteFacture().getTypeDescription())%>" style="width:400px" class="disabled" readonly="readonly"></td>
						</tr>
						<tr>
							<td style="padding:0 5px">
								<a href="<%=request.getContextPath()%>\osiris?userAction=osiris.interets.interetMoratoire.afficher&id=<%=viewBean.getIdInteretMoratoire()%>">Verzinsungsentscheidung</a>
							</td>
							<td style="padding:0 5px">
								<input type="text" name="genreInteret" value="<%=CACodeSystem.getLibelle(session, viewBean.getIdGenreInteret())%>" style="width:296px" class="disabled" readonly="readonly">
								<input type="text" name="dateCalcul" value="<%=viewBean.getDateCalcul()%>" style="width:100px" class="disabled" readonly="readonly">
							</td>
						</tr>
						<tr>
							<td style="padding:0 5px">Total</td>
							<td style="padding:0 5px"><input type="text" name="total" value="<%=viewBean.calculeTotalInteret()%>" style="width:400px; text-align:right;" class="disabled" readonly="readonly"></td>
						</tr>
						
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>