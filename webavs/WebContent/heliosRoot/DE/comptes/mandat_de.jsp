<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.helios.db.comptes.*,globaz.helios.db.interfaces.*" %>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran ="GCF0013";
	CGMandatViewBean viewBean = (CGMandatViewBean)session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getIdMandat();
	userActionValue = "helios.comptes.mandat.modifier";
	String aucune = "Aucune";
	if (languePage.equalsIgnoreCase("DE"))
		aucune = "Keine";
	
%>

<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>



<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
	document.forms[0].elements('userAction').value="helios.comptes.mandat.ajouterMandat";
	typePlanComptableChange(); 
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="helios.comptes.mandat.ajouterMandat";
    else
        document.forms[0].elements('userAction').value="helios.comptes.mandat.modifier";
    
    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="helios.comptes.mandat.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="helios.comptes.mandat.supprimer";
        document.forms[0].submit();
    }
}

function doAVSSettings() {
	var pc = document.getElementById("idTypePlanComptable");
	var cb = document.getElementsByName("estComptabiliteAVS")[0];
	
	if (cb.checked){
		for (i=0;i<pc.length;i++) {
          		if (pc[i].value == "<%=CGMandatViewBean.CS_PC_AVS%>") {
              		pc[i].selected = true;
	                     break;
       	       }
       	}    
	} else {

		if (pc.value == "<%=CGMandatViewBean.CS_PC_AVS%>") {
              		pc[0].selected = true;
		}
	}
	typePlanComptableChange();
}

function typePlanComptableChange () {
	var pc = document.getElementById("idTypePlanComptable");
	var cb = document.getElementsByName("estComptabiliteAVS")[0];
	var ventilerCompte = document.getElementsByName("ventilerCompte1102")[0];
	var controleCompte = document.getElementsByName("controleCompte1106")[0];
	var ventilerCredits = document.getElementsByName("utilise605Dcmf")[0];
	var consolidation = document.getElementsByName("estMandatConsolidation")[0];
	
	if (pc.value == "<%=CGMandatViewBean.CS_PC_AVS%>") {
       	cb.checked = true;
       	cb.disabled = false;
		ventilerCompte.disabled = false;
		controleCompte.disabled = false; 
		ventilerCredits.disabled = false;
		consolidation.disabled = true;		
		consolidation.checked = false;
	} else if (pc.value == "<%=CGMandatViewBean.CS_PC_OFAS%>") {
		cb.checked = false;
		cb.disabled = true;
		ventilerCompte.disabled = true;
		controleCompte.disabled = true; 
		ventilerCredits.disabled = true;
		consolidation.disabled = false;
		consolidation.checked = true;
	} else {
       	cb.checked = false;
       	cb.disabled = false;
		ventilerCompte.disabled = true;
		controleCompte.disabled = true; 
		ventilerCredits.disabled = true;
		consolidation.disabled = true;
		consolidation.checked = false;
	}
}

