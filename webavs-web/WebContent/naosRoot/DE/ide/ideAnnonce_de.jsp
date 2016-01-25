<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.pyxis.adresse.formater.TIAdresseFormater"%>
<%@page import="globaz.webavs.common.ICommonConstantes"%>
<%@page import="globaz.pyxis.adresse.formater.TILocaliteLongFormater"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="globaz.pyxis.constantes.IConstantes"%>
<%@page import="globaz.naos.translation.CodeSystem"%>
<%@page import="globaz.naos.util.AFIDEUtil"%>
<%@page import="globaz.naos.db.ide.AFIdeAnnonceViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CAF0076";
	AFIdeAnnonceViewBean viewBean = (AFIdeAnnonceViewBean)session.getAttribute ("viewBean");
	bButtonDelete = viewBean.canDeleteAnnonce() && viewBean.getSession().hasRight("naos.ide.ideAnnonce.supprimer",FWSecureConstants.REMOVE);
	bButtonUpdate = viewBean.canUpdateAnnonce() && viewBean.getSession().hasRight("naos.ide.ideAnnonce.modifier",FWSecureConstants.UPDATE);
	boolean isTraite = viewBean.isTraite() ;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">


function add() {
}

function upd() {
}

function validate() {
	state = validateFields(); 
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="naos.ide.ideAnnonce.ajouter";
	else
		document.forms[0].elements('userAction').value="naos.ide.ideAnnonce.modifier";
	return (state);
}

function cancel() {
	document.forms[0].elements('userAction').value="back";
}

function del() {
	if (window.confirm("<%=objSession.getLabel("NAOS_ANNONCE_IDE_CONFIRM_SUPPRESSION_ANNONCE")%>")) {
		document.forms[0].elements('userAction').value="naos.ide.ideAnnonce.supprimer";
		document.forms[0].submit();
	}
}
function init() {
}

