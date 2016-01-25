<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CAF0069";
	rememberSearchCriterias = true;
	
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "naos.taxeCo2.taxeCo2.lister";
	bFind = true;
</SCRIPT>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					CO2-Abgabe
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
            				<TD nowrap width="100" height="31">Ab </TD>
							<TD nowrap width="180">
								<ct:FWPopupList name="fromNumAffilie" 
									value="" 
									className="libelle" 
									jspName="<%=jspLocation%>" 
									autoNbrDigit="<%=autoDigiAff%>" 
									size="15"
									minNbrDigit="3"
								/>
								<IMG
									src="<%=servletContext%>/images/down.gif"
									alt="presser sur la touche 'flèche bas' pour effectuer une recherche"
									title="presser sur la touche 'flèche bas' pour effectuer une recherche"
									onclick="if (document.forms[0].elements('forAffiliationId').value != '') forAffilieNumeroPopupTag.validate();">
							</TD>
				            <TD nowrap width="100" title="Jahr ab dem die Lohnsumme angerechnet wurde">Jahr der Lohnsumme</TD>
				            <TD nowrap width="220" title="Jahr ab dem die Lohnsumme angerechnet wurde">
				              <input type="text" name="forAnneeMasse" size="10" maxlength="4" value=""/>
				            </TD>
	            			<TD nowrap width="100" title="Jahr in dem die Lohnsumme ausbezahlt wird">Auszahlungsjahr</TD>
	            			<TD nowrap width="220"  title="Jahr in dem die Lohnsumme ausbezahlt wird"> 
								<input name="forAnneeRedistri" type="text" size="10" value="">
							</TD>
          				</TR>
          				<TR>
          					<TD nowrap width="100" height="31">Für </TD>
							<TD nowrap width="180">
								<ct:FWPopupList name="forNumAffilie" 
									value="" 
									className="libelle" 
									jspName="<%=jspLocation%>" 
									autoNbrDigit="<%=autoDigiAff%>" 
									size="15"
									minNbrDigit="3"
								/>
								<IMG
									src="<%=servletContext%>/images/down.gif"
									alt="presser sur la touche 'flèche bas' pour effectuer une recherche"
									title="presser sur la touche 'flèche bas' pour effectuer une recherche"
									onclick="if (document.forms[0].elements('forAffiliationId').value != '') forAffilieNumeroPopupTag.validate();">
							</TD>
            				<TD nowrap width="100" height="31">Lohnsumme </TD>
            				<TD nowrap width="220">
								<INPUT type="text" name="fromMasse" size="20" value="" tabindex="-1">
	     					</TD>
	     					<TD nowrap width="120" height="31">Forcierter Beitragssatz</TD>
				            <TD nowrap width="220">
								<SELECT style="width:60%" name="forTauxForce">
									<OPTION value="all"></OPTION>
									<OPTION value="force">Taux forcé</OPTION>
									<OPTION value="nonForce">Taux non forcé</OPTION>
								</SELECT>
							</TD>
          				</TR>
          				<TR>
							<TD nowrap width="100" height="31">Status</TD>
				            <TD nowrap width="180">
								<ct:FWCodeSelectTag 
		               				name="forEtatTaxe" 
									defaut="848002"
									codeType="VEETATAXE"
									wantBlank="true"/> 
							</TD>
							<TD nowrap width="100" height="31">Abgangsgrund</TD>
	            			<TD nowrap width="220"> 
								<ct:FWCodeSelectTag 
		               				name="forMotifFin" 
									defaut=""
									codeType="VEMOTIFFIN"
									wantBlank="true"/> 
							</TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>

			<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>