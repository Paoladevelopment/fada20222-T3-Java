package rojinegros;

import lombok.Getter;
import lombok.Setter;

import javax.naming.OperationNotSupportedException;
import java.util.LinkedList;
import java.util.Queue;

public class ArbolRojinegro {
    @Getter
    @Setter
    private ArbolRojinegro izq;

    @Getter
    @Setter
    private ArbolRojinegro der;

    @Getter
    @Setter
    private int valor;

    @Getter
    @Setter
    private boolean black; //Si es negro True, en otro caso rojo

    @Getter
    @Setter
    private ArbolRojinegro father;


    public ArbolRojinegro(ArbolRojinegro izq,
                          ArbolRojinegro der,
                          int valor,
                          boolean black) {
        this.izq = izq;
        this.der = der;
        this.valor = valor;
        this.black = black;
    }

    public ArbolRojinegro() {
        this.izq = null;
        this.der = null;
        this.black = true;
    }
    /*
     * Metodos a implementar
     */

    public void insertar(int x) throws Exception {
        if (this.getFather() == null && this.getValor() == 0){
            this.setValor(x);
            this.setBlack(false);
            arreglarInsercion(this);
        }else{
            ArbolRojinegro nodo = this;
            ArbolRojinegro padre = null;

            while (nodo != null) {
                padre = nodo;
                if (x < nodo.getValor()) {
                    nodo = nodo.getIzq();
                } else if (x > nodo.getValor()) {
                    nodo = nodo.getDer();
                } else {
                    throw new Exception("El arbol ya contiene un nodo con el valor: " + x);
                }
            }

            ArbolRojinegro nuevoNodo = new ArbolRojinegro();
            nuevoNodo.setValor(x);
            nuevoNodo.setBlack(false);
            if (x < padre.getValor()) {
                padre.setIzq(nuevoNodo);
            } else {
                padre.setDer(nuevoNodo);
            }
            nuevoNodo.setFather(padre);
            arreglarInsercion(nuevoNodo);
        }

    }

    public int maximo() throws Exception {
        ArbolRojinegro arbActual= this;
        while (arbActual.getDer() != null){
            arbActual= arbActual.getDer();
        }
        return arbActual.getValor();
    }

    public int minimo() throws Exception {
        ArbolRojinegro arbActual= this;
        while (arbActual.getIzq() != null){
            arbActual= arbActual.getIzq();
        }
        return arbActual.getValor();
    }

    public ArbolRojinegro search(int x) throws Exception {
        if(this.getValor()== x){
            return this;
        }else{
            if(this.getValor() < x){
                if(this.getDer() !=null){
                    return this.getDer().search(x);
                }else {
                    return null;
                }

            }else{
                if (this.getIzq() !=null){
                    return this.getIzq().search(x);
                }else {
                    return null;
                }

            }
        }
    }

    public void rotacionIzquierda(int x) throws Exception {
        ArbolRojinegro arbARotar= this.search(x);
        ArbolRojinegro padre= arbARotar.getFather();
        ArbolRojinegro nuevaRaiz= arbARotar.getDer();
        nuevaRaiz.setFather(padre);
        if (padre !=null){
            arbARotar.setDer(nuevaRaiz.getIzq());
            arbARotar.setFather(nuevaRaiz);
            nuevaRaiz.setIzq(arbARotar);
            if(nuevaRaiz.getValor() < padre.getValor()){
                this.search(padre.getValor()).setIzq(nuevaRaiz);
            }else {
                this.search(padre.getValor()).setDer(nuevaRaiz);
            }
        }else{

            ArbolRojinegro nodoIzquierdo= new ArbolRojinegro(arbARotar.getIzq(), arbARotar.getDer(), arbARotar.getValor(), false);
            nodoIzquierdo.setDer(nuevaRaiz.getIzq());
            if (nuevaRaiz.getIzq() !=null){
                nuevaRaiz.getIzq().setFather(nodoIzquierdo);
            }
            nuevaRaiz.setIzq(nodoIzquierdo);
            nodoIzquierdo.setFather(nuevaRaiz);
            arbARotar.setValor(nuevaRaiz.getValor());
            arbARotar.setBlack(nuevaRaiz.isBlack());
            arbARotar.setDer(nuevaRaiz.getDer());
            arbARotar.setIzq(nodoIzquierdo);

        }


    }

    public void rotacionDerecha(int x) throws  Exception {
        ArbolRojinegro arbARotar= search(x);
        ArbolRojinegro padre= arbARotar.getFather();
        ArbolRojinegro nuevaRaiz= arbARotar.getIzq();
        nuevaRaiz.setFather(padre);
        if (padre !=null){
            arbARotar.setIzq(nuevaRaiz.getDer());
            arbARotar.setFather(nuevaRaiz);
            nuevaRaiz.setDer(arbARotar);
            if(nuevaRaiz.getValor() < padre.getValor()){
                this.search(padre.getValor()).setIzq(nuevaRaiz);
            }else {
                this.search(padre.getValor()).setDer(nuevaRaiz);
            }

        }else{
            ArbolRojinegro nodoDerecho= new ArbolRojinegro(arbARotar.getIzq(), arbARotar.getDer(), arbARotar.getValor(), arbARotar.isBlack());
            nodoDerecho.setIzq(nuevaRaiz.getDer());
            if (nuevaRaiz.getDer() !=null){
                nuevaRaiz.getDer().setFather(nodoDerecho);
            }
            nuevaRaiz.setDer(nodoDerecho);
            nodoDerecho.setFather(nuevaRaiz);
            arbARotar.setValor(nuevaRaiz.getValor());
            arbARotar.setBlack(nuevaRaiz.isBlack());
            arbARotar.setIzq(nuevaRaiz.getIzq());
            arbARotar.setDer(nodoDerecho);

        }
    }

