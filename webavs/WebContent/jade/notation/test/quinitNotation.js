/**
 * @author DMA
 */
/*-----------------Utilitaire---------------------------------------*/

var equals = equals;
var module = module;
var test = test;
var ok = ok;
var deepEqual = deepEqual;
var QUnit = QUnit;
var raises = raises;
var asyncTest = asyncTest;
var start = start;

var testUtils = {
	iniTest: function () {
		this.initUtils();
		this.initFormatter();
		this.initDate();
		this.initInput();
		this.initTemplate();
		this.initReadWidget();
	},

	initDate: function () {
		module("utils:date");
		test("fonction privé", function () {
			var testValueWithoutZero = 1;
			/* 1 */
			equals(globazNotation.utilsDate._format(testValueWithoutZero), "01", "Test fonction privée:_format(), avec ajout 0");
			testValueWithoutZero = 10;
			/* 2 */
			equals(globazNotation.utilsDate._format(testValueWithoutZero), "10", "Test fonction privée:_format(), sans ajout 0");

			var testValue = "12hkj";
			var testObject = new Object();
			var testCorrectDate = new Date();
			/* 3 */
			equals(globazNotation.utilsDate._isJSDate(testValue), false, "Test fonction privée:_isJSDate(), avec objet autre que date[string]");
			/* 4 */
			equals(globazNotation.utilsDate._isJSDate(testObject), false, "Test fonction privée:_isJSDate(), avec objet autre que date[object]");
			/* 5 */
			equals(globazNotation.utilsDate._isJSDate(testCorrectDate), true, "Test fonction privée:_isJSDate(), avec objet JS date valide");

			var correctGlobazDate = "12.12.2010";
			var malFormedGlobazDate = "12.122010";
			var nonCorrectTokenSizeDay = "121.1.2010";
			var nonCorrectTokenSizeMonth = "1.121.2010";
			var nonCorrectTokenSizeYear = "12.11.20101";
			var nonCorrectDateNumber = "33.13.1456";

			/* 6 */
			equals(globazNotation.utilsDate._isValidGlobazDate(correctGlobazDate), true, "Test fonction privée:_isValidGlobazDate(), nombre de tokens correct");
			/* 7 */
			equals(globazNotation.utilsDate._isValidGlobazDate(malFormedGlobazDate), false, "Test fonction privée:_isValidGlobazDate(), nombre de tokens incorrect");
			/* 8 */
			equals(globazNotation.utilsDate._isValidGlobazDate(nonCorrectTokenSizeDay), false, "Test fonction privée:_isValidGlobazDate(), nombre caractere jour incorrect");
			/* 9 */
			equals(globazNotation.utilsDate._isValidGlobazDate(nonCorrectTokenSizeMonth), false, "Test fonction privée:_isValidGlobazDate(), nombre caractere mois incorrect");
			/* 10 */
			equals(globazNotation.utilsDate._isValidGlobazDate(nonCorrectTokenSizeYear), false, "Test fonction privée:_isValidGlobazDate(), nombre caractere annee incorrect");
			/* 11 */
			equals(globazNotation.utilsDate._isValidGlobazDate(nonCorrectDateNumber), false, "Test fonction privée:_isValidGlobazDate(), nombre date incorrect");
		});

		test("convertJSDateToBDstringDateFormat", function () {
			var s_dateTestNow = new Date();// date now
			var s_dateToCompareForTestNow = s_dateTestNow.getFullYear() + '' + (globazNotation.utilsDate._format(s_dateTestNow.getMonth() + 1)) + '' + globazNotation.utilsDate
					._format(s_dateTestNow.getDate());// string pour compare
			/* 1 */
			equals(globazNotation.utilsDate.convertJSDateToDBstringDateFormat(), s_dateToCompareForTestNow, 'Test sans paramètres');
			var s_dateTest = new Date(2000, 11, 1);
			var s_dateToCompareForTest = s_dateTest.getFullYear() + '' + (globazNotation.utilsDate._format(s_dateTest.getMonth() + 1)) + '' + globazNotation.utilsDate
					._format(s_dateTest.getDate());// string pour compare
			/* 2 */
			equals(globazNotation.utilsDate.convertJSDateToDBstringDateFormat(s_dateTest), s_dateToCompareForTest, 'Test avec paramètres');
			/* 3 */
			raises(function () {globazNotation.utilsDate.convertJSDateToDBstringDateFormat("1212"); }, "Test avec paramètres mal formatée");
			/* 4 */
			equals(globazNotation.utilsDate.convertJSDateToDBstringDateFormat(""), s_dateToCompareForTestNow, 'Test avec paramètres vide');
			/* 5 */
			equals(globazNotation.utilsDate.convertJSDateToDBstringDateFormat(null), s_dateToCompareForTestNow, 'Test avec paramètres null');
		});

		test("areDatesSame", function () {
			var d_dateToTest1 = "01.01.2000";
			var d_dateToTest2 = "31.12.1999";
			var d_dateToTest3 = "01.01.2000";
			/* 1 */
			equals(globazNotation.utilsDate.areDatesSame(d_dateToTest1, d_dateToTest2), false, "Test valeur différentes");
			/* 2 */
			equals(globazNotation.utilsDate.areDatesSame(d_dateToTest1, d_dateToTest3), true, "Test valeur identiques");
		});

		test("isDateBefore", function () {
			var d_dateToTestFuture = "01.01.2000";
			var d_dateToTestPast = "31.12.1999";
			/* 1 */
			equals(globazNotation.utilsDate.isDateBefore(d_dateToTestFuture, d_dateToTestPast), false, "Test valeur dans le futur");
			/* 2 */
			equals(globazNotation.utilsDate.isDateBefore(d_dateToTestPast, d_dateToTestFuture), true, "Test valeur dans le passé");
		});

		test("isDateBeforeNow", function () {
			var d_dateToTestFuture = "12.12.2999";
			var d_dateToTestPast = "12.12.2009";
			/* 1 */
			equals(globazNotation.utilsDate.isDateBeforeNow(d_dateToTestFuture), false, "Test valeur dans le futur");
			/* 2 */
			equals(globazNotation.utilsDate.isDateBeforeNow(d_dateToTestPast), true, "Test valeur dans le passé");
		});

		test("isDateAfter", function () {
			var d_dateToTestFuture = "01.01.2000";
			var d_dateToTestPast = "31.12.1999";
			/* 1 */
			equals(globazNotation.utilsDate.isDateAfter(d_dateToTestFuture, d_dateToTestPast), true, "Test valeur dans le futur");
			/* 2 */
			equals(globazNotation.utilsDate.isDateAfter(d_dateToTestPast, d_dateToTestFuture), false, "Test valeur dans le passé");
		});

		test("isDateAfterNow", function () {
			var d_dateToTestFuture = "12.12.2999";
			var d_dateToTestPast = "12.12.2009";
			/* 1 */
			equals(globazNotation.utilsDate.isDateAfterNow(d_dateToTestFuture), true, "Test valeur dans le futur");
			/* 2 */
			equals(globazNotation.utilsDate.isDateAfterNow(d_dateToTestPast), false, "Test valeur dans le passé");
		});

		test("convertJSDateToGlobazStringDateFormat", function () {
			var s_dateTestNow = new Date();// date now
			var s_dateToCompareForTestNow = globazNotation.utilsDate._format(s_dateTestNow.getDate()) + "." + globazNotation.utilsDate._format(s_dateTestNow.getMonth() + 1) + "." + s_dateTestNow
					.getFullYear();// string pour compare
			/* 1 */
			equals(globazNotation.utilsDate.convertJSDateToGlobazStringDateFormat(), s_dateToCompareForTestNow, 'Test sans paramètres');
			var s_dateTest = new Date(2000, 11, 1);
			var s_dateToCompareForTest = globazNotation.utilsDate._format(s_dateTest.getDate()) + "." + globazNotation.utilsDate._format(s_dateTest.getMonth() + 1) + "." + s_dateTest
					.getFullYear();// string pour compare
			/* 2 */
			equals(globazNotation.utilsDate.convertJSDateToGlobazStringDateFormat(s_dateTest), s_dateToCompareForTest, 'Test avec paramètres');
			/* 3 */
			raises(function () {globazNotation.utilsDate.convertJSDateToGlobazStringDateFormat("131s"); }, 'Test avec paramètres mal formatée');
			/* 4 */
			equals(globazNotation.utilsDate.convertJSDateToGlobazStringDateFormat(""), s_dateToCompareForTestNow, 'Test avec paramètres vide');
			/* 5 */
			equals(globazNotation.utilsDate.convertJSDateToGlobazStringDateFormat(null), s_dateToCompareForTestNow, 'Test avec paramètres null');
		});
		
		test("countMonth", function () {
			equals(globazNotation.utilsDate.countMonth("01.01.2011", "31.01.2011"), 0);
			equals(globazNotation.utilsDate.countMonth("01.01.2011", "01.02.2011"), 1);
			equals(globazNotation.utilsDate.countMonth("01.01.2011", "01.05.2011"), 4);
			equals(globazNotation.utilsDate.countMonth("01.05.2011", "01.01.2011"), 4);
			equals(globazNotation.utilsDate.countMonth("10.01.2011", "01.12.2011"), 10);
			equals(globazNotation.utilsDate.countMonth("10.01.2011", "10.12.2011"), 11);
			equals(globazNotation.utilsDate.countMonth("10.01.2011", "11.12.2011"), 11);
			equals(globazNotation.utilsDate.countMonth("24.06.2011", "24.01.2012"), 7);
			equals(globazNotation.utilsDate.countMonth("10.01.2011", "09.12.2012"), 22);
			equals(globazNotation.utilsDate.countMonth("10.01.2011", "10.12.2012"), 23);
			equals(globazNotation.utilsDate.countMonth("10.01.2011", "11.12.2012"), 23);
			
			raises(function () {globazNotation.utilsDate.countMonth("2011", "11.12.2012"); }, "must throw error to pass");
			raises(function () {globazNotation.utilsDate.countMonth("", "11.12.2012"); }, "must throw error to pass");

		});
		
		
	},


	
	initInput: function () {
		module("utils:input");
	},

	initTemplate: function () {
		module("utils:template");
		var s_2each ='<div id="exceptions">' +
							'<ul>' +
								'{{@each exceptions}}' + 
								'<li>{{detailMessage}}</li>' +
								'{{/@each exceptions}}' + 
							'</ul>' +
						'</div>' + 
						'<table width="100%" class="" style="font-size: 9px;">' + 				
							'<tbody>' + 
								'{{@each stackTraceElements}}' + 
								'<tr class="{{@odd odd}}" >' + 
									'<td>{{declaringClass}}</td>' + 
									'<td>{{methodName}}</td>' +
									'<td>{{fileName}}</td>' + 
									'<td>{{lineNumber}}</td>' + 
								'</tr>' + 
								'{{/@each stackTraceElements}}' + 
							'</tbody>' + 
						'</table>';

		var o= {"exceptions":[
		                      {"detailMessage":"mes1"},
		                      {"detailMessage":"mes2"},
		                      {"detailMessage":"mes3"}
		                      ],
		         "stackTraceElements": [
		                                {declaringClass:"c1",methodName:"m1",fileName:"f1",lineNumber:1},
		                                {declaringClass:"c2",methodName:"m2",fileName:"f2",lineNumber:1},
		                                {declaringClass:"c3",methodName:"m3",fileName:"f3",lineNumber:1}
		                                ]
				};
		
		//
		test("each", function () {
			var html = globazNotation.template.compile(o, s_2each);
			equals(html, '<div id="exceptions"><ul><li>mes1</li><li>mes2</li><li>mes3</li></ul></div><table width="100%" class="" style="font-size: 9px;"><tbody><tr class="" ><td>c1</td><td>m1</td><td>f1</td><td>1</td></tr><tr class="odd" ><td>c2</td><td>m2</td><td>f2</td><td>1</td></tr><tr class="" ><td>c3</td><td>m3</td><td>f3</td><td>1</td></tr></tbody></table>', 'Test avec 2 each');
		});
		 
	},
	
	initUtils: function () {
		module("utils:utils");
		test("isString", function () {
			ok(globazNotation.utils.isString('xyz'), "Test avec xyz avec simple quote");
			ok(globazNotation.utils.isString("xyz"), "Test avec xyz avec double quote ");
			ok(!globazNotation.utils.isString({
				p: 1,
				p2: 2
			}), "Test avec {p:1,p2:2}");
			ok(!globazNotation.utils.isString([]), "Test avec []");
		});

		test("isEmpty", function () {
			/* 1 */
			ok(!globazNotation.utils.isEmpty('0.00'), "Test avec '00.0'");
			/* 2 */
			ok(!globazNotation.utils.isEmpty(0), "Test avec 0 ");
			/* 3 */
			ok(!globazNotation.utils.isEmpty('0'), "Test avec '0' ");
			/* 4 */
			ok(globazNotation.utils.isEmpty(''), "Test avec '00.0'");
			/* 5 */
			ok(globazNotation.utils.isEmpty('null'), "Test avec 'null'");
			/* 6 */
			ok(globazNotation.utils.isEmpty(null), "Test avec null");
			/* 7 */
			ok(globazNotation.utils.isEmpty('    '), "Test avec '    '");
			/* 8 */
			ok(globazNotation.utils.isEmpty(undefined), "Test avec undefined");
			/* 9 */
			ok(globazNotation.utils.isEmpty('undefined'), "Test avec 'undefined'");
			/* 10 */
			// ok(globazNotation.utils.isEmpty([]),"Test avec []");
			/* 11 */
			ok(!globazNotation.utils.isEmpty([1, 2, 'test']), "Test avec [1,2,'test']");
			/* 12 */
			// ok(globazNotation.utils.isEmpty({}),"Test avec {}");
			/* 13 */
			ok(!globazNotation.utils.isEmpty({
				p: 1,
				p2: 2
			}), "Test avec {p:1,p2:2}");
		});
	},

	initFormatter: function () {
		module("utils:formatter");
		test("formatStringToAmout", function () {
			var stringToAmout = globazNotation.utilsFormatter.formatStringToAmout;
			/* 1 */
			equals(stringToAmout(000000), "0.00", 'Test avec 00000 en integer');
			/* 2 */
			equals(stringToAmout(10000), "10'000.00", 'Test avec 10000 en integer');
			/* 3 */
			equals(stringToAmout("0000.00"), "0.00", 'Test avec 0000.00 en string');
			/* 4 */
			equals(stringToAmout("10000"), "10'000.00", 'Test avec 10000 en string');
			/* 5 */
			equals(stringToAmout("10'000"), "10'000.00", "Test avec 10'000 en string");
			/* 5 */
			equals(stringToAmout("10'000$"), "", "Test avec 10'000$ en string");
			/* 6 */
			equals(stringToAmout("un deux 3"), "", "Test avec 'un deux 3'");
		});
		
		test("fromateSpy", function () {
			var fromateSpy = globazNotation.utilsFormatter.fromateSpy;
			/* 1 */
			equals(fromateSpy("20141201084512Test"), "01.12.2014,08:45:12 - Test", 'Test avec 00000 en integer');
			equals(fromateSpy("Test20141201084512"), "01.12.2014,08:45:12 - Test", 'Test avec 00000 en integer');

		});
	},
	
	initReadWidget: function () {
		module("notationUtils:readWidget");
		
		asyncTest("TestMessageError", function () {
			var options,  ajax;
			options = {
					serviceClassName: "ch.globaz.jade.noteIt.business.service.JadeNoteService",
					criterias: '',
					cstCriterias: '',
					serviceMethodName: "search",
					parametres: "s",
					callBack: function (data) {

					},
					errorCallBack: function (data) {
						if(data.errorBean){
					    ok(data.errorBean, "Has object errorBean");
					    ok(data.errorBean.exceptions, "Has object exceptions");
					    ok(data.errorBean.messages, "Has object message");
					    ok(data.errorBean.stack, "Has stack");
						}
						start();
					}
				};

			ajax = Object.create($.extend(true, {}, globazNotation.readwidget));
			ajax.options = options;
			ajax.read();
		});
	}
};

