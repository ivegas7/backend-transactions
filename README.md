# Backend Transaction
#  Descripción
Backend para gestionar las transacciones de una cuenta de Tenpista. Permite a los usuarios listar, registrar, editar y eliminar transacciones, las cuales contienen detalles como el monto, el comercio, el nombre de Tenpista y la fecha de la transacción.

# Clonar el Repositorio
Primero, clona el repositorio en tu máquina local:
git clone https://github.com/ivegas7/backend-transaction.git

# Ejecutar los Contenedores con Docker Composer

Descargar, instalar y ejecutar docker (https://docs.docker.com/desktop/setup/install/windows-install/)
Este proyecto está configurado para ejecutarse con Docker y Docker Compose.
Este comando construirá imagenes los contenedores de Docker y los pondrá en marcha : 
docker-compose up --build

Si prefieres hacerlo manual
docker build -t backend . 
luego levanar contenedores
docker-compose up 

El backend estará disponible en http://localhost:8080

# Configuración de la Base de Datos
La base de datos PostgreSQL se configura automáticamente en el archivo docker-compose.yml. No es necesario realizar ninguna configuración adicional para que el sistema funcione localmente, ya que los contenedores están preconfigurados para establecer las conexiones correctas.
Si necesitas interactuar directamente con la base de datos, puedes hacerlo a través del contenedor de PostgreSQL utilizando la herramienta psql o cualquier cliente de base de datos que soporte PostgreSQL.

# Ejecutar las Pruebas (Opcional)
Si deseas ejecutar las pruebas unitarias para asegurarte de que todos los servicios funcionen correctamente

# Documentación Swagger (Interactuar con la API)
La documentación completa de la API está disponible en Swagger UI. Para acceder, abre tu navegador y navega a:
http://localhost:8080/swagger-ui/index.html
Aquí podrás ver todos los endpoints, sus parámetros y ejemplos de respuesta.

# Configuración de Rate Limiting
Para evitar abusos, se ha implementado un rate limiting de 3 solicitudes por minuto por cliente. Si se superan las solicitudes, el servidor devolverá un error HTTP 429 (Too Many Requests).

Ejecutar la aplicación localmente y empezar a interactuar con la API!!
