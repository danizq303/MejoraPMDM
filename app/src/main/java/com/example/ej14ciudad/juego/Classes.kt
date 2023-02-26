package com.example.ej14ciudad.juego
import kotlin.random.Random

class Dado {

    private var numMin = 1
    private var numMax = 6

    fun tirada(): Int {
        return Random.nextInt(numMin, numMax)
    }
}

class Articulo(private var id: String) {

    private var peso: Int = 5
    private var valor: Int = 10
    private var vida: Int = 20

    constructor(id: String, valor: Int) : this(id) {
        this.valor = valor
    }

    fun getPeso(): Int {
        return peso
    }

    fun getValor(): Int {
        return valor
    }

    fun getVida(): Int {
        return vida
    }

    fun getId(): String {
        return id
    }

    override fun toString(): String {
        return "[ID:$id, Peso:$peso, Valor:$valor]"
    }
}

class Mochila(private var pesoMochila: Int) {
    private var contenido = ArrayList<Articulo>()

    fun getPesoMochila(): Int {
        return pesoMochila
    }

    fun addArticulo(articulo: Articulo) {
        if (articulo.getPeso() <= pesoMochila) {
            contenido.add(articulo)
            this.pesoMochila -= articulo.getPeso()
        } else {
            println("La mochila está llena, debes vender artículos")
        }
        println("Peso restante de la Mochila: " + pesoMochila)

    }

    fun getContenido(): ArrayList<Articulo> {
        return contenido
    }

    fun findObjeto(id: String): Int {
        for ((indice, item) in contenido.withIndex()) {
            if (item.getId() == id) {
                return indice
            }
        }
        return -1
    }
}

