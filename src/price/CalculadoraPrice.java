package price;

public class CalculadoraPrice {

	public ValorMonetario calcularValorDaParcela(ValorMonetario valorSolicitadoPeloCliente, ValorTaxa taxaJurosMensais, ValorInteiro quantidadeDeParcelas) {
		
		Decimal cem = new Decimal(100);
		Decimal um = new Decimal(1);
		
		Decimal juros = taxaJurosMensais.dividePor(cem);
		Decimal fator = um.soma(juros).potenciaDe(quantidadeDeParcelas);
		Decimal fatorVezesJuros = fator.multiplicaPor(juros);
		Decimal fatorMenosUm = fator.subtrai(um);
		Decimal fatoresDivididos = fatorVezesJuros.dividePor(fatorMenosUm);
		Decimal resultado = valorSolicitadoPeloCliente.multiplicaPor(fatoresDivididos);
		
		return new ValorMonetario(resultado.getValor()); 
	}

}
