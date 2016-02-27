package price;

public class CalculadoraPrice {

	public ValorMonetario calcularValorDaParcela(ValorMonetario valorSolicitadoPeloCliente, ValorTaxa taxaJurosMensais, ValorInteiro quantidadeDeParcelas) {
		
		ValorDecimal cem = new ValorDecimal(100);
		ValorDecimal um = new ValorDecimal(1);
		
		ValorDecimal juros = taxaJurosMensais.dividePor(cem);
		ValorDecimal fator = um.soma(juros).potenciaDe(quantidadeDeParcelas);
		ValorDecimal fatorVezesJuros = fator.multiplicaPor(juros);
		ValorDecimal fatorMenosUm = fator.subtrai(um);
		ValorDecimal fatoresDivididos = fatorVezesJuros.dividePor(fatorMenosUm);
		ValorDecimal resultado = valorSolicitadoPeloCliente.multiplicaPor(fatoresDivididos);
		
		return new ValorMonetario(resultado.getValor()); 
	}

}
