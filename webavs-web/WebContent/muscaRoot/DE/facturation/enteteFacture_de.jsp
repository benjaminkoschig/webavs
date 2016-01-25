<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CFA0004";%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
 //contrôle des droits
 bButtonNew = objSession.hasRight(userActionNew, "ADD");

 globaz.musca.db.facturation.FAEnteteFactureViewBean viewBean = (globaz.musca.db.facturation.FAEnteteFactureViewBean)session.getAttribute ("viewBean");
 selectedIdValue = viewBean.getIdEntete();
 userActionValue = "musca.facturation.enteteFacture.modifier";
 viewBean.getDonnee();
 //Vérifie si les passage est verrouillé ou comptabilisé. si oui, n'affiche pas les boutons
 String passageStatus = globaz.musca.util.FAUtil.getPassageStatus(viewBean.getIdPassage(),session);
 boolean passageLocked =globaz.musca.util.FAUtil.getPassageLock(viewBean.getIdPassage(),session).booleanValue();

 if ( globaz.musca.db.facturation.FAPassage.CS_ETAT_COMPTABILISE.equalsIgnoreCase(passageStatus)
 	|| passageLocked
 	|| globaz.musca.db.facturation.FAPassage.CS_ETAT_ANNULE.equalsIgnoreCase(passageStatus)
 	|| globaz.musca.db.facturation.FAPassage.CS_ETAT_VALIDE.equalsIgnoreCase(passageStatus)){
		bButtonValidate = false;
		bButtonDelete = false;
		bButtonUpdate = false;
		bButtonNew = false;
	}
 String jspLocation = servletContext + mainServletPath + "Root/tiers_select.jsp";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
<%
	String idExterneRolePopup = request.getParameter("idExterneRolePopup");
	int autoDigiAff = globaz.musca.util.FAUtil.fetchAutoDigitAff(session);
%>
top.document.title = "Fakturierung - Detail der Abrechnungs"


function add() {
    document.forms[0].elements('userAction').value="musca.facturation.enteteFacture.ajouter"
    document.forms[0].elements('idExterneRolePopup').focus();
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="musca.facturation.enteteFacture.ajouter";
    else
        document.forms[0].elements('userAction').value="musca.facturation.enteteFacture.modifier";

    return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add") {

    document.forms[0].elements('userAction').value="musca.facturation.enteteFacture.chercher";
    }
 else
    document.forms[0].elements('userAction').value="musca.facturation.enteteFacture.afficher"
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="musca.facturation.enteteFacture.supprimer";
        document.forms[0].submit();
    }
}


function init(){}
function updateIdExterneRolePopup(tag){
	if(tag.select && tag.select.selectedIndex != -1){
		document.getElementById('idTiers').value=tag.select[tag.select.selectedIndex].idTiers;
		document.getElementById('idExterneRolePopup').value=tag.select[tag.select.selectedIndex].numAffilieActuel;
		document.getElementById('idAffiliation').value=tag.select[tag.select.selectedIndex].idAffiliationActuel;
		updateIdExterneRole(tag.select[tag.select.selectedIndex].numAffilieActuel);
		//alert(tag.select[tag.select.selectedIndex].numAffilieActuel+" "+tag.select[tag.select.selectedIndex].idAffiliationActuel);
		document.forms[0].elements('userAction').value="musca.facturation.enteteFacture.reload";
		document.forms[0].submit();
	}
}
function updatePopuptext(data){
	if(data==517002 || data==517039 || data==517040){
		document.getElementById('idExterneRolePopup').style.visibility = 'visible';
		document.getElementById('idExterneRoleInput').style.visibility='hidden';
		document.getElementById('idExterneRolePopup').style.display='inline';
		document.getElementById('idExterneRoleInput').style.display='none';
	}else{
		document.getElementById('idExterneRoleInput').style.visibility='visible';
		document.getElementById('idExterneRolePopup').style.visibility = 'hidden';
		document.getElementById('idExterneRolePopup').style.display='none';
		document.getElementById('idExterneRoleInput').style.display='inline';
	}

}
function updateIdExterneRole(data){
//alert(data);
	document.getElementById('idExterneRole').value = data;
}

