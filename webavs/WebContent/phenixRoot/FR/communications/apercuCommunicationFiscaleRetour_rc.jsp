<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@page import="globaz.phenix.db.communications.CPJournalRetour"%>
<%@page import="globaz.pyxis.constantes.IConstantes"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
	bButtonNew=false;
	idEcran="CCP1016";
	// si le parametre idJournalRetour ou selectedId est renseigné --> on recherche les communications dans un passage, sinon, toutes les communications
	boolean hasJournal = false;
	String idJournal = request.getParameter("idJournalRetour");
	String allComm = request.getParameter("allComm");
	if (idJournal == null || idJournal.equals("null")) {
		idJournal = request.getParameter("selectedId");
	}
	if (!JadeStringUtil.isIntegerEmpty(idJournal)&& !"yes".equalsIgnoreCase(allComm)) {
		hasJournal = true;
	}
//	actionNew =  servletContext + mainServletPath + "?userAction=" + "phenix.principale.parametrePlausibilite.afficher&_method=add&idPlausibilite="+idPlausibilite;
	globaz.phenix.db.communications.CPJournalRetourViewBean journal = new globaz.phenix.db.communications.CPJournalRetourViewBean();
	journal.getJournalRetour(idJournal, session);
	bButtonNew = journal.canAddCommunication(bButtonNew);
	rememberSearchCriterias=true;
%>





<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT>
top.document.title = "Communications fiscales retour"
usrAction = "phenix.communications.apercuCommunicationFiscaleRetour.lister";
//detailLink = servlet+"?userAction=phenix.principale.apercuCommunicationRetour.afficher&_method=add&idJournalRetour=<%=idJournal%>";
bFind=false;

function onClickCheckBox(monAction){
	var checkboxes = top.fr_main.fr_list.document.getElementsByName("listIdRetour");

	for(var i=0; i<checkboxes.length;i++){
		if (checkboxes(i).checked && checkboxes(i).value != '') {
			var inputTag = document.createElement('input');
			inputTag.type='hidden';
			inputTag.name='listIdRetour';
			inputTag.value=checkboxes(i).value;
			document.forms[0].appendChild(inputTag);
		}		
	}	

	var oldUserAction = document.forms[0].elements.userAction.value;
	var oldTarget = document.forms[0].target;
	document.forms[0].target = "fr_main";
	document.forms[0].method = "post";
	setUserAction(monAction);
	document.forms[0].submit();
}


$(function(){
	$("#forStatus").change(function () {
		if($(this).val()==612005){
			$("#estAbandonne").attr('checked',true);
		}else if($(this).val()=='612009'){
			$("#estEnEnquete").attr('checked',true);
			$("#estAbandonne").attr('checked',false);
		}
		else if($(this).val()!=''){
			$("#estEnEnquete").attr('checked',false);
			$("#estAbandonne").attr('checked',false);
		}
	});
});

