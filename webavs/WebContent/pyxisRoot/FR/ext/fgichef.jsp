<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.globall.util.*"%>
<%@page import="java.util.List"%>
<%@page import="globaz.pyxis.ext.FGIPerfgiVO"%>

<%
	idEcran ="GTI5002";
	globaz.pyxis.ext.FGIChef viewBean = (globaz.pyxis.ext.FGIChef) session.getAttribute("viewBean");
	userActionValue = "pyxis.ext.fgi.genererChef";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	
%>
<SCRIPT language="JavaScript">

function validate() {
	var rue = document.getElementsByName("fgiRueHameau")[0].value
	var nomPrenom = document.getElementsByName("fgiNomPrenom")[0].value
	var profParChez = document.getElementsByName("fgiProfParChez")[0].value
	var localite = document.getElementsByName("fgiLocalite")[0].value

	if (
		(rue.length >20) ||
		(nomPrenom.length >20) ||
		(profParChez.length >20) ||
		(localite.length >14) 
	) {
		return window.confirm("Avez-vous ajusté les zones tronquées ?");
	} else {
		return true;
	}
}

top.document.title = "FGI Chef"
var fieldList = new Array(<%=viewBean.getEqFields()%>);
function removeEq() {
	for (i= 0;i<fieldList.length;i++) {
		if (document.getElementsByName(fieldList[i])[0].value == '=') {
								document.getElementsByName(fieldList[i])[0].value = '';
		};
	}
}
function addEq() {
	for (i= 0;i<fieldList.length;i++) {
		if (document.getElementsByName(fieldList[i])[0].value == '') {
			document.getElementsByName(fieldList[i])[0].value = '=';
		};
	}
}
var oldNEAVSSave=""
var oldNECommuneSave=""

function saveNE() {
	oldNEAVSSave = document.getElementsByName('fgiNeAncien')[0].value;
	oldNECommuneSave= document.getElementsByName('fgiCommuneAncien')[0].value;
}

function restoreNE() {
	document.getElementsByName('fgiNeAncien')[0].value = oldNEAVSSave;
	document.getElementsByName('fgiCommuneAncien')[0].value = oldNECommuneSave;
}



</SCRIPT>




