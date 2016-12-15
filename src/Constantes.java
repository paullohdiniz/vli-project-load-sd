
public class Constantes {
	
	//275
	public static String queryConsultarCarregamento = 
			"		select decode(c6h.c6hcdfer,                                                                                                                                                 "+
					"                '03',                                                                                                                                                              "+
					"                'EFVM',                                                                                                                                                            "+
					"                '04',                                                                                                                                                              "+
					"                'EFC',                                                                                                                                                             "+
					"                '06',                                                                                                                                                              "+
					"                'FCA',                                                                                                                                                             "+
					"                '12',                                                                                                                                                              "+
					"                'FNS',                                                                                                                                                             "+
					"                '07',                                                                                                                                                              "+
					"                'MRS',                                                                                                                                                             "+
					"                'ND') CODIGO_FERROVIA,                                                                                                                                             "+
					"         c6h.c6hserie SERIE_CARREGAMENTO,                                                                                                                                          "+
					"         c6h.c6hnumde NUMERO_CARREGAMENTO,                                                                                                                                         "+
					"         c6h.c6hnumat VERSAO_CARREGAMENTO,                                                                                                                                         "+
					"         decode(c6c.c6cindts,'N','01','R','02','I','03','M','04','S','05','')         TIPO_SERVICO_CTE,                                                                                                                                    "+
					"         cux_ass.cuxserie SERIE_CARREGAMENTO_ASSOC,                                                                                                                                "+
					"         cux_ass.cuxnumde NUMERO_CARREGAMENTO_ASSOC,                                                                                                                                      "+
					"         cux_ass.cuxflxce NUMERO_FLUXO_ASSOC,                                                                                                                                      "+
					" 		  (select distinct c6p.c6pcdunf from c6pcomet c6p where c6c.c6ccdmer = c6p.c6pcdmer) UNIDADE_MEDIDA,																		"+
					" 		  czw.CZWINPVA IND_NIVEL_ESP_PERFIL_VAG,																		"+
					"         c6h.c6hdtemi DATA_EMISSAO,                                                                                                                                                "+
					"         c6h.c6hvltri VALOR_SERVICOS,                                                                                                                                              "+
					"         c6h.c6htpdes TIPO_CARREGAMENTO,                                                                                                                                           "+
					"         c6h.c6hsitua SITUACAO_CARREGAMENTO,                                                                                                                                       "+
					"         cu4.cu4cdflx             CODIGO_FLUXO,                                                                                                                                    "+
					"         estacao_origem.RIENUSEQ ID_ORIGEM,                                                                                                                                        "+
					"         estacao_destino.RIENUSEQ ID_DESTINO,                                                                                                                                      "+
					"         estacao_origem.RIECDLOC  CODIGO_ORIGEM,                                                                                                                                   "+
					"         estacao_destino.RIECDLOC CODIGO_DESTINO,                                                                                                                                  "+
					"         c6c.c6cnustc             COD_TERMINAL_CARGA,                                                                                                                              "+
					"         c6c.c6cnustd             COD_TERMINAL_DESCARGA,                                                                                                                           "+
					"         c6c.c6ccdmer             CODIGO_MERCADORIA,                                                                                                                               "+
					"         emitente.cli_razsoc RAZAO_SOCIAL_EMITENTE,                                                                                                                                "+
					"         emitente.cli_Endere ENDERECO_EMITENTE,                                                                                                                                    "+
					"         emitente.cli_Numcep CEP_EMITENTE,                                                                                                                                         "+
					"         emitente.cli_Bairro BAIRRO_EMITENTE,                                                                                                                                      "+
					"         emitente.cli_Cidade CIDADE_EMITENTE,                                                                                                                                      "+
					"         cue_emitente.cuecdloc IBGE_EMITENTE,                                                                                                                                      "+
					"         '' DESC_ESTADO_EMITENTE,           "+
					"                                                                                                                                                                                   "+
					"         SUBSTR(emitente.cli_estado,1,2) COD_ESTADO_EMITENTE,                                                                                                                                  "+
					"         emitente.cli_numero || emitente.cli_estabe || emitente.cli_digito CNPJ_EMITENTE,                                                                                          "+
					"                                                                                                                                                                                   "+
					"         emitente.cli_insmun INSC_MUNICIPAL_EMITENTE,                                                                                                                              "+
					"         emitente.cli_insest INSC_ESTADUAL_EMITENTE,                                                                                                                               "+
					"                                                                                                                                                                                   "+
					"         czl.czltpras         TIPO_DOC_GERADO,                                                                                                                                        "+
					"         czo.czoincon         IND_EMPRESA_CONTRATADA,                                                                                                                                        "+