function init() {}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail eines Mandanten<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
    			<tr><td>
				<table>
					<tr>
						<TD width="155">Nummer</TD>
						<TD width="39"></TD>
						<TD width="458"><input type="text" name="idMandat" <%=((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add")))?"class='libelleLong'":"readonly class='libelleLongDisabled'"%> value="<%=viewBean.getIdMandat()%>">*</TD>
					</tr>
					<tr>
						<TD width="155">Name</TD>
						<TD width="39">FR</TD>
						<TD width="458"><input type="text" class="libelleLong" name="libelleFr" value="<%=viewBean.getLibelleFr()%>"><%=CGLibelle.getLibelleMandatory((CGLibelleInterface)viewBean,"FR")%></TD>
					</tr>
					<tr>
						<TD width="155"></TD>
						<TD width="39">DE</TD>
						<TD width="458"><input type="text" class="libelleLong" name="libelleDe" value="<%=viewBean.getLibelleDe()%>"><%=CGLibelle.getLibelleMandatory((CGLibelleInterface)viewBean,"AL")%></TD>
					</tr>
					<tr>
						<TD width="155"></TD>
						<TD width="39">IT</TD>
						<TD width="458"><input type="text" class="libelleLong" name="libelleIt" value="<%=viewBean.getLibelleIt()%>">	<%=CGLibelle.getLibelleMandatory((CGLibelleInterface)viewBean,"IT")%>	</TD>
					</tr>
					<tr>
						<TD width="155">Adresse</TD>
						<TD width="39"></TD>
						<TD width="458">
							<textarea rows="5"  width="250" align="left" readonly class="libelleLongDisabled"></textarea>
							<%
								Object[] tiersMethods = new Object[]{
									new String[]{"setIdTiers","getIdTiers"}

									
								};
							%>
							<ct:FWSelectorTag name="tiersSelector"  methods="<%=tiersMethods%>" providerApplication="pyxis" providerPrefix="TI" providerAction="pyxis.tiers.tiers.chercher"/>
							<input type="text" value="<%=viewBean.getIdTiers()%>" name="idTiers">*
							</TD>
					</tr>
					
					<TR>
						<TD width="155">Kassennummer *</TD>
						<TD width="39"></TD>
						<TD width="458"><input type="text" value="<%=(!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getNoCaisse()) ? viewBean.getNoCaisse() : "")%>" name="noCaisse"/></TD>
					</TR>
					
					<TR>
						<TD width="155">Filialenummer *</TD>
						<TD width="39"></TD>
						<TD width="458"><input type="text" value="<%=(!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getNoAgence()) ? viewBean.getNoAgence() : "")%>" name="noAgence"/></TD>
					</TR>
					
					<tr><td colspan="3">&nbsp;</td></tr>
					<tr>
						<TD width="155">Art des Kontoplanes</TD>
						<TD width="39"></TD>
						<TD width="458"><%if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) {%>
							<ct:FWCodeSelectTag name="idTypePlanComptable" defaut="<%=viewBean.getIdTypePlanComptable()%>" libelle="libelle" codeType="CGTYPLAN"/>
							<script>
								document.getElementById("idTypePlanComptable").onchange=function() {typePlanComptableChange();}
							</script>
						<%} else {%>
							<input  type="text" readonly class="libelleLongDisabled" value="<%=globaz.helios.translation.CodeSystem.getLibelle(session,viewBean.getIdTypePlanComptable())%>" >*
						<%}%></TD>
					</tr>
					<tr>
						<TD width="155">Hauptklassifikation</TD>
						<TD width="39"></TD>
						<TD width="458">
							<ct:FWListSelectTag name="idClassificationPrincipale" defaut="<%=viewBean.getIdClassificationPrincipale()%>" data="<%=globaz.helios.translation.CGListes.getClassificationListe(session, viewBean.getIdMandat(), aucune)%>"/>

						</TD>
					</tr>
				</TABLE>
				<table>
					<col width="150">
					<col width="200">
					<col width="150">
					<col width="200">
					<tr>
						<TD width="159">Mandant gesperrt </TD>
						<TD width="187"><input type="checkbox" name="estVerrouille" <%=(viewBean.isEstVerrouille().booleanValue())?"CHECKED":""%>></TD>
						
						<TD width="159">Hauptbücher verwenden</TD>
						<TD width="187">
						<%if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) { %>
							<input type="checkbox" name="utiliseLivres" <%=(viewBean.isUtiliseLivres().booleanValue())?"CHECKED":""%>>
						<%} else {%>
							<input type="checkbox" <%=(viewBean.isUtiliseLivres().booleanValue())?"CHECKED":""%> disabled readonly>
							<input type="hidden" name="utiliseLivres" value="<%=(viewBean.isUtiliseLivres().booleanValue())?"on":""%>">
						<%}%>	
						</TD>
						
						
					</tr>
					
						<tr>
						<TD width="159">AHV-Buchhaltung </TD>
						<TD width="187">
						<%if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) { %>
							<input type="checkbox" name="estComptabiliteAVS" onClick="doAVSSettings()" <%=(viewBean.isEstComptabiliteAVS().booleanValue())?"CHECKED":""%>>
						<%} else {%>
							<input type="checkbox" <%=(viewBean.isEstComptabiliteAVS().booleanValue())?"CHECKED":""%> disabled readonly>
							<input type="hidden" name="estComptabiliteAVS" value="<%=(viewBean.isEstComptabiliteAVS().booleanValue())?"on":""%>">
						<%}%>
						</TD>
						
						<TD width="159">Konsolidierung Mandant</TD>
						<TD>
						<%if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) { %>
							<input type="checkbox" name="estMandatConsolidation" <%=(viewBean.isEstMandatConsolidation().booleanValue())?"CHECKED":""%> disabled/>
						<%} else {%>
							<input type="checkbox" <%=(viewBean.isEstMandatConsolidation().booleanValue())?"CHECKED":""%> disabled readonly/>
							<input type="hidden" name="estMandatConsolidation" value="<%=(viewBean.isEstMandatConsolidation().booleanValue())?"on":""%>"/>
						<%}%>
						</TD>
					</tr>
					
					<tr>
						<td>Verteilung Konto 1102 KK Beitragspfl.</td>
						<td><input type="checkbox" name="ventilerCompte1102" <%=(viewBean.isVentilerCompte1102().booleanValue())?"CHECKED":""%> <%=(((request.getParameter("_method")==null)||(!request.getParameter("_method").equals("add")))&&(!viewBean.isEstComptabiliteAVS().booleanValue()))?"disabled readonly":""%>></td>
						
						<TD width="159">Kontrolle Konti 1106/2740 Schadenersatz</TD>
						<TD width="187"><input type="checkbox" name="controleCompte1106" <%=(viewBean.isControleCompte1106().booleanValue())?"CHECKED":""%> <%=(((request.getParameter("_method")==null)||(!request.getParameter("_method").equals("add")))&&(!viewBean.isEstComptabiliteAVS().booleanValue()))?"disabled readonly":""%>></TD>
					</tr>
					<tr>						
						<td>Aufteilung der Kredite (605 DCMF)</td>
						<td><input type="checkbox" name="utilise605Dcmf" <%=(viewBean.isUtilise605Dcmf().booleanValue())?"CHECKED":""%> <%=(((request.getParameter("_method")==null)||(!request.getParameter("_method").equals("add")))&&(!viewBean.isEstComptabiliteAVS().booleanValue()))?"disabled readonly":""%>></td>
						
						<TD>&nbsp;</TD>
						<TD>&nbsp;</TD>
					</tr>
				</table>
				<table>
					<tr>
					<%if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) {%>

						<td>Rechnungsjahr Beginndatum</td>
						<TD width="187">
						<ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>" />*</TD>
						<td>Rechnungsjahr Enddatum</td>
						<td>
						<ct:FWCalendarTag name="dateFin" value="<%=viewBean.getDateFin()%>" />*</td>
					<%} else {%>
						<!-- pour passer le test de validation (voir _validate())-->
						<input type="hidden" name="dateDebut" value="01.01.2000">
						<input type="hidden" name="dateFin" value="02.01.2000">
					<%}%>
					</tr>
				</table>
			</td></tr>
			<tr><td colspan="3">&nbsp;</td></tr>
			<tr><td colspan="3" class="mtdSpecial">* optional</td></tr>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="CG-mandat" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdMandat()%>"/>
	<ct:menuSetAllParams key="forIdMandat" value="<%=viewBean.getIdMandat()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>