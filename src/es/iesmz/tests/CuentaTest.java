package es.iesmz.tests;

import model.Cuenta;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CuentaTest {
    static Cuenta cuenta;

    @BeforeAll
    @DisplayName("Crear clase DiasLluvia antes de los test.")
    public static void setUpBeforeClass() throws Exception {
        cuenta = new Cuenta("234567891", "Iris");
    }

    @BeforeEach
    public void setUp() throws Exception {
        // TODO
    }

    @Test
    @DisplayName("Método que prueba la comprobación de un IBAN dado. Debe ser correcto.")
    public void compruebaIBANCorrecto() {
        List<String> lista = List.of(
                "ES6621000418401234567891",
                "ES6000491500051234567892",
                "ES9420805801101234567891"
        );

        for (String iban: lista) {
            assertTrue(cuenta.compruebaIBAN(iban));
        }
    }

    @Test
    @DisplayName("Método que prueba la comprobación de un IBAN dado. Debe ser incorrecto.")
    public void compruebaIBANIncorrecto() {
        List<String> lista = List.of(
                "ES7600246912501234567891",
                "ES4721000418401234567891",
                "ES8200491500051234567892"
        );

        for (String iban: lista) {
            assertFalse(cuenta.compruebaIBAN(iban));
        }
    }

    @Test
    @DisplayName("Método que prueba la generación un IBAN correcto.")
    public void generaIBANCorrecto() {
        List<String[]> entradas = List.of(
                new String[]{"0030", "2053", "09", "1234567895"},
                new String[]{"0049", "2352", "08", "2414205416"},
                new String[]{"2085", "2066", "62", "3456789011"}
        );

        List<String> salidas = List.of(
                "ES7100302053091234567895",
                "ES1000492352082414205416",
                "ES1720852066623456789011"
        );

        for (int i = 0; i < entradas.size(); i++) {
            for (int j = 0; j < entradas.get(i).length; j++) {
                // Separar elementos
                String entidad = entradas.get(i)[0];
                String oficina = entradas.get(i)[1];
                String dc = entradas.get(i)[2];
                String numCuenta = entradas.get(i)[3];

                // Juntar elementos y llamar a generaIBAN
                String iban = cuenta.generaIBAN(entidad, oficina, dc, numCuenta);

                // Comprobar si devuelve lo esperado en 'salidas'
                assertEquals(salidas.get(i), iban);
            }
        }
    }

    @Test
    @DisplayName("Método que prueba la generación un IBAN incorrecto.")
    public void generaIBANIncorrecto() {
        // TODO: El primero de todos no devuelve nulo!!
        List<String[]> entradas = List.of(
                new String[]{"2085", "2066", "62", "3456AE9011"},
                new String[]{"208", "2066", "62", "3456789011"},
                new String[]{"2080", "20A6", "62", "3456789011"},
                new String[]{"2080", "2086", "6", "3456789011"},
                new String[]{"2080", "2086", "63", "345678911"}
        );

        for (String[] entrada : entradas) {
            for (int j = 0; j < entrada.length; j++) {
                // Separar elementos
                String entidad = entrada[0];
                String oficina = entrada[1];
                String dc = entrada[2];
                String numCuenta = entrada[3];

                // Juntar elementos y llamar a generaIBAN
                String iban = cuenta.generaIBAN(entidad, oficina, dc, numCuenta);

                // Comprobar si devuelve lo esperado en 'salidas'
                assertNull(iban);
            }
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        // TODO
    }

    @AfterAll
    public static void tearDownAfterClas() throws Exception {
        // TODO
    }
}
