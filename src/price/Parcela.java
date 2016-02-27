package price;

import java.util.Date;

public class Parcela {

	private ValorInteiro numero;
	private ValorMonetario saldoDevedor;
	private ValorMonetario valor;
	private ValorMonetario valorPrincipal;
	private ValorMonetario valorJuros;
	private ValorMonetario valorIofDiario;
	private ValorMonetario valorIofAdicional;
	private Date dataVencimento;

	public ValorInteiro getNumero() {
		return numero;
	}

	public void setNumero(ValorInteiro numero) {
		this.numero = numero;
	}

	public ValorMonetario getSaldoDevedor() {
		return saldoDevedor;
	}

	public void setSaldoDevedor(ValorMonetario saldoDevedor) {
		this.saldoDevedor = saldoDevedor;
	}

	public ValorMonetario getValor() {
		return valor;
	}

	public void setValor(ValorMonetario valor) {
		this.valor = valor;
	}

	public ValorMonetario getValorPrincipal() {
		return valorPrincipal;
	}

	public void setValorPrincipal(ValorMonetario valorPrincipal) {
		this.valorPrincipal = valorPrincipal;
	}

	public ValorMonetario getValorJuros() {
		return valorJuros;
	}

	public void setValorJuros(ValorMonetario valorJuros) {
		this.valorJuros = valorJuros;
	}

	public ValorMonetario getValorIofDiario() {
		return valorIofDiario;
	}

	public void setValorIofDiario(ValorMonetario valorIofDiario) {
		this.valorIofDiario = valorIofDiario;
	}

	public ValorMonetario getValorIofAdicional() {
		return valorIofAdicional;
	}

	public void setValorIofAdicional(ValorMonetario valorIofAdicional) {
		this.valorIofAdicional = valorIofAdicional;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

}