					"         c8i.c8iserdf         SERIE_FATURA,                                                                                                                                        "+
					"         c8i.c8inrnot         NUMERO_FATURA,                                                                                                                                       "+
					"         c8i.c8idtemi         DATA_FATURA,                                                                                                                                         "+
					"         decode(czw.czwindct,'E','57','T','06','')         TIPO_DOC_FISCAL,                                                                                                                                    "+
					"         c8n.c8nstats         STATUS_FATURA,                                                                                                                                       "+
					"         c8i.c8ichcte         CHAVE_CTE,                                                                                                                                           "+
					"                                                                                                                                                                                   "+
					"         remetente.cli_tipcli TIPO_CLIENTE_REMETENTE,                                                                                                                              "+
					"         remetente.CLI_RAZSOC RAZAO_SOCIAL_REMETENTE,                                                                                                                              "+
					"         remetente.Cli_Endere ENDERECO_REMETENTE,                                                                                                                                  "+
					"         remetente.Cli_Numcep CEP_REMETENTE,                                                                                                                                       "+
					"         remetente.Cli_Bairro BAIRRO_REMETENTE,                                                                                                                                    "+
					"         remetente.Cli_Cidade CIDADE_REMETENTE,                                                                                                                                    "+
					"                                                                                                                                                                                   "+
					"         ClienteOrgUnit_rem.Cuecdloc IBGE_REMETENTE,                                                                                                                               "+
					"         ''  DESC_ESTADO_REMETENTE,        "+
					"                                                                                                                                                                                   "+
					"                                                                                                                                                                                   "+
					"         SUBSTR(remetente.cli_estado,1,2) COD_ESTADO_REMETENTE,                                                                                                                                "+
					"         remetente.cli_numero || remetente.cli_estabe || remetente.cli_digito CNPJ_REMETENTE,                                                                                      "+
					"         remetente.cli_insmun INSC_MUNICIPAL_REMETENTE,                                                                                                                             "+
					"         remetente.cli_insest INSC_ESTADUAL_REMETENTE,                                                                                                                             "+
					"                                                                                                                                                                                   "+
					"         destinatario.CLI_RAZSOC RAZAO_SOCIAL_DESTINATARIO,                                                                                                                        "+
					"         destinatario.Cli_Endere ENDERECO_DESTINATARIO,                                                                                                                            "+
					"         destinatario.Cli_Numcep CEP_DESTINATARIO,                                                                                                                                 "+
					"         destinatario.Cli_Bairro BAIRRO_DESTINATARIO,                                                                                                                              "+
					"         destinatario.Cli_Cidade CIDADE_DESTINATARIO,                                                                                                                              "+
					"                                                                                                                                                                                   "+
					"         ClienteOrgUnit_des.Cuecdloc IBGE_DESTINATARIO,                                                                                                                            "+
					"         '' DESC_ESTADO_DESTINATARIO,   "+
					"                                                                                                                                                                                   "+
					"         SUBSTR(destinatario.cli_estado,1,2) COD_ESTADO_DESTINATARIO,                                                                                                                          "+
					"         destinatario.cli_numero || destinatario.cli_estabe ||                                                                                                                     "+
					"         destinatario.cli_digito CNPJ_DESTINATARIO,                                                                                                                                "+
					"                                                                                                                                                                                   "+
					"         destinatario.cli_insmun      INSC_MUNICIPAL_DESTINATARIO,                                                                                                                     "+
					"         destinatario.cli_insest INSC_ESTADUAL_DESTINATARIO,                                                                                                                       "+
					"                                                                                                                                                                                   "+
					"         NULL RAZAO_SOCIAL_EXPEDIDOR,                                                                                                                                              "+
					"         NULL ENDERECO_EXPEDIDOR,                                                                                                                                                  "+
					"         NULL CEP_EXPEDIDOR,                                                                                                                                                       "+
					"         NULL BAIRRO_EXPEDIDOR,                                                                                                                                                    "+
					"         NULL CIDADE_EXPEDIDOR,                                                                                                                                                    "+
					"                                                                                                                                                                                   "+
					"         NULL IBGE_EXPEDIDOR,                                                                                                                                                      "+
					"         NULL DESC_ESTADO_EXPEDIDOR,                                                                                                                                               "+
					"                                                                                                                                                                                   "+
					"         NULL COD_ESTADO_EXPEDIDOR,                                                                                                                                                "+
					"         NULL CNPJ_EXPEDIDOR,                                                                                                                                                      "+
					"         NULL INSC_MUNICIPAL_EXPEDIDOR,                                                                                                                                               "+
					"         NULL INSC_ESTADUAL_EXPEDIDOR,                                                                                                                                             "+
					"                                                                                                                                                                                   "+
					"         NULL RAZAO_SOCIAL_RECEBEDOR,                                                                                                                                              "+
					"         NULL ENDERECO_RECEBEDOR,                                                                                                                                                  "+
					"         NULL CEP_RECEBEDOR,                                                                                                                                                       "+
					"         NULL BAIRRO_RECEBEDOR,                                                                                                                                                    "+
					"         NULL CIDADE_RECEBEDOR,                                                                                                                                                    "+
					"                                                                                                                                                                                   "+
					"         NULL IBGE_RECEBEDOR,                                                                                                                                                      "+
					"         NULL DESC_ESTADO_RECEBEDOR,                                                                                                                                               "+
					"         NULL COD_ESTADO_RECEBEDOR,                                                                                                                                                "+
					"         NULL CNPJ_RECEBEDOR,                                                                                                                                                      "+
					"         NULL INSC_MUNICIPAL_RECEBEDOR,                                                                                                                                               "+
					"         NULL INSC_ESTADUAL_RECEBEDOR,                                                                                                                                             "+
					"                                                                                                                                                                                   "+
					"         ClienteCorrentista.CLI_RAZSOC RAZAO_SOCIAL_TOMADOR,                                                                                                                       "+
					"         ClienteCorrentista.Cli_Endere ENDERECO_TOMADOR,                                                                                                                           "+
					"         ClienteCorrentista.Cli_Numcep CEP_TOMADOR,                                                                                                                                "+
					"         ClienteCorrentista.Cli_Bairro BAIRRO_TOMADOR,                                                                                                                             "+
					"         ClienteCorrentista.Cli_Cidade CIDADE_TOMADOR,                                                                                                                             "+
					"                                                                                                                                                                                   "+
					"         ClienteOrgUnit_cor.Cuecdloc IBGE_TOMADOR,                                                                                                                                 "+
					"         '' DESC_ESTADO_TOMADOR,  "+
					"                                                                                                                                                                                   "+
					"         SUBSTR(ClienteCorrentista.cli_estado,1,2) COD_ESTADO_TOMADOR,                                                                                                                         "+
					"         ClienteCorrentista.cli_numero || ClienteCorrentista.cli_estabe ||                                                                                                         "+
					"         ClienteCorrentista.cli_digito CNPJ_TOMADOR,                                                                                                                               "+
					"         ClienteCorrentista.cli_insmun INSC_MUNICIPAL_TOMADOR,                                                                                                                    "+
					"         ClienteCorrentista.cli_insest INSC_ESTADUAL_TOMADOR,                                                                                                                      "+
					"                                                                                                                                                                                   "+
					"         (select c8i_c.c8ichcte                                                                                                                                                    "+
					"            from cuxredct cux_c,                                                                                                                                                   "+
					"                 c6hdefet c6h_c,                                                                                                                                                   "+
					"                 c8sdesft c8s_c,                                                                                                                                                   "+
					"                 czlraagt czl_c,                                                                                                                                                   "+
					"                 c8nsofat c8n_c,                                                                                                                                                   "+
					"                 c8ifatut c8i_c                                                                                                                                                    "+
					"           where cux_c.cuxserct = c6h_c.c6hserie                                                                                                                                   "+
					"             and cux_c.cuxnumct = c6h_c.c6hnumde                                                                                                                                   "+
					"             and c6h.c6hnumat = c6h_c.c6hnumat                                                                                                                                     "+
					"             and c6h_c.c6hserie = c8s_c.c8sserie                                                                                                                                   "+
					"             and c6h_c.c6hnumde = c8s_c.c8snumde                                                                                                                                   "+
					"             and c6h_c.c6hnumat = c8s_c.c8snumat                                                                                                                                   "+
					"             and c8s_c.c8sidras = czl_c.czlident                                                                                                                                   "+
					"             and czl_c.czlident = c8n_c.c8nidras                                                                                                                                   "+
					"             and c8n_c.c8nidsol = c8i_c.c8iidsol                                                                                                                                   "+
					"             and cux_c.cuxserie = c6h.c6hserie                                                                                                                                     "+
					"             and cux_c.cuxnumde = c6h.c6hnumde                                                                                                                                     "+
					"             and rownum = 1) CHAVE_CTE_ANTERIOR,                                                                                                                                   "+
					"                                                                                                                                                                                   "+
					"         ClienteCorrentista.CLI_RAZSOC RAZAO_SOCIAL_EMISSOR,                                                                                                                       "+
					"         ClienteCorrentista.Cli_Endere ENDERECO_EMISSOR,                                                                                                                           "+
					"         ClienteCorrentista.Cli_Numcep CEP_EMISSOR,                                                                                                                                "+
					"         ClienteCorrentista.Cli_Bairro BAIRRO_EMISSOR,                                                                                                                             "+
					"         ClienteCorrentista.Cli_Cidade CIDADE_EMISSOR,                                                                                                                             "+
					"                                                                                                                                                                                   "+
					"         ClienteOrgUnit_cor.Cuecdloc IBGE_EMISSOR,                                                                                                                                 "+
					"         '' DESC_ESTADO_EMISSOR,  "+
					"                                                                                                                                                                                   "+
					"         SUBSTR(ClienteCorrentista.cli_estado,1,2) COD_ESTADO_EMISSOR,                                                                                                                         "+
					"         ClienteCorrentista.cli_numero || ClienteCorrentista.cli_estabe ||                                                                                                         "+
					"         ClienteCorrentista.cli_digito CNPJ_EMISSOR,                                                                                                                               "+
					"         ClienteCorrentista.cli_insmun INSC_MUNICIPAL_EMISSOR,                                                                                                                    "+
					"         ClienteCorrentista.cli_insest INSC_ESTADUAL_EMISSOR,                                                                                                                      "+
					"                                                                                                                                                                                   "+
					"         NULL CTE_SERIE,                                                                                                                                                           "+
					"         NULL CTE_NUMERO,                                                                                                                                                          "+
					"         NULL CTE_EMISSAO,                                                                                                                                                         "+
					"                                                                                                                                                                                   "+
					"         c8g.c8gorgid ORG_ID                                                                                                                                                       "+
					"                                                                                                                                                                                   "+
					"                                                                                                                                                                                   "+
					"    from c8nsofat c8n,                                                                                                                                                             "+
					"         c8ifatut c8i,                                                                                                                                                             "+
					"         c8sdesft c8s,                                                                                                                                                             "+
					"         czlraagt czl,                                                                                                                                                             "+
					"         c6hdefet c6h,                                                                                                                                                             "+
					"         cuxredct cux_ass,                                                                                                                                                         "+                                                                                                                                                                          
					"         rieuclgv estacao_origem,                                                                                                                                                  "+
					"         rieuclgv estacao_destino,                                                                                                                                                 "+
					"                                                                                                                                                                                   "+
					"         cu4heflt cu4,                                                                                                                                                            "+
					"         c6cacsft c6c,                                                                                                                                                             "+
					"         czwitfet czw,                                                                                                                                                             "+
					"         czohecot czo,                                                                                                                                                             "+
					"                                                                                                                                                                                   "+
					"         c6vestft            c6v,                                                                                                                                                  "+
					"         HZ_LOCATIONS_UNICOM emitente,                                                                                                                                             "+
					"         cueclout          cue_emitente,                                                                                                                                           "+
					"         hz_locations_unicom cliente,                                                                                                                                              "+
					"         cueclout            cue,                                                                                                                                                  "+
					"         c8gcofet            c8g,                                                                                                                                                  "+
					"                                                                                                                                                                                   "+
					"         hz_locations_unicom ClienteCorrentista,                                                                                                                                   "+
					"         c6zclfct            ClienteUnicom_cor,                                                                                                                                    "+
					"         cuareflt            RelComEmpresaFluxo_cor,                                                                                                                               "+
					"         cueclout            ClienteOrgUnit_cor,                                                                                                                                   "+
					"                                                                                                                                                                                   "+
					"         hz_locations_unicom remetente,                                                                                                                                            "+
					"         c6zclfct            ClienteUnicom_rem,                                                                                                                                    "+
					"         cuareflt            RelComEmpresaFluxo_rem,                                                                                                                               "+
					"         cueclout            ClienteOrgUnit_rem,                                                                                                                                   "+
					"                                                                                                                                                                                   "+
					"                                                                                                                                                                                   "+
					"         hz_locations_unicom destinatario,                                                                                                                                         "+
					"         c6zclfct            ClienteUnicom_des,                                                                                                                                    "+
					"         cuareflt            RelComEmpresaFluxo_des,                                                                                                                               "+
					"         cueclout            ClienteOrgUnit_des                                                                                                                                    "+
					"   where c8n.c8nncnpj = c6v.c6vncnpj                                                                                                                                               "+
					"     and c8n.c8nnesta = c6v.c6vnesta                                                                                                                                               "+
					"     and c6v.c6videlo = emitente.cli_identi                                                                                                                                        "+
					"     and emitente.cli_identi = cue_emitente.cueidelo                                                                                                                               "+
					"     and c8i.c8iidsol = c8n.c8nidsol                                                                                                                                               "+
					"     and c8s.c8scdfer = c6h.c6hcdfer                                                                                                                                               "+
					"     and c8s.c8sserie = c6h.c6hserie                                                                                                                                               "+
					"     and c8s.c8snumde = c6h.c6hnumde                                                                                                                                               "+
					"     and c8s.c8snumat = c6h.c6hnumat                                                                                                                                               "+
					"     and c6h.c6hcdflx = cu4.cu4cdflx                                                                                                                                               "+
					"     and czw.czwident = cu4.cu4iditf                                                                                                                                               "+
					"     and czw.czwnuseo = estacao_origem.RIENUSEQ                                                                                                                                    "+
					"     and czw.czwnused = estacao_destino.RIENUSEQ                                                                                                                                   "+
					"                                                                                                                                                                                   "+
					"     and czl.czlident = c8s.c8sidras                                                                                                                                               "+
					"     and czl.czlident = c8n.c8nidras                                                                                                                                               "+
					"     and cue.cueident = czl.czlidcli                                                                                                                                               "+
					"     and cux_ass.cuxserct(+) = c6h.c6hserie                                                                                                                                               "+
					"     and cux_ass.cuxnumct(+) = c6h.c6hnumde                                                                                                                                                                              "+
					"     and cue.cueidelo = cliente.cli_identi                                                                                                                                         "+
					"     and cue.cueorgid = c8g.c8gorgid                                                                                                                                               "+
					"     and c8g.c8gcdfer =                                                                                                                                                            "+
					"         decode(c6c.c6ccdfef, '03', '03', '04', '04', '12', '12', '06')                                                                                                            "+
					"                                                                                                                                                                                   "+
					"     and c6h.c6hcdflx = c6c.c6ccdflx                                                                                                                                               "+
					"     and trunc(c6h.c6hdtemi) >= c6c.c6cdativ                                                                                                                                       "+
					"     and trunc(c6h.c6hdtemi) <= c6c.c6cdattv                                                                                                                                       "+
					"     and c6c.c6cdtcri =                                                                                                                                                            "+
					"         (select max(c6c2.c6cdtcri)                                                                                                                                                "+
					"            from c6cacsft c6c2                                                                                                                                                     "+
					"           WHERE c6c2.c6ccdflx = c6c.c6ccdflx                                                                                                                                      "+
					"             and trunc(c6h.c6hdtemi) >= c6c2.c6cdativ                                                                                                                              "+
					"             and trunc(c6h.c6hdtemi) <= c6c2.c6cdattv)                                                                                                                             "+
					"                                                                                                                                                                                   "+
					"     and RelComEmpresaFluxo_cor.cuacodig = 'CN'                                                                                                                                    "+
					"     and RelComEmpresaFluxo_cor.cuaidcli = ClienteUnicom_cor.c6zident                                                                                                              "+
					"     and ClienteUnicom_cor.c6zident = ClienteOrgUnit_cor.cueident                                                                                                                  "+
					"     and ClienteOrgUnit_cor.cueidelo = ClienteCorrentista.cli_identi                                                                                                               "+
					"     and c6c.c6cident = RelComEmpresaFluxo_cor.cuavrflx                                                                                                                            "+
					"     and ClienteOrgUnit_cor.cueorgid = c8g.c8gorgid                                                                                                                                "+
					"                                                                                                                                                                                   "+
					"     and RelComEmpresaFluxo_rem.cuacodig = 'RE'                                                                                                                                    "+
					"     and RelComEmpresaFluxo_rem.cuaidcli = ClienteUnicom_rem.c6zident                                                                                                              "+
					"     and ClienteUnicom_rem.c6zident = ClienteOrgUnit_rem.cueident                                                                                                                  "+
					"     and ClienteOrgUnit_rem.cueidelo = remetente.cli_identi                                                                                                                        "+
					"     and c6c.c6cident = RelComEmpresaFluxo_rem.cuavrflx                                                                                                                            "+
					"     and ClienteOrgUnit_rem.cueorgid = c8g.c8gorgid                                                                                                                                "+
					"     and czw.czwidacl = CZO.CZOIDENT                                                                                                                                                                              "+
					"     and RelComEmpresaFluxo_des.cuacodig = 'DE'                                                                                                                                    "+
					"     and RelComEmpresaFluxo_des.cuaidcli = ClienteUnicom_des.c6zident                                                                                                              "+
					"     and ClienteUnicom_des.c6zident = ClienteOrgUnit_des.cueident                                                                                                                  "+
					"     and ClienteOrgUnit_des.cueidelo = destinatario.cli_identi                                                                                                                     "+
					"     and c6c.c6cident = RelComEmpresaFluxo_des.cuavrflx                                                                                                                            "+
					"     and ClienteOrgUnit_des.cueorgid = c8g.c8gorgid                                                                                                                                "+
					"     and c6h.c6htpdes in ('FER','SUP','CRE','SUB')                                                                                                                                             "+
					"     and c6h.c6hsitua in ('FA','CO')                                                                                                                                               "+
					//"     and c6h.c6hunico in ('S')                                                                                                                                               		"+
					"     and czl.czltpras not in ('CNF','VICT','MNF','NCS','NDAS','NCAS','CCF','CTF','VIA','ACC','NCSV','NCM')																			"+		
					//"     and trunc(c6h.c6hdtemi) >=  ?                                                                                                                                      			"+
					//"     and trunc(c6h.c6hdtemi) <=  ?                                                                                                                                       			"+
				    "     and c6h.c6hserie = ?                                                                                                                                              "+
					"     and c6h.c6hnumde = ?                                                                                                                                               "+ 
					"     ORDER BY DATA_EMISSAO, SERIE_CARREGAMENTO, NUMERO_CARREGAMENTO, VERSAO_CARREGAMENTO                                                                                                                                                            ";
	