var util = {
	testObjExistant: function (objName) {
		test("Objets:" + objName, function () {
			var obj = Object.create(globazNotation[objName]);
			ok((typeof obj.init === 'function'), 'Test si il existe la fonction init');
			ok((typeof obj.options === 'object'), 'Test si il existe les options');
			ok((typeof obj.description === 'string'), 'Test si il existe une description');
			ok((typeof obj.descriptionOptions === 'object'), 'Test si il existe descriptionOptions');
			ok((typeof obj.forTagHtml === 'string'), 'Test si il existe forTagHtml');
			obj = null;
		});
	},
	count: function (obj) {
		var i = 0;
		for (var f in obj) {
			i += 1;
		}
		return i;
	},

	resteValueTo: function (o_magnagerNotation) {
		o_magnagerNotation.hasError = false;
		o_magnagerNotation.elementsInError = [];
		// notationManager.showErrors = false;
	},

	createElementToTest: function () {
		var element = document.createElement("input");
		var newAttr = document.createAttribute("data-g-amount");
		element.setAttributeNode(newAttr);
		return element;
	}

};

var logs = function (b_log) {
	var begin = 0, moduleStart = 0, moduleDone = 0, 
		testStart = 0, testDone = 0, log = 0, moduleContext, 
		moduleDoneContext, testContext, testDoneContext, logContext;
	
	b_log = b_log || false;

	QUnit.begin = function () {
		begin++;
	};
	QUnit.done = function () {
	};
	QUnit.moduleStart = function (context) {
		moduleStart++;
		moduleContext = context;
	};
	QUnit.moduleDone = function (context) {
		moduleDone++;
		moduleDoneContext = context;
	};
	QUnit.testStart = function (context) {
		testStart++;
		testContext = context;
	};
	QUnit.testDone = function (context) {
		testDone++;
		testDoneContext = context;
	};
	QUnit.log = function (context) {
		log++;
		logContext = context;
	};

	var logs = ["begin", "testStart", "testDone", "log", "moduleStart", "moduleDone", "done"];
	if (b_log && typeof console !== "undefined") {
		for (var i = 0; i < logs.length; i++) {
			(function () {
				var log = logs[i], logger = QUnit[log];
				QUnit[log] = function () {
					console.log(log, arguments);
					logger.apply(this, arguments);
				};
			})();
		}
	}
};


