<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@page import="globaz.campus.vb.annonces.GEImputationsViewBean"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%
	idEcran ="CGE0008";
	GEImputationsViewBean viewBean = (GEImputationsViewBean)session.getAttribute ("viewBean");
	if (GEImputationsViewBean.CS_ETAT_VALIDE.equals(viewBean.getCsEtatAnnonce()) || GEImputationsViewBean.CS_ETAT_COMPTABILISE.equals(viewBean.getCsEtatAnnonce())){
		bButtonUpdate=false;
		bButtonDelete=false;
	}
	if(!viewBean.isNew() && viewBean != null 
			&& viewBean.getSession().hasRight("campus.annonces.imputations.afficher", FWSecureConstants.UPDATE)){
		bButtonNew=true;
	}
	actionNew  += "&idLot=" + viewBean.getIdLot()
	+ "&idAnnonceParent=" + viewBean.getIdAnnonceParent();
%>
<%@page import="globaz.globall.util.JAUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JACalendar"%>
<SCRIPT language="JavaScript">
</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">

function add() {
	document.forms[0].elements('userAction').value="campus.annonces.imputations.ajouter";
}

function upd() {
}

function validate() {
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="campus.annonces.imputations.ajouter";
    else
        document.forms[0].elements('userAction').value="campus.annonces.imputations.modifier";
	return (true);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="campus.annonces.imputations.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer le détail d'assurance sélectionné! Voulez-vous continuer?")) {
		document.forms[0].elements('userAction').value="campus.annonces.imputations.supprimer";
		document.forms[0].submit();
	}
}

