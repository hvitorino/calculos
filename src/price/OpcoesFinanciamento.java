package price;

import java.util.ArrayList;
import java.util.Date;

public class OpcoesFinanciamento {

	private ValorMonetario valorFinanciado;
	private ValorInteiro quantidadeParcelas;
	private Date dataFinanciamento;
	private Date dataPrimeiroVencimento;
	private Juros juros;
	private ValorTaxa taxaIofDiario;
	private ValorTaxa taxaIofAdicional;
	private ValorMonetario valorPago;
	
	public ValorMonetario getValorFinanciado() {
		return valorFinanciado;
	}

	public void setValorFinanciado(ValorMonetario valorFinanciado) {
		this.valorFinanciado = valorFinanciado;
	}

	public ValorInteiro getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(ValorInteiro quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public Date getDataFinanciamento() {
		return dataFinanciamento;
	}

	public void setDataFinanciamento(Date dataFinanciamento) {
		this.dataFinanciamento = dataFinanciamento;
	}

	public Date getDataPrimeiroVencimento() {
		return dataPrimeiroVencimento;
	}

	public void setDataPrimeiroVencimento(Date dataPrimeiroVencimento) {
		this.dataPrimeiroVencimento = dataPrimeiroVencimento;
	}

	public Juros getJuros() {
		return juros;
	}

	public void setJuros(Juros juros) {
		this.juros = juros;
	}

	public ValorTaxa getTaxaIofDiario() {
		return taxaIofDiario;
	}

	public void setTaxaIofDiario(ValorTaxa taxaIofDiario) {
		this.taxaIofDiario = taxaIofDiario;
	}

	public ValorTaxa getTaxaIofAdicional() {
		return taxaIofAdicional;
	}

	public void setTaxaIofAdicional(ValorTaxa taxaIofAdicional) {
		this.taxaIofAdicional = taxaIofAdicional;
	}

	public ValorMonetario getValorPago() {
		return this.valorPago;
	}

	public void setValorPago(ValorMonetario valorPago) {
		this.valorPago = valorPago;
	}
}
