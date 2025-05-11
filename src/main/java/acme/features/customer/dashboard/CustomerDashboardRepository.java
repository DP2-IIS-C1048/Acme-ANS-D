package acme.features.customer.dashboard;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.TravelClass;

@Repository
public interface CustomerDashboardRepository extends AbstractRepository {

	// Últimos cinco destinos
	@Query("""
	    SELECT DISTINCT l.arrivalAirport.city
	    FROM Leg l
	    WHERE l.flight.id IN (
	        SELECT b.flight.id
	        FROM Booking b
	        WHERE b.customer.id = :customerId
	    )
	    ORDER BY l.scheduledArrival DESC
	""")
	List<String> findLastFiveDestinationsByCustomerId(int customerId);

	// Dinero gastado en reservas durante el último año
	@Query("""
		SELECT SUM(b.price.amount * c.rate)
		FROM Booking b
		JOIN CurrencyExchange c
		ON b.price.currency = c.fromCurrency
		WHERE b.customer.id = :customerId
		AND b.purchaseMoment >= CURRENT_DATE - 365
		AND c.toCurrency = 'EUR'
	""")
	Double findMoneySpentInLastYearByCustomerId(int customerId);

	// Número de reservas agrupadas por clase de viaje
	@Query("""
		    SELECT b.travelClass, COUNT(b)
		    FROM Booking b
		    WHERE b.customer.id = :customerId
		    GROUP BY b.travelClass
		""")
	Map<TravelClass, Long> findBookingsGroupedByTravelClassByCustomerId(int customerId);

	// Número total de reservas
	@Query("""
		    SELECT COUNT(b)
		    FROM Booking b
		    WHERE b.customer.id = :customerId
		    AND b.purchaseMoment >= CURRENT_DATE - 1825
		""")
	Long findBookingCountInLastFiveYearsByCustomerId(int customerId);

	// Promedio del costo de reservas
	@Query("""
		    SELECT AVG(b.price.amount)
		    FROM Booking b
			JOIN CurrencyExchange c
			ON b.price.currency = c.fromCurrency
			WHERE b.customer.id = :customerId
			AND b.purchaseMoment >= CURRENT_DATE - 1825
			AND c.toCurrency = 'EUR'
		""")
	Double findBookingAverageCostInLastFiveYearsByCustomerId(int customerId);

	// Costo mínimo de reservas
	@Query("""
		    SELECT MIN(b.price.amount)
		    FROM Booking b
			JOIN CurrencyExchange c
			ON b.price.currency = c.fromCurrency
			WHERE b.customer.id = :customerId
			AND b.purchaseMoment >= CURRENT_DATE - 1825
			AND c.toCurrency = 'EUR'
		""")
	Double findBookingMinCostInLastFiveYearsByCustomerId(int customerId);

	// Costo máximo de reservas
	@Query("""
		    SELECT MAX(b.price.amount)
		    FROM Booking b
			JOIN CurrencyExchange c
			ON b.price.currency = c.fromCurrency
			WHERE b.customer.id = :customerId
			AND b.purchaseMoment >= CURRENT_DATE - 1825
			AND c.toCurrency = 'EUR'
		""")
	Double findBookingMaxCostInLastFiveYearsByCustomerId(int customerId);

	// Desviación estándar del costo de reservas
	@Query("""
		    SELECT STDDEV(b.price.amount)
		    FROM Booking b
			JOIN CurrencyExchange c
			ON b.price.currency = c.fromCurrency
			WHERE b.customer.id = :customerId
			AND b.purchaseMoment >= CURRENT_DATE - 1825
			AND c.toCurrency = 'EUR'
		""")
	Double findBookingCostStdDevInLastFiveYearsByCustomerId(int customerId);

	// Número total de reservas
	@Query("""
		    SELECT COUNT(br.booking)
		    FROM BookingRecord br
		    WHERE br.booking.customer.id = :customerId
		""")
	Long findPassengerBookingCountByCustomerId(int customerId);

	// Promedio del número de pasajeros por reserva
	@Query("""
		    SELECT AVG(COUNT(br.passenger))
		    FROM BookingRecord br
		    WHERE br.booking.customer.id = :customerId
		    GROUP BY br.booking.id
		""")
	Double findPassengerAverageByCustomerId(int customerId);

	// Número mínimo de pasajeros por reserva
	@Query("""
		    SELECT MIN(COUNT(br.passenger))
		    FROM BookingRecord br
		    WHERE br.booking.customer.id = :customerId
		    GROUP BY br.booking.id
		""")
	Long findPassengerMinByCustomerId(int customerId);

	// Número máximo de pasajeros por reserva
	@Query("""
		    SELECT MAX(COUNT(br.passenger))
		    FROM BookingRecord br
		    WHERE br.booking.customer.id = :customerId
		    GROUP BY br.booking.id
		""")
	Long findPassengerMaxByCustomerId(int customerId);

	// Desviación estándar del número de pasajeros por reserva
	@Query("""
		    SELECT STDDEV(COUNT(br.passenger))
		    FROM BookingRecord br
		    WHERE br.booking.customer.id = :customerId
		    GROUP BY br.booking.id
		""")
	Double findPassengerStdDevByCustomerId(int customerId);
}
