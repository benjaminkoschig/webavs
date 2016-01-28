<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
// Les labels de cette page commence par la préfix "JSP_RCR_D"

idEcran="PRE0004";
globaz.corvus.vb.creances.RERepartirLesCreancesViewBean viewBean = (globaz.corvus.vb.creances.RERepartirLesCreancesViewBean) session.getAttribute("viewBean");

bButtonCancel = viewBean.isModifiable();
bButtonValidate = viewBean.isModifiable();
bButtonDelete = false;
bButtonUpdate = false;


int nbColonnes = viewBean.getNombreCreanciers() + 3;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<script language="JavaScript">

	function init(){
		<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
		  	// mise a jour de la liste du parent
			if (parent.document.forms[0]) {
				parent.document.forms[0].submit();
			}
		<%}%>
	}

	function cancel() {
		document.forms[0].elements('userAction').value="<%=IREActions.ACTION_CREANCIER%>.chercher";
	}

	function validate() {
	    state = true
	    document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_CREANCIER%>.miseAJourCreancesAccordees";
	    
	    // pas de differences negatives
	    <%for(int cr = 0; cr < viewBean.getNombreCreanciers(); cr++){%>
	    
		  	// recuperation de la difference
		  	var idDiff = "df"+"<%=cr%>";
		  	var diff = toFloat(document.getElementById(idDiff).value);
	    	
	    	if(diff<0){
	    		state = false;
	    		
	    		errorObj.text = "<ct:FWLabel key='ERREUR_MONTANT_REP_PLUS_GRAND_MONT_REV'/>";
	    		showErrors();
	    		errorObj.text = "";
	    	}
	    <%}%>
	    
	    
	    // pas de solde disponible negatif
	    <%for(int ra = 0; ra < viewBean.getNombreRentesAccordees(); ra++){%>
	    
		  	// recuperation de la difference
		  	var idDiff = "dsp"+"<%=ra%>";
		  	var diff = toFloat(document.getElementById(idDiff).value);
	    	
	    	if(diff<0){
	    		state = false;
	    		
	    		errorObj.text = "<ct:FWLabel key='ERREUR_SOLDE_DISPON_NEGATIF'/>";
	    		showErrors();
	    		errorObj.text = "";
	    	}
	    <%}%>
	    
		return state;
	}

	function toFloat(input){
		var output = input.replace("'", "");
		return output;
	}

	function miseAJoursDifferenceEtDisponible(r, m){
	  	// on recupere la valeur l'ancienne valeur et la valeur courante
	  	// de la creance accordee
	  	var id = "r"+r+"m"+m;
	  	var oldid = "old_r"+r+"m"+m;
	  	var valeurCourante = toFloat(document.getElementById(id).value);
	  	var valeurOld = toFloat(document.getElementById(oldid).value);

	  	// si l'utilisateur donne une valeur vide, on set a zero
	  	if(valeurCourante==""){
	  		valeurCourante = "0";
	  	}

	  	// on calcul la difference entre les deux valeurs
	  	var delta = parseFloat(valeurCourante) - parseFloat(valeurOld);

	  	// calcul de la difference
	  	var idDiff = "df"+m;
	  	var diff = toFloat(document.getElementById(idDiff).value);
	  	var nouvelleDiff = parseFloat(diff) - parseFloat(delta);

	  	// calcul du montant disponible
	  	var idDisp = "dsp"+r;
	  	var disp = toFloat(document.getElementById(idDisp).value);
	  	var nouvelleDisp = parseFloat(disp) - parseFloat(delta);

	  	// calcul du montant disponible total
	  	var dispTotal = toFloat(document.getElementById("mdt").value);
	  	var nouvelleDispTotal = parseFloat(dispTotal) - parseFloat(delta);

	  	// calcul du montant reparti pour le creancier
	  	var idMontRep = "tmr"+m;
	  	var montRep = toFloat(document.getElementById(idMontRep).value);
	  	var nouvelleMontRep = parseFloat(montRep) + parseFloat(delta);

	  	// mise a jour
	  	document.getElementById(idDiff).value = nouvelleDiff;
	  	document.getElementById(idDisp).value = nouvelleDisp;
	  	document.getElementById(oldid).value = valeurCourante;
	  	document.getElementById("mdt").value = nouvelleDispTotal;
	  	document.getElementById(idMontRep).value = nouvelleMontRep;

	  	// validation des nombres
	  	validateFloatNumber(document.getElementById(idDiff));
	  	validateFloatNumber(document.getElementById(idDisp));
	  	validateFloatNumber(document.getElementById("mdt"));
	  	validateFloatNumber(document.getElementById(idMontRep));

	  	// une difference qui n'est pas nulle est affichee en rouge
	  	if(parseFloat(nouvelleDiff) != 0){
	  		document.getElementById(idDiff).style.color = "red";
	  	}else{
	  		document.getElementById(idDiff).style.color = "black";
	  	}

	  	// un montant disp negatif est affiche en rouge
	  	if(parseFloat(nouvelleDisp) < 0){
	  		document.getElementById(idDisp).style.color = "red";
	  	}else{
	  		document.getElementById(idDisp).style.color = "black";
	  	}
	}

	function compenserDiffSurRepartition(r, m){
	  	// on recupere la valeur courante de la creance accordee a compenser
	  	var id = "r"+r+"m"+m;
	  	var oldid = "old_r"+r+"m"+m;
	  	var valeurCourante = toFloat(document.getElementById(id).value);

	  	// on recupere la valeur de la difference
	  	var idDiff = "df"+m;
	  	var diff = toFloat(document.getElementById(idDiff).value);

	  	// le disponible
	  	var idDisp = "dsp"+r;
	  	var disp = toFloat(document.getElementById(idDisp).value);

	  	// le disponible total
	  	var dispTotal = toFloat(document.getElementById("mdt").value);

	  	// le montant reparti pour le creancier
	  	var idMontRep = "tmr"+m;
	  	var montRep = toFloat(document.getElementById(idMontRep).value);

	  	// pas de difference pas de chocolat
	  	if(parseFloat(diff) != 0){

	  		// si la diff. est negative on compense tout
	  		if(diff<0){
		  		document.getElementById(id).value = parseFloat(valeurCourante) + parseFloat(diff);
		  		document.getElementById(oldid).value = parseFloat(valeurCourante) + parseFloat(diff);
		  		document.getElementById(idDisp).value = parseFloat(disp) - parseFloat(diff);
		  		document.getElementById("mdt").value = parseFloat(dispTotal) - parseFloat(diff);
		  		document.getElementById(idDiff).value = parseFloat(diff) - parseFloat(diff);
		  		document.getElementById(idMontRep).value = parseFloat(montRep) + parseFloat(diff);

	  		}else{
	  			// sinon on compense au maximum du montant disponible
	  			if(parseFloat(disp)>=Math.abs(parseFloat(diff))){
	  				document.getElementById(id).value = parseFloat(valeurCourante) + parseFloat(diff);
	  				document.getElementById(oldid).value = parseFloat(valeurCourante) + parseFloat(diff);
	  				document.getElementById(idDiff).value = parseFloat(diff) - parseFloat(diff);
	  				document.getElementById(idDisp).value = parseFloat(disp) - parseFloat(diff);
	  				document.getElementById("mdt").value = parseFloat(dispTotal) - parseFloat(diff);
	  				document.getElementById(idMontRep).value = parseFloat(montRep) + parseFloat(diff);
	  			}else{
	  				document.getElementById(id).value = parseFloat(valeurCourante) + parseFloat(disp);
	  				document.getElementById(oldid).value = parseFloat(valeurCourante) + parseFloat(disp);
	  				document.getElementById(idDiff).value=parseFloat(diff) - parseFloat(disp);
	  				document.getElementById(idDisp).value = parseFloat(disp) - parseFloat(disp);
	  				document.getElementById("mdt").value = parseFloat(dispTotal) - parseFloat(disp);
	  				document.getElementById(idMontRep).value=parseFloat(montRep) + parseFloat(disp);
	  			}
	  		}

			// validation des nombres
		  	validateFloatNumber(document.getElementById(id));
		  	validateFloatNumber(document.getElementById(idDiff));
		  	validateFloatNumber(document.getElementById(idDisp));
		  	validateFloatNumber(document.getElementById("mdt"));
		  	validateFloatNumber(document.getElementById(idMontRep));

		  	// une difference qui n'est pas nulle est affichee en rouge
		  	if(parseFloat(document.getElementById(idDiff).value) != 0){
		  		document.getElementById(idDiff).style.color = "red";
		  	}else{
		  		document.getElementById(idDiff).style.color = "black";
		  	}

		  	// un montant disp negatif est affiche en rouge
		  	if(parseFloat(document.getElementById(idDisp).value) < 0){
		  		document.getElementById(idDisp).style.color = "red";
		  	}else{
		  		document.getElementById(idDisp).style.color = "black";
		  	}
	  	}
	}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RCR_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><H5><ct:FWLabel key="JSP_RCR_D_REQUERANT"/></H5></TD>
							<TD colspan="<%=nbColonnes-1%>">
								<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>">
								<INPUT type="hidden" name="idTierRequerant" value="<%=viewBean.getIdTiers()%>">
								<re:PRDisplayRequerantInfoTag
										session="<%=(globaz.globall.db.BSession)controller.getSession()%>"
										idTiers="<%=viewBean.getIdTiers()%>"
										style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>"/>
							</TD>
						</TR>
						<TR>
							<TD><LABEL for="idDemandeRente"><ct:FWLabel key="JSP_CRE_R_DEMANDE_NO"/></LABEL></TD>
							<TD  colspan="<%=nbColonnes-1%>">
								<INPUT type="hidden" name="noDemandeRente" value="<%=viewBean.getIdDemandeRente()%>">
								<INPUT type="text" name="idDemandeRente" value="<%=viewBean.getIdDemandeRente()%>" class="disabled" readonly>
							</TD>
						</TR>
						<TR>
							<TD  colspan="<%=nbColonnes%>">
								<TABLE  border="0" cellpadding="6" bordercolor="black">


									<TR><TD colspan="<%=nbColonnes%>"><HR></TD></TR>
									<TR>
										<TD colspan="<%=nbColonnes%>">
											<H5><ct:FWLabel key="JSP_RCR_D_RENTES_ACCORDEES"/></H5>
										</TD>
									</TR>
									<TR>
										<TD>&nbsp;</TD>
										<TD align="right" valign="bottom"><H6><ct:FWLabel key="JSP_RCR_D_MONTANT_RETROACTIF"/></H6></TD>
										<%for(int i = 0; i < viewBean.getNombreCreanciers(); i++){
											globaz.corvus.vb.creances.RECreancierViewBean cr =(globaz.corvus.vb.creances.RECreancierViewBean)viewBean.getCreancier(i);
										%>
											<TD align="right" valign="bottom">
												<H6>
													<%=cr.getIsBloque().booleanValue()?"<Font color=\"red\">Bloqué</Font><BR>":""%>
													<%=cr.getCsTypeLibelle() + "<BR>"%>
													<%=(globaz.corvus.api.creances.IRECreancier.CS_IMPOT_SOURCE.equals(cr.getCsType())? cr.getSession().getCodeLibelle(globaz.corvus.api.creances.IRECreancier.CS_IMPOT_SOURCE) :cr.getTiersNomPrenom()) + "<BR>"%>
													<%=cr.getDateDebutPerRev() + " - " + cr.getDateFinPerRev()%>
											    </H6>
											</TD>
										<%}%>
										<TD align="right" valign="bottom"><H6><ct:FWLabel key="JSP_RCR_D_DISPONIBLE"/></H6></TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_RCR_D_MONTANT_REVENDIQUE"/></TD>
										<TD>&nbsp;</TD>
										<%for(int i = 0; i < viewBean.getNombreCreanciers(); i++){
											globaz.corvus.vb.creances.RECreancierViewBean cr =(globaz.corvus.vb.creances.RECreancierViewBean)viewBean.getCreancier(i);
										%>
											<TD align="right">
												<TABLE  border="0" bordercolor="red" cellpadding="0">
													<TR>
														<TD>
															<INPUT type="text" name="m<%=i%>" value="<%=new globaz.framework.util.FWCurrency(cr.getMontantRevandique()).toStringFormat()%>" class="montantCourtDisabled" readonly border="0" style="border-style:solid;border-top-width:0; border-left-width:0; border-right-width:0; border-bottom-width:0;">
														</TD>
														<TD width="10mm">&nbsp;</TD>
													</TR>
												</TABLE>
											</TD>
										<%}%>
										<TD>&nbsp;</TD>
									</TR>
									<TR><TD colspan="<%=nbColonnes%>"><HR></TD></TR>
									<TR>
										<TD colspan="<%=nbColonnes%>">
											<H5><ct:FWLabel key="JSP_RCR_D_REPARTITIONS"/></H5>
										</TD>
									</TR>
									<%for(int r = 0; r < viewBean.getNombreRentesAccordees(); r++){
										globaz.corvus.vb.rentesaccordees.RERenteAccordeeViewBean ra =(globaz.corvus.vb.rentesaccordees.RERenteAccordeeViewBean)viewBean.getRenteAccordee(r);
										globaz.prestation.interfaces.tiers.PRTiersWrapper tiersBenef = globaz.prestation.interfaces.tiers.PRTiersHelper.getTiersParId(viewBean.getSession(),ra.getIdTiersBeneficiaire());
									%>
										<TR>
											<TD><%=globaz.prestation.tools.nnss.PRNSSUtil.formatDetailRequerantListe(tiersBenef.getProperty(globaz.prestation.interfaces.tiers.PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL),
											                                                                         tiersBenef.getProperty(globaz.prestation.interfaces.tiers.PRTiersWrapper.PROPERTY_NOM) + " " +
											                                                                         tiersBenef.getProperty(globaz.prestation.interfaces.tiers.PRTiersWrapper.PROPERTY_PRENOM),
											                                                                         tiersBenef.getProperty(globaz.prestation.interfaces.tiers.PRTiersWrapper.PROPERTY_DATE_NAISSANCE),
											                                                                         viewBean.getSession().getCodeLibelle(tiersBenef.getProperty(globaz.prestation.interfaces.tiers.PRTiersWrapper.PROPERTY_SEXE))) + "<BR>"%>
												<%=ra.getDateDebutDroit() + " - " + ra.getDateFinDroit()%>
											</TD>
											<TD>
												<INPUT type="text" name="r<%=r%>" value="<%=new globaz.framework.util.FWCurrency(ra.getMontantRetroactif()).toStringFormat()%>" class="montantCourtDisabled" readonly border="0" style="border-style:solid;border-top-width:0; border-left-width:0; border-right-width:0; border-bottom-width:0;">
											</TD>
											<%for(int i = 0; i < viewBean.getNombreCreanciers(); i++){
												globaz.corvus.vb.creances.RECreancierViewBean cr =(globaz.corvus.vb.creances.RECreancierViewBean)viewBean.getCreancier(i);
											%>
												<TD align="right" valign="middle">
													<TABLE  border="0" bordercolor="red" cellpadding="0">
														<TR>
															<TD>
																<INPUT type="hidden" name="old_r<%=r%>m<%=i%>" value="<%=viewBean.getCreanceAccordee(r, i).getMontant()%>" disabled="disabled">
																<INPUT type="text" name="r<%=r%>m<%=i%>" value="<%=new globaz.framework.util.FWCurrency(viewBean.getCreanceAccordee(r, i).getMontant()).toStringFormat()%>" class='<%=viewBean.isModifiable()?"montantCourt":"montantCourtDisabled" %>' onchange="javascript:miseAJoursDifferenceEtDisponible('<%=r%>', '<%=i%>');"  onblur="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" <%=viewBean.isModifiable()?"":"readonly='readonly'" %>>														</TD>
															<TD width="10mm">
																<a href="javascript:compenserDiffSurRepartition('<%=r%>', '<%=i%>');"><IMG SRC="<%=request.getContextPath()+ "/images/compenser.gif"%>" alt="" border="0" title="<ct:FWLabel key="JSP_AFFECTER_LA_DIFFERENCE"/>"></a>
															</TD>
														</TR>
													</TABLE>
												</TD>
											<%}%>
											<TD align="right">
												<INPUT type="text" name="dsp<%=r%>" value="<%=viewBean.getMontantDisponible(r)%>" class="montantCourtDisabled" readonly border="0" style="border-style:solid;border-top-width:0; border-left-width:0; border-right-width:0; border-bottom-width:0;<%=new globaz.framework.util.FWCurrency(viewBean.getMontantDisponible(r)).floatValue()<0?"color=red;":"color=black;"%>">
											</TD>
										</TR>
									<%}%>
									<TR>
										<TD>&nbsp;</TD>
										<TD>
											<INPUT type="text" name="mrt" value="<%=viewBean.getMontantRetroactifTotal()%>" class="montantCourtDisabled" readonly border="0" style="border-style:double;border-top-width:3; border-left-width:0; border-right-width:0; border-bottom-width:0;">
										</TD>
										<%for(int d = 0; d < viewBean.getNombreCreanciers(); d++){%>
											<TD align="right">
												<TABLE  border="0" bordercolor="red" cellpadding="0">
													<TR>
														<TD>
															<INPUT type="text" name="tmr<%=d%>" value="<%=new FWCurrency(viewBean.getTotalMontantReparti(d)).toStringFormat()%>" class="montantCourtDisabled" readonly="readonly" border="0" style="border-style:double;border-top-width:3; border-left-width:0; border-right-width:0; border-bottom-width:0;">
														</TD>
														<TD width="10mm">&nbsp;</TD>
													</TR>
												</TABLE>
											</TD>
										<%}%>
										<TD>
											<INPUT type="text" name="mdt" value="<%=new FWCurrency(viewBean.getTotalMontantsDeisponibles()).toStringFormat()%>" class="montantCourtDisabled" readonly border="0" style="border-style:double;border-top-width:3; border-left-width:0; border-right-width:0; border-bottom-width:0;">
										</TD>
									</TR>
									<TR><TD colspan="<%=nbColonnes%>"><HR></TD></TR>
									<TR>
										<TD colspan="<%=2%>">
											<H5><ct:FWLabel key="JSP_RCR_D_DIFFERENCES"/></H5>
										</TD>
										<%for(int d = 0; d < viewBean.getNombreCreanciers(); d++){%>
											<TD valign="bottom" align="right">
												<TABLE  border="0" bordercolor="red" cellpadding="0">
													<TR>
														<TD>
															<INPUT type="text" name="df<%=d%>" value="<%=viewBean.getDifference(d)%>" class="montantCourtDisabled" readonly="readonly" border="0" style="border-style:solid;border-top-width:0; border-left-width:0; border-right-width:0; border-bottom-width:0;<%=new globaz.framework.util.FWCurrency(viewBean.getDifference(d)).floatValue()!=0?"color=red;":"color=black;"%>">
														</TD>
														<TD width="10mm">&nbsp;</TD>
													</TR>
												</TABLE
											</TD>
										<%}%>
										<TD>&nbsp;</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>