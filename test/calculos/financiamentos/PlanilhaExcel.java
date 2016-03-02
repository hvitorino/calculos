package calculos.financiamentos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;

import price.Financiamento;
import price.Parcela;

public class PlanilhaExcel {
	
	private String nomePlanilha;
	private Financiamento financiamento;

	public PlanilhaExcel(String nomePlanilha, Financiamento financiamento) {
		this.nomePlanilha = nomePlanilha;
		this.financiamento = financiamento;
	}
	
	public void salvar() {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			File file = new File("c:\\financiamentos\\" + nomePlanilha + ".csv");

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			StringBuilder tabela = new StringBuilder()
					.append("Número;")
					.append("Vencimento;")
					.append("Prazo (dias da contratação);")
					.append("Valor Parcela;")
					.append("Valor Principal;")
					.append("Valor Juros;")
					.append("Saldo Devedor\r\n");
			
			for(Parcela parcela : financiamento.getParcelas()) {
				tabela
					.append(parcela.getNumero() + ";")
					.append(formatter.format(parcela.getDataVencimento()) + ";")
					.append(parcela.getPrazoEmDias() + ";")
					.append(parcela.getValor() + ";")
					.append(parcela.getValorPrincipal() + ";")
					.append(parcela.getValorJuros() + ";")
					.append(parcela.getSaldoDevedor() + "\r\n");
			}
			
			bw.write(tabela.toString());
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}