	public static String queryInserirCarregamento = 
			"INSERT INTO " + 
					"CARREGAMENTO_HEADER " +
					" (ID_CTE	, " +
					" CODIGO_FERROVIA	, " +
					" SERIE_CARREGAMENTO	, " +
					" NUMERO_CARREGAMENTO	, " +
					" VERSAO_CARREGAMENTO	, " +
					" DATA_EMISSAO	, " +
					" TIPO_CARREGAMENTO	, " +
					" SITUACAO_CARREGAMENTO	, " +
					" CODIGO_FLUXO	, " +
					" CODIGO_ORIGEM	, " +
					" CODIGO_DESTINO	, " +
					" CODIGO_MERCADORIA	, " +
					" RAZAO_SOCIAL_EMITENTE	, " +
					" ENDERECO_EMITENTE	, " +
					" CEP_EMITENTE	, " +
					" BAIRRO_EMITENTE	, " +
					" CIDADE_EMITENTE	, " +
					" IBGE_EMITENTE	, " +
					" DESC_ESTADO_EMITENTE	, " +
					" COD_ESTADO_EMITENTE	, " +
					" CNPJ_EMITENTE	, " +
					" INSC_MUNICIPAL_EMITENTE	, " +
					" INSC_ESTADUAL_EMITENTE	, " +
					" SERIE_FATURA	, " +
					" NUMERO_FATURA	, " +
					" DATA_FATURA	, " +
					" TIPO_SERVICO_CTE	, " +
					" STATUS_FATURA	, " +
					" CHAVE_CTE	, " +
					" RAZAO_SOCIAL_REMETENTE	, " +
					" ENDERECO_REMETENTE	, " +
					" CEP_REMETENTE	, " +
					" BAIRRO_REMETENTE	, " +
					" CIDADE_REMETENTE	, " +
					" IBGE_REMETENTE	, " +
					" DESC_ESTADO_REMETENTE	, " +
					" COD_ESTADO_REMETENTE	, " +
					" CNPJ_REMETENTE	, " +
					" INSC_MUNICIPAL_REMETENTE	, " +
					" INSC_ESTADUAL_REMETENTE	, " +
					" RAZAO_SOCIAL_DESTINATARIO	, " +
					" ENDERECO_DESTINATARIO	, " +
					" CEP_DESTINATARIO	, " +
					" BAIRRO_DESTINATARIO	, " +
					" CIDADE_DESTINATARIO	, " +
					" IBGE_DESTINATARIO	, " +
					" DESC_ESTADO_DESTINATARIO	, " +
					" COD_ESTADO_DESTINATARIO	, " +
					" CNPJ_DESTINATARIO	, " +
					" INSC_MUNICIPAL_DESTINATARIO	, " +
					" INSC_ESTADUAL_DESTINATARIO	, " +
					" RAZAO_SOCIAL_EXPEDIDOR	, " +
					" ENDERECO_EXPEDIDOR	, " +
					" CEP_EXPEDIDOR	, " +
					" BAIRRO_EXPEDIDOR	, " +
					" CIDADE_EXPEDIDOR	, " +
					" IBGE_EXPEDIDOR	, " +
					" DESC_ESTADO_EXPEDIDOR	, " +
					" COD_ESTADO_EXPEDIDOR	, " +
					" CNPJ_EXPEDIDOR	, " +
					" INSC_MUNICIPAL_EXPEDIDOR	, " +
					" INSC_ESTADUAL_EXPEDIDOR	, " +
					" RAZAO_SOCIAL_RECEBEDOR	, " +
					" ENDERECO_RECEBEDOR	, " +
					" CEP_RECEBEDOR	, " +
					" BAIRRO_RECEBEDOR	, " +
					" CIDADE_RECEBEDOR	, " +
					" IBGE_RECEBEDOR	, " +
					" DESC_ESTADO_RECEBEDOR	, " +
					" COD_ESTADO_RECEBEDOR	, " +
					" CNPJ_RECEBEDOR	, " +
					" INSC_MUNICIPAL_RECEBEDOR	, " +
					" INSC_ESTADUAL_RECEBEDOR	, " +
					" RAZAO_SOCIAL_TOMADOR	, " +
					" ENDERECO_TOMADOR	, " +
					" CEP_TOMADOR	, " +
					" BAIRRO_TOMADOR	, " +
					" CIDADE_TOMADOR	, " +
					" IBGE_TOMADOR	, " +
					" DESC_ESTADO_TOMADOR	, " +
					" COD_ESTADO_TOMADOR	, " +
					" CNPJ_TOMADOR	, " +
					" INSC_MUNICIPAL_TOMADOR	, " +
					" INSC_ESTADUAL_TOMADOR	, " +
					" CHAVE_CTE_ANTERIOR	, " +
					" SERIE_ANTERIOR	, " +
					" DOCUMENTO_ANTERIOR	, " +
					" TIPO_ANTERIOR	, " +
					" DATA_EMISSAO_DOC_ANTERIOR	, " +
					" RAZAO_SOCIAL_EMISSOR	, " +
					" ENDERECO_EMISSOR	, " +
					" CEP_EMISSOR	, " +
					" BAIRRO_EMISSOR	, " +
					" CIDADE_EMISSOR	, " +
					" IBGE_EMISSOR	, " +
					" DESC_ESTADO_EMISSOR	, " +
					" COD_ESTADO_EMISSOR	, " +
					" CNPJ_EMISSOR	, " +
					" INSC_MUNICIPAL_EMISSOR	, " +
					" INSC_ESTADUAL_EMISSOR	, " +
					" CTE_SERIE	, " +
					" CTE_NUMERO	, " +
					" CTE_EMISSAO	, " +
					" DT_EXECUCAO	, " +
					" FLAG_LIDO	 )" +
					" VALUES " + 
					"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
					;

