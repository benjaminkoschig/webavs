package ch.globaz.corvus.domaine.constantes;

import globaz.jade.client.util.JadeStringUtil;

/**
 * <p>
 * Codes pour les infirmit�s.
 * </p>
 * <p>
 * Tir� du manuel de la conf�d�ration <a href="www.bsv.admin.ch/vollzug/storage/documents/3952/3952_6_fr.pdf">Codes pour
 * la statistique des infirmit�s et des prestations</a> (Version du 1er janvier 2012)
 * </p>
 */
public enum Infirmite {
    // @formatter:off

	/** Cicatrices cutan�es cong�nitales, lorsqu�une op�ration est n�cessaire (voir aussi chiffre 112) */
	INFIRMITE_101(52810101),
	/** Pt�rygion et syndactylies cutan�es */
	INFIRMITE_102(52810102),
	/** Kystes dermo�des cong�nitaux de l�orbite, de la racine du nez, du cou, du m�diastin et de la r�gion sacr�e */
	INFIRMITE_103(52810103),
	/** Dysplasies ectodermiques */
	INFIRMITE_104(52810104),
	/**
	 * Maladies bulleuses cong�nitales de la peau (Epidermolyse bulleuse h�r�ditaire, acrodermatite ent�ropathique et
	 * pemphigus chronique b�nin familial)
	 */
	INFIRMITE_105(52810105),
	/** Maladies ichthyosiformes cong�nitales et k�ratodermies palmo-plantaires h�r�ditaires */
	INFIRMITE_107(52810107),
	/**
	 * Naevi cong�nitaux, lorsqu�ils pr�sentent une d�g�n�rescence maligne ou lorsqu�une simple excision n�est pas
	 * possible en raison de la grandeur ou de la localisation
	 */
	INFIRMITE_109(52810109),
	/** Mastocytoses cutan�es cong�nitales (urticaire pigmentaire et mastocytose cutan�e diffuse) */
	INFIRMITE_110(52810110),
	/** Xeroderma pigmentosum */
	INFIRMITE_111(52810111),
	/** Aplasies t�gumentaires cong�nitales, lorsqu�une op�ration ou un traitement hospitalier est n�cessaire */
	INFIRMITE_112(52810112),
	/** Amastie cong�nitale et ath�lie cong�nitale */
	INFIRMITE_113(52810113),
	/** Chondrodystrophie (par exemple : achondroplasie, hypochondroplasie, dysplasie �piphysaire multiple) */
	INFIRMITE_121(52810121),
	/** Enchondromatose */
	INFIRMITE_122(52810122),
	/** Dysostoses cong�nitales */
	INFIRMITE_123(52810123),
	/** Exostoses cartilagineuses, lorsqu�une op�ration est n�cessaire */
	INFIRMITE_124(52810124),
	/**
	 * H�mihypertrophies et autres asym�tries corporelles cong�nitales, lorsqu�une op�ration est n�cessaire Infirmit�s
	 * cong�nitales Codes pour la statistique AI
	 */
	INFIRMITE_125(52810125),
	/** Osteogenesis imperfecta */
	INFIRMITE_126(52810126),
	/** Ost�op�trose */
	INFIRMITE_127(52810127),
	/** Dysplasie fibreuse */
	INFIRMITE_128(52810128),
	/** Lacunes cong�nitales du cr�ne */
	INFIRMITE_141(52810141),
	/** Craniosynostoses */
	INFIRMITE_142(52810142),
	/** Platybasie (impression basilaire) */
	INFIRMITE_143(52810143),	

