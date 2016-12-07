
public class Constantes {
	
	public static String querycarregamento = " select decode(c6h.c6hcdfer,                                                                     		  " +
			"               '03',                                                                             " +
			"               'EFVM',                                                                           " +
			"               '04',                                                                             " +
			"               'EFC',                                                                            " +
			"               '06',                                                                             " +
			"               'FCA',                                                                            " +
			"               '12',                                                                             " +
			"               'FNS',                                                                            " +
			"               '07',                                                                             " +
			"               'MRS',                                                                            " +
			"               'ND') CODIGO_FERROVIA,                                                            " +
			"        c6h.c6hserie SERIE_CARREGAMENTO,                                                         " +
			"        c6h.c6hnumde NUMERO_CARREGAMENTO,                                                        " +
			"        c6h.c6hnumat VERSAO_CARREGAMENTO,                                                        " +
			"        c6h.c6hdtemi DATA_EMISSAO,                                                               " +
			"        c6h.c6htpdes TIPO_CARREGAMENTO,                                                          " +
			"        c6h.c6hsitua SITUACAO_CARREGAMENTO,                                                      " +
			"        cu4.cu4cdflx             CODIGO_FLUXO,                                                   " +
			"        estacao_origem.RIENUSEQ ID_ORIGEM,                                                       " +
			"        estacao_destino.RIENUSEQ ID_DESTINO,                                                     " +
			"        estacao_origem.RIECDLOC  CODIGO_ORIGEM,                                                  " +
			"        estacao_destino.RIECDLOC CODIGO_DESTINO,                                                 " +
			"        c6c.c6cnustc             CODIGO_TERMINAL_CARGA,                                          " +
			"        c6c.c6cnustd             CODIGO_TERMINAL_DESCARGA,                                       " +
			"        c6c.c6ccdmer             CODIGO_MERCADORIA,                                              " +
			"        emitente.cli_razsoc RAZAO_SOCIAL_EMITENTE,                                               " +
			"        emitente.cli_Endere ENDERECO_EMITENTE,                                                   " +
			"        emitente.cli_Numcep CEP_EMITENTE,                                                        " +
			"        emitente.cli_Bairro BAIRRO_EMITENTE,                                                     " +
			"        emitente.cli_Cidade CIDADE_EMITENTE,                                                     " +
			"                                                                                                 " +
			"        NULL IBGE_EMITENTE,                                                                      " +
			"        NULL DESC_ESTADO_EMITENTE,                                                               " +
			"                                                                                                 " +
			"        emitente.cli_estado COD_ESTADO_EMITENTE,                                                 " +
			"        emitente.cli_numero || emitente.cli_estabe || emitente.cli_digito CNPJ_EMITENTE,         " +
			"                                                                                                 " +
			"        emitente.cli_insest INSC_ESTADUAL_EMITENTE,                                              " +
			"                                                                                                 " +
			"        c8i.c8iserdf         SERIE_FATURA,                                                       " +
			"        c8i.c8inrnot         NUMERO_FATURA,                                                      " +
			"        c8i.c8idtemi         DATA_FATURA,                                                        " +
			"        c6c.c6cindts         TIPO_FATURA,                                                        " +
			"        c8n.c8nstats         STATUS_FATURA,                                                      " +
			"        c8i.c8ichcte         CHAVE_CTE,                                                          " +
			"                                                                                                 " +
			"		 remetente.cli_tipcli TIPO_CLIENTE_REMETENTE,											  " +
			"        remetente.CLI_RAZSOC RAZAO_SOCIAL_REMETENTE,                                             " +
			"        remetente.Cli_Endere ENDERECO_REMETENTE,                                                 " +
			"        remetente.Cli_Numcep CEP_REMETENTE,                                                      " +
			"        remetente.Cli_Bairro BAIRRO_REMETENTE,                                                   " +
			"        remetente.Cli_Cidade CIDADE_REMETENTE,                                                   " +
			"        ibge_rem.apdcdmun IBGE_REMETENTE,                                                        " +
			"        ibge_rem.apdnomca DESC_ESTADO_REMETENTE,                                                 " +
			"        remetente.cli_estado COD_ESTADO_REMETENTE,                                               " +
			"        remetente.cli_numero || remetente.cli_estabe || remetente.cli_digito CNPJ_REMETENTE,     " +
			"        remetente.cli_insest INSC_ESTADUAL_REMETENTE,                                            " +
			"                                                                                                 " +
			"        destinatario.CLI_RAZSOC RAZAO_SOCIAL_DESTINATARIO,                                       " +
			"        destinatario.Cli_Endere ENDERECO_DESTINATARIO,                                           " +
			"        destinatario.Cli_Numcep CEP_DESTINATARIO,                                                " +
			"        destinatario.Cli_Bairro BAIRRO_DESTINATARIO,                                             " +
			"        destinatario.Cli_Cidade CIDADE_DESTINATARIO,                                             " +
			"        ibge_des.apdcdmun IBGE_DESTINATARIO,                                                     " +
			"        ibge_des.apdnomca DESC_ESTADO_DESTINATARIO,                                              " +
			"        destinatario.cli_estado COD_ESTADO_DESTINATARIO,                                         " +
			"        destinatario.cli_numero || destinatario.cli_estabe ||                                    " +
			"        destinatario.cli_digito CNPJ_DESTINATARIO,                                               " +
			"        destinatario.cli_insest INSC_ESTADUAL_DESTINATARIO,                                      " +
			"                                                                                                 " +
			"        NULL RAZAO_SOCIAL_EXPEDIDOR,                                                             " +
			"        NULL ENDERECO_EXPEDIDOR,                                                                 " +
			"        NULL CEP_EXPEDIDOR,                                                                      " +
			"        NULL BAIRRO_EXPEDIDOR,                                                                   " +
			"        NULL CIDADE_EXPEDIDOR,                                                                   " +
			"        NULL IBGE_EXPEDIDOR,                                                                     " +
			"        NULL DESC_ESTADO_EXPEDIDOR,                                                              " +
			"        NULL COD_ESTADO_EXPEDIDOR,                                                               " +
			"        NULL CNPJ_EXPEDIDOR,                                                                     " +
			"        NULL INSC_ESTADUAL_EXPEDIDOR,                                                            " +
			"                                                                                                 " +
			"        NULL RAZAO_SOCIAL_RECEBEDOR,                                                             " +
			"        NULL ENDERECO_RECEBEDOR,                                                                 " +
			"        NULL CEP_RECEBEDOR,                                                                      " +
			"        NULL BAIRRO_RECEBEDOR,                                                                   " +
			"        NULL CIDADE_RECEBEDOR,                                                                   " +
			"        NULL IBGE_RECEBEDOR,                                                                     " +
			"        NULL DESC_ESTADO_RECEBEDOR,                                                              " +
			"        NULL COD_ESTADO_RECEBEDOR,                                                               " +
			"        NULL CNPJ_RECEBEDOR,                                                                     " +
			"        NULL INSC_ESTADUAL_RECEBEDOR,                                                            " +
			"                                                                                                 " +
			"        ClienteCorrentista.CLI_RAZSOC RAZAO_SOCIAL_TOMADOR,                                      " +
			"        ClienteCorrentista.Cli_Endere ENDERECO_TOMADOR,                                          " +
			"        ClienteCorrentista.Cli_Numcep CEP_TOMADOR,                                               " +
			"        ClienteCorrentista.Cli_Bairro BAIRRO_TOMADOR,                                            " +
			"        ClienteCorrentista.Cli_Cidade CIDADE_TOMADOR,                                            " +
			"        ibge_cor.apdcdmun IBGE_TOMADOR,                                                          " +
			"        ibge_cor.apdnomca DESC_ESTADO_TOMADOR,                                                   " +
			"        ClienteCorrentista.cli_estado COD_ESTADO_TOMADOR,                                        " +
			"        ClienteCorrentista.cli_numero || ClienteCorrentista.cli_estabe ||                        " +
			"        ClienteCorrentista.cli_digito CNPJ_TOMADOR,                                              " +
			"        ClienteCorrentista.cli_insest INSC_ESTADUAL_TOMADOR,                                     " +
			"                                                                                                 " +
			"        (select c8i_c.c8ichcte                                                                   " +
			"           from cuxredct cux_c,                                                                  " +
			"                c6hdefet c6h_c,                                                                  " +
			"                c8sdesft c8s_c,                                                                  " +
			"                czlraagt czl_c,                                                                  " +
			"                c8nsofat c8n_c,                                                                  " +
			"                c8ifatut c8i_c                                                                   " +
			"          where cux_c.cuxserct = c6h_c.c6hserie                                                  " +
			"            and cux_c.cuxnumct = c6h_c.c6hnumde                                                  " +
			"            and c6h.c6hnumat = c6h_c.c6hnumat                                                    " +
			"            and c6h_c.c6hserie = c8s_c.c8sserie                                                  " +
			"            and c6h_c.c6hnumde = c8s_c.c8snumde                                                  " +
			"            and c6h_c.c6hnumat = c8s_c.c8snumat                                                  " +
			"            and c8s_c.c8sidras = czl_c.czlident                                                  " +
			"            and czl_c.czlident = c8n_c.c8nidras                                                  " +
			"            and c8n_c.c8nidsol = c8i_c.c8iidsol                                                  " +
			"            and cux_c.cuxserie = c6h.c6hserie                                                    " +
			"            and cux_c.cuxnumde = c6h.c6hnumde                                                    " +
			"            and rownum = 1) CHAVE_CTE_ANTERIOR,                                                  " +
			"                                                                                                 " +
			"        ClienteCorrentista.CLI_RAZSOC RAZAO_SOCIAL_EMISSOR,                                      " +
			"        ClienteCorrentista.Cli_Endere ENDERECO_EMISSOR,                                          " +
			"        ClienteCorrentista.Cli_Numcep CEP_EMISSOR,                                               " +
			"        ClienteCorrentista.Cli_Bairro BAIRRO_EMISSOR,                                            " +
			"        ClienteCorrentista.Cli_Cidade CIDADE_EMISSOR,                                            " +
			"        ibge_cor.apdcdmun IBGE_EMISSOR,                                                          " +
			"        ibge_cor.apdnomca DESC_ESTADO_EMISSOR,                                                   " +
			"        ClienteCorrentista.cli_estado COD_ESTADO_EMISSOR,                                        " +
			"        ClienteCorrentista.cli_numero || ClienteCorrentista.cli_estabe ||                        " +
			"        ClienteCorrentista.cli_digito CNPJ_EMISSOR,                                              " +
			"        ClienteCorrentista.cli_insest INSC_ESTADUAL_EMISSOR,                                     " +
			"                                                                                                 " +
			"        NULL CTE_SERIE,                                                                          " +
			"        NULL CTE_NUMERO,                                                                         " +
			"        NULL CTE_EMISSAO,                                                                        " +
			" 	                                                                                              " +
			" 	   c8g.c8gorgid ORG_ID                                                                        " +
			"                                                                                                 " +
			"   from c8nsofat c8n,                                                                            " +
			"        c8ifatut c8i,                                                                            " +
			"        c8sdesft c8s,                                                                            " +
			"        czlraagt czl,                                                                            " +
			"        c6hdefet c6h,                                                                            " +
			"                                                                                                 " +
			"        rieuclgv estacao_origem,                                                                 " +
			"        rieuclgv estacao_destino,                                                                " +
			"                                                                                                 " +
			"        czwitfet czw,                                                                            " +
			"        cu4heflt cu4,                                                                            " +
			"        c6cacsft c6c,                                                                            " +
			"                                                                                                 " +
			"        c6vestft            c6v,                                                                 " +
			"        HZ_LOCATIONS_UNICOM emitente,                                                            " +
			"                                                                                                 " +
			"        hz_locations_unicom cliente,                                                             " +
			"        cueclout            cue,                                                                 " +
			"        c8gcofet            c8g,                                                                 " +
			"                                                                                                 " +
			"        hz_locations_unicom ClienteCorrentista,                                                  " +
			"        c6zclfct            ClienteUnicom_cor,                                                   " +
			"        cuareflt            RelComEmpresaFluxo_cor,                                              " +
			"        cueclout            ClienteOrgUnit_cor,                                                  " +
			"        apduclgv            ibge_cor,                                                            " +
			"                                                                                                 " +
			"        hz_locations_unicom remetente,                                                           " +
			"        c6zclfct            ClienteUnicom_rem,                                                   " +
			"        cuareflt            RelComEmpresaFluxo_rem,                                              " +
			"        cueclout            ClienteOrgUnit_rem,                                                  " +
			"        apduclgv            ibge_rem,                                                            " +
			"                                                                                                 " +
			"        hz_locations_unicom destinatario,                                                        " +
			"        c6zclfct            ClienteUnicom_des,                                                   " +
			"        cuareflt            RelComEmpresaFluxo_des,                                              " +
			"        cueclout            ClienteOrgUnit_des,                                                  " +
			"        apduclgv            ibge_des                                                             " +
			"                                                                                                 " +
			"  where c8n.c8nncnpj = c6v.c6vncnpj                                                              " +
			"    and c8n.c8nnesta = c6v.c6vnesta                                                              " +
			"    and c6v.c6videlo = emitente.cli_identi                                                       " +
			"    and c8i.c8iidsol = c8n.c8nidsol                                                              " +
			"    and c8s.c8scdfer = c6h.c6hcdfer                                                              " +
			"    and c8s.c8sserie = c6h.c6hserie                                                              " +
			"    and c8s.c8snumde = c6h.c6hnumde                                                              " +
			"    and c8s.c8snumat = c6h.c6hnumat                                                              " +
			"    and c6h.c6hcdflx = cu4.cu4cdflx                                                              " +
			"    and czw.czwident = cu4.cu4iditf                                                              " +
			"    and czw.czwnuseo = estacao_origem.RIENUSEQ                                                   " +
			"    and czw.czwnused = estacao_destino.RIENUSEQ                                                  " +
			"                                                                                                 " +
			"    and czl.czlident = c8s.c8sidras                                                              " +
			"    and czl.czlident = c8n.c8nidras                                                              " +
			"    and cue.cueident = czl.czlidcli                                                              " +
			"                                                                                                 " +
			"    and cue.cueidelo = cliente.cli_identi                                                        " +
			"    and cue.cueorgid = c8g.c8gorgid                                                              " +
			"    and c8g.c8gcdfer =                                                                           " +
			"        decode(c6c.c6ccdfef, '03', '03', '04', '04', '12', '12', '06')                           " +
			"                                                                                                 " +
			"    and c6h.c6hcdflx = c6c.c6ccdflx                                                              " +
			"    and trunc(c6h.c6hdtemi) >= c6c.c6cdativ                                                      " +
			"    and trunc(c6h.c6hdtemi) <= c6c.c6cdattv                                                      " +
			"    and c6c.c6cdtcri =                                                                           " +
			"        (select max(c6c2.c6cdtcri)                                                               " +
			"           from c6cacsft c6c2                                                                    " +
			"          WHERE c6c2.c6ccdflx = c6c.c6ccdflx                                                     " +
			"            and trunc(c6h.c6hdtemi) >= c6c2.c6cdativ                                             " +
			"            and trunc(c6h.c6hdtemi) <= c6c2.c6cdattv)                                            " +
			"                                                                                                 " +
			"    and RelComEmpresaFluxo_cor.cuacodig = 'CN'                                                   " +
			"    and RelComEmpresaFluxo_cor.cuaidcli = ClienteUnicom_cor.c6zident                             " +
			"    and ClienteUnicom_cor.c6zident = ClienteOrgUnit_cor.cueident                                 " +
			"    and ClienteOrgUnit_cor.cueidelo = ClienteCorrentista.cli_identi                              " +
			"    and c6c.c6cident = RelComEmpresaFluxo_cor.cuavrflx                                           " +
			"    and ClienteOrgUnit_cor.cueorgid = c8g.c8gorgid                                               " +
			"    and ibge_cor.apdcdunf = SUBSTR(ClienteOrgUnit_cor.cuecdloc, 0, 2)                            " +
			"    and ibge_cor.apdcdmun = SUBSTR(ClienteOrgUnit_cor.cuecdloc, 3)                               " +
			"                                                                                                 " +
			"    and RelComEmpresaFluxo_rem.cuacodig = 'RE'                                                   " +
			"    and RelComEmpresaFluxo_rem.cuaidcli = ClienteUnicom_rem.c6zident                             " +
			"    and ClienteUnicom_rem.c6zident = ClienteOrgUnit_rem.cueident                                 " +
			"    and ClienteOrgUnit_rem.cueidelo = remetente.cli_identi                                       " +
			"    and c6c.c6cident = RelComEmpresaFluxo_rem.cuavrflx                                           " +
			"    and ClienteOrgUnit_rem.cueorgid = c8g.c8gorgid                                               " +
			"    and ibge_rem.apdcdunf = SUBSTR(ClienteOrgUnit_rem.cuecdloc, 0, 2)                            " +
			"    and ibge_rem.apdcdmun = SUBSTR(ClienteOrgUnit_rem.cuecdloc, 3)                               " +
			"                                                                                                 " +
			"    and RelComEmpresaFluxo_des.cuacodig = 'DE'                                                   " +
			"    and RelComEmpresaFluxo_des.cuaidcli = ClienteUnicom_des.c6zident                             " +
			"    and ClienteUnicom_des.c6zident = ClienteOrgUnit_des.cueident                                 " +
			"    and ClienteOrgUnit_des.cueidelo = destinatario.cli_identi                                    " +
			"    and c6c.c6cident = RelComEmpresaFluxo_des.cuavrflx                                           " +
			"    and ClienteOrgUnit_des.cueorgid = c8g.c8gorgid                                               " +
			"    and ibge_des.apdcdunf = SUBSTR(ClienteOrgUnit_des.cuecdloc, 0, 2)                            " +
			"    and ibge_des.apdcdmun = SUBSTR(ClienteOrgUnit_des.cuecdloc, 3)                               " +
			"    and c6h.c6htpdes in ('FER','SUP')                                                            " +
			"    and trunc(c6h.c6hdtemi) >= '01/01/2014'                               						  " +
			"    and trunc(c6h.c6hdtemi) <= '31/01/2014'                               						  " ;
	
	
	public String insertCarregamento = "INSERT INTO " + 
			"CARREGAMENTO_HEADER " +
			"(ID_CTE, 						" +
			"CODIGO_FERROVIA                ," +
			"SERIE_CARREGAMENTO             ," +
			"NUMERO_CARREGAMENTO            ," +
			"DATA_EMISSAO                   ," +
			"TIPO_CARREGAMENTO              ," +
			"SITUACAO_CARREGAMENTO          ," +
			"CODIGO_FLUXO                   ," +
			"CODIGO_ORIGEM                  ," +
			"CODIGO_DESTINO                 ," +
			"CODIGO_MERCADORIA              ," +
			"RAZAO_SOCIAL_EMITENTE          ," +
			"ENDERECO_EMITENTE              ," +
			"CEP_EMITENTE                   ," +
			"BAIRRO_EMITENTE                ," +
			"CIDADE_EMITENTE                ," +
			"IBGE_EMITENTE                  ," +
			"DESC_ESTADO_EMITENTE           ," +
			"COD_ESTADO_EMITENTE            ," +
			"CNPJ_EMITENTE                  ," +
			"INSC_ESTADUAL_EMITENTE         ," +
			"SERIE_FATURA                   ," +
			"NUMERO_FATURA                  ," +
			"DATA_FATURA                    ," +
			"TIPO_FATURA                    ," +
			"STATUS_FATURA                  ," +
			"CHAVE_CTE                      ," +
			"RAZAO_SOCIAL_REMETENTE         ," +
			"ENDERECO_REMETENTE             ," +
			"CEP_REMETENTE                  ," +
			"BAIRRO_REMETENTE               ," +
			"CIDADE_REMETENTE               ," +
			"IBGE_REMETENTE                 ," +
			"DESC_ESTADO_REMETENTE          ," +
			"COD_ESTADO_REMETENTE           ," +
			"CNPJ_REMETENTE                 ," +
			"INSC_ESTADUAL_REMETENTE        ," +
			"RAZAO_SOCIAL_DESTINATARIO      ," +
			"ENDERECO_DESTINATARIO          ," +
			"CEP_DESTINATARIO               ," +
			"BAIRRO_DESTINATARIO            ," +
			"CIDADE_DESTINATARIO            ," +
			"IBGE_DESTINATARIO              ," +
			"DESC_ESTADO_DESTINATARIO       ," +
			"COD_ESTADO_DESTINATARIO        ," +
			"CNPJ_DESTINATARIO              ," +
			"INSC_ESTADUAL_DESTINATARIO     ," +
			"RAZAO_SOCIAL_EXPEDIDOR         ," +
			"ENDERECO_EXPEDIDOR             ," +
			"CEP_EXPEDIDOR                  ," +
			"BAIRRO_EXPEDIDOR               ," +
			"CIDADE_EXPEDIDOR               ," +
			"IBGE_EXPEDIDOR                 ," +
			"DESC_ESTADO_EXPEDIDOR          ," +
			"COD_ESTADO_EXPEDIDOR           ," +
			"CNPJ_EXPEDIDOR                 ," +
			"INSC_ESTADUAL_EXPEDIDOR        ," +
			"RAZAO_SOCIAL_RECEBEDOR         ," +
			"ENDERECO_RECEBEDOR             ," +
			"CEP_RECEBEDOR                  ," +
			"BAIRRO_RECEBEDOR               ," +
			"CIDADE_RECEBEDOR               ," +
			"IBGE_RECEBEDOR                 ," +
			"DESC_ESTADO_RECEBEDOR          ," +
			"COD_ESTADO_RECEBEDOR           ," +
			"CNPJ_RECEBEDOR                 ," +
			"INSC_ESTADUAL_RECEBEDOR        ," +
			"RAZAO_SOCIAL_TOMADOR           ," +
			"ENDERECO_TOMADOR               ," +
			"CEP_TOMADOR                    ," +
			"BAIRRO_TOMADOR                 ," +
			"CIDADE_TOMADOR                 ," +
			"IBGE_TOMADOR                   ," +
			"DESC_ESTADO_TOMADOR            ," +
			"COD_ESTADO_TOMADOR             ," +
			"CNPJ_TOMADOR                   ," +
			"INSC_ESTADUAL_TOMADOR          ," +
			"CHAVE_CTE_ANTERIOR             ," +
			"RAZAO_SOCIAL_EMISSOR           ," +
			"ENDERECO_EMISSOR               ," +
			"CEP_EMISSOR                    ," +
			"BAIRRO_EMISSOR                 ," +
			"CIDADE_EMISSOR                 ," +
			"IBGE_EMISSOR                   ," +
			"DESC_ESTADO_EMISSOR            ," +
			"COD_ESTADO_EMISSOR             ," +
			"CNPJ_EMISSOR                   ," +
			"INSC_ESTADUAL_EMISSOR          ," +
			"CTE_SERIE                      ," +
			"CTE_NUMERO                     ," +
			"CTE_EMISSAO                    ," +
			"DT_EXECUCAO                    ," +
			"FLAG_LIDO                      )" +

			"VALUES " + 
			"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

}
