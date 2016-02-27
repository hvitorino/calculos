package price;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Financiar {

	public static Financiar valor(double valor) {
		
		OpcoesFinanciamento opcoes = new OpcoesFinanciamento();
		opcoes.setValorFinanciado(new ValorMonetario(valor));
		
		return new Financiar(opcoes);
	}

	private Financiar(OpcoesFinanciamento opcoes) {
		this.opcoes = opcoes;
	}
	
	private OpcoesFinanciamento opcoes;

	public Financiar divididoEmParcelas(int quantidadeParcelas) {
		
		this.opcoes.setQuantidadeParcelas(new ValorInteiro(quantidadeParcelas));
		return this;
	}

	public Financiar comJuros(double taxaJuros, PeriodicidadeTaxa periodicidadeTaxa) {
		
		this.opcoes.setJuros(new Juros(new ValorTaxa(taxaJuros), periodicidadeTaxa));
		return this;
	}

	public Financiar comIofDiario(double iofDiario) {
		
		this.opcoes.setTaxaIofDiario(new ValorTaxa(iofDiario));
		return this;
	}
	
	public Financiar comIofAdicional(double iofAdicional) {
		
		this.opcoes.setTaxaIofAdicional(new ValorTaxa(iofAdicional));
		return this;
	}

	public Financiar contratadoEm(String dataContratacao) throws ParseException {
		
		Date data = new SimpleDateFormat("dd/MM/yyyy").parse(dataContratacao);
		this.opcoes.setDataFinanciamento(data);
		return this;
	}

	public Financiar vencendoAPrimeiraParcelaEm(String vencimento) throws ParseException {
		
		Date data = new SimpleDateFormat("dd/MM/yyyy").parse(vencimento);
		this.opcoes.setDataPrimeiroVencimento(data);
		return this;
	}
	
	public OpcoesFinanciamento pronto() {
		return this.opcoes;
	}
}
