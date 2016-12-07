import java.util.Date;


public class CteCarregamento {
	
	
	private int    idCarregamento			 ;	
	private String codigo_ferrovia           ;
	private String serie                     ;
	private int carregamento              	 ;
	private String versao                    ;
	private Date data_emissao              	 ;
	private String tipo_carregamento         ;
	private String situacao_carregamento     ;
	private String codigo_fluxo              ;
	private String id_origem                 ;
	private String id_destino                ;
	private String codigo_origem             ;
	private String codigo_destino            ;
	private String cod_terminal_carga        ;
	private String cod_terminal_descarga     ;
	private String codigo_mercadoria         ;
	private String razao_social_emitente     ;
	private String endereco_emitente         ;
	private String cep_emitente              ;
	private String bairro_emitente           ;
	private String cidade_emitente           ;
	private String ibge_emitente             ;
	private String desc_estado_emitente      ;
	private String cod_estado_emitente       ;
	private String cnpj_emitente             ;
	private String insc_estadual_emitente    ;
	private String serie_fatura              ;
	private String numero_fatura             ;
	private Date data_fatura               ;
	private String tipo_fatura               ;
	private String status_fatura             ;
	private String chave_cte                 ;
	private String tipo_cliente_remetente    ;
	private String razao_social_remetente    ;
	private String endereco_remetente        ;
	private String cep_remetente             ;
	private String bairro_remetente          ;
	private String cidade_remetente          ;
	private String ibge_remetente            ;
	private String desc_estado_remetente     ;
	private String cod_estado_remetente      ;
	private String cnpj_remetente            ;
	private String insc_estadual_remetente   ;
	private String razao_social_destinatario ;
	private String endereco_destinatario     ;
	private String cep_destinatario          ;
	private String bairro_destinatario       ;
	private String cidade_destinatario       ;
	private String ibge_destinatario         ;
	private String desc_estado_destinatario  ;
	private String cod_estado_destinatario   ;
	private String cnpj_destinatario         ;
	private String insc_estadual_destinatario;
	private String razao_social_expedidor    ;
	private String endereco_expedidor        ;
	private String cep_expedidor             ;
	private String bairro_expedidor          ;
	private String cidade_expedidor          ;
	private String ibge_expedidor            ;
	private String desc_estado_expedidor     ;
	private String cod_estado_expedidor      ;
	private String cnpj_expedidor            ;
	private String insc_estadual_expedidor   ;
	private String razao_social_recebedor    ;
	private String endereco_recebedor        ;
	private String cep_recebedor             ;
	private String bairro_recebedor          ;
	private String cidade_recebedor          ;
	private String ibge_recebedor            ;
	private String desc_estado_recebedor     ;
	private String cod_estado_recebedor      ;
	private String cnpj_recebedor            ;
	private String insc_estadual_recebedor   ;
	private String razao_social_tomador      ;
	private String endereco_tomador          ;
	private String cep_tomador               ;
	private String bairro_tomador            ;
	private String cidade_tomador            ;
	private String ibge_tomador              ;
	private String desc_estado_tomador       ;
	private String cod_estado_tomador        ;
	private String cnpj_tomador              ;
	private String insc_estadual_tomador     ;
	private String chave_cte_anterior        ;
	private String razao_social_emissor      ;
	private String endereco_emissor          ;
	private String cep_emissor               ;
	private String bairro_emissor            ;
	private String cidade_emissor            ;
	private String ibge_emissor              ;
	private String desc_estado_emissor       ;
	private String cod_estado_emissor        ;
	private String cnpj_emissor              ;
	private String insc_estadual_emissor     ;
	private String cte_serie                 ;
	private String cte_numero                ;
	private String cte_emissao               ;
	private String org_id                    ;
	private String data_execucao_result		 ;
	private String flag_lido				 ;
	
	
	
