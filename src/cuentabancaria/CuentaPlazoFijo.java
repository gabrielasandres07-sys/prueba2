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

public class CuentaPlazoFijo extends CuentaBancaria {

    private Calendar findePlazo;
    private double intereses;

    public CuentaPlazoFijo(int numero, String cliente, String moneda) {
        super(numero, cliente, moneda, TipoCuenta.PLAZO);
        findePlazo = null;
        intereses = 0;
    }

   
    public void setPlazo(int anio, int mes, int dia) {
        Calendar fecha = Calendar.getInstance();
        fecha.clear();
        fecha.setLenient(false);
        try {
            fecha.set(anio, mes - 1, dia);
            fecha.getTime(); 
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Fecha de vencimiento invalida: "+ dia + "/" + mes + "/" + anio);
        }
        findePlazo = fecha;
    }

    
    public boolean estaVencida() {
        return findePlazo != null && !Calendar.getInstance().before(findePlazo);
    }

    public double getIntereses() {
        return intereses;
    }

    
    private void capitalizarIntereses() {
        if (intereses > 0) {
            saldo += intereses;
            registrarMovimiento("CAPITALIZACION +" + intereses + " -> saldo: " + saldo);
            intereses = 0;
        }
    }

    @Override
    public boolean retirar(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser mayor que cero (recibido: " + monto + ")");
        }
        if (!estaVencida()) {
            
            if (monto > intereses) {
                return false;
            }
            intereses -= monto;
            registrarMovimiento("RETIRO INTERESES -" + monto + " -> intereses: " + intereses);
            return true;
        }
        capitalizarIntereses();
        return super.retirar(monto);
    }

    @Override
    public void registrarIntereses() {
        if (!estaVencida()) {
            double interes = saldo * tipo.tasa();
            intereses += interes;
            if (interes != 0) {
                registrarMovimiento("INTERESES ACUMULADOS +" + interes + " -> intereses: " + intereses);
            }
        } else {
            capitalizarIntereses();
            super.registrarIntereses();
        }
    }

    @Override
    public void reintegrar(double monto) {
        if (!estaVencida()) {
            if (monto <= 0) {
                throw new IllegalArgumentException("El monto a reintegrar debe ser mayor que cero");
            }
            intereses += monto;
            registrarMovimiento("REVERSA INTERESES +" + monto + " -> intereses: " + intereses);
        } else {
            super.reintegrar(monto);
        }
    }

    @Override
    public void quienSoy() {
        System.out.println("Soy una CuentaPlazoFijo");
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Intereses acumulados: " + intereses
                + " | Fin de plazo: " + (findePlazo == null ? "no definido" : findePlazo.getTime().toString())
                + " | " + (estaVencida() ? "VENCIDA" : "VIGENTE");
    }
}
