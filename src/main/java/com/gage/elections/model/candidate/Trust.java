package com.gage.elections.model.candidate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trust {
    // Fuente: Ministerio Público / Comisiones de Ética
    private int majorSanctions; // Sanciones administrativas firmes
    private int minorSanctions;

    // Fuente: ROP (Registro de Organizaciones Políticas)
    private int partySwitches; // Cuántas veces renunció a un partido

    // Fuente: Agencias certificadas (Ama Llulla, Convoca, etc.)
    private int factCheckFailures;

    // Fuente: Registro de Sanciones contra Servidores Civiles
    private boolean ethicsSanction;
}