	public static String queryConsultarServicos = 
			" select carregamento.c6hserie as SERIE,								 "+
					"		   carregamento.c6hnumde as CARREGAMENTO,                                        "+
					"	       carregamento.c6hnumat as VERSAO,                                              "+
					"	       decode(servico_carregamento.c6bcdfer,'03','EFVM','04','EFC','06','FCA','12','FNS','07','MRS','ND') as FERROVIA_SERVICO,                                                                             "+
					"	       servico_carregamento.C6BCDTPS as COD_SERVICO,                                 "+
					"	       desc_servico.C6XDESCR as DESCRICAO,                                           "+
					"	       '' UNIDADE_MEDIDA,                                                            "+
					"	       servico_carregamento.c6bqtdec as PESO_CALCULO,                                "+
					"	       servico_fluxo.CU3VLSER as TARIFA_UNIT_S_IMPOSTO,                              "+
					"	       servico_carregamento.C6BVALOR as VALOR_SERVICO,                               "+
					"	       (select impostos.aliq_icms                                                    "+
					"	          from tcfl_cor_dof impostos2, TCFL_COR_IDF impostos                         "+
					"	         where impostos2.dof_import_numero =                                         "+
					"	               'TCFL' || servicos_tcfl.lodanosl || servicos_tcfl.lodnumsl            "+
					"	           and impostos2.ID = impostos.DOF_ID) as ALIQUOTA_ICMS,                     "+
					"	       (select impostos.vl_base_icms                                                 "+
					"	          from tcfl_cor_dof impostos2, TCFL_COR_IDF impostos                         "+
					"	         where impostos2.dof_import_numero =                                         "+
					"	               'TCFL' || servicos_tcfl.lodanosl || servicos_tcfl.lodnumsl            "+
					"	           and impostos2.ID = impostos.DOF_ID) as VALOR_BASE_ICMS,                   "+
					"	       servicos_tcfl.lodvlicm as VALOR_ICMS,                                         "+
					"	       (select impostos.VL_ALIQ_PIS                                                  "+
					"	          from tcfl_cor_dof impostos2, TCFL_COR_IDF impostos                         "+
					"	         where impostos2.dof_import_numero =                                         "+
					"	               'TCFL' || servicos_tcfl.lodanosl || servicos_tcfl.lodnumsl            "+
					"	           and impostos2.ID = impostos.DOF_ID) as ALIQUOTA_PIS,                      "+
					"	       (select impostos.vl_base_pis                                                  "+
					"	          from tcfl_cor_dof impostos2, TCFL_COR_IDF impostos                         "+
					"	         where impostos2.dof_import_numero =                                         "+
					"	               'TCFL' || servicos_tcfl.lodanosl || servicos_tcfl.lodnumsl            "+
					"	           and impostos2.ID = impostos.DOF_ID) as VALOR_BASE_PIS,                    "+
					"	       servicos_tcfl.LODVLPIS as VALOR_PIS,                                          "+
					"	       (select impostos.VL_ALIQ_COFINS                                               "+
					"	          from tcfl_cor_dof impostos2, TCFL_COR_IDF impostos                         "+
					"	         where impostos2.dof_import_numero =                                         "+
					"	               'TCFL' || servicos_tcfl.lodanosl || servicos_tcfl.lodnumsl            "+
					"	           and impostos2.ID = impostos.DOF_ID) as ALIQUOTA_COFINS,                   "+
					"	       (select impostos.vl_base_cofins                                               "+
					"	          from tcfl_cor_dof impostos2, TCFL_COR_IDF impostos                         "+
					"	         where impostos2.dof_import_numero =                                         "+
					"	               'TCFL' || servicos_tcfl.lodanosl || servicos_tcfl.lodnumsl            "+
					"	           and impostos2.ID = impostos.DOF_ID) as VALOR_BASE_COFINS,                 "+
					"	       servicos_tcfl.LODVLCOF as VALOR_COFINS,                                       "+
					"	       (select impostos.vl_base_cofins                                               "+
					"	          from tcfl_cor_dof impostos2, TCFL_COR_IDF impostos                         "+
					"	         where impostos2.dof_import_numero =                                         "+
					"	               'TCFL' || servicos_tcfl.lodanosl || servicos_tcfl.lodnumsl            "+
					"	           and impostos2.ID = impostos.DOF_ID) as VALOR_BASE_COFINS,                 "+
					"	        (select impostos.ALIQ_ISS                                                    "+
					"	          from tcfl_cor_dof impostos2, TCFL_COR_IDF impostos                         "+
					"	         where impostos2.dof_import_numero =                                         "+
					"	               'TCFL' || servicos_tcfl.lodanosl || servicos_tcfl.lodnumsl            "+
					"	           and impostos2.ID = impostos.DOF_ID) as ALIQUOTA_ISS,                      "+
					"	       (select impostos.vl_base_ISS                                                  "+
					"	          from tcfl_cor_dof impostos2, TCFL_COR_IDF impostos                         "+
					"	         where impostos2.dof_import_numero =                                         "+
					"	               'TCFL' || servicos_tcfl.lodanosl || servicos_tcfl.lodnumsl            "+
					"	           and impostos2.ID = impostos.DOF_ID) as VALOR_BASE_ISS,                    "+
					"	       servicos_tcfl.LODVLISS as VALOR_ISS                                           "+
					"	  from c6hdefet carregamento,                                                        "+
					"	       C6BSEDFT servico_carregamento,                                                "+
					"	       C6XSERVT desc_servico,                                                        "+
					"	       cu4heflt fluxo,                                                               "+
					"	       czwitfet item_ferroviario,                                                    "+
					"	       CZXPRIFT preco_item,                                                          "+
					"	       CU3SEFLT servico_fluxo,                                                       "+
					"	       c6cacsft versao_fluxo,                                                        "+
					"	       c6fserft servico_versao_fluxo,                                                "+
					"	       c8sdesft rascunho,                                                            "+
					"	       c8nsofat solicitacao,                                                         "+
					"	       LODCPIST servicos_tcfl                                                        "+
					"                                                                                        "+
					"	 where carregamento.c6hserie = servico_carregamento.c6bserie                         "+
					"	   and carregamento.c6hnumde = servico_carregamento.c6bnumde                         "+
					"	   and carregamento.c6hnumat = servico_carregamento.c6bnumat                         "+
					"	   and servico_carregamento.c6bcdtps = desc_servico.C6XCDTPS                         "+
					"	   and carregamento.c6hcdflx = fluxo.cu4cdflx                                        "+
					"	   and fluxo.CU4IDITF = item_ferroviario.CZWIDENT                                    "+
					"	   and item_ferroviario.CZWIDENT = preco_item.CZXIDITF                               "+
					"	   and preco_item.CZXident =                                                         "+
					"	       (select max(x.czxident)                                                       "+
					"	          from czxprift x                                                            "+
					"	         where carregamento.c6hcdflx = fluxo.cu4cdflx                                "+
					"	           and fluxo.CU4IDITF = item_ferroviario.CZWIDENT                            "+
					"	           and item_ferroviario.CZWIDENT = x.CZXIDITF                                "+
					"	           and x.czxdativ <= carregamento.c6hdtemi                                   "+
					"	           and x.czxdattv >= carregamento.c6hdtemi                                   "+
					"	           and x.czxdtcri <= carregamento.c6hdtcri)                                  "+
					"	   and preco_item.CZXident = servico_fluxo.CU3IDPIF                                  "+
					"	   and fluxo.cu4cdflx = versao_fluxo.c6ccdflx                                        "+
					"	   and versao_fluxo.c6cident =                                                       "+
					"	       (select max(y.c6cident)                                                       "+
					"	          from c6cacsft y                                                            "+
					"	         where carregamento.c6hcdflx = fluxo.cu4cdflx                                "+
					"	           and fluxo.cu4cdflx = y.c6ccdflx                                           "+
					"	           and y.c6cdativ <= carregamento.c6hdtemi                                   "+
					"	           and y.c6cdattv >= carregamento.c6hdtemi                                   "+
					"	           and y.c6cdtcri <= carregamento.c6hdtcri)                                  "+
					"	   and versao_fluxo.c6cident = servico_versao_fluxo.c6fideac                         "+
					"	   and servico_versao_fluxo.c6fcdtps = servico_carregamento.C6BCDTPS                 "+
					"	   and servico_versao_fluxo.c6fcdfer = servico_carregamento.c6bcdfer                 "+
					"	   and servico_versao_fluxo.c6fident = servico_fluxo.cu3idsvf                        "+
					"	   and carregamento.c6hserie = rascunho.c8sserie                                     "+
					"	   and carregamento.c6hnumde = rascunho.c8snumde                                     "+
					"	   and carregamento.c6hnumat = rascunho.c8snumat                                     "+
					"	   and rascunho.c8sidras = solicitacao.c8nidras                                      "+
					"	   and solicitacao.c8nanopr = servicos_tcfl.lodanosl                                 "+
					"	   and solicitacao.c8nnumpr = servicos_tcfl.lodnumsl                                 "+
					"	   and trim(servico_carregamento.C6BCDTPS) = trim(servicos_tcfl.lodcdcpn)            "+
					//"	   and servico_carregamento.c6bcdfer =                                    			 "+
					//"	       servicos_tcfl.lodcdfer                                             			 "+
					"	   and carregamento.c6hserie = :serie                                                "+
					"	   and carregamento.c6hnumde = :carregamento                                         "+
					"	   and carregamento.c6hnumat = :versao                                               "
					;
	
