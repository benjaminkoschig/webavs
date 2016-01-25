<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.pyxis.constantes.IConstantes"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
	<%
	idEcran="CCP1004";
	globaz.phenix.db.communications.CPCommunicationFiscaleViewBean viewBean = (globaz.phenix.db.communications.CPCommunicationFiscaleViewBean)session.getAttribute ("viewBean");
	key="globaz.phenix.db.communications.CPCommunicationFiscaleViewBean-idCommunication"+viewBean.getIdCommunication();
	%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
	top.document.title = "Cotisation - Communication Détail"
	function add() {
		document.forms[0].elements('userAction').value="phenix.principale.communicationFiscaleAffichage.ajouter"
	}
	function upd() {
	}
	function validate() {
		state = validateFields(); 
		if (document.forms[0].elements('_method').value == "add")
			document.forms[0].elements('userAction').value="phenix.communications.communicationFiscaleAffichage.ajouter";
		else
			document.forms[0].elements('userAction').value="phenix.communications.communicationFiscaleAffichage.modifier";
		return (state);
	}
	function cancel() {
		document.forms[0].elements('userAction').value="phenix.communications.communicationFiscaleAffichage.chercher";
	}
	function del() {
		if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
		{
			document.forms[0].elements('userAction').value="phenix.communications.communicationFiscaleAffichage.supprimer";
			document.forms[0].submit();
		}
	}
	function init(){}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Demande de communication fiscale<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
				            <TD nowrap width="140">Tiers</TD>
				            <TD nowrap >
								<INPUT type="text" name="nom" tabindex="-1" value="<%=viewBean.getNom()%>" class="libelleLongDisabled" readonly>
								<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>">
								<%
								Object[] tiersMethodsName= new Object[]{
									new String[]{"setIdTiers","getIdTiers"},
									new String[]{"setNom","getNom"},
									new String[]{"setNumAffilie","getNumAffilieActuel"},
									new String[]{"setNumAvs","getNumAvsActuel"},
									new String[]{"setLocalite","getLocalite"},
									new String[]{"setNumContri","getNumContribuableActuel"}
								};
								Object[]  tiersParams = new Object[]{
								};
								%>
								<ct:ifhasright element="pyxis.tiers.tiers.chercher" crud="r">
								<ct:FWSelectorTag 
									name="tiersSelector" 
									
									methods="<%=tiersMethodsName%>"
									providerApplication ="pyxis"
									providerPrefix="TI"
									providerAction ="pyxis.tiers.tiers.chercher"
									providerActionParams ="<%=tiersParams %>"
								/>
								</ct:ifhasright>
							</TD>
				            <TD nowrap width="50" align="left">Affilié</TD>
				            <TD nowrap >
								<INPUT type="text" name="numAffilie" tabindex="-1" value="<%=viewBean.getNumAffilie()%>" class="libelleLongDisabled" readonly>
							</TD>
						</TR>
						<TR>
				            <TD nowrap width="140"></TD>
				            <TD nowrap >
								<INPUT type="text" name="localite" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getLocalite()%>" readonly>
            				</TD>
            				<TD nowrap align="left">NSS</TD>
            				<TD nowrap>
								<INPUT type="text" name="numAvs" tabindex="-1" value="<%=viewBean.getNumAvs()%>" class="libelleLongDisabled" readonly>
							</TD>
							<TD>
	     					</TD>
          				</TR>
	  					<TR>
				            <TD nowrap width="140">&nbsp;</TD>
				            <TD nowrap></TD>
				            <TD></TD>
				            <TD nowrap></TD>
				            <TD nowrap></TD>
						</TR>
	 					<TR>
				            <TD nowrap width="140">Année</TD>
				            <TD nowrap >
								<INPUT type="text" name="Annee" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getAnnee()%>" readonly>
				            </TD>
				            <TD nowrap align="left">N° Contribuable</TD>
				            <TD nowrap><INPUT type="text" name="numContri" tabindex="-1" value="<%=viewBean.getNumContri()%>" class="libelleLongDisabled" readonly></TD>
							<TD>
							</TD>
          				</TR>
           				<TR>
				            <TD nowrap width="140">&nbsp;</TD>
				            <TD nowrap></TD>
				            <TD></TD>
				            <TD nowrap></TD>
				            <TD nowrap></TD>
          				</TR>
						<TR>
				            <TD nowrap width="140">&nbsp;N° de communication</TD>
				            <TD nowrap><INPUT name="idCommunication" type="text" value="<%=viewBean.getIdCommunication()%>" class="numeroCourtDisabled" readonly></TD>
				            <TD nowrap>Genre</TD>
				            <TD nowrap>
				            <%
								java.util.HashSet except = new java.util.HashSet();
								except.add(globaz.phenix.db.principale.CPDecision.CS_FICHIER_CENTRAL);
								except.add(globaz.phenix.db.principale.CPDecision.CS_ETUDIANT);
								except.add(globaz.phenix.db.principale.CPDecision.CS_NON_SOUMIS);
							%>
				            <ct:FWCodeSelectTag name="genreAffilie" 
									defaut="<%=viewBean.getGenreAffilie()%>"
									wantBlank="<%=true%>"
							        codeType="CPGENDECIS"
							        except="<%=except%>"
							  />
				            </TD>
          				</TR>
          				<TR>
				            <TD nowrap width="140">&nbsp;</TD>
				            <TD nowrap></TD>
				            <TD></TD>
				            <TD nowrap></TD>
				            <TD nowrap></TD>
          				</TR>
						<TR>
				            <TD nowrap width="140">Période IFD n°</TD>
				            <TD nowrap><INPUT type="text" name="numIfd" class="numeroCourtDisabled" value="<%=viewBean.getNumIfd()%>" readonly>
				           		<INPUT type="hidden" name="idIfd" value="<%=viewBean.getIdIfd()%>">
							<%
							Object[] periodeMethodsName= new Object[]{
								new String[]{"setNumIfd","getNumIfd"},
								new String[]{"setIdIfd","getIdIfd"},
								new String[]{"setAnnee","getAnneeDecisionDebut"}
							};
							Object[]  periodeParams = new Object[]{
							};
							%>
							<ct:FWSelectorTag 
								name="decisionSelector" 
								
								methods="<%=periodeMethodsName%>"
								providerApplication ="phenix"
								providerPrefix="CP"
								providerAction ="phenix.divers.periodeFiscale.chercher"
								providerActionParams ="<%=periodeParams %>"
							/>
							</TD>
				            <TD></TD>
				  <!--          <TD nowrap>Code anomalie</TD>
				            <TD nowrap><INPUT name="codeAnomalie" type="text" value="<%=viewBean.getCodeAnomalie()%>" class="numeroCourtDisabled" style="text-align : center;" readonly></TD>
				    -->
		    	          <TD nowrap>&nbsp;</TD>
				            <TD nowrap></TD>
						</TR>
						<TR>
				            <TD nowrap width="140">&nbsp;</TD>
				            <TD nowrap></TD>
				            <TD></TD>
				            <TD nowrap></TD>
				            <TD nowrap></TD>
				        </TR>
						<TR>
				            <TD nowrap width="140">Envoyé au fisc le</TD>
				            <TD nowrap>
								<ct:FWCalendarTag name="dateEnvoi" 
									value="<%=viewBean.getDateEnvoi()%>" 
									doClientValidation="CALENDAR"
								 />
		     				</TD>
							<TD nowrap>Demande annulée</TD>
							<TD nowrap height="31"><input type="checkbox" name="demandeAnnulee" <%=(viewBean.getDemandeAnnulee().booleanValue())? "checked" : "unchecked"%>>
							&nbsp;<ct:FWCalendarTag
									name="dateEnvoiAnnulation"
									value="<%=viewBean.getDateEnvoiAnnulation()%>"
									doClientValidation="CALENDAR" />
							</TD>
	          			</TR>
          				<TR>
				            <TD nowrap width="140">&nbsp;</TD>
				            <TD nowrap></TD>
				            <TD></TD>
				            <TD nowrap></TD>
				            <TD nowrap></TD>
 						</TR>
          				<TR>
				            <TD nowrap width="140">Retourné le</TD>
				            <TD nowrap>
								<ct:FWCalendarTag name="dateRetour" 
									value="<%=viewBean.getDateRetour()%>" 
									doClientValidation="CALENDAR"
				 				/>
							</TD>
            				<TD nowrap width="140">Comptabilisé le</TD>
				            <TD nowrap><INPUT align="left" type="text" name="dateComptabilisation" value="<%=viewBean.getDateComptabilisation()%>" class="montantDisabled" style="width : 2.45cm;" readonly></TD>
            				</TR>
						    <TR>
					            <TD nowrap width="140">&nbsp;</TD>
					            <TD nowrap></TD>
					            <TD></TD>
					            <TD nowrap></TD>
					            <TD nowrap></TD>
					        </TR>
					        <TR> 
								<TD nowrap width="100">Caisse concernée</TD>
								<TD nowrap> 
									<INPUT name="codeAdministration" type="text" value="<%=viewBean.getCodeAdministration()%>" class="numeroCourt">
									<INPUT type="hidden" name="idCaisse" value="<%=viewBean.getIdCaisse()%>">
										<%
										Object[] administrationMethodsName= new Object[]{
											new String[]{"setIdCaisse","getIdTiersAdministration"},
											new String[]{"setDescriptionAdministration","getDescriptionAdministration"},
											new String[]{"setCodeAdministration","getCodeAdministration"}
										};
										Object[]  administrationParams = new Object[]{
											new String[]{"codeAdministration","_pos"},
										};
										%>
										<ct:ifhasright element="pyxis.tiers.administration.chercher" crud="r">
										<ct:FWSelectorTag 
											name="administrationSelector" 
											
											methods="<%=administrationMethodsName%>"
											providerApplication ="pyxis"
											providerPrefix="TI"
											providerAction ="pyxis.tiers.administration.chercher"
											providerActionParams ="<%=administrationParams %>"
										/>
										</ct:ifhasright>
								</TD>
								<TD nowrap>Canton&nbsp;&nbsp;</TD>
	     					<TD nowrap>
								<%
									java.util.HashSet except2 = new java.util.HashSet();
									except2.add(IConstantes.CS_LOCALITE_ETRANGER);
								%>
								<ct:FWCodeSelectTag name="canton"
								        defaut="<%=viewBean.getCanton()%>"
										wantBlank="<%=true%>"
								        codeType="PYCANTON"
										libelle="codeLibelle"
										except="<%=except2%>"
								/>
	     					</TD>
							</TR>
							<TR>
								<TD nowrap width="132"></TD>
								<TD nowrap width="269">
									<TEXTAREA rows="5" width="250" align="left" readonly class="libelleLongDisabled"><%=viewBean.getDescriptionAdministration()%>
									</TEXTAREA>
								</TD>
              					<TD nowrap>Genre de l'envoi</TD>
              					<TD nowrap><INPUT name="codeAnomalie" type="text" value="<%=viewBean.getCodeAnomalie()%>" class="numeroCourtDisabled" style="text-align : center;" readonly></TD>
							</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="CP-ImpressionDemande" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdCommunication()%>" checkAdd="no"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>