# 🗳️ Electoral Integrity Scoring Engine (EISE) - Perú 2026

![Licencia](https://img.shields.io/badge/Licencia-MIT-green)
![Java](https://img.shields.io/badge/Java-17%2B-blue)
![Status](https://img.shields.io/badge/Estado-Beta%20Cívico-orange)

### *Auditoría algorítmica y centralización de datos públicos para un voto informado.*

El **EISE** es un framework de código abierto diseñado para transformar la dispersión informativa en métricas comparables. Este sistema procesa datos judiciales, financieros y programáticos de los candidatos a las Elecciones Generales 2026, permitiendo al ciudadano visualizar el "riesgo de integridad" de cada postulación.

---

## 🏛️ Propósito y Alcance
En el ecosistema electoral peruano, la información de un candidato reside en al menos 5 plataformas distintas. Este proyecto actúa como un **interfaz de consolidación** que:
* **Centraliza** registros de la Ventanilla Única (JNE), ONPE y portales de transparencia.
* **Analiza** la viabilidad técnica de los planes de gobierno.
* **Alerta** sobre inconsistencias éticas o riesgos judiciales críticos.

> **Nota Legal:** Este software es una herramienta de análisis independiente. No constituye una calificación oficial del Estado Peruano. Los datos son obtenidos de fuentes de acceso público.

---

## 🧠 Arquitectura del Scoring
El motor de cálculo (`ScoringEngine`) opera bajo un modelo de **Ponderación Multidimensional**:

### 1. Ejes de Evaluación
| Dimensión | Peso | Factor Crítico |
| :--- | :---: | :--- |
| **Integridad Judicial** | 35% | Sentencias, procesos vigentes e investigaciones fiscales. |
| **Viabilidad del Plan** | 20% | Relación entre Impacto Social y Factibilidad Legal/Económica. |
| **Consistencia Ética** | 20% | Transfuguismo (Party Switches), sanciones y fact-checking. |
| **Transparencia Activa** | 15% | Declaraciones juradas y registros de asistencia previa. |
| **Trayectoria (Aportes)** | 10% | Grados académicos y experiencia en gestión pública. |

### 2. El Filtro de Seguridad (Circuit Breaker)
El algoritmo implementa un **bloqueo de integridad**: Si el sub-puntaje de *Integridad Judicial* es menor a **50/100**, el puntaje final total sufre una reducción del **50%**. Esto asegura que ningún logro académico o promesa de plan de gobierno pueda ocultar un historial judicial grave.

---

## 📊 Niveles de Ranking
El sistema clasifica el resultado final en cuatro niveles de recomendación:

* **Nivel 1 (85 - 100):** Candidato con alta solvencia ética y técnica.
* **Nivel 2 (65 - 84):** Candidato apto con observaciones menores.
* **Nivel 3 (40 - 64):** Candidato con riesgos moderados (judiciales o técnicos).
* **Nivel 4 (0 - 39):** Candidato de alto riesgo o no recomendado.

---

## 🛠️ Especificaciones Técnicas
El sistema está construido pensando en la transparencia total del código (**Open Logic**):
- **Core:** Java 17.
- **Data Engine:** Manejo de perfiles en `JSON`.
- **Configuración:** Reglas de penalización dinámicas en `YAML`.
- **Auditoría de Plan:** Lógica de filtrado por barreras constitucionales y tratados internacionales.

---

## 🤝 Cómo Contribuir
Este es un proyecto colaborativo. Puedes ayudar:
1.  **Reportando datos:** Si encuentras un expediente judicial no mapeado.
2.  **Auditando el Plan:** Ayudando a calificar la viabilidad de las nuevas propuestas.
3.  **Mejorando el Código:** Optimizando las calculadoras de score.

---
*Desarrollado con el fin de fortalecer la democracia y la vigilancia ciudadana.*
