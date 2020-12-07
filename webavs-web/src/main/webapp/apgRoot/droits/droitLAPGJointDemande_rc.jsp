<%@page import="globaz.pyxis.util.CommonNSSFormater" %>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>

<%@ page import="globaz.apg.vb.droits.APDroitDTO" %>
<%@ page import="globaz.apg.db.droits.APDroitLAPG" %>
<%@ page import="globaz.apg.vb.droits.APDroitLAPGJointDemandeViewBean" %>
<%@ page import="globaz.apg.vb.droits.APDroitParametresRCDTO" %>
<%@ page import="globaz.commons.nss.*" %>
<%@ page import="globaz.jade.client.util.JadeStringUtil" %>
<%@ page import="globaz.prestation.api.IPRDemande" %>
<%@ page import="globaz.prestation.tools.PRSessionDataContainerHelper" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>

<%@ include file="/theme/find/header.jspf" %>
<%
    idEcran = "PAP0000";

	rememberSearchCriterias = false;

    // le viewBean doit avoir été créé puis sauvé dans la session par l'action custom APLAPGAction
    APDroitLAPGJointDemandeViewBean viewBean = (APDroitLAPGJointDemandeViewBean) request.getAttribute("viewBean");

    actionNew = servletContext + mainServletPath + "?userAction=" + viewBean.getTypePrestation().toUserAction() + ".afficher&_method=add";
%>
<%@ include file="/theme/find/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%
    if (IPRDemande.CS_TYPE_APG.equals(PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION))) {
        // Si APG Militaire
%> <ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%
} else if (IPRDemande.CS_TYPE_MATERNITE.equals(PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION))) {
    // Si APG Maternité
%>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%
} else if (IPRDemande.CS_TYPE_PANDEMIE.equals(PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION))) {
    // Si Pandémie
%>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalpan" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%
    }
%>
<script type="text/javascript">
<%
	if ((PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO) != null)
		&& PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO) instanceof APDroitDTO
		&& (!JadeStringUtil.isEmpty(((APDroitDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO)).getNoAVS()))) {
    %>
    bFind = true;
    <%
        } else {
    %>
    bFind = false;
    <%
        }
    %>

    usrAction = "apg.droits.droitLAPGJointDemande.lister";

    function clearFields() {
        document.getElementsByName("forIdGestionnaire")[0].value = "<%=viewBean.getCurrentUserId()%>";
        document.getElementsByName("partiallikeNumeroAVS")[0].value = "";
        document.getElementsByName("likeNumeroAVS")[0].value = "";
        document.getElementsByName("likeNom")[0].value = "";
        document.getElementsByName("likePrenom")[0].value = "";
        document.getElementsByName("forEtatDemande")[0].value = "blank";
        document.getElementsByName("forEtatDroit")[0].value = "blank";
        document.getElementsByName("forGenreServiceListDroit")[0].value = "blank";
        document.getElementsByName("forDateNaissance")[0].value = "";
        document.getElementsByName("forCsSexe")[0].value = "";
        document.getElementsByName("orderBy")[0].value = "<%=APDroitLAPGJointDemandeViewBean.FIELDNAME_NOM%>,<%=APDroitLAPGJointDemandeViewBean.FIELDNAME_PRENOM%>,<%=APDroitLAPG.FIELDNAME_DATEDEBUTDROIT%> DESC";
    }

    function likeNomChange() {
        if (document.getElementsByName("likeNom")[0].value != "") {
            document.getElementsByName("orderBy")[0].value = "<%=APDroitLAPGJointDemandeViewBean.FIELDNAME_NOM%>,<%=APDroitLAPGJointDemandeViewBean.FIELDNAME_PRENOM%>,<%=APDroitLAPG.FIELDNAME_DATEDEBUTDROIT%> DESC";
        }
    }

    function likePrenomChange() {
        if (document.getElementsByName("likePrenom")[0].value != "") {
            document.getElementsByName("orderBy")[0].value = "<%=APDroitLAPGJointDemandeViewBean.FIELDNAME_NOM%>,<%=APDroitLAPGJointDemandeViewBean.FIELDNAME_PRENOM%>,<%=APDroitLAPG.FIELDNAME_DATEDEBUTDROIT%> DESC";
        }
    }

    function fromNumeroAVSChange() {
        if (document.getElementsByName("likeNumeroAVS")[0].value != "") {
            document.getElementsByName("orderBy")[0].value = "<%=APDroitLAPGJointDemandeViewBean.FIELDNAME_NUM_AVS%>,<%=APDroitLAPG.FIELDNAME_DATEDEBUTDROIT%> DESC";
        }
    }
