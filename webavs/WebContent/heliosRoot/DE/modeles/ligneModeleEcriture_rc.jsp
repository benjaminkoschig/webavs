<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">


<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.helios.db.interfaces.*,globaz.helios.db.modeles.*" %>
<%
	idEcran = "GCF4013";
	CGLigneModeleEcritureViewBean viewBean = (CGLigneModeleEcritureViewBean) session.getAttribute("viewBean");

	CGModeleEcriture modeleEcriture = viewBean.retrieveModeleEcriture();

	actionNew = servletContext + mainServletPath + "?userAction=helios.parammodeles.gestionModele.afficher&_method=add&idModeleEcriture="+viewBean.getIdModeleEcriture();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CG-modele" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdModeleEcriture()%>"/>
</ct:menuChange>

<SCRIPT>
usrAction = "helios.modeles.ligneModeleEcriture.lister";
bFind = true;

function updateCompte(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById('forIdCompte').value = element.idCompte;
	} else {
		document.getElementById('forIdCompte').value = '';
	}
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Buchungen der Vorlage<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>


                        <TR>
							<TD width="100">Nummer</TD>
                            <TD>
                            	<input type="text" class="libelleLongDisabled" readonly name="forIdModeleEcriture" value="<%=modeleEcriture.getIdModeleEcriture()%>"/>
                            </TD>
                      	</TR>
                        <TR>
							<TD>Vorlage</TD>
                            <TD>
	                            <input type="text" class="libelleLongDisabled" readonly name="libelle" value="<%=modeleEcriture.getLibelle()%>">
                            </TD>
                      	</TR>
                        <TR><TD colspan="2"><hr/></TD></TR>
						<TR>
						<TD width="128">Konto</TD>
                        <TD>
						<%
							String jspLocation = servletContext + "/heliosRoot/compte_select.jsp";
							String params = "";
						%>

								<%
									int autoCompleteStart = globaz.helios.parser.CGAutoComplete.getAutoCompleteAutoStart();
								%>

                            <input type="hidden" name="forIdCompte" value="">
							<ct:FWPopupList name="forIdExterneCompte" onChange="updateCompte(tag);" validateOnChange="true" params="<%=params%>" value="" className="libelle" jspName="<%=jspLocation%>" minNbrDigit="1" autoNbrDigit="<%=autoCompleteStart%>" forceSelection="true"/>
                            </TD>
                      	</TR>

                        <TR>
							<TD width="128">Kostenstelle</TD>
                            <TD>
                            	<INPUT type="text" name="forIdCentreCharge" size="35" maxlength="40" value="" tabindex="-1">
                            </TD>
                      	</TR>


                        <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>