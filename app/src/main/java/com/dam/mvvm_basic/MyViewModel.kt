package com.dam.mvvm_basic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MyViewModel(): ViewModel() {

    // etiqueta para logcat
    private val TAG_LOG = "miDebug"

    // estados del juego
    // usamos LiveData para que la IU se actualice
    // patron de diseño observer
    val estadoActual = MutableStateFlow(Estados.INICIO)

    // este va a ser nuestra lista para la secuencia random
    // usamos mutable, ya que la queremos modificar
    var _numbers = MutableStateFlow(0)
    var numeroCuentaAtras = MutableStateFlow(5)
    private var estadoAuxiliar = EstadosAuxiliares.EMPEZANDO_CUENTA_ATRAS
    // inicializamos variables cuando instanciamos
    init {
        // estado inicial
        Log.d(TAG_LOG, "Inicializamos ViewModel - Estado: ${estadoActual.value}")
    }

    /**
     * crear entero random
     */
    fun crearRandom() {
        // cambiamos estado, por lo tanto la IU se actualiza
        estadoActual.value = Estados.GENERANDO
        _numbers.value = (0..3).random()
        Log.d(TAG_LOG, "creamos random ${_numbers.value} - Estado: ${estadoActual.value}")
        actualizarNumero(_numbers.value)
    }

    fun actualizarNumero(numero: Int) {
        Log.d(TAG_LOG, "actualizamos numero en Datos - Estado: ${estadoActual.value}")
        Datos.numero = numero
        // cambiamos estado, por lo tanto la IU se actualiza
        estadoActual.value = Estados.ADIVINANDO
        cuentaAtrasManejar()
    }

    /**
     * comprobar si el boton pulsado es el correcto
     * @param ordinal: Int numero de boton pulsado
     * @return Boolean si coincide TRUE, si no FALSE
     */
    fun comprobar(ordinal: Int): Boolean {
        Log.d(TAG_LOG, "comprobamos - Estado: ${estadoActual.value}")
        return if (ordinal == Datos.numero) {
            Log.d(TAG_LOG, "es correcto")
            estadoActual.value = Estados.INICIO
            Log.d(TAG_LOG, "GANAMOS - Estado: ${estadoActual.value}")
            //lanzamos estados auxiliares en paralelo
            estadosAuxiliares("Ganador")
            true
        } else {
            Log.d(TAG_LOG, "no es correcto")
            estadoActual.value = Estados.ADIVINANDO
            Log.d(TAG_LOG, "otro intento - Estado: ${estadoActual.value}")
            //lanzamos estados auxiliares en paralelo
            estadosAuxiliares("Fallo")
            false
        }
    }
    fun cuentaAtrasManejar() {
        viewModelScope.launch {
            for (i in 0..10) {
                if (numeroCuentaAtras.value>0) {
                numeroCuentaAtras.value -= 1
                }
                if (numeroCuentaAtras.value==5) {
                    estadoAuxiliar = EstadosAuxiliares.MITAD_CUENTA_ATRAS
                }
                if (numeroCuentaAtras.value==0) {
                    estadoAuxiliar = EstadosAuxiliares.FINALIZANDO_CUENTA_ATRAS
                }
                comprobarAcitvo()
                delay(1000)
            }
        }
    }

    /**
     * Comprobar si el estado es que permite actividad en el juego
     */
    fun comprobarAcitvo() {
        if (estadoAuxiliar== EstadosAuxiliares.FINALIZANDO_CUENTA_ATRAS) {
            estadoActual.value = Estados.INICIO
        }
    }
    /**
     * Corutina que lanza estados auxiliares
     */
    fun estadosAuxiliares(msg: String = "") {
        viewModelScope.launch {
            // inicializamos estado auxiliar
            // los recorremos
            var estadoAux = EstadosAuxiliares.EMPEZANDO_CUENTA_ATRAS
            Log.d(TAG_LOG, "estado (corutina): ${estadoAux}")
            Log.d(TAG_LOG, "mensaje (corutina): ${msg}")
            delay(1500)
            estadoAux = EstadosAuxiliares.MITAD_CUENTA_ATRAS
            Log.d(TAG_LOG, "estado (corutina): ${estadoAux}")
            Log.d(TAG_LOG, "mensaje (corutina): ${msg}")
            delay(1500)
            estadoAux = EstadosAuxiliares.FINALIZANDO_CUENTA_ATRAS
            Log.d(TAG_LOG, "estado (corutina): ${estadoAux}")
            Log.d(TAG_LOG, "mensaje (corutina): ${msg}")
            delay(1500)
        }
    }
}