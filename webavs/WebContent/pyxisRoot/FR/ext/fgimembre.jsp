<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.globall.util.*"%>
<%@page import="java.util.List"%>
<%@page import="globaz.pyxis.ext.FGIPerfgiVO"%>
<%
	idEcran ="GTI5003";
	globaz.pyxis.ext.FGIMembre viewBean = (globaz.pyxis.ext.FGIMembre) session.getAttribute("viewBean");
	userActionValue = "pyxis.ext.fgi.genererMembre";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	
%>
<SCRIPT language="JavaScript">
top.document.title = "FGI Membre"

function validate() {
	var nomPrenom = document.getElementsByName("fgiNomPrenom")[0].value

	if (
		(nomPrenom.length >20) 
	) {
		return window.confirm("Avez-vous ajusté les zones tronquées ?");
	} else {
		return true;
	}
}


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

</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function init()
{
	

}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>FGI Membre<%-- /tpl:put  --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 
		      <tr>
				<td>
				
				
					<b>Coordonnées</b>
		<fieldset>
			<legend>DB</legend>
			<table>
				<tr>
					<td>Nom</td>
					<td><input type="text" readonly class="libelleLongDisabled" name="dbNom" value="<%=viewBean.getDbNom()%>"></td>
				</tr>
				<tr>
					<td>Prénom</td>
					<td><input type="text" readonly class="libelleLongDisabled" name="dbPrenom" value="<%=viewBean.getDbPrenom()%>">
					<input type="hidden" name="personnePhysique"  value="<%=(viewBean.getPersonnePhysique().booleanValue())?"on":"" %>"></td>
					<input type="hidden" name="type" value="<%=viewBean.getType()%>">
				</tr>
				
				<!--  
				<tr>
					<td>Nom d'alliance</td>
					<td><input type="text" readonly name="dbNomAlliance" value="<%=viewBean.getDbNomAlliance()%>"></td>
				</tr>
				-->
			</table>
		</fieldset>
		<fieldset>
			<legend>FGI</legend>
			<table>
				<tr>
					<td>NE</td>
					<td colspan="3">
						<input name="fgiNEcommune" type="text" class="disabled" readonly maxlength="3" size="2" value="<%=viewBean.getFgiNEcommune() %>">
						<input name="fgiNoAvsChef" class="libelleLongDisabled" readonly type="text" value = "<%=viewBean.getFgiNoAvsChef()%>">

						<input name="fgiNeAnnee" class="disabled" size="2" readonly type="text" value = "<%=viewBean.getFgiNeAnnee()%>">
						<input name="fgiNeMF" class="disabled" size="2" readonly type="text" value = "<%=viewBean.getFgiNeMF()%>">
						
					</td>
					</tr>
				<tr>
					<td>No AVS</td>
					<td colspan="2"><input name="fgiNoAvs" maxlength="15" type="text"  value="<%=viewBean.getFgiNoAvs()%>"></td>
					<td>&nbsp;</td>
					
					<td colspan="2">
						<fieldset>
							<legend>Genre traitement</legend>
							<input onclick="removeEq()" type="radio" <%=(("A".equals(viewBean.getFgiGenreTraitement()))?"checked":"")%> name="fgiGenreTraitement" value="A">Ajout
							<input onclick="addEq()" type="radio" <%=(("C".equals(viewBean.getFgiGenreTraitement()))?"checked":"")%> name="fgiGenreTraitement" value="C">Correction
						</fieldset>

					</td>
					
				</tr>
				<% 
				
				List list = viewBean.getOldNE();
				
				if(viewBean.getOldNE().size()==1) {
					FGIPerfgiVO vo = (FGIPerfgiVO) list.get(0);
				 %>
					<script>
					document.getElementsByName('fgiNEcommune')[0].value='<%=vo.getNumeroCommune()%>';
					document.getElementsByName('fgiNoAvsChef')[0].value='<%=globaz.pyxis.ext.FGIAbstractHelper.formatNumAvs11(vo.getNumAvs11())%>';
					</script>
				<% } else if(viewBean.getOldNE().size()>1) { %>
					<tr><td colspan='5'><div style='background-color:white'><table border='1' cellpadding='0' cellspacing='0'>
					<tr><th>&nbsp;</th><th>NIP</th><th>N° com.</th><th>NE</th><th>Section</th><th>MF</th><th>Type</th><th>Date</th><th>Visa</th></tr>
					<%
						for (java.util.Iterator it = list.iterator();it.hasNext();){
							FGIPerfgiVO vo = (FGIPerfgiVO) it.next();
					%>
							<tr>
								<td><input type='radio' name='selNe' onclick="document.getElementsByName('fgiNEcommune')[0].value='<%=vo.getNumeroCommune()%>';document.getElementsByName('fgiNoAvsChef')[0].value='<%=globaz.pyxis.ext.FGIAbstractHelper.formatNumAvs11(vo.getNumAvs11())%>';"></td>
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
					<td colspan="5"><input name="fgiNip" type="text" readonly class='disabled' value="<%=viewBean.getFgiNip() %>"></td>
				</tr>
				<tr>
					<td>Date naissance</td>
					<td colspan="5"><input name="fgiDateNaissance" maxlength="10" size="10" type="text" readonly class='disabled' value="<%=viewBean.getFgiDateNaissance()%>"></td>
				</tr>
				<tr>
					<td>Code sexe/société</td>
					<td colspan="5"><input name="fgiCodeSexeSociete" readonly class='disabled' type="text" size="1" maxlength="1" value="<%=viewBean.getFgiCodeSexeSociete() %>"></td>
				</tr>
				<tr>
					<td>Nom et prénom</td>
					<td colspan="5"><input name="fgiNomPrenom" type="text"  value="<%=viewBean.getFgiNomPrenom()%>"></td>
				</tr>
				<tr>
					<td>Etat civil</td>
					<td>
							<input name="fgiEtatCivil" readonly class="disabled" type="text" maxlength ="2" size="1" value="<%=viewBean.getFgiEtatCivil()%>"> 
					</td>
					
					<td>Profession (1)</td>
					<td><input name="fgiProfession1" type="text" maxlength="1" size="1"  value="<%=viewBean.getFgiProfession1() %>"></td>
					<td>Début étude (1)</td>
					<td><input name="fgiDebutEtude1" <%=(((viewBean.isSocieteOrMembre())||(globaz.pyxis.ext.FGIGP82Helper.TYPE_CONJOINT.equals(viewBean.getType())))?"readonly class='disabled'":"")%> type="text" maxlength="7" size="6" value="<%=viewBean.getFgiDebutEtude1() %>"></td>
				</tr>
				<tr>
					<td>Nationalité</td>
					<td><input name="fgiNationalite" readonly class="disabled" type="text" maxlength="3" size="2" value="<%=viewBean.getFgiNationalite() %>"></td>
					<td>Code domicile</td>
					<td><input name="fgiCodeDomicile" type="text" maxlength="1" size="1" value="<%=viewBean.getFgiCodeDomicile() %>"></td>
					<td>Fin étude (1)</td>
					<td><input name="fgiFinEtude1" type="text" <%=(((viewBean.isSocieteOrMembre())||(globaz.pyxis.ext.FGIGP82Helper.TYPE_CONJOINT.equals(viewBean.getType())))?"readonly class='disabled'":"")%> maxlength="7" size="6"  value="<%=viewBean.getFgiFinEtude1() %>"></td>
				</tr>
				<tr>
					<td>Code tuteur</td>
					<td><input name="fgiCodeTuteur" type="text" maxlength="1" <%=((viewBean.isSocieteOrMembre())?"readonly class='disabled'":"")%> size="1" value="<%=viewBean.getFgiCodeTuteur() %>"></td>
					<td>Statut juridique</td>
					<td><input name="statutJuridique" type="text" <%=(((viewBean.isSocieteOrMembre())||(globaz.pyxis.ext.FGIGP82Helper.TYPE_CONJOINT.equals(viewBean.getType())))?"readonly class='disabled'":"")%> maxlength="1" size="1" value="<%=viewBean.getStatutJuridique() %>"></td>
					<td>Profession (2)</td>
					<td><input name="fgiProfession2" type="text" <%=(((viewBean.isSocieteOrMembre())||(globaz.pyxis.ext.FGIGP82Helper.TYPE_CONJOINT.equals(viewBean.getType())))?"readonly class='disabled'":"")%> maxlength="1" size="1" value="<%=viewBean.getFgiProfession2() %>"></td>
				</tr>
				<tr>

					
					
					<!--
					<td>Date sortie groupe</td>
					<td><input name="fgiDateSortieGroupe"  maxlength="10" size="10"  type="text" value="<%=viewBean.getFgiDateSortieGroupe() %>"></td>
					<td>Agent philos</td>
					<td><input name="fgiAgentPhilos" type="text" <%=((viewBean.isSocieteOrMembre())?"readonly class='disabled'":"")%> maxlength="3" size="1" value="<%=viewBean.getFgiAgentPhilos() %>"></td>
					-->
					<td>Code réfugié</td>
					<td><input name="fgiCodeRefugie" type="text" maxlength="1" <%=((viewBean.isSocieteOrMembre())?"readonly class='disabled'":"")%> size="1" value="<%=viewBean.getFgiCodeRefugie() %>"></td>

					<td>Droit AF %</td>
					<td><input name="fgiDroitAF" type="text" <%=(((viewBean.isSocieteOrMembre())||(globaz.pyxis.ext.FGIGP82Helper.TYPE_CONJOINT.equals(viewBean.getType())))?"readonly class='disabled'":"")%> maxlength="3" size="1" value="<%=viewBean.getFgiDroitAF() %>"></td>

					<td>Début étude (2)</td>
					<td><input name="fgiDebutEtude2"  maxlength="7" size="6"  type="text" <%=(((viewBean.isSocieteOrMembre())||(globaz.pyxis.ext.FGIGP82Helper.TYPE_CONJOINT.equals(viewBean.getType())))?"readonly class='disabled'":"")%> value="<%=viewBean.getFgiDebutEtude2() %>"></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td>Fin étude (2)</td>
					<td><input name="fgiFinEtude2"  maxlength="7" size="6"  type="text" <%=(((viewBean.isSocieteOrMembre())||(globaz.pyxis.ext.FGIGP82Helper.TYPE_CONJOINT.equals(viewBean.getType())))?"readonly class='disabled'":"")%> value="<%=viewBean.getFgiFinEtude2() %>"></td>
					
				</tr>
			</table>
		</fieldset>
		
		<table width="100%"><tr>
		<td>
		Texte libre <input name="texteLibre" maxlength="67" size="70" type="text" value="<%=viewBean.getTexteLibre() %>">
		</td>
		<td>
			CICS :
			<input type="radio" name="cics" checked value="test">Test
			<input type="radio" name="cics" value="prod">Prod
			<input type="hidden" name="societeOrMembre" value="<%=viewBean.getSocieteOrMembre()%>">
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