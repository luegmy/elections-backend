# 🗳️ Plataforma de Consulta Electoral – Perú 2026

### Información pública estructurada para una ciudadanía informada

Esta plataforma es un **sistema backend de consulta ciudadana** orientado a centralizar, estructurar y presentar información pública sobre candidatos electorales del Perú, con el objetivo de **facilitar el acceso a datos relevantes sin interpretaciones políticas ni recomendaciones electorales**.

El sistema **no emite juicios de valor**, **no recomienda candidatos** y **no reemplaza** a las entidades oficiales del sistema electoral peruano. Su propósito es **informativo y técnico**.

---

## 🎯 Propósito del Proyecto

En el contexto electoral, la información sobre candidatos suele encontrarse **dispersa, fragmentada y presentada en formatos poco accesibles**.  
Esta plataforma busca:

- Reducir la fricción de acceso a información pública electoral.
- Estandarizar datos provenientes de distintas fuentes oficiales.
- Presentar perfiles claros y auditables para **consulta ciudadana**.
- Servir como **caso de estudio técnico** en análisis de datos cívicos.

---

## 🧠 Motor de Análisis Informativo (Marco Klitgaard)

El núcleo del sistema es un motor de análisis informativo inspirado en el **marco conceptual de Robert Klitgaard**, adaptado como **modelo de observación y ponderación de información pública**, no como sistema sancionador.

El análisis se organiza en **cuatro pilares informativos**, cada uno con un peso relativo configurable:

| Pilar | Peso | Componente | Enfoque |
|-----|------|-----------|--------|
| Judicial | 40% | `JudicialScoreCalculator` | Estado y severidad informativa de procesos públicos registrados |
| Transparencia | 25% | `TransparencyScoreCalculator` | Nivel de cumplimiento de declaraciones y registros públicos |
| Contribución / Trayectoria | 15% | `ContributionScoreCalculator` | Experiencia académica, legislativa y participación documentada |
| Confianza / Observaciones | 20% | `TrustScoreCalculator` | Observaciones públicas, sanciones éticas y verificaciones externas |

> ⚠️ **Importante:**  
> Este análisis **no determina culpabilidad**, **no reemplaza decisiones judiciales** y **respeta el principio de presunción de inocencia**.  
> Los valores reflejan **exposición informativa**, no sentencias.

---

## 🔍 Búsqueda Ciudadana Inteligente

El sistema implementa un motor de búsqueda avanzado basado en **MongoDB Atlas Search**, optimizado para consultas ciudadanas amplias y no técnicas.

### Características principales:

- **Prioridad Programática:**  
  Las propuestas de planes de gobierno reciben mayor relevancia en las búsquedas temáticas (ej. seguridad, economía, educación).

- **Contexto del Resultado:**  
  Cada coincidencia incluye metadatos que explican **por qué** aparece un resultado (propuesta, trayectoria, referencia pública).

- **Sanitización y estabilidad:**  
  Las consultas son normalizadas para evitar errores, inyecciones o resultados inconsistentes.

---

## 📊 Niveles de Perfil Informativo

Tras procesar los pilares, el sistema asigna un **nivel de perfil informativo**, utilizado únicamente como **clasificación descriptiva**:

- **Nivel 1:** Información consistente y completa.
- **Nivel 2:** Observaciones menores registradas.
- **Nivel 3:** Observaciones relevantes activas.
- **Nivel 4:** Alta exposición a procesos o controversias públicas.
- **Nivel 5:** Múltiples observaciones públicas simultáneas.

> Estos niveles **no constituyen recomendaciones** ni evaluaciones políticas.

---

## 🛠️ Arquitectura Técnica

- **Backend:** Spring Boot 3 (Java)
- **Persistencia:** MongoDB (modelo flexible para hojas de vida dinámicas)
- **Mapeo de datos:** MapStruct
- **Diseño:** Arquitectura orientada a servicios
- **Auditoría:**
    - `lastAuditDate` – última actualización del perfil
    - `dataSourceVersion` – versión y origen de los datos

---

## 🛡️ Principios de Neutralidad y Veracidad

1. **Fuentes públicas verificables**  
   Toda la información proviene de registros públicos u observables oficiales.

2. **Neutralidad algorítmica**  
   El sistema calcula y clasifica; **no interpreta ni recomienda**.

3. **Trazabilidad completa**  
   Cada perfil incluye referencia temporal y versión de la fuente utilizada.

4. **Configuración transparente**  
   Los pesos y reglas del motor están definidos en archivos de configuración, no en lógica opaca.

---

## ⚖️ Declaración de Alcance

Este proyecto es:
- Informativo
- Técnico
- Educativo

No es:
- Un organismo fiscalizador
- Un sistema de recomendación electoral
- Un sustituto del sistema judicial o electoral peruano