	public static String queryConsultarServicos_2 = 
	        "  select carregamento.c6hserie as SERIE,                                                                                          "+
	                "    carregamento.c6hnumde as CARREGAMENTO,                                                                                        "+
	                "      carregamento.c6hnumat as VERSAO,                                                                                            "+
	                "      decode(servico_carregamento.c6bcdfde,'03','EFVM','04','EFC','06','FCA','12','FNS','07','MRS','ND') as FERROVIA_SERVICO,     "+                                                                       
	                "      servico_carregamento.C6BCDTPS as COD_SERVICO,                                                                               "+
	                "      desc_servico.C6XDESCR as DESCRICAO,                                                                                         "+
	                "      '' UNIDADE_MEDIDA,                                                                                                          "+
	                "      servico_carregamento.c6bqtdec as PESO_CALCULO,                                                                              "+
	                "      servico_fluxo.CU3VLSER as TARIFA_UNIT_S_IMPOSTO,                                                                            "+
	                "      servico_carregamento.C6BVALOR as VALOR_SERVICO,                                                                             "+
	                "      doc_fiscal.C8IALICM as ALIQUOTA_ICMS,                                                                                       "+
	                "      doc_fiscal.C8IVLBAS as VALOR_BASE_ICMS,                                                                                     "+
	                "      doc_fiscal.C8IVLNTR as VALOR_ICMS,                                                                                          "+
	                "      '' as ALIQUOTA_PIS,                                                                                                         "+
	                "      '' as VALOR_BASE_PIS,                                                                                                       "+
	                "      '' as VALOR_PIS,                                                                                                            "+
	                "      '' as ALIQUOTA_COFINS,                                                                                                      "+
	                "      '' as VALOR_BASE_COFINS,                                                                                                    "+
	                "      '' as VALOR_COFINS,                                                                                                         "+
	                "      '' as VALOR_BASE_COFINS,                                                                                                    "+
	                "      '' as ALIQUOTA_ISS,                                                                                                         "+
	                "      '' as VALOR_BASE_ISS,                                                                                                       "+
	                "      '' as VALOR_ISS                                                                                                             "+
	                " from c6hdefet carregamento,                                                                                                      "+
	                "      C6BSEDFT servico_carregamento,                                                                                              "+
	                "      C6XSERVT desc_servico,                                                                                                      "+
	                "      cu4heflt fluxo,                                                                                                             "+
	                "      czwitfet item_ferroviario,                                                                                                  "+
	                "      CZXPRIFT preco_item,                                                                                                        "+
	                "      CU3SEFLT servico_fluxo,                                                                                                     "+
	                "      c6cacsft versao_fluxo,                                                                                                      "+
	                "      c6fserft servico_versao_fluxo,                                                                                              "+
	                "      c8sdesft rascunho,                                                                                                          "+
	                "      c8nsofat solicitacao,                                                                                                       "+
	                "      c8ifatut doc_fiscal                                                                                                         "+
	                "                                                                                                                                  "+
	                "where carregamento.c6hserie = servico_carregamento.c6bserie                                                                       "+
	                "  and carregamento.c6hnumde = servico_carregamento.c6bnumde                                                                       "+
	                "  and carregamento.c6hnumat = servico_carregamento.c6bnumat                                                                       "+
	                "  and servico_carregamento.c6bcdtps = desc_servico.C6XCDTPS                                                                       "+
	                "  and carregamento.c6hcdflx = fluxo.cu4cdflx                                                                                      "+
	                "  and fluxo.CU4IDITF = item_ferroviario.CZWIDENT                                                                                  "+
	                "  and item_ferroviario.CZWIDENT = preco_item.CZXIDITF                                                                             "+
	                "  and preco_item.CZXident =                                                                                                       "+
	                "      (select max(x.czxident)                                                                                                     "+
	                "         from czxprift x                                                                                                          "+
	                "        where carregamento.c6hcdflx = fluxo.cu4cdflx                                                                              "+
	                "          and fluxo.CU4IDITF = item_ferroviario.CZWIDENT                                                                          "+
	                "          and item_ferroviario.CZWIDENT = x.CZXIDITF                                                                              "+
	                "          and x.czxdativ <= carregamento.c6hdtemi                                                                                 "+
	                "          and x.czxdattv >= carregamento.c6hdtemi                                                                                 "+
	                "          and x.czxdtcri <= carregamento.c6hdtcri)                                                                                "+
	                "  and preco_item.CZXident = servico_fluxo.CU3IDPIF                                                                                "+
	                "  and fluxo.cu4cdflx = versao_fluxo.c6ccdflx                                                                                      "+
	                "  and versao_fluxo.c6cident =                                                                                                     "+
	                "      (select max(y.c6cident)                                                                                                     "+
	                "         from c6cacsft y                                                                                                          "+
	                "        where carregamento.c6hcdflx = fluxo.cu4cdflx                                                                              "+
	                "          and fluxo.cu4cdflx = y.c6ccdflx                                                                                         "+
	                "          and y.c6cdativ <= carregamento.c6hdtemi                                                                                 "+
	                "          and y.c6cdattv >= carregamento.c6hdtemi                                                                                 "+
	                "          and y.c6cdtcri <= carregamento.c6hdtcri)                                                                                "+
	                "  and versao_fluxo.c6cident = servico_versao_fluxo.c6fideac                                                                       "+
	                "  and servico_versao_fluxo.c6fcdtps = servico_carregamento.C6BCDTPS                                                               "+
	                "  and servico_versao_fluxo.c6fcdfer = servico_carregamento.c6bcdfer                                                               "+
	                "  and servico_versao_fluxo.c6fident = servico_fluxo.cu3idsvf                                                                      "+
	                "  and carregamento.c6hserie = rascunho.c8sserie                                                                                   "+
	                "  and carregamento.c6hnumde = rascunho.c8snumde                                                                                   "+
	                "  and carregamento.c6hnumat = rascunho.c8snumat                                                                                   "+
	                "  and rascunho.c8sidras = solicitacao.c8nidras                                                                                    "+
	                "  and solicitacao.c8nidsol = doc_fiscal.c8iidsol                                                                                  "+
	                "  and carregamento.c6hserie = :serie                                                                                              "+
	                "  and carregamento.c6hnumde = :carregamento                                                                                       "+
	                "  and carregamento.c6hnumat = :versao                                                                                              "
	                ;
	                  

