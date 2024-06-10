package com.ginoquin.springboot.app.models.entity;

public enum TechnologyEnum {
	WHISPER ("Whisper"),
    WAV2VEC ("Wav2vec");
    
    private final String codigo;

    private TechnologyEnum(String codigo) {
        this.codigo = codigo;
    }

	public String getCodigo() {
		return codigo;
	}
	
	public static TechnologyEnum fromCodigo(String codigo) {
        for (TechnologyEnum technology : TechnologyEnum.values()) {
            if (technology.codigo.equalsIgnoreCase(codigo)) {
                return technology;
            }
        }
        throw new IllegalArgumentException("No se encontró una tecnología con el código proporcionado: " + codigo);
    }
    
}