	/**
	 * Malformations vert�brales cong�nitales (vert�bres tr�s fortement cun��formes, vert�bres soud�es en bloc type
	 * Klippel- Feil, vert�bres aplasiques et vert�bres tr�s fortement dysplasiques)
	 */
	INFIRMITE_152(52810152),
	/** C�tes cervicales, lorsqu�une op�ration est n�cessaire */
	INFIRMITE_161(52810161),
	/** Fissure cong�nitale du sternum */
	INFIRMITE_162(52810162),
	/** Thorax en entonnoir */
	INFIRMITE_163(52810163),
	/** Thorax en car�ne */
	INFIRMITE_164(52810164),
	/** Scapula alata congenita et d�formation de Sprengel */
	INFIRMITE_165(52810165),
	/** Torsion cong�nitale du sternum, lorsqu�une op�ration est n�cessaire */
	INFIRMITE_166(52810166),
	/** D�formations cong�nitales lat�rales de la paroi thoracique, lorsqu�une op�ration est n�cessaire */
	INFIRMITE_167(52810167),
	/** Coxa vara cong�nitale, lorsqu�une op�ration est n�cessaire */
	INFIRMITE_170(52810170),
	/** Coxa antetorta ou retrotorta cong�nitale, lorsqu�une op�ration est n�cessaire */
	INFIRMITE_171(52810171),
	/** Pseudarthroses cong�nitales des extr�mit�s */
	INFIRMITE_172(52810172),
	/** Am�lies, dysm�lies et phocom�lies */
	INFIRMITE_176(52810176),
	/**
	 * Autres d�fauts cong�nitaux et malformations cong�nitales des extr�mit�s, lorsqu�une op�ration, un appareillage ou
	 * un traitement par appareil pl�tr� sont n�cessaires
	 */
	INFIRMITE_177(52810177),
	/**
	 * Torsion tibiale interne et externe, lorsque l�enfant a quatre ans r�volus et pour autant qu�une op�ration soit
	 * n�cessaire
	 */
	INFIRMITE_178(52810178),
	/** Pied adductus ou pied metatarsus varus cong�nital, lorsqu�une op�ration est n�cessaires */
	INFIRMITE_180(52810180),
	/** Arthromyodysplasie cong�nitale (arthrogrypose) */
	INFIRMITE_181(52810181),
	/** Pied varus �quin cong�nital */
	INFIRMITE_182(52810182),
	/** Luxation cong�nitale de la hanche et dysplasie cong�nitale de la hanche */
	INFIRMITE_183(52810183),
	/** Dystrophie musculaire progressive et autres myopathies cong�nitales */
	INFIRMITE_184(52810184),
	/** Myasth�nie grave cong�nitale */
	INFIRMITE_185(52810185),
	/** Torticolis cong�nital, lorsqu�une op�ration est n�cessaire */
	INFIRMITE_188(52810188),
	/** Myosite ossifiante progressive cong�nitale */
	INFIRMITE_189(52810189),
	/** Aplasie et tr�s forte hypoplasie de muscles stri�s */
	INFIRMITE_190(52810190),
	/** T�nosynovite st�nosante cong�nitale */
	INFIRMITE_191(52810191),
	/** Adynamie �pisodique h�r�ditaire */
	INFIRMITE_192(52810192),
	/** Pied plat cong�nital, lorsqu�une op�ration ou un traitement par appareil pl�tre sont n�cessaires */
	INFIRMITE_193(52810193),
	/**
	 * Luxation cong�nitale du genou, lorsqu�une op�ration, un appareillage ou un traitement par appareil pl�tr� sont
	 * n�cessaires
	 */
	INFIRMITE_194(52810194),
	/** Luxation cong�nitale de la rotule, lorsqu�une op�ration est n�cessaire */
	INFIRMITE_195(52810195),
	/** Cheilo-gnatho-palatoschisis (fissure labiale, maxillaire, division palatine) */
	INFIRMITE_201(52810201),
	/** Fissures faciales, m�dianes, obliques et transverses cong�nitales */
	INFIRMITE_202(52810202),
	/** Fistules cong�nitales du nez et des l�vres */
	INFIRMITE_203(52810203),
	/** Proboscis lateralis */
	INFIRMITE_204(52810204),
	/**
	 * Dysplasies dentaires cong�nitales, lorsqu�au moins 12 dents de la seconde dentition apr�s �ruption sont tr�s
	 * fortement atteintes. En cas d�odontodysplasie (ghost teeth), il suffit qu�au moins deux dents dans un quadrant
	 * soient atteintes
	 */
	INFIRMITE_205(52810205),
	/**
	 * Anodontie cong�nitale totale ou anodontie cong�nitale partielle par absence d�au moins deux dents permanentes jux
	 * tapos�es ou de quatre dents permanentes par m�choire � l�exclusion des dents de sagesse
	 */
	INFIRMITE_206(52810206),
	/**
	 * Hyperodontie cong�nitale, lorsque la ou les dents surnum�raires provoquent une d�viation intramaxillaire ou
	 * intramandibulaire qui n�cessitent un traitement au moyen d�appareils
	 */
	INFIRMITE_207(52810207),
	/**
	 * Micromandibulie cong�nitale, lorsqu�elle entra�ne au cours de la premi�re ann�e de la vie des troubles de la
	 * d�glutition et de la respiration n�cessitant un traitement ou lorsque l�appr�ciation c�phalom�trique apr�s
	 * l�apparition des incisives d�finitives montre une divergence des rapports sagittaux de la m�choire mesur�e par un
	 * angle ANB de 9 degr�s et plus (respectivement par un angle ANB d�au moins 7 degr�s combin� � un angle
	 * maxillo-basal d�au moins 37 degr�s) ou lorsque les dents permanentes, � l�exclusion des dents de sagesse,
	 * pr�sentent une non occlusion d�au moins trois paires de dents antagonistes dans les segments lat�raux par moiti�
	 * de m�choire
	 */
	INFIRMITE_208(52810208),
	/**
	 * Mordex apertus cong�nital, lorsqu�il entra�ne une b�ance verticale apr�s �ruption des incisives permanentes et
	 * que l�appr�ciation c�phalom�trique montre un angle maxillobasal de 40 degr�s et plus (respectivement de 37 degr�s
	 * au moins combin� � un angle ANB de 7 degr�s et plus). Mordex clausus cong�nital, lorsqu�il entra�ne une
	 * supraclusie apr�s �ruption des incisives permanentes et que l�appr�ciation c�phalom�trique montre un angle
	 * maxillo-basal de 12 degr�s et moins (respectivement de 15 degr�s et moins combin� � un angle ANB de 7 degr�s et
	 * plus)
	 */
	INFIRMITE_209(52810209),
	/**
	 * Prognathie inf�rieure cong�nitale, lorsque l�appr�ciation c�phalom�trique apr�s l�apparition des incisives
	 * d�finitives montre une divergence des rapports sagittaux de la m�choire mesur�e par un angle ANB d�au moins �1
	 * degr� et qu�au moins deux paires antagonistes ant�rieures de la seconde dentition se trouvent en position
	 * d�occlusion crois�e ou en bout � bout, ou lorsqu�il existe une divergence de +1 degr� et moins combin�e � un
	 * angle maxillobasal de 37 degr�s et plus, ou de 15 degr�s et moins
	 */
	INFIRMITE_210(52810210),
	/** Epulis du nouveau-n� */
	INFIRMITE_211(52810211),
	/** Atr�sie des choanes (uni- ou bilat�rale) */
	INFIRMITE_212(52810212),
	/** Glossoschisis */
	INFIRMITE_213(52810213),
	/** Macroglossie et microglossie cong�nitales, lorsqu�une op�ration de la langue est n�cessaire */
	INFIRMITE_214(52810214),
	/** Kystes cong�nitaux et tumeurs cong�nitales de la langue */
	INFIRMITE_215(52810215),
	/**
	 * Affections cong�nitales des glandes salivaires et de leurs canaux excr�teurs (fistules, st�noses, kystes,
	 * tumeurs, ectasies et hypo- ou aplasies de toutes les glandes salivaires importantes).
	 */
	INFIRMITE_216(52810216),
	/**
	 * R�tention ou ankylose cong�nitale des dents, lorsque plusieurs molaires ou au moins deux pr�molaires ou molaires
	 * de la seconde dentition plac�es l�une � c�t� de l�autre (� exclusion des dents de sagesse) sont touch�es,
	 * l�absence de dents (� l�exclusion des dents de sagesse) est trait�e de la m�me mani�re que la r�tention ou
	 * l�ankylose.
	 */
	INFIRMITE_218(52810218),
	/** Goitre cong�nital */
	INFIRMITE_231(52810231),
	/**
	 * Kystes cong�nitaux du cou, fistules et fentes cervicales cong�nitales et tumeurs cong�nitales (cartilage de
	 * Reichert)
	 */
	INFIRMITE_232(52810232),
	/** Bronchectasies cong�nitales */
	INFIRMITE_241(52810241),
	/** Emphys�me lobaire cong�nital */
	INFIRMITE_242(52810242),
	/** Ag�n�sie partielle et hypoplasie des poumons */
	INFIRMITE_243(52810243),
	/** Kystes cong�nitaux et tumeurs cong�nitales des poumons */
	INFIRMITE_244(52810244),
	/** S�questration pulmonaire cong�nitale */
	INFIRMITE_245(52810245),
	/** Syndrome des membranes hyalines */
	INFIRMITE_247(52810247),
	/** Syndrome de Mikity-Wilson */
	INFIRMITE_248(52810248),
	/**
	 * Dyskin�sie primaire des cils immobiles (lorsque l�examen au microscope �lectronique est ex�cut� en dehors d�une
	 * p�riode d�infection)
	 */
	INFIRMITE_249(52810249),
	/** Malformations cong�nitales du larynx et de la trach�e */
	INFIRMITE_251(52810251),
	/** Tumeurs cong�nitales et kystes cong�nitaux du m�diastin */
	INFIRMITE_261(52810261),
	/** Atr�sie et st�nose cong�nitales de l�oesophage et fistule oesophago-trach�ale */
	INFIRMITE_271(52810271),
	/** M�gaoesophage cong�nital */
	INFIRMITE_272(52810272),
	/** St�nose hypertrophique du pylore */
	INFIRMITE_273(52810273),
	/** Atr�sie et st�nose cong�nitales de l�estomac, de l�intestin, du rectum ou de l�anus */
	INFIRMITE_274(52810274),
	/** Kystes, tumeurs, duplicatures et diverticules cong�nitaux du tube digestif */
	INFIRMITE_275(52810275),
	/** Anomalies du situs intestinal, � l�exclusion du caecum mobile */
	INFIRMITE_276(52810276),
	/** Il�us du nouveau-n� */
	INFIRMITE_277(52810277),
	/** Aganglionose et anomalies des cellules ganglionnaires du gros intestin ou de l�intestin gr�le */
	INFIRMITE_278(52810278),
	/** Coeliakie cons�cutive � l�intol�rance cong�nitale � la gliadine */
	INFIRMITE_279(52810279),
	/** Reflux gastro-oesophagien cong�nital, lorsqu�une op�ration est n�cessaire */
	INFIRMITE_280(52810280),
	/** Malformations cong�nitales du diaphragme */
	INFIRMITE_281(52810281),
	/**
	 * Ent�rocolite n�crosante des pr�matur�s ayant � la naissance un poids inf�rieur � 2000 grammes ou des nouveaun�s,
	 * lorsqu�elle se manifeste dans les quatre semaines apr�s la naissance.
	 */
	INFIRMITE_282(52810282),
	/** Atr�sie et hypoplasie des voies biliaires */
	INFIRMITE_291(52810291),
	/** Kyste cong�nital du chol�doque */
	INFIRMITE_292(52810292),
	/** Kystes cong�nitaux du foie */
	INFIRMITE_293(52810293),
	/** Fibrose cong�nitale du foie */
	INFIRMITE_294(52810294),
	/** Tumeurs cong�nitales du foie */
	INFIRMITE_295(52810295),
	/** Malformations cong�nitales et kystes cong�nitaux du pancr�as */
	INFIRMITE_296(52810296),
	/** Omphaloc�le et laparoschisis */
	INFIRMITE_302(52810302),
	/** Hernie inguinale lat�rale */
	INFIRMITE_303(52810303),
	/** H�mangiome caverneux ou tub�reux */
	INFIRMITE_311(52810311),
	/** Lymphangiome cong�nital, lymphangiectasie cong�nitale */
	INFIRMITE_312(52810312),
	/** Malformations cong�nitales du coeur et des vaisseaux */
	INFIRMITE_313(52810313),
	/** Lymphangiectasie intestinale cong�nitale */
	INFIRMITE_314(52810314),
	/** An�mies, leucop�nies et thrombocytop�nies du nouveau-n� */
	INFIRMITE_321(52810321),
	/** An�mies cong�nitales hypoplastiques ou aplastiques, leucop�nies et thrombocytop�nies cong�nitales */
	INFIRMITE_322(52810322),
	/** An�mies h�molytiques cong�nitales (affections des �rythrocytes, des enzymes ou de l�h�moglobine) */
	INFIRMITE_323(52810323),
	/** Coagulopathies et thrombocytopathies cong�nitales (h�mophilies et autres anomalies des facteurs de coagulation) */
	INFIRMITE_324(52810324),
	/** Hyperbilirubin�mie du nouveau-n� de causes diverses, lorsqu�une exsanguino-transfusion a �t� n�cessaire */
	INFIRMITE_325(52810325),
	/** Syndrome cong�nital de d�ficience immunitaire (IDS) */
	INFIRMITE_326(52810326),
	/** Angio-oed�me h�r�ditaire */
	INFIRMITE_327(52810327),
	/** Leuc�mie du nouveau-n� */
	INFIRMITE_329(52810329),
	/** Histiocytoses (granulome �osinophilique, maladie de Hand-Sch�ller-Christian et maladie de Letterer-Siwe) */
	INFIRMITE_330(52810330),
	/**
	 * Polyglobulie cong�nitale, lorsqu�une soustraction th�rapeutique de sang (saign�e) avec remplacement par du plasma
	 * a �t� n�cessaire
	 */
	INFIRMITE_331(52810331),
	/** Malformations cong�nitales et ectopies de la rate */
	INFIRMITE_333(52810333),
	/** Glom�rulopathies et tubulopathies cong�nitales */
	INFIRMITE_341(52810341),
	/**
	 * Malformations du rein, d�doublements et alt�rations cong�nitales des reins, y compris l�hypoplasie, l�ag�n�sie et
	 * la dystopie
	 */
	INFIRMITE_342(52810342),
	/** Tumeurs cong�nitales et kystes cong�nitaux des reins */
	INFIRMITE_343(52810343),
	/** Hydron�phrose cong�nitale */
	INFIRMITE_344(52810344),
	/** Malformations ur�t�rales cong�nitales (st�noses, atr�sies, ur�t�roc�le, dystopies et m�galuret�re) */
	INFIRMITE_345(52810345),
	/** Reflux v�sico-ur�t�ral cong�nital */
	INFIRMITE_346(52810346),
	/** Malformations cong�nitales de la vessie (par exemple : diverticule de la vessie, m�gavessie cong�nitale) */
	INFIRMITE_348(52810348),
	/** Tumeurs cong�nitales de la vessie */
	INFIRMITE_349(52810349),
	/** Exstrophie de la vessie */
	INFIRMITE_350(52810350),
	/** Atr�sie et st�nose cong�nitales de l�ur�tre et diverticule de l�ur�tre */
	INFIRMITE_351(52810351),
	/** Hypospadias et �pispadias */
	INFIRMITE_352(52810352),
	/** Fistule v�sico-ombilicale cong�nitale et kyste cong�nital de l�ouraque */
	INFIRMITE_353(52810353),
	/** Fistules recto-uro-g�nitales cong�nitales */
	INFIRMITE_354(52810354),
	/** Cryptorchidie (unilat�rale ou bilat�rale), lorsqu�une op�ration est n�cessaire */
	INFIRMITE_355(52810355),
	/** Hydroc�le testiculaire et kystes du cordon spermatique ou du ligament rond, lorsqu�une op�ration est n�cessaire */
	INFIRMITE_356(52810356),
	/** Palmure et courbure cong�nitales du p�nis */
	INFIRMITE_357(52810357),
	/** Atr�sie cong�nitale de l�hymen, du vagin, du col ut�rin ou de l�ut�rus et st�nose cong�nitale du vagin */
	INFIRMITE_358(52810358),
	/** Hermaphrodisme vrai et pseudo-hermaphrodisme */
	INFIRMITE_359(52810359),
	/**
	 * D�doublement des organes g�nitaux f�minins (ut�rus bicorne � col simple ou double, ut�rus unicollis et ut�rus
	 * double avec ou sans vagin double)
	 */
	INFIRMITE_361(52810361),
	/**
	 * Malformations du syst�me nerveux et de ses enveloppes (enc�phaloc�le, kyste arachno�dien, my�lom�ningoc�le,
	 * hydromy�lie, m�ningoc�le, m�galenc�phalie, porenc�phalie et diast�matomy�lie)
	 */
	INFIRMITE_381(52810381),
	/** Troubles de l�hypoventilation d�origine centrale du nouveaun� */
	INFIRMITE_382(52810382),
	/**
	 * Affections h�r�do-d�g�n�ratices du syst�me nerveux (p. ex. ataxie de Friedreich, leucodystrophies et affections
	 * progressives de la mati�re grise, atrophies musculaires d�origine spinale ou neutrale, dys-autonomie familiale,
	 * analg�sie cong�nitale, syndrome de Rett)
	 */
	INFIRMITE_383(52810383),
	/** M�dulloblastome, �pendymome, gliome, papillome des plexus choro�des et chordome */
	INFIRMITE_384(52810384),
	/**
	 * Tumeurs et malformations cong�nitales de l�hypophyse (comme le craniopharyngiome, le kyste de Rathke et la poche
	 * persistante de Rathke)
	 */
	INFIRMITE_385(52810385),
	/** Hydroc�phalie cong�nitale */
	INFIRMITE_386(52810386),
	/** Epilepsies cong�nitales */
	INFIRMITE_387(52810387),
	/** Paralysies c�r�brales cong�nitales (spastiques, ath�tosiques et ataxiques) */
	INFIRMITE_390(52810390),
	/** L�gers troubles moteurs c�r�braux (traitement jusqu�� l�accomplissement de la deuxi�me ann�e de la vie) */
	INFIRMITE_395(52810395),
	/** Sympathogoniome (neuroblastome sympathique), sympathicoblastome, ganglioneuroblastome et ganglioneurome */
	INFIRMITE_396(52810396),
	/** Paralysies et par�sies cong�nitales */
	INFIRMITE_397(52810397),
	/** Oligophr�nie cong�nitale (seulement pour le traitement du comportement �r�thique ou apathique) */
	INFIRMITE_403(52810403),
	/**
	 * Troubles c�r�braux cong�nitaux ayant pour cons�quence pr�pond�rante des sympt�mes psychiques et cognitifs chez
	 * les sujets d�intelligence normale, lorsqu�ils ont �t� diagnostiqu�s et trait�s comme tels avant l�accomplissement
	 * de la neuvi�me ann�e (syndrome psycho-organique, psycho-syndrome d� � une l�sion diffuse ou localis�e du cerveau
	 * et syndrome psycho-orgaique cong�nital infantile); l�oligophr�nie cong�nitale est class�e exclusivement sous
	 * chiffre 403
	 */
	INFIRMITE_404(52810404),
	/**
	 * Troubles du spectre autistique, lorsque leurs sympt�mes ont �t� manifestes avant l�accomplissement de la
	 * cinqui�me ann�e
	 */
	INFIRMITE_405(52810405),
	/**
	 * Psychoses primaires du jeune enfant, lorsque leurs sympt�mes ont �t� manifestes avant l�accomplissement de la
	 * cinqui�me ann�e
	 */
	INFIRMITE_406(52810406),
	/** Malformations des paupi�res (colobome et ankylobl�pharon) */
	INFIRMITE_411(52810411),
	/** Ptose cong�nitale de la paupi�re */
	INFIRMITE_412(52810412),
	/** Aplasie des voies lacrymales */
	INFIRMITE_413(52810413),
	/** Anophtalmie, buphtalmie et glaucome cong�nital */
	INFIRMITE_415(52810415),
	/**
	 * Opacit�s cong�nitales de la corn�e avec acuit� visuelle de 0,2 ou moins � un oeil ou 0,4 ou moins aux deux yeux
	 * (apr�s correction du vice de r�fraction)
	 */
	INFIRMITE_416(52810416),
	/** Nystagmus cong�nital, lorsqu�une op�ration est n�cessaire */
	INFIRMITE_417(52810417),
	/**
	 * Anomalies cong�nitales de l�iris et de l�uv�e avec acuit� visuelle de 0,2 ou moins � un oeil ou 0,4 ou moins aux
	 * deux yeux (apr�s correction du vice de r�fraction)
	 */
	INFIRMITE_418(52810418),
	/**
	 * Opacit�s cong�nitales du cristallin ou du corps vitr� et anomalies de position du cristallin avec acuit� visuelle
	 * de 0,2 ou moins � un oeil ou 0,4 ou moins aux deux yeux (apr�s correction du vice de r�fraction)
	 */
	INFIRMITE_419(52810419),
	/** R�tinopathie des pr�matur�s et pseudogliome cong�nital (y compris la maladie de Coats) */
	INFIRMITE_420(52810420),
	/** R�tinoblastome */
	INFIRMITE_421(52810421),
	/** D�g�n�rescences tap�tor�tiniennes cong�nitales */
	INFIRMITE_422(52810422),
	/**
	 * Malformations et affections cong�nitales du nerf optique avec acuit� visuelle de 0,2 ou moins � un oeil ou 0,4 ou
	 * moins aux deux yeux (apr�s correction du vice de r�fraction)
	 */
	INFIRMITE_423(52810423),
	/** Tumeurs cong�nitales de la cavit� orbitaire */
	INFIRMITE_424(52810424),
	/**
	 * Anomalies cong�nitales de r�fraction avec acuit� visuelle de 0,2 ou moins � un oeil ou 0,4 ou moins aux deux yeux
	 * (apr�s correction du vice de r�fraction)
	 */
	INFIRMITE_425(52810425),
	/**
	 * Strabisme et micro strabisme concomitant unilat�ral, lorsqu�il existe une amblyopie de 0,2 ou moins (apr�s
	 * correction)
	 */
	INFIRMITE_427(52810427),
	/** Par�sie cong�nitales des muscles de l�oeil */
	INFIRMITE_428(52810428),
	/** Atr�sie cong�nitale de l�oreille, y compris l�anotie et la microtie */
	INFIRMITE_441(52810441),
	/**
	 * Fentes cong�nitales dans la r�gion de l�oreille, fistules cong�niales de l�oreille moyenne et d�fauts cong�nitaux
	 * du tympan
	 */
	INFIRMITE_443(52810443),
	/**
	 * Malformations cong�nitales de l�oreille moyenne avec surdit� partielle uni- ou bilat�rale entra�nant une perte
	 * auditive d�au moins 30 dB � l�audiogramme tonal dans deux domaines des fr�quences de la conversation de 500, 1
	 * 000, 2 000 et 4 000 Hz
	 */
	INFIRMITE_444(52810444),
	/** Surdit� cong�nitale des deux oreilles */
	INFIRMITE_445(52810445),
	/**
	 * Surdit� cong�nitale neurosensorielle avec, � l�audiogramme toal, une perte de l�audition de 30 dB au moins dans
	 * le do maine des fr�quences de la conversation de 500, 1 000, 2 000 et 4 000 Hz
	 */
	INFIRMITE_446(52810446),
	/** Cholest�atome cong�nital */
	INFIRMITE_447(52810447),
	/**
	 * Troubles cong�nitaux du m�tabolisme des hydrates de carbone (glycog�nose, galactos�mie, intol�rance au fructose,
	 * hypoglyc�mie de Mac Quarrie, hypoglyc�mie de Zetterstroem, hypoglyc�mie par leucino-d�pendance, hyperoxalurie
	 * primaire, anomalies cong�nitales du m�tabolisme du pyruvate, malabsorption du lactose, malabsorption du
	 * saccharose et diab�te sucr�, lorsque celui-ci est constat� dans les quatre premi�res semaines de la vie ou qu�il
	 * �tait sans aucun doute manifeste durant cette p�riode
	 */
	INFIRMITE_451(52810451),
	/**
	 * Troubles cong�nitaux du m�tabolisme des acides amin�s et des prot�ines (par exemple : ph�nylc�tonurie, cystinose,
	 * cystinurie, oxalose, syndrome oculo-c�r�bro-r�nal de Lowe, anomalies cong�nitales du cycle de l�ur�e et autres
	 * hyperammoni�mies cong�nitales)
	 */
	INFIRMITE_452(52810452),
	/**
	 * Troubles cong�nitaux du m�tabolisme des graisses et des lipoprot�ines (par exemple : idiotie amaurotique, maladie
	 * de Niemann-Pick, maladie de Gaucher, hypercholest�rol�mie h�r�ditaire, hyperlip�mie h�r�ditaire,
	 * leucodystrophies)
	 */
	INFIRMITE_453(52810453),
	/**
	 * Troubles cong�nitaux du m�tabolisme des mucopolysaccharides et des glycoprot�ines (par exemple : maladie
	 * Pfaundler-Hurler, maladie de Morquio)
	 */
	INFIRMITE_454(52810454),
	/** Troubles cong�nitaux de m�tabolisme des purines et pyrimidines (xanthinurie) */
	INFIRMITE_455(52810455),
	/** Troubles cong�nitaux du m�tabolisme des m�taux (maladie de Wilson, h�mochromatose et syndrome de Menkes) */
	INFIRMITE_456(52810456),
	/**
	 * Troubles cong�nitaux du m�tabolisme de la myoglobine, de l�h�moglobine et de la bilirubine (porphyrie et
	 * myoglobinurie)
	 */
	INFIRMITE_457(52810457),
	/** Troubles cong�nitaux de la fonction du foie (ict�res h�r�ditaires non h�molytiques) */
	INFIRMITE_458(52810458),
	/** Troubles cong�nitaux de la fonction du pancr�as (mucoviscidose et insuffisance primaire du pancr�as) */
	INFIRMITE_459(52810459),
	/**
	 * Troubles cong�nitaux du m�tabolisme des os (par exemple : hypophosphatasie, dysplasie diaphysaire progressive de
	 * Camurati-Engelmann, ost�odystrophie de Jaff�-Lichtenstein, rachitisme r�sistant au traitement par la vitamine D)
	 */
	INFIRMITE_461(52810461),
	/**
	 * Troubles cong�nitaux de la fonction hypothalamohypophysaire (petite taille d�origine hypophysaire, diab�te
	 * insipide, syndrome de Prader-Willi et syndrome de Kallmann)
	 */
	INFIRMITE_462(52810462),
	/** Troubles cong�nitaux de la fonction de la glande thyro�de (athyro�die, hypothyro�die et cr�tinisme) */
	INFIRMITE_463(52810463),
	/** Troubles cong�nitaux de la fonction des glandes parathyro�des (hypoparathyro�disme et pseudohypoparathyro�disme) */
	INFIRMITE_464(52810464),
	/** Troubles cong�nitaux de la fonction des glandes surr�nales (syndrome adr�no-g�nital et insuffisance surr�nale) */
	INFIRMITE_465(52810465),
	/**
	 * Troubles cong�nitaux de la fonction des gonades (malformation des ovaires, anorchie, syndrome de Klinefelter et
	 * f�minisation testiculaire cong�nitale; voir aussi chiffre 488)
	 */
	INFIRMITE_466(52810466),
	/**
	 * D�faut d�enzyme cong�nital du m�tabolisme interm�diaire lorsque ses sympt�mes ont �t� manifestes avant
	 * l�accomplissement de la cinqui�me ann�e
	 */
	INFIRMITE_467(52810467),
	/** Ph�ochromocytome et ph�ochromoblastome */
	INFIRMITE_468(52810468),
	/** Neurofibromatose */
	INFIRMITE_481(52810481),
	/** Angiomatose c�r�brale et r�tinienne (von Hippel-Lindau) */
	INFIRMITE_482(52810482),
	/** Angiomatose enc�phalo-trig�min�e (Sturge-Weber-Krabbe) */
	INFIRMITE_483(52810483),
	/** Syndrome t�langiectasies-ataxie (Louis Bar) */
	INFIRMITE_484(52810484),
	/**
	 * Dystrophies cong�nitales du tissu conjonctif (par exemple : syndrome de Marfan, syndrome d�Ehlers-Danlos, cutis
	 * laxa conge-nita, pseudoxanthome �lastique)
	 */
	INFIRMITE_485(52810485),
	/**
	 * T�ratomes et autres tumeurs des cellules germinales (par exemple : dysgerminome, carcinome embryonnaire, tumeur
	 * mixte des cellules germinales, tumeur vitelline, choriocarcinome, gonadoblastome)
	 */
	INFIRMITE_486(52810486),
	/** Scl�rose c�r�brale tub�reuse (Bourneville) */
	INFIRMITE_487(52810487),
	/** Syndrome de Turner (seulement troubles de la fonction des gonades et de la croissance) */
	INFIRMITE_488(52810488),
	/** Trisomie 21 (Down-Syndrom)*/
	INFIRMITE_489(52810489),
	/** Infection cong�nitales par HIV */
	INFIRMITE_490(52810490),
	/** Tumeurs du nouveau-n� */
	INFIRMITE_491(52810491),
	/** Monstres doubles (par exemple : fr�res siamois, �pignathe) */
	INFIRMITE_492(52810492),
	/**
	 * S�quelles d�embryopathies et de foetopathies (l�oligophr�nie cong�nitale est class�e sous chiffre 403); maladies
	 * infectieuses cong�nitales (par exemple : lu�s, toxoplasmose, tuberculose, list�riose, cytom�galie)
	 */
	INFIRMITE_493(52810493),
	/**
	 * Nouveaux-n�s ayant � la naissance un poids inf�rieur � 2000 grammes, jusqu�� la reprise d�un poids de 3000
	 * grammes
	 */
	INFIRMITE_494(52810494),
	/**
	 * Infections n�onatales s�v�res, lorsqu�elles sont manifestes au cours des 72 premi�res heures de la vie et qu�un
	 * traitement intensif est n�cessaire
	 */
	INFIRMITE_495(52810495),
	/** Pharmacod�pendance n�onatale, lorsqu�un traitement intensif est n�cessaire */
	INFIRMITE_496(52810496),
	/**
	 * S�v�res troubles respiratoires d�adaptation (par exemple : asphyxie, syndrome de d�tresse respiratoire, apn�e),
	 * lorsqu�ils sont manifestes au cours des 72 premi�res heures de la vie et qu�un traitement intensif est n�cessaire
	 */
	INFIRMITE_497(52810497),
	/**
	 * Troubles m�taboliques n�onataux s�v�res (hypoglyc�mie, hypocalc�mie, hypomagn�si�mie), lorsqu�ils sont manifestes
	 * au cours des 72 premi�res heures de la vie et qu�un traitement intensif est n�cessaire
	 */
	INFIRMITE_498(52810498),
	/** S�v�res l�sions traumatiques dues � la naissance, lorsqu�un traitement intensif est n�cessaire */
	INFIRMITE_499(52810499),
	/** Down-Syndrom */
	INFIRMITE_501(52810501),
	/** Oligophr�nie (idiotie, imb�cillit�, d�bilit�, voir aussi chiffre 403) */
	INFIRMITE_502(52810502),
	/** Autres infirmit�s cong�nitales en dehors de l�OIC */
	INFIRMITE_503(52810503),
	/** (maladie) Tuberculose de l�appareil respiratoire */
	INFIRMITE_601(52810601),
	/** (maladie) Autres formes de tuberculose */
	INFIRMITE_602(52810602),
	/** (maladie) Poliomy�lite */
	INFIRMITE_603(52810603),
	/**
	 * (maladie) Autres affections d�origine infectieuse ou parasitaire (� l�exclusion des affections du syst�me
	 * nerveux, voir sous XXVII, et de l�appareil respiratoire, voir sous XXX)
	 */
	INFIRMITE_604(52810604),
	/** (maladie) Tumeurs malignes */
	INFIRMITE_611(52810611),
	/**
	 * (maladie) Tumeurs des tissus lymphatiques et h�matopo��tiques (lymphosarcome, reticulosarcome,
	 * lymphogranulomatose, autres lymphomes, my�lome multiple, leuc�mie et aleuc�mie, mycosis fongo�de)
	 */
	INFIRMITE_612(52810612),
	/** (maladie) Autres tumeurs */
	INFIRMITE_613(52810613),
	/** (maladie) Asthme bronchique */
	INFIRMITE_621(52810621),
	/** (maladie) Autres allergies */
	INFIRMITE_622(52810622),
	/** (maladie) Diab�te sucr� */
	INFIRMITE_623(52810623),
	/** (maladie) Autres maladies endocriniennes */
	INFIRMITE_624(52810624),
	/** (maladie) Maladies du m�tabolisme et de la nutrition, avitaminoses (voir aussi sous XXXI) */
	INFIRMITE_625(52810625),
	/** (maladie) Maladie du sang et de la rate (� l�exception des infirmit�s cong�nitales et des tumeurs) */
	INFIRMITE_631(52810631),
	/** (maladie) Schizophr�nie */
	INFIRMITE_641(52810641),
	/** (maladie) Acc�s maniaque d�pressif */
	INFIRMITE_642(52810642),
	/** (maladie) Psychoses organiques et l�sions c�r�brales */
	INFIRMITE_643(52810643),
	/**
	 * (maladie) Autres psychoses (cas rares qui ne peuvent pas se ranger sous 641 � 643 ou 841 � 843, tels que
	 * psychoses mixtes dites psychoses schizo-affectives, schizophr�nie chez l�oligophr�ne); d�pressions involutives
	 */
	INFIRMITE_644(52810644),
	/** (maladie) Psychopathie */
	INFIRMITE_645(52810645),
	/**
	 * (maladie) Troubles r�actifs du milieu ou psychog�nes; n�vroses, borderline cases (limite entre la psychose et la
	 * n�vrose); anomalie psychique simple, par exemple; caract�re hypocondriaque ou d�mentiel : troubles fonctionnels
	 * du syst�me nerveux et troubles du langage qui en sont la cons�quence, dans la mesure o� ils n�ont pas �t�
	 * codifi�s comme troubles physiques
	 */
	INFIRMITE_646(52810646),
	/** (maladie) Alcoolisme */
	INFIRMITE_647(52810647),
	/** (maladie) Autres toxicomanies */
	INFIRMITE_648(52810648),
	/**
	 * (maladie) Autres troubles du caract�re, du comportement et de l�intelligence, y compris les troubles du
	 * d�veloppement du langage; oligophr�nie (d�bilit�, imb�cillit�, idiotie) � voir sous XXI
	 */
	INFIRMITE_649(52810649),
	/** (maladie) H�morragies c�r�brales et autres l�sions vasculaires affectant le syst�me nerveux central */
	INFIRMITE_651(52810651),
	/** (maladie) Enc�phalite et m�ningite */
	INFIRMITE_652(52810652),
	/** (maladie) Scl�rose en plaques (scl�rose multiple) */
	INFIRMITE_653(52810653),
	/** (maladie) Epilepsie acquise, � l�exclusion des formes reconnues comme infirmit�s cong�nitales */
	INFIRMITE_654(52810654),
	/** (maladie) Autres affections du cerveau */
	INFIRMITE_655(52810655),
	/** (maladie) Affections de la moelle */
	INFIRMITE_656(52810656),
	/** (maladie) Autres affections du syst�me nerveux; Poliomy�lite � voir sous XXII, 603 */
	INFIRMITE_657(52810657),
	/** (maladie) Affections de l�oeil (conjonctive, paupi�res et orbite) */
	INFIRMITE_661(52810661),
	/** (maladie) Affections de l�oreille (oreille externe, oreille moyenne, et oreille interne) */
	INFIRMITE_671(52810671),
	/**
	 * (maladie) Affections rhumatismales f�briles (polyarthrites aigu�s et suraigu�s, chor�e mineure) accompagn�es de
	 * troubles circulatoires
	 */
	INFIRMITE_681(52810681),
	/** (maladie) L�sions organiques du coeur, y compris l�infarctus */
	INFIRMITE_682(52810682),
	/** (maladie) Troubles fonctionnels du coeur et de la circulation */
	INFIRMITE_683(52810683),
	/** (maladie) Hypertension, art�rioscl�rose, an�vrisme et autres affections des art�res */
	INFIRMITE_684(52810684),
	/** (maladie) Affections des veines et des vaisseaux lymphatiques */
	INFIRMITE_685(52810685),
	/** (maladie) Infections des voies respiratoires */
	INFIRMITE_691(52810691),
	/** (maladie) Pneumoconioses (y compris la silicose) */
	INFIRMITE_692(52810692),
	/** (maladie) Autres affections de l�appareil respiratoire (� l�exclusion de la tuberculose) */
	INFIRMITE_693(52810693),
	/** (maladie) Affections du tube digestif (bouche, oesophage, estomac et intestin), y compris les hernies */
	INFIRMITE_701(52810701),
	/** (maladie) Affections du foie et des voies biliaires */
	INFIRMITE_702(52810702),
	/** (maladie) Affections du pancr�as (� l�exclusion du diab�te sucr�) */
	INFIRMITE_703(52810703),
	/** (maladie) Affections des reins et des voies urinaires */
	INFIRMITE_711(52810711),
	/** (maladie) Affections des organes g�nitaux */
	INFIRMITE_712(52810712),
	/**
	 * (maladie) Alt�rations de la peau et du tissu cellulaire souscutan� (� l�exclusion des tumeurs, voir sous XXIII,
	 * et des allergies, voir sous XXIV)
	 */
	INFIRMITE_721(52810721),
	/** (maladie) Rhumatisme articulaire primaire chronique (y compris la maladie de Bechterew) */
	INFIRMITE_731(52810731),
	/** (maladie) Coxarthrose */
	INFIRMITE_732(52810732),
	/** (maladie) Autres arthroses */
	INFIRMITE_733(52810733),
	/** (maladie) Epiphys�olyse */
	INFIRMITE_734(52810734),
	/** (maladie) Maladie de Perthes */
	INFIRMITE_735(52810735),
	/** (maladie) Spondyloses et ost�ochondroses (y compris la maladie de Scheuermann) */
	INFIRMITE_736(52810736),
	/** (maladie) Scoliose idiopathique */
	INFIRMITE_737(52810737),
	/** (maladie) Autres alt�rations des os et des organes du mouvement (ligaments, muscles et tendons) */
	INFIRMITE_738(52810738),
	/** (accident) Tuberculose de l�appareil respiratoire */
	INFIRMITE_801(52810801),
	/** (accident) Autres formes de tuberculose */
	INFIRMITE_802(52810802),
	/** (accident) Poliomy�lite */
	INFIRMITE_803(52810803),
	/**
	 * (accident) Autres affections d�origine infectieuse ou parasitaire (� l�exclusion des affections du syst�me
	 * nerveux, voir sous XXVII, et de l�appareil respiratoire,
	 */
	INFIRMITE_804(52810804),
	/** (accident) Tumeurs malignes */
	INFIRMITE_811(52810811),
	/**
	 * (accident) Tumeurs des tissus lymphatiques et h�matopo��tiques (lymphosarcome, reticulosarcome,
	 * lymphogranulomatose, autres lymphomes, my�lome multiple, leuc�mie et aleuc�mie, mycosis fongo�de)
	 */
	INFIRMITE_812(52810812),
	/** (accident) Autres tumeurs */
	INFIRMITE_813(52810813),
	/** (accident) Asthme bronchique */
	INFIRMITE_821(52810821),
	/** (accident) Autres allergies */
	INFIRMITE_822(52810822),
	/** (accident) Diab�te sucr� */
	INFIRMITE_823(52810823),
	/** (accident) Autres maladies endocriniennes */
	INFIRMITE_824(52810824),
	/** (accident) Maladies du m�tabolisme et de la nutrition, avitaminoses (voir aussi sous XXXI) */
	INFIRMITE_825(52810825),
	/** (accident) Maladie du sang et de la rate (� l�exception des infirmit�s cong�nitales et des tumeurs) */
	INFIRMITE_831(52810831),
	/** (accident) Schizophr�nie */
	INFIRMITE_841(52810841),
	/** (accident) Acc�s maniaque d�pressif */
	INFIRMITE_842(52810842),
	/** (accident) Psychoses organiques et l�sions c�r�brales */
	INFIRMITE_843(52810843),
	/**
	 * (accident) Autres psychoses (cas rares qui ne peuvent pas se ranger sous 641 � 643 ou 841 � 843, tels que
	 * psychoses mixtes dites psychoses schizo-affectives, schizophr�nie chez l�oligophr�ne); d�pressions involutives
	 */
	INFIRMITE_844(52810844),
	/** (accident) Psychopathie */
	INFIRMITE_845(52810845),
	/**
	 * (accident) Troubles r�actifs du milieu ou psychog�nes; n�vroses, borderline cases (limite entre la psychose et la
	 * n�vrose); anomalie psychique simple, par exemple; caract�re hypocondriaque ou d�mentiel : troubles fonctionnels
	 * du syst�me nerveux et troubles du langage qui en sont la cons�quence, dans la mesure o� ils n�ont pas �t�
	 * codifi�s comme troubles physiques
	 */
	INFIRMITE_846(52810846),
	/** (accident) Alcoolisme */
	INFIRMITE_847(52810847),
	/** (accident) Autres toxicomanies */
	INFIRMITE_848(52810848),
	/**
	 * (accident) Autres troubles du caract�re, du comportement et de l�intelligence, y compris les troubles du
	 * d�veloppement du langage; oligophr�nie (d�bilit�, imb�cillit�, idiotie) � voir sous XXI
	 */
	INFIRMITE_849(52810849),
	/** (accident) H�morragies c�r�brales et autres l�sions vasculaires affectant le syst�me nerveux central */
	INFIRMITE_851(52810851),
	/** (accident) Enc�phalite et m�ningite */
	INFIRMITE_852(52810852),
	/** (accident) Scl�rose en plaques (scl�rose multiple) */
	INFIRMITE_853(52810853),
	/** (accident) Epilepsie acquise, � l�exclusion des formes reconnues comme infirmit�s cong�nitales */
	INFIRMITE_854(52810854),
	/** (accident) Autres affections du cerveau */
	INFIRMITE_855(52810855),
	/** (accident) Affections de la moelle */
	INFIRMITE_856(52810856),
	/** (accident) Autres affections du syst�me nerveux; Poliomy�lite � voir sous XXII, 603 */
	INFIRMITE_857(52810857),
	/** (accident) Affections de l�oeil (conjonctive, paupi�res et orbite) */
	INFIRMITE_861(52810861),
	/** (accident) Affections de l�oreille (oreille externe, oreille moyenne, et oreille interne) */
	INFIRMITE_871(52810871),
	/**
	 * (accident) Affections rhumatismales f�briles (polyarthrites aigu�s et suraigu�s, chor�e mineure) accompagn�es de
	 * troubles circulatoires
	 */
	INFIRMITE_881(52810881),
	/** (accident) L�sions organiques du coeur, y compris l�infarctus */
	INFIRMITE_882(52810882),
	/** (accident) Troubles fonctionnels du coeur et de la circulation */
	INFIRMITE_883(52810883),
	/** (accident) Hypertension, art�rioscl�rose, an�vrisme et autres affections des art�res */
	INFIRMITE_884(52810884),
	/** (accident) Affections des veines et des vaisseaux lymphatiques */
	INFIRMITE_885(52810885),
	/** (accident) Infections des voies respiratoires */
	INFIRMITE_891(52810891),
	/** (accident) Pneumoconioses (y compris la silicose) */
	INFIRMITE_892(52810892),
	/** (accident) Autres affections de l�appareil respiratoire (� l�exclusion de la tuberculose) */
	INFIRMITE_893(52810893),
	/** (accident) Affections du tube digestif (bouche, oesophage, estomac et intestin), y compris les hernies */
	INFIRMITE_901(52810901),
	/** (accident) Affections du foie et des voies biliaires */
	INFIRMITE_902(52810902),
	/** (accident) Affections du pancr�as (� l�exclusion du diab�te sucr�) */
	INFIRMITE_903(52810903),
	/** (accident) Affections des reins et des voies urinaires */
	INFIRMITE_911(52810911),
	/** (accident) Affections des organes g�nitaux */
	INFIRMITE_912(52810912),
	/**
	 * (accident) Alt�rations de la peau et du tissu cellulaire souscutan� (� l�exclusion des tumeurs, voir sous XXIII,
	 * et des allergies, voir sous XXIV)
	 */
	INFIRMITE_921(52810921),
	/** (accident) Rhumatisme articulaire primaire chronique (y compris la maladie de Bechterew) */
	INFIRMITE_931(52810931),
	/** (accident) Coxarthrose */
	INFIRMITE_932(52810932),
	/** (accident) Autres arthroses */
	INFIRMITE_933(52810933),
	/** (accident) Epiphys�olyse */
	INFIRMITE_934(52810934),
	/** (accident) Maladie de Perthes */
	INFIRMITE_935(52810935),
	/** (accident) Spondyloses et ost�ochondroses (y compris la maladie de Scheuermann) */
	INFIRMITE_936(52810936),
	/** (accident) Scoliose idiopathique */
	INFIRMITE_937(52810937),
	/** (accident) Autres alt�rations des os et des organes du mouvement (ligaments, muscles et tendons) */
	INFIRMITE_938(52810938),
	// @formatter:on