	public static String queryConsultarServicos_3 = 
	        "  select carregamento.c6hserie as SERIE,                                                                                             		  "+
	                "        carregamento.c6hnumde as CARREGAMENTO,                                                                                       "+
	                "          carregamento.c6hnumat as VERSAO,                                                                                           "+
	                "          decode(servico_carregamento.c6bcdfer,'03','EFVM','04','EFC','06','FCA','12','FNS','07','MRS','ND') as FERROVIA_SERVICO,    "+    
	                "          servico_versao_fluxo.c6fident,                                                                                             "+
	                "          servico_carregamento.C6BCDTPS as COD_SERVICO,                                                                              "+
	                "          desc_servico.C6XDESCR as DESCRICAO,                                                                                        "+
	                "          '' UNIDADE_MEDIDA,                                                                                                         "+
	                "          servico_carregamento.c6bqtdec as PESO_CALCULO,                                                                             "+
	                "          NVL(servico_carregamento.C6BVALOR/servico_carregamento.c6bqtdec,0) as TARIFA_UNIT_S_IMPOSTO,                               "+
	                "          servico_carregamento.C6BVALOR as VALOR_SERVICO,                                                                            "+
	                "          (select impostos.aliq_icms                                                                                                 "+
	                "             from tcfl_cor_dof impostos2, TCFL_COR_IDF impostos                                                                      "+
	                "            where impostos2.dof_import_numero =                                                                                      "+
	                "                  'TCFL' || servicos_tcfl.lodanosl || servicos_tcfl.lodnumsl                                                         "+
	                "              and impostos2.ID = impostos.DOF_ID) as ALIQUOTA_ICMS,                                                                  "+
	                "          (select impostos.vl_base_icms                                                                                              "+
	                "             from tcfl_cor_dof impostos2, TCFL_COR_IDF impostos                                                                      "+
	                "            where impostos2.dof_import_numero =                                                                                      "+
	                "                  'TCFL' || servicos_tcfl.lodanosl || servicos_tcfl.lodnumsl                                                         "+
	                "              and impostos2.ID = impostos.DOF_ID) as VALOR_BASE_ICMS,                                                                "+
	                "          servicos_tcfl.lodvlicm as VALOR_ICMS,                                                                                      "+
	                "          (select impostos.VL_ALIQ_PIS                                                                                               "+
	                "             from tcfl_cor_dof impostos2, TCFL_COR_IDF impostos                                                                      "+
	                "            where impostos2.dof_import_numero =                                                                                      "+
	                "                  'TCFL' || servicos_tcfl.lodanosl || servicos_tcfl.lodnumsl                                                         "+
	                "              and impostos2.ID = impostos.DOF_ID) as ALIQUOTA_PIS,                                                                   "+
	                "          (select impostos.vl_base_pis                                                                                               "+
	                "             from tcfl_cor_dof impostos2, TCFL_COR_IDF impostos                                                                      "+
	                "            where impostos2.dof_import_numero =                                                                                      "+
	                "                  'TCFL' || servicos_tcfl.lodanosl || servicos_tcfl.lodnumsl                                                         "+
	                "              and impostos2.ID = impostos.DOF_ID) as VALOR_BASE_PIS,                                                                 "+
	                "          servicos_tcfl.LODVLPIS as VALOR_PIS,                                                                                       "+
	                "          (select impostos.VL_ALIQ_COFINS                                                                                            "+
	                "             from tcfl_cor_dof impostos2, TCFL_COR_IDF impostos                                                                      "+
	                "            where impostos2.dof_import_numero =                                                                                      "+
	                "                  'TCFL' || servicos_tcfl.lodanosl || servicos_tcfl.lodnumsl                                                         "+
	                "              and impostos2.ID = impostos.DOF_ID) as ALIQUOTA_COFINS,                                                                "+
	                "          (select impostos.vl_base_cofins                                                                                            "+
	                "             from tcfl_cor_dof impostos2, TCFL_COR_IDF impostos                                                                      "+
	                "            where impostos2.dof_import_numero =                                                                                      "+
	                "                  'TCFL' || servicos_tcfl.lodanosl || servicos_tcfl.lodnumsl                                                         "+
	                "              and impostos2.ID = impostos.DOF_ID) as VALOR_BASE_COFINS,                                                              "+
	                "          servicos_tcfl.LODVLCOF as VALOR_COFINS,                                                                                    "+
	                "          (select impostos.vl_base_cofins                                                                                            "+
	                "             from tcfl_cor_dof impostos2, TCFL_COR_IDF impostos                                                                      "+
	                "            where impostos2.dof_import_numero =                                                                                      "+
	                "                  'TCFL' || servicos_tcfl.lodanosl || servicos_tcfl.lodnumsl                                                         "+
	                "              and impostos2.ID = impostos.DOF_ID) as VALOR_BASE_COFINS,                                                              "+
	                "           (select impostos.ALIQ_ISS                                                                                                 "+
	                "             from tcfl_cor_dof impostos2, TCFL_COR_IDF impostos                                                                      "+
	                "            where impostos2.dof_import_numero =                                                                                      "+
	                "                  'TCFL' || servicos_tcfl.lodanosl || servicos_tcfl.lodnumsl                                                         "+
	                "              and impostos2.ID = impostos.DOF_ID) as ALIQUOTA_ISS,                                                                   "+
	                "          (select impostos.vl_base_ISS                                                                                               "+
	                "             from tcfl_cor_dof impostos2, TCFL_COR_IDF impostos                                                                      "+
	                "            where impostos2.dof_import_numero =                                                                                      "+
	                "                  'TCFL' || servicos_tcfl.lodanosl || servicos_tcfl.lodnumsl                                                         "+
	                "              and impostos2.ID = impostos.DOF_ID) as VALOR_BASE_ISS,                                                                 "+
	                "          servicos_tcfl.LODVLISS as VALOR_ISS                                                                                        "+
	                "       from c6hdefet carregamento,                                                                                                   "+
	                "          C6BSEDFT servico_carregamento,                                                                                             "+
	                "          C6XSERVT desc_servico,                                                                                                     "+
	                "          cu4heflt fluxo,                                                                                                            "+
	                "          czwitfet item_ferroviario,                                                                                                 "+
	                "          c6cacsft versao_fluxo,                                                                                                     "+
	                "          c6fserft servico_versao_fluxo,                                                                                             "+
	                "          c8sdesft rascunho,                                                                                                         "+
	                "          c8nsofat solicitacao,                                                                                                      "+
	                "          LODCPIST servicos_tcfl                                                                                                     "+
	                "                                                                                                                                     "+
	                "    where carregamento.c6hserie = servico_carregamento.c6bserie                                                                      "+
	                "      and carregamento.c6hnumde = servico_carregamento.c6bnumde                                                                      "+
	                "      and carregamento.c6hnumat = servico_carregamento.c6bnumat                                                                      "+
	                "      and servico_carregamento.c6bcdtps = desc_servico.C6XCDTPS                                                                      "+
	                "      and carregamento.c6hcdflx = fluxo.cu4cdflx                                                                                     "+
	                "      and fluxo.CU4IDITF = item_ferroviario.CZWIDENT                                                                                 "+
	                "      and fluxo.cu4cdflx = versao_fluxo.c6ccdflx                                                                                     "+
	                "      and versao_fluxo.c6cident =                                                                                                    "+
	                "          (select max(y.c6cident)                                                                                                    "+
	                "             from c6cacsft y                                                                                                         "+
	                "            where carregamento.c6hcdflx = fluxo.cu4cdflx                                                                             "+
	                "              and fluxo.cu4cdflx = y.c6ccdflx                                                                                        "+
	                "              and y.c6cdativ <= carregamento.c6hdtemi                                                                                "+
	                "              and y.c6cdattv >= carregamento.c6hdtemi                                                                                "+
	                "              and y.c6cdtcri <= carregamento.c6hdtcri)                                                                               "+
	                "      and versao_fluxo.c6cident = servico_versao_fluxo.c6fideac                                                                      "+
	                "      and servico_versao_fluxo.c6fcdtps = servico_carregamento.C6BCDTPS                                                              "+
	                "      and servico_versao_fluxo.c6fcdfer = servico_carregamento.c6bcdfer                                                              "+
	                "      and carregamento.c6hserie = rascunho.c8sserie                                                                                  "+
	                "      and carregamento.c6hnumde = rascunho.c8snumde                                                                                  "+
	                "      and carregamento.c6hnumat = rascunho.c8snumat                                                                                  "+
	                "      and rascunho.c8sidras = solicitacao.c8nidras                                                                                   "+
	                "      and solicitacao.c8nanopr = servicos_tcfl.lodanosl                                                                              "+
	                "      and solicitacao.c8nnumpr = servicos_tcfl.lodnumsl                                                                              "+
	                "      and trim(servico_carregamento.C6BCDTPS) = trim(servicos_tcfl.lodcdcpn)                                                         "+
	                "                                                                                                                                     "+
	                "      and carregamento.c6hserie = :serie                                                                                             "+
	                "      and carregamento.c6hnumde = :carregamento                                                                                      "+
	                "      and carregamento.c6hnumat = :versao                                                                                            "
	        		;
	public static String queryIsDocCargaValida = 
			" select count(1), c6h.c6hvltri as VALOR_SERVICO from c6hdefet c6h, c8sdesft c8s, czlraagt czl  "+
			" where c6h.c6hcdfer = c8s.c8scdfer                              "+
			" and   c6h.c6hserie = c8s.c8sserie                              "+
			" and   c6h.c6hnumde = c8s.c8snumde                              "+
			" and   c8s.c8sidras = czl.czlident                              "+
			" and   czl.czltpras in ('NCM','NCS','NCSV','NC','NCES','NCVI')    "+
			" and   c6h.c6hserie = :serie                                    "+
			" and   c6h.c6hnumde = :carregamento                             "+
			" and   c6h.c6hnumat = 1                             "+
			" group by c6h.c6hvltri                                                     ";
	
