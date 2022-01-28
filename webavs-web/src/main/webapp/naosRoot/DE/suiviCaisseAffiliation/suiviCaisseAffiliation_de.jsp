<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.naos.properties.AFProperties" %>
<%@ page import="globaz.framework.bean.FWViewBeanInterface" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran ="CAF0059";
	globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationViewBean viewBean 
		= (globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationViewBean)session.getAttribute ("viewBean");
	String method = request.getParameter("_method");

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">


function add() {
	document.forms[0].elements('userAction').value="naos.suiviCaisseAffiliation.suiviCaisseAffiliation.ajouter"
}

function upd() {
}

function validate() {
	var exit = true;
	document.forms[0].elements('userAction').value="naos.suiviCaisseAffiliation.suiviCaisseAffiliation.modifier";
		
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.suiviCaisseAffiliation.suiviCaisseAffiliation.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.suiviCaisseAffiliation.suiviCaisseAffiliation.modifier";
	return (exit);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="naos.suiviCaisseAffiliation.suiviCaisseAffiliation.afficher";
}

function del() {
	if (window.confirm("Sie sind dabei, die ausgew�hlte Versicherungsverfolgung zu l�schen! Wollen Sie fortfahren?")) {
		document.forms[0].elements('userAction').value="naos.suiviCaisseAffiliation.suiviCaisseAffiliation.supprimer";
		document.forms[0].submit();
	}
}

function init() {
	onChangeGenre();
	onChangeMotif();
}

function onChangeGenre(){
	var newGenreValeur = document.forms[0].elements('genreCaisse').value;
	var newMotif = document.forms[0].elements('motif').value;
	if (newGenreValeur == <%=globaz.naos.translation.CodeSystem.GENRE_CAISSE_LPP%>&&newMotif=="") {
		document.all('attestationIPTable').style.display = 'block';
		<%
			boolean showGestionDossierCAF = false;
			try {
				showGestionDossierCAF = AFProperties.DISPLAY_GESTION_DOSSIER_CAF.getBooleanValue();
			} catch (Exception e) {
				vBeanHasErrors = true;
				viewBean.setMessage("Die Eigenschaft \"DISPLAY_GESTION_DOSSIER_CAF\" existiert nicht!");
				viewBean.setMsgType(FWViewBeanInterface.ERROR);
			}

			if (showGestionDossierCAF) {
		%>
		document.all('lbRemarque').style.display = 'none';
		document.all('trEnvoieCAF').style.display = 'none';
		document.all('trException').style.display = 'none';
		<%
			}
		%>
	} else if (newGenreValeur == <%=globaz.naos.translation.CodeSystem.GENRE_CAISSE_LAA%>){
		document.forms[0].elements('attestationIp').checked = false;
		document.all('attestationIPTable').style.display = 'none';
		<%
			if (showGestionDossierCAF) {
		%>
		document.all('lbRemarque').style.display = 'none';
		document.all('trEnvoieCAF').style.display = 'none';
		document.all('trException').style.display = 'none';
		<%
			}
		%>
	} else if (newGenreValeur == <%=globaz.naos.translation.CodeSystem.GENRE_CAISSE_AVS%>){
		document.forms[0].elements('attestationIp').checked = false;
		document.all('attestationIPTable').style.display = 'none';
		<%
			if (showGestionDossierCAF) {
		%>
		document.all('lbRemarque').style.display = 'none';
		document.all('trEnvoieCAF').style.display = 'none';
		document.all('trException').style.display = 'none';
		<%
			}
		%>
	} else if (newGenreValeur == <%=globaz.naos.translation.CodeSystem.GENRE_CAISSE_AF%>){
		document.forms[0].elements('attestationIp').checked = false;
		document.all('attestationIPTable').style.display = 'none';
		<%
			if (showGestionDossierCAF) {
		%>
		document.all('lbRemarque').style.display = 'block';
		document.all('trEnvoieCAF').style.display = 'block';
		document.all('trException').style.display = 'block';
		<%
			}
		%>
	}
}

