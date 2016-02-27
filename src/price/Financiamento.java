package price;

import java.util.ArrayList;
import java.util.List;

public class Financiamento {

	private ArrayList<Parcela> parcelas;
	private OpcoesFinanciamento opcoes;
	
	public Financiamento(OpcoesFinanciamento opcoes) {
		this.parcelas = new ArrayList<>();
		this.setOpcoes(opcoes);
	}

	public void adicionaParcela(Parcela parcela) {
		this.parcelas.add(parcela);
	}

	public List<Parcela> getParcelas() {
		return this.parcelas;
	}

	public OpcoesFinanciamento getOpcoes() {
		return opcoes;
	}

	public void setOpcoes(OpcoesFinanciamento opcoes) {
		this.opcoes = opcoes;
	}
}