</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_ANNONCE_IDE_DETAIL"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
		<TR>
        	<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_NUMERO_IDE"/></TD>
        	
        	<%
        	        	    String numeroIde = viewBean.getHistNumeroIde();
        	        	        	if(JadeStringUtil.isBlankOrZero(numeroIde)){
        	        	        	    numeroIde = viewBean.getAffiliation().getNumeroIDE();
        	        	        	}
        	        	     String numeroIdeRemplacement = viewBean.getNumeroIdeRemplacement();
        	        	        	if(!JadeStringUtil.isBlankOrZero(numeroIdeRemplacement)){
        	        	        		String temp = numeroIde;
        	        	        		numeroIde = numeroIdeRemplacement;
        	        	        		numeroIdeRemplacement = temp;
        	        	        	}
        	        	%>
        	
        	<TD nowrap width="200"><INPUT name="" type="text" value="<%=AFIDEUtil.formatNumIDE(numeroIde)%>" class="libelleLongDisabled" readonly="readonly"></TD>
      		<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_STATUT_IDE"/></TD>
        	<TD nowrap width="300"><INPUT name="ideStatut" type="text" value="<%=CodeSystem.getLibelle(viewBean.getSession(),viewBean.getHistStatutIde())%>" class="libelleLongDisabled" readonly></TD>
		</TR>
		<TR>
			<TD nowrap width="140">&nbsp;</TD>
        	<TD nowrap width="300"></TD>
        	<TD nowrap width="140"></TD>
        	<TD nowrap width="300"></TD>
		</TR>
		
		
		<TR>
			<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_NUMERO_IDE_REMPLACEMENT"/></TD>
        	<TD nowrap width="300"><INPUT name="" type="text" value="<%=AFIDEUtil.giveMeNumIdeFormatedWithPrefix(numeroIdeRemplacement)%>" class="libelleLongDisabled" readonly></TD>
        	<TD nowrap width="140">&nbsp;</TD>
        	<TD nowrap width="300">&nbsp;</TD>
		</TR>
		
		<TR>
			<TD nowrap width="140">&nbsp;</TD>
        	<TD nowrap width="300"></TD>
        	<TD nowrap width="140"></TD>
        	<TD nowrap width="300"></TD>
		</TR>
		
		<TR>
        	<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_RAISON_SOCIALE"/></TD>
        	<TD nowrap width="300"><INPUT width="300" name="" type="text" value="<%=viewBean.getHistRaisonSociale()%>" class="libelleLong10Disabled" readonly></TD>
        	<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_PERSONNALITE_JURIDIQUE"/></TD>
        	<TD nowrap width="300"><INPUT name="" type="text" value="<%=viewBean.getFormeJuridique()%>" class="libelleLong10Disabled" readonly></TD>
		</TR>
		
		<TR>
			<TD nowrap width="140">&nbsp;</TD>
        	<TD nowrap width="300"></TD>
        	<TD nowrap width="140"></TD>
        	<TD nowrap width="300"></TD>
		</TR>
		
		<TR>
			<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_NUMERO_AFFILIE"/></TD>
        	<TD nowrap width="300"><INPUT name="" type="text" value="<%=AFIDEUtil.giveMeAllNumeroAffilieInAnnonceSeparatedByVirgul(viewBean)%>" class="libelleLongDisabled" readonly></TD>
        	<%if (CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI.equals(viewBean.getIdeAnnonceCategorie())) {%>
        	<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_LEGALFORM"/></TD>
        	<TD nowrap width="300"><INPUT name="" type="text" value="<%=AFIDEUtil.translatePersJuriVersLegalForm(viewBean.getAffiliation().getPersonnaliteJuridique())%>" class="libelleLong10Disabled" readonly></TD>
			<%} %>
		</TR>
		
		<TR>
			<TD nowrap width="140">&nbsp;</TD>
        	<TD nowrap width="300"></TD>
        	<TD nowrap width="140"></TD>
        	<TD nowrap width="300"></TD>
		</TR>
		<TR>
			
        	<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_BRANCHE_ECONOMIQUE"/></TD>
        	<TD nowrap width="300"><INPUT name="" type="text" value="<%=viewBean.getBrancheEconomique()%>" class="libelleLong10Disabled" readonly></TD>
        	<%if (CodeSystem.CATEGORIE_ANNONCE_IDE_ENVOI.equals(viewBean.getIdeAnnonceCategorie())) {%>
        	<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_ORGANISATION_TYPE"/></TD>
        	<TD nowrap width="300"><INPUT name="" type="text" value="<%=AFIDEUtil.translateTypeEntrepriseVersOrganisationType(viewBean.getAffiliation().getPersonnaliteJuridique())%>" class="libelleLong10Disabled" readonly></TD>
        	<%} %>
		</TR>
		
		<TR>
			<TD nowrap width="140">&nbsp;</TD>
        	<TD nowrap width="300"></TD>
        	<TD nowrap width="140"></TD>
        	<TD nowrap width="300"></TD>
		</TR>
		<TR>
			<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_LANGUE_TIERS"/></TD>
        	<TD nowrap width="300"><INPUT name="" type="text" value="<%=viewBean.getLangueTiers()%>" class="libelleLongDisabled" readonly></TD>
        	<TD nowrap width="140"></TD>
			<TD nowrap width="300">&nbsp;</TD>
		</TR>
		
		<TR>
			<TD nowrap width="140">&nbsp;</TD>
        	<TD nowrap width="300"></TD>
        	<TD nowrap width="140"></TD>
        	<TD nowrap width="300"></TD>
		</TR>
		
		<TR>
		<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_ADRESSE_RUE"/></TD>
	    <TD nowrap width="300" colspan="3"><INPUT  name="" type="text" value="<%=viewBean.getRue()%>" class="libelleLong10Disabled" readonly></TD>
	    <TD nowrap width="140">&nbsp;</TD>
	    <TD nowrap width="300"></TD>
        </TR>
		
		<TR>
			<TD nowrap width="140">&nbsp;</TD>
        	<TD nowrap width="300"></TD>
        	<TD nowrap width="140"></TD>
        	<TD nowrap width="300"></TD>
		</TR>
		
		<TR>
		<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_ADRESSE_NPA"/></TD>
	    <TD nowrap width="300" ><INPUT  name="" type="text" value="<%=viewBean.getNpa()%>" class="libelleLong10Disabled" readonly></TD>
	    <TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_ADRESSE_LOCALITE"/></TD>
	    <TD nowrap width="300" ><INPUT  name="" type="text" value="<%=viewBean.getLocalite()%>" class="libelleLong10Disabled" readonly></TD>
        </TR>
		
		<TR>
			<TD nowrap width="140">&nbsp;</TD>
        	<TD nowrap width="300"></TD>
        	<TD nowrap width="140"></TD>
        	<TD nowrap width="300"></TD>
		</TR>
		
		<TR>
			<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_CANTON"/></TD>
        	<TD nowrap width="300"><INPUT name="" type="text" value="<%=viewBean.getCanton()%>" class="libelleLongDisabled" readonly></TD>
        	<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_PAYS"/></TD>
        	<TD nowrap width="300"><INPUT name="" type="text" value="CHE" class="libelleLongDisabled" readonly></TD>
		</TR>
		
		<TR>
			<TD nowrap width="140">&nbsp;</TD>
        	<TD nowrap width="300"></TD>
        	<TD nowrap width="140"></TD>
        	<TD nowrap width="300"></TD>
		</TR>
		
		<TR> 
		<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_ETAT"/></TD>
		<% if (viewBean.canUpdateAnnonce()) { %>
			<TD nowrap width="310" > 
			<%
	     		java.util.HashSet exceptEtat = new java.util.HashSet();
				exceptEtat.add(CodeSystem.ETAT_ANNONCE_IDE_ERREUR);
				exceptEtat.add(CodeSystem.ETAT_ANNONCE_IDE_TRAITE);
				exceptEtat.add(CodeSystem.ETAT_ANNONCE_IDE_ATTENTE);
       		    %>
				<ct:FWCodeSelectTag 
	   				name="ideAnnonceEtat" 
					defaut="<%=viewBean.getIdeAnnonceEtat()%>"
					except="<%=exceptEtat%>"
					codeType="VEIDEETAAN"/>
		<%
	    } else {
		%>
			<TD>
				<INPUT type="text" name="" class="libelleLongDisabled" readonly value="<%=CodeSystem.getLibelle(viewBean.getSession(),viewBean.getIdeAnnonceEtat())%>">
			</TD>
		<% } %>		
		<TD nowrap width="140">&nbsp;</TD>
		<TD>&nbsp;</TD>
		</TR>
		
		<TR>
			<TD nowrap width="140">&nbsp;</TD>
        	<TD nowrap width="300"></TD>
        	<TD nowrap width="140"></TD>
        	<TD nowrap width="300"></TD>
		</TR>
		<TR>
			<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_MESSAGE_ERREUR_BUSINESS"/></TD>
        	<TD nowrap width="740" colspan="3"><INPUT name="" type="text" value="<%=viewBean.getMessageErreurForBusinessUser()%>" class="libelleLong20Disabled" readonly></TD>
		</TR>
		
		<TR>
			<TD nowrap width="140">&nbsp;</TD>
        	<TD nowrap width="300"></TD>
        	<TD nowrap width="140"></TD>
        	<TD nowrap width="300"></TD>
		</TR>
		<TR>
        	<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_TYPE"/></TD>
			<TD><INPUT type="text" name="" class="libelleLongDisabled" readonly  value="<%=CodeSystem.getLibelle(viewBean.getSession(),viewBean.getIdeAnnonceType())%>"></TD>
      		<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_CATEGORIE"/></TD>
			<TD><INPUT type="text" name="" class="libelleLongDisabled" readonly  value="<%=CodeSystem.getLibelle(viewBean.getSession(),viewBean.getIdeAnnonceCategorie())%>"></TD>
		</TR>
		<TR>
			<TD nowrap width="140">&nbsp;</TD>
        	<TD nowrap width="300"></TD>
        	<TD nowrap width="140"></TD>
        	<TD nowrap width="300"></TD>
		</TR>
		<TR>
        	<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_DATE_TYPE"/></TD>
			<TD><INPUT type="text" name="" class="dateDisabled" readonly  value="<%=viewBean.getTypeAnnonceDate()%>"></TD>
			<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_DATE_CREATION"/></TD>
			<TD><INPUT type="text" name="" class="dateDisabled" readonly  value="<%=viewBean.getIdeAnnonceDateCreation()%>"></TD>
		</TR>
		<TR>
			<TD nowrap width="140">&nbsp;</TD>
        	<TD nowrap width="300"></TD>
        	<TD nowrap width="140"></TD>
        	<TD nowrap width="300"></TD>
		</TR>
		<TR>
        	<TD nowrap width="140">&nbsp;</TD>
        	<TD nowrap width="300"></TD>
      		<TD nowrap width="140"><ct:FWLabel key="NAOS_JSP_IDE_ANNONCE_DATE_TRAITEMENT"/></TD>
			<TD><INPUT type="text" name="" class="dateDisabled" readonly  value="<%=viewBean.getIdeAnnonceDateTraitement()%>"></TD>
		</TR>
					
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>