function onChangeMotif(){
	var newMotif = document.forms[0].elements('motif').value;
	var newGenreValeur = document.forms[0].elements('genreCaisse').value;
	if (newMotif != "") {
		document.forms[0].elements('idTiersCaisse').value = "";
		document.forms[0].elements('numeroCaisse').value = "";
		document.forms[0].elements('nomCaisse').value = "";
		document.forms[0].elements('numeroAffileCaisse').value = "";
		//document.forms[0].elements('categorieSalarie').value = "";
		if(document.forms[0].elements('caisseSelector')!=null)
			document.forms[0].elements('caisseSelector').disabled = true;
		document.forms[0].elements('numeroAffileCaisse').disabled = true;
		document.all('attestationIPTable').style.display = 'none';
	}else{
		if(document.forms[0].elements('caisseSelector')!=null)
			document.forms[0].elements('caisseSelector').disabled = false;
		document.forms[0].elements('numeroAffileCaisse').disabled = false;
		document.forms[0].elements('attestationIp').disabled = false;
		if (newGenreValeur == <%=globaz.naos.translation.CodeSystem.GENRE_CAISSE_LPP%>) {
			document.all('attestationIPTable').style.display = 'block';
		}
	}
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Kassen - Detail
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					    <naos:AFInfoAffiliation name="affiliationId" affiliation="<%=viewBean.getAffiliation()%>" titleWidth="150" colspan="2" /> 

						<TR> 
							<TD nowrap  height="12" colspan="3"> 
								<hr size="3" width="100%"><INPUT type="hidden" name="selectedId" value="<%=viewBean.getSuiviCaisseId()%>">
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="120" height="31">Versicherungsart</TD>
							<TD nowrap width="150">
								
									<ct:FWCodeSelectTag 
										name="genreCaisse" 
										defaut="<%=viewBean.getGenreCaisse()%>"
										codeType="VEGENCAISS"/>
									<script>
										document.getElementsByName('genreCaisse')[0].onchange = function() {onChangeGenre();};
									</script>
								<%if(!"add".equals(request.getParameter("_method"))){
									String genreValue = globaz.naos.translation.CodeSystem.getLibelle(viewBean.getSession(),viewBean.getGenreCaisse());
									%>
									<INPUT name="genreCaisseDummy" size="10" type="hidden" value="<%=genreValue%>" readonly="readonly" tabindex="-1" class="Disabled">
									<INPUT name="genreCaisse" size="10" type="hidden" value="<%=viewBean.getGenreCaisse()%>" >
								<%}%>
							</TD>
							<TD>
								<TABLE border="0" cellspacing="0" cellpadding="0" id="attestationIPTable">
								<TBODY> 
								<TR>
									<TD nowrap width="110">BV Bescheinigung erhalten</TD>
									<TD nowrap width="30"></TD>
									<TD nowrap width="30">
										<input type="checkbox" name="attestationIp" <%=(viewBean.getAttestationIp().booleanValue())? "checked" : ""%> >Ja
									</TD>
								</TR>
								</TBODY> 
								</TABLE>
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="120" height="31" >Nr. und Name der Kassse</TD>
							
							<TD nowrap colspan="2"> 
								<INPUT type="hidden" name="idTiersCaisse" value="<%=viewBean.getIdTiersCaisse()%>">
								<%
									String localNumeroCaisse = "";
									String localNomCaisse = "";
									if (viewBean.getAdministration() != null) {
										localNumeroCaisse = viewBean.getAdministration().getCodeAdministration();
										localNomCaisse    = viewBean.getAdministration().getNom();
									}
								
								%>
								<INPUT name="numeroCaisse" size="10" type="text" 
									value="<%=localNumeroCaisse%>" readonly="readonly" tabindex="-1" class="Disabled">
								<INPUT name="nomCaisse"    size="50" type="text"  
										value="<%=localNomCaisse%>" readonly="readonly" tabindex="-1" class="Disabled">
								<%
									Object[] caisseMethods= new Object[]{
										new String[]{"setIdTiersCaisse","getIdTiers"}
									};
								%>
								<ct:FWSelectorTag 
									name="caisseSelector" 
									
									methods="<%=caisseMethods%>"
									providerApplication ="pyxis"
									providerPrefix="TI"
									providerAction ="pyxis.tiers.administration.chercher"/> 
							</TD>
						</TR>
						<TR> 
							<TD nowrap height="31" width="120" >Externe Abr.-Nr.</TD>
							
							<TD colspan="2"> 
								<INPUT name="numeroAffileCaisse" size="15" type="text" 
									value="<%=viewBean.getNumeroAffileCaisse()%>">
							</TD>
						</TR>
						<TR> 
							<TD nowrap height="31" width="120" >Periode</TD>
							
							<TD>
								<ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>"/>
								&nbsp;bis&nbsp;
								<ct:FWCalendarTag name="dateFin" value="<%=viewBean.getDateFin()%>"/> 
							</TD>
							<%
								if (showGestionDossierCAF) {
							%>
							<TD><p id="lbRemarque">Bemerkung</p></TD>
						</TR>
						<TR id="trEnvoieCAF">
							<TD nowrap height="31" width="120" >Gesendet an die FZK</TD>
							<TD>
								<ct:FWCalendarTag name="dateEnvoieCAF" value="<%=viewBean.getDateEnvoieCAF()%>"/>
							</TD>
							<TD rowspan="5">
								<TEXTAREA name="remarque" rows="10" cols="40" ><%=viewBean.getRemarque()%></TEXTAREA>
							</TD>
							<%
								}
							%>
						</TR>
						<TR> 
							<TD nowrap height="31" width="120" >Arbeitnehmerkategorie</TD>
							
							<TD>
								<ct:FWCodeSelectTag 
									name="categorieSalarie"
									defaut="<%=viewBean.getCategorieSalarie()%>"
									codeType="VECATSALAR"
									wantBlank="true"/>
							</TD>
						</TR>
						<TR> 
							<TD nowrap height="31" width="120" >Grund warum nicht unterstellt</TD>
							
							<TD>
								<ct:FWCodeSelectTag 
									name="motif"
									defaut="<%=viewBean.getMotif()%>"
									codeType="VEMOTNOSOU"
									wantBlank="true"/>
								<script>
									document.getElementsByName('motif')[0].onchange = function() {onChangeMotif();};
								</script>
							</TD>
							
						</TR>
						<TR> 
							<TD nowrap height="31" width="120" >Zusatzkasse</TD>
							<TD"><input type="checkbox" name="accessoire" <%=(viewBean.getAccessoire().booleanValue())? "checked" : "unchecked"%>></TD>
						</TR>
						<%
							if (showGestionDossierCAF) {
						%>
						<TR id="trException">
							<TD nowrap height="31" width="120" >Ausnahme</TD>
							<TD><input type="checkbox" name="exception" <%=(viewBean.getException().booleanValue())? "checked" : "unchecked"%>></TD>
						</TR>
						<%
							}
						%>
						
						
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
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFOptionsAffiliation" showTab="options">
		<ct:menuSetAllParams key="affiliationId" value="<%=viewBean.getAffiliationId()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getAffiliationId()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>