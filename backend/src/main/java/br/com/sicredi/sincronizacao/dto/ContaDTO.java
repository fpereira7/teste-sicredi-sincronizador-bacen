package br.com.sicredi.sincronizacao.dto;

import com.opencsv.bean.CsvBindByName;

public class ContaDTO {

	@CsvBindByName(column = "agencia")
	private String agencia;

	@CsvBindByName(column = "conta")
	private String conta;

	@CsvBindByName(column = "saldo")
	private Double saldo;
	

	public ContaDTO() {
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

	@Override
	public String toString() {
		return "ContaDTOteste [agencia=" + agencia + ", conta=" + conta + ", saldo=" + saldo + "]";
	}

	public ContaDTO(String agencia, String conta, Double saldo) {
		this.agencia = agencia;
		this.conta = conta;
		this.saldo = saldo;
	}

}
