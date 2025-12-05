package com.gage.elections.model;

public enum LegalStatus {
    SENTENCIA_FIRME_CONDENATORIA, // Sentencia final, culpable
    SENTENCIA_PRIMERA_INSTANCIA, // Sentenciado (pero puede apelar)
    PROCESO_JUDICIAL_ABIERTO, // Acusado formalmente
    INVESTIGACION_PRELIMINAR, // Carpeta fiscal
    PRESCRITO, // El delito prescribió
    SENTENCIA_ABSOLUTORIA // Absuelto

}