</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Communications fiscales en retour<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

		<%
		   	// display error message if needed
			if(session.getAttribute("errorMessage")!=null){
		  %>
          <TR>
            <TD nowrap colspan="6"><br><b><%=session.getAttribute("errorMessage")%></b></TD>
          </TR>
		  <%
			  // delete error message
				session.removeAttribute("errorMessage");
			}
		  %>
	<% if (hasJournal) {%>
		   <TR>
              <TD nowrap width="30%" align="left" >Journal de communications retour</TD>
              <TD nowrap width="70%" align="left" >
                <INPUT name="nomJournal" class="libelleLongDisabled" readonly value="<%=journal.getDescription() %>">
              </TD>
              <TD nowrap width="30%" align="left" >Etat du journal </TD>
              <TD nowrap width="70%" align="left" >
                <INPUT name="status" class="libelleLongDisabled" readonly value="<%=journal.getVisibleStatus() %>">
              </TD>
            <TD width="65" height="20">
				<input type="hidden" name="forIdJournalRetour" value="<%=idJournal%>">

            </TD>
 	       </TR>
	<% } %>
		<TR>
            <TD nowrap width="30%" >Recherche</TD>
            <TD nowrap width="70%" align="left" >
				<INPUT type="text" name="reqLibelle"  class="libelleLong" value="" tabindex="1" >
			</TD>
			<TD nowrap width="30%">Etat</TD>
           <TD width="70%" nowrap>
           		<%
				java.util.HashSet except = new java.util.HashSet();
				%>
               <ct:FWCodeSelectTag name="forStatus"
	      		defaut=""
	      		wantBlank="true"
	      		except="<%=except%>"
	      		libelle="libelle"
	    	    codeType="CPETCOMRET"/>
			<!--INPUT type="text" name="annee"  class="libelleLong" value=""-->
			</TD>
			<% if (hasJournal==false ||(journal!=null && journal.getCanton().equalsIgnoreCase(CPJournalRetour.CS_CANTON_SEDEX))) { %>
	     	<TD nowrap width="50" >canton</TD>
	     	<TD nowrap>
				<%
					java.util.HashSet except3 = new java.util.HashSet();
					except3.add(IConstantes.CS_LOCALITE_ETRANGER);
				%>
				<ct:FWCodeSelectTag name="likeSenderId"
				        defaut=""
						wantBlank="<%=true%>"
				        codeType="PYCANTON"
						libelle="code"
						except="<%=except3%>"
				/>
			</TD>
			<% } %>
	    </TR>
		<TR>
            <TD nowrap width="30%" >Critère</TD>
            <TD nowrap width="70%" align="left" >
				<SELECT name="critere"    class="libelleLong" >
					<OPTION selected="selected" value='CRITERE_AFFILIE'>Affilié</OPTION>
					<OPTION value='CRITERE_CONTRIBUABLE'>Contribuable</OPTION>
					<OPTION value='CRITERE_NOM'>Nom</OPTION>
					<OPTION value='CRITERE_PRENOM'>Prénom</OPTION>
					<OPTION value='CRITERE_ANNEE'>Année</OPTION>
				</SELECT>

			</TD>
			<TD nowrap width="30%" >Trié par</TD>
            <TD nowrap width="70%" >
				<SELECT name="trierPar"    class="libelleLong" >
					<OPTION selected="selected" value='ORDER_BY_AFFILIE'>Affilié</OPTION>
					<OPTION value='ORDER_BY_CONTRIBUABLE'>Contribuable</OPTION>
					<OPTION value='ORDER_BY_NOM_PRENOM'>Nom, Prénom</OPTION>
					<OPTION value='ORDER_BY_ANNEE'>Année</OPTION>
					<OPTION value='ORDER_BY_ETAT' >Etat</OPTION>
				</SELECT>
				<%--INPUT type="hidden" name="colonneSelection" value="<%=request.getParameter("colonneSelection")%>"--%>
			</TD>
		</TR>
		<TR>
            <TD nowrap width="30%" >Genre</TD>
            <TD nowrap width="70%" align="left" >
            
             <%
				java.util.HashSet except2 = new java.util.HashSet();
				except.add(globaz.phenix.db.principale.CPDecision.CS_FICHIER_CENTRAL);
				except.add(globaz.phenix.db.principale.CPDecision.CS_ETUDIANT);
				except.add(globaz.phenix.db.principale.CPDecision.CS_NON_SOUMIS);
				except.add(globaz.phenix.db.principale.CPDecision.CS_RENTIER);
			%>
			<ct:FWCodeSelectTag 
				name="forGenreAffilie"
				defaut=""
				wantBlank="<%=true%>"
				codeType="CPGENDECIS"
				 except="<%=except2%>"/>
			</TD>
    		<TD>Type Message</TD>
    		<TD>
    		<SELECT name="forReportType"    class="libelleLong" >
					<OPTION selected="selected" value=''></OPTION>
					<OPTION value='1'>Communication fiscale normale</OPTION>
					<OPTION value='2'>Message supplémentaire</OPTION>
					<OPTION value='4'>Rectification</OPTION>
					<OPTION value='8'>Message immédiat</OPTION>
					<OPTION value='16'>Taxation d'office</OPTION>
			</SELECT>
			</TD>
		</TR>
		<TR>
			<TD nowrap width="80">Plausibilités</TD>
			<TD colspan="3">
				<%
        		java.util.Vector tmp = globaz.phenix.db.communications.CPValidationCalculCommunication.getListPlausibilites(session,"", journal.getCanton());
				%>
				<ct:FWListSelectTag name="forIdPlausibilite"
						defaut=""
	            		data="<%=tmp%>"/>
			</TD>
			<TD nowrap width="30%" >Abandonné&nbsp;&nbsp;&nbsp;<input type="checkbox" name="estAbandonne" id="estAbandonne"/></TD>
            <TD nowrap width="70%" align="left" >&nbsp;&nbsp;&nbsp;Enquête&nbsp;&nbsp;&nbsp;<input type="checkbox" name="estEnEnquete" id="estEnEnquete"/></TD>
		</TR>

