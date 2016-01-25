<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CFA0006";%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
 	//contrôle des droits
	bButtonNew = objSession.hasRight(userActionNew, "ADD");
	
	globaz.musca.db.facturation.FAAfactViewBean viewBean = (globaz.musca.db.facturation.FAAfactViewBean)session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getIdAfact();
	userActionValue = "musca.facturation.afact.modifier";

	//Vérifie si les passage est verrouillé ou comptabilisé. si oui, n'affiche pas les boutons
	String passageStatus = globaz.musca.util.FAUtil.getPassageStatus(viewBean.getIdPassage(),session);
	boolean passageLocked =globaz.musca.util.FAUtil.getPassageLock(viewBean.getIdPassage(),session).booleanValue();

	if ( globaz.musca.db.facturation.FAPassage.CS_ETAT_COMPTABILISE.equalsIgnoreCase(passageStatus)
		|| passageLocked
		|| globaz.musca.db.facturation.FAPassage.CS_ETAT_ANNULE.equalsIgnoreCase(passageStatus)
		|| globaz.musca.db.facturation.FAPassage.CS_ETAT_VALIDE.equalsIgnoreCase(passageStatus)
		|| viewBean.hasInteretSoumis()){
		bButtonValidate = false;
		bButtonDelete = false;
		bButtonUpdate = false;
		bButtonNew = false;
	}
	String jspLocationTiersSelect = servletContext + mainServletPath + "Root/tiers_select.jsp";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers -->
<%
	String idExterneRolePopup = request.getParameter("idExterneRolePopup");
	int autoDigiAff = globaz.musca.util.FAUtil.fetchAutoDigitAff(session);
%>
top.document.title = "Fakturierung - Faktzeile Detail"


function add() {
    document.forms[0].elements('userAction').value="musca.facturation.afact.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="musca.facturation.afact.ajouter";
    else
        document.forms[0].elements('userAction').value="musca.facturation.afact.modifier";
    return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
    document.forms[0].elements('userAction').value="back";
 else
    document.forms[0].elements('userAction').value="musca.facturation.afact.afficher"
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="musca.facturation.afact.supprimer";
        document.forms[0].submit();
    }
}


function showTable(tableToShow) {
	document.all(tableToShow).style.display='block';
}

function hideTable(tableToHide) {
	document.all(tableToHide).style.display='none';
}

function afficheTable() {
	if (document.forms[0].elements('idTypeAfact').value == "904001") {
		showTable("tStandard");
		hideTable("tCompensation");
		hideTable("tTableau");
		showTable("tPeriode");
		showTable("tMontant");
	}
	if (document.forms[0].elements('idTypeAfact').value == "904002") {
		hideTable("tStandard");
		showTable("tCompensation");
		hideTable("tTableau");
		hideTable("tPeriode");
		showTable("tMontant");
		document.getElementById('idExterneRubrique').value="<%=viewBean.getIdExterneRubriqueCompen()%>";
		document.getElementById('descriptionRubrique').value="<%=viewBean.getLibelleRubriqueCompen()%>";
	}
	if (document.forms[0].elements('idTypeAfact').value == "904003") {
		hideTable("tStandard");
		hideTable("tCompensation");
		showTable("tTableau");
		showTable("tPeriode");
		hideTable("tMontant");
	}
	if (document.forms[0].elements('idTypeAfact').value == "904004") {
		hideTable("tStandard");
		showTable("tCompensation");
		hideTable("tTableau");
		hideTable("tPeriode");
		showTable("tMontant");
		document.getElementById('idExterneRubrique').value="<%=viewBean.getIdExterneRubriqueCompen()%>";
		document.getElementById('descriptionRubrique').value="<%=viewBean.getLibelleRubriqueCompen()%>";
	}

}

function updateLibelle(tag){
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("descriptionRubrique").value = element.libelle;
	}
}

