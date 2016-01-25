<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.helios.db.modeles.*" %>
<%
	idEcran = "GCF0023";
	CGModeleEcritureViewBean viewBean = (CGModeleEcritureViewBean) session.getAttribute("viewBean");
	CGLigneModeleEcritureListViewBean listViewBean = (CGLigneModeleEcritureListViewBean) session.getAttribute("listViewBean");

	userActionValue = "helios.comptes.ecritureModele.executer";

	String aucun = "Aucun";
	if (languePage.equalsIgnoreCase("DE")) {
		aucun = "Kein";
	}

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="JavaScript">

var countMultiParam = 0;

function updateBalance() {
	var decote=/'/g;

	balanceMontant = parseFloat('0.0');

	for (i=0; i<countMultiParam; i++) {
		action = document.getElementById("codeDebitCredit" + i).value;

		if (document.getElementById("mc" + i).value != '') {
			montantCollective = document.getElementById("mc" + i).value;

			if ((action == '724001') || (action == '724004')) {
				// Débit ou Extourne Crédit
				montantCollective = montantCollective.replace(decote,"");
				balanceMontant += parseFloat(montantCollective);
			} else {
				// Crédit ou Extourne Débit
				montantCollective = montantCollective.replace(decote,"");
				balanceMontant -= parseFloat(montantCollective);
			}
		} else if ((document.getElementById("mmc" + i) != null) && (document.getElementById("cmc" + i) != null) && (document.getElementById("mmc" + i).value != '') && (document.getElementById("cmc" + i).value != '')) {
			montantMonnaieCollective = document.getElementById("mmc" + i).value;
			coursMonnaieCollective = document.getElementById("cmc" + i).value;

			montantMonnaieCollective = montantMonnaieCollective.replace(decote,"");
			coursMonnaieCollective = coursMonnaieCollective.replace(decote,"");

			montantChf = parseFloat(montantMonnaieCollective) * parseFloat(coursMonnaieCollective);

			if ((action == '724001') || (action == '724004')) {
				// Débit ou Extourne Crédit
				balanceMontant += montantChf;
			} else {
				// Crédit ou Extourne Débit
				balanceMontant -= montantChf;
			}
		}
	}

	document.getElementById("balance").value = '';
	document.getElementById("balance").value = balanceMontant+'';
	validateFloatNumber(document.getElementById("balance"));
}

function enableBlock(i) {
	document.getElementById("block" + i).style.display = "block";
}