	public static String queryInserirServicos = 
			"INSERT INTO " + 
					" CTE_SERVICO " +
					" (ID_CTE, 						" +
					"COD_SERVICO, " +
					"DESCRICAO,  "+
					"UNIDADE_MEDIDA, " +
					"PESO_CALCULO, " +
					"TARIFA_UNIT_S_IMPOSTO, " +
					"VALOR_SERVICO, " +
					"ALIQUOTA_ICMS, " +
					"VALOR_BASE_ICMS, " +
					"VALOR_ICMS, " +
					"ALIQUOTA_PIS, " +
					"VALOR_BASE_PIS, " +
					"VALOR_PIS, " +
					"ALIQUOTA_COFINS, " +
					"VALOR_BASE_COFINS, " +
					"VALOR_COFINS, " +
					"ALIQUOTA_ISS, " +
					"VALOR_BASE_ISS, " +
					"VALOR_ISS," +
					"FERROVIA_SERVICO)"+
					" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
		;
	
	public static String queryInserirVagoes = 
			" INSERT INTO CTE_VAGAO "+
					" (ID_CTE				,"+
					" SERIE_VAGAO		    ,"+
					" CODIGO_VAGAO			,"+
					" UNIDADE_VAGAO	    	,"+
					" PESO_REAL				,"+
					" PESO_CALCULADO		,"+
					" NUM_ROMANEIO			,"+
					" NUM_PEDIDO			,"+
					" MODELO_NOTA		    ,"+
					" SERIE_NOTA		    ,"+
					" NUMERO_NOTA			,"+
					" DATA_EMISSAO	    	,"+
					" VALOR_BASE_ICMS		,"+
					" VALOR_TOTAL_ICMS		,"+
					" VALOR_BASE_IPI	    ,"+
					" VALOR_TOTAL_IPI	    ,"+
					" VALOR_TOTAL_PRODUTOS	,"+
					" VALOR_NF		    	,"+
					" CFOP_PREDOMINANTE		,"+
					" PESO_RATEADO			,"+
					" PIN_SUFRAMA		    ,"+
					" DT_PREV_ENTREGA	    ,"+
					" ID_VAGAO		    	,"+
					" VOLUME 				,"+
					" TIPO_NOTA_FISCAL_MERCADORIA,"+
					" CHAVE_MERCADORIA,"+
					" TIPO_PESO,"+
					" PESO_MINIMO)"+
					" VALUES 				" + 
					"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
	;
	
