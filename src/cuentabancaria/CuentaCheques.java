/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cuentabancaria;

/**
 *
 * @author USER
 */
public class CuentaCheques extends CuentaBancaria {

    private double limiteSobregiro;

    public CuentaCheques(int numero, String cliente, String moneda, double limiteSobregiro) {
        super(numero, cliente, moneda, TipoCuenta.CHEQUES);
        if (limiteSobregiro < 0) {
            throw new IllegalArgumentException("El limite de sobregiro no puede ser negativo");
        }
        this.limiteSobregiro = limiteSobregiro;
    }

    
    public double sobregiroDisponible() {
        return saldo >= 0 ? limiteSobregiro : limiteSobregiro + saldo;
    }

  
    public void girarCheque(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto del cheque debe ser mayor que cero (recibido: " + monto + ")");
        }
        if (monto > saldo + limiteSobregiro) {
            throw new IllegalStateException("Fondos insuficientes para girar el cheque");
        }
        saldo -= monto;
        registrarMovimiento("CHEQUE -" + monto + " -> saldo: " + saldo);
    }

    @Override
    public void quienSoy() {
        System.out.println("Soy una CuentaCheques");
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Limite sobregiro: " + limiteSobregiro
                + " | Sobregiro disponible: " + sobregiroDisponible();
    }
}