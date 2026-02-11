# Sistema de Voto Informado

## 1. Propósito del sistema

Este sistema tiene como objetivo **consolidar información pública e histórica de los candidatos** a cargos de elección popular, con el fin de **apoyar a la ciudadanía en la toma de un voto informado**.

El sistema **no recomienda candidatos ni influye directamente en la decisión de voto**. Su función es **proveer indicadores objetivos de riesgo, idoneidad y confiabilidad**, basados en datos verificables.

---

## 2. Principios rectores

El diseño del sistema se rige por los siguientes principios:

* **Neutralidad política**: no favorece ni perjudica ideologías o partidos.
* **Presunción de inocencia**: distingue claramente entre investigaciones, procesos y sentencias.
* **Transparencia**: cada puntaje puede ser explicado y auditado.
* **Proporcionalidad**: las penalizaciones reflejan la gravedad real de los hechos.
* **Configurabilidad**: las reglas de evaluación pueden ajustarse sin modificar el código.

---

## 3. ¿Qué evalúa el sistema?

El sistema evalúa a cada candidato en cinco dimensiones principales:

1. **Historial Judicial**
2. **Plan de Gobierno**
3. **Transparencia**
4. **Confianza Pública**
5. **Contribución y Experiencia**

Cada dimensión genera un sub‑puntaje, que luego se consolida en un **puntaje electoral global**.

---

## 4. Dimensión Judicial (riesgo legal)

Esta dimensión evalúa **antecedentes judiciales y legales**, considerando tanto el **estado del proceso** como la **naturaleza del caso**.

### Estados legales considerados

El sistema reconoce los siguientes estados, ordenados de menor a mayor impacto:

* **Investigación preliminar**: indicios iniciales, sin imputación formal.
* **Proceso judicial abierto**: existe acusación y el caso está en curso.
* **Sentencia de primera instancia**: existe una decisión judicial no firme.
* **Sentencia firme condenatoria**: condena definitiva.
* **Sentencia absolutoria / Prescrito**: no generan penalización.

### Severidad del caso

Cada caso puede clasificarse como:

* **Leve**
* **Moderado**
* **Grave**

La severidad representa el **impacto social y legal del hecho**, no el estado del proceso.

### Reglas clave

* Una **investigación preliminar solo puede ser leve**.
* Un **proceso en curso no puede ser grave**.
* La **máxima penalización solo aplica a sentencias firmes**.

Estas reglas aseguran respeto al debido proceso y evitan sanciones desproporcionadas.

---

## 5. Dimensión de Plan de Gobierno

Evalúa la **viabilidad y coherencia del plan de gobierno**, considerando:

* Costos estimados excesivos
* Necesidad de reformas constitucionales
* Posibles conflictos con tratados internacionales

No evalúa ideología ni propuestas políticas, solo **factibilidad técnica e institucional**.

---

## 6. Dimensión de Transparencia

Mide el nivel de apertura y consistencia de la información declarada por el candidato:

* Declaraciones patrimoniales
* Consistencia de la información económica
* Asistencia y cumplimiento de obligaciones públicas

Las inconsistencias reiteradas generan penalizaciones progresivas.

---

## 7. Dimensión de Confianza Pública

Evalúa factores que afectan la percepción pública del candidato:

* Escándalos mayores o menores
* Sanciones éticas
* Cambios frecuentes de partido político
* Resultados de verificaciones de hechos (fact‑checking)

Esta dimensión busca reflejar **estabilidad y credibilidad**.

---

## 8. Dimensión de Contribución y Experiencia

Mide el aporte del candidato a la sociedad y su preparación:

* Experiencia profesional
* Producción legislativa
* Formación académica
* Liderazgo social

También considera penalizaciones por promesas incumplidas o impactos fiscales negativos.

---

## 9. Puntaje final

Cada dimensión tiene un **peso configurable**. El puntaje final es el resultado ponderado de todas las dimensiones.

El sistema está diseñado para que:

* Ninguna dimensión por sí sola determine el resultado
* El historial judicial tenga mayor peso por su impacto institucional

---

## 10. Interpretación del resultado

El puntaje final **no indica por quién votar**.

Sirve para:

* Comparar perfiles de riesgo
* Identificar alertas tempranas
* Fomentar un análisis crítico del electorado

El sistema busca fortalecer la democracia mediante **información clara, verificable y explicable**.

---

## 11. Auditoría y trazabilidad

Todas las reglas de evaluación están definidas en archivos de configuración y pueden ser:

* Revisadas
* Auditadas
* Ajustadas según cambios normativos

Esto permite que el sistema sea utilizado como una **herramienta cívica confiable y responsable**.
