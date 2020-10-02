<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ taglib uri="/WEB-INF/pegasus.tld" prefix="pe" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.pegasus.vb.home.PCHomeViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="ch.globaz.pegasus.business.models.home.SimplePeriodeServiceEtat"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%
    //Les labels de cette page commencent par le préfix "JSP_PC_PRET_TIERS_D"
    idEcran="PPCXXXX";

    PCPrimeAssuranceMaladieViewBean viewBean = (PCPrimeAssuranceMaladieViewBean) session.getAttribute("viewBean");

    boolean viewBeanIsNew="add".equals(request.getParameter("_method"));

    autoShowErrorPopup = true;

    bButtonDelete = false;

    if(viewBeanIsNew){
        // change "Valider" action pour
        //userActionValue
    } else {
        bButtonCancel=false;
        bButtonUpdate=false;
        bButtonValidate=false;
    }

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%@ include file="/pegasusRoot/droit/commonDroit.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="ch.globaz.pegasus.business.models.droit.SimpleDonneeFinanciereHeader"%>
<%@page import="ch.globaz.pegasus.business.models.droit.MembreFamilleEtendu"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>


<%@page import="globaz.pegasus.utils.PCCommonHandler"%>
<%@ page import="globaz.pegasus.vb.assurancemaladie.PCPrimeAssuranceMaladieViewBean" %>
<%@ page import="ch.globaz.pegasus.business.models.assurancemaladie.AssuranceMaladie" %>
<%@ page import="ch.globaz.pegasus.business.models.assurancemaladie.SimplePrimeAssuranceMaladie" %>
<script language="JavaScript">
    var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
    var PAGE_ID_DROIT="<%=viewBean.getId() %>";
    var ACTION_AJAX="<%=IPCActions.ACTION_DROIT_ASSURANCE_MALADIE_AJAX%>";
    $(function(){
        $('.cacher').hide();
    });

    var ID_TIERS_REQURANT ;
    $(function () {
        ID_TIERS_REQURANT = $("[idtiersmembrefamillerequerant]").attr("idtiersmembrefamillerequerant");
    });
    var getTitleForEchance = function ($element) {
        var s_onglet = $(".onglets .selected").text();
        return s_onglet ;
    }


</script>

<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/assurancemaladie/PrimeAssuranceMaladie_MembrePart.js"></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/assurancemaladie/saisiePrimeAssuranceMaladie_de.js"></script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
<%=PCCommonHandler.getTitre(objSession,request)%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<TR>
    <td colspan="3">
        <div class="conteneurDF">

            <div class="areaAssure">
                <%=viewBean.getRequerantDetail(objSession) %>
            </div>

            <hr />

            <%=PCDroitHandler.getOngletHtml(objSession,viewBean,IPCDroits.ONGLETS_ASSURANCE_MALADIE,request,servletContext + mainServletPath)%>


            <div class="conteneurMembres">
                <%
                    int j = 0;
                    for(Iterator itMembre=viewBean.getMembres().iterator();itMembre.hasNext();){
                        MembreFamilleEtendu membreFamille=(MembreFamilleEtendu)itMembre.next();
                %>

                <div class="areaMembre" idMembre="<%=membreFamille.getId()%>">
                    <div class="areaTitre">
                        <%=PCDroitHandler.getFromattedTitreHTML(objSession,membreFamille)%>
                    </div>
                    <table class="areaDFDataTable"  width="100%">
                        <thead>
                        <tr>
                            <th data-g-cellformatter="css:formatCellIcon">&#160;</th>
                            <th><ct:FWLabel key="JSP_PC_FRAIS_GARDE_L_MONTANT"/></th>
                            <th data-g-periodformatter=" " data-g-deallaterperiod=" "><ct:FWLabel key="JSP_PC_FRAIS_GARDE_L_PERIODE"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <%
                            FWCurrency montant = new FWCurrency("0.00");

                            String idGroup=null;
                            for(Iterator itDonnee=viewBean.getDonnees(membreFamille.getId()).iterator();itDonnee.hasNext();){
                                AssuranceMaladie donneeComplexe=(AssuranceMaladie)itDonnee.next();

                                SimplePrimeAssuranceMaladie donnee=(SimplePrimeAssuranceMaladie)donneeComplexe.getSimplePrimeAssuranceMaladie();
                                SimpleDonneeFinanciereHeader dfHeader=donneeComplexe.getSimpleDonneeFinanciereHeader();

                                montant = new FWCurrency(donnee.getMontant());

                                if(!dfHeader.getIdEntityGroup().equals(idGroup)){
                                    idGroup=null;
                                }

                        %>
                        <tr idEntity="<%=donnee.getId() %>" idGroup="<%=dfHeader.getIdEntityGroup() %>" header="<%=idGroup==null?"true":"false"%>">
                            <td>&#160;</td>
                            <td style="text-align:right;"><%=montant.toStringFormat() %></td>
                            <td><%=dfHeader.getDateDebut() %> - <%=dfHeader.getDateFin() %></td>
                        </tr>
                        <%
                                idGroup=dfHeader.getIdEntityGroup();
                            }

                        %>
                        </tbody>
                    </table>

                    <div class="areaDFDetail">
                        <table>
                            <tr>
                                <td><ct:FWLabel key="JSP_PC_REN_RAA_D_MONTANT"/></td>
                                <td><input type="text" class="montant" data-g-amount="mandatory:true, periodicity:M"/></td>
                            </tr>
                            <tr>
                                <td><ct:FWLabel key="JSP_PC_MAR_STO_D_DATE_DEBUT"/></td>
                                <td><input type="text" name="dateDebut" value="" data-g-calendar="mandatory:true,type:month"/></td>
                                <td><ct:FWLabel key="JSP_PC_MAR_STO_D_DATE_FIN"/></td>
                                <td><input type="text" name="dateFin" value="" data-g-calendar="type:month"/></td>
                            </tr>
                        </table>
                        <ct:ifhasright element="<%=IPCActions.ACTION_DROIT_ASSURANCE_MALADIE_AJAX%>" crud="cud">
                            <%@ include file="/pegasusRoot/droit/commonButtonDF.jspf" %>
                        </ct:ifhasright>
                    </div>
                </div>
                <%
                    }
                %>
            </div>
        </div>
    </TD>
</TR>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
<%-- /tpl:insert --%>
