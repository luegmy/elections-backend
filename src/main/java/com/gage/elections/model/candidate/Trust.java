package com.gage.elections.model.candidate;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Trust {
    // Fuente: Ministerio Público / Comisiones de Ética
    private int majorSanctions; // Sanciones administrativas firmes
    private int minorSanctions;
    private Map<String, List<String>> sanctionsDetail;

    // Fuente: ROP (Registro de Organizaciones Políticas)
    private int partySwitches; // Cuántas veces renunció a un partido

    // Fuente: Agencias certificadas (Ama Llulla, Convoca, etc.)
    private int factCheckFailures;
    private List<String> factCheckSources;

    // Fuente: Registro de Sanciones contra Servidores Civiles
    private boolean ethicsSanction;
}
