# TuVozATexto (API)

API utilizando Spring Boot para transcripción de voz a texto.

## Tabla de Contenidos

1. [Descripción](#descripción)
2. [Instalación](#instalación)
3. [Uso](#uso)
4. [Contribución](#contribución)
5. [Licencia](#licencia)

## Descripción

Este proyecto es un API para un servicio de conversión de voz a texto desarrollada con Spring Boot.

**Nota:** Para utilizar este proyecto, es necesario integrarlo con otro proyecto relacionado, disponible en [MiTFM-trans](https://github.com/uva2023/MiTFM-trans). Este último proyecto proporciona las funcionalidades de transcripción de audio a texto.

## Instalación

Para ejecutar este proyecto localmente, sigue estos pasos:

1. Clona este repositorio en tu máquina local.
2. Abre el proyecto en tu IDE preferido.
3. Configura tu entorno de desarrollo según sea necesario (instala Java, Maven, etc.).
4. Asegúrate de configurar las siguientes variables de entorno en tu sistema:
   - `DB_PASSWORD`: Contraseña para la base de datos.
   - `DB_URL`: URL de conexión a la base de datos PostgreSQL.
   - `DB_USER`: Usuario de la base de datos.
   - `RABBITMQ_HOST`: Host de RabbitMQ.
5. Ejecuta la aplicación utilizando el comando `mvn spring-boot:run`.
6. Accede a la aplicación en tu navegador web usando la URL proporcionada por Spring Boot (por defecto, `http://localhost:8080`).

## Uso

Puedes acceder a la API de Transcripción de Audio a través de la siguiente URL:

- Swagger UI (por defecto, `http://localhost:8080/swagger-ui/index.html`): Interfaz interactiva para explorar y probar la API de transcripción de audio.

La API proporciona los siguientes endpoints:

### Transcripción de Audio

- **Endpoint:** POST /api/transcription
- **Descripción:** Realiza la transcripción de un archivo de audio.
- **Parámetros de la solicitud:**
  - **technology (string, requerido):** Tecnología usada para la transcripción.
  - **file (archivo, requerido):** Archivo de audio a transcribir en formato multipart/form-data.
- **Respuestas:**
  - **200 OK:** Transcripción exitosa.
    - **Cuerpo de la respuesta:**
      ```json
      {
        "text": "string",
        "time": 0,
        "confidence": 0
      }
      ```
  - **400 Bad Request:** Solicitud incorrecta.
    - **Cuerpo de la respuesta:**
      ```json
      {
        "text": "string",
        "time": 0,
        "confidence": 0
      }
      ```

### Descargar Archivo de Audio

- **Endpoint:** GET /api/files/{fileName}
- **Descripción:** Descarga un archivo de audio.
- **Parámetros de la solicitud:**
  - **fileName (string, requerido):** Nombre del archivo a descargar.
- **Respuestas:**
  - **200 OK:** Descarga exitosa.
    - **Cuerpo de la respuesta:** El contenido del archivo de audio.
  - **400 Bad Request:** Solicitud incorrecta.

Para usar la API, simplemente realiza solicitudes HTTP a los endpoints correspondientes utilizando las herramientas adecuadas (como CURL, Postman, o directamente desde tu navegador).

## Contribución

Siéntete libre de abrir un issue o enviar un pull request con cualquier mejora o corrección.

## Licencia

Este proyecto está bajo la licencia Haz_con_ello_lo_que_quieras_pero_no_me_marees License.