<%-- /tpl:put --%>
<%-- On remplace l'include file="/theme/find/bodyButtons.jspf" par son code pour pouvoir afficher des boutons a gauche --%>
						<TR>
							<TD height="20">
								<INPUT type="hidden" name="userAction" value="">
								<INPUT type="hidden" name="_sl" value="">
								<INPUT type="hidden" name="_method" value="">
								<INPUT type="hidden" name="_valid" value="">
								<INPUT type="hidden" name="colonneSelection" value="<%=request.getParameter("colonneSelection")%>">
							</TD>
						</TR>
					</TBODY>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD bgcolor="#FFFFFF" colspan="2">
				<TABLE width="100%" cellspacing="0" cellpadding="0">
					<TBODY>
						<TR>
							<TD align="left">
								<ct:ifhasright element="phenix.communications.apercuCommunicationFiscaleRetour.afficherEnqueterEnMasse" crud="u">
								<INPUT type="button" name="btnEnquete" value="Enquête" onClick="onClickCheckBox('phenix.communications.apercuCommunicationFiscaleRetour.afficherEnqueterEnMasse');" >
								</ct:ifhasright>	
								<ct:ifhasright element="phenix.communications.apercuCommunicationFiscaleRetour.afficherAbandonnerEnMasse" crud="u">
								<INPUT type="button" name="btnAbandon" value="Abandonner" onClick="onClickCheckBox('phenix.communications.apercuCommunicationFiscaleRetour.afficherAbandonnerEnMasse');" >	
								</ct:ifhasright>	
								<ct:ifhasright element="phenix.communications.apercuCommunicationFiscaleRetour.afficherGenererEnMasse" crud="u">
								<INPUT type="button" name="btnGenerer" value="Générer" onClick="onClickCheckBox('phenix.communications.apercuCommunicationFiscaleRetour.afficherGenererEnMasse');" >	
								</ct:ifhasright>	
								<ct:ifhasright element="phenix.communications.apercuCommunicationFiscaleRetour.afficherReinitialiserEnMasse" crud="u">
								<INPUT type="button" name="btnReinitialiser" value="Réinitialiser" onClick="onClickCheckBox('phenix.communications.apercuCommunicationFiscaleRetour.afficherReinitialiserEnMasse');" >	
								</ct:ifhasright>&nbsp;	
							</TD>
							<TD align="right">
								<%if (bShowExportButton) {%>
									<INPUT type="button" name="btnExport" value="<%=btnExportLabel%>" onClick="onExport();">
								<%}%>
								<%if (bButtonFind) {%>
									<INPUT type="submit" name="btnFind" value="<%=btnFindLabel%>">
								<%} if (bButtonNew) {%>
									<INPUT type="button" name="btnNew" value="<%=btnNewLabel%>" onClick="onClickNew();btnNew.onclick='';document.location.href='<%=actionNew%>'">
								<%}%>
							</TD>
						</TR>
					</TBODY>
				</TABLE>	
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<div style="color:red">Il y a <span id="listCount">0</span> communication(s) sélectionnée(s) pour l'action à exécuter</div>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>