
var pegasusUtils = {
    lancementService : 	function (options){
        ajax = Object.create($.extend(true, {}, globazNotation.readwidget));
        ajax.options = options;
        ajax.read();
    },
    lancementServicePost : function(options) {
        ajax = Object.create($.extend(true, {}, globazNotation.readwidget));
        ajax.options = options;
        $.ajax({
            url: globazNotation.utils.getFromAction(),
            async: ajax.options.async,
            dataType: "json",
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data: ajax.getParamterForAjax(),
            success: function (data) {
                if(data) {
                    ajax.applyCallBack(data);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                ajax.applyErrorCallBack(jqXHR, textStatus, errorThrown);
            },
            type: "POST"
        });
    },
    lancementServiceSync : function (options){
        ajax = Object.create($.extend(true, {}, globazNotation.readwidget));
        ajax.options = options;
        ajax.readSync();
    },
    /**
     * Recherche ajax avec une waiting wheel
     * @param options Options correspondant aux options de la fonction $.ajax(optons) + selectorToAppend
     */
    ajaxWait : function(options) {
        var waiting;
        $.ajax({
            data: options.data,
            success: function (data) {
                options.success(data);
            },
            beforeSend: function(xhr) {
                if(typeof(options.beforeSend) === 'function') {
                    options.beforeSend();
                }
                waiting = $('<img />');
                waiting.attr('src', 'images/common/ajax-loader-1.gif');
                waiting.appendTo($(options.selectorToAppend));
            },
            complete: function() {
                if(typeof(options.complete) === 'function') {
                    options.complete();
                }
                waiting.remove();
            }
        });
    },
    toUpper : function(s) {
        var r=s.toLowerCase();
        r = r.replace(new RegExp("[àáâãäå]", 'g'),"a");
        r = r.replace(new RegExp("ç", 'g'),"c");
        r = r.replace(new RegExp("[èéêë]", 'g'),"e");
        r = r.replace(new RegExp("[ìíîï]", 'g'),"i");
        r = r.replace(new RegExp("ñ", 'g'),"n");
        r = r.replace(new RegExp("[òóôõö]", 'g'),"o");
        r = r.replace(new RegExp("[ùúûü]", 'g'),"u");
        r = r.replace(new RegExp("[ýÿ]", 'g'),"y");
        return r.toUpperCase();
    },
    formatNoAffilie : function($element) {
        var NO_AFFILIE_FIRST_PART_TEMPLATE = '0000000';
        var NO_AFFILIE_FIRST_PART_SIZE = 7;
        var NO_AFFILIE_SECOND_PART_TEMPLATE = '.00';

        var noAffilie = $element.val();
        var posAssocieSeparatorInNo = noAffilie.indexOf(".");
        var posConventionSeparatorInNo = noAffilie.indexOf("-");
        var partieApresSeparateur = "";

        if (posAssocieSeparatorInNo>0) {
            //Récupération de la partie après le séparateur d'associés (.)
            partieApresSeparateur = noAffilie.substring(posAssocieSeparatorInNo);
            //Récupération partie avant le séparateur
            noAffilie = noAffilie.substring(0,posAssocieSeparatorInNo);
        } else if (posConventionSeparatorInNo>0) {
            //Récupération de la partie après le séparateur de convention (-)
            partieApresSeparateur = NO_AFFILIE_SECOND_PART_TEMPLATE+noAffilie.substring(posConventionSeparatorInNo);

            //Récupération partie avant le séparateur
            noAffilie = noAffilie.substring(0,posConventionSeparatorInNo);
        }

        //Formattage de la 1ère partie du numéro d'affilié et concaténation avec le reste
        var lengthNoAffilie = noAffilie.length;
        if (lengthNoAffilie>0 && lengthNoAffilie<NO_AFFILIE_FIRST_PART_SIZE) {
            var noAffiliePadded = String(NO_AFFILIE_FIRST_PART_TEMPLATE+noAffilie).slice(-NO_AFFILIE_FIRST_PART_SIZE);
            noAffiliePadded += partieApresSeparateur;
            $element.val(noAffiliePadded);
        }
    }
};


$.fn.serializeFormJSON = function () {

    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

$.fn.masterDetail = function(options) {
    var container = $(this[0]);

    loadMasterPage();

    var ICON_EXPAND = 'images/icon-expand.gif';
    var ICON_COLLAPSE = 'images/icon-collapse.gif';

    function isDownloaded(element) {
        return $(element).attr('data-found');
    }

    function setIsFound(element) {
        $(element).attr('data-found','found');
    }

    function isVisible(element) {
        return $(element).is(':visible');
    }

    function showRows(element) {
        $(element).show();
        changeToCollapse(element);
    }

    function changeToCollapse(element) {
        $(element).prev().find('img').attr('src',ICON_COLLAPSE);
    }

    function hideRows(element) {
        $(element).hide();
        $(element).prev().find('img').attr('src',ICON_EXPAND);
    }

    function loadMasterPage() {
        defaultTableAjaxList.init({
            s_container: options.s_container,
            s_table : options.s_table,
            s_selector : '#prestationsContent',
            s_actionAjax : options.s_actionAjax,
            userActionDetail : options.s_userActionDetail,

            init : function() {
                var $container = $(options.s_container);
                this.capage(20, [ 10, 20, 30, 50, 100 ]);
                $container.on(eventConstant.AJAX_FIND_COMPLETE, function(data) {
                    bindEvents();
                });
            },

            getParametresForFind : function () {
                var m_map = {};
                m_map[options.s_actionAjaxParam] = options.s_actionAjaxParamValue;
                return m_map;
            }
        });
    }

    function bindEvents() {
        var $links = $(options.s_detailClassForClickEvent);
        $links.click(function () {
            var $this = $(this);
            var $masterTd = $this.closest('td');
            var $masterTr = $this.closest('tr');

            if(isDownloaded($masterTd)) {
                var $details = $masterTr.next('tr');
                if(isVisible($details)) {
                    hideRows($details);
                } else {
                    showRows($details);
                }
            } else {
                var parameters = options.s_detailParam.call($masterTd);
                var userAction = {userAction : options.s_detailPage};
                $.extend(parameters,userAction);
                setIsFound($masterTd);

                var $newTr = $('<tr><td class="actions" /><td colspan="100" class="detail" /></tr>');
                $masterTr.after($newTr);

                var $waiting = $('<img />');
                $waiting.attr('src', 'images/common/ajax-loader-1.gif');

                pegasusUtils.ajaxWait({
                    data : parameters,
                    success : function(data) {
                        $waiting.remove();
                        $newTr.find('.detail').append(data);
                        changeToCollapse($this.closest('tr').next());
                    },
                    beforeSend: function(xhr) {
                        $newTr.find('.actions').append($waiting);
                    }
                });
            }
        });
    }
    return container;
};