function init(){
	var idTypeAfact = "<%=(String)request.getParameter("idTypeAfact")%>";
	if (idTypeAfact != "null"){
		document.forms[0].elements('idTypeAfact').value = idTypeAfact;
		afficheTable();
	}
}
function updateIdExterneRolePopup(tag){
	if(tag.select && tag.select.selectedIndex != -1){
		document.getElementById('idTiersDebiteurCompensation').value=tag.select[tag.select.selectedIndex].idTiers;
		document.getElementById('idExterneDebiteurCompensationPopup').value=tag.select[tag.select.selectedIndex].numAffilieActuel;
		document.getElementById('nomDebiteurCompensation').value=tag.select[tag.select.selectedIndex].nom;
	}
}
function updatePopuptext(data){
	if(data==517002){
		document.getElementById('idExterneDebiteurCompensationPopup').style.visibility = 'visible';
		document.getElementById('idExterneDebiteurCompensationInput').style.visibility='hidden';
		document.getElementById('idExterneDebiteurCompensationPopup').style.display='inline';
		document.getElementById('idExterneDebiteurCompensationInput').style.display='none';
	}else{
		document.getElementById('idExterneDebiteurCompensationInput').style.visibility='visible';
		document.getElementById('idExterneDebiteurCompensationPopup').style.visibility = 'hidden';
		document.getElementById('idExterneDebiteurCompensationPopup').style.display='none';
		document.getElementById('idExterneDebiteurCompensationInput').style.display='inline';
	}
	
}
function updateIdExterneRole(data){
	document.getElementById('idExterneDebiteurCompensation').value = data;
}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail einer Faktzeile<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
	<TR><TD>
              <TABLE border="0">
                <TBODY>
				<TR> 
					<TD nowrap width="145"><A href="<%=request.getContextPath()%>\musca?userAction=musca.facturation.passage.afficher&selectedId=<%=viewBean.getIdPassage()%>" TITLE="Diesen Job editieren">Job</A></TD>
					<TD nowrap><INPUT name="idPassage" type="text" value="<%=viewBean.getIdPassage()%>" class="numeroCourtDisabled" readonly style="width : 4.0cm" tabindex="-1">
					</TD>
					<td align="right" rowspan="4" width="370">
						<TABLE border="1" cellpadding="5" >
							<TR>
								<TH align="center">Hilfsmenü</TH>
							</TR>
							<TR >
			              		<TD bordercolor="navy" bgcolor="#BBCCEE">
								<A href="<%=request.getContextPath()%>\musca?userAction=musca.facturation.enteteFacture.chercher&idPassage=<%=viewBean.getIdPassage()%>" tabindex="-1">Zurück zur Abrechnungsliste</A><br>
								<A href="<%=request.getContextPath()%>\musca?userAction=musca.facturation.afact.chercher&idEnteteFacture=<%=viewBean.getIdEnteteFacture()%>" tabindex="-1">Zurück zu der Faktzeilenliste</A><br>
								<ct:ifhasright element="musca.facturation.passageFacturation.aQuittancer" crud="c">
									<A href="<%=request.getContextPath()%>\musca?userAction=musca.facturation.passageFacturation.aQuittancer&selectedId=<%=viewBean.getIdPassage()%>" tabindex="-1">Zurück zu der pendenten Faktzeilenliste</A><br><br>
								</ct:ifhasright>
								<%	//teste le passage avant de proposer un nouveau décompte
									if( (!globaz.musca.db.facturation.FAPassage.CS_ETAT_COMPTABILISE.equalsIgnoreCase(passageStatus))
										&& (!passageLocked)
										&& (!globaz.musca.db.facturation.FAPassage.CS_ETAT_ANNULE.equalsIgnoreCase(passageStatus)) ){
											%>
											<ct:ifhasright element="musca.facturation.enteteFacture.afficher" crud="c">
											<A href="<%=request.getContextPath()%>\musca?userAction=musca.facturation.enteteFacture.afficher&_method=add&idPassage=<%=viewBean.getIdPassage()%>" tabindex="-1"><b>Erstellung einer neuen Abrechnung</b></A><br>
											</ct:ifhasright>
											<%
									}%>
								</TD>
							</TR>
						</TABLE>
					</td>
				</TR>
                <TR> 
					<TD nowrap width="145">Debitor</TD>
					<TD nowrap> 
						<INPUT name="descriptionTiers" type="text" value="<%=viewBean.getDescriptionTiers()%>" class="numeroCourtDisabled" readonly style="width : 14.0cm" tabindex="-1">
					</TD>
				</TR>
				<TR>
					<TD nowrap width="145"><A href="<%=request.getContextPath()%>\musca?userAction=musca.facturation.enteteFacture.afficher&selectedId=<%=viewBean.getIdEnteteFacture()%>" TITLE="Editer ce décompte">Abrechnung</A></TD>
					<TD nowrap>
						<INPUT name="descriptionDecompte" type="text" value="<%=viewBean.getDescriptionDecompte()%>" class="libelleLongDisabled" readonly style="width : 14.0cm" tabindex="-1">
					</TD>
                </TR>
                <TR>
                	<TD nowrap width="145">Kassen-Nr.</TD>
					<TD nowrap> 
						<INPUT name="idNumCaisse" type="text" value="<%=viewBean.getCodeNumCaisse()%>" class="numeroLong" style="width : 4.0cm">
					</TD>
                </TR>
                </TBODY> 
              </TABLE>
              <br><br>
      <!-- Page 1 -->
		<TABLE cellspacing="0" cellpadding="0" border="0">
        <TBODY>       
	        <TR> 
				<TD nowrap width="145">Faktzeiletyp</TD>
				<TD nowrap><ct:FWSystemCodeSelectTag name="idTypeAfact"
           			defaut="<%=viewBean.getIdTypeAfact()%>"
					codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsTypeAfactWithoutBlank(session)%>"/> 
					<SCRIPT> document.getElementById("idTypeAfact").onchange= afficheTable;	</SCRIPT>
				</TD>
			</TR>
			<TR>
				<TD height="10">&nbsp;</TD>
			</TR>
     		<TR>
            	<TD nowrap width="145">Rubrik</TD>
	            <TD nowrap>
					<%String jspLocation = servletContext + "/muscaRoot/" + languePage + "/facturation/rubrique_select.jsp";%>
					<INPUT type="hidden" name="idEnteteFacture" value='<%=viewBean.getIdEnteteFacture()%>'>
					<INPUT type="hidden" name="quittancer" value='<%=request.getParameter("quittancer")%>'>
					<input type="hidden" name="idRubrique" value="<%=viewBean.getIdRubrique()%>">
					<ct:FWPopupList name="idExterneRubrique" 
									onFailure="document.mainForm.idRubrique.value='';" 
									onChange="if (tag.select) document.mainForm.idRubrique.value = tag.select[tag.select.selectedIndex].idRubrique;updateLibelle(tag);" 
									validateOnChange="true" 
									value="<%=viewBean.getIdExterneRubrique()%>" 
									className="libelle" jspName="<%=jspLocation%>" 
									minNbrDigit="1" 
									forceSelection="true"/>&nbsp;&nbsp;
					<INPUT name="descriptionRubrique" type="text" value="<%=viewBean.getLibelleRubrique()%>" class="libelleLongDisabled" readonly style="width : 12.15cm" tabindex="-1">
					<INPUT name="idExterneRubriqueCompen" type="hidden" value="<%=viewBean.getIdExterneRubriqueCompen()%>" readonly>
				</TD>
			</TR>
			<TR>
				<TD nowrap>Bezeichnung</TD>
				<TD><INPUT name="libelle" type="text" value="<%=viewBean.getLibelle()%>" size="87" maxlength="60"></TD>
			</TR>
		</TBODY>
		</TABLE>
            <!-- Montant -->
		<TABLE border="0" cellspacing="0" cellpadding="0" id="tMontant">
        <TBODY>
			<TR>
        	    <TD nowrap width="145">Betrag</TD>
            	<TD nowrap><INPUT onkeypress="return filterCharForFloat(window.event);" name="montantFacture" type="text" value="<%=viewBean.getMontantFacture()%>" class="montant">
           		<INPUT name="saveMontantFacture" type="hidden" value="<%=viewBean.getMontantFacture()%>">
            	</TD>
           </TR>
		</TBODY>
		</TABLE>
      <!-- Afact standard-->
      <TABLE border="0" cellspacing="0" cellpadding="0" width="701" id="tStandard">
        <TBODY>
	<TR>
            <TD nowrap width="145">Lohnsumme / Beitragsatz</TD>
            <TD nowrap><INPUT onkeypress="return filterCharForFloat(window.event);" name="masseFacture" type="text" value="<%=viewBean.getMasseFacture()%>" class="montant">
		&nbsp;<INPUT onkeypress="return filterCharForFloat(window.event);" name="tauxFacture" type="text" value="<%=viewBean.getTauxFacture()%>"  class="montant">
			<INPUT type="hidden" name="affichtaux" value='<%=viewBean.getAffichtaux()%>'>
		</TD>
		<TD></TD>
            <TD nowrap></TD>
            <TD nowrap align="center">&nbsp;</TD>
          </TR>
	</TBODY>
	   </TABLE>
      <!-- Afact de compensation -->
      <TABLE border="0" cellspacing="0" cellpadding="0" width="701" id="tCompensation">
        <TBODY>
	<TR>
            <TD nowrap width="144">Debitor zu verrechnen</TD>
            <TD nowrap>
            <ct:FWPopupList 
            		name="idExterneDebiteurCompensationPopup" 
            		value="<%=viewBean.getIdExterneDebiteurCompensation()%>" 
            		className="libelle" 
            		jspName="<%=jspLocationTiersSelect%>" 
            		autoNbrDigit="<%=autoDigiAff%>" 
            		size="19"
            		onChange="updateIdExterneRolePopup(tag);updateIdExterneRole(document.getElementById('idExterneDebiteurCompensationPopup').value);"
            		minNbrDigit="3"
            		/>
            	<SCRIPT>
            		document.getElementById('idExterneDebiteurCompensationPopup').style.visibility='visible';
            		document.getElementById('idExterneDebiteurCompensationPopup').style.display='inline';	
            		document.getElementById("idExterneDebiteurCompensationPopup").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
            	</SCRIPT>
                <INPUT name="idExterneDebiteurCompensationInput" type="text" value="<%=viewBean.getIdExterneDebiteurCompensation()%>" onChange="updateIdExterneRole(document.getElementById('idExterneDebiteurCompensationInput').value);"  maxlength="20" size="20">
 			 	<script>
					document.getElementById('idExterneDebiteurCompensationInput').style.visibility='hidden';
					document.getElementById('idExterneDebiteurCompensationInput').style.display='none';
				</script>
            	<INPUT name="idExterneDebiteurCompensation" type="hidden" value="<%=viewBean.getIdExterneDebiteurCompensation()%>" class="montant">
            </TD>
            <TD nowrap> 
              <select name="idRoleDebiteurCompensation">
              	<%=CARoleViewBean.createOptionsTags(objSession, viewBean.getIdRoleDebiteurCompensation(), false)%>
              </select>           
            </TD>
            <TD nowrap colspan="2">
            	<INPUT type="text" value="<%=viewBean.getNomDebiteurCompensation()%>" class="libelleLongDisabled" readonly name="nomDebiteurCompensation" style="width : 6.5cm;" tabindex="-1">
			
            <!--<TD nowrap align="center"&nbsp;><input type="button" onClick="_pos.value=codeAmbassade.value;_meth.value=_method.value;_act.value='pyxis.tiers.gestion.afficher';userAction.value='pyxis.tiers.administration.chercher';selGenre.value=<%=globaz.pyxis.db.tiers.TIAdministrationViewBean.CS_AMBASSADE%>;submit()" value="..."></TD> -->

	    	<%
	    	Object[] debiteurMethodsName= new Object[]{
				new String[]{"setIdExterneDebiteurCompensation","getNumAffilieActuel"},
				new String[]{"setIdTiersDebiteurCompensation","getIdTiers"},
			};
			Object[] debiteurParams = new Object[]{
				new String[]{"selection","_pos"},
			};
			String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/facturation/afact_de.jsp";	
			%>
			
			<ct:FWSelectorTag 
				name="tiersSelector" 
				
				methods="<%=debiteurMethodsName%>"
				providerApplication ="pyxis"
				providerPrefix="TI"
				providerAction ="pyxis.tiers.tiers.chercher"
				providerActionParams ="<%=debiteurParams%>"
				redirectUrl="<%=redirectUrl%>"
			/> 
			 </TD>
          </TR>
	<TR>
	 <TD nowrap><A href="<%=request.getContextPath()%>\osiris?userAction=osiris.comptes.apercuParSection.chercher&selectedId=<%=viewBean.getIdCompteAnnexe()%>" TITLE="Editer ce décompte">Abrechnung zu verrechnen</A></TD>
            <TD nowrap>
            	<INPUT name="idExterneFactureCompensation" type="text" value="<%=viewBean.getIdExterneFactureCompensation()%>" class="montant">
            	<INPUT type="hidden" name="checkIdExterneFactureCompensation" value='<%=viewBean.getCheckIdExterneFactureCompensation()%>'>
            </TD> 
			<TD colspan="2"> 
              <select name="idTypeFactureCompensation" >
                <%globaz.osiris.db.comptes.CATypeSection tempTypeSection;
				  globaz.osiris.db.comptes.CATypeSectionManager manTypeSection = new globaz.osiris.db.comptes.CATypeSectionManager();
				  manTypeSection.setSession(objSession);
				  manTypeSection.find();
				  for(int i = 0; i < manTypeSection.size(); i++){
				    	tempTypeSection = (globaz.osiris.db.comptes.CATypeSection)manTypeSection.getEntity(i);
						if(viewBean.getIdTypeFactureCompensation() == null) { %>
                <option value="<%=tempTypeSection.getIdTypeSection()%>"><%=tempTypeSection.getDescription()%></option>
                <%} else if  (viewBean.getIdTypeFactureCompensation().equalsIgnoreCase(tempTypeSection.getIdTypeSection())) { %>
                <option selected value="<%=tempTypeSection.getIdTypeSection()%>"><%=tempTypeSection.getDescription()%></option>
                <% } else { %>
                <option value="<%=tempTypeSection.getIdTypeSection()%>"><%=tempTypeSection.getDescription()%></option>
                <% } %>
                <% } %>
              </select>
			</TD>
            <TD nowrap>
            	<%
           			String decComp="";
					if(request.getParameter("_method")==null){
						globaz.musca.db.facturation.FAAfactManager afactM = new globaz.musca.db.facturation.FAAfactManager();
						afactM.setSession(viewBean.getSession());
						afactM.setForIdAfact(viewBean.getIdAfact());
						afactM.find();
						globaz.musca.db.facturation.FAAfact entity=(globaz.musca.db.facturation.FAAfact)afactM.getEntity(0);
            	
						globaz.musca.application.FAApplication app = (globaz.musca.application.FAApplication) viewBean.getSession().getApplication();
						globaz.osiris.api.APISectionDescriptor sd = app.getSectionDescriptor(viewBean.getSession());
						sd.setSection(entity.getIdExterneFactureCompensation(),entity.getIdTypeFactureCompensation(),"","","","");

						globaz.globall.db.BTransaction transaction = new globaz.globall.db.BTransaction(viewBean.getSession());
					
						if(globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(entity.getIdExterneFactureCompensation())){
							decComp=entity.getLibelleRubrique(transaction,languePage);
						}else{
							decComp=entity.getLibelleRubrique(transaction,languePage)
									+" - "
									+entity.getIdExterneFactureCompensation()
									+" "
									+ sd.getDescription(languePage);
						}
					}
				%>
            	<%--<input type="text" readonly class="libelleDisabled" style="width : 9.2cm" value="<%=decComp%>" >--%>
            	<INPUT name="libelleSurFacture" type="text" value="<%=viewBean.getLibelleSurFacture(viewBean.getSession().getIdLangueISO())%>" class="libelle">
            </TD>
          </TR>
          <TR> 
              <TD nowrap width="145">Valutadatum</TD>
              <TD nowrap> 
                <ct:FWCalendarTag name="dateValeur" value="<%=viewBean.getDateValeur()%>" />
              </TD>

          </TR> 
	</TBODY>
	   </TABLE>
      <!-- Afact de tableau -->
      <TABLE border="0" cellspacing="0" cellpadding="0" width="825" id="tTableau">
           <TBODY>
          <TR>
            <TH width="144">Element</TH>
            <TH align="center" width="200">Basis</TH>
            <TH align="center" width="200">Bereits fakturiert</TH>
            <TH align="center" width="200">Zu fakturieren</TH>
          </TR>
	   	  <TR>
            <TD nowrap width="144">Lohnsumme</TD>
            <TD nowrap align="center" width="200"><INPUT onkeypress="return filterCharForFloat(window.event);" name="masseInitiale" type="text" value="<%=viewBean.getMasseInitiale()%>" class="montant"></TD>
            <TD nowrap align="center" width="200"><INPUT onkeypress="return filterCharForFloat(window.event);" name="masseDejaFacturee" type="text" value="<%=viewBean.getMasseDejaFacturee()%>" class="montant"></TD>
            <TD nowrap align="center" width="200"><INPUT name="masseFacture" type="text" value="<%=viewBean.getMasseFacture()%>" class="montantDisabled" readonly tabindex="-1"></TD>
          </TR>
          <TR>
            <TD nowrap width="144">Beitragsatz</TD>
            <TD nowrap align="center" width="200"><INPUT onkeypress="return filterCharForFloat(window.event);" name="tauxInitial" type="text" value="<%=viewBean.getTauxInitial()%>" class="montant"></TD>
            <TD nowrap align="center" width="200"><INPUT onkeypress="return filterCharForFloat(window.event);" name="tauxDejaFacture" type="text" value="<%=viewBean.getTauxDejaFacture()%>" class="montant"></TD>
            <TD nowrap align="center" width="200"><INPUT name="tauxFacture" type="text" value="<%=viewBean.getTauxFacture()%>" class="montantDisabled" readonly tabindex="-1"></TD>
          </TR>
	 	  <TR>
            <TD nowrap width="144">Betrag</TD>
            <TD nowrap align="center" width="200"><INPUT onkeypress="return filterCharForFloat(window.event);" name="montantInitial" type="text" value="<%=viewBean.getMontantInitial()%>" class="montant"></TD>
            <TD nowrap align="center" width="200"><INPUT onkeypress="return filterCharForFloat(window.event);" name="montantDejaFacture" type="text" value="<%=viewBean.getMontantDejaFacture()%>" class="montant"></TD>
            <TD nowrap align="center" width="200"><INPUT name="montantFacture1" type="text" value="<%=viewBean.getMontantFacture()%>" class="montantDisabled" readonly tabindex="-1"></TD>
          </TR>
	 	</TBODY>
	   </TABLE>
      <!-- Période -->
      <TABLE border="0" cellspacing="0" cellpadding="0" width="701" id="tPeriode">
        <TBODY>
	<TR>
            <TD nowrap width="145" height="26">Beitragsjahr</TD>
            <TD nowrap width="517" height="26">
				<INPUT name="anneeCotisation" type="text" value="<%=viewBean.getAnneeCotisation()%>" size="4" maxlength="4"> </TD>
            <TD nowrap width="25" height="26"></TD>
          </TR>

	<TR>
            <TD nowrap width="145" height="26">Periode</TD>
            <TD nowrap width="517" height="26">
		<ct:FWCalendarTag name="debutPeriode" 
		value="<%=viewBean.getDebutPeriode()%>" 
		errorMessage="la date de début est incorrecte"
		doClientValidation="CALENDAR"
	  /> &nbsp;bis&nbsp; <ct:FWCalendarTag name="finPeriode" 
		value="<%=viewBean.getFinPeriode()%>"
		errorMessage="la date de fin est incorrecte"
		doClientValidation="CALENDAR"
	       /> </TD>
            <TD nowrap width="25" height="26"></TD>
          </TR>
          <TR> 
              <TD nowrap width="145">Valutadatum</TD>
              <TD nowrap> 
                <ct:FWCalendarTag name="dateValeur" value="<%=viewBean.getDateValeur()%>" />
              </TD>

          </TR> 
	 	</TBODY>
	   </TABLE>
      <!-- Période -->
      <TABLE border="0" cellspacing="0" cellpadding="0" width="701">
        <TBODY>
	<TR>
            <TD width="144" nowrap>&nbsp;</TD>
            <TD nowrap width="537"></TD>
            <TD></TD>
       </TR>
	<TR>
            <TD width="144">Interne Bemerkung</TD>
            <TD ><TEXTAREA name="remarque" rows="5" cols="25" class="libelleLong"><%=viewBean.getRemarque()%>
		</TEXTAREA></TD>
            <TD></TD>
          </TR>

	<TR>
            <TD width="144">Pendent</TD>
            <TD ><input type="checkbox" name="aQuittancer" <%=(viewBean.isAQuittancer().booleanValue())? "checked" : "unchecked"%>></TD>
            <TD></TD>
       </TR>
	<TR>
            <TD width="144">Buchung nicht verbuchen</TD>
            <TD><input type="checkbox" name="nonComptabilisable" <%=(viewBean.isNonComptabilisable().booleanValue())? "checked" : "unchecked"%>>
	     </TD>
            <TD></TD>
          </TR> 
	<TR>
            <TD width="144">Benutzer</TD>
            <TD><INPUT name="module" type="text" value="<%=viewBean.getUser()%>" class="libelleLongDisabled" readonly tabindex="-1"></TD>
            <TD align="center"></TD>
          </TR>
	<TR>
            <TD width="144">Modul</TD>
            <TD><ct:FWListSelectTag name="idModuleFacturation"
			              defaut="<%=viewBean.getIdModuleFacturation()%>"
			              data="<%=globaz.musca.translation.CodeSystem.getListModulesFacturation(session,true)%>"
			/></TD>
            <TD align="center"><INPUT type="hidden" name="idEnteteFacture" value='<%=viewBean.getIdEnteteFacture()%>'>
            <input type="hidden" value="" name="idTiersDebiteurCompensation" value='<%=viewBean.getIdTiersDebiteurCompensation()%>'></TD>
          </TR>
	</TBODY></TABLE></TD></TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>
<script>
if (document.forms[0].elements('idTypeAfact').value == "904001") {
	showTable("tStandard");
	hideTable("tCompensation");
	hideTable("tTableau");
	showTable("tPeriode");
	showTable("tMontant");
}
if (document.forms[0].elements('idTypeAfact').value == "904002") {
	hideTable("tStandard");
	showTable("tCompensation");
	hideTable("tTableau");
	hideTable("tPeriode");
	showTable("tMontant");
}
if (document.forms[0].elements('idTypeAfact').value == "904003") {
	hideTable("tStandard");
	hideTable("tCompensation");
	showTable("tTableau");
	showTable("tPeriode");
	hideTable("tMontant");
}
if (document.forms[0].elements('idTypeAfact').value == "904004") {
	hideTable("tStandard");
	showTable("tCompensation");
	hideTable("tTableau");
	hideTable("tPeriode");
	showTable("tMontant");
}

//Pour agrandir le taglib concerné et l'aligner
document.getElementById("idTypeAfact").style.width=160;
document.getElementById("idTypeFactureCompensation").style.width=260;

</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>