/*-----------------Test avec quinit---------------------------------------*/

$(document)
		.ready(function () {
			logs(false);

			testUtils.iniTest();

			module("notationManager:core");

			test('Convertion de valeur automatique', function () {
				deepEqual(notationManager.convertSrting('0'), 0, 'Test number 0');
				deepEqual(notationManager.convertSrting('10'), 10, 'Test number');
				deepEqual(notationManager.convertSrting('true'), true, 'Test boolean');
				deepEqual(notationManager.convertSrting('s_string'), 's_string', 'Test string');
				deepEqual(notationManager.convertSrting('1string2#'), '1string2#', 'Test string avec nombre');
				deepEqual(notationManager.convertSrting('[1,3,4,5]'), [1, 3, 4, 5], 'Avec tableau');
				deepEqual(notationManager.convertSrting('{t:[1,3,4,5],s:"string"}'), {
					t: [1, 3, 4, 5],
					s: "string"
				}, 'Avec objet');
				deepEqual(typeof notationManager.convertSrting('function(){}'), 'function', 'Test function');
			});

			test('Test format options', function () {
				deepEqual(notationManager.isOptionValidFormat(true, true), true, "test boolean");
				deepEqual(notationManager.isOptionValidFormat(null, 8), true, 'Test avec null');
				deepEqual(notationManager.isOptionValidFormat(1, 4), true, 'Test avec number ');
				deepEqual(notationManager.isOptionValidFormat([], [1, 2]), true, 'Test avec un tabeleau valeur simple');
				deepEqual(notationManager.isOptionValidFormat([], {
					test: 1
				}), false, 'Test avec un tabeleau mais faux ');
				deepEqual(notationManager.isOptionValidFormat({}, {
					test: 1
				}), true, 'Test avec un objet ');
				deepEqual(notationManager.isOptionValidFormat([], {
					test: 1
				}), false, 'Test avec un objet mais faux ');
				deepEqual(notationManager.isOptionValidFormat(function () {
				}, function () {
				}), true, 'Test avec function');
				deepEqual(notationManager.isOptionValidFormat('string', 4), true, 'Test avec (Default value) String et un nombre  ');
				deepEqual(notationManager.isOptionValidFormat(80, 'tes22'), false, 'Test avec un (Default value)nombre et un string, mais faux ');
				deepEqual(notationManager.isOptionValidFormat(true, 'tes22'), false, 'Test avec (Default value)boolean et un string, mais faux ');
				deepEqual(notationManager.isOptionValidFormat(function () {
				}, 'function(){}'), false, 'Test avec fonction mais faux ');

			});

			test('Convertion de strucure conditionnel en boolean ', function () {
				deepEqual(notationManager.execStructureConditionnelle('(true==true)'), true, 'Structures conditionnelles simple');
				deepEqual(notationManager.execStructureConditionnelle('(true==true&&3>1||true==true)'), true, 'Structures conditionnelles complexe');
				deepEqual(notationManager.execStructureConditionnelle('(true==true;alert())'), '(true==true;alert())', 'Test simple avec erreur');
			});

			test('Convertion de string en array', function () {
				deepEqual(notationManager.toArray('[1,2,3]'), [1, 2, 3], 'Normal width number in string');
				deepEqual(notationManager.toArray('["1","2","3"]'), ['1', '2', '3'], 'Avec cote mais des number');
				deepEqual(notationManager.toArray('["TOT","TAT","TIT"]'), ["TOT", "TAT", "TIT"], 'Normal avec string ');
				deepEqual(notationManager.toArray('1,2,3'), null, 'Pas un tableau avec valeur simple');
				deepEqual(notationManager.toArray('function(){var t = [1,2,3]}'), null, 'Pas un tabelau avec valeur comptlex');
			});

			test('Convertion de string en variable', function () {
				
				/*Test avec des variables globales
				 * */
				toto = 'test';
				tab = [1, 2, 3, 4];
				deepEqual(notationManager.string_to_js_variable('toto'), 'test', 'Normal width variable string');
				deepEqual(notationManager.string_to_js_variable('tab'), [1, 2, 3, 4], 'Normal width variable array');
			});

			test("Options mandatory", function () {

				var objNotation = {
					descriptionOptions: {
						selectorForClose: {
							desc: "Des",
							param: "param",
							mandatory: true,
							type: String
						},
						reLoad: {
							desc: "desc",
							param: "true(default), false"
						}
					},
					options: {
						selectorForClose: "",
						reLoad: true
					}
				};

				ok(notationManager.isOptionMandatory(objNotation, "selectorForClose"), 'With option mandatory');

				ok(!notationManager.isOptionMandatory(objNotation, "reLoad"), 'With not option mandatory');

				ok(notationManager.optionMandatoryIsOk(objNotation, {
					selectorForClose: "testParam"
				}, "selectorForClose"), 'With option mandatory');
				ok(!notationManager.optionMandatoryIsOk(objNotation, {
					selectorForClose: ""
				}, "selectorForClose"), 'With option mandatory');

				ok(notationManager.optionMandatoryIsOk(objNotation, {
					reLoad: ""
				}, "reLoad"), 'With not mandatory and not value in the option');
				ok(notationManager.optionMandatoryIsOk(objNotation, {
					reLoad: ""
				}, "reLoad"), 'With not mandatory and  value in the option');

			});

			test("Create options", function () {
				var newAttr = document.createAttribute("param1");
				var retrun = {};
				notationManager.o_objEnTraitement = document.createElement("input");
				deepEqual(notationManager.createOptions(newAttr), {}, 'Options vide');

				newAttr.nodeValue = "";
				deepEqual(notationManager.createOptions(newAttr), {}, 'Avec rien');

				newAttr.nodeValue = " ";
				deepEqual(notationManager.createOptions(newAttr), {}, 'Avec un espace');

				newAttr.nodeValue = "param1:1";
				deepEqual(notationManager.createOptions(newAttr), {
					param1: 1
				}, 'Avec numéro');

				newAttr.nodeValue = "param1:value";
				deepEqual(notationManager.createOptions(newAttr), {
					param1: "value"
				}, 'Avec string');

				newAttr.nodeValue = "param1:éèàäöÏÖïÉÈçÀûÛ2+()'><£!?$§°";
				deepEqual(notationManager.createOptions(newAttr), {
					param1: "éèàäöÏÖïÉÈçÀûÛ2+()'><£!?$§°"
				}, 'Avec string complexe');

				newAttr.nodeValue = "theParam1:value";
				deepEqual(notationManager.createOptions(newAttr), {
					theParam1: "value"
				}, 'Param With lowerCase');

				newAttr.nodeValue = "param:true";
				deepEqual(notationManager.createOptions(newAttr), {
					param: true
				}, 'Avec le booelan true');

				newAttr.nodeValue = "param:false";
				deepEqual(notationManager.createOptions(newAttr), {
					param: false
				}, 'Avec le booelan false');

				newAttr.nodeValue = "param:¦['tot','tat','tit']¦";
				deepEqual(notationManager.createOptions(newAttr), {
					param: ['tot', 'tat', 'tit']
				}, 'Avec tableau');

				newAttr.nodeValue = "param:¦ true==[1,2,3]¦";
				deepEqual(notationManager.createOptions(newAttr), {
					param: 'true==[1,2,3]'
				}, 'Avec tableau valeur false(null)');

				newAttr.nodeValue = "param:¦{valeur1:'toto',valeur2:'mon'}¦";
				deepEqual(notationManager.createOptions(newAttr), {
					param: {
						valeur1: 'toto',
						valeur2: 'mon'
					}
				}, 'Avec un objet valeur simple');

				newAttr.nodeValue = "param:¦{s:'String',n:1,t:[1,2,3],o:{s:'String',n:1,t:[1,2,3,'tess']}}¦";
				deepEqual(notationManager.createOptions(newAttr), {
					param: {
						s: 'String',
						n: 1,
						t: [1, 2, 3],
						o: {
							s: 'String',
							n: 1,
							t: [1, 2, 3, 'tess']
						}
					}
				}, 'Avec un objet, valeur complex');

				newAttr.nodeValue = "param:¦#id1,.class2,'  ={}<,>,[1,2,4]¦";
				deepEqual(notationManager.createOptions(newAttr), {
					param: "#id1,.class2,'  ={}<,>,[1,2,4]"
				}, 'Avec une valeur et des virgules');

				newAttr.nodeValue = "param:¦#id1,.class2,'  ={}<,>,[1,2,4]:2 compte : 123.34¦";
				deepEqual(notationManager.createOptions(newAttr), {
					param: "#id1,.class2,'  ={}<,>,[1,2,4]:2 compte : 123.34"
				}, 'Avec une valeur, des virgules, et les deux points ":"');

				newAttr.nodeValue = "param1:valeur1,param2:valeur2,param3:valeur3";
				deepEqual(notationManager.createOptions(newAttr), {
					param1: "valeur1",
					param2: "valeur2",
					param3: "valeur3"
				}, 'Avec plusieur valeurs simple');

				newAttr.nodeValue = "param1:valeur1, param2 :    valeur2,  param3: \n\r valeur3";
				deepEqual(notationManager.createOptions(newAttr), {
					param1: "valeur1",
					param2: "valeur2",
					param3: "valeur3"
				}, 'Avec plusieur valeurs avec des espaces');

				newAttr.nodeValue = "p:true, p2:v2,t:¦[1,2,3,'ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØŒŠþÙÚÛÜÝŸàáâãäåæçèéêëìíîïðñòóôõöøœšÞùúûüýÿ!;\+-?ˆ§<>¢ß¥£™©®ª×÷±²³¼½¾µ¿¶·¸º°¯§']¦,j:¦$(%,6,#,toto,=,<><\/,:6,3 : test)¦";
				deepEqual(notationManager.createOptions(newAttr), {
					p: true,
					p2: "v2", // éèàäöÏÖÔôïÉÈçÀûÛüÜ
					t: [1, 2, 3, 'ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖØŒŠþÙÚÛÜÝŸàáâãäåæçèéêëìíîïðñòóôõöøœšÞùúûüýÿ!;\+-?ˆ§<>¢ß¥£™©®ª×÷±²³¼½¾µ¿¶·¸º°¯§'],
					j: '$(%,6,#,toto,=,<><\/,:6,3 : test)'
				}, 'Avec plusieur valeurs complexe');
				/*
				 * newAttr.nodeValue = "p:true, p2:v2,t:¦Compte : 28817.72 Clearing : 81380 Swift : RAIFCH22XXX Raiffeisenbank Regio Weinfelden 8575 Bürglen TG Monsieur ZBINDEN
				 * Martin Märwilerstrasse 4 9517 Mettlen¦"; deepEqual(notationManager.createOptions(newAttr), { p: true, p2: "v2", t: [1, 2, 3, 'éèàäöÏÖÔôïÉÈçÀûÛ'], j:
				 * '$(%,6,#,toto,=,<><\/,:6,3 : test)' }, 'Avec plusieur valeurs complexe');
				 */

				newAttr.nodeValue = "callback:¦function(){return true}¦";
				retrun = notationManager.createOptions(newAttr);
				ok(retrun.callback(), 'Avec fonction');
			});

			test("Test si la notation peu être utilisé sur un element html ", function () {
				ok(notationManager.isObjectApllyToElement('input', 'input'), 'Test access simple');
				ok(notationManager.isObjectApllyToElement('input', 'div') === false, 'Test access simple non egale');
				ok(notationManager.isObjectApllyToElement('*', 'div'), 'Test acces *');
				ok(notationManager.isObjectApllyToElement('*', 'INPUT'), 'Test acces *');
				ok(notationManager.isObjectApllyToElement('div,span', 'span'), 'Test access multiples');
				ok(notationManager.isObjectApllyToElement('div,span', 'input') === false, 'Test access multiple non egale');
				ok(notationManager.isObjectApllyToElement('*', 'div'), 'Test access * ');
				ok(notationManager.isObjectApllyToElement('*,!input', 'div'), 'Test access * avec une negation');
				ok(notationManager.isObjectApllyToElement('*,!input,!ul,!li,!h1', 'div'), 'Test access * avec plusieurs negation');
				ok(notationManager.isObjectApllyToElement('*,!input', 'input') === false, 'Test access * avec une negation, doit étre non égale');
				ok(notationManager.isObjectApllyToElement('*,!input,!ul,!li,!h1', 'input') === false, 'Test access * avec une plusieurs, doit étre non égale');
				ok(notationManager.isObjectApllyToElement('*,!input , ! ul  ,!li ,!h1', 'input') === false, 'Test access * avec une plusieurs (avec espaces), doit étre non égale');
			});

			test("Call object to create ", function () {
				var element = document.createElement("input");
				var newAttr = document.createAttribute("data-g-amount");
				element.setAttributeNode(newAttr);
				var obj = notationManager.createObjNotation(newAttr, $(element));
				deepEqual(obj, globazNotation.amount, 'Object create');
			});

			test("Initilisations des options ", function () {
				var element = document.createElement("input");
				var newAttr = document.createAttribute("data-g-amount");
				element.setAttributeNode(newAttr);
				var $element = $(element);
				var obj = null;
				var o_objNotation = notationManager.createObjNotation(newAttr, $element);

				obj = notationManager.addOptionsToObjectNotation(o_objNotation, newAttr, $element);
				equals(obj.options, globazNotation.amount.options, 'Test options object');

				// for ie 6 and 7 :(
				element.removeAttributeNode(newAttr);

				newAttr.nodeValue = "mandatory:true";
				element.setAttributeNode(newAttr);
				obj = notationManager.addOptionsToObjectNotation(o_objNotation, newAttr, $element);
				ok(obj !== null, 'Non null');
				ok((typeof obj === 'object'), 'Is an Object');
				equals(obj.options.mandatory, true, 'Test options');
				equals(obj.options.addSymboleMandatory, true, 'Test valeurs par défaut');
				equals((util.count(obj.options)), 6, 'Nb Options');
				deepEqual(obj.options, {
					mandatory: true,
					addSymboleMandatory: true,
					blankAsZero: true,
					unsigned: false,
					icon: '',
					periodicity:''
				}, 'Test options by elements');

				// for ie 6 and 7 :(
				element.removeAttributeNode(newAttr);
				newAttr.nodeValue = "mandatory:true==true";
				element.setAttributeNode(newAttr);
				obj = notationManager.addOptionsToObjectNotation(o_objNotation, newAttr, $element);
				deepEqual(obj.options, {
					mandatory: true,
					addSymboleMandatory: true,
					stringConditionnel: "true==true",
					blankAsZero: true,
					unsigned: false,
					icon: '',
					periodicity:''
				}, 'Test options by elements and strucure conditionelle');

			});

			test("Initilisations des objets ", function () {
				var element = document.createElement("input");
				var newAttr = document.createAttribute("data-g-amount");
				element.setAttributeNode(newAttr);
				var $element = $(element);
				var o_objNotation = notationManager.createObjNotation(newAttr, $element);
				o_objNotation = notationManager.addAttributAndMethodeToObjNotation(o_objNotation, $element);

				ok(globazNotation.utils.isJQueryObject(o_objNotation.$elementToPutObject), 'vérifie que $elementToPutObject est bien un objet jQuery');

				var obj_amount = Object.create(globazNotation.amount);

				obj_amount.s_contextUrl = notationManager.s_contextUrl;
				obj_amount.getImage = function (s_imgName) {
					// return this.getImage.call(that,s_imgName);
				};
				obj_amount.$elementToPutObject = $element;
				obj_amount.utils = globazNotation.utils;
				obj_amount.utils.input = globazNotation.utilsInput;
				obj_amount.utils.formatter = globazNotation.utilsFormatter;
				obj_amount.utils.date = globazNotation.utilsDate;
				obj_amount.s_contextUrl = "/webavs";

				obj_amount.putError = notationManager.addError;
				obj_amount.putFormattedError = notationManager.putFormattedError;
				obj_amount.createParams = notationManager.createOptionsForObj;
				obj_amount.i18n = notationManager.i18n;

				var obj = notationManager.initObjNotation(o_objNotation, "amount", $(element));

				// deepEqual(obj, obj_amount, 'Oject is initilised');
				// equal(obj, obj_amount, 'Oject is initilised');
				var element1 = document.createElement("div");
				var newAttr1 = document.createAttribute("data-g-amount"); 
				element1.setAttributeNode(newAttr1);
				var obj1 = notationManager.initObjNotation(o_objNotation, "amount", $(element1));
				deepEqual(obj1, null, 'Object is not initilised');
			});

			test("Add js to 1 element", function () {
				var element = document.createElement("input");
				var newAttr = document.createAttribute("data-g-amount");
				var maNo = Object.create(notationManager);
				
				element.setAttributeNode(newAttr);

				var objWithOutParam = maNo.addObjToElement(element);
				var $element = $(element);
				var size = 0;
				notationManager.iterOnElementsWhoHasNotation(function (a,b,c,d,e) {
					size++
				},$element);
				equals(size, 1, 'Objects found');
				$element.removeData("notation_amount");
				
				
				// for ie 6 and 7

				element.removeAttributeNode(newAttr);

				newAttr.nodeValue = "mandatory:true";
				element.setAttributeNode(newAttr);
				var objWithParam = maNo.addObjToElement(element);
				// diffcile de recréer la même inctance de jquery workAround
				//globazNotation.amount.$elementToPutObject =$element.data('notationInfos_amount').$element;
				// deepEqual(maNo.elementsWhoHasNotation[0].o_obj, globazNotation.amount, 'Object ');
			
				ok(objWithParam !== null, 'Non null');
				ok((typeof objWithParam === 'object'), 'Is an Object');
				equals(objWithParam.options.mandatory, true, 'Test options (param)');
				equals(objWithParam.options.addSymboleMandatory, true, 'Test valeurs par défaut');
				
				equals($element.data('notation_technical_amount').s_nameObj, "amount", 'Element html');
			});

			test("Add js to all elements", function () {
				var size = 0;
				notationManager.iterOnElementsWhoHasNotation(function (a,b,c,d,e) {
					size++;
				});
				equals(size, 129, 'objets trouvé');
			});

			test("Erreurs possibles", function () {

				//ok(notationManager.hasError, 'Erreur détecté dans les éléments de la pages ');

				// element = util.createElementToTest();
				var element = document.createElement("input");
				var newAttr = document.createAttribute("data-g-amount1");
				newAttr.nodeValue = "mandatory:true";
				element.setAttributeNode(newAttr);

				var maNo = Object.create(notationManager);
				util.resteValueTo(maNo);

				var o_objNotation = maNo.addObjToElement(element);
				ok(maNo.hasError, 'Erreur détecté dans la définition du nom de la notation ' + maNo.elementsInError[0]);

				util.resteValueTo(maNo);
				element.removeAttributeNode(newAttr);
				newAttr = document.createAttribute("data-g-amount");
				newAttr.nodeValue = "mendatory:true";
				element.setAttributeNode(newAttr);
				o_objNotation = maNo.addObjToElement(element);
				ok(maNo.hasError, 'Erreur détecté dans la définition des options ' + maNo.elementsInError[0].message);

				var element1 = document.createElement("div");
				util.resteValueTo(maNo);
				newAttr = document.createAttribute("data-g-amount");
				newAttr.nodeValue = "mandatory:true";
				element1.setAttributeNode(newAttr);
				o_objNotation = maNo.addObjToElement(element1);
				ok(maNo.hasError, 'Erreur détecté mauvais element ' + maNo.elementsInError[0].message);

				var element2 = document.createElement("input");
				util.resteValueTo(maNo);
				newAttr = document.createAttribute("data-g-amount");
				newAttr.nodeValue = "mandatory:0";
				element2.setAttributeNode(newAttr);
				o_objNotation = maNo.addObjToElement(element2);
				ok(maNo.hasError, 'Erreur détecté mauvais type ' + maNo.elementsInError[0].message);

				//
				util.resteValueTo(maNo);
				element2.removeAttributeNode(newAttr);
				newAttr = document.createAttribute("data-g-amount");
				newAttr.nodeValue = "mandatory:true2=false2;alert";
				element2.setAttributeNode(newAttr);
				$(element2).removeData("notation_amount");
				o_objNotation = maNo.addObjToElement(element2);
				ok(maNo.hasError, 'Erreur non sutrucure conditionnel ' + maNo.elementsInError[0].message);

				ok(maNo.hasManyComma('mandatory:aaaa,alert,ss'), 'Erreur virgules simple');
				ok(maNo.hasManyComma('mandatory:aaaa,alert,ss,Toto:test'), 'Erreur virgules ');

				ok(!maNo.hasManyComma('param1:"param1:valeur1, param2 :    valeur2,  param3: \n\r valeur3'), 'Test non erreur virgule');
				ok(!maNo.hasManyComma('callback:¦function(){return true}¦'), 'Test avec une fonction');
				ok(!maNo.hasManyComma('param:¦{s:"String",n:1,t:[1,2,3],o:{s:"String",n:1,t:[1,2,3,"tess"]}}¦'), 'Erreur virgules complex');
				ok(!maNo.hasManyComma('dispatcher:(54545454==545 && true=true),actionTrue:¦show(),mendatory()¦,actionFalse:¦clear(),show()¦'), 'Erreur virgules complex avec plusieurs paramétres');
			});

			test("Execution de notation par js", function () {
				var element = document.createElement("input");
				var objNotation = notationManager.callObjectToUseOnJS("amount",{mandatory:true},element);
				ok(objNotation.options.mandatory, "Test l'option mandatory");
				var maNo = Object.create(notationManager);
				util.resteValueTo(maNo);
			    objNotation = maNo.callObjectToUseOnJS("amount",{mandatory2:true},element);
				ok(maNo.hasError, "Test erreur dans nom de l'option");
			});
			
			
			module("I18N");
			test("fonction", function () {
				// notationManager.=test
				equals(jQuery.i18n.prop('test.msg_simple'), "Hello notation", 'Test avec message simple');
				equals(jQuery.i18n.prop('test.specialChar'), "éèäàöü[]ô", 'test mesage avec carctères spéciale');
				equals(jQuery.i18n.prop('test.msg_complex', ['Notation']), "Good morning Notation!", 'test avec variable');
			});

			module("AJAX");
			test("jsonToString", function () {
				// notationManager.=test
				var t_tableJson = [{
					id: 1,
					libelle: 'Titi',
					multiplicateur: 2,
					montantDevise: "150.00",
					devise: 'EUR'
				}, {
					id: 2,
					libelle: 'Tata',
					multiplicateur: 4,
					montantDevise: "1'000.00",
					devise: 'CHF'
				}, {
					id: 3,
					libelle: 'Toto',
					multiplicateur: 6,
					montantDevise: "3'500.50",
					devise: '$'
				}],

				s_tableJson = "[{'id':1,'libelle':'Titi','multiplicateur':2,'montantDevise':'150.00','devise':'EUR'}," + "{'id':2,'libelle':'Tata','multiplicateur':4,'montantDevise':'1\\'000.00','devise':'CHF'}," + "{'id':3,'libelle':'Toto','multiplicateur':6,'montantDevise':'3\\'500.50','devise':'$'}]";
				equals(ajaxUtils.jsonToString(t_tableJson), s_tableJson, 'Contient un tableau');

				var t_tableJsonWithInsideTalble = [{
					'id': 1,
					'data': [1, 2, 3, 5],
					'devise': 'EUR'
				}, {
					'id': 1,
					'data': [1, 2, 3, 5],
					'devise': 'CHF'
				}, {
					'id': 1,
					'data': [1, 2, 3, 5],
					'devise': '$'
				}];

				var s_tableJsonWithInsideTalble = "[{'id':1,'data':[1,2,3,5],'devise':'EUR'}," + "{'id':1,'data':[1,2,3,5],'devise':'CHF'}," + "{'id':1,'data':[1,2,3,5],'devise':'$'}]";
				equals(ajaxUtils.jsonToString(t_tableJsonWithInsideTalble), s_tableJsonWithInsideTalble, 'Contient un tableau avec un objet qui contient un tableau');

				t_tableJsonWithInsideTalble = [{
					id: 1,
					data: [{
						id: 11,
						libelle: 'Toto'
					}, {
						id: 1,
						libelle: 'Tata'
					}],
					'devise': 'EUR'
				}, {
					id: 2,
					data: [{
						id: 22,
						libelle: 'Tata'
					}, {
						id: 2,
						libelle: 'Tata'
					}],
					'devise': 'CHF'
				}, {
					id: 3,
					data: [{
						id: 33,
						libelle: 'Titi'
					}, {
						id: 3,
						libelle: 'Tata'
					}],
					'devise': '$'
				}];

				s_tableJsonWithInsideTalble = "[{'id':1,'data':[{'id':11,'libelle':'Toto'},{'id':1,'libelle':'Tata'}],'devise':'EUR'}," + "{'id':2,'data':[{'id':22,'libelle':'Tata'},{'id':2,'libelle':'Tata'}],'devise':'CHF'}," + "{'id':3,'data':[{'id':33,'libelle':'Titi'},{'id':3,'libelle':'Tata'}],'devise':'$'}]";
				equals(ajaxUtils.jsonToString(t_tableJsonWithInsideTalble), s_tableJsonWithInsideTalble, 'Contient un tableau avec un objet qui contient un tableau dont les données sont des objets');

				var o_json = {
					id: 1,
					libelle: "la l''''..",
					devise: 'EUR',
					montant: "1'500"
				};
				var s_json = "{'id':1,'libelle':'la l\\'\\'\\'\\'..','devise':'EUR','montant':'1\\'500'}";
				equals(ajaxUtils.jsonToString(o_json), s_json, 'Contient un objets simple');

				var o_jsonComplex = {
					id: 1,
					libelle: "la l''''..",
					devise: 'EUR',
					montant: "1'500",
					objet: {
						id: 1,
						data: "test"
					}
				};
				var s_jsonComplex = "{'id':1,'libelle':'la l\\'\\'\\'\\'..','devise':'EUR','montant':'1\\'500','objet':{'id':1,'data':'test'}}";
				equals(ajaxUtils.jsonToString(o_jsonComplex), s_jsonComplex, 'Contient un objets simple');
				
				
				var o_jsonComplex = {
						id: 1,
						libelle: null,
						devise: 'EUR',
						montant: "1'500",
						list:null
					};
					var s_jsonComplex = "{'id':1,'libelle':null,'devise':'EUR','montant':'1\\'500','list':null}";
					equals(ajaxUtils.jsonToString(o_jsonComplex), s_jsonComplex, 'Contient un objets simple');


			});

			module("StructureObjets");
			for (var f in globazNotation) {
				// testObjExistant(f);
				if (f.toLowerCase().indexOf('utils') < 0) {
					if(f.type){
						util.testObjExistant(f);
					}
				}
			}

			var $dialogue = $('#testInput').dialog({
				width: 750,
				autoOpen: false,
				title: 'Elements tested'
			});
			$('#affiche').click(function () {
				$dialogue.dialog("open");
			});
			
			module("jqueryPluginNotation");
			
			test("amount",function () {
				
				$input = $("<input>", {
					type:"text"
				});
				//$("html").append($input);
				var notationAmount =  $input.notationAmount();

				ok(notationAmount.init, 'Test si il y a bien la fonction init');
				ok(!notationAmount.options.mandatory, 'Test si l\' option mandatory et bien a false');
				
				notationAmount =  $input.notationAmount({mandatory:true});
				ok(notationAmount.options.mandatory, 'Test si l\' option mandatory et bien a true');

			});
		});