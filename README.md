# Sobre la applicacion

> Autor: wenxiang wang  
> Clase: Curso 2021-2022 - XI Edición  
> Proyecto: Bantumi  
> Fecha: 04/11/2021  

## Senderos de applicacion

```
|-  activity // activity
|- component // componente de Interfaz de usuario
|- db // Dao, VO y Database de Room
|- entity // entity
|- model // model
|- util // herramientas class
-  AppAlertDialog.java // Diálogo de alerta
-  MainActivity.java // Main File
```

## Funciónes

- Reiniciar partida: al pulsar el botón "Reset" se mostrará un diálogo de confirmación. En caso de respuesta afirmativa se procederá a reiniciar la partida actual.
- Guardar partida: esta opción permite guardar la situación actual del tablero. Sólo es necesario guardar una única partida y se empleará el sistema de ficheros del dispositivo.
- Recuperar partida: si existe, recupera el estado de una partida guardada (si se hubiera modificado la partida actual, se solicitará confirmación).
- Guardar puntuación: al finalizar cada partida se deberá guardar la información necesaria para generar un listado de resultados. Dicho listado deberá incluir -al menos- el nombre del jugador (se obtendrá de los ajustes del juego), el día y hora de la partida y el número de semillas que quedaron en cada almacén. La información se deberá guardar en una base de datos.
- Mejores resultados: esta opción mostrará el histórico con los diez mejores resultados obtenidos ordenados por el mayor número de semillas obtenido por cualquier jugador. Además, incluirá una opción -con confirmación- para eliminar todos los resultados guardados.