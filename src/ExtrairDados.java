
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

public class ExtrairDados {

	String  info_inicio;
	String	info_fim;

	private static Integer idCarregamento;

	private final static String sequenceCarregamento = "SELECT CARREGAMENTO_HEADER_SEQ.NEXTVAL FROM DUAL ";
	private final static String sequenceVagao = "SELECT CTE_VAGAO_SEQ.NEXTVAL FROM DUAL ";



	public ExtrairDados (String[] info)
	{
		this.info_inicio = info[0].toString();
		this.info_fim    = info[1].toString();		
	}

	public void consultarCarregamento() throws FileNotFoundException, IOException, SQLException
	{	
		Connection conn = null;
		try {

			conn = OracleConnection.getConnection();


		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		consultar(conn);

		if (!conn.isClosed())
		{	
			conn.close();
		}
	}

	private boolean isDocCargaValido(String serie, int numero, Long valorServicos, Connection conn) throws SQLException {

		String query =
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


		OraclePreparedStatement stmt	= null;
		OracleResultSet rs 				= null;
		boolean cargaValida 			= true;

		try{

			stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());

			stmt.setString      (1, serie);
			stmt.setInt         (2, numero);

			rs = (OracleResultSet) stmt.executeQuery();

			while (rs.next()){
				Long lValorServico =  rs.getLong("VALOR_SERVICO");

				if(lValorServico != null && Math.abs(lValorServico) == valorServicos){
					cargaValida = false;
				}
			} 
		} catch (SQLException e) {
			e.printStackTrace();

		}
		finally
		{      
			rs.close();
			stmt.close();
		}
		return cargaValida;
	}



