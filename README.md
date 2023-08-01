# Resumen

## Conexión a h2
**Connection h2Connection** = DriverManager.getConnection("jdbc:h2:~/test",properties);
## Query desde resources
RunScript.execute(h2Connection,new FileReader("src/main/resources/schema.sql"));
## Statement
`Statement` es una interfaz simple que se utiliza para ejecutar consultas SQL estáticas.

- Las consultas se pasan directamente a la base de datos tal como están escritas en el código Java.


- No permite el uso de parámetros, lo que significa que los valores deben ser concatenados directamente en la consulta, lo cual puede ser propenso a la inyección SQL si no se manejan correctamente.


- Cada vez que se ejecuta una consulta a través de `Statement`, la base de datos la compila y la optimiza nuevamente, lo que puede afectar el rendimiento en caso de ejecuciones repetidas de la misma consulta.


- No es adecuado para consultas que se repiten con frecuencia, ya que cada consulta debe ser compilada nuevamente.


## PreparedStatement
Un PreparedStatement es una interfaz en Java que se utiliza para ejecutar consultas parametrizadas precompiladas en una base de datos a través de JDBC (Java Database Connectivity). 

Permite crear consultas con marcadores de posición (?) en lugar de concatenar valores directamente en la consulta, lo que mejora la seguridad y el rendimiento al evitar la inyección SQL y permitir la reutilización de consultas.  

### Métodos de PreparedStatement y Statement

`executeQuery()`: Se utiliza para ejecutar consultas de selección (SELECT) y devuelve un objeto `ResultSet` que contiene los resultados de la consulta. Es adecuado cuando esperas obtener un conjunto de resultados de una consulta.

`executeUpdate()`: Se utiliza para ejecutar consultas de actualización, como `INSERT`, `UPDATE`, `DELETE`, etc. Devuelve el número de filas afectadas por la consulta. Es adecuado cuando realizas cambios en la base de datos y no necesitas obtener resultados.

`execute()`: Puede manejar tanto consultas de selección como consultas de actualización. 
- Devuelve `true` si la consulta devuelve un conjunto de resultados (`ResultSet`) 


- Devuelve `false` si no hay un conjunto de resultados. 

Para obtener el conjunto de resultados en caso de que `execute()` devuelva `true`, debes utilizar el método `getResultSet()`.

# Try With Resources

Usar `try-with-resources` al trabajar con `DriverManager`, `Statement` y `PreparedStatement` en JDBC es una buena práctica para garantizar la correcta liberación de recursos y evitar problemas de manejo de recursos.

`try-with-resources` es una característica de Java que se introdujo en Java 7 y permite que los recursos que implementan la interfaz `AutoCloseable` (y la subinterfaz `java.io.Closeable`) se cierren automáticamente al finalizar el bloque `try`, sin necesidad de escribir explícitamente un bloque `finally` para cerrar esos recursos. Esto asegura que los recursos se liberen adecuadamente, incluso en caso de excepciones.


- **DriverManager**: Al usar `DriverManager.getConnection()`, el objeto `Connection` representa una conexión a la base de datos. Al finalizar el bloque `try`, el `Connection` se cerrará automáticamente, liberando los recursos asociados y asegurándose de que la conexión se cierre adecuadamente.


- **Statement y PreparedStatement**: Estos objetos se utilizan para ejecutar consultas SQL y realizar operaciones en la base de datos. Al finalizar el bloque `try`, los objetos `Statement` y `PreparedStatement` se cerrarán automáticamente, liberando los recursos asociados, como conexiones subyacentes y buffers utilizados para enviar consultas.

# Transacciones
## Savepoint y Rollback en JDBC

En JDBC (Java Database Connectivity), **Savepoint** y **Rollback** son conceptos relacionados con la gestión de transacciones en bases de datos. Las transacciones se utilizan para agrupar operaciones en una secuencia lógica y asegurar que todas se ejecuten de manera exitosa o que ninguna se ejecute en caso de error. Tanto **Savepoint** como **Rollback** están destinados a proporcionar un mecanismo para deshacer una parte de una transacción en caso de que ocurra un error.

### Savepoint:

Un **Savepoint** es un punto de referencia dentro de una transacción donde se puede marcar el estado de la transacción antes de realizar una serie de operaciones.

- Se puede utilizar para guardar el estado de una transacción y permitir que la transacción vuelva a ese estado si algo sale mal más adelante en la secuencia de operaciones.
- **Savepoint** es especialmente útil cuando deseas realizar una serie de operaciones dentro de una transacción y deshacer solo una parte de esas operaciones sin deshacer toda la transacción.

**Métodos relacionados con Savepoint:**

- `Savepoint setSavepoint()`: Este método crea un nuevo **Savepoint** en el contexto de la transacción actual. Devuelve un objeto **Savepoint** que representa el punto de referencia.

- `Savepoint setSavepoint(String name)`: Este método crea un nuevo **Savepoint** con un nombre específico en el contexto de la transacción actual. Devuelve un objeto **Savepoint** que representa el punto de referencia.

### Rollback:

**Rollback** es una operación que deshace una transacción y restaura el estado de la base de datos a un punto anterior definido por un **Savepoint**.

- Si ocurre un error o algún problema dentro de una transacción, puedes llamar a `rollback()` para deshacer todas las operaciones realizadas desde el último **Savepoint** o desde el inicio de la transacción si no se han establecido **Savepoints**.

**Métodos relacionados con Rollback:**

- `void rollback()`: Este método deshace todas las operaciones realizadas desde el último **Savepoint** o desde el inicio de la transacción si no se han establecido **Savepoints**. Se utiliza para deshacer toda la transacción en caso de un error.

- `void rollback(Savepoint savepoint)`: Este método deshace todas las operaciones realizadas desde el **Savepoint** especificado. Se utiliza para deshacer solo una parte de la transacción.

Es importante tener en cuenta que el soporte para **Savepoint** y **Rollback** depende de la base de datos y del controlador JDBC que estés utilizando. 

# Commit
Los métodos commit y setAutoCommit están relacionados con la gestión de transacciones en JDBC

El método `setAutoCommit(boolean autoCommit)` se utiliza para habilitar o deshabilitar el modo de autocommit en una conexión a la base de datos.

- Cuando el modo de autocommit está habilitado (`autoCommit = true`), cada sentencia SQL individual se trata como una transacción independiente, y se confirma automáticamente una vez que se completa la ejecución de la sentencia.


- Por otro lado, cuando el modo de autocommit está deshabilitado (`autoCommit = false`), las sentencias SQL se agrupan en una transacción, y deben confirmarse explícitamente utilizando `commit()` para que los cambios sean permanentes en la base de datos.

Es importante tener en cuenta que, por defecto, el modo de autocommit está habilitado en JDBC. Esto significa que, si no se deshabilita explícitamente mediante `setAutoCommit(false)`, cada sentencia SQL se ejecutará como una transacción independiente y se confirmará automáticamente una vez que se complete su ejecución.

Es una buena práctica restaurar el valor predeterminado del modo de autocommit (`setAutoCommit(true)`) después de realizar una transacción en JDBC. De esta manera, se asegura que las operaciones posteriores a la transacción se ejecuten como transacciones independientes y se confirmen automáticamente una vez que se completen.

El motivo principal para restaurar el modo de autocommit a `true` después de una transacción es evitar que futuras operaciones no deseadas se agrupen accidentalmente en una transacción más grande.
