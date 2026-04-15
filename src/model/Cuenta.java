package model;

import java.math.BigInteger;

public class Cuenta {
    /* ATRIBUTOS */

    private String numeroCuenta;
    private String nombreTitular;

    /* CONSTRUCTOR */

    public Cuenta(String numeroCuenta, String nombreTitular) {
        setNumeroCuenta(numeroCuenta);
        setNombreTitular(nombreTitular);
    }

    /* GETTERS */

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getNombreTitular() {
        return nombreTitular;
    }

    /* SETTERS */

    public void setNumeroCuenta(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            throw new IllegalArgumentException("El número de cuenta no puede ser nulo ni estar vacío.");
        }

        this.numeroCuenta = numeroCuenta;
    }

    public void setNombreTitular(String nombreTitular) {
        if (nombreTitular == null || nombreTitular.isBlank()) {
            throw new IllegalArgumentException("El nombre del titular no puede ser nulo ni estar vacío.");
        }

        this.nombreTitular = nombreTitular;
    }

    /* METODO TOSTRING */

    @Override
    public String toString() {
        return String.format("Número de cuenta: %s | Nombre del titular: %s", numeroCuenta, nombreTitular);
    }

    /* OTROS METODOS */

    private boolean validarIBAN(String entidad, String oficina, String dc, String cuenta) {
        // Validar largo de entidad
        if (entidad.length() != 4) return false;

        // Validar largo de oficina
        if (oficina.length() != 4) return false;

        // Validar largo de distrito comercial
        if (dc.length() != 2) return false;

        // Validar que cuenta sea solamente numerico y de largo 10
        if (!cuenta.matches("^([0-9]+)$") || cuenta.length() != 10) return false;

        // Si todas las validaciones pasan, devolver 'true'
        return true;
    }

    private String pasarPaisNumerico(String pais) {
        StringBuilder numero = new StringBuilder();

        for (int i = 0; i < pais.length(); i++) {
            // A := '10'; Z := '35'
            numero.append(pais.charAt(i) - 'A' + 10);
        }

        return numero.toString();
    }

    public boolean compruebaIBAN(String iban) {
        // Comprobar que no sea nulo
        if (iban == null) return false;

        // Limpiar iban
        iban = iban.replaceAll("\\W+", "").toUpperCase().trim();

        // Comprobar si alguno de los caracteres no es alfanumerico y si supera el maximo de 34 caracteres
        if (!iban.matches("[A-Z0-9]+") || iban.length() > 34) return false;

        // Obtener pais y digito de control
        String pais = iban.substring(0, 2);
        String digitoControl = iban.substring(2, 4);

        // Si el digito de control es 00, todavia no es un IBAN valido
        if (digitoControl.equalsIgnoreCase("00")) return false;

        // Intercambiar pais a numeros
        String paisNumerico = pasarPaisNumerico(pais);

        // Construir iban
        iban = iban.substring(4) + paisNumerico + digitoControl;

        // Convertir string a BigInteger (es demasiado grande hasta para un long)
        BigInteger ibanNumero = new BigInteger(iban);

        // Comprobar si es valido o no (si da 1 es valido)
        return (ibanNumero).mod(BigInteger.valueOf(97)).intValue() == 1;
    }

    public String generaIBAN(String entidad, String oficina, String dc, String cuenta) {
        // En esta version no se pasa el pais por parametro, asi que se asume 'ES' de Espana
        // Pasar pais a numeros (A := '10'; Z := '35' -> E := '14'; S := '28'

        //String es = "14" + "28";
        String es = pasarPaisNumerico("ES");

        // Inicializar el digito de control a '00'
        String digitoControl = "00";

        // Validar datos del IBAN
        if (!validarIBAN(entidad, oficina, dc, cuenta)) return null;

        // Construir IBAN
        String iban = entidad + oficina + dc + cuenta + es + digitoControl;

        // Eliminar cualquier espacio y caracter no alfanumerico que se haya podido colar
        iban = iban.replaceAll("\\W+", "").toUpperCase().trim();

        // Comprobar si alguno de los caracteres no es numerico y si supera el maximo de 34 caracteres
        if (!iban.matches("^[0-9]+$") || iban.length() > 34) return null;

        // Generar digito de control con resto del numero / 97 (de ahi el modulo) y restar 98 al resto
        int resto = new BigInteger(iban).mod(BigInteger.valueOf(97)).intValue();
        // Dos cifras! si es 5 que sea 05
        digitoControl = String.format("%02d", 98 - resto);

        // Intercambiar '00' por el digito de control
        iban = iban.substring(0, iban.length() - 2) + digitoControl;

        // Reorganizar iban de nuevo
        iban = "ES" + digitoControl + iban.substring(0, iban.length() - 6);

        return iban;
    }
}
