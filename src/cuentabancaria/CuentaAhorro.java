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

public class CuentaAhorro extends CuentaBancaria {

    private Calendar ultimoUso;

    public CuentaAhorro(int numero, String cliente, String moneda) {
        super(numero, cliente, moneda, TipoCuenta.AHORRO);
        ultimoUso = Calendar.getInstance();
    }

  
    public boolean estaActiva() {
        Calendar limite = Calendar.getInstance();
        limite.add(Calendar.MONTH, -6);
        return !ultimoUso.before(limite);
    }

 
    public void setUltimoUso(Calendar ultimoUso) {
        if (ultimoUso == null) {
            throw new IllegalArgumentException("La fecha de ultimo uso no puede ser nula");
        }
        this.ultimoUso = ultimoUso;
    }

    private void validarActiva() {
        if (!estaActiva()) {
            throw new IllegalStateException("La cuenta de ahorro " + numero+ " esta inactiva (sin movimientos en los ultimos 6 meses)");
        }
    }

    @Override
    public void depositar(double monto) {
        validarActiva();
        super.depositar(monto);
        ultimoUso = Calendar.getInstance();
    }

    @Override
    public boolean retirar(double monto) {
        validarActiva();
        boolean exito = super.retirar(monto);
        if (exito) {
            ultimoUso = Calendar.getInstance();
        }
        return exito;
    }

    @Override
    public void quienSoy() {
        System.out.println("Soy una CuentaAhorro");
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Ultimo uso: " + ultimoUso.getTime()
                + " | Estado: " + (estaActiva() ? "ACTIVA" : "INACTIVA");
    }
}
