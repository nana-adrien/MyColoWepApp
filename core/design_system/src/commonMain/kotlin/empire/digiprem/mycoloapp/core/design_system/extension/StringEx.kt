package empire.digiprem.mycoloapp.core.design_system.extension

fun String.normalizeText(): String =
    this.lowercase()
        .map { char ->
            when (char) {
                'à', 'â', 'ä' -> 'a'
                'é', 'è', 'ê', 'ë' -> 'e'
                'î', 'ï' -> 'i'
                'ô', 'ö' -> 'o'
                'ù', 'û', 'ü' -> 'u'
                'ç' -> 'c'
                'ñ' -> 'n'
                else -> char
            }
        }
        .joinToString("")


 fun String.format(
    dayOfMonth: Int,
    monthNumber: Int,
    year: Int
): String {
    return this
        .replaceFirst("%02d", dayOfMonth.toString().padStart(2, '0'))
        .replaceFirst("%02d", monthNumber.toString().padStart(2, '0'))
        .replaceFirst("%04d", year.toString().padStart(4, '0'))
}