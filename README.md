# Aplicación Consejo



Esta aplicación es una pequeña prueba de concepto destinada a comprobar los requerimientos de la aplicación del Consejo. Se trata de una aplicación de **escritorio**.

### Importante:

Dado que se trata de un *spike* y se busca lograr el mayor avance posible en la menor cantidad de tiempo, hay aspectos que pese a ser básicos en una aplicación real, no se han implementado. Por ejemplo, a la hora de crear un punto o un documento, no se comprueba que los campos estén rellenos antes de permitir aceptar, o al adjuntar un documento no se comprueba que su extensión sea *pdf*, por citar algunos casos.

En definitiva, la aplicación funciona correctamente **si se usa correctamente**, pero no contempla todos los casos y posibles errores de un entorno real, ya que solo pretende mostrar la funcionalidad y comprobar las necesidades del cliente.

De la misma forma, el código fuente no está estructurado de manera ideal o en algunos casos no está refactorizado u optimizado, ya que la tecnología utilizada se fue aprendiendo al mismo tiempo que se desarrollaba el software, y de la misma forma puede ocurrir con otros aspectos como lo relativo a configuraciones, persistencia, etc...

## Tecnologías empleadas

La aplicación está desarrollada en Java 8 y Swing. Para la persistencia se utiliza una **base de datos H2** en fichero.

## Ejecutar la aplicación (como desarrollador)

**Nota: Si tan solo se desea probar y utilizar la aplicación, ignorar esta sección y saltar a la siguiente**

1. [Instalar la base de datos H2](https://www.h2database.com/html/main.html)  y a través de su aplicación Java crear una base de datos. Si se desea aprovechar la configuración ya escrita en el fichero de *Hibernate*, utilizar estos datos en la creación de la misma:

   ```
   Nombre de la Base de datos: consejo
   Usuario: avantic
   Password: avantic
   ```

2. Al crear la base de datos, fijarse y recordar el directorio elegido para la misma.

3. Una vez conectado, pasar los parches que se encuentran en *resources/sql*

4. Editar el fichero *hibernate.cfg.xml* que se encuentra en  el directorio *resources*. Localizar la línea de configuración de la url de conexión y modificarla con la ruta que se eligió al crear la base de datos H2. En el siguiente ejemplo, el fichero de base de datos estaba en el home del usuario. Si es el caso, modificar también el usuario y la contraseña de acceso.

```xml
<property name="hibernate.connection.url"> jdbc:h2:file:~/consejo;DB_CLOSE_ON_EXIT=TRUE;FILE_LOCK=NO</property>
```

5. Compilar y ejecutar la aplicación

## Ejecutar la aplicación (como usuario)

La aplicación consta de un *JAR* y un fichero de base de datos vacío sobre el que ya se han pasado los parches y está listo para utilizarse.

1. Descomprimir el fichero *consejo.7z*
2. Colocar el fichero *JAR* donde se desee.
3. Colocar el fichero de base de datos **en el home** del usuario.

#### Usando la aplicación

Al ejecutar el *JAR*, se creará una carpeta llamada *workingCopy* en la misma ubicación. Los documentos importados a la aplicación se irán añadiendo a esta carpeta. Si se borra un documento desde la aplicación, se borrará en el *workingCopy*, pero nunca el documento original.

Si uno o más documentos han sido incorporados (se encuentran en el *workingCopy*) y posteriormente los documentos originales son modificados, al pulsar el botón de sincronización, se informará de cuáles son estos documentos que han cambiado y si se confirma el mensaje de aviso, los documentos del *workingCopy* serán actualizados.

Al pulsar *Generar documento*, se generará el documento PDF unificado en la misma ubicación que el fichero *JAR*. La aplicación añade a este documento un índice clickable.

La portada es un punto especial que no puede ser eliminado. Puede adjuntarse un documento o bien pulsar el botón *Generar* para crear una portada automáticamente siguiendo la plantilla del Consejo.

#### Bugs detectados

* Al pulsar el botón *Generar* para generar una portada, se creará un documento de portada. Si una vez que este documento existe se vuelve a pulsar el botón *Generar*, el programa arroja una excepción (seguramente por alguna comprobación muy sencilla) pero el documento no se genera pese a que aparece en la interfaz gráfica. Esto se puede comprobar porque si se pulsa el botón de *ver documento* este no se abrirá, y su fecha figurará como el 1/1/1970 00:00:00. Si esto ocurre, simplemente hay que pulsar en el icono de *borrar documento* y volver a generar o adjuntar una portada con normalidad.

* No se decidió emplear tiempo en el formato visual del índice, pero este funciona correctamente. La zona clickable de cada apartado parece estar ligeramente descentrada dos o tres píxeles hacia abajo con respecto al texto.

* Como se mencionó al principio, para avanzar lo más rápidamente posible, el programa no es *user safe* y no impide que se use incorrectamente o se llegue a situaciones de error. Por ejemplo, no se comprueba que se haya adjuntado o generado una portada antes de generar el documento final, entre otras situaciones.