	public static String queryConsultarVagoes = 
			"	   SELECT carregamento.c6hserie AS serie_carregamento          		   "+
					"	  ,carregamento.c6hnumde AS numero_carregamento                "+
					"	  ,carregamento.c6hnumat AS atualizacao_carregamento           "+
					"	  ,serie_vagao.SV_COD_SER AS SERIE_VAGAO                        "+
					"	  ,vagao_unifer.VG_COD_VAG AS CODIGO_VAGAO                     "+
					"	  ,'' UNIDADE_VAGAO                                            "+
					"	  ,vagao.C6GPESOR AS PESO_REAL                                 "+
					"	  ,vagao.C6GQTDEC AS PESO_CALCULADO                            "+
					"	  ,nota_fiscal_carreg.C6JNUROM AS NUM_ROMANEIO                 "+
					"	  ,nota_fiscal_carreg.C6JNUPED AS NUM_PEDIDO                   "+
					"	  ,nota_fiscal_carreg.C6JTPMOD AS MODELO_NOTA                  "+
					"	  ,nota_fiscal_carreg.C6JCODSE AS SERIE_NOTA                   "+
					"	  ,nota_fiscal_carreg.C6JNUMNF AS NUMERO_NOTA                  "+
					"	  ,nota_fiscal_carreg.C6JDTEMI AS DATA_EMISSAO                 "+
					"	  ,nota_fiscal_carreg.C6JVLBIC AS VALOR_BASE_ICMS              "+
					"	  ,nota_fiscal_carreg.C6JVLTIC AS VALOR_TOTAL_ICMS             "+
					"	  ,'' VALOR_BASE_IPI                                           "+
					"	  ,'' VALOR_TOTAL_IPI                                          "+
					"	  ,nota_fiscal_carreg.C6JVLPRO AS VALOR_TOTAL_PRODUTOS         "+
					"	  ,nota_fiscal_carreg.C6JVALOR AS VALOR_NF                     "+
					"	  ,nota_fiscal_carreg.C6JCFOPP AS CFOP_PREDOMINANTE            "+
					"	  ,nota_fiscal_carreg.C6JPESOV AS PESO_RATEADO    			   "+
					"	  ,nota_fiscal_carreg.C6JNUPIN AS PIN_SUFRAMA                  "+
					"	  ,'' DT_PREV_ENTREGA                                          "+
					"	  ,nota_fiscal_carreg.c6jtpdoo   AS TIPO_NOTA_FISCAL_MERCADORIA  "+
					"	  ,nota_fiscal_carreg.c6jchave AS CHAVE_MERCADORIA  "+
					"	  FROM c6hdefet carregamento                                   "+
					"	  ,C6JNFDET nota_fiscal_carreg                                 "+
					"	  ,CV7NFVAT nota_vagao                                         "+
					"	  ,VAGAO_UNICOM vagao_unifer                                   "+
					"	  ,SERIE_VAGAO_UNICOM serie_vagao                              "+
					"	  ,C6GVADET vagao                                              "+
					"	  WHERE carregamento.c6hserie = nota_fiscal_carreg.c6jserie    "+
					"	  AND carregamento.c6hnumde = nota_fiscal_carreg.c6jnumde      "+
					"	  AND carregamento.c6hnumat = nota_fiscal_carreg.c6jnumat      "+
					"	  AND carregamento.c6hserie = vagao.c6gserie                   "+
					"	  AND carregamento.c6hnumde = vagao.c6gnumde                   "+
					"	  AND carregamento.c6hnumat = vagao.c6gnumat                   "+
					"	  AND carregamento.c6hserie = nota_vagao.cv7serie              "+
					"	  AND carregamento.c6hnumde = nota_vagao.cv7numde              "+
					"	  AND carregamento.c6hnumat = nota_vagao.cv7numat              "+
					"	  AND nota_vagao.cv7nofis = nota_fiscal_carreg.c6jnofis        "+
					"	  AND nota_vagao.cv7idvag = vagao.c6gidvag                     "+
					"	  AND vagao.c6gidvag = vagao_unifer.VG_ID_VG                   "+
					"	  AND vagao_unifer.SV_ID_SV = serie_vagao.sv_id_sv             "+
					"	  AND carregamento.c6hserie = ?                           	   "+
					"	  AND carregamento.c6hnumde = ?                                "+
					"	  AND carregamento.c6hnumat = ?                                "
			;
	
	public static String queryConsultarDadosDocAnterior = 
			"  select c8i_c.c8ichcte CHAVE_CTE_ANTERIOR, c8i_c.c8iserdf SERIE_ANTERIOR, c8i_c.c8inrnot DOCUMENTO_ANTERIOR, to_char(c8i_c.c8idtemi,'dd/MM/yyyy') DATA_EMISSAO_DOC_ANTERIOR  "+
					"  from cuxredct cux_c,                     "+
					"       c6hdefet c6h_c,                     "+
					"       c8sdesft c8s_c,                     "+
					"       czlraagt czl_c,                     "+
					"       c8nsofat c8n_c,                     "+
					"       c8ifatut c8i_c                      "+
					" where cux_c.cuxserct = c6h_c.c6hserie     "+
					"   and cux_c.cuxnumct = c6h_c.c6hnumde     "+
					"   and c6h_c.c6hnumat = ?			        "+
					"   and c6h_c.c6hserie = c8s_c.c8sserie     "+
					"   and c6h_c.c6hnumde = c8s_c.c8snumde     "+
					"   and c6h_c.c6hnumat = c8s_c.c8snumat     "+
					"   and c8s_c.c8sidras = czl_c.czlident     "+
					"   and czl_c.czlident = c8n_c.c8nidras     "+
					"   and c8n_c.c8nidsol = c8i_c.c8iidsol     "+
					"   and cux_c.cuxserie = ?			        "+
					"   and cux_c.cuxnumde = ?                  "+
					"   and rownum = 1                          ";

}
