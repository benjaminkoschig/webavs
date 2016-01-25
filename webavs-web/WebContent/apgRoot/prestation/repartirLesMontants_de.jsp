<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP0042";
globaz.apg.vb.prestation.APRepartirLesMontantsViewBean viewBean = (globaz.apg.vb.prestation.APRepartirLesMontantsViewBean) session.getAttribute("viewBean");

bButtonCancel = true;
bButtonValidate = true;
bButtonDelete = false;
bButtonUpdate = true;

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">

  function validate() {
    state = true

    document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_REPARTITION_PAIEMENTS%>.miseAJourRepartitionsCotisations";
    
	if(parseFloat(toFloat(document.getElementById('diff').value)) != 0){
    	state = false;
    	errorObj.text = "La somme des répartitions doit être égale au montant brut de la prestation.";
    	showErrors();
    	errorObj.text = "";
    }
    
    return state;
  }

  function cancel() {
      document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_REPARTITION_PAIEMENTS%>.chercher";
  }

  function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_REPARTITION_PAIEMENTS%>.supprimer";
        document.forms[0].submit();
    }
  }
  	
  function init(){
	<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	  	// mise a jour de la liste du parent
		if (parent.document.forms[0]) {
			parent.document.forms[0].submit();
		}
	<%}%>
  }
  
	function compenserDiffSurRepartition(nb){
	  	// on recupere la valeur courante de la repartition a compenser
	  	var id = "val_"+nb;
	  	var oldid = "old_val_"+nb;
	  	var valeurCourante = toFloat(document.getElementById(id).value);
	  	
	  	// on recupere la valeur de la difference
	  	var difference = toFloat(document.getElementById('diff').value);
	  	var totalRepartition = toFloat(document.getElementById('totalRepartition').value);
	  	
	  	// pas de difference pas de chocolat
	  	if(parseFloat(difference) != 0){
	  		
	  		// si la diff. est positive on compense tout
	  		if(difference>0){
		  		document.getElementById(id).value = parseFloat(valeurCourante) + parseFloat(difference);
		  		document.getElementById(oldid).value = parseFloat(valeurCourante) + parseFloat(difference);
		  		document.getElementById('totalRepartition').value = parseFloat(totalRepartition) + parseFloat(difference);
		  		document.getElementById('diff').value=0;
	  		}else{
	  			// sinon on compense au maximum jusqu'a la valeur de la repartition
	  			if(valeurCourante>=Math.abs(parseFloat(difference))){
	  				document.getElementById(id).value = parseFloat(valeurCourante) + parseFloat(difference);
	  				document.getElementById(oldid).value = parseFloat(valeurCourante) + parseFloat(difference);
	  				document.getElementById('totalRepartition').value = parseFloat(totalRepartition) + parseFloat(difference);
	  				document.getElementById('diff').value=0
	  			}else{
	  				document.getElementById(id).value = 0;
	  				document.getElementById(oldid).value = 0;
	  				document.getElementById('diff').value=parseFloat(difference)+parseFloat(valeurCourante);
	  				document.getElementById('totalRepartition').value = parseFloat(totalRepartition) - parseFloat(valeurCourante);
	  			}
	  		}
	
			// validation des nombres
		  	validateFloatNumber(document.getElementById(id));
		  	validateFloatNumber(document.getElementById('diff'));
		  	validateFloatNumber(document.getElementById('totalRepartition'));
		  	
		  	// une difference qui n'est pas nulle est affichee en rouge
		  	if(parseFloat(toFloat(document.getElementById('diff').value)) != 0){
		  		document.getElementById('diff').style.color = "red";
		  	}else{
		  		document.getElementById('diff').style.color = "black";
		  	}
	  	}
	}
	
	function miseAJoursTotaux(nb){
	  	// on recupere la valeur l'ancienne valeur et la valeur courante de la repartition
	  	var id = "val_"+nb;
	  	var oldid = "old_val_"+nb;
	  	var valeurCourante = toFloat(document.getElementById(id).value);
	  	var valeurOld = toFloat(document.getElementById(oldid).value);
	  	
	  	// si l'utilisateur donne une valeur vide, on set a zero
	  	if(valeurCourante==""){
	  		valeurCourante = "0";
	  	}
	  	
	  	// on calcul la difference entre les deux valeurs
	  	var delta = parseFloat(valeurOld) - parseFloat(valeurCourante);
	  	
	  	// calcul de la difference
	  	var diff = toFloat(document.getElementById('diff').value);
	  	var nouvelleDiff = parseFloat(diff) + parseFloat(delta);
	  	
	  	// calcul du montant de la repartition
	  	var totalRepartition = toFloat(document.getElementById('totalRepartition').value);
	  	var nouveauTotalRepartition = parseFloat(totalRepartition) - parseFloat(delta);
	  	
	  	// mise a jour
	  	document.getElementById('diff').value = nouvelleDiff;
	  	document.getElementById('totalRepartition').value = nouveauTotalRepartition;
	  	document.getElementById(oldid).value = valeurCourante;
	  	
	  	// validation des nombres
	  	validateFloatNumber(document.getElementById('diff'));
	  	validateFloatNumber(document.getElementById('totalRepartition'));
	  	
	  	// une difference qui n'est pas nulle est affichee en rouge
	  	if(parseFloat(nouvelleDiff) != 0){
	  		document.getElementById('diff').style.color = "red";
	  	}else{
	  		document.getElementById('diff').style.color = "black";
	  	}
	}
	
	function toFloat(input){
		var output = input.replace("'", ""); 
		return output;
	}
  
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_REPARTITION_PAIEMENT"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD colspan="6">
								<H6><ct:FWLabel key="JSP_DROIT"/></H6>
								
								<INPUT type="hidden" name="idDroit" value="<%=viewBean.getIdDroit()%>">
								<INPUT type="hidden" name="genreService" value="<%=viewBean.getGenreService()%>">
								<INPUT type="hidden" name="idOfIdPrestationCourante" value="<%=viewBean.getIdOfIdPrestationCourante()%>">
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_NSS_ABREGE"/></TD>
							<TD><INPUT type="text" name="noAVS" value="<%=viewBean.getNoAVSAssure()%>" class="Disabled" readonly></TD>
							<TD><ct:FWLabel key="JSP_NOM"/></TD>
							<TD><INPUT type="text" name="prenomNom"  value="<%=viewBean.getNomPrenomAssure()%>" class="libelleLongDisabled" readonly></TD>
							<TD><ct:FWLabel key="JSP_DATE_DEBUT"/></TD>
							<TD><INPUT type="text" name="dateDebutDroit" value="<%=viewBean.getDateDebutDroit()%>" class="dateDisabled" readonly></TD>
						</TR>
						<TR><TD colspan="6"><HR></TD></TR>
						<TR>
							<TD colspan="6">
								<H6><ct:FWLabel key="JSP_PRESTATION"/></H6>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_PERIODE_DU"/></TD>
							<TD><INPUT type="text" name="dateDebutPrestation"  value="<%=viewBean.getDateDebutPrestation()%>" class="dateDisabled" readonly></TD>
							<TD><ct:FWLabel key="JSP_AU"/></TD>
							<TD colspan="3"><INPUT type="text" name="dateFinPrestation" value="<%=viewBean.getDateFinPrestation()%>" class="dateDisabled" readonly></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_MONTANT_BRUT"/></TD>
							<TD><INPUT type="text" name="montantBrutPrestation"  value="<%=viewBean.getMontantBrutPrestation()%>" class="montantDisabled" readonly></TD>
						</TR>
						<TR><TD colspan="6"><HR></TD></TR>
						<TR>
							<TD colspan="6">
								<H6><ct:FWLabel key="JSP_REPARTITIONS"/></H6>
							</TD>
						</TR>
						<TR>
							<TD>&nbsp;</TD>
							<TD colspan="5">
								<TABLE border="3">
									<THEAD>
										<TR>
											<TH><ct:FWLabel key="JSP_BENEFICIAIRE"/></TH>
											<TH><ct:FWLabel key="JSP_PAIEMENT_POUR"/></TH>
											<TH><ct:FWLabel key="JSP_MONTANT_BRUT"/></TH>
										</TR>
									</THEAD>
									<TBODY>
										<%
										globaz.framework.util.FWCurrency total = new globaz.framework.util.FWCurrency("0");
										globaz.framework.util.FWCurrency allocFraisGarde = new globaz.framework.util.FWCurrency("0");
										int count = 0;
										java.util.Iterator repartitions = viewBean.getRepartitions();
										while (repartitions.hasNext()) {
											count++;
											globaz.apg.db.prestation.APRepartitionJointPrestation repartition = (globaz.apg.db.prestation.APRepartitionJointPrestation) repartitions.next();
										%>
										<TR>
											<TD>
												<%=repartition.getNom()%>
											</TD>
											<TD style="text-align: center">
												<%=repartition.getPaiementPourLibelle()%>
											</TD>
											<TD style="text-align:right;">
												<INPUT type="text" name="val_<%=count%>" value="<%=repartition.getMontantBrut()%>" class="montant" onchange="javascript:miseAJoursTotaux('<%=count%>');"  onblur="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" style="border-style:solid;border-top-width:0; border-left-width:0; border-right-width:0; border-bottom-width:0;">
												<INPUT type="hidden" name="old_val_<%=count%>" value="<%=repartition.getMontantBrut()%>">
												<INPUT type="hidden" name="id_<%=count%>" value="<%=repartition.getIdRepartitionBeneficiairePaiement()%>">
											</TD>
											<TD style="border-style:solid;border-top-width:0; border-left-width:0; border-right-width:0; border-bottom-width:0;">
												<a href="javascript:compenserDiffSurRepartition('<%=count%>');"><IMG SRC="<%=request.getContextPath()+ "/images/compenser.gif"%>" alt="" border="0" title="<ct:FWLabel key="JSP_AFFECTER_LA_DIFFERENCE"/>"></a>
											</TD>
										</TR>
										<%	
												total.add(repartition.getMontantBrut());
											}

											globaz.framework.util.FWCurrency difference = new globaz.framework.util.FWCurrency(viewBean.getMontantBrutPrestation());
											difference.add(-total.floatValue());
										%>
										<TR><TD colspan="3" style="border-style:solid;border-top-width:0; border-left-width:0; border-right-width:0; border-bottom-width:0;">&nbsp;</TD></TR>
										<TR>
											<TD colspan="2" style="border-style:solid;border-top-width:0; border-left-width:0; border-right-width:0; border-bottom-width:0;">
												<ct:FWLabel key="JSP_MONTANT_BRUT_PRESTATION"/>
											</TD>
											<TD style="border-style:solid;border-top-width:0; border-left-width:0; border-right-width:0; border-bottom-width:0;text-align:right;">
												<INPUT type="text" value="<%=viewBean.getMontantBrutPrestation()%>" class="montantDisabled" readonly border="0" style="border-style:solid;border-top-width:0; border-left-width:0; border-right-width:0; border-bottom-width:0;">
											</TD>
										</TR>
										<TR>
											<TD style="border-style:solid;border-top-width:0; border-left-width:0; border-right-width:0; border-bottom-width:0;">
												<ct:FWLabel key="JSP_TOTAL_DES_REPARTITIONS"/>
											</TD>
											<TD style="text-align:right;border-style:solid;border-top-width:0; border-left-width:0; border-right-width:0; border-bottom-width:0;">
												(-)
											</TD>
											<TD style="border-style:solid;border-top-width:0; border-left-width:0; border-right-width:0; border-bottom-width:0;text-align:right;">
												<INPUT type="text" name="totalRepartition" value="<%=total.toStringFormat()%>" border="0" class="montantDisabled" readonly style="border-style:solid;border-top-width:0; border-left-width:0; border-right-width:0; border-bottom-width:0;">
											</TD>
										</TR>
										<TR>
											<TD colspan="2" style="border-style:solid;border-top-width:0; border-left-width:0; border-right-width:0; border-bottom-width:0;">
												<ct:FWLabel key="JSP_DIFFERENCE"/>
											</TD>
											<TD style="border-color:black;border-style:double;border-top-width:3; border-left-width:0; border-right-width:0; border-bottom-width:0;text-align:right;">
												<INPUT type="text" name="diff" value="<%=difference.toStringFormat()%>" class="montantDisabled" readonly border="0" style="border-style:solid;border-top-width:0; border-left-width:0; border-right-width:0; border-bottom-width:0;">
											</TD>
										</TR>
									</TBODY>
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