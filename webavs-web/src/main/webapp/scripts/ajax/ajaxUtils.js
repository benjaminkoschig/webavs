var ajaxUtils = {
    b_once: false,
    nb_container: null,
    conteur_nb_call_afterInint: 0,
    s_nameClassContainer: null,
    url: null,

    addFocusOnFirstElement: function ($mainContainer) {
        var $eleForm = $mainContainer.find(':input').filter(':visible:enabled').not('.forFocus,.noFocus');
        var $el = $eleForm.parent().find(':input').first();
        $el.show();
        $el.prop('disabled', false);
        $el.prop('readOnly', false);
        $el.focus();
    },

    triggerStartNotation: function () {
        $('html').triggerHandler(eventConstant.AJAX_INIT_DONE);
    },

    addElementForFocus: function ($element) {
        $element.append($('<input>', {
            'type': 't ext',
            'class': 'forFocus',
            'tabindex': -1
        }).css({
            'display': 'inline',
            'position': 'absolute',
            'width': '0px',
            'height': '0px',
            'margin': '0px',
            'padding': '0px',
            'border': '0px',
            'font-size': '0px',
            'line-height': '0px'
        }));
    },

    getLogsFromLevel: function (t_logs, level) {
        var t_logsReturn = [],
            o_currentLog;
        for (var i = 0; t_logs && (i < t_logs.length); i++) {
            o_currentLog = t_logs[i];
            if (o_currentLog.level === 3) {
                t_logsReturn.push(o_currentLog);
            }
        }
        return t_logsReturn;
    },

    hasLog: function (logs, level) {
        return this.getLogsFromLevel(logs, level).length > 0;
    },

    hasError: function (data) {
        var hasError = false, o_objet;
        if ($.isXMLDoc(data)) {
            s_errorJson = $(data).find('errorJson').text();
            if (s_errorJson) {
                o_objet = $.parseJSON(s_errorJson);
                hasError = o_objet.errorBean || this.hasLog(o_objet.messages, 3);
            } else {
                hasError = $.trim($(data).find('error').text()).length > 0;
            }
        } else {
            if (data) {
                hasError = data.errorBean || this.hasLog(data.messages, 3);
            }
        }
        return hasError;
    },

    displaySpy: function (s_pathSpy, data, $detail) {
        var objetSpy;

        if (s_pathSpy) {
            var t = s_pathSpy.split('\.');
            if (t.length) {
                objetSpy = data;
                for (var i = 0; i < t.length; i++) {
                    objetSpy = objetSpy[t[i]];
                }
                globazNotation.utilsFormatter.displaySpy($detail, objetSpy.creationSpy, objetSpy.spy);
            }
        }
    },

    ajaxExecut: function (s_action, o_data, f_callBack, f_errorCallBack) {
        var that = this;
        var userAction = s_action + '.executerAJAX';
        this.ajaxCustom(userAction, o_data, f_callBack, f_errorCallBack);
    },


    ajaxCustom: function (s_action, o_data, f_callBack, f_errorCallBack) {
        var that = this;
        o_data.userAction = s_action;

        $.ajax({
            data: o_data,
            url: ajaxUtils.url,
            dataType: 'json',
            success: function (data) {
                if (data === null) {
                    throw 'executerAJAX must forward a FWAJAXViewBeanInterface (like in FWDefaultServletAction.actionExecuterAJAX)';
                }
                if (ajaxUtils.hasError(data)) {
                    that.displayError(data);
                    if (typeof f_errorCallBack === 'function') {
                        f_errorCallBack(data);
                    }
                } else {
                    if (typeof f_callBack === 'function') {
                        f_callBack(data);
                    }
                }
            },
            type: 'GET'
        });
    },

    serialize: function ($detail) {
        var map = {};
        $detail.find(':input').each(function () {
            map[this.id] = this.value;
        });
        return map;
    },

    dispalyLogOrError: function (data) {
        if (data.errorBean) {
            this.displayError(data.errorBean);
        }
        this.displayLog(data.messages);
    },

    renderLogs: function (logs) {
        var s_template = '<div class="{{clazz}}">'
            + '<div id="businessMesage">'
            + '<ul>'
            + '{{@each logs}}'
            + '<li style="margin:0px 0 10px 0 ">{{message}}</li>'
            + '{{/@each logs}}'
            + '</ul>'
            + '</div>'
            + '</div>';
        return globazNotation.template.compile(logs, s_template);
    },
    renderLogsV2: function (logs,niveau) {
        var tab = logs.split('\n');
        var logsFinal = '';
        for( i = 0;i< tab.length;i++){
            logsFinal = logsFinal+tab[i] + '<br>';
        }

        var s_template = '<div class="'+niveau+'">'
            + '<div id="businessMesage">'
            + '<ul>'
            + logsFinal
            + '</ul>'
            + '</div>'
            + '</div>';
        return globazNotation.template.compile(logs, s_template);
    },


    displayLogsIfExsite: function (data) {
        if ($.isXMLDoc(data)) {
            var s_errorJson = $(data).find('errorJson').text();
            if (s_errorJson) {
                data = $.parseJSON(s_errorJson);
            }
        }
        if (data && data.messages) {
            this.displayLogs(data.messages);
        }
    },

    displayLogsIfExisteV2: function (data,niveau) {
        var logs = $(data).find('messageLogs').text();
        if (logs) {
            this.displayLogsPopupV2(logs,niveau);
        }
    },

    notifyInfo: function (message,dialog) {
        dialog.dialog({
            resizable: false,
            autoOpen: false,
            modal: false,
            text : message,
            buttons: {
                'ok': function () {
                    $(this).dialog('close');
                }
            }
        });
        dialog.dialog("open");
    },
    displayLogs: function (t_logs) {
        var t_logsError = [],
            t_logWarn = [],
            t_logInfos = [],
            o_currentLog = null,
            s_logErrors = '',
            s_logWarns = '',
            s_logInfos = '',
            logs = {};

        t_logs = t_logs ? t_logs : [];

        for (var i = 0; i < t_logs.length; i++) {
            o_currentLog = t_logs[i];
            if (o_currentLog.level === 3) {
                t_logsError.push(o_currentLog);
            }
            if (o_currentLog.level === 2) {
                t_logWarn.push(o_currentLog);
            }
            if (o_currentLog.level === 1) {
                t_logInfos.push(o_currentLog);
            }
        }

        if (t_logsError.length) {
            logs.logs = t_logsError;
            logs.clazz = 'error';
            s_logErrors = this.renderLogs(logs);
        }
        if (t_logWarn.length) {
            logs.logs = t_logWarn;
            logs.clazz = 'warn';
            s_logWarns = this.renderLogs(logs);
        }
        if (t_logInfos.length) {
            logs.logs = t_logInfos;
            logs.clazz = 'infos';
            s_logInfos = this.renderLogs(logs);
        }

        globazNotation.utils.console('<div>' + s_logErrors + s_logWarns + s_logInfos + '</div>');
    },
    displayLogsPopupV2: function (logInfo,niveau) {
        var s_logInfos = '';
        s_logInfos = this.renderLogsV2(logInfo,niveau);
        if(niveau == 'error'){
            globazNotation.utils.consoleError('<div>' + s_logInfos + '</div>');
        }
        if(niveau == 'warn'){
            globazNotation.utils.consoleWarn('<div>' + s_logInfos + '</div>');
        }

    },
    renderError: function (o_error) {
        var s_template = '<div style="color:#8F0707">'
            + '<div id="errorInfos">'
            + '<div id="exceptions">'
            + '<div><strong>Exception</strong></div>'
            + '<ul>'
            + '{{@each exceptions}}'
            + '<li style="margin:0px 0 10px 0 ">{{detailMessage}}</li>'
            + '{{/@each exceptions}}'
            + '</ul>'
            + '</div>'
            + '<div id="businessMesage">'
            + '<div><strong >BusinessMessage</strong></div>'
            + '<ul>'
            + '{{@each messages}}'
            + '<li style="margin:0px 0 10px 0 ">{{message}}</li>'
            + '{{/@each messages}}'
            + '</ul>'
            + '</div>'
            + '</div>'
            + '<div>'
            + '<div style="border-top:1px solid #C6A9A9; margin:25px 0 0 0; padding: 5px 0 0 0; text-align: right;">'
            + '<span id="disaplayStack">Stack...</span>'
            + '<a id="mailToException" href="{{hrefMailTo}}">Mail</a>'
            + '</div>'
            + '<div id="stack" style="height:500px;margin:0px 0 50px 0;overflow:auto">'
            + '<div><strong >Stack</strong></div>'
            + '<pre style="font-size: 11px;">{{stack}}</pre>'
            + '</div>'
            + '</div>'
            + '</div>';
        return globazNotation.template.compile(o_error, s_template);
    },

    uniqueMessage: function (t_message, s_parameterName) {
        var o_unique = {},
            i = 0;

        for (i = 0; t_message && i < t_message.length; i++) {
            o_unique[t_message[i][s_parameterName]] = t_message[i];
        }

        t_message = [];
        i = 0;
        for (var key in o_unique) {
            t_message[i] = o_unique[key];
            i++;
        }
        return t_message;
    },

    renderDialogExceptionError: function (o_error) {
        var s_error = '',
            $dialog = null,
            $err = null,
            $stack = null,
            $disaplayStack = null,
            propSave = null,
            prop = ['position', 'top', 'bottom', 'left', 'right', 'width', 'height'];

        var cssProps = function (element, set) {
            var properties = {};
            for (var i = 0; i < set.length; i++) {
                if (set[i] !== null) {
                    properties[set[i]] = element[0].style[set[i]];
                }
            }
            return properties;
        };

        for (var i = 0; o_error.exceptions && i < o_error.exceptions.length; i++) {
            if (!o_error.exceptions[i].detailMessage) {
                o_error.exceptions[i].detailMessage = o_error.exceptions[i].messageToString;
            }
        }

        // une seule fois chaque message d'erreur et chaque exception
        o_error.exceptions = this.uniqueMessage(o_error.exceptions, 'detailMessage');
        o_error.messages = this.uniqueMessage(o_error.messages, 'message');

        o_error.idEcran = encodeURI($('.idEcran').text()).replace(/\?/g, '%3F');
        o_error.hrefMailTo = 'mailto:globaz@globaz.ch?subject=Exception -> idEcran(' + o_error.idEcran + ')&body=' + encodeURI(o_error.stack.substr(0, 1822));

        s_error = this.renderError(o_error);
        $err = $(s_error);
        $stack = $err.find('#stack');
        $('#stackTrace').hide();
        $err.find('#stack').hide();
        if (!o_error.message) {
            $('#businessMesage').hide();
        }

        $err.find('#mailToException').hide().button({
            icons: {
                primary: 'ui-icon-mail-closed',
                secondary: null
            }
        });
        $disaplayStack = $err.find('#disaplayStack');
        $disaplayStack.button();


        $disaplayStack.click(function () {
            var iteration = $(this).data('iteration') || 1;
            switch (iteration) {
                case 1:
                    if (!$dialog) {
                        $dialog = $stack.closest('.ui-dialog');
                        propSave = cssProps($dialog, prop);
                        propSave.height = $dialog.height() + 'px';
                    }
                    $stack.show();
                    $dialog.animate({
                        left: 0,
                        'width': '100%',
                        'height': '100%'
                    }, function () {
                        $stack.css('height', ($err.height() - $err.find('#errorInfos').height() - 20) + 'px');
                    });
                    break;
                case 2:
                    $stack.fadeOut(500);
                    $dialog.animate(propSave, 500);
                    break;
            }
            iteration++;
            if (iteration > 2) iteration = 1;
            $(this).data('iteration', iteration);
        });

        return $err;
    },

    displayError: function (data) {
        var $data = null,
            $error = null,
            $json = null,
            s_error = null,
            s_errorJson = null;

        if (!$.isPlainObject(data)) {
            $data = $(data);

            $error = $data.find('error');

            if ($error.attr('errorPage')) {
                $json = $data.find('json');
                s_errorJson = $json.find('exception').text();
                if (s_errorJson) {
                    try {
                        data = $.parseJSON(s_errorJson);
                    } catch (e) {
                        s_error = s_errorJson;
                    }
                }
            } else {
                s_error = $error.text();
                s_errorJson = $data.find('errorJson').text();
                if (s_errorJson) {
                    data = $.parseJSON(s_errorJson);
                }
            }
        }

        if ($.isPlainObject(data)) {
            // on n'est dans le cas d'une exception
            if (data.errorBean) {
                s_error = this.renderDialogExceptionError(data.errorBean);
            } else if (data.messages && data.exceptions && data.stack) {
                s_error = this.renderDialogExceptionError(data);
            } else if (data.messages) {
                var logsRender = {};
                logsRender.logs = this.getLogsFromLevel(data.messages, 3);
                logsRender.clazz = 'error';

                logsRender.logs = this.uniqueMessage(logsRender.logs, 'message');

                s_error = this.renderLogs(logsRender);
            }
        }

        if (!s_error || !s_error.length) {
            s_error = data;
            if (data && $.isPlainObject(data)) {
                s_error = data.error;
                if ($.trim(s_error).length === 0 && data.viewBean) {
                    s_error = data.viewBean.message;
                }
            }
        }

        if (s_error) {
            globazNotation.utils.consoleError(s_error);
        }
        $('.mainContainerAjax').find('.loading_horizonzal').remove();
    },

    displayServerError: function (data) {
        globazNotation.utils.consoleError(data, 'Error', null, '90%');
    },

    afterInit: function ($mainContainer) {
        var t_class;
        this.conteur_nb_call_afterInint = this.conteur_nb_call_afterInint + 1;
        if ($mainContainer.length) {
            if (this.conteur_nb_call_afterInint === 1) {
                this.s_nameClassContainer = ($mainContainer.attr('class'));
                if (this.s_nameClassContainer && this.s_nameClassContainer.length) {
                    t_class = this.s_nameClassContainer.split(' ');
                    if (t_class.length > 1) {
                        this.s_nameClassContainer = t_class[0];
                    }
                    this.nb_container = $('.' + this.s_nameClassContainer).length;
                }
            }
            $mainContainer.addClass('mainContainerAjax');
            if ($mainContainer.attr('title')) {

                var $title = $('<span/>');
                $title.append($('<strong/>', {
                    'text': $mainContainer.attr('title')
                }));

                $mainContainer.prepend($title);
            }
            if (this.nb_container === this.conteur_nb_call_afterInint) {
                var $html = $('html');
                if (top.fr_main !== undefined) {
                    top.fr_main.focus();
                }
                $html.triggerHandler(eventConstant.AJAX_INIT_DONE);
            }
        }
    },

    addOverlay: function ($container) {

        if (!$container.find('.loading_horizonzal').length) {
            //	var $divOverlay = $('<div/>', {
            //		'class': 'ajaxOverlay uidOverlay_'+	$container.data("UID_ajax_container")
            //	}).css({
            //		'width': $container.outerWidth() + 'px',
            //		'height': $container.outerHeight() + 'px',
            //		'left': $container.offset().left + 'px',
            //		'top': $container.offset().top + 'px',
            //		'opacity': 0.2,
            //		'filter': 'Alpha(Opacity=20)'
            //	});
            //
            //	$divOverlay.appendTo('body');
            $container.overlay({b_relatif: false});
            this.addAnimLoading($container);
        }
    },

    beforeAjax: function ($container) {
        this.addOverlay($container);
        $container.find(':button').attr('disabled', true);
    },

    addAnimLoading: function ($container) {
        var s_src = './images/loader_horizontal.gif';
        if (!$container.find('.loading_horizonzal').length) {
            $('<img />', {
                'src': s_src,
                'class': 'loading_horizonzal'
            }).css({
                'z-index': 1000,
                'opacity': 1,
                'text-align': 'center'
            }).appendTo($container.find(".imgLoading"));
        }
    },

    removeOverlay: function ($container) {
        $container.find('.loading_horizonzal').remove();
        //$('#uidOverlay_'+$container.data("UID_ajax_container")).fadeOut("200").remove();
        $container.removeOverlay();
    },

    afterAjaxComplete: function ($container) {
        $container.find(':button').attr('disabled', false);
        this.removeOverlay($container);
    },

    parseRecurs: function (json) {
        var t_objects = [];
        var chaine = '';
        return this.parseRecursIndirect(json, chaine, t_objects, true);
    },

    parseRecursIndirect: function (json, chaine, t_objects, b_isTreeRoot) {
        var object;
        if ($.isPlainObject(json)) {
            for (var j in json) {
                object = this.parseRecursIndirect(json[j], (chaine === '') ? j : chaine + '.' + j, t_objects, false);
                if (typeof object !== 'undefined') {
                    t_objects.push(object);
                }
            }
        } else {
            object = {
                cheminComplet: chaine,
                nomCourt: chaine.substr(chaine.lastIndexOf('.') + 1),
                valeur: json
            };
            return object;
        }
        if (b_isTreeRoot) {
            return t_objects;
        }
    },

    displayJson: function (json) {
        this.s_global = '';
        return this.displayJsonIndirect(json, null, true);
    },

    displayJsonIndirect: function (json, key, b_isTreeRoot) {
        var s_html = '',
            s_html0 = '',
            that = this;
        if ($.isPlainObject(json) || ($.isArray(json) && $.isPlainObject(json[0]))) {
            if (key !== null) {
                this.s_global = this.s_global + '<ul>' + key + ' ';
            }
            $.each(json, function (key, json) {
                s_html0 = that.displayJsonIndirect(json, key, false);
                if (typeof s_html0 !== 'undefined') {
                    s_html = s_html + s_html0;
                }
            });
            if (s_html.length) {
                this.s_global = this.s_global + '<ul>' + s_html + '</ul>';
                if (key !== null) {
                    this.s_global = this.s_global + '</ul>';
                }
            }
        } else {
            s_html = '<li>' + key + ':' + json + '</li>';
            return s_html;
        }
        if (b_isTreeRoot) {
            return this.s_global;
        }
    },

    defaultLoadData: function (o_data, $detail, s_selector, b_shortId) {
        var s_cheminComplet = null,
            id = null;
        b_shortId = b_shortId || false;

        if ($.isPlainObject(o_data)) {
            var t_objects = this.parseRecurs(o_data),
                t_element = [];

            for (var key in t_objects) {
                s_cheminComplet = t_objects[key].cheminComplet.replace(/\./g, '\\.');
                if (b_shortId) {
                    id = t_objects[key].nomCourt;
                } else {
                    id = s_cheminComplet;
                }
                $element = $detail.find(s_selector + id);

                if ($element.length) {
                    t_element.push($element);
                    if ($element.get(0).type === 'checkbox') {
                        var valCheckBox = t_objects[key].valeur;
                        if (valCheckBox && $.type(valCheckBox) === 'string') {
                            valCheckBox = valCheckBox.toUpperCase();
                        }

                        if (valCheckBox === 'ON' || valCheckBox === 'TRUE' || valCheckBox === true || t_objects[key].valeur == $element.val()) {
                            $element.prop('checked', "checked");
                        } else {
                            $element.prop('checked', "");
                        }
                    }
                    if ($element.is(':input')) {
                        $element.val(t_objects[key].valeur);
                    } else {
                        $element.text(t_objects[key].valeur);
                    }
                }
            }
            return t_element;
        } else {
            o_data.find('contenu').children().each(function () {
                var s_name = this.nodeName;
                $detail.find(s_selector + s_name).val(o_data.find(s_name).text());
            });
        }
    },

    createMapForSendData: function ($container, s_selector) {
        var o_map = {},
            s_name = '',
            s_class = '';

        $container.find(':input').each(function () {
            s_name = this.name;
            if (s_selector === '#') {
                s_name = this.id;
            }
            if (s_selector === '.') {
                s_class = $(this).attr('class');

                if (s_class.split(' ').length > 1) {
                    s_name = this.name;
                    if (s_class.indexOf(s_name) === -1) {
                        throw 'Il exite plus de une class sur l\'attribut class ou '
                        + 'la valeur de l\'attribut du name n\'est pas définit dans l\'attribut class '
                        + 'Il faut définir l\'identifiant(\'' + s_class + '\') dans l\'attribut name';
                    }
                } else {
                    s_name = s_class;
                }
            }
            if ($.trim(s_name)) {
                o_map[s_name] = this.value;
                if (this.type !== 'button') {
                    switch (this.type) {
                        case 'password':
                        case 'select-multiple':
                        case 'select-one':
                        case 'text':
                        case 'textarea':
                        case 'hidden':
                            break;
                        case 'checkbox':
                            if (this.checked) {
                                o_map[s_name] = true;
                            } else {
                                o_map[s_name] = false;
                            }
                            break;
                        case 'radio':
                            if (this.checked) {
                                o_map[s_name] = true;
                            } else {
                                o_map[s_name] = false;
                            }
                    }
                }
            }
        });
        return o_map;
    },

    disabeldEnableForm: function ($mainContainer, $formElement, b_disabeEnable) {
        $formElement.prop('disabled', b_disabeEnable);
        $mainContainer.triggerHandler(eventConstant.AJAX_DISABLE_ENABLED_INPUT);
    },

    addNoCache: function (o_data) {
        o_data.noCache = globazNotation.utilsDate.toDayInStringJadeFormate() + (new Date()).getMilliseconds();
        return o_data;
    },

    jsonToString: function (json) {
        var s_toString = '',
            b_array = false,
            s_key = '';

        if ($.isArray(json)) {
            b_array = true;
        }

        if (b_array || $.isPlainObject(json)) {
            for (var key in json) {
                if (b_array) {
                    s_key = '';
                } else {
                    s_key = '\'' + key + '\':';
                }

                if ($.isArray(json[key])) {
                    s_toString += s_key + '[';
                    for (var i = 0; i < json[key].length; i++) {
                        s_toString += this.jsonToString(json[key][i]) + ',';
                    }
                    s_toString = s_toString.substring(0, s_toString.lastIndexOf(','));
                    s_toString += ']';
                } else if ($.isPlainObject(json[key])) {
                    s_toString += s_key + this.jsonToString(json[key]);
                } else {
                    var value = json[key],
                        b_isNumeric = $.type(value) === 'number';

                    s_toString += s_key;
                    if (!b_isNumeric) {
                        if (value !== null) {
                            s_toString += '\'';
                            value = (value).toString().replace(/'/g, '\\\'');
                        }
                    }

                    s_toString += value;
                    if (!b_isNumeric && value !== null) {
                        s_toString += '\'';
                    }
                }
                s_toString += ',';
            }
        } else {
            return json;
        }
        s_toString = s_toString.substring(0, s_toString.lastIndexOf(','));

        return ((b_array) ? '[' : '{') + s_toString + ((b_array) ? ']' : '}');
    }
};

$(function () {
    // configure AJAX
    ajaxUtils.url = $('[name=formAction]').attr('content');
    $.ajaxSetup({
        url: ajaxUtils.url,
        error: function (req, textStatus, errorThrown) {
            ajaxUtils.displayServerError(req.responseText);
        }
    });
});