	private void consultar(Connection conn) throws SQLException {


		String query = 
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
						"         decode(emitente.cli_estado, 'MG','MINAS GERAIS','BA','BAHIA','GO','GOIÁS','ES','ESPÍRITO SANTOS','MT','MATO GROSSO','SP','SÃO PAULO','' ) DESC_ESTADO_EMITENTE,           "+
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
						"         decode(remetente.cli_estado, 'MG','MINAS GERAIS','BA','BAHIA','GO','GOIÁS','ES','ESPÍRITO SANTOS','MT','MATO GROSSO','SP','SÃO PAULO','' )  DESC_ESTADO_REMETENTE,        "+
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
						"         decode(destinatario.cli_estado, 'MG','MINAS GERAIS','BA','BAHIA','GO','GOIÁS','ES','ESPÍRITO SANTOS','MT','MATO GROSSO','SP','SÃO PAULO','' ) DESC_ESTADO_DESTINATARIO,   "+
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
						"         decode(ClienteCorrentista.cli_estado, 'MG','MINAS GERAIS','BA','BAHIA','GO','GOIÁS','ES','ESPÍRITO SANTOS','MT','MATO GROSSO','SP','SÃO PAULO','' ) DESC_ESTADO_TOMADOR,  "+
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
						"         decode(ClienteCorrentista.cli_estado, 'MG','MINAS GERAIS','BA','BAHIA','GO','GOIÁS','ES','ESPÍRITO SANTOS','MT','MATO GROSSO','SP','SÃO PAULO','' ) DESC_ESTADO_EMISSOR,  "+
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
						"     and c6h.c6hunico in ('S')                                                                                                                                               		"+
						"     and czl.czltpras not in ('CNF','VICT','MNF','NCS','NDAS','NCAS','CCF','CTF','VIA','ACC','NCSV','NCM')																			"+		
						//						"     and trunc(c6h.c6hdtemi) >=  ?                                                                                                                                      			"+
						//						"     and trunc(c6h.c6hdtemi) <=  ?                                                                                                                                       			"+
						"     and c6h.c6hserie = '041'                                                                                                                                                       "+
						"     and c6h.c6hnumde = 19181                                                                                                                                               "+ 
						"     ORDER BY DATA_EMISSAO, SERIE_CARREGAMENTO, NUMERO_CARREGAMENTO, VERSAO_CARREGAMENTO                                                                                                                                                            ";



		OraclePreparedStatement stmt = null;
		OracleResultSet rs			 = null;

		try {

			stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());

			//stmt.setString(1, info_inicio);
			//stmt.setString(2, info_fim);


			rs = (OracleResultSet) stmt.executeQuery();
			int cont = 0;


			while (rs.next()) {

				String 	dataExecuçãoResult 	= info_inicio.replace("/", "").substring(2,8);
				String serie				= rs.getString("SERIE_CARREGAMENTO");
				int numero					= rs.getInt("NUMERO_CARREGAMENTO");
				String versao_carregamento 	= rs.getString("VERSAO_CARREGAMENTO");
				Long   valorServicos     	= rs.getLong("VALOR_SERVICOS");
				String unidadeMedida	  	= rs.getString("UNIDADE_MEDIDA");
				
				/*
				 * Verificação se o carregamento na versão zero tem um crédito total ou substituição. Neste caso o carregamento não será processado
				 * TODO Verificar se a proxima versao será credito e portanto não será processada
				 */
				if(versao_carregamento.equals("0") && !isDocCargaValido(serie,numero,valorServicos,conn))
				{
					continue;
				}



				boolean isVOF = (rs.getString("IND_EMPRESA_CONTRATADA").equals("VF"));
				boolean isGenerico = (rs.getString("IND_NIVEL_ESP_PERFIL_VAG").equals("G"));


				idCarregamento = getSequence(conn, sequenceCarregamento);

				try {

					inserirCarregamentos(conn,  idCarregamento,  dataExecuçãoResult, isVOF, rs);
					inserirServicos(conn, idCarregamento, serie, numero, versao_carregamento, unidadeMedida);
					inserirVagoes(conn, idCarregamento, serie, numero, versao_carregamento, unidadeMedida, isGenerico);

					cont++;
				}
				catch (SQLException e) {
					System.out.println(" Serie : "+  serie + " Numero Carregamento " + numero + " VersaoCarregamento" + versao_carregamento + " DataEmissao " + dataExecuçãoResult); 
					e.printStackTrace();
				}

				if((cont % 200) == 0){

					System.out.println("foram commitados "+ cont + " da Data :" + rs.getString("DATA_EMISSAO"));
					conn.commit();
					Date dt1 = new Date();

					SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", new Locale("pt", "BR"));
					String dateInicio = df.format(dt1);

					System.out.println("Horas Commit:" + dateInicio);
					System.out.println("Numero de carregamentos commitados" + cont);
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		finally
		{
			rs.close();
			stmt.close();
		}
	}

	private Integer getSequence(Connection conn, String stringSequence)throws SQLException{

		OraclePreparedStatement stmtSeq	= null;
		OracleResultSet rset 			= null;
		Integer idSequence 				= null;

		try{

			stmtSeq = (OraclePreparedStatement)conn.prepareStatement(stringSequence);

			rset = (OracleResultSet)stmtSeq.executeQuery();

			if ( rset.next() ) {
				if( rset.getObject( 1 ) != null ) {
					idSequence = rset.getInt( 1 );
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		finally
		{
			rset.close();
			stmtSeq.close();
		}
		return idSequence;
	}


	private void inserirCarregamentos(Connection conn, Integer idCarregamento, String dataExecuçãoResult, boolean isVOF, OracleResultSet rs)throws SQLException{


		String query = "INSERT INTO " + 
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
				"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		OraclePreparedStatement stmt = null;

		String serie = null;
		String numCarregamento = null;
		String versaoCarregamento = null;
		Date dataEmissao = null;

		try{

			stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());

			String orgId = rs.getString("ORG_ID") != null ? rs.getString("ORG_ID").toString() : null;


			DadosExpedidorRecebedor dadosExpedidorRecebedor = new DadosExpedidorRecebedor();
			DadosDocumentoAnterior  dadosDocumentoAnterior = new DadosDocumentoAnterior();

			String cod_terminal_carga = rs.getString("COD_TERMINAL_CARGA") != null ? rs.getString("COD_TERMINAL_CARGA").toString() : null;

			String cod_terminal_descarga = rs.getString("COD_TERMINAL_DESCARGA") != null ? rs.getString("COD_TERMINAL_DESCARGA").toString() : null;

			String cod_estacao_origem = rs.getString("ID_ORIGEM") != null ? rs.getString("ID_ORIGEM").toString() : null;

			String cod_estacao_destino = rs.getString("ID_DESTINO") != null ? rs.getString("ID_DESTINO").toString() : null;

			String tipoCliente = rs.getString("TIPO_CLIENTE_REMETENTE") != null ? rs.getString("TIPO_CLIENTE_REMETENTE").toString() : null;

			boolean isComplementar = rs.getString("TIPO_CARREGAMENTO").equals("SUP");
			
			String tipoDocFiscal = rs.getString("TIPO_DOC_FISCAL");

			stmt.setInt 		(1	, idCarregamento		   ) ;
			stmt.setString  	(2	, rs.getString("CODIGO_FERROVIA"));

			serie = rs.getString("SERIE_CARREGAMENTO");
			numCarregamento = rs.getString("NUMERO_CARREGAMENTO");
			versaoCarregamento = rs.getString("VERSAO_CARREGAMENTO");
			Date dateEmissao = rs.getDate  ("DATA_EMISSAO");


			if(!isVOF)
			{
				stmt.setString  (	3	, rs.getString("SERIE_CARREGAMENTO"));
				stmt.setInt     (	4	, rs.getInt   ("NUMERO_CARREGAMENTO"));
			}
			else
			{
				stmt.setString  (	3	, rs.getString("SERIE_CARREGAMENTO_ASSOC"));
				stmt.setInt    (	4	, rs.getInt("NUMERO_CARREGAMENTO_ASSOC"));
			}

			stmt.setString  (	5	, rs.getString("VERSAO_CARREGAMENTO"));
			stmt.setDate    (	6	, rs.getDate  ("DATA_EMISSAO"));
			stmt.setString  (	7	, rs.getString("TIPO_CARREGAMENTO"));
			stmt.setString  (	8	, rs.getString("SITUACAO_CARREGAMENTO"));
			stmt.setString  (	9	, rs.getString("CODIGO_FLUXO"));
			stmt.setString  (	10	, rs.getString("CODIGO_ORIGEM"));
			stmt.setString  (	11	, rs.getString("CODIGO_DESTINO"));
			stmt.setString  (	12	, rs.getString("CODIGO_MERCADORIA"));
			stmt.setString  (	13	, rs.getString("RAZAO_SOCIAL_EMITENTE"));
			stmt.setString  (	14	, rs.getString("ENDERECO_EMITENTE"));
			stmt.setString  (	15	, rs.getString("CEP_EMITENTE"));
			stmt.setString  (	16	, rs.getString("BAIRRO_EMITENTE"));
			stmt.setString  (	17	, rs.getString("CIDADE_EMITENTE"));
			stmt.setString  (	18	, rs.getString("IBGE_EMITENTE"));
			stmt.setString  (	19	, rs.getString("DESC_ESTADO_EMITENTE"));
			stmt.setString  (	20	, rs.getString("COD_ESTADO_EMITENTE"));
			stmt.setString  (	21	, rs.getString("CNPJ_EMITENTE"));
			stmt.setString  (	22	, rs.getString("INSC_MUNICIPAL_EMITENTE"));
			stmt.setString  (	23	, rs.getString("INSC_ESTADUAL_EMITENTE"));
			stmt.setString  (	24	, rs.getString("SERIE_FATURA"));
			stmt.setString  (	25	, rs.getString("NUMERO_FATURA"));
			stmt.setDate    (	26	, rs.getDate  ("DATA_FATURA"));
			stmt.setString  (	27	, rs.getString("TIPO_SERVICO_CTE"));
			stmt.setString  (	28	, rs.getString("STATUS_FATURA"));
			stmt.setString  (	29	, rs.getString("CHAVE_CTE"));
			stmt.setString  (	30	, rs.getString("RAZAO_SOCIAL_REMETENTE"));
			stmt.setString  (	31	, rs.getString("ENDERECO_REMETENTE"));
			stmt.setString  (	32	, rs.getString("CEP_REMETENTE"));
			stmt.setString  (	33	, rs.getString("BAIRRO_REMETENTE"));
			stmt.setString  (	34	, rs.getString("CIDADE_REMETENTE"));
			stmt.setString  (	35	, rs.getString("IBGE_REMETENTE"));
			stmt.setString  (	36	, rs.getString("DESC_ESTADO_REMETENTE"));
			stmt.setString  (	37	, rs.getString("COD_ESTADO_REMETENTE"));
			stmt.setString  (	38	, rs.getString("CNPJ_REMETENTE"));
			stmt.setString  (	39	, rs.getString("INSC_MUNICIPAL_REMETENTE"));
			stmt.setString  (	40	, rs.getString("INSC_ESTADUAL_REMETENTE"));
			stmt.setString  (	41	, rs.getString("RAZAO_SOCIAL_DESTINATARIO"));
			stmt.setString  (	42	, rs.getString("ENDERECO_DESTINATARIO"));
			stmt.setString  (	43	, rs.getString("CEP_DESTINATARIO"));
			stmt.setString  (	44	, rs.getString("BAIRRO_DESTINATARIO"));
			stmt.setString  (	45	, rs.getString("CIDADE_DESTINATARIO"));
			stmt.setString  (	46	, rs.getString("IBGE_DESTINATARIO"));
			stmt.setString  (	47	, rs.getString("DESC_ESTADO_DESTINATARIO"));
			stmt.setString  (	48	, rs.getString("COD_ESTADO_DESTINATARIO"));
			stmt.setString  (	49	, rs.getString("CNPJ_DESTINATARIO"));
			stmt.setString  (	50	, rs.getString("INSC_MUNICIPAL_DESTINATARIO"));
			stmt.setString  (	51	, rs.getString("INSC_ESTADUAL_DESTINATARIO"));

			/*
			 * Paulo Diniz
			 * Projeto SAP carga SD
			 * Criado m[etodo à parte para obter dados do Expedidor
			 * 30-11-2016 
			 * 
			 */
			obterDadosExpedidorRecebedor(conn, orgId, dadosExpedidorRecebedor,
					cod_terminal_carga, cod_estacao_origem, tipoCliente, false);

			stmt.setString		(52,		dadosExpedidorRecebedor.getRazaoSocialExpedidorRecebedor());
			stmt.setString		(53,		dadosExpedidorRecebedor.getEnderecoExpedidorRecebedor());
			stmt.setString		(54,		dadosExpedidorRecebedor.getCepExpedidorRecebedor());
			stmt.setString		(55,		dadosExpedidorRecebedor.getBairroExpedidorRecebedor());
			stmt.setString		(56,		dadosExpedidorRecebedor.getCidadeExpedidorRecebedor());
			stmt.setString		(57,		dadosExpedidorRecebedor.getIbgeCodMunicipioExpedidorRecebedor());
			stmt.setString		(58,		dadosExpedidorRecebedor.getIbgeDescEstadoExpedidorRecebedor());
			stmt.setString		(59,		dadosExpedidorRecebedor.getEstadoExpedidorRecebedor());
			stmt.setString		(60,		dadosExpedidorRecebedor.getCnpjExpedidorRecebedor());
			stmt.setString		(61,		dadosExpedidorRecebedor.getInscrMunicipalExpedidorRecebedor());
			stmt.setString		(62,		dadosExpedidorRecebedor.getInscrEstadualExpedidorRecebedor());

			/*
			 * Paulo Diniz
			 * Projeto SAP carga SD
			 * Criado m[etodo à parte para obter dados do Recebedor
			 * 30-11-2016 
			 * 
			 */
			obterDadosExpedidorRecebedor(conn, orgId, dadosExpedidorRecebedor,
					cod_terminal_descarga, cod_estacao_destino, tipoCliente, true);

			stmt.setString		(63,		dadosExpedidorRecebedor.getRazaoSocialExpedidorRecebedor());
			stmt.setString		(64,		dadosExpedidorRecebedor.getEnderecoExpedidorRecebedor());
			stmt.setString		(65,		dadosExpedidorRecebedor.getCepExpedidorRecebedor());
			stmt.setString		(66,		dadosExpedidorRecebedor.getBairroExpedidorRecebedor());
			stmt.setString		(67,		dadosExpedidorRecebedor.getCidadeExpedidorRecebedor());
			stmt.setString		(68,		dadosExpedidorRecebedor.getIbgeCodMunicipioExpedidorRecebedor());
			stmt.setString		(69,		dadosExpedidorRecebedor.getIbgeDescEstadoExpedidorRecebedor());
			stmt.setString		(70,		dadosExpedidorRecebedor.getEstadoExpedidorRecebedor());
			stmt.setString		(71,		dadosExpedidorRecebedor.getCnpjExpedidorRecebedor());
			stmt.setString		(72,		dadosExpedidorRecebedor.getInscrMunicipalExpedidorRecebedor());
			stmt.setString		(73,		dadosExpedidorRecebedor.getInscrEstadualExpedidorRecebedor());


			stmt.setString  (	74	, rs.getString("RAZAO_SOCIAL_TOMADOR"));
			stmt.setString  (	75	, rs.getString("ENDERECO_TOMADOR"));
			stmt.setString  (	76	, rs.getString("CEP_TOMADOR"));
			stmt.setString  (	77	, rs.getString("BAIRRO_TOMADOR"));
			stmt.setString  (	78	, rs.getString("CIDADE_TOMADOR"));
			stmt.setString  (	79	, rs.getString("IBGE_TOMADOR"));
			stmt.setString  (	80	, rs.getString("DESC_ESTADO_TOMADOR"));
			stmt.setString  (	81	, rs.getString("COD_ESTADO_TOMADOR"));
			stmt.setString  (	82	, rs.getString("CNPJ_TOMADOR"));
			stmt.setString  (	83	, rs.getString("INSC_MUNICIPAL_TOMADOR"));
			stmt.setString  (	84	, rs.getString("INSC_ESTADUAL_TOMADOR"));
			
			/*
			 * Paulo Diniz
			 * Projeto SAP carga SD
			 * Criado metodo à parte para obter dados Documento Anterior
			 * 06-12-2016 
			 * 
			 */
			obterDadosDocumentoAnterior(conn, serie, numCarregamento, versaoCarregamento, tipoDocFiscal, dadosDocumentoAnterior);

			stmt.setString  (	85	, dadosDocumentoAnterior.getChaveDocAnterior());
			stmt.setString  (	86	, dadosDocumentoAnterior.getSerieDocAnterior());
			stmt.setString  (	87	, dadosDocumentoAnterior.getNumCarregamentoDocAnterior());
			stmt.setString  (	88	, dadosDocumentoAnterior.getTipoDocAnterior());
			stmt.setString  (	89	, dadosDocumentoAnterior.getDataEmissaoDocAnterior());
			stmt.setString  (	90	, rs.getString("RAZAO_SOCIAL_EMISSOR"));
			stmt.setString  (	91	, rs.getString("ENDERECO_EMISSOR"));
			stmt.setString  (	92	, rs.getString("CEP_EMISSOR"));
			stmt.setString  (	93	, rs.getString("BAIRRO_EMISSOR"));
			stmt.setString  (	94	, rs.getString("CIDADE_EMISSOR"));
			stmt.setString  (	95	, rs.getString("IBGE_EMISSOR"));
			stmt.setString  (	96	, rs.getString("DESC_ESTADO_EMISSOR"));
			stmt.setString  (	97	, rs.getString("COD_ESTADO_EMISSOR"));
			stmt.setString  (	98	, rs.getString("CNPJ_EMISSOR"));
			stmt.setString  (	99	, rs.getString("INSC_MUNICIPAL_EMISSOR"));
			stmt.setString  (	100	, rs.getString("INSC_ESTADUAL_EMISSOR"));


			if(isComplementar)
			{
				if(!isVOF)
				{
					stmt.setString  (	101	, rs.getString("SERIE_CARREGAMENTO"));
					stmt.setInt    (	102	, rs.getInt   ("NUMERO_CARREGAMENTO"));
					stmt.setDate  (	103	, rs.getDate("DATA_EMISSAO"));
				}
				else
				{
					stmt.setString  (	101	, rs.getString("SERIE_CARREGAMENTO_ASSOC"));
					stmt.setInt  (	102	, rs.getInt   ("NUMERO_CARREGAMENTO_ASSOC"));
					stmt.setDate  (	103	, rs.getDate("DATA_EMISSAO"));
				}


			}
			else
			{
				stmt.setString  (	101	, rs.getString("CTE_SERIE"));
				stmt.setString  (	102	, rs.getString("CTE_NUMERO"));
				stmt.setDate  (	103	, rs.getDate("CTE_EMISSAO"));

			}
			stmt.setString  (	104	, dataExecuçãoResult);
			stmt.setString  (	105	, "N");


			stmt.execute();

		} catch (SQLException e) {

			//System.out.println(" Serie : "+  serie + " Numero Carregamento " + numCarregamento + " VersaoCarregamento" + versaoCarregamento + " DataEmissao " + dataEmissao);
			e.printStackTrace();
			throw e;
		}
		finally
		{
			stmt.close();
		}

	}

	private void obterDadosDocumentoAnterior(Connection conn, String serie,
			String numCarregamento,
			String versaoCarregamento, String tipoDoc, DadosDocumentoAnterior dadosDocumentoAnterior) throws SQLException {

		String query = 
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
		
		OraclePreparedStatement stmt = null;
		OracleResultSet rs = null;
		try{

			stmt = (OraclePreparedStatement) conn.prepareStatement(query.toString());
			stmt.setString(1, versaoCarregamento);
			stmt.setString(2, serie);
			stmt.setString(3, numCarregamento);

			rs = (OracleResultSet) stmt.executeQuery();

			if ( rs.next() ) 
			{	
				dadosDocumentoAnterior.setChaveDocAnterior(rs.getString("CHAVE_CTE_ANTERIOR"));
				dadosDocumentoAnterior.setSerieDocAnterior(rs.getString("SERIE_ANTERIOR"));
				dadosDocumentoAnterior.setNumCarregamentoDocAnterior(rs.getString("DOCUMENTO_ANTERIOR"));
				dadosDocumentoAnterior.setTipoDocAnterior(tipoDoc);
				dadosDocumentoAnterior.setDataEmissaoDocAnterior(rs.getString("DATA_EMISSAO_DOC_ANTERIOR"));

			}


		}
		catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		finally
		{	
			rs.close();
			stmt.close();
		}
	}
		private void obterDadosExpedidorRecebedor(Connection conn, String orgId,
				DadosExpedidorRecebedor dadosExpedidorRecebedor,
				String codTerminalCargaDescarga, String codEstacaoCargaDescarga, String tipoCliente, boolean isDestino)
						throws SQLException {


			/*
			 * Verificaçao se for cliente exterior, não carregará dados do recebedor
			 */
			if(isDestino && tipoCliente != null && tipoCliente.toString().equals("3"))
			{
				return;
			}
			else
			{
				if(codTerminalCargaDescarga != null)
				{
					obterDadosExpedidorRecebedorTerminal(conn, dadosExpedidorRecebedor, codTerminalCargaDescarga, orgId); // IBGE REMETENTE

					if(dadosExpedidorRecebedor.getCnpjExpedidorRecebedor() == null)
					{
						obterDadosExpedidorRecebedorEstacao(conn, dadosExpedidorRecebedor, codEstacaoCargaDescarga, orgId);
					}
				}
				else
				{
					obterDadosExpedidorRecebedorEstacao(conn, dadosExpedidorRecebedor, codEstacaoCargaDescarga, orgId);
				}

			}
		}

		private void inserirVagoes(Connection conn, Integer idCarregamento, String serie, int numero, String versao, String unidadeMedida, boolean isGenerico) throws SQLException {


			String queryVagao	=
					"	   SELECT carregamento.c6hserie AS serie_carregamento          "+
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
							"	  AND carregamento.c6hnumat = ?                                ";

			OraclePreparedStatement stmt = null;
			OracleResultSet rs = null;
			try{

				stmt = (OraclePreparedStatement) conn.prepareStatement(queryVagao.toString());

				stmt.setString	(1, serie);
				stmt.setInt		(2, numero);
				stmt.setString	(3, versao);

				rs = (OracleResultSet) stmt.executeQuery();

				while (rs.next()){

					inserirVagao(conn,  idCarregamento, rs, unidadeMedida, isGenerico);

				} 
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
			finally
			{	
				rs.close();
				stmt.close();
			}

		}

		private void inserirVagao(Connection conn, Integer idCarregamento, OracleResultSet rs, String unidadeMedida, boolean isGenerico)throws SQLException{

			String insert_vagao = 
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
							"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


			OraclePreparedStatement stmt = null;
			try {

				stmt = (OraclePreparedStatement) conn.prepareStatement(insert_vagao.toString());

				Integer idVagao = getSequence(conn, sequenceVagao);

				stmt.setInt		(1, 	idCarregamento);
				stmt.setString	(2, 	rs.getString("SERIE_VAGAO"));
				stmt.setString	(3, 	rs.getString("CODIGO_VAGAO"));
				stmt.setString	(4, 	unidadeMedida);
				stmt.setInt		(5, 	rs.getInt("PESO_REAL"));
				if(unidadeMedida.equals("TO") || unidadeMedida.equals("UN") ||unidadeMedida.equals("CN"))
					stmt.setDouble		(6, 	rs.getInt("PESO_CALCULADO"));
				else
					stmt.setNull		(6, 	0);
				stmt.setString	(7, 	rs.getString("NUM_ROMANEIO"));
				stmt.setString	(8, 	rs.getString("NUM_PEDIDO"));
				stmt.setString	(9, 	rs.getString("MODELO_NOTA"));
				stmt.setString	(10, 	rs.getString("SERIE_NOTA"));
				stmt.setString	(11, 	rs.getString("NUMERO_NOTA"));
				stmt.setDate	(12, 	rs.getDate("DATA_EMISSAO"));
				stmt.setDouble		(13, 	rs.getDouble("VALOR_BASE_ICMS"));
				stmt.setDouble		(14, 	rs.getDouble("VALOR_TOTAL_ICMS"));
				stmt.setDouble		(15, 	rs.getDouble("VALOR_BASE_IPI"));
				stmt.setDouble		(16, 	rs.getDouble("VALOR_TOTAL_IPI"));
				stmt.setDouble		(17, 	rs.getDouble("VALOR_TOTAL_PRODUTOS"));
				stmt.setDouble		(18, 	rs.getDouble("VALOR_NF"));
				stmt.setString	(19, 	rs.getString("CFOP_PREDOMINANTE"));
				stmt.setInt		(20, 	rs.getInt("PESO_RATEADO"));
				stmt.setString	(21, 	rs.getString("PIN_SUFRAMA"));
				stmt.setDate	(22, 	rs.getDate("DT_PREV_ENTREGA"));
				stmt.setInt		(23, 	idVagao);
				
				if(unidadeMedida.equals("M3"))
					stmt.setDouble		(24, 	rs.getDouble("PESO_CALCULADO"));
				else
					stmt.setNull		(24, 	0);
				
				stmt.setString	(25, 	rs.getString("TIPO_NOTA_FISCAL_MERCADORIA"));
				stmt.setString	(26, 	rs.getString("CHAVE_MERCADORIA"));
				stmt.setString	(27, 	isGenerico ? "PR" : "PM");
				
				//TODO colocar peso minimo
				stmt.setNull	(28, 	0);

				stmt.execute();

			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
			finally
			{
				stmt.close();
			}
		}

		private void inserirServico(Connection conn, Integer idCarregamento, OracleResultSet rs, String unidadeMedida)throws SQLException{


			String insert_servico = "INSERT INTO " + 
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
					"ALIQUOTA_COFINS, " +
					"VALOR_BASE_COFINS, " +
					"VALOR_COFINS, " +
					"ALIQUOTA_ISS, " +
					"VALOR_BASE_ISS, " +
					"VALOR_ISS," +
					"FERROVIA_SERVICO)"+
					" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			OraclePreparedStatement stmt = null;

			try {

				stmt = (OraclePreparedStatement) conn.prepareStatement(insert_servico.toString());


				stmt.setInt(1, idCarregamento);
				stmt.setString(2, rs.getString("COD_SERVICO"));
				stmt.setString(3,  rs.getString("DESCRICAO"));
				stmt.setString(4, unidadeMedida);
				stmt.setDouble(5, rs.getDouble("PESO_CALCULO"));
				stmt.setDouble(6, rs.getDouble("TARIFA_UNIT_S_IMPOSTO"));
				stmt.setDouble(7, rs.getDouble("VALOR_SERVICO"));
				stmt.setDouble(8, rs.getDouble("ALIQUOTA_ICMS"));
				stmt.setDouble(9, rs.getDouble("VALOR_BASE_ICMS"));
				stmt.setDouble(10, rs.getDouble("VALOR_ICMS"));
				stmt.setDouble(11, rs.getDouble("ALIQUOTA_PIS"));
				stmt.setDouble(12, rs.getDouble("VALOR_BASE_PIS"));
				stmt.setDouble(13, rs.getDouble("ALIQUOTA_COFINS"));
				stmt.setDouble(14, rs.getDouble("VALOR_BASE_COFINS"));
				stmt.setDouble(15, rs.getDouble("VALOR_COFINS"));
				stmt.setDouble(16, rs.getDouble("ALIQUOTA_ISS"));
				stmt.setDouble(17, rs.getDouble("VALOR_BASE_ISS"));
				stmt.setDouble(18, rs.getDouble("VALOR_ISS"));
				stmt.setString(19, rs.getString("FERROVIA_SERVICO"));

				stmt.execute();

			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
			finally
			{
				stmt.close();
			}
		}

		private void inserirServicos(Connection conn, Integer idCarregamento, String serie, int numero, String versao, String unidadeMedida) throws SQLException {


			String consultar_servico = " select carregamento.c6hserie as SERIE,								 "+
					"		   carregamento.c6hnumde as CARREGAMENTO,                                        "+
					"	       carregamento.c6hnumat as VERSAO,                                              "+
					"	       decode(servico_carregamento.c6bcdfde,'03','EFVM','04','EFC','06','FCA','12','FNS','07','MRS','ND') as FERROVIA_SERVICO,                                                                             "+
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
					"	   and carregamento.c6hnumat = :versao                                               ";


			OraclePreparedStatement stmt = null;
			OracleResultSet rs = null;
			try {

				stmt = (OraclePreparedStatement) conn.prepareStatement(consultar_servico.toString());
				stmt.setStringAtName("serie", serie);
				stmt.setIntAtName("carregamento", numero);
				stmt.setStringAtName("versao", versao);

				rs = (OracleResultSet) stmt.executeQuery();

				while (rs.next()) {

					inserirServico(conn,  idCarregamento, rs, unidadeMedida);


				}

			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
			finally
			{
				rs.close();
				stmt.close();
			}

		}

		private void obterDadosExpedidorRecebedorEstacao(Connection conn, DadosExpedidorRecebedor dadosExpedidorRecebedor,
				String codEstacaoCargaDescarga, String orgId) throws SQLException 
				{

			String cnpjClien = "";
			String cnpjEstab = "";
			String cnpjDigit = "";

			String descrLocalizacao = "";
			String unidLocalizacao = "";


			//		if(idLocalidade != null)
			//		{
			StringBuilder query_2 = new StringBuilder();

			query_2.append("    select complementoLocalFerroviario.CUDNUSEQ, complementoLocalFerroviario.CUDCDLOC, complementoLocalFerroviario.CUDEMAIL, complementoLocalFerroviario.CUDINDTO from CUDCOLFT complementoLocalFerroviario ");
			query_2.append("    where complementoLocalFerroviario.CUDNUSEQ = :codEstacaoCargaDescarga ");

			OraclePreparedStatement stmt = null;
			OracleResultSet rs_2 = null;
			try{

				stmt = (OraclePreparedStatement) conn.prepareStatement(query_2.toString());
				stmt.setStringAtName("codEstacaoCargaDescarga", codEstacaoCargaDescarga);

				rs_2 = (OracleResultSet) stmt.executeQuery();

				if ( rs_2.next() ) 
				{

					StringBuilder query_est = new StringBuilder();

					query_est.append(" SELECT R3ALOCAT.R3ADESCR, R3ALOCAT.R3AUNIDF ");
					query_est.append("  FROM  R3ALOCAT R3ALOCAT ");
					query_est.append(" WHERE  R3ALOCAT.R3ACDLOC = :codEstacaoCargaDescarga ");

					OraclePreparedStatement stmt_01 = null;
					OracleResultSet rs_01 = null;
					try{

						stmt_01 = (OraclePreparedStatement) conn.prepareStatement(query_est.toString());
						stmt_01.setStringAtName("codEstacaoCargaDescarga", codEstacaoCargaDescarga);

						rs_01 = (OracleResultSet) stmt_01.executeQuery();

						if ( rs_01.next() ) 
						{
							if( rs_01.getObject( 1 ) != null ) 
							{
								descrLocalizacao = 	rs_01.getString( 1 );
								unidLocalizacao = 	rs_01.getString( 2 );

								dadosExpedidorRecebedor.setIbgeDescEstadoExpedidorRecebedor(descrLocalizacao);
								dadosExpedidorRecebedor.setIbgeCodMunicipioExpedidorRecebedor(unidLocalizacao);

							}
						}
					}
					catch (SQLException e) {
						e.printStackTrace();
						throw e;
					}

					finally
					{
						stmt_01.close();
						rs_01.close();
					}
					StringBuilder query_ao = new StringBuilder();

					query_ao.append(" SELECT NVL(AREA_OPERACIONAL_UNICOM.EP_CGC_EMP,AREA_OPERACIONAL_UNICOM.EP_CPF_EMP) ");
					query_ao.append("  FROM AREA_OPERACIONAL_UNICOM AREA_OPERACIONAL_UNICOM, ");
					query_ao.append("       RJSUCLGV                RJSUCLGV, ");
					query_ao.append("       RIEUCLGV                RIEUCLGV ");
					query_ao.append(" WHERE AREA_OPERACIONAL_UNICOM.AO_ID_AO = RJSUCLGV.RJSNSQAO ");
					query_ao.append("   AND RIEUCLGV.RIENUSEQ = RJSUCLGV.RJSNUSEQ ");
					query_ao.append("   AND RIEUCLGV.RIENUSEQ = :codEstacaoCargaDescarga ");


					OraclePreparedStatement stmt_2 = null;
					OracleResultSet rs = null;

					try {

						stmt_2 = (OraclePreparedStatement) conn.prepareStatement(query_ao.toString());
						stmt_2.setStringAtName("codEstacaoCargaDescarga", codEstacaoCargaDescarga);

						rs = (OracleResultSet) stmt_2.executeQuery();

						if ( rs.next() ) 
						{
							if( rs.getObject( 1 ) != null ) 
							{
								cnpjClien = rs.getString( 1 ).substring(0,8);
								cnpjEstab = rs.getString( 1 ).substring(8,12); 
								cnpjDigit = rs.getString( 1 ).substring(12,14); 
							}
						}
					}


					catch (SQLException e) {
						e.printStackTrace();
						throw e;
					}
					finally
					{
						rs.close();
						stmt_2.close();
					}

					if(cnpjClien != null && cnpjEstab != null && cnpjDigit != null)
					{
						StringBuilder query_3 = new StringBuilder();

						query_3.append("    select CLI_RAZSOC RAZAO_SOCIAL_EXPEDIDOR,CLI_ENDERE ENDERECO_EXPEDIDOR,CLI_NUMCEP CEP_EXPEDIDOR,CLI_BAIRRO BAIRRO_EXPEDIDOR,CLI_CIDADE CIDADE_EXPEDIDOR,CLI_ESTADO COD_ESTADO_EXPEDIDOR, CLI_NUMERO || CLI_ESTABE || CLI_DIGITO CNPJ_EXPEDIDOR, CLI_INSEST INSC_ESTADUAL_EXPEDIDOR from hz_locations_unicom ");
						query_3.append("    where hz_locations_unicom.cli_numero = :cnpj ");
						query_3.append("    and   hz_locations_unicom.cli_estabe = :estab ");
						query_3.append("    and   hz_locations_unicom.cli_digito = :digit ");
						query_3.append("    and   hz_locations_unicom.cli_orgid  = :orgId ");

						OraclePreparedStatement stmt_3 = null;
						OracleResultSet rs_3 = null;
						try {

							stmt_3 = (OraclePreparedStatement) conn.prepareStatement(query_3.toString());
							stmt_3.setStringAtName("cnpj", cnpjClien);
							stmt_3.setStringAtName("estab", cnpjEstab);
							stmt_3.setStringAtName("digit", cnpjDigit);
							stmt_3.setStringAtName("orgId", orgId);

							rs_3 = (OracleResultSet) stmt_3.executeQuery();

							if ( rs_3.next() ) 
							{
								if( rs_3.getObject( 1 ) != null ) 
								{
									dadosExpedidorRecebedor.setRazaoSocialExpedidorRecebedor(rs_3.getString("RAZAO_SOCIAL_EXPEDIDOR"));
									dadosExpedidorRecebedor.setEnderecoExpedidorRecebedor(rs_3.getString("ENDERECO_EXPEDIDOR"));
									dadosExpedidorRecebedor.setCepExpedidorRecebedor(rs_3.getString("CEP_EXPEDIDOR"));
									dadosExpedidorRecebedor.setBairroExpedidorRecebedor(rs_3.getString("BAIRRO_EXPEDIDOR"));
									dadosExpedidorRecebedor.setCidadeExpedidorRecebedor(rs_3.getString("CIDADE_EXPEDIDOR"));
									dadosExpedidorRecebedor.setEstadoExpedidorRecebedor(rs_3.getString("COD_ESTADO_EXPEDIDOR"));
									dadosExpedidorRecebedor.setCnpjExpedidorRecebedor(rs_3.getString("CNPJ_EXPEDIDOR"));
									dadosExpedidorRecebedor.setInscrEstadualExpedidorRecebedor(rs_3.getString("INSC_ESTADUAL_EXPEDIDOR"));
								}
							}
						}
						catch (SQLException e) {
							e.printStackTrace();
							throw e;
						}
						finally
						{
							rs_3.close();
							stmt_3.close();
						}

					}

				}
			}
			catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
			finally
			{
				rs_2.close();
				stmt.close();
			}


			//		}


				}
		private void obterDadosExpedidorRecebedorTerminal(Connection conn, DadosExpedidorRecebedor dadosExpedidorRecebedor,
				String cod_terminal_carga_descarga, String orgId) throws SQLException {


			String cnpjClien = "";
			String cnpjEstab = "";
			String cnpjDigit = "";

			StringBuilder query_ao = new StringBuilder();

			query_ao.append(" SELECT NVL(AREA_OPERACIONAL_UNICOM.EP_CGC_EMP,AREA_OPERACIONAL_UNICOM.EP_CPF_EMP) ");
			query_ao.append("  FROM AREA_OPERACIONAL_UNICOM AREA_OPERACIONAL_UNICOM, ");
			query_ao.append("       RJSUCLGV                RJSUCLGV, ");
			query_ao.append("       RIEUCLGV                RIEUCLGV ");
			query_ao.append(" WHERE AREA_OPERACIONAL_UNICOM.AO_ID_AO = RJSUCLGV.RJSNSQAO ");
			query_ao.append("   AND RIEUCLGV.RIENUSEQ = RJSUCLGV.RJSNUSEQ ");
			query_ao.append("   AND RIEUCLGV.RIENUSEQ = :cod_terminal_carga_descarga ");


			OraclePreparedStatement stmt = null;
			OracleResultSet rs = null;
			try {

				stmt = (OraclePreparedStatement) conn.prepareStatement(query_ao.toString());

				stmt.setStringAtName("cod_terminal_carga_descarga", cod_terminal_carga_descarga);

				rs = (OracleResultSet) stmt.executeQuery();

				if ( rs.next() ) 
				{
					if( rs.getObject( 1 ) != null ) 
					{
						cnpjClien = rs.getString( 1 ).substring(0,8);
						cnpjEstab = rs.getString( 1 ).substring(8,12); 
						cnpjDigit = rs.getString( 1 ).substring(12,14); 
					}
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
			finally
			{
				rs.close();
				stmt.close();
			}

			if(cnpjClien != null && cnpjEstab != null && cnpjDigit != null)
			{	

				StringBuilder query_est = new StringBuilder();

				query_est.append(" SELECT R3ALOCAT.R3ADESCR, R3ALOCAT.R3AUNIDF ");
				query_est.append("  FROM  R3ALOCAT R3ALOCAT ");
				query_est.append(" WHERE  R3ALOCAT.R3ACDLOC = :cod_terminal_carga_descarga ");

				OraclePreparedStatement stmt_01 = null;
				OracleResultSet rs_01 = null;

				try {

					stmt_01 = (OraclePreparedStatement) conn.prepareStatement(query_est.toString());
					stmt_01.setStringAtName("cod_terminal_carga_descarga", cod_terminal_carga_descarga);

					rs_01 = (OracleResultSet) stmt_01.executeQuery();

					if ( rs_01.next() ) 
					{
						if( rs_01.getObject( 1 ) != null ) 
						{
							dadosExpedidorRecebedor.setIbgeDescEstadoExpedidorRecebedor(rs_01.getString( 1 ));
							dadosExpedidorRecebedor.setIbgeCodMunicipioExpedidorRecebedor(rs_01.getString( 2 ));

						}
					}
				}
				catch (SQLException e) {
					e.printStackTrace();
					throw e;
				}

				finally
				{
					rs_01.close();
					stmt_01.close();
				}


				StringBuilder query_2 = new StringBuilder();

				query_2.append("    select CLI_RAZSOC RAZAO_SOCIAL_EXPEDIDOR,CLI_ENDERE ENDERECO_EXPEDIDOR,CLI_NUMCEP CEP_EXPEDIDOR,CLI_BAIRRO BAIRRO_EXPEDIDOR,CLI_CIDADE CIDADE_EXPEDIDOR,CLI_ESTADO COD_ESTADO_EXPEDIDOR, CLI_NUMERO || CLI_ESTABE || CLI_DIGITO CNPJ_EXPEDIDOR, CLI_INSEST INSC_ESTADUAL_EXPEDIDOR from hz_locations_unicom ");
				query_2.append("    where hz_locations_unicom.cli_numero = :cnpj ");
				query_2.append("    and   hz_locations_unicom.cli_estabe = :estab ");
				query_2.append("    and   hz_locations_unicom.cli_digito = :digit ");
				query_2.append("    and   hz_locations_unicom.cli_orgid  = :orgId ");

				OracleResultSet rs_2 = null;
				try {

					stmt = (OraclePreparedStatement) conn.prepareStatement(query_2.toString());
					stmt.setStringAtName("cnpj", cnpjClien);
					stmt.setStringAtName("estab", cnpjEstab);
					stmt.setStringAtName("digit", cnpjDigit);
					stmt.setStringAtName("orgId", orgId);

					rs_2 = (OracleResultSet) stmt.executeQuery();

					if ( rs_2.next() ) 
					{
						if( rs_2.getObject( 1 ) != null ) 
						{
							dadosExpedidorRecebedor.setRazaoSocialExpedidorRecebedor(rs_2.getString("RAZAO_SOCIAL_EXPEDIDOR"));
							dadosExpedidorRecebedor.setEnderecoExpedidorRecebedor(rs_2.getString("ENDERECO_EXPEDIDOR"));
							dadosExpedidorRecebedor.setCepExpedidorRecebedor(rs_2.getString("CEP_EXPEDIDOR"));
							dadosExpedidorRecebedor.setBairroExpedidorRecebedor(rs_2.getString("BAIRRO_EXPEDIDOR"));
							dadosExpedidorRecebedor.setCidadeExpedidorRecebedor(rs_2.getString("CIDADE_EXPEDIDOR"));
							dadosExpedidorRecebedor.setEstadoExpedidorRecebedor(rs_2.getString("COD_ESTADO_EXPEDIDOR"));
							dadosExpedidorRecebedor.setCnpjExpedidorRecebedor(rs_2.getString("CNPJ_EXPEDIDOR"));
							dadosExpedidorRecebedor.setInscrEstadualExpedidorRecebedor(rs_2.getString("INSC_ESTADUAL_EXPEDIDOR"));
						}
					}
				}
				catch (SQLException e) {
					e.printStackTrace();
					throw e;
				}
				finally
				{
					rs_2.close();
					stmt.close();
				}

			}

		}

	}
