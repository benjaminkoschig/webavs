<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="globaz.pavo.db.bta.CIRequerantBtaViewBean"%>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%
	CIRequerantBtaViewBean viewBean = (CIRequerantBtaViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdRequerant();
	userActionValue = "pavo.bta.requerantBta.modifier";
	idEcran = "CCI0039";
	//r�cup�ration de l'id du dossier
	if(request.getParameter("idDossierBta")!=null){
		viewBean.setIdDossierBta(request.getParameter("idDossierBta"));
	}
	//out.print("id du dossier :"+viewBean.getIdDossierBta());
%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	
	String fieldDisable = "class='disabled' readonly tabindex='-1'";
	//bButtonValidate = false;
	//bButtonUpdate = false;
	//bButtonCancel = false;
	
%>


<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<script>
	
	function del() {
	    if (window.confirm("Sie sind dabei, das ausgew�hlte Objekt zu l�schen! Wollen Sie fortfahren?")){
	        document.forms[0].elements('userAction').value="pavo.bta.requerantBta.supprimer";
	        document.forms[0].submit();
	    }
	}
	function init(){
		displayAutreInfo(document.getElementById('typeRequerant').value);
		//displayDateNaissanceCadet();
		masqueAnnee(<%if(viewBean.getDateDebut().equals(viewBean.getDateFin()) && !JadeStringUtil.isEmpty(viewBean.getDateDebut())){ %>'refused' <%}%>);
	}

	function masqueAnnee(value){
		if(value=="refused"){
			document.getElementById('anneeDebut').style.visibility="hidden";
			document.getElementById('anneeDebutTexte').style.visibility="hidden";
			document.getElementById('anneeFin').style.visibility="hidden";
			document.getElementById('anneeFinTexte').style.visibility="hidden";
		}
		else{
			document.getElementById('anneeDebut').style.visibility="visible";
			document.getElementById('anneeDebutTexte').style.visibility="visible";
			document.getElementById('anneeFin').style.visibility="visible";
			document.getElementById('anneeFinTexte').style.visibility="visible";
		}
	}
	
	function postInit(){
		document.forms[0].elements('dateNaissanceCadetTexte').disabled = false;
		document.forms[0].elements('dateNaissanceCadetTexte').readOnly = true;
		document.forms[0].elements('isMenageCommunTexte').disabled = false;
		document.forms[0].elements('isMenageCommunTexte').readOnly = true;
		document.forms[0].elements('hasEnfantTexte').disabled = false;
		document.forms[0].elements('hasEnfantTexte').readOnly = true;
		document.forms[0].elements('lienParenteTexte').disabled = false;
		document.forms[0].elements('lienParenteTexte').readOnly = true;
		document.forms[0].elements('idConjointRequerantTexte').disabled = false;
		document.forms[0].elements('idConjointRequerantTexte').readOnly = true;
		document.forms[0].elements('anneeDebutTexte').disabled = false;
		document.forms[0].elements('anneeDebutTexte').readOnly = true;
		document.forms[0].elements('anneeFinTexte').disabled = false;
		document.forms[0].elements('anneeFinTexte').readOnly = true;
	}
	function displayDateNaissanceCadet(){
		if(document.getElementById('hasEnfant').checked){
			document.getElementById('dateNaissanceCadet').style.visibility="visible";
			document.getElementById('dateNaissanceCadetTexte').style.visibility="visible";
			document.getElementById('anchor_dateNaissanceCadet').style.visibility="visible";
		}
		else{
			document.getElementById('dateNaissanceCadet').style.visibility="hidden";
			document.getElementById('dateNaissanceCadetTexte').style.visibility="hidden";
			document.getElementById('anchor_dateNaissanceCadet').style.visibility="hidden";
		}
	}
	function displayAutreInfo(data){
		//si type de requerant = conjoint -> on masque les champs d'informations et affiche le select du conjoint
		if(data=='334001'){
			//valeur � chaine vide
			document.getElementById('isMenageCommun').checked=false;
			document.getElementById('lienParente').value="";	
			document.getElementById('dateNaissanceCadet').value="";
			document.getElementById('hasEnfant').checked=false;	
			
			document.getElementById('idConjointRequerant').style.visibility="visible";
			document.getElementById('isMenageCommun').style.visibility="hidden";
			document.getElementById('lienParente').style.visibility="hidden";	

			//displayDateNaissanceCadet();
			document.getElementById('dateNaissanceCadet').style.visibility="hidden";
			document.getElementById('dateNaissanceCadetTexte').style.visibility="hidden";
			document.getElementById('anchor_dateNaissanceCadet').style.visibility="hidden";
			
			document.getElementById('hasEnfant').style.visibility="hidden";
			document.getElementById('idConjointRequerantTexte').style.visibility="visible";
			document.getElementById('isMenageCommunTexte').style.visibility="hidden";
			document.getElementById('lienParenteTexte').style.visibility="hidden";
			document.getElementById('hasEnfantTexte').style.visibility="hidden";
		}
		else{
			//valeur � la valeur de viewBean
			document.getElementById('isMenageCommun').checked=<%=viewBean.getIsMenageCommun().booleanValue()%>;
			document.getElementById('lienParente').value="<%=viewBean.getLienParente()%>";	
			document.getElementById('dateNaissanceCadet').value="<%=viewBean.getDateNaissanceCadet()%>";
			document.getElementById('hasEnfant').checked=<%=viewBean.getHasEnfant().booleanValue()%>;
			document.getElementById('idConjointRequerant').value="";

			document.getElementById('idConjointRequerant').style.visibility="hidden";
			document.getElementById('isMenageCommun').style.visibility="visible";
			document.getElementById('lienParente').style.visibility="visible";	

			//displayDateNaissanceCadet();
			document.getElementById('dateNaissanceCadet').style.visibility="visible";
			document.getElementById('dateNaissanceCadetTexte').style.visibility="visible";
			document.getElementById('anchor_dateNaissanceCadet').style.visibility="visible";
			
			document.getElementById('hasEnfant').style.visibility="visible";
			document.getElementById('idConjointRequerantTexte').style.visibility="hidden";
			document.getElementById('isMenageCommunTexte').style.visibility="visible";
			document.getElementById('lienParenteTexte').style.visibility="visible";
			document.getElementById('hasEnfantTexte').style.visibility="visible";
		}
	}
	
	function upd() {
	}
	
	function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pavo.bta.requerantBta.ajouter";
    else
        document.forms[0].elements('userAction').value="pavo.bta.requerantBta.modifier";
    
    return state;

	}
	function cancel() {
	if (document.forms[0].elements('_method').value == "add")
  		document.forms[0].elements('userAction').value="back";
 	else
  		document.forms[0].elements('userAction').value="pavo.bta.requerantBta.afficher";
	}

	function add() {
	}
