# Consulta a BD usando hilos

A veces una consulta a BD demora demasiado, en ese caso, podemos evaluar si realizando consultas por páginas mejoramos el rendimiento.

> La idea de este ejercicio es
> mejorar el tiempo de respuesta final
> de una consulta de tamaño considerable
> realizando en paralelo consultas de menor tamaño.

### Procedimiento

- Definimos un conveniente tamaño de página.
- Obtenemos el total de elementos.
- Calculamos la cantidad de páginas.
- Obtenemos las páginas.
- Completamos el resultado.

### Código

Utilizamos el API de concurrencia de Java, específicamente CompletableFuture que nos permite realizar pedidos asincrónicos, uno por cada página, y esperar la finalización de todos ellos.

```
futures[i] = CompletableFuture.runAsync(() -> { records.addAll( ... ); }, executor);
...
CompletableFuture.allOf(futures).join();
```

En el código, abordamos una situación de ejemplo, 100k bebés nacidos de una región de un año determinado, en BD, cada uno con nombre y fecha.

### Observación

Es importante considerar que esta solución se podría utilizar en otras situaciones donde el resultado completo esté paginado, por ejemplo, en un API Rest que devuelve páginas de un cierto elemento.

 