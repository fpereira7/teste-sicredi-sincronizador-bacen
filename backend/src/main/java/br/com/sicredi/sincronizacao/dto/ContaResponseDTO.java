package br.com.sicredi.sincronizacao.dto;

import com.opencsv.bean.CsvBindByName;

public class ContaResponseDTO {

	@CsvBindByName(column = "agencia")
	private String agencia;

	@CsvBindByName(column = "conta")
	private String conta;

	@CsvBindByName(column = "saldo")
	private Double saldo;

	@CsvBindByName(column = "status")
	private boolean status;

	public ContaResponseDTO() {
	}

	public ContaResponseDTO(String agencia, String conta, Double saldo, boolean status) {
		this.agencia = agencia;
		this.conta = conta;
		this.saldo = saldo;
		this.status = status;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ContaResponseDTO [agencia=" + agencia + ", conta=" + conta + ", saldo=" + saldo + ", status=" + status
				+ "]";
	}

}
