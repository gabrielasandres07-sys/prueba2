/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cuentabancaria;

/**
 *
 * @author USER
 */
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final Banco banco = new Banco();

    public static void main(String[] args) {
        int opcion = 0;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");
            
            try {
                switch (opcion) {
                    case 1: crearCuenta(); break;
                    case 2: depositar(); break;
                    case 3: retirar(); break;
                    case 4: transferir(); break;
                    case 5: aplicarIntereses(); break;
                    case 6: verReporte(); break;
                    case 7: verHistorial(); break;
                    case 8: girarCheque(); break;
                    case 9: System.out.println("Gracias por usar Finanzas del Valle. Hasta pronto."); break;
                    default: System.out.println("Opcion no valida, intente de nuevo.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR (dato invalido): " + e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println("ERROR (operacion no permitida): " + e.getMessage());
            }
        } while (opcion != 9);
        
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("====== BANCO FINANZAS DEL VALLE ======");
        System.out.println("1. Crear cuenta");
        System.out.println("2. Depositar");
        System.out.println("3. Retirar");
        System.out.println("4. Transferir");
        System.out.println("5. Aplicar intereses a todas las cuentas");
        System.out.println("6. Ver reporte ordenado y saldo total");
        System.out.println("7. Ver historial de una cuenta");
        System.out.println("8. Girar cheque");
        System.out.println("9. Salir");
    }

    

    private static void crearCuenta() {
        System.out.println("Tipo de cuenta: 1) Ahorro  2) Cheques  3) Plazo Fijo");
        int tipo = leerEntero("Tipo: ");
        if (tipo < 1 || tipo > 3) {
            throw new IllegalArgumentException("Tipo de cuenta no valido: " + tipo);
        }
        int numero = leerEntero("Numero de cuenta: ");
        String cliente = leerTexto("Nombre del cliente: ");
        String moneda = leerTexto("Moneda (ej. USD, CRC): ");

        CuentaBancaria cuenta;
        if (tipo == 1) {
            cuenta = new CuentaAhorro(numero, cliente, moneda);
        } else if (tipo == 2) {
            double limite = leerDouble("Limite de sobregiro: ");
            cuenta = new CuentaCheques(numero, cliente, moneda, limite);
        } else {
            CuentaPlazoFijo plazo = new CuentaPlazoFijo(numero, cliente, moneda);
            System.out.println("Fecha de vencimiento del plazo:");
            int anio = leerEntero("  Anio: ");
            int mes = leerEntero("  Mes (1-12): ");
            int dia = leerEntero("  Dia: ");
            plazo.setPlazo(anio, mes, dia);
            cuenta = plazo;
        }

        banco.agregarCuenta(cuenta);
        System.out.print("Cuenta creada con exito. ");
        cuenta.quienSoy();
    }

    private static void depositar() {
        CuentaBancaria c = banco.buscarPorNumero(leerEntero("Numero de cuenta: "));
        double monto = leerDouble("Monto a depositar: ");
        c.depositar(monto);
        System.out.println("Deposito realizado. Nuevo saldo: " + c.getSaldo());
    }

    private static void retirar() {
        CuentaBancaria c = banco.buscarPorNumero(leerEntero("Numero de cuenta: "));
        double monto = leerDouble("Monto a retirar: ");
        if (c.retirar(monto)) {
            System.out.println("Retiro realizado. Nuevo saldo: " + c.getSaldo());
        } else {
            System.out.println("No se pudo retirar: fondos insuficientes (o intereses insuficientes en plazo fijo vigente).");
        }
    }

    private static void transferir() {
        int origen = leerEntero("Cuenta origen: ");
        int destino = leerEntero("Cuenta destino: ");
        double monto = leerDouble("Monto a transferir: ");
        banco.transferir(origen, destino, monto);
        System.out.println("Transferencia de " + monto + " de la cuenta " + origen
                + " a la cuenta " + destino + " realizada con exito.");
    }

    private static void aplicarIntereses() {
        banco.aplicarInteresesGlobal();
        System.out.println("Intereses aplicados a todas las cuentas.");
    }

    private static void verReporte() {
        banco.generarReporte();
        System.out.println("Saldo total del banco: " + banco.saldoTotalDelBanco());
    }

    private static void verHistorial() {
        CuentaBancaria c = banco.buscarPorNumero(leerEntero("Numero de cuenta: "));
        c.quienSoy();
        c.imprimirHistorial();
    }

    private static void girarCheque() {
        CuentaBancaria c = banco.buscarPorNumero(leerEntero("Numero de cuenta: "));
        if (!(c instanceof CuentaCheques)) {
            throw new IllegalStateException("La cuenta " + c.getNumero() + " no es de tipo Cheques");
        }
        double monto = leerDouble("Monto del cheque: ");
        CuentaCheques cheques = (CuentaCheques) c;
        cheques.girarCheque(monto);
        System.out.println("Cheque girado. Nuevo saldo: " + cheques.getSaldo()
                + " | Sobregiro disponible: " + cheques.sobregiroDisponible());
    }

    

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = leerLinea();
            try {
                return Integer.parseInt(linea.trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero entero valido.");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = leerLinea();
            try {
                return Double.parseDouble(linea.trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero valido.");
            }
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return leerLinea();
    }

   
    private static String leerLinea() {
        if (!sc.hasNextLine()) {
            System.out.println();
            System.out.println("Fin de la entrada. Saliendo.");
            System.exit(0);
        }
        return sc.nextLine();
    }
}

