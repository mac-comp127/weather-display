package comp127.weather.api;

import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Generates conditions for use in tests. The generated conditions are pseudorandom, but will always
 * be the same given the same seed.
 */
public class WeatherDataFixtures {
    public static WeatherData generateWeatherData(int seed) {
        if(seed == 0) {
            return new WeatherData(null, CurrentConditions.BLANK, List.of());
        }
        return new WeatherData(
            generateCityName(seed),
            generateCurrentConditions(seed),
            IntStream.range(1, Math.abs(seed) * 17 % 39)
                .mapToObj((offset) ->
                    generateForecastConditions((offset + 1) * 3, seed + offset))
                .collect(toList()));
    }

    private static CurrentConditions generateCurrentConditions(int seed) {
        return new CurrentConditions(
            generateDouble(seed, "temperature", -60, 120),
            generateDouble(seed, "humidity", 0, 100),
            generateDouble(seed, "pressure", 26, 36),
            generateDouble(seed, "cloudCoverage", 0, 100),
            generateDouble(seed, "windSpeed", 0, 150),
            generateDouble(seed, "windDirectionInDegrees", 0, 360),
            generateWeatherIcon(seed),
            generateCondition(seed),
            generateTimeOfDay(seed, "sunrise", 4, 8),
            generateTimeOfDay(seed, "sunset", 16, 20));
    }

    private static ForecastConditions generateForecastConditions(int hoursFromNow, int seed) {
        LocalDateTime randomTimeOfDay = LocalDateTime.now().plusHours(hoursFromNow);
        Date predictionTime = toDate(randomTimeOfDay);

        double temperature = generateDouble(seed, "temperature", -40, 100);
        return new ForecastConditions(
            predictionTime,
            temperature,
            temperature - generateDouble(seed, "minTemperature", 1, 20),
            temperature + generateDouble(seed, "maxTemperature", 1, 20),
            generateDouble(seed, "humidity", 0, 100),
            generateDouble(seed, "pressure", 26, 36),
            generateDouble(seed, "cloudCoverage", 0, 100),
            generateDouble(seed, "windSpeed", 0, 150),
            generateDouble(seed, "windDirectionInDegrees", 0, 360),
            generateCondition(seed),
            generateWeatherIcon(seed));
    }

    // Helpers to generate stable pseudorandom values so the same seed always generates the same
    // test data

    private static long[] bunchOfPrimes = { 31, 127, 709, 1787, 5381, 8527, 15299, 19577, 27457, 42043, 52711, 72727, 87803, 96797, 112129, 137077, 167449, 173867, 219613, 239489, 250751, 285191, 318211, 352007, 401519, 443419, 464939, 490643, 506683, 527623, 648391, 683873, 718807, 755387, 839483, 864013, 919913, 985151, 1021271, 1080923, 1128889, 1159901, 1254739, 1278779, 1323503, 1342907, 1471343, 1656649, 1693031, 1715761, 1751411, 1793237, 1828669, 1950629, 1993039, 2071583, 2167937, 2193689, 2269733, 2332537, 2364361, 2487943, 2685911, 2750357, 2779781, 2810191, 3042161, 3129913, 3260657, 3284657, 3338989, 3403457, 3509299, 3643579, 3760921, 3829223, 3888551, 3965483, 4030889, 4142053, 4326473, 4348681, 4535189, 4578163, 4658099, 4748047, 4863959, 4989697, 5054303, 5138719, 5182717, 5363167, 5496349, 5587537, 5670851, 5741453, 5823667, 6037513, 6095731, 6415081, 6478961 };

    private static int generateInt(int seed, String property, int min, int maxInclusive) {
        // As seed varies, we want it to cover all the values in the given range. To cycle through
        // all those values before repeating, we want a seed multiplier that's relatively prime to
        // the size of the range — which property.hashCode() might not be. So we use the hash code
        // to grab a prime from an array of primes that itself has a prime length (so the hash codes
        // sample use all of it), then use _that_ prime as the multiplier for seed.
        long prime = bunchOfPrimes[Math.abs(property.hashCode()) % bunchOfPrimes.length];
        return (int) (Math.abs(seed * prime) % (maxInclusive - min + 1) + min);
    }

    private static double generateDouble(int seed, String property, double min, double maxInclusive) {
        return generateInt(seed, property, 0, 1_000_000) / 1_000_000.0 * (maxInclusive - min) + min;
    }
    
    private static <T> T generateFromOptions(int seed, String property, List<T> options) {
        return options.get(generateInt(seed, property, 0, options.size() - 1));
    }

    private static String generateCityName(int seed) {
        return generateFromOptions(seed, "cityName", List.of(
            "Hill Valley",
            "Emerald City",
            "Mos Eisley",
            "Toontown",
            "Sunnydale",
            "Neptune",
            "Springfield",
            "Hogsmeade",
            "Brigadoon",
            "Middlemarch",
            "Wortlethorpe"));
    }

    private static Date generateTimeOfDay(int seed, String property, int minHour, int maxHour) {
        // Hat tip to https://stackoverflow.com/a/6850919/239816
        LocalDate now = LocalDate.now();
        LocalDateTime todayMidnight = LocalDateTime.of(now, LocalTime.MIDNIGHT);
        LocalDateTime randomTimeOfDay = todayMidnight.plusMinutes(
            generateInt(seed, property, minHour * 60, maxHour * 60));
        return toDate(randomTimeOfDay);
    }

    // Surely there's a better way to do this? -PPC
    private static Date toDate(LocalDateTime localDateTime) {
        return Date.from(Instant.from(ZonedDateTime.of(localDateTime, ZoneId.systemDefault())));
    }

    private static String generateCondition(int seed) {
        return generateFromOptions(seed, "currentWeather", List.of(
            "Sunnish",
            "Partly clowny",
            "Blue Blue Electric Blue",
            "Rainbows and unicorns",
            "Clear with UFO sightings",
            "Punderstorm",
            "Heavy Thunderdome",
            "Sailors take warning",
            "Ineluctible drizzle",
            "Volcanic asps",
            "Apocalyptic cold",
            "Cloudy with a chance of meatballs"));
    }

    private static String generateWeatherIcon(int seed) {
        return generateFromOptions(seed, "weatherIcon",
            Stream.of("01", "02", "03", "04", "09", "10", "11", "13", "50")
                .flatMap((num) -> Stream.of(num + "d", num + "n"))
                .collect(toList()));
    }
}