function init() {}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Détail d'une imputation
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD width="20%">N° Lot</TD>
							<TD width="30%"><input name='idLot' class="numeroCourtDisabled" readonly 
								value='<%=!JadeStringUtil.isBlankOrZero(viewBean.getIdLot())?viewBean.getIdLot():request.getParameter("idLot")%>'></TD>
							
						</TR>
						<TR>
							<TD>Nom</TD>
							<TD><input name='nom' class='libelleLongDisabled' readonly
								value="<%=!JadeStringUtil.isNull(viewBean.getNom())?viewBean.getNom():""%>"></TD>
							<TD>N° Immatriculation</TD>
							<TD><input name='numImmatriculationTransmis' class="libelleLongDisabled" readonly
								value="<%=!JadeStringUtil.isNull(viewBean.getNumImmatriculationTransmis())?viewBean.getNumImmatriculationTransmis():""%>"></TD>
						</TR>
						<TR>
							<TD>Prénom</TD>
							<TD><input name='prenom' class='libelleLongDisabled' readonly 
								value="<%=!JadeStringUtil.isNull(viewBean.getPrenom())?viewBean.getPrenom():""%>"></TD>
							<TD>Etat civil</TD>
							<TD><input name='csEtatCivil' class='libelleLongDisabled' readonly 
								value="<%=!JadeStringUtil.isNull(viewBean.getCsEtatCivil())?viewBean.getSession().getCodeLibelle(viewBean.getCsEtatCivil()):""%>">
							</TD
						</TR>
						<TR>
							<TD>NSS</TD>
							<TD><input name='numAvs' class='libelleLongDisabled' readonly 
								value="<%=!JadeStringUtil.isNull(viewBean.getNumAvs())?viewBean.getNumAvs():""%>">
							</TD>
							<TD>Sexe</TD>
							<TD><input name='csSexe' class='libelleLongDisabled' readonly 
								value="<%=!JadeStringUtil.isNull(viewBean.getCsSexe())?viewBean.getSession().getCodeLibelle(viewBean.getCsSexe()):""%>">
							</TD>
						</TR>
						<TR>
							<TD>Date de naissance</TD>
							<TD><input name='dateNaissance' class='libelleLongDisabled' readonly 
								value="<%=!JadeStringUtil.isNull(viewBean.getDateNaissance())?viewBean.getDateNaissance():""%>">
							</TD>
						    <TD>Code doctorant</TD>
						    <TD><input name='csCodeDoctorant' class='libelleLongDisabled' readonly 
								value="<%=!JadeStringUtil.isNull(viewBean.getCsCodeDoctorant())?viewBean.getSession().getCodeLibelle(viewBean.getCsCodeDoctorant()):""%>">
							</TD>
						</TR>
						<TR><TD>&nbsp</TD></TR>
						<TR><TD nowrap  height="11" colspan="4"> <hr size="3" width="100%"></TD></TR>
						<TR><TD>&nbsp</TD></TR>
						<TR><TD nowrap colspan="4" style="font-weight : bolder;">Le calcul de l'imputation lors du processus de validation ne sera pas effectué si le revenu et la cotisation sont tous les deux renseignés. Les montants</TD></TR>
						<TR><TD nowrap colspan="4" style="font-weight : bolder;">seront repris comme indiqué. Il est obligatoire de saisir au moins l'un des deux montants (soit le revenu, soit la cotisation). Le processus de validation</TD></TR>
						<TR><TD nowrap colspan="4" style="font-weight : bolder;">calculera le montant qui n'est pas renseigné.</TD></TR>
						<TR><TD>&nbsp</TD></TR>
						<TR>
							<TD>Revenu</TD>
							<TD><input name='montantCI' class="montant" tabindex="1"
								value="<%=!JadeStringUtil.isNull(viewBean.getMontantCI())?viewBean.getMontantCI():""%>">
							</TD>
							<TD>Cotisation</TD>
							<TD><input name='cotisation' class="montant" tabindex="1"
								value="<%=!JadeStringUtil.isNull(viewBean.getCotisation())?viewBean.getCotisation():""%>">
							</TD>
						</TR>
						<TR><TD>&nbsp</TD></TR>
						<TR> <TD nowrap  height="11" colspan="4"> <hr size="3" width="100%"></TD></TR>
						<TR><TD>&nbsp</TD></TR>
						<TR>
						    <TD>Etat Annonce</TD>
							<TD><ct:FWCodeSelectTag name="csEtatAnnonce"
							    	defaut="<%=viewBean.getCsEtatAnnonce()%>"
						      		codeType="GEETAT_ANN"
						      		wantBlank="false"/>
						      	<script>
								document.getElementsByName("csEtatAnnonce")[0].tabIndex = 24;
								</script>
						    </TD>
						</TR>
						<TR><TD>&nbsp</TD></TR>
						<TR><TD nowrap  height="11" colspan="4"> <hr size="3" width="100%"></TD></TR>
						<TR><TD>&nbsp</TD></TR>
						<TR>
							<TH align="left">Messages d'erreurs</TH>
							<TD>
							&nbsp
            				<A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.chercher&forDesignationUpper1Like=<%=viewBean.getNom()%>&forDesignationUpper2Like=<%=viewBean.getPrenom()%>&forDateNaissance=<%=viewBean.getDateNaissance()%>" class="external_link">Tiers</A>
           					&nbsp
           					<A href="<%=request.getContextPath()%>\campus?userAction=campus.etudiants.etudiants.chercher&forNomLike=<%=viewBean.getNom()%>&forPrenomLike=<%=viewBean.getPrenom()%>&forNumImmatriculation=<%=viewBean.getNumImmatriculationTransmis()%>&forNumAvsLike=<%=viewBean.getNumAvs()%>">Etudiant</A>
           					&nbsp
           					<%if (!JadeStringUtil.isBlankOrZero(viewBean.getIdEtudiant())){ %>
            					<A href="<%=request.getContextPath()%>\naos?userAction=naos.affiliation.affiliation.chercher&idTiers=<%=viewBean.getIdTiers(session)%>" class="external_link">Affiliation</A>
           					<%} %>
           					<%if(!JadeStringUtil.isBlankOrZero(viewBean.getIdDecision())){ %>
           						&nbsp
            					<A href="<%=request.getContextPath()%>\naos?userAction=naos.affiliation.affiliation.rechercheDecisionCP&affiliationId=<%=viewBean.getIdAffiliation(session)%>" class="external_link">Décision</A>
           					<%} %>
           					</TD>
						</TR>
						<TR><TD>&nbsp</TD></TR>
						<TR>
						<TD colspan="4" height="99">
						<textarea rows="5" style="width: 100%"><%=viewBean.getMessageLog()%></textarea>
						<input type="hidden" name="idLog" value="<%=viewBean.getIdLog()%>">
						<input type="hidden" name="idAnnonceParent" value='<%=!JadeStringUtil.isBlankOrZero(viewBean.getIdAnnonceParent())?viewBean.getIdAnnonceParent():request.getParameter("idAnnonceParent")%>'>
						<input type="hidden" name="isImputation" value='<%=new Boolean(true)%>'>
						<INPUT type="hidden" name="selectedId" value="<%=viewBean.getIdAnnonce()%>">
						</TD>
						</TR>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<% if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> 
<SCRIPT>
</SCRIPT> 
<% } %> 
	<ct:menuChange displayId="menu" menuId="GEMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="GEMenuVide" showTab="options"></ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>