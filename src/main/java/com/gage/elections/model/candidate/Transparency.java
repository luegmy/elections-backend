package com.gage.elections.model.candidate;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Transparency {
    // Fuente: Contraloría General de la República
    boolean submittedDeclaration;
    int declarationInconsistencies; // Reportadas por Contraloría
    List<String> inconsistencyDetails;
    String economicAuditNotes;

    // Fuente: Portal de Transparencia Estándar
    boolean wasPublicOfficer;
    double attendancePercentage; // Solo aplica si wasPublicOfficer es true

    // Fuente: ONPE (Claridad en cuentas de campaña)
    boolean publishedIncome;
    boolean publishedExpenses;
    boolean auditsAvailable;
}
