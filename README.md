# Diario Espacial – PC2 Desarrollo de Aplicaciones Móviles (2026-2)

App Android en **Kotlin + Jetpack Compose** que consume la API abierta de la NASA
(APOD y NeoWs) y guarda los datos en una base local **SQLite con Room**.

## Integrantes

| Rol | Opción | Integrante | Código |
|-----|--------|------------|--------|
| Alumno 1 | Imágenes (APOD) | _Nombre completo_ COZ | 23100101 |
| Alumno 2 | Asteroides (NeoWs) | Hugo FLORES | 24200293 |

## Cómo ejecutar el proyecto

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Hugo275-maker/24200293-FLORES-23100101-COZ-PC2-DAM.git
   ```
2. Abrir la carpeta en **Android Studio** (`File > Open`) y esperar el *Gradle Sync*.
3. Generar una API key en <https://api.nasa.gov> (sección *Generate API Key*).
   No usar `DEMO_KEY`.
4. Abrir el archivo **`local.properties`** de la raíz del proyecto (Android Studio lo
   crea solo con la ruta del SDK) y agregar esta línea:
   ```properties
   NASA_API_KEY=tu_clave_aqui
   ```
   `local.properties` está en `.gitignore`, así que **la clave nunca se sube al repositorio**.
   El código la lee con `BuildConfig.NASA_API_KEY`. Hay un ejemplo en `local.properties.example`.
5. Volver a sincronizar Gradle (*Sync Now*) y ejecutar la app (▶) en un emulador o celular.

Requisitos: Gradle 9.6.0 (wrapper), AGP 8.7.2, Java 17, Android Studio reciente (JDK 17 o superior incluido), SDK 35, dispositivo con Android 8.0 (API 26) o superior.

## Tecnologías

- Kotlin, Jetpack Compose, Material 3
- Navigation Compose (barra inferior + navegación interna por opción)
- Retrofit + Gson (consumo de la API), OkHttp
- Room (persistencia local), KSP
- Coil (carga de imágenes)
- ViewModel + StateFlow, Repository

## Estructura del proyecto

```
app/src/main/java/pe/edu/esan/diarioespacial/
├── DiarioEspacialApp.kt        # Application: crea el AppContainer
├── AppContainer.kt             # Dependencias compartidas (BD + repositorios)
├── MainActivity.kt
├── data/
│   ├── remote/RetrofitClient.kt     # [común] Retrofit + api_key automática
│   └── local/
│       ├── AppDatabase.kt           # [común] Base de datos Room (2 tablas)
│       ├── apod/                    # [Alumno 1] ApodEntity, ApodDao
│       └── asteroides/              # [Alumno 2] AsteroidEntity, AsteroidDao
├── ui/
│   ├── navigation/                  # [común] Barra inferior y NavHost
│   ├── common/                      # [común] Pantalla con barra, carga, mensaje vacío
│   ├── theme/                       # [común] Colores y tema
│   ├── imagenes/                    # [Alumno 1] Grafo, pantallas, ViewModel
│   └── asteroides/                  # [Alumno 2] Grafo, pantallas, ViewModel
└── util/FechaUtils.kt               # [común] Rango de los últimos 7 días
```

## Acuerdo de coordinación

**Base común.** La prepara el **Alumno 2 (Flores)** en la rama `feature/base-comun` y la
entrega con un Pull Request a `main` que revisa y aprueba el **Alumno 1 (Coz)**.
Incluye:

- Proyecto Gradle con todas las dependencias (Compose, Navigation, Retrofit, Room, Coil).
- `RetrofitClient`: URL base `https://api.nasa.gov/` y un interceptor que agrega la
  `api_key` a todas las peticiones (leída de `BuildConfig`).
- `AppDatabase` con las **dos tablas ya registradas** (`apod` y `asteroides`). Cada
  Entity y cada DAO están en el paquete de su alumno con lo mínimo para compilar.
- Navegación principal: barra inferior con *Imágenes* y *Asteroides*. Cada opción tiene
  su propio grafo anidado (`imagenesGraph` / `asteroidesGraph`) en su paquete.
- Componentes comunes: `PantallaConBarra`, `IndicadorCarga`, `MensajeVacio`, y `FechaUtils`.

**Cómo la integra cada uno.**

1. Después de fusionar la base, cada integrante actualiza `main` y crea su rama:
   - Alumno 1: `feature/imagenes`
   - Alumno 2: `feature/asteroides`
2. Cada uno trabaja **solo en sus archivos**:
   - su interfaz de Retrofit (`ApodApiService` / `NeoApiService`), creada con
     `RetrofitClient.create<...>()`;
   - sus DTOs, su Entity, su DAO, su Repository, su ViewModel y sus pantallas;
   - su grafo de navegación (`ImagenesNavGraph.kt` / `AsteroidesNavGraph.kt`).
3. Los únicos archivos compartidos que se tocan son `AppContainer.kt` (cada uno en su
   bloque marcado) y, si cambia una Entity, nada más: la BD usa
   `fallbackToDestructiveMigration()` durante el desarrollo.
4. Antes de abrir el PR: `git pull origin main` en la rama propia para resolver
   conflictos localmente.

**Reglas del repositorio.**

- `main` está protegida: no se hacen commits directos. Todo entra por Pull Request.
- Cada PR lo revisa y aprueba el otro integrante, con al menos un comentario de revisión.
- Commits pequeños con mensajes que explican el cambio.
- `AppDatabase` está en la **versión 3** (cambiaron `AsteroidEntity` y `ApodEntity`). Si alguien
  vuelve a cambiar una Entity, sube la versión a 4; con
  `fallbackToDestructiveMigration()` la base se recrea sola.