class Personaje(
    private var nombre: String,
    private var pesoMochila: Int,
    private var estadoVital: String,
    private var raza: String,
    private var clase: String,
) {

    private var mochila = Mochila(pesoMochila)
    var monedero = HashMap<Int, Int>()

    //Nuevos atributos
    var fuerza = 0
    var destreza = 0
    var constitucion = 0
    var inteligencia = 0
    var sabiduria = 0
    var carisma = 0
    var vida = 1000
    var defensa = (1..5).random()

    var isPlaying = false
    var battallasGanadas = 2

    init {
        monedero.put(1, 5)
        monedero.put(5, 5)
        monedero.put(10, 5)
        monedero.put(25, 5)
        monedero.put(100, 5)

        //Iniciamos los atributos en funcion de se clase
        changeInitAttr()

        //Calculamos los valores de los distintos atributos
        fuerza = calcularAtributos()
        destreza = calcularAtributos()
        constitucion = calcularAtributos()
        inteligencia = calcularAtributos()
        sabiduria = calcularAtributos()
        carisma = calcularAtributos()

        //Calculamos la vida en funcion de la constitucion
        //calcularVidaInicial()

        for (i in 0..5) {
            mochila.addArticulo(Articulo("BaseObjeto"))
        }
    }

    constructor() : this( "Jugador", 100, "Vivo", "Humano", "Guerrero")


    private fun calcularVidaInicial() {
        vida = (10 + (constitucion - 10))
    }

    private fun changeInitAttr() {
        when (raza) {
            "Humano" -> {
                val extra = 5
                fuerza += extra
                destreza += extra
                constitucion += extra
                inteligencia += extra
            }

            "Elfo" -> {
                val extra = 7
                sabiduria += extra
                destreza += extra
                inteligencia += extra
            }

            "Enano" -> {
                val extra = 10
                fuerza += extra
                constitucion += extra
                destreza += extra
            }

            "Goblin" -> {
                val extra = 8
                destreza += extra
                fuerza += extra
                carisma += extra
            }
        }
    }

    private fun calcularAtributos(): Int {
        val dados = mutableListOf<Int>()

        repeat(4) {
            dados.add(Dado().tirada())
        }

        dados.sortByDescending { it }
        var suma = 0

        for (i in 0..3) {
            suma += dados[i]
        }

        return suma
    }

    fun getNombre(): String {
        return nombre
    }

    fun setNombre(nombre: String) {
        this.nombre = nombre
    }

    fun getEstadoVital(): String {
        return estadoVital
    }

    fun setEstadoVital(estadoVital: String) {
        this.estadoVital = estadoVital
    }

    fun getRaza(): String {
        return raza
    }

    fun setRaza(raza: String) {
        this.raza = raza
    }

    fun getClase(): String {
        return clase
    }

    fun setClase(clase: String) {
        this.clase = clase
    }

    fun getMochila(): Mochila {
        return this.mochila
    }

    fun cifrado(mensaje: String, ROT: Int): String {
        val abecedario: ArrayList<Char> = "abcdefghijklmnñopqrstuvwxyz".toList() as ArrayList<Char>
        var stringInv = ""
        var indice = 0

        for (i in mensaje.lowercase().toList() as ArrayList<Char>) {
            if (!i.isLetter() || i.isWhitespace()) {
                stringInv += i
            } else {
                indice = abecedario.indexOf(i) + ROT
                if (indice >= abecedario.size) {
                    indice -= abecedario.size
                    stringInv += abecedario[indice]
                } else
                    stringInv += abecedario[indice]
            }
        }
        return stringInv.filter { !it.isWhitespace() && it.isLetter() }
    }

    fun comunicacion(mensaje: String) {
        var respuesta = ""
        when (estadoVital) {
            "Adulto" -> {
                //Se agrega esta condicion en todos los estados vitales para cumplicar con el enunciado
                if (mensaje.equals("Hasta la próxima luchadores"))
                    respuesta = "Un placer servirle"
                else
                    if (mensaje.equals("¿Como estas?"))
                        respuesta = "En la flor de la vida, pero me empieza a doler la espalda"
                    else
                        if ((mensaje.contains('?') || mensaje.contains('¿')) && mensaje == mensaje.uppercase())
                            respuesta = "Estoy buscando la mejor solución"
                        else
                            if (mensaje == mensaje.uppercase())
                                respuesta = "No me levantes la voz mequetrefe"
                            else
                                if (mensaje == nombre)
                                    respuesta = "¿Necesitas algo?"
                                else
                                    respuesta = "No sé de qué me estás hablando"
            }

            "Joven" -> {
                if (mensaje.equals("Hasta la próxima luchadores"))
                    respuesta = "Un placer servirle"
                else
                    if (mensaje.equals("¿Como estas?"))
                        respuesta = "De lujo"
                    else
                        if ((mensaje.contains('?') || mensaje.contains('¿')) && mensaje == mensaje.uppercase())
                            respuesta = "Tranqui se lo que hago"
                        else
                            if (mensaje == mensaje.uppercase())
                                respuesta = "Eh relájate"
                            else
                                if (mensaje == nombre)
                                    respuesta = "Qué pasa?"
                                else
                                    respuesta = "Yo que se"

            }

            "Anciano" -> {
                if (mensaje.equals("Hasta la próxima luchadores"))
                    respuesta = "Un placer servirle"
                else
                    if (mensaje.equals("¿Como estas?"))
                        respuesta = "No me puedo mover"
                    else
                        if ((mensaje.contains('?') || mensaje.contains('¿')) && mensaje == mensaje.uppercase())
                            respuesta = "Que no te escucho!"
                        else
                            if (mensaje == mensaje.uppercase())
                                respuesta = "Háblame más alto que no te escucho"
                            else
                                if (mensaje == nombre)
                                    respuesta = "Las 5 de la tarde"
                                else
                                    respuesta = "En mis tiempos esto no pasaba"
            }
        }
        when (raza) {
            "Elfo" -> println(cifrado(respuesta, 1))
            "Enano" -> println(respuesta.uppercase())
            "Goblin" -> println(cifrado(respuesta, 1))
            else -> println(respuesta)
        }
    }

    fun comprar(articulo: Articulo) {
        if (this.mochila.getPesoMochila() + articulo.getPeso() <= this.mochila.getPesoMochila()) {
            this.mochila.addArticulo(articulo)
            cashConverterInverse(articulo)
        }
    }

    fun vender2(articulo: Articulo) {
        if (this.mochila.getPesoMochila() + articulo.getPeso() <= this.mochila.getPesoMochila()) {
            this.mochila.getContenido().remove(mochila.getContenido()[mochila.getContenido().size - 1])
            cashConverter(articulo)
        }
    }

    fun vender(mercader: Personaje, articulo: Articulo) {
        var id: String
        var posicion: Int
        if (!mercader.clase.equals("Mercader"))
            print("No soy un Mercader")
        else {
            if (this.mochila.getContenido().size != 0) {
                println("Tienes ${this.mochila.getContenido().size} objetos")
                mochila.getContenido().forEach { println(it) }
                posicion = mochila.findObjeto(articulo.getId())
                if (posicion != -1) {
                    println("Movemos el artículo al mercader")
                    mercader.mochila.addArticulo(mochila.getContenido()[posicion])
                    println("Convertimos el artículo en monedas")
                    cashConverter(mochila.getContenido()[posicion])
                    println("Eliminamos el artículo del jugador principal")
                    this.mochila.getContenido().remove(mochila.getContenido()[posicion])
                } else println("No hay ningún objeto con ese ID")

            } else println("No tienes objetos para vender")

            println("Te quedan ${this.mochila.getContenido().size} objetos")
            println("Hasta pronto")
        }
    }

    fun cashConverterInverse(articulo: Articulo) {
        var total = 0
        var i = 0
        var coins = arrayListOf(1, 5, 10, 25, 100)
        coins.sortDescending()

        while (total < articulo.getValor() && i < coins.size) {
            if (total + coins[i] <= articulo.getValor()) {
                total += coins[i]
                monedero[coins[i]] = monedero[coins[i]]!! - 1
            } else
                i++
        }
    }

    fun cashConverter(articulo: Articulo) {
        var total = 0
        var i = 0
        var coins = arrayListOf(1, 5, 10, 25, 100)
        coins.sortDescending()

        while (total < articulo.getValor() && i < coins.size) {
            if (total + coins[i] <= articulo.getValor()) {
                total += coins[i]
                monedero[coins[i]] = monedero[coins[i]]!! + 1
            } else
                i++
        }
    }

}