	public int getIdCarregamento() {
		return idCarregamento;
	}
	public void setIdCarregamento(int idCarregamento) {
		this.idCarregamento = idCarregamento;
	}
	public String getCodigo_ferrovia() {
		return codigo_ferrovia;
	}
	public void setCodigo_ferrovia(String codigo_ferrovia) {
		this.codigo_ferrovia = codigo_ferrovia;
	}
	public String getSerie() {
		return serie;
	}
	public void setSerie(String serie) {
		this.serie = serie;
	}
	public int getCarregamento() {
		return carregamento;
	}
	public void setCarregamento(int carregamento) {
		this.carregamento = carregamento;
	}
	public String getVersao() {
		return versao;
	}
	public void setVersao(String versao) {
		this.versao = versao;
	}
	public Date getData_emissao() {
		return data_emissao;
	}
	public void setData_emissao(Date data_emissao) {
		this.data_emissao = data_emissao;
	}
	public String getTipo_carregamento() {
		return tipo_carregamento;
	}
	public void setTipo_carregamento(String tipo_carregamento) {
		this.tipo_carregamento = tipo_carregamento;
	}
	public String getSituacao_carregamento() {
		return situacao_carregamento;
	}
	public void setSituacao_carregamento(String situacao_carregamento) {
		this.situacao_carregamento = situacao_carregamento;
	}
	public String getCodigo_fluxo() {
		return codigo_fluxo;
	}
	public void setCodigo_fluxo(String codigo_fluxo) {
		this.codigo_fluxo = codigo_fluxo;
	}
	public String getId_origem() {
		return id_origem;
	}
	public void setId_origem(String id_origem) {
		this.id_origem = id_origem;
	}
	public String getId_destino() {
		return id_destino;
	}
	public void setId_destino(String id_destino) {
		this.id_destino = id_destino;
	}
	public String getCodigo_origem() {
		return codigo_origem;
	}
	public void setCodigo_origem(String codigo_origem) {
		this.codigo_origem = codigo_origem;
	}
	public String getCodigo_destino() {
		return codigo_destino;
	}
	public void setCodigo_destino(String codigo_destino) {
		this.codigo_destino = codigo_destino;
	}
	public String getCod_terminal_carga() {
		return cod_terminal_carga;
	}
	public void setCod_terminal_carga(String cod_terminal_carga) {
		this.cod_terminal_carga = cod_terminal_carga;
	}
	public String getCod_terminal_descarga() {
		return cod_terminal_descarga;
	}
	public void setCod_terminal_descarga(String cod_terminal_descarga) {
		this.cod_terminal_descarga = cod_terminal_descarga;
	}
	public String getCodigo_mercadoria() {
		return codigo_mercadoria;
	}
	public void setCodigo_mercadoria(String codigo_mercadoria) {
		this.codigo_mercadoria = codigo_mercadoria;
	}
	public String getRazao_social_emitente() {
		return razao_social_emitente;
	}
	public void setRazao_social_emitente(String razao_social_emitente) {
		this.razao_social_emitente = razao_social_emitente;
	}
	public String getEndereco_emitente() {
		return endereco_emitente;
	}
	public void setEndereco_emitente(String endereco_emitente) {
		this.endereco_emitente = endereco_emitente;
	}
	public String getCep_emitente() {
		return cep_emitente;
	}
	public void setCep_emitente(String cep_emitente) {
		this.cep_emitente = cep_emitente;
	}
	public String getBairro_emitente() {
		return bairro_emitente;
	}
	public void setBairro_emitente(String bairro_emitente) {
		this.bairro_emitente = bairro_emitente;
	}
	public String getCidade_emitente() {
		return cidade_emitente;
	}
	public void setCidade_emitente(String cidade_emitente) {
		this.cidade_emitente = cidade_emitente;
	}
	public String getIbge_emitente() {
		return ibge_emitente;
	}
	public void setIbge_emitente(String ibge_emitente) {
		this.ibge_emitente = ibge_emitente;
	}
	public String getDesc_estado_emitente() {
		return desc_estado_emitente;
	}
	public void setDesc_estado_emitente(String desc_estado_emitente) {
		this.desc_estado_emitente = desc_estado_emitente;
	}
	public String getCod_estado_emitente() {
		return cod_estado_emitente;
	}
	public void setCod_estado_emitente(String cod_estado_emitente) {
		this.cod_estado_emitente = cod_estado_emitente;
	}
	public String getCnpj_emitente() {
		return cnpj_emitente;
	}
	public void setCnpj_emitente(String cnpj_emitente) {
		this.cnpj_emitente = cnpj_emitente;
	}
	public String getInsc_estadual_emitente() {
		return insc_estadual_emitente;
	}
	public void setInsc_estadual_emitente(String insc_estadual_emitente) {
		this.insc_estadual_emitente = insc_estadual_emitente;
	}
	public String getSerie_fatura() {
		return serie_fatura;
	}
	public void setSerie_fatura(String serie_fatura) {
		this.serie_fatura = serie_fatura;
	}
	public String getNumero_fatura() {
		return numero_fatura;
	}
	public void setNumero_fatura(String numero_fatura) {
		this.numero_fatura = numero_fatura;
	}
	public Date getData_fatura() {
		return data_fatura;
	}
	public void setData_fatura(Date data_fatura) {
		this.data_fatura = data_fatura;
	}
	public String getTipo_fatura() {
		return tipo_fatura;
	}
	public void setTipo_fatura(String tipo_fatura) {
		this.tipo_fatura = tipo_fatura;
	}
	public String getStatus_fatura() {
		return status_fatura;
	}
	public void setStatus_fatura(String status_fatura) {
		this.status_fatura = status_fatura;
	}
	public String getChave_cte() {
		return chave_cte;
	}
	public void setChave_cte(String chave_cte) {
		this.chave_cte = chave_cte;
	}
	public String getTipo_cliente_remetente() {
		return tipo_cliente_remetente;
	}
	public void setTipo_cliente_remetente(String tipo_cliente_remetente) {
		this.tipo_cliente_remetente = tipo_cliente_remetente;
	}
	public String getRazao_social_remetente() {
		return razao_social_remetente;
	}
	public void setRazao_social_remetente(String razao_social_remetente) {
		this.razao_social_remetente = razao_social_remetente;
	}
	public String getEndereco_remetente() {
		return endereco_remetente;
	}
	public void setEndereco_remetente(String endereco_remetente) {
		this.endereco_remetente = endereco_remetente;
	}
	public String getCep_remetente() {
		return cep_remetente;
	}
	public void setCep_remetente(String cep_remetente) {
		this.cep_remetente = cep_remetente;
	}
	public String getBairro_remetente() {
		return bairro_remetente;
	}
	public void setBairro_remetente(String bairro_remetente) {
		this.bairro_remetente = bairro_remetente;
	}
	public String getCidade_remetente() {
		return cidade_remetente;
	}
	public void setCidade_remetente(String cidade_remetente) {
		this.cidade_remetente = cidade_remetente;
	}
	public String getIbge_remetente() {
		return ibge_remetente;
	}
	public void setIbge_remetente(String ibge_remetente) {
		this.ibge_remetente = ibge_remetente;
	}
	public String getDesc_estado_remetente() {
		return desc_estado_remetente;
	}
	public void setDesc_estado_remetente(String desc_estado_remetente) {
		this.desc_estado_remetente = desc_estado_remetente;
	}
	public String getCod_estado_remetente() {
		return cod_estado_remetente;
	}
	public void setCod_estado_remetente(String cod_estado_remetente) {
		this.cod_estado_remetente = cod_estado_remetente;
	}
	public String getCnpj_remetente() {
		return cnpj_remetente;
	}
	public void setCnpj_remetente(String cnpj_remetente) {
		this.cnpj_remetente = cnpj_remetente;
	}
	public String getInsc_estadual_remetente() {
		return insc_estadual_remetente;
	}
	public void setInsc_estadual_remetente(String insc_estadual_remetente) {
		this.insc_estadual_remetente = insc_estadual_remetente;
	}
	public String getRazao_social_destinatario() {
		return razao_social_destinatario;
	}
	public void setRazao_social_destinatario(String razao_social_destinatario) {
		this.razao_social_destinatario = razao_social_destinatario;
	}
	public String getEndereco_destinatario() {
		return endereco_destinatario;
	}
	public void setEndereco_destinatario(String endereco_destinatario) {
		this.endereco_destinatario = endereco_destinatario;
	}
	public String getCep_destinatario() {
		return cep_destinatario;
	}
	public void setCep_destinatario(String cep_destinatario) {
		this.cep_destinatario = cep_destinatario;
	}
	public String getBairro_destinatario() {
		return bairro_destinatario;
	}
	public void setBairro_destinatario(String bairro_destinatario) {
		this.bairro_destinatario = bairro_destinatario;
	}
	public String getCidade_destinatario() {
		return cidade_destinatario;
	}
	public void setCidade_destinatario(String cidade_destinatario) {
		this.cidade_destinatario = cidade_destinatario;
	}
	public String getIbge_destinatario() {
		return ibge_destinatario;
	}
	public void setIbge_destinatario(String ibge_destinatario) {
		this.ibge_destinatario = ibge_destinatario;
	}
	public String getDesc_estado_destinatario() {
		return desc_estado_destinatario;
	}
	public void setDesc_estado_destinatario(String desc_estado_destinatario) {
		this.desc_estado_destinatario = desc_estado_destinatario;
	}
	public String getCod_estado_destinatario() {
		return cod_estado_destinatario;
	}
	public void setCod_estado_destinatario(String cod_estado_destinatario) {
		this.cod_estado_destinatario = cod_estado_destinatario;
	}
	public String getCnpj_destinatario() {
		return cnpj_destinatario;
	}
	public void setCnpj_destinatario(String cnpj_destinatario) {
		this.cnpj_destinatario = cnpj_destinatario;
	}
	public String getInsc_estadual_destinatario() {
		return insc_estadual_destinatario;
	}
	public void setInsc_estadual_destinatario(String insc_estadual_destinatario) {
		this.insc_estadual_destinatario = insc_estadual_destinatario;
	}
	public String getRazao_social_expedidor() {
		return razao_social_expedidor;
	}
	public void setRazao_social_expedidor(String razao_social_expedidor) {
		this.razao_social_expedidor = razao_social_expedidor;
	}
	public String getEndereco_expedidor() {
		return endereco_expedidor;
	}
	public void setEndereco_expedidor(String endereco_expedidor) {
		this.endereco_expedidor = endereco_expedidor;
	}
	public String getCep_expedidor() {
		return cep_expedidor;
	}
	public void setCep_expedidor(String cep_expedidor) {
		this.cep_expedidor = cep_expedidor;
	}
	public String getBairro_expedidor() {
		return bairro_expedidor;
	}
	public void setBairro_expedidor(String bairro_expedidor) {
		this.bairro_expedidor = bairro_expedidor;
	}
	public String getCidade_expedidor() {
		return cidade_expedidor;
	}
	public void setCidade_expedidor(String cidade_expedidor) {
		this.cidade_expedidor = cidade_expedidor;
	}
	public String getIbge_expedidor() {
		return ibge_expedidor;
	}
	public void setIbge_expedidor(String ibge_expedidor) {
		this.ibge_expedidor = ibge_expedidor;
	}
	public String getDesc_estado_expedidor() {
		return desc_estado_expedidor;
	}
	public void setDesc_estado_expedidor(String desc_estado_expedidor) {
		this.desc_estado_expedidor = desc_estado_expedidor;
	}
	public String getCod_estado_expedidor() {
		return cod_estado_expedidor;
	}
	public void setCod_estado_expedidor(String cod_estado_expedidor) {
		this.cod_estado_expedidor = cod_estado_expedidor;
	}
	public String getCnpj_expedidor() {
		return cnpj_expedidor;
	}
	public void setCnpj_expedidor(String cnpj_expedidor) {
		this.cnpj_expedidor = cnpj_expedidor;
	}
	public String getInsc_estadual_expedidor() {
		return insc_estadual_expedidor;
	}
	public void setInsc_estadual_expedidor(String insc_estadual_expedidor) {
		this.insc_estadual_expedidor = insc_estadual_expedidor;
	}
	public String getRazao_social_recebedor() {
		return razao_social_recebedor;
	}
	public void setRazao_social_recebedor(String razao_social_recebedor) {
		this.razao_social_recebedor = razao_social_recebedor;
	}
	public String getEndereco_recebedor() {
		return endereco_recebedor;
	}
	public void setEndereco_recebedor(String endereco_recebedor) {
		this.endereco_recebedor = endereco_recebedor;
	}
	public String getCep_recebedor() {
		return cep_recebedor;
	}
	public void setCep_recebedor(String cep_recebedor) {
		this.cep_recebedor = cep_recebedor;
	}
	public String getBairro_recebedor() {
		return bairro_recebedor;
	}
	public void setBairro_recebedor(String bairro_recebedor) {
		this.bairro_recebedor = bairro_recebedor;
	}
	public String getCidade_recebedor() {
		return cidade_recebedor;
	}
	public void setCidade_recebedor(String cidade_recebedor) {
		this.cidade_recebedor = cidade_recebedor;
	}
	public String getIbge_recebedor() {
		return ibge_recebedor;
	}
	public void setIbge_recebedor(String ibge_recebedor) {
		this.ibge_recebedor = ibge_recebedor;
	}
	public String getDesc_estado_recebedor() {
		return desc_estado_recebedor;
	}
	public void setDesc_estado_recebedor(String desc_estado_recebedor) {
		this.desc_estado_recebedor = desc_estado_recebedor;
	}
	public String getCod_estado_recebedor() {
		return cod_estado_recebedor;
	}
	public void setCod_estado_recebedor(String cod_estado_recebedor) {
		this.cod_estado_recebedor = cod_estado_recebedor;
	}
	public String getCnpj_recebedor() {
		return cnpj_recebedor;
	}
	public void setCnpj_recebedor(String cnpj_recebedor) {
		this.cnpj_recebedor = cnpj_recebedor;
	}
	public String getInsc_estadual_recebedor() {
		return insc_estadual_recebedor;
	}
	public void setInsc_estadual_recebedor(String insc_estadual_recebedor) {
		this.insc_estadual_recebedor = insc_estadual_recebedor;
	}
	public String getRazao_social_tomador() {
		return razao_social_tomador;
	}
	public void setRazao_social_tomador(String razao_social_tomador) {
		this.razao_social_tomador = razao_social_tomador;
	}
	public String getEndereco_tomador() {
		return endereco_tomador;
	}
	public void setEndereco_tomador(String endereco_tomador) {
		this.endereco_tomador = endereco_tomador;
	}
	public String getCep_tomador() {
		return cep_tomador;
	}
	public void setCep_tomador(String cep_tomador) {
		this.cep_tomador = cep_tomador;
	}
	public String getBairro_tomador() {
		return bairro_tomador;
	}
	public void setBairro_tomador(String bairro_tomador) {
		this.bairro_tomador = bairro_tomador;
	}
	public String getCidade_tomador() {
		return cidade_tomador;
	}
	public void setCidade_tomador(String cidade_tomador) {
		this.cidade_tomador = cidade_tomador;
	}
	public String getIbge_tomador() {
		return ibge_tomador;
	}
	public void setIbge_tomador(String ibge_tomador) {
		this.ibge_tomador = ibge_tomador;
	}
	public String getDesc_estado_tomador() {
		return desc_estado_tomador;
	}
	public void setDesc_estado_tomador(String desc_estado_tomador) {
		this.desc_estado_tomador = desc_estado_tomador;
	}
	public String getCod_estado_tomador() {
		return cod_estado_tomador;
	}
	public void setCod_estado_tomador(String cod_estado_tomador) {
		this.cod_estado_tomador = cod_estado_tomador;
	}
	public String getCnpj_tomador() {
		return cnpj_tomador;
	}
	public void setCnpj_tomador(String cnpj_tomador) {
		this.cnpj_tomador = cnpj_tomador;
	}
	public String getInsc_estadual_tomador() {
		return insc_estadual_tomador;
	}
	public void setInsc_estadual_tomador(String insc_estadual_tomador) {
		this.insc_estadual_tomador = insc_estadual_tomador;
	}
	public String getChave_cte_anterior() {
		return chave_cte_anterior;
	}
	public void setChave_cte_anterior(String chave_cte_anterior) {
		this.chave_cte_anterior = chave_cte_anterior;
	}
	public String getRazao_social_emissor() {
		return razao_social_emissor;
	}
	public void setRazao_social_emissor(String razao_social_emissor) {
		this.razao_social_emissor = razao_social_emissor;
	}
	public String getEndereco_emissor() {
		return endereco_emissor;
	}
	public void setEndereco_emissor(String endereco_emissor) {
		this.endereco_emissor = endereco_emissor;
	}
	public String getCep_emissor() {
		return cep_emissor;
	}
	public void setCep_emissor(String cep_emissor) {
		this.cep_emissor = cep_emissor;
	}
	public String getBairro_emissor() {
		return bairro_emissor;
	}
	public void setBairro_emissor(String bairro_emissor) {
		this.bairro_emissor = bairro_emissor;
	}
	public String getCidade_emissor() {
		return cidade_emissor;
	}
	public void setCidade_emissor(String cidade_emissor) {
		this.cidade_emissor = cidade_emissor;
	}
	public String getIbge_emissor() {
		return ibge_emissor;
	}
	public void setIbge_emissor(String ibge_emissor) {
		this.ibge_emissor = ibge_emissor;
	}
	public String getDesc_estado_emissor() {
		return desc_estado_emissor;
	}
	public void setDesc_estado_emissor(String desc_estado_emissor) {
		this.desc_estado_emissor = desc_estado_emissor;
	}
	public String getCod_estado_emissor() {
		return cod_estado_emissor;
	}
	public void setCod_estado_emissor(String cod_estado_emissor) {
		this.cod_estado_emissor = cod_estado_emissor;
	}
	public String getCnpj_emissor() {
		return cnpj_emissor;
	}
	public void setCnpj_emissor(String cnpj_emissor) {
		this.cnpj_emissor = cnpj_emissor;
	}
	public String getInsc_estadual_emissor() {
		return insc_estadual_emissor;
	}
	public void setInsc_estadual_emissor(String insc_estadual_emissor) {
		this.insc_estadual_emissor = insc_estadual_emissor;
	}
	public String getCte_serie() {
		return cte_serie;
	}
	public void setCte_serie(String cte_serie) {
		this.cte_serie = cte_serie;
	}
	public String getCte_numero() {
		return cte_numero;
	}
	public void setCte_numero(String cte_numero) {
		this.cte_numero = cte_numero;
	}
	public String getCte_emissao() {
		return cte_emissao;
	}
	public void setCte_emissao(String cte_emissao) {
		this.cte_emissao = cte_emissao;
	}
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
	public String getData_execucao_result() {
		return data_execucao_result;
	}
	public void setData_execucao_result(String data_execucao_result) {
		this.data_execucao_result = data_execucao_result;
	}
	
	public String getFlag_lido() {
		return flag_lido;
	}
	public void setFlag_lido(String flag_lido) {
		this.flag_lido = flag_lido;
	}
	
}
