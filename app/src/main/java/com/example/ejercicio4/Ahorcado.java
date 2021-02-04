package com.example.ejercicio4;

public class Ahorcado {
    private String palabraSecreta;
    private String categoria;
    private StringBuilder palabra;
    private int errores;
    private static int victorias = 0;

    public Ahorcado(String palabraSecreta, String categoria){
        this.palabraSecreta = palabraSecreta;
        this.categoria = categoria;
        this.errores = 0;
        this.inicializar(palabraSecreta.length());
    }

    public String getPalabraSecreta() {
        return palabraSecreta;
    }

    public void setPalabraSecreta(String palabraSecreta) {
        this.palabraSecreta = palabraSecreta;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPalabra() {
        return palabra.toString();
    }

    public void setPalabra(String palabra) {
        this.palabra = new StringBuilder(palabra);
    }

    public int getErrores() {
        return errores;
    }

    public void setErrores(int errores) {
        this.errores = errores;
    }

    public static int getVictorias(){
        return Ahorcado.victorias;
    }

    //Metodo para inicializar la cadena que se muestra en partida
    public void inicializar(int tamanio){
        //Crea objeto StringBuilder con tamaño dado
        this.palabra = new StringBuilder(tamanio);
        for(int i = 0; i<tamanio; i++){
            this.palabra.append("_");
        }
    }

    //Metodo para validar letra ingresada
    public boolean validar(char c){
        boolean resultado = false;
        for (int i=0; i<this.palabraSecreta.length(); i++){
            if(this.palabraSecreta.charAt(i) == c){
                this.palabra.setCharAt(i, c);
                resultado  = true;
            }
        }
        if(!resultado){
            //No se encontró la letra, aumenta contador de errores
            this.errores++;
        }
        return resultado;
    }

    //Metodo para validar si se ha ganado la partida
    public boolean victoria(){
        if(this.palabraSecreta.equals(this.getPalabra())){
            //Las palabras son iguales, partida ganada
            Ahorcado.victorias++;
            return true;
        }
        return false;
    }

    //Metodo para validar si se ha perdido la partida
    public boolean derrota(){
        if(this.errores == 6){
            //Máximo de errores alcanzado, reinicia contador de victorias
            Ahorcado.victorias = 0;
            return true;
        }
        return false;
    }
}