<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>FGI Chef<%-- /tpl:put  --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 
		      <tr>
				<td>
				
				<b>Coordonnées</b>
		<fieldset>
			<LEGEND align=top>DB</LEGEND>
			<table>
				<tr>
					<td>Nom/Rais. soc. 1</td>
					<td><input type="text" name="dbDesignation1" readonly class="libelleLongDisabled" value="<%=viewBean.getDbDesignation1()%>"></td>
				
					<td>Prénom/Rais. soc. 2</td>
					<td>
						<input type="text" name="dbDesignation2" readonly  class="libelleLongDisabled" value="<%=viewBean.getDbDesignation2()%>">
						<input type="hidden" name="personnePhysique"  value="<%=(viewBean.getPersonnePhysique().booleanValue())?"on":"" %>">
						<input type="hidden" name="individuel" value="<%=viewBean.getIndividuel()%>">
					</td>
				
				<!--
					<td>Nom d'alliance</td>
					<td><input type="text" name="dbNomAlliance"></td>
				-->
				</tr>
			</table>
		</fieldset>
		<fieldset >
			<LEGEND align=top>FGI</LEGEND> 
			<table>
			
				<tr>
					<td>NE nouveau</td>
					<td><input type="text" maxlength ="3" size="2" name="fgiCommune" value="<%=viewBean.getFgiCommune()%>">
						<input type="text" class='libellelong' name="fgiNeNouveau" value="<%=viewBean.getFgiNeNouveau()%>"></td>
					<td>&nbsp;</td>
					<td>Commune</td>
					<td><select onchange ="document.getElementsByName('fgiCommune')[0].value=this.value" name="fgiCommunesSelect">
					<% for (java.util.Iterator it = globaz.pyxis.ext.FGICommunesProvider.getCommunes().iterator();it.hasNext();) {
						globaz.pyxis.ext.FGICommune commune =(globaz.pyxis.ext.FGICommune) it.next();
					%>
						<option value="<%=commune.getNumero()%>"><%=commune.getLibelle()%></option>
					<%}%>
					</select></td>
				</tr>
				<tr valign="top">
					<td>NE ancien</td>
					<td><input type="text" class="disabled" maxlength ="3" name="fgiCommuneAncien" size="2" readonly value="<%=viewBean.getFgiCommuneAncien()%>">
						<input name="fgiNeAncien" type="text" class="libelleLongDisabled" readonly value="<%=viewBean.getFgiNeAncien()%>">
					</td>
					<td>&nbsp;</td>
					<td colspan="2" rowspan="3">
						<fieldset>
							<legend>Genre traitement</legend>
							<input onclick="saveNE();removeEq();document.getElementsByName('fgiCommuneAncien')[0].value='';document.getElementsByName('fgiNeAncien')[0].value='';" type="radio" <%=(("A".equals(viewBean.getFgiGenreTraitement()))?"checked":"")%> name="fgiGenreTraitement" value="A">Ajout
							<input onclick="restoreNE();addEq()" type="radio" <%=(("C".equals(viewBean.getFgiGenreTraitement()))?"checked":"")%> name="fgiGenreTraitement" value="C">Correction
						</fieldset>
						
					</td>
				</tr>
				
				<% 
				List list = viewBean.getOldNE();
				if(viewBean.getOldNE().size()==1) {
					FGIPerfgiVO vo = (FGIPerfgiVO) list.get(0);
				 %>
					<script>
						document.getElementsByName('fgiCommuneAncien')[0].value='<%=vo.getNumeroCommune()%>';
						document.getElementsByName('fgiNeAncien')[0].value='<%=globaz.pyxis.ext.FGIAbstractHelper.formatNumAvs11(vo.getNumAvs11())%>';
						<%if ("A".equals(viewBean.getFgiGenreTraitement())) {%>
							saveNE();
							document.getElementsByName('fgiCommuneAncien')[0].value='';document.getElementsByName('fgiNeAncien')[0].value='';
						<%}%>
					
					<%if("S".equals(viewBean.getFgiCodeSexeSociete())) {%>	
						document.getElementsByName('fgiNeNouveau')[0].value='<%=globaz.pyxis.ext.FGIAbstractHelper.formatNumAvs11(vo.getNumAvs11())%>';
					<%}%>	
					document.getElementsByName('fgiCommune')[0].value='<%=vo.getNumeroCommune()%>';
					</script>
				<% } else if(viewBean.getOldNE().size()>0) { %>
					<tr><td colspan='2'><div style='background-color:white'><table border='1' cellpadding='0' cellspacing='0'>
					
					<tr><th>&nbsp;</th><th>NIP</th><th>N° com.</th><th>NE</th><th>Section</th><th>MF</th><th>Type</th><th>Date</th><th>Visa</th></tr>
					<%
						
						for (java.util.Iterator it = list.iterator();it.hasNext();){
							
							FGIPerfgiVO vo = (FGIPerfgiVO) it.next();
					%>
							<tr>
								<td><input type='radio' name='selNe' onclick="document.getElementsByName('fgiCommune')[0].value='<%=vo.getNumeroCommune()%>';document.getElementsByName('fgiCommuneAncien')[0].value='<%=vo.getNumeroCommune()%>';document.getElementsByName('fgiNeAncien')[0].value='<%=globaz.pyxis.ext.FGIAbstractHelper.formatNumAvs11(vo.getNumAvs11())%>';"></td>
								<td><%=vo.getNip()%></td>
								<td><%=vo.getNumeroCommune()%></td>
								<td><%=globaz.pyxis.ext.FGIAbstractHelper.formatNumAvs11(vo.getNumAvs11())%></td>
								<td><%=vo.getSecteur()%></td>
								<td><%=vo.getMf()%></td>
								<td><%=vo.getTypeNip()%></td>
								<td><%=vo.getTimeStamp()%></td>
								<td><%=vo.getVisa()%>&nbsp;</td>
							</tr>
						<%}%>
						</table></div></td></tr>		
					<%}%>
				<tr>
					<td>NIP</td>
					<td><input name="fgiNip" type="text" readonly class='disabled' value="<%=viewBean.getFgiNip()%>"></td>
				</tr>
				<tr>
					<td>Date de naissance</td>
					<td><input name="fgiDateNaissance" type="text" readonly class='disabled' value="<%=viewBean.getFgiDateNaissance()%>"></td>
				</tr>
				<tr>
					<td>Code sexe/société</td>
					<td><input name="fgiCodeSexeSociete"   maxlength="1" readonly class='disabled' size="1" type="text" value="<%=viewBean.getFgiCodeSexeSociete()%>"></td>
				</tr>
				<tr>
					<td>Nom et prénom</td>
					<td><input name="fgiNomPrenom"  type="text" value="<%=viewBean.getFgiNomPrenom()%>"></td>
					<td>&nbsp;</td>
					<td>Commission impôt</td>
					<td><input name="fgiComissionImpot" type="text"  <%=(("S".equals(viewBean.getFgiCodeSexeSociete()))?"readonly class='disabled'":"")%> maxlength="1"  size="1" value="<%=viewBean.getFgiComissionImpot() %>"></td>
				</tr>
				<tr>
					<td>Complément Nom</td>
					<td><input name="fgiComplementNom" type="text" <%=(("S".equals(viewBean.getFgiCodeSexeSociete()))?"readonly class='disabled'":"")%> maxlength ="20" value="<%=viewBean.getFgiComplementNom()%>"></td>
					<td>&nbsp;</td>
					<td>LPP</td>
					<td><input name="fgiLpp" type="text"  maxlength="1" size="1" value="<%=viewBean.getFgiLpp()%>"></td>
				</tr>
				<tr>
					<td>Code complément</td>
					<td><input name="fgiCodeComplement"  <%=(("S".equals(viewBean.getFgiCodeSexeSociete()))?"readonly class='disabled'":"")%> maxlength="1" size="1" type="text" value="<%=viewBean.getFgiCodeComplement()%>"></td>
					<td>&nbsp;</td>
					<td>LAA</td>
					<td><input name="fgiLaa" type="text"   maxlength="1" size="1" value="<%=viewBean.getFgiLaa()%>"></td>
				</tr>
				<tr>
					<td>Etat civil</td>
					<td>
						<%if("S".equals(viewBean.getFgiCodeSexeSociete())) {%>
							<select name="fgiCodeEtatCivil"> 
								
								<%if("C".equals(viewBean.getFgiGenreTraitement())) {%>
									<option value="=">=</option>
									<option value=""></option>
								<%} else {%>
									<option value=""></option>
									<option value="=">=</option>
								<%}%>
								<option value="HO">Hoirie</option>
								<option value="SS">Société simple</option>
								<option value="NC">Société en nom collectif</option>
								<option value="CS">Société en commandite simple</option>
								<option value="SA">Société anonyme</option>
								<option value="CA">Société en commandite par actions</option>
								<option value="RL">Société à responsabilité limitée</option>
								<option value="SI">Société immobilière</option>
								<option value="CO">Société coopérative</option>
								<option value="AS">Association</option>
								<option value="FO">Fondation</option>
								<option value="CP">Corporation/Etablissement de droit public</option>
							</select> 
						<%} else {%>
							<input readonly class ="disabled" name="fgiCodeEtatCivil" type="text" maxlength ="2" size="1" value="<%=viewBean.getFgiCodeEtatCivil()%>"> 
						<%}%>
						
						<%if(!"S".equals(viewBean.getFgiCodeSexeSociete())) {%>
						Depuis&nbsp;<input name="fgiDateEtatCivil" type="text" maxlength ="7" size="6" value="<%=viewBean.getFgiDateEtatCivil()%>">
						<%}%>
						</td>
					<td>&nbsp;</td>
					<td>Prof. principale</td>
					<td><input name="fgiProfPrincipale" type="text" <%=(("S".equals(viewBean.getFgiCodeSexeSociete()))?"readonly class='disabled'":"")%>  maxlength="1" size="1" value="<%=viewBean.getFgiProfPrincipale()%>"></td>
				</tr>
				<tr>
					<td>Nationalité</td>
					<td><input name="fgiNationalite" type="text" readonly class='disabled' maxlength="3" size="2" value="<%=viewBean.getFgiNationalite()%>"></td>
					<td>&nbsp;</td>
					<td>Prof. accessoire</td>
					<td><input name="fgiProfAccessoire" type="text"  <%=(("S".equals(viewBean.getFgiCodeSexeSociete()))?"readonly class='disabled'":"")%> maxlength="1" size="1" value="<%=viewBean.getFgiProfAccessoire()%>"></td>
				</tr>
				<tr>
					<td>Code tuteur</td>
					<td><input name="fgiCodeTuteur" type="text"  <%=(("S".equals(viewBean.getFgiCodeSexeSociete()))?"readonly class='disabled'":"")%> maxlength="1" size="1" value="<%=viewBean.getFgiCodeTuteur()%>"></td>
					<td>&nbsp;</td>
					<td>No contribuable</td>
					<td><input  name="fgiNoContribuable" type="text"  value="<%=viewBean.getFgiNoContribuable()%>"></td>
				</tr>
				<tr>
					<td>Code réfugié</td>
					<td><input name="fgiCodeRefugie" type="text" <%=(("S".equals(viewBean.getFgiCodeSexeSociete()))?"readonly class='disabled'":"")%>  maxlength="1" size="1" value="<%=viewBean.getFgiCodeRefugie()%>"></td>
					<td>&nbsp;</td>
					<!-- 
					<td>Agent Philos</td>
					<td><input name="fgiAgentPhilos" type="text" maxlength="2" size="1" value="="></td>
					 -->
				</tr>
			</table>	
		</fieldset>
		<hr>
		<b>Adresse de domicile</b>
		<table>
		<tr>
		<td valign="top">
		<fieldset>
			<LEGEND align=top>DB</LEGEND> 
			<textarea name="adresseText" rows="6" cols="30"  class="disabled" readonly><%=viewBean.getAdresseText()%></textarea>
		</fieldset>
		</td>
		<td valign ="top">
		<fieldset >
			<LEGEND align=top>FGI</LEGEND> 
			<table>
				<tr>
					<td>Prof./Par/Chez</td>
					<td><input name="fgiProfParChez" type="text" value="<%=viewBean.getFgiProfParChez()%>"></td>
				</tr>
				<tr>
					<td>Rue/Hameau</td>
					<td><input name="fgiRueHameau" type="text" value="<%=viewBean.getFgiRueHameau()%>"></td>
				</tr>
				<tr>
					<td>Npa/Localité</td>
					<td><input name="fgiNpa" type="text" maxlength="4" size="3" value="<%=viewBean.getFgiNpa()%>">
					<input name="fgiLocalite" type="text" value="<%=viewBean.getFgiLocalite()%>"></td>
				</tr>
				<tr>
					<td>Canton</td>
					<td><input name="fgiCanton" type="text" maxlength="3" size="1" value="<%=viewBean.getFgiCanton()%>"></td>
				</tr>
			</table>			
		</fieldset>
		</td>
		</tr>
		</table>
		<hr>
		<table width="100%"><tr>
		<td>
		Texte libre <input name="texteLibre" maxlength="67" size="70" type="text" value="<%=viewBean.getTexteLibre() %>">
		</td>
		<td>
			CICS :
			<input type="radio" name="cics" checked value="test">Test
			<input type="radio" name="cics" value="prod">Prod
			<input type="hidden" name="destinationUrl" value="<%=viewBean.getDestinationUrl()%>">
		</td>
		
		</tr>
		</table>
				
				</td>
             </tr>			

          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%> 
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>
<ct:menuChange displayId="options" menuId="TIMenuVide" showTab="menu">
</ct:menuChange>

<script>
// menu 
//top.fr_menu.location.replace('appMenu.jsp?_optionMenu=-defaut-&changeTab=Menu');	
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>