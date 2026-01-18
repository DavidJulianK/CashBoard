package Cashboard.Backend;

import org.junit.jupiter.api.Test;

@CashboardSpringBootTest
class CashboardApplicationTests {

	/**
	 * Prüft, ob der Spring Application Context vollständig und fehlerfrei startet.
	 *
	 * Dieser Test stellt sicher, dass:
	 * - alle Beans korrekt konfiguriert sind
	 * - keine Abhängigkeitsfehler (z.B. fehlende Security- oder JPA-Beans) auftreten
	 * - das Test-Profil korrekt geladen wird
	 *
	 * @author DavidJulianK
	 */
	@Test
	void springKontextStartetOhneFehler() {
		// leer – der erfolgreiche Start des Contexts ist der Test
	}

}