    /**
     * Vieux codes systemes inactifs, mais utiles pour d'anciennes demandes lors
     * d'utilisation et d'importation de donn�es ACOR
     * 
     * (K160202_002)
     */
    INFIRMITE_004(52810004),
    INFIRMITE_151(52810151),
    INFIRMITE_401(52810401),
    INFIRMITE_426(52810426),
    INFIRMITE_442(52810442);

    /**
     * Retourne l'�num�r� correspondant au code syst�me. </br><strong>Renvoie une {@link IllegalArgumentException} si la
     * cha�ne de caract�re pass�e en param�tre est invalide (null ou vide) ou si le code syst�me ne correspond pas � un
     * valeur de cette �num�r�.</strong>
     * 
     * @param codeSysteme la valeur du code syst�me � rechercher
     * @throws IllegalArgumentException si le param�tre codeSystem est null, une cha�ne vide ou ne correspond pas � une
     *             valeur connue
     * @return l'�num�r� correspondant au code syst�me. </br><strong>Si la valeur n'est pas trouv�e une
     *         IllegalArgumentException sera lanc�e</strong>
     */
    public static Infirmite parse(final String codeSysteme) {
        if (!JadeStringUtil.isDigit(codeSysteme)) {
            throw new IllegalArgumentException("The value [" + codeSysteme
                    + "] is not valid for the systemCode of type [" + Infirmite.class.getName() + "]");
        }
        Integer intCodeSysteme = Integer.parseInt(codeSysteme);
        return Infirmite.valueOf(intCodeSysteme);
    }