function disableAllBlock(max) {
	for (i=0;i<max;i++) {
		document.getElementById("block" + i).style.display = "none";
	}
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ajout d'écritures par modèles<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD nowrap>Modèle</TD>
                            <TD nowrap>
                            	<input type="text" class="libelleLongDisabled" readonly name="modeleEcriture" value="<%=viewBean.getIdModeleEcriture()%> - <%=viewBean.getLibelle()%>"/>
                            	<input type="hidden" name="selectedIdModel" value="<%=viewBean.getIdModeleEcriture()%>"/>
                            	<input type="hidden" name="interMandat" value="<%=request.getParameter("interMandat")%>"/>
                            	<input type="hidden" name="saisieEcran" value="true">
                            </TD>
							<TD nowrap>Balance</TD>
                            <TD nowrap><input type="text" class="montantDisabled" readonly id="balance" value="0.00" tabindex="-1" onchange="validateFloatNumber(this);"/>&nbsp;CHF
   						</TR>
						<%
							if ((request.getParameter("idJournal") != null) && (!"".equals(request.getParameter("idJournal")))) {
						%>
						<TR>
							<TD nowrap>Numéro du journal</td>
							<TD nowrap><input type="text" name="idJournal" class="libelleDisabled" readonly tabindex="-1" value="<%=request.getParameter("idJournal")%>"/></TD>
							<TD nowrap>Numéro d'écriture</TD>
							<TD nowrap><input type="text" name="idEnteteEcriture" class="libelleDisabled" readonly value=""/></TD>
                      	</TR>
                      	<%
							} else {
						%>
						<tr>
							<TD nowrap>Libellé journal</TD>
							<TD nowrap>
							<input type="hidden" name="idJournal" value=""/>
							<input type="text" name="libelleJournal" class="libelle" value="<%=request.getParameter("libelleJournal")%>" size="25"/>
							</TD>
							<TD nowrap colspan="2">&nbsp;</TD>
						</tr>
						<%
							}
						%>

					<tr>
			            <td nowrap>E-mail</td>
			            <td nowrap>
							<input type="text" name="eMailAddress" value="<%=listViewBean.getEMailAddress()%>" class="libelleLong"/>
			            </td>
			            <td colspan="2">&nbsp;</td>
					</tr>

                        <TR>
							<TD nowrap>Pièce</TD>
                            <TD nowrap>
                            	<input type="text" maxlength="10" class="libelle" name="piece" value="<%=viewBean.getPiece()%>">
                            </TD>
                             <td colspan="2">&nbsp;</td>
                      	</TR>

                        <TR>
							<TD nowrap>Date</TD>
                            <TD nowrap>
                            <%
                            	String calDate = new String();
                            	if (!globaz.jade.client.util.JadeStringUtil.isBlank(request.getParameter("date"))) {
                            		calDate = request.getParameter("date");
                            	}
                            %>
								<ct:FWCalendarTag name="date" value="<%=calDate%>"/>
                            </TD>
                             <td colspan="2">&nbsp;</td>
                      	</TR>


                      	<TR><TD colspan="4"><HR/><BR/>
                      	<input type="hidden" value="<%=session.getAttribute("idJournal")%>" name="idJournal"/>
                      	</TD></TR>

                      	<%
                      		int countDoubleParam = 0;
                      		int countMultiParam = 0;
                      		String rowStyle = "row";

                      		int countDisplayedLine = 0;
                      		int countDisplayedBlock = 0;
                      		int displayMaxBlock = 12;

                      		int i = 0;

                      		while (i<listViewBean.getSize()) {
                      			CGLigneModeleEcritureViewBean entity = (CGLigneModeleEcritureViewBean) listViewBean.get(i);

                      			CGEnteteModeleEcritureViewBean entete = entity.getEnteteViewBean();

	                  			if (entete.getExerciceForDate(calDate) != null) {

	                      			if (countDisplayedLine%displayMaxBlock == 0) {
	                      				%>
	                	      				<TR><TD colspan="4">
					                      	<TABLE width="100%" border="0" cellspacing="0" cellpadding="0" id="block<%=countDisplayedBlock%>">
											<TR align="center">
					                      		<TH colspan="2">Numéro</TH>
					                      		<TH width="6%">Mandat</TH>
					                      		<TH width="15%">Libellé</TH>
					                      		<TH width="8%">Code</TH>
					                      		<TH width="8%">Montant</TH>
					                      		<TH width="13%">Monnaie (&euro;,&#36;...)</TH>
					                      		<TH colspan="2">Cours</TH>
					                      	</TR>
	                      				<%
	                      				countDisplayedBlock++;
	                      			}

									String logoME = new String();
									if (globaz.helios.db.comptes.CGCompte.CS_MONNAIE_ETRANGERE.equals(entity.getCompte().getIdNature())) {
										logoME = "<span title=\"" + entity.getCompte().getCodeISOMonnaie();
										if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(entity.getMontantMonnaie())) {
											logoME += " : " + entity.getMontantMonnaie();
										}
										logoME += "\">" + "€</span>&nbsp;";
									}

		                  			String idExerciceComptableForDate = entete.getExerciceForDate(calDate).getIdExerciceComptable();
		                   			if ((entete != null) && (entete.getIdTypeEcriture().equals(globaz.helios.db.comptes.CGEcritureViewBean.CS_TYPE_ECRITURE_DOUBLE))) {
		                   				i++;
		                   				CGLigneModeleEcritureViewBean nextEntity = (CGLigneModeleEcritureViewBean) listViewBean.get(i);

					     				String nextLogoME = new String();
										if (globaz.helios.db.comptes.CGCompte.CS_MONNAIE_ETRANGERE.equals(nextEntity.getCompte().getIdNature())) {
											nextLogoME = "<span title=\"" + nextEntity.getCompte().getCodeISOMonnaie();
											if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(nextEntity.getMontantMonnaie())) {
												nextLogoME += " : " + nextEntity.getMontantMonnaie();
											}
											nextLogoME += "\">" + "€</span>&nbsp;";
										}

		                   				%>
		                   				<TR class="<%=rowStyle%>">
		                  				<TD>&nbsp;</TD>
		                   				<TD class="mtd" title="<%=entity.getCompteLibelle(idExerciceComptableForDate)%> :: <%=nextEntity.getCompteLibelle(idExerciceComptableForDate)%>">

		                   				<table>
		                   				<tr>
		                   				<td><%=entity.getIdExterne(idExerciceComptableForDate)%>&nbsp;<%=logoME%></td>
		                   				<td>::&nbsp;</td>
		                   				<td><%=nextEntity.getIdExterne(idExerciceComptableForDate)%>&nbsp;<%=nextLogoME%></td>
		                   				</tr>

		                   				<%
		                   				if ((entity.isCompteCentreCharge(idExerciceComptableForDate)) || (nextEntity.isCompteCentreCharge(idExerciceComptableForDate))) {
		                   				%>

		                   				<tr>
		                   				<td>
		                   				<%
		                   				if (entity.isCompteCentreCharge(idExerciceComptableForDate)) {
		                   						String tmp = "chd" + countDoubleParam;
		                   				%>
		                   				<ct:FWListSelectTag name="<%=tmp%>" defaut="<%=entity.getIdCentreCharge()%>" data="<%=globaz.helios.translation.CGListes.getCentreChargeListe(aucun, session, entete.getIdMandat())%>"/>
		                   				<%
		                   					}
		                   				%>
		                   				</td>
		                   				<td>&nbsp;</td>
		                   				<td>
		                   				<%
		                   					if (nextEntity.isCompteCentreCharge(idExerciceComptableForDate)) {
		                   						String tmp = "achd" + countDoubleParam;
		                   				%>
		                   				<ct:FWListSelectTag name="<%=tmp%>" defaut="<%=nextEntity.getIdCentreCharge()%>" data="<%=globaz.helios.translation.CGListes.getCentreChargeListe(aucun, session, entete.getIdMandat())%>"/>
		                   				<%
		                   					}
		                   				%>
		                   				</td>
		                   				</tr>

		                   				<%
		                   				}
		                   				%>

		                   				</table>

		                   				<input type="hidden" value="<%=entity.getIdLigneModeleEcriture()%>" name="ided<%=countDoubleParam%>"/>
		                   				<input type="hidden" value="<%=entity.getIdEnteteModeleEcriture()%>" name="ided<%=countDoubleParam%>"/>
		                   				<input type="hidden" value="<%=nextEntity.getIdLigneModeleEcriture()%>" name="aided<%=countDoubleParam%>"/>
		                   				</TD>
										<TD class="mtd" title="<%=entete.getMandat().getLibelle()%>"><%=entete.getIdMandat()%></TD>
		                   				<TD class="mtd"><INPUT type="text" maxlength="40" name="ld<%=countDoubleParam%>" value="<%=entity.getLibelle()%>"  tabindex="-1"/></TD>
		                   				<TD class="mtd"><%=entity.getCodeDebitCreditLibelle()%>&nbsp;/&nbsp;<%=nextEntity.getCodeDebitCreditLibelle()%>&nbsp;</TD>
		                   				<TD class="mtd"><INPUT type="text" class="numero" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" name="md<%=countDoubleParam%>" value="<%if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(entity.getMontant())) {%><%=entity.getFormattedMontant()%><%}%>" class="libelle" maxlength="15"/></TD>

		                      			<%
		                      			if (entity.getCompte().getIdNature().equals(globaz.helios.db.comptes.CGCompte.CS_MONNAIE_ETRANGERE)) {
		                      			%>
		                      			<TD class="mtd"><INPUT type="text" class="numero" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" name="mmd<%=countDoubleParam%>" value="<%if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(entity.getMontantMonnaie())) {%><%=entity.getFormattedMontantMonnaie()%><%}%>" class="libelle" maxlength="15"/></TD>
		                     			<TD>&nbsp;&nbsp;<INPUT type="text" onchange="validateFloatNumber(this,5);" onkeypress="return filterCharForPositivFloat(window.event);" name="cmd<%=countDoubleParam%>" value="<%if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(entity.getCoursMonnaie())) {%><%=entity.getCoursMonnaie()%><%}%>" class="numero" maxlength="9"/></TD>
		                     			<%
		                   				} else if (nextEntity.getCompte().getIdNature().equals(globaz.helios.db.comptes.CGCompte.CS_MONNAIE_ETRANGERE)) {
		                  				%>
		                   				<TD class="mtd"><INPUT type="text" class="numero" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" name="mmd<%=countDoubleParam%>" value="<%if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(nextEntity.getMontantMonnaie())) {%><%=nextEntity.getFormattedMontantMonnaie()%><%}%>" class="libelle" maxlength="15"/></TD>
		                   				<TD>&nbsp;&nbsp;<INPUT type="text" onchange="validateFloatNumber(this,5);" onkeypress="return filterCharForPositivFloat(window.event);" name="cmd<%=countDoubleParam%>" value="<%if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(nextEntity.getCoursMonnaie())) {%><%=nextEntity.getCoursMonnaie()%><%}%>" class="numero" maxlength="9"/></TD>
		                   				<%
		                   				} else {
	                    				%>
		                      			<TD class="mtd">&nbsp;</TD>
		                      			<TD>&nbsp;</TD>
		                      			<%
		                      			}
		                      			%>
		                      			<TD>&nbsp;</TD>
		                      			</TR>
		                      			<%
		                      			countDoubleParam++;
		                      			countDisplayedLine++;
		                      		} else {
		                      			%>
			                      		<TR class="<%=rowStyle%>">
			                      		<%
			                      		if ((i==0) || (!entity.getIdEnteteModeleEcriture().equals(((CGLigneModeleEcritureViewBean) listViewBean.get(i-1)).getIdEnteteModeleEcriture()))) {
			                      		%>
			                      			<TD valign="top" width="7" align="left"><img src="<%=request.getContextPath()%>/images/top_left.gif"/></TD>
			                      		<%
			                      			} else if ((i==listViewBean.size()-1) || (!entity.getIdEnteteModeleEcriture().equals(((CGLigneModeleEcritureViewBean) listViewBean.get(i+1)).getIdEnteteModeleEcriture()))) {
			                      		%>
			                      			<TD valign="bottom" width="7" align="left"><img src="<%=request.getContextPath()%>/images/bottom_left.gif"/></TD>
			                      		<%
			                      			} else {
			                      		%>
			                      			<TD>&nbsp;</TD>
			                      		<%
			                      			}
			                      		%>
			                      			<TD class="mtd" title="<%=entity.getCompteLibelle(idExerciceComptableForDate)%>"><span style="color:#0000FF"><%=entity.getIdExterne(idExerciceComptableForDate)%>&nbsp;<%=logoME%></span>
			                      			<%
			                   					if (entity.isCompteCentreCharge(idExerciceComptableForDate)) {
			                   						String tmp = "chc" + countMultiParam;
			                   				%>
			                   				<ct:FWListSelectTag name="<%=tmp%>" defaut="<%=entity.getIdCentreCharge()%>" data="<%=globaz.helios.translation.CGListes.getCentreChargeListe(aucun, session, entete.getIdMandat())%>"/>
			                   				<%
			                   					}
			                   				%>
			                      			<input type="hidden" value="<%=entity.getIdLigneModeleEcriture()%>" name="idec<%=countMultiParam%>"/>
			                      			<input type="hidden" value="<%=entity.getIdEnteteModeleEcriture()%>" name="idec<%=countMultiParam%>"/>
			                      			</TD>

			                      			<TD class="mtd" title="<%=entete.getMandat().getLibelle()%>"><%=entete.getIdMandat()%></TD>
			                      			<TD class="mtd"><span style="color:#0000FF"><INPUT type="text" maxlength="40" name="lc<%=countMultiParam%>" value="<%=entity.getLibelle()%>" tabindex="-1"/></span></TD>
		                      				<TD class="mtd"><span style="color:#0000FF"><%=entity.getCodeDebitCreditLibelle()%>&nbsp;</span>
		                      				<input type="hidden" name="codeDebitCredit<%=countMultiParam%>" value="<%=entity.getCodeDebitCredit()%>"/>
		                      				</TD>
			                      			<TD class="mtd"><INPUT type="text" class="numero" onchange="updateBalance();validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" name="mc<%=countMultiParam%>" value="<%if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(entity.getMontant())) {%><%=entity.getFormattedMontant()%><%}%>" class="libelle" maxlength="15"/></TD>
		                      				<%
		                      				if (entity.getCompte().getIdNature().equals(globaz.helios.db.comptes.CGCompte.CS_MONNAIE_ETRANGERE)) {
		                      				%>
		                      				<TD class="mtd"><INPUT type="text" class="numero" onchange="updateBalance();validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" name="mmc<%=countMultiParam%>" value="<%if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(entity.getMontantMonnaie())) {%><%=entity.getFormattedMontantMonnaie()%><%}%>" class="libelle" maxlength="15"/></TD>
		                      				<TD><INPUT type="text" onchange="updateBalance();validateFloatNumber(this,5);" onkeypress="return filterCharForPositivFloat(window.event);" name="cmc<%=countMultiParam%>" value="<%if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(entity.getCoursMonnaie())) {%><%=entity.getCoursMonnaie()%><%}%>" class="numero" maxlength="9"/></TD>
		                      				<%
		                      				} else {
		                      				%>
		                      				<TD class="mtd">&nbsp;</TD>
		                      				<TD>&nbsp;</TD>
		                      				<%
		                      				}
		                      				%>
		                      			<%
			                      			if ((i==0) || (!entity.getIdEnteteModeleEcriture().equals(((CGLigneModeleEcritureViewBean) listViewBean.get(i-1)).getIdEnteteModeleEcriture()))) {
			                      		%>
			                      			<TD colspan="2" valign="top" align="right"><img src="<%=request.getContextPath()%>/images/top_right.gif"/></TD>
			                      		<%
			                      			} else if ((i==listViewBean.size()-1) || (!entity.getIdEnteteModeleEcriture().equals(((CGLigneModeleEcritureViewBean) listViewBean.get(i+1)).getIdEnteteModeleEcriture()))) {
			                      		%>
			                      			<TD valign="bottom" align="right" colspan="2"><img src="<%=request.getContextPath()%>/images/bottom_right.gif"/></TD>
			                      		<%
			                      			} else {
			                      		%>
			                      			<TD colspan="2">&nbsp;</TD>
			                      		<%
			                      			}
			                      		%>
			                      			</TR>
			                      		<%
			                      			countMultiParam++;
			                      			countDisplayedLine++;
		                      		}


		                      		if ((countDisplayedLine%displayMaxBlock == 0) || (i == listViewBean.getSize() - 1)) {
	                      				%>
	                	      				</TABLE>
	                	      				</TD></TR>
	                      				<%
	                      			}

		                      		if (rowStyle.equals("rowOdd")) {
		       	            			rowStyle = "row";
		   	                  		} else {
		   	                  			rowStyle = "rowOdd";
		                      		}
								}

	                      		i++;
	                      	}
	                    %>

	                    <TR><TD colspan="4">
				        <TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	                    <TR><TD colspan="8" align="right">&nbsp;
						<%
							if (countDisplayedBlock > 1) {
								for (int j=0;j<countDisplayedBlock;j++) {
						%>
								<A href="#" onclick="disableAllBlock(<%=countDisplayedBlock%>);enableBlock(<%=j%>);">Page<%=j%></A><%if (j != countDisplayedBlock - 1) {%>,<%}%>&nbsp;
						<%
								}
							}
						%>
						</TD></TR>
                      	</TABLE>
                      	</TD></TR>

                      	<TR><TD colspan="4">&nbsp;<br/></TD></TR>

			          <SCRIPT language="JavaScript">
			          	countMultiParam = <%=(countMultiParam)%>;
			          	updateBalance();

          				disableAllBlock(<%=countDisplayedBlock%>);
          				enableBlock(0);
			          </SCRIPT>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>