</script>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%=viewBean.getTitreEcran()%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<tr>
							<td>
								<table border="0" cellspacing="0" cellpadding="0" width="100%">
									<tr>
										<td>
											<label for="forIdGestionnaire">
												<ct:FWLabel key="JSP_GESTIONNAIRE" />
											</label>
										</td>
										<%
										if (PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_PARAMETRES_RC_DTO) != null) {
										%>
											<td>
												<ct:FWListSelectTag	data="<%=viewBean.getResponsableData()%>"
																	defaut="<%=((APDroitParametresRCDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_PARAMETRES_RC_DTO)).getResponsable()%>"
																	name="forIdGestionnaire" />
											</td>
										<%
											} else {
										%>
											<td>
												<ct:FWListSelectTag	data="<%=viewBean.getResponsableData()%>"
																	defaut="<%=viewBean.getCurrentUserId()%>"
																	name="forIdGestionnaire" />
											</td>
										<%
											}
										%>
										<td colspan="4">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td colspan="6">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td>
											<label for="fromNumeroAVS">
												<ct:FWLabel key="JSP_NSS_ABREGE" />
											</label>
										</td>
										<%
										if (PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO) != null) {
										%>
											<td>
												<ct1:nssPopup	avsMinNbrDigit="99"
																nssMinNbrDigit="99"
																name="likeNumeroAVS"
																newnss="<%=viewBean.isNNSS(((APDroitDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO)).getNoAVS())%>"
																onChange="fromNumeroAVSChange();"
																value="<%=NSUtil.formatWithoutPrefixe(((APDroitDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO)).getNoAVS(), ((APDroitDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_DTO)).getNoAVS().length() > 14 ? true : false)%>" />
											</td>
										<%
											} else {
										%>
											<td>
												<ct1:nssPopup	avsMinNbrDigit="99"
																nssMinNbrDigit="99"
																name="likeNumeroAVS"
																newnss="<%=viewBean.isNNSS()%>"
																onChange="fromNumeroAVSChange();" />
											</td>
										<%
										}
										%>
										<td>
											<label for="likeNom">
												<ct:FWLabel key="JSP_NOM" />
											</label>
										</td>
										<td>
											<input type="text" name="likeNom" value="" onchange="likeNomChange();" />
										</td>
										<td>
											<label for="likePrenom">
												<ct:FWLabel key="JSP_PRENOM" />
											</label>
										</td>
										<td>
											<input type="text" name="likePrenom" value="" onchange="likePrenomChange();" />
											<input type="hidden" name="hasPostitField" value="<%=true%>" />
										</td>
									</tr>
									<tr>
										<td>
											<ct:FWLabel key="JSP_DATE_NAISSANCE" />
										</td>
										<td>
											<input type="text" name="forDateNaissance" data-g-calendar=" " />
										</td>
										<td>
											<ct:FWLabel key="JSP_SEXE" />
										</td>
										<td>
											<ct:FWCodeSelectTag name="forCsSexe" wantBlank="<%=true%>" codeType="PYSEXE" defaut="" />
										</td>
										<td colspan="2">
											&nbsp;
										</td>
									</tr>
									<tr>
										<td>
											<label for="forEtatDemande">
												<ct:FWLabel key="JSP_ETAT_DEMANDE" />
											</label>
										</td>
<%
	if (PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_PARAMETRES_RC_DTO) != null) {
                %>
                <td>
											<ct:FWCodeSelectTag	codeType="PRETADEMAN"
																name="forEtatDemande"
																wantBlank="true"
																defaut="<%=((APDroitParametresRCDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_PARAMETRES_RC_DTO)).getEtatDemande()%>" />
										</td>
<%
	} else {
                %>
                <td>
                    <ct:FWCodeSelectTag codeType="PRETADEMAN" name="forEtatDemande" wantBlank="true" defaut=" "/>
                </td>
                <%
                    }
                %>
                <td>
                    <label for="forEtatDroit">
                        <ct:FWLabel key="JSP_ETAT_DROIT"/>
                    </label>
                </td>
                <%
                    if (PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_PARAMETRES_RC_DTO) != null) {
                %>
                <td>
											<ct:FWListSelectTag	data="<%=viewBean.getEtatDroitData()%>"
																defaut="<%=((APDroitParametresRCDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_PARAMETRES_RC_DTO)).getEtatDroit()%>"
																name="forEtatDroit" />
										</td>
<%
	} else {
                %>
                <td>
											<ct:FWListSelectTag	data="<%=viewBean.getEtatDroitData()%>"
																defaut=" "
																name="forEtatDroit" />
										</td>
<%
	}
                %>
                <td colspan="2">
                    &nbsp;
                </td>
            </tr>
            <tr>
                <td colspan="6">
                    &nbsp;
                </td>
            </tr>
            <tr>
                <td>
                    <ct:FWLabel key="JSP_TRIER_PAR"/>
                </td>
                <%
                    if (PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_PARAMETRES_RC_DTO) != null) {
                %>
                <td>
											<ct:FWListSelectTag	data="<%=viewBean.getOrderByData()%>"
																defaut="<%=((APDroitParametresRCDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_PARAMETRES_RC_DTO)).getOrderBy()%>"
																name="orderBy" />
										</td>
<%
	} else {
                %>
                <td><ct:FWListSelectTag data="<%=viewBean.getOrderByData()%>" defaut="" name="orderBy"/></td>
                <%
                    }
                    if (IPRDemande.CS_TYPE_PANDEMIE.equals(PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION))) {
                %>
                <td>
                    <label for="forGenreServiceListDroit">
                        <ct:FWLabel key="JSP_GENRE_SERVICE"/>
                    </label>
                </td>
                <%
                    if (PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_PARAMETRES_RC_DTO) != null) {
                %>
                <td>
                    <ct:FWListSelectTag data="<%=viewBean.getGenreDroitData()%>"
                                        defaut="<%=((APDroitParametresRCDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_DROIT_PARAMETRES_RC_DTO)).getGenreService()%>"
                                        name="forGenreServiceListDroit"/>
                </td>
                <%
                } else {
                %>
                <td>
                    <ct:FWListSelectTag data="<%=viewBean.getGenreDroitData()%>"
                                        defaut=" "
                                        name="forGenreServiceListDroit"/>
                </td>
                <td colspan="2"></td>
                <%
                    }
                } else {
                %>
                <td colspan="4"></td>
                <% } %>
            </tr>
            <tr>
                <td colspan="6">&nbsp;</td>
            </tr>
            <tr>
                <td><input type="button" onclick="clearFields()" accesskey="C" value="Clear"> [ALT+C]</td>
            </tr>
        </table>
    </td>
</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>