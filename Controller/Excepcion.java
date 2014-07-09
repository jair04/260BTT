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
public class Excepcion extends Exception {
     public Excepcion() {}

     public Excepcion(String msg){
        super(msg);
     }
}
