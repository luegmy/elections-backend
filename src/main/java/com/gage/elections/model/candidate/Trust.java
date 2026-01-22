package com.gage.elections.model.candidate;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Trust {
    // Fuente: Ministerio Público / Comisiones de Ética
    int majorSanctions; // Sanciones administrativas firmes
    int minorSanctions;
    Map<String, List<String>> sanctionsDetail;

    // Fuente: ROP (Registro de Organizaciones Políticas)
    int partySwitches; // Cuántas veces renunció a un partido

    // Fuente: Agencias certificadas (Ama Llulla, Convoca, etc.)
    int factCheckFailures;
    List<String> factCheckSources;

    // Fuente: Registro de Sanciones contra Servidores Civiles
    boolean ethicsSanction;
}
