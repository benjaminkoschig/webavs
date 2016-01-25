<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.aquila.db.irrecouvrables.COSectionsViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="globaz.aquila.db.irrecouvrables.COPoste"%>
<%@page import="globaz.aquila.db.irrecouvrables.CODetailSection"%>
<%@page import="globaz.globall.util.JANumberFormatter"%>
<%@page import="globaz.osiris.api.APIVPDetailMontant"%>
<%
	COSectionsViewBean viewBean = (COSectionsViewBean) session.getAttribute("viewBean");

	idEcran = "GCO0012";
	userActionValue = "aquila.irrecouvrables.sections.executer";
	formEncType = "iso-8859-1\" method=\"POST\"";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%><ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CO-OptionsDefaut" showTab="menu"/>

	<script language="JavaScript">
		function validate() {
			jscss("add", document.getElementById("btnOk"), "hidden");
			//document.getElementById("btnOk").disabled = true;
			return true;
		}

		function postInit() {
			if (document.forms[0].elements("libelleJournal").value == "") {
				document.forms[0].elements("libelleJournal").value = "<%=JadeStringUtil.isBlank(viewBean.getLibelleJournal())?"Irr. " + viewBean.getCompteAnnexe().getRole().getDescription() + " " + viewBean.getCompteAnnexe().getIdExterneRole():viewBean.getLibelleJournal()%>";
			}
		}

		function checkNegatif(element){
			if (parseFloat(element.value)<0){
				element.style.color = 'red';
			} else {
				element.style.color = 'black';
			}
		}

		function deformatNumber(nombre){
			var truc = new String(nombre);
			var result = truc.replace(/'/g,"");
			return result;
		}


		var idsSectionsEtRubriques = new Array();
		var idsRubriquesIrrecouvrables = new Array();

		function montantVerseChange(idSectionEtRubrique,idRubriqueIrrecouvrable){
			var idRubEtSecDeDepart = 0;
			var idRubIrrecDeDepart = 0;
			//parcours du tableau afin de changer les valeurs des champs en fonction de ce qui a changé
			//on cherche tout d'abord ou on doit commencer
			for (i=0;i<idsRubriquesIrrecouvrables.length;i++){
				if (idsRubriquesIrrecouvrables[i]==idRubriqueIrrecouvrable){
					idRubIrrecDeDepart = i;
					break;
				}
			}
			for (i=0;i<idsSectionsEtRubriques.length;i++){
				if (idsSectionsEtRubriques[i]==idSectionEtRubrique){
					idRubEtSecDeDepart = i;
					break;
				}
			}

			//on fait les changements
			//nouveau montant irrecouvrable
			document.forms[0].elements("montantIrrecouvrable_"+idSectionEtRubrique).value=Math.round((parseFloat(deformatNumber(document.forms[0].elements("montantDu_"+idSectionEtRubrique).value)) - parseFloat(deformatNumber(document.forms[0].elements("montantVerse_"+idSectionEtRubrique).value)))*100)/100;
			//en rouge si négatif
			checkNegatif(document.forms[0].elements("montantIrrecouvrable_"+idSectionEtRubrique));

			// mise à jour des montants totaux
			var totalVerse = 0;
			for (i=0;i<idsSectionsEtRubriques.length;i++){
				if (("montantVerse_"+idsSectionsEtRubriques[i]).split("_")[4]==idRubriqueIrrecouvrable ){
					totalVerse = Math.round((totalVerse + parseFloat(deformatNumber(document.forms[0].elements("montantVerse_"+idsSectionsEtRubriques[i]).value)))*100)/100;
				}
			}
			document.forms[0].elements("montantTotalVerse_"+idRubriqueIrrecouvrable).value=totalVerse;

			document.forms[0].elements("montantTotalIrrecouvrable_"+idRubriqueIrrecouvrable).value=Math.round((parseFloat(deformatNumber(document.forms[0].elements("montantTotalDu_"+idRubriqueIrrecouvrable).value)) - parseFloat(deformatNumber(document.forms[0].elements("montantTotalVerse_"+idRubriqueIrrecouvrable).value)))*100)/100;
			// en rouge si négatif
			checkNegatif(document.forms[0].elements("montantTotalIrrecouvrable_" + idRubriqueIrrecouvrable));


			//Mise à jour des montants disponibles
			var montantPrecedent = 0;

			for (i=idRubIrrecDeDepart;i<idsRubriquesIrrecouvrables.length;i++){
				var montantPrecedent = 0;
				if (i==0){
					montantPrecedent = parseFloat(deformatNumber(document.forms[0].elements("montantTotalDisponible").value));
				} else {
					montantPrecedent = parseFloat(deformatNumber(document.forms[0].elements("montantDisponible_"+idsRubriquesIrrecouvrables[i-1]).value));
				}

				document.forms[0].elements("montantDisponible_"+idsRubriquesIrrecouvrables[i]).value = Math.round((parseFloat(montantPrecedent)-parseFloat(deformatNumber(document.forms[0].elements("montantTotalVerse_"+idsRubriquesIrrecouvrables[i]).value)))*100)/100;

				//en rouge si négatif
				checkNegatif(document.forms[0].elements("montantDisponible_"+idsRubriquesIrrecouvrables[i]));

			}

			//montant total irrecouvrable
			var total = 0;
			for (i=0;i<idsRubriquesIrrecouvrables.length;i++){
				total = Math.round((parseFloat(total)+parseFloat(deformatNumber(document.forms[0].elements("montantTotalIrrecouvrable_"+idsRubriquesIrrecouvrables[i]).value)))*100)/100;
			}
			document.forms[0].elements("montantTotalIrrecouvrables").value=total;




			//on reformat tout les trucs
			//l'irrecouvrable du truc qu'on a changé
			validateFloatNumber(document.forms[0].elements("montantIrrecouvrable_"+idSectionEtRubrique));
			//le total irrecouvrable de la rubrique qu'on a modifié
			validateFloatNumber(document.forms[0].elements("montantTotalIrrecouvrable_"+idRubriqueIrrecouvrable));
			//total versé pour la rubrique qu'on vient de changer
			validateFloatNumber(document.forms[0].elements("montantTotalVerse_"+idRubriqueIrrecouvrable));
			//les nouveaux montants dispo
			for (i=idRubIrrecDeDepart;i<idsRubriquesIrrecouvrables.length;i++){
				validateFloatNumber(document.forms[0].elements("montantDisponible_"+idsRubriquesIrrecouvrables[i]));
			}
			//le montant total irrecouvrable
			validateFloatNumber(document.forms[0].elements("montantTotalIrrecouvrables"));
			//en rouge si negatif
			checkNegatif(document.forms[0].elements("montantTotalIrrecouvrables"));

			// mise à jour du champ caché dernier montant dispo
			document.forms[0].elements("dernierMontantDisponible").value = document.forms[0].elements("montantDisponible_"+idsRubriquesIrrecouvrables[idsRubriquesIrrecouvrables.length - 1]).value;
		}
	</script>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Répartition des montants payés<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD colspan="3"><table>
								<tr>
									<TD>E-mail</TD>
									<TD><INPUT type="text" name="emailAddress" value="<%=viewBean.getEmailAddress()%>" class="libelleLong"></TD>
								</tr>
								<tr>
									<TD>Libelle journal</TD>
									<TD><INPUT type="text" name="libelleJournal" value="" size="50"></TD>
								</tr>
								<tr>
									<TD>Année</TD>
									<TD><INPUT type="text" name="annee" value="<%=viewBean.getAnnee()%>" size="4" maxlength="4" class="annee"></TD>
								</tr>
								<tr>
									<TD></TD>
									<td><input type="checkbox" id="extourneCI" name="extourneCI" value="on" checked><label for="extourneCI">Effectuer l'extourne aux CI</label></td>
								</tr>
							</table>
							</TD>
						</TR>
						<TR><TD colspan="4">&nbsp;</TD></TR>
						<TR>
							<TD>
								<INPUT type="hidden" name="selectedIds" value="<%=viewBean.getSelectedIds()%>">
								<INPUT type="hidden" name="idCompteAnnexe" value="<%=viewBean.getIdCompteAnnexe()%>">
							</TD>
							<TH>Montant dû</TH>
							<TH>Montant payé</TH>
							<TH>Irrécouvrable</TH>
						</TR>
						<TR><TD colspan="4">&nbsp;</TD></TR>
						<TR>
							<TD><b>Montant total disponible</b></TD>
							<TD></TD>
							<TD><INPUT style="background-color:#a2b5cd;" type="text" name="montantTotalDisponible" value="<%=viewBean.getMontantTotalDisponibleFormatte()%>" class="montantDisabledNoBorder" readonly></TD>
							<TD></TD>
						</TR>
						<%
						BigDecimal montantDisponible = viewBean.getMontantTotalDisponible();

						for (Iterator postes = viewBean.getListePostesTriesParOrdre().iterator(); postes.hasNext();) {
							COPoste poste = (COPoste) postes.next();
						%>
						<TR><TD colspan="4">&nbsp;</TD></TR>
						<TR>
							<TD colspan="4"><H4><%=poste.getLibellePoste()%></H4></TD>
						</TR>
						<%
							for (Iterator detailSections = poste.getDetailsSections().iterator(); detailSections.hasNext();) {
								CODetailSection detailSection = (CODetailSection) detailSections.next();
								String style = "color=#000000";
								String texte = "";

								if (APIVPDetailMontant.CS_VP_MONTANT_EMPLOYEUR.equals(detailSection.getTypeOrdre())) {
									texte = " - employeur";
								} else if (APIVPDetailMontant.CS_VP_MONTANT_SALARIE.equals(detailSection.getTypeOrdre())) {
									style = "color=#000099";
									texte = " - salarié";
								}
						%>
						<TR>
							<TD style='<%=style%>'><%=detailSection.getIdExterne()%> <%=detailSection.getLibelle()%><%=texte%></TD>
							<TD><INPUT type="text" name="montantDu_<%=detailSection.getIdSection()%>_<%=detailSection.getIdRubrique()%>_<%=detailSection.getTypeOrdre()%>_<%=poste.getIdRubriqueIrrecouvrable()%>" value="<%=detailSection.getMontantDuFormatte()%>" class="montantDisabledNoBorder" readonly></TD>
							<TD><INPUT type="text" name="montantVerse_<%=detailSection.getIdSection()%>_<%=detailSection.getIdRubrique()%>_<%=detailSection.getTypeOrdre()%>_<%=poste.getIdRubriqueIrrecouvrable()%>" value="<%=detailSection.getMontantVerseFormatte()%>" class="montant" onchange="validateFloatNumber(this);montantVerseChange('<%=detailSection.getIdSection()%>_<%=detailSection.getIdRubrique()%>_<%=detailSection.getTypeOrdre()%>_<%=poste.getIdRubriqueIrrecouvrable()%>','<%=poste.getIdRubriqueIrrecouvrable()%>');" onkeypress="return filterCharForFloat(window.event);"></TD>
							<TD><INPUT type="text" name="montantIrrecouvrable_<%=detailSection.getIdSection()%>_<%=detailSection.getIdRubrique()%>_<%=detailSection.getTypeOrdre()%>_<%=poste.getIdRubriqueIrrecouvrable()%>" value="<%=detailSection.getMontantIrrecouvrableFormatte()%>" readonly class="montantDisabledNoBorder" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
						</TR>
						<script language="JavaScript">
							idsSectionsEtRubriques[idsSectionsEtRubriques.length]="<%=detailSection.getIdSection()%>_<%=detailSection.getIdRubrique()%>_<%=detailSection.getTypeOrdre()%>_<%=poste.getIdRubriqueIrrecouvrable()%>";
						</script>
						<%
							}

							montantDisponible = montantDisponible.subtract(poste.getMontantTotalVerse());

						%>

						<TR>
							<TD><i>Total pour ce poste</i></TD>
							<TD class="total"><INPUT type="text" name="montantTotalDu_<%=poste.getIdRubriqueIrrecouvrable()%>" value="<%=poste.getMontantTotalDuFormatte()%>" class="montantDisabledNoBorder" readonly></TD>
							<TD class="total"><INPUT type="text" name="montantTotalVerse_<%=poste.getIdRubriqueIrrecouvrable()%>" value="<%=poste.getMontantTotalVerseFormatte()%>" class="montantDisabledNoBorder" readonly></TD>
							<TD class="total"><INPUT type="text" name="montantTotalIrrecouvrable_<%=poste.getIdRubriqueIrrecouvrable()%>" value="<%=poste.getMontantIrrecouvrableFormatte()%>" class="montantDisabledNoBorder" readonly></TD>
						</TR>
						<TR>
							<TD><b><i>Nouveau montant disponible</i></b></TD>
							<TD></TD>
							<TD><INPUT style="background-color:#a2b5cd;" type="text" name="montantDisponible_<%=poste.getIdRubriqueIrrecouvrable()%>" value="<%=JANumberFormatter.format(montantDisponible)%>" class="montantDisabledNoBorder" readonly></TD>
						<%if (!postes.hasNext()){%>
							<TD><INPUT type="hidden" name="dernierMontantDisponible" value="<%=JANumberFormatter.format(montantDisponible)%>"> </TD>
						<%}%>
						</TR>
						<script language="JavaScript">
							idsRubriquesIrrecouvrables[idsRubriquesIrrecouvrables.length]="<%=poste.getIdRubriqueIrrecouvrable()%>";
						</script>
						<%
						}
						%>
						<TR><TD colspan="4">&nbsp;</TD></TR>
						<TR>
							<TD><b>Montant total irrécouvrables</b></TD>
							<TD></TD>
							<TD></TD>
							<TD class="total"><INPUT style="background-color:#cdb5a2;" type="text" name="montantTotalIrrecouvrables" value="<%=viewBean.getMontantTotalIrrecouvrableFormatte()%>" readonly class="montantDisabledNoBorder" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>