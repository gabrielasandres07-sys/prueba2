/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cuentabancaria;

/**
 *
 * @author USER
 */
import java.util.Calendar;
public class CuentaBancaria {
    protected int numero;
    protected double saldo;
    protected String cliente;
    protected String moneda;
    protected TipoCuenta tipo;
    protected Calendar apertura;
    
    protected String[]historial=new String[50];
    protected int cantidadMovimientos;
    
    public CuentaBancaria(int numero, String cliente, String moneda){
        this.numero=numero;
        this.cliente=cliente;
        this.moneda=moneda;
        this.saldo=0;
        this.apertura=Calendar.getInstance();
        this.tipo=TipoCuenta.CHEQUES;
                
    }
    
    public CuentaBancaria(int numero, String cliente, String moneda,
                          TipoCuenta tipo) {
        this.numero = numero;
        this.cliente = cliente;
        this.moneda = moneda;
        this.saldo = 0;
        this.apertura = Calendar.getInstance();
        this.tipo = tipo;
    }
    public int getNumero() {
        return numero;
    }

    public double getSaldo() {
        return saldo;
    }
    
    public void depositar(double monto){
        if(monto<=0){
            throw new IllegalArgumentException("El monto a depositar debe ser mayor que cero");
        }
        saldo=saldo+monto;
        registrarMovimiento("DEPOSITO +" + monto + " -> saldo: " + saldo);
        
    }
    public boolean retirar(double monto){
        if(monto<=0){
            throw new IllegalArgumentException("El monto a retirar debe ser mayor que cero");
            
        }
        if(monto>saldo){
            return false;
        }
        saldo=saldo-monto;
        registrarMovimiento("RETIRO -" + monto + " -> saldo: " + saldo);
        return true;
        
    }
    public void reintegrar(double monto){
        if(monto<=0){
            throw new IllegalArgumentException("El monto a reintegrar debe ser mayor que cero");
        }
        saldo=saldo+monto;
        registrarMovimiento("REVERSA +" + monto + " -> saldo: " + saldo);
    }
    public void registrarIntereses(){
        double interes=saldo*tipo.tasa();
        saldo=saldo+interes;
        if(interes!=0){
            registrarMovimiento("INTERESES +" + interes + " -> saldo: " + saldo);
        }
    }
    
    public void registrarMovimiento(String linea) {
        try {
            if (cantidadMovimientos >= historial.length) {
                throw new IllegalStateException("Historial lleno, no se pudo registrar: " + linea);
            }
            historial[cantidadMovimientos] = linea;
            cantidadMovimientos++;
        } catch (IllegalStateException e) {
            System.out.println("AVISO: " + e.getMessage());
        }
    }
    
    public void imprimirHistorial(){
        System.out.println("Historial de la cuenta "+ numero + ":");
        if (cantidadMovimientos == 0) {
            System.out.println("(sin movimientos)");
        }
        for (int i = 0; i < cantidadMovimientos; i++) {
            System.out.println((i + 1) + ". " + historial[i]);
        }
    }
    
    public void quienSoy(){
        System.out.println("CuentaBancaria (clase base)");
    }
    
    public String toString(){
        return "Cuenta #" + numero
                + " | Cliente: " + cliente
                + " | Saldo: " + saldo + " " + moneda
                + " | Tipo: " + tipo
                + " | Apertura: " + apertura.getTime();
    }
    

    
    
}
