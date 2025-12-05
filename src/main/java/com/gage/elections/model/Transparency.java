package com.gage.elections.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transparency {
    // ¿Presentó su declaración jurada de bienes e intereses?
    private boolean submittedDeclaration;
    // ¿Cuántas omisiones o inconsistencias se encontraron en esa declaración?
    private int declarationInconsistencies;
    // ¿Cuántas veces cambió de partido ("transfuguismo")?
    private int partySwitches;
    // ¿Fue congresista/funcionario antes?
    private boolean wasPublicOfficer;
    // Si lo fue, ¿cuál fue su % de asistencia a sesiones? (ej: 95.0)
    private double attendancePercentage;
    private boolean publishedIncome;   // Declaró ingresos detallados
    private boolean publishedExpenses; // Detalle de gastos de campaña
    private boolean auditsAvailable;   // Informes de auditoría accesibles

}
