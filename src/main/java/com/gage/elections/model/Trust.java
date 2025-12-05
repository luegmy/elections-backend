package com.gage.elections.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trust {
    // Número de escándalos mediáticos graves verificados
    // (Ej: Nepotismo comprobado, conflictos de interés, "amiguismo" en contratos)
    private int majorScandals;
    // Número de controversias menores o "gaffes"
    // (Ej: Declaraciones públicas ofensivas, uso indebido de recursos menores)
    private int minorControversies;
    // Conteo de verificaciones de "Falso" o "Engañoso"
    // por agencias de Fact-Checking reconocidas.
    private int factCheckFailures;
    // ¿Ha recibido sanciones de comités de ética (del Congreso, del partido, etc.)?
    private boolean ethicsSanction;

}