    /**
     * Retourne l'�num�r� correspondant au code syst�me.
     * </br><strong>Renvoie une {@link IllegalArgumentException} si la la valeur du param�tre <code>codeSystem</code>
     * est null ou si le code syst�me ne correspond pas � une valeur de cette �num�r�.</strong>
     * 
     * @param codeSysteme la valeur du code syst�me � rechercher
     * @throws IllegalArgumentException si le param�tre codeSystem est null, ou ne correspond pas � une
     *             valeur connue
     * @return l'�num�r� correspondant au code syst�me. </br><strong>Si la valeur n'est pas trouv�e une
     *         IllegalArgumentException sera lanc�e</strong>
     */
    public static Infirmite valueOf(final Integer codeSysteme) {
        if (codeSysteme != null) {
            for (Infirmite uneInfirmite : Infirmite.values()) {
                if (uneInfirmite.getCodeSysteme().equals(codeSysteme)) {
                    return uneInfirmite;
                }
            }
        }
        throw new IllegalArgumentException("The value [" + codeSysteme + "] is not valid for the systemCode of type ["
                + Infirmite.class.getName() + "]");
    }

    private Integer codeSysteme;

    private Infirmite(final Integer codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * @return le code syst�me correspondant, sous la forme d'un entier
     */
    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}