</script>

<ct:menuChange displayId="options" menuId="dossierBta-detail" showTab="options">
	<ct:menuSetAllParams key="idDossierBta" value="<%=viewBean.getIdDossierBta()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDossierBta()%>"/>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail eines Antragsstellers<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						

			<%
			Object[] tiersMethodsName = new Object[]
			{
				new String[]{"setIdTiersRequerant","getIdTiers"}
			};
			Object[] tiersParams = new Object[]{
						new String[]{"selection","_pos"}
					};
			%>
			<!--
			-->
			<tr>
				<td>
					<font size="2"><b>Detail des Antragsstellers</b></font>
				</td>
			</tr>	
			<tr>
				<td>
					Auswahl des Partners
				</td>
				<td>
            		<ct:FWSelectorTag 
					name="tiersSelector" 		
					methods="<%=tiersMethodsName%>"
					providerPrefix="TI"			
					providerApplication ="pyxis"			
					providerAction ="pyxis.tiers.tiers.chercher"			
					providerActionParams ="<%=tiersParams%>"
					target="fr_main"
					/>
					<%if(!JadeStringUtil.isBlank(viewBean.getIdTiersRequerant())){ %>	
					 <ct:ifhasright element="pyxis.tiers.tiers.afficher" crud="r">
						<a href="<%=request.getContextPath()%>/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%= viewBean.getIdTiersRequerant() %>">Partner</a>
					</ct:ifhasright>
					<%} %>
					<%if(!JadeStringUtil.isBlank(viewBean.getIdTiersRequerant()) && !JadeStringUtil.isBlank(viewBean.getCompteIndividuel())){ %>
					<ct:ifhasright element="pavo.compte.ecriture.chercherEcriture" crud="r">	
						| <a href="<%=request.getContextPath()%>/pavo?userAction=pavo.compte.ecriture.chercherEcriture&compteIndividuelId=<%= viewBean.getCompteIndividuel() %>">IK des Partners</a>
					</ct:ifhasright>
					<%}%>
				</td>
			</tr>
						<tr>
							<td>
								SVN
							</td>
							<td>
								<input type="text" size="17" value='<%=(viewBean.getTiersViewBeanForDisplay()!=null)?viewBean.getTiersViewBeanForDisplay().getNumAvsActuel():""%>' <%=fieldDisable%> >
							</td>
						</tr>
						<tr>
							<td>
								Name und Vorname
							</td>
							<td>
								<input type="text" size="70" value='<%=(viewBean.getTiersViewBeanForDisplay()!=null)?viewBean.getTiersViewBeanForDisplay().getDesignation1()+" "+viewBean.getTiersViewBeanForDisplay().getDesignation2():""%>' <%=fieldDisable%> >
							</td>
						</tr>
						<tr>
							<td>
								Geburtsdatum
							</td>
							<td>
								<input type="text" size="10" value='<%=(viewBean.getTiersViewBeanForDisplay()!=null)?viewBean.getTiersViewBeanForDisplay().getDateNaissance():""%>' <%=fieldDisable%> >
							</td>
						</tr>
						<tr>
							<td>
								Adresse
							</td>
							<td>
								<TEXTAREA id="adresseTiers" cols="35" class="libelleLongDisabled" tabindex="-1" readonly="readonly" rows="5"><%=(viewBean.getTiersViewBeanForDisplay()!=null)?viewBean.getTiersViewBeanForDisplay().getAdresseAsString():""%></TEXTAREA>
							</td>
						</tr>
						<tr>
                            <td nowrap colspan="7" height="11">
                               <hr size="2">
                            </td>
                        </tr>


						<tr>
						<td>
							<font size="2"><b>Voraussetzungen</b></font>
						</td>
						</tr>
						<tr>
							<td>Antragsstellertyp</td>
							<td>
								<ct:FWCodeSelectTag name="typeRequerant" defaut='<%=(!JadeStringUtil.isEmpty(viewBean.getTypeRequerant()))?viewBean.getTypeRequerant():""%>' codeType="CITYPEREQ" wantBlank="true"/>
							</td>
							 <SCRIPT>
            					document.getElementById("typeRequerant").onchange=new Function ("","return displayAutreInfo(this.value);");
		            		</SCRIPT>
						</tr>
						<tr>
							<td>
								<input class="text" type="text" name="idConjointRequerantTexte" tabIndex="-1" size="37" value="Ehepartner" style="border-width : 0px; background-color: transparent;"> 
							</td>
							<td>
								<ct:FWListSelectTag name="idConjointRequerant" defaut='<%=(!JadeStringUtil.isBlank(viewBean.getIdConjointRequerant()))?viewBean.getIdConjointRequerant():""%>' data="<%=viewBean.getListRequerantDossier()%>"/>
							</td>
						</tr>
						<tr>
							<td>
								<input class="text" type="text" name="isMenageCommunTexte" size="58" tabIndex="-1" value="Leichte Erreichbarkeit der betreuenden Person" style="border-width : 0px; background-color: transparent;">
							</td>
							<td>
								<input name="isMenageCommun" size="20" type="checkbox" <%=(viewBean.getIsMenageCommun().booleanValue())?"checked":"unchecked"%> style="text-align : right;">
							</td>
						<tr>
							<td>
								<input class="text" type="text" name="lienParenteTexte" tabIndex="-1" size="58" value="Verwandtschaftsgrad mit der Person, die die Pflege ben�tigt" style="border-width : 0px; background-color: transparent;">
							</td>
							<td>
								<ct:FWCodeSelectTag name="lienParente" defaut='<%=(!JadeStringUtil.isEmpty(viewBean.getLienParente()))?viewBean.getLienParente():""%>' codeType="CIPARENTE" wantBlank="true"/>
							</td>
						</tr>
						<tr>
							<td>
								<input class="text" type="text" name="hasEnfantTexte" tabIndex="-1" size="35" value="Hat Kindern unter 16 Jahren" style="border-width : 0px; background-color: transparent;">
							</td>
							<td>
								<input name="hasEnfant" size="20" type="checkbox" <%=(viewBean.getHasEnfant().booleanValue())?"checked":"unchecked"%> style="text-align : right;">
							</td>
						</tr>
							<td>
								<input class="text" type="text" name="dateNaissanceCadetTexte" tabIndex="-1" size="25" value="Geburtsdatum des J�ngstens" style="border-width : 0px; background-color: transparent;">
							</td>
							<td>
								<ct:FWCalendarTag name="dateNaissanceCadet" value="<%=viewBean.getDateNaissanceCadet()%>"/>
							</td>
						<tr>
						
						<tr>
                            <td nowrap colspan="7" height="11">
                               <hr size="2">
                            </td>
                        </tr>


						<tr>
						<td>
							<font size="2"><b>Zulassungsentscheid</b></font>
						</td>
						<tr>
							<td>
								Status des Antrags 
							</td>
							<td>
								<%if(!viewBean.getDateDebut().equals(viewBean.getDateFin()) && !JadeStringUtil.isEmpty(viewBean.getDateDebut())){ %><font color='green'><b>Angenommen</b></font> <%}else{ %><font color='black'>Angenommen</font><%} %> <input name="demandeRequerant" type="radio" onClick="masqueAnnee('accepted')" value="accepted" <%if(!viewBean.getDateDebut().equals(viewBean.getDateFin()) && !JadeStringUtil.isEmpty(viewBean.getDateDebut())){ %>checked <%} %>>
								<%if(viewBean.getDateDebut().equals(viewBean.getDateFin()) && !JadeStringUtil.isEmpty(viewBean.getDateDebut())){ %><font color='red'><b>Abgelehnt</b></font> <%}else{ %><font color='black'>Abgelehnt</font><%} %> <input name="demandeRequerant" type="radio" onClick="masqueAnnee('refused')" value="refused" <%if(viewBean.getDateDebut().equals(viewBean.getDateFin()) && !JadeStringUtil.isEmpty(viewBean.getDateDebut())){ %>checked <%} %>>  
							</td>
						</tr>
						<tr>
							<td>
								<input class="text" type="text" name="anneeDebutTexte" tabIndex="-1" size="35" value="Beginnjahr der IK-Buchungen" style="border-width : 0px; background-color: transparent;">
							</td>
							<td>
								<!--<ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>"/>-->
								<input name="anneeDebut" onClick="document.forms[0].demandeRequerant[0].checked=true;" type="text" size=3 maxlength="4" value='<%=viewBean.getAnneeDebut()%>'/>
							</td>
						</tr>
						<tr>
							<td>
								<input class="text" type="text" name="anneeFinTexte" tabIndex="-1" size="35" value="Enddatum der IK-Buchungen" style="border-width : 0px; background-color: transparent;">
							</td>
							<td>
								<!--<ct:FWCalendarTag name="dateFin" value="<%=viewBean.getDateFin()%>"/>-->
								<input name="anneeFin" onClick="document.forms[0].demandeRequerant[0].checked=true;" type="text" size=3 maxlength="4" value='<%=viewBean.getAnneeFin()%>'/>
							</td>
						</tr>
					
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>