package price;

public enum PeriodicidadeEnum {
	
	MES("M","30"),
	BIMESTRE("B","60"),
	TRIMESTRE("T","90"),
	QUADRIMESTRE("Q","120"),
	SEMESTRE("S","180"),
	ANUAL("A","365"),
	CORRIDO("C","360");
	
	public String periodicidade;
	
	public String quantidadeDias;
	
	private PeriodicidadeEnum(String periodicidade, String quantidadeDias){
		this.periodicidade = periodicidade;
		this.quantidadeDias = quantidadeDias; 
	}

}
