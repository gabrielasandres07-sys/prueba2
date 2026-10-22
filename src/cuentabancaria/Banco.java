/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cuentabancaria;

/**
 *
 * @author USER
 */
public class Banco {

    private CuentaBancaria[] cuentas = new CuentaBancaria[20];
    private int cantidadCuentas = 0;

    public void agregarCuenta(CuentaBancaria c) {
        if (c == null) {
            throw new IllegalArgumentException("La cuenta no puede ser nula");
        }
        if (cantidadCuentas >= cuentas.length) {
            throw new IllegalStateException("El banco alcanzo el maximo de " + cuentas.length + " cuentas");
        }
        if (existeAux(c.getNumero(), 0)) {
            throw new IllegalArgumentException("Ya existe una cuenta con el numero " + c.getNumero());
        }
        cuentas[cantidadCuentas] = c;
        cantidadCuentas++;
    }

    

    public CuentaBancaria buscarPorNumero(int numero) {
        return buscarAux(numero, 0);
    }

    private CuentaBancaria buscarAux(int numero, int indice) {
        if (indice >= cantidadCuentas) {
            throw new IllegalArgumentException("No existe una cuenta con el numero " + numero);
        }
        if (cuentas[indice].getNumero() == numero) {
            return cuentas[indice];
        }
        return buscarAux(numero, indice + 1);
    }

    private boolean existeAux(int numero, int indice) {
        if (indice >= cantidadCuentas) {
            return false;
        }
        if (cuentas[indice].getNumero() == numero) {
            return true;
        }
        return existeAux(numero, indice + 1);
    }

    

    public void ordenarPorSaldo() {
        ordenarAux(cantidadCuentas);
    }

    
    private void ordenarAux(int n) {
        if (n <= 1) {
            return;
        }
        pasadaBurbuja(0, n);
        ordenarAux(n - 1);
    }

    private void pasadaBurbuja(int i, int n) {
        if (i >= n - 1) {
            return;
        }
        if (cuentas[i].getSaldo() > cuentas[i + 1].getSaldo()) {
            CuentaBancaria temp = cuentas[i];
            cuentas[i] = cuentas[i + 1];
            cuentas[i + 1] = temp;
        }
        pasadaBurbuja(i + 1, n);
    }

   

    public void transferir(int numeroOrigen, int numeroDestino, double monto) {
        if (numeroOrigen == numeroDestino) {
            throw new IllegalArgumentException("La cuenta origen y destino no pueden ser la misma");
        }
        CuentaBancaria origen = buscarPorNumero(numeroOrigen);
        CuentaBancaria destino = buscarPorNumero(numeroDestino);

        if (!origen.retirar(monto)) {
            throw new IllegalStateException("Fondos insuficientes en la cuenta origen " + numeroOrigen);
        }
        try {
            destino.depositar(monto);
        } catch (IllegalArgumentException | IllegalStateException e) {
           
            origen.reintegrar(monto);
            System.out.println("Transferencia revertida: se reintegraron " + monto + " a la cuenta " + numeroOrigen);
            throw e;
        }
    }

    

    public void aplicarInteresesGlobal() {
        for (int i = 0; i < cantidadCuentas; i++) {
            try {
                cuentas[i].registrarIntereses();
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("No se aplicaron intereses a la cuenta "
                        + cuentas[i].getNumero() + ": " + e.getMessage());
            }
        }
    }

   

    public double saldoTotalDelBanco() {
        return sumaAux(0);
    }

    private double sumaAux(int indice) {
        if (indice >= cantidadCuentas) {
            return 0;
        }
        return cuentas[indice].getSaldo() + sumaAux(indice + 1);
    }

    

    public void generarReporte() {
        ordenarPorSaldo();
        System.out.println("===== REPORTE DE CUENTAS (menor a mayor saldo) =====");
        if (cantidadCuentas == 0) {
            System.out.println("(no hay cuentas registradas)");
        }
        for (int i = 0; i < cantidadCuentas; i++) {
            System.out.println(cuentas[i].toString());
        }
    }

    public int getCantidadCuentas() {
        return cantidadCuentas;
    }
}

