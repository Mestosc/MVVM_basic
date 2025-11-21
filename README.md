## Introducción

El objetivo de esta app es describir las diferentes clases y como se interrelacionan para el [modelo MVVM](https://developer.android.com/topic/libraries/architecture/viewmodel?hl=es-419)

## Escenario
Tenemos nuestra aplicación diseñada y codificada y queremos transformarla a la arquitectura MVVC, separar el manejo de datos de la activity principal.

Además utilizar el patrón de diseño [Observer](https://es.wikipedia.org/wiki/Observer_(patr%C3%B3n_de_dise%C3%B1o))

En este caso, el único dato que vamos a manejar son enteros aleatorios. 

## Corrutinas
En esta rama vamos a usar corrutinas:

- En el ViewModel con la función `estadosAuxiliares` utilizando `viewModelScope.launch { }`
- En la IU con `LaunchedEffect(_activo)` en el botón start

## Ejercicio 1
Para el ejercicio 1 he aprovechado 3 estados auxiliares que son la cuenta atras esta empezando, que esta a la mitad y que acabo
despues de cada cambio de numero a la cuenta atras compruebo si se dan las condiciones para los cambios de estado y luego verifico si el estadoAuxiliar actual es el de que la cuenta atras finalizo
cambio el estado principal actual a INICIO y como es un MutableStateFlow eso afecta directamente a la interfaz