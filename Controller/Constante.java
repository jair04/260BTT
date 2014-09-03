/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

/**
 *
 * @author Jair
 */
public interface Constante {
    
    //Definir el tipo de mensaje que se recibe y/o envia 
    final int MSN_COLA = 0;  
    final int MSN_TEMA = 1;  
    
    //Define el tipo de peticion que realiza el comando o la aeronave
     final int CONECTAR_AERONAVE = 2; 
     final int RECHAZAR_AERONAVE = 3;
     final int ACEPTAR_AERONAVE = 4;
     final int ACTUALIZAR_AERONAVE = 5;
     final String NOMBRE_TEMA = "FAM_MISION";
     final String NOMBRE_COLA = "FAM_MISION_COLA";
     final int RESPUESTA_PETICION_CONEXION = 6;
     final int MISION_ACTUALIZADA=7;
}
