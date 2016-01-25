<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CGE0006";
	GELotsViewBean viewBean = (GELotsViewBean)session.getAttribute ("viewBean");
	boolean champActif = true;
%>
<%@page import="globaz.campus.vb.lots.GELotsViewBean"%>
<%@page import="globaz.globall.util.JAUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JACalendar"%>
<SCRIPT language="JavaScript">
</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<%
	if (GELotsViewBean.CS_ETAT_VALIDE.equals(viewBean.getCsEtatLot()) || GELotsViewBean.CS_ETAT_COMPTABILISE.equals(viewBean.getCsEtatLot())){
		champActif=false;
	}
	if(!champActif){
		bButtonUpdate=false;
		bButtonDelete=false;
	}
%>
function add() {
	document.forms[0].elements('userAction').value="campus.lots.lots.ajouter";
}

function upd() {
}

function validate() {
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="campus.lots.lots.ajouter";
    else
        document.forms[0].elements('userAction').value="campus.lots.lots.modifier";
	return (true);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="campus.lots.lots.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer le détail d'assurance sélectionné! Voulez-vous continuer?")) {
		document.forms[0].elements('userAction').value="campus.lots.lots.supprimer";
		document.forms[0].submit();
	}
}

function init() {}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Détail d'un lot
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD width="15%">Numéro du lot</TD>
							<TD width="20%"><input name='idLot' class="numeroCourtDisabled" readonly value='<%=viewBean.getIdLot()%>'/></TD>
							<TD width="5%" rowspan="4" valign="top">Ecole</TD>
							<TD width="30%" rowspan="4">
							<textarea name='ecole' readonly class='libelleLongDisabled'
							rows='6'><%=viewBean.getEcoleDescription(viewBean.getIdTiersEcole())%></textarea>
							<%
							String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+ globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/lots/lots_de.jsp";
							Object[] ecoleMethodsName = new Object[]{
								new String[]{"setIdTiersEcole","getIdTiersAdministration"},
							};
							Object[]  ecoleParams = new Object[]{
								new String[]{"ecoleDescription","_pos"},
							};
							%>
				            <ct:FWSelectorTag 
								name="ecoleSelector"
								methods="<%=ecoleMethodsName%>"
								providerApplication ="pyxis"
								providerPrefix="TI"
								providerAction ="pyxis.tiers.administration.chercher&selGenre='509036'&_pos=''"
								providerActionParams ="<%=ecoleParams%>"
								redirectUrl="<%=redirectUrl%>"
							/>
							<input type="hidden" name="selectorName" value="">
							<input name="idTiersEcole" type="hidden" value='<%=viewBean.getIdTiersEcole()!=null?viewBean.getIdTiersEcole():""%>'/>
							</TD>
						</TR>
						<TR>
							<TD>Date de réception / création</TD>
							<%if (champActif){ %>
							<TD><ct:FWCalendarTag name="dateReceptionLot" value="<%=JadeStringUtil.isBlank(viewBean.getDateReceptionLot())?JACalendar.todayJJsMMsAAAA():viewBean.getDateReceptionLot()%>"/></TD>
							<%}else{%>
							<TD><INPUT name='dateReceptionLot' class='libelleLongDisabled'
							readonly tabindex="-1" value='<%=viewBean.getDateReceptionLot()%>'/></TD>
							<%}%>
						</TR>
						<TR>
							<TD>Libellé du lot</TD>
							<%if (champActif){ %>
							<TD><INPUT name='libelleTraitement' class='libelleLong' maxlength="40" size="40" tabindex="-1" 
								value='<%=viewBean.getLibelleTraitement()!=null?viewBean.getLibelleTraitement():""%>'/></TD>
							<%}else{%>
							<TD><INPUT name='libelleTraitement' class='libelleLongDisabled' readonly maxlength="40" size="40" tabindex="-1" 
								value='<%=viewBean.getLibelleTraitement()!=null?viewBean.getLibelleTraitement():""%>'/></TD>
							<%}%>
						</TR>
						<TR>
							<TD>Année</TD>
							<%if (champActif){ %>
							<TD>
							<INPUT name='annee' class='numeroCourt' maxlength="4" size="4" tabindex="-1" 
								value='<%=viewBean.getAnnee()!=null?viewBean.getAnnee():""%>'/>
							</TD>
							<%} else { %>
							<TD>
							<INPUT name='annee' class='numeroCourtDisabled' readonly size="4" size="4" tabindex="-1" 
								value='<%=viewBean.getAnnee()%>'/>
							</TD>
							<%} %>
						</TR>
						<TR>
							<TD>Etat du lot</TD>
								<%if (JadeStringUtil.isBlank(viewBean.getCsEtatLot())){ %>
									<TD>
									<INPUT name="csEtatLot" type="hidden" readonly value="<%=GELotsViewBean.CS_ETAT_A_TRAITER%>" />
									<INPUT name="etatLibelle" type="text" readonly
									class="libelleLongDisabled"
									value="<%=viewBean.getSession().getCodeLibelle(GELotsViewBean.CS_ETAT_A_TRAITER)%>" tabindex="-1">
									</TD>
								<%}else{ %>
									<TD>
									<INPUT name="csEtatLot" type="hidden" readonly value="<%=viewBean.getCsEtatLot()%>" />
									<INPUT name="etatLibelle" class='libelleLongDisabled' readonly
									value='<%=viewBean.getSession().getCodeLibelle(viewBean.getCsEtatLot())%>' tabindex="-1">
									</TD>
								<%}%>
							<INPUT type="hidden" name="selectedId" value="<%=viewBean.getIdLot()%>">
						</TR>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<% if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> 
<SCRIPT>
</SCRIPT> 
<% } %> 
	<ct:menuChange displayId="menu" menuId="GEMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="GEOptionsLot" showTab="options">
		<ct:menuSetAllParams key="idLot" value="<%=viewBean.getIdLot()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>