package empire.digiprem.mycolowepapp.core.design_system.extension

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