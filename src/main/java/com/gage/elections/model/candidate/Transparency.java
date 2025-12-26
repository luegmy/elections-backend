package com.gage.elections.model.candidate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transparency {
    // Fuente: Contraloría General de la República
    private boolean submittedDeclaration;
    private int declarationInconsistencies; // Reportadas por Contraloría

    // Fuente: Portal de Transparencia Estándar
    private boolean wasPublicOfficer;
    private double attendancePercentage; // Solo aplica si wasPublicOfficer es true

    // Fuente: ONPE (Claridad en cuentas de campaña)
    private boolean publishedIncome;
    private boolean publishedExpenses;
    private boolean auditsAvailable;

    /*
     * Regla de Negocio: Si no fue funcionario, la asistencia no le resta puntos (N/A).
     */

}
