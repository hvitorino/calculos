package price;

import java.util.ArrayList;
import java.util.List;

public class Financiamento {

	private OpcoesFinanciamento opcoes;
	private ArrayList<Parcela> parcelas;
	private ValorMonetario valorEmprestimoAjustado;
	private ValorMonetario valorPrestacao;
	private ValorMonetario valorIofAdicional;
	private ValorMonetario valorIofTotal;
	
	public Financiamento(OpcoesFinanciamento opcoes) {
		this.parcelas = new ArrayList<>();
		this.setOpcoes(opcoes);
	}

	public void adicionaParcela(Parcela parcela) {
		this.parcelas.add(parcela);
	}

	public ValorMonetario getValorEmprestimoAjustado() {
		return valorEmprestimoAjustado;
	}

	public void setValorEmprestimoAjustado(ValorMonetario valorFinanciadoAjustado) {
		this.valorEmprestimoAjustado = valorFinanciadoAjustado;
	}

	public ValorMonetario getValorTotalJuros() {
		
		ValorMonetario jurosTotal = new ValorMonetario(0.0);
		
		for(Parcela parcela : this.getParcelas()) {
			jurosTotal = jurosTotal.soma(parcela.getValorJuros())
					.valorMonetario();
		}
		
		return jurosTotal;
	}

	public ValorMonetario getValorTotalEmprestimoAjustado() {
		
		return this.getOpcoes().getValorFinanciado()
				.soma(this.getValorTotalJuros())
				.valorMonetario();
	}

	public ValorMonetario getValorPrestacao() {
		return valorPrestacao;
	}

	public void setValorPrestacao(ValorMonetario valorPrestacao) {
		this.valorPrestacao = valorPrestacao;
	}

	public void setParcelas(ArrayList<Parcela> parcelas) {
		this.parcelas = parcelas;
	}

	public List<Parcela> getParcelas() {
		return this.parcelas;
	}

	public OpcoesFinanciamento getOpcoes() {
		return opcoes;
	}
	
	public ValorMonetario getValorIofAdicional() {
		return valorIofAdicional;
	}

	public void setValorIofAdicional(ValorMonetario valorIofAdicional) {
		this.valorIofAdicional = valorIofAdicional;
	}

	public ValorMonetario getValorIofTotal() {
		return valorIofTotal;
	}

	public void setValorIofTotal(ValorMonetario valorIofTotal) {
		this.valorIofTotal = valorIofTotal;
	}

	private void setOpcoes(OpcoesFinanciamento opcoes) {
		this.opcoes = opcoes;
	}
	
}