// Variable globale
var tailleTA = 90;

function limite(champ,taille)
{
	if(champ.value.length >= taille)
	{
		alert ("Vous avez dépassé le nombre maximum de caractères : " + tailleTA);
		// ici on bloque la taille en cas de copier/coller
		champ.value = champ.value.substr(0, taille);
		// et on retourne faux sinon il ajoute le caractere quand meme onKeyPress.
		return false;
	}
}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail der Abrechnung<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					<TR>
						<TD>
							<TABLE border="0">
								<TBODY>
									<TR>
										<TD nowrap width="165">Debitor</TD>
										<TD nowrap>
											<ct:FWPopupList
												name="idExterneRolePopup"
							            		value="<%=viewBean.getIdExterneRole()%>"
							            		className="libelle"
							            		jspName="<%=jspLocation%>"
							            		autoNbrDigit="<%=autoDigiAff%>"
							            		size="15"
							            		onChange="updateIdExterneRolePopup(tag);"
							            		minNbrDigit="3"
											/>
											<SCRIPT>
							            		document.getElementById('idExterneRolePopup').style.visibility='visible';
							            		document.getElementById('idExterneRolePopup').style.display='inline';
							            		document.getElementById("idExterneRolePopup").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
							            	</SCRIPT>
                							<INPUT name="idExterneRoleInput" type="text" value="<%=viewBean.getIdExterneRole()%>" onChange="updateIdExterneRole(document.getElementById('idExterneRoleInput').value);"  maxlength="20" size="15">
							 			 	<script>
												document.getElementById('idExterneRoleInput').style.visibility='hidden';
												document.getElementById('idExterneRoleInput').style.display='none';
											</script>
											<INPUT name="idExterneRole" type="hidden" value="<%=viewBean.getIdExterneRole()%>" doclientvalidation="NOT_EMPTY" maxlength="20" size="15">
										</TD>
										<TD nowrap>
							              <select name="idRole">
							              	<%=CARoleViewBean.createOptionsTags(objSession, viewBean.getIdRole(), false)%>
							              </select>
							              <script>
												document.getElementById('idRole').onchange = new Function("","return updatePopuptext(this.options[this.selectedIndex].value);");
										  </script>
							            </TD>

										<TD nowrap><INPUT type="text" value="<%=viewBean.getNomTiers()%>" class="libelleLongDisabled" readonly name="nomDebiteur" tabindex="-1"></TD>
										<TD nowrap align="right"><input type="hidden" onClick="_pos.value=codeAmbassade.value;_meth.value=_method.value;_act.value='pyxis.tiers.gestion.afficher';userAction.value='pyxis.tiers.administration.chercher';selGenre.value=<%=globaz.pyxis.db.tiers.TIAdministrationViewBean.CS_AMBASSADE%>;submit()" value="..."></TD>
									</TR>
									<TR>
										<TD nowrap>Abrechnung</TD>
										<TD nowrap>
											<input name="idExterneFacture" type="text" value="<%=viewBean.getIdExterneFacture()%>" doclientvalidation="NOT_EMPTY" maxlength="20" size="15">
										</TD>
										<TD width="50"><ct:FWSystemCodeSelectTag name="idSousType"
											defaut="<%=viewBean.getIdSousType()%>"
											libelle=" "
											codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsTypeFacture(session)%>"/>
										</TD>
										<td align="right" rowspan="4" width="370">
											<TABLE border="1" cellpadding="5" >
												<TR>
													<TH align="center">Hilfsmenü</TH>
												</TR>
												<TR >
													<TD bordercolor="navy" bgcolor="#BBCCEE">
														<A href="<%=request.getContextPath()%>\musca?userAction=musca.facturation.enteteFacture.chercher&idPassage=<%=viewBean.getIdPassage()%>" tabindex="-1">Zurück zur Abrechnungsliste</A><br>
													</TD>
												</TR>
											</TABLE>
										</td>
									</TR>
									<tr>
									<TD nowrap>Referenz Person</TD>
										<TD nowrap>
										<INPUT type="hidden" name="wantReferenceFacture" value='<%=viewBean.isWantReferenceFacture()%>'>
										<% if(viewBean.isWantReferenceFacture().equals(Boolean.TRUE)) { %>
											<input name="referenceFacture" type="text" value="<%=viewBean.getReferenceFacture()%>" maxlength="16" size="16">
										<% } else { %>
											<input name="referenceFacture" type="text" value="<%=viewBean.getReferenceFacture()%>"readonly class="inputDisabled" size="16">
										<% }%>
										</TD>
										<TD nowrap>	
											<input name="nomPrenomFacture" type="text" class='libelleLongDisabled' readonly value="<%=viewBean.getNomPrenomFacture()%>">
										</TD>
									</tr>
									<% if (viewBean.getIdAffiliation() != null && viewBean.getIdAffiliation().length() != 0) { %>
									<TR>
										<TD>Erfassungsplan:</TD>
										<TD>
											<select name="planAffiliationId">
												<%=globaz.naos.util.AFUtil.getPlanAffiliation(viewBean.getIdAffiliation(), null, session,true)%>
											</select>
										</TD>
									</TR>
									<% } %>
								</TBODY>
							</TABLE>
	   				<br><br>
      				<!-- Page 1 -->
				<TABLE border="0" cellspacing="0" cellpadding="0" width="701">
	                <TBODY>
	                <TR>
	                  <TD nowrap colspan="2" width="140">Empfangsdatum</TD>
	                  <TD nowrap width="500">
	                    <ct:FWCalendarTag name="dateReceptionDS" value="<%=viewBean.getDateReceptionDS()%>" />
	                  	&nbsp;&nbsp;&nbsp;<I>Muss eingegeben werden für die Abrechnungstypen 13 oder 14</TD>

	                </TR>
	                <TR>
	                  <TD nowrap colspan="2" width="140"></TD>
	                  <TD nowrap width="150">&nbsp;</TD>
	                  <TD nowrap width="70"></TD>
	                </TR>
	                <TR>
	                  <TD nowrap colspan="2" width="140">Betrag der Abrechnung</TD>
	                  <TD nowrap width="150">
	                    <INPUT name="totalFacture" type="text" value="<%=viewBean.getTotalFacture()%>" class="montantDisabled" readonly tabindex="-1">
	                  </TD>
	                  <TD nowrap width="70"></TD>
	                </TR>
	                <TR>
	                  <TD nowrap colspan="2" width="140"></TD>
	                  <TD nowrap width="150">&nbsp;</TD>
	                  <TD nowrap width="70"></TD>
	                </TR>
	                <TR>
	                  <TD nowrap colspan="2">Rechnungsadresse</TD>
	                  <TD nowrap>
	                    <TEXTAREA rows="5" width="250" align="left" readonly class="libelleLongDisabled" tabindex="-1"><%=globaz.musca.util.FAUtil.getAdressePrincipaleCourrier(session, viewBean.getIdAdresse())%>
		      			</TEXTAREA>
	                    <input type="button" onClick="_pos.value=codeAmbassade.value;_meth.value=_method.value;_act.value='pyxis.tiers.gestion.afficher';userAction.value='pyxis.tiers.administration.chercher';selGenre.value=<%=globaz.pyxis.db.tiers.TIAdministrationViewBean.CS_AMBASSADE%>;submit()" value="..." name="button">
	                  </TD>
	                  <TD align="center">&nbsp; </TD>
	                </TR>
	                
	                <TR>
	                	<TD nowrap colspan="2">?Mode d'impression</TD>
	                	<TD>
		                	<ct:FWSystemCodeSelectTag name="idCSModeImpression"
		            		defaut="<%=viewBean.giveIdCSModeImpression()%>"
		            		codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsModeImpression(session)%>"
		            		/>
	                	</TD>
	                	<TD></TD>
	                </TR>
	                
	                <TR>
	                  <TD nowrap colspan="2">Verzugszinsen</TD>
	                  <TD nowrap>
	                  <%
			            	java.util.HashSet except = new java.util.HashSet();
			            	except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_MANUEL);
			            	except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_A_CONTROLER);
			            %>

	                  <ct:FWSystemCodeSelectTag name="idSoumisInteretsMoratoires"
	            		defaut="<%=viewBean.getIdSoumisInteretsMoratoires()%>"
	            		codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsMotifIm(session)%>"
	            		except="<%=except%>"
	            		/>
	                  </TD>
	                  <TD>Grund</TD>
	                  <TD>
	                    <input name="motifInteretsMoratoires" type="text" value="<%=viewBean.getMotifInteretsMoratoires()%>" class="libelleLong" maxlength="100">
	                  </TD>
	                  <TD width="80" nowrap>&nbsp;</TD>
	                  <TD>&nbsp; </TD>
	                </TR>
	                <TR>
	                  <TD nowrap colspan="2">Eintreibungsgrund</TD>
	                  <TD nowrap><ct:FWSystemCodeSelectTag name="idModeRecouvrement"
	            		defaut="<%=viewBean.getIdModeRecouvrement()%>"
	            		codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsModeRecouvrementWithoutBlank(session)%>"/>
	                  </TD>
	                  <TD></TD>
	                </TR>
	                <TR>
	                  <TD nowrap colspan="2" width="140"></TD>
	                  <TD nowrap width="150">&nbsp;</TD>
	                  <TD nowrap width="70"></TD>
	                </TR>
	                <TR>
	                  <TD nowrap colspan="2">Zahlungsadresse</TD>
	                  <TD nowrap>
	                    <textarea rows="5" cols="25" readonly class="libelleLongDisabled" name="textarea" tabindex="-1"><%=globaz.musca.util.FAUtil.getAdressePrincipalePaiementFromLinkPmt(session, viewBean.getIdAdressePaiement())%></textarea>
	                    <input type="button" onClick="_pos.value=codeAmbassade.value;_meth.value=_method.value;_act.value='pyxis.tiers.gestion.afficher';userAction.value='pyxis.tiers.administration.chercher';selGenre.value=<%=globaz.pyxis.db.tiers.TIAdministrationViewBean.CS_AMBASSADE%>;submit()" value="..." name="button2">
	                  </TD>
	                  <TD align="center">&nbsp; </TD>
	                </TR>
	                <TR>
	                  <TD nowrap colspan="2">Bemerkung<BR>(erscheint auf der Abrechnung)</TD>
	                  <TD nowrap>
	                    <TEXTAREA name="remarque" rows="3" class="libelleLong" onkeypress="return limite(this,tailleTA);" onchange="limite(this,tailleTA);"><%=viewBean.getRemarque()%></TEXTAREA>
	                  </TD>
	                  <TD>
	                    <INPUT type="hidden" name="idPassage" value='<%=viewBean.getIdPassage()%>'>
	                    <input type="hidden" value="" name="idTiers" value='<%=viewBean.getIdTiers()%>'>
	                    <input type="hidden" value="" name="idAffiliation" value='<%=viewBean.getIdAffiliation()%>'>
	                  </TD>
	                </TR>
	                </TBODY>
	              </TABLE>
            </TD></TR>
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

//Pour agrandir le taglib concerné et l'aligner
document.getElementById("idRole").style.width=348;
document.getElementById("idSoumisInteretsMoratoires").style.width=268;
document.getElementById("idModeRecouvrement").style.width=268;

</script>
<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal"/>
<ct:menuChange displayId="options" menuId="FA-EnteteFactureDetail" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdEntete()%>"/>
	<ct:menuSetAllParams key="idEnteteFacture" value="<%=viewBean.getIdEntete()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdEntete()%>"/>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>