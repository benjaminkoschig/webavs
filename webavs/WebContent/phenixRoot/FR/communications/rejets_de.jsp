<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.phenix.db.communications.CPRejetsViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
	<%
	idEcran="CCP1038";
	CPRejetsViewBean viewBean = (CPRejetsViewBean)session.getAttribute ("viewBean");
	bButtonDelete = false;
	bButtonUpdate = false;
	%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
	function add() {
	}
	function upd() {
	}
	function validate() {
	}
	function cancel() {
	}
	function del() {
	}
	function init(){}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Rejet<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<TR>
				            <TD nowrap width="140">Sujet</TD>
				            <TD COLSPAN="5">
								<TABLE BORDER="2">
										<TBODY>
											<TR>
												<TD width="1000"><%=viewBean.getSubject()%></TD>
											</TR>
										</TBODY>
								</TABLE>
							</TD>
						</TR>
						<TR>
				            <TD nowrap >&nbsp;</TD>
				             <TD nowrap >&nbsp;</TD>
				              <TD nowrap >&nbsp;</TD>
				               <TD nowrap >&nbsp;</TD>
						</TR>
						<TR>
				            <TD nowrap width="140">SenderId</TD>
				            <TD nowrap >
				            	<INPUT type="text" tabindex="-1" value="<%=viewBean.getSenderId()%>" class="libelleLongDisabled" readonly>
				            </TD>
				            <TD nowrap width="140">Message id</TD>
				            <TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getMessageId()%>" class="libelleLongDisabled" readonly>
				            </TD>
						</TR>
						<TR>
				            <TD nowrap width="140">Référence</TD>
				            <TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getNomReference()%>" class="libelleLongDisabled" readonly>
				            </TD>
				            <TD nowrap width="140">Référence Message id</TD>
				            <TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getReferenceMessageId()%>" class="libelleLongDisabled" readonly>
				            </TD>
						</TR>
						<TR>
						<TD nowrap >&nbsp;</TD>
						<TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getDepartementReference()%>" class="libelleLongDisabled" readonly>
				        </TD>
				        <TD nowrap width="140">Référence métier (FISC)</TD>
				            <TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getOurBusinessReferenceId()%>" class="libelleLongDisabled" readonly>
				            </TD>
				        </TR>
				        <TR>
				        <TD nowrap >&nbsp;</TD>
						<TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getTelephoneReference()%>" class="libelleLongDisabled" readonly>
				        </TD>
				        <TD nowrap width="140">Référence métier (CAISSE)</TD>
				            <TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getYourBusinessReferenceId()%>" class="libelleLongDisabled" readonly>
				            </TD>
				        </TR>
				        <TR>
				        <TD nowrap >&nbsp;</TD>
						<TD nowrap >
				            	<INPUT type="text"  tabindex="-1" data-g-string="mandatory:true" value="<%=viewBean.getEmailReference()%>" class="libelleLongDisabled" readonly>
				        </TD>
				         <TD nowrap >&nbsp;</TD>
						<TD nowrap ></TD>
				        </TR>
				       <TR>
				            <TD nowrap width="140">Personne id catégorie</TD>
				            <TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getPersonIdCategory()%>" class="libelleLongDisabled" readonly>
				            </TD>
				              <TD nowrap width="140">Date du message</TD>
				            <TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getDateMessage()%>" class="libelleLongDisabled" readonly>
				            </TD>
						</TR>
						<TR>
				            <TD nowrap width="140">Personne id</TD>
				            <TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getPersonId()%>" class="libelleLongDisabled" readonly>
				            </TD>
				              <TD nowrap width="140">Date initiale du message</TD>
				            <TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getDateInitialeMessage()%>" class="libelleLongDisabled" readonly>
				            </TD>
						</TR>
						<TR>
				            <TD nowrap >&nbsp;</TD>
				             <TD nowrap >&nbsp;</TD>
				              <TD nowrap >&nbsp;</TD>
				               <TD nowrap >&nbsp;</TD>
						</TR>
						<TR>
				            <TD nowrap width="140">Nom</TD>
				            <TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getNom()%>" class="libelleLongDisabled" readonly>
				            </TD>
				            <TD nowrap width="140">Adresse</TD>
				            <TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getAdresse1()%>" class="libelleLongDisabled" readonly>
				            </TD>
						</TR>
						<TR>
				            <TD nowrap width="140">Prénom</TD>
				            <TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getPrenom()%>" class="libelleLongDisabled" readonly>
				            </TD>
				            <TD nowrap >&nbsp;</TD>
						<TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getAdresse2()%>" class="libelleLongDisabled" readonly>
				        </TD>
						</TR>
						<TR>
				            <TD nowrap width="140">Sexe</TD>
				            <TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getSexLibelle()%>" class="libelleLongDisabled" readonly>
				            </TD>
				            <TD nowrap >&nbsp;</TD>
						<TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getRue()%>" class="libelleLongDisabled" readonly>
				        </TD>
						</TR>
						<TR>
						 <TD nowrap width="140">Etat civil</TD>
				            <TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getEtatCivilLibelle()%>" class="libelleLongDisabled" readonly>
				            </TD>
				            
				            <TD nowrap >&nbsp;</TD>
						<TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getLocalite()%>" class="libelleLongDisabled" readonly>
				        </TD>
						</TR>
					     <TR>
					     <TD nowrap width="140">Date de naissance</TD>
				            <TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getDateNaissance()%>" class="libelleLongDisabled" readonly>
				            </TD>
				         <TD nowrap >&nbsp;</TD>
						<TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getVille()%>" class="libelleLongDisabled" readonly>
				        </TD>
				        </TR>
				        <TR>
				            <TD nowrap >&nbsp;</TD>
				             <TD nowrap >&nbsp;</TD>
				              <TD nowrap >&nbsp;</TD>
				               <TD nowrap >&nbsp;</TD>
						</TR>
						<TR>
				            <TD nowrap width="140">Raison du rejet</TD>
				            <TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getRejetVisible()%>" class="libelleLongDisabled" readonly>
				            </TD>
						</TR>
						<TR>
				            <TD nowrap width="140">Remarque</TD>
				            <TD COLSPAN="5">
								<TABLE BORDER="2">
										<TBODY>
											<TR>
												<TD width="1000"><%=viewBean.getRemark()%></TD>
											</TR>
										</TBODY>
								</TABLE>
							</TD>
						</TR>
						<TR>
				            <TD nowrap width="140">Etat</TD>
				            <TD nowrap >
				            	<INPUT type="text"  tabindex="-1" value="<%=viewBean.getVisibleStatus()%>" class="libelleLongDisabled" readonly>
				            </TD>
						</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>