package price;

import java.io.Serializable;
import java.util.Date;

public class PlanilhaOperacaoTO implements Serializable {

	private static final long serialVersionUID = -2695995675105318537L;

	private Integer numeroParcela;

	private Date vencimentoParcela;

	private Integer diasCorridos;

	private Integer diasDiferencaParcelas;

	private Double saldoDevedor;

	private Double principalParcela;

	private Double jurosParcela;

	private Double valorParcela;

	private Double iofParcela;

	private Double iofTotal;

	private Double valorTotalOperacao;
	
	private Double bonus;
	
	private Double jurosBonus;
	
	private Double valorParcelaBonus;

	public Integer getNumeroParcela() {
		return numeroParcela;
	}

	public void setNumeroParcela(Integer numeroParcela) {
		this.numeroParcela = numeroParcela;
	}

	public Date getVencimentoParcela() {
		return vencimentoParcela;
	}

	public void setVencimentoParcela(Date vencimentoParcela) {
		this.vencimentoParcela = vencimentoParcela;
	}

	public Integer getDiasCorridos() {
		return diasCorridos;
	}

	public void setDiasCorridos(Integer diasCorridos) {
		this.diasCorridos = diasCorridos;
	}

	public Integer getDiasDiferencaParcelas() {
		return diasDiferencaParcelas;
	}

	public void setDiasDiferencaParcelas(Integer diasDiferencaParcelas) {
		this.diasDiferencaParcelas = diasDiferencaParcelas;
	}

	public Double getSaldoDevedor() {
		return saldoDevedor;
	}

	public void setSaldoDevedor(Double saldoDevedor) {
		this.saldoDevedor = saldoDevedor;
	}

	public Double getPrincipalParcela() {
		return principalParcela;
	}

	public void setPrincipalParcela(Double principalParcela) {
		this.principalParcela = principalParcela;
	}

	public Double getJurosParcela() {
		return jurosParcela;
	}

	public void setJurosParcela(Double jurosParcela) {
		this.jurosParcela = jurosParcela;
	}

	public Double getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(Double valorParcela) {
		this.valorParcela = valorParcela;
	}

	public Double getIofParcela() {
		return iofParcela;
	}

	public void setIofParcela(Double iofParcela) {
		this.iofParcela = iofParcela;
	}

	public Double getIofTotal() {
		return iofTotal;
	}

	public void setIofTotal(Double iofTotal) {
		this.iofTotal = iofTotal;
	}

	public Double getValorTotalOperacao() {
		return valorTotalOperacao;
	}

	public void setValorTotalOperacao(Double valorTotalOperacao) {
		this.valorTotalOperacao = valorTotalOperacao;
	}

	public Double getBonus() {
		return bonus;
	}

	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}

	public Double getJurosBonus() {
		return jurosBonus;
	}

	public void setJurosBonus(Double jurosBonus) {
		this.jurosBonus = jurosBonus;
	}

	public Double getValorParcelaBonus() {
		return valorParcelaBonus;
	}

	public void setValorParcelaBonus(Double valorParcelaBonus) {
		this.valorParcelaBonus = valorParcelaBonus;
	}
}