    //Area de métodos útiles
    public ArbolRojinegro getTio(){
        ArbolRojinegro abuelo= this.getFather().getFather();
        if (abuelo !=null){
            if (this.getFather().getValor() < abuelo.getValor()){
                return abuelo.getDer();
            }else {
                return abuelo.getIzq();
            }
        }else{
            return null;
        }
    }

    public ArbolRojinegro getAbuelo(){
        return this.getFather().getFather();
    }

    public void arreglarInsercion(ArbolRojinegro nodoAgregado) throws Exception{
        ArbolRojinegro padre= nodoAgregado.getFather();
        if (padre == null) {
            // Regla 2: La raiz siempre debe ser negra
            nodoAgregado.black = true;
            return;
        }
        // Si el padre es de color negro, no hay que hacer nada
        if (padre.isBlack()) {
            return;
        }
        ArbolRojinegro tio= nodoAgregado.getTio();
        ArbolRojinegro abuelo= nodoAgregado.getAbuelo();
        while (!nodoAgregado.getFather().isBlack()){
            if (!nodoAgregado.isBlack() && !nodoAgregado.getFather().isBlack() && tio ==null){
                if (nodoAgregado== nodoAgregado.getFather().getIzq() && nodoAgregado.getFather()== nodoAgregado.getAbuelo().getIzq()){
                    nodoAgregado.getAbuelo().setBlack(false);
                    nodoAgregado.getFather().setBlack(true);
                    this.rotacionDerecha(abuelo.getValor());


                } else if (nodoAgregado== nodoAgregado.getFather().getDer() && nodoAgregado.getFather()==nodoAgregado.getAbuelo().getDer()){
                    nodoAgregado.getAbuelo().setBlack(false);
                    nodoAgregado.getFather().setBlack(true);
                    this.rotacionIzquierda(abuelo.getValor());

                } else if (nodoAgregado== nodoAgregado.getFather().getIzq() && nodoAgregado.getFather() == nodoAgregado.getAbuelo().getDer()) {
                    nodoAgregado= nodoAgregado.getFather();
                    this.rotacionDerecha(padre.getValor());


                } else if (nodoAgregado== nodoAgregado.getFather().getDer() && nodoAgregado.getFather() == nodoAgregado.getAbuelo().getIzq()) {
                    nodoAgregado= nodoAgregado.getFather();
                    this.rotacionIzquierda(padre.getValor());
                }

            } else {
                if (!padre.isBlack() && !tio.isBlack()) {
                    padre.setBlack(true);
                    tio.setBlack(true);
                    abuelo.setBlack(false);
                    nodoAgregado = nodoAgregado.getAbuelo();
                    if (nodoAgregado.getFather() == null){
                        break;
                    }
                }else {
                    nodoAgregado= this.search(nodoAgregado.getValor());
                    if (nodoAgregado== nodoAgregado.getFather().getIzq() && nodoAgregado.getFather()== nodoAgregado.getAbuelo().getIzq()){
                        nodoAgregado.getAbuelo().setBlack(false);
                        nodoAgregado.getFather().setBlack(true);
                        this.rotacionDerecha(abuelo.getValor());


                    } else if (nodoAgregado== nodoAgregado.getFather().getDer() && nodoAgregado.getFather()==nodoAgregado.getAbuelo().getDer()){
                        nodoAgregado.getAbuelo().setBlack(false);
                        nodoAgregado.getFather().setBlack(true);
                        this.rotacionIzquierda(abuelo.getValor());
                    } else if (nodoAgregado== nodoAgregado.getFather().getIzq() && nodoAgregado.getFather() == nodoAgregado.getAbuelo().getDer()) {
                        nodoAgregado= nodoAgregado.getFather();
                        this.rotacionDerecha(padre.getValor());


                    } else if (nodoAgregado== nodoAgregado.getFather().getDer() && nodoAgregado.getFather() == nodoAgregado.getAbuelo().getIzq()) {
                        nodoAgregado= nodoAgregado.getFather();
                        this.rotacionIzquierda(padre.getValor());
                    }else{
                        break;
                    }
                }
            }
        }

        if (nodoAgregado != null){
            while (nodoAgregado.getFather() !=null){
                nodoAgregado=nodoAgregado.getFather();
            }
            ArbolRojinegro raiz= nodoAgregado;
            raiz.setBlack(true);
        }

    }



    /*
     *  Area de pruebas, no modificar.
     */
    //Verificaciones
    /*
     * Busqueda por amplitud para verificar arbol.
     */
    public String bfs() {
        String salida = "";
        String separador = "";
        Queue<ArbolRojinegro> cola = new LinkedList<>();
        cola.add(this);
        while (cola.size() > 0) {
            ArbolRojinegro nodo = cola.poll();
            salida += separador + String.valueOf(nodo.getValor());
            separador = " ";
            if (nodo.getIzq() != null) {
                cola.add(nodo.getIzq());
            }
            if (nodo.getDer() != null) {
                cola.add(nodo.getDer());
            }
        }
        return salida;
    }

    /*
     * Recorrido inorder.
     * Verifica propiedad de orden.
     */
    public String inorden() {
        String recorrido = "";
        String separador = "";
        if (this.getIzq() != null) {
            recorrido += this.getIzq().inorden();
            separador = " ";
        }
        recorrido += separador + String.valueOf(this.getValor());
        if (this.getDer() != null) {
            recorrido += " " + this.getDer().inorden();
        }
        return recorrido;
    }

}